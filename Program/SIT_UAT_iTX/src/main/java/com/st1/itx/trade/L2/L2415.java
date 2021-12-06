package com.st1.itx.trade.L2;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.ClBuilding;
import com.st1.itx.db.domain.ClBuildingId;
import com.st1.itx.db.domain.ClBuildingOwner;
import com.st1.itx.db.domain.ClBuildingOwnerId;
import com.st1.itx.db.domain.ClBuildingParking;
import com.st1.itx.db.domain.ClBuildingParkingId;
import com.st1.itx.db.domain.ClBuildingPublic;
import com.st1.itx.db.domain.ClBuildingPublicId;
import com.st1.itx.db.domain.ClBuildingReason;
import com.st1.itx.db.domain.ClBuildingReasonId;
import com.st1.itx.db.domain.ClMain;
import com.st1.itx.db.domain.ClMainId;
import com.st1.itx.db.domain.ClParking;
import com.st1.itx.db.domain.ClParkingId;
import com.st1.itx.db.domain.ClParkingType;
import com.st1.itx.db.domain.ClParkingTypeId;
import com.st1.itx.db.service.CdAreaService;
import com.st1.itx.db.service.CdCityService;
import com.st1.itx.db.service.ClBuildingOwnerService;
import com.st1.itx.db.service.ClBuildingParkingService;
import com.st1.itx.db.service.ClBuildingPublicService;
import com.st1.itx.db.service.ClBuildingReasonService;
import com.st1.itx.db.service.ClBuildingService;
import com.st1.itx.db.service.ClImmService;
import com.st1.itx.db.service.ClMainService;
import com.st1.itx.db.service.ClParkingService;
import com.st1.itx.db.service.ClParkingTypeService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L2415")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2415 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CdCityService sCdCityService;

	/* DB服務注入 */
	@Autowired
	public CdAreaService sCdAreaService;

	/* DB服務注入 */
	@Autowired
	public ClMainService sClMainService;

	/* DB服務注入 */
	@Autowired
	public ClImmService sClImmService;

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
	public ClParkingService sClParkingService;

	/* DB服務注入 */
	@Autowired
	public ClParkingTypeService sClParkingTypeService;

	/* DB服務注入 */
	@Autowired
	public ClBuildingOwnerService sClBuildingOwnerService;

	/* DB服務注入 */
	@Autowired
	public ClBuildingReasonService sClBuildingReasonService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Autowired
	public DataLog dataLog;

	// tita
	int iFunCd;
	int iClCode1;
	int iClCode2;
	int iClNo;

	// new table PK
	ClMainId clMainId = new ClMainId();
	ClBuildingId clBuildingId = new ClBuildingId();
	ClBuildingPublicId clBuildingPublicId = new ClBuildingPublicId();
	ClBuildingParkingId clBuildingParkingId = new ClBuildingParkingId();
	ClBuildingOwnerId clBuildingOwnerId = new ClBuildingOwnerId();
	ClBuildingReasonId clBuildingReasonId = new ClBuildingReasonId();

	// new ArrayList
	List<ClBuildingPublic> lClBuildingPublic = new ArrayList<ClBuildingPublic>();
	List<ClBuildingParking> lClBuildingParking = new ArrayList<ClBuildingParking>();
	List<ClBuildingOwner> lClBuildingOwner = new ArrayList<ClBuildingOwner>();
	List<ClBuildingReason> lClBuildingReason = new ArrayList<ClBuildingReason>();

	// new table
	ClMain tClMain = new ClMain();
	ClBuilding tClBuilding = new ClBuilding();
	ClBuildingPublic tClBuildingPublic = new ClBuildingPublic();
	ClBuildingParking tClBuildingParking = new ClBuildingParking();
	ClBuildingOwner tClBuildingOwner = new ClBuildingOwner();
	ClBuildingReason tClBuildingReason = new ClBuildingReason();
	private boolean isEloan = false;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2415 ");
		this.totaVo.init(titaVo);

		// isEloan
		if (titaVo.isEloan() || "ELTEST".equals(titaVo.getTlrNo())) {
			this.isEloan = true;
		}

		iFunCd = parse.stringToInteger(titaVo.getParam("FunCd"));
		iClCode1 = parse.stringToInteger(titaVo.getParam("ClCode1"));
		iClCode2 = parse.stringToInteger(titaVo.getParam("ClCode2"));
		iClNo = parse.stringToInteger(titaVo.getParam("ClNo"));

		// 塞 ClMain
		clMainId.setClCode1(iClCode1);
		clMainId.setClCode2(iClCode2);
		clMainId.setClNo(iClNo);

		// 塞 ClBuilding
		clBuildingId.setClCode1(iClCode1);
		clBuildingId.setClCode2(iClCode2);
		clBuildingId.setClNo(iClNo);

		tClMain = sClMainService.findById(clMainId, titaVo);
		if (tClMain == null) {
			throw new LogicException("E2027", "擔保品主檔");
		}

		// 不動產建物檔是否存在
		tClBuilding = sClBuildingService.holdById(clBuildingId);

		// 單一性
		if (this.isEloan && iFunCd == 1 && tClBuilding != null) {
			iFunCd = 2;
		}

		// 擔保品主檔有資料
