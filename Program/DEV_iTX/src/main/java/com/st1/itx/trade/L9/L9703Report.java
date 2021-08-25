package com.st1.itx.trade.L9;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.buffer.TxBuffer;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.parse.Parse;

/**
 * L9703Report
 * 
 * @author ChihWei
 * @version 1.0.0
 */
@Component
@Scope("prototype")
public class L9703Report extends MakeReport {

	@Autowired
	Parse parse;

	@Autowired
	L9703Report1 l9703report1;

	@Autowired
	L9703Report2 l9703report2;

	public void exec(TitaVo titaVo, TxBuffer txbuffer) throws LogicException {

		String parentTranCode = this.getParentTranCode();

		int custNo = parse.stringToInteger(titaVo.getParam("CustNo"));

		if (custNo != 0) { // 個人
			this.info("L9703Report: CustNo != 0,出通知書");
			l9703report2.exec(titaVo, txbuffer);

		} else { // 整批
			this.info("L9703Report: CustNo == 0,出明細表+通知書");
			l9703report1.setParentTranCode(parentTranCode);
			l9703report2.setParentTranCode(parentTranCode);

			l9703report1.exec(titaVo, txbuffer);
			l9703report2.exec(titaVo, txbuffer);
		}

	}

}
