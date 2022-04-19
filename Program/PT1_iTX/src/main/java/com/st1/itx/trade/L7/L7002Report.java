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
import com.st1.itx.db.service.springjpa.cm.L7002ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.MakeFile;

import java.text.DecimalFormat;

@Component("L7002Report")
@Scope("prototype")

public class L7002Report extends MakeReport {
	// private static final Logger logger =
	// LoggerFactory.getLogger(L7002Report.class);

	@Autowired
	public L7002ServiceImpl L7002ServiceImpl;

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
			List<HashMap<String, String>> L7002List = L7002ServiceImpl.findAll();
			if (L7002List.size() > 0) {
				genFile(titaVo, 0, L7002List);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("L7002ServiceImpl.findAll error = " + errors.toString());
		}
	}

	private void genFile(TitaVo titaVo, int fg, List<HashMap<String, String>> L7List) throws LogicException {
		this.info("=========== L7002 genFile");
		String txt = "F0;F1;F2;F3;F4;F5;F6";
		String txt1[] = txt.split(";");

		try {
			String strContent = "";

			makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), titaVo.getTxCode(), "IAS39 資料欄位清單B", "LNM34BP.csv", 2);

			// 標題列
			strContent = "戶號(1~7),借款人ID/統編(8~17),額度編號(18~20),撥款序號(21~23),貸放利率(24~31),利率調整方式(32~32),利率欄位生效日(33~40)";
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
					} // 額度編號
					if (j == 4) {
						strField = makeFile.fillStringL(strField, 3, '0');
					} // 撥款序號
					if (j == 5) {
						DecimalFormat formatter = new DecimalFormat("0.000000");
						strField = formatter.format(Float.parseFloat(strField = (strField.isEmpty() ? "0" : strField)) / 100);
						strField = makeFile.fillStringL(strField, 8, '0');
					} // 貸放利率
					if (j == 6) {
						strField = makeFile.fillStringL(strField, 1, '0');
					} // 利率調整方式
					if (j == 7) {
						strField = makeFile.fillStringL(strField, 8, '0');
					} // 利率欄位生效日

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
			this.info("L7002ServiceImpl.genFile error = " + errors.toString());
		}
	}
}
