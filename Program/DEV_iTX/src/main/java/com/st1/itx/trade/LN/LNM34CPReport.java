package com.st1.itx.trade.LN;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LNM34CPServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.MakeFile;

import java.util.Date;
import java.text.SimpleDateFormat;

@Component("lNM34CPReport")
@Scope("prototype")

public class LNM34CPReport extends MakeReport {

	Date dateNow = new Date();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	String strToday = String.valueOf(Integer.parseInt(sdf.format(dateNow)) - 19110000); // 民國年

	@Autowired
	public LNM34CPServiceImpl lNM34CPServiceImpl;

	@Autowired
	public MakeExcel makeExcel;

	@Autowired
	public MakeFile makeFile;

	// 自訂明細標題
	@Override
	public void printTitle() {
		this.info("printTitle nowRow = " + this.NowRow);
	}

	public boolean exec(TitaVo titaVo) throws LogicException {
		// LNM34CP 資料欄位清單C
		this.info("---------- LNM34CPReport exec titaVo: " + titaVo);

		List<Map<String, String>> LNM34CPList = null;
		try {
			LNM34CPList = lNM34CPServiceImpl.findAll(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("LNM34CPReport LNM34CPServiceImpl.findAll error = " + errors.toString());
			return false;
		}

		try {
			// excel-CSV
			genFile(titaVo, LNM34CPList);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("LNM34CPReport.genFile error = " + errors.toString());
			return false;
		}

		return true;
	}

	private void genFile(TitaVo titaVo, List<Map<String, String>> L7List) throws LogicException {
		this.info("=========== LNM34CP genFile : ");
		String txt = "F0;F1;F2;F3;F4;F5;F6;F7";
		String txt1[] = txt.split(";");

		try {
			String strContent = "";

			makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LNM34CP", "IAS39 資料欄位清單C", "LNM34CP.csv", 2);

			// 標題列
			// strContent =
			// "戶號(1~7),借款人ID/統編(8~17),額度編號(18~20),撥款序號(21~23),約定還款方式(24~24),繳息週期(25~26),還本週期(27~28),生效日期(29~36)";
			// makeFile.put(strContent);

			// 欄位內容
			// this.info("-----------------" + L7List);
			if (L7List.size() == 0) { // 無資料時，會出空檔

			} else {
				for (Map<String, String> tL7Vo : L7List) {
					strContent = "";
					for (int j = 1; j <= 8; j++) {
						String strField = "";
						if (tL7Vo.get(txt1[j - 1]) == null) {
							strField = "";
						} else {
							strField = tL7Vo.get(txt1[j - 1]).replace(",", "，").trim(); // csv檔: 逗號轉全形
						}

						// 格式處理
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
						if (j != 8) {
							strContent = strContent + ",";
						}
					}
					makeFile.put(strContent);
				}
			}
			makeFile.close();

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LNM34CPServiceImpl.genFile error = " + errors.toString());
		}
	}
}
