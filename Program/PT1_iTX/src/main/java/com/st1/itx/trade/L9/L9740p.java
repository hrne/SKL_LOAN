package com.st1.itx.trade.L9;

import java.math.BigDecimal;
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

@Service("L9740p")
@Scope("prototype")
/**
 * L9740 檢核政府優惠房貸利率脫鉤
 * 
 * @author
 * @version 1.0.0
 */
public class L9740p extends TradeBuffer {

	@Autowired
	L9740Report l9740Report;

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	WebClient webClient;

	String txCD = "L9740";
	String txName = "公會無自用住宅利率報送";

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active " + txCD + "p");
		this.totaVo.init(titaVo);

		this.info(txCD + "p titaVo.getTxcd() = " + titaVo.getTxcd());
		String parentTranCode = titaVo.getTxcd();

		l9740Report.setParentTranCode(parentTranCode);

		int DrawDownDate1 = Integer.valueOf(titaVo.getParam("DrawDownDate1")) + 19110000;

		
		int DrawDownDate2 = Integer.valueOf(titaVo.getParam("DrawDownDate2")) + 19110000;

		
		int DrawDownDate3 = Integer.valueOf(titaVo.getParam("DrawDownDate3")) + 19110000;
		BigDecimal rate = new BigDecimal(titaVo.getParam("Rate"));

		this.info("DrawDownDate1 = " + DrawDownDate1);
		this.info("DrawDownDate2 = " + DrawDownDate2);
		this.info("DrawDownDate3 = " + DrawDownDate3);
		this.info("rate = " + rate);

		boolean isFinish = false;

		isFinish = l9740Report.exec(titaVo, DrawDownDate1, DrawDownDate2, DrawDownDate3, rate);

		if (isFinish) {
			webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009",
					titaVo.getParam("TLRNO"), txCD + txName + " 已完成", titaVo);
		} else {
			webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009",
					titaVo.getParam("TLRNO"), txCD + txName + " 查無資料", titaVo);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}