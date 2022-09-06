package com.st1.itx.trade.LN;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LNM34DPServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.MakeFile;

import java.text.DecimalFormat;
import java.util.Date;
import java.text.SimpleDateFormat;

@Component("lNM34DPReport")
@Scope("prototype")

public class LNM34DPReport extends MakeReport {

	Date dateNow = new Date();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	String strToday = String.valueOf(Integer.parseInt(sdf.format(dateNow)) - 19110000); // 民國年

	@Autowired
	public LNM34DPServiceImpl lNM34DPServiceImpl;

	@Autowired
	public MakeExcel makeExcel;

	@Autowired
	public MakeFile makeFile;

	// 自訂明細標題
	@Override
	public void printTitle() {
		this.info("printTitle nowRow = " + this.NowRow);
	}

	public boolean exec(TitaVo titaVo) throws LogicException {
		// LNM34DP 資料欄位清單D
		this.info("---------- LNM34DPReport exec titaVo: " + titaVo);

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

		List<Map<String, String>> LNM34DPList = null;
		try {
			LNM34DPList = lNM34DPServiceImpl.findAll(dataMonth, titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("LNM34DPReport LNM34DPServiceImpl.findAll error = " + errors.toString());
			return false;
		}

		try {
			// excel-CSV
			genFile(titaVo, LNM34DPList);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("LNM34DPReport.genFile error = " + errors.toString());
			return false;
		}

		return true;
	}

	private void genFile(TitaVo titaVo, List<Map<String, String>> L7List) throws LogicException {
		this.info("=========== LNM34DP genFile : ");
		boolean isNewForm = false; // 格式:false舊格式,true新格式
		String txt = "F0;F1;F2;F3;F4;F5;F6;F7;F8;F9;F10;F11;F12;F13;F14;F15;F16;F17;F18;F19;F20;F21;F22;F23;F24;" + "F25;F26;F27;F28;F29;F30;F31;F32;F33;F34;F35;F36;F37;F38;F39;F40;F41;F42;F43";
		String txt1[] = txt.split(";");

		try {
			DecimalFormat formatter = new DecimalFormat("0");
			String strContent = "";

			makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LNM34DP", "IAS39 資料欄位清單D", "LNM34DP.csv", 2);

			// 標題列
			// strContent = "戶號(1~7),借款人ID/統編(8~17),額度編號(18~20),撥款序號(21~23),會計科目(24~31)," +
			// "案件狀態(32~32),初貸日期(33~40),貸放日期(41~48),到期日(49~56),"
			// + "核准金額(57~67),撥款金額(68~78),本金餘額(撥款)(79~89),應收利息(90~100),法拍及火險費用(101~108)," +
			// "逾期繳款天數(109~112),轉催收款日期(113~120),轉銷呆帳日期(121~128),轉銷呆帳金額(129~139),"
			// +
			// "個案減損客觀證據發生日期(140~147),上述發生日期前之最近一次利率(148~155),上述發生日期時之本金餘額(156~166),上述發生日期時之應收利息(167~177),上述發生日期時之法拍及火險費用(178~185),"
			// +
			// "個案減損客觀證據發生後第一年本金回收金額(186~196),個案減損客觀證據發生後第二年本金回收金額(197~207),個案減損客觀證據發生後第三年本金回收金額(208~218),個案減損客觀證據發生後第四年本金回收金額(219~229),個案減損客觀證據發生後第五年本金回收金額(230~240),"
			// +
			// "個案減損客觀證據發生後第一年應收利息回收金額(241~251),個案減損客觀證據發生後第二年應收利息回收金額(252~262),個案減損客觀證據發生後第三年應收利息回收金額(263~273),個案減損客觀證據發生後第四年應收利息回收金額(274~284),個案減損客觀證據發生後第五年應收利息回收金額(285~295),"
			// +
			// "個案減損客觀證據發生後第一年法拍及火險費用回收金額(296~303),個案減損客觀證據發生後第二年法拍及火險費用回收金額(304~311),個案減損客觀證據發生後第三年法拍及火險費用回收金額(312~319),個案減損客觀證據發生後第四年法拍及火險費用回收金額(320~327),個案減損客觀證據發生後第五年法拍及火險費用回收金額(328~335),"
			// +
			// "授信行業別(336~341),擔保品類別(342~343),擔保品地區別(344~346),商品利率代碼(347~348),企業戶/個人戶(349~349),產品別(350~351)";
			// makeFile.put(strContent);

			// 欄位內容
			// this.info("-----------------" + L7List);
			if (L7List.size() == 0) { // 無資料時，會出空檔

			} else {
				for (Map<String, String> tL7Vo : L7List) {
					strContent = "";
					for (int j = 1; j <= 44; j++) {
						String strField = "";
						if (tL7Vo.get(txt1[j - 1]) == null) {
							strField = "";
						} else {
							strField = tL7Vo.get(txt1[j - 1]).replace(",", "，"); // csv檔: 逗號轉全形
						}

						// 格式處理(直接搬值，不刪除前後空白者)
						if (j == 5) { // 會計科目(舊:8碼/新:11碼)
						} else {
							strField = tL7Vo.get(txt1[j - 1]).trim();
						}

						// 格式處理
						switch (j) {
						case 1:
							strField = makeFile.fillStringL(strField, 7, '0');
							break; // 戶號
						case 2:
							strField = makeFile.fillStringR(strField, 10, ' ');
							break; // 借款人ID / 統編
						case 3:
							strField = makeFile.fillStringL(strField, 3, '0');
							break; // 額度編號
						case 4:
							strField = makeFile.fillStringL(strField, 3, '0');
							break; // 撥款序號
						case 5:
							break; // 會計科目(舊:8碼/新:11碼) (直接搬值)
						case 6:
							strField = makeFile.fillStringL(strField, 1, '0');
							break; // 案件狀態
						case 7:
							strField = makeFile.fillStringL(strField, 8, '0');
							break; // 初貸日期
						case 8:
							strField = makeFile.fillStringL(strField, 8, '0');
							break; // 貸放日期
						case 9:
							strField = makeFile.fillStringL(strField, 8, '0');
							break; // 到期日
						case 10:
							formatter.applyPattern("00000000000");
							strField = new BigDecimal(strField).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
							strField = makeFile.fillStringL(strField, 11, '0');
							break; // 核准金額
						case 11:
							formatter.applyPattern("00000000000");
							strField = new BigDecimal(strField).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
							strField = makeFile.fillStringL(strField, 11, '0');
							break; // 撥款金額
						case 12:
							formatter.applyPattern("00000000000");
							strField = new BigDecimal(strField).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
							strField = makeFile.fillStringL(strField, 11, '0');
							break; // 本金餘額(撥款)
						case 13:
							formatter.applyPattern("00000000000");
							strField = new BigDecimal(strField).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
							strField = makeFile.fillStringL(strField, 11, '0');
							break; // 應收利息
						case 14:
							formatter.applyPattern("00000000");
							strField = new BigDecimal(strField).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
							strField = makeFile.fillStringL(strField, 8, '0');
							break; // 法拍及火險費用
						case 15:
							strField = makeFile.fillStringL(strField, 4, '0');
							break; // 逾期繳款天數
						case 16:
							strField = makeFile.fillStringL(strField, 8, '0');
							break; // 轉催收款日期
						case 17:
							strField = makeFile.fillStringL(strField, 8, '0');
							break; // 轉銷呆帳日期
						case 18:
							formatter.applyPattern("00000000000");
							strField = new BigDecimal(strField).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
							strField = makeFile.fillStringL(strField, 11, '0');
							break; // 轉銷呆帳金額
						case 19:
							strField = makeFile.fillStringL(strField, 8, '0');
							break; // 個案減損客觀證據發生日期
						case 20:
							formatter.applyPattern("0.000000");
							strField = formatter.format(Float.parseFloat(strField = (strField.isEmpty() ? "0" : strField)));
							strField = makeFile.fillStringL(strField, 8, '0');
							break; // 上述發生日期前之最近一次利率
						case 21:
							formatter.applyPattern("00000000000");
							strField = new BigDecimal(strField).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
							strField = makeFile.fillStringL(strField, 11, '0');
							break; // 上述發生日期時之本金餘額
						case 22:
							formatter.applyPattern("00000000000");
							strField = new BigDecimal(strField).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
							strField = makeFile.fillStringL(strField, 11, '0');
							break; // 上述發生日期時之應收利息
						case 23:
							formatter.applyPattern("00000000");
							strField = new BigDecimal(strField).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
							strField = makeFile.fillStringL(strField, 8, '0');
							break; // 上述發生日期時之法拍及火險費用
						case 24:
							formatter.applyPattern("00000000000");
							strField = new BigDecimal(strField).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
							strField = makeFile.fillStringL(strField, 11, '0');
							break; // 個案減損客觀證據發生後第一年本金回收金額
						case 25:
							formatter.applyPattern("00000000000");
							strField = new BigDecimal(strField).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
							strField = makeFile.fillStringL(strField, 11, '0');
							break; // 個案減損客觀證據發生後第二年本金回收金額
						case 26:
							formatter.applyPattern("00000000000");
							strField = new BigDecimal(strField).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
							strField = makeFile.fillStringL(strField, 11, '0');
							break; // 個案減損客觀證據發生後第三年本金回收金額
						case 27:
							formatter.applyPattern("00000000000");
							strField = new BigDecimal(strField).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
							strField = makeFile.fillStringL(strField, 11, '0');
							break; // 個案減損客觀證據發生後第四年本金回收金額
						case 28:
							formatter.applyPattern("00000000000");
							strField = new BigDecimal(strField).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
							strField = makeFile.fillStringL(strField, 11, '0');
							break; // 個案減損客觀證據發生後第五年本金回收金額
						case 29:
							formatter.applyPattern("00000000000");
							strField = new BigDecimal(strField).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
							strField = makeFile.fillStringL(strField, 11, '0');
							break; // 個案減損客觀證據發生後第一年應收利息回收金額
						case 30:
							formatter.applyPattern("00000000000");
							strField = new BigDecimal(strField).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
							strField = makeFile.fillStringL(strField, 11, '0');
							break; // 個案減損客觀證據發生後第二年應收利息回收金額
						case 31:
							formatter.applyPattern("00000000000");
							strField = new BigDecimal(strField).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
							strField = makeFile.fillStringL(strField, 11, '0');
							break; // 個案減損客觀證據發生後第三年應收利息回收金額
						case 32:
							formatter.applyPattern("00000000000");
							strField = new BigDecimal(strField).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
							strField = makeFile.fillStringL(strField, 11, '0');
							break; // 個案減損客觀證據發生後第四年應收利息回收金額
						case 33:
							formatter.applyPattern("00000000000");
							strField = new BigDecimal(strField).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
							strField = makeFile.fillStringL(strField, 11, '0');
							break; // 個案減損客觀證據發生後第五年應收利息回收金額
						case 34:
							formatter.applyPattern("00000000");
							strField = new BigDecimal(strField).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
							strField = makeFile.fillStringL(strField, 8, '0');
							break; // 個案減損客觀證據發生後第一年法拍及火險費用回收金額
						case 35:
							formatter.applyPattern("00000000");
							strField = new BigDecimal(strField).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
							strField = makeFile.fillStringL(strField, 8, '0');
							break; // 個案減損客觀證據發生後第二年法拍及火險費用回收金額
						case 36:
							formatter.applyPattern("00000000");
							strField = new BigDecimal(strField).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
							strField = makeFile.fillStringL(strField, 8, '0');
							break; // 個案減損客觀證據發生後第三年法拍及火險費用回收金額
						case 37:
							formatter.applyPattern("00000000");
							strField = new BigDecimal(strField).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
							strField = makeFile.fillStringL(strField, 8, '0');
							break; // 個案減損客觀證據發生後第四年法拍及火險費用回收金額
						case 38:
							formatter.applyPattern("00000000");
							strField = new BigDecimal(strField).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
							strField = makeFile.fillStringL(strField, 8, '0');
							break; // 個案減損客觀證據發生後第五年法拍及火險費用回收金額
						case 39:
							strField = makeFile.fillStringR(strField, 6, ' ');
							break; // 授信行業別
						case 40:
							strField = makeFile.fillStringR(strField, 2, ' ');
							break; // 擔保品類別
						case 41:
							strField = makeFile.fillStringR(strField, 3, ' ');
							break; // 擔保品地區別(擔保品郵遞區號)
						case 42:
							if (isNewForm == true) {
								strField = makeFile.fillStringR(strField, 5, ' ');
							} else {
								strField = makeFile.fillStringR(strField, 2, ' ');
							}
							break; // 商品利率代碼
						case 43:
							strField = makeFile.fillStringL(strField, 1, '0');
							break; // 企業戶/個人戶
						case 44:
							strField = makeFile.fillStringR(strField, 2, ' ');
							break; // 產品別
						}
						strContent = strContent + strField;
						if (j != 44) {
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
			this.info("LNM34DPServiceImpl.genFile (LNM34DPReport) error = " + errors.toString());
		}
	}
}
