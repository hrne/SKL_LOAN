package com.st1.itx.trade.L3;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.AcClose;
import com.st1.itx.db.domain.AcCloseId;
import com.st1.itx.db.domain.AcDetail;
import com.st1.itx.db.domain.CdAcCode;
import com.st1.itx.db.domain.CdBank;
import com.st1.itx.db.domain.CdBankId;
import com.st1.itx.db.domain.CdCode;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.FacMainId;
import com.st1.itx.db.domain.LoanBorMain;
import com.st1.itx.db.domain.LoanBorMainId;
import com.st1.itx.db.domain.TxTranCode;
import com.st1.itx.db.service.AcCloseService;
import com.st1.itx.db.service.CdAcCodeService;
import com.st1.itx.db.service.CdBankService;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.db.service.TxTranCodeService;
import com.st1.itx.util.common.LoanCom;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;

@Component
@Scope("prototype")

public class L3100Report extends MakeReport {

	/* DB服務注入 */
	@Autowired
	LoanBorMainService loanBorMainService;
	@Autowired
	CdAcCodeService cdAcCodeService;
	@Autowired
	CdCodeService cdCodeService;
	@Autowired
	TxTranCodeService txTranCodeService;
	@Autowired
	FacMainService facMainService;
	@Autowired
	CdEmpService cdEmpService;

	@Autowired
	LoanCom loanCom;
	@Autowired
	CdBankService cdBankService;
	@Autowired
	AcCloseService acCloseService;
	@Autowired
	public Parse parse;

	private DecimalFormat df = new DecimalFormat("##,###,###,###,##0");
	private ArrayList<AcDetail> lAcDetail;

	// ReportDate : 報表日期(帳務日)
	// ReportCode : 報表編號
	// ReportItem : 報表名稱
	// Security : 報表機密等級(中文敍述)
	// PageSize : 紙張大小;
	// PageOrientation : 紙張方向
	// P:Portrait Orientation (直印) , L:Landscape Orientation(橫印)
	private int reportDate = 0;
	private String brno = "";
	private String reportCode = "L3100";
	private String reportItem = "撥款傳票(主管審核)";
	private String security = "機密";
	private String pageSize = "A4";
	private String pageOrientation = "P";
	private AcClose tAcClose = new AcClose();
	private AcCloseId acCloseId = new AcCloseId();
	// 製表日期
	private String nowDate;
	// 製表時間
	private String nowTime;

	private int wkRvCnt = 0;
	List<String> tempList = null;
	HashMap<String, BigDecimal> dbAmt = new HashMap<>();
	HashMap<String, BigDecimal> crAmt = new HashMap<>();
	HashMap<String, String> relTxSeq = new HashMap<>();
	HashMap<String, Integer> cntR = new HashMap<>();
	HashMap<String, String> slipNo = new HashMap<>();
	int cnt = 0; // 總筆數
	private BigDecimal wkRvDrawdownAmt = new BigDecimal(0);
	private int facLastBormNo = 0;
	private int wkRvBormNo = 0;
	BigDecimal sumDbAmt = BigDecimal.ZERO;// 借方總金額
	BigDecimal sumCrAmt = BigDecimal.ZERO;// 貸方總金額

	// 自訂表頭
	@Override
	public void printHeader() {

		this.info("L3100Report.printHeader");

		this.print(-1, 2, "程式ID：" + this.getParentTranCode());
		this.print(-1, 59, "新光人壽保險股份有限公司", "C");
		this.print(-1, 98, "機密等級：" + this.security);
		this.print(-2, 2, "報　表：" + this.reportCode);
		this.print(-2, 59, this.reportItem, "C");
		this.print(-2, 98, "日　　期：" + this.showRocDate(this.nowDate, 1));
		this.print(-3, 2, "來源別：放款服務課");
		this.print(-3, 98, "時　　間：" + showTime(this.nowTime));
//		this.print(-4, 2, "批號：" + "01");
		this.print(-4, 98, "頁　　次：" + this.getNowPage());

		// 頁首帳冊別判斷
//		print(-4, 10, this.nowAcBookCode);
//		print(-4, 14, this.nowAcBookItem);

		// 明細起始列(自訂亦必須)
		this.setBeginRow(9);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(28);

		/**
		 * ---------1---------2---------3---------4---------5---------6---------7---------8---------9---------0---------1---------2---------3---------4---------5---------6
		 * 1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890
		 */
		print(2, 1, "　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　");
		print(1, 1, "　傳票號碼　　科子目名稱　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　借方金額　　　　貸方金額　　");
		print(1, 1, "－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－");

	}

	// 自訂表尾
	@Override
	public void printFooter() {
//		this.print(-41, 1, "　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　");
//		this.print(-42, 1, "　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　");
//		this.print(-43, 1, "　放 款 部　　　協理：　　　　　　　　經理：　　　　　　　　襄理：　　　　　　　　　　　　　　製表人：　　　　　　　　　　　　　　　　　");
//		this.print(-44, 1, "總經理：　　　　　　　副總經理：　　　　　　　資深協理：　　　　　　　協理：　　　　　　　經理：　　　　　　　經辦：" + this.titaVo.getEmpNm());
//		this.print(-47, 1, "　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　覆核：　　　　　　　　");
	}

