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
import com.st1.itx.db.service.springjpa.cm.LNM39GPServiceImpl;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.MakeFile;

import java.text.ParseException;
import java.util.Date;
import java.util.Calendar;
import java.text.SimpleDateFormat;

@Component("lNM39GPReport")
@Scope("prototype")

public class LNM39GPReport extends MakeReport {

	Date dateNow = new Date();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	String strToday = String.valueOf(Integer.parseInt(sdf.format(dateNow)) - 19110000); // 民國年

	Calendar calendar = Calendar.getInstance();
	String monthlyEndDay = "00000000"; // 會計日當月的月底日曆日

	@Autowired
	public LNM39GPServiceImpl lNM39GPServiceImpl;

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
		// LNM39GP 欄位清單７
		try {
			this.info("---------- LNM39GPReport exec titaVo: " + titaVo);
			List<Map<String, String>> LNM39GPList = lNM39GPServiceImpl.findAll(titaVo);
			genFile(titaVo, LNM39GPList);
//			genExcel(titaVo, LNM39GPList);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LNM39GPServiceImpl.findAll error = " + errors.toString());
		}
	}

	private void genFile(TitaVo titaVo, List<Map<String, String>> L7List) throws LogicException {
		this.info("=========== LNM39GP genFile : ");
		String txt = "F0;F1;F2;F3;F4;F5;F6;F7;F8;F9;F10;F11;F12;F13;F14;F15;F16;F17;F18";
		String txt1[] = txt.split(";");

		try {
			String strContent = "";

			makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LNFGP", "LNM39GP 欄位清單７", "LNFGP.TXT", 1); // UTF-8
			// 產製[控制檔]
			makeFileC.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LNFGPIDX", "LNM39GP 欄位清單７控制檔", "LNFGP.IDX", 1); // UTF-8

			// 標題列
			// strContent = "";
			// makeFile.put(strContent);

			if (L7List.size() == 0) { // 無資料時，會出空檔

			} else {
				// 欄位內容
				for (Map<String, String> tL7Vo : L7List) {
					strContent = "";
					for (int j = 1; j <= 19; j++) {
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
							break; // 借款人統編 放空白
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
							strField = makeFile.fillStringL(strField, 1, '0');
							break; // 1企業戶/2個人戶
						case 7:
							strField = makeFile.fillStringR(strField, 1, ' ');
							break; // 戶況
						case 8:
							strField = makeFile.fillStringL(strField, 8, '0');
							break; // 轉催收款日期
						case 9:
							strField = makeFile.fillStringR(strField, 1, ' ');
							break; // 原始認列時時信用評等 (空白)
						case 10:
							strField = makeFile.fillStringR(strField, 1, ' ');
							break; // 原始認列時信用評等模型 (空白)
						case 11:
							strField = makeFile.fillStringR(strField, 1, ' ');
							break; // 財務報導日時信用評等 (空白)
						case 12:
							strField = makeFile.fillStringR(strField, 1, ' ');
							break; // 財務報導日時信用評等模型 (空白)
						case 13:
							strField = makeFile.fillStringL(strField, 4, '0');
							break; // 逾期繳款天數
						case 14:
							strField = makeFile.fillStringL(strField, 1, '0');
							break; // IAS39減損客觀條件1 (1=是 2=否)
						case 15:
							strField = makeFile.fillStringL(strField, 1, '0');
							break; // IAS39減損客觀條件2 (1=是 2=否)
						case 16:
							strField = makeFile.fillStringL(strField, 1, '0');
							break; // IAS39減損客觀條件3 (1=是 2=否)
						case 17:
							strField = makeFile.fillStringL(strField, 1, '0');
							break; // IAS39減損客觀條件4 (1=是 2=否)
						case 18:
							strField = makeFile.fillStringL(strField, 1, '0');
							break; // IAS39減損客觀條件5 (1=是 2=否)
						case 19:
							strField = makeFile.fillStringR(strField, 1, ' ');
							break; // 內部違約機率降至D評等 (空白)
						default:
							strField = "";
							break;
						}
						strContent = strContent + strField;
						if (j != 19) {
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
			this.info("=========== LNM39GP genFile close === ");
			makeFileC.close();
			this.info("=========== LNM39GP genFile IDX close === ");
			// makeFile.toFile(snoIdx); // 不直接下傳

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LNM39GPServiceImpl.genFile error = " + errors.toString());
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
