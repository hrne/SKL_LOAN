package com.st1.itx.trade.L9;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;

import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeFile;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.date.DateUtil;

@Component("L9135Report")
@Scope("prototype")

public class L9135Report extends MakeReport {

	@Autowired
	MakeExcel makeExcel;

	@Autowired
	MakeFile makeFile;

	@Autowired
	DateUtil dateUtil;

	String f0 = "";
	String f1 = "";
	int ptfg = 0;
	int cnt = 0;

	int amtDb = 0;
	int amtCr = 0;

	int tmpCnt = 0;
	int tmpAmtDb = 0;
	int tmpAmtCr = 0;

	String tradeNo = "L9135";
	String tradeName = "銀行存款媒體明細表（總帳）";

	String iAcDate = "0";

	DecimalFormat formatAmt = new DecimalFormat("#,##0.00");

	// 分隔線
	String divider = "---------------------------------------------------------------------------------------------------------------------------------------------------------------------";
	String nextPageText = "=====  續下頁  =====";
	String endText = "=====  報  表  結  束  =====";

	// 橫式規格
	@Override
	public void printHeader() {
		this.setFontSize(10);
		this.setCharSpaces(0);

		int rNum = 3;
		int lNum = 146;
		int cNum = this.getMidXAxis();

		this.print(-1, lNum, "機密等級：" + this.getSecurity());
		this.print(-2, rNum, "　程式ID：" + this.getParentTranCode());
		this.print(-2, cNum, "新光人壽保險股份有限公司", "C");
		this.print(-3, rNum, "　報　表：" + this.getRptCode());
		String tim = String.format("%02d", Integer.parseInt(dateUtil.getNowStringBc().substring(4, 6)));

		this.print(-2, lNum, "日　  期：" + tim + "/" + dateUtil.getNowStringBc().substring(6, 8) + "/"
				+ dateUtil.getNowStringBc().substring(2, 4));
		this.print(-3, cNum, tradeName, "C");
		this.print(-3, lNum, "時　  間：" + dateUtil.getNowStringTime().substring(0, 2) + ":"
				+ dateUtil.getNowStringTime().substring(2, 4) + ":" + dateUtil.getNowStringTime().substring(4, 6));
		this.print(-4, lNum, "頁　  數：   " + Integer.toString(this.getNowPage()));

		this.print(-5, cNum, showRocDate(this.iAcDate, 0), "C");

		this.print(-7, 3, "科目");
		this.print(-7, 60, "子目");
		this.print(-7, 100, "傳票號碼");
		this.print(-7, 120, "借方金額");
		this.print(-7, 150, "貸方金額");
		this.print(-8, 1, divider);

		// 明細起始列(自訂亦必須)
		this.setBeginRow(9);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(45);
	}

	/**
	 * 小計(總計)筆數及合計
	 * 
	 */
	private void reportTot() {
		this.print(1, 1, this.divider);
		this.print(1, 3, "子目小計", "L");
		this.print(0, 30, cnt + " 筆", "R");
		this.print(0, 130, formatAmt.format(amtDb), "R");
		this.print(0, 160, formatAmt.format(amtCr), "R");

		this.print(1, 1, "");

		cnt = 0;
		amtDb = 0;
		amtCr = 0;
		if (ptfg != 9) {
			return;
		}
		this.print(1, 1, this.divider);
		this.print(1, 3, "合    計", "L");
		this.print(0, 30, tmpCnt + " 筆", "R");
		this.print(0, 130, formatAmt.format(tmpAmtDb), "R");
		this.print(0, 160, formatAmt.format(tmpAmtCr), "R");
	}

	/**
	 * 執行報表產製
	 * 
	 * @param titaVo
	 * @param l9135Result 資料串
	 * @param iAcDate     會計日
	 * @return
	 * @throws LogicException
	 */

	public void exec(TitaVo titaVo, List<Map<String, String>> l9135Result, int iAcDate) throws LogicException {
		this.info("L9135Report exec");
		this.info("L9135Report exportExcel");
		// 銀行存款媒體明細表
		exportExcel(titaVo, l9135Result, iAcDate);

		this.info("L9135Report exportTxt");
		// 媒體檔
		exportTxt(titaVo, l9135Result, iAcDate);

	}

