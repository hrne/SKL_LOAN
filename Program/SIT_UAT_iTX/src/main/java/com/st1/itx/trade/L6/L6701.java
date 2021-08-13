
package com.st1.itx.trade.L6;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdBank;
import com.st1.itx.db.domain.CdBankId;
import com.st1.itx.db.service.CdBankService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.util.data.DataLog;

/*
 * Tita
 * FuncCode=9,1
 * BankCode=X,3
 * BranchCode=X,4
 * BankItem=X,100
 * BranchItem=X,100
 */

@Service("L6701")
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */
public class L6701 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L6701.class);

	/* DB服務注入 */
	@Autowired
	public CdBankService sCdBankService;
	@Autowired
	DateUtil dDateUtil;
	@Autowired
	Parse parse;
	@Autowired
	public DataLog dataLog;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6701 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iFuncCode = this.parse.stringToInteger(titaVo.getParam("FuncCode"));
		String iBankCode = titaVo.getParam("BankCode");
		String iBranchCode = titaVo.getParam("BranchCode");
		String iBankItem = titaVo.getParam("BankItem");
		String iBranchItem = titaVo.getParam("BranchItem");
		if (iBranchCode.isEmpty()) {
			iBranchCode = "    ";
		}
		this.info(
				"L6701 CdBank : " + iFuncCode + "-" + iBankCode + iBranchCode + "-" + iBankItem + "-" + iBranchItem);

		// 檢查輸入資料
		if (!(iFuncCode >= 1 && iFuncCode <= 5)) {
			throw new LogicException(titaVo, "E0010", "L6701"); // 功能選擇錯誤
		}

		// 更新行庫資料檔
		CdBank tCdBank = new CdBank();
		CdBankId tCdBankId = new CdBankId();
		switch (iFuncCode) {
		case 1: // 新增
			tCdBankId.setBankCode(iBankCode);
			tCdBankId.setBranchCode(iBranchCode);
			tCdBank.setCdBankId(tCdBankId);

			tCdBank.setBankItem(iBankItem);
			tCdBank.setBranchItem(iBranchItem);
			tCdBank.setCreateDate(
					parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
			tCdBank.setCreateEmpNo(titaVo.getTlrNo());
			tCdBank.setLastUpdate(
					parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
			tCdBank.setLastUpdateEmpNo(titaVo.getTlrNo());
			try {
				sCdBankService.insert(tCdBank, titaVo);
				this.info("2");
			} catch (DBException e) {
				if (e.getErrorId() == 2) {
					throw new LogicException(titaVo, "E0002", "行庫代號:" + iBankCode + "-" + iBranchCode); // 新增資料已存在
				} else {
					throw new LogicException(titaVo, "E0005", e.getErrorMsg()); // 新增資料時，發生錯誤
				}
			}
			break;

		case 2: // 修改
			tCdBank = sCdBankService.holdById(new CdBankId(iBankCode, iBranchCode));
			if (tCdBank == null) {
				throw new LogicException(titaVo, "E0003", "行庫代號:" + iBankCode + "-" + iBranchCode); // 修改資料不存在
			}
			CdBank tCdBank2 = (CdBank) dataLog.clone(tCdBank); ////
			try {
				tCdBank.setBankItem(iBankItem);
				tCdBank.setBranchCode(iBranchCode);
				tCdBank.setBranchItem(iBranchItem);
				tCdBank.setLastUpdate(
						parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
				tCdBank.setLastUpdateEmpNo(titaVo.getTlrNo());
				tCdBank = sCdBankService.update2(tCdBank, titaVo); ////
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", e.getErrorMsg()); // 更新資料時，發生錯誤
			}
			dataLog.setEnv(titaVo, tCdBank2, tCdBank); ////
			dataLog.exec(); ////
			break;

		case 4: // 刪除
			tCdBank = sCdBankService.holdById(new CdBankId(iBankCode, iBranchCode));
			if (tCdBank != null) {
				try {
					sCdBankService.delete(tCdBank);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0008", e.getErrorMsg()); // 刪除資料時，發生錯誤
				}
			} else {
				throw new LogicException(titaVo, "E0004", "行庫代號:" + iBankCode + "-" + iBranchCode); // 刪除資料不存在
			}
			break;

		case 5: // inq
			break;
		}
		this.info("3");
		this.addList(this.totaVo);
		return this.sendList();
	}
}