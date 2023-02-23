package com.st1.itx.trade.L9;

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
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.date.DateUtil;

@Component("L9136Report")
@Scope("prototype")

public class L9136Report extends MakeReport {

	@Autowired
	DateUtil dateUtil;

	@Autowired
	L9136ServiceImpl l9136ServiceImpl;

	int ptfg = 0;

	private String tradeNo = "L9136";
	private String tradeName = "檔案資料變更日報表";

	private String isAcDate = "0";
	private String ieAcDate = "0";

	// 分頁標題分類
	private String titleItem = "";
	// 欄位標題
	private String item = "";
	// 資料來源 1:資料變更 2:主管核可、主管放行
	private int dataSource = 1;

//	private DecimalFormat formatAmt = new DecimalFormat("#,##0.00");

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

		this.print(-5, 3, "種類：" + titleItem);

		this.print(-7, 3, "主管卡使用日");
		this.print(-7, 28, "戶  號");
		this.print(-7, 43, "戶  名");
		this.print(-7, 55, "核准號碼");
		this.print(-7, 69, "押品別");
		this.print(-7, 86, "押品號碼");
		this.print(-7, 100, item);

		if (dataSource == 1) {
			this.print(-7, 121, "更  改  前  內  容");
			this.print(-7, 148, "更  改  後  內  容");
		} else {
			this.print(-7, 136, "內  容");
		}
		this.print(-7, 177, "經辦");
		this.print(-7, 186, "授權主管");
		this.print(-7, 200, "備註");
		this.print(-8, 1, divider);

