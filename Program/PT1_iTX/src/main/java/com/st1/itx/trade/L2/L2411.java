package com.st1.itx.trade.L2;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdArea;
import com.st1.itx.db.domain.CdAreaId;
import com.st1.itx.db.domain.CdCity;
import com.st1.itx.db.domain.CdLandSection;
import com.st1.itx.db.domain.CdLandSectionId;
import com.st1.itx.db.domain.ClBuilding;
import com.st1.itx.db.domain.ClBuildingId;
import com.st1.itx.db.domain.ClBuildingOwner;
import com.st1.itx.db.domain.ClBuildingOwnerId;
import com.st1.itx.db.domain.ClFac;
import com.st1.itx.db.domain.ClFacId;
import com.st1.itx.db.domain.ClImm;
import com.st1.itx.db.domain.ClImmId;
import com.st1.itx.db.domain.ClImmRankDetail;
import com.st1.itx.db.domain.ClImmRankDetailId;
import com.st1.itx.db.domain.ClLand;
import com.st1.itx.db.domain.ClLandId;
import com.st1.itx.db.domain.ClLandOwner;
import com.st1.itx.db.domain.ClLandOwnerId;
import com.st1.itx.db.domain.ClMain;
import com.st1.itx.db.domain.ClMainId;
import com.st1.itx.db.domain.ClOwnerRelation;
import com.st1.itx.db.domain.ClOwnerRelationId;
import com.st1.itx.db.domain.ClParkingType;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.service.CdAreaService;
import com.st1.itx.db.service.CdCityService;
import com.st1.itx.db.service.CdClService;
import com.st1.itx.db.service.CdLandSectionService;
import com.st1.itx.db.service.ClBuildingOwnerService;
import com.st1.itx.db.service.ClBuildingService;
import com.st1.itx.db.service.ClFacService;
import com.st1.itx.db.service.ClImmRankDetailService;
import com.st1.itx.db.service.ClImmService;
import com.st1.itx.db.service.ClLandOwnerService;
import com.st1.itx.db.service.ClLandService;
import com.st1.itx.db.service.ClMainService;
import com.st1.itx.db.service.ClOwnerRelationService;
import com.st1.itx.db.service.ClParkingTypeService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacCaseApplService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.CheckClEva;
import com.st1.itx.util.common.ClFacCom;
import com.st1.itx.util.common.GSeqCom;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L2411")
@Scope("prototype")
/**
 * ??????????????????????????????
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2411 extends TradeBuffer {

	@Autowired
	public DataLog dataLog;

	/* DB???????????? */
	@Autowired
	public ClImmService sClImmService;
	@Autowired
	public ClImmRankDetailService sClImmRankDetailService;
	@Autowired
	public ClMainService sClMainService;
	@Autowired
	public CustMainService sCustMainService;
	@Autowired
	public CdClService sCdClService;
	@Autowired
	public CdCityService sCdCityService;
	@Autowired
	public CdAreaService sCdAreaService;
	@Autowired
	public CdLandSectionService sCdLandSectionService;
	@Autowired
	public ClBuildingService sClBuildingService;
	@Autowired
	public ClBuildingOwnerService sClBuildingOwnerService;
	@Autowired
	public ClLandService sClLandService;
	@Autowired
	public ClLandOwnerService sClLandOwnerService;
	@Autowired
	public ClFacService sClFacService;
	@Autowired
	public FacMainService sFacMainService;
	@Autowired
	public FacCaseApplService sFacCaseApplService;

	@Autowired
	public CheckClEva sCheckClEva;

	/* DB???????????? */
	@Autowired
	public ClParkingTypeService sClParkingTypeService;

	@Autowired
	public ClOwnerRelationService sClOwnerRelationService;

	/* ???????????? */
	@Autowired
	GSeqCom gGSeqCom;
	@Autowired
	public ClFacCom clFacCom;
	/* ???????????? */
	@Autowired
	public DateUtil dateUtil;

	/* ???????????? */
	@Autowired
	public Parse parse;

	/* work area */
	private int iFunCd;

	// ???????????????1
	private int iClCode1;
	// ???????????????2
	private int iClCode2;
	// ???????????????
	private int iClNo;
	// ????????????
	private int iApplNo;
	// ????????????
	private String iSettingSeq;
	// ??????

	private String finalClNo;
	private ClMainId ClMainId = new ClMainId();
	private ClImmId ClImmId = new ClImmId();
	private ClImmRankDetailId ClImmRankDetailId = new ClImmRankDetailId();
	private ClMain tClMain = new ClMain();
	private ClImm tClImm = new ClImm();
	private ClImmRankDetail tClImmRankDetail = new ClImmRankDetail();
	private ClBuilding tClBuilding = new ClBuilding();
	private ClBuildingId clBuildingId = new ClBuildingId();
	private ClBuildingOwner tClBuildingOwner = new ClBuildingOwner();
	private ClBuildingOwnerId clBuildingOwnerId = new ClBuildingOwnerId();
	private ClLandId clLandId = new ClLandId();
	private ClLandOwnerId clLandOwnerId = new ClLandOwnerId();
	private ClLand tClLand = new ClLand();
	private ClLandOwner tClLandOwner = new ClLandOwner();
	private List<ClLandOwner> lClLandOwner = new ArrayList<ClLandOwner>();
	private List<ClBuildingOwner> lClBuildingOwner = new ArrayList<ClBuildingOwner>();
	private List<ClImmRankDetail> lClImmRankDetail = new ArrayList<ClImmRankDetail>();
	private String bdLocation;
	private boolean isEloan = false;
	private FacMain tFacMain;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2411 ");
		this.totaVo.init(titaVo);
		if (titaVo.get("ClStat") == null) {
			titaVo.putParam("ClStat", "0"); // ??????????????? 0: ??????
		}
		if (titaVo.get("CancelDate") == null) {
			titaVo.putParam("CancelDate", "0"); // ????????????
		}
		if (titaVo.get("CancelNo") == null) {
			titaVo.putParam("CancelNo", ""); // ????????????
		}
		// new table PK

		// tita
		// ??????
		iFunCd = parse.stringToInteger(titaVo.getParam("FunCd"));

		// ????????????
		iApplNo = parse.stringToInteger(titaVo.getParam("ApplNo"));
		// ???????????????1
		iClCode1 = parse.stringToInteger(titaVo.getParam("ClCode1"));
		// ???????????????2
		iClCode2 = parse.stringToInteger(titaVo.getParam("ClCode2"));
		// ???????????????
		iClNo = parse.stringToInteger(titaVo.getParam("ClNo"));
		// ??????
		finalClNo = StringUtils.leftPad(String.valueOf(iClNo), 7, "0");
		// ????????????
		iSettingSeq = titaVo.getParam("SettingSeq");
		// isEloan
		if (titaVo.isEloan() || "ELTEST".equals(titaVo.getTlrNo())) {
			this.isEloan = true;
		}

		// ????????????
		if (iClCode1 == 1) {
			bdLocation = getBdLocation(titaVo);
		}

