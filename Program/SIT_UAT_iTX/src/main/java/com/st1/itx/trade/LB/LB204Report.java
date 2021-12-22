package com.st1.itx.trade.LB;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LB204ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.MakeFile;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.DecimalFormat;

@Component("lB204Report")
@Scope("prototype")

public class LB204Report extends MakeReport {

	Date dateNow = new Date();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	String strToday = String.valueOf(Integer.parseInt(sdf.format(dateNow)) - 19110000); // 民國年月日
	String strTodayMM = strToday.substring(3, 5); // 月
	String strTodaydd = strToday.substring(5, 7); // 日
	int listCount = 0;

	@Autowired
	LB204ServiceImpl lB204ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	@Autowired
	public MakeFile makeFile;

	// 自訂明細標題
	@Override
	public void printTitle() {
		this.info("printTitle nowRow = " + this.NowRow);
	}

	public boolean exec(TitaVo titaVo) throws LogicException {
		// LB204 聯徵授信餘額日報檔
		this.info("-----strToday=" + strToday);
		this.info("-----strTodayMM=" + strTodayMM);
		this.info("-----strTodaydd=" + strTodaydd);

		List<Map<String, String>> LBList = null;

		try {
			LBList = lB204ServiceImpl.findAll(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("LB204Report LB204ServiceImpl.findAll error = " + errors.toString());
			return false;
		}

		if (LBList == null) {
			listCount = 0;
		} else {
			listCount = LBList.size();
		}
		this.info("--------LBList.size()=" + listCount);

		try {
			// txt
			genFile(titaVo, LBList);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("LB204Report.genFile error = " + errors.toString());
			return false;
		}

		try {
			// excel-CSV
			genExcel(titaVo, LBList);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("LB204Report.genExcel error = " + errors.toString());
			return false;
		}

		return true;
	}

	private void genFile(TitaVo titaVo, List<Map<String, String>> LBList) throws LogicException {
		this.info("=========== LB204 genFile : ");

		String acctDate = titaVo.getEntDy(); // 8位 民國年
		this.info("-----LB204 genFile acctDate=" + acctDate);

		String txt = "F0;F1;F2;F3;F4;F5;F6;F7;F8;F9;F10;F11;F12";
		String txt1[] = txt.split(";");

		try {
			int sumLineAmt = 0; // 訂約總金額
			int sumDrawdownAmt = 0; // 授信／清償金額總金額
			String strContent = "";
			DecimalFormat formatter = new DecimalFormat("0");

			String strFileName = "458" + strTodayMM + strTodaydd + "1" + ".204"; // 458+月日+序號(1).204
			makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "B204", "聯徵授信餘額日報檔", strFileName, 2);

			// 首筆
			strContent = "JCIC-DAT-B204-V01-458" + StringUtils.repeat(" ", 5) + strToday + "01" + StringUtils.repeat(" ", 10) + makeFile.fillStringR(" ", 16, ' ') // 聯絡電話
					+ StringUtils.repeat(" ", 67);
			makeFile.put(strContent);

			// 欄位內容
			if (LBList.size() == 0) { // 無資料時，會出空檔

			} else {
				for (Map<String, String> tLBVo : LBList) {
					strContent = "";
					for (int j = 1; j <= 13; j++) {
						String strField = "";
						if (tLBVo.get(txt1[j - 1]) == null) {
							strField = "";
						} else {
							strField = tLBVo.get(txt1[j - 1]).trim();
						}
						// 格式處理
						switch (j) {
						case 1:
							strField = makeFile.fillStringL(strField, 3, '0');
							break;
						case 2:
							strField = makeFile.fillStringL(strField, 4, '0');
							break;
						case 3:
							strField = makeFile.fillStringL(strField, 7, '0');
							break;
						case 4:
							strField = makeFile.fillStringR(strField, 50, ' ');
							break;
						case 5:
							strField = makeFile.fillStringR(strField, 10, ' ');
							break;
						case 6:
							strField = makeFile.fillStringR(strField, 1, ' ');
							break;
						case 7:
							strField = makeFile.fillStringR(strField, 1, ' ');
							break;
						case 8:
							strField = makeFile.fillStringR(strField, 1, ' ');
							break;
						case 9: // 訂約金額
							if (Float.parseFloat(strField) < 0) {
								formatter.applyPattern("000000000"); // 數值會自動補-,所以格式少一位
								strField = formatter.format(Float.parseFloat(strField));
							} else {
								formatter.applyPattern("0000000000");
								strField = formatter.format(Float.parseFloat(strField));
							}
							sumLineAmt = sumLineAmt + Integer.parseInt(strField);
							break;
						case 10: // 新增核准額度當日動撥／清償金額
							strField = makeFile.fillStringL(strField, 10, '0');
							sumDrawdownAmt = sumDrawdownAmt + Integer.parseInt(strField);
							break;
						case 11: // 本筆新增核准額度應計入DBR22倍規範之金額
							if (Float.parseFloat(strField) < 0) {
								formatter.applyPattern("000000000"); // 數值會自動補-,所以格式少一位
								strField = formatter.format(Float.parseFloat(strField));
							} else {
								formatter.applyPattern("0000000000");
								strField = formatter.format(Float.parseFloat(strField));
							}
							break;
						case 12:
							strField = makeFile.fillStringR(strField, 1, ' ');
							break;
						case 13:
							strField = makeFile.fillStringR(strField, 20, ' ');
							break;
						default:
							strField = "";
							break;
						}
						strContent = strContent + strField;
					}

					makeFile.put(strContent);
				}
			}

			// 末筆
			strContent = "TRLR" + StringUtils.repeat(" ", 3) + makeFile.fillStringL(String.valueOf(sumLineAmt), 11, '0') // 訂約總金額
					+ StringUtils.repeat(" ", 3) + makeFile.fillStringL(String.valueOf(sumDrawdownAmt), 11, '0') // 授信／清償金額總金額
					+ StringUtils.repeat(" ", 3) + makeFile.fillStringL(String.valueOf(listCount), 7, '0') + StringUtils.repeat(" ", 86);
			makeFile.put(strContent);

			makeFile.close();

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LB204ServiceImpl.genFile error = " + errors.toString());
		}
	}

