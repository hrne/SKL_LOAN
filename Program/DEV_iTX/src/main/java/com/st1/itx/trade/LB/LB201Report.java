package com.st1.itx.trade.LB;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LB201ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.MakeFile;

import java.util.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

@Component("lB201Report")
@Scope("prototype")

public class LB201Report extends MakeReport {
	// private static final Logger logger = LoggerFactory.getLogger(LB201Report.class);

	Date dateNow = new Date();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	String strToday = String.valueOf(Integer.parseInt(sdf.format(dateNow)) - 19110000); // 民國年月日
	String strTodayMM = strToday.substring(3,5); // 月
	String strTodaydd= strToday.substring(5,7);  // 日
	int listCount = 0;

	@Autowired
	LB201ServiceImpl lB201ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	@Autowired
	public MakeFile makeFile;

	// 自訂明細標題
	@Override
	public void printTitle() {
		this.info("printTitle nowRow = " + this.NowRow);
	}

	public void exec(TitaVo titaVo) throws LogicException {
		// LB201 聯徵授信餘額月報檔
		try {
			this.info("-----strToday=" + strToday);
			this.info("-----strTodayMM=" + strTodayMM);
			this.info("-----strTodaydd=" + strTodaydd);
			
			List<HashMap<String, String>> LBList = lB201ServiceImpl.findAll(titaVo);
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
			// genExcel(titaVo, LBList);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LB201ServiceImpl.findAll error = " + errors.toString());
		}
	}

