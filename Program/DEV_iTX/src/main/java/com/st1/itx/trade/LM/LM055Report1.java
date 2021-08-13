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
public class LM055Report1 extends MakeReport {
	// private static final Logger logger = LoggerFactory.getLogger(LM055Report1.class);

	@Autowired
	LM055ServiceImpl LM055ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	@Override
	public void printTitle() {

	}
	BigDecimal tot = new BigDecimal("0");
	String fdnm = "";
	int row = 1;

	public void exec(TitaVo titaVo) throws LogicException {

		List<Map<String, String>> fnAllList_1 = new ArrayList<>();

		this.info("LM055Report1 exec");

		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM055", "A042放款餘額彙總表_手搞)", "LM055-A042放款餘額彙總表_手搞", "LM055-A042放款餘額彙總表_手搞.xlsx", "LNM34AP");

		try {
			fnAllList_1 = LM055ServiceImpl.findAll_1(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LM055ServiceImpl.findAll_1 error = " + errors.toString());
		}

		if (fnAllList_1.size() > 0) {
			row = 1;
			for (Map<String, String> tLDVo : fnAllList_1) {
				exportExcel_1(tLDVo);
			}
			this.info("tot = " + tot);
			makeExcel.setValue(1, 6, tot, "#,##0");
		}else {
			makeExcel.setValue(2, 1, "本日無資料");
		}

		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
	}

	private void exportExcel_1(Map<String, String> tLDVo) throws LogicException {
		row++;
		makeExcel.setValue(row, 1, tLDVo.get("F0") + tLDVo.get("F2"));
		for (int i = 0; i < tLDVo.size(); i++) {
			fdnm = "F" + String.valueOf(i);
			switch (i) {
			case 0:
			case 2:
			case 3:
			case 5:
			case 6:
				// 戶號(數字右靠)
				if (tLDVo.get(fdnm).equals("")) {
					makeExcel.setValue(row, i + 2, 0);
				} else {
					makeExcel.setValue(row, i + 2, tLDVo.get(fdnm));
				}
				break;
			case 4:
				// 金額
				if (tLDVo.get(fdnm).equals("")) {
					makeExcel.setValue(row, i + 2, BigDecimal.ZERO, "#,##0");
				} else if(tLDVo.get(fdnm).equals(" ")) {
					makeExcel.setValue(row, i + 2, BigDecimal.ZERO, "#,##0");
				} else {
					makeExcel.setValue(row, i + 2, tLDVo.get(fdnm) == null ? BigDecimal.ZERO : new BigDecimal(tLDVo.get(fdnm)), "#,##0");
					tot = tot.add(tLDVo.get(fdnm) == null ? BigDecimal.ZERO : new BigDecimal(tLDVo.get(fdnm)));
				}
				break;
			default:
				// 字串左靠
				makeExcel.setValue(row, i + 2, tLDVo.get(fdnm));
				break;
			}
		}
	}
}