package com.st1.itx.trade.L5;

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

@Service("L5736p")
@Scope("prototype")
/**
 * L5736 正常戶餘額明細
 * 
 * @author
 * @version 1.0.0
 */
public class L5736p extends TradeBuffer {

	@Autowired
	L5736Report l5736Report;

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	WebClient webClient;

	String txCD = "L5736";
	
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active " + txCD + "p");
		this.totaVo.init(titaVo);

		this.info(txCD + "p titaVo.getTxcd() = " + titaVo.getTxcd());
		String parentTranCode = titaVo.getTxcd();

		l5736Report.setParentTranCode(parentTranCode);
		int totalItem = Integer.parseInt(titaVo.getParam("TotalItem"));

		int cnt = 0;
		for (int i = 1; i <= totalItem; i++) {
			if (titaVo.getParam("BtnShell" + i).equals("V")) {
				cnt++;
			}
		}

		if (cnt == 0) {
			throw new LogicException(titaVo, "E0019", "請勾選報表項目");
		}

		String tradeName = "";
		String tradeCode = "";
		String msg = "";

		for (int i = 1; i <= totalItem; i++) {
			if (titaVo.getParam("BtnShell" + i).equals("V")) {
				cnt++;

				tradeCode = titaVo.getParam("TradeCode" + i);
				tradeName = titaVo.getParam("rpName" + i);
				// tradeCode - tradeName
//				L5736C-首購餘額明細-催收戶
//				L5736F-催收戶餘額明細
//				L5736H-住宅貸款餘額明細-催收戶

				l5736Report.exec(tradeCode, tradeName, titaVo);

				msg = msg + (tradeCode + "-" + tradeName) + ",";
			}
		}

		msg = msg.substring(0, msg.length() - 1);

		webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009",
				titaVo.getParam("TLRNO"), msg + "已完成", titaVo);
		this.addList(this.totaVo);
		return this.sendList();
	}
}