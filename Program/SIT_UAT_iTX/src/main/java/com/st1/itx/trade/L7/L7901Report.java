package com.st1.itx.trade.L7;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.L7901ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.MakeFile;

import java.text.DecimalFormat;

@Component("L7901Report")
@Scope("prototype")

public class L7901Report extends MakeReport {
	// private static final Logger logger =
	// LoggerFactory.getLogger(L7901Report.class);

	@Autowired
	public L7901ServiceImpl L7901ServiceImpl;

	@Autowired
	public MakeExcel makeExcel;

	@Autowired
	public MakeFile makeFile;

	// 自訂表頭
//	@Override
//	public void printHeader() {
//
//
//		//明細起始列(自訂亦必須)
//		this.setBeginRow(10);
//
//		//設定明細列數(自訂亦必須)
//		this.setMaxRows(35);
//	}

	// 自訂明細標題
	@Override
	public void printTitle() {
		this.info("printTitle nowRow = " + this.NowRow);

	}

	public void exec(TitaVo titaVo) throws LogicException {
		int fg = 0;

		for (fg = 1; fg <= 3; fg++) {

			// 設定資料庫(必須的)
			String strSql = "";
			try {
				switch (fg) {
				case 1:
					strSql = "SELECT \"CustNo\", \"CustId\", \"FacmNo\", \"ApplNo\", \"BormNo\"" + ", \"AcCode\", \"Status\", \"FirstDrawdownDate\", \"DrawdownDate\", \"FacLineDate\""
							+ ", \"MaturityDate\", \"LineAmt\", \"DrawdownAmt\", \"AcctFee\", \"LoanBal\"" + ", \"IntAmt\", \"Fee\", \"Rate\", \"OvduDays\", \"OvduDate\""
							+ ", \"BadDebtDate\", \"BadDebtAmt\", \"GracePeriod\", \"ApproveRate\"" + ", \"AmortizedCode\", \"RateCode\", \"RepayFreq\", \"PayIntFreq\", \"IndustryCode\""
							+ ", \"ClTypeJCIC\"" + ", CASE" + "    WHEN \"CityCode\" = '05' THEN 'A'" + "    WHEN \"CityCode\" = '10' THEN 'B'" + "    WHEN \"CityCode\" = '15' THEN 'C'"
							+ "    WHEN \"CityCode\" = '35' THEN 'D'" + "    WHEN \"CityCode\" = '65' THEN 'E'" + "    WHEN \"CityCode\" = '70' THEN 'F'" + "    ELSE 'G'"
							+ "  END                                  as \"CityCode\"" + ", \"BaseRateCode\", \"CustKind\", \"AssetKind\""
							+ ", \"ProdNo\", \"EvaAmt\", \"FirstDueDate\", \"TotalPeriod\"" + ", CASE" + "    WHEN NVL(\"LineAmt\",0) > NVL(\"UtilBal\",0) THEN NVL(\"LineAmt\",0) - NVL(\"UtilBal\",0)"
							+ "    ELSE 0 " + "  END                                  as \"AvblBal\"" + ", \"RecycleCode\", \"IrrevocableFlag\""
							+ ", \"TempAmt\", \"AcCurcd\", \"AcBookCode\", \"CurrencyCode\", \"ExchangeRate\"" + ", \"LineAmt\"      as \"txLineAmt\"" + ", \"DrawdownAmt\"  as \"txDrawdownAmt\""
							+ ", \"AcctFee\"      as \"txAcctFee\"" + ", \"LoanBal\"      as \"txLoanBal\"" + ", \"IntAmt\"       as \"txIntAmt\"" + ", \"Fee\"          as \"txFee\"" + ", CASE"
							+ "    WHEN NVL(\"LineAmt\",0) > NVL(\"UtilBal\",0) THEN NVL(\"LineAmt\",0) - NVL(\"UtilBal\",0)" + "    ELSE 0 " + "  END              as \"txAvblBal\""
							+ ", \"TempAmt\"      as \"txTempAmt\"" + " FROM  \"Ias34Ap\"" + " WHERE \"DataYM\" = 202005" + " ORDER BY \"CustNo\", \"FacmNo\", \"BormNo\"";
					break;
				case 2:
					strSql = "SELECT \"CustNo\", \"CustId\", \"FacmNo\", \"BormNo\", \"LoanRate\", \"RateCode\", \"EffectDate\"" + " FROM  \"Ias34Bp\"" + " WHERE \"DataYM\" = 202005"
							+ " ORDER BY \"CustNo\", \"FacmNo\", \"BormNo\", \"EffectDate\" DESC";
					break;
				case 3:
					strSql = "SELECT \"CustNo\", \"CustId\", \"FacmNo\", \"BormNo\", \"AmortizedCode\", \"PayIntFreq\", \"RepayFreq\", \"EffectDate\"" + " FROM  \"Ias34Cp\""
							+ " WHERE \"DataYM\" = 202005" + " ORDER BY \"CustNo\", \"FacmNo\", \"BormNo\", \"EffectDate\"";
					break;
				}

				List<HashMap<String, String>> L7901List = L7901ServiceImpl.findAll(strSql);

				// switch(fg) {
				// case 1 : genFile_01(titaVo, 0, L7901List);
				// case 2 : genFile_02(titaVo, 0, L7901List);
				// case 3 : genFile_03(titaVo, 0, L7901List);
				// }

				genExcel(titaVo, fg, L7901List);

			} catch (Exception e) {
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				this.info("L7901ServiceImpl.findAll (" + fg + ") error = " + errors.toString());
			}
		}
	}

