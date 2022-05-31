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
import com.st1.itx.db.service.springjpa.cm.L9710ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;

@Service("L9710p")
@Scope("prototype")
/**
 * 
 * 
 * @author Eric Chang
 * @version 1.0.0
 */
public class L9710p extends TradeBuffer {

	@Autowired
	L9710Report l9710Report;

	@Autowired
	L9711Report2 l9710NoticeTol9711;

	@Autowired
	L9710ServiceImpl l9710ServiceImpl;

	@Autowired
	L9705Form l9705Form;
	
	@Autowired
	DateUtil dDateUtil;

	@Autowired
	public WebClient webClient;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L9710p ");
		TxBuffer txbuffer = this.getTxBuffer();
		String infoNotification = "";

		this.totaVo.init(titaVo);

		this.info("L9710p titaVo.getTxcd() = " + titaVo.getTxcd());
	
		String parentTranCode = titaVo.getTxcd();

		l9710Report.setParentTranCode(parentTranCode);

//		List<Map<String, String>> l9710List = l9710Report.exec(titaVo);
		List<Map<String, String>> l9710List = null;

		//帳務日(西元)
		int tbsdy = this.txBuffer.getTxCom().getTbsdyf();
		
		try {

			l9710List = l9710ServiceImpl.findAll(titaVo,tbsdy);

		} catch (Exception e) {

			this.info("L9710ServiceImpl.LoanBorTx error = " + e.toString());

		}

		if (l9710List != null && !l9710List.isEmpty()) {

			String iCUSTNO = titaVo.get("CustNo");

			// 0000000多列印一個寬限到期明細表
			if (iCUSTNO.equals("0000000")) {
				this.info("active L9710report data detail");
				l9710Report.exec(titaVo,tbsdy);

				infoNotification = "L9710寬限到期明細表已完成";
				webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009",
						titaVo.getParam("TLRNO")+"L9710", infoNotification, titaVo);
			}

			this.info("active L9710report(form type by l9711) notice");
			
			l9710NoticeTol9711.exec(titaVo, txbuffer, l9710List);
			
			infoNotification = "L9710 通知單已完成";
			
			//by eric 2021.12.10
			l9705Form.exec(l9710List, titaVo, txbuffer);
			
		} else {

			infoNotification = "L9710 查無資料";

		}
		webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009",
				titaVo.getParam("TLRNO")+"L9710", infoNotification, titaVo);

		this.addList(this.totaVo);

		return this.sendList();
	}

}