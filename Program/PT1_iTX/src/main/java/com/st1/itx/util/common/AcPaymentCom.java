package com.st1.itx.util.common;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AcClose;
import com.st1.itx.db.domain.AcCloseId;
import com.st1.itx.db.domain.AcDetail;
import com.st1.itx.db.domain.BankRemit;
import com.st1.itx.db.domain.BankRemitId;
import com.st1.itx.db.domain.CdBank;
import com.st1.itx.db.domain.CdBankId;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.domain.LoanChequeId;
import com.st1.itx.db.domain.TxAmlLog;
import com.st1.itx.db.domain.LoanCheque;
import com.st1.itx.db.service.AcCloseService;
import com.st1.itx.db.service.BankRemitService;
import com.st1.itx.db.service.CdBankService;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.LoanChequeService;
import com.st1.itx.db.service.TxAmlLogService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.data.RemitFormVo;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.util.report.RemitForm;

/**
 * 收付欄處理<BR>
 * 1.run 收付欄(含溢收)出帳 call by L3XXX<BR>
 * 2.loanCheque 兌現票入帳更新支票檔 call by L3XXX<BR>
 * 3.remit 撥款匯款檔處理 call by L3100、L3220<BR>
 * 4.printRemitForm 產出單筆匯款單、存入憑條call by L3100、L3220<BR>
 * 
 * @author st1
 *
 */
@Component("AcPaymentCom")
@Scope("prototype")
public class AcPaymentCom extends TradeBuffer {

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Autowired
	public AcCloseService acCloseService;

	@Autowired
	public BankRemitService bankRemitService;

	@Autowired
	public LoanChequeService loanChequeService;

	@Autowired
	public AcNegCom acNegCom;

	@Autowired
	public RemitForm remitForm;

	@Autowired
	public CdBankService cdBankService;

	@Autowired
	public CdEmpService cdEmpService;

	@Autowired
	public TxAmlLogService txAmlLogService;

	private List<AcDetail> acDetailList;
	private AcDetail acDetail;
	private int acDate;
	private String titaTlrNo;
	private String titaTxtNo;
	private String RpFlag;
	private TempVo tTempVo;

	// initialize variable
	@PostConstruct
	public void init() {
		this.acDate = 0;
		this.titaTlrNo = "";
		this.titaTxtNo = "";
		this.RpFlag = "";
		this.acDetailList = new ArrayList<AcDetail>();
		this.acDetail = new AcDetail();
		new AcClose();
		this.tTempVo = new TempVo();
	}

	/*-----------  收付欄(含溢收)出帳  -------------- */
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("AcPayment run ...");
		acNegCom.setTxBuffer(this.txBuffer);
		acDate = this.txBuffer.getTxCom().getTbsdy();

		acDetailList = new ArrayList<AcDetail>();

		/* 1:應收 2:應付 */
		RpFlag = titaVo.getParam("RpFlag"); /* 收付記號 DECIMAL(1) */

		for (int i = 1; i <= 50; i++) {
			/* 還款來源／撥款方式為 0 者跳出 */
			if (titaVo.get("RpCode" + i) == null || parse.stringToInteger(titaVo.getParam("RpCode" + i)) == 0)
				break;
			addAcDetail(i, titaVo);
		}

		// 1->短收->此交易不處理(由各交易寫入短收記號資料)
		// 2->溢收->再加一筆溢收記號資料
		// 3->溢收(整批入帳、部分繳款)->再加一筆溢收記號資料
		if (titaVo.getParam("OverRpFg").equals("2") || titaVo.getParam("OverRpFg").equals("3")) {
			/* 寫入溢收 */
			addOverRp(titaVo);
		}

