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
import com.st1.itx.db.domain.LoanFacTmp;
import com.st1.itx.db.domain.LoanFacTmpId;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.LoanFacTmpService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L3704")
@Scope("prototype")
/**
 * 
 * 
 * @author Mata
 * @version 1.0.0
 */
public class L3704 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public LoanFacTmpService sLoanFacTmpService;

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
		this.info("active L3704 ");
		this.totaVo.init(titaVo);

		// tita
		// 功能 1新增 2修改 4刪除
		int iFunCd = parse.stringToInteger(titaVo.getParam("FunCd"));
		// 戶號
		int iCustNo = parse.stringToInteger(titaVo.getParam("CustNo"));
		// 額度編號
		int iFacmNo = parse.stringToInteger(titaVo.getParam("FacmNo"));
		// 設定理由
		String iRmkDescribe = titaVo.getParam("RmkDescribe");

		this.info("FunCd    = " + iFunCd);
		this.info("CustNo   = " + iCustNo);
		this.info("FacmNo   = " + iFacmNo);
		this.info("RmkDescribe = " + iRmkDescribe);

		// new table
		CustMain tCustMain = new CustMain();
		LoanFacTmp tLoanFacTmp = new LoanFacTmp();
		// new PK
		LoanFacTmpId LoanFacTmpId = new LoanFacTmpId();
		// 塞值到TablePK
		LoanFacTmpId.setCustNo(iCustNo);
		LoanFacTmpId.setFacmNo(iFacmNo);
		this.info("iFunCd    = " + iFunCd);
		this.info("titaVo = " + titaVo);
		// 新增
		if (iFunCd == 1) {

			// 測試該戶號是否存在客戶主檔
			tCustMain = sCustMainService.custNoFirst(iCustNo, iCustNo);
			this.info("tCustMain = " + tCustMain);
			// 該戶號不存在客戶主檔 拋錯
			if (tCustMain == null) {
				throw new LogicException(titaVo, "E0005", "該戶號不存在客戶主檔。");
			}

			tLoanFacTmp = sLoanFacTmpService.findById(LoanFacTmpId, titaVo);
			if (tLoanFacTmp != null) {
				if (tLoanFacTmp.getCustNo() == iCustNo && tLoanFacTmp.getFacmNo() == iFacmNo) {
					throw new LogicException(titaVo, "E0002","該戶號、與額度編號已存在於暫收款指定額度設定查詢檔。");
				}
			}

			tLoanFacTmp = new LoanFacTmp();
			tLoanFacTmp.setLoanFacTmpId(LoanFacTmpId);
			tLoanFacTmp.setCustNo(iCustNo);
			tLoanFacTmp.setFacmNo(iFacmNo);
			tLoanFacTmp.setDescribe(iRmkDescribe);

			this.info("tLoanFacTmp    = " + tLoanFacTmp);
			this.info("titaVo   = " + titaVo);
			/* 存入DB */
			try {
				sLoanFacTmpService.insert(tLoanFacTmp, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005", e.getErrorMsg());
			}
			// FunCd 2修改
		} else if (iFunCd == 2) {

			tLoanFacTmp = new LoanFacTmp();

			tLoanFacTmp = sLoanFacTmpService.holdById(LoanFacTmpId);

			if (tLoanFacTmp == null) {
				throw new LogicException(titaVo, "E0003",
						"該戶號、與額度編號,不存在暫收款指定額度設定查詢檔。");
			}

			// 變更前
			LoanFacTmp beforeLoanFacTmp = (LoanFacTmp) dataLog.clone(tLoanFacTmp);

			tCustMain = sCustMainService.custNoFirst(iCustNo, iCustNo);
			tLoanFacTmp.setLoanFacTmpId(LoanFacTmpId);
			tLoanFacTmp.setCustNo(iCustNo);
			tLoanFacTmp.setFacmNo(iFacmNo);
			tLoanFacTmp.setDescribe(iRmkDescribe);

			// 非建檔者修改須刷主管卡
			if (!tLoanFacTmp.getCreateEmpNo().equals(tLoanFacTmp.getLastUpdateEmpNo())
					&& titaVo.getEmpNos().trim().isEmpty()) {
				sendRsp.addvReason(this.txBuffer, titaVo, "0004", "非建檔者修改");
			}

			try {
				// 修改
				tLoanFacTmp = sLoanFacTmpService.update2(tLoanFacTmp);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", e.getErrorMsg());
			}

			// 紀錄變更前變更後
			dataLog.setEnv(titaVo, beforeLoanFacTmp, tLoanFacTmp);
			dataLog.exec("修改暫收款指定額度設定查詢檔");

			// FunCd 4刪除
		} else if (iFunCd == 4) {

			tLoanFacTmp = new LoanFacTmp();

			tLoanFacTmp = sLoanFacTmpService.holdById(LoanFacTmpId);

			// 刪除須刷主管卡
			if (titaVo.getEmpNos().trim().isEmpty()) {
				sendRsp.addvReason(this.txBuffer, titaVo, "0004", ""); // 交易需主管核可
			}

			try {

				this.info(" L3704 deleteLoanCustRmkLog" + tLoanFacTmp);

				if (tLoanFacTmp != null) {
					dataLog.setEnv(titaVo, tLoanFacTmp, tLoanFacTmp);
					dataLog.exec("刪除暫收款指定額度設定查詢檔");
					sLoanFacTmpService.delete(tLoanFacTmp);
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