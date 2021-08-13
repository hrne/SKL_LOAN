package com.st1.itx.trade.L9;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.service.springjpa.cm.L9723ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;

@Service("L9723")
@Scope("prototype")
/**
 * 
 * 
 * @author Xiang Wei Huang
 * @version 1.0.0
 */
public class L9723 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L9723.class);
	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	@Autowired
	public L9723ServiceImpl l9723ServiceImpl;
	
	private String count;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L9723 ");
		this.totaVo.init(titaVo);

			try {
				count = l9723ServiceImpl.findAll(titaVo);
			} catch (Exception e) {
				this.error("l9723ServiceImpl findByCondition " + e.getMessage());
				throw new LogicException("E0013", e.getMessage());
			}

		this.totaVo.putParam("Count", count);

		this.addList(this.totaVo);
		return this.sendList();
	}

}