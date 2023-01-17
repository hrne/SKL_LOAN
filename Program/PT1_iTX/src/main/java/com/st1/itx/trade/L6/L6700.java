package com.st1.itx.trade.L6;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdLoanNotYet;
import com.st1.itx.db.service.CdLoanNotYetService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;

@Service("L6700")
@Scope("prototype")
/**
 * 
 * 
 * @author eric chang
 * @version 1.0.0
 */
public class L6700 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CdLoanNotYetService cdLoanNotYetService;

	@Autowired
	public DataLog dataLog;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6700 ");
		this.totaVo.init(titaVo);

		String iFunCode = titaVo.get("FunCode").trim();
		String iNotYetCode = titaVo.get("NotYetCode").trim();

		CdLoanNotYet cdLoanNotYet = cdLoanNotYetService.holdById(iNotYetCode, titaVo);

		if (cdLoanNotYet == null) {
			if ("1".equals(iFunCode)) {
				cdLoanNotYet = new CdLoanNotYet();
				cdLoanNotYet.setNotYetCode(iNotYetCode);
				cdLoanNotYet = setCdLoanNotYet(titaVo, cdLoanNotYet);
				try {
					cdLoanNotYetService.insert(cdLoanNotYet);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0005", "未齊件代碼檔");
				}
			} else {
				throw new LogicException("E0001", "未齊件代碼檔");
			}
		} else {
			if ("1".equals(iFunCode)) {
				throw new LogicException("E0002", "未齊件代碼檔");
			} else if ("2".equals(iFunCode)) {
				CdLoanNotYet cdLoanNotYet2 = (CdLoanNotYet) dataLog.clone(cdLoanNotYet);

				cdLoanNotYet.setNotYetCode(iNotYetCode);
				cdLoanNotYet = setCdLoanNotYet(titaVo, cdLoanNotYet);
				try {
					cdLoanNotYet = cdLoanNotYetService.update2(cdLoanNotYet, titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", "未齊件代碼檔");
				}
				//
				dataLog.setEnv(titaVo, cdLoanNotYet2, cdLoanNotYet);
				dataLog.exec("修改未齊件代碼檔");
			} else if ("4".equals(iFunCode)) {
				try {
					cdLoanNotYetService.delete(cdLoanNotYet, titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0008", "未齊件代碼檔");
				}
				dataLog.setEnv(titaVo, cdLoanNotYet, cdLoanNotYet);
				dataLog.exec("刪除未齊件代碼檔");
				
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private CdLoanNotYet setCdLoanNotYet(TitaVo titaVo, CdLoanNotYet cdLoanNotYet) {
		cdLoanNotYet.setNotYetItem(titaVo.get("NotYetItem").trim());
		cdLoanNotYet.setYetDays(Integer.valueOf(titaVo.get("YetDays").trim()));
		cdLoanNotYet.setEnable(titaVo.get("Enable").trim());

		return cdLoanNotYet;
	}
}