package com.st1.itx.trade.L9;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.trade.BS.BS901;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.MySpring;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;

@Service("L9713")
@Scope("prototype")
/**
 * 
 * 
 * @author Eric Chang
 * @version 1.0.0
 */

public class L9713 extends TradeBuffer {
	@Autowired
	L9713Report l9713Report;

	@Autowired
	BS901 bs901;

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	public WebClient webClient;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L9713 ");
		this.totaVo.init(titaVo);
		String parentTranCode = titaVo.getTxcd();
		String content = "";
		l9713Report.setParentTranCode(parentTranCode);
		BigDecimal checqueAmt = l9713Report.exec(titaVo);
		if (checqueAmt.compareTo(BigDecimal.ZERO) > 0) {
			content = "L9713應收票據之帳齡分析表已完成";
			titaVo.put("ChecqueAmt", "" + checqueAmt);
			MySpring.newTask("BS903", this.txBuffer, titaVo); // 未兌現應收票據提存
		} else {
			content = "L9713應收票據之帳齡分析表無資料";
		}
//		this.info("L9713p content = " + content);

		webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009",
				titaVo.getParam("TLRNO") + "L9713", content, titaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}

}