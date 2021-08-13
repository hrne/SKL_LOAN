package com.st1.itx.trade.L7;

import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.service.springjpa.cm.L7002ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L7002")
@Scope("prototype")
/**
 * 
 * 
 * @author Eric Chang
 * @version 1.0.0
 */
public class L7002 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L7002.class);

	@Autowired
	public L7002ServiceImpl L7002ServiceImpl;

	@Autowired
	public L7002Report txReport;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L7002 ");
		this.totaVo.init(titaVo);

		txReport.exec(titaVo);
		this.addList(this.totaVo);
		return this.sendList();
	}

}