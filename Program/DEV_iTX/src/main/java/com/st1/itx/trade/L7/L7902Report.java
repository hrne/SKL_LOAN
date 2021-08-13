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
import com.st1.itx.db.service.springjpa.cm.L7902ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.MakeFile;

import java.text.DecimalFormat;

@Component("L7902Report")
@Scope("prototype")

public class L7902Report extends MakeReport {
	// private static final Logger logger = LoggerFactory.getLogger(L7902Report.class);

	@Autowired
	public L7902ServiceImpl L7902ServiceImpl;

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
		try {
			List<HashMap<String, String>> L7902List = L7902ServiceImpl.findAll();
			if (L7902List.size() > 0) {
				// genFile(titaVo, 0, L7902List);
				genExcel(titaVo, 0, L7902List);
			}
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("L7902ServiceImpl.findAll error = " + errors.toString());
		}
	}

	private void genExcel(TitaVo titaVo, int fg, List<HashMap<String, String>> L7List) throws LogicException {
		this.info("=========== L7902 genExcel");
		// 自訂標題 inf
		String inf = "戶號;借款人ID/統編;額度編號;撥款序號;會計科目;案件狀態;初貸日期;貸放日期;到期日;核准金額(台幣);撥款金額(台幣);本金餘額(撥款)(台幣);應收利息(台幣);法拍及火險費用(台幣);逾期繳款天數;轉催收款日期;轉銷呆帳日期;轉銷呆帳金額;stage3發生日期;上述發生日期前之最近一次利率;上述發生日期時之本金餘額(台幣);上述發生日期時之應收利息(台幣);上述發生日期時之法拍及火險費用(台幣);stage3發生後第一年本金回收金額(台幣);stage3發生後第二年本金回收金額(台幣);stage3發生後第三年本金回收金額(台幣);stage3發生後第四年本金回收金額(台幣);stage3發生後第五年本金回收金額(台幣);stage3發生後第一年應收利息回收金額(台幣);stage3發生後第二年應收利息回收金額(台幣);stage3發生後第三年應收利息回收金額(台幣);stage3發生後第四年應收利息回收金額(台幣);stage3發生後第五年應收利息回收金額(台幣);stage3發生後第一年法拍及火險費用回收金額(台幣);stage3發生後第二年法拍及火險費用回收金額(台幣);stage3發生後第三年法拍及火險費用回收金額(台幣);stage3發生後第四年法拍及火險費用回收金額(台幣);stage3發生後第五年法拍及火險費用回收金額(台幣);授信行業別;擔保品類別;擔保品地區別;商品利率代碼;企業戶/個人戶;產品別";
		String txt = "F0;F1;F2;F3;F4;F5;F6;F7;F8;F9;F10;F11;F12;F13;F14;F15;F16;F17;F18;F19;F20;F21;F22;F23;F24;" + "F25;F26;F27;F28;F29;F30;F31;F32;F33;F34;F35;F36;F37;F38;F39;F40;F41;F42;F43";

		// 自訂標題列寫入記號
		boolean infFg = true;
		if ((inf.trim()).equals("")) {
			infFg = false;
		}

		String inf1[] = inf.split(";");
		String txt1[] = txt.split(";");

		try {
			// 若有底稿，需自行判斷列印起始行數 i
			// 若無底稿，設定列印起始行數 i=1
			int i = 1;
			this.info("-----------------" + L7List);
			// this.info("----------------- L7List.size()=" + L7List.size());

			makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L7902", "IFRS9 資料欄位清單4", "IFRS9_04");

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

	private void genFile(TitaVo titaVo, int fg, List<HashMap<String, String>> L7List) throws LogicException {
		this.info("=========== L7902 genFile");
		String txt = "F0;F1;F2;F3;F4;F5;F6;F7;F8;F9;F10;F11;F12;F13;F14;F15;F16;F17;F18;F19;F20;F21;F22;F23;F24;" + "F25;F26;F27;F28;F29;F30;F31;F32;F33;F34;F35;F36;F37;F38;F39;F40;F41;F42;F43";
		String txt1[] = txt.split(";");

		try {
			String strContent = "";

			makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), titaVo.getTxCode(), "IFRS9 資料欄位清單4", "IFRS9_04.csv", 2);

			// 標題列
			strContent = "戶號(1~7),借款人ID / 統編(8~17),額度編號(18~20),撥款序號(21~23),會計科目(24~31),案件狀態(32~32),初貸日期(33~40),貸放日期(41~48),到期日(49~56),核准金額(台幣)(57~69),撥款金額(台幣)(70~82),本金餘額(撥款)(台幣)(83~95),應收利息(台幣)(96~108),法拍及火險費用(台幣)(109~121),逾期繳款天數(122~125),轉催收款日期(126~133),轉銷呆帳日期(134~141),轉銷呆帳金額(142~154),stage3發生日期(155~162),上述發生日期前之最近一次利率(163~171),上述發生日期時之本金餘額(台幣)(172~184),上述發生日期時之應收利息(台幣)(185~197),上述發生日期時之法拍及火險費用(台幣)(198~210),stage3發生後第一年本金回收金額(台幣)(211~223),stage3發生後第二年本金回收金額(台幣)(224~236),stage3發生後第三年本金回收金額(台幣)(237~249),stage3發生後第四年本金回收金額(台幣)(250~262),stage3發生後第五年本金回收金額(台幣)(263~275),stage3發生後第一年應收利息回收金額(台幣)(276~288),stage3發生後第二年應收利息回收金額(台幣)(289~301),stage3發生後第三年應收利息回收金額(台幣)(302~314),stage3發生後第四年應收利息回收金額(台幣)(315~327),stage3發生後第五年應收利息回收金額(台幣)(328~340),stage3發生後第一年法拍及火險費用回收金額(台幣)(341~353),stage3發生後第二年法拍及火險費用回收金額(台幣)(354~366),stage3發生後第三年法拍及火險費用回收金額(台幣)(367~379),stage3發生後第四年法拍及火險費用回收金額(台幣)(380~392),stage3發生後第五年法拍及火險費用回收金額(台幣)(393~405),授信行業別(406~411),擔保品類別(412~413),擔保品地區別(414~414),商品利率代碼(415~416),企業戶/個人戶(417~417),產品別(418~418)";
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
					} // 借款人ID/統編
					if (j == 3) {
						strField = makeFile.fillStringL(strField, 3, '0');
					} // 額度編號
					if (j == 4) {
						strField = makeFile.fillStringL(strField, 3, '0');
					} // 撥款序號
					if (j == 5) {
						strField = makeFile.fillStringR(strField, 8, ' ');
					} // 會計科目
					if (j == 6) {
						strField = makeFile.fillStringL(strField, 1, '0');
					} // 案件狀態
					if (j == 7) {
						strField = makeFile.fillStringL(strField, 8, '0');
					} // 初貸日期
					if (j == 8) {
						strField = makeFile.fillStringL(strField, 8, '0');
					} // 貸放日期
					if (j == 9) {
						strField = makeFile.fillStringL(strField, 8, '0');
					} // 到期日
					if (j == 10) {
						DecimalFormat formatter = new DecimalFormat("0.00");
						strField = formatter.format(Float.parseFloat(strField = (strField.isEmpty() ? "0" : strField)));
						strField = makeFile.fillStringL(strField, 13, '0');
					} // 核准金額(台幣)
					if (j == 11) {
						DecimalFormat formatter = new DecimalFormat("0.00");
						strField = formatter.format(Float.parseFloat(strField = (strField.isEmpty() ? "0" : strField)));
						strField = makeFile.fillStringL(strField, 13, '0');
					} // 撥款金額(台幣)
					if (j == 12) {
						DecimalFormat formatter = new DecimalFormat("0.00");
						strField = formatter.format(Float.parseFloat(strField = (strField.isEmpty() ? "0" : strField)));
						strField = makeFile.fillStringL(strField, 13, '0');
					} // 本金餘額(撥款)(台幣)
					if (j == 13) {
						DecimalFormat formatter = new DecimalFormat("0.00");
						strField = formatter.format(Float.parseFloat(strField = (strField.isEmpty() ? "0" : strField)));
						strField = makeFile.fillStringL(strField, 13, '0');
					} // 應收利息(台幣)
					if (j == 14) {
						DecimalFormat formatter = new DecimalFormat("0.00");
						strField = formatter.format(Float.parseFloat(strField = (strField.isEmpty() ? "0" : strField)));
						strField = makeFile.fillStringL(strField, 13, '0');
					} // 法拍及火險費用(台幣)
					if (j == 15) {
						strField = makeFile.fillStringL(strField, 4, '0');
					} // 逾期繳款天數
					if (j == 16) {
						strField = makeFile.fillStringL(strField, 8, '0');
					} // 轉催收款日期
					if (j == 17) {
						strField = makeFile.fillStringL(strField, 8, '0');
					} // 轉銷呆帳日期
					if (j == 18) {
						DecimalFormat formatter = new DecimalFormat("0.00");
						strField = formatter.format(Float.parseFloat(strField = (strField.isEmpty() ? "0" : strField)));
						strField = makeFile.fillStringL(strField, 13, '0');
					} // 轉銷呆帳金額
					if (j == 19) {
						strField = makeFile.fillStringL(strField, 8, '0');
					} // stage3發生日期
					if (j == 20) {
						DecimalFormat formatter = new DecimalFormat("0.000000");
						strField = formatter.format(Float.parseFloat(strField = (strField.isEmpty() ? "0" : strField)));
						strField = makeFile.fillStringL(strField, 9, '0');
					} // 上述發生日期前之最近一次利率
					if (j == 21) {
						DecimalFormat formatter = new DecimalFormat("0.00");
						strField = formatter.format(Float.parseFloat(strField = (strField.isEmpty() ? "0" : strField)));
						strField = makeFile.fillStringL(strField, 13, '0');
					} // 上述發生日期時之本金餘額(台幣)
					if (j == 22) {
						DecimalFormat formatter = new DecimalFormat("0.00");
						strField = formatter.format(Float.parseFloat(strField = (strField.isEmpty() ? "0" : strField)));
						strField = makeFile.fillStringL(strField, 13, '0');
					} // 上述發生日期時之應收利息(台幣)
					if (j == 23) {
						DecimalFormat formatter = new DecimalFormat("0.00");
						strField = formatter.format(Float.parseFloat(strField = (strField.isEmpty() ? "0" : strField)));
						strField = makeFile.fillStringL(strField, 13, '0');
					} // 上述發生日期時之法拍及火險費用(台幣)
					if (j == 24) {
						DecimalFormat formatter = new DecimalFormat("0.00");
						strField = formatter.format(Float.parseFloat(strField = (strField.isEmpty() ? "0" : strField)));
						strField = makeFile.fillStringL(strField, 13, '0');
					} // stage3發生後第一年本金回收金額(台幣)
					if (j == 25) {
						DecimalFormat formatter = new DecimalFormat("0.00");
						strField = formatter.format(Float.parseFloat(strField = (strField.isEmpty() ? "0" : strField)));
						strField = makeFile.fillStringL(strField, 13, '0');
					} // stage3發生後第二年本金回收金額(台幣)
					if (j == 26) {
						DecimalFormat formatter = new DecimalFormat("0.00");
						strField = formatter.format(Float.parseFloat(strField = (strField.isEmpty() ? "0" : strField)));
						strField = makeFile.fillStringL(strField, 13, '0');
					} // stage3發生後第三年本金回收金額(台幣)
					if (j == 27) {
						DecimalFormat formatter = new DecimalFormat("0.00");
						strField = formatter.format(Float.parseFloat(strField = (strField.isEmpty() ? "0" : strField)));
						strField = makeFile.fillStringL(strField, 13, '0');
					} // stage3發生後第四年本金回收金額(台幣)
					if (j == 28) {
						DecimalFormat formatter = new DecimalFormat("0.00");
						strField = formatter.format(Float.parseFloat(strField = (strField.isEmpty() ? "0" : strField)));
						strField = makeFile.fillStringL(strField, 13, '0');
					} // stage3發生後第五年本金回收金額(台幣)
					if (j == 29) {
						DecimalFormat formatter = new DecimalFormat("0.00");
						strField = formatter.format(Float.parseFloat(strField = (strField.isEmpty() ? "0" : strField)));
						strField = makeFile.fillStringL(strField, 13, '0');
					} // stage3發生後第一年應收利息回收金額(台幣)
					if (j == 30) {
						DecimalFormat formatter = new DecimalFormat("0.00");
						strField = formatter.format(Float.parseFloat(strField = (strField.isEmpty() ? "0" : strField)));
						strField = makeFile.fillStringL(strField, 13, '0');
					} // stage3發生後第二年應收利息回收金額(台幣)
					if (j == 31) {
						DecimalFormat formatter = new DecimalFormat("0.00");
						strField = formatter.format(Float.parseFloat(strField = (strField.isEmpty() ? "0" : strField)));
						strField = makeFile.fillStringL(strField, 13, '0');
					} // stage3發生後第三年應收利息回收金額(台幣)
					if (j == 32) {
						DecimalFormat formatter = new DecimalFormat("0.00");
						strField = formatter.format(Float.parseFloat(strField = (strField.isEmpty() ? "0" : strField)));
						strField = makeFile.fillStringL(strField, 13, '0');
					} // stage3發生後第四年應收利息回收金額(台幣)
					if (j == 33) {
						DecimalFormat formatter = new DecimalFormat("0.00");
						strField = formatter.format(Float.parseFloat(strField = (strField.isEmpty() ? "0" : strField)));
						strField = makeFile.fillStringL(strField, 13, '0');
					} // stage3發生後第五年應收利息回收金額(台幣)
					if (j == 34) {
						DecimalFormat formatter = new DecimalFormat("0.00");
						strField = formatter.format(Float.parseFloat(strField = (strField.isEmpty() ? "0" : strField)));
						strField = makeFile.fillStringL(strField, 13, '0');
					} // stage3發生後第一年法拍及火險費用回收金額(台幣)
					if (j == 35) {
						DecimalFormat formatter = new DecimalFormat("0.00");
						strField = formatter.format(Float.parseFloat(strField = (strField.isEmpty() ? "0" : strField)));
						strField = makeFile.fillStringL(strField, 13, '0');
					} // stage3發生後第二年法拍及火險費用回收金額(台幣)
					if (j == 36) {
						DecimalFormat formatter = new DecimalFormat("0.00");
						strField = formatter.format(Float.parseFloat(strField = (strField.isEmpty() ? "0" : strField)));
						strField = makeFile.fillStringL(strField, 13, '0');
					} // stage3發生後第三年法拍及火險費用回收金額(台幣)
					if (j == 37) {
						DecimalFormat formatter = new DecimalFormat("0.00");
						strField = formatter.format(Float.parseFloat(strField = (strField.isEmpty() ? "0" : strField)));
						strField = makeFile.fillStringL(strField, 13, '0');
					} // stage3發生後第四年法拍及火險費用回收金額(台幣)
					if (j == 38) {
						DecimalFormat formatter = new DecimalFormat("0.00");
						strField = formatter.format(Float.parseFloat(strField = (strField.isEmpty() ? "0" : strField)));
						strField = makeFile.fillStringL(strField, 13, '0');
					} // stage3發生後第五年法拍及火險費用回收金額(台幣)
					if (j == 39) {
						strField = makeFile.fillStringR(strField, 6, ' ');
					} // 授信行業別
					if (j == 40) {
						strField = makeFile.fillStringR(strField, 2, ' ');
					} // 擔保品類別
					if (j == 41) {
						strField = makeFile.fillStringR(strField, 1, ' ');
					} // 擔保品地區別
					if (j == 42) {
						strField = makeFile.fillStringR(strField, 2, ' ');
					} // 商品利率代碼
					if (j == 43) {
						strField = makeFile.fillStringL(strField, 1, '0');
					} // 企業戶/個人戶
					if (j == 44) {
						strField = makeFile.fillStringR(strField, 1, ' ');
					} // 產品別

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
			this.info("L7902ServiceImpl.genFile error = " + errors.toString());
		}
	}
}
