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
@Service("L4211")
@Scope("prototype")
public class L4211  extends TradeBuffer{

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
	
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4211 ");
		this.totaVo.init(titaVo);

		if("1".equals(titaVo.get("FunctionCode"))) {
		  //產生匯款總傳票明細表
		  l4211Report.exec(titaVo);
		} else {
		  //產生匯款總傳票明細表依戶號排序
		  l4211Report2.exec(titaVo);
		}
		String sendMsg = "L4211-報表已完成";
		
		webClient.sendPost(dDateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "LC009", titaVo.getTlrNo(),
				sendMsg, titaVo);

		
		this.addList(this.totaVo);
		return this.sendList();

	}
}
