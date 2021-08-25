package com.st1.itx.trade.L5;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.service.CollListService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.db.domain.CollList;
import com.st1.itx.db.domain.CollListId;

@Component("L5606")
@Scope("prototype")

/**
 * 人員資料登錄
 * 
 * @author Fegie
 * @version 1.0.0
 */

public class L5606 extends TradeBuffer {
	/* 轉型共用工具 */
	@Autowired
	public CollListService iCollListService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.totaVo.init(titaVo);
		this.info("L5605 start");
		int iCustNo = Integer.valueOf(titaVo.getParam("CustNo"));
		int iFacmNo = Integer.valueOf(titaVo.getParam("FacmNo"));

		String iCollPsn = titaVo.getParam("AccCollPsn");
		String iLegalCons = titaVo.getParam("LegalPsn");
		CollListId iCollListId = new CollListId();
		CollList iCollList = new CollList();
		iCollListId.setCustNo(iCustNo);
		iCollListId.setFacmNo(iFacmNo);

		iCollList = iCollListService.holdById(iCollListId, titaVo);
		if (iCollList == null) {
			throw new LogicException(titaVo, "E0001", "");
		} else {
			iCollList.setAccCollPsn(iCollPsn);
			iCollList.setLegalPsn(iLegalCons);
			try {
				iCollListService.update(iCollList, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005", "");
			}
		}
		this.addList(this.totaVo);
		return this.sendList();
	}

}