//		 ??????????????????????????????
//		  1. ??????????????????
//		         ????????????+??????????????????(???)
//		  2. ??????????????????
//			  ????????????+??????????????????(???)

		if (iFunCd == 1 && iClNo == 0) {
			// ??????????????????????????????
			if (iClCode1 == 1) {
				iClNo = getBuildingClNo(titaVo);

			} else {
				iClNo = getLandClNo(titaVo);
			}
			if (iClNo > 0) {
				finalClNo = StringUtils.leftPad(String.valueOf(iClNo), 7, "0");
				// from ELOAN ????????????
				if (this.isEloan) {
					iFunCd = 2;
					// ???????????????????????????
					sCheckClEva.setClEva(titaVo, iClNo);
				} else {
					iFunCd = 2;
					if (iClCode1 == 1) {
						this.totaVo.setWarnMsg("?????????????????????" + iClNo + "??????????????????????????????????????????????????????????????????");
					} else {
						this.totaVo.setWarnMsg("?????????????????????" + iClNo + "??????????????????????????????????????????????????????????????????");
					}
				}
			}

		}

		// ??????????????? ???????????????????????????????????????+1
		if (iFunCd == 1 && iClNo == 0) {
			this.info("???????????????");

			String clCode = StringUtils.leftPad(String.valueOf(iClCode1), 2, "0")
					+ StringUtils.leftPad(String.valueOf(iClCode2), 2, "0");

			iClNo = gGSeqCom.getSeqNo(0, 0, "L2", clCode, 9999999, titaVo);

			finalClNo = StringUtils.leftPad(String.valueOf(iClNo), 7, "0");

		}
		this.info("??????????????? = " + iClNo);

		ClMainId.setClCode1(iClCode1);
		ClMainId.setClCode2(iClCode2);
		ClMainId.setClNo(iClNo);

		ClImmId.setClCode1(iClCode1);
		ClImmId.setClCode2(iClCode2);
		ClImmId.setClNo(iClNo);

		clBuildingId.setClCode1(iClCode1);
		clBuildingId.setClCode2(iClCode2);
		clBuildingId.setClNo(iClNo);

		clLandId.setClCode1(iClCode1);
		clLandId.setClCode2(iClCode2);
		clLandId.setClNo(iClNo);
		clLandId.setLandSeq(0);

		if (iApplNo > 0) { // ??????????????????0?????????
			tFacMain = sFacMainService.facmApplNoFirst(iApplNo, titaVo);
			if (tFacMain == null) {
				throw new LogicException("E2019", "???????????? = " + iApplNo);
			} else {
//				ELOAN:??????????????????????????????????????????????????????????????????????????????????????????????????????
				if (isEloan) {
					FacMain updFacMain = sFacMainService.holdById(tFacMain, titaVo);
					if (updFacMain != null) {
						updFacMain.setSettingDate(this.txBuffer.getTxCom().getTbsdy());
						try {
							sFacMainService.update(updFacMain, titaVo);
						} catch (DBException e) {
							throw new LogicException("E0007", "????????????" + e.getErrorMsg());
						}
					}

				}
			}
		}

		// ??????????????????
		tClMain = sClMainService.findById(ClMainId, titaVo);

		if (tClMain == null) {
			if (iFunCd == 1) {

				// ???????????????
				this.info("ClMainId1 = " + ClMainId);

				setClMain(titaVo);
				try {
					sClMainService.insert(tClMain, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0005", "???????????????" + e.getErrorMsg());
				}

				// ?????????????????????
				setClImm(titaVo);
				try {
					sClImmService.insert(tClImm, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0005", "?????????????????????" + e.getErrorMsg());
				}

				// ???????????????????????????????????????
				setClImmRankDetail(titaVo);

				// ???????????????
				if (iClCode1 == 1) {
					// ?????????????????????????????????
					tClBuilding = new ClBuilding();
					setClBuilding(titaVo);
					try {
						sClBuildingService.insert(tClBuilding, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0005", "?????????????????????????????????" + e.getErrorMsg());
					}
					// ???????????????????????????
					InsertClBuildingOwner(titaVo);
				} else {
					// ???????????????
					tClLand = new ClLand();
					// ??????????????????????????????
					setClLand(titaVo);
					try {
						sClLandService.insert(tClLand, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0005", "??????????????????????????????" + e.getErrorMsg());
					}
					// insert ?????????????????????
					InsertClLandOwner(titaVo);
				}

				if (iApplNo > 0) {
					List<HashMap<String, String>> ownerMap = new ArrayList<HashMap<String, String>>();
					for (int i = 1; i <= 20; i++) {
						// ?????????????????????????????????
						if (titaVo.getParam("OwnerId" + i) == null || titaVo.getParam("OwnerId" + i).trim().isEmpty()) {
							break;
						}

						String iOwnerId = titaVo.getParam("OwnerId" + i);

						CustMain custMain = sCustMainService.custIdFirst(iOwnerId, titaVo);

						if (custMain != null) {
							String custUKey = custMain.getCustUKey().trim();
							String relCode = titaVo.getParam("OwnerRelCode" + i).trim();
							HashMap<String, String> map = new HashMap<String, String>();
							map.put("OwnerCustUKey", custUKey);
							map.put("OwnerRelCode", relCode);
							ownerMap.add(map);
						}
					}
					clFacCom.insertClFac(titaVo, iClCode1, iClCode2, iClNo, iApplNo, ownerMap);

				} // if

			} else if (iFunCd == 2) {
				throw new LogicException("E0003", "???????????????"); // ?????????????????????
			}
		} else {
			if (iFunCd == 1) {
				throw new LogicException("E0002", "?????????????????????");

			}
			if (iFunCd == 2) {
				// ???????????????
				tClMain = sClMainService.holdById(ClMainId, titaVo);
				// ?????????
				ClMain beforeClMain = (ClMain) dataLog.clone(tClMain);
				// set
				setClMain(titaVo);
				// update
				try {
					tClMain = sClMainService.update2(tClMain, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0007", "???????????????" + e.getErrorMsg());
				}
				// ????????????????????????
				dataLog.setEnv(titaVo, beforeClMain, tClMain);
				dataLog.exec("???????????????????????????");

				// ????????????
				tClImm = sClImmService.holdById(ClImmId, titaVo);
				// ?????????
				ClImm beforeClImm = (ClImm) dataLog.clone(tClImm);
				// set
				setClImm(titaVo);
				// update
				try {
					tClImm = sClImmService.update2(tClImm, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0007", "?????????????????????" + e.getErrorMsg());
				}

				// ???????????????????????? ????????????
				dataLog.setEnv(titaVo, beforeClImm, tClImm);
				dataLog.exec("?????????????????????????????????");

				// ???????????????????????????????????????
				deleteClImmRankDetail(titaVo);

				setClImmRankDetail(titaVo);

				// ???????????????
				if (iClCode1 == 1) {
					// ?????????????????????????????????
					tClBuilding = sClBuildingService.holdById(clBuildingId, titaVo);
					if (tClBuilding == null) {
						tClBuilding = new ClBuilding();
						// set
						setClBuilding(titaVo);

						// update
						try {
							sClBuildingService.insert(tClBuilding, titaVo);
						} catch (DBException e) {
							throw new LogicException("E0005", "???????????????" + e.getErrorMsg());
						}
					} else {
						// ?????????
						ClBuilding beforeClBuilding = (ClBuilding) dataLog.clone(tClBuilding);

						// set
						setClBuilding(titaVo);
						// update
						try {
							tClBuilding = sClBuildingService.update2(tClBuilding, titaVo);
						} catch (DBException e) {
							throw new LogicException("E0007", "???????????????" + e.getErrorMsg());
						}
						// ????????????????????????
						dataLog.setEnv(titaVo, beforeClBuilding, tClBuilding);
						dataLog.exec("???????????????????????????????????????");
					}

					// delete ??????????????????
					deleteClBuildingOwner(titaVo);
					// insert?????????????????????
					InsertClBuildingOwner(titaVo);
				}
				// ???????????????
				else {
					// ??????????????????????????????
					tClLand = sClLandService.holdById(clLandId, titaVo);

					if (tClLand == null) {
						tClLand = new ClLand();
						// ??????????????????????????????
						setClLand(titaVo);
						try {
							tClLand = sClLandService.insert(tClLand, titaVo);
						} catch (DBException e) {
							throw new LogicException("E0005", "???????????????????????????" + e.getErrorMsg());
						}
					} else {
						// ?????????
						ClLand beforeClLand = (ClLand) dataLog.clone(tClLand);
						// ??????????????????????????????
						setClLand(titaVo);
						try {
							tClLand = sClLandService.update2(tClLand, titaVo);
						} catch (DBException e) {
							throw new LogicException("E0007", "???????????????????????????" + e.getErrorMsg());
						}
						// ????????????????????????
						dataLog.setEnv(titaVo, beforeClLand, tClLand);
						dataLog.exec("???????????????????????????????????????");
					}

					// delete ??????????????????
					deleteClLandOwner(titaVo);
					// insert ?????????????????????
					InsertClLandOwner(titaVo);
				}

				if (iApplNo > 0) {
					List<HashMap<String, String>> ownerMap = new ArrayList<HashMap<String, String>>();
					for (int i = 1; i <= 20; i++) {
						// ?????????????????????????????????
						if (titaVo.getParam("OwnerId" + i) == null || titaVo.getParam("OwnerId" + i).trim().isEmpty()) {
							break;
						}

						String iOwnerId = titaVo.getParam("OwnerId" + i);

						CustMain custMain = sCustMainService.custIdFirst(iOwnerId, titaVo);
						if (custMain != null) {
							String custUKey = custMain.getCustUKey().trim();
							String relCode = titaVo.getParam("OwnerRelCode" + i).trim();

							HashMap<String, String> map = new HashMap<String, String>();
							map.put("OwnerCustUKey", custUKey);
							map.put("OwnerRelCode", relCode);
							ownerMap.add(map);

							FacMain facMain = sFacMainService.facmApplNoFirst(iApplNo, titaVo);
							if (facMain == null) {
								throw new LogicException(titaVo, "E0001", "????????????:" + iApplNo);
							}

							ClOwnerRelationId clOwnerRelationId = new ClOwnerRelationId();
							clOwnerRelationId.setCreditSysNo(facMain.getCreditSysNo());
							clOwnerRelationId.setCustNo(facMain.getCustNo());
							clOwnerRelationId.setOwnerCustUKey(custUKey);

							ClOwnerRelation clOwnerRelation = sClOwnerRelationService.holdById(clOwnerRelationId,
									titaVo);

							if (clOwnerRelation == null) {
								clOwnerRelation = new ClOwnerRelation();
								clOwnerRelation.setClOwnerRelationId(clOwnerRelationId);
								clOwnerRelation.setOwnerRelCode(relCode);
								try {
									sClOwnerRelationService.insert(clOwnerRelation, titaVo);
								} catch (DBException e) {
									throw new LogicException("E0005", "??????????????????????????????????????????" + e.getErrorMsg());
								}
							} else {
								clOwnerRelation.setOwnerRelCode(relCode);
								try {
									sClOwnerRelationService.update(clOwnerRelation, titaVo);
								} catch (DBException e) {
									throw new LogicException("E0007", "??????????????????????????????????????????" + e.getErrorMsg());
								}
							} // else

						} // if
					} // for

					if (this.isEloan) { // eloan ????????????????????????????????????????????? 2022.3.10
						ClFacId clFacId = new ClFacId();
						clFacId.setClCode1(iClCode1);
						clFacId.setClCode2(iClCode2);
						clFacId.setClNo(iClNo);
						clFacId.setApproveNo(iApplNo);
						ClFac clFac = sClFacService.findById(clFacId, titaVo);
						if (clFac == null) {
							clFacCom.insertClFac(titaVo, iClCode1, iClCode2, iClNo, iApplNo, ownerMap);
						}
					}

				} // if

			} else
			/* ?????? */
			if (iFunCd == 4) {

				Slice<ClFac> slClFac = sClFacService.clNoEq(iClCode1, iClCode2, iClNo, 0, Integer.MAX_VALUE, titaVo);
				List<ClFac> lClFac = slClFac == null ? null : slClFac.getContent();
//				???????????????????????????????????????,??????????????????
				if (lClFac != null) {
					throw new LogicException(titaVo, "E2073", "??????????????? = " + iClCode1 + "-" + iClCode2 + "-" + iClNo); // ??????????????????????????????????????????
				}
				// ?????????????????????
				tClImm = sClImmService.holdById(ClImmId, titaVo);
				if (tClImm == null) {
					throw new LogicException("E0004", "?????????????????????");
				}
				try {
					sClImmService.delete(tClImm, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0008", "?????????????????????" + e.getErrorMsg());
				}

				// ???????????????????????????????????????
				deleteClImmRankDetail(titaVo);

				// ???????????????
				if (iClCode1 == 1) {
					// ?????????????????????????????????
					tClBuilding = sClBuildingService.holdById(clBuildingId, titaVo);
					if (tClBuilding == null) {
						throw new LogicException("E0004", "?????????????????????????????????");
					}
					try {
						sClBuildingService.delete(tClBuilding, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0008", "?????????????????????" + e.getErrorMsg());
					}
					// delete ??????????????????
					deleteClBuildingOwner(titaVo);
				}
				// ???????????????
				else {
					tClLand = sClLandService.holdById(clLandId, titaVo);
					if (tClLand == null) {
						throw new LogicException("E0004", "??????????????????????????????");
					}
					try {
						sClLandService.delete(tClLand, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0008", "?????????????????????" + e.getErrorMsg());
					}
					// delete ??????????????????
					deleteClLandOwner(titaVo);
				}

				deleteClParkingType(titaVo);

				// ???????????????
				tClMain = sClMainService.holdById(ClMainId, titaVo);
				if (tClMain == null) {
					throw new LogicException("E0004", "???????????????");
				}
				try {
					sClMainService.delete(tClMain, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0008", "???????????????" + e.getErrorMsg());
				}

			}
		}
		this.totaVo.putParam("OClNo", finalClNo);

		this.addList(this.totaVo);
		return this.sendList();
	}

	// ?????????????????????
	private int getBuildingClNo(TitaVo titaVo) throws LogicException {
//	 ??????????????????????????????
//	  1. ??????????????????
//	         ????????????+??????????????????(???)
//	  2. ??????????????????
//		  ????????????+??????????????????(???)
		int clNo = 0;
//		Slice<ClBuilding> slClBuilding = sClBuildingService.findBdLocationEq(bdLocation, this.index, Integer.MAX_VALUE, titaVo);
		Slice<ClBuilding> slClBuilding = sClBuildingService.findBdLocationEq(titaVo.getParam("CityCode").trim(),
				titaVo.getParam("AreaCode").trim(), titaVo.getParam("IrCode").trim(), titaVo.getParam("BdNo1").trim(),
				titaVo.getParam("BdNo2").trim(), this.index, Integer.MAX_VALUE, titaVo);
		List<ClBuilding> lClBuilding = slClBuilding == null ? null : slClBuilding.getContent();
		if (lClBuilding != null) {
			for (ClBuilding cl : lClBuilding) {
				if (cl.getClCode1() == iClCode1 && cl.getClCode2() == iClCode2) {
					if (checkBuildingOwner(cl.getClNo(), titaVo)) {
						clNo = cl.getClNo();
						break;
					}
				}
			}
		}
		return clNo;
	}

	// ????????????????????????????????????
	private boolean checkBuildingOwner(int clNo, TitaVo titaVo) throws LogicException {
// ???????????????????????????????????????
		boolean isSameOwner = false;
		Slice<ClBuildingOwner> slClBuildingOwner = sClBuildingOwnerService.clNoEq(iClCode1, iClCode2, clNo, this.index,
				this.limit, titaVo);
		lClBuildingOwner = slClBuildingOwner == null ? null : slClBuildingOwner.getContent();
		if (lClBuildingOwner != null) {
			for (ClBuildingOwner o : lClBuildingOwner) {
				if (isSameOwner) {
					break;
				}
				for (int i = 1; i <= 10; i++) {
					// ?????????????????????????????????

					if (titaVo.getParam("OwnerId" + i) == null || titaVo.getParam("OwnerId" + i).trim().isEmpty()) {
						break;
					}
					CustMain custMain = sCustMainService.findById(o.getOwnerCustUKey(), titaVo);
					if (custMain != null && custMain.getCustId().equals(titaVo.getParam("OwnerId" + i))) {
						isSameOwner = true;
					}
				}
			}
		}
		return isSameOwner;
	}

	// ?????????????????????
	private int getLandClNo(TitaVo titaVo) throws LogicException {
		int clNo = 0;
		Slice<ClLand> slClLand = sClLandService.findLandLocationEq(titaVo.getParam("CityCodeB").trim(),
				titaVo.getParam("AreaCodeB").trim(), titaVo.getParam("IrCodeB").trim(),
				titaVo.getParam("LandNo1").trim(), titaVo.getParam("LandNo2").trim(), this.index, Integer.MAX_VALUE,
				titaVo);
		List<ClLand> lClLand = slClLand == null ? null : slClLand.getContent();
		if (lClLand != null) {
			for (ClLand cl : lClLand) {
				if (cl.getClCode1() == iClCode1 && cl.getClCode2() == iClCode2) {
					if (checkLandOwner(cl.getClNo(), titaVo)) {
						clNo = cl.getClNo();
						break;
					}
				}
			}
		}
		return clNo;
	}

	// ????????????????????????????????????
	private boolean checkLandOwner(int clNo, TitaVo titaVo) throws LogicException {
// ???????????????????????????????????????
		boolean isSameOwner = false;
		Slice<ClLandOwner> slClLandOwner = sClLandOwnerService.LandSeqEq(iClCode1, iClCode2, clNo, 000, this.index,
				this.limit, titaVo);
		lClLandOwner = slClLandOwner == null ? null : slClLandOwner.getContent();
		if (lClBuildingOwner != null) {
			for (ClLandOwner o : lClLandOwner) {
				if (isSameOwner) {
					break;
				}
				for (int i = 1; i <= 10; i++) {
					// ?????????????????????????????????
					if (titaVo.getParam("OwnerId" + i) == null || titaVo.getParam("OwnerId" + i).trim().isEmpty()) {
						break;
					}
					CustMain custMain = sCustMainService.findById(o.getOwnerCustUKey(), titaVo);
					if (custMain != null && custMain.getCustId().equals(titaVo.getParam("OwnerId" + i))) {
						isSameOwner = true;
						break;
					}
				}
			}
		}
		return isSameOwner;
	}

	private void setClMain(TitaVo titaVo) throws LogicException {
		tClMain = new ClMain();
		this.info("ClMainId2 = " + ClMainId);
		tClMain.setClMainId(ClMainId);
		tClMain.setClCode1(iClCode1);
		tClMain.setClCode2(iClCode2);
		tClMain.setClNo(iClNo);
		tClMain.setClTypeCode(titaVo.getParam("ClTypeCode"));
		tClMain.setCityCode(titaVo.getParam("CityCode"));
		tClMain.setAreaCode(titaVo.getParam("AreaCode"));
		tClMain.setClStatus(titaVo.getParam("ClStatus"));
		tClMain.setEvaDate(parse.stringToInteger(titaVo.getParam("EvaDate")));
		tClMain.setEvaAmt(parse.stringToBigDecimal(titaVo.getParam("EvaAmt")));
		tClMain.setSynd(titaVo.getParam("Synd"));
		tClMain.setSyndCode(titaVo.getParam("SyndCode"));
		tClMain.setDispPrice(parse.stringToBigDecimal(titaVo.getParam("DispPrice")));
		tClMain.setDispDate(parse.stringToInteger(titaVo.getParam("DispDate")));

		// ?????????????????????
		BigDecimal shareTotal = new BigDecimal(0);
		BigDecimal shareCompAmt = BigDecimal.ZERO;
		BigDecimal wkEvaAmt = parse.stringToBigDecimal(titaVo.getParam("EvaAmt"));
		BigDecimal wkEvaNetWorth = parse.stringToBigDecimal(titaVo.getParam("EvaNetWorth"));

		// ????????????????????????????????????,?????????????????????.
		if (wkEvaNetWorth.compareTo(BigDecimal.ZERO) > 0) {
			shareCompAmt = wkEvaNetWorth;
		} else {
			shareCompAmt = wkEvaAmt;
		}

		// ????????????
		BigDecimal loanToValue = new BigDecimal(titaVo.getParam("LoanToValue"));

		this.info("L2411 shareCompAmt = " + shareCompAmt.toString());
		this.info("L2411 LoanToValue = " + loanToValue.toString());
		this.info("L2411 SettingAmt = " + parse.stringToBigDecimal(titaVo.getParam("SettingAmt")).toString());

//		"1.???""????????????""?????????""????????????""?????????""????????????"")*????????????(????????????????????????)
//		2.??????????????????????????????????????????????????????
//		3.???????????????/???????????????(??????????????????????????????????????????)"
//		??????????????????0?????????????????????
		if (loanToValue.compareTo(BigDecimal.ZERO) == 0) {
			shareTotal = shareCompAmt;
		} else {
			shareTotal = shareCompAmt.multiply(loanToValue).divide(new BigDecimal(100)).setScale(0,
					BigDecimal.ROUND_HALF_UP);
		}
		if (parse.stringToBigDecimal(titaVo.getParam("SettingAmt")).compareTo(shareTotal) < 0) {
			shareTotal = parse.stringToBigDecimal(titaVo.getParam("SettingAmt"));
		}
		if ("1".equals(titaVo.getParam("ClStat")) || "2".equals(titaVo.getParam("SettingStat"))) {
			tClMain.setShareTotal(BigDecimal.ZERO);
		} else {
			tClMain.setShareTotal(shareTotal);
		}

		this.info("L2411 shareTotal = " + shareTotal.toString());
		tClMain.setShareTotal(shareTotal);
	}

	private void setClImm(TitaVo titaVo) throws LogicException {
		tClImm = new ClImm();
		tClImm.setClImmId(ClImmId);
		tClImm.setClCode1(iClCode1);
		tClImm.setClCode2(iClCode2);
		tClImm.setClNo(iClNo);
		tClImm.setEvaNetWorth(parse.stringToBigDecimal(titaVo.getParam("EvaNetWorth")));
		tClImm.setLVITax(parse.stringToBigDecimal(titaVo.getParam("LVITax")));
		tClImm.setRentEvaValue(parse.stringToBigDecimal(titaVo.getParam("RentEvaValue")));
		tClImm.setRentPrice(parse.stringToBigDecimal(titaVo.getParam("RentPrice")));
		tClImm.setOwnershipCode(titaVo.getParam("OwnershipCode"));
		tClImm.setClStat(titaVo.getParam("ClStat"));
		tClImm.setMtgCode(titaVo.getParam("MtgCode"));
		tClImm.setMtgCheck(titaVo.getParam("MtgCheck"));
		tClImm.setMtgLoan(titaVo.getParam("MtgLoan"));
		tClImm.setMtgPledge(titaVo.getParam("MtgPledge"));
		tClImm.setAgreement(titaVo.getParam("Agreement"));
		tClImm.setEvaCompanyCode(titaVo.getParam("EvaCompany"));
		tClImm.setLimitCancelDate(parse.stringToInteger(titaVo.getParam("LimitCancelDate")));
		tClImm.setClCode(titaVo.getParam("ClCode"));
		tClImm.setLoanToValue(parse.stringToBigDecimal(titaVo.getParam("LoanToValue")));
		tClImm.setOtherOwnerTotal(parse.stringToBigDecimal(titaVo.getParam("OtherOwnerTotal")));
		tClImm.setCompensationCopy(titaVo.getParam("CompensationCopy"));
		tClImm.setBdRmk(titaVo.getParam("BdRmk"));
		tClImm.setMtgReasonCode(titaVo.getParam("MtgReasonCode"));
		tClImm.setReceivedDate(parse.stringToInteger(titaVo.getParam("ReceivedDate")));
		tClImm.setReceivedNo(titaVo.getParam("ReceivedNo"));
		tClImm.setCancelDate(parse.stringToInteger(titaVo.getParam("CancelDate")));
		tClImm.setCancelNo(titaVo.getParam("CancelNo"));
		tClImm.setSettingDate(parse.stringToInteger(titaVo.getParam("SettingDate")));
		tClImm.setSettingStat(titaVo.getParam("SettingStat"));
		tClImm.setSettingAmt(parse.stringToBigDecimal(titaVo.getParam("SettingAmt")));
		tClImm.setClaimDate(parse.stringToInteger(titaVo.getParam("ClaimDate")));
		tClImm.setSettingSeq(titaVo.getParam("SettingSeq"));
	}

	private void setClImmRankDetail(TitaVo titaVo) throws LogicException {

		int times = parse.stringToInteger(iSettingSeq);

		for (int i = 1; i <= times; i++) {

			ClImmRankDetailId.setClCode1(iClCode1);
			ClImmRankDetailId.setClCode2(iClCode2);
			ClImmRankDetailId.setClNo(iClNo);
			ClImmRankDetailId.setSettingSeq("" + i);

			tClImmRankDetail = new ClImmRankDetail();

			tClImmRankDetail.setClImmRankDetailId(ClImmRankDetailId);

			tClImmRankDetail.setClCode1(iClCode1);
			tClImmRankDetail.setClCode2(iClCode2);
			tClImmRankDetail.setClNo(iClNo);
			tClImmRankDetail.setSettingSeq("" + i);

			if (i > 1) { // ??????????????????
				tClImmRankDetail.setFirstCreditor(titaVo.getParam("FirstCreditor" + (i - 1)));
				tClImmRankDetail.setFirstAmt(parse.stringToBigDecimal(titaVo.getParam("FirstAmt" + (i - 1))));
			}

			try {
				sClImmRankDetailService.insert(tClImmRankDetail, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "??????????????????????????????????????????" + e.getErrorMsg());
			}
		}
	}

	private void deleteClImmRankDetail(TitaVo titaVo) throws LogicException {

		Slice<ClImmRankDetail> slClImmRankDetail = sClImmRankDetailService.clNoEq(iClCode1, iClCode2, iClNo, this.index,
				this.limit, titaVo);
		lClImmRankDetail = slClImmRankDetail == null ? null : slClImmRankDetail.getContent();
		if (lClImmRankDetail != null && lClImmRankDetail.size() > 0) {
			try {
				sClImmRankDetailService.deleteAll(lClImmRankDetail, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0008", "??????????????????????????????????????????" + e.getErrorMsg());
			}
		}
	}

	private void setClBuilding(TitaVo titaVo) throws LogicException {

		tClBuilding.setClBuildingId(clBuildingId);
		tClBuilding.setClCode1(iClCode1);
		tClBuilding.setClCode2(iClCode2);
		tClBuilding.setClNo(iClNo);
		tClBuilding.setCityCode(titaVo.getParam("CityCode"));
		tClBuilding.setAreaCode(titaVo.getParam("AreaCode"));
		tClBuilding.setIrCode(titaVo.getParam("IrCode"));
		tClBuilding.setRoad(titaVo.getParam("Road"));
		tClBuilding.setSection(titaVo.getParam("Section"));
		tClBuilding.setAlley(titaVo.getParam("Alley"));
		tClBuilding.setLane(titaVo.getParam("Lane"));
		tClBuilding.setNum(titaVo.getParam("Num"));
		tClBuilding.setNumDash(titaVo.getParam("NumDash"));
		tClBuilding.setFloor(titaVo.getParam("Floor"));
		tClBuilding.setFloorDash(titaVo.getParam("FloorDash"));
		tClBuilding.setBdNo1(titaVo.getParam("BdNo1"));
		tClBuilding.setBdNo2(titaVo.getParam("BdNo2"));
		tClBuilding.setBdLocation(bdLocation);
	}

	private void setClLand(TitaVo titaVo) throws LogicException {
		tClLand.setClLandId(clLandId);
		tClLand.setClCode1(iClCode1);
		tClLand.setClCode2(iClCode2);
		tClLand.setClNo(iClNo);
		tClLand.setCityCode(titaVo.getParam("CityCodeB"));
		tClLand.setAreaCode(titaVo.getParam("AreaCodeB"));
		tClLand.setIrCode(titaVo.getParam("IrCodeB"));
		tClLand.setLandNo1(titaVo.getParam("LandNo1"));
		tClLand.setLandNo2(titaVo.getParam("LandNo2"));
		tClLand.setLandLocation(titaVo.getParam("LandLocation"));
	}

	// insert ?????????????????????
	private void InsertClBuildingOwner(TitaVo titaVo) throws LogicException {

		for (int i = 1; i <= 10; i++) {
			// ?????????????????????????????????
			if (titaVo.getParam("OwnerId" + i) == null || titaVo.getParam("OwnerId" + i).trim().isEmpty()) {
				break;
			}
			tClBuildingOwner = new ClBuildingOwner();
			String iOwnerId = (titaVo.getParam("OwnerId" + i));
			clBuildingOwnerId = new ClBuildingOwnerId();
			clBuildingOwnerId.setClCode1(iClCode1);
			clBuildingOwnerId.setClCode2(iClCode2);
			clBuildingOwnerId.setClNo(iClNo);
			String iRelCode = (titaVo.getParam("OwnerRelCode" + i));

			CustMain custMain = sCustMainService.custIdFirst(iOwnerId, titaVo);
			// ID????????????,?????????????????????CustMain
			if (custMain == null) {
				String Ukey = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
				custMain = new CustMain();
				custMain.setCustUKey(Ukey);
				custMain.setCustId(iOwnerId);
				custMain.setCustName(titaVo.getParam("OwnerName" + i));
				custMain.setDataStatus(1);
				custMain.setTypeCode(2);
				try {
					sCustMainService.insert(custMain, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0005", "??????????????????");
				}
			}
			clBuildingOwnerId.setOwnerCustUKey(custMain.getCustUKey());

			tClBuildingOwner.setClBuildingOwnerId(clBuildingOwnerId);
			tClBuildingOwner.setClCode1(iClCode1);
			tClBuildingOwner.setClCode2(iClCode2);
			tClBuildingOwner.setClNo(iClNo);
			tClBuildingOwner.setOwnerRelCode(titaVo.getParam("OwnerRelCode" + i));
			tClBuildingOwner.setOwnerPart(parse.stringToBigDecimal(titaVo.getParam("OwnerPart" + i)));
			tClBuildingOwner.setOwnerTotal(parse.stringToBigDecimal(titaVo.getParam("OwnerTotal" + i)));
			try {
				sClBuildingOwnerService.insert(tClBuildingOwner, titaVo);
			} catch (DBException e) {
				throw new LogicException("E2009", "??????????????????????????????" + e.getErrorMsg());
			}
			if (iApplNo > 0) {
				ClOwnerRelationId clOwnerRelationId = new ClOwnerRelationId();
				clOwnerRelationId.setCreditSysNo(tFacMain.getCreditSysNo());
				clOwnerRelationId.setCustNo(tFacMain.getCustNo());
				clOwnerRelationId.setOwnerCustUKey(custMain.getCustUKey());
				ClOwnerRelation clOwnerRelation = sClOwnerRelationService.holdById(clOwnerRelationId, titaVo);

				if (clOwnerRelation == null) {
					clOwnerRelation = new ClOwnerRelation();
					clOwnerRelation.setClOwnerRelationId(clOwnerRelationId);
					clOwnerRelation.setOwnerRelCode(iRelCode);
					try {
						sClOwnerRelationService.insert(clOwnerRelation, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0005", "??????????????????????????????????????????" + e.getErrorMsg());
					}
				} else {
					clOwnerRelation.setOwnerRelCode(iRelCode);
					try {
						sClOwnerRelationService.update(clOwnerRelation, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0007", "??????????????????????????????????????????" + e.getErrorMsg());
					}
				}
			}
		}
	}

	// delete ?????????????????????
	private void deleteClBuildingOwner(TitaVo titaVo) throws LogicException {
		Slice<ClBuildingOwner> slClBuildingOwner = sClBuildingOwnerService.clNoEq(iClCode1, iClCode2, iClNo, this.index,
				this.limit, titaVo);
		lClBuildingOwner = slClBuildingOwner == null ? null : slClBuildingOwner.getContent();
		if (lClBuildingOwner != null && lClBuildingOwner.size() > 0) {
			try {
				sClBuildingOwnerService.deleteAll(lClBuildingOwner, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0008", "??????????????????????????????" + e.getErrorMsg());
			}
		}
	}

	private void InsertClLandOwner(TitaVo titaVo) throws LogicException {
		for (int i = 1; i <= 10; i++) {

			// ?????????????????????????????????
			if (titaVo.getParam("OwnerId" + i) == null || titaVo.getParam("OwnerId" + i).trim().isEmpty()) {
				break;
			}
			String iOwnerId = titaVo.getParam("OwnerId" + i);
			tClLandOwner = new ClLandOwner();
			clLandOwnerId = new ClLandOwnerId();
			clLandOwnerId.setClCode1(iClCode1);
			clLandOwnerId.setClCode2(iClCode2);
			clLandOwnerId.setClNo(iClNo);
			clLandOwnerId.setLandSeq(0);

			CustMain custMain = sCustMainService.custIdFirst(iOwnerId, titaVo);
			// ID????????????,?????????????????????CustMain
			if (custMain == null) {
				String Ukey = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
				custMain = new CustMain();
				custMain.setCustUKey(Ukey);
				custMain.setCustId(iOwnerId);
				custMain.setCuscCd(iOwnerId.length() == 8 ? "2" : "1");
				custMain.setCustName(titaVo.getParam("OwnerName" + i));
				custMain.setDataStatus(1);
				custMain.setTypeCode(2);
				try {
					sCustMainService.insert(custMain, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0005", "??????????????????");
				}
			}
			clLandOwnerId.setOwnerCustUKey(custMain.getCustUKey());

			tClLandOwner.setClLandOwnerId(clLandOwnerId);
			tClLandOwner.setClCode1(iClCode1);
			tClLandOwner.setClCode2(iClCode2);
			tClLandOwner.setClNo(iClNo);
			tClLandOwner.setLandSeq(0);
			tClLandOwner.setOwnerRelCode(titaVo.getParam("OwnerRelCode" + i));
			tClLandOwner.setOwnerPart(parse.stringToBigDecimal(titaVo.getParam("OwnerPart" + i)));
			tClLandOwner.setOwnerTotal(parse.stringToBigDecimal(titaVo.getParam("OwnerTotal" + i)));
			try {
				sClLandOwnerService.insert(tClLandOwner, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "??????????????????????????????" + e.getErrorMsg());
			}
		}
	}

	// delete ?????????????????????
	private void deleteClLandOwner(TitaVo titaVo) throws LogicException {
		Slice<ClLandOwner> slClLandOwner = sClLandOwnerService.LandSeqEq(iClCode1, iClCode2, iClNo, 000, this.index,
				this.limit, titaVo);
		lClLandOwner = slClLandOwner == null ? null : slClLandOwner.getContent();

		if (lClLandOwner != null && lClLandOwner.size() > 0) {
			try {
				sClLandOwnerService.deleteAll(lClLandOwner, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0008", "??????????????????????????????" + e.getErrorMsg());
			}
		}
	}

	// delete ??????
	private void deleteClParkingType(TitaVo titaVo) throws LogicException {
		this.info("L2415 deleteClParkingType");
		Slice<ClParkingType> slClParkingType = sClParkingTypeService.clNoEq(iClCode1, iClCode2, iClNo, 0,
				Integer.MAX_VALUE);
		List<ClParkingType> lClParkingType = slClParkingType == null ? null : slClParkingType.getContent();
		if (lClParkingType != null) {
			try {
				sClParkingTypeService.deleteAll(lClParkingType);
			} catch (DBException e) {
				throw new LogicException("E0008", "???????????????????????????" + e.getErrorMsg());
			}
		}
	}

	private String getBdLocation(TitaVo titaVo) throws LogicException {

		String result = "";

		String CityCode = titaVo.getParam("CityCode").trim();

		this.info("L2415 getBdLocation CityCode = " + CityCode);

		String CityItem = "";
		CdCity tCdCity = sCdCityService.findById(CityCode, titaVo);
		if (tCdCity == null) {
			throw new LogicException("E0003", "??????????????????" + CityCode);
		}

		CityItem = tCdCity.getCityItem().trim();

		String AreaCode = titaVo.getParam("AreaCode").trim();

		String AreaItem = "";
		CdArea tCdArea = sCdAreaService.findById(new CdAreaId(CityCode, AreaCode), titaVo);
		if (tCdArea == null) {
			throw new LogicException("E0003", "???????????????????????????" + CityCode + "-" + AreaCode);
		}

		AreaItem = tCdArea.getAreaItem().trim();

		String IrCode = titaVo.getParam("IrCode").trim();

		String IrItem = "";
		CdLandSection tCdLandSection = sCdLandSectionService.findById(new CdLandSectionId(CityCode, AreaCode, IrCode),
				titaVo);
		if (tCdLandSection == null) {
			throw new LogicException("E0003", "???????????????" + CityCode + "-" + AreaCode);
		}

		IrItem = tCdLandSection.getIrItem().trim();

		String Road = titaVo.getParam("Road").trim();
		String Section = titaVo.getParam("Section").trim();
		String Alley = titaVo.getParam("Alley").trim();
		String Lane = titaVo.getParam("Lane").trim();
		String Num = titaVo.getParam("Num").trim();
		String NumDash = titaVo.getParam("NumDash").trim();
		String Floor = titaVo.getParam("Floor").trim();
		String FloorDash = titaVo.getParam("FloorDash").trim();

		if (!CityItem.isEmpty()) {
			result += CityItem;
		}
		if (!AreaItem.isEmpty()) {
			result += AreaItem;
		}
		if (!IrItem.isEmpty()) {
			result += IrItem;
		}
		if (!Road.isEmpty()) {
			result += Road;
		}
		if (!Section.isEmpty()) {
			result += Section + "???";
		}
		if (!Alley.isEmpty()) {
			result += Alley + "???";
		}
		if (!Lane.isEmpty()) {
			result += Lane + "???";
		}
		if (!Num.isEmpty()) {
			result += Num + "???";
		}
		if (!NumDash.isEmpty()) {
			result += "???" + NumDash + ",";
		}
		if (!Floor.isEmpty()) {
			result += Floor + "???";
		}
		if (!FloorDash.isEmpty()) {
			result += "???" + FloorDash;
		}
		this.info("L2415 getBdLocation result = " + result);
		return result;
	}

}