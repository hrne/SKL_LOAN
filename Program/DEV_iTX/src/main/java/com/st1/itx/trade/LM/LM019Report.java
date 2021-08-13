package com.st1.itx.trade.LM;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LM019ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;

@Component
@Scope("prototype")

public class LM019Report extends MakeReport {
	// private static final Logger logger = LoggerFactory.getLogger(LM019Report.class);

	@Autowired
	LM019ServiceImpl lM019ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	@Override
	public void printTitle() {

	}

	public void exec(TitaVo titaVo) throws LogicException {
		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM019", "利息收入明細表(印花稅)", "LM019-利息收入明細表(印花稅)", "LM019印花稅.xlsx", "10912明細");
		String entdy = String.valueOf(Integer.valueOf(titaVo.get("ENTDY").toString()) / 100);
		makeExcel.setSheet("10912明細", entdy + "明細");
		List<Map<String, String>> LM019List = null;
		try {
			LM019List = lM019ServiceImpl.findAll(titaVo);
			exportExcel(titaVo, LM019List);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LM019ServiceImpl.testExcel error = " + errors.toString());
		}
		makeExcel.setSheet("10912(250以上)");
		makeExcel.setSheet("10912(250以上)", entdy + "(250以上)");
		List<Map<String, String>> LM019List_1 = null;
		try {
			LM019List_1 = lM019ServiceImpl.findAll_1(titaVo);
			exportExcel_1(titaVo, LM019List_1);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LM019ServiceImpl.testExcel error = " + errors.toString());
		}
		makeExcel.setSheet("10912放款部");
		makeExcel.setSheet("10912放款部", entdy + "放款部");
		List<Map<String, String>> LM019List_2 = null;
		try {
			LM019List_2 = lM019ServiceImpl.findAll_2(titaVo);
			exportExcel_2(titaVo, LM019List_2);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LM019ServiceImpl.testExcel error = " + errors.toString());
		}
		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
	}
	private void exportExcel(TitaVo titaVo, List<Map<String, String>> LDList) throws LogicException {
		if(LDList.size() == 0) {
			makeExcel.setValue(2, 1, "本日無資料");
		} else {
			int row = 2;
			for (Map<String, String> tLDVo : LDList) {
				String ad = "";
				String acnacc = "";
				for (int i = 0; i < tLDVo.size(); i++) {
					ad = "F" + String.valueOf(i);
					switch(i) {
					case 0:
						acnacc = tLDVo.get(ad);
						break;
					case 1:
						acnacc += tLDVo.get(ad);
						makeExcel.setValue(row, i, acnacc);
						break;
					default:
						makeExcel.setValue(row, i, new BigDecimal(tLDVo.get(ad)));
						break;
					}
				}
				row++;
			}
		}
	}
	private void exportExcel_1(TitaVo titaVo, List<Map<String, String>> LDList) throws LogicException {
		if(LDList.size() == 0) {
			makeExcel.setValue(2, 1, "本日無資料");
		} else {
			int row = 2;
			for (Map<String, String> tLDVo : LDList) {
				String ad = "";
				String acnacc = "";
				for (int i = 0; i < tLDVo.size(); i++) {
					ad = "F" + String.valueOf(i);
					switch(i) {
					case 0:
						acnacc = tLDVo.get(ad);
						break;
					case 1:
						acnacc += tLDVo.get(ad);
						makeExcel.setValue(row, i, acnacc);
						break;
					case 4:
						makeExcel.setValue(row, i, new BigDecimal(tLDVo.get(ad)), "#,##0.00");
						break;
					default:
						makeExcel.setValue(row, i, new BigDecimal(tLDVo.get(ad)));
						break;
					}
				}
				row++;
			}
		}
	}
	private void exportExcel_2(TitaVo titaVo, List<Map<String, String>> LDList) throws LogicException {
		String entdy = titaVo.get("ENTDY").toString();
//		makeExcel.setColor("RED");
		makeExcel.setValue(2, 1, entdy.substring(1, 4) + "/" + entdy.substring(4, 6) + "/" + "01" + "~" + entdy.substring(1, 4) + "/" + entdy.substring(4, 6) + "/" + entdy.substring(6, 8));
		if(LDList.size() == 0) {
			makeExcel.setValue(5, 1, "本日無資料");
		} else {
			BigDecimal total[] = new BigDecimal[5];
			for(int i = 0 ; i < 5 ; i++) {
				total[i] = BigDecimal.ZERO;
			}
			int row = 5;
			for (Map<String, String> tLDVo : LDList) {
				String ad = "";
				String acnacc = "";
				for (int i = 0; i < tLDVo.size(); i++) {
					ad = "F" + String.valueOf(i);
					switch(i) {
					case 0:
						acnacc = tLDVo.get(ad);
						break;
					case 1:
						acnacc += tLDVo.get(ad);
						makeExcel.setValue(row, i, acnacc);
						break;
					default:
//						if(i == 5 || i == 6) {
//							makeExcel.setColor("RED");
//						}
						makeExcel.setValue(row, i, new BigDecimal(tLDVo.get(ad)), "#,##0");
						total[i - 2] = total[i - 2].add(new BigDecimal(tLDVo.get(ad)));
						break;
					}
				}
				row++;
			}
			for(int i = 0 ; i < 5 ; i++) {
//				if(i == 3 || i == 4) {
//					makeExcel.setColor("RED");
//				}
				makeExcel.setValue(13, i + 2, total[i], "#,##0");
			}
		}
	}
}
