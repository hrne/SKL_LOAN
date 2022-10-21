package com.st1.itx.trade.L2;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.ClBuilding;
import com.st1.itx.db.domain.ClBuildingId;
import com.st1.itx.db.domain.ClBuildingOwner;
import com.st1.itx.db.domain.ClBuildingOwnerId;
import com.st1.itx.db.domain.ClFac;
import com.st1.itx.db.domain.ClFacId;
import com.st1.itx.db.domain.ClImm;
import com.st1.itx.db.domain.ClImmId;
import com.st1.itx.db.domain.ClLand;
import com.st1.itx.db.domain.ClLandId;
import com.st1.itx.db.domain.ClLandOwner;
import com.st1.itx.db.domain.ClLandOwnerId;
import com.st1.itx.db.domain.ClMain;
import com.st1.itx.db.domain.ClMainId;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.service.CdAreaService;
import com.st1.itx.db.service.CdCityService;
import com.st1.itx.db.service.CdClService;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.CdLandSectionService;
import com.st1.itx.db.service.ClBuildingOwnerService;
import com.st1.itx.db.service.ClBuildingService;
import com.st1.itx.db.service.ClFacService;
import com.st1.itx.db.service.ClImmService;
import com.st1.itx.db.service.ClLandOwnerService;
import com.st1.itx.db.service.ClLandService;
import com.st1.itx.db.service.ClMainService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.TxFileService;
import com.st1.itx.db.service.springjpa.cm.L8110ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.ClFacCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;

@Service("L2419Batch")
@Scope("prototype")
/**
 * 擔保品整批匯入,寫入擔保品資料
 * 
 * @author Chih Wei
 * @version 1.0.0
 */
public class L2419Batch extends TradeBuffer {

	@Value("${iTXInFolder}")
	private String inFolder = "";

	@Autowired
	private Parse parse;

	@Autowired
	private DateUtil dateUtil;

	@Autowired
	private WebClient webClient;

	@Autowired
	private ClImmService sClImmService;
	@Autowired
	private ClMainService sClMainService;
	@Autowired
	private CdCodeService sCdCodeService;
	@Autowired
	private CustMainService sCustMainService;
	@Autowired
	private CdClService sCdClService;
	@Autowired
	private CdCityService sCdCityService;
	@Autowired
	private CdAreaService sCdAreaService;
	@Autowired
	private ClBuildingService sClBuildingService;
	@Autowired
	private ClBuildingOwnerService sClBuildingOwnerService;
	@Autowired
	private ClLandService sClLandService;
	@Autowired
	private ClLandOwnerService sClLandOwnerService;
	@Autowired
	private ClFacService sClFacService;
	@Autowired
	private FacMainService sFacMainService;
	/* DB服務注入 */
	@Autowired
	private TxFileService sTxFileService;
	@Autowired
	private CdLandSectionService sCdLandSectionService;
	@Autowired
	private ClFacCom clFacCom;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2419Batch ");
		this.totaVo.init(titaVo);

		int idx = parse.stringToInteger(titaVo.getParam("Idx"));

		this.info("active Step2 = " + idx);

		int applNo = parse.stringToInteger(titaVo.getParam("ApplNo"));

		FacMain facMain = sFacMainService.facmApplNoFirst(applNo, titaVo);
		if (facMain == null) {
			throw new LogicException("E2019", "核准號碼 = " + applNo);
		}

		CustMain custMain = sCustMainService.custNoFirst(facMain.getCustNo(), facMain.getCustNo(), titaVo);
		if (custMain == null) {
			throw new LogicException("E1004", "戶號 = " + facMain.getCustNo());
		}

		String fileItem = titaVo.getParam("FileItem");
		String fileName = inFolder + dateUtil.getNowStringBc() + File.separatorChar + titaVo.getTlrNo()
				+ File.separatorChar + fileItem;

		this.info("fileitem=" + fileItem);

		int clCode1 = parse.stringToInteger(titaVo.getParam("ClCode1"));
		int clCode2 = parse.stringToInteger(titaVo.getParam("ClCode2"));
		int clNo = parse.stringToInteger(titaVo.getParam("ClNo"));

		if (clCode1 == 0 || clCode2 == 0) {
			throw new LogicException("E0019", "擔保品代號錯誤");
		}