//		// 新增
//		if (iFunCd == 1) {
//			if (tClBuilding != null) {
//				throw new LogicException("E0002", "擔保品不動產建物檔主檔");
//			} else {
//				tClBuilding = new ClBuilding();
//			}
//			// 擔保品不動產建物檔主檔
//			setClBuilding(titaVo);
//			try {
//				tClBuilding = sClBuildingService.insert(tClBuilding, titaVo);
//			} catch (DBException e) {
//				throw new LogicException("E0005", "擔保品不動產建物檔主檔" + e.getErrorMsg());
//			}
//
//			// insert 公設建號
//			insertClBuildingPublic(titaVo);
//
//			// insert ClParking 車位
//			insertClParking(titaVo);
//
//			// insert 擔保品不動產建物修改原因檔
//			insertClBuildingReason(titaVo);
//
		if (iFunCd == 1 || iFunCd == 2) {

			if (tClBuilding == null) {
				tClBuilding = new ClBuilding();
			}
			this.info("tClBuilding = " + tClBuilding);
			// 變更前
			ClBuilding beforeClBuilding = (ClBuilding) dataLog.clone(tClBuilding);
			// 擔保品不動產建物檔主檔
			setClBuilding(titaVo);
			try {
				tClBuilding = sClBuildingService.update2(tClBuilding, titaVo);

			} catch (DBException e) {
				throw new LogicException("E0007", "擔保品不動產建物檔" + e.getErrorMsg());
			}

			// delete 停車位形式
			deleteClParkingType(titaVo);
			// insert 停車位形式
			insertClParkingType(titaVo);

			// 紀錄變更前變更後
			dataLog.setEnv(titaVo, beforeClBuilding, tClBuilding);
			dataLog.exec("修改擔保品不動產建物檔資料");

			// delete 公設建號
			deleteClBuildingPublic(titaVo);
			// insert 公設建號
			insertClBuildingPublic(titaVo);

			// delete ClBuildingParking 獨立產權車位

			deleteClParking(titaVo);
			// insert ClBuildingParking 獨立產權車位

			insertClParking(titaVo);

			// delete 擔保品不動產建物修改原因檔
			deleteClBuildingReason(titaVo);
			// insert 擔保品不動產建物修改原因檔
			insertClBuildingReason(titaVo);

			// FunCD=4 刪除
		} else if (iFunCd == 4) {

			// 擔保品不動產建物檔主檔
			setClBuilding(titaVo);
			try {
				tClBuilding = sClBuildingService.update2(tClBuilding, titaVo);

			} catch (DBException e) {
				throw new LogicException("E0007", "擔保品不動產建物檔" + e.getErrorMsg());
			}

			// delete 停車位形式
			deleteClParkingType(titaVo);

			// delete 公設建號
			deleteClBuildingPublic(titaVo);

			// delete ClBuildingParking 獨立產權車位

			deleteClParking(titaVo);

			// delete 擔保品不動產建物修改原因檔
			deleteClBuildingReason(titaVo);
		}
		//
		this.totaVo.putParam("OResult", "Y");

		this.addList(this.totaVo);
		return this.sendList();

	}

	//
	private void setClBuilding(TitaVo titaVo) throws LogicException {

		if (iFunCd == 4) {
			tClBuilding.setBdMainUseCode("");
			tClBuilding.setBdMtrlCode("");
			tClBuilding.setBdTypeCode("");
			tClBuilding.setClBuildingId(clBuildingId);
			tClBuilding.setTotalFloor(0);
			tClBuilding.setFloorNo("");
			tClBuilding.setFloorArea(BigDecimal.ZERO);
			tClBuilding.setEvaUnitPrice(BigDecimal.ZERO);
			tClBuilding.setRoofStructureCode("");
			tClBuilding.setBdDate(0);
			tClBuilding.setBdSubUsageCode("");
			tClBuilding.setBdSubArea(BigDecimal.ZERO);
			tClBuilding.setSellerId("");
			tClBuilding.setSellerName("");
			tClBuilding.setContractPrice(BigDecimal.ZERO);
			tClBuilding.setContractDate(0);
			tClBuilding.setBdUsageCode("");
			tClBuilding.setParkingTypeCode("");
//			tClBuilding.setParkingProperty("");
//			tClBuilding.setParkingArea(BigDecimal.ZERO);
			tClBuilding.setHouseTaxNo("");
			tClBuilding.setHouseBuyDate(0);
		} else {
			tClBuilding.setBdMainUseCode(titaVo.getParam("BdMainUseCode"));
			tClBuilding.setBdMtrlCode(titaVo.getParam("BdMtrlCode"));
			tClBuilding.setBdTypeCode(titaVo.getParam("BdTypeCode"));
			tClBuilding.setClBuildingId(clBuildingId);
			tClBuilding.setTotalFloor(parse.stringToInteger(titaVo.getParam("TotalFloor")));
			tClBuilding.setFloorNo(titaVo.getParam("FloorNo"));
			tClBuilding.setFloorArea(parse.stringToBigDecimal(titaVo.getParam("FloorArea")));
			tClBuilding.setEvaUnitPrice(parse.stringToBigDecimal(titaVo.getParam("EvaUnitPrice")));
			tClBuilding.setRoofStructureCode(titaVo.getParam("RoofStructureCode"));
			tClBuilding.setBdDate(parse.stringToInteger(titaVo.getParam("BdDate")));
			tClBuilding.setBdSubUsageCode(titaVo.getParam("BdSubUsageCode"));
			tClBuilding.setBdSubArea(parse.stringToBigDecimal(titaVo.getParam("BdSubArea")));
			tClBuilding.setSellerId(titaVo.getParam("SellerId"));
			tClBuilding.setSellerName(titaVo.getParam("SellerName"));
			tClBuilding.setContractPrice(parse.stringToBigDecimal(titaVo.getParam("ContractPrice")));
			tClBuilding.setContractDate(parse.stringToInteger(titaVo.getParam("ContractDate")));
			tClBuilding.setBdUsageCode(titaVo.getParam("BdUsageCode"));
			tClBuilding.setParkingTypeCode(titaVo.getParam("ParkingTypeCode"));
//			tClBuilding.setParkingArea(parse.stringToBigDecimal(titaVo.getParam("ParkingArea")));
//			tClBuilding.setParkingProperty(titaVo.getParam("ParkingProperty"));
			tClBuilding.setHouseTaxNo(titaVo.getParam("HouseTaxNo"));
			tClBuilding.setHouseBuyDate(parse.stringToInteger(titaVo.getParam("HouseBuyDate")));
		}
	}

	private void insertClParkingType(TitaVo titaVo) throws LogicException {

		for (int i = 1; i <= 5; i++) {
			// 若該筆無資料就離開迴圈
			String publicTypeCode = titaVo.get("ParkingTypeCodeA" + i);

			// 若該筆無資料就離開迴圈
			if (publicTypeCode == null || "".equals(publicTypeCode.trim())) {
				break;
			}

			ClParkingType tClParkingType = new ClParkingType();

			this.info("iParkingNo L2415 " + publicTypeCode);
			ClParkingTypeId clParkingTypeId = new ClParkingTypeId();

			clParkingTypeId.setClCode1(iClCode1);
			clParkingTypeId.setClCode2(iClCode2);
			clParkingTypeId.setClNo(iClNo);
			clParkingTypeId.setParkingTypeCode(publicTypeCode);
			this.info("ClParkingId L2415 " + clParkingTypeId);

			tClParkingType.setClParkingTypeId(clParkingTypeId);
			tClParkingType.setClCode1(iClCode1);
			tClParkingType.setClCode2(iClCode2);
			tClParkingType.setClNo(iClNo);
			tClParkingType.setParkingTypeCode(publicTypeCode);
			tClParkingType.setParkingQty(parse.stringToInteger(titaVo.get("ParkingQtyA" + i)));
			tClParkingType.setParkingArea(parse.stringToBigDecimal(titaVo.get("ParkingAreaA" + i)));
			this.info("tClParking L2415" + tClParkingType);
			try {
				sClParkingTypeService.insert(tClParkingType, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "擔保品停車位型式檔" + e.getErrorMsg());
			}
		}

	}

	// delete 車位
	private void deleteClParkingType(TitaVo titaVo) throws LogicException {
		this.info("L2415 deleteClParkingType");
		Slice<ClParkingType> slClParkingType = sClParkingTypeService.clNoEq(iClCode1, iClCode2, iClNo, 0, Integer.MAX_VALUE);
		List<ClParkingType> lClParkingType = slClParkingType == null ? null : slClParkingType.getContent();
		if (lClParkingType != null) {
			try {
				sClParkingTypeService.deleteAll(lClParkingType);
			} catch (DBException e) {
				throw new LogicException("E0008", "擔保品停車位型式檔" + e.getErrorMsg());
			}
		}
	}

	// insert 公設建號
	private void insertClBuildingPublic(TitaVo titaVo) throws LogicException {
		for (int i = 1; i <= 10; i++) {
			// 若該筆無資料就離開迴圈
			String publicBdNoA = titaVo.get("PublicBdNoA" + i);

			if (publicBdNoA == null || "".equals(publicBdNoA) || parse.stringToInteger(publicBdNoA) == 0) {
				break;
			}

			// new table ClBuildingPublic
			tClBuildingPublic = new ClBuildingPublic();
			this.info("i = " + i);
			clBuildingPublicId = new ClBuildingPublicId();
			clBuildingPublicId.setClCode1(iClCode1);
			clBuildingPublicId.setClCode2(iClCode2);
			clBuildingPublicId.setClNo(iClNo);
//			clBuildingPublicId.setPublicBdNo1(parse.stringToInteger(titaVo.getParam("PublicBdNoA" + i)));
//			clBuildingPublicId.setPublicBdNo2(parse.stringToInteger(titaVo.getParam("PublicBdNoB" + i)));
			tClBuildingPublic.setClBuildingPublicId(clBuildingPublicId);
			tClBuildingPublic.setClCode1(iClCode1);
			tClBuildingPublic.setClCode2(iClCode2);
			tClBuildingPublic.setClNo(iClNo);
			tClBuildingPublic.setPublicBdNo1(parse.stringToInteger(titaVo.getParam("PublicBdNoA" + i)));
			tClBuildingPublic.setPublicBdNo2(parse.stringToInteger(titaVo.getParam("PublicBdNoB" + i)));
			tClBuildingPublic.setArea(parse.stringToBigDecimal(titaVo.getParam("Area" + i)));
			tClBuildingPublic.setOwnerId(titaVo.getParam("PublicBdOwnerId" + i));
			tClBuildingPublic.setOwnerName(titaVo.getParam("PublicBdOwnerName" + i));
			try {
				this.info("clBuildingPublicId = " + clBuildingPublicId);
				sClBuildingPublicService.insert(tClBuildingPublic, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "擔保品不動產建物公設建號檔" + e.getErrorMsg());
			}
		}
	}

	// 先刪除資料後新增
	private void deleteClBuildingPublic(TitaVo titaVo) throws LogicException {
		Slice<ClBuildingPublic> slClBuildingPublic = sClBuildingPublicService.clNoEq(iClCode1, iClCode2, iClNo, 0, Integer.MAX_VALUE);
		lClBuildingPublic = slClBuildingPublic == null ? null : slClBuildingPublic.getContent();
		if (lClBuildingPublic != null) {
			try {
				sClBuildingPublicService.deleteAll(lClBuildingPublic);
			} catch (DBException e) {
				throw new LogicException("E0008", "擔保品不動產建物公設建號檔" + e.getErrorMsg());
			}
		}
	}

	// insert 車位
	private void insertClParking(TitaVo titaVo) throws LogicException {
		this.info("L2415 insertClParking");
		// List ClParking 獨立產權車位
		for (int i = 1; i <= 10; i++) {
			String parkingNo = titaVo.get("ParkingNo" + i);

			// 若該筆無資料就離開迴圈
			if (parkingNo == null || "".equals(parkingNo.trim())) {
				break;
			}
			// new table ClBuildingPublic
			ClParking tClParking = new ClParking();

			this.info("iParkingNo L2415 " + parkingNo);
			ClParkingId clParkingId = new ClParkingId();

			clParkingId.setClCode1(iClCode1);
			clParkingId.setClCode2(iClCode2);
			clParkingId.setClNo(iClNo);
			clParkingId.setParkingSeqNo(i);
			this.info("ClParkingId L2415 " + clParkingId);

			tClParking.setClParkingId(clParkingId);

			tClParking.setParkingNo(parkingNo);
			tClParking.setParkingQty(parse.stringToInteger(titaVo.getParam("ParkingQty" + i)));
			tClParking.setParkingTypeCode(titaVo.getParam("ParkingTypeCode" + i));
			tClParking.setOwnerPart(parse.stringToBigDecimal(titaVo.getParam("ParkingOwnerPart" + i)));
			tClParking.setOwnerTotal(parse.stringToBigDecimal(titaVo.getParam("ParkingOwnerTotal" + i)));
			tClParking.setParkingArea(parse.stringToBigDecimal(titaVo.getParam("ParkingArea" + i)));
			tClParking.setLandNo1(titaVo.getParam("ParkingLandNoA" + i));
			tClParking.setLandNo2(titaVo.getParam("ParkingLandNoB" + i));
			tClParking.setBdNo1(titaVo.getParam("ParkingBdNoA" + i));
			tClParking.setBdNo2(titaVo.getParam("ParkingBdNoB" + i));
			tClParking.setAmount(parse.stringToBigDecimal(titaVo.getParam("ParkingAmount" + i)));

			this.info("tClParking L2415" + tClParking);
			try {
				sClParkingService.insert(tClParking, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "擔保品不動產車位檔" + e.getErrorMsg());
			}
		}
	}

	// delete 車位
	private void deleteClParking(TitaVo titaVo) throws LogicException {
		this.info("L2415 deleteClParking");
		Slice<ClParking> slClParking = sClParkingService.clNoEq(iClCode1, iClCode2, iClNo, 0, Integer.MAX_VALUE);
		List<ClParking> lClParking = slClParking == null ? null : slClParking.getContent();
		if (lClParking != null) {
			try {
				sClParkingService.deleteAll(lClParking);
			} catch (DBException e) {
				throw new LogicException("E0008", "擔保品不動產車位檔" + e.getErrorMsg());
			}
		}
	}

	// insert 擔保品不動產建物修改原因檔
	private void insertClBuildingReason(TitaVo titaVo) throws LogicException {
		for (int i = 1; i <= 10; i++) {
			String reason = titaVo.get("Reason" + i);
			// 若該筆無資料就離開迴圈
//			if (titaVo.getParam("Reason" + i) == null || titaVo.getParam("Reason" + i).trim().isEmpty()) {
//				break;
//			}
			if (reason == null || "".equals(reason.trim())) {
				break;
			}

			tClBuildingReason = new ClBuildingReason();
			clBuildingReasonId = new ClBuildingReasonId();

			clBuildingReasonId.setClCode1(iClCode1);
			clBuildingReasonId.setClCode2(iClCode2);
			clBuildingReasonId.setClNo(iClNo);
			clBuildingReasonId.setReasonSeq(i);

			tClBuildingReason.setClBuildingReasonId(clBuildingReasonId);
			tClBuildingReason.setClCode1(iClCode1);
			tClBuildingReason.setClCode2(iClCode2);
			tClBuildingReason.setClNo(iClNo);
			tClBuildingReason.setReasonSeq(i);
			tClBuildingReason.setReason(parse.stringToInteger(titaVo.getParam("Reason" + i)));
			this.info("OtherReason =" + titaVo.getParam("OtherReason" + i));
			this.info("CreateEmpNo =" + titaVo.getParam("CreateEmpNo" + i));

			tClBuildingReason.setOtherReason(titaVo.getParam("OtherReason" + i));
			tClBuildingReason.setCreateEmpNo(titaVo.getParam("CreateEmpNo" + i));
			tClBuildingReason.setLastUpdateEmpNo(titaVo.getParam("CreateEmpNo" + i));

			try {
				sClBuildingReasonService.insert(tClBuildingReason, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "擔保品不動產建物修改原因檔" + e.getErrorMsg());
			}
		}
	}

	// delete 擔保品不動產建物修改原因檔
	private void deleteClBuildingReason(TitaVo titaVo) throws LogicException {
		Slice<ClBuildingReason> slClBuildingReason = sClBuildingReasonService.clNoEq(iClCode1, iClCode2, iClNo, 0, Integer.MAX_VALUE);
		lClBuildingReason = slClBuildingReason == null ? null : slClBuildingReason.getContent();
		if (lClBuildingReason != null) {
			try {
				sClBuildingReasonService.deleteAll(lClBuildingReason);
			} catch (DBException e) {
				throw new LogicException("E0008", "擔保品不動產建物修改原因檔" + e.getErrorMsg());
			}
		}
	}

}