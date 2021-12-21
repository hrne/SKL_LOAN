package com.st1.itx.trade.L8;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.L8402ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.MakeFile;

import java.util.Date;
import java.text.SimpleDateFormat;

@Component("L8402Report")
@Scope("prototype")

public class L8402Report extends MakeReport {
	Date dateNow = new Date();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	String strToday = String.valueOf(Integer.parseInt(sdf.format(dateNow)) - 19110000); // 民國年

	// private static final Logger logger =
	// LoggerFactory.getLogger(L8402Report.class);

	@Autowired
	public L8402ServiceImpl L8402ServiceImpl;

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
		// 設定資料庫(必須的)
		for (int fg = 1; fg <= 12; fg++) { // 總報表數
			try {
				genExcel(titaVo, fg);
			} catch (Exception e) {
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				this.info("L8402ServiceImpl.findAll (" + fg + ") error = " + errors.toString());
			}
		}
	}

	// 以下新系統［需求規格書］未列入
	// fg=13: B086 聯貸合約各參貸機構參貸比例資料檔；<br>
	// fg=14: B091 有價證券(股票除外)擔保品明細檔；<br>

	/**
	 * 製檔<br>
	 * 
	 * @param titaVo titaVo
	 * @param fg     fg=1: B201 聯徵授信餘額月報檔；<br>
	 *               fg=2: B207 授信戶基本資料檔；<br>
	 *               fg=3: B080 授信額度資料檔；<br>
	 *               fg=4: B085 帳號轉換資料檔；<br>
	 *               fg=5: B087 聯貸案首次動撥後６個月內發生違約之實際主導金融機構註記檔；<br>
	 *               fg=6: B090 擔保品關聯檔資料檔；<br>
	 *               fg=7: B092 不動產擔保品明細檔；<br>
	 *               fg=8: B093 動產及貴重物品擔保品明細檔；<br>
	 *               fg=9: B094 股票擔保品明細檔；<br>
	 *               fg=10: B095 不動產擔保品明細-建號附加檔；<br>
	 *               fg=11: B096 不動產擔保品明細-地號附加檔；<br>
	 *               fg=12: B680 「貸款餘額(擔保放款餘額加上部分擔保、副擔保貸款餘額)扣除擔保品鑑估值」之金額資料檔；<br>
	 * @throws LogicException LogicException
	 */
	private void genExcel(TitaVo titaVo, int fg) throws LogicException {
		this.info("=========== L8402 genExcel: " + fg);
		this.info("L8402 genExcel TitaVo=" + titaVo);

		// 自訂標題 inf (首筆/尾筆)
		String inf = "";
		String infLast = "";
		String txt = "";

		if (fg == 1) { // B201
			inf = "總行代號(1~3);分行代號(4~7);交易代碼(8~8);帳號屬性註記(9~9);本筆撥款帳號(10~59);金額合計(60~69);授信戶IDN/BAN(70~79);上欄IDN或BAN錯誤註記(80~80);負責人IDN/負責之事業體BAN(81~90);上欄IDN或BAN錯誤註記(91~91);外僑兼具中華民國國籍IDN(92~101);授信戶行業別(102~107);空白(108~110);科目別(111~111);科目別註記(112~112);轉催收款(或呆帳)前原科目別(113~113);個人消費性貸款註記(114~114);融資分類(115~115);政府專業補助貸款分類(116~117);不計入授信項目(118~118);用途別(119~119);本筆撥款利率(120~126);本筆撥款開始年月(127~131);本筆撥款約定清償年月(132~136);授信餘額幣別(137~139);訂約金額(台幣)(140~149);訂約金額(外幣)(150~159);循環信用註記(160~160);額度可否撤銷(161~161);上階共用額度控制編碼(162~211);未逾期/乙類逾期/應予觀察授信餘額(台幣)(212~221);未逾期/乙類逾期/應予觀察授信餘額(外幣)(222~231);逾期未還餘額（台幣）(232~241);逾期未還餘額（外幣）(242~251);逾期期限(252~252);本月還款紀錄(253~253);本月（累計）應繳金額(254~269);本月收回本金(270~285);本月收取利息(286~301);本月收取其他費用(302~317);甲類逾期放款分類(318~318);乙類逾期放款分類(319~319);不良債權處理註記(320~322);債權結束註記(323~325);債權處理後新債權人ID/債權轉讓後前手債權人ID/信保基金退理賠信用保證機構BAN(326~335);債權處理案號(336~349);債權轉讓年月/債權轉讓後原債權機構買回年月(350~354);空白(355~360);擔保品組合型態(361~361);擔保品(合計)鑑估值(362~371);擔保品類別(372~373);國內或國際連貸(374~374);聯貸合約訂定日期(375~382);聯貸參貸比例(383~387);空白(388~389);空白(390~395);代放款註記(396~396);債務協商註記(397~397);空白(398~398);共同債務人或債務關係人身份代號1(399~399);共同債務人或債務關係人身份統一編號1(400~409);上欄IDN或BAN錯誤註記(410~410);與主債務人關係1(411~412);共同債務人或債務關係人身份代號2(413~413);共同債務人或債務關係人身份統一編號2(414~423);上欄IDN或BAN錯誤註記(424~424);與主債務人關係2(425~426);共同債務人或債務關係人身份代號3(427~427);共同債務人或債務關係人身份統一編號3(428~437);上欄IDN或BAN錯誤註記(438~438);與主債務人關係3(439~440);共同債務人或債務關係人身份代號4(441~441);共同債務人或債務關係人身份統一編號4(442~451);上欄IDN或BAN錯誤註記(452~452);與主債務人關係4(453~454);共同債務人或債務關係人身份代號5(455~455);共同債務人或債務關係人身份統一編號5(456~465);上欄IDN或BAN錯誤註記(466~466);與主債務人關係5(467~468);空白(469~478);空白(479~488);呆帳轉銷年月(489~493);聯貸主辦(管理)行註記(494~498);破產宣告日(或法院裁定開始清算日)(499~505);建築貸款註記(506~506);授信餘額列報1（千元）之原始金額（元）(507~510);補充揭露案件註記－案件屬性(511~511);補充揭露案件註記－案件情形(512~513);空白(514~522);資料所屬年月(523~527);資料結束註記(528~528)";
			txt = "F0;F1;F2;F3;F4;F5;F6;F7;F8;F9;F10;F11;F12;F13;F14;F15;F16;F17;F18;F19;" + "F20;F21;F22;F23;F24;F25;F26;F27;F28;F29;F30;F31;F32;F33;F34;F35;F36;F37;F38;F39;"
					+ "F40;F41;F42;F43;F44;F45;F46;F47;F48;F49;F50;F51;F52;F53;F54;F55;F56;F57;F58;F59;" + "F60;F61;F62;F63;F64;F65;F66;F67;F68;F69;F70;F71;F72;F73;F74;F75;F76;F77;F78;F79;"
					+ "F80;F81;F82;F83;F84;F85;F86;F87;F88;F89;F90";
		}
		if (fg == 2) { // B207
			inf = "交易代碼(1~1);總行代號(2~4);空白(5~8);資料日期(9~15);授信戶IDN(16~25);中文姓名(26~55);英文姓名(56~75);出生日期(76~82);戶籍地址(83~181);聯絡地址郵遞區號(182~186);聯絡地址(187~285);聯絡電話(286~315);行動電話(316~331);空白(332~336);教育程度代號(337~337);自有住宅有無(338~338);任職機構名稱(339~383);任職機構統一編號(384~391);職業類別(392~397);任職機構電話(398~413);職位名稱(414~428);服務年資(429~430);年收入(431~436);年收入資料年月(437~441);性別(442~442);國籍(443~444);護照號碼(445~464);舊有稅籍編號(465~474);中文姓名超逾10個字之全名(475~574);空白(575~610)";
			txt = "F0;F1;F2;F3;F4;F5;F6;F7;F8;F9;F10;F11;F12;F13;F14;F15;F16;F17;F18;F19;" + "F20;F21;F22;F23;F24;F25;F26;F27;F28;F29";
		}
		if (fg == 3) { // B080
			inf = "資料別(1~2);總行代號(3~5);分行代號(6~9);交易代碼(10~10);空白(11~14);授信戶IDN/BAN(15~24);本階共用額度控制編碼(25~74);授信幣別(75~77);本階訂約金額(台幣)(78~87);本階訂約金額(外幣)(88~97);本階額度開始年月(98~102);本階額度約定截止年月(103~107);循環信用註記(108~108);額度可否撤銷(109~109);上階共用額度控制編碼(110~159);科目別(160~160);科目別註記(161~161);擔保品類別(162~163);空白(164~187);資料所屬年月(188~192)";
			txt = "F0;F1;F2;F3;F4;F5;F6;F7;F8;F9;F10;F11;F12;F13;F14;F15;F16;F17;F18;F19";
		}
		if (fg == 4) { // B085
			inf = "資料別(1~2);轉換帳號年月(3~7);授信戶IDN/BAN(8~17);轉換前總行代號(18~20);轉換前分行代號(21~24);空白(25~26);轉換前帳號(27~76);轉換後總行代號(77~79);轉換後分行代號(80~83);空白(84~85);轉換後帳號(86~135);空白(136~160)";
			txt = "F0;F1;F2;F3;F4;F5;F6;F7;F8;F9;F10;F11";
		}
		if (fg == 5) { // B087
			inf = "";
			txt = "";
		}
		if (fg == 6) { // B090
			inf = "資料別(1~2);總行代號(3~5);分行代號(6~9);空白(10~11);授信戶IDN/BAN(12~21);擔保品控制編碼(22~71);額度控制編碼(72~121);海外不動產擔保品資料註記(122~123);資料所屬年月(124~128)";
			txt = "F0;F1;F2;F3;F4;F5;F6;F7;F8";
		}
		if (fg == 7) { // B092
			inf = "資料別(1~2);總行代號(3~5);分行代號(6~9);空白(10~11);擔保品控制編碼(12~61);擔保品類別(62~63);擔保品所有權人或代表人IDN/BAN(64~73);鑑估(總市)值(74~81);鑑估日期(82~86);可放款值(87~94);設定日期(95~99);本行本月設定金額(100~107);本行設定抵押順位(108~108);本行累計已設定總金額(109~116);其他債權人已設定金額(117~124);處分價格(125~132);權利到期年月(133~137);縣市別(138~138);鄉鎮市區別(139~140);段、小段號(141~144);地號-前四碼(145~148);地號-後四碼(149~152);建號-前五碼(153~157);建號-後三碼(158~160);郵遞區號(161~165);是否有保險(166~166);預估應計土地增值稅(167~174);應計土地增值稅之預估年月(175~179);買賣契約價格(180~187);買賣契約日期(188~194);停車位形式(195~195);車位單獨登記面積(196~204);土地持份面積(205~214);建物類別(215~216);空白(217~245);資料所屬年月(246~250)";
			txt = "F0;F1;F2;F3;F4;F5;F6;F7;F8;F9;F10;F11;F12;F13;F14;F15;F16;F17;F18;F19;" + "F20;F21;F22;F23;F24;F25;F26;F27;F28;F29;F30;F31;F32;F33;F34;F35";
		}
		if (fg == 8) { // B093
			inf = "資料別(1~2);總行代號(3~5);分行代號(6~9);空白(10~11);擔保品控制編碼(12~61);擔保品類別(62~63);擔保品所有權人或代表人IDN/BAN(64~73);鑑估值(74~81);鑑估日期(82~86);可放款值(87~94);設定日期(95~99);本行本月設定金額(100~107);本月設定抵押順位(108~108);本行累計已設定總金額(109~116);其他債權人前已設定金額(117~124);處分價格(125~132);權利到期年月(133~137);是否有保險(138~138);空白(139~155);資料所屬年月(156~160)";
			txt = "F0;F1;F2;F3;F4;F5;F6;F7;F8;F9;F10;F11;F12;F13;F14;F15;F16;F17;F18;F19";
		}
		if (fg == 9) { // B094
			inf = "資料別(1~2);總行代號(3~5);分行代號(6~9);空白(10~11);擔保品控制編碼(12~61);擔保品類別(62~63);擔保品所有權人或代表人IDN/BAN(64~73);鑑估值(74~81);鑑估日期(82~88);可放款值(89~98);設質日期(99~105);發行機構BAN(106~113);發行機構所在國別(114~115);股票代號(116~125);股票種類(126~126);幣別(127~129);設定股數餘額(130~143);股票質押授信餘額(144~153);公司內部人職稱(154~154);公司內部人身分註記(155~155);公司內部人法定關係人(156~165);處分價格(166~173);空白(174~187);資料所屬年月(188~192)";
			txt = "F0;F1;F2;F3;F4;F5;F6;F7;F8;F9;F10;F11;F12;F13;F14;F15;F16;F17;F18;F19;F20;F21;F22;F23";
		}
		if (fg == 10) { // B095
			inf = "資料別(1~2);總行代號(3~5);分行代號(6~9);空白(10~11);擔保品控制編碼(12~61);擔保品所有權人或代表人IDN/BAN(62~71);縣市別(72~72);鄉鎮市區別(73~74);段、小段號(75~78);建號-前五碼(79~83);建號-後三碼(84~86);縣市名稱(87~98);鄉鎮市區名稱(99~110);村里/街路/段/巷/弄/號/樓(111~186);主要用途(187~187);主要建材(結構體)(188~188);附屬建物用途(189~194);層數(標的所在樓高)(195~197);層次(標的所在樓層)(198~204);建築完成日期(屋齡)(205~211);建物總面積(212~221);主建物(層次)面積(222~231);附屬建物面積(232~241);共同部份持分面積(242~251);空白(252~295);資料所屬年月(296~300)";
			txt = "F0;F1;F2;F3;F4;F5;F6;F7;F8;F9;F10;F11;F12;F13;F14;F15;F16;F17;F18;F19;F20;F21;F22;F23;F24;F25";
		}
		if (fg == 11) { // B096
			inf = "資料別(1~2);總行代號(3~5);分行代號(6~9);空白(10~11);擔保品控制編碼(12~61);擔保品所有權人或代表人IDN/BAN(62~71);縣市別(72~72);鄉鎮市區別(73~74);段、小段號(75~78);地號-前四碼(79~82);地號-後四碼(83~86);地目(87~87);面積(88~97);使用分區(98~98);使用地類別(99~100);公告土地現值(101~110);公告土地現值年月(111~115);空白(116~145);資料所屬年月(146~150)";
			txt = "F0;F1;F2;F3;F4;F5;F6;F7;F8;F9;F10;F11;F12;F13;F14;F15;F16;F17;F18";
		}
		if (fg == 12) { // B680
			inf = "總行代號(1~3);分行代號(4~7);交易代碼(8~8);授信戶IDN/BAN(9~18);上欄IDN或BAN錯誤註記(19~19);空白(20~59);貸款餘額扣除擔保品鑑估值之金額(60~69);資料所屬年月(70~74);空白(75~128)";
			txt = "F0;F1;F2;F3;F4;F5;F6;F7;F8";
		}
		// 以下新系統〔需求規格書〕未列入
		/*
		 * if (fg == 13) { // B086 inf = "" ; txt = "" ; } if (fg == 14) { // B091 inf =
		 * ""; txt = "F0;F1;F2;F3;F4;F5;F6;F7;F8;F9;F10;F11;F12;F13;F14;F15;F16;F17;F18"
		 * ; }
		 */

		// 自訂標題列寫入記號
		boolean infFg = true;
		if ((inf.trim()).equals("")) {
			infFg = false;
		}

		String inf1[] = inf.split(";");
		String txt1[] = txt.split(";");

		try {
			List<HashMap<String, String>> L8List = null;
			if (fg != 5) {
				L8List = L8402ServiceImpl.findAll(titaVo, fg);
			}

			this.info("-----------------" + L8List);

			String strContent = "";
			// 若有底稿，需自行判斷列印起始行數 i
			// 若無底稿，設定列印起始行數 i=1
			int i = 1;
			switch (fg) {
			case 1:
				makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "B201", "聯徵授信餘額月報檔", "B201");
				break;
			case 2:
				makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "B207", "授信戶基本資料檔", "B207");
				break;
			case 3:
				makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "B080", "授信額度資料檔", "B080");
				break;
			case 4:
				makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "B085", "帳號轉換資料檔", "B085");
				break;
			case 5:
				makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "B087", "聯貸案首次動撥後６個月內發生違約之實際主導金融機構註記檔", "B087");
				break;
			case 6:
				makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "B090", "擔保品關聯檔資料檔", "B090");
				break;
			case 7:
				makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "B092", "不動產擔保品明細檔", "B092");
				break;
			case 8:
				makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "B093", "動產及貴重物品擔保品明細檔", "B093");
				break;
			case 9:
				makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "B094", "股票擔保品明細檔", "B094");
				break;
			case 10:
				makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "B095", "不動產擔保品明細-建號附加檔", "B095");
				break;
			case 11:
				makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "B096", "不動產擔保品明細-地號附加檔", "B096");
				break;
			case 12:
				makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "B680", "貸款餘額扣除擔保品鑑估值之金額資料檔", "B680");
				break;
			/*
			 * 未列在新系統需求書 case 13 : makeExcel.open(titaVo, titaVo.getEntDyI(),
			 * titaVo.getKinbr(), "B086", "聯貸合約各參貸機構參貸比例資料檔", "B086"); break; case 14 :
			 * makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "B091",
			 * "有價證券(股票除外)擔保品明細檔", "B091"); break;
			 */
			}

			// 自訂標題列
			if (infFg == true) {
				for (int j = 1; j <= inf1.length; j++) {
					makeExcel.setValue(i, j, inf1[j - 1]);
				}
				infFg = false;
				i++;
			}

			// 欄位內容
			if (L8List != null) {
				this.info("--------L8List.size=" + L8List.size());
				for (HashMap<String, String> tL8Vo : L8List) {
					for (int j = 1; j <= tL8Vo.size(); j++) {
						if (tL8Vo.get(txt1[j - 1]) == null) {
							makeExcel.setValue(i, j, "");
						} else {
							makeExcel.setValue(i, j, tL8Vo.get(txt1[j - 1]));
						}
					}
					i++;
				}
			}

			// 設定欄位寬度
			if (L8List != null) {
				switch (fg) {
				case 1:
					makeExcel.setWidth(1, 5);
					makeExcel.setWidth(2, 6);
					makeExcel.setWidth(3, 3);
					makeExcel.setWidth(4, 3);
					makeExcel.setWidth(5, 52);
					makeExcel.setWidth(6, 12);
					makeExcel.setWidth(7, 12);
					makeExcel.setWidth(8, 3);
					makeExcel.setWidth(9, 12);
					makeExcel.setWidth(10, 3);
					makeExcel.setWidth(11, 12);
					makeExcel.setWidth(12, 8);
					makeExcel.setWidth(13, 5);
					makeExcel.setWidth(14, 3);
					makeExcel.setWidth(15, 3);
					makeExcel.setWidth(16, 3);
					makeExcel.setWidth(17, 3);
					makeExcel.setWidth(18, 3);
					makeExcel.setWidth(19, 4);
					makeExcel.setWidth(20, 3);
					makeExcel.setWidth(21, 3);
					makeExcel.setWidth(22, 9);
					makeExcel.setWidth(23, 7);
					makeExcel.setWidth(24, 7);
					makeExcel.setWidth(25, 5);
					makeExcel.setWidth(26, 12);
					makeExcel.setWidth(27, 12);
					makeExcel.setWidth(28, 3);
					makeExcel.setWidth(29, 3);
					makeExcel.setWidth(30, 52);
					makeExcel.setWidth(31, 12);
					makeExcel.setWidth(32, 12);
					makeExcel.setWidth(33, 12);
					makeExcel.setWidth(34, 12);
					makeExcel.setWidth(35, 3);
					makeExcel.setWidth(36, 3);
					makeExcel.setWidth(37, 18);
					makeExcel.setWidth(38, 18);
					makeExcel.setWidth(39, 18);
					makeExcel.setWidth(40, 18);
					makeExcel.setWidth(41, 3);
					makeExcel.setWidth(42, 3);
					makeExcel.setWidth(43, 5);
					makeExcel.setWidth(44, 5);
					makeExcel.setWidth(45, 12);
					makeExcel.setWidth(46, 16);
					makeExcel.setWidth(47, 7);
					makeExcel.setWidth(48, 8);
					makeExcel.setWidth(49, 3);
					makeExcel.setWidth(50, 12);
					makeExcel.setWidth(51, 4);
					makeExcel.setWidth(52, 3);
					makeExcel.setWidth(53, 10);
					makeExcel.setWidth(54, 7);
					makeExcel.setWidth(55, 4);
					makeExcel.setWidth(56, 8);
					makeExcel.setWidth(57, 3);
					makeExcel.setWidth(58, 3);
					makeExcel.setWidth(59, 3);
					makeExcel.setWidth(60, 3);
					makeExcel.setWidth(61, 12);
					makeExcel.setWidth(62, 3);
					makeExcel.setWidth(63, 4);
					makeExcel.setWidth(64, 3);
					makeExcel.setWidth(65, 12);
					makeExcel.setWidth(66, 3);
					makeExcel.setWidth(67, 4);
					makeExcel.setWidth(68, 3);
					makeExcel.setWidth(69, 12);
					makeExcel.setWidth(70, 3);
					makeExcel.setWidth(71, 4);
					makeExcel.setWidth(72, 3);
					makeExcel.setWidth(73, 12);
					makeExcel.setWidth(74, 3);
					makeExcel.setWidth(75, 4);
					makeExcel.setWidth(76, 3);
					makeExcel.setWidth(77, 12);
					makeExcel.setWidth(78, 3);
					makeExcel.setWidth(79, 4);
					makeExcel.setWidth(80, 12);
					makeExcel.setWidth(81, 12);
					makeExcel.setWidth(82, 7);
					makeExcel.setWidth(83, 7);
					makeExcel.setWidth(84, 9);
					makeExcel.setWidth(85, 3);
					makeExcel.setWidth(86, 6);
					makeExcel.setWidth(87, 3);
					makeExcel.setWidth(88, 4);
					makeExcel.setWidth(89, 11);
					makeExcel.setWidth(90, 7);
					makeExcel.setWidth(91, 3);
					break;
				case 2:
					makeExcel.setWidth(1, 3);
					makeExcel.setWidth(2, 5);
					makeExcel.setWidth(3, 6);
					makeExcel.setWidth(4, 9);
					makeExcel.setWidth(5, 12);
					makeExcel.setWidth(6, 32);
					makeExcel.setWidth(7, 22);
					makeExcel.setWidth(8, 9);
					makeExcel.setWidth(9, 101);
					makeExcel.setWidth(10, 7);
					makeExcel.setWidth(11, 101);
					makeExcel.setWidth(12, 32);
					makeExcel.setWidth(13, 18);
					makeExcel.setWidth(14, 7);
					makeExcel.setWidth(15, 3);
					makeExcel.setWidth(16, 3);
					makeExcel.setWidth(17, 47);
					makeExcel.setWidth(18, 10);
					makeExcel.setWidth(19, 8);
					makeExcel.setWidth(20, 18);
					makeExcel.setWidth(21, 17);
					makeExcel.setWidth(22, 4);
					makeExcel.setWidth(23, 8);
					makeExcel.setWidth(24, 7);
					makeExcel.setWidth(25, 3);
					makeExcel.setWidth(26, 4);
					makeExcel.setWidth(27, 22);
					makeExcel.setWidth(28, 12);
					makeExcel.setWidth(29, 102);
					makeExcel.setWidth(30, 38);
					break;
				case 3:
					break;
				case 4:
					break;
				case 5:
					break;
				case 6:
					makeExcel.setWidth(1, 4);
					makeExcel.setWidth(2, 5);
					makeExcel.setWidth(3, 6);
					makeExcel.setWidth(4, 4);
					makeExcel.setWidth(5, 12);
					makeExcel.setWidth(6, 52);
					makeExcel.setWidth(7, 52);
					makeExcel.setWidth(8, 4);
					makeExcel.setWidth(9, 7);
					break;
				case 7:
					makeExcel.setWidth(1, 4);
					makeExcel.setWidth(2, 5);
					makeExcel.setWidth(3, 6);
					makeExcel.setWidth(4, 4);
					makeExcel.setWidth(5, 52);
					makeExcel.setWidth(6, 4);
					makeExcel.setWidth(7, 12);
					makeExcel.setWidth(8, 10);
					makeExcel.setWidth(9, 7);
					makeExcel.setWidth(10, 10);
					makeExcel.setWidth(11, 7);
					makeExcel.setWidth(12, 10);
					makeExcel.setWidth(13, 3);
					makeExcel.setWidth(14, 10);
					makeExcel.setWidth(15, 10);
					makeExcel.setWidth(16, 10);
					makeExcel.setWidth(17, 7);
					makeExcel.setWidth(18, 3);
					makeExcel.setWidth(19, 4);
					makeExcel.setWidth(20, 6);
					makeExcel.setWidth(21, 6);
					makeExcel.setWidth(22, 6);
					makeExcel.setWidth(23, 7);
					makeExcel.setWidth(24, 5);
					makeExcel.setWidth(25, 7);
					makeExcel.setWidth(26, 3);
					makeExcel.setWidth(27, 10);
					makeExcel.setWidth(28, 7);
					makeExcel.setWidth(29, 10);
					makeExcel.setWidth(30, 9);
					makeExcel.setWidth(31, 3);
					makeExcel.setWidth(32, 11);
					makeExcel.setWidth(33, 12);
					makeExcel.setWidth(34, 4);
					makeExcel.setWidth(35, 31);
					makeExcel.setWidth(36, 7);
					break;
				case 8:
					break;
				case 9:
					break;
				case 10:
					break;
				case 11:
					break;
				case 12:
					break;
				}
			}

			long sno = makeExcel.close();
			makeExcel.toExcel(sno);

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("L8402ServiceImpl.genExcel error = " + errors.toString());
		}
	}
}