	private void report(Map<String, String> r) throws LogicException {
		// 科目
		this.print(1, 3, r.get("AcNoCode") + " " + r.get("AcNoItem"));

		// 子目
		this.print(0, 59, r.get("AcSubCode") + "   " + r.get("AcctItem"));

		// 傳票號碼
		this.print(0, 102, r.get("SlipNo"));

		// 借方金額
		this.print(0, 130, formatAmt.format(Integer.valueOf(r.get("DbTxAmt"))), "R");

		// 貸方金額
		this.print(0, 160, formatAmt.format(Integer.valueOf(r.get("CrTxAmt"))), "R");

		cnt += 1;
		amtDb += Integer.valueOf(r.get("DbTxAmt"));
		amtCr += Integer.valueOf(r.get("CrTxAmt"));

		// 全部合計
		tmpCnt += 1;
		tmpAmtDb += Integer.valueOf(r.get("DbTxAmt"));
		tmpAmtCr += Integer.valueOf(r.get("CrTxAmt"));
	}

	public void exportExcel(TitaVo titaVo, List<Map<String, String>> l9135Result, int iAcDate) throws LogicException {

//		this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), tradeNo, tradeName, "", "A4", "L");
		String txcd = titaVo.getTxCode();
		int reportDate = titaVo.getEntDyI() + 19110000;
		String brno = titaVo.getBrno();
		String reportItem = tradeName;
		String security = this.getSecurity();
		String pageSize = "A4";
		String pageOrientation = "L";

		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(txcd)
				.setRptItem(reportItem).setSecurity(security).setRptSize(pageSize).setPageOrientation(pageOrientation)
				.build();
		this.open(titaVo, reportVo);

		// 記錄筆數
		int count = 0;

		if (l9135Result != null && l9135Result.size() != 0) {

			for (Map<String, String> r : l9135Result) {

				count++;

				// 判斷子目不同分隔
				if (!f1.equals(r.get("AcSubCode"))) {

					if (count > 1) {
						reportTot();

					}

					f0 = r.get("AcNoCode").toString();
					f1 = r.get("AcSubCode").toString();

				}

				report(r);

				// 超過40行 換新頁
				if (this.NowRow >= 40) {

					this.print(2, 80, this.nextPageText, "C");
					this.newPage();

				}

			}

			if (this.getNowPage() > 0 && count == l9135Result.size()) {
				ptfg = 9;
				reportTot();

				this.print(-45, 80, this.endText, "C");
			}

		} else {
			this.print(1, 1, "本日無資料");
		}

		this.close();
	}

	public void exportTxt(TitaVo titaVo, List<Map<String, String>> l9135Result, int iAcDate) throws LogicException {

		BigDecimal amtDb = BigDecimal.ZERO;
		BigDecimal amtCr = BigDecimal.ZERO;
		BigDecimal amt = BigDecimal.ZERO;
		DecimalFormat df = new DecimalFormat("0.00#");

		int date = iAcDate;
		String brno = titaVo.getBrno();
		String fileCode = titaVo.getTxcd();
		String fileItem = "銀行存款媒體檔";
		String fileName = "銀行存款媒體檔.txt";

		ReportVo reportVo = ReportVo.builder().setRptDate(date).setBrno(brno).setRptCode(fileCode).setRptItem(fileItem)
				.build();

		makeFile.open(titaVo, reportVo, fileName, 2);

		if (l9135Result.size() > 0) {

			for (Map<String, String> r : l9135Result) {
//				this.info("r.get(\"DbTxAmt\") =" + r.get("DbTxAmt"));
//				this.info("r.get(\"DbTxAmt\") =" + r.get("CrTxAmt"));
				amtDb = new BigDecimal(r.get("DbTxAmt") == null ? "0" : r.get("DbTxAmt"));
				amtCr = new BigDecimal(r.get("CrTxAmt") == null ? "0" : r.get("CrTxAmt"));

				// 借貸合計金額
				amt = amtCr.add(amtDb);
				// 格式化
				BigDecimal iamt = new BigDecimal(df.format(amt));

				// 日期
				String iDate = String.valueOf(iAcDate);
				// 帳號
				String iAcctItem = r.get("AcctItem2") == null ? "          " : r.get("AcctItem2");
				// 借貸別 C = 3, D = 4
				String iDbCr = "C".equals(r.get("DbCr")) ? "3" : "4";
				// 金額
				String amtText = fillUpWord(iamt.toString(), 13, "0", "L");
				// 傳票號碼
				String iSlipNo = fillUpWord(r.get("SlipNo"), 5, "0", "L");

				// 日期(7) + 帳號(10) + 借貸別(1) + 金額(13) + 傳票號碼(5)
				String text = iDate + iAcctItem + iDbCr + amtText + iSlipNo;
//				this.info("text = " + text);
				makeFile.put(text);
			}
		}
		makeFile.close();

	}

}
