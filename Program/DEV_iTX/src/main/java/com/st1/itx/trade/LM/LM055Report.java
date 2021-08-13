package com.st1.itx.trade.LM;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LM055ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;

@Service
@Scope("prototype")
/**
 * 
 * 
 * @author Eric Chang
 * @version 1.0.0
 */
public class LM055Report extends MakeReport {
	// private static final Logger logger = LoggerFactory.getLogger(LM055Report.class);

	@Autowired
	LM055ServiceImpl LM055ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	@Override
	public void printTitle() {

	}

	String fdnm = "";
	int row = 1;
	BigDecimal tot = new BigDecimal("0");

	public void exec(TitaVo titaVo) throws LogicException {

		List<Map<String, String>> fnAllList = new ArrayList<>();

		this.info("LM055Report exec");

		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM055", "A042放款餘額彙總表_工作表)", "LM055-A042放款餘額彙總表_工作表", "LM055-A042放款餘額彙總表_工作表.xlsx", "LNM34AP");
		try {
			fnAllList = LM055ServiceImpl.findAll(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LM055ServiceImpl.findAll error = " + errors.toString());
		}

		if (fnAllList.size() > 0) {
			tot = new BigDecimal("0");
			row = 1;
			for (Map<String, String> tLDVo : fnAllList) {
				exportExcel(tLDVo);
			}
			makeExcel.setValue(1, 14, tot, "#,##0");
		}else {
			makeExcel.setValue(2, 1, "本日無資料");
		}
		
		int yearMonth = (Integer.valueOf(titaVo.get("ENTDY")) + 19110000) / 100;
		makeExcel.setSheet("A042放款餘額彙總表)");
		makeExcel.setValue(2, 3, yearMonth);

		long sno = makeExcel.close();
		makeExcel.toExcel(sno);

	}

	private void exportExcel(Map<String, String> tLDVo) throws LogicException {
		row++;
		makeExcel.setValue(row, 1, tLDVo.get("F0") + tLDVo.get("F2"));

		for (int i = 0; i < tLDVo.size(); i++) {
			fdnm = "F" + String.valueOf(i);
			switch (i) {
			case 1:
			case 18:
			case 19:
			case 20:
			case 23:
			case 24:
			case 25:
			case 26:
			case 28:
			case 29:
				// 字串左靠
				makeExcel.setValue(row, i + 2, tLDVo.get(fdnm));
				break;
			case 11:
			case 12:
			case 13:
			case 14:
				// 金額
				if (tLDVo.get(fdnm).equals("")) {
					makeExcel.setValue(row, i + 2, 0, "#,##0");
				} else {
					makeExcel.setValue(row, i + 2, Float.valueOf(tLDVo.get(fdnm)), "#,##0");
					if (i == 12) {
						tot = tot.add(tLDVo.get(fdnm) == null ? BigDecimal.ZERO : new BigDecimal(tLDVo.get(fdnm)));
					}
				}
				break;
			case 15:
			case 21:
				// 利率
				if (tLDVo.get(fdnm).equals("")) {
					makeExcel.setValue(row, i + 2, 0);
				} else if(tLDVo.get(fdnm).equals(" ")) {
					makeExcel.setValue(row, i + 2, 0);
				} else {
					makeExcel.setValue(row, i + 2, tLDVo.get(fdnm) == null ? "" : Float.valueOf(tLDVo.get(fdnm)));
				}
				break;
			default:
				// 戶號(數字右靠)
				if (tLDVo.get(fdnm).equals("")) {
					makeExcel.setValue(row, i + 2, 0);
				} else if(tLDVo.get(fdnm).equals(" ")) {
					makeExcel.setValue(row, i + 2, " ");
				} else {
					makeExcel.setValue(row, i + 2, tLDVo.get(fdnm));
				}
				
				break;
			}
		}
	}

}