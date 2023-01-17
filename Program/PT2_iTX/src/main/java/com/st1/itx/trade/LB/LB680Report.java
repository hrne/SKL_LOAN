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
import com.st1.itx.db.service.springjpa.cm.LB680ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.MakeFile;
import com.st1.itx.db.domain.SystemParas;
import com.st1.itx.db.service.SystemParasService;

import java.util.Date;
import java.text.SimpleDateFormat;

@Component("lB680Report")
@Scope("prototype")

public class LB680Report extends MakeReport {

	Date dateNow = new Date();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	String strToday = String.valueOf(Integer.parseInt(sdf.format(dateNow)) - 19110000); // 民國年月日
	String strTodayMM = strToday.substring(3, 5); // 月
	String strTodaydd = strToday.substring(5, 7); // 日
	int listCount = 0;

	@Autowired
	public LB680ServiceImpl lB680ServiceImpl;

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
		// LB680 「貸款餘額(擔保放款餘額加上部分擔保、副擔保貸款餘額)扣除擔保品鑑估值」之金額資料檔
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
			LBList = lB680ServiceImpl.findAll(dataMonth, titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("LB680Report LB680ServiceImpl.findAll error = " + errors.toString());
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
			this.error("LB680Report.genFile error = " + errors.toString());
			return false;
		}

		try {
			// excel-CSV
			genExcel(titaVo, LBList);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("LB680Report.genExcel error = " + errors.toString());
			return false;
		}

		return true;
	}

	private void genFile(TitaVo titaVo, List<Map<String, String>> LBList) throws LogicException {
		this.info("=========== LB680 genFile : ");

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
		
		String txt = "F0;F1;F2;F3;F4;F5;F6;F7;F8";
		String txt1[] = txt.split(";");

		try {
			String strContent = "";

			String strFileName = "458" + strTodayMM + strTodaydd + sfileNo1 + ".680"; // 458+月日+序號.680
			makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "B680",
					"「貸款餘額(擔保放款餘額加上部分擔保、副擔保貸款餘額)扣除擔保品鑑估值」之金額資料檔", strFileName, 2);

			// 首筆
			strContent = "JCIC-DAT-B680-V01-458" + StringUtils.repeat(" ", 5) + strToday + sfileNo2
					+ StringUtils.repeat(" ", 10) + makeFile.fillStringR(jcicEmpTel, 16, ' ')
					+ makeFile.fillStringR("審查單位聯絡人－" +jcicEmpName, 67, ' ');
			makeFile.put(strContent);

			// 欄位內容
			if (LBList.size() == 0) { // 無資料時，會出空檔

			} else {
				for (Map<String, String> tLBVo : LBList) {
					strContent = "";
					for (int j = 1; j <= 9; j++) {
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
							strField = makeFile.fillStringR(strField, 1, ' ');
							break;
						case 4:
							strField = makeFile.fillStringR(strField, 10, ' ');
							break;
						case 5:
							strField = makeFile.fillStringR(strField, 1, ' ');
							break;
						case 6:
							strField = makeFile.fillStringR(strField, 40, ' ');
							break;
						case 7:
							strField = makeFile.fillStringL(strField, 10, '0');
							break;
						case 8:
							strField = makeFile.fillStringL(strField, 5, '0');
							break;
						case 9:
							strField = makeFile.fillStringR(strField, 54, ' ');
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
					+ StringUtils.repeat(" ", 116);
			makeFile.put(strContent);

			makeFile.close();

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("L7001ServiceImpl.genFile error = " + errors.toString());
		}
	}

	private void genExcel(TitaVo titaVo, List<Map<String, String>> LBList) throws LogicException {
		this.info("=========== LB680 genExcel: ");
		this.info("LB680 genExcel TitaVo=" + titaVo);

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

		// B680 「貸款餘額(擔保放款餘額加上部分擔保、副擔保貸款餘額)扣除擔保品鑑估值」之金額資料檔
		inf = "總行代號(1~3),分行代號(4~7),交易代碼(8),授信戶IDN/BAN(9~18),上欄IDN或BAN錯誤註記(19),空白(20~59),"
				+ "貸款餘額扣除擔保品鑑估值之金額(60~69),資料所屬年月(70~74),空白(75~128)";
		txt = "F0;F1;F2;F3;F4;F5;F6;F7;F8";

		String txt1[] = txt.split(";");

		try {
			String strContent = "";
			String strFileName = "458" + strTodayMM + strTodaydd + sfileNo1 + ".680.CSV"; // 458+月日+序號.680.CSV
			this.info("------------titaVo.getEntDyI()=" + titaVo.getEntDyI());
			this.info("------------titaVo.getKinbr()=" + titaVo.getKinbr());
			makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "B680",
					"「貸款餘額(擔保放款餘額加上部分擔保、副擔保貸款餘額)扣除擔保品鑑估值」之金額資料檔", strFileName, 2);

			// 標題列
			strContent = inf;

			makeFile.put(strContent);

			// 欄位內容
			if (LBList.size() == 0) { // 無資料時，會出空檔

			} else {
				for (Map<String, String> tLBVo : LBList) {
					strContent = "";
					for (int j = 1; j <= 9; j++) {
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
							strField = makeFile.fillStringR(strField, 1, ' ');
							break;
						case 4:
							strField = makeFile.fillStringR(strField, 10, ' ');
							break;
						case 5:
							strField = makeFile.fillStringR(strField, 1, ' ');
							break;
						case 6:
							strField = makeFile.fillStringR(strField, 40, ' ');
							break;
						case 7:
							strField = makeFile.fillStringL(strField, 10, '0');
							break;
						case 8:
							strField = makeFile.fillStringL(strField, 5, '0');
							break;
						case 9:
							strField = makeFile.fillStringR(strField, 54, ' ');
							break;
						default:
							strField = "";
							break;
						}
						strContent = strContent + strField;
						if (j != 9) {
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
			this.info("LB680ServiceImpl.genExcel error = " + errors.toString());
		}
	}

}
