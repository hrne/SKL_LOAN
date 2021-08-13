package com.st1.itx.trade.LB;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LB092ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.MakeFile;

import java.util.Date;
import java.text.SimpleDateFormat;

@Component("lB092Report")
@Scope("prototype")

public class LB092Report extends MakeReport {
	private static final Logger logger = LoggerFactory.getLogger(LB092Report.class);

	Date dateNow = new Date();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	String strToday = String.valueOf(Integer.parseInt(sdf.format(dateNow)) - 19110000); // 民國年月日
	String strTodayMM = strToday.substring(3,5); // 月
	String strTodaydd= strToday.substring(5,7);  // 日
	int listCount = 0;

	@Autowired
	public LB092ServiceImpl lB092ServiceImpl;

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
		// LB092 不動產擔保品明細檔
		try {
			this.info("-----strToday=" + strToday);
			this.info("-----strTodayMM=" + strTodayMM);
			this.info("-----strTodaydd=" + strTodaydd);
			
			List<HashMap<String, String>> LBList = lB092ServiceImpl.findAll(titaVo);
//			this.info("-----------------" + LBList);
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
			this.info("LB092ServiceImpl.findAll error = " + errors.toString());
		}
	}

	private void genFile(TitaVo titaVo, List<HashMap<String, String>> LBList) throws LogicException {
		this.info("=========== LB092 genFile : ");
		String txt = "F0;F1;F2;F3;F4;F5;F6;F7;F8;F9;F10;F11;F12;F13;F14;F15;F16;F17;F18;F19;" 
				   + "F20;F21;F22;F23;F24;F25;F26;F27;F28;F29;F30;F31;F32;F33;F34;F35";
		String txt1[] = txt.split(";");

		try {
			String strContent = "";

			String strFileName = "458"+ strTodayMM + strTodaydd + "1" + ".092";		// 458+月日+序號(1).092
			makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "B092", "不動產擔保品明細檔", strFileName, 2);

			// 首筆
			strContent = "JCIC-DAT-B092-V01-458" + StringUtils.repeat(" ",5) + strToday + "01" + StringUtils.repeat(" ",10) 
					   + makeFile.fillStringR("02-23895858#7067", 16, ' ') + makeFile.fillStringR("審查單位聯絡人－許高政", 189, ' ');
			makeFile.put(strContent);

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
							case 1 :  strField = makeFile.fillStringL(strField, 2, '0');			break;
							case 2 :  strField = makeFile.fillStringL(strField, 3, '0');			break;
							case 3 :  strField = makeFile.fillStringL(strField, 4, '0');			break;
							case 4 :  strField = makeFile.fillStringR(strField, 2, ' ');			break;
							case 5 :  strField = makeFile.fillStringR(strField,50, ' ');			break;
							case 6 :  strField = makeFile.fillStringR(strField, 2, ' ');			break;
							case 7 :  strField = makeFile.fillStringR(strField,10, ' ');			break;
							case 8 :  // 鑑估(總市)值(74~81)
								  	  if (strField.trim().equals("*")) {
								  		  strField = makeFile.fillStringR(strField, 8, ' ');
								  	  } else {
								  		  strField = makeFile.fillStringL(strField, 8, '0');
								  	  }
								  	  break;
							case 9 :  strField = makeFile.fillStringL(strField, 5, '0');			break;
							case 10:  // 可放款值(87~94)
							  	 	  if (strField.trim().equals("*")) {
							  	 		  strField = makeFile.fillStringR(strField, 8, ' ');
							  	 	  } else {
							  	 		  strField = makeFile.fillStringL(strField, 8, '0');
							  	 	  }
							  	 	  break;
							case 11:  strField = makeFile.fillStringL(strField, 5, '0');			break;
							case 12:  // 本行本月設定金額(100~107)
						  	 	  	  if (strField.trim().equals("*")) {
						  	 	  		  strField = makeFile.fillStringR(strField, 8, ' ');
						  	 	  	  } else {
						  	 	  		  strField = makeFile.fillStringL(strField, 8, '0');
						  	 	  	  }
						  	 	  	  break;
							case 13:  strField = makeFile.fillStringL(strField, 1, '0');			break;
							case 14:  // 本行累計已設定總金額(109~116)
				  	 	  	  		  if (strField.trim().equals("*")) {
				  	 	  	  			  strField = makeFile.fillStringR(strField, 8, ' ');
				  	 	  	  		  } else {
				  	 	  	  			  strField = makeFile.fillStringL(strField, 8, '0');
				  	 	  	  		  }
				  	 	  	  		  break;
							case 15:  // 其他債權人已設定金額(117~124)
				  	 	  	  		  if (strField.trim().equals("*")) {
				  	 	  	  			  strField = makeFile.fillStringR(strField, 8, ' ');
				  	 	  	  		  } else {
				  	 	  	  			  strField = makeFile.fillStringL(strField, 8, '0');
				  	 	  	  		  }
				  	 	  	  		  break;
							case 16:  // 處分價格(125~132)
				  	 	  	  		  if (strField.trim().equals("*")) {
				  	 	  	  			  strField = makeFile.fillStringR(strField, 8, ' ');
				  	 	  	  		  } else {
				  	 	  	  			  strField = makeFile.fillStringL(strField, 8, '0');
				  	 	  	  		  }
				  	 	  	  		  break;
							case 17:  strField = makeFile.fillStringL(strField, 5, '0');			break;
							case 18:  strField = makeFile.fillStringR(strField, 1, ' ');			break;
							case 19:  strField = makeFile.fillStringL(strField, 2, '0');			break;
							case 20:  strField = makeFile.fillStringR(strField, 4, ' ');			break;
							case 21:  strField = makeFile.fillStringL(strField, 4, '0');			break;
							case 22:  strField = makeFile.fillStringL(strField, 4, '0');			break;
							case 23:  strField = makeFile.fillStringL(strField, 5, '0');			break;
							case 24:  strField = makeFile.fillStringL(strField, 3, '0');			break;
							case 25:  strField = makeFile.fillStringR(strField, 5, ' ');			break;
							case 26:  strField = makeFile.fillStringR(strField, 1, ' ');			break;
							case 27:  // 預估應計土地增值稅(167~174)
									  if (strField.trim().equals("X")) {
										  strField = makeFile.fillStringR(strField, 8, ' ');
									  } else {
										  strField = makeFile.fillStringL(strField, 8, '0');
									  }
									  break;
							case 28:  // 應計土地增值稅之預估年月(175~179) 
								  	  if (strField.trim().equals("X")) {
								  		  strField = makeFile.fillStringR(strField, 5, ' ');
								  	  } else {
								  		  strField = makeFile.fillStringL(strField, 5, '0');
								  	  }
								  	  break;
							case 29:  // 買賣契約價格  
							  	  	  if (strField.trim().equals("")) {
							  	  		  strField = makeFile.fillStringR(" ", 8, ' ');
							  	  	  } else {
							  	  		  strField = makeFile.fillStringL(strField, 8, '0');
							  	  	  }
							  	  	  break;
							case 30:  // 買賣契約日期 
									  if (strField.trim().equals("")) {
						  	  		  	strField = makeFile.fillStringR(" ", 7, ' ');
						  	  	  	  } else {
						  	  	  		strField = makeFile.fillStringL(strField, 7, '0');
						  	  	  	  }
						  	  	  	  break;
							case 31:  strField = makeFile.fillStringR(strField, 1, ' ');			break;
							case 32:  strField = makeFile.fillStringL(strField, 9, '0');			break;
							case 33:  strField = makeFile.fillStringL(strField,10, '0');			break;
							case 34:  strField = makeFile.fillStringR(strField, 2, ' ');			break;
							case 35:  strField = makeFile.fillStringR(strField,29, ' ');			break;
							case 36:  strField = makeFile.fillStringL(strField, 5, '0');			break;
							default:  strField = "";                                             	break;
						}
						strContent = strContent + strField;
					}
					makeFile.put(strContent);
				}
			}

			// 末筆
			strContent = "TRLR" + makeFile.fillStringL(String.valueOf(listCount), 8, '0') + StringUtils.repeat(" ",238);
			makeFile.put(strContent);

			long sno = makeFile.close();
			// makeFile.toFile(sno);	// 不直接下傳

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LB092ServiceImpl.genFile error = " + errors.toString());
		}
	}

	private void genExcel(TitaVo titaVo, List<HashMap<String, String>> LBList) throws LogicException {
		this.info("=========== LB092 genExcel: ");
		this.info("LB092 genExcel TitaVo=" + titaVo);

		// 自訂標題 inf (首筆/尾筆)
		String inf = "";
		String infLast = "";
		String txt = "";

		// B092 不動產擔保品明細檔
		inf = "資料別(1~2);總行代號(3~5);分行代號(6~9);空白(10~11);擔保品控制編碼(12~61);擔保品類別(62~63);擔保品所有權人或代表人IDN/BAN(64~73);鑑估(總市)值(74~81);鑑估日期(82~86);可放款值(87~94);設定日期(95~99);本行本月設定金額(100~107);本行設定抵押順位(108~108);本行累計已設定總金額(109~116);其他債權人已設定金額(117~124);處分價格(125~132);權利到期年月(133~137);縣市別(138~138);鄉鎮市區別(139~140);段、小段號(141~144);地號-前四碼(145~148);地號-後四碼(149~152);建號-前五碼(153~157);建號-後三碼(158~160);郵遞區號(161~165);是否有保險(166~166);預估應計土地增值稅(167~174);應計土地增值稅之預估年月(175~179);買賣契約價格(180~187);買賣契約日期(188~194);停車位形式(195~195);車位單獨登記面積(196~204);土地持份面積(205~214);建物類別(215~216);空白(217~245);資料所屬年月(246~250)";
		txt = "F0;F1;F2;F3;F4;F5;F6;F7;F8;F9;F10;F11;F12;F13;F14;F15;F16;F17;F18;F19;" 
		    + "F20;F21;F22;F23;F24;F25;F26;F27;F28;F29;F30;F31;F32;F33;F34;F35";

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
			String strFileName = "458"+ strTodayMM + strTodaydd + "1" + ".092";		// 458+月日+序號(1).092
			this.info("------------titaVo.getEntDyI()=" + titaVo.getEntDyI());
			this.info("------------titaVo.getKinbr()=" + titaVo.getKinbr());
			makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "B092", "不動產擔保品明細檔", strFileName);

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
			this.info("LB092ServiceImpl.genExcel error = " + errors.toString());
		}
	}

	private void setAllWidth() throws LogicException {
		this.info("=========== setAllWidth()");
		makeExcel.setWidth(1, 6);
		makeExcel.setWidth(2, 7);
		makeExcel.setWidth(3, 8);
		makeExcel.setWidth(4, 6);
		makeExcel.setWidth(5, 54);
		makeExcel.setWidth(6, 6);
		makeExcel.setWidth(7, 14);
		makeExcel.setWidth(8, 12);
		makeExcel.setWidth(9, 9);
		makeExcel.setWidth(10, 12);
		makeExcel.setWidth(11, 9);
		makeExcel.setWidth(12, 12);
		makeExcel.setWidth(13, 5);
		makeExcel.setWidth(14, 12);
		makeExcel.setWidth(15, 12);
		makeExcel.setWidth(16, 12);
		makeExcel.setWidth(17, 9);
		makeExcel.setWidth(18, 5);
		makeExcel.setWidth(19, 6);
		makeExcel.setWidth(20, 8);
		makeExcel.setWidth(21, 8);
		makeExcel.setWidth(22, 8);
		makeExcel.setWidth(23, 9);
		makeExcel.setWidth(24, 7);
		makeExcel.setWidth(25, 9);
		makeExcel.setWidth(26, 5);
		makeExcel.setWidth(27, 12);
		makeExcel.setWidth(28, 9);
		makeExcel.setWidth(29, 12);
		makeExcel.setWidth(30, 11);
		makeExcel.setWidth(31, 5);
		makeExcel.setWidth(32, 13);
		makeExcel.setWidth(33, 14);
		makeExcel.setWidth(34, 6);
		makeExcel.setWidth(35, 33);
		makeExcel.setWidth(36, 9);
	}

}
