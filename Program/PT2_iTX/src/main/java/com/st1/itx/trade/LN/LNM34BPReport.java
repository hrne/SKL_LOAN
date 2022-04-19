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
import com.st1.itx.db.service.springjpa.cm.LNM34BPServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.MakeFile;

import java.text.DecimalFormat;
import java.util.Date;
import java.text.SimpleDateFormat;

@Component("lNM34BPReport")
@Scope("prototype")

public class LNM34BPReport extends MakeReport {

	Date dateNow = new Date();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	String strToday = String.valueOf(Integer.parseInt(sdf.format(dateNow)) - 19110000); // 民國年

	@Autowired
	public LNM34BPServiceImpl lNM34BPServiceImpl;

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
		// LNM34BP 資料欄位清單B
		this.info("---------- LNM34BPReport exec titaVo: " + titaVo);

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

		List<Map<String, String>> LNM34BPList = null;
		try {
			LNM34BPList = lNM34BPServiceImpl.findAll(dataMonth, titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("LNM34BPReport LNM34BPServiceImpl.findAll error = " + errors.toString());
			return false;
		}

		try {
			genFile(titaVo, LNM34BPList);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("LNM34BPReport.genFile error = " + errors.toString());
			return false;
		}

		return true;
	}

	private void genFile(TitaVo titaVo, List<Map<String, String>> L7List) throws LogicException {
		this.info("=========== LNM34BP genFile : ");
		String txt = "F0;F1;F2;F3;F4;F5;F6";
		String txt1[] = txt.split(";");

		try {
			String strContent = "";

			makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LNM34BP", "IAS39 資料欄位清單B", "LNM34BP.csv", 2);

			// 標題列
			// strContent =
			// "戶號(1~7),借款人ID/統編(8~17),額度編號(18~20),撥款序號(21~23),貸放利率(24~31),利率調整方式(32~32),利率欄位生效日(33~40)";
			// makeFile.put(strContent);

			// 欄位內容
			if (L7List.size() == 0) { // 無資料時，會出空檔

			} else {
				for (Map<String, String> tL7Vo : L7List) {
					strContent = "";
					for (int j = 1; j <= 7; j++) {
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
							DecimalFormat formatter = new DecimalFormat("0.000000");
							strField = formatter
									.format(Float.parseFloat(strField = (strField.isEmpty() ? "0" : strField)));
							strField = makeFile.fillStringL(strField, 8, '0');
						} // 貸放利率
						if (j == 6) {
							strField = makeFile.fillStringL(strField, 1, '0');
						} // 利率調整方式
						if (j == 7) {
							strField = makeFile.fillStringL(strField, 8, '0');
						} // 利率欄位生效日

						strContent = strContent + strField;
						if (j != 7) {
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
			this.info("LNM34BPServiceImpl.genFile error = " + errors.toString());
		}
	}
}