		ClMain clMain = new ClMain();
		ClMainId clMainId = new ClMainId();

		clMainId.setClCode1(clCode1);
		clMainId.setClCode2(clCode2);

		boolean newfg = false; // 修改
		if (clNo == 0) {
			// TODO: 剔錯誤:回饋檔產生時應該已取號
		} else {
			clMainId.setClNo(clNo);

			clMain = sClMainService.findById(clMainId, titaVo);
			if (clMain == null) {
				String clNoX = String.format("%01d-%02d-%07d", clCode1, clCode2, clNo);
				throw new LogicException("E0001", "擔保品主檔(ClMain)=" + clNoX);
			}
		}

		String clNoX = String.format("%01d-%02d-%07d", clCode1, clCode2, clNo);

		this.info("L2419 ClCode = " + clNoX);

		// 擔保品類別代碼
		clMain.setClTypeCode(titaVo.getParam("TypeCode"));
		// 地區別
		clMain.setCityCode(titaVo.getParam("CityCode"));
		// 鄉鎮區
		clMain.setAreaCode(titaVo.getParam("AreaCode"));
		// 擔保品狀況碼-1:已抵押
		clMain.setClStatus("1");
		// 鑑估日期
		int evaDate = parse.stringToInteger(titaVo.getParam("EvaDate"));
		clMain.setEvaDate(evaDate);
		// 鑑估總值
		clMain.setEvaAmt(parse.stringToBigDecimal(titaVo.getParam("EvaAmt")));

		// 2022/7/29新增
		// 計算可分配金額
		BigDecimal shareTotal;
		BigDecimal shareCompAmt;
		BigDecimal wkEvaAmt = parse.stringToBigDecimal(titaVo.getParam("EvaAmt")); // 鑑估總值
		BigDecimal wkEvaNetWorth = parse.stringToBigDecimal(titaVo.getParam("NetWorth")); // 評估淨值
		// 評估淨值有值時擺評估淨值,否則擺鑑估總值.
		if (wkEvaNetWorth.compareTo(BigDecimal.ZERO) > 0) {
			shareCompAmt = wkEvaNetWorth;
		} else {
			shareCompAmt = wkEvaAmt;
		}
		BigDecimal loanToValue = new BigDecimal(titaVo.getParam("LoanToValue"));// 貸放成數
		this.info("L2419 shareCompAmt = " + shareCompAmt.toString());
		this.info("L2419 LoanToValue = " + loanToValue.toString());
		this.info("L2419 SettingAmt = " + parse.stringToBigDecimal(titaVo.getParam("SettingAmt")).toString());
//		"1.若""評估淨值""有值取""評估淨值""否則取""鑑估總值"")*貸放成數(四捨五入至個位數)
//		2.若設定金額低於可分配金額則為設定金額
//		3.擔保品塗銷/解除設定時(該筆擔保品的可分配金額設為零)"
//		若貸放成數為0則不乘貸放成數
		if (loanToValue.compareTo(BigDecimal.ZERO) == 0) {
			shareTotal = shareCompAmt;
		} else {
			shareTotal = shareCompAmt.multiply(loanToValue).divide(new BigDecimal(100)).setScale(0,
					BigDecimal.ROUND_HALF_UP);
		}
		if (parse.stringToBigDecimal(titaVo.getParam("SettingAmt")).compareTo(shareTotal) < 0) {
			shareTotal = parse.stringToBigDecimal(titaVo.getParam("SettingAmt"));
		}
		clMain.setShareTotal(shareTotal);

