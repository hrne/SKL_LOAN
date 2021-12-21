package com.st1.itx.trade.L8;

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

@Service("L8205p")
@Scope("prototype")
/**
 * 
 * 
 * @author ChihCheng
 * @version 1.0.0
 */
public class L8205p extends TradeBuffer {

	@Autowired
	L8205Report1 l8205Report1;

	@Autowired
	L8205Report2 l8205Report2;

	@Autowired
	L8205Report3 l8205Report3;

	@Autowired
	L8205Report4 l8205Report4;

	@Autowired
	L8205Report5 l8205Report5;

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	public WebClient webClient;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8205p");
		this.totaVo.init(titaVo);
		boolean isFinish = false;

		String parentTranCode = titaVo.getTxcd();
		int iType = Integer.parseInt(titaVo.getParam("Type"));
		this.info("iType=" + iType);

		switch (iType) {

		case 1:
			// 樣態三報表

			l8205Report1.setParentTranCode(parentTranCode);

			isFinish = l8205Report1.exec(titaVo);

			if (isFinish) {
				webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009", titaVo.getParam("TLRNO"), "疑似洗錢樣態3合理性報表完成", titaVo);
			} else {
				webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009", titaVo.getParam("TLRNO"), "疑似洗錢樣態3合理性報表完成", titaVo);
			}

			break;

		case 2:
			// 合理性報表

			l8205Report2.setParentTranCode(parentTranCode);

			isFinish = l8205Report2.exec(titaVo);

			if (isFinish) {
				webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009", titaVo.getParam("TLRNO"), "疑似洗錢合理性報表完成", titaVo);
			} else {
				webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009", titaVo.getParam("TLRNO"), "疑似洗錢合理性報表完成", titaVo);
			}

			break;
		case 3:
			// 樣態3延遲交易確認

			l8205Report3.setParentTranCode(parentTranCode);

			isFinish = l8205Report3.exec(titaVo);

			if (isFinish) {
				webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009", titaVo.getParam("TLRNO"), "疑似洗錢樣態3延遲交易確認報表完成", titaVo);
			} else {
				webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009", titaVo.getParam("TLRNO"), "疑似洗錢樣態3延遲交易確認報表完成", titaVo);
			}

			break;

		case 4:
			// 延遲交易

			l8205Report4.setParentTranCode(parentTranCode);

			isFinish = l8205Report4.exec(titaVo);

			if (isFinish) {
				webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009", titaVo.getParam("TLRNO"), "疑似洗錢延遲交易確認報表完成", titaVo);
			} else {
				webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009", titaVo.getParam("TLRNO"), "疑似洗錢延遲交易確認報表完成", titaVo);
			}

			break;

		case 5:
			// 訪談報表
			isFinish = l8205Report5.exec(titaVo);

			if (isFinish) {
				webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009", titaVo.getParam("TLRNO"), "疑似洗錢訪談報表完成", titaVo);
			} else {
				webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009", titaVo.getParam("TLRNO"), "疑似洗錢訪談報表完成", titaVo);
			}
			break;

		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}