		// 明細起始列(自訂亦必須)
		this.setBeginRow(9);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(45);
	}

	/**
	 * 
	 * @param titaVo
	 * @param l9136Result
	 * @param l9136Result2
	 * @param isAcDate
	 * @param ieAcDate
	 * @return
	 * @throws LogicException
	 */
	public void exec(TitaVo titaVo, List<Map<String, String>> l9136Result, List<Map<String, String>> l9136Result2,
			int isAcDate, int ieAcDate) throws LogicException {

		this.info("L9136Report exec");

		List<Map<String, String>> l9136List = l9136Result;
		List<Map<String, String>> l9136List2 = l9136Result2;

		this.isAcDate = String.valueOf(isAcDate);
		this.ieAcDate = String.valueOf(ieAcDate);

		this.titleItem = "資料變更";
		this.item = "變更項目";
		this.dataSource = 1;

		ReportVo reportVo = ReportVo.builder().setRptDate(titaVo.getEntDyI()).setBrno(titaVo.getKinbr())
				.setRptCode(tradeNo).setRptItem(tradeName).setSecurity("").setRptSize("A4").setPageOrientation("L")
				.build();

		this.open(titaVo, reportVo);

		// 記錄筆數
		int count = 0;

		if (l9136List != null && l9136List.size() != 0) {

			for (Map<String, String> r : l9136List) {
				count++;

				String[] tmpUpdateItem = r.get("Item").replaceAll("\\[\"", "").replaceAll("\"\\]", "")
						.replaceAll("\"\\,\"", "@").split("@");

				String[] tmpOldContent = r.get("Old").replaceAll("\\[\"", "").replaceAll("\"\\]", "")
						.replaceAll("\"\\,\"", "@").split("@");

				String[] tmpNewContent = r.get("New").replaceAll("\\[\"", "").replaceAll("\"\\]", "")
						.replaceAll("\"\\,\"", "@").split("@");

				// 要排除的字段
				String[] word = { "最後更新日期時間", "最後更新人員", "建檔日期時間", "交易進行記號", "上次櫃員編號", "上次交易序號", "已編BorTx流水號", "上次會計日",
						"會計日期", "上次交易行別", "上次交易日", "經辦合理性說明" };

				List<String> tmpWord = new ArrayList<String>(Arrays.asList(word));
				List<String> tmp1 = new ArrayList<String>(Arrays.asList(tmpUpdateItem));
				List<String> tmp2 = new ArrayList<String>(Arrays.asList(tmpOldContent));
				List<String> tmp3 = new ArrayList<String>(Arrays.asList(tmpNewContent));

				// 避免有空白
				if (tmp1.size() == 0 || tmp2.size() == 0 || tmp3.size() == 0) {
					continue;
				}

				for (int i = 0; i < tmp1.size(); i++) {
					for (int j = 0; j < tmpWord.size(); j++) {
						// 排除不需要的顯示的字串
						if (tmp1.get(i).toString().indexOf(tmpWord.get(j).toString()) == 0) {
							tmp1.set(i, "");
							tmp2.set(i, "");
							tmp3.set(i, "");
						}
					}
				}

				this.info("tmp1.size()=" + tmp1.size());
				this.info("tmp1 = " + tmp1.toString());
				this.info("tmp2.size()=" + tmp2.size());
				this.info("tmp2 = " + tmp2.toString());
				this.info("tmp3.size()=" + tmp3.size());
				this.info("tmp3 = " + tmp3.toString());

				if (tmp1.size() == tmp2.size() && tmp1.size() == tmp3.size() && tmp2.size() == tmp3.size()) {
				} else {
					continue;
				}

				for (int i = 0; i < tmp1.size(); i++) {

					// 排除空值的資料
					if (tmp1.get(i).length() != 0) {

						report(r, tmp1.get(i), tmp2.get(i), tmp3.get(i), this.dataSource);
					}

					// 超過40行 換新頁
					if (this.NowRow >= 40) {

						this.print(2, this.getMidXAxis(), this.nextPageText, "C");
						this.newPage();

					}

				}

			}

		} else {
			this.print(1, 1, "本日無資料");
		}

		this.titleItem = "主管核可";
		this.item = "交易別";
		this.dataSource = 2;
		this.newPage();

		// 主管核可
		if (l9136List2 != null && l9136List2.size() != 0) {
			int tmpCount = 0;
			for (Map<String, String> r : l9136List2) {
				if ("1".equals(r.get("Seq"))) {
					count++;

					report(r, r.get("Item"), r.get("Old"), r.get("New"), this.dataSource);

					// 超過40行 換新頁
					if (this.NowRow >= 40) {

						this.print(2, this.getMidXAxis(), this.nextPageText, "C");
						this.newPage();

					}

					tmpCount = count;
				}
			}

			// 沒有增加筆數表示此表無資料
			if (tmpCount != count) {
				this.print(1, 1, "本日無資料");
			}

		} else {
			this.print(1, 1, "本日無資料");
		}

		this.titleItem = "主管放行";
		this.item = "交易別";
		this.dataSource = 2;
		this.newPage();

		// 主管放行
		if (l9136List2 != null && l9136List2.size() != 0) {
			int tmpCount = 0;
			for (Map<String, String> r : l9136List2) {
				if ("2".equals(r.get("Seq"))) {
					count++;

					report(r, r.get("Item"), r.get("Old"), r.get("New"), this.dataSource);

					// 超過40行 換新頁
					if (this.NowRow >= 40) {

						this.print(2, this.getMidXAxis(), this.nextPageText, "C");
						this.newPage();

					}
					tmpCount = count;
				}

			}
			// 沒有增加筆數表示此表無資料
			if (tmpCount != count) {
				this.print(1, 1, "本日無資料");
			}

		} else {
			this.print(1, 1, "本日無資料");
		}

		if (this.getNowPage() > 0 && count == ((l9136List != null ? l9136List.size() : 0)
				+ (l9136List2 != null ? l9136List2.size() : 0))) {
			ptfg = 9;

			this.print(-45, this.getMidXAxis(), this.endText, "C");
		}

		this.close();

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

			supNoName = r.get("SupNoName");
			txNo = String.valueOf(Integer.valueOf(r.get("TxSeq").substring(10, 18)));

		} else {

			supNoName = r.get("SupNoName");
			txNo = String.valueOf(Integer.valueOf(r.get("TxSeq")));
		}

		// 主管卡使用日
		this.print(1, 1, showRocDate(r.get("AcDate"), 1) + "-");
		// 交易序號(8碼)
		this.print(0, 21, fillUpWord(txNo, 7, "0", "L"), "R");

		// 戶號-額度-撥款
		if (!"0".equals(r.get("CustNo"))) {
			this.print(0, 31, r.get("CustNo"), "R");

			if (!"0".equals(r.get("FacmNo"))) {
				this.print(0, 31, "-", "L");
				this.print(0, 36, fillUpWord(r.get("FacmNo"), 3, "0", "L"), "R");

				if (!"0".equals(r.get("BormNo"))) {
					this.print(0, 36, "-", "L");
					this.print(0, 41, fillUpWord(r.get("BormNo"), 3, "0", "L"), "R");
				}
			}
		}

		// 戶名
		this.print(0, 43, r.get("CustName").length() > 5 ? r.get("CustName").substring(0, 6) : r.get("CustName"));

		// 判斷屬於"資料變更"
		if (dataSource == 1) {
			// "資料變更"的 放入交易別
			// 1.有核准號碼、押品別、押品號碼
			// 2.有L56XX 法催紀錄、L57XX 債務協商
			if (r.get("ApproveNo").trim().length() != 0) {

				// 核准號碼
				this.print(0, 56, r.get("ApproveNo"));
				// 押品別
				this.print(0, 66, r.get("ClCode1") + "  " + r.get("ClCode2") + "  " + r.get("ClName"));
				// 押品號碼
				this.print(0, 86, r.get("ClNo"));

			} else if ("L56".equals(r.get("TranNo").substring(0, 3)) || "L57".equals(r.get("TranNo").substring(0, 3))) {

				this.print(0, 56, r.get("TranNo") + " " + r.get("TranItem"));

			}

		} else {
			// 核准號碼
			this.print(0, 56, r.get("ApproveNo"));
			// 押品別
			this.print(0, 66, r.get("ClCode1") + "  " + r.get("ClCode2") + "  " + r.get("ClName"));
			// 押品號碼
			this.print(0, 86, r.get("ClNo"));
		}

		// 變更項目/交易別
		if (dataSource == 1) {
			tmpUpdateItem = tmpUpdateItem.trim();
			this.print(0, 97, tmpUpdateItem.length() == 0 ? " " : fillUpWord(tmpUpdateItem, 20, ".", "R"));

		} else {
			tmpUpdateItem = r.get("TranNo") + " " + r.get("TranItem");
			this.print(0, 97, tmpUpdateItem.length() == 0 ? " " : fillUpWord(tmpUpdateItem, 32, " ", "R"));
		}

//		String bfContent = tmpNewContent.trim().length() == 0 ? " "
//				: String.format("%03d", Integer.valueOf(r.get("FacmNo"))) + "-"
//						+ String.format("%03d", Integer.valueOf(r.get("BormNo"))) + " " + tmpOldContent;
		// 更改前內容
		this.print(0, 122, tmpOldContent.length() == 0 ? " " : fillUpWord(tmpOldContent, 22, " ", "R"));

		// 更改後內容
		String afContent = tmpNewContent.trim();

		if (dataSource == 1) {

			this.print(0, 149, afContent.length() == 0 ? " " : fillUpWord(afContent, 24, " ", "R"));

		} else {

			this.print(0, 136, afContent.length() == 0 ? " " : afContent);

		}

		// 經辦
		this.print(0, 177, r.get("Name"));

		// 授權主管
		this.print(0, 186, supNoName);
//		this.print(0, 186, "1234567");

	}


}