	private void genFile(TitaVo titaVo, List<HashMap<String, String>> LBList) throws LogicException {
		this.info("=========== LB201 genFile : ");
		String txt = "F0;F1;F2;F3;F4;F5;F6;F7;F8;F9;F10;F11;F12;F13;F14;F15;F16;F17;F18;F19;" +
			      	 "F20;F21;F22;F23;F24;F25;F26;F27;F28;F29;F30;F31;F32;F33;F34;F35;F36;F37;F38;F39;" +
			      	 "F40;F41;F42;F43;F44;F45;F46;F47;F48;F49;F50;F51;F52;F53;F54;F55;F56;F57;F58;F59;" +
			      	 "F60;F61;F62;F63;F64;F65;F66;F67;F68;F69;F70;F71;F72;F73;F74;F75;F76;F77;F78;F79;" +
			      	 "F80;F81;F82;F83;F84;F85;F86;F87;F88;F89;F90";
		String txt1[] = txt.split(";");

		try {
			int sumDrawdownAmt = 0;		// 訂約金額(台幣)
			int sumUnDelayBal = 0;		// 未逾期/乙類逾期/應予觀察授信餘額(台幣)
			int sumDelayBal = 0;		// 逾期未還餘額（台幣）
			String strContent = "";
			DecimalFormat formatter = new DecimalFormat("0");
					
			String strFileName = "458"+ strTodayMM + strTodaydd + "1" + ".201";		// 458+月日+序號(1).201
			makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "B201", "聯徵授信餘額月報檔", strFileName, 2);

			// 首筆
			strContent = "JCIC-DAT-B201-V01-458" + StringUtils.repeat(" ",5) + strToday + "01" + StringUtils.repeat(" ",10) 
					   + makeFile.fillStringR("審查聯絡人－許高政", 20, ' ') + makeFile.fillStringR("02-23895858#7067", 16, ' ') 
					   + makeFile.fillStringR("", 20, ' ') + makeFile.fillStringR("", 16, ' ') 
					   + StringUtils.repeat(" ", 80) + StringUtils.repeat(" ", 315) ;
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
							case 1 :	strField = makeFile.fillStringL(strField, 3, '0');			break;
							case 2 :	strField = makeFile.fillStringL(strField, 4, '0');			break;
							case 3 :	strField = makeFile.fillStringR(strField, 1, ' ');			break;
							case 4 :	strField = makeFile.fillStringR(strField, 1, ' ');			break;
							case 5 :	strField = makeFile.fillStringR(strField,50, ' ');			break;
							case 6 :	strField = makeFile.fillStringL(strField,10, '0');			break;
							case 7 :	strField = makeFile.fillStringR(strField,10, ' ');			break;
							case 8 :	strField = makeFile.fillStringR(strField, 1, ' ');			break;
							case 9 :	strField = makeFile.fillStringR(strField,10, ' ');			break;
							case 10:	strField = makeFile.fillStringR(strField, 1, ' ');			break;
							case 11:	strField = makeFile.fillStringR(strField,10, ' ');			break;
							case 12:	strField = makeFile.fillStringL(strField, 6, '0');			break;
							case 13:	strField = makeFile.fillStringR(strField, 3, ' ');			break;
							case 14:	strField = makeFile.fillStringR(strField, 1, ' ');			break;
							case 15:	strField = makeFile.fillStringR(strField, 1, ' ');			break;
							case 16:	strField = makeFile.fillStringR(strField, 1, ' ');			break;
							case 17:	strField = makeFile.fillStringR(strField, 1, ' ');			break;
							case 18:	strField = makeFile.fillStringR(strField, 1, ' ');			break;
							case 19:	strField = makeFile.fillStringR(strField, 2, ' ');			break;
							case 20:	strField = makeFile.fillStringR(strField, 1, ' ');			break;
							case 21:	strField = makeFile.fillStringR(strField, 1, ' ');			break;
							case 22:	formatter.applyPattern("00.00000");
										strField = formatter.format(Float.parseFloat(strField));	break;
							case 23:	strField = makeFile.fillStringL(strField, 5, '0');			break;
							case 24:	strField = makeFile.fillStringL(strField, 5, '0');			break;
							case 25:	strField = makeFile.fillStringR(strField, 3, ' ');			break;
							case 26:	strField = makeFile.fillStringL(strField,10, '0');			
										sumDrawdownAmt = sumDrawdownAmt + Integer.parseInt(strField);
										break;
							case 27:	strField = makeFile.fillStringL(strField,10, '0');			break;
							case 28:	strField = makeFile.fillStringR(strField, 1, ' ');			break;
							case 29:	strField = makeFile.fillStringR(strField, 1, ' ');			break;
							case 30:	strField = makeFile.fillStringR(strField,50, ' ');			break;
							case 31:	strField = makeFile.fillStringL(strField,10, '0');
										sumUnDelayBal = sumUnDelayBal + Integer.parseInt(strField);
										break;
							case 32:	strField = makeFile.fillStringL(strField,10, '0');			break;
							case 33:	strField = makeFile.fillStringL(strField,10, '0');
										sumDelayBal = sumDelayBal + Integer.parseInt(strField);
										break;
							case 34:	strField = makeFile.fillStringL(strField,10, '0');			break;
							case 35:	strField = makeFile.fillStringR(strField, 1, ' ');			break;
							case 36:	strField = makeFile.fillStringR(strField, 1, ' ');			break;
							case 37: // 本月(累計)應繳金額 (255~267)
										if (Float.parseFloat(strField) < 0) {	// 本月(累計)應繳金額
											formatter.applyPattern("00000000.000");		// 數值會自動補-,所以格式少一位
											strField = formatter.format(Float.parseFloat(strField));
										// } else if (Float.parseFloat(strField) == 0) {
										//		strField = makeFile.fillStringL("0", 13, '0');
										} else {
											formatter.applyPattern("000000000.000");
											strField = formatter.format(Float.parseFloat(strField));
										}
										break;							
							case 38: // 本月收回本金 (268~278)
										if (Float.parseFloat(strField) < 0) {
											formatter.applyPattern("000000.000");
											strField = formatter.format(Float.parseFloat(strField));
										// } else if (Float.parseFloat(strField) == 0) {
										//		strField = makeFile.fillStringL("0", 11, '0');
										} else {
											formatter.applyPattern("0000000.000");
											strField = formatter.format(Float.parseFloat(strField));
										}
										break;							
							case 39: // 本月收取利息 (279~289)
										if (Float.parseFloat(strField) < 0) {
											formatter.applyPattern("000000.000");
											strField = formatter.format(Float.parseFloat(strField));
										// } else if (Float.parseFloat(strField) == 0) {
										//		strField = makeFile.fillStringL("0", 11, '0');
										} else {
											formatter.applyPattern("0000000.000");
											strField = formatter.format(Float.parseFloat(strField));
										}
										break;							
							case 40: // 本月收取其他費用 (290~300) 
										if (Float.parseFloat(strField) < 0) {
											formatter.applyPattern("000000.000");
											strField = formatter.format(Float.parseFloat(strField));
										// } else if (Float.parseFloat(strField) == 0) {
										//		strField = makeFile.fillStringL("0", 11, '0');
										} else {
											formatter.applyPattern("0000000.000");
											strField = formatter.format(Float.parseFloat(strField));
										}
										break;							
							case 41:	strField = makeFile.fillStringR(strField, 3, ' ');			break;
							case 42:	strField = makeFile.fillStringR(strField, 1, ' ');			break;
							case 43:	strField = makeFile.fillStringR(strField, 3, ' ');			break;
							case 44:	strField = makeFile.fillStringR(strField, 3, ' ');			break;
							case 45:	strField = makeFile.fillStringR(strField,10, ' ');			break;
							case 46: 	strField = makeFile.fillStringR(strField,14, ' ');			break;
							case 47:	strField = makeFile.fillStringR(strField, 5, ' ');			break;
							case 48:	strField = makeFile.fillStringR(strField, 6, ' ');			break;
							case 49:	strField = makeFile.fillStringR(strField, 1, ' ');			break;
							case 50: // 擔保品(合計)鑑估值(347~356)
										if (strField.trim().equals("*")) {
					  	 	  	  			strField = makeFile.fillStringR(strField,10, ' ');
					  	 	  	  		} else {
					  	 	  	  			strField = makeFile.fillStringL(strField,10, '0');
					  	 	  	  		}
					  	 	  	  		break;
							case 51:	strField = makeFile.fillStringR(strField, 2, ' ');			break;
							case 52:	strField = makeFile.fillStringR(strField, 1, ' ');			break;
							case 53:	strField = makeFile.fillStringR(strField, 8, ' ');			break;
							case 54: // 聯貸參貸比例(368~371)
										if (strField.trim().equals("*")) {
											strField = makeFile.fillStringR(strField, 4, ' ');
										} else if (strField.trim().equals("0")) {
											strField = StringUtils.repeat(" ", 4);
										} else {
											strField = makeFile.fillStringL(strField, 4, '0');
										}
										break;
							case 55:	strField = makeFile.fillStringR(strField, 2, ' ');			break;
							case 56:	strField = makeFile.fillStringR(strField, 6, ' ');			break;
							case 57:	strField = makeFile.fillStringR(strField, 1, ' ');			break;
							case 58:	strField = makeFile.fillStringR(strField, 1, ' ');			break;
							case 59:	strField = makeFile.fillStringR(strField, 1, ' ');			break;
							case 60:	strField = makeFile.fillStringR(strField, 1, ' ');			break;
							case 61:	strField = makeFile.fillStringR(strField,10, ' ');			break;
							case 62:	strField = makeFile.fillStringR(strField, 1, ' ');			break;
							case 63:	strField = makeFile.fillStringR(strField, 2, ' ');			break;
							case 64:	strField = makeFile.fillStringR(strField, 1, ' ');			break;
							case 65:	strField = makeFile.fillStringR(strField,10, ' ');			break;
							case 66:	strField = makeFile.fillStringR(strField, 1, ' ');			break;
							case 67:	strField = makeFile.fillStringR(strField, 2, ' ');			break;
							case 68:	strField = makeFile.fillStringR(strField, 1, ' ');			break;
							case 69:	strField = makeFile.fillStringR(strField,10, ' ');			break;
							case 70:	strField = makeFile.fillStringR(strField, 1, ' ');			break;
							case 71:	strField = makeFile.fillStringR(strField, 2, ' ');			break;
							case 72:	strField = makeFile.fillStringR(strField, 1, ' ');			break;
							case 73:	strField = makeFile.fillStringR(strField,10, ' ');			break;
							case 74:	strField = makeFile.fillStringR(strField, 1, ' ');			break;
							case 75:	strField = makeFile.fillStringR(strField, 2, ' ');			break;
							case 76:	strField = makeFile.fillStringR(strField, 1, ' ');			break;
							case 77:	strField = makeFile.fillStringR(strField,10, ' ');			break;
							case 78:	strField = makeFile.fillStringR(strField, 1, ' ');			break;
							case 79:	strField = makeFile.fillStringR(strField, 2, ' ');			break;
							case 80:	strField = makeFile.fillStringR(strField, 5, ' ');			break;
							case 81:	strField = makeFile.fillStringR(strField,15, ' ');			break;
							case 82: // 呆帳轉銷年月(473~477)
										if (strField.trim().equals("0")) {
											strField = StringUtils.repeat(" ",5);
										} else {
											strField = makeFile.fillStringL(strField, 5, '0');
										}
										break;
							case 83:	strField = makeFile.fillStringR(strField, 5, ' ');			break;
							case 84: // 破產宣告日(或法院裁定開始清算日)(483~489)
										if (strField.trim().equals("0")) {
											strField = StringUtils.repeat(" ", 7);
										} else {
											strField = makeFile.fillStringL(strField, 7, '0');
										}
										break;
							case 85:	strField = makeFile.fillStringR(strField, 1, ' ');			break;
							case 86:	strField = makeFile.fillStringL(strField, 4, '0');			break;
							case 87:	strField = makeFile.fillStringR(strField, 1, ' ');			break;
							case 88:	strField = makeFile.fillStringR(strField, 2, ' ');			break;
							case 89:	strField = makeFile.fillStringR(strField, 9, ' ');			break;
							case 90:	strField = makeFile.fillStringL(strField, 5, '0');			break;
							case 91:	strField = makeFile.fillStringR(strField, 1, ' ');			break;
							default:	strField = "";                                              break;
						}
						strContent = strContent + strField;
					}
					
					// 不良債權特別處理(305~310)  金額格式 (比照原SKL申報檔)
