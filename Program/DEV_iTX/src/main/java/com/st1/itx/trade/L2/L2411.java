package com.st1.itx.trade.L2;

import java.math.BigDecimal;
import java.util.ArrayList;
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
import com.st1.itx.db.domain.ClOwnerRelation;
import com.st1.itx.db.domain.ClOwnerRelationId;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.service.CdAreaService;
import com.st1.itx.db.service.CdCityService;
import com.st1.itx.db.service.CdClService;
import com.st1.itx.db.service.ClBuildingOwnerService;
import com.st1.itx.db.service.ClBuildingService;
import com.st1.itx.db.service.ClFacService;
import com.st1.itx.db.service.ClImmService;
import com.st1.itx.db.service.ClLandOwnerService;
import com.st1.itx.db.service.ClLandService;
import com.st1.itx.db.service.ClMainService;
import com.st1.itx.db.service.ClOwnerRelationService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacCaseApplService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.ClFacCom;
import com.st1.itx.util.common.GSeqCom;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;


@Service("L2411")
@Scope("prototype")
/**
 * 不動產擔保品資料登錄
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2411 extends TradeBuffer {

	@Autowired
	public DataLog dataLog;

	/* DB服務注入 */
	@Autowired
	public ClImmService sClImmService;
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
	public ClOwnerRelationService sClOwnerRelationService;

	/* 自動取號 */
	@Autowired
	GSeqCom gGSeqCom;
	@Autowired
	public ClFacCom clFacCom;
	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	/* work area */
	private int iFunCd;

	// 擔保品代號1
	private int iClCode1;
	// 擔保品代號2
	private int iClCode2;
	// 擔保品編號
	private int iClNo;
	// 核准號碼
	private int iApplNo;
	// 宣告

	private String finalClNo;
	private ClMainId ClMainId = new ClMainId();
	private ClImmId ClImmId = new ClImmId();
	private ClMain tClMain = new ClMain();
	private ClImm tClImm = new ClImm();
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
	private String bdLocation;
	private boolean isEloan = false;
	private FacMain tFacMain;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2411 ");
		this.totaVo.init(titaVo);
		if (titaVo.get("ClStat") == null) {
			titaVo.putParam("ClStat", "0"); // 擔保品狀態 0: 正常
		}
		if (titaVo.get("CancelDate") == null) {
			titaVo.putParam("CancelDate", "0"); // 撤銷日期
		}
		if (titaVo.get("CancelNo") == null) {
			titaVo.putParam("CancelNo", ""); // 撤銷案號
		}
		// new table PK

		// tita
		// 功能
		iFunCd = parse.stringToInteger(titaVo.getParam("FunCd"));

		// 核准號碼
		iApplNo = parse.stringToInteger(titaVo.getParam("ApplNo"));
		// 擔保品代號1
		iClCode1 = parse.stringToInteger(titaVo.getParam("ClCode1"));
		// 擔保品代號2
		iClCode2 = parse.stringToInteger(titaVo.getParam("ClCode2"));
		// 擔保品編號
		iClNo = parse.stringToInteger(titaVo.getParam("ClNo"));
		// 宣告
		finalClNo = StringUtils.leftPad(String.valueOf(iClNo), 7, "0");
		// isEloan
		if (titaVo.isEloan() || "ELTEST".equals(titaVo.getTlrNo())) {
			this.isEloan = true;
		}

		// 建物門牌
		if (iClCode1 == 1) {
			bdLocation = getBdLocation(titaVo);
		}

