package com.st1.itx.trade.L7;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.service.Ias39LoanCommitService;
import com.st1.itx.trade.LM.LM011Report;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.MySpring;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;

@Service("L7201")
@Scope("prototype")
/**
 * 
 * 
 * @author ChihWei
 * @version 1.0.0
 */
public class L7201 extends TradeBuffer {

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	public WebClient webClient;

	@Autowired
	LM011Report lM011Report;

	@Autowired
	Ias39LoanCommitService sIas39LoanCommitService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L7021");
		this.totaVo.init(titaVo);

		String dateYear = titaVo.getParam("Year");
		String dateMonth = titaVo.getParam("Month");
		int dateSent = Integer.parseInt(dateYear + dateMonth + "00") + 19110000;

		String empNo = titaVo.getTlrNo();

		// IF RemakeYN = "Y" THEN 更新table

		if (titaVo.getParam("RemakeYN").equals("Y")) {
			this.info("L7201: RemakeYN == Y. Update the table.");
			sIas39LoanCommitService.Usp_L7_Ias39LoanCommit_Upd(dateSent, empNo, titaVo);
		}
		// 整批處理：月底放款承諾提存
		MySpring.newTask("BS910", this.txBuffer, titaVo);

		this.info("L7201 titaVo.getTxcd() = " + titaVo.getTxcd());
		String parentTranCode = titaVo.getTxcd();

		lM011Report.setParentTranCode(parentTranCode);
		lM011Report.exec(titaVo);

		webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009", titaVo.getParam("TLRNO"), "LM011表外放款承諾資料產出已完成", titaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}
}