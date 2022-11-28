package com.st1.itx.trade.L5;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.service.springjpa.cm.L5904ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * StartDate=9,7<br>
 * EndDate=9,7<br>
 * reportFlag=9,1<br>
 * END=X,1<br>
 */

@Service("L5104")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L5104 extends TradeBuffer {

	@Autowired
	public Parse parse;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	@Autowired
	public L5904ServiceImpl l5904ServiceImpl;

	@Autowired
	WebClient webClient;

	@Autowired
	DateUtil dDateUtil;

	/* 報表服務注入 */
	@Autowired
	L5104Report l5104Report;
	@Autowired
	L5104Report2 l5104Report2;
	@Autowired
	L5104Report3 l5104Report3;

	private int reportFlag = 0;
	private int startDate = 0;
	private int endDate = 0;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5104 ");
		this.info("titaVo   = " + titaVo);
		this.totaVo.init(titaVo);
		reportFlag = parse.stringToInteger(titaVo.getParam("ReportFlag"));
		startDate = Integer.parseInt(titaVo.getParam("StartDate")) + 19110000;
		endDate = Integer.parseInt(titaVo.getParam("EndDate")) + 19110000;

		doRpt(titaVo);

		// 交易櫃員
		String empNo = titaVo.getTlrNo();

		// MSG帶入預設值
		String ntxbuf = titaVo.getTlrNo() + FormatUtil.padX("L5104", 60) + (endDate - 19110000);

		this.info("ntxbuf = " + ntxbuf);

		String checkMsg = "L5104檔案借閱報表作業(列印已完成)";
		webClient.sendPost(dDateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "LC009", ntxbuf, checkMsg,
				titaVo);

		this.addList(this.totaVo);
		return this.sendList();

	}

	public void doRpt(TitaVo titaVo) throws LogicException {
//		 1:未歸還月報表;
//	     2:法拍件月報表;
//	     3:件數統計表;
//	     4:全部

		switch (reportFlag) {
		case 1:
			l5104Report.exec(startDate, endDate, titaVo);
			break;
		case 2:
			l5104Report2.exec(startDate, endDate, titaVo);
			break;
		case 3:
			l5104Report3.exec(startDate, endDate, titaVo);
			break;
		case 4:
			l5104Report.exec(startDate, endDate, titaVo);
			l5104Report2.exec(startDate, endDate, titaVo);
			l5104Report3.exec(startDate, endDate, titaVo);
			break;
		}
		this.info("L5104 doRpt finished.");
	}

}
