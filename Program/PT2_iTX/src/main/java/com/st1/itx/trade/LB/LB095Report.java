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
import com.st1.itx.db.service.springjpa.cm.LB095ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.MakeFile;
import com.st1.itx.db.domain.SystemParas;
import com.st1.itx.db.service.SystemParasService;

import java.util.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

@Component("lB095Report")
@Scope("prototype")

public class LB095Report extends MakeReport {

	Date dateNow = new Date();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	String strToday = String.valueOf(Integer.parseInt(sdf.format(dateNow)) - 19110000); // 民國年月日
	String strTodayMM = strToday.substring(3, 5); // 月
	String strTodaydd = strToday.substring(5, 7); // 日
	int listCount = 0;

	@Autowired
	public LB095ServiceImpl lB095ServiceImpl;

	@Autowired
	public MakeExcel makeExcel;

	@Autowired
	public MakeFile makeFile;

	@Autowired
	public SystemParasService sSystemParasService;

	// 自訂明細標題
	@Override
	public void printTitle() {
		this.info("printTitle nowRow = " + this.NowRow);
	}

	public boolean exec(TitaVo titaVo) throws LogicException {
		// LB095 不動產擔保品明細-建號附加檔
		this.info("-----strToday=" + strToday);
		this.info("-----strTodayMM=" + strTodayMM);
		this.info("-----strTodaydd=" + strTodaydd);

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

		List<Map<String, String>> LBList = null;
		try {
			LBList = lB095ServiceImpl.findAll(dataMonth, titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("LB095Report LB095ServiceImpl.findAll error = " + errors.toString());
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
			this.error("LB095Report.genFile error = " + errors.toString());
			return false;
		}

		try {
			// excel-CSV
			genExcel(titaVo, LBList);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("LB095Report.genExcel error = " + errors.toString());
			return false;
		}

		return true;
	}

	private void genFile(TitaVo titaVo, List<Map<String, String>> LBList) throws LogicException {
		this.info("=========== LB095 genFile : ");

		int fileday = Integer.parseInt(titaVo.getParam("FileDay")); // 民國年月日 -畫面輸入報送日期
		if (fileday > 0) {
			strToday = String.valueOf(fileday); // 7位 民國年
			strTodayMM = strToday.substring(3, 5); // 月
			strTodaydd = strToday.substring(5, 7); // 日
		}
		int ifileNo = Integer.parseInt(titaVo.getParam("FileNo"));//檔案序號
		String sfileNo1 = String.valueOf(ifileNo);
		String sfileNo2 = titaVo.getParam("FileNo");
		if (ifileNo == 0) {
			sfileNo1 = "1";
			sfileNo2 = "01"; 
		}

		// 查詢系統參數設定檔-JCIC放款報送人員資料
		String iRimBusinessType = "LN";
		String jcicEmpName = "";
		String jcicEmpTel = "";
		SystemParas tSystemParas = sSystemParasService.findById(iRimBusinessType, titaVo);
		/* 如有找到資料 */
		if (tSystemParas != null) {
			jcicEmpName = tSystemParas.getJcicEmpName();
			jcicEmpTel = tSystemParas.getJcicEmpTel();
			if (jcicEmpName == null || jcicEmpTel == null) {
				throw new LogicException(titaVo, "E0015", "請執行L8501設定JCIC報送人員資料");
			}
		} else {
			throw new LogicException(titaVo, "E0001", "系統參數設定檔"); // 查無資料
		}

		String txt = "F0;F1;F2;F3;F4;F5;F6;F7;F8;F9;F10;F11;F12;F13;F14;F15;F16;F17;F18;F19;F20;F21;F22;F23;F24;F25";
		String txt1[] = txt.split(";");

		try {
			String strContent = "";
			DecimalFormat formatter = new DecimalFormat("0");

			String strFileName = "458" + strTodayMM + strTodaydd + sfileNo1 + ".095"; // 458+月日+序號.095
			makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "B095", "不動產擔保品明細－建號附加檔", strFileName, 2);

			// 首筆
			strContent = "JCIC-DAT-B095-V01-458" + StringUtils.repeat(" ", 5) + strToday + sfileNo2
					+ StringUtils.repeat(" ", 10) + makeFile.fillStringR(jcicEmpTel, 16, ' ')
					+ makeFile.fillStringR("審查單位聯絡人－" + jcicEmpName, 80, ' ') + StringUtils.repeat(" ", 159);
			makeFile.put(strContent);

			// 欄位內容
			if (LBList.size() == 0) { // 無資料時，會出空檔

			} else {
				for (Map<String, String> tLBVo : LBList) {
					strContent = "";
					for (int j = 1; j <= 26; j++) {
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
							strField = makeFile.fillStringL(strField, 3, '0');
							break;
						case 3:
							strField = makeFile.fillStringL(strField, 4, '0');
							break;
						case 4:
							strField = makeFile.fillStringR(strField, 2, ' ');
							break;
						case 5:
							strField = makeFile.fillStringR(strField, 50, ' ');
							break;
						case 6:
							strField = makeFile.fillStringR(strField, 10, ' ');
							break;
						case 7:
							strField = makeFile.fillStringR(strField, 1, ' ');
							break;
						case 8:
							strField = makeFile.fillStringL(strField, 2, '0');
							break;
						case 9:
							strField = makeFile.fillStringR(strField, 4, ' ');
							break;
						case 10:
							strField = makeFile.fillStringL(strField, 5, '0');
							break;
						case 11:
							strField = makeFile.fillStringL(strField, 3, '0');
							break;
						case 12:
							strField = makeFile.fillStringR(strField, 12, ' ');
							break;
						case 13:
							strField = makeFile.fillStringR(strField, 12, ' ');
							break;
						case 14:
							strField = makeFile.fillStringR(strField, 76, ' ');
							break;
						case 15:
							strField = makeFile.fillStringR(strField, 1, ' ');
							break;
						case 16:
							strField = makeFile.fillStringR(strField, 1, ' ');
							break;
						case 17:
							strField = makeFile.fillStringR(strField, 6, ' ');
							break;
						case 18:
							strField = makeFile.fillStringL(strField, 3, '0');
							break;
						case 19:
							strField = makeFile.fillStringR(strField, 7, ' ');
							break;
						case 20:
							strField = makeFile.fillStringL(strField, 7, '0');
							break;
						case 21:
							formatter.applyPattern("0000000.00");
							strField = formatter.format(Float.parseFloat(strField));
							break;
						case 22:
							formatter.applyPattern("0000000.00");
							strField = formatter.format(Float.parseFloat(strField));
							break;
						case 23:
							formatter.applyPattern("0000000.00");
							strField = formatter.format(Float.parseFloat(strField));
							break;
						case 24:
							formatter.applyPattern("0000000.00");
							strField = formatter.format(Float.parseFloat(strField));
							break;
						case 25:
							strField = makeFile.fillStringR(strField, 44, ' ');
							break;
						case 26:
							strField = makeFile.fillStringL(strField, 5, '0');
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
			strContent = "TRLR" + makeFile.fillStringL(String.valueOf(listCount), 8, '0')
					+ StringUtils.repeat(" ", 288);
			makeFile.put(strContent);

			makeFile.close();

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LB095ServiceImpl.genFile error = " + errors.toString());
		}
	}

	private void genExcel(TitaVo titaVo, List<Map<String, String>> LBList) throws LogicException {
		this.info("=========== LB095 genExcel: ");
		this.info("LB095 genExcel TitaVo=" + titaVo);

		int fileday = Integer.parseInt(titaVo.getParam("FileDay")); // 民國年月日 -畫面輸入報送日期
		if (fileday > 0) {
			strToday = String.valueOf(fileday); // 7位 民國年
			strTodayMM = strToday.substring(3, 5); // 月
			strTodaydd = strToday.substring(5, 7); // 日
		}
		int ifileNo = Integer.parseInt(titaVo.getParam("FileNo"));//檔案序號
		String sfileNo1 = String.valueOf(ifileNo);
		if (ifileNo == 0) {
			sfileNo1 = "1";
		}
		// 自訂標題 inf (首筆/尾筆)
		String inf = "";
		String txt = "";

		// B095 不動產擔保品明細-建號附加檔
		inf = "資料別(1~2),總行代號(3~5),分行代號(6~9),空白(10~11),擔保品控制編碼(12~61),擔保品所有權人或代表人IDN/BAN(62~71),縣市別(72~72),"
				+ "鄉鎮市區別(73~74),段、小段號(75~78),建號-前五碼(79~83),建號-後三碼(84~86),縣市名稱(87~98),鄉鎮市區名稱(99~110),"
				+ "村里/街路/段/巷/弄/號/樓(111~186),主要用途(187~187),主要建材(結構體)(188),附屬建物用途(189~194),層數(標的所在樓高)(195~197),"
				+ "層次(標的所在樓層)(198~204),建築完成日期(屋齡)(205~211),建物總面積(212~221),主建物(層次)面積(222~231),附屬建物面積(232~241),"
				+ "共同部份持分面積(242~251),空白(252~295),資料所屬年月(296~300)";
		txt = "F0;F1;F2;F3;F4;F5;F6;F7;F8;F9;F10;F11;F12;F13;F14;F15;F16;F17;F18;F19;F20;F21;F22;F23;F24;F25";

		String txt1[] = txt.split(";");

		try {
			String strContent = "";
			DecimalFormat formatter = new DecimalFormat("0");

			String strFileName = "458" + strTodayMM + strTodaydd + sfileNo1 + ".095.CSV"; // 458+月日+序號.095.CSV
			this.info("------------titaVo.getEntDyI()=" + titaVo.getEntDyI());
			this.info("------------titaVo.getKinbr()=" + titaVo.getKinbr());
			makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "B095", "不動產擔保品明細－建號附加檔", strFileName, 2);

			// 標題列
			strContent = inf;

			makeFile.put(strContent);

			// 欄位內容
			if (LBList.size() == 0) { // 無資料時，會出空檔

			} else {
				for (Map<String, String> tLBVo : LBList) {
					strContent = "";
					for (int j = 1; j <= 26; j++) {
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
							strField = makeFile.fillStringL(strField, 3, '0');
							break;
						case 3:
							strField = makeFile.fillStringL(strField, 4, '0');
							break;
						case 4:
							strField = makeFile.fillStringR(strField, 2, ' ');
							break;
						case 5:
							strField = makeFile.fillStringR(strField, 50, ' ');
							break;
						case 6:
							strField = makeFile.fillStringR(strField, 10, ' ');
							break;
						case 7:
							strField = makeFile.fillStringR(strField, 1, ' ');
							break;
						case 8:
							strField = makeFile.fillStringL(strField, 2, '0');
							break;
						case 9:
							strField = makeFile.fillStringR(strField, 4, ' ');
							break;
						case 10:
							strField = makeFile.fillStringL(strField, 5, '0');
							break;
						case 11:
							strField = makeFile.fillStringL(strField, 3, '0');
							break;
						case 12:
							strField = makeFile.fillStringR(strField, 12, ' ');
							break;
						case 13:
							strField = makeFile.fillStringR(strField, 12, ' ');
							break;
						case 14:
							strField = makeFile.fillStringR(strField, 76, ' ');
							break;
						case 15:
							strField = makeFile.fillStringR(strField, 1, ' ');
							break;
						case 16:
							strField = makeFile.fillStringR(strField, 1, ' ');
							break;
						case 17:
							strField = makeFile.fillStringR(strField, 6, ' ');
							break;
						case 18:
							strField = makeFile.fillStringL(strField, 3, '0');
							break;
						case 19:
							strField = makeFile.fillStringR(strField, 7, ' ');
							break;
						case 20:
							strField = makeFile.fillStringL(strField, 7, '0');
							break;
						case 21:
							formatter.applyPattern("0000000.00");
							strField = formatter.format(Float.parseFloat(strField));
							break;
						case 22:
							formatter.applyPattern("0000000.00");
							strField = formatter.format(Float.parseFloat(strField));
							break;
						case 23:
							formatter.applyPattern("0000000.00");
							strField = formatter.format(Float.parseFloat(strField));
							break;
						case 24:
							formatter.applyPattern("0000000.00");
							strField = formatter.format(Float.parseFloat(strField));
							break;
						case 25:
							strField = makeFile.fillStringR(strField, 44, ' ');
							break;
						case 26:
							strField = makeFile.fillStringL(strField, 5, '0');
							break;
						default:
							strField = "";
							break;
						}
						strContent = strContent + strField;
						if (j != 26) {
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
			this.info("LB095ServiceImpl.genExcel error = " + errors.toString());
		}
	}

}
