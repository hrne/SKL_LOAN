package com.st1.itx.trade.L7;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.L7903ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.MakeFile;

import java.text.DecimalFormat;

@Component("L7903Report")
@Scope("prototype")

public class L7903Report extends MakeReport {
	// private static final Logger logger =
	// LoggerFactory.getLogger(L7903Report.class);

	@Autowired
	public L7903ServiceImpl L7903ServiceImpl;

	@Autowired
	public MakeExcel makeExcel;

	@Autowired
	public MakeFile makeFile;

	// 自訂明細標題
	@Override
	public void printTitle() {
		this.info("printTitle nowRow = " + this.NowRow);

	}

	public void exec(TitaVo titaVo) throws LogicException {
		// 設定資料庫(必須的)
		try {
			List<HashMap<String, String>> L7903List = L7903ServiceImpl.findAll();
			if (L7903List.size() > 0) {
				// genFile(titaVo, 0, L7903List);
				genExcel(titaVo, 0, L7903List);
			}
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("L7903ServiceImpl.findAll error = " + errors.toString());
		}
	}

	private void genExcel(TitaVo titaVo, int fg, List<HashMap<String, String>> L7List) throws LogicException {
		this.info("=========== L7903 genExcel");
		// 自訂標題 inf
		String inf = "財會年月;會計科目代碼;金額;會計帳冊";
		String txt = "F0;F1;F2;F3";

		// 自訂標題列寫入記號
		boolean infFg = true;
		if ((inf.trim()).equals("")) {
			infFg = false;
		}

		String inf1[] = inf.split(";");
		String txt1[] = txt.split(";");

		try {
			// 若有底稿，需自行判斷列印起始行數 i
			// 若無底稿，設定列印起始行數 i=1
			int i = 1;
			this.info("-----------------" + L7List);
			// this.info("----------------- L7List.size()=" + L7List.size());

			makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L7903", "IFRS9 資料欄位清單5", "IFRS9_05");

			// 自訂標題列
			if (infFg == true) {
				for (int j = 1; j <= inf1.length; j++) {
					makeExcel.setValue(i, j, inf1[j - 1]);
				}
				infFg = false;
				i++;
			}

			// 輸出內容
			for (HashMap<String, String> tL7Vo : L7List) {
				for (int j = 1; j <= tL7Vo.size(); j++) {
					if (tL7Vo.get(txt1[j - 1]) == null) {
						makeExcel.setValue(i, j, "");
					} else {
						makeExcel.setValue(i, j, tL7Vo.get(txt1[j - 1]));
					}
				}
				i++;
			}

			long sno = makeExcel.close();
			makeExcel.toExcel(sno);

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("L7903ServiceImpl.genExcel error = " + errors.toString());
		}
	}

	private void genFile(TitaVo titaVo, int fg, List<HashMap<String, String>> L7List) throws LogicException {
		this.info("=========== L7903 genFile");
		String txt = "F0;F1;F2;F3";
		String txt1[] = txt.split(";");

		try {
			String strContent = "";

			makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), titaVo.getTxCode(), "IFRS9 資料欄位清單5", "IFRS9_05.csv", 2);

			// 標題列
			strContent = "財會年月(1~6),會計科目代碼(7~14),金額(15~27),會計帳冊(28~28)";
			makeFile.put(strContent);

			// 欄位內容
			this.info("-----------------" + L7List);
			for (HashMap<String, String> tL7Vo : L7List) {
				strContent = "";
				// this.info("-----------------" + tL7Vo);
				// this.info("--------tL7Vo.size=" + tL7Vo.size());
				for (int j = 1; j <= tL7Vo.size(); j++) {
					// this.info("--------------j=" + j);
					String strField = "";
					if (tL7Vo.get(txt1[j - 1]) == null) {
						strField = "";
					} else {
						strField = tL7Vo.get(txt1[j - 1]).replace(",", "，").trim(); // csv檔: 逗號轉全形
					}

					// 格式處理
					// this.info("--------------strField=" + strField);
					if (j == 1) {
						strField = makeFile.fillStringL(strField, 6, '0');
					} // 財會年月
					if (j == 2) {
						strField = makeFile.fillStringR(strField, 8, ' ');
					} // 會計科目代碼
					if (j == 3) {
						DecimalFormat formatter = new DecimalFormat("0.00");
						strField = formatter.format(Float.parseFloat(strField = (strField.isEmpty() ? "0" : strField)));
						strField = makeFile.fillStringL(strField, 13, '0');
					} // 金額
					if (j == 4) {
						strField = makeFile.fillStringL(strField, 1, '0');
					} // 會計帳冊 (1=一般 2=分紅 3=利變 4=OIU)

					strContent = strContent + strField;
					if (j != tL7Vo.size()) {
						strContent = strContent + ",";
					}
				}
				makeFile.put(strContent);
			}

			long sno = makeFile.close();
			makeFile.toFile(sno);

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("L7903ServiceImpl.genFile error = " + errors.toString());
		}
	}
}
