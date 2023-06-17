package com.st1.itx.trade.L2;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

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
import com.st1.itx.db.domain.ClStock;
import com.st1.itx.db.domain.ClStockId;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.FacCaseAppl;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.service.ClFacService;
import com.st1.itx.db.service.ClImmService;
import com.st1.itx.db.service.ClMainService;
import com.st1.itx.db.service.ClMovablesService;
import com.st1.itx.db.service.ClOtherService;
import com.st1.itx.db.service.ClStockService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacCaseApplService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.domain.ClOwnerRelation;
import com.st1.itx.db.domain.ClOwnerRelationId;
import com.st1.itx.db.service.ClOwnerRelationService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.ClFacCom;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L2417")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2417 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public FacMainService sFacMainService;
	/* DB服務注入 */
	@Autowired
	public FacCaseApplService sFacCaseApplService;
	/* DB服務注入 */
	@Autowired
	public ClFacService sClFacService;
	/* DB服務注入 */
	@Autowired
	public ClMainService sClMainService;

	@Autowired
	public CustMainService sCustMainService;

	@Autowired
	public ClOwnerRelationService sClOwnerRelationService;

	@Autowired
	public DataLog dataLog;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Autowired
	public ClImmService sClImmService;
	@Autowired
	public ClStockService sClStockService;
	@Autowired
	public ClOtherService sClOtherService;
	@Autowired
	public ClMovablesService sClMovablesService;

	@Autowired
	public ClFacCom clFacCom;

	private boolean isEloan = false;

	int iFunCd;
	int iClCode1;
	int iClCode2;
	int iClNo;
	int iApproveNo;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2417 ");
		this.totaVo.init(titaVo);
		clFacCom.setTxBuffer(this.txBuffer);
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 100;

		// isEloan
		if (titaVo.isEloan() || "ELTEST".equals(titaVo.getTlrNo())) {
			this.isEloan = true;
		}

		// tita
		iFunCd = parse.stringToInteger(titaVo.getParam("FunCd"));
		iClCode1 = parse.stringToInteger(titaVo.getParam("ClCode1"));
		iClCode2 = parse.stringToInteger(titaVo.getParam("ClCode2"));
		iClNo = parse.stringToInteger(titaVo.getParam("ClNo"));
		iApproveNo = parse.stringToInteger(titaVo.getParam("ApproveNo"));

		// 檢查該擔保品編號是否存在擔保品主檔
		ClMain tClMain = new ClMain();

		ClMainId tClMainId = new ClMainId();

		tClMainId.setClCode1(iClCode1);
		tClMainId.setClCode2(iClCode2);
		tClMainId.setClNo(iClNo);

		tClMain = sClMainService.findById(tClMainId, titaVo);

		// 該擔保品編號不存在擔保品主檔 拋錯
		if (tClMain == null) {
			throw new LogicException("E2009", "L2417該擔保品編號不存在擔保品主檔");// 新增資料時，發生錯誤
		}

		ClFacId clFacId = new ClFacId();
		clFacId.setClCode1(iClCode1);
		clFacId.setClCode2(iClCode2);
		clFacId.setClNo(iClNo);
		clFacId.setApproveNo(iApproveNo);
		ClFac tClFac = new ClFac();

		// eloan 唯一性
		if (isEloan && iFunCd == 1) {
			// hold住資料
			tClFac = sClFacService.findById(clFacId, titaVo);
			if (tClFac != null) {
				iFunCd = 2;
			}
		}

		// FunCd = 1 新增
		if (iFunCd == 1) {

			FacCaseAppl tFacCaseAppl = new FacCaseAppl();
			// 查該核准號碼是否存在案件申請檔
			tFacCaseAppl = sFacCaseApplService.findById(iApproveNo, titaVo);

			// 該核准號碼不存在案件申請檔 拋錯
			if (tFacCaseAppl == null) {
				throw new LogicException("E2009", "L2417該核准號碼不存在案件申請檔");// 新增資料時，發生錯誤
			}

			if (!tFacCaseAppl.getProcessCode().equals("1")) {
				throw new LogicException("E2021", "L2417該核准號碼尚未核准");// 此核准號碼尚未核准
			}

			tClFac = sClFacService.findById(clFacId, titaVo);

			// 新增時該組擔保品號碼及核准編號已存在 擔保品與額度關聯檔 拋錯
			if (tClFac != null) {
				throw new LogicException("E0002", "擔保品與額度關聯檔");// 新增資料已存在
			} else {

				tClFac = new ClFac();

				// 新增塞table
				tClFac.setClFacId(clFacId);
				tClFac.setClCode1(iClCode1);
				tClFac.setClCode2(iClCode2);
				tClFac.setClNo(iClNo);
				tClFac.setApproveNo(iApproveNo);
				tClFac.setMainFlag("Y");
				BigDecimal settingAmt = BigDecimal.ZERO;
				BigDecimal evaNotWorth = BigDecimal.ZERO;
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
					evaNotWorth = tClImm.getEvaNetWorth();
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
					evaNotWorth = tClMain.getEvaAmt();
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
					evaNotWorth = tClMain.getEvaAmt();
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
					evaNotWorth = tClMain.getEvaAmt();
					break;
				}

				tClFac.setOriSettingAmt(settingAmt);
				tClFac.setOriEvaNotWorth(evaNotWorth);
				try {
					sClFacService.insert(tClFac, titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0005", "擔保品與額度關聯檔");// 新增資料時，發生錯誤
				}
			}

			updateClOwnerRelation(titaVo);

		} else if (iFunCd == 2) {// FunCd = 2 修改

			// hold住資料
			tClFac = sClFacService.holdById(clFacId, titaVo);

			if (tClFac == null) {
				throw new LogicException("E2003", "擔保品與額度關聯檔");
			}

//			deleteClOwnerRelation(titaVo);
			updateClOwnerRelation(titaVo);

		} else if (iFunCd == 4) {// FunCd = 4 刪除

			tClFac = sClFacService.holdById(clFacId, titaVo);
			if (tClFac == null) {
				throw new LogicException("E2003", "擔保品與額度關聯檔"); // 鎖定資料時，發生錯誤
			}

			try {
				sClFacService.delete(tClFac, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0008", "擔保品與額度關聯檔");// 刪除資料時，發生錯誤
			}

//			deleteClOwnerRelation(titaVo);
		}

		this.info("L2417 check MainFg ...");

		// 額度與擔保品關聯檔變動處理
		clFacCom.changeClFac(iApproveNo, titaVo);

		// end
		this.addList(this.totaVo);
		return this.sendList();
	}

	private void updateClOwnerRelation(TitaVo titaVo) throws LogicException {
		FacMain facMain = sFacMainService.facmApplNoFirst(iApproveNo, titaVo);
		if (facMain == null) {
			throw new LogicException(titaVo, "E0001", "核准號碼:" + iApproveNo);
		}

		if (isEloan) {
			setEloanOwner(facMain, titaVo);
		} else {
			for (int i = 1; i <= 20; i++) {
				String custUKey = titaVo.getParam("OwnerCustUKey" + i).trim();
				String relCode = titaVo.getParam("OwnerRelCode" + i).trim();
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
						// 變更前
						ClOwnerRelation beforeClOwnerRelation = (ClOwnerRelation) dataLog.clone(clOwnerRelation);

						clOwnerRelation.setOwnerRelCode(relCode);
						if (!relCode.equals(beforeClOwnerRelation.getOwnerRelCode())) {
							// 紀錄變更前變更後
							dataLog.setEnv(titaVo, beforeClOwnerRelation, clOwnerRelation);

							CustMain tCustMain = sCustMainService.findById(custUKey, titaVo);
							String CustName = "";
							if (tCustMain != null) {
								CustName = tCustMain.getCustName();
							}
							dataLog.exec("修改所有權人與授信戶關係:" + CustName);
						}

						try {
							sClOwnerRelationService.update(clOwnerRelation, titaVo);
						} catch (DBException e) {
							throw new LogicException("E0007", "擔保品所有權人與授信戶關係檔" + e.getErrorMsg());
						}
					}
				}

			}
		}

	}

	private void setEloanOwner(FacMain facMain, TitaVo titaVo) throws LogicException {

		Boolean flag = true;

		for (int i = 1; flag; i++) {

			if (!titaVo.getParam("OwnerId" + i).isEmpty() && titaVo.getParam("OwnerId" + i) != null) {

				CustMain custMain = sCustMainService.custIdFirst(titaVo.getParam("OwnerId" + i), titaVo);
				String Ukey = "";
				// 是否存在客戶主檔
				if (custMain == null) {
					Ukey = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
					custMain = new CustMain();
					custMain.setCustUKey(Ukey);
					custMain.setCustId(titaVo.getParam("OwnerId" + i));
					custMain.setCustName(titaVo.getParam("OwnerName" + i));
					custMain.setDataStatus(1);
					custMain.setTypeCode(2);
					if (titaVo.getParam("OwnerId" + i).length() == 8) {
						custMain.setCuscCd("2");
					} else {
						custMain.setCuscCd("1");
					}
					try {
						sCustMainService.insert(custMain, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0005", "客戶資料主檔");
					}
				} else {
					Ukey = custMain.getCustUKey();
				}

				if (!"".equals(Ukey)) {

					ClOwnerRelationId clOwnerRelationId = new ClOwnerRelationId();
					clOwnerRelationId.setCreditSysNo(facMain.getCreditSysNo());
					clOwnerRelationId.setCustNo(facMain.getCustNo());
					clOwnerRelationId.setOwnerCustUKey(titaVo.getParam("OwnerRelCode" + i));

					ClOwnerRelation clOwnerRelation = sClOwnerRelationService.holdById(clOwnerRelationId, titaVo);

					if (clOwnerRelation == null) {
						clOwnerRelation = new ClOwnerRelation();
						clOwnerRelation.setClOwnerRelationId(clOwnerRelationId);
						clOwnerRelation.setOwnerRelCode(titaVo.getParam("OwnerRelCode" + i));
						try {
							sClOwnerRelationService.insert(clOwnerRelation, titaVo);
						} catch (DBException e) {
							throw new LogicException("E0005", "擔保品所有權人與授信戶關係檔" + e.getErrorMsg());
						}
					} else {
						// 變更前
						ClOwnerRelation beforeClOwnerRelation = (ClOwnerRelation) dataLog.clone(clOwnerRelation);

						clOwnerRelation.setOwnerRelCode(titaVo.getParam("OwnerRelCode" + i));
						if (!titaVo.getParam("OwnerRelCode" + i).equals(beforeClOwnerRelation.getOwnerRelCode())) {
							// 紀錄變更前變更後
							dataLog.setEnv(titaVo, beforeClOwnerRelation, clOwnerRelation);

							CustMain tCustMain = sCustMainService.findById(titaVo.getParam("OwnerCustUKey" + i).trim(),
									titaVo);
							String CustName = "";
							if (tCustMain != null) {
								CustName = tCustMain.getCustName();
							}
							dataLog.exec("修改所有權人與授信戶關係:" + CustName);

						}

						try {
							sClOwnerRelationService.update(clOwnerRelation, titaVo);
						} catch (DBException e) {
							throw new LogicException("E0007", "擔保品所有權人與授信戶關係檔" + e.getErrorMsg());
						}
					}
				}

			} else {
				flag = false;
			}
		} // for

	}

}
