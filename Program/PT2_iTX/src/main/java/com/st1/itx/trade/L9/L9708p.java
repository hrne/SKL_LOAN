package com.st1.itx.trade.L9;

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

@Service("L9708p")
@Scope("prototype")
/**
 * 
 * 
 * @author Eric Chang
 * @version 1.0.0
 */
public class L9708p extends TradeBuffer {

	@Autowired
	L9708Report l9708Report;

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	public WebClient webClient;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L9708p ");
		this.totaVo.init(titaVo);

		this.info("L9708p titaVo.getTxcd() = " + titaVo.getTxcd());
		String parentTranCode = titaVo.getTxcd();

		String content = "";

		l9708Report.setParentTranCode(parentTranCode);

		if (l9708Report.exec(titaVo)) {
			content = "L9708貸款自動轉帳申請書明細表已完成";
		} else {
			content = "L9708貸款自動轉帳申請書明細表查無資料";
		}

		webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009", titaVo.getParam("TLRNO") + "L9708", content, titaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}

}