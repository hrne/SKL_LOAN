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
import com.st1.itx.db.service.springjpa.cm.LNM39JPServiceImpl;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.MakeFile;

import java.text.ParseException;
import java.util.Date;
import java.util.Calendar;
import java.text.SimpleDateFormat;

@Component("lNM39JPReport")
@Scope("prototype")

public class LNM39JPReport extends MakeReport {

	Date dateNow = new Date();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	String strToday = String.valueOf(Integer.parseInt(sdf.format(dateNow)) - 19110000); // 民國年

	Calendar calendar = Calendar.getInstance();
	String monthlyEndDay = "00000000"; // 會計日當月的月底日曆日

	@Autowired
	public LNM39JPServiceImpl lNM39JPServiceImpl;

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
		// LNM39JP 欄位清單10
		this.info("---------- LNM39JPReport exec titaVo: " + titaVo);

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

		List<Map<String, String>> LNM39JPList = null;
		try {
			LNM39JPList = lNM39JPServiceImpl.findAll(dataMonth, titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("LNM39JPReport LNM39JPServiceImpl.findAll error = " + errors.toString());
			return false;
		}

		try {
			// txt
			genFile(titaVo, LNM39JPList);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("LNM39JPReport.genFile error = " + errors.toString());
			return false;
		}

		return true;
	}

	private void genFile(TitaVo titaVo, List<Map<String, String>> L7List) throws LogicException {
		this.info("=========== LNM39JP genFile : ");
		String txt = "F0;F1;F2;F3;F4;F5";
		String txt1[] = txt.split(";");

		try {
			String strContent = "";

			makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LNFNP", "LNM39JP 欄位清單10", "LNFNP.TXT", 1); // UTF-8
			// 產製[控制檔]
			makeFileC.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LNFNPIDX", "LNM39JP 欄位清單10控制檔", "LNFNP.IDX",
					1); // UTF-8

			// 標題列
			// strContent = "";
			// makeFile.put(strContent);

			if (L7List.size() == 0) { // 無資料時，會出空檔

			} else {
				// 欄位內容
				for (Map<String, String> tL7Vo : L7List) {
					strContent = "";
					for (int j = 1; j <= 6; j++) {
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
							strField = makeFile.fillStringL(strField, 6, '0');
							break; // 發生時會計日期年月
						case 2:
							strField = makeFile.fillStringL(strField, 7, '0');
							break; // 戶號
						case 3:
							strField = makeFile.fillStringL(strField, 3, '0');
							break; // 新額度編號
						case 4:
							strField = makeFile.fillStringL(strField, 3, '0');
							break; // 新撥款序號
						case 5:
							strField = makeFile.fillStringL(strField, 3, '0');
							break; // 舊額度編號
						case 6:
							strField = makeFile.fillStringL(strField, 3, '0');
							break; // 舊撥款序號
						default:
							strField = "";
							break;
						}
						strContent = strContent + strField;
						if (j != 6) {
							strContent = strContent + ",";
						}
					}
					makeFile.put(strContent);
				}
			}

			// makeFile.toFile(sno); // 不直接下傳

			strContent = sdf.format(dateNow) + "," + calendarEntDyMonthlyEndDate(titaVo) + ","
					+ String.format("%06d", L7List.size());
			makeFileC.put(strContent);
			makeFile.close();
			this.info("=========== LNM39JP genFile close === ");
			makeFileC.close();
			this.info("=========== LNM39JP genFile IDX close === ");
			// makeFile.toFile(snoIdx); // 不直接下傳

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LNM39JPServiceImpl.genFile error = " + errors.toString());
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
