package com.st1.itx.trade.LY;

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

@Service("LY002p")
@Scope("prototype")
/**
 * 
 * 
 * @author Ted Lin
 * @version 1.0.0
 */
public class LY002p extends TradeBuffer {

	@Autowired
	LY002Report lY002Report;

	@Autowired
	LY002Report2 lY002Report2;

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	public WebClient webClient;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active lY002p");
		this.totaVo.init(titaVo);

		this.info("LY002p titaVo.getTxcd() = " + titaVo.getTxcd());
		String parentTranCode = titaVo.getTxcd();

		lY002Report.setParentTranCode(parentTranCode);
		// A141重要放款餘額明細表";
		String noticeText = "LY002 A141重要放款餘額明細表";

		lY002Report2.exec(titaVo);

		webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009",
				titaVo.getParam("TLRNO"), noticeText + "已完成", titaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}
}