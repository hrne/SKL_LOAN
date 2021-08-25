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
			// 從第三列開始塞值
			int row = 3;
			int num = 0;

			for (Map<String, String> tLDVo : fnAllList) {

				// 第二筆資料起,先將下一列以下的資料向下搬移一列
				if (row > 3) {
					makeExcel.setShiftRow(row, 1);
				}

//				makeExcel.setValue(row, 1, tLDVo.get("F19"));
			
				if (!tempCustNo.equals(tLDVo.get("F0"))) {

					num++;

				}
				tempCustNo = tLDVo.get("F0");
				
				makeExcel.setValue(row, 2, num);

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
					if (tLDVo.get("F12").equals("056") || tLDVo.get("F12").equals("058")) {
						makeExcel.setValue(row, 13,
								tLDVo.get("F11").isEmpty() ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F11")),
								"$* #,##0", "R");

						makeExcel.setValue(row, 14, this.computeDivide(ovduBal,  new BigDecimal(tLDVo.get("F11")), 4), "##0.0%");
					}

					if (tLDVo.get("F12").equals("077")) {
						makeExcel.setValue(row, 18, "V", "C");
						makeExcel.setValue(row, 19,
								tLDVo.get("F11").isEmpty() ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F11")),
								"$* #,##0", "R");
					}

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
