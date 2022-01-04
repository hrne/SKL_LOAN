package com.st1.itx.trade.LD;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.CdWorkMonthService;
import com.st1.itx.db.service.springjpa.cm.LD006ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.format.StringCut;
import com.st1.itx.util.parse.Parse;

@Component
@Scope("prototype")

public class LD006Report extends MakeReport {

	/* DB服務注入 */
	@Autowired
	CdWorkMonthService sCdWorkMonthService;

	@Autowired
	LD006ServiceImpl lD006ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	@Autowired
	Parse parse;

	public Boolean exec(TitaVo titaVo) throws LogicException {

		List<Map<String, String>> lD006List = null;

		try {
			lD006List = lD006ServiceImpl.findAll(titaVo);
		} catch (Exception e) {
			this.error("lD006ServiceImpl.findAll error = " + e.getMessage());
			e.printStackTrace();
		}

		exportExcel(titaVo, lD006List);

		return true;
	}

	private void exportExcel(TitaVo titaVo, List<Map<String, String>> lD006List) throws LogicException {
		this.info("exportExcel ... ");

		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LD006", "三階放款明細統計", "LD006三階放款明細統計", "LD006三階放款明細統計.xls", "三階放款明細統計");

		// 有標題列，從第二列開始塞值
		int row = 2;
		BigDecimal total = BigDecimal.ZERO;
		if (lD006List != null && !lD006List.isEmpty()) {
			for (Map<String, String> tLDVo : lD006List) {

				// query欄位數有變時, 這裡也須修改
				for (int i = 0; i < 24; i++) {

					// 查詢結果第一個欄位為F0
					String tmpValue = tLDVo.get("F" + i);

					// 寫入Excel時，A欄為1
					int col = i + 1;

					switch (i) {
					case 2: // 戶名
						makeExcel.setValue(row, col, StringCut.stringMask(tmpValue));
						break;
					case 10: // K欄:撥款金額
						total = total.add(getBigDecimal(tmpValue));
						makeExcel.setValue(row, col, parse.isNumeric(tmpValue) ? getBigDecimal(tmpValue) : tmpValue, "#,##0");
						break;
					case 11: // L, M, N欄: 部室/區部/單位代號
					case 12:
					case 13:
						makeExcel.setValue(row, col, parse.isNumeric(tmpValue) ? getBigDecimal(tmpValue) : tmpValue, "L");
						break;
					case 17: // 員工代號
						makeExcel.setValue(row, col, tmpValue);
						break;
					case 21: // V欄:換算業績
					case 22: // W欄:業務報酬
					case 23: // X欄:業績金額
						makeExcel.setValue(row, col, getBigDecimal(tmpValue), "#,##0");
						break;
					default:
						makeExcel.setValue(row, col, parse.isNumeric(tmpValue) ? getBigDecimal(tmpValue) : tmpValue);
						break;
					}
				} // for

				row++;
			} // for
			makeExcel.setValue(row, 11, total);
		} else {
			this.info("LD006Report exportExcel ... 本日無資料");
			makeExcel.setValue(row, 1, "本日無資料");
		}
		long sno = makeExcel.close();
		// makeExcel.toExcel(sno);
	}

}
