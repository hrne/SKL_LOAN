package com.st1.itx.trade.LM;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.MonthlyLM042RBC;
import com.st1.itx.db.domain.MonthlyLM042RBCId;
import com.st1.itx.db.service.MonthlyLM042RBCService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;

@Service("LM042p")
@Scope("prototype")
/**
 * 
 * 
 * @author ChihWei
 * @version 1.0.0
 */
public class LM042p extends TradeBuffer {

	@Autowired
	LM042Report LM042Report;

	@Autowired
	MonthlyLM042RBCService sMonthlyLM042RBCService;

	@Autowired
	Parse parse;

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	public WebClient webClient;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active LM042p");
		this.totaVo.init(titaVo);

		int tbsdy = this.txBuffer.getTxCom().getTbsdyf();
		// 月底日(西元)
		int mfbsdy = this.txBuffer.getTxCom().getMfbsdyf();
		// 年
		int iYear = mfbsdy / 10000;
		// 月
		int iMonth = (mfbsdy / 100) % 100;
		// 當年月
		int thisYM = 0;

		// 判斷帳務日與月底日是否同一天 
		if (tbsdy < mfbsdy) {
			iYear = iMonth - 1 == 0 ? (iYear - 1) : iYear;
			iMonth = iMonth - 1 == 0 ? 12 : iMonth - 1;
		}

		thisYM = iYear * 100 + iMonth;

		
		
//		int yearMonth = thisYM;
		int yearMonth = this.parse.stringToInteger(titaVo.getParam("YearMonth")) + 191100;

		int tYMD = ymd(yearMonth, 0);//本月底日
		int lYMD = ymd(yearMonth, -1);//上月底日

		
		
		checkAndUpdateData(titaVo, yearMonth);

		this.info("LM042p titaVo.getTxcd() = " + titaVo.getTxcd());
		String parentTranCode = titaVo.getTxcd();
		LM042Report.setParentTranCode(parentTranCode);

		int iAcDate = Integer.parseInt(titaVo.getEntDy());

		// MSG帶入預設值
		String ntxbuf = titaVo.getTlrNo() + FormatUtil.padX("LM042", 60) + iAcDate;

