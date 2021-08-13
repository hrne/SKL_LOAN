package com.st1.itx.trade.LM;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LM032ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;

@Component
@Scope("prototype")

public class LM032Report extends MakeReport {
	// private static final Logger logger = LoggerFactory.getLogger(LM032Report.class);

	@Autowired
	LM032ServiceImpl lM032ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	@Override
	public void printTitle() {

	}

	public void exec(TitaVo titaVo) throws LogicException {

		List<Map<String, String>> LM032List = null;
		try {

			LM032List = lM032ServiceImpl.findAll(titaVo);
			exportExcel(titaVo, LM032List);
				
		} catch (Exception e) {
			// TODO Auto-generated catch block
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LM032ServiceImpl.testExcel error = " + errors.toString());
		}
	}

	private void exportExcel(TitaVo titaVo, List<Map<String, String>> LDList) throws LogicException {

		String lastdy = String.valueOf((Integer.valueOf(titaVo.get("ENTDY").toString()) + 19110000));

		lastdy = String.format("%s-%s-%s", lastdy.substring(0, 4), lastdy.substring(4, 6), lastdy.substring(6, 8));

		// 設定日期格式
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// 進行轉換
		Date date;
		try {
			date = sdf.parse(lastdy);

			Calendar cal = Calendar.getInstance();

			cal.setTime(date);

			cal.add(Calendar.MONTH, -1);

			lastdy = sdf.format(cal.getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// xxxx-xx-xx
		lastdy = lastdy.substring(0, 4) + lastdy.substring(5, 7);

		this.info("LM032Report exportExcel");
		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM032", "逾期案件滾動率明細", "LM032逾期案件滾動率明細", "逾期案件滾動率明細.xlsx", "D9612263");
		if (LDList.size() == 0) {
			makeExcel.setValue(3, 1, "本日無資料");
		}
		int row = 3;
                BigDecimal dataCount = BigDecimal.ZERO;
                BigDecimal loanBalTotal_LastMonth = BigDecimal.ZERO;
                BigDecimal loanBalTotal_ThisMonth = BigDecimal.ZERO;

		for (Map<String, String> tLDVo : LDList) {

			String ad = "";
			int col = 0;
			for (int i = 0; i < tLDVo.size(); i++) {

				ad = "F" + String.valueOf(col);
				col++;

				switch (col) {

                                case 4:
                                        //戶號，算資料筆數
                                        dataCount = dataCount.add(new BigDecimal("1"));
                                        break;

                                case 6:
                                        //上月放款餘額，算總計
                                        loanBalTotal_LastMonth = loanBalTotal_LastMonth.add(new BigDecimal(tLDVo.get(ad)));
                                        break;

                                case 14:
                                        //當月放款餘額，算總計
                                        loanBalTotal_ThisMonth = loanBalTotal_ThisMonth.add(new BigDecimal(tLDVo.get(ad)));
                                        break;

				default:
					break;
				}

				makeExcel.setValue(row, col, tLDVo.get(ad), "R");
			} // for

			row++;
		} // for

                //寫總計
                makeExcel.setValue(1, 4, formatAmt(dataCount, 0));
                makeExcel.setValue(1, 6, formatAmt(loanBalTotal_LastMonth, 0));
                makeExcel.setValue(1, 14, formatAmt(loanBalTotal_ThisMonth, 0));

		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
	}

}
