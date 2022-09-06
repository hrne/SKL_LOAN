package com.st1.itx.trade.LM;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LM013ServiceImpl;
import com.st1.itx.db.service.springjpa.cm.LM013ServiceImpl.EntCodeCondition;
import com.st1.itx.db.service.springjpa.cm.LM013ServiceImpl.IsRelsCondition;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.StringCut;

@Component
@Scope("prototype")

public class LM013Report extends MakeReport {

	@Autowired
	public LM013ServiceImpl lM013ServiceImpl;

	@Autowired
	DateUtil dDateUtil;

	private BigDecimal marginAmount;
	private String validDate;

	private String newBorder = "-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------";

	private String reportKind = "";

	private enum DataType {
		Normal(null), Below_Sum("Below_Sum"), Above_Sum("Above_Sum"), Total("Total");

		// keyword對應query中的CustId欄
		// 合計, 總計資料會在該欄位有特別的字串
		// 這些是用來分類特別字串的關鍵字
		private String keyword;

		DataType(String keyword) {
			this.keyword = keyword;
		}

		// 找到第一個擁有對應keyword的資料種類並回傳
		// 因為這裡的設計, 關鍵字不能有重複的情況, 否則會出現意外結果
		public static DataType getType(String s) {
			for (int i = 1; i < DataType.values().length; i++) {
				if (s.contains(DataType.values()[i].keyword)) {
					return DataType.values()[i];
				}
			}

			return DataType.Normal;
		}
	}

	private class DataList {
		private List<Map<String, String>> listBody = null;
		private String reportKind = "";
		private Boolean isFirst = false;

		DataList(List<Map<String, String>> list, String reportKind, Boolean isFirst) {
			this.listBody = list;
			this.reportKind = reportKind;
			this.isFirst = isFirst;
		}

		public List<Map<String, String>> getListBody() {
			return this.listBody;
		}

		public String getReportKind() {
			return this.reportKind;
		}

		public Boolean getIsFirst() {
			return this.isFirst;
		}
	}

	@Override
	public void printHeader() {

		printHeaderL();

	}

	private void printHeaderL() {

		print(-3, 3, "程式ID：" + this.getParentTranCode());
		print(-4, 3, "報  表：" + this.getRptCode());

		print(-1, 192, "機密等級：密");
		print(-2, 192, "日  期：" + this.showBcDate(dDateUtil.getNowStringBc(), 1));
		print(-3, 192, "時  間：" + dDateUtil.getNowStringTime().substring(0, 2) + ":" + dDateUtil.getNowStringTime().substring(2, 4) + ":" + dDateUtil.getNowStringTime().substring(4, 6));
		print(-4, 192, "頁  數：" + this.getNowPage());

		print(-3, newBorder.length() / 2, "新光人壽保險股份有限公司", "C");
		print(-4, newBorder.length() / 2, "金檢報表(放款種類表)" + reportKind, "C");

		print(-6, 1, "核貸總值分界. " + formatAmt(marginAmount, 0));
		print(-7, 1, "金檢日期..... " + validDate);

		/**
		 * -------------------------1---------2---------3---------4---------5---------6---------7---------8---------9---------0---------1---------2---------3---------4---------5---------6---------7---------8---------9---------0
		 * ----------------123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890
		 */
		print(-9, 0, "          放款對象                                                核貸總值                                                                                  帳面總值");
		print(-10, 0, "");
		print(-11, 0, "戶號 額度/名稱 統編　　       不動產抵押     動產抵押       有價證券       銀行保證       專案放款           合計      不動產抵押       動產抵押       有價證券       銀行保證       專案放款           合計");
		print(-12, 0, newBorder);

		// 明細起始列(自訂亦必須)
		this.setBeginRow(13);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(44);

	}

