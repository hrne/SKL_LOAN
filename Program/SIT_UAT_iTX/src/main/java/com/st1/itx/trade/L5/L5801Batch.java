package com.st1.itx.trade.L5;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;

@Service("L5801Batch")
@Scope("prototype")
/**
 * 
 * 
 * @author Eric Chang
 * @version 1.0.0
 */
public class L5801Batch extends TradeBuffer {
	@Autowired
	L5801Report l5801report;

	@Autowired
	L5801Report2 l5801report2;

	@Autowired
	L5801Report3 l5801report3;

	@Autowired
	L5801Report4 l5801report4;

	@Autowired
	WebClient webClient;

	@Autowired
	DateUtil dDateUtil;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5801 Batch");
		this.totaVo.init(titaVo);

		// 輸入參數檢核
		int thisMonth = Integer.parseInt(titaVo.getParam("ThisMonth"));
		int lastMonth = Integer.parseInt(titaVo.getParam("LastMonth"));

		if (thisMonth == 0 || lastMonth == 0) {
			webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", titaVo.getTxcd(), titaVo.getParam("TLRNO"), "L5801補貼息作業輸入參數[年月]有誤", titaVo);
			this.addList(this.totaVo);
			return this.sendList();
		}

		thisMonth += 191100;
		lastMonth += 191100;

		this.info("L5801 titaVo.getTxcd() = " + titaVo.getTxcd());
		String parentTranCode = titaVo.getTxcd();

		l5801report.setParentTranCode(parentTranCode);
		l5801report2.setParentTranCode(parentTranCode);
		l5801report3.setParentTranCode(parentTranCode);
		l5801report4.setParentTranCode(parentTranCode);

		l5801report.exec(thisMonth, lastMonth, titaVo);
		webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009", titaVo.getParam("TLRNO"), "L5801補貼息申貸名冊工作檔已完成", titaVo);

		l5801report2.exec(thisMonth, lastMonth, titaVo);
		webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009", titaVo.getParam("TLRNO"), "L5801補貼息結清名冊終止名冊工作檔已完成", titaVo);

		l5801report3.exec(thisMonth, lastMonth, titaVo);
		webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009", titaVo.getParam("TLRNO"), "L5801補貼息核撥清單工作檔已完成", titaVo);

		l5801report4.exec(thisMonth, lastMonth, titaVo);
		webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009", titaVo.getParam("TLRNO"), "L5801補貼息核撥清單明細檔已完成", titaVo);
		this.addList(this.totaVo);
		return this.sendList();
	}

}