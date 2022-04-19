package com.st1.itx.trade.L9;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.MonthlyLM052Loss;
import com.st1.itx.db.service.MonthlyLM052LossService;
import com.st1.itx.tradeService.TradeBuffer;

/**
 * L9111會計部核定備抵損失查詢
 * 
 * @author ST1 - Chih Wei
 * @version 1.0.0
 */
@Service("L9111")
@Scope("prototype")
public class L9111 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L9111.class);

	/* DB服務注入 */
	@Autowired
	MonthlyLM052LossService sMonthlyLM052LossService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		logger.info("active L9111 ");
		this.totaVo.init(titaVo);

		int yearMonth = Integer.parseInt(titaVo.getParam("YearMonth")) + 191100;

		MonthlyLM052Loss tMonthlyLM052Loss = sMonthlyLM052LossService.holdById(yearMonth, titaVo);

		if (tMonthlyLM052Loss == null) {
			throw new LogicException(titaVo, "E0003", "備抵損失資料檔"); // 查無資料
		} else {
			BigDecimal approvedLoss = titaVo.getParam("ApprovedLoss") == null ? BigDecimal.ZERO
					: new BigDecimal(titaVo.getParam("ApprovedLoss"));
			tMonthlyLM052Loss.setApprovedLoss(approvedLoss);
			try {
				sMonthlyLM052LossService.update(tMonthlyLM052Loss, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", "備抵損失資料檔"); // 更新失敗
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}