package com.st1.itx.trade.L2;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.ClBuilding;
import com.st1.itx.db.domain.ClBuildingId;
import com.st1.itx.db.domain.ClBuildingOwner;
//import com.st1.itx.db.domain.ClBuildingParking;
import com.st1.itx.db.domain.ClBuildingPublic;
import com.st1.itx.db.domain.ClBuildingReason;
import com.st1.itx.db.domain.ClMain;
import com.st1.itx.db.domain.ClMainId;
import com.st1.itx.db.domain.ClParking;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.service.ClBuildingOwnerService;
//import com.st1.itx.db.service.ClBuildingParkingService;
import com.st1.itx.db.service.ClBuildingPublicService;
import com.st1.itx.db.service.ClBuildingReasonService;
import com.st1.itx.db.service.ClBuildingService;
import com.st1.itx.db.service.ClMainService;
import com.st1.itx.db.service.ClParkingService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;


@Service("L2915")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2915 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public ClMainService sClMainService;

	/* DB服務注入 */
	@Autowired
	public ClBuildingService sClBuildingService;

	/* DB服務注入 */
	@Autowired
	public ClBuildingPublicService sClBuildingPublicService;

	/* DB服務注入 */
	@Autowired
	public ClParkingService sClParkingService;

	/* DB服務注入 */
	@Autowired
	public ClBuildingOwnerService sClBuildingOwnerService;

	/* DB服務注入 */
	@Autowired
	public ClBuildingReasonService sClBuildingReasonService;
	
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
		this.info("active L2915 ");
		this.totaVo.init(titaVo);

		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 100; // 75 * 500 = 37500

		int iClCode1 = parse.stringToInteger(titaVo.getParam("RimClCode1"));
		int iClCode2 = parse.stringToInteger(titaVo.getParam("RimClCode2"));
		int iClNo = parse.stringToInteger(titaVo.getParam("RimClNo"));
		// new ArrayList
		List<ClBuildingPublic> lClBuildingPublic = new ArrayList<ClBuildingPublic>();
		List<ClBuildingOwner> lClBuildingOwner = new ArrayList<ClBuildingOwner>();
		List<ClBuildingReason> lClBuildingReason = new ArrayList<ClBuildingReason>();
		int dataSize3 = 0;
		// new TABLE
		ClMain tClMain = new ClMain();
		ClBuilding tClBuilding = new ClBuilding();
		// new PK
		ClMainId ClMainId = new ClMainId();
		ClBuildingId ClBuildingId = new ClBuildingId();
		// 裝 ClBuilding PK
		ClBuildingId.setClCode1(iClCode1);
		ClBuildingId.setClCode2(iClCode2);
		ClBuildingId.setClNo(iClNo);
		// 裝 ClMain PK
		ClMainId.setClCode1(iClCode1);
		ClMainId.setClCode2(iClCode2);
		ClMainId.setClNo(iClNo);
		// TITA擔保品編號取擔保品不動產建物檔資料
		tClBuilding = sClBuildingService.findById(ClBuildingId, titaVo);
		tClMain = sClMainService.findById(ClMainId, titaVo);
		// 測試該擔保品編號是否存在擔保品不動產建物檔
		// 若不存在 拋錯
		if (tClBuilding == null) {
			throw new LogicException("E0001", "L2915該擔保品編號不存在擔保品不動產建物檔");
		}
		if (tClMain == null) {
			throw new LogicException("E0001", "L2915該擔保品編號不存在擔保品主檔");
		}
		// 該擔保品編號存在擔保品不動產建物檔

		this.totaVo.putParam("BdLocation", tClBuilding.getBdLocation() + "，建號" + tClBuilding.getBdNo1() + "-" + tClBuilding.getBdNo2());
		this.totaVo.putParam("BdMainUseCode", tClBuilding.getBdMainUseCode());
		this.totaVo.putParam("BdMtrlCode", tClBuilding.getBdMtrlCode());
		this.totaVo.putParam("BdTypeCode", tClBuilding.getBdTypeCode());
		this.totaVo.putParam("TotalFloor", tClBuilding.getTotalFloor());
		this.totaVo.putParam("FloorNo", tClBuilding.getFloorNo());
		this.totaVo.putParam("FloorArea", tClBuilding.getFloorArea());
		this.totaVo.putParam("EvaUnitPrice", tClBuilding.getEvaUnitPrice());
		this.totaVo.putParam("RoofStructureCode", tClBuilding.getRoofStructureCode());
		this.totaVo.putParam("BdDate", tClBuilding.getBdDate());
		this.totaVo.putParam("BdSubUsageCode", tClBuilding.getBdSubUsageCode());
		this.totaVo.putParam("BdSubArea", tClBuilding.getBdSubArea());
		this.totaVo.putParam("SellerId", tClBuilding.getSellerId());
		this.totaVo.putParam("SellerName", tClBuilding.getSellerName());
		this.totaVo.putParam("ContractPrice", tClBuilding.getContractPrice());
		this.totaVo.putParam("ContractDate", tClBuilding.getContractDate());
		this.totaVo.putParam("BdUsageCode", tClBuilding.getBdUsageCode());
		this.totaVo.putParam("ParkingTypeCode", tClBuilding.getParkingTypeCode());
		this.totaVo.putParam("ParkingProperty", tClBuilding.getParkingProperty());
		this.totaVo.putParam("HouseTaxNo", tClBuilding.getHouseTaxNo());
		this.totaVo.putParam("HouseBuyDate", tClBuilding.getHouseBuyDate());
		this.totaVo.putParam("ClStatus", tClMain.getClStatus());

		// TITA擔保品編號取建物公設建號檔資料list
		Slice<ClBuildingPublic> slClBuildingPublic = sClBuildingPublicService.clNoEq(iClCode1, iClCode2, iClNo, 0, Integer.MAX_VALUE, titaVo);
		lClBuildingPublic = slClBuildingPublic == null ? null : new ArrayList<ClBuildingPublic>(slClBuildingPublic.getContent());
		// 資料筆數
		if (lClBuildingPublic == null) {
			lClBuildingPublic = new ArrayList<ClBuildingPublic>();
		}
		int dataSize = lClBuildingPublic.size();
		this.info("L2915 lClBuildingPublic size in DB = " + dataSize);

		// 暫時只抓前10筆,把第11筆之後的刪除
		if (dataSize > 10) {
			for (int i = dataSize + 1; i <= dataSize; i++) {
				lClBuildingPublic.remove(i);
			}
		} else if (dataSize < 10) {
			// 若不足10筆,補足10筆
			for (int i = dataSize + 1; i <= 10; i++) {
				ClBuildingPublic tClBuildingPublic = new ClBuildingPublic();
				lClBuildingPublic.add(tClBuildingPublic);
			}
		}
		int i = 1;
		for (ClBuildingPublic tClBuildingPublic : lClBuildingPublic) {

			if (tClBuildingPublic == null) {

				tClBuildingPublic = new ClBuildingPublic();

			}
			this.info("tClBuildingPublic L2915 " + tClBuildingPublic);
			this.totaVo.putParam("PublicBdNoA" + i, tClBuildingPublic.getPublicBdNo1());
			this.totaVo.putParam("PublicBdNoB" + i, tClBuildingPublic.getPublicBdNo2());
			this.totaVo.putParam("PublicArea" + i, tClBuildingPublic.getArea());
			this.totaVo.putParam("PublicOwnerId" + i, tClBuildingPublic.getOwnerId());
			this.totaVo.putParam("PublicOwnerName" + i, tClBuildingPublic.getOwnerName());
			i++;

		}


		// TITA擔保品編號取建物所有權人檔資料list
		Slice<ClBuildingOwner> slClBuildingOwner = sClBuildingOwnerService.clNoEq(iClCode1, iClCode2, iClNo, 0, Integer.MAX_VALUE, titaVo);
		lClBuildingOwner = slClBuildingOwner == null ? null : new ArrayList<ClBuildingOwner>(slClBuildingOwner.getContent());
		if (lClBuildingOwner == null) {
			lClBuildingOwner = new ArrayList<ClBuildingOwner>();
			// 資料筆數
			dataSize3 = 0;
		} else {
			// 資料筆數
			dataSize3 = lClBuildingOwner.size();
		}

		this.info("L1R05 lClBuildingOwner size in DB = " + dataSize3);

		// 暫時只抓前10筆,把第11筆之後的刪除
		if (dataSize3 > 10) {
			for (int k = dataSize3 + 1; k <= dataSize3; k++) {
				lClBuildingOwner.remove(k);
			}
		} else if (dataSize3 <= 10) {
			// 若不足10筆,補足10筆
			for (int k = dataSize3 + 1; k <= 10; k++) {
				ClBuildingOwner tClBuildingOwner = new ClBuildingOwner();
				lClBuildingOwner.add(tClBuildingOwner);
			}
		}

		int k = 1;
		for (ClBuildingOwner tClBuildingOwner : lClBuildingOwner) {
			
			CustMain custMain = sCustMainService.findById(tClBuildingOwner.getOwnerCustUKey(), titaVo);
			
			if(custMain != null) {
			  this.totaVo.putParam("OwnerId" + k, custMain.getCustId());
			  this.totaVo.putParam("OwnerName" + k, custMain.getCustName());
			  this.totaVo.putParam("OwnerRelCode" + k, tClBuildingOwner.getOwnerRelCode());
			  this.totaVo.putParam("OwnerPart" + k, tClBuildingOwner.getOwnerPart());
			  this.totaVo.putParam("OwnerTotal" + k, tClBuildingOwner.getOwnerTotal());
			} else {
			  this.totaVo.putParam("OwnerId" + k, "");
		      this.totaVo.putParam("OwnerName" + k, "");
			  this.totaVo.putParam("OwnerRelCode" + k, "");
			  this.totaVo.putParam("OwnerPart" + k, "");
			  this.totaVo.putParam("OwnerTotal" + k, "");
			}
			k++;
		}
		// TITA擔保品編號取建物修改原因檔資料list
		Slice<ClBuildingReason> slClBuildingReason = sClBuildingReasonService.clNoEq(iClCode1, iClCode2, iClNo, 0, Integer.MAX_VALUE, titaVo);
		lClBuildingReason = slClBuildingReason == null ? null : new ArrayList<ClBuildingReason>(slClBuildingReason.getContent());
		// 資料筆數
		if (lClBuildingReason == null) {
			lClBuildingReason = new ArrayList<ClBuildingReason>();
		}
		int dataSize4 = lClBuildingReason.size();
		this.info("L1R05 lClBuildingReason size in DB = " + dataSize4);

		// 暫時只抓前10筆,把第11筆之後的刪除
		if (dataSize4 > 10) {
			for (int l = dataSize4 + 1; l <= dataSize4; l++) {
				lClBuildingReason.remove(l);
			}
		} else if (dataSize4 < 10) {
			// 若不足10筆,補足10筆
			for (int l = dataSize4 + 1; l <= 10; l++) {
				ClBuildingReason tClBuildingReason = new ClBuildingReason();
				lClBuildingReason.add(tClBuildingReason);
			}
		}
		int l = 1;
		for (ClBuildingReason tClBuildingReason : lClBuildingReason) {
			int createDate = 0;
			if (tClBuildingReason.getClBuildingReasonId() == null) {
				this.info("tClBuildingReason=null =" + tClBuildingReason);
				tClBuildingReason = new ClBuildingReason();

			} else {
				// 宣告
				Timestamp ts = tClBuildingReason.getCreateDate();
				this.info("ts = " + ts);

				DateFormat sdfdate = new SimpleDateFormat("yyyyMMdd");

				String sCreateDate = sdfdate.format(ts);
				createDate = parse.stringToInteger(sCreateDate) - 19110000;
				this.info("createDate = " + createDate);
			}

			this.totaVo.putParam("Reason" + l, tClBuildingReason.getReason());
			this.totaVo.putParam("OtherReason" + l, tClBuildingReason.getOtherReason());
			this.totaVo.putParam("CreateEmpNo" + l, tClBuildingReason.getCreateEmpNo());
			this.totaVo.putParam("CreateDate" + l, createDate);

			l++;

		}

		// 車位
		Slice<ClParking> slClParking = sClParkingService.clNoEq(iClCode1, iClCode2, iClNo, 0, Integer.MAX_VALUE, titaVo);
		List<ClParking> lClParking = slClParking == null ? null : new ArrayList<ClParking>(slClParking.getContent());

		int j = 1;
		if (lClParking != null) {
			for (ClParking tClParking : lClParking) {
				this.info("tClParkingL2416 " + tClParking);
				if (j > 100) {
					break;
				}

				this.totaVo.putParam("ParkingNo" + j, tClParking.getParkingNo());
				this.totaVo.putParam("ParkingQty" + j, tClParking.getParkingQty());
				this.totaVo.putParam("ParkingTypeCode" + j, tClParking.getParkingTypeCode());
				this.totaVo.putParam("ParkingOwnerPart" + j, tClParking.getOwnerPart());
				this.totaVo.putParam("ParkingOwnerTotal" + j, tClParking.getOwnerTotal());
				this.totaVo.putParam("ParkingBdNoA" + j, tClParking.getBdNo1());
				this.totaVo.putParam("ParkingBdNoB" + j, tClParking.getBdNo2());
				this.totaVo.putParam("ParkingLandNoA" + j, tClParking.getLandNo1());
				this.totaVo.putParam("ParkingLandNoB" + j, tClParking.getLandNo2());
				this.totaVo.putParam("ParkingArea" + j, tClParking.getParkingArea());
				this.totaVo.putParam("ParkingAmount" + j, tClParking.getAmount());

				j++;
			}
		}

		for (int m = j; m <= 100; m++) {
			this.totaVo.putParam("ParkingNo" + m, "");
			this.totaVo.putParam("ParkingQty" + m, "");
			this.totaVo.putParam("ParkingTypeCode" + m, "");
			this.totaVo.putParam("ParkingOwnerPart" + m, "");
			this.totaVo.putParam("ParkingOwnerTotal" + m, "");
			this.totaVo.putParam("ParkingBdNoA" + m, "");
			this.totaVo.putParam("ParkingBdNoB" + m, "");
			this.totaVo.putParam("ParkingLandNoA" + m, "");
			this.totaVo.putParam("ParkingLandNoB" + m, "");
			this.totaVo.putParam("ParkingArea" + m, 0);
			this.totaVo.putParam("ParkingAmount" + m, 0);
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}