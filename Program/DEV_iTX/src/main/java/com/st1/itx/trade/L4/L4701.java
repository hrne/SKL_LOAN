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
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.LoanCheque;
import com.st1.itx.db.service.BatxChequeService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.LoanChequeService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.FileCom;
import com.st1.itx.util.common.MakeFile;
import com.st1.itx.util.common.data.BatxChequeFileVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;
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
	// private static final Logger logger = LoggerFactory.getLogger(L4701.class);

	@Autowired
	public LoanChequeService loanChequeService;

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

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4701 ");
		this.totaVo.init(titaVo);

//		 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();
//		設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 500;

		Slice<LoanCheque> sLoanCheque = null;

		List<LoanCheque> lLoanCheque = new ArrayList<LoanCheque>();

		List<String> lStatus = new ArrayList<String>();

		lStatus.add("0");

		ArrayList<OccursList> tmp = new ArrayList<>();

		sLoanCheque = loanChequeService.receiveDateRange(this.getTxBuffer().getTxCom().getTbsdyf(), this.getTxBuffer().getTxCom().getTbsdyf(), lStatus, this.index, this.limit);

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
				occursList.putParam("ChequeNo", FormatUtil.pad9("" + tLoanCheque.getChequeNo(), 7));
				occursList.putParam("ChequeDate", FormatUtil.pad9("" + tLoanCheque.getChequeDate(), 7));
				occursList.putParam("BankCode", FormatUtil.padX("" + tLoanCheque.getBankCode(), 7));
				occursList.putParam("ChequeAcct", FormatUtil.padX("" + tLoanCheque.getChequeAcct(), 9));
				occursList.putParam("ChequeAmt", FormatUtil.pad9("" + tLoanCheque.getChequeAmt(), 7));
				occursList.putParam("MediaDate", FormatUtil.pad9("" + this.getTxBuffer().getTxCom().getTbsdy(), 7));
				occursList.putParam("Teller", FormatUtil.padX(this.getTxBuffer().getTxCom().getRelTlr(), 8));
				occursList.putParam("UnitCode", "10H400");
				occursList.putParam("SrcCode", "1");
				occursList.putParam("SrcUnit", "10H400");
				occursList.putParam("RecipeNo", FormatUtil.padX("" + tLoanCheque.getReceiptNo(), 5));
				occursList.putParam("EntryDate", FormatUtil.pad9("" + tLoanCheque.getEntryDate(), 7));
				occursList.putParam("CustNo", FormatUtil.pad9("" + tLoanCheque.getCustNo(), 7));

				tmp.add(occursList);

//				2.產生票據明細表

//		 		戶號 戶名 支票銀行 支票分行 帳號 票號 票面金額 埠別 到期日 交換區號
//				發票人ID 發票人姓名

//				支票數 本 外 金額
				OccursList occursListRpt = new OccursList();

				CustMain tCustMain = new CustMain();
				tCustMain = custMainService.custNoFirst(tLoanCheque.getCustNo(), tLoanCheque.getCustNo());
//						#OOCustNo=A,7,O
//						#OOCustName=x,20,O
//						#OOChequeBank=A,3,O
//						#OOChequeBranch=A,4,O
//						#OOChequeAcctNo=A,9,O
//						#OOChequeNo=A,7,O
//						#OOChequeAmt=m,14.2,O
//						#OOOutsideCode=X,1,O
//						#OOChequeDate=D,7,O
//						#OOAreaCode=X,2,O
//						#OOChequeId=X,10,O
//						#OOChequeName=c,120,O

				occursListRpt.putParam("OOCustNo", FormatUtil.pad9("" + tLoanCheque.getCustNo(), 7));
				if (tCustMain != null) {
					occursListRpt.putParam("OOCustName", FormatUtil.padX(tCustMain.getCustName(), 10));
				}
				occursListRpt.putParam("OOChequeBank", FormatUtil.pad9(tLoanCheque.getBankCode(), 7).substring(0, 3));
				occursListRpt.putParam("OOChequeBranch", FormatUtil.pad9(tLoanCheque.getBankCode(), 7).substring(3, 7));
				occursListRpt.putParam("OOChequeAcctNo", FormatUtil.padX("" + tLoanCheque.getChequeAcct(), 9));
				occursListRpt.putParam("OOChequeNo", FormatUtil.padX("" + tLoanCheque.getChequeNo(), 7));
				occursListRpt.putParam("OOChequeAmt", FormatUtil.pad9("" + tLoanCheque.getChequeAmt(), 14));
				occursListRpt.putParam("OOOutsideCode", FormatUtil.padX("" + tLoanCheque.getOutsideCode(), 1));
				occursListRpt.putParam("OOChequeDate", FormatUtil.pad9("" + tLoanCheque.getChequeDate(), 7));
				occursListRpt.putParam("OOAreaCode", FormatUtil.pad9("" + tLoanCheque.getAreaCode(), 2));
				occursListRpt.putParam("OOChequeId", FormatUtil.padX("", 10));
				occursListRpt.putParam("OOChequeName", FormatUtil.padX(tLoanCheque.getChequeName(), 60));

				chequeCnt = chequeCnt + 1;
				if ("2".equals(tLoanCheque.getOutsideCode())) {
					chequeOutCnt = chequeOutCnt + 1;
				} else if ("1".equals(tLoanCheque.getOutsideCode())) {
					chequeInCnt = chequeInCnt + 1;
				}

				chequeTotAmt = chequeTotAmt.add(tLoanCheque.getChequeAmt());

				this.totaVo.addOccursList(occursListRpt);
			}
			// 把明細資料容器裝到檔案資料容器內
			batxChequeFileVo.setOccursList(tmp);
			// 轉換資料格式
			ArrayList<String> file = batxChequeFileVo.toFile();

//			String path = "D:\\temp\\pdcm.csv";

			makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), titaVo.getTxCode(), titaVo.getTxCode() + "-票據媒體檔", "pdcm.csv", 2);

			for (String line : file) {
				makeFile.put(line);
			}

			long snoM = makeFile.close();

			this.info("sno : " + snoM);

			makeFile.toFile(snoM);

			totaVo.put("PdfSnoM", "" + snoM);

		}
//		#OChequeCnt=m,6,O
//		#OChequeOutCnt=m,6,O
//		#OChequeInCnt=m,6,O
//		#OChequeTotAmt=m,14.2,O

		this.totaVo.putParam("OChequeCnt", FormatUtil.pad9("" + chequeCnt, 6));
		this.totaVo.putParam("OChequeOutCnt", FormatUtil.pad9("" + chequeOutCnt, 6));
		this.totaVo.putParam("OChequeInCnt", FormatUtil.pad9("" + chequeInCnt, 6));
		this.totaVo.putParam("OChequeTotAmt", FormatUtil.pad9("" + chequeTotAmt, 14));

		this.addList(this.totaVo);
		return this.sendList();
	}
}