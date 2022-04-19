package com.st1.itx.trade.L4;

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
import com.st1.itx.util.parse.Parse;

@Service("L4455Batch")
@Scope("prototype")
/**
 * 
 * 
 * @author Zijin
 * @version 1.0.0
 */
public class L4455Batch extends TradeBuffer {

	@Autowired
	public Parse parse;

	@Autowired
	public L4455Report l4455Report;
	
	@Autowired
	public L4455Report2 l4455Report2;
	
	@Autowired
	public DateUtil dateUtil;

	@Autowired
	public WebClient webClient;


	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4455Batch ");
		this.totaVo.init(titaVo);

		int ifunctioncode = parse.stringToInteger(titaVo.getParam("FunctionCode"));
		 
		if(ifunctioncode == 1) {
		  l4455Report.exec(titaVo);
		} else {
		  l4455Report2.exec(titaVo);
		}
		webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "LC009",
				titaVo.getTlrNo()+"L4455", "L4455銀行扣款報表 處理完畢", titaVo);

		// end
		this.addList(this.totaVo);
		return this.sendList();
	}


}