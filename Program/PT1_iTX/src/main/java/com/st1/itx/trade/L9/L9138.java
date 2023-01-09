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

/**
 * L9138
 * 
 * @author Ted
 * @version 1.0.0
 */
@Service("L9138")
@Scope("prototype")
public class L9138  extends TradeBuffer {

	@Autowired
	L9138Report L9138report;

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	public WebClient webClient;
	
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L9138 ");
		
		int acdate = Integer.valueOf(titaVo.getParam("AcDate"));
		
		L9138report.exec(titaVo,acdate);
		
		String  infoNotification= "L9138 放款授信日報表 已完成";
		
		webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009",
				titaVo.getParam("TLRNO") + "L9138", infoNotification, titaVo);
		
		this.addList(this.totaVo);

		return this.sendList();
	}

}