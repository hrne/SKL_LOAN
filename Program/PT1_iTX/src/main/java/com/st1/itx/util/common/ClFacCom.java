package com.st1.itx.util.common;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;
import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.ClFac;
import com.st1.itx.db.domain.ClFacId;
import com.st1.itx.db.domain.ClImm;
import com.st1.itx.db.domain.ClImmId;
import com.st1.itx.db.domain.ClMain;
import com.st1.itx.db.domain.ClMainId;
import com.st1.itx.db.domain.ClMovables;
import com.st1.itx.db.domain.ClMovablesId;
import com.st1.itx.db.domain.ClOther;
import com.st1.itx.db.domain.ClOtherId;
import com.st1.itx.db.domain.ClOwnerRelation;
import com.st1.itx.db.domain.ClOwnerRelationId;
import com.st1.itx.db.domain.ClStock;
import com.st1.itx.db.domain.ClStockId;
import com.st1.itx.db.domain.FacCaseAppl;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.FacShareAppl;
import com.st1.itx.db.service.ClFacService;
import com.st1.itx.db.service.ClImmService;
import com.st1.itx.db.service.ClMainService;
import com.st1.itx.db.service.ClMovablesService;
import com.st1.itx.db.service.ClOtherService;
import com.st1.itx.db.service.ClStockService;
import com.st1.itx.db.service.FacCaseApplService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.FacShareApplService;
import com.st1.itx.db.service.ClOwnerRelationService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;

/**
 * 額度與擔保品關聯檔處理 <BR>
 * 
 * @author Lai
 * @version 1.0.0
 */
