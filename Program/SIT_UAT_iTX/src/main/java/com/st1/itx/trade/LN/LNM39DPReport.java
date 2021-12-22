package com.st1.itx.trade.LN;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LNM39DPServiceImpl;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.MakeFile;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Calendar;
import java.text.SimpleDateFormat;

@Component("lNM39DPReport")
@Scope("prototype")

public class LNM39DPReport extends MakeReport {

	Date dateNow = new Date();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	String strToday = String.valueOf(Integer.parseInt(sdf.format(dateNow)) - 19110000); // 民國年

	Calendar calendar = Calendar.getInstance();
	String monthlyEndDay = "00000000"; // 會計日當月的月底日曆日

	@Autowired
	public LNM39DPServiceImpl lNM39DPServiceImpl;

	@Autowired
	public MakeFile makeFile;

	@Autowired
	@Qualifier("makeFile")
	public MakeFile makeFileC;

	// 自訂明細標題
	@Override
	public void printTitle() {
		this.info("printTitle nowRow = " + this.NowRow);
	}

	public boolean exec(TitaVo titaVo) throws LogicException {
		// LNM39DP 欄位清單４
		this.info("---------- LNM39DPReport exec titaVo: " + titaVo);

		List<Map<String, String>> LNM39DPList = null;
		try {
			LNM39DPList = lNM39DPServiceImpl.findAll(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("LNM39DPReport LNM39DPServiceImpl.findAll error = " + errors.toString());
			return false;
		}

		try {
			// txt
			genFile(titaVo, LNM39DPList);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("LNM39DPReport.genFile error = " + errors.toString());
			return false;
		}

		return true;
	}

	private void genFile(TitaVo titaVo, List<Map<String, String>> L7List) throws LogicException {
		this.info("=========== LNM39DP genFile : ");
		String txt = "F0;F1;F2;F3;F4;F5;F6;F7;F8;F9;F10;F11;F12;F13;F14;F15;F16;F17;F18;F19;F20;F21;F22;F23;F24;" + "F25;F26;F27;F28;F29;F30;F31;F32;F33;F34;F35;F36;F37;F38;F39;F40;F41;F42;F43";
		String txt1[] = txt.split(";");
		DecimalFormat formatter = new DecimalFormat("0");

		try {
			String strContent = "";

			makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LNFDP", "LNM39DP 欄位清單４", "LNFDP.TXT", 1); // UTF-8
			// 產製[控制檔]
			makeFileC.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LNFDPIDX", "LNM39DP 欄位清單４控制檔", "LNFDP.IDX", 1); // UTF-8

			// 標題列
			// strContent = "";
			// makeFile.put(strContent);

			if (L7List.size() == 0) { // 無資料時，會出空檔

			} else {
				// 欄位內容
				for (Map<String, String> tL7Vo : L7List) {
					strContent = "";
					for (int j = 1; j <= 44; j++) {
						String strField = "";
						if (tL7Vo.get(txt1[j - 1]) == null) {
							strField = "";
						} else {
							strField = tL7Vo.get(txt1[j - 1]).replace(",", "，").trim(); // csv檔: 逗號轉全形
							strField = tL7Vo.get(txt1[j - 1]).trim();
						}

						// 格式處理
						switch (j) {
						case 1:
							strField = makeFile.fillStringL(strField, 7, '0');
							break; // 戶號
						case 2:
							strField = makeFile.fillStringR(" ", 10, ' ');
							break; // 借款人ID / 統編 (空白)
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
							strField = new BigDecimal(strField).setScale(0,BigDecimal.ROUND_HALF_UP).toString();
							strField = makeFile.fillStringL(strField, 11, '0');
							break; // 核准金額
						case 11:
							formatter.applyPattern("00000000000");
							strField = new BigDecimal(strField).setScale(0,BigDecimal.ROUND_HALF_UP).toString();
							strField = makeFile.fillStringL(strField, 11, '0');
							break; // 撥款金額
						case 12:
							formatter.applyPattern("00000000000");
							strField = new BigDecimal(strField).setScale(0,BigDecimal.ROUND_HALF_UP).toString();
							strField = makeFile.fillStringL(strField, 11, '0');
							break; // 本金餘額(撥款)
						case 13:
							formatter.applyPattern("00000000000");
							strField = new BigDecimal(strField).setScale(0,BigDecimal.ROUND_HALF_UP).toString();
							strField = makeFile.fillStringL(strField, 11, '0');
							break; // 應收利息
						case 14:
							formatter.applyPattern("00000000");
							strField = new BigDecimal(strField).setScale(0,BigDecimal.ROUND_HALF_UP).toString();
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
							strField = new BigDecimal(strField).setScale(0,BigDecimal.ROUND_HALF_UP).toString();
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
							strField = new BigDecimal(strField).setScale(0,BigDecimal.ROUND_HALF_UP).toString();
							strField = makeFile.fillStringL(strField, 11, '0');
							break; // 上述發生日期時之本金餘額
						case 22:
							formatter.applyPattern("00000000000");
							strField = new BigDecimal(strField).setScale(0,BigDecimal.ROUND_HALF_UP).toString();
							strField = makeFile.fillStringL(strField, 11, '0');
							break; // 上述發生日期時之應收利息
						case 23:
							formatter.applyPattern("00000000");
							strField = new BigDecimal(strField).setScale(0,BigDecimal.ROUND_HALF_UP).toString();
							strField = makeFile.fillStringL(strField, 8, '0');
							break; // 上述發生日期時之法拍及火險費用
						case 24:
							formatter.applyPattern("00000000000");
							strField = new BigDecimal(strField).setScale(0,BigDecimal.ROUND_HALF_UP).toString();
							strField = makeFile.fillStringL(strField, 11, '0');
							break; // 個案減損客觀證據發生後第一年本金回收金額
						case 25:
							formatter.applyPattern("00000000000");
							strField = new BigDecimal(strField).setScale(0,BigDecimal.ROUND_HALF_UP).toString();
							strField = makeFile.fillStringL(strField, 11, '0');
							break; // 個案減損客觀證據發生後第二年本金回收金額
						case 26:
							formatter.applyPattern("00000000000");
							strField = new BigDecimal(strField).setScale(0,BigDecimal.ROUND_HALF_UP).toString();
							strField = makeFile.fillStringL(strField, 11, '0');
							break; // 個案減損客觀證據發生後第三年本金回收金額
						case 27:
							formatter.applyPattern("00000000000");
							strField = new BigDecimal(strField).setScale(0,BigDecimal.ROUND_HALF_UP).toString();
							strField = makeFile.fillStringL(strField, 11, '0');
							break; // 個案減損客觀證據發生後第四年本金回收金額
						case 28:
							formatter.applyPattern("00000000000");
							strField = new BigDecimal(strField).setScale(0,BigDecimal.ROUND_HALF_UP).toString();
							strField = makeFile.fillStringL(strField, 11, '0');
							break; // 個案減損客觀證據發生後第五年本金回收金額
						case 29:
							formatter.applyPattern("00000000000");
							strField = new BigDecimal(strField).setScale(0,BigDecimal.ROUND_HALF_UP).toString();;
							strField = makeFile.fillStringL(strField, 11, '0');
							break; // 個案減損客觀證據發生後第一年應收利息回收金額
						case 30:
							formatter.applyPattern("00000000000");
							strField = new BigDecimal(strField).setScale(0,BigDecimal.ROUND_HALF_UP).toString();
							strField = makeFile.fillStringL(strField, 11, '0');
							break; // 個案減損客觀證據發生後第二年應收利息回收金額
						case 31:
							formatter.applyPattern("00000000000");
							strField = new BigDecimal(strField).setScale(0,BigDecimal.ROUND_HALF_UP).toString();
							strField = makeFile.fillStringL(strField, 11, '0');
							break; // 個案減損客觀證據發生後第三年應收利息回收金額
						case 32:
							formatter.applyPattern("00000000000");
							strField = new BigDecimal(strField).setScale(0,BigDecimal.ROUND_HALF_UP).toString();
							strField = makeFile.fillStringL(strField, 11, '0');
							break; // 個案減損客觀證據發生後第四年應收利息回收金額
						case 33:
							formatter.applyPattern("00000000000");
							strField = new BigDecimal(strField).setScale(0,BigDecimal.ROUND_HALF_UP).toString();
							strField = makeFile.fillStringL(strField, 11, '0');
							break; // 個案減損客觀證據發生後第五年應收利息回收金額
						case 34:
							formatter.applyPattern("00000000");
							strField = new BigDecimal(strField).setScale(0,BigDecimal.ROUND_HALF_UP).toString();
							strField = makeFile.fillStringL(strField, 8, '0');
							break; // 個案減損客觀證據發生後第一年法拍及火險費用回收金額
						case 35:
							formatter.applyPattern("00000000");
							strField = new BigDecimal(strField).setScale(0,BigDecimal.ROUND_HALF_UP).toString();
							strField = makeFile.fillStringL(strField, 8, '0');
							break; // 個案減損客觀證據發生後第二年法拍及火險費用回收金額
						case 36:
							formatter.applyPattern("00000000");
							strField = new BigDecimal(strField).setScale(0,BigDecimal.ROUND_HALF_UP).toString();
							strField = makeFile.fillStringL(strField, 8, '0');
							break; // 個案減損客觀證據發生後第三年法拍及火險費用回收金額
						case 37:
							formatter.applyPattern("00000000");
							strField = new BigDecimal(strField).setScale(0,BigDecimal.ROUND_HALF_UP).toString();
							strField = makeFile.fillStringL(strField, 8, '0');
							break; // 個案減損客觀證據發生後第四年法拍及火險費用回收金額
						case 38:
							formatter.applyPattern("00000000");
							strField = new BigDecimal(strField).setScale(0,BigDecimal.ROUND_HALF_UP).toString();
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
							strField = makeFile.fillStringR(strField, 5, ' ');
							break; // 商品利率代碼
						case 43:
							strField = makeFile.fillStringL(strField, 1, '0');
							break; // 企業戶/個人戶
						case 44:
							strField = makeFile.fillStringR(strField, 2, ' ');
							break; // 產品別
						default:
							strField = "";
							break;
						}
						strContent = strContent + strField;
						if (j != 44) {
							strContent = strContent + ",";
						}
					}
					makeFile.put(strContent);
				}
			}

			// makeFile.toFile(sno); // 不直接下傳

			strContent = sdf.format(dateNow) + "," + calendarEntDyMonthlyEndDate(titaVo) + "," + String.format("%06d", L7List.size());
			makeFileC.put(strContent);
			makeFile.close();
			this.info("=========== LNM39DP genFile close === ");
			makeFileC.close();
			this.info("=========== LNM39DP genFile IDX close === ");
			// makeFile.toFile(snoIdx); // 不直接下傳

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LNM39DPServiceImpl.genFile error = " + errors.toString());
		}
	}

	private String calendarEntDyMonthlyEndDate(TitaVo titaVo) throws LogicException {
		// 會計日當月的月底日曆日
		try {
			this.info("=========== titaVo.getEntDy()=" + titaVo.getEntDy());
			Date dateEntDy = sdf.parse(String.valueOf(Integer.parseInt(titaVo.getEntDy()) + 19110000));
			calendar.setTime(dateEntDy);
			// 月份+1，天設置為0。下個月第0天，就是這個月最後一天
			calendar.add(Calendar.MONTH, 1);
			calendar.set(Calendar.DAY_OF_MONTH, 0);
			monthlyEndDay = sdf.format(calendar.getTime());
			this.info("=========== lastMonthlyDay=" + monthlyEndDay);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return monthlyEndDay;
	}

}
