package com.st1.itx.trade.L6;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.db.domain.CdLoanNotYet;
import com.st1.itx.db.service.CdLoanNotYetService;

@Service("L6R39")
@Scope("prototype")
/**
 * 
 * 
 * @author eric chang
 * @version 1.0.0
 */
public class L6R39 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L6R39.class);

	/* DB服務注入 */
	@Autowired
	public CdLoanNotYetService cdLoanNotYetService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6R39 ");
		this.totaVo.init(titaVo);

		String iFunCode = titaVo.get("FunCode").trim();
		String iNotYetCode = titaVo.get("NotYetCode").trim();

		CdLoanNotYet cdLoanNotYet = cdLoanNotYetService.findById(iNotYetCode, titaVo);

		if (cdLoanNotYet == null) {
			if ("1".equals(iFunCode)) {
				this.totaVo.putParam("NotYetCode", iNotYetCode);
				this.totaVo.putParam("NotYetItem", "");
				this.totaVo.putParam("YetDays", 0);
				this.totaVo.putParam("Enable", "Y");
			} else {
				throw new LogicException("E0001", "未齊件代碼檔");
			}
		} else {
			if ("1".equals(iFunCode)) {
				throw new LogicException("E0002", "未齊件代碼檔");
			} else {
				this.totaVo.putParam("NotYetCode", cdLoanNotYet.getNotYetCode());
				this.totaVo.putParam("NotYetItem", cdLoanNotYet.getNotYetItem());
				this.totaVo.putParam("YetDays", cdLoanNotYet.getYetDays());
				this.totaVo.putParam("Enable", cdLoanNotYet.getEnable());
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}