package com.st1.itx.trade.L2;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
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
import com.st1.itx.db.domain.LoanBorMain;
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

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 報表服務注入 */
	@Autowired
	public L2634ReportA L2076Report;
	@Autowired
	public L2634ReportB L2076ReportB;
	@Autowired
	public L2634ReportC L2076ReportC;
	@Autowired
	public L2634ReportD L2076ReportD;
	@Autowired
	public L2634ReportE L2076ReportE;

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
		this.info("active L2631 ");
		this.totaVo.init(titaVo);
		L2076Report.setTxBuffer(txBuffer);
		L2076ReportB.setTxBuffer(txBuffer);
		L2076ReportC.setTxBuffer(txBuffer);
		L2076ReportD.setTxBuffer(txBuffer);
		// new PK
		FacCloseId tFacCloseId = new FacCloseId();
		// new table
		FacClose tFacCloseMaxCloseNo = new FacClose();
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
		tFacCloseMaxCloseNo = sFacCloseService.findMaxCloseNoFirst(iCustNo);
		if (tFacCloseMaxCloseNo != null) {
			wkcloseNo = tFacCloseMaxCloseNo.getCloseNo() + 1;
		}
		this.info("清償序號 = " + wkcloseNo);

		tFacCloseId.setCustNo(iCustNo);
		tFacCloseId.setCloseNo(wkcloseNo);

		tFacClose.setFacCloseId(tFacCloseId);
		if ("0".equals(iFunCode)) {

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
		tFacClose.setEntryDate(iTranDate);
		tFacClose.setRmk(titaVo.getParam("Rmk1"));
		// **[清償違約金即時收取]部分償還時一律記短收，連同下期期款收回，提前結案時計算清償違約金併入結案金額。
		// **[清償違約金領清償證明收取]<清償請領>結案時計算全部的部分償還及提前結案的清償違約金，併入結案金額。
		// [清償違約金領清償證明收取]<清償補領>計算全部的部分償還及提前結案的清償違約金，L3200收取清償違約金領後，才可列印清償證明。
		if ("2".equals(iFunCode)) {
			if ("".equals(titaVo.getParam("RpFg")) && iCloseAmt.compareTo(BigDecimal.ZERO) > 0) {
				// 短繳清償違約金處理, 新增銷帳檔
				List<AcReceivable> lAcReceivable = new ArrayList<AcReceivable>();
				AcReceivable tAcReceivable = new AcReceivable();
				tAcReceivable.setReceivableFlag(3); // 未收費用
				tAcReceivable.setAcctCode("YOP");
				tAcReceivable.setCustNo(iCustNo);
				tAcReceivable.setFacmNo(iFacmNo);
				tAcReceivable.setRvNo("000");
				tAcReceivable.setRvAmt(iCloseAmt);
				lAcReceivable.add(tAcReceivable);
				acReceivableCom.setTxBuffer(this.getTxBuffer());
				acReceivableCom.mnt(0, lAcReceivable, titaVo); // 0-起帳
			}
			// 收取清償違約金後 (L3200)，才能列印清償證明
			if ("7".equals(titaVo.getParam("RpFg"))) {
				// 查詢各項費用
				baTxCom.setTxBuffer(this.getTxBuffer());
				baTxCom.settingUnPaid(iTranDate, iCustNo, iFacmNo, 0, 99, BigDecimal.ZERO, titaVo); // 99-費用全部(含未到期)
				if (baTxCom.getShortCloseBreach().compareTo(BigDecimal.ZERO) > 0) {
					throw new LogicException(titaVo, "E3094", "清償違約金 = " + baTxCom.getShortCloseBreach()); // 不可有短繳金額
				}
			}
		}

		if ("7".equals(titaVo.getParam("RpFg"))) {

			int wkFacmNoSt = 1;
			int wkFacmNoEd = 999;
			if (iFacmNo > 0) {
				wkFacmNoSt = iFacmNo;
				wkFacmNoEd = iFacmNo;
			}
			// 擔保品與額度關聯檔
			Slice<ClFac> slClFac = clFacService.selectForL2017CustNo(iCustNo, wkFacmNoSt, wkFacmNoEd, 0, Integer.MAX_VALUE, titaVo);
			lClFac = slClFac == null ? null : slClFac.getContent();
			// 全部結案
			boolean isAllClose = true;
			for (ClFac c : lClFac) {
				if ((c.getCustNo() == iCustNo && iFacmNo == 0) || (c.getCustNo() == iCustNo && c.getFacmNo() == iFacmNo)) {

					// 撥款主檔
					Slice<LoanBorMain> slLoanBorMain = loanBorMainService.bormCustNoEq(c.getCustNo(), c.getFacmNo(), c.getFacmNo(), 1, 900, 0, Integer.MAX_VALUE, titaVo);
					if (slLoanBorMain != null) {
						for (LoanBorMain t : slLoanBorMain.getContent()) {
							// 戶況 0: 正常戶1:展期2: 催收戶3: 結案戶4: 逾期戶5: 催收結案戶6: 呆帳戶7: 部分轉呆戶8: 債權轉讓戶9: 呆帳結案戶
							if (t.getStatus() == 0 || t.getStatus() == 2 || t.getStatus() == 4 || t.getStatus() == 6 || t.getStatus() == 8) {
								isAllClose = false;
								break;
							}
						}
					}
				}
			}
			if (!isAllClose) {
				throw new LogicException(titaVo, "E2074", "未全部結案不可列印清償證明 "); // 未全部結案，不可列印
			}

			BeforeFacClose = sFacCloseService.holdById(new FacCloseId(iCustNo, iCloseNo), titaVo);
			// 自動取公文編號
			this.txBuffer.getTxCom();
			wkDocNo = gGSeqCom.getSeqNo(titaVo.getEntDyI() / 10000, 1, "L2", "2631", 9999, titaVo);
			String finalDocNo = StringUtils.leftPad(String.valueOf(wkDocNo), 4, "0");

			this.info("BeforeFacClose = " + BeforeFacClose);
			String docNo = titaVo.getCalDy().substring(0, 3) + finalDocNo;
			tFacClose.setFunCode("3"); // 列印後固定為3補發
			tFacClose.setActFlag(BeforeFacClose.getActFlag());
			tFacClose.setApplDate(BeforeFacClose.getApplDate());
			tFacClose.setCloseInd(BeforeFacClose.getCloseInd());
			tFacClose.setCloseDate(BeforeFacClose.getCloseDate());
			tFacClose.setCloseAmt(BeforeFacClose.getCloseAmt());
			tFacClose.setAgreeNo(BeforeFacClose.getAgreeNo());
			tFacClose.setCollectFlag("Y");
			tFacClose.setEntryDate(BeforeFacClose.getEntryDate());
			tFacClose.setDocNo(parse.stringToInteger(docNo));
			tFacClose.setClsNo(BeforeFacClose.getClsNo());
			tFacClose.setRmk(titaVo.getParam("Rmk1"));
		}
		this.info("tFacClose  = " + tFacClose);
		try {
			sFacCloseService.insert(tFacClose, titaVo);
		} catch (DBException e) {
			throw new LogicException("E0005", "清償作業檔");
		}

		if ("7".equals(titaVo.getParam("RpFg"))) {

			try {
				sFacCloseService.update(BeforeFacClose, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "清償作業檔");
			}
			// 總筆數
			this.info("selectTotal = " + titaVo.get("selectTotal"));
			// 當前筆數
			this.info("selectIndex = " + titaVo.get("selectIndex"));

			String txtNo = "";
			if (parse.stringToInteger(titaVo.get("selectIndex")) == 1) {
				setTxTemp(txtNo, titaVo);
			} else {
				if (titaVo.getSelectReturnOK() == 0) {
					setTxTemp(txtNo, titaVo);
				} else {
					TxTemp t2TxTemp = txTempService.txtNoLastFirst(titaVo.getTlrNo(), titaVo);
					if (t2TxTemp != null) {
						txtNo = t2TxTemp.getTxtNo();
					}
					setTxTemp(txtNo, titaVo);
				}

			}

			if (titaVo.get("selectTotal") == null || titaVo.get("selectTotal").equals(titaVo.get("selectIndex"))) {
				String t3txtNo = "";
				TxTemp t3TxTemp = txTempService.txtNoLastFirst(titaVo.getTlrNo(), titaVo);
				if (t3TxTemp != null) {
					t3txtNo = t3TxTemp.getTxtNo();
				}
				Slice<TxTemp> slTxTemp = txTempService.txTempTxtNoEq(titaVo.getEntDyI() + 19110000, titaVo.getKinbr(), titaVo.getTlrNo(), t3txtNo, this.index, Integer.MAX_VALUE, titaVo);
				lTxTemp = slTxTemp == null ? null : slTxTemp.getContent();
				if (lTxTemp == null || lTxTemp.size() == 0) {
					throw new LogicException(titaVo, "E0001",
							"交易暫存檔 會計日期 = " + parse.stringToInteger(titaVo.getEntDy()) + 19110000 + "分行別 = " + titaVo.getKinbr() + " 交易員代號 = " + titaVo.getTlrNo() + " 交易序號 = " + t3txtNo); // 查詢資料不存在
				}

				String checkMsg = "抵押權塗銷同意書已完成。";
				webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "LC009", titaVo.getTlrNo() + "L2631", checkMsg, titaVo);
			}
		}

		this.totaVo.putParam("OCloseNo", wkcloseNo);

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
}