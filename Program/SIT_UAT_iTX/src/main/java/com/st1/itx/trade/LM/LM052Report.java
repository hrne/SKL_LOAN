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
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LM052ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;

@Service
@Scope("prototype")

public class LM052Report extends MakeReport {
	private static final Logger logger = LoggerFactory.getLogger(LM052Report.class);

	@Autowired
	LM052ServiceImpl LM052ServiceImpl;
 
	@Autowired
	MakeExcel makeExcel;

	@Override
	public void printTitle() {

	}

	public void exec(TitaVo titaVo) throws LogicException {

		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM052", "放款資產分類案件明細表_呆提存比率15_內部控管",
				"LM052_放款資產分類案件明細表_呆提存比率1.5%_內部控管", "LM052放款資產分類案件明細表_呆提存比率15_內部控管.xlsx", "la$w30p");
		
		List<Map<String, String>> LM052List = null;
		
		try {
			LM052List = LM052ServiceImpl.findAll(titaVo);


		} catch (Exception e) {
			// TODO Auto-generated catch block
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LM052ServiceImpl.findAll error = " + errors.toString());
		}

		
		exportExcel(LM052List);
		
		

		List<Map<String, String>> LM052List_1 = null;
		try {
			LM052List_1 = LM052ServiceImpl.findAll_1(titaVo);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LM052ServiceImpl.findAll_1 error = " + errors.toString());
		}
		makeExcel.setSheet("LNM34AP");
		
		exportExcel_1(LM052List_1);
		
		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
	}

	private void exportExcel(List<Map<String, String>> LDList) throws LogicException {
		this.info("LM052Report exportExcel");
		


		int row = 1;
		String ad = "";
		BigDecimal tot = new BigDecimal("0");
		if (LDList.size() > 0) {
	
		for (Map<String, String> tLDVo : LDList) {
			row++;
			makeExcel.setValue(row, 1, Float.valueOf(tLDVo.get("F0") + tLDVo.get("F1") + tLDVo.get("F2")));
			for (int i = 0; i < tLDVo.size(); i++) {

				ad = "F" + String.valueOf(i);
				switch (i) {
				case 0:
				case 1:
				case 2:
				case 6:
				case 7:
				case 8:
				case 11:
				case 13:
					// 戶號(數字右靠)
					if (tLDVo.get(ad).trim().equals("")) {
						makeExcel.setValue(row, i + 2, 0);
					} else {
//						makeExcel.setValue(row, i + 2, Integer.valueOf(tLDVo.get(ad)));
						makeExcel.setValue(row, i + 2, tLDVo.get(ad));
					}
					break;
				case 10:
					// 金額
					if (tLDVo.get(ad).equals("")) {
						makeExcel.setValue(row, i + 2, 0, "#,##0");
					} else {
						makeExcel.setValue(row, i + 2, Float.valueOf(tLDVo.get(ad)), "#,##0");
						tot = tot.add(new BigDecimal((tLDVo.get(ad))));
					}
					break;
				default:
					makeExcel.setValue(row, i + 2, tLDVo.get(ad));
					break;
				}
			}
		}
		}else {
			makeExcel.setValue(2, 1, "本日無資料");
	
		}
		makeExcel.setValue(1, 12, tot.floatValue(), "#,##0");
	}

	private void exportExcel_1(List<Map<String, String>> LDList) throws LogicException {
		this.info("LM052Report exportExcel_1");
		if (LDList.size() == 0) {
			makeExcel.setValue(2, 1, "本日無資料");
		}
		int row = 1;
		String ad = "";

		for (Map<String, String> tLDVo : LDList) {
			row++;
			makeExcel.setValue(row, 1, Float.valueOf(tLDVo.get("F0") + tLDVo.get("F2") + tLDVo.get("F4")));
			for (int i = 0; i < tLDVo.size(); i++) {

				ad = "F" + String.valueOf(i);
				switch (i) {
				case 0:
				case 2:
				case 3:
				case 4:
				case 5:
					// 戶號(數字右靠)
					if (tLDVo.get(ad).equals("")) {
						makeExcel.setValue(row, i + 2, 0);
					} else {
						makeExcel.setValue(row, i + 2, Integer.valueOf(tLDVo.get(ad)));
					}
					break;
				default:
					makeExcel.setValue(row, i + 2, tLDVo.get(ad));
					break;
				}
			}
		}
	}

}