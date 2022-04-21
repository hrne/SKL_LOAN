package com.st1.itx.trade.LM;

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

@Service("LM040p")
@Scope("prototype")
/**
 * 
 * 
 * @author ChihWei
 * @version 1.0.0
 */
public class LM040p extends TradeBuffer {

	@Autowired
	LM040Report lM040Report;

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	public WebClient webClient;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active LM040p");

		int tbsdy = this.txBuffer.getTxCom().getTbsdyf();
		// 月底日(西元)
		int mfbsdy = this.txBuffer.getTxCom().getMfbsdyf();
		// 年
		int iYear = mfbsdy / 10000;
		// 月
		int iMonth = (mfbsdy / 100) % 100;
		// 當年月
		int thisYM = 0;

		// 判斷帳務日與月底日是否同一天
		if (tbsdy < mfbsdy) {
			iYear = iMonth - 1 == 0 ? (iYear - 1) : iYear;
			iMonth = iMonth - 1 == 0 ? 12 : iMonth - 1;
		}

		thisYM = iYear * 100 + iMonth;

		this.totaVo.init(titaVo);

		this.info("LM040p titaVo.getTxcd() = " + titaVo.getTxcd());
		String parentTranCode = titaVo.getTxcd();

		lM040Report.setParentTranCode(parentTranCode);

		boolean isFinished = lM040Report.exec(titaVo, thisYM);

		if (isFinished) {
			webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009",
					titaVo.getParam("TLRNO"), "LM040地區別正常戶金額已完成", titaVo);
		} else {
			webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009",
					titaVo.getParam("TLRNO"), "LM040地區別正常戶金額無資料", titaVo);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}