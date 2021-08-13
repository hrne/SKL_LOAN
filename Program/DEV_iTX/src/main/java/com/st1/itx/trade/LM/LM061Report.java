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
	// private static final Logger logger = LoggerFactory.getLogger(LM061Report.class);

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

			// 從第三列開始塞值
			int row = 3;

			for (Map<String, String> tLDVo : fnAllList) {

				// 第二筆資料起,先將下一列以下的資料向下搬移一列
				if (row > 3) {
					makeExcel.setShiftRow(row , 1);
				}

				makeExcel.setValue(row, 1, tLDVo.get("F19"));
				makeExcel.setValue(row, 2, String.valueOf(row - 2));

				for (int i = 0; i < tLDVo.size(); i++) {
					fdnm = "F" + i;

					String value = tLDVo.get(fdnm);

					switch (i) {
					case 0:
					case 1:
						// 戶號(數字右靠)
						if (value == null || value.isEmpty()) {
							makeExcel.setValue(row, i + 3, 0);
						} else {
							makeExcel.setValue(row, i + 3, Integer.valueOf(tLDVo.get(fdnm)));
						}
						break;
					case 3:
					case 4:
					case 5:
					case 10:
					case 13:
					case 14:
					case 16:
						// 金額
						BigDecimal amt = value == null || value.isEmpty() ? BigDecimal.ZERO : new BigDecimal(value);

						if (amt.compareTo(BigDecimal.ZERO) > 0) {

							makeExcel.setValue(row, i + 3, amt, "$* #,##0");

							if (i == 10 && amt.compareTo(BigDecimal.ZERO) > 0) {

								String xOvduBal = tLDVo.get("F5");

								BigDecimal ovduBal = xOvduBal == null || xOvduBal.isEmpty() ? BigDecimal.ZERO
										: new BigDecimal(xOvduBal);

								makeExcel.setValue(row, 14, this.computeDivide(ovduBal, amt, 4), "##0.0%");
							}
						}
						break;
					case 7:
						// 利率
						BigDecimal rate = value == null || value.isEmpty() ? BigDecimal.ZERO : new BigDecimal(value);

						makeExcel.setValue(row, i + 3, rate, "0.0000");
						break;
					case 11:
					case 18:
					case 20:
						break;
					default:
						// 字串左靠
						makeExcel.setValue(row, i + 3, value);
						break;
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
