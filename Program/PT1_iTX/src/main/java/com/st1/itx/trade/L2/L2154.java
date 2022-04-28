package com.st1.itx.trade.L2;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.ClFac;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.FacCaseAppl;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.FacMainId;
import com.st1.itx.db.domain.FacProd;
import com.st1.itx.db.domain.FacProdStepRate;
import com.st1.itx.db.domain.FacProdStepRateId;
import com.st1.itx.db.domain.TxTemp;
import com.st1.itx.db.domain.TxTempId;
import com.st1.itx.db.service.CdGseqService;
import com.st1.itx.db.service.ClFacService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacCaseApplService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.FacProdBreachService;
import com.st1.itx.db.service.FacProdService;
import com.st1.itx.db.service.FacProdStepRateService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.db.service.TxTempService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.AuthLogCom;
import com.st1.itx.util.common.BankAuthActCom;
import com.st1.itx.util.common.LoanCom;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;

/*
 * L2154 額度資料維護
 * a.修改:
 * a1.若已撥款則額度之核准額度須>=已貸金額
 * a2.修改資料須刷主管卡之欄位:基本利率代碼,核准利率,利率調整週期,攤還額異動碼,動支期限,還本週期,循環動用期限,代繳所得稅,攤還方式,寬限期到期日,繳款方式,繳息週期,客戶別,核准額度
 * b.刪除:額度尚未撥款前才可刪除額度資料
 */

/**
 * L2154 額度資料維護
 * 
 * @author iza
 * @version 1.0.0
 */
