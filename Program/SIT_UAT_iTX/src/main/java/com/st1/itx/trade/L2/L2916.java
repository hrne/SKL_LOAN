package com.st1.itx.trade.L2;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.ClLand;
import com.st1.itx.db.domain.ClLandId;
import com.st1.itx.db.domain.ClLandOwner;
import com.st1.itx.db.domain.ClLandReason;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.ClLandOwnerService;
import com.st1.itx.db.service.ClLandReasonService;
import com.st1.itx.db.service.ClLandService;
import com.st1.itx.db.service.ClMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * ClCode1=9,1<br>
 * ClCode2=9,2<br>
 * ClNo=9,7<br>
 */

@Service("L2916")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2916 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public ClMainService sClMainService;

	/* DB服務注入 */
	@Autowired
	public ClLandService sClLandService;

	/* DB服務注入 */
	@Autowired
	public ClLandOwnerService sClLandOwnerService;

	/* DB服務注入 */
	@Autowired
	public ClLandReasonService sClLandReasonService;

	@Autowired
	public CustMainService sCustMainService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2916 ");
		this.totaVo.init(titaVo);

		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 100; // 75 * 500 = 37500

		// tita
		int iClCode1 = parse.stringToInteger(titaVo.getParam("RimClCode1"));
		int iClCode2 = parse.stringToInteger(titaVo.getParam("RimClCode2"));
		int iClNo = parse.stringToInteger(titaVo.getParam("RimClNo"));
		int iLandSeq = parse.stringToInteger(titaVo.getParam("RimLandSeq"));
		// new ArrayList
		List<ClLandOwner> lClLandOwner = new ArrayList<ClLandOwner>();
		List<ClLandReason> lClLandReason = new ArrayList<ClLandReason>();
		// new TABLE
		ClLand tClLand = new ClLand();
		// new PK
		ClLandId ClLandId = new ClLandId();
		// 裝 ClBuilding PK
		ClLandId.setClCode1(iClCode1);
		ClLandId.setClCode2(iClCode2);
		ClLandId.setClNo(iClNo);
		ClLandId.setLandSeq(iLandSeq);
		int postedLandValueYear = 0;
		int postedLandValueMonth = 0;
		// tita擔保品編號取擔保品不動產建物檔資料
		tClLand = sClLandService.findById(ClLandId, titaVo);
		// 測試該擔保品編號是否存在擔保品不動產建物檔
		// 若不存在 拋錯
		if (tClLand == null) {
			throw new LogicException("E0001", "L2916該擔保品編號在擔保品不動產土地檔無資料");
		}
		// 該擔保品編號存在擔保品不動產建物檔
		// 塞tota
		this.totaVo.putParam("LandLocation", tClLand.getLandLocation());
		this.totaVo.putParam("LandCode", tClLand.getLandCode());
		this.totaVo.putParam("Area", tClLand.getArea());
		this.totaVo.putParam("LandZoningCode", tClLand.getLandZoningCode());
		this.totaVo.putParam("LandUsageType", tClLand.getLandUsageType());
		this.totaVo.putParam("PostedLandValue", tClLand.getPostedLandValue());
		if (tClLand.getPostedLandValueYearMonth() > 0) {
			int postedLandValueYearMonth = tClLand.getPostedLandValueYearMonth();

			postedLandValueYear = (postedLandValueYearMonth / 100) - 1911;
			this.info("postedLandValueYear =" + postedLandValueYear);

			postedLandValueMonth = postedLandValueYearMonth - ((postedLandValueYear + 1911) * 100);
			this.info("postedLandValueMonth =" + postedLandValueMonth);
		}
		int transferedYear = tClLand.getTransferedYear();
		if (tClLand.getTransferedYear() > 0) {
			transferedYear = tClLand.getTransferedYear() - 1911;
		}

		this.totaVo.putParam("PostedLandValueYear", postedLandValueYear);
		this.totaVo.putParam("PostedLandValueMonth", postedLandValueMonth);
		this.totaVo.putParam("TransferedYear", transferedYear);
		this.totaVo.putParam("LastTransferedAmt", tClLand.getLastTransferedAmt());
		this.totaVo.putParam("EvaUnitPrice", tClLand.getEvaUnitPrice());
		this.totaVo.putParam("LandUsageCode", tClLand.getLandUsageCode());
		this.totaVo.putParam("LandRentStartDate", tClLand.getLandRentStartDate());
		this.totaVo.putParam("LandRentEndDate", tClLand.getLandRentEndDate());
