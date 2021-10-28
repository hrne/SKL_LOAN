package com.st1.itx.trade.L4;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.EmpDeductMediaService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * AcDate=9,7<br>
 * PerfMonth=9,5<br>
 * BatchNoFrom=X,6<br>
 * BatchNoTo=X,6<br>
 */

@Service("L4520")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4520 extends TradeBuffer {
	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Autowired
	public EmpDeductMediaService empDeductMediaService;

	@Autowired
	public CustMainService custMainService;

	@Autowired
	public TotaVo totaA;

	@Autowired
	public TotaVo totaB;

	@Autowired
	public WebClient webClient;
	
	@Autowired
	public L4520Report l4520Report;

	@Autowired
	public L4520Report3 l4520Report3;
	
	int succCnt = 0;
	BigDecimal succRpAmt = BigDecimal.ZERO;
	BigDecimal succTxAmt = BigDecimal.ZERO;
	int failCnt = 0;
	BigDecimal failRpAmt = BigDecimal.ZERO;
	BigDecimal failTxAmt = BigDecimal.ZERO;
	int totlCnt = 0;
	BigDecimal totlRpAmt = BigDecimal.ZERO;
	BigDecimal totlTxAmt = BigDecimal.ZERO;

	private String sendMsg = "";
	
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4520 ");
		this.totaVo.init(titaVo);
		

		//產生更新成功失敗明細表
		l4520Report.exec(titaVo);
		
		//產生員工扣薪總傳票明細表
		
		
		//產生火險費沖銷明細表(員工扣薪)
		long sno = l4520Report3.exec(titaVo);
		
//		if(sno == 0) {
//			throw new LogicException("E0001", "查無資料");
//		}
		sendMsg = "L4520-報表已完成";

		webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "LC009", titaVo.getTlrNo(),
				sendMsg, titaVo);

		this.addList(totaVo);
		return this.sendList();
		
	}
}