	private void genExcel(TitaVo titaVo, int fg, List<HashMap<String, String>> L7List) throws LogicException {
		this.info("=========== L7901-" + fg + " genExcel");

		// 自訂標題 inf
		String inf = "";
		String txt = "";
		switch (fg) {
		case 1:
			inf = "戶號;借款人ID/統編;額度編號;核准號碼;撥款序號;會計科目(8碼);戶況;初貸日期;撥款日期;到期日(額度);到期日(撥款);核准金額;撥款金額;帳管費;本金餘額(撥款);應收利息;法拍及火險費用;利率(撥款);逾期繳款天數;轉催收款日期;轉銷呆帳日期;轉銷呆帳金額;初貸時約定還本寬限期;核准利率;契約當時還款方式;契約當時利率調整方式;契約約定當時還本週期;契約約定當時繳息週期;授信行業別;擔保品類別;擔保品地區別;商品利率代碼;企業戶/個人戶;五類資產分類;產品別;原始鑑價金額;首次應繳日;總期數;可動用餘額(台幣);該筆額度是否可循環動用;該筆額度是否為不可徹銷;暫收款金額(台幣);記帳幣別;會計帳冊;交易幣別;報導日匯率;核准金額(交易幣);撥款金額(交易幣);帳管費(交易幣);本金餘額(撥款_交易幣);應收利息(交易幣);法拍及火險費用(交易幣);可動用餘額(交易幣);暫收款金額(交易幣)";
			txt = "F0;F1;F2;F3;F4;F5;F6;F7;F8;F9;F10;F11;F12;F13;F14;F15;F16;F17;F18;F19;F20;F21;F22;F23;F24;" + "F25;F26;F27;F28;F29;F30;F31;F32;F33;F34;F35;F36;F37;F38;F39;F40;F41;F42;F43;F44;F45;"
					+ "F46;F47;F48;F49;F50;F51;F52;F53";
			break;
		case 2:
			inf = "戶號;借款人ID/統編;額度編號;撥款序號;貸放利率;利率調整方式;利率欄位生效日";
			txt = "F0;F1;F2;F3;F4;F5;F6";
			break;
		case 3:
			inf = "戶號;借款人ID/統編;額度編號;撥款序號;約定還款方式;繳息週期;還本週期;生效日期";
			txt = "F0;F1;F2;F3;F4;F5;F6;F7";
			break;
		}

		// 自訂標題列寫入記號
		boolean infFg = true;
		if ((inf.trim()).equals("")) {
			infFg = false;
		}

		String inf1[] = inf.split(";");
		String txt1[] = txt.split(";");

		try {
			this.info("-----------------" + L7List);
			// this.info("----------------- L7List.size()=" + L7List.size());

			// 若有底稿，需自行判斷列印起始行數 i
			// 若無底稿，設定列印起始行數 i=1
			int i = 1;
			switch (fg) {
			case 1:
				i = 1;
				makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L7901", "IFRS9 資料欄位清單1", "IFRS9_01");
				break;
			case 2:
				i = 1;
				makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L7901", "IFRS9 資料欄位清單2", "IFRS9_02");
				break;
			case 3:
				i = 1;
				makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L7901", "IFRS9 資料欄位清單3", "IFRS9_03");
				break;
			}

			// 自訂標題列
			if (infFg == true) {
				for (int j = 1; j <= inf1.length; j++) {
					makeExcel.setValue(i, j, inf1[j - 1]);
				}
				infFg = false;
				i++;
			}

			// 輸出內容
			for (HashMap<String, String> tL7Vo : L7List) {
				for (int j = 1; j <= tL7Vo.size(); j++) {
					if (tL7Vo.get(txt1[j - 1]) == null) {
						makeExcel.setValue(i, j, "");
					} else {
						makeExcel.setValue(i, j, tL7Vo.get(txt1[j - 1]));
					}
				}
				i++;
			}

			long sno = makeExcel.close();
			makeExcel.toExcel(sno);

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("L7902ServiceImpl.genExcel error = " + errors.toString());
		}
	}

