package com.st1.itx.trade.L4;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
//import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;

import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.BankAuthActCom;

/**
 * Tita<br>
 * FUNCTIONCD=9,1<br>
 * CUSTNO=9,7<br>
 * FACMNO=9,3<br>
 * REPAYBANK=9,3<br>
 * REPAYACCTNO=9,14<br>
 * LIMAMT=9,14.2<br>
 * RELATIONIND=9,2<br>
 * RELACCTNAME=X,100<br>
 * RELACCTBIRTH=9,7<br>
 * RELACCTGENDER=X,1<br>
 * END=X,1<br>
 */

@Service("L4410")
@Scope("prototype")
public class L4410 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L4410.class);

	@Autowired
	public BankAuthActCom bankAuthActCom;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4410 ");
		this.totaVo.init(titaVo);

		bankAuthActCom.setTxBuffer(this.getTxBuffer());
		int returnCode = 0;

		returnCode = bankAuthActCom.acctCheck(titaVo);

		this.info("returnCode :" + returnCode);
		this.addList(this.totaVo);
		return this.sendList();
	}
}