package com.st1.itx.trade.L6;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.db.domain.TxErrCode;
import com.st1.itx.db.domain.TxTranCode;
import com.st1.itx.db.service.TxErrCodeService;
import com.st1.itx.db.service.TxTranCodeService;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.menu.MenuBuilder;
import com.st1.itx.util.parse.Parse;

@Service("L6204")
@Scope("prototype")
/**
 * 
 * 
 * @author ChihCheng
 * @version 1.0.0
 */
public class L6204 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public TxErrCodeService sTxErrCodeService;

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	Parse parse;

	@Autowired
	public DataLog dataLog;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6204 ");
		this.totaVo.init(titaVo);

		String iFunCode = titaVo.get("FunCode").trim();
		String iErrCode = titaVo.get("ErrCode").trim();

		TxErrCode tTxErrCode = sTxErrCodeService.holdById(iErrCode);
		this.info("tTxErrCode =" + tTxErrCode);
		this.info("iFunCode   =" + iFunCode);
		
		if (tTxErrCode == null) {
			if ("2".equals(iFunCode)) {
				throw new LogicException(titaVo, "E0003", "交易代碼:" + iErrCode);
			} else if ("4".equals(iFunCode)) {
				throw new LogicException(titaVo, "E0004", "交易代碼:" + iErrCode);
			}

			tTxErrCode = new TxErrCode();
			tTxErrCode = MoveToDb(iErrCode, tTxErrCode, titaVo);
			try {
				sTxErrCodeService.insert(tTxErrCode);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005", e.getErrorMsg()); // 新增資料已存在
			}
		} else {
			if ("1".equals(iFunCode)) {
				throw new LogicException(titaVo, "E0002", "交易代碼:" + iErrCode);
			}
			try {
				TxErrCode tTxErrCode2 = (TxErrCode) dataLog.clone(tTxErrCode);
				if ("2".equals(iFunCode)) {

					tTxErrCode = MoveToDb(iErrCode, tTxErrCode, titaVo);
					sTxErrCodeService.update2(tTxErrCode, titaVo);
					int iCustNo = Integer.valueOf(tTxErrCode.getLastUpdateEmpNo());
					this.info("iCustNo   = " + iCustNo);
					titaVo.putParam("CustNo", iCustNo);
					dataLog.setEnv(titaVo, tTxErrCode2, tTxErrCode); ////
					dataLog.exec("修改錯誤代碼"); ////

				}
				
				if ("4".equals(iFunCode)) {

					this.info("tTxErrCode2   = " + tTxErrCode2);
					tTxErrCode = MoveToDb(iErrCode, tTxErrCode, titaVo);
					sTxErrCodeService.delete(tTxErrCode, titaVo);
					int iCustNo = Integer.valueOf(tTxErrCode.getLastUpdateEmpNo());
					this.info("iCustNo   = " + iCustNo);
					titaVo.putParam("CustNo", iCustNo);
					dataLog.setEnv(titaVo, tTxErrCode2, tTxErrCode);
					dataLog.exec("刪除錯誤代碼");
				}

			} catch (DBException e) {
				if ("2".equals(iFunCode)) {
					throw new LogicException(titaVo, "E0007", e.getErrorMsg());
				} else if ("4".equals(iFunCode)) {
					throw new LogicException(titaVo, "E0008", e.getErrorMsg());
				}
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private TxErrCode MoveToDb(String ErrCode, TxErrCode tTxErrCode, TitaVo titaVo) throws LogicException {

		tTxErrCode.setErrCode(ErrCode);
		tTxErrCode.setErrContent(titaVo.get("ErrContent").trim());

		return tTxErrCode;

	}
}