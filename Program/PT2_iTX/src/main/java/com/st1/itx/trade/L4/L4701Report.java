package com.st1.itx.trade.L4;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CdCode;
import com.st1.itx.db.domain.LoanCheque;
import com.st1.itx.db.service.AcCloseService;
import com.st1.itx.db.service.AcDetailService;
import com.st1.itx.db.service.CdAcCodeService;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.LoanChequeService;
import com.st1.itx.db.service.SlipMediaService;
import com.st1.itx.util.common.LoanCom;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;

@Component
@Scope("prototype")

public class L4701Report extends MakeReport {

	/* DB服務注入 */
	@Autowired
	SlipMediaService slipMediaService;
	@Autowired
	AcDetailService acDetailService;
	@Autowired
	AcCloseService acCloseService;
	@Autowired
	CdAcCodeService cdAcCodeService;
	@Autowired
	LoanChequeService loanChequeService;
	@Autowired
	CdCodeService cdCodeService;

	@Autowired
	LoanCom loanCom;

	private DecimalFormat df = new DecimalFormat("##,###,###,###,##0");
	@Autowired
	public Parse parse;

	// ReportDate : 報表日期(帳務日)
	// ReportCode : 報表編號
	// ReportItem : 報表名稱
	// Security : 報表機密等級(中文敍述)
	// PageSize : 紙張大小;
	// PageOrientation : 紙張方向
	// P:Portrait Orientation (直印) , L:Landscape Orientation(橫印)
	private int reportDate = 0;
	private String brno = "";
	private String reportCode = "L4701";
	private String reportItem = "票據明細表";
	private String security = "機密";
	private String pageSize = "A4";
	private String pageOrientation = "L";

	// 製表日期
	private String nowDate;
	// 製表時間
	private String nowTime;

	List<String> tempList = null;
	HashMap<String, BigDecimal> dbAmt = new HashMap<>();
	HashMap<String, BigDecimal> crAmt = new HashMap<>();
	HashMap<String, String> relTxSeq = new HashMap<>();
	HashMap<String, Integer> cntR = new HashMap<>();
	HashMap<String, String> slipNo = new HashMap<>();
	int cnt = 0; // 總筆數
	BigDecimal sumDbAmt = BigDecimal.ZERO;// 借方總金額
	BigDecimal sumCrAmt = BigDecimal.ZERO;// 貸方總金額

	// 自訂表頭
	@Override
	public void printHeader() {

		this.info("L4701Report.printHeader");

		this.print(-1, 2, "程式ID：" + this.getParentTranCode());
		this.print(-1, 85, "新光人壽保險股份有限公司", "C");
		this.print(-1, 145, "機密等級：" + this.security);
		this.print(-2, 2, "報　表：" + this.reportCode);
		this.print(-2, 85, this.reportItem, "C");
		this.print(-2, 145, "日　　期：" + showBcDate(this.nowDate, 1));
		this.print(-3, 2, "來源別：放款服務課");
		this.print(-3, 145, "時　　間：" + showTime(this.nowTime));
		this.print(-4, 2, "批號：" + "01");
		this.print(-4, 145, "頁　　次：" + this.getNowPage());

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
		print(2, 1, "　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　");
		print(1, 1, "　戶號　　戶名　　　　　　　　支票銀行　　　支票分行　　　　　帳號　　　票號　　　　　　票面金額　　　　　　埠別　　　到期日　　交換區號　");
		print(1, 1, "　　　　　　　　　　　　　　　發票人ID　　　發票人姓名　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　");
		print(1, 1, "－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－");

	}

