package com.st1.itx.trade.L8;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.db.domain.TxAmlLog;
import com.st1.itx.db.service.TxAmlLogService;

import com.st1.itx.util.common.data.CheckAmlVo;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.common.CheckAml;

@Service("L8100")
@Scope("prototype")
/**
 * 
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public class L8100 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public TxAmlLogService txAmlLogService;

	@Autowired
	public CheckAml checkAml;
	@Autowired
	public DataLog iDataLog;
	
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8100 ");
		this.totaVo.init(titaVo);
		checkAml.setTxBuffer(this.txBuffer);
		String iFunCode = titaVo.getParam("FunCode");
		Long iLogNo = Long.valueOf(titaVo.get("LogNo").trim());

		CheckAmlVo checkAmlVo = new CheckAmlVo();
		//1.姓名檢核 2.更新狀態 3.人工確認 5.查詢
		if ("1".equals(iFunCode)) {

			String refno = (titaVo.getEntDyI() + 19110000) + titaVo.getKinbr() + titaVo.getTlrNo() + titaVo.getTxtNo();

			if ("".equals(titaVo.get("TransactionId2").trim())) {
				titaVo.put("TransactionId2", refno);
			}
			checkAmlVo.setEntdy(titaVo.getEntDyI());
			checkAmlVo.setBrNo(titaVo.getKinbr());
			checkAmlVo.setRefNo(refno);
			checkAmlVo.setTransactionId(titaVo.get("TransactionId1").trim() + titaVo.get("TransactionId2").trim());
			checkAmlVo.setUnit(titaVo.get("Unit").trim());
			checkAmlVo.setAcceptanceUnit(titaVo.get("AcceptanceUnit").trim());
			checkAmlVo.setRoleId(titaVo.get("RoleId").trim());
			checkAmlVo.setAcctNo(titaVo.get("AcctNo").trim());
			checkAmlVo.setCaseNo(titaVo.get("CaseNo").trim());
			checkAmlVo.setName(titaVo.get("Name").trim());
			checkAmlVo.setEnglishName(titaVo.get("EnglishName").trim());
			checkAmlVo.setCustKey(titaVo.get("CustKey").trim());
			checkAmlVo.setIdentityCd(titaVo.get("IdentityCd").trim());
			checkAmlVo.setNationalCd(titaVo.get("NationalCd").trim());
			checkAmlVo.setBirthNationCd(titaVo.get("BirthNationCd").trim());
			checkAmlVo.setSex(titaVo.get("Sex").trim());
			checkAmlVo.setBirthEstDt(titaVo.get("BirthEstDt").trim());
			checkAmlVo.setNotifyEmail(titaVo.get("NotifyEmail").trim());
			checkAmlVo.setQueryId(titaVo.get("QueryId").trim());

			checkAmlVo = checkAml.checkName(checkAmlVo, titaVo);
		} else if ("2".equals(iFunCode)) {

			checkAmlVo = checkAml.refreshStatus(iLogNo, titaVo);

		} else if ("3".equals(iFunCode)) {
			
			if (!checkAml.isManualConFirm(iLogNo, titaVo)) {
				throw new LogicException("E0010", "AML系統連線正常，請至AML系統確認"); // 功能選擇錯誤
			}
			String iConfirmCode = titaVo.getParam("ConfirmCode");

			TxAmlLog tTxAmlLog = txAmlLogService.holdById(iLogNo);
			
			if (tTxAmlLog == null) {
				throw new LogicException("EC001", "TxAmlLog.LogNo:" + iLogNo);
			}
			TxAmlLog oldtxAmlLog = (TxAmlLog)iDataLog.clone(tTxAmlLog);
			tTxAmlLog.setConfirmCode(iConfirmCode);
			tTxAmlLog.setConfirmEmpNo(titaVo.get("ConfirmEmpNo"));
			checkAmlVo.setLogNo(tTxAmlLog.getLogNo());
			checkAmlVo.setStatus(tTxAmlLog.getStatus());
			checkAmlVo.setStatusCode(tTxAmlLog.getStatusCode());
			checkAmlVo.setStatusDesc(tTxAmlLog.getStatusDesc());
			checkAmlVo.setIsSimilar(tTxAmlLog.getIsSimilar());
			checkAmlVo.setIsSan(tTxAmlLog.getIsSan());
			checkAmlVo.setIsBanNation(tTxAmlLog.getIsBanNation());

			checkAmlVo.setConfirmStatus(tTxAmlLog.getConfirmStatus());
			checkAmlVo.setConfirmCode(tTxAmlLog.getConfirmCode());
			checkAmlVo.setConfirmEmpNo(tTxAmlLog.getConfirmEmpNo());
			checkAmlVo.setConfirmTranCode(tTxAmlLog.getConfirmTranCode());
			try {
				txAmlLogService.update(tTxAmlLog);
			} catch (DBException e) {
				throw new LogicException("EC003", "update TxAmlLog:" + e.getErrorMsg()); // 更新資料
			}
			iDataLog.setEnv(titaVo, oldtxAmlLog, tTxAmlLog);
			iDataLog.exec();
			
		} else if ("5".equals(iFunCode)) {
			checkAmlVo = checkAml.inquiryStatus(iLogNo, titaVo);
		}

		this.totaVo.putParam("LogNo", checkAmlVo.getLogNo());
		this.totaVo.putParam("Status", checkAmlVo.getStatus());
		this.totaVo.putParam("StatusCode", checkAmlVo.getStatusCode());
		this.totaVo.putParam("StatusDesc", checkAmlVo.getStatusDesc());
		this.totaVo.putParam("IsSimilar", checkAmlVo.getIsSimilar());
		this.totaVo.putParam("IsSan", checkAmlVo.getIsSan());
		this.totaVo.putParam("IsBanNation", checkAmlVo.getIsBanNation());

		this.totaVo.putParam("ConfirmStatus", checkAmlVo.getConfirmStatus());
		this.totaVo.putParam("ConfirmCode", checkAmlVo.getConfirmCode());
		this.totaVo.putParam("ConfirmEmpNo", checkAmlVo.getConfirmEmpNo());
		this.totaVo.putParam("ConfirmTranCode", checkAmlVo.getConfirmTranCode());

		this.addList(this.totaVo);
		return this.sendList();
	}
}