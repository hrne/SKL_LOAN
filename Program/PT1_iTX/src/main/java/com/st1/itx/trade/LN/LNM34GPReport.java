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
import com.st1.itx.db.service.springjpa.cm.LNM34GPServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.MakeFile;

import java.util.Date;
import java.text.SimpleDateFormat;

@Component("lNM34GPReport")
@Scope("prototype")

public class LNM34GPReport extends MakeReport {

	Date dateNow = new Date();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	String strToday = String.valueOf(Integer.parseInt(sdf.format(dateNow)) - 19110000); // 民國年

	@Autowired
	public LNM34GPServiceImpl lNM34GPServiceImpl;

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
		// LNM34GP 資料欄位清單G
		this.info("---------- LNM34GPReport exec titaVo: " + titaVo);

		// 系統營業日
		int tbsdyf = this.txBuffer.getTxCom().getTbsdyf();
		// 月底營業日
		int mfbsdyf = this.txBuffer.getTxCom().getMfbsdyf();
		// 上月月底日
		int lmndyf = this.txBuffer.getTxCom().getLmndyf();

		int dataMonth = 0;

		if (tbsdyf == mfbsdyf) {
			// 今日為月底營業日:產本月報表
			dataMonth = tbsdyf / 100;
		} else {
			// 今日非月底營業日:產上月報表
			dataMonth = lmndyf / 100;
		}

		this.info("dataMonth= " + dataMonth);

		List<Map<String, String>> LNM34GPList = null;
		try {
			LNM34GPList = lNM34GPServiceImpl.findAll(dataMonth, titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("LNM34GPReport LNM34GPServiceImpl.findAll error = " + errors.toString());
			return false;
		}

		try {
			// excel-CSV
			genFile(titaVo, LNM34GPList);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("LNM34GPReport.genFile error = " + errors.toString());
			return false;
		}

		return true;
	}

	private void genFile(TitaVo titaVo, List<Map<String, String>> L7List) throws LogicException {
		this.info("=========== LNM34GP genFile : ");
		String txt = "F0;F1;F2;F3;F4;F5";
		String txt1[] = txt.split(";");

		try {
			String strContent = "";

			makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LNM34GP", "IAS39 資料欄位清單G", "LNM34GP.csv", 2);

			// 標題列
			// strContent =
			// "戶號(1~7),借款人ID/統編(8~17),協議編號(18~20),協議前後(21~21),額度編號(22~24),撥款序號(25~27)";
			// makeFile.put(strContent);

			// 欄位內容
			// this.info("-----------------" + L7List);
			if (L7List.size() == 0) { // 無資料時，會出空檔

			} else {
				for (Map<String, String> tL7Vo : L7List) {
					strContent = "";
					for (int j = 1; j <= 6; j++) {
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
						if (j != 6) {
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
			this.info("LNM34GPServiceImpl.genFile error = " + errors.toString());
		}
	}
}