		this.info("ntxbuf = " + ntxbuf);

		
		boolean isFinish = LM042Report.exec(titaVo, lYMD, tYMD);
		if (isFinish) {
			webClient.sendPost(dDateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "LC009", ntxbuf,
					"LM042RBC表_會計部報表 已完成", titaVo);
		} else {
			webClient.sendPost(dDateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "LC009", ntxbuf,
					"LM042RBC表_會計部報表 查無資料", titaVo);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void checkAndUpdateData(TitaVo titaVo, int yearMonth) throws LogicException {

		Slice<MonthlyLM042RBC> sMonthlyLM042RBC;

		sMonthlyLM042RBC = sMonthlyLM042RBCService.findYearMonthAll(yearMonth, 0, 12, titaVo);
		
		// 判斷有無當月資料
		if (sMonthlyLM042RBC == null) {
			this.info("insert data");
			// 新增當月資料
			insertData(titaVo, yearMonth);

			// 新增後再次搜尋
			sMonthlyLM042RBC = sMonthlyLM042RBCService.findYearMonthAll(yearMonth, 0, 12, titaVo);
		}
		
		
		List<MonthlyLM042RBC> lMonthlyLM042RBC = sMonthlyLM042RBC == null ? null : sMonthlyLM042RBC.getContent();

		this.info("lMonthlyLM042RBC=" + lMonthlyLM042RBC.toString());

		for (MonthlyLM042RBC tMonthlyLM042RBC : lMonthlyLM042RBC) {

			switch (tMonthlyLM042RBC.getLoanItem()) {
			case "A":
				tMonthlyLM042RBC.setRiskFactor(new BigDecimal(titaVo.getParam("RiskFactor1")));
				break;
			case "B":
				tMonthlyLM042RBC.setRiskFactor(new BigDecimal(titaVo.getParam("RiskFactor2")));
				break;
			case "C":
				tMonthlyLM042RBC.setRiskFactor(new BigDecimal(titaVo.getParam("RiskFactor3")));
				break;
			case "D":
				tMonthlyLM042RBC.setRiskFactor(new BigDecimal(titaVo.getParam("RiskFactor4")));
				break;
			case "E":
				tMonthlyLM042RBC.setRiskFactor(new BigDecimal(titaVo.getParam("RiskFactor5")));
				break;
			case "F":
				tMonthlyLM042RBC.setRiskFactor(new BigDecimal(titaVo.getParam("RiskFactor6")));
				break;
			}

		}

//		this.info("lMonthlyLM042RBC=" + lMonthlyLM042RBC.toString());

		try {
			sMonthlyLM042RBCService.updateAll(lMonthlyLM042RBC, titaVo);
		} catch (DBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.info("update done");
	}

	public void insertData(TitaVo titaVo, int yearMonth) throws LogicException {
		List<MonthlyLM042RBC> insMonthlyLM042RBC = new ArrayList<MonthlyLM042RBC>();

		MonthlyLM042RBC mMonthlyLM042RBC = new MonthlyLM042RBC();
		MonthlyLM042RBCId monthlyLM042RBCId = new MonthlyLM042RBCId();

		for (int i = 1; i <= 2; i++) {
			mMonthlyLM042RBC = new MonthlyLM042RBC();
			monthlyLM042RBCId = new MonthlyLM042RBCId();
			monthlyLM042RBCId.setYearMonth(yearMonth);
			monthlyLM042RBCId.setLoanType(String.valueOf(i));
			monthlyLM042RBCId.setLoanItem("A");
			monthlyLM042RBCId.setRelatedCode("N");
			mMonthlyLM042RBC.setMonthlyLM042RBCId(monthlyLM042RBCId);
			mMonthlyLM042RBC.setRiskFactor(new BigDecimal(titaVo.getParam("RiskFactor1")));
			mMonthlyLM042RBC.setLoanAmount(new BigDecimal("0"));
			insMonthlyLM042RBC.add(mMonthlyLM042RBC);

			mMonthlyLM042RBC = new MonthlyLM042RBC();
			monthlyLM042RBCId = new MonthlyLM042RBCId();
			monthlyLM042RBCId.setYearMonth(yearMonth);
			monthlyLM042RBCId.setLoanType(String.valueOf(i));
			monthlyLM042RBCId.setLoanItem("B");
			monthlyLM042RBCId.setRelatedCode("N");
			mMonthlyLM042RBC.setMonthlyLM042RBCId(monthlyLM042RBCId);
			mMonthlyLM042RBC.setRiskFactor(new BigDecimal(titaVo.getParam("RiskFactor2")));
			mMonthlyLM042RBC.setLoanAmount(new BigDecimal("0"));
			insMonthlyLM042RBC.add(mMonthlyLM042RBC);

			mMonthlyLM042RBC = new MonthlyLM042RBC();
			monthlyLM042RBCId = new MonthlyLM042RBCId();
			monthlyLM042RBCId.setYearMonth(yearMonth);
			monthlyLM042RBCId.setLoanType(String.valueOf(i));
			monthlyLM042RBCId.setLoanItem("C");
			monthlyLM042RBCId.setRelatedCode("N");
			mMonthlyLM042RBC.setMonthlyLM042RBCId(monthlyLM042RBCId);
			mMonthlyLM042RBC.setRiskFactor(new BigDecimal(titaVo.getParam("RiskFactor3")));
			mMonthlyLM042RBC.setLoanAmount(new BigDecimal("0"));
			insMonthlyLM042RBC.add(mMonthlyLM042RBC);

			mMonthlyLM042RBC = new MonthlyLM042RBC();
			monthlyLM042RBCId = new MonthlyLM042RBCId();
			monthlyLM042RBCId.setYearMonth(yearMonth);
			monthlyLM042RBCId.setLoanType(String.valueOf(i));
			monthlyLM042RBCId.setLoanItem("D");
			monthlyLM042RBCId.setRelatedCode("N");
			mMonthlyLM042RBC.setMonthlyLM042RBCId(monthlyLM042RBCId);
			mMonthlyLM042RBC.setRiskFactor(new BigDecimal(titaVo.getParam("RiskFactor4")));
			mMonthlyLM042RBC.setLoanAmount(new BigDecimal("0"));
			insMonthlyLM042RBC.add(mMonthlyLM042RBC);

			mMonthlyLM042RBC = new MonthlyLM042RBC();
			monthlyLM042RBCId = new MonthlyLM042RBCId();
			monthlyLM042RBCId.setYearMonth(yearMonth);
			monthlyLM042RBCId.setLoanType(String.valueOf(i));
			monthlyLM042RBCId.setLoanItem("E");
			monthlyLM042RBCId.setRelatedCode("Y");
			mMonthlyLM042RBC.setMonthlyLM042RBCId(monthlyLM042RBCId);
			mMonthlyLM042RBC.setRiskFactor(new BigDecimal(titaVo.getParam("RiskFactor5")));
			mMonthlyLM042RBC.setLoanAmount(new BigDecimal("0"));
			insMonthlyLM042RBC.add(mMonthlyLM042RBC);

			mMonthlyLM042RBC = new MonthlyLM042RBC();
			monthlyLM042RBCId = new MonthlyLM042RBCId();
			monthlyLM042RBCId.setYearMonth(yearMonth);
			monthlyLM042RBCId.setLoanType(String.valueOf(i));
			monthlyLM042RBCId.setLoanItem("F");
			monthlyLM042RBCId.setRelatedCode("Y");
			mMonthlyLM042RBC.setMonthlyLM042RBCId(monthlyLM042RBCId);
			mMonthlyLM042RBC.setRiskFactor(new BigDecimal(titaVo.getParam("RiskFactor6")));
			mMonthlyLM042RBC.setLoanAmount(new BigDecimal("0"));
			insMonthlyLM042RBC.add(mMonthlyLM042RBC);
		}

		try {

			sMonthlyLM042RBCService.insertAll(insMonthlyLM042RBC, titaVo);
		} catch (DBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.info("insert done");

	}

	/**
	 * 取得月底日
	 * @param yearMonth 西元年月(YYYYMM)
	 * @paran num 單位(0=本月底日,1=下月底日,-1=上月底日)
	 * */
	private Integer ymd(int yearMonth,int num) {
		
		// 格式
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		Calendar calendar = Calendar.getInstance();

		int iYear = yearMonth/100;
		int iMonth = yearMonth%100;
		
		int number = num - 1;
		// 設月底日
		calendar.set(Calendar.YEAR, iYear);
		calendar.set(Calendar.MONTH, iMonth + number);
		calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));

		
		return Integer.valueOf(dateFormat.format(calendar.getTime()));
	}
}