	// 自訂表尾
	@Override
	public void printFooter() {
//		this.print(-41, 1, "　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　");
//		this.print(-42, 1, "　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　");
//		this.print(-43, 1, "　放 款 部　　　協理：　　　　　　　　經理：　　　　　　　　襄理：　　　　　　　　　　　　　　製表人：　　　　　　　　　　　　　　　　　");
//		this.print(-44, 1, "　財 務 部　　　協理：　　　　　　　　經理：　　　　　　　　襄理：　　　　　　　　　　　　　　製表人：　　　　　　　　　　　　　　　　　");
//		this.print(-44, 1, "　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　");
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

		Slice<LoanCheque> sLoanCheque = null;
		List<LoanCheque> lLoanCheque = new ArrayList<LoanCheque>();
		List<String> lStatus = new ArrayList<String>();

		lStatus.add("0");

		ArrayList<OccursList> tmp = new ArrayList<>();

		sLoanCheque = loanChequeService.receiveDateRange(this.getTxBuffer().getTxCom().getTbsdyf(),
				this.getTxBuffer().getTxCom().getTbsdyf(), lStatus, this.index, this.limit);

		lLoanCheque = sLoanCheque == null ? null : sLoanCheque.getContent();

		if (lLoanCheque == null || lLoanCheque.isEmpty()) {
			// 出空表
			this.open(titaVo, reportDate, brno, reportCode, reportItem, security, pageSize, pageOrientation);
			this.setCharSpaces(0);
			print(1, 1, "本日無資料");
			return;
		}

		this.open(titaVo, reportDate, brno, reportCode, reportItem, security, pageSize, pageOrientation);

		this.setCharSpaces(0);
//		測試
//		int b = 1;
//		for (int i = 1; i <= 400; i++) {
//			if ((i % 10) == 0) {
//				this.print(-2, i, "" + (i / 10));
//			}
//			this.print(-1, i, "" + (i % 10));
//			this.print(-i, 1, "" + (i % 10));
//		}

		int chequeCnt = 0; // 總計
		int chequeOutCnt = 0; // 外
		int chequeInCnt = 0; // 內
		BigDecimal chequeTotAmt = BigDecimal.ZERO; // 總金額
		CdCode tCdCode = new CdCode();
		for (LoanCheque tLoanCheque : lLoanCheque) {
			print(1, 1, "　　　　　　           ");

			print(1, 1, "　　");
			print(0, 1, FormatUtil.pad9("" + tLoanCheque.getCustNo(), 7)); // 戶號
			print(0, 11, FormatUtil.padX(loanCom.getCustNameByNo(tLoanCheque.getCustNo()), 16)); // 戶名
			print(0, 31, FormatUtil.pad9(tLoanCheque.getBankCode(), 7).substring(0, 3)); // 支票銀行
			print(0, 45, FormatUtil.pad9(tLoanCheque.getBankCode(), 7).substring(3, 7));// 支票分行
			print(0, 61, FormatUtil.padX("" + tLoanCheque.getChequeAcct(), 9));// 帳號
			print(0, 72, FormatUtil.padX("" + tLoanCheque.getChequeNo(), 7));// 票號
			this.print(0, 102, df.format(tLoanCheque.getChequeAmt()), "R");// 票面金額
			tCdCode = new CdCode();
			tCdCode = cdCodeService.getItemFirst(3, "OutsideCode", tLoanCheque.getOutsideCode(), titaVo);
			if (tCdCode != null) {
				print(0, 109, tCdCode.getItem());// 埠別
			} else {
				print(0, 109, FormatUtil.padX("" + tLoanCheque.getOutsideCode(), 1));// 埠別
			}

			print(0, 117, this.showBcDate((tLoanCheque.getChequeDate() + 19110000), 0));// 到期日
			tCdCode = new CdCode();
			tCdCode = cdCodeService.getItemFirst(3, "AreaCode", tLoanCheque.getAreaCode(), titaVo);
			if (tCdCode != null) {
				print(0, 131, tCdCode.getItem());// 交換區號
			} else {
				print(0, 131, FormatUtil.pad9("" + tLoanCheque.getAreaCode(), 2));// 交換區號
			}
			print(1, 31, FormatUtil.padX("", 10));// 發票人ID
			print(0, 45, FormatUtil.padX(tLoanCheque.getChequeName(), 60));// 發票人姓名

			chequeCnt = chequeCnt + 1;
			if ("2".equals(tLoanCheque.getOutsideCode())) {
				chequeOutCnt = chequeOutCnt + 1;
			} else if ("1".equals(tLoanCheque.getOutsideCode())) {
				chequeInCnt = chequeInCnt + 1;
			}

			chequeTotAmt = chequeTotAmt.add(tLoanCheque.getChequeAmt());
		}

		print(1, 1, "－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－");
		print(1, 1, "　　　　　　           ");

		print(0, 1,
				"支票 ： 共  " + FormatUtil.pad9("" + chequeCnt, 3) + "  張　　本埠 ： " + FormatUtil.pad9("" + chequeInCnt, 3)
						+ "  張　　外埠 ： " + FormatUtil.pad9("" + chequeOutCnt, 3) + "  張　　總計 ： "
						+ df.format(chequeTotAmt));

	}

}
