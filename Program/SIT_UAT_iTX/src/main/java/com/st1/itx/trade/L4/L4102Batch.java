package com.st1.itx.trade.L4;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AcClose;
import com.st1.itx.db.domain.AcCloseId;
import com.st1.itx.db.domain.AcDetail;
import com.st1.itx.db.domain.BankRemit;
import com.st1.itx.db.domain.CdBank;
import com.st1.itx.db.domain.CdBankId;
import com.st1.itx.db.domain.CdBcm;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.FacMainId;
import com.st1.itx.db.service.AcCloseService;
import com.st1.itx.db.service.AcDetailService;
import com.st1.itx.db.service.BankRemitService;
import com.st1.itx.db.service.CdAcCodeService;
import com.st1.itx.db.service.CdBankService;
import com.st1.itx.db.service.CdBcmService;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.FileCom;
import com.st1.itx.util.common.MakeFile;
import com.st1.itx.util.common.data.BankRemitFileVo;
import com.st1.itx.util.common.data.RemitFormVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.util.report.RemitForm;

/**
 * Tita<br>
 * ACCTDATE=9,7<br>
 * BATNO=X,6<br>
 * END=X,1<br>
 */

@Service("L4102Batch")
@Scope("prototype")
/**
 * 撥款匯款作業
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4102Batch extends TradeBuffer {

	@Autowired
	public Parse parse;
	@Autowired
	public DateUtil dateUtil;
	@Autowired
	public FileCom fileCom;
	@Autowired
	public TotaVo totaA;
	@Autowired
	public TotaVo totaB;
	@Autowired
	public BankRemitService bankRemitService;
	@Autowired
	public BankRemitFileVo bankRemitFileVo;
	@Autowired
	public AcDetailService acDetailService;
	@Autowired
	public CdAcCodeService cdAcCodeService;
	@Autowired
	public CustMainService custMainService;
	@Autowired
	public FacMainService facMainService;
	@Autowired
	public CdBcmService cdBcmService;
	@Autowired
	public AcCloseService acCloseService;
	@Autowired
	public MakeFile makeFile;
	@Autowired
	RemitForm remitForm;
	@Autowired
	public WebClient webClient;
	@Autowired
	CdBankService cdBankService;
	@Autowired
	CdEmpService cdEmpService;

	/* 報表服務注入 */
	@Autowired
	L4101ReportA l4101ReportA;
	@Autowired
	L4101ReportC l4101ReportC;

	@Value("${iTXOutFolder}")
	private String outFolder = "";

	int acDate = 0;
	String batchNo = "";

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4102Batch ");

		acDate = parse.stringToInteger(titaVo.getParam("AcDate")) + 19110000;
		batchNo = this.getBatchNo(titaVo);
		String wkbatchNo = titaVo.getBacthNo();
		this.info("L4102 Batch batchNo = " + batchNo);

		// 分錄
		List<AcDetail> lAcDetail = new ArrayList<AcDetail>();

		Slice<AcDetail> slAcDetail = acDetailService.acdtlTitaBatchNo(titaVo.getAcbrNo(), titaVo.getCurName(), acDate,
				batchNo, 0, Integer.MAX_VALUE, titaVo);
		lAcDetail = slAcDetail == null ? null : new ArrayList<AcDetail>(slAcDetail.getContent());

		totaVo.put("PdfSnoM", "");
		totaVo.put("PdfSnoF", "");

//		產出撥款傳票
		doRptA(titaVo);
