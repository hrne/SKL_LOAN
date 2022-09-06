package com.st1.itx.trade.L2;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CustDataCtrl;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.FacCaseAppl;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.FacShareAppl;
import com.st1.itx.db.service.CdGseqService;
import com.st1.itx.db.service.CustDataCtrlService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacCaseApplService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.FacShareApplService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.GSeqCom;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/*
 * L2111 案件申請登錄
 * a.申請號碼:新增時由電腦產生,營業日之民國年(2位)+5位之流水號
 * b.若為團體戶件,則須輸入團體戶之統一編號
 * c.案件核准後,只可修改專辦,協辦, 核決主管,介紹人及駐區資料
 */

/**
 * L2111 案件申請登錄
 * 
 * @author iza
 * @version 1.0.0
 */
@Service("L2111")
@Scope("prototype")
public class L2111 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public FacCaseApplService facCaseApplService;
	@Autowired
	public CustMainService custMainService;
	@Autowired
	public CdGseqService cdGseqService;
	@Autowired
	public FacShareApplService facShareApplService;
	@Autowired
	public FacMainService facMainService;
	@Autowired
	public CustDataCtrlService sCustDataCtrlService;
	@Autowired
	public DataLog iDataLog;

	@Autowired
	Parse parse;
	@Autowired
	GSeqCom gGSeqCom;
	@Autowired
	DateUtil dDateUtil;

	// input area
	private TitaVo titaVo = new TitaVo();
	private int iFuncCode;
	private int iApplNo;
	private String iCustId;
	private String iGroupId;

	// work area
	private FacCaseAppl tFacCaseAppl = new FacCaseAppl();
	FacShareAppl tFacShareAppl = new FacShareAppl();
	FacMain tFacMain = new FacMain();
	private String wkCustUkey = "";
	private String wkGroupUkey = "";
	private int iCustNo;
	private boolean isEloan = false;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2111 ");

		this.totaVo.init(titaVo);
		this.titaVo = titaVo;

		// isEloan
		if (titaVo.isEloan() || "ELTEST".equals(titaVo.getTlrNo())) {
			this.isEloan = true;
		}

		iApplNo = this.parse.stringToInteger(titaVo.getParam("ApplNo"));
		iCustId = titaVo.getParam("CustId").trim();
		iGroupId = titaVo.getParam("GroupId").trim();

		CustMain tCustMain = custMainService.custIdFirst(iCustId);
		if (tCustMain == null) {
			throw new LogicException(titaVo, "E2003", "客戶資料主檔" + iCustId); // 查無資料
		}
		wkCustUkey = tCustMain.getCustUKey();
		if (!iGroupId.isEmpty()) {
			tCustMain = custMainService.custIdFirst(iGroupId);
			if (tCustMain == null) {
				throw new LogicException(titaVo, "E2003", "客戶資料主檔" + iGroupId); // 查無資料
			}
			wkGroupUkey = tCustMain.getCustUKey();
		}

		// 取得輸入資料
		if (this.isEloan) {
			iFuncCode = 1;

			// 申請記號 ApplMark
			// 1:滿五年自動寫入(案件申請自動刪除)
			iCustNo = tCustMain.getCustNo();

			CustDataCtrl tCustDataCtrl = new CustDataCtrl();

			tCustDataCtrl = sCustDataCtrlService.findById(iCustNo);
			int iApplMark = 0;
			if (tCustDataCtrl != null) {
				iApplMark = tCustDataCtrl.getApplMark();
				if (iApplMark == 1) {
					try {

						this.info(" L2703 deletetCustDataCtrlLog : " + tCustDataCtrl);

						if (tCustDataCtrl != null) {
							sCustDataCtrlService.delete(tCustDataCtrl);
						}
					} catch (DBException e) {
						throw new LogicException(titaVo, "E0008", e.getErrorMsg());
					}
				} // if
			} // if

		} else {
			iFuncCode = this.parse.stringToInteger(titaVo.getParam("FuncCode"));
		}

		// 檢查輸入資料
		if (!(iFuncCode >= 1 && iFuncCode <= 5)) {
			throw new LogicException(titaVo, "E2004", ""); // 功能選擇錯誤
		}

		// 更新案件申請檔
		int WkTbsYy = this.txBuffer.getTxCom().getTbsdy() / 10000;
		int wkApplNo = iApplNo;

		switch (iFuncCode) {
		case 1: // 新增

			// 需確認eloan是否有送
//			if (!isEloan) {
//				String IsSuspectedCheckType = titaVo.get("IsSuspectedCheckType");
//				if (IsSuspectedCheckType != null && "Y".equals(IsSuspectedCheckType)) {
//					CustMain custMain = custMainService.holdById(tCustMain, titaVo);
//					if (custMain == null) {
//						throw new LogicException(titaVo, "E2003", "客戶資料主檔" + iGroupId); // 查無資料
//					}
//					// 變更前
//					CustMain beforeCustMain = (CustMain) iDataLog.clone(custMain);
//
//					custMain.setIsSuspected(titaVo.getParam("IsSuspected"));
//					custMain.setIsSuspectedCheck(titaVo.getParam("IsSuspectedCheck"));
//					custMain.setIsSuspectedCheckType(titaVo.getParam("IsSuspectedCheckType"));
//
//					// 搬值
//
//					try {
//						custMain = custMainService.update2(custMain, titaVo);
//					} catch (DBException e) {
//						throw new LogicException(titaVo, "E0007", "客戶主檔" + e.getErrorMsg()); // 新增資料時，發生錯誤
//					}
//					// 紀錄變更前變更後
//					iDataLog.setEnv(titaVo, beforeCustMain, custMain);
//					iDataLog.exec("修改客戶主檔資料");
//				}
//
//			}

		case 3: // 拷貝
			// 新增時由電腦產生,營業日之民國年(2位)+5位之流水號
			wkApplNo = gGSeqCom.getSeqNo(WkTbsYy * 10000, 1, "L2", "0002", 99999, titaVo);
			wkApplNo = (WkTbsYy % 100) * 100000 + wkApplNo;
			tFacCaseAppl = new FacCaseAppl();
			tFacCaseAppl.setApplNo(wkApplNo);
			FacCaseApplRoutine();
			try {
				facCaseApplService.insert(tFacCaseAppl, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E2009", e.getErrorMsg()); // 新增資料時，發生錯誤
			}
			// eloan送共同借款人
			if (this.isEloan) {
				this.info("共同借款人");
				int iFacShareApplNo = parse.stringToInteger(titaVo.get("FacShareApplNo"));
				String iJcicMergeFlag = titaVo.get("JcicMergeFlag");
				this.info("iFacShareApplNo = " + iFacShareApplNo);
				if (iFacShareApplNo > 0) {
					tFacShareAppl = facShareApplService.findById(iFacShareApplNo, titaVo);
					tFacMain = facMainService.facmApplNoFirst(iFacShareApplNo, titaVo);
					int mCustNo = 0;
					int mFacmNo = 0;
					if (tFacMain != null) {
						mCustNo = tFacMain.getCustNo();
						mFacmNo = tFacMain.getFacmNo();
					}
					if (tFacShareAppl != null) {
						int wkMApplNo = tFacShareAppl.getMainApplNo();
						// 找此主核准號碼最後一筆
						FacShareAppl lastFacShareAppl = facShareApplService.mApplNoFirst(wkMApplNo, titaVo);
						this.info("lastFacShareAppl 最後一筆 = " + lastFacShareAppl.getKeyinSeq());
						int wkSeq = lastFacShareAppl.getKeyinSeq() + 1;
						tFacShareAppl = new FacShareAppl();

						tFacShareAppl.setApplNo(wkApplNo);
						tFacShareAppl.setCustNo(0);
						tFacShareAppl.setFacmNo(0);
						tFacShareAppl.setMainApplNo(wkMApplNo);
						tFacShareAppl.setKeyinSeq(wkSeq);
						tFacShareAppl.setJcicMergeFlag(iJcicMergeFlag);

						try {
							facShareApplService.insert(tFacShareAppl, titaVo);
						} catch (DBException e) {
							throw new LogicException("E0005", "共同借款人" + e.getErrorMsg());
						}
					} else {
						for (int i = 1; i <= 2; i++) {
							tFacShareAppl = new FacShareAppl();

							if (i == 1) {

								tFacShareAppl.setApplNo(iFacShareApplNo);
								tFacShareAppl.setCustNo(mCustNo);
								tFacShareAppl.setFacmNo(mFacmNo);
							} else {

								tFacShareAppl.setApplNo(wkApplNo);
								tFacShareAppl.setCustNo(0);
								tFacShareAppl.setFacmNo(0);
							}
							tFacShareAppl.setMainApplNo(iFacShareApplNo);
							tFacShareAppl.setKeyinSeq(i);
							tFacShareAppl.setJcicMergeFlag(iJcicMergeFlag);

							try {
								facShareApplService.insert(tFacShareAppl, titaVo);
							} catch (DBException e) {
								throw new LogicException("E0005", "共同借款人" + e.getErrorMsg());
							}
						}

					}

				}
			}

			break;
		case 2: /*
				 * 修改 案件核准後,只可修改專辦,協辦, 核決主管,介紹人及駐區資料 非本日所建資料不可修改 非建檔之放款站或櫃員,不可修改
				 */
			tFacCaseAppl = facCaseApplService.holdById(iApplNo);
			// 變更前
			FacCaseAppl beforeFacCaseAppl = (FacCaseAppl) iDataLog.clone(tFacCaseAppl);
			if (tFacCaseAppl == null) {
				throw new LogicException(titaVo, "E2006", "案件申請檔"); // 修改資料不存在
			}
			if (tFacCaseAppl.getProcessCode().equals("1")) {
				tFacCaseAppl.setSyndNo(parse.stringToInteger(titaVo.getParam("SyndNo"))); // 聯貸案序號 2021/09/27新增
				tFacCaseAppl.setCreditOfficer(titaVo.getParam("CreditOfficer"));
				tFacCaseAppl.setLoanOfficer(titaVo.getParam("LoanOfficer"));
				tFacCaseAppl.setIntroducer(titaVo.getParam("Introducer"));
				tFacCaseAppl.setSupervisor(titaVo.getParam("Supervisor"));
				tFacCaseAppl.setCoorgnizer(titaVo.getParam("Coorgnizer"));
			} else {
				FacCaseApplRoutine();
			}
			try {
				tFacCaseAppl = facCaseApplService.update2(tFacCaseAppl, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E2010", "案件申請檔"); // 更新資料時，發生錯誤
			}

			// 紀錄變更前變更後
			iDataLog.setEnv(titaVo, beforeFacCaseAppl, tFacCaseAppl);
			iDataLog.exec("修改案件申請檔資料");
			break;
		case 4: // 刪除 案件申請准駁後禁止刪除
			tFacCaseAppl = facCaseApplService.holdById(iApplNo);
			if (tFacCaseAppl != null) {
				if (!tFacCaseAppl.getProcessCode().equals("0")) {
					throw new LogicException(titaVo, "E2063", "案件申請檔"); // 已作過准駁處理之案件不可刪除
				}
			} else {
				throw new LogicException(titaVo, "E2007", "案件申請檔"); // 刪除資料不存在
			}
			try {
				facCaseApplService.delete(tFacCaseAppl);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E2008", "案件申請檔"); // 刪除資料時，發生錯誤
			}
			break;
		case 5: // inq
			break;
		}
		this.totaVo.putParam("OApplNo", wkApplNo);
		this.addList(this.totaVo);
		return this.sendList();
	}

	private void FacCaseApplRoutine() throws LogicException {
		tFacCaseAppl.setCustUKey(wkCustUkey);
		tFacCaseAppl.setCreditSysNo(parse.stringToInteger(titaVo.getParam("CreditSysNo")));
		tFacCaseAppl.setSyndNo(parse.stringToInteger(titaVo.getParam("SyndNo"))); // 聯貸案序號 2021/09/27新增
		tFacCaseAppl.setApplDate(parse.stringToInteger(titaVo.getParam("ApplDate")));
		tFacCaseAppl.setDepartmentCode(titaVo.getParam("DepartmentCode"));
		tFacCaseAppl.setCurrencyCode(titaVo.getParam("CurrencyCode"));
		tFacCaseAppl.setApplAmt(parse.stringToBigDecimal(titaVo.getParam("TimApplAmt")));
		tFacCaseAppl.setProdNo(titaVo.getParam("ProdNo"));
		tFacCaseAppl.setEstimate(titaVo.getParam("Estimate"));
		tFacCaseAppl.setPieceCode(titaVo.getParam("PieceCode"));
		tFacCaseAppl.setCreditOfficer(titaVo.getParam("CreditOfficer"));
		tFacCaseAppl.setLoanOfficer(titaVo.getParam("LoanOfficer"));
		tFacCaseAppl.setIsLimit(titaVo.getParam("IsLimit"));
		tFacCaseAppl.setIsLnrelNear(titaVo.getParam("IsLnrelNear"));
		tFacCaseAppl.setIsRelated(titaVo.getParam("IsRelated"));
		// 2022.3.28 新增欄位by 昱衡
		tFacCaseAppl.setIsSuspected(titaVo.getParam("IsSuspected"));
		tFacCaseAppl.setIsSuspectedCheck(titaVo.getParam("IsSuspectedCheck"));
		tFacCaseAppl.setIsSuspectedCheckType(titaVo.getParam("IsSuspectedCheckType"));
		tFacCaseAppl.setIsDate(parse.stringToInteger(titaVo.getParam("IsDate")));

		tFacCaseAppl.setIntroducer(titaVo.getParam("Introducer"));
		tFacCaseAppl.setSupervisor(titaVo.getParam("Supervisor"));
		tFacCaseAppl.setCoorgnizer(titaVo.getParam("Coorgnizer"));
		tFacCaseAppl.setProcessCode(titaVo.getParam("ProcessCode"));
		tFacCaseAppl.setGroupUKey(wkGroupUkey);
		if (iFuncCode != 2) {
			tFacCaseAppl.setApproveDate(0);
		}
//		新增修改時更新客戶主檔介紹人
		CustMain upCustMain = custMainService.findById(wkCustUkey, titaVo);
		if (upCustMain != null) {
			upCustMain.setIntroducer(titaVo.getParam("Introducer"));
			try {
				custMainService.update2(upCustMain, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E2010", "客戶主檔"); // 更新資料時，發生錯誤
			}
		}

	}
}