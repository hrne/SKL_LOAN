package com.st1.itx.trade.L9;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.L9136ServiceImpl;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.date.DateUtil;

@Component("L9136Report")
@Scope("prototype")

public class L9136Report extends MakeReport {

	@Autowired
	DateUtil dateUtil;

	@Autowired
	L9136ServiceImpl l9136ServiceImpl;

	int ptfg = 0;

	String tradeNo = "L9136";
	String tradeName = "檔案資料變更日報表";

	String isAcDate = "0";
	String ieAcDate = "0";

	DecimalFormat formatAmt = new DecimalFormat("#,##0.00");

	// 分隔線
	String divider = "-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------";
	String nextPageText = "=====  續下頁  =====";
	String endText = "=====  報  表  結  束  =====";

	// 橫式規格
	@Override
	public void printHeader() {

		this.setFontSize(9);
		this.setCharSpaces(0);

		int rNum = 3;
		int lNum = 182;
		int cNum = this.getMidXAxis();

		this.print(-1, lNum, "機密等級：普通");
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

		this.print(-5, cNum, showRocDate(this.isAcDate, 0) + " 至 " + showRocDate(this.ieAcDate, 0), "C");

		this.print(-7, 3, "主管卡使用日");
		this.print(-7, 28, "戶  號");
		this.print(-7, 43, "戶  名");
		this.print(-7, 55, "核准號碼");
		this.print(-7, 69, "押品別");
		this.print(-7, 86, "押品號碼");
		this.print(-7, 100, "更正項目");
		this.print(-7, 119, "更  改  前  內  容");
		this.print(-7, 149, "更  改  後  內  容");
		this.print(-7, 175, "經辦");
		this.print(-7, 186, "授權主管");
		this.print(-7, 200, "備註");
		this.print(-8, 1, divider);

		// 明細起始列(自訂亦必須)
		this.setBeginRow(9);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(45);
	}

	/**
	 * 執行報表產製
	 * 
	 * @param titaVo
	 * @param L9136Result 資料串
	 * @param iAcDate     會計日
	 */

