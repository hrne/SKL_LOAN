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
import com.st1.itx.db.service.springjpa.cm.L7004ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.MakeFile;

import java.text.DecimalFormat;

@Component("L7004Report")
@Scope("prototype")

public class L7004Report extends MakeReport {
	// private static final Logger logger = LoggerFactory.getLogger(L7004Report.class);

	@Autowired
	public L7004ServiceImpl L7004ServiceImpl;

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
			List<HashMap<String, String>> L7004List = L7004ServiceImpl.findAll();
			if (L7004List.size() > 0) {
				genFile(titaVo, 0, L7004List);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("L7004ServiceImpl.findAll error = " + errors.toString());
		}
	}

	private void genFile(TitaVo titaVo, int fg, List<HashMap<String, String>> L7List) throws LogicException {
		this.info("=========== L7004 genFile");
		String txt = "F0;F1;F2;F3;F4;F5;F6;F7;F8;F9;F10;F11;F12;F13;F14;F15;F16;F17;F18;F19;F20;F21;F22;F23;F24;" + "F25;F26;F27;F28;F29;F30;F31;F32;F33;F34;F35;F36;F37;F38;F39;F40;F41;F42;F43";
		String txt1[] = txt.split(";");

		try {
			String strContent = "";

			makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), titaVo.getTxCode(), "IAS39 資料欄位清單D", "LNM34DP.csv", 2);

			// 標題列
			strContent = "戶號(1~7),借款人ID/統編(8~17),額度編號(18~20),撥款序號(21~23),會計科目(24~31),案件狀態(32~32),初貸日期(33~40),貸放日期(41~48),到期日(49~56),核准金額(57~67),撥款金額(68~78),本金餘額(撥款)(79~89),應收利息(90~100),法拍及火險費用(101~108),逾期繳款天數(109~112),轉催收款日期(113~120),轉銷呆帳日期(121~128),轉銷呆帳金額(129~139),"
					+ "個案減損客觀證據發生日期(140~147),上述發生日期前之最近一次利率(148~155),上述發生日期時之本金餘額(156~166),上述發生日期時之應收利息(167~177),上述發生日期時之法拍及火險費用(178~185),"
					+ "個案減損客觀證據發生後第一年本金回收金額(186~196),個案減損客觀證據發生後第二年本金回收金額(197~207),個案減損客觀證據發生後第三年本金回收金額(208~218),個案減損客觀證據發生後第四年本金回收金額(219~229),個案減損客觀證據發生後第五年本金回收金額(230~240),"
					+ "個案減損客觀證據發生後第一年應收利息回收金額(241~251),個案減損客觀證據發生後第二年應收利息回收金額(252~262),個案減損客觀證據發生後第三年應收利息回收金額(263~273),個案減損客觀證據發生後第四年應收利息回收金額(274~284),個案減損客觀證據發生後第五年應收利息回收金額(285~295),"
					+ "個案減損客觀證據發生後第一年法拍及火險費用回收金額(296~303),個案減損客觀證據發生後第二年法拍及火險費用回收金額(304~311),個案減損客觀證據發生後第三年法拍及火險費用回收金額(312~319),個案減損客觀證據發生後第四年法拍及火險費用回收金額(320~327),個案減損客觀證據發生後第五年法拍及火險費用回收金額(328~335),"
					+ "授信行業別(336~341),擔保品類別(342~343),擔保品地區別(344~346),商品利率代碼(347~348),企業戶/個人戶(349~349),產品別(350~351)";
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
						strField = makeFile.fillStringL(strField, 11, '0');
					} // 核准金額
					if (j == 11) {
						strField = makeFile.fillStringL(strField, 11, '0');
					} // 撥款金額
					if (j == 12) {
						strField = makeFile.fillStringL(strField, 11, '0');
					} // 本金餘額(撥款)
					if (j == 13) {
						strField = makeFile.fillStringL(strField, 11, '0');
					} // 應收利息
					if (j == 14) {
						strField = makeFile.fillStringL(strField, 8, '0');
					} // 法拍及火險費用
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
						strField = makeFile.fillStringL(strField, 11, '0');
					} // 轉銷呆帳金額
					if (j == 19) {
						strField = makeFile.fillStringL(strField, 8, '0');
					} // 個案減損客觀證據發生日期
					if (j == 20) {
						DecimalFormat formatter = new DecimalFormat("0.000000");
						strField = formatter.format(Float.parseFloat(strField = (strField.isEmpty() ? "0" : strField)) / 100);
						strField = makeFile.fillStringL(strField, 8, '0');
					} // 上述發生日期前之最近一次利率
					if (j == 21) {
						strField = makeFile.fillStringL(strField, 11, '0');
					} // 上述發生日期時之本金餘額
					if (j == 22) {
						strField = makeFile.fillStringL(strField, 11, '0');
					} // 上述發生日期時之應收利息
					if (j == 23) {
						strField = makeFile.fillStringL(strField, 8, '0');
					} // 上述發生日期時之法拍及火險費用
					if (j == 24) {
						strField = makeFile.fillStringL(strField, 11, '0');
					} // 個案減損客觀證據發生後第一年本金回收金額
					if (j == 25) {
						strField = makeFile.fillStringL(strField, 11, '0');
					} // 個案減損客觀證據發生後第二年本金回收金額
					if (j == 26) {
						strField = makeFile.fillStringL(strField, 11, '0');
					} // 個案減損客觀證據發生後第三年本金回收金額
					if (j == 27) {
						strField = makeFile.fillStringL(strField, 11, '0');
					} // 個案減損客觀證據發生後第四年本金回收金額
					if (j == 28) {
						strField = makeFile.fillStringL(strField, 11, '0');
					} // 個案減損客觀證據發生後第五年本金回收金額
					if (j == 29) {
						strField = makeFile.fillStringL(strField, 11, '0');
					} // 個案減損客觀證據發生後第一年應收利息回收金額
					if (j == 30) {
						strField = makeFile.fillStringL(strField, 11, '0');
					} // 個案減損客觀證據發生後第二年應收利息回收金額
					if (j == 31) {
						strField = makeFile.fillStringL(strField, 11, '0');
					} // 個案減損客觀證據發生後第三年應收利息回收金額
					if (j == 32) {
						strField = makeFile.fillStringL(strField, 11, '0');
					} // 個案減損客觀證據發生後第四年應收利息回收金額
					if (j == 33) {
						strField = makeFile.fillStringL(strField, 11, '0');
					} // 個案減損客觀證據發生後第五年應收利息回收金額
					if (j == 34) {
						strField = makeFile.fillStringL(strField, 8, '0');
					} // 個案減損客觀證據發生後第一年法拍及火險費用回收金額
					if (j == 35) {
						strField = makeFile.fillStringL(strField, 8, '0');
					} // 個案減損客觀證據發生後第二年法拍及火險費用回收金額
					if (j == 36) {
						strField = makeFile.fillStringL(strField, 8, '0');
					} // 個案減損客觀證據發生後第三年法拍及火險費用回收金額
					if (j == 37) {
						strField = makeFile.fillStringL(strField, 8, '0');
					} // 個案減損客觀證據發生後第四年法拍及火險費用回收金額
					if (j == 38) {
						strField = makeFile.fillStringL(strField, 8, '0');
					} // 個案減損客觀證據發生後第五年法拍及火險費用回收金額
					if (j == 39) {
						strField = makeFile.fillStringR(strField, 6, ' ');
					} // 授信行業別
					if (j == 40) {
						strField = makeFile.fillStringR(strField, 2, ' ');
					} // 擔保品類別
					if (j == 41) {
						strField = makeFile.fillStringR(strField, 3, ' ');
					} // 擔保品地區別
					if (j == 42) {
						strField = makeFile.fillStringR(strField, 2, ' ');
					} // 商品利率代碼
					if (j == 43) {
						strField = makeFile.fillStringL(strField, 1, '0');
					} // 企業戶/個人戶
					if (j == 44) {
						strField = makeFile.fillStringR(strField, 2, ' ');
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
			this.info("L7004ServiceImpl.genFile error = " + errors.toString());
		}
	}
}
