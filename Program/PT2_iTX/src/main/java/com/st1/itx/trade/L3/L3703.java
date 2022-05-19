package com.st1.itx.trade.L3;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.LoanBorTx;
import com.st1.itx.db.domain.LoanBorTxId;
import com.st1.itx.db.service.LoanBorTxService;
import com.st1.itx.db.service.LoanCustRmkService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.parse.Parse;

@Service("L3703")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L3703 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public LoanBorTxService LoanBorTxService;

	@Autowired
	public LoanCustRmkService sLoanCustRmkService;
	
	@Autowired
	public DataLog dataLog;
	
	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L3703 ");
		this.totaVo.init(titaVo);

		// tita
		// 戶號
		int iCustNo = parse.stringToInteger(titaVo.getParam("CustNo"));
		// 額度
		int iFacmNo = parse.stringToInteger(titaVo.getParam("FacmNo"));
		// 撥款序號
		int iBormNo = parse.stringToInteger(titaVo.getParam("BormNo"));
		// 放款明細序號
		int iBorxNo = parse.stringToInteger(titaVo.getParam("BorxNo"));

		// new table
		LoanBorTx tLoanBorTx = new LoanBorTx();

		// new PK
		LoanBorTxId LoanBorTxId = new LoanBorTxId();

		// 塞值到TablePK
		LoanBorTxId.setCustNo(iCustNo);
		LoanBorTxId.setFacmNo(iFacmNo);
		LoanBorTxId.setBormNo(iBormNo);
		LoanBorTxId.setBorxNo(iBorxNo);
		
		tLoanBorTx = new LoanBorTx();

		// PK找帳務備忘錄明細檔HOLD資料
		tLoanBorTx = LoanBorTxService.holdById(LoanBorTxId);

		if (tLoanBorTx == null) {
			throw new LogicException(titaVo, "E0003", iCustNo + "不存在於帳務明細檔。");
		}
		TempVo tTempVo = new TempVo();
		tTempVo.clear();
		tTempVo = tTempVo.getVo(tLoanBorTx.getOtherFields());
		tTempVo.putParam("Note", titaVo.getParam("Note"));
		tLoanBorTx.setOtherFields(tTempVo.getJsonString());
		
		
		tLoanBorTx = LoanBorTxService.holdById(LoanBorTxId);
		
		if (tLoanBorTx == null) {
			throw new LogicException(titaVo, "E0003", "L3703 該戶號,備忘錄序號" + iCustNo + "不存在帳務備忘錄明細資料檔。");
		}
		// 變更前
		LoanBorTx beforeLoanBorTx = (LoanBorTx) dataLog.clone(tLoanBorTx);

		tLoanBorTx.setOtherFields(tTempVo.getJsonString());

		try {
			// 修改
			tLoanBorTx = LoanBorTxService.update(tLoanBorTx, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007", e.getErrorMsg());
		}
		
		// 紀錄變更前變更後
		dataLog.setEnv(titaVo, beforeLoanBorTx, tLoanBorTx);
		dataLog.exec("修改帳務備忘錄明細資料檔");

		this.addList(this.totaVo);
		return this.sendList();
	}
}