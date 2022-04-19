package com.st1.itx.trade.L5;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.service.JobMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.MySpring;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;

/**
 * L5810p
 * 
 * @author ST1-ChihWei
 * @version 1.0.0
 */
@Service("L5810p")
@Scope("prototype")
public class L5810p extends TradeBuffer {

	@Autowired
	JobMainService sJobMainService;

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	WebClient webClient;

	@Autowired
	L5811Batch sL5811Batch;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5810p");
		this.totaVo.init(titaVo);

		int tbsdyf = this.txBuffer.getTxCom().getTbsdyf();

		String empNo = titaVo.getTlrNo();

		int startMonth = 0;
		int endMonth = 0;
		int custNo = 0;
		String acct = "";

		if (titaVo.containsKey("StartMonth")) {
			startMonth = Integer.parseInt(titaVo.getParam("StartMonth"));
			if (startMonth > 0) {
				startMonth = startMonth + 191100; // 轉西元年
			}
		}

		if (titaVo.containsKey("EndMonth")) {
			endMonth = Integer.parseInt(titaVo.getParam("EndMonth"));
			if (endMonth > 0) {
				endMonth = endMonth + 191100; // 轉西元年
			}
		}

		if (titaVo.containsKey("CustNo")) {
			custNo = Integer.parseInt(titaVo.getParam("CustNo"));
		}

		if (titaVo.containsKey("Acct")) {
			acct = titaVo.getParam("Acct");
		}

		this.info("tbsdyf = " + tbsdyf);
		this.info("empNo = " + empNo);
		this.info("startMonth = " + startMonth);
		this.info("endMonth = " + endMonth);
		this.info("custNo = " + custNo);
		this.info("acct = " + acct);

		// 發動Usp
		sJobMainService.Usp_L9_YearlyHouseLoanInt_Upd(tbsdyf, empNo, startMonth, endMonth, custNo, acct, titaVo);

		titaVo.putParam("Year", (startMonth - 191100) / 100);
		// 產生檢核檔
		MySpring.newTask("L5811Batch", this.txBuffer, titaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}
}