@Component("clFacCom")
@Scope("prototype")
public class ClFacCom extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public FacMainService sFacMainService;
	/* DB服務注入 */
	@Autowired
	public ClFacService sClFacService;
	/* DB服務注入 */
	@Autowired
	public ClMainService sClMainService;
	@Autowired
	public FacShareApplService facShareApplService;

	@Autowired
	public ClImmService sClImmService;
	@Autowired
	public ClStockService sClStockService;
	@Autowired
	public ClOtherService sClOtherService;
	@Autowired
	public ClMovablesService sClMovablesService;
	@Autowired
	public FacCaseApplService sFacCaseApplService;
	@Autowired
	public ClOwnerRelationService sClOwnerRelationService;

	@Autowired
	public DataLog dataLog;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 新增一筆擔保品與額度關連檔
	 * 
	 * @param titaVo   titaVo
	 * @param iClCode1 擔保品代號1
	 * @param iClCode2 擔保品代號2
	 * @param iClNo    擔保品編號
	 * @param iApplNo  核准號碼
	 * @param ownerMap 擔保品提供人CustUKey＆與授信戶關係
	 * @throws LogicException LogicException
	 */
	public void insertClFac(TitaVo titaVo, int iClCode1, int iClCode2, int iClNo, int iApplNo, List<HashMap<String, String>> ownerMap) throws LogicException {
		ClFacId clFacId = new ClFacId();
		clFacId.setClCode1(iClCode1);
		clFacId.setClCode2(iClCode2);
		clFacId.setClNo(iClNo);
		clFacId.setApproveNo(iApplNo);

		// 查該核准號碼是否存在案件申請檔
		FacCaseAppl tFacCaseAppl = sFacCaseApplService.findById(iApplNo, titaVo);

		// 該核准號碼不存在案件申請檔 拋錯
		if (tFacCaseAppl == null) {
			throw new LogicException("E2009", "該核准號碼不存在案件申請檔");// 新增資料時，發生錯誤
		}

		if (!tFacCaseAppl.getProcessCode().equals("1")) {
			throw new LogicException("E2021", "該核准號碼尚未核准");// 此核准號碼尚未核准
		}

		ClFac clFac = sClFacService.findById(clFacId, titaVo);
		if (clFac != null) {
			throw new LogicException("E0002", "擔保品與額度關聯檔");// 新增資料已存在
		}

		ClFac tClFac = new ClFac();

		// 新增塞table
		tClFac.setClFacId(clFacId);
		tClFac.setClCode1(iClCode1);
		tClFac.setClCode2(iClCode2);
		tClFac.setClNo(iClNo);
		tClFac.setApproveNo(iApplNo);
		tClFac.setMainFlag("Y");

		BigDecimal settingAmt = settingAmt(titaVo, iClCode1, iClCode2, iClNo);

		tClFac.setOriSettingAmt(settingAmt);

		try {
			sClFacService.insert(tClFac, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0005", "擔保品與額度關聯檔");// 新增資料時，發生錯誤
		}

		// 擔保品提供人與授信戶關係
		if (ownerMap != null && ownerMap.size() > 0) {
			updateClOwnerRelation(titaVo, iApplNo, ownerMap);
		}

		// 額度與擔保品關聯檔變動處理
		changeClFac(iApplNo, titaVo);
	}

	/**
	 * 計算擔保品與額度關聯檔的 OriSettingAmt 設定金額
	 * 
	 * @param titaVo   titaVo
	 * @param iClCode1 擔保品代號1
	 * @param iClCode2 擔保品代號21
	 * @param iClNo    擔保品編號
	 * @return 設定金額
	 */

	public BigDecimal settingAmt(TitaVo titaVo, int iClCode1, int iClCode2, int iClNo) {
		BigDecimal settingAmt = BigDecimal.ZERO;

		// 依據擔保品代號1查不同Table
		switch (iClCode1) {
		case 1:
		case 2:
			ClImmId tClImmId = new ClImmId();
			tClImmId.setClCode1(iClCode1);
			tClImmId.setClCode2(iClCode2);
			tClImmId.setClNo(iClNo);
			ClImm tClImm = sClImmService.findById(tClImmId, titaVo);
			if (tClImm == null) {
				tClImm = new ClImm();
			}
			settingAmt = tClImm.getSettingAmt();
			break;
		case 3:
		case 4:
			ClStockId tClStockId = new ClStockId();
			tClStockId.setClCode1(iClCode1);
			tClStockId.setClCode2(iClCode2);
			tClStockId.setClNo(iClNo);
			ClStock tClStock = sClStockService.findById(tClStockId, titaVo);
			if (tClStock == null) {
				tClStock = new ClStock();
			}
			settingAmt = tClStock.getSettingBalance();
			break;
		case 5:
			ClOtherId tClOtherId = new ClOtherId();
			tClOtherId.setClCode1(iClCode1);
			tClOtherId.setClCode2(iClCode2);
			tClOtherId.setClNo(iClNo);
			ClOther tClOther = sClOtherService.findById(tClOtherId, titaVo);
			if (tClOther == null) {
				tClOther = new ClOther();
			}
			settingAmt = tClOther.getSettingAmt();

			break;
		case 9:
			ClMovablesId tClMovablesId = new ClMovablesId();
			tClMovablesId.setClCode1(iClCode1);
			tClMovablesId.setClCode2(iClCode2);
			tClMovablesId.setClNo(iClNo);
			ClMovables tClMovables = sClMovablesService.findById(tClMovablesId, titaVo);
			if (tClMovables == null) {
				tClMovables = new ClMovables();
			}
			settingAmt = tClMovables.getSettingAmt();
			break;
		}

		return settingAmt;
	}

	// 擔保品提供人與授信戶關係
	private void updateClOwnerRelation(TitaVo titaVo, int iApplNo, List<HashMap<String, String>> ownerMap) throws LogicException {
		FacMain facMain = sFacMainService.facmApplNoFirst(iApplNo, titaVo);
		if (facMain == null) {
			throw new LogicException(titaVo, "E0001", "核准號碼:" + iApplNo);
		}

		for (HashMap<String, String> map : ownerMap) {
			String custUKey = map.get("OwnerCustUKey").toString().trim();
			String relCode = map.get("OwnerRelCode").toString().trim();
			if (!"".equals(custUKey)) {

				ClOwnerRelationId clOwnerRelationId = new ClOwnerRelationId();
				clOwnerRelationId.setCreditSysNo(facMain.getCreditSysNo());
				clOwnerRelationId.setCustNo(facMain.getCustNo());
				clOwnerRelationId.setOwnerCustUKey(custUKey);

				ClOwnerRelation clOwnerRelation = sClOwnerRelationService.holdById(clOwnerRelationId, titaVo);

				if (clOwnerRelation == null) {
					clOwnerRelation = new ClOwnerRelation();
					clOwnerRelation.setClOwnerRelationId(clOwnerRelationId);
					clOwnerRelation.setOwnerRelCode(relCode);
					try {
						sClOwnerRelationService.insert(clOwnerRelation, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0005", "擔保品所有權人與授信戶關係檔" + e.getErrorMsg());
					}
				} else {
					clOwnerRelation.setOwnerRelCode(relCode);
					try {
						sClOwnerRelationService.update(clOwnerRelation, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0007", "擔保品所有權人與授信戶關係檔" + e.getErrorMsg());
					}
				}
			}

		}
	}

	/**
	 * 額度與擔保品關聯檔變動處理<BR>
	 * 1.更新額度與擔保品關聯檔的戶號、額度 <BR>
	 * 2.更新額度與擔保品關聯檔的主要擔保品記號 <BR>
	 * 3.更新額度主檔設定擔保品記號 4.自動建立共同借款人之額度與擔保品關聯檔
	 * 
	 * @param iApproveNo iApproveNo
	 * @param titaVo     titaVo
	 * @throws LogicException ...
	 */
	public void changeClFac(int iApproveNo, TitaVo titaVo) throws LogicException {
		this.info("ClFacCom change ....." + iApproveNo);
		int custNo = 0;
		int facmNo = 0;
		int mainFlagIdx = 0;
		String colSetFlag = "N";
		FacMain tFacMain = sFacMainService.facmApplNoFirst(iApproveNo, titaVo);
		if (tFacMain != null) {
			custNo = tFacMain.getCustNo();
			facmNo = tFacMain.getFacmNo();
		}
		// 共同借款人自動建立額度與擔保品關聯檔
		facShareAppl(iApproveNo, titaVo);

		// 以相同核准號碼中,可分配金額最高者為主要擔保品
		Slice<ClFac> slClFac = sClFacService.approveNoEq(iApproveNo, this.index, this.limit);
		List<ClFac> lClFac = slClFac == null ? null : new ArrayList<ClFac>(slClFac.getContent());
		BigDecimal wkShareTotal = BigDecimal.ZERO;
		// 有資料才需檢查
		if (lClFac != null) {
			for (int i = 0; i < lClFac.size(); i++) {
				ClMain tClMain = sClMainService.findById(new ClMainId(lClFac.get(i).getClCode1(), lClFac.get(i).getClCode2(), lClFac.get(i).getClNo()), titaVo);
				if (tClMain.getShareTotal().compareTo(wkShareTotal) > 0) {
					mainFlagIdx = i;
				}
			}
			for (int i = 0; i < lClFac.size(); i++) {
				if (i == mainFlagIdx) {
					lClFac.get(i).setMainFlag("Y");
				} else {
					lClFac.get(i).setMainFlag("N");
				}
				lClFac.get(i).setCustNo(custNo);
				lClFac.get(i).setFacmNo(facmNo);
			}
			colSetFlag = "Y";
			try {
				sClFacService.updateAll(lClFac);
			} catch (DBException e) {
				throw new LogicException("E0007", "擔保品與額度關聯檔");// 更新資料時，發生錯誤
			}
		}

		// 2.更新額度主檔設定擔保品記號
		if (tFacMain != null) {
			if (!tFacMain.getColSetFlag().equals(colSetFlag)) {
				tFacMain = sFacMainService.holdById(tFacMain, titaVo);
				tFacMain.setColSetFlag(colSetFlag);
				try {
					sFacMainService.update(tFacMain);
				} catch (DBException e) {
					throw new LogicException("E0007", "核准號碼 = " + iApproveNo);// 更新資料時，發生錯誤
				}
			}
		}

	}

	// 共同借款人自動建立額度與擔保品關聯檔
	private void facShareAppl(int iApproveNo, TitaVo titaVo) throws LogicException {
		FacShareAppl tFacShareAppl = facShareApplService.findById(iApproveNo, titaVo);
		if (tFacShareAppl == null) {
			return;
		}
		// 共同借款人全部
		Slice<FacShareAppl> slFacShareAppl = facShareApplService.findMainApplNo(tFacShareAppl.getMainApplNo(), 0, Integer.MAX_VALUE, titaVo);
		// 登錄者為主核准號碼(主額度)，刪除其他(非主額度)自動建立的額度與擔保品關聯檔
		if (iApproveNo == tFacShareAppl.getMainApplNo()) {
			for (FacShareAppl facShareAppl : slFacShareAppl.getContent()) {
				if (facShareAppl.getApplNo() != facShareAppl.getMainApplNo()) {
					this.info("ClFac 1 deleteAll " + facShareAppl.getApplNo());
					Slice<ClFac> slClFac = sClFacService.approveNoEq(facShareAppl.getApplNo(), 0, Integer.MAX_VALUE);
					if (slClFac != null) {
						try {
							sClFacService.deleteAll(slClFac.getContent(), titaVo);
						} catch (DBException e) {
							throw new LogicException(titaVo, "E0005", "擔保品與額度關聯檔");// 新增資料時，發生錯誤
						}
					}
				}
			}
		}
		// 登錄者非主核准號碼，刪除該核准號碼下額度與擔保品關聯檔
		else {
			this.info("ClFac 2  deleteAll " + iApproveNo);
			Slice<ClFac> slClFac = sClFacService.approveNoEq(iApproveNo, this.index, this.limit);
			if (slClFac != null) {
				try {
					sClFacService.deleteAll(slClFac.getContent(), titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0005", "擔保品與額度關聯檔");// 新增資料時，發生錯誤
				}
			}
		}

		// 以主核准號碼建立全部共同借款人下額度與擔保品關聯檔
		Slice<ClFac> slClFacMain = sClFacService.approveNoEq(tFacShareAppl.getMainApplNo(), 0, Integer.MAX_VALUE);
		if (slClFacMain != null) {
			for (FacShareAppl facShareAppl : slFacShareAppl.getContent()) {
				for (ClFac clFac : slClFacMain.getContent()) {
					if (facShareAppl.getApplNo() != facShareAppl.getMainApplNo()) {
						shareApplClFac(clFac, facShareAppl, titaVo);
					}
				}
			}
		}
		// 更新戶號及額度編號
		upDateFacShareApplFacmNo(iApproveNo, titaVo);
	}

	// 共同借款人自動建立
	private void shareApplClFac(ClFac clFac, FacShareAppl facShareAppl, TitaVo titaVo) throws LogicException {
		this.info("ClFac insert " + facShareAppl.toString() + clFac.toString());
		ClFacId tClFacId = new ClFacId();
		ClFac tClFac = (ClFac) dataLog.clone(clFac);
		tClFacId.setApproveNo(facShareAppl.getApplNo());
		tClFacId.setClCode1(clFac.getClCode1());
		tClFacId.setClCode2(clFac.getClCode2());
		tClFacId.setClNo(clFac.getClNo());
		tClFac.setClFacId(tClFacId);
		tClFac.setCustNo(facShareAppl.getCustNo());
		tClFac.setFacmNo(facShareAppl.getFacmNo());
		try {
			sClFacService.insert(tClFac, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0005", "擔保品與額度關聯檔" + tClFacId.toString());// 新增資料時，發生錯誤
		}
	}

	// 更新共同借款人戶號額度編號
	private void upDateFacShareApplFacmNo(int iApproveNo, TitaVo titaVo) throws LogicException {
		this.info("upDateFacshareApplFacmNo ... " + iApproveNo);
		// 更新此核准號碼共同借款人的戶號核准號碼
		FacMain nFacMain = sFacMainService.facmApplNoFirst(iApproveNo, titaVo);
		if (nFacMain != null) {
			FacShareAppl updaFacShareAppl = facShareApplService.holdById(iApproveNo, titaVo);
			updaFacShareAppl.setCustNo(nFacMain.getCustNo());
			updaFacShareAppl.setFacmNo(nFacMain.getFacmNo());
			try {
				facShareApplService.update(updaFacShareAppl, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005", "共同借款人檔" + updaFacShareAppl.toString());// 新增資料時，發生錯誤
			}
		}

	}
}