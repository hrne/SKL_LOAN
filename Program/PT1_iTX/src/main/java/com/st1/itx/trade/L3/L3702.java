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
	public LoanCustRmkService sLoanCustRmkService;

	/* DB服務注入 */
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
		// 功能 1新增 2修改 3複製 4刪除 5查詢
		int iFunCd = parse.stringToInteger(titaVo.getParam("FunCd"));
		// 戶號
		int iCustNo = parse.stringToInteger(titaVo.getParam("CustNo"));
		// 備忘錄序號
		int iRmkNo = parse.stringToInteger(titaVo.getParam("RmkNo"));
		// 會計日期
		int iAcDate = parse.stringToInteger(titaVo.getParam("AcDate"));

		// new table
		CustMain tCustMain = new CustMain();
		LoanCustRmk tLoanCustRmk = new LoanCustRmk();
		// new PK
		LoanCustRmkId LoanCustRmkId = new LoanCustRmkId();
		// 塞值到TablePK
		LoanCustRmkId.setCustNo(iCustNo);
		LoanCustRmkId.setRmkNo(iRmkNo);
		LoanCustRmkId.setAcDate(iAcDate);

		// 新增
		if (iFunCd == 1 || iFunCd == 3) {

			// 測試該戶號是否存在客戶主檔
			tCustMain = sCustMainService.custNoFirst(iCustNo, iCustNo);
			this.info("tCustMain = " + tCustMain);
			// 該戶號部存在客戶主檔 拋錯
			if (tCustMain == null) {
				throw new LogicException(titaVo, "E0005", "該戶號不存在客戶主檔。");
			}

			tLoanCustRmk = sLoanCustRmkService.findById(LoanCustRmkId);
			// 新增時 該戶號,備忘錄序號查有資料 拋錯
			if (tLoanCustRmk != null) {
				throw new LogicException(titaVo, "E0002", "該戶號,備忘錄序號已存在於帳務備忘錄明細資料檔。");
			}

			tLoanCustRmk = new LoanCustRmk();

			tLoanCustRmk.setLoanCustRmkId(LoanCustRmkId);
			tLoanCustRmk.setCustNo(iCustNo);
			tLoanCustRmk.setRmkNo(iRmkNo);
			tLoanCustRmk.setAcDate(iAcDate);
			tLoanCustRmk.setRmkCode(titaVo.getParam("LoanRmkCode"));
			tLoanCustRmk.setRmkDesc(titaVo.getParam("RmkDesc"));
			tLoanCustRmk.setCreateEmpNo(titaVo.getParam("TlrNo"));
			tLoanCustRmk.setLastUpdateEmpNo(titaVo.getParam("TlrNo"));

			/* 存入DB */
			try {
				sLoanCustRmkService.insert(tLoanCustRmk);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005", e.getErrorMsg());
			}
			// FunCd 2修改
		} else if (iFunCd == 2) {

			tLoanCustRmk = new LoanCustRmk();

			tLoanCustRmk = sLoanCustRmkService.holdById(LoanCustRmkId);

			if (tLoanCustRmk == null) {
				throw new LogicException(titaVo, "E0003", "該戶號,備忘錄序號不存在帳務備忘錄明細資料檔。");
			}
			// 變更前
			LoanCustRmk beforeLoanCustRmk = (LoanCustRmk) dataLog.clone(tLoanCustRmk);

			tCustMain = sCustMainService.custNoFirst(iCustNo, iCustNo);

			tLoanCustRmk.setLoanCustRmkId(LoanCustRmkId);
			tLoanCustRmk.setCustNo(iCustNo);
			tLoanCustRmk.setRmkNo(iRmkNo);
			tLoanCustRmk.setAcDate(iAcDate);
			tLoanCustRmk.setRmkCode(titaVo.getParam("LoanRmkCode"));
			tLoanCustRmk.setRmkDesc(titaVo.getParam("RmkDesc"));
			tLoanCustRmk.setLastUpdateEmpNo(titaVo.getParam("TlrNo"));

			// 非建檔者修改須刷主管卡
			if (!tLoanCustRmk.getCreateEmpNo().equals(tLoanCustRmk.getLastUpdateEmpNo()) && titaVo.getEmpNos().trim().isEmpty()) {
				sendRsp.addvReason(this.txBuffer, titaVo, "0004", "非建檔者修改");
			}

			try {
				// 修改
				tLoanCustRmk = sLoanCustRmkService.update2(tLoanCustRmk);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", e.getErrorMsg());
			}

			// 紀錄變更前變更後
			dataLog.setEnv(titaVo, beforeLoanCustRmk, tLoanCustRmk);
			dataLog.exec("修改帳務備忘錄明細資料檔");

			// FunCd 4刪除
		} else if (iFunCd == 4) {

			tLoanCustRmk = new LoanCustRmk();

			tLoanCustRmk = sLoanCustRmkService.holdById(LoanCustRmkId);
			if (tLoanCustRmk == null) {
				throw new LogicException(titaVo, "E0004", "該戶號,備忘錄序號不存在於帳務備忘錄明細資料檔。");
			}

			// 刪除須刷主管卡
			if (titaVo.getEmpNos().trim().isEmpty()) {
				sendRsp.addvReason(this.txBuffer, titaVo, "0004", ""); // 交易需主管核可
			}

			try {

				this.info(" L3702 deleteLoanCustRmkLog" + tLoanCustRmk);

				if (tLoanCustRmk != null) {
					dataLog.setEnv(titaVo, tLoanCustRmk, tLoanCustRmk);
					dataLog.exec("刪除帳務備忘錄明細資料檔");
					sLoanCustRmkService.delete(tLoanCustRmk);
				}
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0008", e.getErrorMsg());
			}
		} else if (iFunCd == 5) {

		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}