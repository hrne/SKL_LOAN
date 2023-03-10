package com.st1.itx.trade.L4;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.BankRemit;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.service.BankRemitService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;

@Component
@Scope("prototype")

public class L4101ReportG extends MakeReport {

	/* DB服務注入 */

	@Autowired
	BankRemitService bankRemitService;
	@Autowired
	CustMainService custMainService;

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
	private String reportCode = "L4101-G";
	private String reportItem = "付款憑單";
	private String security = "機密";
	private String pageSize = "A4";
	private String pageOrientation = "L";

	// 製表日期
	private String nowDate;
	// 製表時間
	private String nowTime;

	int iAcDate = 0;
	String batchNo = "";
	int cnt = 0; // 總筆數

	// 自訂表頭
	@Override
	public void printHeader() {

		this.info("L4101ReportG.printHeader");

		this.print(-1, 2, "程式ID：" + this.getParentTranCode());
		this.print(-1, 85, "新光人壽保險股份有限公司", "C");
		this.print(-1, 145, "機密等級：" + this.security);
		this.print(-2, 2, "報　表：" + this.reportCode);
		this.print(-2, 85, this.reportItem, "C");
		this.print(-2, 145, "日　　期：" + showBcDate(this.nowDate, 1));
		this.print(-3, 2, "來源別：放款服務課");
		this.print(-3, 145, "時　　間：" + showTime(this.nowTime));
		this.print(-4, 2, "批號：" + titaVo.getBacthNo());
		this.print(-4, 145, "頁　　次：" + this.getNowPage());
		this.print(-4, 85, "會計日期：" + this.showRocDate(reportDate, 1), "C");

		// 頁首帳冊別判斷
//		print(-4, 10, this.nowAcBookCode);
//		print(-4, 14, this.nowAcBookItem);

		// 明細起始列(自訂亦必須)
		this.setBeginRow(10);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(28);

		/**
		 * ---------1---------2---------3---------4---------5---------6---------7---------8---------9---------0---------1---------2---------3---------4---------5---------6
		 * 1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890
		 */
//		print(1, 1, "請款單位：　　　　　　　　　付款方式：　　　　　　　　　　匯款/票匯類別：　　　　　　　　付款憑單批號：　　　　　　　　　");
		print(1, 1, "");
		print(1, 1, "－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－");
		print(1, 1, "受款銀行　受款人ID　　受款人　　　　　　支票限制　請款日期　　　　　　付款金額　相關號碼　　　　　　付款來源　　　　　　　　　作業者");
		print(1, 1, "　　　　　　　　　　　受款帳號　　　　　　　　　　　　　　　　　　　　　　　　　交易摘要");
		print(1, 1, "－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－");
	}

	// 自訂表尾
	@Override
	public void printFooter() {
		this.print(-41, 1, "　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　");
		this.print(-42, 1, "　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　");
		this.print(-43, 1, "　　　　　　　　　　　　　　主管覆核：　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　製表：　　　　　　　　　　　　　　　　　");
		this.print(-44, 1, "　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　");
	}

	public void exec(List<BankRemit> lBankRemit, TitaVo titaVo) throws LogicException {
		this.info("L4101ReportG exec ...");

		// 設定字體1:標楷體 字體大小12
		this.setFont(1, 12);

		this.reportDate = Integer.valueOf(titaVo.getParam("AcDate")) + 19110000;

		this.brno = titaVo.getBrno();
		this.nowDate = dDateUtil.getNowStringRoc();
		this.nowTime = dDateUtil.getNowStringTime();

		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(reportCode)
				.setRptItem(reportItem).setSecurity(security).setRptSize(pageSize).setPageOrientation(pageOrientation)
				.build();
		this.open(titaVo, reportVo);

		// 合計容器
		BigDecimal totalremitAmt = BigDecimal.ZERO;
		String wkBankItem = "";
		String wkBranchItem = "";
		String wkFullName = "";
		String wkAmlRspItem = "";
		String CurrencyCode = lBankRemit.get(0).getCurrencyCode();

		this.setCharSpaces(0);
		this.setFont(1, 10);

		int rounds = 0;
		int oldCustNo = 0;
		int oldFacmNo = 0;
		int i = 0;
		this.info("總數 = " + lBankRemit.size());
		for (BankRemit tBankRemit : lBankRemit) {
			rounds++;
			i++;

			// 受款銀行
			String remitBank = tBankRemit.getRemitBank() + tBankRemit.getRemitBranch();
			// 受款人ID
			String wkCustId = "";
			String wkCustNm = "";
			CustMain tCustMain = custMainService.custNoFirst(tBankRemit.getCustNo(), tBankRemit.getCustNo(), titaVo);
			if (tCustMain != null) {
				wkCustId = tCustMain.getCustId();
			}
			// 受款人
			wkCustNm = tBankRemit.getCustName();
			// 受款帳號
			String remitAcctNo = tBankRemit.getRemitAcctNo();
			// TODO:支票限制??
			// 請款日期
			int acDate = tBankRemit.getAcDate();
			// 付款金額
			BigDecimal remitAmt = tBankRemit.getRemitAmt();
			// 相關號碼
			String number = FormatUtil.pad9("" + tBankRemit.getCustNo(), 7) + "-"
					+ FormatUtil.pad9("" + tBankRemit.getFacmNo(), 3) + "-"
					+ FormatUtil.pad9("" + tBankRemit.getBormNo(), 3);
			// 交易摘要
			String remarkt = tBankRemit.getRemark();
			// 付款來源 t:放款系統
			// 作業員
			String tlrNo = tBankRemit.getTitaTlrNo();

			// 明細資料第一行
			print(1, 1, "　　");

			print(0, 1, remitBank);// 受款銀行
			print(0, 11, wkCustId);// 受款人ID
			print(0, 23, wkCustNm);// 受款人
			print(0, 51, "" + acDate);// 請款日期
			print(0, 79, formatAmt(remitAmt, 0), "R");// 付款金額
			print(0, 81, number);// 相關號碼
			print(0, 100, "t:放款系統");// 付款來源
			print(0, 127, tlrNo);// 作業員

			print(1, 1, "　　");
			print(0, 23, remitAcctNo);// 受款帳號
			print(0, 81, remarkt);// 交易摘要
			totalremitAmt = totalremitAmt.add(remitAmt);

		}

		print(1, 1, "－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－");
		print(1, 1, "　　　　　　           ");
		print(0, 1, "付款筆數：　　　");
		print(0, 20, "" + i, "R");
		print(0, 30, "付款金額：　　　");
		print(0, 58, formatAmt(totalremitAmt, 0), "R");
		print(0, 60, "請款筆數：　　　");
		print(0, 80, "" + i, "R");
		print(0, 90, "請款金額：　　　");
		print(0, 118, formatAmt(totalremitAmt, 0), "R");
	}

}
