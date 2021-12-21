package com.st1.itx.trade.L8;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.service.MlaundryDetailService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.util.MySpring;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.data.DataLog;

/**
 * Tita FuncCode=9,1 AcDate=9,7 CustNo=9,7 FacmNo=9,3 BormNo=9,3 Factor=9,2
 * TotalAmt=9,14 TotalCnt=9,4 MemoSeq=9,2 Rational=X,1 EmpNoDesc=X,100
 * ManagerDesc=X,100 END=X,1
 */

@Service("L8202")
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */
public class L8202 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L8202.class);

	/* DB服務注入 */
	@Autowired
	public MlaundryDetailService sMlaundryDetailService;
	@Autowired
	DateUtil dDateUtil;
	@Autowired
	Parse parse;
	@Autowired
	public DataLog dataLog;

	@Autowired
	SendRsp sendRsp;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8202 ");
		this.totaVo.init(titaVo);

		// 執行交易
		MySpring.newTask("L8202Batch", this.txBuffer, titaVo);
		this.addList(this.totaVo);
		return this.sendList();
	}

}
