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

@Service("L9739p")
@Scope("prototype")
/**
 * L9739 檢核政府優惠房貸利率脫鉤
 * 
 * @author
 * @version 1.0.0
 */
public class L9739p extends TradeBuffer {

	@Autowired
	L9739Report l9739Report;

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	WebClient webClient;

	String txCD = "L9739";
	String txName = "檢核政府優惠房貸利率脫鉤";

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active " + txCD + "p");
		this.totaVo.init(titaVo);

		this.info(txCD + "p titaVo.getTxcd() = " + titaVo.getTxcd());
		String parentTranCode = titaVo.getTxcd();

		l9739Report.setParentTranCode(parentTranCode);

		// 帳務日(西元)
		int tbsdy = this.txBuffer.getTxCom().getTbsdyf();
		// 月底日(西元)
		int mfbsdy = this.txBuffer.getTxCom().getMfbsdyf();
		// 上月底日(西元)
		int mfbsdsy = this.txBuffer.getTxCom().getLmndyf();

		int iYearMonth = 0;

		// 帳務日==月底日
		if (tbsdy == mfbsdy) {
			iYearMonth = mfbsdy / 100;
		}
		// 帳務日< 月底日 取上的月底
		if (tbsdy < mfbsdy) {
			iYearMonth = mfbsdsy / 100;
		}

		boolean isFinish = false;

		isFinish = l9739Report.exec(titaVo,iYearMonth);

		if (isFinish) {
			webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009",
					titaVo.getParam("TLRNO"), txCD + txName + " 已完成", titaVo);
		} else {
			webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009",
					titaVo.getParam("TLRNO"), txCD + txName + " 查無資料", titaVo);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}