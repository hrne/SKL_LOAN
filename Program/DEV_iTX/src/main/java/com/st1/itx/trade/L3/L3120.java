package com.st1.itx.trade.L3;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.LoanBorMain;
import com.st1.itx.db.domain.LoanBorMainId;
import com.st1.itx.db.domain.LoanBorTx;
import com.st1.itx.db.domain.LoanBorTxId;
import com.st1.itx.db.domain.TxToDoDetail;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.db.service.LoanBorTxService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.LoanCom;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.common.TxToDoCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/*
 * L3120 預約撥款刪除
 * a.此功能供預約撥款未到預約日真正撥款或預約當日撥款未成功時，均可將預約撥款資料刪除，並將佔用額度回復。
 */
/*
 * Tita
 * FuncFg=9,1
 * TimCustNo=9,7
 * FacmNo=9,3
 * BormNo=9,3
 */
/**
 * L3120 預約撥款刪除
 * 
 * @author iza
 * @version 1.0.0
 */
@Service("L3120")
@Scope("prototype")
public class L3120 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public LoanBorMainService loanBorMainService;
	@Autowired
	public LoanBorTxService loanBorTxService;
	@Autowired
	public FacMainService facMainService;

	@Autowired
	Parse parse;
	@Autowired
	LoanCom loanCom;
	@Autowired
	public TxToDoCom txToDoCom;
	@Autowired
	DateUtil dDateUtil;

	@Autowired
	SendRsp sendRsp;

	// work area
	private TitaVo titaVo;
	private BigDecimal wkDrawdownAmt;
	private int iFuncFg;
	private int iCustNo;
	private int iFacmNo;
	private int iBormNo;
	private int wkBorxNo;
	private int wkTbsDy;
	private LoanBorMain tLoanBorMain;
	private LoanBorTx tLoanBorTx;
	private LoanBorTxId tLoanBorTxId;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L3120 ");
		this.totaVo.init(titaVo);
		this.titaVo = titaVo;

		// 取得輸入資料
		iFuncFg = this.parse.stringToInteger(titaVo.getParam("FuncFg"));
		iCustNo = this.parse.stringToInteger(titaVo.getParam("TimCustNo"));
		iFacmNo = this.parse.stringToInteger(titaVo.getParam("FacmNo"));
		iBormNo = this.parse.stringToInteger(titaVo.getParam("BormNo"));
		wkTbsDy = this.txBuffer.getTxCom().getTbsdy();

		// 檢查輸入資料
		if (!(iFuncFg == 4 || iFuncFg == 5)) {
			throw new LogicException(titaVo, "E0010", "功能 = " + iFuncFg); // 功能選擇錯誤
		}

		// 查詢時直接return, 不須處理
		if (iFuncFg == 5) {
			this.addList(this.totaVo);
			return this.sendList();
		}
		// 交易需主管核可
		if (iFuncFg == 4) {
			if (!titaVo.getHsupCode().equals("1")) {
				sendRsp.addvReason(this.txBuffer, titaVo, "0004", "");
			}
		}

		// 維護放款主檔
		LoanBorMainRoutine();

		// 維護額度主檔
//		FacMainRoutine();

		// 新增放款交易內容檔
		LoanBorTxRoutine();

		// 撥款日期到期刪除應處理明細
		if (tLoanBorMain.getDrawdownDate() <= this.txBuffer.getTxCom().getTbsdy()) {
			TxToDoDetail tTxToDoDetail = new TxToDoDetail();
			tTxToDoDetail.setItemCode("RVTX00"); // 預約撥款到期
			tTxToDoDetail.setCustNo(tLoanBorMain.getCustNo());
			tTxToDoDetail.setFacmNo(tLoanBorMain.getFacmNo());
			tTxToDoDetail.setBormNo(tLoanBorMain.getBormNo());
			txToDoCom.setTxBuffer(this.getTxBuffer());
			txToDoCom.addDetail(true, 1, tTxToDoDetail, titaVo); // DupSkip = true ->重複跳過
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void LoanBorMainRoutine() throws LogicException {
		// 鎖定放款主檔
		tLoanBorMain = loanBorMainService.holdById(new LoanBorMainId(iCustNo, iFacmNo, iBormNo));
		if (tLoanBorMain == null) {
			throw new LogicException(titaVo, "E0006", "放款主檔 戶號 = " + iCustNo + " 額度編號 =  " + iFacmNo + " 預約序號 = " + iBormNo); // 鎖定資料時，發生錯誤
		}
		if (tLoanBorMain.getStatus() == 97) {
			throw new LogicException(titaVo, "E0011", "放款主檔 戶號 = " + iCustNo + " 額度編號 =  " + iFacmNo + " 預約序號 = " + iBormNo); // 該筆資料已被刪除
		}
		if (tLoanBorMain.getStatus() == 98) {
			throw new LogicException(titaVo, "E3054", "放款主檔 戶號 = " + iCustNo + " 額度編號 =  " + iFacmNo + " 預約序號 = " + iBormNo); // 該筆預約撥款資料已撥款
		}
		if (tLoanBorMain.getStatus() != 99) {
			throw new LogicException(titaVo, "E3055", "放款主檔 戶號 = " + iCustNo + " 額度編號 =  " + iFacmNo + " 預約序號 = " + iBormNo); // 該筆資料非預約撥款
		}

		wkBorxNo = tLoanBorMain.getLastBorxNo() + 1;
		wkDrawdownAmt = tLoanBorMain.getDrawdownAmt();
		tLoanBorMain.setStatus(97); // 97:預約撥款已刪除

		// 更新放款主檔
		try {
			tLoanBorMain.setLastUpdate(parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
			tLoanBorMain.setLastUpdateEmpNo(titaVo.getTlrNo());
			loanBorMainService.update(tLoanBorMain);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0008", "放款主檔 戶號 = " + iCustNo + " 額度編號 =  " + iFacmNo + " 預約序號 = " + iBormNo + " " + e.getErrorMsg()); // 刪除資料時，發生錯誤
		}
	}

	private void LoanBorTxRoutine() throws LogicException {
		// 新增放款交易內容檔
		tLoanBorTx = new LoanBorTx();
		tLoanBorTxId = new LoanBorTxId();
		loanCom.setLoanBorTx(tLoanBorTx, tLoanBorTxId, iCustNo, iFacmNo, iBormNo, wkBorxNo, titaVo);
		tLoanBorTx.setDesc("預約撥款刪除");
		tLoanBorTx.setAcDate(wkTbsDy);
		tLoanBorTx.setDisplayflag("Y");
		tLoanBorTx.setTxAmt(this.parse.stringToBigDecimal(titaVo.getTxAmt()));
		try {
			loanBorTxService.insert(tLoanBorTx);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0005", "放款交易內容檔 " + e.getErrorMsg()); // 新增資料時，發生錯誤
		}
	}
}