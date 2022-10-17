package com.st1.itx.trade.L4;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AcClose;
import com.st1.itx.db.domain.AcCloseId;
import com.st1.itx.db.domain.AcDetail;
import com.st1.itx.db.domain.BankRemit;
import com.st1.itx.db.domain.CdBank;
import com.st1.itx.db.domain.CdBankId;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.domain.SystemParas;
import com.st1.itx.db.domain.TxFile;
import com.st1.itx.db.service.AcCloseService;
import com.st1.itx.db.service.AcDetailService;
import com.st1.itx.db.service.BankRemitService;
import com.st1.itx.db.service.CdAcCodeService;
import com.st1.itx.db.service.CdBankService;
import com.st1.itx.db.service.CdBcmService;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.SystemParasService;
import com.st1.itx.db.service.TxFileService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.FileCom;
import com.st1.itx.util.common.FtpClient;
import com.st1.itx.util.common.MakeFile;
import com.st1.itx.util.common.data.BankRemitFileVo;
import com.st1.itx.util.common.data.L4101OldVo;
import com.st1.itx.util.common.data.L4101Vo;
import com.st1.itx.util.common.data.RemitFormVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.util.report.RemitForm;

/**
 * Tita<br>
 * ACCTDATE=9,7<br>
 * BATNO=X,6<br>
 * END=X,1<br>
 */