@Service("L2154")
@Scope("prototype")
public class L2154 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public FacProdService facProdService;
	@Autowired
	public FacProdBreachService facProdBreachService;
	@Autowired
	public FacProdStepRateService facProdStepRateService;
	@Autowired
	public FacMainService facMainService;
	@Autowired
	public FacCaseApplService facCaseApplService;
	@Autowired
	public CustMainService custMainService;
	@Autowired
	public LoanBorMainService loanBorMainService;
	@Autowired
	public CdGseqService cdGseqService;
	@Autowired
	public TxTempService txTempService;
	@Autowired
	public ClFacService sClFacService;

	@Autowired
	Parse parse;
	@Autowired
	LoanCom loanCom;
	@Autowired
	DateUtil dDateUtil;
	@Autowired
	DataLog datalog;
	@Autowired
	BankAuthActCom bankAuthActCom;
	@Autowired
	public AuthLogCom authLogCom;

	// input area
	private TitaVo titaVo = new TitaVo();
	private int iFuncCode;
	private int iCustNo;
	private int iFacmNo;
	private String iProdNo;

	// work area
	private int wkCustNo;
	private int wkFacmNo;
	private int wkApplNo;
	private int wkIdx;
	private String sProdNo = "";
	private TempVo tTempVo = new TempVo();
	private TxTemp tTxTemp;
	private TxTempId tTxTempId;
	private FacProd tFacProd;
	private FacMain tFacMain;
	private FacMain beforeFacMain;
	private FacMainId tFacMainId;
	private List<TxTemp> lTxTemp;
	private TitaVo txtitaVo;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2154 ");
		this.info("   isActfgEntry   = " + titaVo.isActfgEntry());
		this.info("   isActfgSuprele = " + titaVo.isActfgSuprele());
		this.info("   isHcodeNormal  = " + titaVo.isHcodeNormal());
		this.info("   isHcodeErase   = " + titaVo.isHcodeErase());
		this.info("   isHcodeModify  = " + titaVo.isHcodeModify());
		this.info("   EntdyI         = " + titaVo.getEntDyI());
		this.info("   Kinbr          = " + titaVo.getKinbr());
		this.info("   TlrNo          = " + titaVo.getTlrNo());
		this.info("   Tno            = " + titaVo.getTxtNo());
		this.info("   OrgEntdyI      = " + titaVo.getOrgEntdyI());
		this.info("   OrgKin         = " + titaVo.getOrgKin());
		this.info("   OrgTlr         = " + titaVo.getOrgTlr());
		this.info("   OrgTno         = " + titaVo.getOrgTno());

		this.totaVo.init(titaVo);
		this.titaVo = titaVo;

		bankAuthActCom.setTxBuffer(txBuffer);

		// 取得輸入資料
		iFuncCode = this.parse.stringToInteger(titaVo.getParam("FuncCode"));
		iCustNo = this.parse.stringToInteger(titaVo.getParam("CustNo"));
		iFacmNo = this.parse.stringToInteger(titaVo.getParam("FacmNo"));
		iProdNo = titaVo.getParam("ProdNo");
		wkCustNo = iCustNo;
		wkFacmNo = iFacmNo;

		// 檢查輸入資料
		if (!(iFuncCode == 2 || iFuncCode == 4 || iFuncCode == 5)) {
			throw new LogicException(titaVo, "E2004", "功能"); // 功能選擇錯誤
		}
		if (iFuncCode == 5) {
			this.addList(this.totaVo);
			return this.sendList();
		}

		// 查詢商品參數檔
		tFacProd = facProdService.findById(iProdNo, titaVo);
		if (tFacProd == null) {
			throw new LogicException(titaVo, "E2003", "商品參數檔  商品代碼=" + iProdNo); // 查無資料
		}

		// 登錄
		if (titaVo.isActfgEntry() && titaVo.isHcodeNormal()) {
			EntryNormalRoutine();
		}
		// 登錄 修正
		if (titaVo.isActfgEntry() && titaVo.isHcodeModify()) {
			if (iFuncCode == 4) { // 刪除
				throw new LogicException(titaVo, "E0020", "額度主檔 戶號=" + iCustNo + " 額度編號=" + iFacmNo); // 已刪除資料，不可做修正
			}
			EntryEraseRoutine();
			EntryNormalRoutine();
		}
		// 登錄 訂正
		if (titaVo.isActfgEntry() && titaVo.isHcodeErase()) {
			EntryEraseRoutine();
		}
		// 放行及放行訂正
		if (titaVo.isActfgSuprele()) {
			if (iFuncCode == 4 & titaVo.isHcodeErase()) { // 刪除
				throw new LogicException(titaVo, "E0010", "刪除後不可訂正"); // 功能選擇錯誤
			}
			ReleaseRoutine();
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	// 登錄
	private void EntryNormalRoutine() throws LogicException {
		this.info("EntryNormalRoutine ... ");

		tFacMain = facMainService.holdById(new FacMainId(wkCustNo, wkFacmNo), titaVo);
		if (tFacMain == null) {
			throw new LogicException(titaVo, "E0003", "額度主檔 戶號=" + wkCustNo + " 額度編號=" + wkFacmNo); // 修改資料不存在
		}

		// 新增交易暫存檔
		AddTxTempRoutine(1);

		// 更新額度主檔
		if (tFacMain.getActFg() == 1 && titaVo.isHcodeNormal()) {
			throw new LogicException(titaVo, "E0021", "額度檔 戶號 = " + tFacMain.getCustNo() + " 額度編號 =  " + tFacMain.getFacmNo()); // 該筆資料待放行中
		}
		switch (iFuncCode) {
		case 2: // 修改
			UpdateFacMainRoutine();
			break;
		case 4: // 刪除, 額度尚未撥款前才可刪除額度資料, 放行時才真正刪除檔案

			if (tFacMain.getLastBormNo() > 0) {
				throw new LogicException(titaVo, "E2071", "撥款序號=" + tFacMain.getLastBormNo()); // 額度已撥款後，禁止刪除
			}
			if (tFacMain.getLastBormRvNo() > 900) {
				throw new LogicException(titaVo, "E2071", "預約撥款序號=" + tFacMain.getLastBormRvNo()); // 額度已撥款後，禁止刪除預約撥款
			}
			Slice<ClFac> slClFac = sClFacService.approveNoEq(tFacMain.getApplNo(), 0, Integer.MAX_VALUE, titaVo);
			List<ClFac> lClFac = slClFac == null ? null : slClFac.getContent();
//			該額度與擔保品關聯不可刪除,請先解除關聯
			if (lClFac != null) {
				throw new LogicException(titaVo, "E2073", "核准編號 =" + tFacMain.getApplNo()); // 該額度與擔保品關聯，不可刪除
			}
			tFacMain.setActFg(titaVo.getActFgI());
			tFacMain.setLastAcctDate(titaVo.getEntDyI());
			tFacMain.setLastKinbr(titaVo.getKinbr());
			tFacMain.setLastTlrNo(titaVo.getTlrNo());
			tFacMain.setLastTxtNo(titaVo.getTxtNo());
			try {
				facMainService.update(tFacMain, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E2010", "額度主檔"); // 更新資料時，發生錯誤
			}
			break;
		}

		// 更新階梯式利率
		UpdateFacProdStepRateRoutine();
	}

	// 登錄 訂正
	private void EntryEraseRoutine() throws LogicException {
		this.info("EntryEraseRoutine ... ");

		Slice<TxTemp> slTxTemp = txTempService.txTempTxtNoEq(titaVo.getOrgEntdyI() + 19110000, titaVo.getOrgKin(), titaVo.getOrgTlr(), titaVo.getOrgTno(), this.index, Integer.MAX_VALUE, titaVo);
		lTxTemp = slTxTemp == null ? null : slTxTemp.getContent();
		if (lTxTemp == null || lTxTemp.size() == 0) {
			throw new LogicException(titaVo, "E0001", "交易暫存檔 分行別 = " + titaVo.getOrgKin() + " 交易員代號 = " + titaVo.getOrgTlr() + " 交易序號 = " + titaVo.getOrgTno()); // 查詢資料不存在
		}
		for (TxTemp tx : lTxTemp) {
			wkCustNo = this.parse.stringToInteger(tx.getSeqNo().substring(0, 7));
			wkFacmNo = this.parse.stringToInteger(tx.getSeqNo().substring(7, 10));
			wkIdx = this.parse.stringToInteger(tx.getSeqNo().substring(10, 13));
			tTempVo = tTempVo.getVo(tx.getText());
			this.info("   tTempVo = " + tTempVo.toString());
			this.info("   wkCustNo = " + wkCustNo);
			this.info("   wkFacmNo = " + wkFacmNo);
			switch (iFuncCode) {
			case 2: // 修改
				if (wkIdx == 0) {
					// 還原額度檔
					RestoredFacMainRoutine();
				} else {
					// 還原清償金類型
//					RestoredFacProdBreachRoutine();
				}
				break;
			case 4: // 刪除 還原交序號
				tFacMain = facMainService.holdById(new FacMainId(wkCustNo, wkFacmNo), titaVo);
				if (tFacMain == null) {
					throw new LogicException(titaVo, "E0006", "額度主檔 戶號 = " + wkCustNo + " 額度編號 = " + wkFacmNo); // 鎖定資料時，發生錯誤
				}

				tFacMain.setActFg(this.parse.stringToInteger(tTempVo.getParam("ActFg")));
				tFacMain.setLastAcctDate(this.parse.stringToInteger(tTempVo.getParam("LastAcctDate")));
				tFacMain.setLastKinbr(tTempVo.getParam("LastKinbr"));
				tFacMain.setLastTlrNo(tTempVo.getParam("LastTlrNo"));
				tFacMain.setLastTxtNo(tTempVo.getParam("LastTxtNo"));
				try {
					tFacMain = facMainService.update(tFacMain, titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", "額度主檔 戶號 = " + wkCustNo + " 額度編號 = " + wkFacmNo + " " + e.getErrorMsg()); // 更新資料時，發生錯誤
				}
				break;
			}
		}
	}

	// 放行及放行訂正
	private void ReleaseRoutine() throws LogicException {
		this.info("ReleaseRoutine ... ");

		Slice<TxTemp> slTxTemp = txTempService.txTempTxtNoEq(titaVo.getOrgEntdyI() + 19110000, titaVo.getOrgKin(), titaVo.getOrgTlr(), titaVo.getOrgTno(), this.index, Integer.MAX_VALUE, titaVo);
		lTxTemp = slTxTemp == null ? null : slTxTemp.getContent();
		if (lTxTemp == null || lTxTemp.size() == 0) {
			throw new LogicException(titaVo, "E0001", "交易暫存檔 分行別 = " + titaVo.getOrgKin() + " 交易員代號 = " + titaVo.getOrgTlr() + " 交易序號 = " + titaVo.getOrgTno()); // 查詢資料不存在
		}
		for (TxTemp tx : lTxTemp) {
			wkCustNo = this.parse.stringToInteger(tx.getSeqNo().substring(0, 7));
			wkFacmNo = this.parse.stringToInteger(tx.getSeqNo().substring(7, 10));
			wkIdx = this.parse.stringToInteger(tx.getSeqNo().substring(10, 13));
			tTempVo = tTempVo.getVo(tx.getText());
			this.info("   tTempVo = " + tTempVo);
			this.info("   wkCustNo = " + wkCustNo);
			this.info("   wkFacmNo = " + wkFacmNo);
			this.info("   wkIdx    = " + wkIdx);
			if (titaVo.isHcodeNormal() && wkIdx > 0) {
				continue;
			}
			tFacMain = facMainService.holdById(new FacMainId(wkCustNo, wkFacmNo), titaVo);
			if (tFacMain == null) {
				throw new LogicException(titaVo, "E0006", "額度主檔"); // 鎖定資料時，發生錯誤
			}
			wkApplNo = tFacMain.getApplNo();
			// 放行 一般
			if (titaVo.isHcodeNormal()) {
				if (tFacMain.getActFg() != 1) {
					throw new LogicException(titaVo, "E0017", "額度主檔 戶號 = " + wkCustNo + "額度編號 = " + wkFacmNo); // 該筆交易狀態非待放行，不可做交易放行
				}
				switch (iFuncCode) {
				case 2: // 修改, 更新額度主檔
					// 新增交易暫存檔
					TxTemp tTxTemp = new TxTemp();
					TxTempId tTxTempId = new TxTempId();
					loanCom.setTxTemp(tTxTempId, tTxTemp, wkCustNo, wkFacmNo, 0, 0, titaVo);
					tTempVo.clear();
					tTempVo.putParam("ActFg", tFacMain.getActFg());
					tTempVo.putParam("LastAcctDate", tFacMain.getLastAcctDate());
					tTempVo.putParam("LastKinbr", tFacMain.getLastKinbr());
					tTempVo.putParam("LastTlrNo", tFacMain.getLastTlrNo());
					tTempVo.putParam("LastTxtNo", tFacMain.getLastTxtNo());
					tTxTemp.setText(tTempVo.getJsonString());
					try {
						txTempService.insert(tTxTemp, titaVo);
					} catch (DBException e) {
						throw new LogicException(titaVo, "E0005", "交易暫存檔 Key = " + tTxTempId); // 新增資料時，發生錯誤
					}
					// 更新額度主檔
					tFacMain.setActFg(titaVo.getActFgI());
					tFacMain.setLastAcctDate(titaVo.getEntDyI());
					tFacMain.setLastKinbr(titaVo.getKinbr());
					tFacMain.setLastTlrNo(titaVo.getTlrNo());
					tFacMain.setLastTxtNo(titaVo.getTxtNo());
					try {
						tFacMain = facMainService.update(tFacMain, titaVo);
					} catch (DBException e) {
						throw new LogicException(titaVo, "E0008", "額度主檔 戶號 = " + wkCustNo + "額度編號 = " + wkFacmNo + e.getErrorMsg()); // 新增資料時，發生錯誤
					}
					// by eric 2022.4.23
					tTempVo = tTempVo.getVo(tx.getText());
					beforeFacMain = (FacMain) datalog.clone(tFacMain);
					beforeFacMain = tempVoToFacMain(beforeFacMain, tTempVo);
					beforeFacMain.setActFg(tFacMain.getActFg());
					beforeFacMain.setLastAcctDate(tFacMain.getLastAcctDate());
					beforeFacMain.setLastKinbr(tFacMain.getLastKinbr());
					beforeFacMain.setLastTlrNo(tFacMain.getLastTlrNo());
					beforeFacMain.setLastTxtNo(tFacMain.getLastTxtNo());
					datalog.setEnv(titaVo, beforeFacMain, tFacMain);
					datalog.setLog("扣款銀行", tTempVo.get("RepayBank"), this.titaVo.getParam("RepayBank"));
					datalog.setLog("扣款帳號", tTempVo.get("RepayAcctNo"), this.titaVo.getParam("RepayAcctNo"));
					datalog.setLog("扣款帳號.郵局存款別", tTempVo.get("PostCode"), this.titaVo.getParam("PostCode"));
					datalog.setLog("扣款帳號.與借款人關係", tTempVo.get("RelationCode"), this.titaVo.getParam("RelationCode"));
					datalog.setLog("扣款帳號.帳戶戶名", tTempVo.get("RelationName"), this.titaVo.getParam("RelationName"));
					datalog.setLog("扣款帳號.身份證字號", tTempVo.get("RelationId"), this.titaVo.getParam("RelationId"));
					datalog.setLog("扣款帳號.出生日期", tTempVo.get("RelationBirthday"), this.titaVo.getParam("RelationBirthday"));
					datalog.setLog("扣款帳號.性別", tTempVo.get("RelationGender"), this.titaVo.getParam("RelationGender"));

					datalog.exec("修改額度主檔資料", String.format("%07d-%03d-%07d", tFacMain.getCustNo(), tFacMain.getFacmNo(), tFacMain.getApplNo()));

					break;
				case 4: // 刪除, 刪除額度資料及清償金類型

					// 刪除時更新案件申請檔
					UpdateAppl();
					// 新增交易暫存檔
					AddTxTempRoutine(2);
					// 刪除時檢查為該戶號最後一筆額度 客戶主檔該額度-1
					CustMain tCustMain = null;
					tCustMain = custMainService.custNoFirst(tFacMain.getCustNo(), tFacMain.getCustNo(), titaVo);
					if (tCustMain == null) {
						throw new LogicException(titaVo, "E2003", "客戶資料主檔 = " + tFacMain.getCustNo()); // 查無資料
					}
					String wkCustUKey = tCustMain.getCustUKey().trim();
					tCustMain = custMainService.holdById(wkCustUKey, titaVo);
					if (tCustMain == null) {
						throw new LogicException(titaVo, "E2011", "客戶資料主檔"); // 鎖定資料時，發生錯誤
					}
//					if (tFacMain.getFacmNo() == tCustMain.getLastFacmNo()) {
//						tCustMain.setLastFacmNo(tCustMain.getLastFacmNo() - 1);
//						try {
//							custMainService.update(tCustMain, titaVo);
//						} catch (DBException e) {
//							throw new LogicException(titaVo, "E2010", "客戶資料主檔"); // 更新資料時，發生錯誤
//						}
//					}
					try {
						facMainService.delete(tFacMain, titaVo);
					} catch (DBException e) {
						throw new LogicException(titaVo, "E2008", "額度主檔"); // 刪除資料時，發生錯誤
					}

					// 抓更新刪除額度後目前最後一筆額度 2022.4.13
					tFacMain = facMainService.findLastFacmNoFirst(wkCustNo, titaVo);
					if (tFacMain == null) {
						tCustMain.setLastFacmNo(0);
					} else {
						tCustMain.setLastFacmNo(tFacMain.getFacmNo());
					}
					try {
						custMainService.update(tCustMain, titaVo);
					} catch (DBException e) {
						throw new LogicException(titaVo, "E2010", "客戶資料主檔"); // 更新資料時，發生錯誤
					}

					// 刪除階梯式利率
					DeleteFacProdStepRateRoutine();
					break;
				}

			}
			// 放行訂正
			if (titaVo.isHcodeErase()) {
				// 放款交易訂正交易須由最後一筆交易開始訂正
				loanCom.checkEraseFacmTxSeqNo(tFacMain, titaVo);
				tTempVo = tTempVo.getVo(tx.getText());
				switch (iFuncCode) {
				case 2: // 修改
					tFacMain.setActFg(this.parse.stringToInteger(tTempVo.getParam("ActFg")));
					tFacMain.setLastAcctDate(this.parse.stringToInteger(tTempVo.getParam("LastAcctDate")));
					tFacMain.setLastKinbr(tTempVo.getParam("LastKinbr"));
					tFacMain.setLastTlrNo(tTempVo.getParam("LastTlrNo"));
					tFacMain.setLastTxtNo(tTempVo.getParam("LastTxtNo"));
					try {
						tFacMain = facMainService.update(tFacMain, titaVo);
					} catch (DBException e) {
						throw new LogicException(titaVo, "E0008", "額度主檔 戶號 = " + wkCustNo + "額度編號 = " + wkFacmNo + e.getErrorMsg()); // 新增資料時，發生錯誤
					}
					// by eric 2022.4.23
					datalog.delete(titaVo);

					break;
				case 4: // 刪除
					// 還原額度檔
					RestoredDeletedFacMainRoutine();
					break;
				}
			}
		}

		// 銀扣授權帳號檔
		bankAuthActRoutine();
	}

	// 刪除更新案件申請檔
	private void UpdateAppl() throws LogicException {
		this.info("UpdateAppl ...");
		tTxTemp = new TxTemp();
		tTxTempId = new TxTempId();

		FacCaseAppl tfacCaseAppl = facCaseApplService.holdById(wkApplNo, titaVo);
		tfacCaseAppl.setProcessCode("0");
		try {
			facCaseApplService.update(tfacCaseAppl, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007", "案件申請檔"); // 更新資料時，發生錯誤
		}
	}

	// 新增交易暫存檔
	private void AddTxTempRoutine(int actfg) throws LogicException {
		this.info("AddTxTempRoutine ...");

		tTxTemp = new TxTemp();
		tTxTempId = new TxTempId();
		loanCom.setTxTemp(tTxTempId, tTxTemp, wkCustNo, wkFacmNo, 0, 0, titaVo);
		tTempVo.clear();
		tTempVo.putParam("LastBormNo", tFacMain.getLastBormNo());
		tTempVo.putParam("LastBormRvNo", tFacMain.getLastBormRvNo());
		tTempVo.putParam("ApplNo", tFacMain.getApplNo());
		tTempVo.putParam("AnnualIncr", tFacMain.getAnnualIncr());
		tTempVo.putParam("EmailIncr", tFacMain.getEmailIncr());
		tTempVo.putParam("GraceIncr", tFacMain.getGraceIncr());
		tTempVo.putParam("CurrencyCode", tFacMain.getCurrencyCode());
		tTempVo.putParam("UtilAmt", tFacMain.getUtilAmt());
		tTempVo.putParam("UtilBal", tFacMain.getUtilBal());
		tTempVo.putParam("AcctCode", tFacMain.getAcctCode());
		tTempVo.putParam("FirstDrawdownDate", tFacMain.getFirstDrawdownDate());
		tTempVo.putParam("MaturityDate", tFacMain.getMaturityDate());
		tTempVo.putParam("CreditScore", tFacMain.getCreditScore());
		tTempVo.putParam("GuaranteeDate", tFacMain.getGuaranteeDate());
		tTempVo.putParam("ContractNo", tFacMain.getContractNo());
		tTempVo.putParam("ColSetFlag", tFacMain.getColSetFlag());
		tTempVo.putParam("L9110Flag", tFacMain.getL9110Flag());
		// 以下為可維護項目
		tTempVo.putParam("ProdNo", tFacMain.getProdNo());
		tTempVo.putParam("CreditSysNo", tFacMain.getCreditSysNo());
		tTempVo.putParam("BaseRateCode", tFacMain.getBaseRateCode());
		tTempVo.putParam("RateIncr", tFacMain.getRateIncr());
		tTempVo.putParam("IndividualIncr", tFacMain.getIndividualIncr());
		tTempVo.putParam("ApproveRate", tFacMain.getApproveRate());
		tTempVo.putParam("RateCode", tFacMain.getRateCode());
		tTempVo.putParam("FirstRateAdjFreq", tFacMain.getFirstRateAdjFreq());
		tTempVo.putParam("RateAdjFreq", tFacMain.getRateAdjFreq());
		tTempVo.putParam("LineAmt", tFacMain.getLineAmt());
		tTempVo.putParam("AcctCode", tFacMain.getAcctCode());
		tTempVo.putParam("LoanTermYy", tFacMain.getLoanTermYy());
		tTempVo.putParam("LoanTermMm", tFacMain.getLoanTermMm());
		tTempVo.putParam("LoanTermDd", tFacMain.getLoanTermDd());
		tTempVo.putParam("AmortizedCode", tFacMain.getAmortizedCode());
		tTempVo.putParam("FreqBase", tFacMain.getFreqBase());
		tTempVo.putParam("PayIntFreq", tFacMain.getPayIntFreq());
		tTempVo.putParam("RepayFreq", tFacMain.getRepayFreq());
		tTempVo.putParam("UtilDeadline", tFacMain.getUtilDeadline());
		tTempVo.putParam("GracePeriod", tFacMain.getGracePeriod());
		tTempVo.putParam("AcctFee", tFacMain.getAcctFee());
		tTempVo.putParam("HandlingFee", tFacMain.getHandlingFee());
		tTempVo.putParam("GuaranteeDate", tFacMain.getGuaranteeDate());
		tTempVo.putParam("ExtraRepayCode", tFacMain.getExtraRepayCode());
		tTempVo.putParam("CustTypeCode", tFacMain.getCustTypeCode());
		tTempVo.putParam("RuleCode", tFacMain.getRuleCode());
		tTempVo.putParam("RecycleCode", tFacMain.getRecycleCode());
		tTempVo.putParam("RecycleDeadline", tFacMain.getRecycleDeadline());
		tTempVo.putParam("UsageCode", tFacMain.getUsageCode());
		tTempVo.putParam("DepartmentCode", tFacMain.getDepartmentCode());
		tTempVo.putParam("IncomeTaxFlag", tFacMain.getIncomeTaxFlag());
		tTempVo.putParam("CompensateFlag", tFacMain.getCompensateFlag());
		tTempVo.putParam("IrrevocableFlag", tFacMain.getIrrevocableFlag());
		tTempVo.putParam("RateAdjNoticeCode", tFacMain.getRateAdjNoticeCode());
		tTempVo.putParam("PieceCode", tFacMain.getPieceCode());
		tTempVo.putParam("ProdBreachFlag", tFacMain.getProdBreachFlag());
		tTempVo.putParam("Breach", tFacMain.getBreachDescription());
		tTempVo.putParam("CreditScore", tFacMain.getCreditScore());
		tTempVo.putParam("RepayCode", tFacMain.getRepayCode());
		tTempVo.putParam("RepayBank", titaVo.getParam("OldRepayBank"));
		tTempVo.putParam("RepayAcctNo", titaVo.getParam("OldAcctNo"));
		tTempVo.putParam("PostCode", titaVo.getParam("OldPostCode"));
		tTempVo.putParam("RelationCode", titaVo.getParam("OldRelationCode"));
		tTempVo.putParam("RelationName", titaVo.getParam("OldRelationName"));
		tTempVo.putParam("RelationId", titaVo.getParam("OldRelationId"));
		tTempVo.putParam("RelationBirthday", titaVo.getParam("OldRelationBirthday"));
		tTempVo.putParam("RelationGender", titaVo.getParam("OldRelationGender"));
		tTempVo.putParam("Introducer", tFacMain.getIntroducer());
		tTempVo.putParam("District", tFacMain.getDistrict());
		tTempVo.putParam("FireOfficer", tFacMain.getFireOfficer());
		tTempVo.putParam("Estimate", tFacMain.getEstimate());
		tTempVo.putParam("CreditOfficer", tFacMain.getCreditOfficer());
		tTempVo.putParam("LoanOfficer", tFacMain.getBusinessOfficer());
		tTempVo.putParam("BusinessOfficer", tFacMain.getBusinessOfficer());
		tTempVo.putParam("ApprovedLevel", tFacMain.getApprovedLevel());
		tTempVo.putParam("Supervisor", tFacMain.getSupervisor());
		tTempVo.putParam("InvestigateOfficer", tFacMain.getInvestigateOfficer());
		tTempVo.putParam("EstimateReview", tFacMain.getEstimateReview());
		tTempVo.putParam("Coorgnizer", tFacMain.getCoorgnizer());
		tTempVo.putParam("AdvanceCloseCode", tFacMain.getAdvanceCloseCode());
		tTempVo.putParam("ActFg", tFacMain.getActFg());
		tTempVo.putParam("LastAcctDate", tFacMain.getLastAcctDate());
		tTempVo.putParam("LastKinbr", tFacMain.getLastKinbr());
		tTempVo.putParam("LastTlrNo", tFacMain.getLastTlrNo());
		tTempVo.putParam("LastTxtNo", tFacMain.getLastTxtNo());
		tTempVo.putParam("AcDate", tFacMain.getAcDate());
		// 新增綠色授信
		tTempVo.putParam("Grcd", tFacMain.getGrcd());
		tTempVo.putParam("GrKind", tFacMain.getGrKind());
		tTempVo.putParam("EsGcd", tFacMain.getEsGcd());
		tTempVo.putParam("EsGKind", tFacMain.getEsGKind());
		tTempVo.putParam("EsGcnl", tFacMain.getEsGcnl());

		tTxTemp.setText(tTempVo.getJsonString());

		if (actfg == 1) {
			if (tFacMain.getRepayCode() == 2) {
				TempVo tempVo = authLogCom.exec(wkCustNo, wkFacmNo, titaVo);
				if (tempVo == null) {
					throw new LogicException(titaVo, "E0001", " 額度主檔 借款人戶號 = " + wkCustNo + " 額度編號 = " + wkFacmNo); // 查詢資料不存在
				}
				tTempVo.putParam("RepayBank", tempVo.getParam("RepayBank"));
				tTempVo.putParam("RepayAcctNo", tempVo.getParam("RepayAcctNo"));
				tTempVo.putParam("PostCode", tempVo.getParam("PostCode"));
				tTempVo.putParam("AchAuthCode", tempVo.getParam("AchAuthCode"));
				tTempVo.putParam("RelationCode", tempVo.getParam("RelationCode"));
				tTempVo.putParam("RelationName", tempVo.getParam("RelationName"));
				tTempVo.putParam("RelationId", tempVo.getParam("RelationId"));
				tTempVo.putParam("RelationBirthday", tempVo.getParam("RelationBirthday"));
				tTempVo.putParam("RelationGender", tempVo.getParam("RelationGender"));
			} else {
				tTempVo.putParam("RepayBank", "");
				tTempVo.putParam("RepayAcctNo", "");
				tTempVo.putParam("PostCode", "");
				tTempVo.putParam("AchAuthCode", "");
				tTempVo.putParam("RelationCode", "");
				tTempVo.putParam("RelationName", "");
				tTempVo.putParam("RelationId", "");
				tTempVo.putParam("RelationBirthday", 0);
				tTempVo.putParam("RelationGender", "");
			}
		}
		try {
			txTempService.insert(tTxTemp, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0005", "交易暫存檔 Key = " + tTxTempId); // 新增資料時，發生錯誤
		}
	}

	private void UpdateFacMainRoutine() throws LogicException {
		this.info("UpdateFacMainRoutine ...");

		beforeFacMain = (FacMain) datalog.clone(tFacMain);
		tFacMain.setCreditSysNo(this.parse.stringToInteger(titaVo.getParam("CreditSysNo")));
		tFacMain.setProdNo(titaVo.getParam("ProdNo"));
		tFacMain.setBaseRateCode(titaVo.getParam("BaseRateCode"));
		tFacMain.setRateIncr(this.parse.stringToBigDecimal(titaVo.getParam("RateIncr")));
		tFacMain.setIndividualIncr(this.parse.stringToBigDecimal(titaVo.getParam("IndividualIncr")));
		tFacMain.setApproveRate(this.parse.stringToBigDecimal(titaVo.getParam("ApproveRate")));
		tFacMain.setRateCode(titaVo.getParam("RateCode"));
		tFacMain.setFirstRateAdjFreq(this.parse.stringToInteger(titaVo.getParam("FirstRateAdjFreq")));
		tFacMain.setRateAdjFreq(this.parse.stringToInteger(titaVo.getParam("RateAdjFreq")));
		tFacMain.setAcctCode(titaVo.getParam("AcctCode"));
		tFacMain.setLineAmt(this.parse.stringToBigDecimal(titaVo.getParam("TimApplAmt")));
		tFacMain.setLoanTermYy(this.parse.stringToInteger(titaVo.getParam("LoanTermYy")));
		tFacMain.setLoanTermMm(this.parse.stringToInteger(titaVo.getParam("LoanTermMm")));
		tFacMain.setLoanTermDd(this.parse.stringToInteger(titaVo.getParam("LoanTermDd")));
		tFacMain.setAmortizedCode(titaVo.getParam("AmortizedCode"));
		tFacMain.setFreqBase(titaVo.getParam("FreqBase"));
		tFacMain.setPayIntFreq(this.parse.stringToInteger(titaVo.getParam("PayIntFreq")));
		tFacMain.setRepayFreq(this.parse.stringToInteger(titaVo.getParam("RepayFreq")));
		tFacMain.setUtilDeadline(this.parse.stringToInteger(titaVo.getParam("UtilDeadline")));
		tFacMain.setGracePeriod(this.parse.stringToInteger(titaVo.getParam("GracePeriod")));
		tFacMain.setAcctFee(this.parse.stringToBigDecimal(titaVo.getParam("TimAcctFee")));
		tFacMain.setHandlingFee(this.parse.stringToBigDecimal(titaVo.getParam("TimHandlingFee")));
		tFacMain.setGuaranteeDate(this.parse.stringToInteger(titaVo.getParam("GuaranteeDate")));
		tFacMain.setExtraRepayCode(titaVo.getParam("ExtraRepayCode"));
		tFacMain.setCustTypeCode(titaVo.getParam("CustTypeCode"));
		tFacMain.setRuleCode(titaVo.getParam("RuleCode"));
		tFacMain.setRecycleCode(titaVo.getParam("RecycleCode"));
		tFacMain.setRecycleDeadline(this.parse.stringToInteger(titaVo.getParam("RecycleDeadline")));
		tFacMain.setUsageCode(titaVo.getParam("UsageCode"));
		tFacMain.setDepartmentCode(titaVo.getParam("DepartmentCode"));
		tFacMain.setIncomeTaxFlag(titaVo.getParam("IncomeTaxFlag"));
		tFacMain.setCompensateFlag(titaVo.getParam("CompensateFlag"));
		tFacMain.setIrrevocableFlag(titaVo.getParam("IrrevocableFlag"));
		tFacMain.setRateAdjNoticeCode(titaVo.getParam("RateAdjNoticeCode"));
		tFacMain.setPieceCode(titaVo.getParam("PieceCode"));
		tFacMain.setProdBreachFlag(titaVo.getParam("ProdBreachFlag"));
		tFacMain.setBreachDescription(titaVo.getParam("Breach"));
		tFacMain.setCreditScore(this.parse.stringToInteger(titaVo.getParam("CreditScore")));
		tFacMain.setRepayCode(this.parse.stringToInteger(titaVo.getParam("RepayCode")));
		tFacMain.setIntroducer(titaVo.getParam("Introducer"));
		tFacMain.setDistrict(titaVo.getParam("District"));
		tFacMain.setFireOfficer(titaVo.getParam("FireOfficer"));
		tFacMain.setEstimate(titaVo.getParam("Estimate"));
		tFacMain.setCreditOfficer(titaVo.getParam("CreditOfficer"));
		tFacMain.setLoanOfficer(titaVo.getParam("BusinessOfficer"));
		tFacMain.setBusinessOfficer(titaVo.getParam("BusinessOfficer"));
		tFacMain.setApprovedLevel(titaVo.getParam("ApprovedLevel"));
		tFacMain.setSupervisor(titaVo.getParam("Supervisor"));
		tFacMain.setInvestigateOfficer(titaVo.getParam("InvestigateOfficer"));
		tFacMain.setEstimateReview(titaVo.getParam("EstimateReview"));
		tFacMain.setCoorgnizer(titaVo.getParam("Coorgnizer"));
		tFacMain.setActFg(titaVo.getActFgI());
		tFacMain.setLastAcctDate(titaVo.getEntDyI());
		tFacMain.setLastKinbr(titaVo.getKinbr());
		tFacMain.setLastTlrNo(titaVo.getTlrNo());
		tFacMain.setLastTxtNo(titaVo.getTxtNo());
		tFacMain.setAcDate(this.txBuffer.getTxCom().getTbsdy());
		// 新增綠色授信相關
		tFacMain.setGrcd(titaVo.getParam("Grcd"));
		tFacMain.setGrKind(titaVo.getParam("GrKind"));
		tFacMain.setEsGcd(titaVo.getParam("EsGcd"));
		tFacMain.setEsGKind(titaVo.getParam("EsGKind"));
		tFacMain.setEsGcnl(titaVo.getParam("EsGcnl"));

		this.info("RepayCode = " + titaVo.getParam("RepayCode"));
		if ("02".equals(titaVo.getParam("RepayCode")) || "2".equals(titaVo.getParam("RepayCode"))) {

//			bankAuthActCom.changeAcctNo(titaVo);   TODO:修改ach授權

		}

		try {
			tFacMain = facMainService.update2(tFacMain, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E2010", "額度主檔"); // 更新資料時，發生錯誤
		}
//		datalog.setEnv(titaVo, beforeFacMain, tFacMain);
//		datalog.exec("修改額度主檔資料");

	}

	// 訂正時, 還原額度檔
	private void RestoredFacMainRoutine() throws LogicException {
		this.info("RestoredFacMainRoutine ... ");

		tFacMain = facMainService.holdById(new FacMainId(wkCustNo, wkFacmNo), titaVo);
		if (tFacMain == null) {
			throw new LogicException(titaVo, "E0006", "額度主檔 戶號 = " + wkCustNo + " 額度編號 = " + wkFacmNo); // 鎖定資料時，發生錯誤
		}
		// 放款交易訂正交易須由最後一筆交易開始訂正
		loanCom.checkEraseFacmTxSeqNo(tFacMain, titaVo);
		beforeFacMain = (FacMain) datalog.clone(tFacMain);

		// by eric 2022.4.23
		tFacMain = tempVoToFacMain(tFacMain, tTempVo);

		try {
			tFacMain = facMainService.update2(tFacMain, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007", "額度主檔 戶號 = " + wkCustNo + " 額度編號 = " + wkFacmNo + " " + e.getErrorMsg()); // 更新資料時，發生錯誤
		}
//		datalog.setEnv(titaVo, beforeFacMain, tFacMain);
//		datalog.exec("修改額度主檔資料");
	}

	// by eric 2022.4.23
	private FacMain tempVoToFacMain(FacMain tFacMain, TempVo tTempVo) throws LogicException {
		tFacMain.setCreditSysNo(this.parse.stringToInteger(tTempVo.getParam("CreditSysNo")));
		tFacMain.setProdNo(tTempVo.getParam("ProdNo"));
		tFacMain.setBaseRateCode(tTempVo.getParam("BaseRateCode"));
		tFacMain.setRateIncr(this.parse.stringToBigDecimal(tTempVo.getParam("RateIncr")));
		tFacMain.setIndividualIncr(this.parse.stringToBigDecimal(tTempVo.getParam("IndividualIncr")));
		tFacMain.setApproveRate(this.parse.stringToBigDecimal(tTempVo.getParam("ApproveRate")));
		tFacMain.setRateCode(tTempVo.getParam("RateCode"));
		tFacMain.setFirstRateAdjFreq(this.parse.stringToInteger(tTempVo.getParam("FirstRateAdjFreq")));
		tFacMain.setRateAdjFreq(this.parse.stringToInteger(tTempVo.getParam("RateAdjFreq")));
		tFacMain.setLineAmt(this.parse.stringToBigDecimal(tTempVo.getParam("LineAmt")));
		tFacMain.setAcctCode(tTempVo.getParam("AcctCode"));
		tFacMain.setLoanTermYy(this.parse.stringToInteger(tTempVo.getParam("LoanTermYy")));
		tFacMain.setLoanTermMm(this.parse.stringToInteger(tTempVo.getParam("LoanTermMm")));
		tFacMain.setLoanTermDd(this.parse.stringToInteger(tTempVo.getParam("LoanTermDd")));
		tFacMain.setAmortizedCode(tTempVo.getParam("AmortizedCode"));
		tFacMain.setFreqBase(tTempVo.getParam("FreqBase"));
		tFacMain.setPayIntFreq(this.parse.stringToInteger(tTempVo.getParam("PayIntFreq")));
		tFacMain.setRepayFreq(this.parse.stringToInteger(tTempVo.getParam("RepayFreq")));
		tFacMain.setUtilDeadline(this.parse.stringToInteger(tTempVo.getParam("UtilDeadline")));
		tFacMain.setGracePeriod(this.parse.stringToInteger(tTempVo.getParam("GracePeriod")));
		tFacMain.setAcctFee(this.parse.stringToBigDecimal(tTempVo.getParam("AcctFee")));
		tFacMain.setHandlingFee(this.parse.stringToBigDecimal(tTempVo.getParam("HandlingFee")));
		tFacMain.setGuaranteeDate(this.parse.stringToInteger(tTempVo.getParam("GuaranteeDate")));
		tFacMain.setExtraRepayCode(tTempVo.getParam("ExtraRepayCode"));
		tFacMain.setCustTypeCode(tTempVo.getParam("CustTypeCode"));
		tFacMain.setRuleCode(tTempVo.getParam("RuleCode"));
		tFacMain.setRecycleCode(tTempVo.getParam("RecycleCode"));
		tFacMain.setRecycleDeadline(this.parse.stringToInteger(tTempVo.getParam("RecycleDeadline")));
		tFacMain.setUsageCode(tTempVo.getParam("UsageCode"));
		tFacMain.setDepartmentCode(tTempVo.getParam("DepartmentCode"));
		tFacMain.setIncomeTaxFlag(tTempVo.getParam("IncomeTaxFlag"));
		tFacMain.setCompensateFlag(tTempVo.getParam("CompensateFlag"));
		tFacMain.setIrrevocableFlag(tTempVo.getParam("IrrevocableFlag"));
		tFacMain.setRateAdjNoticeCode(tTempVo.getParam("RateAdjNoticeCode"));
		tFacMain.setPieceCode(tTempVo.getParam("PieceCode"));
		tFacMain.setProdBreachFlag(tTempVo.getParam("ProdBreachFlag"));
		tFacMain.setBreachDescription(tTempVo.getParam("Breach"));
		tFacMain.setCreditScore(this.parse.stringToInteger(tTempVo.getParam("CreditScore")));
		tFacMain.setRepayCode(this.parse.stringToInteger(tTempVo.getParam("RepayCode")));
		tFacMain.setIntroducer(tTempVo.getParam("Introducer"));
		tFacMain.setDistrict(tTempVo.getParam("District"));
		tFacMain.setFireOfficer(tTempVo.getParam("FireOfficer"));
		tFacMain.setEstimate(tTempVo.getParam("Estimate"));
		tFacMain.setCreditOfficer(tTempVo.getParam("CreditOfficer"));
		tFacMain.setLoanOfficer(tTempVo.getParam("BusinessOfficer"));
		tFacMain.setBusinessOfficer(tTempVo.getParam("BusinessOfficer"));
		tFacMain.setApprovedLevel(tTempVo.getParam("ApprovedLevel"));
		tFacMain.setSupervisor(tTempVo.getParam("Supervisor"));
		tFacMain.setInvestigateOfficer(tTempVo.getParam("InvestigateOfficer"));
		tFacMain.setEstimateReview(tTempVo.getParam("EstimateReview"));
		tFacMain.setCoorgnizer(tTempVo.getParam("Coorgnizer"));
		tFacMain.setActFg(this.parse.stringToInteger(tTempVo.getParam("ActFg")));
		tFacMain.setLastAcctDate(this.parse.stringToInteger(tTempVo.getParam("LastAcctDate")));
		tFacMain.setLastKinbr(tTempVo.getParam("LastKinbr"));
		tFacMain.setLastTlrNo(tTempVo.getParam("LastTlrNo"));
		tFacMain.setLastTxtNo(tTempVo.getParam("LastTxtNo"));
		tFacMain.setAcDate(this.parse.stringToInteger(tTempVo.getParam("AcDate")));

		// 新增綠色授信相關
		tFacMain.setGrcd(tTempVo.getParam("Grcd"));
		tFacMain.setGrKind(tTempVo.getParam("GrKind"));
		tFacMain.setEsGcd(tTempVo.getParam("EsGcd"));
		tFacMain.setEsGKind(tTempVo.getParam("EsGKind"));
		tFacMain.setEsGcnl(tTempVo.getParam("EsGcnl"));

		return tFacMain;
	}

	// 放行訂正時, 還原被刪除的額度檔
	private void RestoredDeletedFacMainRoutine() throws LogicException {
		this.info("RestoredDeletedFacMainRoutine ... ");

		tFacMain = new FacMain();
		tFacMainId = new FacMainId();
		tFacMainId.setCustNo(wkCustNo);
		tFacMainId.setFacmNo(wkFacmNo);
		tFacMain.setCustNo(wkCustNo);
		tFacMain.setFacmNo(wkFacmNo);
		tFacMain.setFacMainId(tFacMainId);
		tFacMain.setLastBormNo(this.parse.stringToInteger(tTempVo.getParam("LastBormNo")));
		tFacMain.setLastBormRvNo(this.parse.stringToInteger(tTempVo.getParam("LastBormRvNo")));
		tFacMain.setApplNo(this.parse.stringToInteger(tTempVo.getParam("ApplNo")));
		tFacMain.setAnnualIncr(this.parse.stringToBigDecimal(tTempVo.getParam("AnnualIncr")));
		tFacMain.setEmailIncr(this.parse.stringToBigDecimal(tTempVo.getParam("EmailIncr")));
		tFacMain.setGraceIncr(this.parse.stringToBigDecimal(tTempVo.getParam("GraceIncr")));
		tFacMain.setCurrencyCode(tTempVo.getParam("CurrencyCode"));
		tFacMain.setUtilAmt(this.parse.stringToBigDecimal(tTempVo.getParam("UtilAmt")));
		tFacMain.setUtilBal(this.parse.stringToBigDecimal(tTempVo.getParam("UtilBal")));
		tFacMain.setFirstDrawdownDate(this.parse.stringToInteger(tTempVo.getParam("FirstDrawdownDate")));
		tFacMain.setMaturityDate(this.parse.stringToInteger(tTempVo.getParam("MaturityDate")));
		tFacMain.setCreditScore(this.parse.stringToInteger(tTempVo.getParam("CreditScore")));
		tFacMain.setGuaranteeDate(this.parse.stringToInteger(tTempVo.getParam("GuaranteeDate")));
		tFacMain.setContractNo(tTempVo.getParam("ContractNo"));
		tFacMain.setColSetFlag(tTempVo.getParam("ColSetFlag"));
		tFacMain.setL9110Flag(tTempVo.getParam("L9110Flag"));
		// 以下為可維護項目
		tFacMain.setCreditSysNo(this.parse.stringToInteger(tTempVo.getParam("CreditSysNo")));
		tFacMain.setProdNo(tTempVo.getParam("ProdNo"));
		tFacMain.setBaseRateCode(tTempVo.getParam("BaseRateCode"));
		tFacMain.setRateIncr(this.parse.stringToBigDecimal(tTempVo.getParam("RateIncr")));
		tFacMain.setIndividualIncr(this.parse.stringToBigDecimal(tTempVo.getParam("IndividualIncr")));
		tFacMain.setApproveRate(this.parse.stringToBigDecimal(tTempVo.getParam("ApproveRate")));
		tFacMain.setRateCode(tTempVo.getParam("RateCode"));
		tFacMain.setFirstRateAdjFreq(this.parse.stringToInteger(tTempVo.getParam("FirstRateAdjFreq")));
		tFacMain.setRateAdjFreq(this.parse.stringToInteger(tTempVo.getParam("RateAdjFreq")));
		tFacMain.setLineAmt(this.parse.stringToBigDecimal(tTempVo.getParam("TimApplAmt")));
		tFacMain.setAcctCode(tTempVo.getParam("AcctCode"));
		tFacMain.setLoanTermYy(this.parse.stringToInteger(tTempVo.getParam("LoanTermYy")));
		tFacMain.setLoanTermMm(this.parse.stringToInteger(tTempVo.getParam("LoanTermMm")));
		tFacMain.setLoanTermDd(this.parse.stringToInteger(tTempVo.getParam("LoanTermDd")));
		tFacMain.setAmortizedCode(tTempVo.getParam("AmortizedCode"));
		tFacMain.setFreqBase(tTempVo.getParam("FreqBase"));
		tFacMain.setPayIntFreq(this.parse.stringToInteger(tTempVo.getParam("PayIntFreq")));
		tFacMain.setRepayFreq(this.parse.stringToInteger(tTempVo.getParam("RepayFreq")));
		tFacMain.setUtilDeadline(this.parse.stringToInteger(tTempVo.getParam("UtilDeadline")));
		tFacMain.setGracePeriod(this.parse.stringToInteger(tTempVo.getParam("GracePeriod")));
		tFacMain.setAcctFee(this.parse.stringToBigDecimal(tTempVo.getParam("AcctFee")));
		tFacMain.setHandlingFee(this.parse.stringToBigDecimal(tTempVo.getParam("HandlingFee")));
		tFacMain.setGuaranteeDate(this.parse.stringToInteger(tTempVo.getParam("GuaranteeDate")));
		tFacMain.setExtraRepayCode(tTempVo.getParam("ExtraRepayCode"));
		tFacMain.setCustTypeCode(tTempVo.getParam("CustTypeCode"));
		tFacMain.setRuleCode(tTempVo.getParam("RuleCode"));
		tFacMain.setRecycleCode(tTempVo.getParam("RecycleCode"));
		tFacMain.setRecycleDeadline(this.parse.stringToInteger(tTempVo.getParam("RecycleDeadline")));
		tFacMain.setUsageCode(tTempVo.getParam("UsageCode"));
		tFacMain.setDepartmentCode(tTempVo.getParam("DepartmentCode"));
		tFacMain.setIncomeTaxFlag(tTempVo.getParam("IncomeTaxFlag"));
		tFacMain.setCompensateFlag(tTempVo.getParam("CompensateFlag"));
		tFacMain.setIrrevocableFlag(tTempVo.getParam("IrrevocableFlag"));
		tFacMain.setRateAdjNoticeCode(tTempVo.getParam("RateAdjNoticeCode"));
		tFacMain.setPieceCode(tTempVo.getParam("PieceCode"));
		tFacMain.setProdBreachFlag(tTempVo.getParam("ProdBreachFlag"));
		tFacMain.setBreachDescription(tTempVo.getParam("Breach"));
		tFacMain.setCreditScore(this.parse.stringToInteger(tTempVo.getParam("CreditScore")));
		tFacMain.setRepayCode(this.parse.stringToInteger(tTempVo.getParam("RepayCode")));
		tFacMain.setIntroducer(tTempVo.getParam("Introducer"));
		tFacMain.setDistrict(tTempVo.getParam("District"));
		tFacMain.setFireOfficer(tTempVo.getParam("FireOfficer"));
		tFacMain.setEstimate(tTempVo.getParam("Estimate"));
		tFacMain.setCreditOfficer(tTempVo.getParam("CreditOfficer"));
		tFacMain.setLoanOfficer(tTempVo.getParam("BusinessOfficer"));
		tFacMain.setBusinessOfficer(tTempVo.getParam("BusinessOfficer"));
		tFacMain.setApprovedLevel(tTempVo.getParam("ApprovedLevel"));
		tFacMain.setSupervisor(tTempVo.getParam("Supervisor"));
		tFacMain.setInvestigateOfficer(tTempVo.getParam("InvestigateOfficer"));
		tFacMain.setEstimateReview(tTempVo.getParam("EstimateReview"));
		tFacMain.setCoorgnizer(tTempVo.getParam("Coorgnizer"));
		tFacMain.setActFg(titaVo.getActFgI());
		tFacMain.setLastAcctDate(this.parse.stringToInteger(tTempVo.getParam("LastAcctDate")));
		tFacMain.setLastKinbr(tTempVo.getParam("LastKinbr"));
		tFacMain.setLastTlrNo(tTempVo.getParam("LastTlrNo"));
		tFacMain.setLastTxtNo(tTempVo.getParam("LastTxtNo"));
		tFacMain.setAcDate(this.parse.stringToInteger(tTempVo.getParam("AcDate")));

		// 新增綠色授信相關
		tFacMain.setGrcd(tTempVo.getParam("Grcd"));
		tFacMain.setGrKind(tTempVo.getParam("GrKind"));
		tFacMain.setEsGcd(tTempVo.getParam("EsGcd"));
		tFacMain.setEsGKind(tTempVo.getParam("EsGKind"));
		tFacMain.setEsGcnl(tTempVo.getParam("EsGcnl"));
		try {
			tFacMain = facMainService.insert(tFacMain, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0005", "額度主檔 戶號 = " + wkCustNo + " 額度編號 = " + wkFacmNo + " " + e.getErrorMsg()); // 新增資料時，發生錯誤
		}
	}

	private void bankAuthActRoutine() throws LogicException {

		this.info("titaVo.getParam(RepayCode)" + titaVo.getParam("RepayCode"));
		this.info("titaVo.getParam(OldRepayCode)" + titaVo.getParam("OldRepayCode"));
		this.info("titaVo.getParam(PostCode)" + titaVo.getParam("PostCode"));
		this.info("titaVo.getParam(OldPostCode)" + titaVo.getParam("OldPostCode"));
		this.info("titaVo.getParam(RepayAcctNo)" + titaVo.getParam("RepayAcctNo"));
		this.info("titaVo.getParam(OldRepayAcctNo)" + titaVo.getParam("OldAcctNo"));
		this.info("titaVo.getParam(RepayBank)" + titaVo.getParam("RepayBank"));
		this.info("titaVo.getParam(OldRepayBank)" + titaVo.getParam("OldRepayBank"));

		if (titaVo.getParam("RepayCode").equals(titaVo.getParam("OldRepayCode")) && titaVo.getParam("RepayBank").equals(titaVo.getParam("OldRepayBank"))
				&& titaVo.getParam("PostCode").equals(titaVo.getParam("OldPostCode")) && titaVo.getParam("RepayAcctNo").equals(titaVo.getParam("OldAcctNo"))) {

			return;
		}

		// 舊還款帳號(含還款方式)刪除
		if ("02".equals(titaVo.getParam("OldRepayCode"))) {
			txtitaVo = new TitaVo();
			txtitaVo = (TitaVo) titaVo.clone();
			txtitaVo.putParam("RepayCode", titaVo.getParam("OldRepayCode"));
			txtitaVo.putParam("PostCode", titaVo.getParam("OldPostCode"));
			txtitaVo.putParam("RepayAcctNo", titaVo.getParam("OldAcctNo"));
			txtitaVo.putParam("RelationCode", titaVo.getParam("OldRelationCode"));
			txtitaVo.putParam("RelationName", titaVo.getParam("OldRelationName"));
			txtitaVo.putParam("RelationBirthday", titaVo.getParam("OldRelationBirthday"));
			txtitaVo.putParam("RelationGender", titaVo.getParam("OldRelationGender"));
			txtitaVo.putParam("RelationId", titaVo.getParam("OldRelationId"));
			txtitaVo.putParam("RepayBank", titaVo.getParam("OldRepayBank"));
			bankAuthActCom.del("A", txtitaVo);

		}
		// 新還款帳號(含還款方式)刪除
		if ("02".equals(titaVo.getParam("RepayCode"))) {
			bankAuthActCom.add("A", titaVo);
		} else {
			bankAuthActCom.addRepayActChangeLog(titaVo);
		}
	}

	// 更新階梯式利率
	private void UpdateFacProdStepRateRoutine() throws LogicException {
		this.info("UpdateFacProdStepRateRoutine ...");

		DeleteFacProdStepRateRoutine();
		// 更新階梯式利率
		FacProdStepRate tFacProdStepRate = new FacProdStepRate();

		for (int i = 1; i <= 10; i++) {
			if (this.parse.stringToDouble(titaVo.getParam("StepMonthE" + i)) > 0) {
				tFacProdStepRate.setProdNo(sProdNo);
				tFacProdStepRate.setMonthStart(this.parse.stringToInteger(titaVo.getParam("StepMonthS" + i)));
				tFacProdStepRate.setFacProdStepRateId(new FacProdStepRateId(sProdNo, this.parse.stringToInteger(titaVo.getParam("StepMonthS" + i))));
				tFacProdStepRate.setMonthEnd(this.parse.stringToInteger(titaVo.getParam("StepMonthE" + i)));
				tFacProdStepRate.setRateType(titaVo.getParam("StepRateType" + i));
				tFacProdStepRate.setRateIncr(this.parse.stringToBigDecimal(titaVo.getParam("StepRateIncr" + i)));
				tFacProdStepRate.setCreateDate(parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
				tFacProdStepRate.setCreateEmpNo(titaVo.getTlrNo());
				tFacProdStepRate.setLastUpdate(parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
				tFacProdStepRate.setLastUpdateEmpNo(titaVo.getTlrNo());

				try {
					facProdStepRateService.insert(tFacProdStepRate, titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E2009", "階梯式利率"); // 新增資料時，發生錯誤
				}
			} else {
				break;
			}
		}

	}

	// 刪除階梯式利率
	private void DeleteFacProdStepRateRoutine() throws LogicException {
		this.info("DeleteFacProdStepRateRoutine ...");

		sProdNo = FormatUtil.pad9(String.valueOf(tFacMain.getCustNo()), 7) + FormatUtil.pad9(String.valueOf(tFacMain.getFacmNo()), 3);

		Slice<FacProdStepRate> slFacProdStepRate = facProdStepRateService.stepRateProdNoEq(sProdNo, 0, 999, this.index, Integer.MAX_VALUE, titaVo);
		List<FacProdStepRate> lFacProdStepRate = slFacProdStepRate == null ? null : slFacProdStepRate.getContent();
		if (lFacProdStepRate != null && lFacProdStepRate.size() > 0) {
			try {
				facProdStepRateService.deleteAll(lFacProdStepRate, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E2008", "階梯式利率"); // 刪除資料時，發生錯誤
			}
		}

	}

}