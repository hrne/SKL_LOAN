package com.st1.itx.trade.LN;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LNM39APServiceImpl;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.MakeFile;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Calendar;
import java.text.SimpleDateFormat;

@Component("lNM39APReport")
@Scope("prototype")

public class LNM39APReport extends MakeReport {

	Date dateNow = new Date();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	String strToday = String.valueOf(Integer.parseInt(sdf.format(dateNow)) - 19110000); // 民國年

	Calendar calendar = Calendar.getInstance();
	String monthlyEndDay = "00000000"; // 會計日當月的月底日曆日

	@Autowired
	public LNM39APServiceImpl lNM39APServiceImpl;


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

	public void exec(TitaVo titaVo) throws LogicException {
		// LNM39AP 欄位清單１
		try {
			this.info("---------- LNM39APReport exec titaVo: " + titaVo);
			List<Map<String, String>> LNM39APList = lNM39APServiceImpl.findAll(titaVo);
			genFile(titaVo, LNM39APList);
//			genExcel(titaVo, LNM39APList);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LNM39APServiceImpl.findAll error = " + errors.toString());
		}
	}

	private void genFile(TitaVo titaVo, List<Map<String, String>> L7List) throws LogicException {
		this.info("=========== LNM39AP genFile : ");
		String txt = "F0;F1;F2;F3;F4;F5;F6;F7;F8;F9;F10;F11;F12;F13;F14;F15;F16;F17;F18;F19;" + "F20;F21;F22;F23;F24;F25;F26;F27;F28;F29;F30;F31;F32;F33;F34;F35;F36;F37;F38;F39;"
				+ "F40;F41;F42;F43;F44;F45;F46;F47;F48;F49;F50;F51;F52;F53";

		String txt1[] = txt.split(";");
		DecimalFormat formatter = new DecimalFormat("0");

		try {
			String strContent = "";

			makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LNFAP", "LNM39AP 欄位清單１", "LNFAP.TXT", 1); // UTF-8
			// 產製[控制檔]
			makeFileC.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LNFAPIDX", "LNM39AP 欄位清單１控制檔", "LNFAP.IDX", 1); // UTF-8

			// 標題列
			// strContent = "戶號(1~7), ";
			// makeFile.put(strContent);

			if (L7List.size() == 0) { // 無資料時，會出空檔

			} else {
				// 欄位內容
				for (Map<String, String> tL7Vo : L7List) {
					strContent = "";
					for (int j = 1; j <= 54; j++) {
						String strField = "";
						if (tL7Vo.get(txt1[j - 1]) == null) {
							strField = "";
						} else {
							strField = tL7Vo.get(txt1[j - 1]).replace(",", "，"); // 逗號轉全形
						}

						// 格式處理(直接搬值，不刪除前後空白者)
						if (j == 6) { // 會計科目(舊:8碼/新:11碼)
						} else {
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
							strField = makeFile.fillStringL(strField, 7, '0');
							break; // 核准號碼
						case 5:
							strField = makeFile.fillStringL(strField, 3, '0');
							break; // 撥款序號
						case 6:
							break; // 會計科目(舊:8碼/新:11碼) (直接搬值)
						case 7:
							strField = makeFile.fillStringL(strField, 1, '0');
							break; // 戶況
						case 8:
							strField = makeFile.fillStringL(strField, 8, '0');
							break; // 初貸日期
						case 9:
							strField = makeFile.fillStringL(strField, 8, '0');
							break; // 撥款日期
						case 10:
							strField = makeFile.fillStringL(strField, 8, '0');
							break; // 到期日(額度)
						case 11:
							strField = makeFile.fillStringL(strField, 8, '0');
							break; // 到期日(撥款)
						case 12:
							strField = makeFile.fillStringL(strField, 11, '0');
							break; // 核准金額
						case 13:
							strField = makeFile.fillStringL(strField, 11, '0');
							break; // 撥款金額
						case 14:
							strField = makeFile.fillStringL(strField, 5, '0');
							break; // 帳管費
						case 15:
							strField = makeFile.fillStringL(strField, 11, '0');
							break; // 本金餘額(撥款)
						case 16:
							strField = makeFile.fillStringL(strField, 11, '0');
							break; // 應收利息
						case 17:
							strField = makeFile.fillStringL(strField, 8, '0');
							break; // 法拍及火險費用
						case 18:
							formatter.applyPattern("0.000000");
							strField = formatter.format(Float.parseFloat(strField = (strField.isEmpty() ? "0" : strField)));
							strField = makeFile.fillStringL(strField, 8, '0');
							break; // 利率(撥款)
						case 19:
							strField = makeFile.fillStringL(strField, 4, '0');
							break; // 逾期繳款天數
						case 20:
							strField = makeFile.fillStringL(strField, 8, '0');
							break; // 轉催收款日期
						case 21:
							strField = makeFile.fillStringL(strField, 8, '0');
							break; // 轉銷呆帳日期
						case 22:
							strField = makeFile.fillStringL(strField, 11, '0');
							break; // 轉銷呆帳金額
						case 23:
							strField = makeFile.fillStringL(strField, 3, '0');
							break; // 初貸時約定還本寬限期
						case 24:
							formatter.applyPattern("0.000000");
							strField = formatter.format(Float.parseFloat(strField = (strField.isEmpty() ? "0" : strField)));
							strField = makeFile.fillStringL(strField, 8, '0');
							break; // 核准利率
						case 25:
							strField = makeFile.fillStringL(strField, 1, '0');
							break; // 契約當時還款方式
						case 26:
							strField = makeFile.fillStringL(strField, 1, '0');
							break; // 契約當時利率調整方式
						case 27:
							strField = makeFile.fillStringL(strField, 2, '0');
							break; // 契約約定當時還本週期
						case 28:
							strField = makeFile.fillStringL(strField, 2, '0');
							break; // 契約約定當時繳息週期
						case 29:
							strField = makeFile.fillStringR(strField, 6, ' ');
							break; // 授信行業別
						case 30:
							strField = makeFile.fillStringR(strField, 2, ' ');
							break; // 擔保品類別
						case 31:
							strField = makeFile.fillStringR(strField, 3, ' ');
							break; // 擔保品地區別
						case 32:
							strField = makeFile.fillStringR(strField, 5, ' ');
							break; // 商品利率代碼
						case 33:
							strField = makeFile.fillStringL(strField, 1, '0');
							break; // 企業戶/個人戶
						case 34:
							strField = makeFile.fillStringL(strField, 1, '0');
							break; // 五類資產分類
						case 35:
							strField = makeFile.fillStringR(strField, 2, ' ');
							break; // 產品別
						case 36:
							strField = makeFile.fillStringL(strField, 11, '0');
							break; // 原始鑑價金額
						case 37:
							strField = makeFile.fillStringL(strField, 8, '0');
							break; // 首次應繳日
						case 38:
							strField = makeFile.fillStringL(strField, 3, '0');
							break; // 總期數
						case 39:
							strField = makeFile.fillStringL(strField, 11, '0');
							break; // 可動用餘額(台幣)
						case 40:
							strField = makeFile.fillStringR(strField, 1, ' ');
							break; // 該筆額度是否可循環動用
						case 41:
							strField = makeFile.fillStringR(strField, 1, ' ');
							break; // 該筆額度是否為不可徹銷
						case 42:
							strField = makeFile.fillStringL(strField, 11, '0');
							break; // 暫收款金額(台幣)
						case 43:
							strField = makeFile.fillStringL(strField, 1, '0');
							break; // 記帳幣別
						case 44:
							strField = makeFile.fillStringR(strField, 1, ' ');
							break; // 會計帳冊
						case 45:
							strField = makeFile.fillStringR(strField, 4, ' ');
							break; // 交易幣別
						case 46:
							formatter.applyPattern("00.00000000");
							strField = formatter.format(Float.parseFloat(strField = (strField.isEmpty() ? "0" : strField)));
							strField = makeFile.fillStringL(strField, 11, '0');
							break; // 報導日匯率
						case 47:
							strField = makeFile.fillStringL(strField, 11, '0');
							break; // 核准金額(交易幣)
						case 48:
							strField = makeFile.fillStringL(strField, 11, '0');
							break; // 撥款金額(交易幣)
						case 49:
							strField = makeFile.fillStringL(strField, 5, '0');
							break; // 帳管費(交易幣)
						case 50:
							strField = makeFile.fillStringL(strField, 11, '0');
							break; // 本金餘額(撥款)(交易幣)
						case 51:
							strField = makeFile.fillStringL(strField, 11, '0');
							break; // 應收利息(交易幣)
						case 52:
							strField = makeFile.fillStringL(strField, 8, '0');
							break; // 法拍及火險費用(交易幣)
						case 53:
							strField = makeFile.fillStringL(strField, 11, '0');
							break; // 可動用餘額(交易幣)
						case 54:
							strField = makeFile.fillStringL(strField, 11, '0');
							break; // 暫收款金額(交易幣)
						default:
							strField = "";
							break;
						}
						strContent = strContent + strField;
						if (j != 54) {
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
			this.info("=========== LNM39AP genFile close === ");
			makeFileC.close();
			this.info("=========== LNM39AP genFile IDX close === ");
			// makeFile.toFile(snoIdx); // 不直接下傳

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("L7001ServiceImpl.genFile error = " + errors.toString());
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
