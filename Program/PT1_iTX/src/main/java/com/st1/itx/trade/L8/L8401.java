package com.st1.itx.trade.L8;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.service.JobMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.MySpring;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L8401")
@Scope("prototype")
/**
 * 
 * @author Eric Chang
 * @version 1.0.0
 */
public class L8401 extends TradeBuffer {

	@Autowired
	private Parse parse;

	@Autowired
	private DateUtil dateUtil;

	@Autowired
	private JobMainService jobMainService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("=========== L8401 run titaVo: " + titaVo);

		this.info("active L8401 ");
		this.totaVo.init(titaVo);
		String job = "";

		String acdatestart = titaVo.getParam("AcDateStart");
		String acdateend = titaVo.getParam("AcDateEnd");

		// 需同年月,若起訖有不同月份踢錯誤訊息(同一檔案中之交易日期須為相同年月份)
		if (titaVo.get("DAILY2").equals("Y")) {
			if (acdatestart.substring(0, 5).compareTo(acdateend.substring(0, 5)) != 0) {
				throw new LogicException("E0010", "B211 同一檔案中之交易日期須為相同年月份");
			}
		}

		// IF RemakeYN = "Y" THEN 更新table
		if (titaVo.getParam("RemakeYN").equals("Y")) {
			this.info("L8401: RemakeYN == Y. Update the table.");
			execStoredProcedure(acdatestart, acdateend, titaVo);
		}

		// B204 聯徵每日新增授信及清償資料檔
		if (titaVo.get("DAILY1").equals("Y")) {
			this.info("L8401 active LB204 ");
			MySpring.newTask("LB204p", this.txBuffer, titaVo);
//			job += ";jLB204";
		}

		// B211 聯徵每日授信餘額變動資料檔
		if (titaVo.get("DAILY2").equals("Y")) {
			this.info("L8401 active LB211 ");
			MySpring.newTask("LB211p", this.txBuffer, titaVo);
//			job += ";jLB211";
		}

		if (!job.equals("")) {
			this.info("=========== L8401 setBatchJobId : ");
			titaVo.setBatchJobId(job.substring(1));
		}
		this.addList(this.totaVo);
		return this.sendList();
	}

	private void execStoredProcedure(String acDateStart, String acDateEnd, TitaVo titaVo) throws LogicException {
		int execDateBegin = parse.stringToInteger(acDateStart) + 19110000;
		int execDateEnd = parse.stringToInteger(acDateEnd) + 19110000;
		this.info("execDateBegin = " + execDateBegin);
		this.info("execDateEnd = " + execDateEnd);
		for (int execDate = execDateBegin; execDate <= execDateEnd; execDate = getNextDay(execDate)) {
			this.info("execDate = " + execDate);
			dateUtil.init();
			dateUtil.setDate_2(execDate);
			if (!dateUtil.isHoliDay()) {
				this.info("execDate " + execDate + " is not holiday exec usp ...");
				if (titaVo.get("DAILY1").equals("Y")) {
					jobMainService.Usp_L8_JcicB204_Upd(execDate, titaVo.getTlrNo(), titaVo);
				}
				if (titaVo.get("DAILY2").equals("Y")) {
					jobMainService.Usp_L8_JcicB211_Upd(execDate, titaVo.getTlrNo(), titaVo);
				}
				this.info("execDate " + execDate + " exec usp finished.");
			}
		}
	}

	private int getNextDay(int date) throws LogicException {
		dateUtil.init();
		dateUtil.setDate_1(date);
		dateUtil.setDays(1);
		dateUtil.getCalenderDay();
		return dateUtil.getDate_2Integer();
	}
}