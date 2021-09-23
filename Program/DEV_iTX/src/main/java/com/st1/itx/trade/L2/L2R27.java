package com.st1.itx.trade.L2;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.ClBuilding;
import com.st1.itx.db.domain.ClBuildingId;
import com.st1.itx.db.domain.ClBuildingOwner;
import com.st1.itx.db.domain.ClBuildingParking;
import com.st1.itx.db.domain.ClBuildingPublic;
import com.st1.itx.db.domain.ClBuildingReason;
import com.st1.itx.db.domain.ClParking;
import com.st1.itx.db.domain.ClParkingType;
import com.st1.itx.db.domain.ClMain;
import com.st1.itx.db.domain.ClMainId;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.ClBuildingOwnerService;
import com.st1.itx.db.service.ClBuildingParkingService;
import com.st1.itx.db.service.ClBuildingPublicService;
import com.st1.itx.db.service.ClBuildingReasonService;
import com.st1.itx.db.service.ClBuildingService;
import com.st1.itx.db.service.ClParkingService;
import com.st1.itx.db.service.ClParkingTypeService;
import com.st1.itx.db.service.ClMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L2R27")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2R27 extends TradeBuffer {

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
	public ClBuildingParkingService sClBuildingParkingService;

	/* DB服務注入 */
	@Autowired
	public ClBuildingOwnerService sClBuildingOwnerService;

	/* DB服務注入 */
	@Autowired
	public ClBuildingReasonService sClBuildingReasonService;

	/* DB服務注入 */
	@Autowired
	public ClParkingService sClParkingService;

	/* DB服務注入 */
	@Autowired
	public ClParkingTypeService sClParkingTypeService;
	
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
		this.info("active L2R27 ");
		this.totaVo.init(titaVo);

		// tita
		int iClCode1 = parse.stringToInteger(titaVo.getParam("RimClCode1"));
		int iClCode2 = parse.stringToInteger(titaVo.getParam("RimClCode2"));
		int iClNo = parse.stringToInteger(titaVo.getParam("RimClNo"));
		int iFunCd = parse.stringToInteger(titaVo.getParam("RimFunCd"));

		// new table ClMain
		ClMain tClMain = new ClMain();
		ClBuilding tClBuilding = new ClBuilding();
		// new list
		List<ClBuildingPublic> lClBuildingPublic = new ArrayList<ClBuildingPublic>();
		List<ClBuildingParking> lClBuildingParking = new ArrayList<ClBuildingParking>();
		List<ClParking> lClParking = new ArrayList<ClParking>();
		List<ClParkingType> lClParkingType = new ArrayList<ClParkingType>();
		List<ClBuildingOwner> lClBuildingOwner = new ArrayList<ClBuildingOwner>();
		List<ClBuildingReason> lClBuildingReason = new ArrayList<ClBuildingReason>();
		// new pk
		ClMainId ClMainId = new ClMainId();
		ClBuildingId ClBuildingId = new ClBuildingId();
		// 塞pk
		ClMainId.setClCode1(iClCode1);
		ClMainId.setClCode2(iClCode2);
		ClMainId.setClNo(iClNo);
		// 塞pk
		ClBuildingId.setClCode1(iClCode1);
		ClBuildingId.setClCode2(iClCode2);
		ClBuildingId.setClNo(iClNo);

		tClMain = sClMainService.findById(ClMainId, titaVo);
		tClBuilding = sClBuildingService.findById(ClBuildingId, titaVo);
		Slice<ClBuildingPublic> slClBuildingPublic = sClBuildingPublicService.clNoEq(iClCode1, iClCode2, iClNo, 0, Integer.MAX_VALUE, titaVo);
		lClBuildingPublic = slClBuildingPublic == null ? null : new ArrayList<ClBuildingPublic>(slClBuildingPublic.getContent());
		Slice<ClBuildingParking> slClBuildingParking = sClBuildingParkingService.clNoEq(iClCode1, iClCode2, iClNo, 0, Integer.MAX_VALUE, titaVo);
		lClBuildingParking = slClBuildingParking == null ? null : new ArrayList<ClBuildingParking>(slClBuildingParking.getContent());

		Slice<ClParking> slClParking = sClParkingService.clNoEq(iClCode1, iClCode2, iClNo, 0, Integer.MAX_VALUE, titaVo);
		lClParking = slClParking == null ? null : new ArrayList<ClParking>(slClParking.getContent());

		Slice<ClParkingType> slClParkingType = sClParkingTypeService.clNoEq(iClCode1, iClCode2, iClNo, 0, Integer.MAX_VALUE, titaVo);
		lClParkingType = slClParkingType == null ? null : new ArrayList<ClParkingType>(slClParkingType.getContent());
		
		Slice<ClBuildingOwner> slClBuildingOwner = sClBuildingOwnerService.clNoEq(iClCode1, iClCode2, iClNo, 0, Integer.MAX_VALUE, titaVo);
		lClBuildingOwner = slClBuildingOwner == null ? null : new ArrayList<ClBuildingOwner>(slClBuildingOwner.getContent());
		Slice<ClBuildingReason> slClBuildingReason = sClBuildingReasonService.clNoEq(iClCode1, iClCode2, iClNo, 0, Integer.MAX_VALUE, titaVo);
		lClBuildingReason = slClBuildingReason == null ? null : new ArrayList<ClBuildingReason>(slClBuildingReason.getContent());
		this.info("tClMain l2r27" + tClMain);
		// 不存在擔保品主檔 拋錯
		if (tClMain == null) {

			switch (iFunCd) {
			case 1:
				// 若為新增且資料不存在主檔
				throw new LogicException("E2003", "(擔保品主檔)");
			case 2:
				// 若為修改，但資料不存在，拋錯
				throw new LogicException("E0003", "(擔保品主檔)");
			case 4:
				// 若為刪除，但資料不存在，拋錯
				throw new LogicException("E0004", "(擔保品主檔)");
			default:
				// funch不在以上範圍，拋錯
				throw new LogicException("E0010", "(擔保品主檔)");
			}
		} else {

			if (tClBuilding == null) {

				// 該擔保品存在擔保品主檔且不動產建物檔無資料

				this.info("iFunCd L2R27 " + iFunCd);
				switch (iFunCd) {

				case 1:
					// 若為新增，傳tota給零 空白
					tClBuilding = new ClBuilding();
					lClBuildingPublic = new ArrayList<ClBuildingPublic>();
					lClBuildingParking = new ArrayList<ClBuildingParking>();
					lClBuildingOwner = new ArrayList<ClBuildingOwner>();
					lClBuildingReason = new ArrayList<ClBuildingReason>();
					break;
				case 2:
					tClBuilding = new ClBuilding();
					if (lClBuildingPublic == null) {
						lClBuildingPublic = new ArrayList<ClBuildingPublic>();
					}
					if (lClBuildingParking == null) {
						lClBuildingParking = new ArrayList<ClBuildingParking>();
					}
					if (lClBuildingOwner == null) {
						lClBuildingOwner = new ArrayList<ClBuildingOwner>();
					}
					if (lClBuildingReason == null) {
						lClBuildingReason = new ArrayList<ClBuildingReason>();
					}
					break;

				case 4:
					// 若為刪除，但資料不存在，拋錯
					throw new LogicException("E0004", "L2R27(ClBuilding)");

				default:
					// funch不在以上範圍，拋錯
					throw new LogicException("E0010", "L2R27(ClBuilding)");

				}

			} else {
				if (iFunCd == 1) {
					// 若為新增，但資料已存在，拋錯
					throw new LogicException("E0002", "L2R27(ClBuilding)");
				}
			}

			this.info("tClMain L2R27 " + tClMain);
			this.info("tClBuilding L2R27 " + tClBuilding);
			this.totaVo.putParam("L2r27ClCode1", tClMain.getClCode1());
			this.totaVo.putParam("L2r27ClCode2", tClMain.getClCode1());
			this.totaVo.putParam("L2r27ClNo", tClMain.getClCode1());
			this.totaVo.putParam("L2r27ClTypeCode", tClMain.getClTypeCode());
			this.totaVo.putParam("L2r27CityCode", tClBuilding.getCityCode());
			this.totaVo.putParam("L2r27AreaCode", tClBuilding.getAreaCode());
			this.totaVo.putParam("L2r28IrCode", tClBuilding.getIrCode());
			this.totaVo.putParam("L2r27IrCode", tClBuilding.getIrCode());
			this.totaVo.putParam("L2r27Road", tClBuilding.getRoad());
			this.totaVo.putParam("L2r27Section", tClBuilding.getSection());
			this.totaVo.putParam("L2r27Alley", tClBuilding.getAlley());
			this.totaVo.putParam("L2r27Lane", tClBuilding.getLane());
			this.totaVo.putParam("L2r27Num", tClBuilding.getNum());
			this.totaVo.putParam("L2r27NumDash", tClBuilding.getNumDash());
			this.totaVo.putParam("L2r27Floor", tClBuilding.getFloor());
			this.totaVo.putParam("L2r27FloorDash", tClBuilding.getFloorDash());
			this.totaVo.putParam("L2r27BdNo1", tClBuilding.getBdNo1());
			this.totaVo.putParam("L2r27BdNo2", tClBuilding.getBdNo2());
			this.totaVo.putParam("L2r27BdLocation", tClBuilding.getBdLocation().trim());
			this.totaVo.putParam("L2r27BdMainUseCode", tClBuilding.getBdMainUseCode());
			this.totaVo.putParam("L2r27BdMtrlCode", tClBuilding.getBdMtrlCode());
			this.totaVo.putParam("L2r27BdTypeCode", tClBuilding.getBdTypeCode());
			this.totaVo.putParam("L2r27TotalFloor", tClBuilding.getTotalFloor());
			this.totaVo.putParam("L2r27FloorNo", tClBuilding.getFloorNo());
			this.totaVo.putParam("L2r27FloorArea", tClBuilding.getFloorArea());
			this.totaVo.putParam("L2r27EvaUnitPrice", tClBuilding.getEvaUnitPrice());
			this.totaVo.putParam("L2r27RoofStructureCode", tClBuilding.getRoofStructureCode());
			this.totaVo.putParam("L2r27BdDate", tClBuilding.getBdDate());
			this.totaVo.putParam("L2r27BdSubUsageCode", tClBuilding.getBdSubUsageCode());
			this.totaVo.putParam("L2r27BdSubArea", tClBuilding.getBdSubArea());
			this.totaVo.putParam("L2r27SellerId", tClBuilding.getSellerId());
			this.totaVo.putParam("L2r27SellerName", tClBuilding.getSellerName());
			this.totaVo.putParam("L2r27ContractPrice", tClBuilding.getContractPrice());
			this.totaVo.putParam("L2r27ContractDate", tClBuilding.getContractDate());
			this.totaVo.putParam("L2r27BdUsageCode", tClBuilding.getBdUsageCode());
			this.totaVo.putParam("L2r27ParkingTypeCode", tClBuilding.getParkingTypeCode());
			this.totaVo.putParam("L2r27ParkingProperty", tClBuilding.getParkingProperty());
			this.totaVo.putParam("L2r27HouseTaxNo", tClBuilding.getHouseTaxNo());
			this.totaVo.putParam("L2r27HouseBuyDate", tClBuilding.getHouseBuyDate());
			this.totaVo.putParam("L2r27SellerName", tClBuilding.getSellerName());
			this.totaVo.putParam("L2r27SellerName", tClBuilding.getSellerName());

			// 公設建號
			// 資料筆數
			if (lClBuildingPublic == null) {
				lClBuildingPublic = new ArrayList<ClBuildingPublic>();
			}
			int dataSize = lClBuildingPublic.size();
			this.info("L1R05 listCustTelNo size in DB = " + dataSize);

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
				this.info("tClBuildingPublic L2r27 " + tClBuildingPublic);

				this.totaVo.putParam("L2r27PublicBdNoA" + i, tClBuildingPublic.getPublicBdNo1() == 0 ? "" : tClBuildingPublic.getPublicBdNo1());
				this.totaVo.putParam("L2r27PublicBdNoB" + i, tClBuildingPublic.getPublicBdNo2());
				this.totaVo.putParam("L2r27Area" + i, tClBuildingPublic.getArea());
				this.totaVo.putParam("L2r27PublicBdOwnerId" + i, tClBuildingPublic.getOwnerId());
				this.totaVo.putParam("L2r27PublicBdOwnerName" + i, tClBuildingPublic.getOwnerName());
				i++;

			}
			// 獨立產權車位
			if (lClBuildingParking == null) {
				lClBuildingParking = new ArrayList<ClBuildingParking>();
			}
			// 資料筆數
			int dataSize2 = lClBuildingParking.size();
			this.info("L2R27 lClBuildingParking size in DB = " + dataSize2);

			int j = 1;
			if (lClParking != null) {
				for (ClParking tClParking : lClParking) {
					this.info("tClParkingL2416 " + tClParking);
					if (j > 100) {
						break;
					}
					OccursList occursList = new OccursList();

					occursList.putParam("L2r27ParkingNo", tClParking.getParkingNo());
					occursList.putParam("L2r27ParkingQty", tClParking.getParkingQty());
					occursList.putParam("L2r27ParkingTypeCode", tClParking.getParkingTypeCode());
					occursList.putParam("L2r27ParkingOwnerPart", tClParking.getOwnerPart());
					occursList.putParam("L2r27ParkingOwnerTotal", tClParking.getOwnerTotal());
					occursList.putParam("L2r27ParkingBdNoA", tClParking.getBdNo1());
					occursList.putParam("L2r27ParkingBdNoB", tClParking.getBdNo2());
					occursList.putParam("L2r27ParkingLandNoA", tClParking.getLandNo1());
					occursList.putParam("L2r27ParkingLandNoB", tClParking.getLandNo2());
					occursList.putParam("L2r27ParkingArea", tClParking.getParkingArea());
					occursList.putParam("L2r27ParkingAmount", tClParking.getAmount());

					this.totaVo.addOccursList(occursList);
					j++;
				}
			}

		}
		// 建物所有權人
		if (lClBuildingOwner == null) {
			lClBuildingOwner = new ArrayList<ClBuildingOwner>();
		}
		// 資料筆數
		int dataSize3 = lClBuildingOwner.size();
		this.info("L2R27 lClBuildingOwner size in DB = " + dataSize3);

		// 暫時只抓前10筆,把第11筆之後的刪除
		if (dataSize3 > 10) {
			for (int j = dataSize3 + 1; j <= dataSize3; j++) {
				lClBuildingOwner.remove(j);
			}
		} else if (dataSize3 <= 10) {
			// 若不足10筆,補足10筆
			for (int j = dataSize3 + 1; j <= 10; j++) {
				ClBuildingOwner tClBuildingOwner = new ClBuildingOwner();
				lClBuildingOwner.add(tClBuildingOwner);
			}
		}

		int k = 1;
		for (ClBuildingOwner tClBuildingOwner : lClBuildingOwner) {
			this.info("tClBuildingOwnerL2416 " + tClBuildingOwner);

			// 判斷是否有資料 無資料new table給tota
			CustMain custMain = sCustMainService.findById(tClBuildingOwner.getOwnerCustUKey(), titaVo);
			if(custMain!= null) {
			  this.totaVo.putParam("L2r27OwnerId" + k, custMain.getCustId());
			  this.totaVo.putParam("L2r27OwnerName" + k, custMain.getCustName());
			  this.totaVo.putParam("L2r27OwnerRelCode" + k, tClBuildingOwner.getOwnerRelCode());
			  this.totaVo.putParam("L2r27OwnerPart" + k, tClBuildingOwner.getOwnerPart());
			  this.totaVo.putParam("L2r27OwnerTotal" + k, tClBuildingOwner.getOwnerTotal());
			} else {
			  this.totaVo.putParam("L2r27OwnerId" + k, "");
			  this.totaVo.putParam("L2r27OwnerName" + k, "");
			  this.totaVo.putParam("L2r27OwnerRelCode" + k, "");
			  this.totaVo.putParam("L2r27OwnerPart" + k, "");
			  this.totaVo.putParam("L2r27OwnerTotal" + k, "");
			}
			k++;
		}
		// 修改原因
		if (lClBuildingReason == null) {
			lClBuildingReason = new ArrayList<ClBuildingReason>();
		}
		// 資料筆數
		int dataSize4 = lClBuildingReason.size();
		this.info("L2R27 lClBuildingReason size in DB = " + dataSize4);

		// 暫時只抓前10筆,把第11筆之後的刪除
		if (dataSize4 > 10) {
			for (int j = dataSize4 + 1; j <= dataSize4; j++) {
				lClBuildingReason.remove(j);
			}
		} else if (dataSize4 <= 10) {
			// 若不足10筆,補足10筆
			for (int j = dataSize4 + 1; j <= 10; j++) {
				ClBuildingReason tClBuildingReason = new ClBuildingReason();
				lClBuildingReason.add(tClBuildingReason);
			}
		}

		int l = 1;
		for (ClBuildingReason tClBuildingReason : lClBuildingReason) {
			String CreateDate4 = " ";
			int reason = tClBuildingReason.getReason();
			// 判斷是否有資料 無資料new table給tota
			if (reason == 0) {

				tClBuildingReason = new ClBuildingReason();

			} else {
				String CreateDate = tClBuildingReason.getCreateDate().toString();
				String CreateDate2 = CreateDate.substring(0, 4) + CreateDate.substring(5, 7) + CreateDate.substring(8, 10);
				int CreateDate3 = parse.stringToInteger(CreateDate2) - 19110000;
				CreateDate4 = String.valueOf(CreateDate3);
			}

			this.info("tClBuildingReasonL2R27 " + tClBuildingReason);
			this.totaVo.putParam("L2r27Reason" + l, tClBuildingReason.getReason() == 0 ? "" : tClBuildingReason.getReason());
			this.totaVo.putParam("L2r27OtherReason" + l, tClBuildingReason.getOtherReason());
			this.totaVo.putParam("L2r27CreateEmpNo" + l, tClBuildingReason.getCreateEmpNo());
			this.totaVo.putParam("L2r27CreateDate" + l, CreateDate4);
			this.info("DATE2 " + CreateDate4);
			l++;
		}
		
		// 修改原因
		if (lClParkingType == null) {
			lClParkingType = new ArrayList<ClParkingType>();
			for(int i = 1 ; i <=5 ; i++) {
				this.totaVo.putParam("L2r27ParkingTypeCodeA" + i, "");
				this.totaVo.putParam("L2r27ParkingQtyA" + i, "");
				this.totaVo.putParam("L2r27ParkingAreaA" + i, "");
			}
		} else {
				
		  int m = 1;
		  for (ClParkingType tClParkingType : lClParkingType) {	
			this.totaVo.putParam("L2r27ParkingTypeCodeA" + m, tClParkingType.getParkingTypeCode());
			this.totaVo.putParam("L2r27ParkingQtyA" + m, tClParkingType.getParkingQty());
			this.totaVo.putParam("L2r27ParkingAreaA" + m, tClParkingType.getParkingArea());
			m++;
		  }
		}
		
		this.addList(this.totaVo);
		return this.sendList();
	}
}