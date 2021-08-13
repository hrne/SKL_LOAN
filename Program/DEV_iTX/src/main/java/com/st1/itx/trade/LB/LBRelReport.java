package com.st1.itx.trade.LB;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LBRelServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.MakeFile;

import java.util.Date;
import java.text.SimpleDateFormat;

@Component("lBRelReport")
@Scope("prototype")

public class LBRelReport extends MakeReport {
	// private static final Logger logger = LoggerFactory.getLogger(LBRelReport.class);

	Date dateNow = new Date();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	String strToday = String.valueOf(Integer.parseInt(sdf.format(dateNow)) - 19110000); // 民國年月日
	String strTodayMM = strToday.substring(3,5); // 月
	String strTodaydd= strToday.substring(5,7);  // 日
	int listCount = 0;

	@Autowired
	public LBRelServiceImpl lBRelServiceImpl;

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
		// LBRel 聯徵授信「同一關係企業及集團企業」資料報送檔
		try {
			this.info("-----strToday=" + strToday);
			this.info("-----strTodayMM=" + strTodayMM);
			this.info("-----strTodaydd=" + strTodaydd);
			
			List<HashMap<String, String>> LBList = lBRelServiceImpl.findAll(titaVo);
			if (LBList == null) {
				listCount = 0;
			} else {
				listCount = LBList.size();
			}
			this.info("--------LBList.size()=" + listCount);

			// txt
			genFile(titaVo, LBList);
			// excel
			genExcel(titaVo, LBList);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LBRelServiceImpl.findAll error = " + errors.toString());
		}
	}

	private void genFile(TitaVo titaVo, List<HashMap<String, String>> LBList) throws LogicException {
		this.info("=========== LBRel genFile : ");
		String txt = "F0;F1;F2;F3;F4;F5;F6;F7;F8;F9;F10";
		String txt1[] = txt.split(";");

		try {
			String strContent = "";

			String strFileName = "458"+ strToday + "1" + ".GRM";		// 458+年月日+序號(1).GRM
			makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "BRel", "聯徵授信「同一關係企業及集團企業」資料報送檔", strFileName, 2);

			// 首筆  無 ???
//			strContent = "JCIC-DAT-BRel-V01-458" + StringUtils.repeat(" ",5) + strToday + "01" + StringUtils.repeat(" ",10) 
//					   + makeFile.fillStringR("02-23895858#7067", 16, ' ') + makeFile.fillStringR("審查單位聯絡人－許高政", 67, ' ');
//			makeFile.put(strContent);

			// 欄位內容
			if (LBList.size() == 0) {	// 無資料時，會出空檔

			} else {
				for (HashMap<String, String> tLBVo : LBList) {
					strContent = "";
					for (int j = 1; j <= tLBVo.size(); j++) {
						String strField = "";
						if (tLBVo.get(txt1[j - 1]) == null) {
							strField = "";
						} else {
							strField = tLBVo.get(txt1[j - 1]).trim();
						}
						// 格式處理
						switch (j)	{
							case 1 :	strField = makeFile.fillStringL(strField, 3, '0');			break;
							case 2 :	strField = makeFile.fillStringL(strField, 4, '0');			break;
							case 3 :	strField = makeFile.fillStringL(strField, 5, '0');			break;
							case 4 :	strField = makeFile.fillStringR(strField, 1, ' ');			break;
							case 5 :	strField = makeFile.fillStringR(strField, 8, ' ');			break;
							case 6 :	strField = makeFile.fillStringR(strField, 1, ' ');			break;
							case 7 :	strField = makeFile.fillStringR(strField, 8, ' ');			break;
							case 8 :	strField = makeFile.fillStringR(strField, 1, ' ');			break;
							case 9 :	strField = makeFile.fillStringR(strField, 3, ' ');			break;
							case 10:	strField = makeFile.fillStringR(strField, 5, ' ');			break;  // 空白
							case 11:	strField = makeFile.fillStringR(strField, 1, ' ');			break;  // 結束記號
							default:	strField = "";                                              break;
						}

						strContent = strContent + strField;
					}
					makeFile.put(strContent);
				}
			}

			// 末筆  無 ???
			// strContent = "TRLR" + makeFile.fillStringL(String.valueOf(listCount), 8, '0') + StringUtils.repeat(" ",116);
			// makeFile.put(strContent);

			long sno = makeFile.close();
			// makeFile.toFile(sno);	// 不直接下傳

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("L7001ServiceImpl.genFile error = " + errors.toString());
		}
	}

	private void genExcel(TitaVo titaVo, List<HashMap<String, String>> LBList) throws LogicException {
		this.info("=========== LBRel genExcel: ");
		this.info("LBRel genExcel TitaVo=" + titaVo);

		// 自訂標題 inf (首筆/尾筆)
		String inf = "";
		String infLast = "";
		String txt = "";

		// BRel 聯徵授信「同一關係企業及集團企業」資料報送檔
		inf = "總行代號(1~3);分行代號(4~7);客戶填表年月(8~12);報送時機(13~13);授信企業統編(14~21);空白(22~22);"
			+ " 關係企業統編(23~30);空白(31~31);關係企業關係代號(32~34);空白(35~39);結束註記碼(40~40)";
		txt = "F0;F1;F2;F3;F4;F5;F6;F7;F8;F9;F10";

		// 自訂標題列寫入記號
		boolean infFg = true;
		if ((inf.trim()).equals("")) {
			infFg = false;
		}

		String inf1[] = inf.split(";");
		String txt1[] = txt.split(";");

		try {
			String strContent = "";
			// 若有底稿，需自行判斷列印起始行數 i
			// 若無底稿，設定列印起始行數 i=1
			int i = 1;
			String strFileName = "458"+ strToday + "1" + ".GRM";		// 458+年月日+序號(1).GRM
			this.info("------------titaVo.getEntDyI()=" + titaVo.getEntDyI());
			this.info("------------titaVo.getKinbr()=" + titaVo.getKinbr());
			makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "BRel", "聯徵授信「同一關係企業及集團企業」資料報送檔", strFileName);

			// 不設定CellStyle
			makeExcel.setNeedStyle(false);

			// 設定欄位寬度
			setAllWidth();

			// 自訂標題列
			if (infFg == true) {
				for (int j = 1; j <= inf1.length; j++) {
					makeExcel.setValue(i, j, inf1[j - 1]);
				}
				infFg = false;
				i++;
			}

			// 欄位內容
			if (LBList.size() == 0) {	// 無資料時，會出空檔

			} else {
				for (HashMap<String, String> tLBVo : LBList) {
					for (int j = 1; j <= tLBVo.size(); j++) {
						if (tLBVo.get(txt1[j - 1]) == null) {
							makeExcel.setValue(i, j, "");
						} else {
							makeExcel.setValue(i, j, tLBVo.get(txt1[j - 1]));
						}
					}
					i++;
				}
			}

			long sno = makeExcel.close();
			// makeExcel.toExcel(sno);	// 不直接下傳

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LBRelServiceImpl.genExcel error = " + errors.toString());
		}
	}
	
	private void setAllWidth() throws LogicException {
		this.info("=========== setAllWidth()");
		makeExcel.setWidth(1, 5);
		makeExcel.setWidth(2, 6);
		makeExcel.setWidth(3, 7);
		makeExcel.setWidth(4, 3);
		makeExcel.setWidth(5, 10);
		makeExcel.setWidth(6, 3);
		makeExcel.setWidth(7, 10);
		makeExcel.setWidth(8, 3);
		makeExcel.setWidth(9, 5);
		makeExcel.setWidth(10,7);
		makeExcel.setWidth(11,3);
	}

}