		if (newfg) {
			try {
				sClMainService.insert(clMain, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "擔保品主檔(ClMain)" + e.getErrorMsg());
			}
		} else {
			try {
				clMain = sClMainService.update2(clMain, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0007", "擔保品主檔(ClMain)" + e.getErrorMsg());
			}

		}

		ClImmId clImmId = new ClImmId();

		clImmId.setClCode1(clCode1);
		clImmId.setClCode2(clCode2);
		clImmId.setClNo(clNo);

		ClImm clImm = sClImmService.findById(clImmId, titaVo);
		if (clImm == null) {
			newfg = true;

			clImm = new ClImm();
			clImm.setClImmId(clImmId);
		} else {
			newfg = false;
		}

		// 評估淨值
		clImm.setEvaNetWorth(parse.stringToBigDecimal(titaVo.getParam("NetWorth")));
		// 土地增值稅
		clImm.setLVITax(parse.stringToBigDecimal(titaVo.getParam("Tax")));
		// 出租評估淨值
		clImm.setRentEvaValue(BigDecimal.ZERO);
		// 押租金
		clImm.setRentPrice(parse.stringToBigDecimal(titaVo.getParam("RentPrice")));
		// 權利種類:1:抵押權
		clImm.setOwnershipCode("1");
		// 抵押權註記:0:最高限額抵押權
		clImm.setMtgCode("0");
		// 最高限額抵押權之擔保債權種類-票據
		clImm.setMtgCheck("Y");
		// 最高限額抵押權之擔保債權種類-借款
		clImm.setMtgLoan("Y");
		// 最高限額抵押權之擔保債權種類-保證債務
		clImm.setMtgPledge("Y");
		// 檢附同意書
		clImm.setAgreement("Y");
		// 鑑價公司
		clImm.setEvaCompanyCode(titaVo.getParam("EvaCompany"));
		// 擔保註記:1:擔保
		clImm.setClCode("1");
		// 貸放成數(%)
		clImm.setLoanToValue(parse.stringToBigDecimal(titaVo.getParam("LoanToValue")));
		// 設定狀態:1:設定
		clImm.setSettingStat("1");
		// 擔保品狀態:0:正常
		clImm.setClStat("0");
		// 設定日期
		clImm.setSettingDate(getDate(titaVo.getParam("SettingDate")));
		// 設定金額
		clImm.setSettingAmt(parse.stringToBigDecimal(titaVo.getParam("SettingAmt")));
		// 擔保債權確定日期:設定日期+30年
		clImm.setClaimDate(clImm.getSettingDate() + 300000);
		// 設定順位(1~9)
		clImm.setSettingSeq("1");

		if (newfg) {
			try {
				sClImmService.insert(clImm, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "擔保品不動產檔(ClImm)" + e.getErrorMsg());
			}
		} else {
			try {
				sClImmService.update2(clImm, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0007", "擔保品不動產檔(ClImm)" + e.getErrorMsg());
			}
		}

		if (clCode1 == 1) {

			ClBuildingId clBuildingId = new ClBuildingId();

			clBuildingId.setClCode1(clCode1);
			clBuildingId.setClCode2(clCode2);
			clBuildingId.setClNo(clNo);

			ClBuilding clBuilding = sClBuildingService.findById(clBuildingId, titaVo);
			if (clBuilding == null) {
				newfg = true;
				clBuilding = new ClBuilding();
				clBuilding.setClBuildingId(clBuildingId);
			} else {
				newfg = false;
			}

			// 縣市
			clBuilding.setCityCode(clMain.getCityCode());
			// 鄉鎮市區
			clBuilding.setAreaCode(clMain.getAreaCode());
			// 段小段代碼
			clBuilding.setIrCode(titaVo.getParam("IrCode"));
			// 路名
			clBuilding.setRoad(titaVo.getParam("Road"));
			// 建物門牌
			clBuilding.setBdLocation(
					titaVo.getParam("CityCodeX") + titaVo.getParam("AreaCodeX") + titaVo.getParam("Road"));
			// 建號
			clBuilding.setBdNo1(titaVo.getParam("BdNo1"));
			clBuilding.setBdNo2(titaVo.getParam("BdNo2"));
			// 建物主要用途
			clBuilding.setBdMainUseCode(titaVo.getParam("UseCode"));
			// 建物使用別:6:其他
			clBuilding.setBdUsageCode("6");
			// 建物主要建材
			clBuilding.setBdMtrlCode(titaVo.getParam("MtrlCode"));
			// 建物類別
			clBuilding.setBdTypeCode(titaVo.getParam("BuTypeCode"));
			// 總樓層
			clBuilding.setTotalFloor(parse.stringToInteger(titaVo.getParam("TotalFloor")));
			// 擔保品所在樓層
			clBuilding.setFloorNo(titaVo.getParam("FloorNo"));
			// 擔保品所在樓層面積
			clBuilding.setFloorArea(parse.stringToBigDecimal(titaVo.getParam("FloorArea")));
			// 鑑價單價/坪
			clBuilding.setEvaUnitPrice(parse.stringToBigDecimal(titaVo.getParam("UnitPrice")));
			// 屋頂結構:07:其他 ???
			clBuilding.setRoofStructureCode("07");
			// 建築完成日期
			clBuilding.setBdDate(getDate(titaVo.getParam("BdDate")));
			// 房屋取得日期,預設為"建築完成日期"
			clBuilding.setHouseBuyDate(clBuilding.getBdDate());

			if (newfg) {
				try {
					sClBuildingService.insert(clBuilding, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0005", "擔保品不動產建物檔主檔(ClBuilding)" + e.getErrorMsg());
				}
			} else {
				try {
					sClBuildingService.update2(clBuilding, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0007", "擔保品不動產建物檔主檔(ClBuilding)" + e.getErrorMsg());
				}

			}

		} else {
			ClLandId clLandId = new ClLandId();

			clLandId.setClCode1(clCode1);
			clLandId.setClCode2(clCode2);
			clLandId.setClNo(clNo);

			ClLand clLand = sClLandService.findById(clLandId, titaVo);
			if (clLand == null) {
				newfg = true;
				clLand = new ClLand();
				clLand.setClLandId(clLandId);
			} else {
				newfg = false;
			}

			// 土地序號(固定放000)
			clLand.setLandSeq(0);
			// 縣市
			clLand.setCityCode(clMain.getCityCode());
			// 鄉鎮市區
			clLand.setAreaCode(clMain.getAreaCode());
			// 段小段代碼
			clLand.setIrCode(titaVo.getParam("IrCode"));
			// 地號
			clLand.setLandNo1(titaVo.getParam("LdNo1"));
			clLand.setLandNo2(titaVo.getParam("LdNo2"));
			// 土地座落
			String landLocation = titaVo.getParam("CityCodeX") + titaVo.getParam("AreaCodeX")
					+ titaVo.getParam("IrCodeX") + "，地號" + titaVo.getParam("LdNo1") + "-" + titaVo.getParam("LdNo2");
			clLand.setLandLocation(landLocation);
			// 土地增值稅
			clLand.setLVITax(parse.stringToBigDecimal(titaVo.getParam("Tax")));
			// 鑑價單價/坪
			clLand.setEvaUnitPrice(parse.stringToBigDecimal(titaVo.getParam("UnitPrice")));
			// 面積
			clLand.setArea(parse.stringToBigDecimal(titaVo.getParam("FloorArea")));

			if (newfg) {
				try {
					sClLandService.insert(clLand, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0005", "擔保品不動產建土地主檔(ClLand)" + e.getErrorMsg());
				}
			} else {
				try {
					sClLandService.update2(clLand, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0007", "擔保品不動產建土地主檔(ClLand)" + e.getErrorMsg());
				}

			}
		}

		String ownerString = titaVo.getParam("Owner").trim();

		this.info("OwnerString = " + ownerString);

		if (ownerString.isEmpty()) {
			buildOwner(titaVo, clCode1, clCode2, clNo, custMain.getCustId(), custMain.getCustName(), "00",
					new BigDecimal("1"), new BigDecimal("1"));
		} else {
//			String[] owners = ownerString.split("/");
//			for (int j = 0; j < owners.length; j++) {
//				String[] s = owners[j].split(",");
//				if (custMain.getCustId().equals(s[0])) {
//					if (owners.length == 1) {
//						buildOwner(titaVo, clCode1, clCode2, clNo, custMain.getCustId(), custMain.getCustName(), "00",
//								new BigDecimal("1"), new BigDecimal("1"));
//					} else {
//						buildOwner(titaVo, clCode1, clCode2, clNo, custMain.getCustId(), custMain.getCustName(), "00",
//								new BigDecimal(s[3]), new BigDecimal(s[4]));
//					}
//				} else {
//					String relaCode = s[2];
//					if (owners.length == 1) {
//						buildOwner(titaVo, clCode1, clCode2, clNo, s[0], s[1], relaCode, new BigDecimal("1"),
//								new BigDecimal("1"));
//					} else {
//						buildOwner(titaVo, clCode1, clCode2, clNo, s[0], s[1], relaCode, new BigDecimal(s[3]),
//								new BigDecimal(s[4]));
//					}
//				}
//			}
		}

		// 刪除不在名單 owner

//		if (clCode1 == 1) {
//			Slice<ClBuildingOwner> slClBuildingOwner = sClBuildingOwnerService.clNoEq(clCode1, clCode2, clNo, 0,
//					Integer.MAX_VALUE, titaVo);
//			List<ClBuildingOwner> lClBuildingOwner = slClBuildingOwner == null ? null : slClBuildingOwner.getContent();
//			if (lClBuildingOwner != null) {
//				for (ClBuildingOwner clBuildingOwner : lClBuildingOwner) {
//					ClBuildingOwnerId clBuildingOwnerId = clBuildingOwner.getClBuildingOwnerId();
//					this.info("ClBuildingOwner=" + clBuildingOwnerId.getClCode1() + "-" + clBuildingOwnerId.getClCode2()
//							+ "-" + clBuildingOwnerId.getClNo() + "/" + clBuildingOwnerId.getOwnerCustUKey());
//					if (ownerids.get(clBuildingOwnerId.getOwnerCustUKey()) == null) {
////						this.info("[" + clBuildingOwner.getOwnerCustUKey() + "] is null / " + ownerids.size());
//						try {
//							sClBuildingOwnerService.delete(clBuildingOwner, titaVo);
//						} catch (DBException e) {
//							throw new LogicException("E0008", "擔保品-建物所有權人檔(ClBuildingOwner)" + e.getErrorMsg());
//						}
//					}
//				}
//			}
//		} else {
//			Slice<ClLandOwner> slClLandOwner = sClLandOwnerService.clNoEq(clCode1, clCode2, clNo, 0, Integer.MAX_VALUE,
//					titaVo);
//			List<ClLandOwner> lClLandOwner = slClLandOwner == null ? null : slClLandOwner.getContent();
//			if (lClLandOwner != null) {
//				for (ClLandOwner clLandOwner : lClLandOwner) {
//					ClLandOwnerId clLandOwnerId = clLandOwner.getClLandOwnerId();
//					this.info("clLandOwner=" + clLandOwnerId.getClCode1() + "-" + clLandOwnerId.getClCode2() + "-"
//							+ clLandOwnerId.getClNo() + "/" + clLandOwnerId.getOwnerCustUKey());
//					if (ownerids.get(clLandOwnerId.getOwnerCustUKey()) == null) {
////						this.info("[" + clBuildingOwner.getOwnerCustUKey() + "] is null / " + ownerids.size());
//						try {
//							sClLandOwnerService.delete(clLandOwner, titaVo);
//						} catch (DBException e) {
//							throw new LogicException("E0008", "擔保品-建物所有權人檔(ClBuildingOwner)" + e.getErrorMsg());
//						}
//					}
//				}
//			}
//		}

		ClFacId clFacId = new ClFacId();
		clFacId.setClCode1(clCode1);
		clFacId.setClCode2(clCode2);
		clFacId.setClNo(clNo);
		clFacId.setApproveNo(applNo);

		ClFac clFac = sClFacService.findById(clFacId, titaVo);
		if (clFac == null) {
			newfg = true;
			clFac = new ClFac();
			clFac.setClFacId(clFacId);
		} else {
			newfg = false;
		}

		clFac.setCustNo(facMain.getCustNo());
		clFac.setFacmNo(facMain.getFacmNo());
		if (newfg) {
			clFac.setOriSettingAmt(parse.stringToBigDecimal(titaVo.getParam("SettingAmt")));
			try {
				sClFacService.insert(clFac, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "擔保品與額度關聯檔(ClFac)" + e.getErrorMsg());
			}
		} else {
			try {
				sClFacService.update2(clFac, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0007", "擔保品與額度關聯檔(ClFac)" + e.getErrorMsg());
			}
		}

		// 額度與擔保品關聯檔變動處理
		clFacCom.changeClFac(applNo, titaVo);

		this.totaVo.putParam("ClCode1", clCode1);
		this.totaVo.putParam("ClCode2", clCode2);
		this.totaVo.putParam("ClNo", clNo);
		this.totaVo.putParam("ClCode", clNoX);

		// TODO: 連動L2038查詢擔保品寫入資料 可以用核准號碼
		String bufferL2038 = "";

		webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "L2038", bufferL2038, "", titaVo);

		this.batchTransaction.commit();
		return new ArrayList<>();
	}

	private void buildOwner(TitaVo titaVo, int clCode1, int clCode2, int clNo, String ownerId, String ownerName,
			String relCode, BigDecimal part, BigDecimal total) throws LogicException {

		CustMain custMain = sCustMainService.custIdFirst(ownerId, titaVo);

		if (custMain == null) {
			String ukey = UUID.randomUUID().toString().toUpperCase().replace("-", "");
			custMain = new CustMain();
			custMain.setCustUKey(ukey);
			custMain.setCustId(ownerId);
			custMain.setCustName(ownerName);
			custMain.setDataStatus(1);
			custMain.setTypeCode(2);
			if (ownerId.length() == 8) {
				custMain.setCuscCd("2");
			} else {
				custMain.setCuscCd("1");
			}
			try {
				sCustMainService.insert(custMain, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "客戶資料主檔");
			}

		}

		this.info("owner =" + clCode1 + "-" + clCode2 + "-" + clNo + "/" + custMain.getCustId());

		if (clCode1 == 1) {
			ClBuildingOwnerId clBuildingOwnerId = new ClBuildingOwnerId();
			clBuildingOwnerId.setClCode1(clCode1);
			clBuildingOwnerId.setClCode2(clCode2);
			clBuildingOwnerId.setClNo(clNo);
			clBuildingOwnerId.setOwnerCustUKey(custMain.getCustUKey());

			ClBuildingOwner clBuildingOwner = sClBuildingOwnerService.findById(clBuildingOwnerId, titaVo);

			boolean newfg = false;
			if (clBuildingOwner == null) {
				newfg = true;

				clBuildingOwner = new ClBuildingOwner();
				clBuildingOwner.setClBuildingOwnerId(clBuildingOwnerId);
			}

			// 與授信戶關係
			clBuildingOwner.setOwnerRelCode(relCode);
			// 持份比率(分子)
			clBuildingOwner.setOwnerPart(part);
			// 持份比率(分母)
			clBuildingOwner.setOwnerTotal(total);

			if (newfg) {
				try {
					sClBuildingOwnerService.insert(clBuildingOwner, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0005", "擔保品-建物所有權人檔(ClBuildingOwner)" + e.getErrorMsg());
				}
			} else {
				try {
					sClBuildingOwnerService.update2(clBuildingOwner, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0007", "擔保品-建物所有權人檔(ClBuildingOwner)" + e.getErrorMsg());
				}
			}
		} else {
			ClLandOwnerId clLanOwnerId = new ClLandOwnerId();
			clLanOwnerId.setClCode1(clCode1);
			clLanOwnerId.setClCode2(clCode2);
			clLanOwnerId.setClNo(clNo);
			clLanOwnerId.setOwnerCustUKey(custMain.getCustUKey());

			ClLandOwner clLandOwner = sClLandOwnerService.findById(clLanOwnerId, titaVo);

			boolean newfg = false;
			if (clLandOwner == null) {
				newfg = true;

				clLandOwner = new ClLandOwner();
				clLandOwner.setClLandOwnerId(clLanOwnerId);
			}

			// 與授信戶關係
			clLandOwner.setOwnerRelCode(relCode);
			// 持份比率(分子)
			clLandOwner.setOwnerPart(part);
			// 持份比率(分母)
			clLandOwner.setOwnerTotal(total);

			if (newfg) {
				try {
					sClLandOwnerService.insert(clLandOwner, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0005", "擔保品-土地所有權人檔(ClBuildingOwner)" + e.getErrorMsg());
				}
			} else {
				try {
					sClLandOwnerService.update2(clLandOwner, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0007", "擔保品-土地所有權人檔(ClBuildingOwner)" + e.getErrorMsg());
				}
			}
		}

		this.info("ownerids put = " + custMain.getCustUKey());
	}

	private int getDate(String s) throws LogicException {
		int r = 0;

		s = s.replace("/", "");

		r = parse.stringToInteger(s);

		return r;

	}
}