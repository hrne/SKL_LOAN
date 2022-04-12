package com.st1.itx.trade.L3;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.LoanCustRmk;
import com.st1.itx.db.domain.LoanCustRmkId;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.LoanCustRmkService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L3702")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L3702 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public LoanCustRmkService loanCustRmkService;
	@Autowired
	public CustMainService sCustMainService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Autowired
	public DataLog dataLog;
	@Autowired
	public SendRsp sendRsp;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L3702 ");
		this.totaVo.init(titaVo);

		// tita
		// 功能 1新增 2修改 4刪除 5查詢
		int iFunCd = parse.stringToInteger(titaVo.getParam("FunCd"));
		// 戶號
		int iCustNo = parse.stringToInteger(titaVo.getParam("CustNo"));
		// 會計日期
		int iAcDate = parse.stringToInteger(titaVo.getParam("AcDate"));
		// 備忘錄序號
		int iRmkNo = parse.stringToInteger(titaVo.getParam("RmkNo"));

		// new table
		CustMain tCustMain = new CustMain();
		LoanCustRmk tLoanCustRmk = new LoanCustRmk();
		// new PK
		LoanCustRmkId loanCustRmkId = new LoanCustRmkId();
		// 塞值到TablePK
		loanCustRmkId.setCustNo(iCustNo);
		loanCustRmkId.setAcDate(iAcDate);
		loanCustRmkId.setRmkNo(iRmkNo);

		// 新增
		if (iFunCd == 1) {
			// 測試該戶號是否存在客戶主檔
			tCustMain = sCustMainService.custNoFirst(iCustNo, iCustNo);
			// 該戶號部存在客戶主檔 拋錯
			if (tCustMain == null) {
				throw new LogicException(titaVo, "E0005", "戶號" + iCustNo + ",不存在客戶主檔。");
			}
//			tLoanCustRmk = loanCustRmkService.findById(loanCustRmkId);
//			// 新增時 該戶號,備忘錄序號查有資料 拋錯
//			if (tLoanCustRmk != null) {
//				throw new LogicException(titaVo, "E0002", "該戶號,備忘錄序號" + iCustNo + iRmkNo + "已存在於帳務備忘錄明細檔。");
//			}
			
			LoanCustRmk loanCustRmk = loanCustRmkService.maxRmkNoFirst(iCustNo, iAcDate + 19110000, titaVo);
			if (loanCustRmk == null) {
				iRmkNo = 1;
			} else {
				iRmkNo = loanCustRmk.getRmkNo() + 1;
			}
			
			String mrkey = String.format("%07d-%07d-%03d", iCustNo,iAcDate,iRmkNo);
			titaVo.put("MRKEY", mrkey);
			
			loanCustRmkId.setRmkNo(iRmkNo);

			tLoanCustRmk = new LoanCustRmk();

			tLoanCustRmk.setLoanCustRmkId(loanCustRmkId);
			tLoanCustRmk.setCustNo(iCustNo);
			tLoanCustRmk.setAcDate(iAcDate);
			tLoanCustRmk.setRmkNo(iRmkNo);
			tLoanCustRmk.setCustUKey(tCustMain.getCustUKey());
//			tLoanCustRmk.setRmkCode(titaVo.getParam("RmkCode"));
			tLoanCustRmk.setRmkDesc(titaVo.getParam("RmkDesc"));
//			tLoanCustRmk.setCreateEmpNo(titaVo.getParam("TlrNo"));
//			tLoanCustRmk.setLastUpdateEmpNo(titaVo.getParam("TlrNo"));

			// 新增須刷主管卡 經理層級
//			if (titaVo.getEmpNos().trim().isEmpty()) {
//				sendRsp.addvReason(this.txBuffer, titaVo, "0004", "");
//			}

			/* 存入DB */
			try {
				tLoanCustRmk = loanCustRmkService.insert(tLoanCustRmk, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005", e.getErrorMsg());
			}
			// FunCd 2修改
		} else if (iFunCd == 2) {

			tLoanCustRmk = new LoanCustRmk();
			// PK找帳務備忘錄明細檔HOLD資料
			tLoanCustRmk = loanCustRmkService.holdById(loanCustRmkId);

			if (tLoanCustRmk == null) {
				throw new LogicException(titaVo, "E0003", "該戶號,備忘錄序號" + iCustNo + iRmkNo + "不存在於帳務備忘錄明細檔。");
			}
			// 變更前
			LoanCustRmk beforeLoanCustRmk = (LoanCustRmk) dataLog.clone(tLoanCustRmk);

			tCustMain = sCustMainService.custNoFirst(iCustNo, iCustNo);

			tLoanCustRmk.setRmkDesc(titaVo.getParam("RmkDesc"));

			// 非建檔者修改須刷主管卡
			if (!titaVo.getHsupCode().equals("1") && !tLoanCustRmk.getCreateEmpNo().equals(titaVo.getTlrNo())) {
				sendRsp.addvReason(this.txBuffer, titaVo, "0004", "非建檔者修改");
			}

			try {
				// 修改
				tLoanCustRmk = loanCustRmkService.update2(tLoanCustRmk, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", e.getErrorMsg());
			}

			// 紀錄變更前變更後
			dataLog.setEnv(titaVo, beforeLoanCustRmk, tLoanCustRmk);
			dataLog.exec("修改帳務備忘錄");

			// FunCd 4刪除
		} else if (iFunCd == 4) {

			tLoanCustRmk = new LoanCustRmk();

			// PK找帳務備忘錄明細檔HOLD資料
			tLoanCustRmk = loanCustRmkService.holdById(loanCustRmkId);
			if (tLoanCustRmk == null) {
				throw new LogicException(titaVo, "E0004", "L3702 該戶號,備忘錄序號" + iCustNo + iRmkNo + "不存在於帳務備忘錄明細檔。");
			}

			// 刪除須刷主管卡
			if (!titaVo.getHsupCode().equals("1")) {
				sendRsp.addvReason(this.txBuffer, titaVo, "0004", "刪除帳務備忘錄");
			}

			try {

				this.info(" deleteLoanCustRmkLog" + tLoanCustRmk);

				if (tLoanCustRmk != null) {
					dataLog.setEnv(titaVo, tLoanCustRmk, tLoanCustRmk);
					dataLog.exec("刪除帳務備忘錄明細檔");
					loanCustRmkService.delete(tLoanCustRmk, titaVo);
				}
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0008", e.getErrorMsg());
			}
		} 
		
		this.totaVo.putParam("RmkNo", tLoanCustRmk.getRmkNo());

		this.addList(this.totaVo);
		return this.sendList();
	}
}