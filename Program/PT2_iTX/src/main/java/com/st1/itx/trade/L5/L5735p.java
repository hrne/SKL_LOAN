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

@Service("L5735p")
@Scope("prototype")
/**
 * L5735 建商餘額明細
 * 
 * @author
 * @version 1.0.0
 */
public class L5735p extends TradeBuffer {

	@Autowired
	L5735Report l5735Report;

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	WebClient webClient;

	String txCD = "L5735";

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active " + txCD + "p");
		this.totaVo.init(titaVo);

		this.info(txCD + "p titaVo.getTxcd() = " + titaVo.getTxcd());

		l5735Report.setParentTranCode(titaVo.getTxcd());

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
				//tradeCode - tradeName  
//				L5735A-建商餘額明細
//				L5735B-首購餘額明細
//				L5735D-工業區土地抵押餘額明細
//				L5735E-正常戶餘額明細
//				L5735G-住宅貸款餘額明細
//				L5735I-補助貸款餘額明細
//				L5735J-政府優惠貸款餘額明細
//				L5735K-保險業投資不動產及放款情形
				l5735Report.exec(tradeCode, tradeName, titaVo);

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