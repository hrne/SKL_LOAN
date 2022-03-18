package com.st1.itx.trade.LB;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LB080ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.MakeFile;
import com.st1.itx.db.domain.SystemParas;
import com.st1.itx.db.service.SystemParasService;

import java.util.Date;
import java.text.SimpleDateFormat;

@Component("lB080Report")
@Scope("prototype")

public class LB080Report extends MakeReport {

	Date dateNow = new Date();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	String strToday = String.valueOf(Integer.parseInt(sdf.format(dateNow)) - 19110000); // 民國年月日
	String strTodayMM = strToday.substring(3, 5); // 月
	String strTodaydd = strToday.substring(5, 7); // 日
	int listCount = 0;

	@Autowired
	public LB080ServiceImpl lB080ServiceImpl;

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
		// LB080 授信額度資料檔
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
			LBList = lB080ServiceImpl.findAll(dataMonth, titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("LB080Report LB080ServiceImpl.findAll error = " + errors.toString());
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
			this.error("LB080Report.genFile error = " + errors.toString());
			return false;
		}

		try {
			// excel-CSV
			genExcel(titaVo, LBList);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("LB080Report.genExcel error = " + errors.toString());
			return false;
		}

		return true;
	}

	private void genFile(TitaVo titaVo, List<Map<String, String>> LBList) throws LogicException {
		this.info("=========== LB080 genFile : ");
		String txt = "F0;F1;F2;F3;F4;F5;F6;F7;F8;F9;F10;F11;F12;F13;F14;F15;F16;F17;F18;F19";
		String txt1[] = txt.split(";");

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
				throw new LogicException(titaVo, "E0015", "請執行L8501設定JCIC放款報送人員資料");
			}
		} else {
			throw new LogicException(titaVo, "E0001", "系統參數設定檔"); // 查無資料
		}

		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = null;

		int sumDrawdownAmt = 0; // 本階訂約金額(台幣)(78~87)
		String strContent = "";

		String strFileName = "458" + strTodayMM + strTodaydd + sfileNo1 + ".080"; // 458+月日+序號.080
		makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "B080", "授信額度資料檔", strFileName, 2);

		// 首筆
		strContent = "JCIC-DAT-B080-V01-458" + StringUtils.repeat(" ", 5) + strToday + sfileNo2
				+ StringUtils.repeat(" ", 10) + makeFile.fillStringR(jcicEmpTel, 16, ' ')
				+ makeFile.fillStringR("審查單位聯絡人－" + jcicEmpName, 80, ' ') + StringUtils.repeat(" ", 51);
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
						strField = makeFile.fillStringR(strField, 1, ' ');
						break;
					case 5:
						strField = makeFile.fillStringR(strField, 4, ' ');
						break;
					case 6:
						strField = makeFile.fillStringR(strField, 10, ' ');
						break;
					case 7:
						strField = makeFile.fillStringR(strField, 50, ' ');
						break;
					case 8:
						strField = makeFile.fillStringR(strField, 3, ' ');
						break;
					case 9: // 本階訂約金額(台幣)(78~87)
						isNum = pattern.matcher(strField);
						if (isNum.matches()) {
							sumDrawdownAmt = sumDrawdownAmt + Integer.parseInt(strField);
							strField = makeFile.fillStringL(strField, 10, '0');
						} else {
							strField = makeFile.fillStringR(strField, 10, ' ');
						}
						break;
					case 10: // 本階訂約金額(外幣)(88~97)
						isNum = pattern.matcher(strField);
						if (isNum.matches()) {
							strField = makeFile.fillStringL(strField, 10, '0');
						} else {
							strField = makeFile.fillStringR(strField, 10, ' ');
						}
						break;
					case 11:
						strField = makeFile.fillStringL(strField, 5, '0');
						break;
					case 12:
						strField = makeFile.fillStringL(strField, 5, '0');
						break;
					case 13:
						strField = makeFile.fillStringR(strField, 1, ' ');
						break;
					case 14:
						strField = makeFile.fillStringR(strField, 1, ' ');
						break;
					case 15:
						strField = makeFile.fillStringR(strField, 50, ' ');
						break;
					case 16:
						strField = makeFile.fillStringR(strField, 1, ' ');
						break;
					case 17:
						strField = makeFile.fillStringR(strField, 1, ' ');
						break;
					case 18:
						strField = makeFile.fillStringR(strField, 2, ' ');
						break;
					case 19:
						strField = makeFile.fillStringR(strField, 24, ' ');
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
		strContent = "TRLR" + StringUtils.repeat(" ", 3) + makeFile.fillStringL(String.valueOf(sumDrawdownAmt), 13, '0') // 訂約金額(台幣)
				+ StringUtils.repeat(" ", 3) + makeFile.fillStringL(String.valueOf(listCount), 9, '0')
				+ StringUtils.repeat(" ", 160);
		makeFile.put(strContent);

		makeFile.close();

	}

	private void genExcel(TitaVo titaVo, List<Map<String, String>> LBList) throws LogicException {
		this.info("=========== LB080 genExcel: ");
		this.info("LB080 genExcel TitaVo=" + titaVo);

		int ifileNo = Integer.parseInt(titaVo.getParam("FileNo"));//檔案序號
		String sfileNo1 = String.valueOf(ifileNo);
		if (ifileNo == 0) {
			sfileNo1 = "1";
		}
		// 自訂標題 inf (首筆/尾筆)
		String inf = "";
		String txt = "";

		// B080 授信戶基本資料檔
		inf = "資料別(1~2),總行代號(3~5),分行代號(6~9),交易代碼(10),空白(11~14),授信戶IDN/BAN(15~24),本階共用額度控制編碼(25~74),"
				+ "授信幣別(75~77),本階訂約金額(台幣)(78~87),本階訂約金額(外幣)(88~97),本階額度開始年月(98~102),本階額度約定截止年月(103~107),"
				+ "循環信用註記(108),額度可否撤銷(109),上階共用額度控制編碼(110~159),科目別(160),科目別註記(161),"
				+ "擔保品類別(162~163),空白(164~187),資料所屬年月(188~192)";
		txt = "F0;F1;F2;F3;F4;F5;F6;F7;F8;F9;F10;F11;F12;F13;F14;F15;F16;F17;F18;F19";

		String txt1[] = txt.split(";");
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = null;

		int sumDrawdownAmt = 0; // 本階訂約金額(台幣)(78~87)
		String strContent = "";
		String strFileName = "458" + strTodayMM + strTodaydd + sfileNo1 + ".080.CSV"; // 458+月日+序號+.080.CSV
		this.info("------------titaVo.getEntDyI()=" + titaVo.getEntDyI());
		this.info("------------titaVo.getKinbr()=" + titaVo.getKinbr());
		makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "B080", "授信額度資料檔", strFileName, 2);

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
						strField = makeFile.fillStringR(strField, 1, ' ');
						break;
					case 5:
						strField = makeFile.fillStringR(strField, 4, ' ');
						break;
					case 6:
						strField = makeFile.fillStringR(strField, 10, ' ');
						break;
					case 7:
						strField = makeFile.fillStringR(strField, 50, ' ');
						break;
					case 8:
						strField = makeFile.fillStringR(strField, 3, ' ');
						break;
					case 9: // 本階訂約金額(台幣)(78~87)
						isNum = pattern.matcher(strField);
						if (isNum.matches()) {
							sumDrawdownAmt = sumDrawdownAmt + Integer.parseInt(strField);
							strField = makeFile.fillStringL(strField, 10, '0');
						} else {
							strField = makeFile.fillStringR(strField, 10, ' ');
						}
						break;
					case 10: // 本階訂約金額(外幣)(88~97)
						isNum = pattern.matcher(strField);
						if (isNum.matches()) {
							strField = makeFile.fillStringL(strField, 10, '0');
						} else {
							strField = makeFile.fillStringR(strField, 10, ' ');
						}
						break;
					case 11:
						strField = makeFile.fillStringL(strField, 5, '0');
						break;
					case 12:
						strField = makeFile.fillStringL(strField, 5, '0');
						break;
					case 13:
						strField = makeFile.fillStringR(strField, 1, ' ');
						break;
					case 14:
						strField = makeFile.fillStringR(strField, 1, ' ');
						break;
					case 15:
						strField = makeFile.fillStringR(strField, 50, ' ');
						break;
					case 16:
						strField = makeFile.fillStringR(strField, 1, ' ');
						break;
					case 17:
						strField = makeFile.fillStringR(strField, 1, ' ');
						break;
					case 18:
						strField = makeFile.fillStringR(strField, 2, ' ');
						break;
					case 19:
						strField = makeFile.fillStringR(strField, 24, ' ');
						break;
					case 20:
						strField = makeFile.fillStringL(strField, 5, '0');
						break;
					default:
						strField = "";
						break;
					}
					strContent = strContent + strField;
					if (j != 20) {
						strContent = strContent + ",";
					}
				}
				makeFile.put(strContent);
			}

		}

		makeFile.close();

	}

}
