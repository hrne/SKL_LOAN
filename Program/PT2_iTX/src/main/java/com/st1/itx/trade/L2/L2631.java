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
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AcReceivable;
import com.st1.itx.db.domain.ClFac;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.FacClose;
import com.st1.itx.db.domain.FacCloseId;
import com.st1.itx.db.domain.TxTemp;
import com.st1.itx.db.domain.TxTempId;
import com.st1.itx.db.service.CdGseqService;
import com.st1.itx.db.service.ClFacService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacCloseService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.db.service.TxTempService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.AcReceivableCom;
import com.st1.itx.util.common.BaTxCom;
import com.st1.itx.util.common.GSeqCom;
import com.st1.itx.util.common.LoanCloseBreachCom;
import com.st1.itx.util.common.data.LoanCloseBreachVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 */

@Service("L2631")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2631 extends TradeBuffer {

	@Autowired
	public CdGseqService cdGseqService;
	/* DB服務注入 */
	@Autowired
	public FacCloseService sFacCloseService;
	/* DB服務注入 */
	@Autowired
	public CustMainService sCustMainService;
	@Autowired
	public ClFacService clFacService;
	@Autowired
	public LoanBorMainService loanBorMainService;
	@Autowired
	public LoanCloseBreachCom loanCloseBreachCom;
	@Autowired
	public L2631ReportA l2631ReportA;
	@Autowired
	public L2631ReportB l2631ReportB;
	@Autowired
	public L2631ReportC l2631ReportC;
	@Autowired
	public L2631ReportD l2631ReportD;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Autowired
	GSeqCom gGSeqCom;
	@Autowired
	public WebClient webClient;

	@Autowired
	AcReceivableCom acReceivableCom;

	@Autowired
	BaTxCom baTxCom;

	@Autowired
	public TxTempService txTempService;
	private TxTemp tTxTemp;
	private TxTempId tTxTempId;
	private List<TxTemp> lTxTemp;
	private int iCustNo;
	private int iFacmNo;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2631");
		this.totaVo.init(titaVo);
		l2631ReportD.setTxBuffer(this.txBuffer);
		// new PK
		FacCloseId tFacCloseId = new FacCloseId();
		// new table
		FacClose tLastCloseNo = new FacClose();
		FacClose tFacClose = new FacClose();
		CustMain tCustMain = new CustMain();
		List<ClFac> lClFac = new ArrayList<ClFac>();
		FacClose BeforeFacClose = new FacClose();
		// tita
		// 登放記號
		int iActFg = parse.stringToInteger(titaVo.getParam("ACTFG"));
		// 戶號
		iCustNo = parse.stringToInteger(titaVo.getParam("CustNo"));
		// 統編
		String iCustId = titaVo.getParam("CustId");
		long pdfSnoF = 0;

		if (!iCustId.isEmpty()) {
			// 如輸入統編,先取出戶號
			tCustMain = sCustMainService.custIdFirst(iCustId);
			iCustNo = tCustMain.getCustNo();
			if (iCustNo == 0) {
				this.info("該統編在客戶主檔無戶號");
			}
		}
		// 功能
		String iFunCode = titaVo.getParam("FunCode");
		// 入帳日期
		int iTranDate = parse.stringToInteger(titaVo.getParam("TranDate"));
		// 申請日期
		int iApplDate = parse.stringToInteger(titaVo.getParam("ApplDate"));
		String iCloseInd = titaVo.getParam("CloseInd");
		// 額度編號
		iFacmNo = parse.stringToInteger(titaVo.getParam("FacmNo"));

		int iCloseNo = parse.stringToInteger(titaVo.getParam("CloseNo"));
		BigDecimal iCloseAmt = parse.stringToBigDecimal(titaVo.getParam("CloseAmt"));

		// 清償序號
		int wkcloseNo = 1;
		// wk
		int wkDocNo = 0;
		// 測試是否存在清償作業檔 如不存在清償序號為01,如存在取該戶號額度清償序號最大筆+1
		tLastCloseNo = sFacCloseService.findLastCloseNoFirst(iApplDate + 19110000, titaVo);
		if (tLastCloseNo != null) {
			wkcloseNo = tLastCloseNo.getCloseNo() + 1;
		}
		this.info("清償序號 = " + wkcloseNo);

		tFacCloseId.setApplDate(iApplDate);
		tFacCloseId.setCloseNo(wkcloseNo);

		tFacClose.setFacCloseId(tFacCloseId);
		if ("0".equals(iFunCode)) {
			// 清償資料登錄時檢核不可重複建檔
			checkisDup(titaVo);
			// 清償日期
			tFacClose.setCloseDate(0);
			// 塗銷同意書編號L2632
			tFacClose.setAgreeNo("");
			// 公文編號L2632
			tFacClose.setDocNo(wkDocNo);
			// 銷號欄L2632
			tFacClose.setClsNo("");
		}
		tFacClose.setCustNo(iCustNo);
		tFacClose.setCloseNo(wkcloseNo);
		tFacClose.setFacmNo(iFacmNo);
		tFacClose.setActFlag(iActFg);
		tFacClose.setFunCode(titaVo.getParam("FunCode"));
		tFacClose.setApplDate(iApplDate);
		tFacClose.setCloseInd(iCloseInd);

		tFacClose.setCollectFlag(titaVo.getParam("CollectFlag"));
		tFacClose.setCloseAmt(iCloseAmt);
		tFacClose.setTelNo1(titaVo.getParam("TelNo1"));
		tFacClose.setTelNo2(titaVo.getParam("TelNo2"));
		tFacClose.setTelNo3(titaVo.getParam("TelNo3"));
		tFacClose.setCloseReasonCode(titaVo.getParam("CloseReasonCode"));
		tFacClose.setCollectWayCode(titaVo.getParam("CollectWayCode"));
		tFacClose.setPostAddress(titaVo.getParam("PostAddress"));
		tFacClose.setEntryDate(iTranDate);
		tFacClose.setRmk(titaVo.getParam("Rmk1"));
		// **[清償違約金即時收取]部分償還時一律記短收，連同下期期款收回，提前結案時計算清償違約金併入結案金額。
		// **[清償違約金領清償證明收取]<清償請領>結案時計算全部的部分償還及提前結案的清償違約金，併入結案金額。
		// [清償違約金領清償證明收取]<清償補領>計算全部的部分償還及提前結案的清償違約金，L3200收取清償違約金領後，才可列印清償證明。
		if ("2".equals(iFunCode)) {
			if ("".equals(titaVo.getParam("RpFg")) && iCloseAmt.compareTo(BigDecimal.ZERO) > 0) {

				ArrayList<LoanCloseBreachVo> oListCloseBreach = new ArrayList<LoanCloseBreachVo>();
				// 計算清償違約金
				oListCloseBreach = loanCloseBreachCom.getCloseBreachAmtAll(iApplDate, iCustNo, iFacmNo, 0, null,
						titaVo);
				// 輸出清償違約金
				if (oListCloseBreach != null && oListCloseBreach.size() > 0) {
					List<AcReceivable> lAcReceivable = new ArrayList<AcReceivable>();
					for (LoanCloseBreachVo v : oListCloseBreach) {
						boolean isfind = false;
						for (AcReceivable t : lAcReceivable) {
							if (t.getFacmNo() == v.getFacmNo() && parse.stringToInteger(t.getRvNo()) == v.getBormNo()) {
								t.setRvAmt(t.getRvAmt().add(v.getCloseBreachAmtUnpaid()));
								isfind = true;
							}
						}
						if (!isfind) {
							// 短繳清償違約金處理, 新增銷帳檔
							AcReceivable tAcReceivable = new AcReceivable();
							tAcReceivable.setReceivableFlag(3); // 未收費用
							tAcReceivable.setAcctCode("IOP");
							tAcReceivable.setCustNo(iCustNo);
							tAcReceivable.setFacmNo(v.getFacmNo());
							tAcReceivable.setRvNo(parse.IntegerToString(v.getBormNo(), 3));
							tAcReceivable.setRvAmt(v.getCloseBreachAmtUnpaid());
							lAcReceivable.add(tAcReceivable);
						}
					}
					acReceivableCom.setTxBuffer(this.getTxBuffer());
					acReceivableCom.mnt(0, lAcReceivable, titaVo); // 0-起帳
				}
			}
		}

		this.info("tFacClose  = " + tFacClose);
		try {
			sFacCloseService.insert(tFacClose, titaVo);
		} catch (DBException e) {
			throw new LogicException("E0005", "清償作業檔");
		}

		this.totaVo.putParam("OCloseNo", wkcloseNo);

		String parentTranCode = titaVo.getTxcd();

		l2631ReportD.setParentTranCode(parentTranCode);

//		boolean isFinished = l9110Report.exec(titaVo, this.totaVo);

		l2631ReportD.exec(titaVo, this.totaVo);

		if ("Y".equals(titaVo.getParam("CollectFlag"))) {
			l2631ReportA.exec(titaVo);
			l2631ReportB.exec(titaVo);
			l2631ReportC.exec(titaVo);

			String checkMsg = "列印已完成。";
			webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "LC009",
					titaVo.getTlrNo() + "L2631", checkMsg, titaVo);
		}
		this.addList(this.totaVo);
		return this.sendList();
	}

	public void setTxTemp(String txtNo, TitaVo titaVo) throws LogicException {
		this.info("setTxTemp ... ");
		this.info("   titaVo.getEntDy() = " + parse.stringToInteger(titaVo.getEntDy()) + 19110000);
		this.info("   titaVo.getKinbr()  = " + titaVo.getKinbr());
		this.info("   titaVo.getTlrNo()  = " + titaVo.getTlrNo());
		this.info("   titaVo.getTxtNo()  = " + titaVo.getTxtNo());
		tTxTempId = new TxTempId();
		tTxTemp = new TxTemp();
		TempVo tTempVo = new TempVo();
		String wkSeqNo = titaVo.getTxtNo();
		tTxTempId.setEntdy(parse.stringToInteger(titaVo.getEntDy()) + 19110000);
		tTxTempId.setKinbr(titaVo.getKinbr());
		tTxTempId.setTlrNo(titaVo.getTlrNo());
		tTxTempId.setTxtNo(txtNo.isEmpty() ? titaVo.getTxtNo() : txtNo);
		tTxTempId.setSeqNo(wkSeqNo);
		tTxTemp.setEntdy(parse.stringToInteger(titaVo.getEntDy()) + 19110000);
		tTxTemp.setKinbr(titaVo.getKinbr());
		tTxTemp.setTlrNo(titaVo.getTlrNo());
		tTxTemp.setTxtNo(txtNo.isEmpty() ? titaVo.getTxtNo() : txtNo);
		tTxTemp.setSeqNo(wkSeqNo);
		tTxTemp.setTxTempId(tTxTempId);

		tTempVo.clear();
		tTempVo.putParam("CustNo", iCustNo);
		tTempVo.putParam("FacmNo", iFacmNo);
		tTempVo.putParam("ClCode1", parse.stringToInteger(titaVo.getParam("OOClCode1")));
		tTempVo.putParam("ClCode2", parse.stringToInteger(titaVo.getParam("OOClCode2")));
		tTempVo.putParam("ClNo", parse.stringToInteger(titaVo.getParam("OOClNo")));
		tTxTemp.setText(tTempVo.getJsonString());
		try {
			txTempService.insert(tTxTemp);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0005", "交易暫存檔 Key = " + tTxTempId); // 新增資料時，發生錯誤 }
		}
	}

	public void checkisDup(TitaVo titaVo) throws LogicException {
		this.info("checkisDup ....");

		Slice<FacClose> slFacClose = sFacCloseService.findCustNo(iCustNo, 0, Integer.MAX_VALUE, titaVo);
		// 檢核機制
		if (slFacClose != null) {
			for (FacClose t : slFacClose.getContent()) {
				if (t.getCloseDate() == 0 && "0".equals(t.getFunCode())) {
					// 同戶號額度未結案不可重複建檔
					if (iFacmNo == t.getFacmNo()) {
						throw new LogicException(titaVo, "E0005", "同戶號額度未結案不可重複建檔"); // 新增資料時，發生錯誤
					}
					// 額度輸入0則不可建其他額度資料
					// 額度有輸入則可輸入其他額度或0
					if (iFacmNo != 0 && t.getFacmNo() == 0) {
						throw new LogicException(titaVo, "E0005", "已有額度為0未結案資料"); // 新增資料時，發生錯誤
					}
				}
			}
		}
	}
}