	private void genFile_01(TitaVo titaVo, int fg, List<HashMap<String, String>> L7List) throws LogicException {
		// IFRS9 資料欄位清單1 (預期損失計算作業流程)
		this.info("===========in L7901 genFile_01");
		String txt = "F0;F1;F2;F3;F4;F5;F6;F7;F8;F9;F10;F11;F12;F13;F14;F15;F16;F17;F18;F19;F20;F21;F22;F23;F24;"
				+ "F25;F26;F27;F28;F29;F30;F31;F32;F33;F34;F35;F36;F37;F38;F39;F40;F41;F42;F43;F44;F45;" + "F46;F47;F48;F49;F50;F51;F52;F53";
		String txt1[] = txt.split(";");

		try {
			String strContent = "";

			makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), titaVo.getTxCode(), "IFRS9 資料欄位清單1", "IFRS9_01.csv", 2);

			// 標題列
			strContent = "戶號(1~7),借款人ID/統編(8~17),額度編號(18~20),核准號碼(21~27),撥款序號(28~30),會計科目(8碼)(31~38),戶況(39~39)," + "初貸日期(40~47),撥款日期(48~55),到期日(額度)(56~63),到期日(撥款)(64~71),核准金額(72~84),撥款金額(85~97),"
					+ "帳管費(98~110),本金餘額(撥款)(111~123),應收利息(124~136),法拍及火險費用(137~149),利率(撥款)(150~158)," + "逾期繳款天數(159~162),轉催收款日期(163~170),轉銷呆帳日期(171~178),轉銷呆帳金額(179~191),"
					+ "初貸時約定還本寬限期(192~194),核准利率(195~202),契約當時還款方式(203~203),契約當時利率調整方式(204~204)," + "契約約定當時還本週期(205~206),契約約定當時繳息週期(207~208),授信行業別(209~214),擔保品類別(215~216),"
					+ "擔保品地區別(217~217),商品利率代碼(218~219),企業戶/個人戶(220~220),五類資產分類(221~221),產品別(222~222)," + "原始鑑價金額(223~235),首次應繳日(236~243),總期數(244~246),可動用餘額(台幣)(247~259),該筆額度是否可循環動用(260~260),"
					+ "該筆額度是否為不可徹銷(261~261),暫收款金額(台幣)(262~274),記帳幣別(275~275),會計帳冊(276~276),交易幣別(277~280)," + "報導日匯率(281~289),核准金額(交易幣)(290~302),撥款金額(交易幣)(303~315),帳管費(交易幣)(316~328),"
					+ "本金餘額(撥款_交易幣)(329~341),應收利息(交易幣)(342~354),法拍及火險費用(交易幣)(355~367),可動用餘額(交易幣)(368~380)," + "暫收款金額(交易幣)(381~393)";
			makeFile.put(strContent);

