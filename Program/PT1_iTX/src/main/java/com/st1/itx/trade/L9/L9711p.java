package com.st1.itx.trade.L9;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.buffer.TxBuffer;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;

@Service("L9711p")
@Scope("prototype")
/**
 * 
 * 
 * @author Eric Chang
 * @version 1.0.0
 */
public class L9711p extends TradeBuffer {

	@Autowired
	L9711Report l9711report;

	@Autowired
	L9711Report2 l9711report2;

	@Autowired
	public WebClient webClient;

	@Autowired
	DateUtil dDateUtil;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L9711p ");
		TxBuffer txbuffer = this.getTxBuffer();

		this.totaVo.init(titaVo);

		this.info("L9711p titaVo.getTxcd() = " + titaVo.getTxcd());
		String parentTranCode = titaVo.getTxcd();

		l9711report.setParentTranCode(parentTranCode);

		List<Map<String, String>> l9711List = l9711report.exec(titaVo);

		if (l9711List != null && !l9711List.isEmpty()) {
			l9711report2.exec(titaVo, txbuffer, l9711List,parentTranCode);
			webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009", titaVo.getParam("TLRNO"), "L9711放款到期明細表及通知單已完成", titaVo);
		} else {
			webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009", titaVo.getParam("TLRNO"), "L9711放款到期明細表及通知單查無資料", titaVo);
		}

		if (true) {
			this.addList(this.totaVo);
		}
		return this.sendList();
	}
}