package com.st1.itx.trade.L9;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;

import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.date.DateUtil;

@Component("L9135Report")
@Scope("prototype")

public class L9135Report extends MakeReport {

	@Autowired
	MakeExcel makeExcel;

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

		this.print(-1, lNum, "機密等級：密");
		this.print(-2, rNum, "　程式ID：" + this.getParentTranCode());
		this.print(-2, cNum, "新光人壽保險股份有限公司", "C");
		this.print(-3, rNum, "　報　表：" + this.getRptCode());
		String tim = String.format("%02d", Integer.parseInt(dateUtil.getNowStringBc().substring(4, 6)));

		this.print(-2, lNum, "日　  期：" + tim + "/" + dateUtil.getNowStringBc().substring(6, 8) + "/" + dateUtil.getNowStringBc().substring(2, 4));
		this.print(-3, cNum, tradeName, "C");
		this.print(-3, lNum, "時　  間：" + dateUtil.getNowStringTime().substring(0, 2) + ":" + dateUtil.getNowStringTime().substring(2, 4) + ":" + dateUtil.getNowStringTime().substring(4, 6));
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
	 */

	public List<Map<String, String>> exec(TitaVo titaVo, List<Map<String, String>> l9135Result, int iAcDate) throws LogicException {

		this.info("L9135Report exec");

		List<Map<String, String>> l9135List = l9135Result;

		this.iAcDate = String.valueOf(iAcDate);

		this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), tradeNo, tradeName, "", "A4", "L");

		// 記錄筆數
		int count = 0;

		if (l9135List != null && l9135List.size() != 0) {

			for (Map<String, String> r : l9135List) {

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

			if (this.getNowPage() > 0 && count == l9135List.size()) {
				ptfg = 9;
				reportTot();

				this.print(-45, 80, this.endText, "C");
			}

		} else {
			this.print(1, 1, "本日無資料");
		}

		this.close();

		return l9135List;

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

}
