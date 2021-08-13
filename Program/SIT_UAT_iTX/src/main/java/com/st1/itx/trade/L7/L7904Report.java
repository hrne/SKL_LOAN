package com.st1.itx.trade.L7;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.L7904ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.MakeFile;

@Component("L7904Report")
@Scope("prototype")

public class L7904Report extends MakeReport {
	private static final Logger logger = LoggerFactory.getLogger(L7904Report.class);

	@Autowired
	public L7904ServiceImpl L7904ServiceImpl;

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
		String iPage = titaVo.getParam("Page").trim();

		// 設定資料庫(必須的)
		try {
			List<HashMap<String, String>> L7904List = L7904ServiceImpl.findAll();
			if (L7904List.size() > 0) {
				// genFile(titaVo, 0, L7904List);
				genExcel(titaVo, 0, L7904List);
			}
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("L7904ServiceImpl.findAll error = " + errors.toString());
		}
	}

	private void genExcel(TitaVo titaVo, int fg, List<HashMap<String, String>> L7List) throws LogicException {
		this.info("=========== L7904 genExcel");
		// 自訂標題 inf
		String inf = "戶號;借款人ID/統編;協議編號;協議前後;額度編號;撥款序號";
		String txt = "F0;F1;F2;F3;F4;F5";

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

			makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L7904", "IFRS9 資料欄位清單6", "IFRS9_06");

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
			this.info("L7904ServiceImpl.genExcel error = " + errors.toString());
		}
	}

	private void genFile(TitaVo titaVo, int fg, List<HashMap<String, String>> L7List) throws LogicException {
		this.info("=========== L7904 genFile");
		String txt = "F0;F1;F2;F3;F4;F5";
		String txt1[] = txt.split(";");

		try {
			String strContent = "";

			makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), titaVo.getTxCode(), "IFRS9 資料欄位清單6", "IFRS9_06.csv", 2);

			// 標題列
			strContent = "戶號(1~7),借款人ID/統編(7~16),協議編號(17~19),協議前後(20~20),額度編號(21~23),撥款序號(24~26)";
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
						strField = makeFile.fillStringL(strField, 7, '0');
					} // 戶號
					if (j == 2) {
						strField = makeFile.fillStringR(strField, 10, ' ');
					} // 借款人ID/統編
					if (j == 3) {
						strField = makeFile.fillStringL(strField, 3, '0');
					} // 協議編號
					if (j == 4) {
						strField = makeFile.fillStringR(strField, 1, ' ');
					} // 協議前後
					if (j == 5) {
						strField = makeFile.fillStringL(strField, 3, '0');
					} // 額度編號
					if (j == 6) {
						strField = makeFile.fillStringL(strField, 3, '0');
					} // 撥款序號

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
			this.info("L7904ServiceImpl.genFile error = " + errors.toString());
		}
	}
}