			// 欄位內容
			this.info("-----------------" + L7List);
			for (HashMap<String, String> tL7Vo : L7List) {
				strContent = "";
				// this.info("-----------------" + tL7Vo);
				// this.info("--------tL7Vo.size=" + tL7Vo.size());
				for (int j = 1; j <= tL7Vo.size(); j++) {
					// this.info("--------------j=" + j);
					String strField = "";
					if (tL7Vo.get(txt1[j - 1]) == null) {
						strField = "";
					} else {
						strField = tL7Vo.get(txt1[j - 1]).replace(",", "，").trim(); // csv檔: 逗號轉全形
					}

					// 格式處理
					// this.info("--------------strField=" + strField);
					if (j == 1) {
						strField = makeFile.fillStringL(strField, 7, '0');
					} // 戶號
					if (j == 2) {
						strField = makeFile.fillStringR(strField, 10, ' ');
					} // 借款人ID / 統編
					if (j == 3) {
						strField = makeFile.fillStringL(strField, 3, '0');
					} // 額度編號
					if (j == 4) {
						strField = makeFile.fillStringL(strField, 7, '0');
					} // 核准號碼
					if (j == 5) {
						strField = makeFile.fillStringL(strField, 3, '0');
					} // 撥款序號
					if (j == 6) {
						strField = makeFile.fillStringR(strField, 8, ' ');
					} // 會計科目(8碼)
					if (j == 7) {
						strField = makeFile.fillStringL(strField, 1, '0');
					} // 戶況
					if (j == 8) {
						strField = makeFile.fillStringL(strField, 8, '0');
					} // 初貸日期
					if (j == 9) {
						strField = makeFile.fillStringL(strField, 8, '0');
					} // 撥款日期
					if (j == 10) {
						strField = makeFile.fillStringL(strField, 8, '0');
					} // 到期日(額度)
					if (j == 11) {
						strField = makeFile.fillStringL(strField, 8, '0');
					} // 到期日(撥款)
					if (j == 12) {
						DecimalFormat formatter = new DecimalFormat("0.00");
						strField = formatter.format(Float.parseFloat(strField = (strField.isEmpty() ? "0" : strField)));
						strField = makeFile.fillStringL(strField, 13, '0');
					} // 核准金額
					if (j == 13) {
						DecimalFormat formatter = new DecimalFormat("0.00");
						strField = formatter.format(Float.parseFloat(strField = (strField.isEmpty() ? "0" : strField)));
						strField = makeFile.fillStringL(strField, 13, '0');
					} // 撥款金額
					if (j == 14) {
						DecimalFormat formatter = new DecimalFormat("0.00");
						strField = formatter.format(Float.parseFloat(strField = (strField.isEmpty() ? "0" : strField)));
						strField = makeFile.fillStringL(strField, 13, '0');
					} // 帳管費
					if (j == 15) {
						DecimalFormat formatter = new DecimalFormat("0.00");
						strField = formatter.format(Float.parseFloat(strField = (strField.isEmpty() ? "0" : strField)));
						strField = makeFile.fillStringL(strField, 13, '0');
					} // 本金餘額(撥款)
					if (j == 16) {
						DecimalFormat formatter = new DecimalFormat("0.00");
						strField = formatter.format(Float.parseFloat(strField = (strField.isEmpty() ? "0" : strField)));
						strField = makeFile.fillStringL(strField, 13, '0');
					} // 應收利息
					if (j == 17) {
						DecimalFormat formatter = new DecimalFormat("0.00");
						strField = formatter.format(Float.parseFloat(strField = (strField.isEmpty() ? "0" : strField)));
						strField = makeFile.fillStringL(strField, 13, '0');
					} // 法拍及火險費用
					if (j == 18) {
						DecimalFormat formatter = new DecimalFormat("0.000000");
						strField = formatter.format(Float.parseFloat(strField = (strField.isEmpty() ? "0" : strField)));
						strField = makeFile.fillStringL(strField, 9, '0');
					} // 利率(撥款)
					if (j == 19) {
						strField = makeFile.fillStringL(strField, 4, '0');
					} // 逾期繳款天數
					if (j == 20) {
						strField = makeFile.fillStringL(strField, 8, '0');
					} // 轉催收款日期
					if (j == 21) {
						strField = makeFile.fillStringL(strField, 8, '0');
					} // 轉銷呆帳日期
					if (j == 22) {
						DecimalFormat formatter = new DecimalFormat("0.00");
						strField = formatter.format(Float.parseFloat(strField = (strField.isEmpty() ? "0" : strField)));
						strField = makeFile.fillStringL(strField, 13, '0');
					} // 轉銷呆帳金額
					if (j == 23) {
						strField = makeFile.fillStringL(strField, 3, '0');
					} // 初貸時約定還本寬限期
					if (j == 24) {
						DecimalFormat formatter = new DecimalFormat("0.000000");
						strField = formatter.format(Float.parseFloat(strField = (strField.isEmpty() ? "0" : strField)) / 100);
						strField = makeFile.fillStringL(strField, 8, '0');
					} // 核准利率
					if (j == 25) {
						strField = makeFile.fillStringL(strField, 1, '0');
					} // 契約當時還款方式
					if (j == 26) {
						strField = makeFile.fillStringL(strField, 1, '0');
					} // 契約當時利率調整方式
					if (j == 27) {
						strField = makeFile.fillStringL(strField, 2, '0');
					} // 契約約定當時還本週期
					if (j == 28) {
						strField = makeFile.fillStringL(strField, 2, '0');
					} // 契約約定當時繳息週期
					if (j == 29) {
						strField = makeFile.fillStringR(strField, 6, ' ');
					} // 授信行業別
					if (j == 30) {
						strField = makeFile.fillStringR(strField, 2, ' ');
					} // 擔保品類別
					if (j == 31) {
						strField = makeFile.fillStringR(strField, 1, ' ');
					} // 擔保品地區別
					if (j == 32) {
						strField = makeFile.fillStringR(strField, 2, ' ');
					} // 商品利率代碼
					if (j == 33) {
						strField = makeFile.fillStringL(strField, 1, '0');
					} // 企業戶/個人戶
					if (j == 34) {
						strField = makeFile.fillStringL(strField, 1, '0');
					} // 五類資產分類
					if (j == 35) {
						strField = makeFile.fillStringR(strField, 1, ' ');
					} // 產品別
					if (j == 36) {
						DecimalFormat formatter = new DecimalFormat("0.00");
						strField = formatter.format(Float.parseFloat(strField = (strField.isEmpty() ? "0" : strField)));
						strField = makeFile.fillStringL(strField, 13, '0');
					} // 原始鑑價金額
					if (j == 37) {
						strField = makeFile.fillStringL(strField, 8, '0');
					} // 首次應繳日
					if (j == 38) {
						strField = makeFile.fillStringL(strField, 3, '0');
					} // 總期數
					if (j == 39) {
						DecimalFormat formatter = new DecimalFormat("0.00");
						strField = formatter.format(Float.parseFloat(strField = (strField.isEmpty() ? "0" : strField)));
						strField = makeFile.fillStringL(strField, 13, '0');
					} // 可動用餘額(台幣)
					if (j == 40) {
						strField = makeFile.fillStringL(strField, 1, '0');
					} // 該筆額度是否可循環動用 0: 非循環動用 1: 循環動用
					if (j == 41) {
						strField = makeFile.fillStringL(strField, 1, '0');
					} // 該筆額度是否為不可徹銷 1=是 0=否
					if (j == 42) {
						DecimalFormat formatter = new DecimalFormat("0.00");
						strField = formatter.format(Float.parseFloat(strField = (strField.isEmpty() ? "0" : strField)));
						strField = makeFile.fillStringL(strField, 13, '0');
					} // 暫收款金額(台幣)
					if (j == 43) {
						strField = makeFile.fillStringL(strField, 1, '0');
					} // 記帳幣別
					if (j == 44) {
						strField = makeFile.fillStringR(strField, 1, ' ');
					} // 會計帳冊 1=一般 2=分紅 3=利變 4=OIU
					if (j == 45) {
						strField = makeFile.fillStringR(strField, 4, ' ');
					} // 交易幣別
					if (j == 46) {
						DecimalFormat formatter = new DecimalFormat("0.00000");
						strField = formatter.format(Float.parseFloat(strField = (strField.isEmpty() ? "0" : strField)));
						strField = makeFile.fillStringL(strField, 9, '0');
					} // 報導日匯率
					if (j == 47) {
						DecimalFormat formatter = new DecimalFormat("0.00");
						strField = formatter.format(Float.parseFloat(strField = (strField.isEmpty() ? "0" : strField)));
						strField = makeFile.fillStringL(strField, 13, '0');
					} // 核准金額(交易幣)
					if (j == 48) {
						DecimalFormat formatter = new DecimalFormat("0.00");
						strField = formatter.format(Float.parseFloat(strField = (strField.isEmpty() ? "0" : strField)));
						strField = makeFile.fillStringL(strField, 13, '0');
					} // 撥款金額(交易幣)
					if (j == 49) {
						DecimalFormat formatter = new DecimalFormat("0.00");
						strField = formatter.format(Float.parseFloat(strField = (strField.isEmpty() ? "0" : strField)));
						strField = makeFile.fillStringL(strField, 13, '0');
					} // 帳管費(交易幣)
					if (j == 50) {
						DecimalFormat formatter = new DecimalFormat("0.00");
						strField = formatter.format(Float.parseFloat(strField = (strField.isEmpty() ? "0" : strField)));
						strField = makeFile.fillStringL(strField, 13, '0');
					} // 本金餘額(撥款_交易幣)
					if (j == 51) {
						DecimalFormat formatter = new DecimalFormat("0.00");
						strField = formatter.format(Float.parseFloat(strField = (strField.isEmpty() ? "0" : strField)));
						strField = makeFile.fillStringL(strField, 13, '0');
					} // 應收利息(交易幣)
					if (j == 52) {
						DecimalFormat formatter = new DecimalFormat("0.00");
						strField = formatter.format(Float.parseFloat(strField = (strField.isEmpty() ? "0" : strField)));
						strField = makeFile.fillStringL(strField, 13, '0');
					} // 法拍及火險費用(交易幣)
					if (j == 53) {
						DecimalFormat formatter = new DecimalFormat("0.00");
						strField = formatter.format(Float.parseFloat(strField = (strField.isEmpty() ? "0" : strField)));
						strField = makeFile.fillStringL(strField, 13, '0');
					} // 可動用餘額(交易幣)
					if (j == 54) {
						DecimalFormat formatter = new DecimalFormat("0.00");
						strField = formatter.format(Float.parseFloat(strField = (strField.isEmpty() ? "0" : strField)));
						strField = makeFile.fillStringL(strField, 13, '0');
					} // 暫收款金額(交易幣)

					strContent = strContent + strField;
					if (j != tL7Vo.size()) {
						strContent = strContent + ",";
					}
				}
				makeFile.put(strContent);
			}

