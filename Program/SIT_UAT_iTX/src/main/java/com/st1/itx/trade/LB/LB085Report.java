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
import com.st1.itx.db.service.springjpa.cm.LB085ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.L8ConstantEum;
import com.st1.itx.util.common.MakeFile;

import java.util.Date;
import java.text.SimpleDateFormat;

@Component("lB085Report")
@Scope("prototype")

public class LB085Report extends MakeReport {

	Date dateNow = new Date();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	String strToday = String.valueOf(Integer.parseInt(sdf.format(dateNow)) - 19110000); // 民國年月日
	String strTodayMM = strToday.substring(3, 5); // 月
	String strTodaydd = strToday.substring(5, 7); // 日
	int listCount = 0;

	@Autowired
	public LB085ServiceImpl lB085ServiceImpl;

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
		// LB085 帳號轉換資料檔
		this.info("-----strToday=" + strToday);
		this.info("-----strTodayMM=" + strTodayMM);
		this.info("-----strTodaydd=" + strTodaydd);

		List<Map<String, String>> LBList = null;
		try {
			LBList = lB085ServiceImpl.findAll(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("LB085Report LB085ServiceImpl.findAll error = " + errors.toString());
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
			this.error("LB085Report.genFile error = " + errors.toString());
			return false;
		}

		try {
			// excel-CSV
			genExcel(titaVo, LBList);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("LB085Report.genExcel error = " + errors.toString());
			return false;
		}

		return true;

	}

	private void genFile(TitaVo titaVo, List<Map<String, String>> LBList) throws LogicException {
		this.info("=========== LB085 genFile : ");
		String txt = "F0;F1;F2;F3;F4;F5;F6;F7;F8;F9;F10;F11";
		String txt1[] = txt.split(";");

		try {
			String strContent = "";

			String strFileName = "458" + strTodayMM + strTodaydd + "1" + ".085"; // 458+月日+序號(1).085
			makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "B085", "帳號轉換資料檔", strFileName, 2);

			// 首筆
			strContent = "JCIC-DAT-B085-V01-458" + StringUtils.repeat(" ", 5) + strToday + "01" + StringUtils.repeat(" ", 10) + makeFile.fillStringR(L8ConstantEum.phoneNum, 16, ' ')
					+ makeFile.fillStringR("審查單位聯絡人－" + L8ConstantEum.contact, 67, ' ');
			makeFile.put(strContent);

			// 欄位內容
			if (LBList.size() == 0) { // 無資料時，會出空檔

			} else {
				for (Map<String, String> tLBVo : LBList) {
					strContent = "";
					for (int j = 1; j <= 12; j++) {
						String strField = "";
						if (tLBVo.get(txt1[j - 1]) == null) {
							strField = "";
						} else {
							strField = tLBVo.get(txt1[j - 1]).trim();
						}
						// 格式處理
						switch (j) {
						case 1:
							strField = makeFile.fillStringL(strField, 2, '0');
							break;
						case 2:
							strField = makeFile.fillStringL(strField, 5, '0');
							break;
						case 3:
							strField = makeFile.fillStringR(strField, 10, ' ');
							break;
						case 4:
							strField = makeFile.fillStringL(strField, 3, '0');
							break;
						case 5:
							strField = makeFile.fillStringL(strField, 4, '0');
							break;
						case 6:
							strField = makeFile.fillStringR(strField, 2, ' ');
							break;
						case 7:
							strField = makeFile.fillStringR(strField, 50, ' ');
							break;
						case 8:
							strField = makeFile.fillStringL(strField, 3, '0');
							break;
						case 9:
							strField = makeFile.fillStringL(strField, 4, '0');
							break;
						case 10:
							strField = makeFile.fillStringR(strField, 2, ' ');
							break;
						case 11:
							strField = makeFile.fillStringR(strField, 50, ' ');
							break;
						case 12:
							strField = makeFile.fillStringR(strField, 25, ' ');
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
			strContent = "TRLR" + makeFile.fillStringL(String.valueOf(listCount), 8, '0') + StringUtils.repeat(" ", 116);
			makeFile.put(strContent);

			makeFile.close();

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LB085ServiceImpl.genFile error = " + errors.toString());
		}
	}

	private void genExcel(TitaVo titaVo, List<Map<String, String>> LBList) throws LogicException {
		this.info("=========== LB085 genExcel: ");
		this.info("LB085 genExcel TitaVo=" + titaVo);

		// 自訂標題 inf (首筆/尾筆)
		String inf = "";
		String txt = "";

		// B085 帳號轉換資料檔
		inf = "資料別(1~2),轉換帳號年月(3~7),授信戶IDN/BAN(8~17),轉換前總行代號(18~20),轉換前分行代號(21~24),空白(25~26)," + "轉換前帳號(27~76),轉換後總行代號(77~79),轉換後分行代號(80~83),空白(84~85),轉換後帳號(86~135),空白(136~160)";
		txt = "F0;F1;F2;F3;F4;F5;F6;F7;F8;F9;F10;F11";

		String txt1[] = txt.split(";");

		try {
			String strContent = "";
			String strFileName = "458" + strTodayMM + strTodaydd + "1" + ".085.CSV"; // 458+月日+序號(1)+.085.CSV
			this.info("------------titaVo.getEntDyI()=" + titaVo.getEntDyI());
			this.info("------------titaVo.getKinbr()=" + titaVo.getKinbr());
			makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "B085", "帳號轉換資料檔", strFileName, 2);

			// 標題列
			strContent = inf;

			makeFile.put(strContent);

			// 欄位內容
			if (LBList.size() == 0) { // 無資料時，會出空檔

			} else {
				for (Map<String, String> tLBVo : LBList) {
					strContent = "";
					for (int j = 1; j <= 12; j++) {
						String strField = "";
						if (tLBVo.get(txt1[j - 1]) == null) {
							strField = "";
						} else {
							strField = tLBVo.get(txt1[j - 1]).trim();
						}
						// 格式處理
						switch (j) {
						case 1:
							strField = makeFile.fillStringL(strField, 2, '0');
							break;
						case 2:
							strField = makeFile.fillStringL(strField, 5, '0');
							break;
						case 3:
							strField = makeFile.fillStringR(strField, 10, ' ');
							break;
						case 4:
							strField = makeFile.fillStringL(strField, 3, '0');
							break;
						case 5:
							strField = makeFile.fillStringL(strField, 4, '0');
							break;
						case 6:
							strField = makeFile.fillStringR(strField, 2, ' ');
							break;
						case 7:
							strField = makeFile.fillStringR(strField, 50, ' ');
							break;
						case 8:
							strField = makeFile.fillStringL(strField, 3, '0');
							break;
						case 9:
							strField = makeFile.fillStringL(strField, 4, '0');
							break;
						case 10:
							strField = makeFile.fillStringR(strField, 2, ' ');
							break;
						case 11:
							strField = makeFile.fillStringR(strField, 50, ' ');
							break;
						case 12:
							strField = makeFile.fillStringR(strField, 25, ' ');
							break;
						default:
							strField = "";
							break;
						}
						strContent = strContent + strField;
						if (j != 12) {
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
			this.info("LB085ServiceImpl.genExcel error = " + errors.toString());
		}
	}

}
