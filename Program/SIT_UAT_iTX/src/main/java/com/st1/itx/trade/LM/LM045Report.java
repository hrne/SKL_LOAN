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
import com.st1.itx.db.service.springjpa.cm.LM045ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;

@Component
@Scope("prototype")

public class LM045Report extends MakeReport {
	private static final Logger logger = LoggerFactory.getLogger(LM045Report.class);

	@Autowired
	LM045ServiceImpl lM045ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	@Override
	public void printTitle() {

	}

	BigDecimal toverbal = new BigDecimal("0");
	BigDecimal tbadbal = new BigDecimal("0");
	BigDecimal totbal = new BigDecimal("0");
	String tmp = "";
	String yymm = "";
	String lF0 = "";
	int row = 4;
	int startRow = 5;
	int nameCol = 2;
	int nameRow = 2;
	int totalRow = 19;
	public void exec(TitaVo titaVo) throws LogicException {

		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM045", "年度催收逾放總額明細表_內部控管", "LM045_年度催收逾放總額明細表_內部控管", "LM045年度催收逾放總額明細表_內部控管.xlsx", "108年逾放工作表");

		yymm = titaVo.get("ENTDY").substring(1, 6);
		tmp = String.valueOf(Integer.valueOf(titaVo.get("ENTDY")) - 10000);
		makeExcel.setSize(36);
		makeExcel.setValue(1, 1, yymm.substring(0, 3) + "年度催收逾放總額明細表");
		makeExcel.setSheet("108年逾放工作表", yymm.substring(0, 3) + "年逾放總表");
		tmp = "與" + tmp.substring(0, 3) + "年底相較";

		
		
		List<Map<String, String>> LM045List = null;
		try {
			LM045List = lM045ServiceImpl.findAll(titaVo);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LM045ServiceImpl.testExcel error = " + errors.toString());
		}
		exportExcel(LM045List);
		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
	}

	private void exportExcel(List<Map<String, String>> LDList) throws LogicException {
		this.info("LM045Report exportExcel yymm=" + yymm); // yyymm
		String ad = "";
		int ym = 0;
		int fg = 0;
		BigDecimal[] total = new BigDecimal[13];//用來計算最右邊的，第一欄合計為各月逾放總額加總，第二欄月報猜是去年年底逾放總額，第三欄為第二欄減第一欄
		for(int i = 0 ; i < 13 ; i++) {
			total[i] = BigDecimal.ZERO;
		}
		if (LDList.size() == 0) {
			makeExcel.setValue(2, 1, "本日無資料");
		}
		int count = 0;
		for (Map<String, String> tLDVo : LDList) {
			row++;
			
			// F0 is edited by a later editor.
			// Originally it's only e.fullname, but count will go above 12 if at least two collectors fullname are null.
			// I added NVL() so it gives AccCollPsn in that case.
			// If both fullname and AccCollPsn are null... I think it's better if we skip that input?
			
			// this method will fail under supposedly rare situations,
			// such as when a collector has two data input on one single month.
			// in that case, i guess that's the input-interface's problem?
			
			if (tLDVo.get("F0") != null)
			{
				if (!tLDVo.get("F0").equals(lF0)) {
					count = 0;
					if (row > 5) {
						totExcel();
						nameCol += 3;
						if(nameCol > 14) {
							nameCol = 2;
							nameRow = 20;
							startRow = 23;
							totalRow = 37;
						}
						row = startRow;
					}
					toverbal =tLDVo.get("F2") == null ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F2"));
					tbadbal = tLDVo.get("F3") == null ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F3"));
					totbal = tLDVo.get("F4") == null ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F4"));
				}
	
				ym = Integer.valueOf(tLDVo.get("F1")) - 191100;
				if (ym < Integer.valueOf(yymm)) {
					fg = 1;
				} else if (ym == Integer.valueOf(yymm)) {
					fg = 2;
				} else {
					fg = 0;
				}
	
				for (int i = 0; i < tLDVo.size(); i++) {
					ad = "F" + String.valueOf(i);
					switch (i) {
					case 0:
						makeExcel.setSize(30);
						makeExcel.setValue(nameRow, nameCol, tLDVo.get(ad));
						break;
					case 1:
						makeExcel.setSize(20);
						makeExcel.setValue(row, 1, String.valueOf(ym));
						break;
					default:
						// 金額
						if (fg > 0) {
							makeExcel.setSize(18);
							if (tLDVo.get(ad).equals("")) {
								makeExcel.setValue(row, nameCol + i - 2, 0, "#,##0");
							} else {
								makeExcel.setValue(row, nameCol + i - 2, new BigDecimal(tLDVo.get(ad)), "#,##0");
							}
						}
						if(i == 4) {
							total[count] = total[count].add(new BigDecimal(tLDVo.get(ad)) ==null ? BigDecimal.ZERO : new BigDecimal(tLDVo.get(ad)));
							count++;
						}
						break;
					}
				}
				lF0 = tLDVo.get("F0");
				if (fg == 2) {
					toverbal = tLDVo.get("F2") == null ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F2")).subtract(toverbal);
					tbadbal = tLDVo.get("F3") == null ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F3")).subtract(tbadbal);
					totbal = tLDVo.get("F4") == null ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F4")).subtract(totbal);
					this.info("LM030Report exportExcel" + ym + ",bal" + toverbal);
				}
		}
			
		totExcel();
		for(int i = 0 ; i < count ; i++) {//最右邊三欄(合計、月報、...)
			makeExcel.setValue(i + 5, 17, total[i], "#,##0");
			makeExcel.setValue(i + 5, 18, total[0], "#,##0");
			makeExcel.setValue(i + 5, 19, total[0].subtract(total[i]), "#,##0");
		}
		
		}
		
	}

	private void totExcel() throws LogicException {
		makeExcel.setSize(15);
		makeExcel.setValue(totalRow, 1, tmp);
		if(toverbal.compareTo(BigDecimal.ZERO) == -1) {
			makeExcel.setColor("red");
		}
		makeExcel.setSize(18);
		makeExcel.setValue(totalRow, nameCol, toverbal, "#,##0", "R");
		if(tbadbal.compareTo(BigDecimal.ZERO) == -1) {
			makeExcel.setColor("red");
		}
		makeExcel.setSize(18);
		makeExcel.setValue(totalRow, nameCol + 1, tbadbal, "#,##0", "R");
		if(totbal.compareTo(BigDecimal.ZERO) == -1) {
			makeExcel.setColor("red");
		}
		makeExcel.setSize(18);
		makeExcel.setValue(totalRow, nameCol + 2, totbal, "#,##0", "R");
	}

}
