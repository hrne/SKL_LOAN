package com.st1.itx.trade.L4;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.service.springjpa.cm.L4211AServiceImpl;
import com.st1.itx.db.service.springjpa.cm.L4211BServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;

/**
 * L4211
 * 
 * @author Mata
 * @version 1.0.0
 */
@Service("L4211Batch")
@Scope("prototype")
public class L4211Batch extends TradeBuffer {

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	/* 日期工具 */
	@Autowired
	DateUtil dDateUtil;

	@Autowired
	public WebClient webClient;

	@Autowired
	public L4211Report l4211Report;

	@Autowired
	public L4211Report2 l4211Report2;

	@Autowired
	public L4211AServiceImpl l4211AServiceImpl;

	@Autowired
	public L4211BServiceImpl l4211BServiceImpl;

	private String sendMsg = "";
	private Boolean flag = true;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4211Batch ");
		this.totaVo.init(titaVo);

		if ("1".equals(titaVo.get("FunctionCode"))) {
			// 產生匯款總傳票明細表

			try {
				l4211Report.exec(titaVo);
			} catch (LogicException e) {
				sendMsg = e.getErrorMsg();
				flag = false;
			}

		} else {
			// 產生匯款總傳票明細表依戶號排序

			try {
				l4211Report2.exec(titaVo);
			} catch (LogicException e) {
				sendMsg = e.getErrorMsg();
				flag = false;
			}

		}

		// 送出通知訊息
		sendMessage(titaVo);

		this.addList(this.totaVo);
		return this.sendList();

	}

	private void sendMessage(TitaVo titaVo) throws LogicException {
		if (flag) {

			sendMsg = "L4211-報表已完成";

			webClient.sendPost(dDateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "LC009",
					titaVo.getTlrNo() + "L4211", sendMsg, titaVo);
		} else {
			webClient.sendPost(dDateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "L4211", "", sendMsg,
					titaVo);
		}
	}

}