	public Boolean exec(TitaVo titaVo, int type) throws LogicException {

		marginAmount = getBigDecimal(titaVo.getParam("inputAmount"));
		validDate = showRocDate(titaVo.getParam("inputDate"), 1);

		this.info("type=" + type);

		DataList[] listsArray = new DataList[5];

		listsArray[0] = null;
		listsArray[1] = null;
		listsArray[2] = null;
		listsArray[3] = null;
		listsArray[4] = null;

		try {

			switch (type) {
			case 0:
				listsArray[0] = new DataList(lM013ServiceImpl.findAll(titaVo, EntCodeCondition.All, IsRelsCondition.All), "", false);
				break;
			case 1:
				listsArray[1] = new DataList(lM013ServiceImpl.findAll(titaVo, EntCodeCondition.Natural, IsRelsCondition.No), "非關係自然人", false);
				break;
			case 2:
				listsArray[2] = new DataList(lM013ServiceImpl.findAll(titaVo, EntCodeCondition.Natural, IsRelsCondition.Yes), "關係自然人", false);
				break;
			case 3:
				listsArray[3] = new DataList(lM013ServiceImpl.findAll(titaVo, EntCodeCondition.Enterprise, IsRelsCondition.No), "非關係法人", false);
				break;
			case 4:
				listsArray[4] = new DataList(lM013ServiceImpl.findAll(titaVo, EntCodeCondition.Enterprise, IsRelsCondition.Yes), "關係法人", false);
				break;
			default:
				break;
			}

		} catch (Exception e) {
			this.info("lM013ServiceImpl.findAll error = " + e.toString());
		}

		this.info("getReportKind=" + listsArray[type].getReportKind());
		this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM013", "金檢報表(放款種類表)" + listsArray[type].getReportKind(), "密", "A4", "L");

		this.setFont(1, 8);

		this.setCharSpaces(0);

		for (DataList list : listsArray) {

			if (list != null) {

				List<Map<String, String>> thisList = list.getListBody();

				// 除第一頁以外，換頁

				reportKind = list.getReportKind();

//				if (list.getIsFirst() == false) {
//					this.newPage();
//				}

				if (thisList != null && !thisList.isEmpty()) {

					// 每張表初始化處理

					BigDecimal lineAmtTotal = BigDecimal.ZERO;
					BigDecimal bookValueTotal = BigDecimal.ZERO;

					Map<String, String> lastTLDVo = null;

					DataType lastDataType = DataType.Normal;
					DataType thisDataType = DataType.Normal;

					// 開始輸出資料
					this.print(1, 0, " ");

					for (Map<String, String> tLDVo : thisList) {

						// F0 企金別 0,1
						// F1 關係人別 Y,N
						// F2 戶號 7
						// F3 額度 3
						// F4 身分證 10
						// F5 姓名 至少10
						// F6 擔保品代號1
						// F7 核貸總值
						// F8 帳面價值
						// F9 LineTotal

						// 輸出 x座標, align

						// 戶號 1, L
						// 額度 9, L
						// 姓名 9, L
						// 身分證 21, L

						// (核貸總值類)
						// 不動產抵押 38, R
						// 動產抵押 53, R
						// 有價證券 68, R
						// 銀行保證 83, R
						// 專案放款 98, R
						// 合計 113, R

						// (帳面價值類)
						// 不動產抵押 129, R
						// 動產抵押 144, R
						// 有價證券 159, R
						// 銀行保證 174, R
						// 專案放款 189, R
						// 合計 204, R

						// 最後的合計字樣
						// 以上合計 19, L
						// 以下合計 19, L
						// 總計 19, L

						//

						// 處理: 判斷是否換了一類, 需要畫線? 需要換行?

						thisDataType = DataType.getType(tLDVo.get("F4"));

						if (lastTLDVo != null) {
							lastDataType = DataType.getType(lastTLDVo.get("F4"));

							// 情況1A : 兩種資料類型不同，前一筆是正常資料 - 出上一戶最後的合計（顯示名字）, 然後畫線換行
							// 情況1B : 兩種資料類型不同，前一筆是合計資料 (Sum) - 換行
							// 情況1C : 兩種資料類型不同，這一筆是總計資料 (Total) - 畫線換行

							// 情況2A : 兩種資料類型一樣, 正常資料, 同戶號 - 換行
							// 情況2B : 兩種資料類型一樣, 正常資料, 不同戶號 - 出上一戶/筆最後的合計（顯示名字）, 然後畫線換行
							// 情況2C : 兩種資料類型一樣，合計資料 (Sum) - 無處理
							// 情況2D : 兩種資料類型一樣，合計資料 (Total) - 無處理

							if (lastDataType != thisDataType) {
								// 情況1A : 兩種資料類型不同，前一筆是正常資料 - 出上一戶最後的合計（顯示名字）, 然後畫線換行
								// 情況1B : 兩種資料類型不同，前一筆是合計資料 (Sum) - 換行

								if (lastDataType == DataType.Normal) {
									// 情況1A Only

									this.print(1, 0, " ");

									this.print(0, 0, lastTLDVo.get("F2"), "L"); // 戶號
									this.print(0, 8, StringCut.stringCut(lastTLDVo.get("F5"), 0, 10) + " " + lastTLDVo.get("F4"), "L");
								}

								// 出上一筆資料最後的合計
								this.print(0, 113, formatAmt(lineAmtTotal, 0), "R"); // 核貸總值合計
								this.print(0, 204, formatAmt(bookValueTotal, 0), "R"); // 帳面價值合計

								// 重置

								lineAmtTotal = BigDecimal.ZERO;
								bookValueTotal = BigDecimal.ZERO;

								if (lastDataType != DataType.Above_Sum) // 偷吃步，如果因為Above_Sum一定在Below_Sum之前，這裡這樣讓合計區不多畫線
								{
									this.print(1, 0, newBorder);
								} else if (thisDataType == DataType.Total) {
									// 情況1C : 兩種資料類型不同，這一筆是總計資料 (Total) - 畫線換行
									// 合計區畫線
									this.print(1, 0, newBorder);
								}

								this.print(1, 0, " ");

							}

							// 這裡開始都是兩筆DataType相同為前提

							else if (thisDataType == DataType.Normal && !tLDVo.get("F2").equals(lastTLDVo.get("F2"))) {
								// 情況2B : 兩種資料類型一樣, 正常資料, 不同戶號 - 出上一戶/筆最後的合計（顯示名字）, 然後畫線換行

								this.print(1, 0, " ");

								this.print(0, 0, lastTLDVo.get("F2"), "L"); // 戶號
								this.print(0, 8, StringCut.stringCut(lastTLDVo.get("F5"), 0, 10) + " " + lastTLDVo.get("F4"), "L");

								// 出上一筆資料最後的合計
								this.print(0, 113, formatAmt(lineAmtTotal, 0), "R"); // 核貸總值合計
								this.print(0, 204, formatAmt(bookValueTotal, 0), "R"); // 帳面價值合計

								// 重置

								lineAmtTotal = BigDecimal.ZERO;
								bookValueTotal = BigDecimal.ZERO;

								this.print(1, 0, newBorder);
								this.print(1, 0, " ");

							} else if (thisDataType == DataType.Normal) {
								// 情況2A : 兩種資料類型一樣, 正常資料, 同戶號 - 換行
								this.print(1, 0, " ");
							}
						}

						// 本行開頭

						this.print(0, 0, tLDVo.get("F2"), "L"); // 戶號
						this.print(0, 8, tLDVo.get("F3"), "L"); // 額度

						// 僅正常資料的時候出身分證,
						// 其他情況出姓名(表示合計/總計)

						if (thisDataType == DataType.Normal) {
							this.print(0, 15, tLDVo.get("F4"), "L"); // 身分證
						} else {
							// 姓名 - Sum與Total類資料的此欄為"以上合計/以下合計/總計"
							// 只有在前一筆資料與這一筆資料種類不同時, 才出字樣
							// 避免多次重覆輸出疊字

							if (lastTLDVo != null && lastDataType != thisDataType) {
								this.print(0, 15, tLDVo.get("F5"), "L");
							}
						}

						// 計算合計用
						lineAmtTotal = lineAmtTotal.add(getBigDecimal(tLDVo.get("F7")));
						bookValueTotal = bookValueTotal.add(getBigDecimal(tLDVo.get("F8")));

						// 處理: 輸出款項數目

						// 依據款項項目指定X座標
						switch (tLDVo.get("F6")) {
						case "0":
							// 專案放款
							this.print(0, 98, formatAmt(tLDVo.get("F7"), 0), "R");
							this.print(0, 189, formatAmt(tLDVo.get("F8"), 0), "R");
							break;
						case "2":
							// 不動產抵押
							this.print(0, 40, formatAmt(tLDVo.get("F7"), 0), "R");
							this.print(0, 129, formatAmt(tLDVo.get("F8"), 0), "R");
							break;
						case "4":
							// 有價證券
							this.print(0, 68, formatAmt(tLDVo.get("F7"), 0), "R");
							this.print(0, 159, formatAmt(tLDVo.get("F8"), 0), "R");
							break;
						case "5":
							// 銀行保證
							this.print(0, 83, formatAmt(tLDVo.get("F7"), 0), "R");
							this.print(0, 174, formatAmt(tLDVo.get("F8"), 0), "R");
							break;
						case "9":
							// 動產抵押
							this.print(0, 53, formatAmt(tLDVo.get("F7"), 0), "R");
							this.print(0, 144, formatAmt(tLDVo.get("F8"), 0), "R");
							break;
						default:
							// 其餘情況, 不予輸出
							break;
						}

						// 結束此行

						lastTLDVo = tLDVo;

					}

					// 出最後總計的子項合計, 並畫最後的線

					this.print(0, 113, formatAmt(lineAmtTotal, 0), "R"); // 核貸總值合計
					this.print(0, 204, formatAmt(bookValueTotal, 0), "R"); // 帳面價值合計

					this.print(1, 0, newBorder);

				} else {
					// 處理: 無資料時

					this.print(1, 0, "本月無資料!");
				}

			}
		}
		this.close();
		// this.toPdf(sno);

		return true;
	}
}