//		 擔保品編號唯一性規則
//		  1. 房地擔保品：
//		         建物門牌+建物所有權人(多)
//		  2. 土地擔保品：
//			  土地座落+土地所有權人(多)
		
		if (iFunCd == 1 && iClNo == 0) {
			// 擔保品編號唯一性規則
			if (iClCode1 == 1) {
				iClNo = getBuildingClNo(titaVo);

			} else {
				iClNo = getLandClNo(titaVo);
			}
			if (iClNo > 0) {
				finalClNo = StringUtils.leftPad(String.valueOf(iClNo), 7, "0");
				// from ELOAN 主檔變更
				if (this.isEloan) {
					iFunCd = 2;
				} else {
					iFunCd = 2;
					if (iClCode1 == 1) {
						this.totaVo.setWarnMsg("與擔保品編號：" + iClNo + "，建物門牌之建物所有權人相同，修改擔保品內容");
					} else {
						this.totaVo.setWarnMsg("與擔保品編號：" + iClNo + "，土地座落之土地所有權人相同，修改擔保品內容");
					}
				}
			}

		}

		// 新增時取號 進擔保品代號檔取最後使用碼+1
		if (iFunCd == 1 && iClNo == 0) {
			this.info("新增時取號");

			String clCode = StringUtils.leftPad(String.valueOf(iClCode1), 2, "0") + StringUtils.leftPad(String.valueOf(iClCode2), 2, "0");

			iClNo = gGSeqCom.getSeqNo(0, 0, "L2", clCode, 9999999, titaVo);

			finalClNo = StringUtils.leftPad(String.valueOf(iClNo), 7, "0");

		}
		this.info("擔保品編號 = " + iClNo);

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

		if (iApplNo > 0) { // 核准編號大於0才去做
			tFacMain = sFacMainService.facmApplNoFirst(iApplNo, titaVo);
			if (tFacMain == null) {
				throw new LogicException("E2019", "核准號碼 = " + iApplNo);
			}
		}
		
		// 查擔保品主檔
		tClMain = sClMainService.findById(ClMainId, titaVo);

		if (tClMain == null) {
			if (iFunCd == 1) {

				// 擔保品主檔
				this.info("ClMainId1 = " + ClMainId);

				setClMain(titaVo);
				try {
					sClMainService.insert(tClMain, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0005", "擔保品主檔" + e.getErrorMsg());
				}

				// 擔保品不動產檔
				setClImm(titaVo);
				try {
					sClImmService.insert(tClImm, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0005", "擔保品不動產檔" + e.getErrorMsg());
				}
				// 房地擔保品
				if (iClCode1 == 1) {
					// 擔保品不動產建物檔主檔
					tClBuilding = new ClBuilding();
					setClBuilding(titaVo);
					try {
						sClBuildingService.insert(tClBuilding, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0005", "擔保品不動產建物檔主檔" + e.getErrorMsg());
					}
					// 新增建物所有權人檔
					InsertClBuildingOwner(titaVo);
				} else {
					// 土地擔保品
					tClLand = new ClLand();
					// 擔保品不動產土地主檔
					setClLand(titaVo);
					try {
						sClLandService.insert(tClLand, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0005", "擔保品不動產土地主檔" + e.getErrorMsg());
					}
					// insert 土地所有權人檔
					InsertClLandOwner(titaVo);
				}
				// 依核准號碼建立一筆額度與擔保品關聯檔
				if (iApplNo > 0) { // 核准編號大於0才去做

					ClFacId clFacId = new ClFacId();
					clFacId.setClCode1(iClCode1);
					clFacId.setClCode2(iClCode2);
					clFacId.setClNo(iClNo);
					clFacId.setApproveNo(iApplNo);
					ClFac tClFac = sClFacService.findById(clFacId, titaVo);
					
					// 新增資料重複
					if (tClFac != null) {
						throw new LogicException("E0005", "額度與擔保品關聯檔"); // 新增資料時，發生錯誤
					} else {
						tClFac = new ClFac();
						clFacId = new ClFacId();
						clFacId.setClCode1(iClCode1);
						clFacId.setClCode2(iClCode2);
						clFacId.setClNo(iClNo);
						clFacId.setApproveNo(iApplNo);
						tClFac.setApproveNo(iApplNo);
						tClFac.setClCode1(iClCode1);
						tClFac.setClCode2(iClCode2);
						tClFac.setClNo(iClNo);
						tClFac.setClFacId(clFacId);
						tClFac.setCustNo(tFacMain.getCustNo());
						tClFac.setFacmNo(tFacMain.getFacmNo());
						tClFac.setMainFlag("Y");
						tClFac.setShareAmt(BigDecimal.ZERO);
						tClFac.setFacShareFlag(0);
						tClFac.setOriSettingAmt(parse.stringToBigDecimal(titaVo.getParam("SettingAmt")));

						// insert
						try {
							sClFacService.insert(tClFac, titaVo);
						} catch (DBException e) {
							throw new LogicException("E0005", "額度與擔保品關聯檔" + e.getErrorMsg()); // 新增資料時，發生錯誤
						}

						// 額度與擔保品關聯檔變動處理
						clFacCom.changeClFac(iApplNo, titaVo);
					}
				} // if

			} else if (iFunCd == 2) {
				throw new LogicException("E0003", "擔保品主檔"); // 修改資料不存在
			}
		} else {
			if (iFunCd == 1) {
				throw new LogicException("E0002", "擔保品不動產檔");

			}
			if (iFunCd == 2) {
				// 擔保品主檔
				tClMain = sClMainService.holdById(ClMainId, titaVo);
				// 變更前
				ClMain beforeClMain = (ClMain) dataLog.clone(tClMain);
				// set
				setClMain(titaVo);
				// update
				try {
					tClMain = sClMainService.update2(tClMain, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0007", "擔保品主檔" + e.getErrorMsg());
				}
				// 紀錄變更前變更後
				dataLog.setEnv(titaVo, beforeClMain, tClMain);
				dataLog.exec();

				// 不動產檔
				tClImm = sClImmService.holdById(ClImmId, titaVo);
				// 變更前
				ClImm beforeClImm = (ClImm) dataLog.clone(tClImm);
				// set
				setClImm(titaVo);
				// update
				try {
					tClImm = sClImmService.update2(tClImm, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0007", "擔保品不動產檔" + e.getErrorMsg());
				}
				// 紀錄變更前變更後 不動產檔
				dataLog.setEnv(titaVo, beforeClImm, tClImm);
				dataLog.exec();

				// 房地擔保品
				if (iClCode1 == 1) {
					// 擔保品不動產建物檔主檔
					tClBuilding = sClBuildingService.holdById(clBuildingId, titaVo);
					if (tClBuilding == null) {
						tClBuilding = new ClBuilding();
						// set
						setClBuilding(titaVo);

						// update
						try {
							sClBuildingService.insert(tClBuilding, titaVo);
						} catch (DBException e) {
							throw new LogicException("E0005", "擔保品主檔" + e.getErrorMsg());
						}
					} else {
						// 變更前
						ClBuilding beforeClBuilding = (ClBuilding) dataLog.clone(tClBuilding);

						// set
						setClBuilding(titaVo);
						// update
						try {
							tClBuilding = sClBuildingService.update2(tClBuilding, titaVo);
						} catch (DBException e) {
							throw new LogicException("E0007", "擔保品主檔" + e.getErrorMsg());
						}
						// 紀錄變更前變更後
						dataLog.setEnv(titaVo, beforeClBuilding, tClBuilding);
						dataLog.exec();
					}

					// delete 建物所有權人
					deleteClBuildingOwner(titaVo);
					// insert建物所有權人檔
					InsertClBuildingOwner(titaVo);
				}
				// 土地擔保品
				else {
					// 擔保品不動產土地主檔
					tClLand = sClLandService.holdById(clLandId, titaVo);

					if (tClLand == null) {
						tClLand = new ClLand();
						// 擔保品不動產土地主檔
						setClLand(titaVo);
						try {
							tClLand = sClLandService.insert(tClLand, titaVo);
						} catch (DBException e) {
							throw new LogicException("E0005", "擔保品不動產土地檔" + e.getErrorMsg());
						}
					} else {
						// 變更前
						ClLand beforeClLand = (ClLand) dataLog.clone(tClLand);
						// 擔保品不動產土地主檔
						setClLand(titaVo);
						try {
							tClLand = sClLandService.update2(tClLand, titaVo);
						} catch (DBException e) {
							throw new LogicException("E0007", "擔保品不動產土地檔" + e.getErrorMsg());
						}
						// 紀錄變更前變更後
						dataLog.setEnv(titaVo, beforeClLand, tClLand);
						dataLog.exec();
					}

					// delete 土地所有權人
					deleteClLandOwner(titaVo);
					// insert 土地所有權人檔
					InsertClLandOwner(titaVo);
				}
			} else
			/* 刪除 */
			if (iFunCd == 4) {

				Slice<ClFac> slClFac = sClFacService.clNoEq(iClCode1, iClCode2, iClNo, 0, Integer.MAX_VALUE, titaVo);
				List<ClFac> lClFac = slClFac == null ? null : slClFac.getContent();
//				該額度與擔保品關聯不可刪除,請先解除關聯
				if (lClFac != null) {
					throw new LogicException(titaVo, "E2073", "擔保品號碼 = " + iClCode1 + "-" + iClCode2 + "-" + iClNo); // 該額度與擔保品關聯，不可刪除
				}
				// 擔保品不動產檔
				tClImm = sClImmService.holdById(ClImmId, titaVo);
				if (tClImm == null) {
					throw new LogicException("E0004", "擔保品不動產檔");
				}
				try {
					sClImmService.delete(tClImm, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0008", "擔保品不動產檔" + e.getErrorMsg());
				}
				// 房地擔保品
				if (iClCode1 == 1) {
					// 擔保品不動產建物檔主檔
					tClBuilding = sClBuildingService.holdById(clBuildingId, titaVo);
					if (tClBuilding == null) {
						throw new LogicException("E0004", "擔保品不動產建物檔主檔");
					}
					try {
						sClBuildingService.delete(tClBuilding, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0008", "擔保品建物主檔" + e.getErrorMsg());
					}
					// delete 建物所有權人
					deleteClBuildingOwner(titaVo);
				}
				// 土地擔保品
				else {
					tClLand = sClLandService.holdById(clLandId, titaVo);
					if (tClLand == null) {
						throw new LogicException("E0004", "擔保品不動產土地主檔");
					}
					try {
						sClLandService.delete(tClLand, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0008", "擔保品土地主檔" + e.getErrorMsg());
					}
					// delete 土地所有權人
					deleteClLandOwner(titaVo);
				}

				// 擔保品主檔
				tClMain = sClMainService.holdById(ClMainId, titaVo);
				if (tClMain == null) {
					throw new LogicException("E0004", "擔保品主檔");
				}
				try {
					sClMainService.delete(tClMain, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0008", "擔保品主檔" + e.getErrorMsg());
				}

			}
		}
		this.totaVo.putParam("OClNo", finalClNo);

		this.addList(this.totaVo);
		return this.sendList();
	}

	// 房地擔保品編號
	private int getBuildingClNo(TitaVo titaVo) throws LogicException {
//	 擔保品編號唯一性規則
//	  1. 房地擔保品：
//	         建物門牌+建物所有權人(多)
//	  2. 土地擔保品：
//		  土地座落+土地所有權人(多)
		int clNo = 0;
		Slice<ClBuilding> slClBuilding = sClBuildingService.findBdLocationEq(bdLocation, this.index, Integer.MAX_VALUE, titaVo);
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

	// 判斷建物所有權人是否相同
	private boolean checkBuildingOwner(int clNo, TitaVo titaVo) throws LogicException {
// 有一個有權人相同即視為相同
		boolean isSameOwner = false;
		Slice<ClBuildingOwner> slClBuildingOwner = sClBuildingOwnerService.clNoEq(iClCode1, iClCode2, clNo, this.index, this.limit, titaVo);
		lClBuildingOwner = slClBuildingOwner == null ? null : slClBuildingOwner.getContent();
		if (lClBuildingOwner != null) {
			for (ClBuildingOwner o : lClBuildingOwner) {
				if (isSameOwner) {
					break;
				}
				for (int i = 1; i <= 10; i++) {
					// 若該筆無資料就離開迴圈

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

	// 土地擔保品編號
	private int getLandClNo(TitaVo titaVo) throws LogicException {
		int clNo = 0;
		Slice<ClLand> slClLand = sClLandService.findLandLocationEq(titaVo.getParam("LandLocation"), this.index, Integer.MAX_VALUE, titaVo);
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

	// 判斷土地所有權人是否相同
	private boolean checkLandOwner(int clNo, TitaVo titaVo) throws LogicException {
// 有一個有權人相同即視為相同
		boolean isSameOwner = false;
		Slice<ClLandOwner> slClLandOwner = sClLandOwnerService.LandSeqEq(iClCode1, iClCode2, clNo, 000, this.index, this.limit, titaVo);
		lClLandOwner = slClLandOwner == null ? null : slClLandOwner.getContent();
		if (lClBuildingOwner != null) {
			for (ClLandOwner o : lClLandOwner) {
				if (isSameOwner) {
					break;
				}
				for (int i = 1; i <= 10; i++) {
					// 若該筆無資料就離開迴圈
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

		// 計算可分配金額
		BigDecimal shareTotal = new BigDecimal(0);
		BigDecimal shareCompAmt = BigDecimal.ZERO;
		BigDecimal wkEvaAmt = parse.stringToBigDecimal(titaVo.getParam("EvaAmt"));
		BigDecimal wkEvaNetWorth = parse.stringToBigDecimal(titaVo.getParam("EvaNetWorth"));

		// 評估淨值有值時擺評估淨值,否則擺鑑估總值.
		if (wkEvaNetWorth.compareTo(BigDecimal.ZERO) > 0) {
			shareCompAmt = wkEvaNetWorth;
		} else {
			shareCompAmt = wkEvaAmt;
		}

		// 貸放成數
		BigDecimal loanToValue = new BigDecimal(titaVo.getParam("LoanToValue"));

		this.info("L2411 shareCompAmt = " + shareCompAmt.toString());
		this.info("L2411 LoanToValue = " + loanToValue.toString());
		this.info("L2411 SettingAmt = " + parse.stringToBigDecimal(titaVo.getParam("SettingAmt")).toString());

//		"1.若""評估淨值""有值取""評估淨值""否則取""鑑估總值"")*貸放成數(四捨五入至個位數)
//		2.若設定金額低於可分配金額則為設定金額
//		3.擔保品塗銷/解除設定時(該筆擔保品的可分配金額設為零)"

		shareTotal = shareCompAmt.multiply(loanToValue).divide(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_HALF_UP);
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
		tClImm.setFirstCreditor(titaVo.getParam("FirstCreditor"));
		tClImm.setFirstAmt(parse.stringToBigDecimal(titaVo.getParam("FirstAmt")));
		tClImm.setSecondCreditor(titaVo.getParam("SecondCreditor"));
		tClImm.setSecondAmt(parse.stringToBigDecimal(titaVo.getParam("SecondAmt")));
		tClImm.setThirdCreditor(titaVo.getParam("ThirdCreditor"));
		tClImm.setThirdAmt(parse.stringToBigDecimal(titaVo.getParam("ThirdAmt")));
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

	// insert 建物所有權人檔
	private void InsertClBuildingOwner(TitaVo titaVo) throws LogicException {

		for (int i = 1; i <= 10; i++) {
			// 若該筆無資料就離開迴圈
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
			// ID不存在時,新增一筆資料在CustMain
			if (custMain == null) {
				String Ukey = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
				custMain = new CustMain();
				custMain.setCustUKey(Ukey);
				custMain.setCustId(iOwnerId);
				custMain.setCustName(titaVo.getParam("OwnerName" + i));
				custMain.setDataStatus(1);

				try {
					sCustMainService.insert(custMain, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0005", "客戶資料主檔");
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
				throw new LogicException("E2009", "擔保品不動產土地檔" + e.getErrorMsg());
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
						throw new LogicException("E0005", "擔保品所有權人與授信戶關係檔" + e.getErrorMsg());
					}
				} else {
					clOwnerRelation.setOwnerRelCode(iRelCode);
					try {
						sClOwnerRelationService.update(clOwnerRelation, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0007", "擔保品所有權人與授信戶關係檔" + e.getErrorMsg());
					}
				}
			}
		}
	}

	// delete 建物所有權人檔
	private void deleteClBuildingOwner(TitaVo titaVo) throws LogicException {
		Slice<ClBuildingOwner> slClBuildingOwner = sClBuildingOwnerService.clNoEq(iClCode1, iClCode2, iClNo, this.index, this.limit, titaVo);
		lClBuildingOwner = slClBuildingOwner == null ? null : slClBuildingOwner.getContent();
		if (lClBuildingOwner != null && lClBuildingOwner.size() > 0) {
			try {
				sClBuildingOwnerService.deleteAll(lClBuildingOwner, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0008", "擔保品土地所有權人檔" + e.getErrorMsg());
			}
		}
	}

	private void InsertClLandOwner(TitaVo titaVo) throws LogicException {
		for (int i = 1; i <= 10; i++) {

			// 若該筆無資料就離開迴圈
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
			// ID不存在時,新增一筆資料在CustMain
			if (custMain == null) {
				String Ukey = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
				custMain = new CustMain();
				custMain.setCustUKey(Ukey);
				custMain.setCustId(iOwnerId);
				custMain.setCustName(titaVo.getParam("OwnerName" + i));
				custMain.setDataStatus(1);

				try {
					sCustMainService.insert(custMain, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0005", "客戶資料主檔");
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
				throw new LogicException("E0005", "擔保品土地所有權人檔" + e.getErrorMsg());
			}
		}
	}

	// delete 土地所有權人檔
	private void deleteClLandOwner(TitaVo titaVo) throws LogicException {
		Slice<ClLandOwner> slClLandOwner = sClLandOwnerService.LandSeqEq(iClCode1, iClCode2, iClNo, 000, this.index, this.limit, titaVo);
		lClLandOwner = slClLandOwner == null ? null : slClLandOwner.getContent();

		if (lClLandOwner != null && lClLandOwner.size() > 0) {
			try {
				sClLandOwnerService.deleteAll(lClLandOwner, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0008", "擔保品土地所有權人檔" + e.getErrorMsg());
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
			throw new LogicException("E0003", "地區別代碼檔" + CityCode);
		}

		CityItem = tCdCity.getCityItem().trim();
		String AreaCode = titaVo.getParam("AreaCode").trim();

		String AreaItem = "";
		CdArea tCdArea = sCdAreaService.findById(new CdAreaId(CityCode, AreaCode), titaVo);
		if (tCdArea == null) {
			throw new LogicException("E0003", "縣市與鄉鎮區對照檔" + CityCode + "-" + AreaCode);
		}

		AreaItem = tCdArea.getAreaItem().trim();

		String Road = titaVo.getParam("Road").trim();
		String Section = titaVo.getParam("Section").trim();
		String Alley = titaVo.getParam("Alley").trim();
		String Lane = titaVo.getParam("Lane").trim();
		String Num = titaVo.getParam("Num").trim();
		String NumDash = titaVo.getParam("NumDash").trim();
		String Floor = titaVo.getParam("Floor").trim();
		String FloorDash = titaVo.getParam("FloorDash").trim();
		String BdNo1 = titaVo.getParam("BdNo1").trim();
		String BdNo2 = titaVo.getParam("BdNo2").trim();

		if (!CityItem.isEmpty()) {
			result += CityItem;
		}
		if (!AreaItem.isEmpty()) {
			result += AreaItem;
		}
		if (!Road.isEmpty()) {
			result += Road;
		}
		if (!Section.isEmpty()) {
			result += Section + "段";
		}
		if (!Alley.isEmpty()) {
			result += Alley + "巷";
		}
		if (!Lane.isEmpty()) {
			result += Lane + "弄";
		}
		if (!Num.isEmpty()) {
			result += Num + "號";
		}
		if (!NumDash.isEmpty()) {
			result += "之" + NumDash + ",";
		}
		if (!Floor.isEmpty()) {
			result += Floor + "樓";
		}
		if (!FloorDash.isEmpty()) {
			result += "之" + FloorDash;
		}
		if (!BdNo1.isEmpty()) {
			result += "建號" + BdNo1;
		}
		if (!BdNo2.isEmpty()) {
			result += "-" + BdNo2;
		}
		this.info("L2415 getBdLocation result = " + result);
		return result;
	}

}