//		傳票明細表
		doRptC(titaVo);

		String checkMsg = "撥款匯款產檔已完成。   批號 = " + wkbatchNo;

		webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "LC009", titaVo.getTlrNo(),
				checkMsg, titaVo);

		return this.sendList();
	}

	private void procBankRemitMedia(List<BankRemit> lBankRemit, TitaVo titaVo) throws LogicException {

//		String path = outFolder + "LNM24p.txt";

		ArrayList<OccursList> tmp = new ArrayList<>();

		int seq = 0;

		for (BankRemit tBankRemit : lBankRemit) {

			if (tBankRemit.getDrawdownCode() == 2 || tBankRemit.getDrawdownCode() == 4
					|| tBankRemit.getDrawdownCode() == 11) {
				this.info("Continue... DrawdownCode = " + tBankRemit.getDrawdownCode());
				continue;
			}
			seq = seq + 1;

			OccursList occursList = new OccursList();

//			DataSeq		序號			4	0	4
//			AcctNo		帳號			X	14	4	18
//			Amount		金額			X	13	18	31
//			UnitCode	解付單位代號	X	7	31	38
//			RemitName	代償專戶		X	59	38	97
//			ColumnA	新光人壽保險股份有限公司─放款服務課	X	35	97	132
//			ColumnB		space		X	59	132	191
//			ColumnC		00174		X	5	191	196
//			RemitDate	匯款日期		X	8	196	204
//			BatchNo		批號			X	2	204	206

			occursList.putParam("DataSeq", FormatUtil.pad9("" + seq, 4));
			occursList.putParam("AcctNo", FormatUtil.padX(tBankRemit.getRemitAcctNo(), 14));
			occursList.putParam("Amount", FormatUtil.pad9("" + tBankRemit.getRemitAmt(), 13));
			occursList.putParam("UnitCode", "" + FormatUtil.pad9("" + tBankRemit.getRemitBank(), 3)
					+ FormatUtil.pad9("" + tBankRemit.getRemitBranch(), 4));
			occursList.putParam("RemitName", FormatUtil.padX(tBankRemit.getCustName(), 59));
			occursList.putParam("ColumnA", "新光人壽保險股份有限公司─放款服務課");
			occursList.putParam("ColumnB", FormatUtil.padX("", 59));
			occursList.putParam("ColumnC", "00174");
			occursList.putParam("RemitDate", tBankRemit.getAcDate() + 19110000);
			occursList.putParam("BatchNo", tBankRemit.getBatchNo().substring(4, 6));

			tmp.add(occursList);
		}
		// 把明細資料容器裝到檔案資料容器內
		bankRemitFileVo.setOccursList(tmp);
		// 轉換資料格式
		ArrayList<String> file = bankRemitFileVo.toFile();

		makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), titaVo.getTxCode(),
				titaVo.getTxCode() + "-撥款匯款媒體檔", "LNM24p.txt", 2);

		for (String line : file) {
			makeFile.put(line);
		}

		long snoM = makeFile.close();

		this.info("snoM : " + snoM);

		makeFile.toFile(snoM);
		totaVo.put("PdfSnoM", "" + snoM);

	}

	private String getBatchNo(TitaVo titaVo) throws LogicException {
		String batchNo = "";
		AcCloseId tAcCloseId = new AcCloseId();
		tAcCloseId.setAcDate(this.txBuffer.getTxCom().getTbsdy());
		tAcCloseId.setBranchNo(titaVo.getAcbrNo());
		tAcCloseId.setSecNo("09"); // 業務類別: 01-撥款匯款 02-支票繳款 09-放款
		AcClose tAcClose = acCloseService.findById(tAcCloseId, titaVo);
		if (tAcClose == null) {
			throw new LogicException(titaVo, "E0001", "無帳務資料"); // 查詢資料不存在
		}
		batchNo = "LN" + parse.IntegerToString(tAcClose.getClsNo() + 1, 2) + "  ";
		return batchNo;
	}

	// 更新批號
	private String updBatchNo(String batchNo, TitaVo titaVo) throws LogicException {
		AcCloseId tAcCloseId = new AcCloseId();
		tAcCloseId.setAcDate(this.txBuffer.getTxCom().getTbsdy());
		tAcCloseId.setBranchNo(titaVo.getAcbrNo());
		tAcCloseId.setSecNo("01"); // 業務類別: 01-撥款匯款 02-支票繳款 09-放款
		AcClose tAcClose = acCloseService.holdById(tAcCloseId, titaVo);
		if (tAcClose == null) {
			throw new LogicException(titaVo, "E0001", "無撥款帳務資料"); // 查詢資料不存在
		}
		batchNo = batchNo.trim() + parse.IntegerToString(tAcClose.getBatNo(), 2);
		tAcClose.setBatNo(tAcClose.getBatNo() + 1);
		try {
			acCloseService.update(tAcClose, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007", e.getErrorMsg()); // 更新資料時，發生錯誤
		}
		return batchNo;
	}

//	產出整批匯款單
	private long printRemitForm(List<BankRemit> lBankRemit, TitaVo titaVo) throws LogicException {

		long sno = 0;
		int cnt = 0;

		if (lBankRemit.size() > 0) {
			RemitFormVo remitformVo = new RemitFormVo();

			// 報表代號(交易代號)
			remitformVo.setReportCode(titaVo.getTxCode());
			// 報表說明(預設為"國內匯款申請書(兼取款憑條)")
			remitformVo.setReportItem("國內匯款申請書(兼取款憑條)_整批");

			remitForm.open(titaVo, remitformVo);

			for (BankRemit tBankRemit : lBankRemit) {
				remitformVo = new RemitFormVo();

				CdBank tCdBank = new CdBank();
				CdBankId tCdBankId = new CdBankId();

				tCdBankId.setBankCode(tBankRemit.getRemitBank());
				tCdBankId.setBranchCode(tBankRemit.getRemitBranch());

				tCdBank = cdBankService.findById(tCdBankId, titaVo);

				CdEmp tCdEmp = new CdEmp();

				tCdEmp = cdEmpService.findById(tBankRemit.getTitaTlrNo(), titaVo);

//				01:整批匯款 02:單筆匯款 04:退款台新(存款憑條) 05:退款他行(整批匯款) 11:退款新光(存款憑條)
//				跳過單筆
				if (tBankRemit.getDrawdownCode() == 2 || tBankRemit.getDrawdownCode() == 4
						|| tBankRemit.getDrawdownCode() == 11) {
					this.info("Continue... DrawdownCode = " + tBankRemit.getDrawdownCode());
					continue;
				}

				cnt = cnt + 1;

				// 報表代號(交易代號)
				remitformVo.setReportCode(titaVo.getTxCode());

				// 報表說明(預設為"國內匯款申請書(兼取款憑條)")
				remitformVo.setReportItem("國內匯款申請書(兼取款憑條)_整批" + tBankRemit.getBatchNo());

				// 申請日期(民國年)
				remitformVo.setApplyDay(titaVo.getEntDyI());

				// 取款金額記號:1.同匯款金額 2.同匯款金額及手續費
//				remitformVo.setAmtFg(2);

				// 取款帳號
//				remitformVo.setWithdrawAccount("1234567890");

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
				remitformVo.setRemitTel("23895858#7086");

				// 附言
				remitformVo.setNote(tBankRemit.getRemark());

				remitForm.addpage(titaVo, remitformVo);
			}
		}

		if (cnt >= 0) {
			sno = remitForm.close();

			remitForm.toPdf(sno);
		}
		return sno;
	}

//	整批匯款單明細報表
	private void procReportB(List<BankRemit> lBankRemit, TitaVo titaVo) {
		this.info("setReportB .... ");

		HashMap<tmpFacm, BigDecimal> remitAmt = new HashMap<>();
		BigDecimal sum = BigDecimal.ZERO;
//		沖轉總計
		int corCnt = 0;
//		客戶總計
		int custCnt = 0;
//		行數
		int rounds = 1;

//		第一筆 與第二筆比較用
		tmpFacm oldTmp = new tmpFacm(0, 0);

		for (BankRemit tBankRemit : lBankRemit) {
			tmpFacm tmp = new tmpFacm(tBankRemit.getCustNo(), tBankRemit.getFacmNo());

			OccursList occursList = new OccursList();

			CustMain tCustMain = new CustMain();
			tCustMain = custMainService.custNoFirst(tBankRemit.getCustNo(), tBankRemit.getCustNo(), titaVo);

			FacMain tFacMain = facMainService.findById(new FacMainId(tBankRemit.getCustNo(), tBankRemit.getFacmNo()),
					titaVo);
			CdBcm tCdBcm = new CdBcm();
			if (tFacMain != null) {
				tCdBcm = cdBcmService.distCodeFirst(tFacMain.getDistrict(), titaVo);
			}

			if (rounds == 1) {
				oldTmp = new tmpFacm(tBankRemit.getCustNo(), tBankRemit.getFacmNo());
				remitAmt.put(tmp, tBankRemit.getRemitAmt());
				custCnt = custCnt + 1;
			} else {
				if (remitAmt.containsKey(tmp)) {
					remitAmt.put(tmp, remitAmt.get(tmp).add(tBankRemit.getRemitAmt()));
				} else {

					this.info(" tmp : " + tmp);
					occursList = new OccursList();
					occursList.putParam("ReportBLine", FormatUtil.padLeft("小計", 91)
							+ FormatUtil.padLeft("" + amtFormat(remitAmt.get(oldTmp)), 18) + FormatUtil.padX("", 22));
					totaB.addOccursList(occursList);

					occursList = new OccursList();
					occursList.putParam("ReportBLine", addMark("-", 200));
					totaB.addOccursList(occursList);

					remitAmt.put(tmp, tBankRemit.getRemitAmt());

					oldTmp = new tmpFacm(tBankRemit.getCustNo(), tBankRemit.getFacmNo());
					custCnt = custCnt + 1;
				}
			}

			Slice<AcDetail> slAcDetail = acDetailService.acdtlRelTxseqEq(acDate,
					titaVo.getKinbr() + tBankRemit.getTitaTlrNo() + tBankRemit.getTitaTxtNo(), acDate, 0,
					Integer.MAX_VALUE, titaVo);

//			沖轉 1.是 0.否
			int corFlag = 1;

			if (slAcDetail != null) {
				corFlag = 0;
			} else {
				corCnt = corCnt + 1;
			}

			String distItem = "";
			BigDecimal lineAmt = BigDecimal.ZERO;
			String businessOfficer = "";
			if (tFacMain != null) {
				lineAmt = tFacMain.getLineAmt();
				if (tCdBcm != null)
					distItem = tCdBcm.getDistItem();
				businessOfficer = tFacMain.getBusinessOfficer();
			}

			occursList = new OccursList();
			occursList.putParam("ReportBLine",
					" " + FormatUtil.pad9("" + rounds, 3) + " " + FormatUtil.pad9("" + tBankRemit.getCustNo(), 7) + "-"
							+ FormatUtil.pad9("" + tBankRemit.getFacmNo(), 3) + "-"
							+ FormatUtil.pad9("" + tBankRemit.getBormNo(), 3) + "  "
							+ FormatUtil.padX("" + tCustMain.getCustName(), 20) + "  "
							+ FormatUtil.padX("" + tBankRemit.getRemitBank(), 3) + "  "
							+ FormatUtil.padX("" + tBankRemit.getRemitBranch(), 4) + "  "
							+ FormatUtil.padX("" + tBankRemit.getRemitAcctNo(), 14) + "  "
							+ FormatUtil.padX("" + tBankRemit.getCustName(), 20) + "  "
							+ FormatUtil.padLeft(amtFormat(tBankRemit.getRemitAmt()), 18) + "  "
							+ FormatUtil.padLeft(amtFormat(lineAmt), 18) + "  " + FormatUtil.padX("" + distItem, 10)
							+ "  " + FormatUtil.padX("" + businessOfficer, 10) + "  "
							+ FormatUtil.pad9("" + corFlag, 1));

			totaB.addOccursList(occursList);

			if (rounds == lBankRemit.size()) {
				occursList = new OccursList();
				occursList.putParam("ReportBLine",
						FormatUtil.padLeft("小計", 91) + FormatUtil.padLeft("" + amtFormat(remitAmt.get(oldTmp)), 18));
				totaB.addOccursList(occursList);

				occursList = new OccursList();
				occursList.putParam("ReportBLine", addMark("-", 200));
				totaB.addOccursList(occursList);
			}
			rounds++;
			sum = sum.add(tBankRemit.getRemitAmt());
		}

//		總計
		totaB.putParam("MSGID", "L411B");
		totaB.putParam("ReportBCnt", custCnt);
		totaB.putParam("ReportBTotalSum", sum);
		totaB.putParam("ReportBCorCnt", corCnt);

		this.addList(totaB);
	}

//	暫時紀錄戶號額度
	private class tmpFacm {

		private int custNo = 0;
		private int facmNo = 0;

		public tmpFacm(int custNo, int facmNo) {
			this.setCustNo(custNo);
			this.setFacmNo(facmNo);
		}

		public int getCustNo() {
			return custNo;
		}

		public void setCustNo(int custNo) {
			this.custNo = custNo;
		}

		public int getFacmNo() {
			return facmNo;
		}

		public void setFacmNo(int facmNo) {
			this.facmNo = facmNo;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getEnclosingInstance().hashCode();
			result = prime * result + custNo;
			result = prime * result + facmNo;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			tmpFacm other = (tmpFacm) obj;
			if (!getEnclosingInstance().equals(other.getEnclosingInstance()))
				return false;
			if (custNo != other.custNo)
				return false;
			if (facmNo != other.facmNo)
				return false;
			return true;
		}

		private L4102Batch getEnclosingInstance() {
			return L4102Batch.this;
		}
	}

	private String addMark(String mark, int number) {
		String result = mark;
		for (int i = 1; i < number; i++) {
			result = result + mark;
		}
		return result;
	}

	private String amtFormat(BigDecimal amt) {
		String result = "";
		this.info("amt : " + amt);
		this.info("amt.length : " + (amt + "").length());
		int length = 0;
		int quotient = 0;
		int remainder = 0;
		int divisor = 3;
		int q = 1;

		length = (amt + "").length();
		quotient = length / divisor;
		remainder = length % divisor;

		if (amt != null) {
			if (remainder == 0) {
				q = 0;
			}
			for (int i = 0; i < quotient + q; i++) {
				if (remainder == 0) {
					if (i == 0) {
						result = (amt + "").substring(remainder + divisor * i, divisor * (i + 1));
					} else {
						result = result + "," + (amt + "").substring(divisor * i, divisor * (i + 1));
					}
				} else {
					if (i == 0) {
						result = (amt + "").substring(divisor * i, remainder + divisor * i);
					} else {
						result = result + ","
								+ (amt + "").substring(remainder + divisor * (i - 1), remainder + divisor * (i));
					}
				}
			}
		}
		return result;
	}

	public void doRptA(TitaVo titaVo) throws LogicException {
		this.info("L411A doRpt started.");
		l4101ReportA.setTxBuffer(txBuffer);
		String parentTranCode = titaVo.getTxcd();

		l4101ReportA.setParentTranCode(parentTranCode);

		// 撈資料組報表
		l4101ReportA.exec(titaVo);

		// 寫產檔記錄到TxReport
		long rptNo = l4101ReportA.close();

		// 產生PDF檔案
		l4101ReportA.toPdf(rptNo);

		this.info("L411A doRpt finished.");

	}

	public void doRptC(TitaVo titaVo) throws LogicException {
		this.info("L411A doRpt started.");
		l4101ReportC.setTxBuffer(txBuffer);
		String parentTranCode = titaVo.getTxcd();

		l4101ReportC.setParentTranCode(parentTranCode);

		// 撈資料組報表
		l4101ReportC.exec(titaVo);

		// 寫產檔記錄到TxReport
		long rptNo = l4101ReportC.close();

		// 產生PDF檔案
		l4101ReportC.toPdf(rptNo);

		this.info("L411C doRpt finished.");

	}

}