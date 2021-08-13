package com.st1.itx.trade.LM;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LM040ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;

@Component
@Scope("prototype")

public class LM040Report extends MakeReport {
	private static final Logger logger = LoggerFactory.getLogger(LM040Report.class);

	@Autowired
	LM040ServiceImpl lM040ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	@Override
	public void printTitle() {

	}

	public boolean exec(TitaVo titaVo) throws LogicException {
		List<Map<String, String>> LM040List = null;
		try {
			LM040List = lM040ServiceImpl.findAll(titaVo);
			exportExcel(titaVo, LM040List);
			
			return true;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LM040ServiceImpl.testExcel error = " + errors.toString());
			
			return false;
		}
	}

	private void exportExcel(TitaVo titaVo, List<Map<String, String>> LMList) throws LogicException {
		this.info("LM040Report exportExcel");
		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM040", "地區別正常戶金額", "LM040地區別正常戶金額", "LM040地區別正常戶金額.xlsx", "D960717");
		if (LMList.size() == 0) {
			makeExcel.setValue(3, 1, "本日無資料");
		}
		int row = 3;
		String lastEntCode = "";
		int set0 = 0;
		int set1 = 0;
		BigDecimal firstTotal = BigDecimal.ZERO;
		BigDecimal secondTotal = BigDecimal.ZERO;
		BigDecimal thirdTotal = BigDecimal.ZERO;
		for (Map<String, String> tLDVo : LMList) {
			String ad = "";
			int col = 0;
			for (int i = 0; i < tLDVo.size(); i++) {

				ad = "F" + String.valueOf(col);
				col++;
				switch (col) {
				case 1:
					if(row == 3) {
						lastEntCode = tLDVo.get(ad);
					} else {
						if(!tLDVo.get(ad).equals(lastEntCode)) {
							makeExcel.setValue(row, 1, lastEntCode);
							makeExcel.setValue(row, 2, "0");
							if(lastEntCode.contentEquals("0")) {
								makeExcel.setValue(row, 3, firstTotal, "#,##0");
								set0 = 1;
							} else if(lastEntCode.contentEquals("1")) {
								makeExcel.setValue(row, 3, secondTotal, "#,##0");
								set1 = 1;
							}
							row++;
						}
					}
					lastEntCode = tLDVo.get(ad);
					makeExcel.setValue(row, col, tLDVo.get(ad));
					break;
				case 3:
					// 金額
					makeExcel.setValue(row, col, new BigDecimal(tLDVo.get(ad)), "#,##0", "R");
					if(tLDVo.get("F0").equals("0")) {
						firstTotal = firstTotal.add(new BigDecimal(tLDVo.get(ad)));
					} else if(tLDVo.get("F0").equals("1")) {
						secondTotal = secondTotal.add(new BigDecimal(tLDVo.get(ad)));
					} else if(tLDVo.get("F0").equals("2")) {
						thirdTotal = thirdTotal.add(new BigDecimal(tLDVo.get(ad)));
					}
					break;
				default:
					makeExcel.setValue(row, col, tLDVo.get(ad) == null || tLDVo.get(ad) == " " ? "0" : Integer.parseInt(tLDVo.get(ad)));
					break;
				}
			} // for

			row++;
		} // for
		if(set0 == 0 && firstTotal.compareTo(BigDecimal.ZERO) == 1) {
			makeExcel.setValue(row, 1, "0");
			makeExcel.setValue(row, 2, "0");
			makeExcel.setValue(row, 3, firstTotal, "#,##0");
			row++;
		}
		if(set1 == 0 && secondTotal.compareTo(BigDecimal.ZERO) == 1) {
			makeExcel.setValue(row, 1, "1");
			makeExcel.setValue(row, 2, "0");
			makeExcel.setValue(row, 3, secondTotal, "#,##0");
			row++;
		}
		if(thirdTotal.compareTo(BigDecimal.ZERO) == 1) {
			makeExcel.setValue(row, 1, "2");
			makeExcel.setValue(row, 2, "0");
			makeExcel.setValue(row, 3, thirdTotal, "#,##0");
			row++;
		}
		makeExcel.setValue(row, 2, "0");
		makeExcel.setValue(row, 3, thirdTotal.add(secondTotal.add(firstTotal)), "#,##0");
		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
	}

}
