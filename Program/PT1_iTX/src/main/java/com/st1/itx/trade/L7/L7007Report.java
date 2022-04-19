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
import com.st1.itx.db.service.springjpa.cm.L7007ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.MakeFile;

@Component("L7007Report")
@Scope("prototype")

public class L7007Report extends MakeReport {
	// private static final Logger logger =
	// LoggerFactory.getLogger(L7007Report.class);

	@Autowired
	public L7007ServiceImpl L7007ServiceImpl;

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
			List<HashMap<String, String>> L7007List = L7007ServiceImpl.findAll();
			if (L7007List.size() > 0) {
				genFile(titaVo, 0, L7007List);
			}
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("L7007ServiceImpl.findAll error = " + errors.toString());
		}
	}

	private void genFile(TitaVo titaVo, int fg, List<HashMap<String, String>> L7List) throws LogicException {
		this.info("=========== L7007 genFile");
		String txt = "F0;F1;F2;F3;F4;F5";
		String txt1[] = txt.split(";");

		try {
			String strContent = "";

			makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), titaVo.getTxCode(), "IAS39 資料欄位清單G", "LNM34GP.csv", 2);

			// 標題列
			strContent = "戶號(1~7),借款人ID/統編(8~17),協議編號(18~20),協議前後(21~21),額度編號(22~24),撥款序號(25~27)";
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
					} // 借款人ID / 統編
					if (j == 3) {
						strField = makeFile.fillStringL(strField, 3, '0');
					} // 協議編號
					if (j == 4) {
						strField = makeFile.fillStringR(strField, 1, ' ');
					} // 協議前後 (B=協議前; A=協議後)
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
			this.info("L7007ServiceImpl.genFile error = " + errors.toString());
		}
	}
}
