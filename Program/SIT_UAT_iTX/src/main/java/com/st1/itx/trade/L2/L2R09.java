package com.st1.itx.trade.L2;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdBank;
import com.st1.itx.db.domain.CdBankId;
import com.st1.itx.db.service.CdBankService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.format.FormatUtil;

/* Tita
 * RimTxCode=X,5
 * RimBankCode=X,7
 */
/**
 * L2R09 尋找行庫資料檔
 * 
 * @author iza
 * @version 1.0.0
 */
@Service("L2R09")
@Scope("prototype")
public class L2R09 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CdBankService cdBankService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2R09 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		String iBankCode = titaVo.getParam("RimBankCode");
		iBankCode = FormatUtil.padX(iBankCode, 7);
		String bankCode = FormatUtil.padX(iBankCode, 3);
		String branchCode = FormatUtil.right(iBankCode, 4);

		// 檢查輸入資料
		if (iBankCode.isEmpty()) {
			throw new LogicException(titaVo, "E2012", "行庫代號"); // 查詢資料不可為空白
		}

		// 查詢行庫代號檔
		CdBank tCdBank = cdBankService.findById(new CdBankId(bankCode, branchCode), titaVo);
		if (tCdBank == null) {
			throw new LogicException(titaVo, "E0001", " 行庫代號檔  行庫代號=" + iBankCode); // 查無資料
		}

		this.totaVo.putParam("OBankItem", tCdBank.getBankItem());
		this.totaVo.putParam("OBranchItem", tCdBank.getBranchItem());

		this.addList(this.totaVo);
		return this.sendList();
	}
}