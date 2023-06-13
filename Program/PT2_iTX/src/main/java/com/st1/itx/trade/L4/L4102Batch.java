package com.st1.itx.trade.L4;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
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
	String wkBatchNo = "";

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4102Batch ");

		acDate = parse.stringToInteger(titaVo.getParam("AcDate")) + 19110000;
		int iItemCode = parse.stringToInteger(titaVo.getParam("ItemCode")); // 1.撥款 2.退款
		wkBatchNo = titaVo.getBacthNo();
		this.info("L4102 Batch batchNo = " + wkBatchNo);

		totaVo.put("PdfSnoM", "");
		totaVo.put("PdfSnoF", "");

//		產出撥款傳票
		doRptA(titaVo);
//		傳票明細表
		doRptC(titaVo);

		String checkMsg = "報表已製作完成。   批號 = " + wkBatchNo;

		webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "LC009",
				titaVo.getTlrNo() + "L4102", checkMsg, titaVo);

		return this.sendList();
	}

	public void doRptA(TitaVo titaVo) throws LogicException {
		this.info("L411A doRpt started.");
		l4101ReportA.setTxBuffer(txBuffer);
		String parentTranCode = titaVo.getTxcd();

		l4101ReportA.setParentTranCode(parentTranCode);

		// 撈資料組報表
		this.info("wkBatchNo = " + (wkBatchNo.substring(4, wkBatchNo.length())));
		if (!"00".equals(wkBatchNo.substring(4, wkBatchNo.length()))) {
			l4101ReportA.execA(titaVo);
		} else {
			l4101ReportA.execB(titaVo); // A4傳票總表
		}

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