//		this.totaVo.putParam("LandOwnedArea", tClLand.getLandOwnedArea());

		// tita擔保品編號取建物所有權人檔資料list
		Slice<ClLandOwner> slClLandOwner = sClLandOwnerService.LandSeqEq(iClCode1, iClCode2, iClNo, iLandSeq, 0, Integer.MAX_VALUE, titaVo);
		lClLandOwner = slClLandOwner == null ? null : new ArrayList<ClLandOwner>(slClLandOwner.getContent());

		if (lClLandOwner == null || lClLandOwner.size() == 0) {
			lClLandOwner = new ArrayList<ClLandOwner>();
		}
		// 資料筆數
		int dataSize1 = lClLandOwner.size();
		this.info("L1R05 lClBuildingOwner size in DB = " + dataSize1);

		// 暫時只抓前10筆,把第11筆之後的刪除
		if (dataSize1 > 10) {
			for (int k = dataSize1 + 1; k <= dataSize1; k++) {
				lClLandOwner.remove(k);
			}
		} else if (dataSize1 <= 10) {
			// 若不足10筆,補足10筆
			for (int k = dataSize1 + 1; k <= 10; k++) {
				ClLandOwner tClLandOwner = new ClLandOwner();
				lClLandOwner.add(tClLandOwner);
			}
		}

		int k = 1;
		for (ClLandOwner tClLandOwner : lClLandOwner) {

			CustMain custMain = sCustMainService.findById(tClLandOwner.getOwnerCustUKey(), titaVo);
			if (custMain != null) {
				this.totaVo.putParam("OwnerId" + k, custMain.getCustId());
				this.totaVo.putParam("OwnerName" + k, custMain.getCustName());
			} else {
				this.totaVo.putParam("OwnerId" + k, "");
				this.totaVo.putParam("OwnerName" + k, "");
			}
			this.totaVo.putParam("OwnerRelCode" + k, tClLandOwner.getOwnerRelCode());
			this.totaVo.putParam("OwnerPart" + k, tClLandOwner.getOwnerPart());
			this.totaVo.putParam("OwnerTotal" + k, tClLandOwner.getOwnerTotal());

			k++;
		}

		// tita擔保品編號取建物修改原因檔資料list
		Slice<ClLandReason> slClLandReason = sClLandReasonService.clNoEq(iClCode1, iClCode2, iClNo, 0, Integer.MAX_VALUE, titaVo);
		lClLandReason = slClLandReason == null ? null : new ArrayList<ClLandReason>(slClLandReason.getContent());
		// 資料筆數
		if (lClLandReason == null || lClLandReason.size() == 0) {
			lClLandReason = new ArrayList<ClLandReason>();
		}
		int dataSize4 = lClLandReason.size();
		this.info("L1R05 lClBuildingReason size in DB = " + dataSize4);

		// 暫時只抓前10筆,把第11筆之後的刪除
		if (dataSize4 > 10) {
			for (int l = dataSize4 + 1; l <= dataSize4; l++) {
				lClLandReason.remove(l);
			}
		} else if (dataSize4 < 10) {
			// 若不足10筆,補足10筆
			for (int l = dataSize4 + 1; l <= 10; l++) {
				ClLandReason tClLandReason = new ClLandReason();
				lClLandReason.add(tClLandReason);
			}
		}
		int l = 1;
		for (ClLandReason tClLandReason : lClLandReason) {

			String CreateDate4 = "";
			if (tClLandReason.getClLandReasonId() == null) {

				tClLandReason = new ClLandReason();

			} else {
				this.info("tClLandReason.getCreateDate().toString()L2915 " + tClLandReason.getCreateDate().toString());
				String CreateDate = tClLandReason.getCreateDate().toString();
				this.info("CreateDate L2915 " + CreateDate);
				String CreateDate2 = CreateDate.substring(0, 4) + CreateDate.substring(5, 7) + CreateDate.substring(8, 10);
				int CreateDate3 = parse.stringToInteger(CreateDate2) - 19110000;
				CreateDate4 = String.valueOf(CreateDate3);
			}

			this.totaVo.putParam("Reason" + l, tClLandReason.getReason() == 0 ? "" : tClLandReason.getReason());
			this.totaVo.putParam("OtherReason" + l, tClLandReason.getOtherReason());
			this.totaVo.putParam("CreateEmpNo" + l, tClLandReason.getCreateEmpNo());
			this.totaVo.putParam("CreateDate" + l, CreateDate4);

			l++;

		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}