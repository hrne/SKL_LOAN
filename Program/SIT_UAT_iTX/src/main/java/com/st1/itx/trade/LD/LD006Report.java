package com.st1.itx.trade.LD;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CdWorkMonth;
import com.st1.itx.db.service.CdWorkMonthService;
import com.st1.itx.db.service.springjpa.cm.LD006ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;

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

	public Boolean exec(TitaVo titaVo) throws LogicException {

		List<Map<String, String>> lD006List = null;

		// 取得本帳務日
		int entDy = titaVo.getEntDyI() + 19110000;

		// 取得本工作季
		CdWorkMonth tCdWorkMonth = sCdWorkMonthService.findDateFirst(entDy, entDy, titaVo);

		if (tCdWorkMonth == null) {
			this.error("LD006Report tCdWorkMonth is null.");
			// 產空表
			exportExcel(titaVo, null);
			return false;
		}

		int workYear = tCdWorkMonth.getYear();
		int workMonth = tCdWorkMonth.getMonth();

		String workSeason = "" + workYear;
		
		// 工作月是13個月

		switch (workMonth) {
		case 1:
		case 2:
		case 3:
			workSeason += "1";
			break;
		case 4:
		case 5:
		case 6:
			workSeason += "2";
			break;
		case 7:
		case 8:
		case 9:
			workSeason += "3";
			break;
		default:
			workSeason += "4";
			break;
		}

		try {
			lD006List = lD006ServiceImpl.findAll(entDy, workSeason, titaVo);
		} catch (Exception e) {
			this.error("lD006ServiceImpl.findAll error = " + e.getMessage());
			e.printStackTrace();
		}

		exportExcel(titaVo, lD006List);

		return true;
	}

	private void exportExcel(TitaVo titaVo, List<Map<String, String>> lD006List) throws LogicException {
		this.info("exportExcel ... ");

		makeExcel.open(titaVo
				, titaVo.getEntDyI()
				, titaVo.getKinbr()
				, "LD006"
				, "三階放款明細統計"
				, "LD006三階放款明細統計"
				, "LD006三階放款明細統計.xls"
				, "三階放款明細統計");

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
					case 10: // K欄:撥款金額
						total = total.add(getBigDecimal(tmpValue));
					case 21: // V欄:換算業績
					case 22: // W欄:業務報酬
					case 23: // X欄:業績金額
						makeExcel.setValue(row, col, getBigDecimal(tmpValue), "#,##0");
						break;
					default:
						makeExcel.setValue(row, col, tmpValue);
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
		makeExcel.toExcel(sno);
	}

}
