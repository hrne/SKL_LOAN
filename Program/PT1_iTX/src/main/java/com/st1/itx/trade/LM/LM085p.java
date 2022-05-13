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
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.http.WebClient;

/**
 * LM085 逾期月報表
 * 
 * @author kaichieh
 * @version 1.0.0
 */
@Service("LM085p")
@Scope("prototype")
public class LM085p extends TradeBuffer {

	@Autowired
	LM085Report lm085report;

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	WebClient webClient;
	
	String txcd = "LM085";
	String txName = "逾期月報表";

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {


		this.totaVo.init(titaVo);
		
		this.info(txcd + "p titaVo.getTxcd() = " + titaVo.getTxcd());
		String parentTranCode = titaVo.getTxcd();

		lm085report.setParentTranCode(parentTranCode);
		
		int iAcDate = Integer.parseInt(titaVo.getEntDy());

		// MSG帶入預設值
		String ntxbuf = titaVo.getTlrNo() + FormatUtil.padX("LM085", 60) + iAcDate;

		// 帳務日(西元)
		int tbsdy = this.txBuffer.getTxCom().getTbsdyf();
		// 月底日(西元)
		int mfbsdy = this.txBuffer.getTxCom().getMfbsdyf();
		// 上個月底日(西元)
		int lmndy = this.txBuffer.getTxCom().getLmndyf();
		
		// 年
		int iYear = mfbsdy / 10000;
		// 月
		int iMonth = (mfbsdy / 100) % 100;
		// 當年月
		int thisYM = 0;

		int lastYM = lmndy / 100;
		// 判斷帳務日與月底日是否同一天
		if (tbsdy < mfbsdy) {
			iYear = iMonth - 1 == 0 ? (iYear - 1) : iYear;
			iMonth = iMonth - 1 == 0 ? 12 : iMonth - 1;
		}

		thisYM = iYear * 100 + iMonth;
		
		lm085report.setTxBuffer(this.getTxBuffer());
		
		boolean isFinish = lm085report.exec(titaVo, thisYM,lastYM);

		if (isFinish) {
			webClient.sendPost(dDateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "LC009", ntxbuf,
					txcd + txName +" 已完成", titaVo);
		} else {
			webClient.sendPost(dDateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "LC009", ntxbuf,
					txcd + txName +" 查無資料", titaVo);
		}
		
		this.addList(this.totaVo);
		
		return this.sendList();
	}
}