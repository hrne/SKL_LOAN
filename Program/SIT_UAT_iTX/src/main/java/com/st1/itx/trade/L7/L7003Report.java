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
import com.st1.itx.db.service.springjpa.cm.L7003ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.MakeFile;

@Component("L7003Report")
@Scope("prototype")

public class L7003Report extends MakeReport {
	private static final Logger logger = LoggerFactory.getLogger(L7003Report.class);

	@Autowired
	public L7003ServiceImpl L7003ServiceImpl;

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
			List<HashMap<String, String>> L7003List = L7003ServiceImpl.findAll();
			if (L7003List.size() > 0) {
				genFile(titaVo, 0, L7003List);
			}
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("L7003ServiceImpl.findAll error = " + errors.toString());
		}
	}

	private void genFile(TitaVo titaVo, int fg, List<HashMap<String, String>> L7List) throws LogicException {
		this.info("=========== L7003 genFile");
		String txt = "F0;F1;F2;F3;F4;F5;F6;F7";
		String txt1[] = txt.split(";");

		try {
			String strContent = "";

			makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), titaVo.getTxCode(), "IAS39 資料欄位清單C", "LNM34CP.csv", 2);

			// 標題列
			strContent = "戶號(1~7),借款人ID/統編(8~17),額度編號(18~20),撥款序號(21~23),約定還款方式(24~24),繳息週期(25~26),還本週期(27~28),生效日期(29~36)";
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
						strField = makeFile.fillStringL(strField, 1, '0');
					} // 約定還款方式
					if (j == 6) {
						strField = makeFile.fillStringL(strField, 2, '0');
					} // 繳息週期
					if (j == 7) {
						strField = makeFile.fillStringL(strField, 2, '0');
					} // 還本週期
					if (j == 8) {
						strField = makeFile.fillStringL(strField, 8, '0');
					} // 生效日期

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
			this.info("L7003ServiceImpl.genFile error = " + errors.toString());
		}
	}
}
