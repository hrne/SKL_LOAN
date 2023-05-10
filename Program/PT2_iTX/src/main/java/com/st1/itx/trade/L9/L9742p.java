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
 * L9742p
 * 
 * @author ChihWei
 * @version 1.0.0
 */
@Service("L9742p")
@Scope("prototype")
public class L9742p extends TradeBuffer {

	@Autowired
	L9742Report l9742Report;

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	WebClient webClient;

	String tranCode = "L9742";
	String tranName = "企金戶還本收據及繳息收據";

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active " + tranCode + "p");
		this.totaVo.init(titaVo);

		this.info(tranCode + "p titaVo.getTxcd() = " + titaVo.getTxcd());
		String parentTranCode = titaVo.getTxcd();

		l9742Report.setParentTranCode(parentTranCode);

		boolean isFinish = true;

		int functionCode = Integer.valueOf(titaVo.getParam("FunctionCode"));
		int option = 0;

		this.info("functionCode = " + functionCode);

		// 先確認是手動輸入或整批
		if (functionCode == 1) {
			//1.還本 2.利息 3.手續費
			option = Integer.valueOf(titaVo.getParam("inputOption"));

			this.info("option = " + option);

			if (option == 0) {
				isFinish = l9742Report.exec(titaVo, 1) && l9742Report.exec(titaVo, 2) && l9742Report.exec(titaVo, 3);
			} else {
				isFinish = l9742Report.exec(titaVo, option);
			}
		} else {
			//1.還本 2.利息 3.手續費
			isFinish = l9742Report.exec(titaVo, 1) && l9742Report.exec(titaVo, 2) && l9742Report.exec(titaVo, 3);
		}

		if (isFinish) {
			webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009",
					titaVo.getParam("TLRNO"), tranCode + tranName + "已完成", titaVo);
		} else {
			webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009",
					titaVo.getParam("TLRNO"), tranCode + tranName + "查無資料", titaVo);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}