	private void genExcel(TitaVo titaVo, List<Map<String, String>> LBList) throws LogicException {
		this.info("=========== LB204 genExcel: ");
		this.info("LB204 genExcel TitaVo=" + titaVo);

		// 自訂標題 inf (首筆/尾筆)
		String inf = "";
		String txt = "";

		// B204 聯徵授信餘額日報檔
		inf = "總行代號(1~3),分行代號(4~7),新增核准額度日期／清償日期／額度到期或解約日期(8~14),額度控制編碼／帳號(15~64),"
				+ "授信戶IDN/BAN(65~74),科目別(75),科目別註記(76),交易別(77),訂約金額(78~87),新增核准額度當日動撥／清償金額(88~97),"
				+ "本筆新增核准額度應計入DBR22倍規範之金額(98~107),1~7欄資料值相同之交易序號(108),空白(109~128)";
		txt = "F0;F1;F2;F3;F4;F5;F6;F7;F8;F9;F10;F11;F12";

		String txt1[] = txt.split(";");

		try {
			int sumLineAmt = 0; // 訂約總金額
			int sumDrawdownAmt = 0; // 授信／清償金額總金額
			String strContent = "";
			DecimalFormat formatter = new DecimalFormat("0");
			String strFileName = "458" + strTodayMM + strTodaydd + "1" + ".204.CSV"; // 458+月日+序號(1)+.204.CSV
			this.info("------------titaVo.getEntDyI()=" + titaVo.getEntDyI());
			this.info("------------titaVo.getKinbr()=" + titaVo.getKinbr());
			makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "B204", "聯徵授信餘額日報檔", strFileName, 2);

			// 標題列
			strContent = inf;

			makeFile.put(strContent);

			// 欄位內容
			if (LBList.size() == 0) { // 無資料時，會出空檔

			} else {
				for (Map<String, String> tLBVo : LBList) {
					strContent = "";
					for (int j = 1; j <= 13; j++) {
						String strField = "";
						if (tLBVo.get(txt1[j - 1]) == null) {
							strField = "";
						} else {
							strField = tLBVo.get(txt1[j - 1]).trim();
						}
						// 格式處理
						switch (j) {
						case 1:
							strField = makeFile.fillStringL(strField, 3, '0');
							break;
						case 2:
							strField = makeFile.fillStringL(strField, 4, '0');
							break;
						case 3:
							strField = makeFile.fillStringL(strField, 7, '0');
							break;
						case 4:
							strField = makeFile.fillStringR(strField, 50, ' ');
							break;
						case 5:
							strField = makeFile.fillStringR(strField, 10, ' ');
							break;
						case 6:
							strField = makeFile.fillStringR(strField, 1, ' ');
							break;
						case 7:
							strField = makeFile.fillStringR(strField, 1, ' ');
							break;
						case 8:
							strField = makeFile.fillStringR(strField, 1, ' ');
							break;
						case 9: // 訂約金額
							if (Float.parseFloat(strField) < 0) {
								formatter.applyPattern("000000000"); // 數值會自動補-,所以格式少一位
								strField = formatter.format(Float.parseFloat(strField));
							} else {
								formatter.applyPattern("0000000000");
								strField = formatter.format(Float.parseFloat(strField));
							}
							sumLineAmt = sumLineAmt + Integer.parseInt(strField);
							break;
						case 10: // 新增核准額度當日動撥／清償金額
							strField = makeFile.fillStringL(strField, 10, '0');
							sumDrawdownAmt = sumDrawdownAmt + Integer.parseInt(strField);
							break;
						case 11: // 本筆新增核准額度應計入DBR22倍規範之金額
							if (Float.parseFloat(strField) < 0) {
								formatter.applyPattern("000000000"); // 數值會自動補-,所以格式少一位
								strField = formatter.format(Float.parseFloat(strField));
							} else {
								formatter.applyPattern("0000000000");
								strField = formatter.format(Float.parseFloat(strField));
							}
							break;
						case 12:
							strField = makeFile.fillStringR(strField, 1, ' ');
							break;
						case 13:
							strField = makeFile.fillStringR(strField, 20, ' ');
							break;
						default:
							strField = "";
							break;
						}
						strContent = strContent + strField;
						if (j != 13) {
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
			this.info("LB204ServiceImpl.genExcel error = " + errors.toString());
		}
	}

}
