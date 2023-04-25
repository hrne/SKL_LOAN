package com.st1.itx.trade.L4;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.service.springjpa.cm.L4721ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.MySpring;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;

@Service("L4721")
@Scope("prototype")
/**
 * 
 * 
 * @author Zijin
 * @version 1.0.0
 */
public class L4721 extends TradeBuffer {

	@Autowired
	public L4721Batch sL4721Batch;

	@Autowired
	public WebClient webClient;

	@Autowired
	public DateUtil dateUtil;

	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4721 ");
		this.totaVo.init(titaVo);

//		還本繳息對帳單.pdf
		// 執行交易
		webClient.sendPost(dateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "", "",
				titaVo.getParam("TLRNO"), "批次作業啟動請稍等", titaVo);
		MySpring.newTask("L4721Batch", this.txBuffer, titaVo);
		
//		sL4721Batch.run(titaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}
}