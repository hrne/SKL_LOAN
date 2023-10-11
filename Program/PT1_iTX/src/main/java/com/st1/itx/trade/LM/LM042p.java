package com.st1.itx.trade.LM;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.MonthlyLM042RBC;
import com.st1.itx.db.domain.MonthlyLM042Statis;
import com.st1.itx.db.service.MonthlyLM042RBCService;
import com.st1.itx.db.service.MonthlyLM042StatisService;
import com.st1.itx.eum.ContentName;
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
	MonthlyLM042StatisService sMonthlyLM042StatisService;
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

		int tYMD = ymd(yearMonth, 0);// 本月底日
		int lYMD = ymd(yearMonth, -1);// 上月底日

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
		Slice<MonthlyLM042Statis> slMonthlyLM042Statis = sMonthlyLM042StatisService.findYearMonthAll(yearMonth, 0,
				Integer.MAX_VALUE, titaVo);
		Slice<MonthlyLM042RBC> slMonthlyLM042RBC = sMonthlyLM042RBCService.findYearMonthAll(yearMonth, 0,
				Integer.MAX_VALUE, titaVo);

		changeDBEnv(titaVo);
		try {
			sMonthlyLM042StatisService.insertAll(slMonthlyLM042Statis.getContent(), titaVo);
		} catch (DBException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			sMonthlyLM042RBCService.insertAll(slMonthlyLM042RBC.getContent(), titaVo);
		} catch (DBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		titaVo.setDataBaseOnOrg();// 還原原本的環境
		this.addList(this.totaVo);
		return this.sendList();
	}

	/**
	 * 取得月底日
	 * 
	 * @param yearMonth 西元年月(YYYYMM)
	 * @paran num 單位(0=本月底日,1=下月底日,-1=上月底日)
	 */
	private Integer ymd(int yearMonth, int num) {

		// 格式
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		Calendar calendar = Calendar.getInstance();

		int iYear = yearMonth / 100;
		int iMonth = yearMonth % 100;

		int number = num - 1;
		// 設月底日
		calendar.set(Calendar.YEAR, iYear);
		calendar.set(Calendar.MONTH, iMonth + number);
		calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));

		return Integer.valueOf(dateFormat.format(calendar.getTime()));
	}

	/**
	 * 切換環境
	 */
	private void changeDBEnv(TitaVo titaVo) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.info("now env = " + ContentName.onLine);
		this.info("now getDataBase = " + titaVo.getDataBase());
		if (ContentName.onLine.equals(titaVo.getDataBase())) {
			titaVo.setDataBaseOnMon();// 指定月報環境
		} else {
			titaVo.setDataBaseOnLine();// 指定連線環境
		}
//		titaVo.setDataBaseOnLine();// 指定連線環境
	}
}