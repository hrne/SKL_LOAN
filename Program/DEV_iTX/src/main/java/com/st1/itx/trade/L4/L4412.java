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
 * FunctionCode=9,1<br>
 * CustId=X,10<br>
 * AuthApplCode=X,1<br>
 * CustNo=9,7<br>
 * PostDepCode=X,1<br>
 * RepayAcct=9,14<br>
 * RepayAcctSeq=9,2<br>
 * AuthCode=9,1<br>
 * RelationCode=9,2<br>
 * RelAcctName=X,100<br>
 * RelAcctBirthday=9,7<br>
 * RelAcctGender=X,1<br>
 * AuthCreatedate=9,7<br>
 * END=X,1<br>
 */

@Service("L4412")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4412 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L4412.class);

	@Autowired
	public BankAuthActCom bankAuthActCom;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4412 ");
		this.totaVo.init(titaVo);

		bankAuthActCom.setTxBuffer(this.getTxBuffer());

		bankAuthActCom.acctCheck(titaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}
}