package com.st1.itx.trade.L4;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AcDetail;
import com.st1.itx.db.domain.LoanCheque;
import com.st1.itx.db.service.AcDetailService;
import com.st1.itx.db.service.BatxChequeService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.LoanChequeService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.FileCom;
import com.st1.itx.util.common.MakeFile;
import com.st1.itx.util.common.data.BatxChequeFileVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * OENTRYDATE=9,7<br>
 * BATNO=X,6<br>
 * END=X,1<br>
 */

@Service("L4701")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4701 extends TradeBuffer {

	@Autowired
	public LoanChequeService loanChequeService;
	@Autowired
	public AcDetailService acDetailService;

	@Autowired
	public Parse parse;

	@Autowired
	public DateUtil dateUtil;

	@Autowired
	public FileCom fileCom;

	@Autowired
	public BatxChequeFileVo batxChequeFileVo;

	@Autowired
	public CustMainService custMainService;

	@Autowired
	public BatxChequeService batxChequeService;

	@Autowired
	public MakeFile makeFile;
	@Autowired
	public WebClient webClient;

	/* 報表服務注入 */
	@Autowired
	L4701Report l4701Report;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4701 ");
		this.totaVo.init(titaVo);

		// tita
		int iAcDate = parse.stringToInteger(titaVo.getParam("EntryDate"));
		int iAcDateF = parse.stringToInteger(titaVo.getParam("EntryDate")) + 19110000;

		Slice<LoanCheque> sLoanCheque = null;

		List<LoanCheque> lLoanCheque = new ArrayList<LoanCheque>();

		List<String> lStatus = new ArrayList<String>();

		lStatus.add("0");

		ArrayList<OccursList> tmp = new ArrayList<>();

		sLoanCheque = loanChequeService.receiveDateRange(iAcDateF, iAcDateF, lStatus, 0, Integer.MAX_VALUE, titaVo);

		lLoanCheque = sLoanCheque == null ? null : sLoanCheque.getContent();

//		lLoanCheque = loanChequeService.acDateRange(20200116, 20200116, lStatus);

//		1.產生票據媒體檔
//		ChequeNo	支票號碼		X	7	0	7	
//		ChequeDate	支票日期		A	7	7	14	YYYMMDD
//		BankCode	銀行代號		X	7	14	21	
//		ChequeAcct	支票銀行帳號	X	9	21	30	
//		ChequeAmt	金額			A	10	30	40	
//		MediaDate	輸入日期		A	7	40	47	YYYMMDD 轉檔的日期
//		Teller		作業者		X	8	47	55	帶轉檔作業者
//		UnitCode	部門代號		X	6	55	61	部門代號=來源單位
//		SrcCode		支票來源碼2		X	1	61	62	* ----->
//		SrcUnit		來源單位		X	6	62	68	
//		RecipeNo	傳票號碼		X	10	68	78	
//		EntryDate	作帳日期		A	7	78	85	YYYMMDD
//		CustNo		相關資訊		X	10	85	95	放款戶戶號

		int chequeCnt = 0;
		int chequeOutCnt = 0;
		int chequeInCnt = 0;
		BigDecimal chequeTotAmt = BigDecimal.ZERO;

		totaVo.put("PdfSnoM", "");

		if (lLoanCheque != null && lLoanCheque.size() != 0) {
			for (LoanCheque tLoanCheque : lLoanCheque) {
				OccursList occursList = new OccursList();
				AcDetail tAcDetail = new AcDetail();
				int slipNo = 0;
				tAcDetail = acDetailService
						.findL4701First("RCK", tLoanCheque.getCustNo(),
								FormatUtil.pad9("" + tLoanCheque.getChequeAcct(), 9) + " "
										+ FormatUtil.pad9("" + tLoanCheque.getChequeNo(), 7),
								iAcDateF, "D", "0", titaVo);
				if (tAcDetail != null) {
					slipNo = tAcDetail.getSlipNo();
				}
				occursList.putParam("ChequeNo", FormatUtil.pad9("" + tLoanCheque.getChequeNo(), 7));
				occursList.putParam("ChequeDate", FormatUtil.pad9("" + tLoanCheque.getChequeDate(), 7));
				occursList.putParam("BankCode", FormatUtil.padX("" + tLoanCheque.getBankCode(), 7));
				occursList.putParam("ChequeAcct", FormatUtil.padX("" + tLoanCheque.getChequeAcct(), 9));
				occursList.putParam("ChequeAmt", FormatUtil.pad9("" + tLoanCheque.getChequeAmt(), 7));
				occursList.putParam("MediaDate", FormatUtil.pad9("" + titaVo.getCalDy(), 7));
				occursList.putParam("Teller", FormatUtil.padX(titaVo.getTlrNo(), 8));
				occursList.putParam("UnitCode", "10H400");
				occursList.putParam("SrcCode", "1");
				occursList.putParam("SrcUnit", "10H400");
				occursList.putParam("RecipeNo", FormatUtil.padX("" + slipNo, 5));
				occursList.putParam("EntryDate", FormatUtil.pad9("" + tLoanCheque.getReceiveDate(), 7));
				occursList.putParam("CustNo", FormatUtil.pad9("" + tLoanCheque.getCustNo(), 7));

				tmp.add(occursList);

			}
			// 把明細資料容器裝到檔案資料容器內
			batxChequeFileVo.setOccursList(tmp);
			// 轉換資料格式
			ArrayList<String> file = batxChequeFileVo.toFile();

//			String path = "D:\\temp\\pdcm.csv";

			makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), titaVo.getTxCode(),
					titaVo.getTxCode() + "-票據媒體檔", "pdcm.csv", 2);

			for (String line : file) {
				makeFile.put(line);
			}

			long snoM = makeFile.close();

			this.info("sno : " + snoM);

			makeFile.toFile(snoM);

			totaVo.put("PdfSnoM", "" + snoM);

//			傳票明細表
			doRptA(titaVo);

			String checkMsg = "票據明細表已完成。 ";

			webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "LC009",
					titaVo.getTlrNo() + "L4701", checkMsg, titaVo);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	public void doRptA(TitaVo titaVo) throws LogicException {
		this.info("L4701 doRpt started.");
		l4701Report.setTxBuffer(txBuffer);
		String parentTranCode = titaVo.getTxcd();

		l4701Report.setParentTranCode(parentTranCode);

		// 撈資料組報表
		l4701Report.exec(titaVo);

		// 寫產檔記錄到TxReport
		long rptNoA = l4701Report.close();

		// 產生PDF檔案
		l4701Report.toPdf(rptNoA);

		this.info("L4701 doRpt finished.");

	}
}