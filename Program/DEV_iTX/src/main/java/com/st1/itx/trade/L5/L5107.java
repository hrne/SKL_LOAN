package com.st1.itx.trade.L5;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.InnReCheck;
import com.st1.itx.db.domain.InnReCheckId;
import com.st1.itx.db.service.InnReCheckService;
import com.st1.itx.tradeService.TradeBuffer;

@Component("L5107")
@Scope("prototype")

/**
 * 指定覆審案件維護
 * 
 * @author Fegie
 * @version 1.0.0
 */

public class L5107 extends TradeBuffer {
	/* 轉型共用工具 */
	@Autowired
	public InnReCheckService iInnReCheckService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.totaVo.init(titaVo);
		int iCustNo = Integer.valueOf(titaVo.getParam("CustNo"));
		int iFacmNo = Integer.valueOf(titaVo.getParam("FacmNo"));
		int iReChkYearMonth = Integer.valueOf(titaVo.getParam("ReChkYearMonth"));
		int iCycle = Integer.valueOf(titaVo.getParam("Cycle"));
		String iRemark = titaVo.getParam("Remark");
		
		InnReCheck iInnReCheck = new InnReCheck();
		InnReCheckId iInnReCheckId = new InnReCheckId();
		iInnReCheckId.setYearMonth(0);
		iInnReCheckId.setConditionCode(99);
		iInnReCheckId.setCustNo(iCustNo);
		iInnReCheckId.setFacmNo(iFacmNo);
		iInnReCheck.setInnReCheckId(iInnReCheckId);
		iInnReCheck.setReChkYearMonth(iReChkYearMonth+191100);
		// iInnReCheck.setCycle(iCycle);
		// 20211112 morning, 根據Fegie指示先註解掉這行以解決error
		// - xiangwei
		iInnReCheck.setRemark(iRemark);
		iInnReCheck.setSpecifyFg("Y");
		try {
			iInnReCheckService.insert(iInnReCheck, titaVo);
		}catch (DBException e) {
			throw new LogicException(titaVo, "E0005", "已有相同資料"); // 資料新建錯誤
		}
		
		this.addList(this.totaVo);
		return this.sendList();
	}
}
