package com.st1.itx.trade.LM;

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

/**
 * 
 * 
 * @author ChihWei
 * @version 1.0.0
 */
@Service("LM048p")
@Scope("prototype")
public class LM048p extends TradeBuffer {

	@Autowired
	LM048Report lM048Report;

	@Autowired
	WebClient webClient;

	@Autowired
	DateUtil dDateUtil;

	String txcd = "LM048";
	String txName = "企業放款風險承擔限額控管表";

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active" + txcd + "p");
		this.totaVo.init(titaVo);

		this.info(txcd + "p titaVo.getTxcd() = " + titaVo.getTxcd());
		String parentTranCode = titaVo.getTxcd();

		lM048Report.setParentTranCode(parentTranCode);

		// 西元月底日
		int mfbsdy = this.txBuffer.getTxCom().getMfbsdyf();
		
		lM048Report.exec(mfbsdy,titaVo);

		webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getTlrNo(), "Y", "LC009", titaVo.getTlrNo(),
				txcd + txName + "已完成", titaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}

}