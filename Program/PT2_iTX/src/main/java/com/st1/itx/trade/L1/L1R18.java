package com.st1.itx.trade.L1;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L1R18")
@Scope("prototype")
/**
 * 依戶號或統編調客戶名稱
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L1R18 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CustMainService iCustMainService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L1R18 ");

		this.totaVo.init(titaVo);

		String iCustId = titaVo.getParam("RimCustId");
		int iCustNo = Integer.valueOf(titaVo.getParam("RimCustNo"));
		CustMain iCustMain = new CustMain();
		if (iCustNo == 0) {
			iCustMain = iCustMainService.custIdFirst(iCustId, titaVo);
		} else {
			iCustMain = iCustMainService.custNoFirst(iCustNo, iCustNo, titaVo);
		}

		if (iCustMain == null) {
			if (iCustNo == 0) {
				throw new LogicException("E0001", "客戶檔查無此統一編號:" + iCustId); // 查無資料
			} else {
				throw new LogicException("E0001", "客戶檔查無此戶號:" + iCustNo); // 查無資料
			}
		}

		totaVo.putParam("L1R18CustName", iCustMain.getCustName());

		this.addList(this.totaVo);
		return this.sendList();
	}
}