	public void exec(TitaVo titaVo) throws LogicException {
		this.info("L4701ReportA exec ...");

		// 設定字體1:標楷體 字體大小12
		this.setFont(1, 12);

		this.reportDate = Integer.valueOf(titaVo.getEntDy());

		this.brno = titaVo.getBrno();

		this.nowDate = dDateUtil.getNowStringRoc();
		this.nowTime = dDateUtil.getNowStringTime();

		reportCode = titaVo.getTxcd();

		List<AcDetail> acDetailList = new ArrayList<AcDetail>();

		if ("L3100".equals(titaVo.getTxcd())) {

			acDetailList = this.txBuffer.getAcDetailList();
		} else {
			List<AcDetail> lAcDetail = new ArrayList<AcDetail>();
			AcDetail acDetail = new AcDetail();

			FacMain tFacMain = facMainService.findById(new FacMainId(parse.stringToInteger(titaVo.getParam("CustNo")),
					parse.stringToInteger(titaVo.getParam("FacmNo"))), titaVo);
			if (tFacMain == null) {
				throw new LogicException(titaVo, "E0001",
						" 額度主檔 借款人戶號 = " + titaVo.getParam("CustNo") + " 額度編號 = " + titaVo.getParam("FacmNo")); // 查詢資料不存在
			}
			Slice<LoanBorMain> slLoanBorMain = loanBorMainService.findStatusEq(Arrays.asList(99),
					parse.stringToInteger(titaVo.getParam("CustNo")), parse.stringToInteger(titaVo.getParam("FacmNo")),
					parse.stringToInteger(titaVo.getParam("FacmNo")), 0, Integer.MAX_VALUE, titaVo);
			if (slLoanBorMain != null) {
				for (LoanBorMain rv : slLoanBorMain.getContent()) {
					wkRvCnt++;
				}
			}

			facLastBormNo = tFacMain.getLastBormNo();
			wkRvBormNo = wkRvCnt + facLastBormNo;
			// 借方
			acDetail.setDbCr("D");
			acDetail.setAcctCode(tFacMain.getAcctCode());
			acDetail.setTxAmt(parse.stringToBigDecimal(titaVo.getParam("TimDrawdownAmt")));
			acDetail.setCustNo(parse.stringToInteger(titaVo.getParam("CustNo")));
			acDetail.setFacmNo(parse.stringToInteger(titaVo.getParam("FacmNo")));
			acDetail.setBormNo(wkRvBormNo);
			acDetailList.add(acDetail);
			acDetail = new AcDetail();
			// 貸方
			acDetail.setDbCr("C");
			acDetail.setAcctCode("P02");
			acDetail.setTxAmt(parse.stringToBigDecimal(titaVo.getParam("TimDrawdownAmt")));
			acDetail.setCustNo(parse.stringToInteger(titaVo.getParam("CustNo")));
			acDetail.setFacmNo(parse.stringToInteger(titaVo.getParam("FacmNo")));
			acDetail.setBormNo(wkRvBormNo);
			acDetailList.add(acDetail);
		}

		if (acDetailList != null && acDetailList.size() != 0) {

			acCloseId.setAcDate(titaVo.getEntDyI());
			acCloseId.setBranchNo(titaVo.getAcbrNo());
			acCloseId.setSecNo("09"); // 業務類別: 09-放款
			tAcClose = acCloseService.findById(acCloseId, titaVo); // holdById
			int SlipNo = 0;
			if (tAcClose != null) {
				SlipNo = tAcClose.getSlipNo(); // 傳票號碼
			}
			AcDetail headAcDetail = new AcDetail();
			headAcDetail = acDetailList.get(0);

//			業務類別 01.撥款匯款 02.支票繳款 03.債協 09.放款
			CdCode tSN = cdCodeService.getItemFirst(6, "SecNo", FormatUtil.pad9(headAcDetail.getTitaSecNo(), 2),
					titaVo);

			String secNoX = "";
			int acDate = 0;
			String wkBankItem = "";
			String wkBranchItem = "";
			String wkBankCode = "";
			String wkBranchCode = "";
			String payNm = "";
			String remark = "";
			String remitAcctNo = "";
			String remitBankt = "";
			String paymentBank = "";

			if (tSN != null) {
				secNoX = tSN.getItem();
				this.info("secNoX = " + secNoX);
			}

			TxTranCode txTranCode = txTranCodeService.findById(headAcDetail.getTitaTxCd());

			if (acDetailList == null || acDetailList.isEmpty()) {
				// 出空表
				this.open(titaVo, reportDate, brno, reportCode, reportItem, security, pageSize, pageOrientation);
				this.setCharSpaces(0);
				print(1, 1, "本日無資料");
				return;
			}

			this.open(titaVo, reportDate, brno, reportCode, reportItem, security, pageSize, pageOrientation);

			this.setCharSpaces(0);
//				測試
//			int b = 1;
//			for (int i = 1; i <= 400; i++) {
//				if ((i % 10) == 0) {
//					this.print(-2, i, "" + (i / 10));
//				}
//				this.print(-1, i, "" + (i % 10));
//				this.print(-i, 1, "" + (i % 10));
//			}

			CdEmp tCdEmp = new CdEmp();

			tCdEmp = cdEmpService.findById(titaVo.getTlrNo(), titaVo);

			LoanBorMain tLoanBorMain = loanBorMainService.findById(new LoanBorMainId(
					parse.stringToInteger(titaVo.getParam("CustNo")), parse.stringToInteger(titaVo.getParam("FacmNo")),
					parse.stringToInteger(titaVo.getParam("BormNo"))), titaVo);
			if (tLoanBorMain != null) {
				acDate = tLoanBorMain.getDrawdownDate() + 19110000;
				wkBankCode = tLoanBorMain.getRemitBank();
				wkBranchCode = tLoanBorMain.getRemitBranch();
				CdBank t = cdBankService.findById(new CdBankId(wkBankCode, wkBranchCode), titaVo);
				if (t != null) {
					wkBankItem = t.getBankItem();
					wkBranchItem = t.getBranchItem();
				} else {
					wkBankItem = wkBankCode;
					wkBranchItem = wkBranchCode;
				}

				payNm = tLoanBorMain.getCompensateAcct(); // 收款戶名
				remark = tLoanBorMain.getRemark();// 附言
				remitAcctNo = tLoanBorMain.getRemitAcctNo().toString();// 匯款帳號
				remitBankt = wkBankItem + "　" + wkBranchItem;// 匯款銀行
				paymentBank = tLoanBorMain.getPaymentBank();// 解付單位代號
			}
			print(-4, 2,
					"借款人戶號　:" + parse.IntegerToString(headAcDetail.getCustNo(), 7) + "-"
							+ parse.IntegerToString(headAcDetail.getFacmNo(), 3) + "-"
							+ parse.IntegerToString(headAcDetail.getBormNo(), 3));
			print(-4, 59, this.showRocDate(acDate, 1), "C"); // 日期
			print(-5, 2, "戶名　　　　:" + loanCom.getCustNameByNo(headAcDetail.getCustNo()));

			for (AcDetail tAcDetail : acDetailList) {

				String acNoCodeX = " ";
				CdAcCode tCdAcCode = new CdAcCode();
				tCdAcCode = cdAcCodeService.acCodeAcctFirst(tAcDetail.getAcctCode(), titaVo);
//				科子細目中文
				if (tCdAcCode != null) {
					acNoCodeX = tCdAcCode.getAcNoItem();
				}
				print(1, 1, "　　　　　　           ");
				SlipNo++;
				print(0, 3, "" + parse.IntegerToString(SlipNo, 6)); // 傳票號碼
				print(0, 15, acNoCodeX); // 科子目名稱

				if ("D".equals(tAcDetail.getDbCr())) {
					print(0, 97, formatAmt(tAcDetail.getTxAmt(), 0), "R");// 借方金額
					sumDbAmt = sumDbAmt.add(tAcDetail.getTxAmt());
				}
				if ("C".equals(tAcDetail.getDbCr())) {

					print(0, 113, formatAmt(tAcDetail.getTxAmt(), 0), "R");// 貸方金額
					sumCrAmt = sumCrAmt.add(tAcDetail.getTxAmt());
				}
			}

			print(1, 1, "－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－");
			print(1, 1, "　　　　　　           ");

			print(0, 3, "總　計  ");
			print(0, 97, formatAmt(sumDbAmt, 0), "R");// 總借方金額
			print(0, 113, formatAmt(sumCrAmt, 0), "R");// 總貸方金額

			print(1, 3, "　　　　　　           ");
			print(1, 3, "收款戶名　:" + payNm);
			print(1, 3, "附言　　　:" + remark);
			print(0, 65, "解付單位代號　:" + paymentBank);
			print(1, 3, "匯款帳號　:" + FormatUtil.pad9(remitAcctNo, 14));
			print(0, 65, "匯款銀行　　　:" + remitBankt);
			print(1, 1, "　　　　　　           ");
			print(1, 1, "　　　　　　           ");

			this.setFont(1, 8);
			print(3, 1, "總經理：　　　　　　　　　副總經理：　　　　　　　　　資深協理：　　　　　　　　　協理：　　　　　　　　經理：　　　　　　　　經辦：");
			print(2, 1, "　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　覆核：　　　　　　　　");
			this.setFont(1, 12);
			if (tCdEmp != null) {
				print(-15, 1, "　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　" + tCdEmp.getFullname());
			}
			this.setFont(1, 12);
		}
	}
}
