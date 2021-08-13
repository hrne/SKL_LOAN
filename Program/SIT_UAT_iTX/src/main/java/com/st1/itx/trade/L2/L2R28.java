package com.st1.itx.trade.L2;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.st1.itx.db.domain.ClMain;
import com.st1.itx.db.domain.ClMainId;
import com.st1.itx.db.service.ClLandOwnerService;
import com.st1.itx.db.service.ClLandReasonService;
import com.st1.itx.db.service.ClLandService;
import com.st1.itx.db.service.ClMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L2R28")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2R28 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L2R28.class);

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

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2R28 ");
		this.totaVo.init(titaVo);

		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 500;

		// tita
		int iClCode1 = parse.stringToInteger(titaVo.getParam("RimClCode1"));
		int iClCode2 = parse.stringToInteger(titaVo.getParam("RimClCode2"));
		int iClNo = parse.stringToInteger(titaVo.getParam("RimClNo"));
		int iLandSeq = parse.stringToInteger(titaVo.getParam("RimLandSeq"));
		int iFunCd = parse.stringToInteger(titaVo.getParam("RimFunCd"));

		// wk
		int wkTransferedYear = 0;
		// new table ClMain
		ClMain tClMain = new ClMain();
		ClLand tClLand = new ClLand();

		int dataSize = 0;
		// new list
		List<ClLandOwner> lClLandOwner = new ArrayList<ClLandOwner>();
		List<ClLandReason> lClLandReason = new ArrayList<ClLandReason>();
		// new pk
		ClLandId ClLandId = new ClLandId();
		ClMainId ClMainId = new ClMainId();
		// new 年月
		String year = "";
		String month = "";
		// 塞pk
		ClLandId.setClCode1(iClCode1);
		ClLandId.setClCode2(iClCode2);
		ClLandId.setClNo(iClNo);
		ClLandId.setLandSeq(iLandSeq);

		// 塞pk
		ClMainId.setClCode1(iClCode1);
		ClMainId.setClCode2(iClCode2);
		ClMainId.setClNo(iClNo);

		tClMain = sClMainService.findById(ClMainId, titaVo);
		tClLand = sClLandService.findById(ClLandId, titaVo);

		Slice<ClLandOwner> slClLandOwner = sClLandOwnerService.LandSeqEq(iClCode1, iClCode2, iClNo, iLandSeq, 0,
				Integer.MAX_VALUE, titaVo);
		lClLandOwner = slClLandOwner == null ? null : new ArrayList<ClLandOwner>(slClLandOwner.getContent());
		Slice<ClLandReason> slClLandReason = sClLandReasonService.clNoEq(iClCode1, iClCode2, iClNo, 0,
				Integer.MAX_VALUE, titaVo);
		lClLandReason = slClLandReason == null ? null : new ArrayList<ClLandReason>(slClLandReason.getContent());

		this.totaVo.putParam("L2r28LandSeq", iLandSeq);
		// 不存在擔保品主檔 拋錯
		if (tClMain == null) {

			switch (iFunCd) {
			case 1:
				// 若為新增且資料不存在主檔
				throw new LogicException("E2003", "(擔保品主檔)");
			case 2:
				// 若為修改，但資料不存在，拋錯
				throw new LogicException("E0003", "L2R28(ClLand)");
			case 4:
				// 若為刪除，但資料不存在，拋錯
				throw new LogicException("E0004", "L2R28(ClLand)");
			default:
				// funch不在以上範圍，拋錯
				throw new LogicException("E0010", "L2R28(ClLand)");
			}
		} else {

			if (tClLand == null) {

				// 該擔保品存在擔保品主檔且不動產土地檔無資料

				switch (iFunCd) {
				case 1:

					ClLand tLastClLand = sClLandService.findClNoFirst(iClCode1, iClCode2, iClNo, titaVo);
//remark by eric 2021.4.22 新增改由L2416.java編號
//					if (tLastClLand == null) {
//						this.totaVo.putParam("L2r28LandSeq", 1);
//					} else {
//						this.totaVo.putParam("L2r28LandSeq", tLastClLand.getLandSeq() + 1);
//					}
					this.totaVo.putParam("L2r28LandSeq", 0);

					// 若為新增，傳tota給零 空白

					tClLand = new ClLand();
					lClLandOwner = new ArrayList<ClLandOwner>();
					lClLandReason = new ArrayList<ClLandReason>();

					break;
				case 2:
					// 若為修改，但資料不存在，拋錯
					throw new LogicException("E0003", "L2R28(ClLand)");
				case 4:
					// 若為刪除，但資料不存在，拋錯
					throw new LogicException("E0004", "L2R28(ClLand)");
				default:
					// funch不在以上範圍，拋錯
					throw new LogicException("E0010", "L2R28(ClLand)");
				}

			} else {
				if (iFunCd == 1) {
					// 新增土地
					if (iClCode1 == 2) {

						this.totaVo.putParam("L2r28LandSeq", 0);
						lClLandReason = new ArrayList<ClLandReason>();
					} else {
						// 新增房地，但資料已存在，拋錯
						throw new LogicException("E0002", "L2R26(ClOther)");
					}
				}
			}

			this.totaVo.putParam("L2r28ClCode1", tClMain.getClCode1());
			this.totaVo.putParam("L2r28ClCode2", tClMain.getClCode2());
			this.totaVo.putParam("L2r28ClNo", tClMain.getClNo());
			this.totaVo.putParam("L2r28ClTypeCode", tClMain.getClTypeCode());
			this.totaVo.putParam("L2r28CityCode", tClLand.getCityCode());
			this.totaVo.putParam("L2r28AreaCode", tClLand.getAreaCode());
			this.totaVo.putParam("L2r28IrCode", tClLand.getIrCode());
			this.totaVo.putParam("L2r28LandNo1", tClLand.getLandNo1());
			this.totaVo.putParam("L2r28LandNo2", tClLand.getLandNo2());
			this.totaVo.putParam("L2r28LandLocation", tClLand.getLandLocation());
			this.totaVo.putParam("L2r28LandCode", tClLand.getLandCode());
			this.totaVo.putParam("L2r28Area", tClLand.getArea());
			this.totaVo.putParam("L2r28LandZoningCode", tClLand.getLandZoningCode());
			this.totaVo.putParam("L2r28LandUsageType", tClLand.getLandUsageType());
			this.totaVo.putParam("L2r28PostedLandValue", tClLand.getPostedLandValue());
			int postedLandValueYear = 0;
			int postedLandValueMonth = 0;
			int postedLandValueYearMonth = tClLand.getPostedLandValueYearMonth();
			this.info("postedLandValueYearMonth = " + postedLandValueYearMonth);
			if (tClLand.getPostedLandValueYearMonth() != 0) {
				postedLandValueYear = (postedLandValueYearMonth - 191100) / 100;

				postedLandValueMonth = postedLandValueYearMonth - ((postedLandValueYear + 1911) * 100);

				this.info("postedLandValueYear = " + postedLandValueYear);
				this.info("postedLandValueMonth = " + postedLandValueMonth);
			}
			if (tClLand.getTransferedYear() > 1911) {
				wkTransferedYear = tClLand.getTransferedYear() - 1911;
			}

			this.info("postedLandValueMonth = " + postedLandValueMonth);
			this.totaVo.putParam("L2r28PostedLandValueYear", postedLandValueYear);
			this.totaVo.putParam("L2r28PostedLandValueMonth", postedLandValueMonth);
			this.totaVo.putParam("L2r28TransferedYear", wkTransferedYear);
			this.totaVo.putParam("L2r28LastTransferedAmt", tClLand.getLastTransferedAmt());
			this.totaVo.putParam("L2r28EvaUnitPrice", tClLand.getEvaUnitPrice());
			this.totaVo.putParam("L2r28LandUsageCode", tClLand.getLandUsageCode());
			this.totaVo.putParam("L2r28LandRentStartDate", tClLand.getLandRentStartDate());
			this.totaVo.putParam("L2r28LandRentEndDate", tClLand.getLandRentEndDate());
//			this.totaVo.putParam("L2r28LandOwnedArea", tClLand.getLandOwnedArea()); TODO DB改動

			// 資料筆數
			if (lClLandOwner != null) {

				dataSize = lClLandOwner.size();
			} else {
				lClLandOwner = new ArrayList<ClLandOwner>();
			}
			this.info("L1R05 listCustTelNo size in DB = " + dataSize);

			// 暫時只抓前10筆,把第11筆之後的刪除
			if (dataSize > 10) {
				for (int i = dataSize + 1; i <= dataSize; i++) {
					lClLandOwner.remove(i);
				}
			} else if (dataSize < 10) {
				// 若不足10筆,補足10筆
				for (int i = dataSize + 1; i <= 10; i++) {
					ClLandOwner tClLandOwner = new ClLandOwner();
					lClLandOwner.add(tClLandOwner);
				}
			}

			this.info("lClLandOwner  = " + lClLandOwner);
			int i = 1;
			for (ClLandOwner tClLandOwner : lClLandOwner) {

				if (tClLandOwner == null) {

					tClLandOwner = new ClLandOwner();

				}
				this.info("tClLandOwner2 L2r28 " + tClLandOwner);
				this.totaVo.putParam("L2r28OwnerId" + i, tClLandOwner.getOwnerId());
				this.totaVo.putParam("L2r28OwnerName" + i, tClLandOwner.getOwnerName());
				this.totaVo.putParam("L2r28OwnerRelCode" + i, tClLandOwner.getOwnerRelCode());
				this.totaVo.putParam("L2r28OwnerPart" + i, tClLandOwner.getOwnerPart());
				this.totaVo.putParam("L2r28OwnerTotal" + i, tClLandOwner.getOwnerTotal());
				i++;

			}
			if (lClLandReason == null) {
				lClLandReason = new ArrayList<ClLandReason>();
			}
			// 資料筆數
			int dataSize2 = lClLandReason.size();
			this.info("L1R05 lClLandReason size in DB = " + dataSize2);

			// 暫時只抓前10筆,把第11筆之後的刪除
			if (dataSize2 > 10) {
				for (int j = dataSize2 + 1; j <= dataSize2; j++) {
					lClLandReason.remove(j);
				}
			} else if (dataSize2 <= 10) {
				// 若不足10筆,補足10筆
				for (int j = dataSize2 + 1; j <= 10; j++) {
					ClLandReason tClLandReason = new ClLandReason();
					lClLandReason.add(tClLandReason);
				}
			}

			int j = 1;
			for (ClLandReason tClLandReason : lClLandReason) {
				this.info("lClLandReasonL2416 " + lClLandReason);
				this.info("tClLandReasonL2416 " + tClLandReason);
				String CreateDate4 = " ";
				int reason = tClLandReason.getReason();
				// 判斷是否有資料 無資料new table給tota
				if (reason == 0) {

					tClLandReason = new ClLandReason();

				} else {
					String CreateDate = tClLandReason.getCreateDate().toString();
					String CreateDate2 = CreateDate.substring(0, 4) + CreateDate.substring(5, 7)
							+ CreateDate.substring(8, 10);
					int CreateDate3 = parse.stringToInteger(CreateDate2) - 19110000;
					CreateDate4 = String.valueOf(CreateDate3);
				}

				this.totaVo.putParam("L2r28Reason" + j, tClLandReason.getReason());
				this.totaVo.putParam("L2r28OtherReason" + j, tClLandReason.getOtherReason());
				this.totaVo.putParam("L2r28CreateEmpNo" + j, tClLandReason.getCreateEmpNo());
				this.info("DATE " + tClLandReason.getCreateDate());
				this.totaVo.putParam("L2r28CreateDate" + j, CreateDate4);
				this.info("DATE2 " + CreateDate4);
				j++;
			}

		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}