@Service("L4101Batch")
@Scope("prototype")
/**
 * 撥款匯款作業
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4101Batch extends TradeBuffer {

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
	@Autowired
	TxFileService txFileService;
	@Autowired
	FtpClient ftpClient;
	@Autowired
	SystemParasService systemParasService;

	/* 報表服務注入 */
	@Autowired
	L4101ReportA l4101ReportA;
	@Autowired
	L4101ReportB l4101ReportB;
	@Autowired
	L4101ReportC l4101ReportC;
	@Autowired
	L4101ReportD l4101ReportD;
	@Autowired
	L4101ReportE l4101ReportE;
	@Autowired
	L4101Vo l4101Vo;
	@Autowired
	L4101OldVo l4101OldVo;

	@Value("${iTXOutFolder}")
	private String outFolder = "";

	int iAcDate = 0;
	String batchNo = "";
	private String nowBatchNo = "";

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4101Batch ");

		iAcDate = parse.stringToInteger(titaVo.getParam("AcDate"));
		int iItemCode = parse.stringToInteger(titaVo.getParam("ItemCode")); // 1.撥款 2.退款
		batchNo = this.getBatchNo(iItemCode, titaVo);

		String wkbatchNo = titaVo.getBacthNo();
		this.info("L4101 Batch batchNo = " + batchNo);

		List<BankRemit> lBankRemit = new ArrayList<BankRemit>();
		List<BankRemit> lBankRemit2 = new ArrayList<BankRemit>();
		List<BankRemit> lBankRemit3 = new ArrayList<BankRemit>();
		List<BankRemit> lBankRemit4 = new ArrayList<BankRemit>();

		Slice<BankRemit> slBankRemit = bankRemitService.findL4901B(iAcDate + 19110000, batchNo, 00, 99, 0, 0, 0,
				Integer.MAX_VALUE, titaVo);

		for (BankRemit t : slBankRemit.getContent()) {
			// 作業項目為1.撥款時把退款篩選掉
			if (iItemCode == 1) {
				if (t.getDrawdownCode() == 4 || t.getDrawdownCode() == 5 || t.getDrawdownCode() == 11) {
					continue;
				}
			}

			// 作業項目為2.退款時把撥款篩選掉
			if (iItemCode == 2) {
				if (t.getDrawdownCode() == 1 || t.getDrawdownCode() == 2) {
					continue;
				}
			}

			if (t.getActFg() != 1) {
				lBankRemit.add(t);
			}
		}

		// 更新批號
		batchNo = this.updBatchNo(batchNo, titaVo);
		this.info("L4101 batchNo = " + batchNo);
		this.info("L4101 lBankRemit = " + lBankRemit);
		for (BankRemit tBankRemit : lBankRemit) {
			this.info("L4101 tBankRemit = " + tBankRemit);
			tBankRemit.setBatchNo(batchNo);
			this.info("L4101 setBatchNo = " + tBankRemit.getBatchNo());
		}

		this.info("L4101 lBankRemit -2 = " + lBankRemit);
		try {
			bankRemitService.updateAll(lBankRemit, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007", "BankRemit " + e.getErrorMsg()); // 更新資料時，發生錯誤
		}

		// 更新批號
		List<AcDetail> lAcDetail = new ArrayList<AcDetail>();
		for (BankRemit tBankRemit : lBankRemit) {
			Slice<AcDetail> slAcDetail = acDetailService.acdtlRelTxseqEq(iAcDate,
					titaVo.getKinbr() + tBankRemit.getTitaTlrNo() + tBankRemit.getTitaTxtNo(), 0, Integer.MAX_VALUE,
					titaVo);
			if (slAcDetail != null) {
				for (AcDetail tAcDetail : slAcDetail.getContent()) {
					if (tAcDetail.getEntAc() == 1) {
						tAcDetail.setTitaBatchNo(batchNo);
						lAcDetail.add(tAcDetail);
					}
				}
			}
		}
		if (lAcDetail.size() > 0) {
			try {
				acDetailService.updateAll(lAcDetail, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", "AcDetail " + e.getErrorMsg()); // 更新資料時，發生錯誤
			}
		}
		// commit

		this.batchTransaction.commit();
		titaVo.setBatchNo(batchNo);

		// 批號查全部
		lBankRemit = new ArrayList<BankRemit>();
		lBankRemit2 = new ArrayList<BankRemit>();
		lBankRemit3 = new ArrayList<BankRemit>();
		lBankRemit4 = new ArrayList<BankRemit>();
		slBankRemit = bankRemitService.findL4901B(iAcDate + 19110000, batchNo, 00, 99, 0, 0, 0, Integer.MAX_VALUE,
				titaVo);
		if (slBankRemit == null) {
			throw new LogicException(titaVo, "E0001", "查無資料");
		}
		lBankRemit = slBankRemit == null ? null : new ArrayList<BankRemit>(slBankRemit.getContent());
		for (BankRemit t : lBankRemit) {
			// 作業項目為1.撥款時把退款篩選掉
			if (iItemCode == 1) {
				if (t.getDrawdownCode() == 4 || t.getDrawdownCode() == 5 || t.getDrawdownCode() == 11) {
					continue;
				}
				if (t.getRemitAmt().compareTo(new BigDecimal(15000000)) > 0) {
					lBankRemit3.add(t);
				} else {
					lBankRemit4.add(t);
				}

			}

			// 作業項目為2.退款時把撥款篩選掉
			if (iItemCode == 2) {
				if (t.getDrawdownCode() == 1 || t.getDrawdownCode() == 2) {
					continue;
				}
			}
			lBankRemit2.add(t);
		}

		totaVo.put("PdfSnoM", "");
		totaVo.put("PdfSnoF", "");

		if (iItemCode == 1) {
//			step1.產出媒體檔
			procBankRemitMedia(lBankRemit4, titaVo);
//		1500萬以上需產舊格式
			if (lBankRemit3.size() != 0) {
				String reportItem = "-撥款匯款媒體檔(舊格式1500萬)";
				procBankRemitMediaOld(lBankRemit3, reportItem, titaVo);
			}
		} else {
//			step1.產出媒體檔
			procBankRemitMedia(lBankRemit2, titaVo);
		}

		if (batchNo.length() > 2) {
			String reportItem = "-撥款匯款媒體檔(舊格式)";
			if ("LN".equals(batchNo.substring(0, 2))) {
				procBankRemitMediaOld(lBankRemit2, reportItem, titaVo);
			}
		}
//			step2產出撥款傳票
//		totaA.init(titaVo);
		doRptA(titaVo);

//			step3產出整批匯款單
		totaB.init(titaVo);
//			1.匯款申請書
		long snoF = printRemitForm(lBankRemit2, titaVo);

		this.info("snoF : " + snoF);

		totaVo.put("PdfSnoF", "" + snoF);

//			4.匯款明細
		doRptB(titaVo);
//			5.傳票明細表
		doRptC(titaVo);

		if (iItemCode == 1) {
//			6.撥款未齊件表
			doRptD(titaVo);
//			7.貸款自動轉帳申請書明細表
			doRptE(titaVo);
		}

		String checkMsg = "撥款匯款產檔已完成。   批號 = " + batchNo;

		webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "LC009",
				titaVo.getTlrNo() + "L4101", checkMsg, titaVo);

		return this.sendList();
	}

	private void procBankRemitMedia(List<BankRemit> lBankRemit, TitaVo titaVo) throws LogicException {
		this.info("procBankRemitMedia ...");
//		String path = outFolder + "LNM24p.txt";

		l4101Vo.setOccursList(lBankRemit, titaVo);

		// 轉換資料格式
		ArrayList<String> file = l4101Vo.toFile();
// 檔案產生者員編_disb_送匯日期_3碼檔案序號_secret.csv

		String reportItem = "-撥款匯款媒體檔";
		if (batchNo.length() > 2)
			if ("RT".equals(batchNo.substring(0, 2))) {
				reportItem = "-退款匯款媒體檔";
			}
		makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), titaVo.getTxCode(),
				titaVo.getTxCode() + reportItem, titaVo.getTlrNo() + "_disb_"
						+ (this.getTxBuffer().getTxBizDate().getTbsDy() + 19110000) + "_" + nowBatchNo + "_secret.csv",
				2);

		for (String line : file) {
			makeFile.put(line);
		}

		long snoM = makeFile.close();

		this.info("snoM : " + snoM);

		makeFile.toFile(snoM);

		sendToFTP(snoM, titaVo);

		totaVo.put("PdfSnoM", "" + snoM);

	}

	private void procBankRemitMediaOld(List<BankRemit> lBankRemit, String reportItem, TitaVo titaVo)
			throws LogicException {
		this.info("procBankRemitMediaOld ...");

		l4101OldVo.setOccursList(lBankRemit, titaVo);

		// 轉換資料格式
		ArrayList<String> file = l4101OldVo.toFile();
// 檔案產生者員編_disb_送匯日期_3碼檔案序號_secret.csv

		makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), titaVo.getTxCode(),
				titaVo.getTxCode() + reportItem, "LNM24p.txt", 2);

		for (String line : file) {
			makeFile.put(line);
		}

		makeFile.close();

	}

	private void sendToFTP(long fileNo, TitaVo titaVo) {
		TxFile txFile = txFileService.findById(fileNo, titaVo);

		if (txFile == null) {
			this.error("Tried to sendToFTP() but fileNo is not found in TxFile!");
			this.error("fileNo: " + fileNo);
			return;
		}

		SystemParas systemParas = systemParasService.findById("LN", titaVo);
		String[] auth = systemParas.getLoanMediaFtpAuth().split(":");
		String fileName = txFile.getFileOutput();
		Path fullPath = Paths.get(outFolder, fileName);

		ftpClient.sendFile(systemParas.getLoanMediaFtpUrl(), auth[0], auth[1], fullPath.toString(), "outbound");
	}

	private String getBatchNo(int iItemCode, TitaVo titaVo) throws LogicException {
		String batchNo = "";
		AcCloseId tAcCloseId = new AcCloseId();
		tAcCloseId.setAcDate(iAcDate);
		tAcCloseId.setBranchNo(titaVo.getAcbrNo());
		tAcCloseId.setSecNo("09"); // 業務類別: 01-撥款匯款 02-支票繳款 09-放款
		AcClose tAcClose = acCloseService.findById(tAcCloseId, titaVo);
		if (tAcClose == null) {
			throw new LogicException(titaVo, "E0001", "無帳務資料"); // 查詢資料不存在
		}
		if (iItemCode == 1) {
			batchNo = "LN" + parse.IntegerToString(tAcClose.getClsNo() + 1, 2) + "  ";

		} else {
			batchNo = "RT" + parse.IntegerToString(tAcClose.getClsNo() + 1, 2) + "  ";

		}
		return batchNo;
	}

	// 更新批號
	private String updBatchNo(String batchNo, TitaVo titaVo) throws LogicException {
		AcCloseId tAcCloseId = new AcCloseId();
		tAcCloseId.setAcDate(iAcDate);
		tAcCloseId.setBranchNo(titaVo.getAcbrNo());
		tAcCloseId.setSecNo("01"); // 業務類別: 01-撥款匯款 02-支票繳款 09-放款
		AcClose tAcClose = acCloseService.holdById(tAcCloseId, titaVo);
		if (tAcClose == null) {
			throw new LogicException(titaVo, "E0001", "無撥款帳務資料"); // 查詢資料不存在
		}
		nowBatchNo = parse.IntegerToString(tAcClose.getBatNo(), 3);
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
			remitformVo.setReportCode(titaVo.getTxCode() + "-F");
			// 報表說明(預設為"國內匯款申請書(兼取款憑條)")
			remitformVo.setReportItem("國內匯款申請書(兼取款憑條)_整批" + "-" + batchNo);

			remitForm.open(titaVo, remitformVo);

			BankRemit lastTalBankRemit = new BankRemit();
			BigDecimal talAmt = BigDecimal.ZERO;
			String fReceiveName = "";
			for (BankRemit t : lBankRemit) {
				talAmt = talAmt.add(t.getRemitAmt());
				if (fReceiveName.isEmpty()) {
					fReceiveName = t.getCustName();
				}
			}
			fReceiveName += "，等共" + lBankRemit.size() + "筆";

			lastTalBankRemit.setCustName(fReceiveName);
			lastTalBankRemit.setDrawdownCode(1);
			lastTalBankRemit.setRemitAmt(talAmt);

			lBankRemit.add(lastTalBankRemit);
			this.info("lBankRemit.size =" + lBankRemit.size());
			for (BankRemit tBankRemit : lBankRemit) {

				remitformVo = new RemitFormVo();
				// 第二筆
				if (cnt > 0) {
					remitformVo.setNewPageFg();
				}
				this.info("tBankRemit =" + tBankRemit);

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
				remitformVo.setRemitTel("23895858");

				// 附言
				remitformVo.setNote(tBankRemit.getRemark());

				remitForm.addpage(titaVo, remitformVo);

			}
		}

		if (cnt >= 0) {
			sno = remitForm.close();

//			remitForm.toPdf(sno);
		}
		return sno;
	}

	public void doRptA(TitaVo titaVo) throws LogicException {
		this.info("L4101A doRpt started.");
		l4101ReportA.setTxBuffer(txBuffer);
		String parentTranCode = titaVo.getTxcd();

		l4101ReportA.setParentTranCode(parentTranCode);

		// 撈資料組報表
		l4101ReportA.exec(titaVo);

		// 寫產檔記錄到TxReport
		long rptNo = l4101ReportA.close();

		// 產生PDF檔案
		l4101ReportA.toPdf(rptNo);

		this.info("L4101A doRpt finished.");

	}

	public void doRptB(TitaVo titaVo) throws LogicException {
		this.info("L4101B doRpt started.");
		l4101ReportB.setTxBuffer(txBuffer);
		String parentTranCode = titaVo.getTxcd();

		l4101ReportB.setParentTranCode(parentTranCode);

		// 撈資料組報表
		l4101ReportB.exec(titaVo);

		// 寫產檔記錄到TxReport
		long rptNoB = l4101ReportB.close();

		// 產生PDF檔案
		l4101ReportB.toPdf(rptNoB);

		this.info("L4101B doRpt finished.");

	}

	public void doRptC(TitaVo titaVo) throws LogicException {
		this.info("L4101C doRpt started.");
		l4101ReportC.setTxBuffer(txBuffer);
		String parentTranCode = titaVo.getTxcd();

		l4101ReportC.setParentTranCode(parentTranCode);

		// 撈資料組報表
		l4101ReportC.exec(titaVo);

		// 寫產檔記錄到TxReport
		long rptNoB = l4101ReportC.close();

		// 產生PDF檔案
		l4101ReportC.toPdf(rptNoB);

		this.info("L4101C doRpt finished.");

	}

	public void doRptD(TitaVo titaVo) throws LogicException {
		this.info("L4101D doRpt started.");
		l4101ReportD.setTxBuffer(txBuffer);
		String parentTranCode = titaVo.getTxcd();

		l4101ReportD.setParentTranCode(parentTranCode);

		// 撈資料組報表
		l4101ReportD.exec(titaVo);

		// 寫產檔記錄到TxReport
		long rptNod = l4101ReportD.close();

		// 產生PDF檔案
		l4101ReportD.toPdf(rptNod);

		this.info("L4101D doRpt finished.");

	}

	public void doRptE(TitaVo titaVo) throws LogicException {
		this.info("L4101E doRpt started.");
		l4101ReportE.setTxBuffer(txBuffer);
		String parentTranCode = titaVo.getTxcd();
//		acDate = parse.stringToInteger(titaVo.getParam("AcDate")) + 19110000;

		l4101ReportE.setParentTranCode(parentTranCode);

		// 撈資料組報表
		l4101ReportE.exec(titaVo, iAcDate);

		// 寫產檔記錄到TxReport
		long rptNod = l4101ReportE.close();

		// 產生PDF檔案
		l4101ReportE.toPdf(rptNod);

		this.info("L4101E doRpt finished.");

	}

}