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

@Service("L9740p")
@Scope("prototype")
/**
 * L9740 檢核政府優惠房貸利率脫鉤
 * 
 * @author
 * @version 1.0.0
 */
public class L9740p extends TradeBuffer {

	@Autowired
	L9740Report l9740Report;

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	WebClient webClient;

	String txCD = "L9740";
	String txName = "公會無自用住宅利率報送";

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active " + txCD + "p");
		this.totaVo.init(titaVo);

//		// 帳務日(西元)
//		int tbsdy = this.txBuffer.getTxCom().getTbsdyf();
//		// 月底日(西元)
//		int mfbsdy = this.txBuffer.getTxCom().getMfbsdyf();
//
//		// 年
//		int iYear = mfbsdy / 10000;
//		// 月
//		int iMonth = (mfbsdy / 100) % 100;

//		int drawDownDateA1 = Integer.valueOf(titaVo.getParam("DrawDownDateA1")) + 19110000;
//		int drawDownDateA2 = Integer.valueOf(titaVo.getParam("DrawDownDateA2")) + 19110000;
//
//		int drawDownDateB1 = Integer.valueOf(titaVo.getParam("DrawDownDateB1")) + 19110000;

		int drawDownDateC1 = Integer.valueOf(titaVo.getParam("DrawDownDateC1")) + 19110000;

		int year = drawDownDateC1 / 10000;
		int month = drawDownDateC1 / 100 % 100;
		int day = drawDownDateC1 % 100;

		switch (month) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			if (day < 31) {
				month = month - 1;
			}
			break;
		case 2:
			if (year % 4 == 0) {
				if (day < 29) {
					month = month - 1;
				}
			} else {
				if (day < 28) {
					month = month - 1;
				}
			}

			break;
		case 4:
		case 6:
		case 9:
		case 11:
			if (day < 30) {
				month = month - 1;
			}
			break;
		}

		year = month == 0 ? year - 1 : year;
		month = month == 0 ? 12 : month;

		int yearMonth = year * 100 + month;

		this.info("yearMonth =" + yearMonth);

		// 判斷帳務日與月底日是否同一天
//		if (tbsdy < mfbsdy) {
//			iYear = iMonth - 1 == 0 ? (iYear - 1) : iYear;
//			iMonth = iMonth - 1 == 0 ? 12 : iMonth - 1;
//		}

		this.info(txCD + "p titaVo.getTxcd() = " + titaVo.getTxcd());
		String parentTranCode = titaVo.getTxcd();

		l9740Report.setParentTranCode(parentTranCode);

		boolean isFinish = false;

		isFinish = l9740Report.exec(titaVo, yearMonth);

		dDateUtil.init();

		if (isFinish) {
			webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009",
					titaVo.getTlrNo() + txCD, txCD + txName + " 已完成", titaVo);
		} else {
			webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009",
					titaVo.getTlrNo() + txCD, txCD + txName + " 查無資料", titaVo);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}