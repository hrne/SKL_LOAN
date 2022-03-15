package com.st1.itx.trade.L6;

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
import com.st1.itx.util.parse.Parse;

/* Tita
 * RimFuncCode=9,1
 * RimTxCode=X,5
 * RimL6R01BankCode=X,3
 * RimL6R01BranchCode=X,4
 */
@Service("L6R01") // 尋找行庫資料檔資料
@Scope("prototype")
/**
 * 
 * 
 * @author Yoko
 * @version 1.0.0
 */
public class L6R01 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L6R01.class);

	/* DB服務注入 */
	@Autowired
	public CdBankService sCdBankService;
	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6R01 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iRimFuncCode = this.parse.stringToInteger(titaVo.getParam("RimFuncCode"));
		String iRimTxCode = titaVo.getParam("RimTxCode");
		String iBankCode = titaVo.getParam("RimL6R01BankCode");
		String iBranchCode = titaVo.getParam("RimL6R01BranchCode");

		// 檢查輸入資料
		if (iRimTxCode.isEmpty()) {
			throw new LogicException(titaVo, "E0009", "L6R01"); // 交易代號不可為空白
		}
		if (!(iRimFuncCode >= 1 && iRimFuncCode <= 5)) {
			throw new LogicException(titaVo, "E0010", "L6R01"); // 功能選擇錯誤
		}

		// 初始值Tota
		moveTotaCdBank(new CdBank());

		// 查詢行庫資料檔
		CdBank tCdBank = sCdBankService.findById(new CdBankId(iBankCode, iBranchCode), titaVo);
		/* 如有找到資料 */
		if (tCdBank != null) {
			if (iRimTxCode.equals("L6701") && iRimFuncCode == 1) {
				throw new LogicException(titaVo, "E0002", "行庫代號:" + iBankCode + "-" + iBranchCode); // 新增資料已存在
			} else {
				/* 將每筆資料放入Tota */
				moveTotaCdBank(tCdBank);
			}
		} else {
			if (iRimTxCode.equals("L6701") && iRimFuncCode == 1) {
				this.addList(this.totaVo);
				return this.sendList();
			} else {
				throw new LogicException(titaVo, "E0001", "行庫代號:" + iBankCode + "-" + iBranchCode); // 查無資料
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	// 將每筆資料放入Tota
	// 行庫資料檔
	private void moveTotaCdBank(CdBank mCdBank) throws LogicException {
		this.totaVo.putParam("L6R01BankCode", mCdBank.getBankCode());
		this.totaVo.putParam("L6R01BranchCode", mCdBank.getBranchCode());
		this.totaVo.putParam("L6R01BankItem", mCdBank.getBankItem());
		this.totaVo.putParam("L6R01BranchItem", mCdBank.getBranchItem());
	}

}