	public List<Map<String, String>> exec(TitaVo titaVo, List<Map<String, String>> l9136Result,
			List<Map<String, String>> l9136Result2, int isAcDate, int ieAcDate) throws LogicException {

		this.info("L9136Report exec");

		List<Map<String, String>> l9136List = l9136Result;
		List<Map<String, String>> l9136List2 = l9136Result2;

		this.isAcDate = String.valueOf(isAcDate);
		this.ieAcDate = String.valueOf(ieAcDate);

		this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), tradeNo, tradeName, "", "A4", "L");

		// 記錄筆數
		int count = 0;

		if (l9136List != null && l9136List.size() != 0) {

			for (Map<String, String> r : l9136List) {

				count++;

				String[] tmpUpdateItem = r.get("Item").replaceAll("\\[", "").replaceAll("\\]", "")
						.replaceAll("\"\"", " ").replaceAll("\"", "").split(",");

				String[] tmpOldContent = r.get("Old").replaceAll("\\[", "").replaceAll("\\]", "")
						.replaceAll("\"\"", " ").replaceAll("\"", "").split(",");

				String[] tmpNewContent = r.get("New").replaceAll("\\[", "").replaceAll("\\]", "")
						.replaceAll("\"\"", " ").replaceAll("\"", "").split(",");

				// 要排除的字段
				String[] word = { "最後更新日期時間", "最後更新人員" };

				List<String> tmpWord = new ArrayList<String>(Arrays.asList(word));
				List<String> tmp1 = new ArrayList<String>(Arrays.asList(tmpUpdateItem));
				List<String> tmp2 = new ArrayList<String>(Arrays.asList(tmpOldContent));
				List<String> tmp3 = new ArrayList<String>(Arrays.asList(tmpNewContent));

//				this.info("======"+tmp1.size());
//				this.info("tmp1======"+tmp1.toString());
//				this.info("tmp2======"+tmp2.toString());
//				this.info("tmp3======"+tmp3.toString());

				for (int i = 0; i < tmp1.size(); i++) {
					for (int j = 0; j < tmpWord.size(); j++) {
						// 排除不需要的顯示的字串
						if (tmp1.get(i).toString().indexOf(tmpWord.get(j).toString()) == 0) {

							tmp1.remove(i);
							tmp2.remove(i);
							tmp3.remove(i);
							break;
						}
					}
				}

				for (int i = 0; i < tmp1.size(); i++) {

					report(r, tmp1.get(i), tmp2.get(i), tmp3.get(i), 1);

					// 超過40行 換新頁
					if (this.NowRow >= 40) {

						this.print(2, this.getMidXAxis(), this.nextPageText, "C");
						this.newPage();

					}

				}

			}

		}

		// 找訂正
		if (l9136List2 != null && l9136List2.size() != 0) {

			for (Map<String, String> r : l9136List2) {

				count++;

				report(r, r.get("Item"), r.get("Old"), r.get("New"), 2);

				// 超過40行 換新頁
				if (this.NowRow >= 40) {

					this.print(2, this.getMidXAxis(), this.nextPageText, "C");
					this.newPage();

				}

			}

		}

		if (this.getNowPage() > 0 && count == l9136List.size() + l9136List2.size()) {
			ptfg = 9;

			this.print(-45, this.getMidXAxis(), this.endText, "C");
		}

		if (l9136List == null  && l9136List2 == null ) {
			this.print(1, 1, "本日無資料");
		}

		this.close();

		return l9136List;

	}

	/**
	 * @param r             資料串
	 * @param tmpUpdateItem 更改項目名稱
	 * @param tmpOldContent 更改前內容
	 * @param tmpNewContent 更改後內容
	 * @param dataSource    資料來源
	 */
	private void report(Map<String, String> r, String tmpUpdateItem, String tmpOldContent, String tmpNewContent,
			int dataSource) throws LogicException {

		// 授權主管
		String supNoName = "";
		// 交易序號
		String txNo = "";

		if (dataSource == 1) {

			List<Map<String, String>> l9136findSupNo = null;

			try {
				l9136findSupNo = l9136ServiceImpl.findSupNo(titaVo, r.get("TxSeq").toString());
			} catch (Exception e) {
				this.info("L9136ServiceImpl.findSupNo error = " + e.toString());
			}

			supNoName = l9136findSupNo.get(0).get("SupNoName");
			txNo = String.valueOf(Integer.valueOf(l9136findSupNo.get(0).get("TxSeq")));

		} else {

			supNoName = r.get("SupNoName");
			txNo = String.valueOf(Integer.valueOf(r.get("TxSeq")));
		}

		// 主管卡使用日
		this.print(1, 1, showRocDate(r.get("AcDate"), 1) + "-");
		// 交易序號(8碼)
		this.print(0, 21, txNo, "R");

		// 戶號-額度-撥款
		this.print(0, 31, r.get("CustNo"), "R");
		this.print(0, 31, "-", "L");
		this.print(0, 36, r.get("FacmNo"), "R");
		this.print(0, 36, "-", "L");
		this.print(0, 41, r.get("BormNo"), "R");

		// 戶名
		this.print(0, 43, r.get("CustName").length() > 5 ? r.get("CustName").substring(0, 6) : r.get("CustName"));

		// 核准號碼
//		this.print(0, 55, "1122383");
		this.print(0, 56, r.get("ApproveNo"));

		// 押品別
//		this.print(0, 66, 1 + "  " + 1 + "  " + "房地－住宅");
		this.print(0, 66, r.get("ClCode1") + "  " + r.get("ClCode2") + "  " + r.get("ClName"));

		// 押品號碼
//		this.print(0, 87, "1705394");
		this.print(0, 87, r.get("ClNo"));

		tmpUpdateItem = tmpUpdateItem.trim();
		tmpUpdateItem = tmpUpdateItem.length() > 8 ? tmpUpdateItem.substring(0, 8) : tmpUpdateItem;

		// 更正項目
		this.print(0, 97, tmpUpdateItem.length() == 0 ? " " : fillUp(tmpUpdateItem, 16, ".", "R"));

		// 更改前內容
		this.print(0, 120,
				tmpNewContent.trim().length() == 0 ? " "
						: String.format("%03d", Integer.valueOf(r.get("FacmNo"))) + "-"
								+ String.format("%03d", Integer.valueOf(r.get("BormNo"))) + " " + tmpOldContent);

		// 更改後內容
		this.print(0, 150, tmpNewContent.trim().length() == 0 ? " " : tmpNewContent);

		// 經辦
		this.print(0, 175, r.get("Name"));

		// 授權主管
		this.print(0, 186, supNoName);
//		this.print(0, 186, "1234567");

	}

	/**
	 * @param text 文字
	 * @param all  滿額字數
	 * @param word 要補滿的
	 * @param pos  向左補齊(L)/向右補齊(R)
	 */

	private String fillUp(String text, int all, String word, String pos) {

		String str = text;
		int num = 0;
		int allnum = all;
		try {

			num = allnum - str.getBytes("gbk").length;
//			System.out.println(str.getBytes("gbk").length);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String lstr = "";
		for (int i = 0; i < num; i++) {
			if (pos == "L") {
				lstr += word;
			} else {
				str += word;
			}
		}
		if (pos == "L") {
			lstr += text;
			str = lstr;
		}

		return str;
	}

}