//					if ( !strContent.substring(304, 310).trim().equals("")) {
//						if(strContent.substring(289, 300).equals("0000000.000")) {           // 本月收取其他費用(290~300)
//							strContent = strContent.substring(0, 289) + "00000000000" + strContent.substring(300);
//						}
//					}
					
					
					makeFile.put(strContent);
				}
			}

			// 末筆
			strContent = "TRLR" + StringUtils.repeat(" ",3)
					   + makeFile.fillStringL(String.valueOf(sumDrawdownAmt), 13, '0')  // 訂約金額(台幣)
					   + makeFile.fillStringL(String.valueOf(sumUnDelayBal), 12, '0') 	// 未逾期/乙類逾期/應予觀察授信餘額(台幣)
					   + makeFile.fillStringL(String.valueOf(sumDelayBal), 12, '0')  	// 逾期未還餘額（台幣）
					   + StringUtils.repeat(" ",3)
					   + makeFile.fillStringL(String.valueOf(listCount), 9, '0') 
					   + StringUtils.repeat(" ",456);
			makeFile.put(strContent);

			long sno = makeFile.close();
			// makeFile.toFile(sno);	// 不直接下傳

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LB201ServiceImpl.genFile error = " + errors.toString());
		}
	}

	private void genExcel(TitaVo titaVo, List<HashMap<String, String>> LBList) throws LogicException {
		this.info("=========== LB201 genExcel: ");
		this.info("LB201 genExcel TitaVo=" + titaVo);

		// 自訂標題 inf (首筆/尾筆)
		String inf = "";
		String infLast = "";
		String txt = "";

		// B201 聯徵授信餘額月報檔
		inf = "總行代號(1~3);分行代號(4~7);交易代碼(8~8);帳號屬性註記(9~9);本筆撥款帳號(10~59);金額合計(60~69);授信戶IDN/BAN(70~79);上欄IDN或BAN錯誤註記(80~80);負責人IDN/負責之事業體BAN(81~90);上欄IDN或BAN錯誤註記(91~91);外僑兼具中華民國國籍IDN(92~101);授信戶行業別(102~107);空白(108~110);科目別(111~111);科目別註記(112~112);轉催收款(或呆帳)前原科目別(113~113);個人消費性貸款註記(114~114);融資分類(115~115);政府專業補助貸款分類(116~117);不計入授信項目(118~118);用途別(119~119);本筆撥款利率(120~127);本筆撥款開始年月(128~132);本筆撥款約定清償年月(133~137);授信餘額幣別(138~140);訂約金額(台幣)(141~150);訂約金額(外幣)(151~160);循環信用註記(161~161);額度可否撤銷(162~162);上階共用額度控制編碼(163~212);未逾期/乙類逾期/應予觀察授信餘額(台幣)(213~222);未逾期/乙類逾期/應予觀察授信餘額(外幣)(223~232);逾期未還餘額（台幣）(233~242);逾期未還餘額（外幣）(243~252);逾期期限(253~253);本月還款紀錄(254~254);本月（累計）應繳金額(255~267);本月收回本金(268~278);本月收取利息(279~289);本月收取其他費用(290~300);甲類逾期放款分類(301~303);乙類逾期放款分類(304~304);不良債權處理註記(305~307);債權結束註記(308~310);債權處理後新債權人ID/債權轉讓後前手債權人ID/信保基金退理賠信用保證機構BAN(311~320);債權處理案號(321~334);債權轉讓年月/債權轉讓後原債權機構買回年月(335~339);空白(340~345);擔保品組合型態(346~346);擔保品(合計)鑑估值(347~356);擔保品類別(357~358);國內或國際連貸(359~359);聯貸合約訂定日期(360~367);聯貸參貸比例(368~371);空白(372~373);空白(374~379);代放款註記(380~380);債務協商註記(381~381);空白(382~382);共同債務人或債務關係人身份代號1(383~383);共同債務人或債務關係人身份統一編號1(384~393);上欄IDN或BAN錯誤註記(394~394);與主債務人關係1(395~396);共同債務人或債務關係人身份代號2(397~397);共同債務人或債務關係人身份統一編號2(398~407);上欄IDN或BAN錯誤註記(408~408);與主債務人關係2(409~410);共同債務人或債務關係人身份代號3(411~411);共同債務人或債務關係人身份統一編號3(412~421);上欄IDN或BAN錯誤註記(422~422);與主債務人關係3(423~424);共同債務人或債務關係人身份代號4(425~425);共同債務人或債務關係人身份統一編號4(426~435);上欄IDN或BAN錯誤註記(436~436);與主債務人關係4(437~438);共同債務人或債務關係人身份代號5(439~439);共同債務人或債務關係人身份統一編號5(440~449);上欄IDN或BAN錯誤註記(450~450);與主債務人關係5(451~452);空白(453~457);空白(458~472);呆帳轉銷年月(473~477);聯貸主辦(管理)行註記(478~482);破產宣告日(或法院裁定開始清算日)(483~489);建築貸款註記(490~490);授信餘額列報1（千元）之原始金額（元）(491~494);補充揭露案件註記－案件屬性(495~495);補充揭露案件註記－案件情形(496~497);空白(498~506);資料所屬年月(507~511);資料結束註記(512~512)";
		txt = "F0;F1;F2;F3;F4;F5;F6;F7;F8;F9;F10;F11;F12;F13;F14;F15;F16;F17;F18;F19;" +
		      "F20;F21;F22;F23;F24;F25;F26;F27;F28;F29;F30;F31;F32;F33;F34;F35;F36;F37;F38;F39;" +
		 	  "F40;F41;F42;F43;F44;F45;F46;F47;F48;F49;F50;F51;F52;F53;F54;F55;F56;F57;F58;F59;" +
		 	  "F60;F61;F62;F63;F64;F65;F66;F67;F68;F69;F70;F71;F72;F73;F74;F75;F76;F77;F78;F79;" +
		 	  "F80;F81;F82;F83;F84;F85;F86;F87;F88;F89;F90";

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
			String strFileName = "458"+ strTodayMM + strTodaydd + "1" + ".201";		// 458+月日+序號(1).201
			this.info("------------titaVo.getEntDyI()=" + titaVo.getEntDyI());
			this.info("------------titaVo.getKinbr()=" + titaVo.getKinbr());
			makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "B201", "授信額度資料檔", strFileName);

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
			this.info("LB201ServiceImpl.genExcel error = " + errors.toString());
		}
	}

	private void setAllWidth() throws LogicException {
		this.info("=========== setAllWidth()");
		makeExcel.setWidth(1,   7);
		makeExcel.setWidth(2,   8);
		makeExcel.setWidth(3,   5);
		makeExcel.setWidth(4,   5);
		makeExcel.setWidth(5,  54);
		makeExcel.setWidth(6,  14);
		makeExcel.setWidth(7,  14);
		makeExcel.setWidth(8,   5);
		makeExcel.setWidth(9,  14);
		makeExcel.setWidth(10,  5);
		makeExcel.setWidth(11, 14);
		makeExcel.setWidth(12, 10);
		makeExcel.setWidth(13,  7);
		makeExcel.setWidth(14,  5);
		makeExcel.setWidth(15,  5);
		makeExcel.setWidth(16,  5);
		makeExcel.setWidth(17,  5);
		makeExcel.setWidth(18,  5);
		makeExcel.setWidth(19,  6);
		makeExcel.setWidth(20,  5);
		makeExcel.setWidth(21,  5);
		makeExcel.setWidth(22, 12);
		makeExcel.setWidth(23,  9);
		makeExcel.setWidth(24,  9);
		makeExcel.setWidth(25,  7);
		makeExcel.setWidth(26, 14);
		makeExcel.setWidth(27, 14);
		makeExcel.setWidth(28,  5);
		makeExcel.setWidth(29,  5);
		makeExcel.setWidth(30, 54);
		makeExcel.setWidth(31, 14);
		makeExcel.setWidth(32, 14);
		makeExcel.setWidth(33, 14);
		makeExcel.setWidth(34, 14);
		makeExcel.setWidth(35,  5);
		makeExcel.setWidth(36,  5);
		makeExcel.setWidth(37, 17);
		makeExcel.setWidth(38, 15);
		makeExcel.setWidth(39, 15);
		makeExcel.setWidth(40, 15);
		makeExcel.setWidth(41,  7);
		makeExcel.setWidth(42,  5);
		makeExcel.setWidth(43,  7);
		makeExcel.setWidth(44,  7);
		makeExcel.setWidth(45, 14);
		makeExcel.setWidth(46, 18);
		makeExcel.setWidth(47,  9);
		makeExcel.setWidth(48, 10);
		makeExcel.setWidth(49,  5);
		makeExcel.setWidth(50, 14);
		makeExcel.setWidth(51,  6);
		makeExcel.setWidth(52,  5);
		makeExcel.setWidth(53, 12);
		makeExcel.setWidth(54,  8);
		makeExcel.setWidth(55,  6);
		makeExcel.setWidth(56, 10);
		makeExcel.setWidth(57,  5);
		makeExcel.setWidth(58,  5);
		makeExcel.setWidth(59,  5);
		makeExcel.setWidth(60,  5);
		makeExcel.setWidth(61, 14);
		makeExcel.setWidth(62,  5);
		makeExcel.setWidth(63,  6);
		makeExcel.setWidth(64,  5);
		makeExcel.setWidth(65, 14);
		makeExcel.setWidth(66,  5);
		makeExcel.setWidth(67,  6);
		makeExcel.setWidth(68,  5);
		makeExcel.setWidth(69, 14);
		makeExcel.setWidth(70,  5);
		makeExcel.setWidth(71,  6);
		makeExcel.setWidth(72,  5);
		makeExcel.setWidth(73, 14);
		makeExcel.setWidth(74,  5);
		makeExcel.setWidth(75,  6);
		makeExcel.setWidth(76,  5);
		makeExcel.setWidth(77, 14);
		makeExcel.setWidth(78,  5);
		makeExcel.setWidth(79,  6);
		makeExcel.setWidth(80,  9);
		makeExcel.setWidth(81, 19);
		makeExcel.setWidth(82,  9);
		makeExcel.setWidth(83,  9);
		makeExcel.setWidth(84, 11);
		makeExcel.setWidth(85,  5);
		makeExcel.setWidth(86,  8);
		makeExcel.setWidth(87,  5);
		makeExcel.setWidth(88,  6);
		makeExcel.setWidth(89, 13);
		makeExcel.setWidth(90,  9);
		makeExcel.setWidth(91,  5);
	}
	
}
