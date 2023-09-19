package com.st1.itx.trade.L4;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.EmpDeductMediaService;
import com.st1.itx.db.service.springjpa.cm.L4212ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;

@Service("L4212Batch")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L4212Batch extends TradeBuffer {
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
	public L4212Report l4212Report;
	@Autowired
	public L4212ServiceImpl l4212ServiceImpl;

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
		this.info("active L4212 Batch");
		this.totaVo.init(titaVo);

		List<Map<String, String>> fnAllList = new ArrayList<>();

		try {
			fnAllList = l4212ServiceImpl.findAll(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("l4212ServiceImpl.fs error = " + errors.toString());
		}
		this.info("fnAllList = " + fnAllList.toString());
		this.info("fnAllList.size() 1 " + fnAllList.size());
		if (fnAllList != null && fnAllList.size() != 0) {
			// 產生更新成功失敗明細表
			l4212Report.exec(titaVo, fnAllList);
		}

		sendMsg = "L4212-報表已完成";

		webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "LC009",
				titaVo.getTlrNo() + "L4212", sendMsg, titaVo);

		this.addList(totaVo);
		return this.sendList();

	}
}