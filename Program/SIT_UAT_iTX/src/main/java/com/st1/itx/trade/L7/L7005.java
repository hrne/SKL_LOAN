package com.st1.itx.trade.L7;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.service.springjpa.cm.L7005ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L7005")
@Scope("prototype")
/**
 * 
 * 
 * @author Eric Chang
 * @version 1.0.0
 */
public class L7005 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L7005.class);

	@Autowired
	public L7005ServiceImpl L7005ServiceImpl;

	@Autowired
	public L7005Report txReport;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L7005 ");
		this.totaVo.init(titaVo);

		txReport.exec(titaVo);
		this.addList(this.totaVo);
		return this.sendList();
	}

}