		/* 將處理完的AcDetail List 放回txBuffer */
		this.txBuffer.addAllAcDetailList(acDetailList);
		for (int i = 0; i < this.txBuffer.getAcDetailList().size(); i++) {
			this.info("AcPaymentCom AcDetailList = " + i + this.txBuffer.getAcDetailList().get(i));
		}
		return null;
	}

	/* 會計分錄明細 */
	private void addAcDetail(int i, TitaVo titaVo) throws LogicException {
		acDetail = new AcDetail();

		acDetail.setDscptCode(titaVo.get("RpDscpt" + i)); // 摘要代號
		acDetail.setSlipNote(titaVo.get("RpNote" + i)); // 摘要
		// 收付金額
		acDetail.setTxAmt(parse.stringToBigDecimal(titaVo.getParam("RpAmt" + i)));

		// 戶號 0123456-890-234
		acDetail.setCustNo(parse.stringToInteger(titaVo.getMrKey().substring(0, 7)));
		// 額度
		if (titaVo.getMrKey().length() >= 11 && titaVo.getMrKey().substring(7, 8).equals("-"))
			acDetail.setFacmNo(parse.stringToInteger(titaVo.getMrKey().substring(8, 11)));
		// 撥款序號
		if (titaVo.getMrKey().length() >= 15 && titaVo.getMrKey().substring(11, 12).equals("-"))
			acDetail.setBormNo(parse.stringToInteger(titaVo.getMrKey().substring(12, 15)));

		// 借貸別 應收-D 應付-C
		if ("1".equals(RpFlag))
			acDetail.setDbCr("D");
		else
			acDetail.setDbCr("C");

		// 彙總別：撥還共用(0XX)／還款來源(1xx)／撥款方式(2xx) 、業務科目對應表
// 1-應收  RpAcctCode (整批入帳檔帶入)
//       101.匯款轉帳 102.銀行扣款 103.員工扣款 104.支票兌現
//       105.法院扣薪 106.理賠金 107.代收款-債權協商 109.其他 111.匯款轉帳預先作業
//
// 2-應付
//		201:整批匯款                        P02 銀行存款－新光       
//		202:單筆匯款                        P02 銀行存款－新光
//		204:退款他行(匯款單)           P02 銀行存款－台新
//		205:核心匯款(整批匯款)        P02 銀行存款－新光
//		211:退款新光(存款憑條)        P03 銀行存款－新光
//		

// 0-共用
//      090.暫收抵繳    (額度)         TAV 暫收款－可抵繳
//      091:借新還舊                              TRO 暫收款－借新還舊
//      092:暫收轉帳     (戶號+額度)   TAV 暫收款－可抵繳
//      093:繳抽退票                              TCK 暫收款－支票
//      094:轉債協暫收款  (戶號)      T1x 債協暫收款      
//      095:轉債協退還款  (戶號)      T2x 債協退還款  
//      096:暫收沖正(戶號)           THC 暫收款－沖正

		if (parse.stringToInteger(titaVo.getParam("RpCode" + i)) >= 90) {
			acDetail.setSumNo("0" + FormatUtil.pad9(titaVo.getParam("RpCode" + i), 2));
		} else {
			acDetail.setSumNo(RpFlag + FormatUtil.pad9(titaVo.getParam("RpCode" + i), 2));
			if ("1".equals(RpFlag)) {
				acDetail.setAcctCode(titaVo.getParam("RpAcctCode" + i));
				String rpAcCode = FormatUtil.padX(titaVo.getParam("RpAcCode" + i).trim(), 18);
				acDetail.setAcNoCode(rpAcCode.substring(0, 11)); // 會科科子細目 11+5+2
				acDetail.setAcSubCode(rpAcCode.substring(11, 16));
				acDetail.setAcDtlCode(rpAcCode.substring(16, 18));
			}
			if ("2".equals(RpFlag)) {
				moveTitaToTempVo(i, titaVo); // 匯款資料
				acDetail.setJsonFields(tTempVo.getJsonString());
			}
		}

		// 銷帳編號
		acDetail.setRvNo(titaVo.get("RpRvno" + i)); /* 銷帳編號 VARCHAR2(30) */

		switch (acDetail.getSumNo()) {
		case "090":
			acDetail.setAcctCode("TAV");
			acDetail.setFacmNo(parse.stringToInteger(titaVo.getParam("RpFacmNo" + i)));
			break;
		case "091":
			acDetail.setAcctCode("TRO");
// 1:應收     L3410  結案   D       500,000     FacmNo002    原額度002
// 2:應付     L3100  撥貸   C       500,000     FacmNo002    新額度004，原額度002
			if (RpFlag.equals("1")) {
				acDetail.setRvNo("FacmNo" + titaVo.getMrKey().substring(8, 11));
				acDetail.setFacmNo(parse.stringToInteger(titaVo.getMrKey().substring(8, 11)));
			} else {
				acDetail.setRvNo("FacmNo" + titaVo.getParam("RpFacmNo" + i));
				acDetail.setFacmNo(parse.stringToInteger(titaVo.getParam("RpFacmNo" + i)));
				acDetail.setSlipNote(
						"新額度" + titaVo.getMrKey().substring(8, 11) + "，原額度" + titaVo.getParam("RpFacmNo" + i));
			}
			break;
		case "092":
			acDetail.setAcctCode("TAV");
			acDetail.setCustNo(parse.stringToInteger(titaVo.getParam("RpCustNo" + i)));
			acDetail.setFacmNo(parse.stringToInteger(titaVo.getParam("RpFacmNo" + i)));
			break;
		case "093":
			acDetail.setAcctCode("TCK");
			acDetail.setFacmNo(parse.stringToInteger(titaVo.getParam("RpFacmNo" + i)));
			break;
		case "094":
			acDetail.setFacmNo(0); // 額度不放
			acDetail.setCustNo(parse.stringToInteger(titaVo.getParam("RpCustNo" + i)));
			acDetail.setAcctCode(acNegCom.getAcctCode(acDetail.getCustNo(), titaVo));
			break;
		case "095":
			acDetail.setFacmNo(0); // 額度不放
			acDetail.setCustNo(parse.stringToInteger(titaVo.getParam("RpCustNo" + i)));
			acDetail.setAcctCode(acNegCom.getReturnAcctCode(acDetail.getCustNo(), titaVo));
			break;
		case "096":
			acDetail.setAcctCode("THC");
			acDetail.setFacmNo(parse.stringToInteger(titaVo.getParam("RpFacmNo" + i)));
			break;
		case "101": // 101.匯款轉帳
			acDetail.setAcctCode("P03");
			break;
		case "102": // 102.銀行扣款 C01 暫收款－非核心資金運用 核心銷帳碼 0010060yyymmdd (銀扣 ACH)
			if ("C01".equals(acDetail.getAcctCode())) {
				acDetail.setRvNo("0010060" + this.acDate);
			}
			break;
		case "103": // 103.員工扣款
			break;
		case "104": // 104.支票兌現
			break;
		// 其他還款來源有核心銷帳碼
		case "105": // 105.法院扣薪
		case "106": // 106.理賠金
		case "107": // 107.代收款-債權協商
		case "109": // 109.其他
			break;
		case "111": // 111.匯款轉帳預先作業
			break;
		case "201":
			acDetail.setAcctCode("P02");
			break;
		case "202":
			acDetail.setAcctCode("P02");
			break;
		case "204":
			acDetail.setAcctCode("P02");
			break;
		case "205":
			acDetail.setAcctCode("P02");
			break;
		case "211":
			acDetail.setAcctCode("P03");
			break;
		default:
			break;
		}
		acDetailList.add(acDetail);
	}

	/* 寫入溢收 */
	private void addOverRp(TitaVo titaVo) throws LogicException {
		this.info("AcPayment addOverRp ..." + titaVo.getParam("OverRpAmt"));
		acDetail = new AcDetail();
		acDetail.setDbCr("C");
		acDetail.setAcctCode("TAV");
		acDetail.setTxAmt(parse.stringToBigDecimal(titaVo.getParam("OverRpAmt")));
		acDetail.setCustNo(parse.stringToInteger(titaVo.getMrKey().substring(0, 7)));
		acDetail.setFacmNo(parse.stringToInteger(titaVo.getParam("OverRpFacmNo")));

		acDetailList.add(acDetail);
	}

	/* 匯款內容 */
	private void moveTitaToTempVo(int i, TitaVo titaVo) throws LogicException {
		tTempVo.clear();
		tTempVo.putParam("DrawdownCode", titaVo.getParam("RpCode" + i));
		tTempVo.putParam("RemitBank", titaVo.getParam("RpRemitBank" + i));
		tTempVo.putParam("RemitBranch", titaVo.getParam("RpRemitBranch" + i));
		tTempVo.putParam("RemitAcctNo", titaVo.getParam("RpRemitAcctNo" + i));
		tTempVo.putParam("CustName", titaVo.getParam("RpCustName" + i));
		tTempVo.putParam("Remark", titaVo.getParam("RpRemark" + i));
	}

	/**
	 * 兌現票入帳更新支票檔
	 * 
	 * @param titaVo TitaVo
	 * @return LoanCheque
	 * @throws LogicException LogicException
	 */
	public LoanCheque loanCheque(TitaVo titaVo) throws LogicException {
		this.info("AcPayment LoanCheque ...");
		int iCustNo = parse.stringToInteger(titaVo.getMrKey().substring(0, 7));
		String iRpRvno = titaVo.getParam("RpRvno1");
		int iChequeAcct = this.parse.stringToInteger(iRpRvno.substring(0, 9));
		int iChequeNo = this.parse.stringToInteger(iRpRvno.substring(10, 17));
		BigDecimal iRpAmt = this.parse.stringToBigDecimal(titaVo.getParam("RpAmt1"));

		this.info("   iRpRvno = " + iRpRvno);
		this.info("   iRpAmt = " + iRpAmt);
		this.info("   iChequeAcct = " + iChequeAcct);
		this.info("   iChequeNo = " + iChequeNo);

		LoanCheque tLoanCheque = loanChequeService.holdById(new LoanChequeId(iChequeAcct, iChequeNo), titaVo);
		if (tLoanCheque == null) {
			throw new LogicException(titaVo, "E0006",
					"戶號 = " + iCustNo + " 支票帳號 = " + iChequeAcct + " 支票號碼 = " + iChequeNo); // 鎖定資料時，發生錯誤
		}
		// 隔日訂正，來源科目回沖至暫收可抵繳，不處理
		if (titaVo.isHcodeErase() && titaVo.getEntDyI() != titaVo.getOrgEntdyI()) {
			this.info("BookAcHcode = 2, Skip Update");
			return tLoanCheque;
		}

		// 更新欄
		// StatusCode票據狀況碼 4: 兌現未入帳 -> 1: 兌現入帳
		// AcDate 會計日期
		// RepaidAmt已入帳金額

		// 正常交易
		if (titaVo.isHcodeNormal()) {
			if (!(tLoanCheque.getStatusCode().equals("1") || tLoanCheque.getStatusCode().equals("4"))) {
				throw new LogicException(titaVo, "E3057",
						"戶號 = " + iCustNo + " 支票帳號 = " + iChequeAcct + " 支票號碼 = " + iChequeNo); // 該票據狀況碼非為兌現，不可入帳
			}
			if (tLoanCheque.getChequeAmt().subtract(tLoanCheque.getRepaidAmt()).compareTo(iRpAmt) < 0) {
				throw new LogicException(titaVo, "E3058",
						"支票尚未入帳金額 = " + tLoanCheque.getChequeAmt().subtract(tLoanCheque.getRepaidAmt())); // 暫收金額超過支票尚未入帳金額
			}
			tLoanCheque.setStatusCode("1"); // 1: 兌現入帳
			tLoanCheque.setRepaidAmt(tLoanCheque.getRepaidAmt().add(iRpAmt));
			tLoanCheque.setAcDate(this.txBuffer.getTxCom().getTbsdy());
			tLoanCheque.setKinbr(titaVo.getKinbr());
			tLoanCheque.setTellerNo(titaVo.getTlrNo());
			tLoanCheque.setTxtNo(titaVo.getTxtNo());
		}
		// 訂正
		if (titaVo.isHcodeErase()) {
			tLoanCheque.setRepaidAmt(tLoanCheque.getRepaidAmt().subtract(iRpAmt));
			if (tLoanCheque.getRepaidAmt().compareTo(new BigDecimal(0)) > 0) {
				tLoanCheque.setStatusCode("1"); // 1: 兌現入帳
			} else {
				tLoanCheque.setStatusCode("4"); // 4: 兌現未入帳
				tLoanCheque.setAcDate(0);
				tLoanCheque.setKinbr("");
				tLoanCheque.setTellerNo("");
				tLoanCheque.setTxtNo("");
			}
		}
		try {
			loanChequeService.update(tLoanCheque, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007",
					"戶號 = " + iCustNo + " 支票帳號 = " + iChequeAcct + " 支票號碼 = " + iChequeNo + " " + e.getErrorMsg()); // 更新資料時，發生錯誤
		}
		return tLoanCheque;
	}

	/**
	 * 撥款匯款檔處理
	 * 
	 * @param titaVo TitaVo
	 * @throws LogicException LogicException
	 */
	public void remit(TitaVo titaVo) throws LogicException {
		acDate = this.txBuffer.getTxCom().getTbsdy();
		titaTlrNo = this.txBuffer.getTxCom().getRelTlr();
		titaTxtNo = String.format("%08d", this.txBuffer.getTxCom().getRelTno());
		this.info("remit ..." + acDate + "-" + titaTlrNo + "-" + titaTxtNo);
		for (int i = 1; i <= 5; i++) {
			/* 若為撥款匯款，產生撥款匯款檔 BankRemit */
			/* SecNo : 01:撥款匯款 */
			/* 撥款方式為 1 ~ 89 */
			if (titaVo.getSecNo().equals("01") && titaVo.get("RpCode" + i) != null) {
				if (parse.stringToInteger(titaVo.getParam("RpCode" + i)) > 0
						&& parse.stringToInteger(titaVo.getParam("RpCode" + i)) < 90) {

					/* BankRemit寫入 */
					procBankRemit(i, titaVo);
				}
			}
		}
	}

	/* 取值與決定寫入資料庫之動作：正常、修改、訂正 */
	private void procBankRemit(int i, TitaVo titaVo) throws LogicException {
		/* 對BankRemit 撥款匯款檔查詢 */
		String batchNo = "";
		BankRemitId tBankRemitId = new BankRemitId();
		tBankRemitId.setAcDate(acDate);
		tBankRemitId.setTitaTlrNo(titaTlrNo);
		tBankRemitId.setTitaTxtNo(titaTxtNo);
		BankRemit tBankRemit = bankRemitService.findById(tBankRemitId, titaVo);
		if (titaVo.isHcodeNormal() && titaVo.isActfgEntry()) {
			if (tBankRemit != null) {
				throw new LogicException("E0006", "撥款匯款檔已存在 " + tBankRemitId.toString());// 鎖定資料時，發生錯誤
			}
			batchNo = this.getBatchNo(parse.stringToInteger(titaVo.getParam("RpCode" + i)), titaVo);
		} else {
			if (tBankRemit == null) {
				throw new LogicException("E0006", "撥款匯款檔不存在 " + tBankRemitId.toString());// 鎖定資料時，發生錯誤
			}
			batchNo = tBankRemit.getBatchNo();
		}
		// set tita BatchNo
		titaVo.setBatchNo(batchNo);
		//
		boolean isUpdate = false;
		boolean isInsert = false;
		boolean isDelete = false;
		/* isHcodeNormal()正常交易且isActfgEntry() 登錄 */
		// 寫一筆新的進資料庫。 */
		if (titaVo.isHcodeNormal() && titaVo.isActfgEntry()) {
			tBankRemit = new BankRemit();
			tBankRemit = moveTitaToBankRemit(i, tBankRemit, titaVo);// 搬BankRemit內容
			tBankRemit.setBatchNo(batchNo);
			tBankRemit.setStatusCode(0); // 正常
			tBankRemit.setActFg(titaVo.getActFgI());
			isInsert = true;
		}

		/* isHcodeErase() 訂正交易 且 isActfgEntry() 登錄 */
		// 已有批號，狀態= 2:產檔後訂正，並提示產檔後訂正警告訊息。 */
		if (titaVo.isHcodeErase() && titaVo.isActfgEntry()) {
			if (isDoL4101(tBankRemit, titaVo)) {
				tBankRemit.setStatusCode(2); // 2:產檔後訂正
				tBankRemit.setActFg(titaVo.getActFgI());
				isUpdate = true;
				this.totaVo.setWarnMsg("產檔後訂正，批號=" + tBankRemit.getBatchNo());
				this.addList(this.totaVo);
			} else {
				isDelete = true;
			}
		}

		/* isHcodeModify()修正交易 */
		// 未有批號,直接更新內容
		// 已有批號，比對內容不同 ==> 狀態= 1:產檔後修正、內容放入產檔後修正後匯款單資料(jason)、提示產檔後修正警告訊息。
		if (titaVo.isHcodeModify()) {
			if (isDoL4101(tBankRemit, titaVo)) {
				BankRemit t2BankRemit = new BankRemit();
				t2BankRemit = moveTitaToBankRemit(i, t2BankRemit, titaVo);// 搬BankRemit內容
				if (isModifyRemit(tBankRemit, t2BankRemit)) {
					if (t2BankRemit.getDrawdownCode() != tBankRemit.getDrawdownCode()
							&& tBankRemit.getDrawdownCode() == 01) {
						tBankRemit.setStatusCode(4); // 4.產檔後改單筆匯款
					} else {
						tBankRemit.setStatusCode(3);// 3. 產檔後修正
					}
					tBankRemit.setModifyContent(tTempVo.getJsonString());
					isUpdate = true;
					this.totaVo.setWarnMsg("該筆匯款產檔後修正，批號=" + tBankRemit.getBatchNo());
					this.addList(this.totaVo);
				}
			} else {
				tBankRemit = moveTitaToBankRemit(i, tBankRemit, titaVo);// 搬BankRemit內容
				isUpdate = true;
			}
		}

		/* isHcodeNormal 且 isActfgSuprele()主管放行 */
		// 狀態= 1:產檔後修正，並提示產檔後修正警告訊息
		if (titaVo.isHcodeNormal() && titaVo.isActfgSuprele()) {
			tBankRemit.setActFg(2);
			isUpdate = true;
			if (tBankRemit.getStatusCode() == 3) {
				this.totaVo.setWarnMsg("該筆匯款產檔後修正，批號=" + tBankRemit.getBatchNo());
				this.addList(this.totaVo);
			}
		}

		/* isHcodeErase() 訂正交易 且 isActfgSuprele()主管放行 */
		// 狀態= 1:產檔後修正，並提示產檔後修正警告訊息
		if (titaVo.isHcodeErase() && titaVo.isActfgSuprele()) {
			isUpdate = true;
			tBankRemit.setActFg(1);
			if (isDoL4101(tBankRemit, titaVo)) {
				this.totaVo.setWarnMsg("該筆匯款資料已產檔，批號=" + tBankRemit.getBatchNo());
				this.addList(this.totaVo);
			}
		}

		// 放行更新AML回應碼
		if (titaVo.isHcodeNormal() && titaVo.isActfgRelease()) {
			// AML@交易序號：前兩碼03+會計日期+交易序號
			String transactionId = "03" + "-" + (tBankRemit.getAcDate() + 19110000) + "-";
			if (titaVo.isActfgSuprele()) {
				transactionId += titaVo.getOrgTxSeq();
			} else {
				transactionId += titaVo.getTxSeq();
			}
			TxAmlLog tTxAmlLog = txAmlLogService.findByTransactionIdFirst(tBankRemit.getAcDate() + 19110000,
					transactionId, titaVo);
			if (tTxAmlLog == null) {
				throw new LogicException(titaVo, "E0014", "TxAmlLog not found"); // 檔案錯誤
			}
			tBankRemit.setAmlRsp(tTxAmlLog.getConfirmStatus());
		}

		if (isInsert) {
			tBankRemit.setBankRemitId(tBankRemitId);
			try {
				bankRemitService.insert(tBankRemit, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005", "BankRemit " + tBankRemitId + " " + e.getErrorMsg()); // 新增資料時，發生錯誤
			}
		}
		if (isUpdate) {
			try {
				bankRemitService.update(tBankRemit, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", "BankRemit " + tBankRemitId + " " + e.getErrorMsg()); // 更新資料時，發生錯誤
			}
		}
		if (isDelete) {
			try {
				bankRemitService.delete(tBankRemit, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0008", "BankRemit " + tBankRemitId + " " + e.getErrorMsg()); // 刪除資料時，發生錯誤
			}
		}

	}

	// 是否已執行L4101-撥款匯款作業
	private boolean isDoL4101(BankRemit tBankRemit, TitaVo titaVo) throws LogicException {
		// LN+ 傳票批號 + 撥款批號 (整批匯款:' '、單筆匯款:'00')
		String remitBatchNo = tBankRemit.getBatchNo().substring(4, 6);
		if ("  ".equals(remitBatchNo) || "00".equals(remitBatchNo)) {
			return false;

		} else {
			return true;
		}
	}

	// 取得批號
	private String getBatchNo(int drawdownCode, TitaVo titaVo) throws LogicException {
		// LN+ 傳票批號 + 撥款批號 (整批匯款:' '、單筆匯款:'00')
		// LN為撥款
		// RT為退款
		String batchNo = "";
		if (drawdownCode == 1 || drawdownCode == 2) {
			batchNo = "LN";
		} else {
			batchNo = "RT";
		}
		AcCloseId tAcCloseId = new AcCloseId();
		tAcCloseId.setAcDate(this.txBuffer.getTxCom().getTbsdy());
		tAcCloseId.setBranchNo(titaVo.getAcbrNo());
		tAcCloseId.setSecNo("09"); // 業務類別: 01-撥款匯款 02-支票繳款 09-放款
		AcClose tAcClose = acCloseService.findById(tAcCloseId, titaVo);
		if (tAcClose == null) {
			batchNo += "01";
		} else {
			batchNo += parse.IntegerToString(tAcClose.getClsNo() + 1, 2);
		}
		if (drawdownCode == 1 || drawdownCode == 5) {
			batchNo += "  ";
		} else {
			batchNo += "00";
		}
		return batchNo;
	}

	/* 搬BankRemit內容 */
	private BankRemit moveTitaToBankRemit(int i, BankRemit tBankRemit, TitaVo titaVo) throws LogicException {
		acDate = this.txBuffer.getTxCom().getTbsdy();
		titaTlrNo = this.txBuffer.getTxCom().getRelTlr();
		titaTxtNo = String.format("%08d", this.txBuffer.getTxCom().getRelTno());
		tBankRemit.setAcDate(acDate); /* 會計日期 DECIMAL(8) */
		tBankRemit.setTitaTlrNo(titaTlrNo);// 經辦
		tBankRemit.setTitaTxtNo(titaTxtNo);// 交易序號
		tBankRemit.setDrawdownCode(parse.stringToInteger(titaVo.getParam("RpCode" + i)));
		tBankRemit.setRemitBank(titaVo.getParam("RpRemitBank" + i));
		tBankRemit.setRemitBranch(titaVo.getParam("RpRemitBranch" + i));
		tBankRemit.setRemitAcctNo(titaVo.getParam("RpRemitAcctNo" + i));
		tBankRemit.setCustNo(parse.stringToInteger(titaVo.getMrKey().substring(0, 7)));
		if (titaVo.getMrKey().length() >= 11 && titaVo.getMrKey().substring(7, 8).equals("-")) {
			tBankRemit.setFacmNo(parse.stringToInteger(titaVo.getMrKey().substring(8, 11)));
		}
		if (titaVo.getMrKey().length() >= 15 && titaVo.getMrKey().substring(11, 12).equals("-")) {
			tBankRemit.setBormNo(parse.stringToInteger(titaVo.getMrKey().substring(12, 15)));
		}
		tBankRemit.setCustName(titaVo.getParam("RpCustName" + i));
		tBankRemit.setRemark(titaVo.getParam("RpRemark" + i));
		tBankRemit.setCurrencyCode(titaVo.getCurName());
		tBankRemit.setRemitAmt(parse.stringToBigDecimal(titaVo.getParam("RpAmt" + i)));
		tBankRemit.setCustId(titaVo.get("RemitId"));
		tBankRemit.setCustBirthday(parse.stringToInteger(titaVo.get("RemitBirthday")));
		tBankRemit.setCustGender(titaVo.get("RemitGender"));
		return tBankRemit;

		/* --------設定建值 End-------- */
	}

	/* 資料比對 tBankRemit:原內容 t2BankRemit新內容 */
	private Boolean isModifyRemit(BankRemit tBankRemit, BankRemit t2BankRemit) throws LogicException {

		tTempVo.clear();
		Boolean isModify = true;
		if (t2BankRemit.getDrawdownCode() == tBankRemit.getDrawdownCode()
				&& t2BankRemit.getRemitBank().equals(tBankRemit.getRemitBank())
				&& t2BankRemit.getRemitBranch().equals(tBankRemit.getRemitBranch())
				&& t2BankRemit.getRemitAcctNo().equals(tBankRemit.getRemitAcctNo())
				&& t2BankRemit.getCustName().equals(tBankRemit.getCustName())
				&& t2BankRemit.getRemark().equals(tBankRemit.getRemark())
				&& t2BankRemit.getRemitAmt().equals(tBankRemit.getRemitAmt())) {
			isModify = false;
		}
		if (isModify) {
			if (t2BankRemit.getDrawdownCode() != tBankRemit.getDrawdownCode()) {
				tTempVo.putParam("DrawdownCode", t2BankRemit.getDrawdownCode());
			}
			if (!t2BankRemit.getRemitBank().equals(tBankRemit.getRemitBank())) {
				tTempVo.putParam("RemitBank", t2BankRemit.getRemitBank());
			}
			if (!t2BankRemit.getRemitBranch().equals(tBankRemit.getRemitBranch())) {
				tTempVo.putParam("RemitBranch", t2BankRemit.getRemitBranch());
			}
			if (!t2BankRemit.getRemitAcctNo().equals(tBankRemit.getRemitAcctNo())) {
				tTempVo.putParam("RemitAcctNo", t2BankRemit.getRemitAcctNo());
			}
			if (!t2BankRemit.getCustName().equals(tBankRemit.getCustName())) {
				tTempVo.putParam("CustName", t2BankRemit.getCustName());
			}
			if (!t2BankRemit.getRemark().equals(tBankRemit.getRemark())) {
				tTempVo.putParam("Remark", t2BankRemit.getRemark());
			}
			if (!t2BankRemit.getRemitAmt().equals(tBankRemit.getRemitAmt())) {
				tTempVo.putParam("RemitAmt", t2BankRemit.getRemitAmt());
			}
		}
		this.info("isModify=" + isModify + tTempVo.toString());
		return isModify;
	}

	/**
	 * 產出單筆匯款單、存入憑條
	 * 
	 * @param titaVo TitaVo
	 * @return 檔案序號
	 * @throws LogicException LogicException
	 */
	public long printRemitForm(TitaVo titaVo) throws LogicException {

		long sno = 0;

		RemitFormVo remitformVo = new RemitFormVo();

		BankRemit tBankRemit = new BankRemit();
		BankRemitId tBankRemitId = new BankRemitId();

		acDate = this.txBuffer.getTxCom().getTbsdy();
		titaTlrNo = this.txBuffer.getTxCom().getRelTlr();
		titaTxtNo = String.format("%08d", this.txBuffer.getTxCom().getRelTno());
		tBankRemitId.setAcDate(acDate);
		tBankRemitId.setTitaTlrNo(titaTlrNo);
		tBankRemitId.setTitaTxtNo(titaTxtNo);

		tBankRemit = bankRemitService.findById(tBankRemitId, titaVo);

		this.info("tBankRemit ..." + tBankRemit.toString());

		if (tBankRemit != null) {
			// 報表代號(交易代號)
			remitformVo.setReportCode(titaVo.getTxCode());
			// 報表說明(預設為"國內匯款申請書(兼取款憑條)")
			remitformVo.setReportItem("國內匯款申請書(兼取款憑條)_" + tBankRemit.getCustNo() + "_" + tBankRemit.getFacmNo() + "_"
					+ tBankRemit.getBormNo());

			remitForm.open(titaVo, remitformVo);

			remitformVo = new RemitFormVo();

			CdBank tCdBank = new CdBank();
			CdBankId tCdBankId = new CdBankId();

			tCdBankId.setBankCode(tBankRemit.getRemitBank());
			tCdBankId.setBranchCode(tBankRemit.getRemitBranch());

			tCdBank = cdBankService.findById(tCdBankId, titaVo);

			CdEmp tCdEmp = new CdEmp();

			tCdEmp = cdEmpService.findById(tBankRemit.getTitaTlrNo(), titaVo);

//			01:整批匯款 02:單筆匯款 04:退款台新(存款憑條) 05:退款他行(整批匯款) 11:退款新光(存款憑條)
//			跳過單筆
			if (tBankRemit.getDrawdownCode() != 1 || tBankRemit.getDrawdownCode() != 5) {
				// 報表代號(交易代號)
				remitformVo.setReportCode(titaVo.getTxCode());

				// 報表說明(預設為"國內匯款申請書(兼取款憑條)")
				remitformVo.setReportItem("國內匯款申請書(兼取款憑條)_" + tBankRemit.getCustNo() + "_" + tBankRemit.getFacmNo()
						+ "_" + tBankRemit.getBormNo());

				// 申請日期(民國年)
				remitformVo.setApplyDay(titaVo.getEntDyI());

				// 取款金額記號:1.同匯款金額 2.同匯款金額及手續費
//						remitformVo.setAmtFg(2);

				// 取款帳號
//						remitformVo.setWithdrawAccount("1234567890");

				// 銀行記號:1.跨行 2.聯行 3.國庫 4.同業 5.證券 6.票券
				remitformVo.setBankFg(1);

				if (tCdBank != null) {
					// 收款行-銀行
					remitformVo.setReceiveBank(tCdBank.getBankItem());
					// 收款行-分行
					remitformVo.setReceiveBranch(tCdBank.getBranchItem());
				}

				// 財金費
				remitformVo.setFiscFeeAmt(0);

				// 手續費
				remitformVo.setNormalFeeAmt(0);

				// 收款人-帳號
				remitformVo.setReceiveAccount(tBankRemit.getRemitAcctNo());

				// 收款人-戶名
				remitformVo.setReceiveName(tBankRemit.getCustName());

				if (tCdEmp != null) {
					// 匯款代理人
					remitformVo.setAgentName(tCdEmp.getFullname());

					// 匯款代理人身份證號碼
					remitformVo.setAgentId(tCdEmp.getAgentId());
				}

				// 匯款人代理人電話
				remitformVo.setAgentTel("戶號" + parse.IntegerToString(tBankRemit.getCustNo(), 7));

				// 匯款金額
				remitformVo.setRemitAmt(parse.stringToInteger("" + tBankRemit.getRemitAmt()));

				// 匯款人名稱
				remitformVo.setRemitName("新光人壽保險股份有限公司");

				// 匯款人統一編號
				remitformVo.setRemitId("03458902");

				// 匯款人電話
				remitformVo.setRemitTel("23895858");

				// 附言
				remitformVo.setNote(tBankRemit.getRemark());

				remitForm.addpage(titaVo, remitformVo);

				sno = remitForm.close();

				remitForm.toPdf(sno);
			} else {
				this.info("Continue... DrawdownCode = " + tBankRemit.getDrawdownCode());
			}
		}
		return sno;
	}
}