			long sno = makeFile.close();
			makeFile.toFile(sno);

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("L7901ServiceImpl.genFile(01) error = " + errors.toString());
		}
	}

	private void genFile_02(TitaVo titaVo, int fg, List<HashMap<String, String>> L7List) throws LogicException {
		// IFRS9 資料欄位清單1 (預期損失計算作業流程)
		this.info("===========in L7901 genFile_02");
		String txt = "F0;F1;F2;F3;F4;F5;F6";
		String txt1[] = txt.split(";");

		try {
			String strContent = "";

			makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), titaVo.getTxCode(), "IFRS9 資料欄位清單2", "IFRS9_02.csv", 2);

			// 標題列
			strContent = "戶號(1~7),借款人ID/統編(8~17),額度編號(18~20),撥款序號(21~23),貸放利率(24~32),利率調整方式(33~33),利率欄位生效日(34~41)";
			makeFile.put(strContent);

			// 欄位內容
			this.info("-----------------" + L7List);
			for (HashMap<String, String> tL7Vo : L7List) {
				strContent = "";
				// this.info("-----------------" + tL7Vo);
				// this.info("--------tL7Vo.size=" + tL7Vo.size());
				for (int j = 1; j <= tL7Vo.size(); j++) {
					// this.info("--------------j=" + j);
					String strField = "";
					if (tL7Vo.get(txt1[j - 1]) == null) {
						strField = "";
					} else {
						strField = tL7Vo.get(txt1[j - 1]).replace(",", "，").trim(); // csv檔: 逗號轉全形
					}

					// 格式處理
					// this.info("--------------strField=" + strField);
					if (j == 1) {
						strField = makeFile.fillStringL(strField, 7, '0');
					} // 戶號
					if (j == 2) {
						strField = makeFile.fillStringR(strField, 10, ' ');
					} // 借款人ID / 統編
					if (j == 3) {
						strField = makeFile.fillStringL(strField, 3, '0');
					} // 額度編號
					if (j == 4) {
						strField = makeFile.fillStringL(strField, 3, '0');
					} // 撥款序號
					if (j == 5) {
						DecimalFormat formatter = new DecimalFormat("0.000000");
						strField = formatter.format(Float.parseFloat(strField = (strField.isEmpty() ? "0" : strField)) / 100);
						strField = makeFile.fillStringL(strField, 9, '0');
					} // 貸放利率
					if (j == 6) {
						strField = makeFile.fillStringL(strField, 1, '0');
					} // 利率調整方式
					if (j == 7) {
						strField = makeFile.fillStringL(strField, 8, '0');
					} // 利率欄位生效日

					strContent = strContent + strField;
					if (j != tL7Vo.size()) {
						strContent = strContent + ",";
					}
				}
				makeFile.put(strContent);
			}

			long sno = makeFile.close();
			makeFile.toFile(sno);

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("L7901ServiceImpl.genFile(02) error = " + errors.toString());
		}
	}

	private void genFile_03(TitaVo titaVo, int fg, List<HashMap<String, String>> L7List) throws LogicException {
		// IFRS9 資料欄位清單1 (預期損失計算作業流程)
		this.info("===========in L7901 genFile_03");
		String txt = "F0;F1;F2;F3;F4;F5;F6;F7";
		String txt1[] = txt.split(";");

		try {
			String strContent = "";

			makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), titaVo.getTxCode(), "IFRS9 資料欄位清單3", "IFRS9_03.csv", 2);

			// 標題列
			strContent = "戶號(1~7),借款人ID/統編(8~17),額度編號(18~20),撥款序號(21~23),約定還款方式(24~24),繳息週期(25~26),還本週期(27~28),生效日期(29~36)";
			makeFile.put(strContent);

			// 欄位內容
			this.info("-----------------" + L7List);
			for (HashMap<String, String> tL7Vo : L7List) {
				strContent = "";
				// this.info("-----------------" + tL7Vo);
				// this.info("--------tL7Vo.size=" + tL7Vo.size());
				for (int j = 1; j <= tL7Vo.size(); j++) {
					// this.info("--------------j=" + j);
					String strField = "";
					if (tL7Vo.get(txt1[j - 1]) == null) {
						strField = "";
					} else {
						strField = tL7Vo.get(txt1[j - 1]).replace(",", "，").trim(); // csv檔: 逗號轉全形
					}

					// 格式處理
					// this.info("--------------strField=" + strField);
					if (j == 1) {
						strField = makeFile.fillStringL(strField, 7, '0');
					} // 戶號
					if (j == 2) {
						strField = makeFile.fillStringR(strField, 10, ' ');
					} // 借款人ID / 統編
					if (j == 3) {
						strField = makeFile.fillStringL(strField, 3, '0');
					} // 額度編號
					if (j == 4) {
						strField = makeFile.fillStringL(strField, 3, '0');
					} // 撥款序號
					if (j == 5) {
						strField = makeFile.fillStringL(strField, 1, '0');
					} // 約定還款方式
					if (j == 6) {
						strField = makeFile.fillStringL(strField, 2, '0');
					} // 繳息週期
					if (j == 7) {
						strField = makeFile.fillStringL(strField, 2, '0');
					} // 還本週期
					if (j == 8) {
						strField = makeFile.fillStringL(strField, 8, '0');
					} // 生效日期

					strContent = strContent + strField;
					if (j != tL7Vo.size()) {
						strContent = strContent + ",";
					}
				}
				makeFile.put(strContent);
			}

			long sno = makeFile.close();
			makeFile.toFile(sno);

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("L7901ServiceImpl.genFile(03) error = " + errors.toString());
		}
	}
}