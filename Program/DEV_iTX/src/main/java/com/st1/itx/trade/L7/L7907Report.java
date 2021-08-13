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
import com.st1.itx.db.service.springjpa.cm.L7907ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.MakeFile;

import java.text.DecimalFormat;

@Component("L7907Report")
@Scope("prototype")

public class L7907Report extends MakeReport {
	// private static final Logger logger = LoggerFactory.getLogger(L7907Report.class);

	@Autowired
	public L7907ServiceImpl L7907ServiceImpl;

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
			List<HashMap<String, String>> L7907List = L7907ServiceImpl.findAll();
			if (L7907List.size() > 0) {
				// genFile(titaVo, 0, L7907List);
				genExcel(titaVo, 0, L7907List);
			}
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("L7907ServiceImpl.findAll error = " + errors.toString());
		}
	}

	private void genExcel(TitaVo titaVo, int fg, List<HashMap<String, String>> L7List) throws LogicException {
		this.info("=========== L7907 genExcel");
		// 自訂標題 inf
		String inf = "戶號;借款人ID/統編;額度編號;核准號碼;核准日期;初貸日期;核准金額(台幣);帳管費(台幣);法拍及火險費用(台幣);核准利率;初貸時約定還本寬限期;契約當時還款方式;契約當時利率調整方式;契約約定當時還本週期;契約約定當時繳息週期;授信行業別;擔保品類別;擔保品地區別;商品利率代碼;企業戶/個人戶;產品別;原始鑑價金額;可動用餘額;該筆額度是否可循環動用;該筆額度是否為不可徹銷;合約期限;備忘分錄會計科目;記帳幣別;會計帳冊;交易幣別;報導日匯率;核准金額(交易幣);帳管費(交易幣);法拍及火險費用(交易幣)";
		String txt = "F0;F1;F2;F3;F4;F5;F6;F7;F8;F9;F10;F11;F12;F13;F14;F15;F16;F17;F18;F19;F20;F21;F22;F23;F24;" + "F25;F26;F27;F28;F29;F30;F31;F32;F33";

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

			makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L7907", "IFRS9 資料欄位清單9", "IFRS9_09");

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
			this.info("L7907ServiceImpl.genExcel error = " + errors.toString());
		}
	}

	private void genFile(TitaVo titaVo, int fg, List<HashMap<String, String>> L7List) throws LogicException {
		this.info("=========== L7907 genFile");
		String txt = "F0;F1;F2;F3;F4;F5;F6;F7;F8;F9;F10;F11;F12;F13;F14;F15;F16;F17;F18;F19;F20;F21;F22;F23;F24;" + "F25;F26;F27;F28;F29;F30;F31;F32;F33";
		String txt1[] = txt.split(";");

		try {
			String strContent = "";

			makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), titaVo.getTxCode(), "IFRS9 資料欄位清單9", "IFRS9_09.csv", 2);

			// 標題列
			strContent = "戶號(1~7),借款人ID/統編(8~17),額度編號(18~20),核准號碼(21~27),核准日期(28~35),初貸日期(36~43),核准金額(台幣)(44~56),帳管費(台幣)(57~69),法拍及火險費用(台幣)(70~82),核准利率(83~91),初貸時約定還本寬限期(92~94),契約當時還款方式(95~95),契約當時利率調整方式(96~96),契約約定當時還本週期(97~98),契約約定當時繳息週期(99~100),授信行業別(101~106),擔保品類別(107~108),擔保品地區別(109~109),商品利率代碼(110~111),企業戶/個人戶(112~112),產品別(113~113),原始鑑價金額(114~126),可動用餘額(127~139),該筆額度是否可循環動用(140~140),該筆額度是否為不可徹銷(141~141),合約期限(142~149),備忘分錄會計科目(8碼)(150~157),記帳幣別(158~158),會計帳冊(159~159),交易幣別(160~163),報導日匯率(164~176),核准金額(交易幣)(177~189),帳管費(交易幣)(190~202),法拍及火險費用(交易幣)(203~215)";
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
						strField = makeFile.fillStringL(strField, 8, '0');
						if (strField.equals("00000000")) {
							strField = makeFile.fillStringR("", 8, ' ');
						}
					} // 核准日期
					if (j == 6) {
						strField = makeFile.fillStringL(strField, 8, '0');
						if (strField.equals("00000000")) {
							strField = makeFile.fillStringR("", 8, ' ');
						}
					} // 初貸日期
					if (j == 7) {
						DecimalFormat formatter = new DecimalFormat("0.00");
						strField = formatter.format(Float.parseFloat(strField = (strField.isEmpty() ? "0" : strField)));
						strField = makeFile.fillStringL(strField, 13, '0');
					} // 核准金額(台幣)
					if (j == 8) {
						DecimalFormat formatter = new DecimalFormat("0.00");
						strField = formatter.format(Float.parseFloat(strField = (strField.isEmpty() ? "0" : strField)));
						strField = makeFile.fillStringL(strField, 13, '0');
					} // 帳管費(台幣)
					if (j == 9) {
						DecimalFormat formatter = new DecimalFormat("0.00");
						strField = formatter.format(Float.parseFloat(strField = (strField.isEmpty() ? "0" : strField)));
						strField = makeFile.fillStringL(strField, 13, '0');
					} // 法拍及火險費用(台幣)
					if (j == 10) {
						DecimalFormat formatter = new DecimalFormat("0.000000");
						strField = formatter.format(Float.parseFloat(strField = (strField.isEmpty() ? "0" : strField)) / 100);
						strField = makeFile.fillStringL(strField, 9, '0');
					} // 核准利率
					if (j == 11) {
						strField = makeFile.fillStringL(strField, 3, '0');
					} // 初貸時約定還本寬限期
					if (j == 12) {
						strField = makeFile.fillStringL(strField, 1, '0');
					} // 契約當時還款方式
					if (j == 13) {
						strField = makeFile.fillStringL(strField, 1, '0');
					} // 契約當時利率調整方式
					if (j == 14) {
						strField = makeFile.fillStringL(strField, 2, '0');
					} // 契約約定當時還本週期
					if (j == 15) {
						strField = makeFile.fillStringL(strField, 2, '0');
					} // 契約約定當時繳息週期
					if (j == 16) {
						strField = makeFile.fillStringR(strField, 6, ' ');
					} // 授信行業別
					if (j == 17) {
						strField = makeFile.fillStringR(strField, 2, ' ');
					} // 擔保品類別
					if (j == 18) {
						strField = makeFile.fillStringR(strField, 1, ' ');
					} // 擔保品地區別
					if (j == 19) {
						strField = makeFile.fillStringR(strField, 2, ' ');
					} // 商品利率代碼
					if (j == 20) {
						strField = makeFile.fillStringL(strField, 1, '0');
					} // 企業戶/個人戶
					if (j == 21) {
						strField = makeFile.fillStringR(strField, 1, ' ');
					} // 產品別
					if (j == 22) {
						DecimalFormat formatter = new DecimalFormat("0.00");
						strField = formatter.format(Float.parseFloat(strField = (strField.isEmpty() ? "0" : strField)));
						strField = makeFile.fillStringL(strField, 13, '0');
					} // 原始鑑價金額
					if (j == 23) {
						DecimalFormat formatter = new DecimalFormat("0.00");
						strField = formatter.format(Float.parseFloat(strField = (strField.isEmpty() ? "0" : strField)));
						strField = makeFile.fillStringL(strField, 13, '0');
					} // 可動用餘額
					if (j == 24) {
						strField = makeFile.fillStringL(strField, 1, '0');
					} // 該筆額度是否可循環動用 1=是 0=否
					if (j == 25) {
						strField = makeFile.fillStringL(strField, 1, '0');
					} // 該筆額度是否為不可徹銷 1=是 0=否
					if (j == 26) {
						strField = makeFile.fillStringL(strField, 8, '0');
					} // 合約期限
					if (j == 27) {
						strField = makeFile.fillStringR(strField, 8, ' ');
					} // 備忘分錄會計科目(8碼)
					if (j == 28) {
						strField = makeFile.fillStringR(strField, 1, ' ');
					} // 記帳幣別
					if (j == 29) {
						strField = makeFile.fillStringR(strField, 1, ' ');
					} // 會計帳冊 1=一般 2=分紅 3=利變 4=OIU
					if (j == 30) {
						strField = makeFile.fillStringR(strField, 4, ' ');
					} // 交易幣別
					if (j == 31) {
						DecimalFormat formatter = new DecimalFormat("0.000000000");
						strField = formatter.format(Float.parseFloat(strField = (strField.isEmpty() ? "0" : strField)));
						strField = makeFile.fillStringL(strField, 13, '0');
					} // 報導日匯率
					if (j == 32) {
						DecimalFormat formatter = new DecimalFormat("0.00");
						strField = formatter.format(Float.parseFloat(strField = (strField.isEmpty() ? "0" : strField)));
						strField = makeFile.fillStringL(strField, 13, '0');
					} // 核准金額(交易幣)
					if (j == 33) {
						DecimalFormat formatter = new DecimalFormat("0.00");
						strField = formatter.format(Float.parseFloat(strField = (strField.isEmpty() ? "0" : strField)));
						strField = makeFile.fillStringL(strField, 13, '0');
					} // 帳管費(交易幣)
					if (j == 34) {
						DecimalFormat formatter = new DecimalFormat("0.00");
						strField = formatter.format(Float.parseFloat(strField = (strField.isEmpty() ? "0" : strField)));
						strField = makeFile.fillStringL(strField, 13, '0');
					} // 法拍及火險費用(交易幣)

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
			this.info("L7907ServiceImpl.genFile error = " + errors.toString());
		}
	}
}
