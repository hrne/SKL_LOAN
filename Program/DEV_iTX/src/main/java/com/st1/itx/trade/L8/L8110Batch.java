package com.st1.itx.trade.L8;

import java.util.ArrayList;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.service.springjpa.cm.L8110ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L8110Batch")
@Scope("prototype")
/**
 * 更新 AmlCustList 每日有效客戶名單<BR>
 * call by L8180
 *
 * @author Yoko
 * @version 1.0.0
 */
public class L8110Batch extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L8110Batch.class);

	@Autowired
	public L8110ServiceImpl l8110ServiceImpl;
	@Autowired
	public Parse parse;
	@Autowired
	public DateUtil dateUtil;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8110Batch ");
		this.totaVo.init(titaVo);
		int iDate = parse.stringToInteger(titaVo.getCalDy());
		dateUtil.init();
		dateUtil.setDate_1(iDate);
		dateUtil.setYears(-3);
		int iDate3Y = dateUtil.getCalenderDay();
		dateUtil.init();
		dateUtil.setDate_1(iDate);
		dateUtil.setYears(-5);
		int iDate5Y = dateUtil.getCalenderDay();
		
		try {
			l8110ServiceImpl.deleteAll(titaVo);
		} catch (Exception e) {
			throw new LogicException("E0015", ", " + e.getMessage()); // 檢查錯誤
		}
		this.batchTransaction.commit();

		try {
			l8110ServiceImpl.insertAll(iDate + 19110000, iDate3Y+ 19110000, iDate5Y+ 19110000, 25000000, titaVo);
		} catch (Exception e) {
			throw new LogicException("E0015", ", " + e.getMessage()); // 檢查錯誤
		}
		
		this.batchTransaction.commit();

		return null;
	}
}