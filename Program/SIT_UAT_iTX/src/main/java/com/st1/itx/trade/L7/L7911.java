package com.st1.itx.trade.L7;

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

@Service("L7911")
@Scope("prototype")
/**
 * eloan戶號查詢
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L7911 extends TradeBuffer {

	@Autowired
	public CustMainService iCustMainService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 500;

		this.info("active L7911 ");
		this.totaVo.init(titaVo);
		String iCustId = titaVo.getParam("CustId");

		CustMain iCustMain = null;
		iCustMain = iCustMainService.custIdFirst(iCustId, titaVo);
		if (iCustMain == null) {
			throw new LogicException(titaVo, "E0001", "此身分證/統一編號不存在");
		}
		totaVo.putParam("OCustId", iCustMain.getCustId());
		totaVo.putParam("OCustNo", iCustMain.getCustNo());
		totaVo.putParam("OCustName", iCustMain.getCustName());

		this.addList(this.totaVo);
		return this.sendList();
	}
}