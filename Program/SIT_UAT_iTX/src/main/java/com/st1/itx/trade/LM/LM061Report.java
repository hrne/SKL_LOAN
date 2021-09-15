package com.st1.itx.trade.LM;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LM061ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;

@Component
@Scope("prototype")
public class LM061Report extends MakeReport {

	@Autowired
	LM061ServiceImpl lM061ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	public void exec(TitaVo titaVo) throws LogicException {

		List<Map<String, String>> fnAllList = new ArrayList<>();

		this.info("LM061Report exec");

		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM061", "逾清償期二年案件追蹤控管表", "LM061_逾清償期二年案件追蹤控管表",
				"LM061-逾清償期二年案件追蹤控管表.xlsx", "1080430");

		String iENTDY = titaVo.get("ENTDY");

		makeExcel.setSheet("1080430", iENTDY.substring(1, 8));

		makeExcel.setValue(1, 23, "機密等級：機密\n單位：元\n" + iENTDY.substring(1, 4) + "." + iENTDY.substring(4, 6) + "."
				+ iENTDY.substring(6, 8) + "止");

		try {
			fnAllList = lM061ServiceImpl.findAll(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("LM061ServiceImpl.findAll error = " + errors.toString());
		}

		if (fnAllList.size() > 0) {
			String fdnm = "";

			String tempCustNo = "";

			String tempCustNo2 = "";

			// 暫存 轉催收金額
			BigDecimal tempOvduBal = BigDecimal.ZERO;

			// 鑑價金額
			BigDecimal F11 = BigDecimal.ZERO;

			// 暫存 鑑價金額
			BigDecimal tempF11 = BigDecimal.ZERO;

			// 同戶號多額度 筆數
			int tempCount = 0;

			// 從第三列開始塞值
			int row = 3;

			// 序列
			int num = 0;

			// 筆數
			int count = 0;

			for (Map<String, String> tLDVo : fnAllList) {

				// 筆數
				count++;

				// 第二筆資料起,先將下一列以下的資料向下搬移一列
				if (row > 3) {
					makeExcel.setShiftRow(row, 1);
				}

				// 上一筆與這一筆戶號不同 序列加一
				if (!tempCustNo.equals(tLDVo.get("F0"))) {
					num++;
				}

				// 暫存戶號
				tempCustNo = tLDVo.get("F0");

				// 欄位：序列
				makeExcel.setValue(row, 2, num);

				// 轉催收金額
				BigDecimal ovduBal = tLDVo.get("F3") == null || tLDVo.get("F3").isEmpty() ? BigDecimal.ZERO
						: new BigDecimal(tLDVo.get("F3"));

				this.info("tLDVo:" + tLDVo);

				for (int i = 0; i < tLDVo.size(); i++) {
					fdnm = "F" + i;

					switch (i) {
					case 0:// 戶號
					case 1:// 額度
						makeExcel.setValue(row, i + 3,
								tLDVo.get(fdnm).isEmpty() ? BigDecimal.ZERO : new BigDecimal(tLDVo.get(fdnm)), "R");
						break;
					case 2:// 戶名
						makeExcel.setValue(row, i + 3, tLDVo.get(fdnm), "R");
						break;

					case 3:// 核貸金額
					case 4:// 轉催收本息
					case 5:// 催收款餘額
						makeExcel.setValue(row, i + 3,
								tLDVo.get(fdnm).isEmpty() ? BigDecimal.ZERO : new BigDecimal(tLDVo.get(fdnm)),
								"$* #,##0", "R");
						break;

					case 6:// 繳息迄日
						makeExcel.setValue(row, i + 3,
								tLDVo.get(fdnm).isEmpty() ? BigDecimal.ZERO : new BigDecimal(tLDVo.get(fdnm)), "R");
						break;

					case 7: // 利率
						makeExcel.setValue(row, i + 3,
								tLDVo.get(fdnm).isEmpty() ? BigDecimal.ZERO : new BigDecimal(tLDVo.get(fdnm)), "0.0000",
								"R");
						break;

					case 8:// 到期日
					case 9:// 轉催收日
						makeExcel.setValue(row, i + 3,
								tLDVo.get(fdnm).isEmpty() ? BigDecimal.ZERO : new BigDecimal(tLDVo.get(fdnm)), "R");
						break;

					case 13:// 轉呆金額
						makeExcel.setValue(row, 17,
								tLDVo.get(fdnm).isEmpty() ? BigDecimal.ZERO : new BigDecimal(tLDVo.get(fdnm)),
								"$* #,##0", "R");
						break;

					case 14:// 擔保品坐落
					case 15:// 符合規範
					case 16:// 催收人員
						makeExcel.setValue(row, i + 6, tLDVo.get(fdnm), "R");
						break;

					default:

						break;

					}

					// 法務進度(F10)
					makeExcel.setValue(row, 15, tLDVo.get("F10"), "L");

					// 金額(F11)
					// 法務進度代號(F12)

					// 鑑價金額
					F11 = tLDVo.get("F11").isEmpty() ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F11"));

					// 代號 56 拍定金額 58 分配金額
					if (tLDVo.get("F12").equals("056")) {

						// 和上一個戶號不一樣就歸零 並直接附值
						// 和上一個戶號一樣就累加 並 合併儲存格 再賦值
						if (!tempCustNo2.equals(tLDVo.get("F0"))) {

						

							// 上一同戶號多額度跟這筆比較，如果有1筆以上就使用合併
							if (tempCount > 1) {

								// 爛位的列數為多少筆額度減去當前列數
								makeExcel.setValue(row - tempCount, 13, tempF11, "L");

								// 同上方式，同戶號 累計轉催收金額 除以 鑑價金額 (LTV)
								makeExcel.setValue(row - tempCount, 14, this.computeDivide(tempOvduBal, tempF11, 4),
										"##0.0%");
								this.info("2有相同戶號的累積金額"+tempOvduBal+",單筆金額"+ovduBal);
								this.info("戶號：" + tLDVo.get("F0") + ",需合併：從" + (row - tempCount) + "到" + (row - 1));

								// 賦值完後 合併
								makeExcel.setMergedRegion(row - tempCount, row -1, 13, 13);
								// 賦值完後 合併
								makeExcel.setMergedRegion(row - tempCount, row -1, 14, 14);

							}
							// 暫存鑑價金額 歸零
							tempF11 = BigDecimal.ZERO;
							// 賦值鑑價金額
							tempF11 = F11;

							// 暫存轉催收金額 歸零
							tempOvduBal = BigDecimal.ZERO;
							// 累加轉催收金額
							tempOvduBal = tempOvduBal.add(ovduBal);

							// 計數 歸零
							tempCount = 0;
							// 多筆額度計數
							tempCount++;

							// 單筆
							makeExcel.setValue(row, 13,
									tLDVo.get("F11").isEmpty() ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F11")),
									"$* #,##0", "R");

							makeExcel.setValue(row, 14,
									this.computeDivide(ovduBal, new BigDecimal(tLDVo.get("F11")), 4), "##0.0%");

						} else {

							// 紀錄同戶號多額度的筆數
							tempCount++;

							// 累加轉催收金額
							
							tempOvduBal = tempOvduBal.add(ovduBal);
							this.info("1有相同戶號的累積金額"+tempOvduBal+",單筆金額"+ovduBal);

							// BigDecimal的比較大小：-1 小於 0 等於 1 大於
							if (tempF11.compareTo(F11) == -1) {
								tempF11 = F11;
							}

						}
						
						tempCustNo2 = tLDVo.get("F0");

						// 最後一筆時如果是需要合併，且同戶號多額度有1筆以上就使用合併。
						if (count == tLDVo.size() && tempCount > 1) {

							// 爛位的列數為多少筆額度減去當前列數
							makeExcel.setValue(row - tempCount, 13, tempF11, "L");

							// 同上方式，同戶號 累計轉催收金額 除以 鑑價金額 (LTV)
							makeExcel.setValue(row - tempCount, 14, this.computeDivide(tempOvduBal, tempF11, 4),
									"##0.0%");

							// 賦值完後 合併
							makeExcel.setMergedRegion(row - tempCount, row , 13, 13);
							// 賦值完後 合併
							makeExcel.setMergedRegion(row - tempCount, row , 14, 14);

						}

					}

					// 代號 77 協議達成
					if (tLDVo.get("F12").equals("077")) {
						makeExcel.setValue(row, 18, " ", "C");
						makeExcel.setValue(row, 19,
								tLDVo.get("F11").isEmpty() ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F11")),
								"$* #,##0", "R");
					}

					// 代號 901 拍定不足額
					if (tLDVo.get("F12").equals("901")) {
						makeExcel.setValue(row, 16,
								tLDVo.get("F11").isEmpty() ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F11")),
								"$* #,##0", "R");
					}

				}
				row++;
			}
		} else {
			makeExcel.setValue(3, 3, "本日無資料");
		}

		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
	}
}
