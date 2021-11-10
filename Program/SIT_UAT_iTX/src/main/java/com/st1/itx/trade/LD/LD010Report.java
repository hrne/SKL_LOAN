package com.st1.itx.trade.LD;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LD010ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;

@Component
@Scope("prototype")

public class LD010Report extends MakeReport {

	@Autowired
	LD010ServiceImpl lD010ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	public Boolean exec(TitaVo titaVo) throws LogicException {

		this.info("LD010Report exec");

		List<Map<String, String>> lD010List = null;

		try {
			lD010List = lD010ServiceImpl.findAll(titaVo);

		} catch (Exception e) {
			this.error("lD010ServiceImpl.findAll error = " + e.getMessage());
			e.printStackTrace();
		}

		exportExcel(titaVo, lD010List);

		return true;
	}

	private void exportExcel(TitaVo titaVo, List<Map<String, String>> dataList) throws LogicException {
		this.info("exportExcel ... ");

		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LD010", "介紹人換算業績報酬檢核表", "LD010介紹人換算業績報酬檢核表",
				"LD010_底稿_介紹人換算業績報酬檢核表.xlsx", "介紹人換算業績報酬檢核表");

		// 有標題列，從第二列開始塞值
		int row = 2;

		if (dataList != null && !dataList.isEmpty()) {
			for (Map<String, String> data : dataList) {

				// query欄位數有變時, 這裡也須修改
				for (int i = 0; i < 16; i++) {

					// 查詢結果第一個欄位為F0
					String tmpValue = data.get("F" + i);

					// 寫入Excel時，A欄為1
					int col = i + 1;

					switch (i) {
					case 5: // F5 = F欄 = 撥款金額
					case 12: // F12 = M欄 = 三階換算業績
					case 13: // F13 = B欄 = 三階業務報酬
						makeExcel.setValue(row, col, getBigDecimal(tmpValue), "#,##0");
						break;
					default:
						makeExcel.setValue(row, col, tmpValue);
						break;
					}
				} // for

				row++;
			} // for
		} else {
			this.info("LD010Report exportExcel ... 本日無資料");
			makeExcel.setValue(row, 1, "本日無資料");
		}
		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
	}

}
