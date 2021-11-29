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
import com.st1.itx.db.service.springjpa.cm.LB093ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.L8ConstantEum;
import com.st1.itx.util.common.MakeFile;

import java.util.Date;
import java.text.SimpleDateFormat;

@Component("lB093Report")
@Scope("prototype")

public class LB093Report extends MakeReport {

	Date dateNow = new Date();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	String strToday = String.valueOf(Integer.parseInt(sdf.format(dateNow)) - 19110000); // 民國年月日
	String strTodayMM = strToday.substring(3, 5); // 月
	String strTodaydd = strToday.substring(5, 7); // 日
	int listCount = 0;

	@Autowired
	public LB093ServiceImpl lB093ServiceImpl;

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
		// LB093 動產及貴重物品擔保品明細檔
		try {
			this.info("-----strToday=" + strToday);
			this.info("-----strTodayMM=" + strTodayMM);
			this.info("-----strTodaydd=" + strTodaydd);

			List<Map<String, String>> LBList = lB093ServiceImpl.findAll(titaVo);
			if (LBList == null) {
				listCount = 0;
			} else {
				listCount = LBList.size();
			}
			this.info("--------LBList.size()=" + listCount);

			// txt
			genFile(titaVo, LBList);
			// excel-CSV
			genExcel(titaVo, LBList);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LB093ServiceImpl.findAll error = " + errors.toString());
		}
	}

	private void genFile(TitaVo titaVo, List<Map<String, String>> LBList) throws LogicException {
		this.info("=========== LB093 genFile : ");
		String txt = "F0;F1;F2;F3;F4;F5;F6;F7;F8;F9;F10;F11;F12;F13;F14;F15;F16;F17;F18;F19";
		String txt1[] = txt.split(";");

		try {
			String strContent = "";

			String strFileName = "458" + strTodayMM + strTodaydd + "1" + ".093"; // 458+月日+序號(1).093
			makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "B093", "動產及貴重物品擔保品明細檔", strFileName, 2);

			// 首筆
			strContent = "JCIC-DAT-B093-V01-458" + StringUtils.repeat(" ", 5) + strToday + "01"
					+ StringUtils.repeat(" ", 10) + makeFile.fillStringR(L8ConstantEum.phoneNum, 16, ' ')
					+ makeFile.fillStringR("審查單位聯絡人－" + L8ConstantEum.contact, 99, ' ');
			makeFile.put(strContent);

			// 欄位內容
			if (LBList.size() == 0) { // 無資料時，會出空檔

			} else {
				for (Map<String, String> tLBVo : LBList) {
					strContent = "";
					for (int j = 1; j <= 20; j++) {
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
							strField = makeFile.fillStringR(strField, 2, ' ');
							break;
						case 7:
							strField = makeFile.fillStringR(strField, 10, ' ');
							break;
						case 8:
							strField = makeFile.fillStringL(strField, 8, '0');
							break;
						case 9:
							strField = makeFile.fillStringL(strField, 5, '0');
							break;
						case 10:
							strField = makeFile.fillStringL(strField, 8, '0');
							break;
						case 11:
							strField = makeFile.fillStringL(strField, 5, '0');
							break;
						case 12:
							strField = makeFile.fillStringL(strField, 8, '0');
							break;
						case 13:
							strField = makeFile.fillStringL(strField, 1, '0');
							break;
						case 14:
							strField = makeFile.fillStringL(strField, 8, '0');
							break;
						case 15:
							strField = makeFile.fillStringL(strField, 8, '0');
							break;
						case 16:
							strField = makeFile.fillStringL(strField, 8, '0');
							break;
						case 17:
							strField = makeFile.fillStringL(strField, 5, '0');
							break;
						case 18:
							strField = makeFile.fillStringL(strField, 1, ' ');
							break;
						case 19:
							strField = makeFile.fillStringR(strField, 17, ' ');
							break;
						case 20:
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
			strContent = "TRLR" + makeFile.fillStringL(String.valueOf(listCount), 8, '0') + StringUtils.repeat(" ", 148);
			makeFile.put(strContent);

			makeFile.close();

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LB093ServiceImpl.genFile error = " + errors.toString());
		}
	}

	private void genExcel(TitaVo titaVo, List<Map<String, String>> LBList) throws LogicException {
		this.info("=========== LB093 genExcel: ");
		this.info("LB093 genExcel TitaVo=" + titaVo);

		// 自訂標題 inf (首筆/尾筆)
		String inf = "";
		String txt = "";

		// B093 動產及貴重物品擔保品明細檔
		inf = "資料別(1~2),總行代號(3~5),分行代號(6~9),空白(10~11),擔保品控制編碼(12~61),擔保品類別(62~63),擔保品所有權人或代表人IDN/BAN(64~73),"
				+ "鑑估值(74~81),鑑估日期(82~86),可放款值(87~94),設定日期(95~99),本行本月設定金額(100~107),本月設定抵押順位(108),"
				+ "本行累計已設定總金額(109~116),其他債權人前已設定金額(117~124),處分價格(125~132),權利到期年月(133~137),是否有保險(138),"
				+ "空白(139~155),資料所屬年月(156~160)";
		txt = "F0;F1;F2;F3;F4;F5;F6;F7;F8;F9;F10;F11;F12;F13;F14;F15;F16;F17;F18;F19";

		String txt1[] = txt.split(";");

		try {
			String strContent = "";
			String strFileName = "458" + strTodayMM + strTodaydd + "1" + ".093.CSV"; // 458+月日+序號(1).093.CSV
			this.info("------------titaVo.getEntDyI()=" + titaVo.getEntDyI());
			this.info("------------titaVo.getKinbr()=" + titaVo.getKinbr());
			makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "B093", "動產及貴重物品擔保品明細檔", strFileName, 2);

			// 標題列
			strContent = inf;

			makeFile.put(strContent);

			// 欄位內容
			if (LBList.size() == 0) { // 無資料時，會出空檔

			} else {
				for (Map<String, String> tLBVo : LBList) {
					strContent = "";
					for (int j = 1; j <= 20; j++) {
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
							strField = makeFile.fillStringR(strField, 2, ' ');
							break;
						case 7:
							strField = makeFile.fillStringR(strField, 10, ' ');
							break;
						case 8:
							strField = makeFile.fillStringL(strField, 8, '0');
							break;
						case 9:
							strField = makeFile.fillStringL(strField, 5, '0');
							break;
						case 10:
							strField = makeFile.fillStringL(strField, 8, '0');
							break;
						case 11:
							strField = makeFile.fillStringL(strField, 5, '0');
							break;
						case 12:
							strField = makeFile.fillStringL(strField, 8, '0');
							break;
						case 13:
							strField = makeFile.fillStringL(strField, 1, '0');
							break;
						case 14:
							strField = makeFile.fillStringL(strField, 8, '0');
							break;
						case 15:
							strField = makeFile.fillStringL(strField, 8, '0');
							break;
						case 16:
							strField = makeFile.fillStringL(strField, 8, '0');
							break;
						case 17:
							strField = makeFile.fillStringL(strField, 5, '0');
							break;
						case 18:
							strField = makeFile.fillStringL(strField, 1, ' ');
							break;
						case 19:
							strField = makeFile.fillStringR(strField, 17, ' ');
							break;
						case 20:
							strField = makeFile.fillStringL(strField, 5, '0');
							break;
						default:
							strField = "";
							break;
						}
						strContent = strContent + strField;
						if (j != tLBVo.size()) {
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
			this.info("LB093ServiceImpl.genExcel error = " + errors.toString());
		}
	}

}
