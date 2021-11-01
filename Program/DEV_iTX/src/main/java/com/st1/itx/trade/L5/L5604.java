package com.st1.itx.trade.L5;

import java.math.BigDecimal;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;

import com.st1.itx.db.service.CollLawService;
import com.st1.itx.db.service.CollListService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.common.SendRsp;
/* DB容器 */
import com.st1.itx.db.domain.CollLaw;
import com.st1.itx.db.domain.CollLawId;
import com.st1.itx.db.domain.CollList;
import com.st1.itx.db.domain.CollListId;

@Component("L5604")
@Scope("prototype")

/**
 * 法催登錄
 * 
 * @author Fegie
 * @version 1.0.0
 */

public class L5604 extends TradeBuffer {
	@Autowired
	public CollLawService iCollLawService;
	@Autowired
	public CollListService iCollListService;
	@Autowired
	public DataLog iDataLog;
	@Autowired
	SendRsp iSendRsp;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.totaVo.init(titaVo);
		this.info("L5604 start");

		String iFunctionCd = titaVo.getParam("FunctionCd");
		String iCaseCode = titaVo.getParam("CaseCode");
		int iClCode1 = Integer.valueOf(titaVo.getParam("ClCode1"));
		int iClCode2 = Integer.valueOf(titaVo.getParam("ClCode2"));
		int iClNo = Integer.valueOf(titaVo.getParam("ClNo"));
		int iCustNo = Integer.valueOf(titaVo.getParam("CustNo"));
		int iFacmNo = Integer.valueOf(titaVo.getParam("FacmNo"));
		int iRecordDate = Integer.valueOf(titaVo.getParam("RecordDate"));
		String iLegalProg = titaVo.getParam("LegalProg");
		BigDecimal iAmount = new BigDecimal(titaVo.getParam("Amount"));
		String iRemark = titaVo.getParam("Remark");
		String iMemo = titaVo.getParam("Memo");

		CollLaw iCollLaw = new CollLaw();
		CollLawId iCollLawId = new CollLawId();
		switch (iFunctionCd) {
		case "1":
			int iAcDate = Integer.valueOf(titaVo.getCalDy());
			String iTitaTlrNo = titaVo.getTlrNo();
			String iTitaTxtNo = titaVo.getTxtNo();
			iCollLawId.setAcDate(iAcDate);
			iCollLawId.setCaseCode(iCaseCode);
			iCollLawId.setCustNo(iCustNo);
			iCollLawId.setFacmNo(iFacmNo);
			iCollLawId.setTitaTlrNo(iTitaTlrNo);
			iCollLawId.setTitaTxtNo(iTitaTxtNo);
			iCollLaw.setCollLawId(iCollLawId);
			iCollLaw.setClCode1(iClCode1);
			iCollLaw.setClCode2(iClCode2);
			iCollLaw.setClNo(iClNo);
			iCollLaw.setRecordDate(iRecordDate);
			iCollLaw.setLegalProg(iLegalProg);
			iCollLaw.setAmount(iAmount);
			iCollLaw.setRemark(iRemark);
			iCollLaw.setMemo(iMemo);
			try {
				iCollLawService.insert(iCollLaw, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005", e.getErrorMsg());
			}
			break;
		case "2":
			int uAcDate = Integer.valueOf(titaVo.getParam("TitaAcDate"));
			String uTitaTlrNo = titaVo.getParam("TitaTlrNo");
			String uTitaTxtNo = titaVo.getParam("TitaTxtNo");
			iCollLawId.setAcDate(uAcDate);
			iCollLawId.setCaseCode(iCaseCode);
			iCollLawId.setCustNo(iCustNo);
			iCollLawId.setFacmNo(iFacmNo);
			iCollLawId.setTitaTlrNo(uTitaTlrNo);
			iCollLawId.setTitaTxtNo(uTitaTxtNo);
			CollLaw uCollLaw = new CollLaw();
			uCollLaw = iCollLawService.holdById(iCollLawId, titaVo);
			if (uCollLaw == null) {
				throw new LogicException(titaVo, "E0003", "查無此資料");
			}
			CollLaw beforeCollLaw = (CollLaw) iDataLog.clone(uCollLaw);
			uCollLaw.setRecordDate(iRecordDate);
			uCollLaw.setLegalProg(iLegalProg);
			uCollLaw.setAmount(iAmount);
			uCollLaw.setRemark(iRemark);
			uCollLaw.setMemo(iMemo);
			try {
				iCollLawService.update(uCollLaw, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", e.getErrorMsg());
			}
			iDataLog.setEnv(titaVo, beforeCollLaw, uCollLaw);
			iDataLog.exec();
			break;
		case "3":
			int cAcDate = Integer.valueOf(titaVo.getCalDy());
			String cTitaTlrNo = titaVo.getTlrNo();
			String cTitaTxtNo = titaVo.getTxtNo();
			iCollLawId.setAcDate(cAcDate);
			iCollLawId.setCaseCode(iCaseCode);
			iCollLawId.setCustNo(iCustNo);
			iCollLawId.setFacmNo(iFacmNo);
			iCollLawId.setTitaTlrNo(cTitaTlrNo);
			iCollLawId.setTitaTxtNo(cTitaTxtNo);
			iCollLaw.setCollLawId(iCollLawId);
			iCollLaw.setClCode1(iClCode1);
			iCollLaw.setClCode2(iClCode2);
			iCollLaw.setClNo(iClNo);
			iCollLaw.setRecordDate(iRecordDate);
			iCollLaw.setLegalProg(iLegalProg);
			iCollLaw.setAmount(iAmount);
			iCollLaw.setRemark(iRemark);
			iCollLaw.setMemo(iMemo);
			try {
				iCollLawService.insert(iCollLaw, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005", e.getErrorMsg());
			}
			break;
		case "4":
			int dAcDate = Integer.valueOf(titaVo.getParam("TitaAcDate"));
			String dTitaTlrNo = titaVo.getParam("TitaTlrNo");
			String dTitaTxtNo = titaVo.getParam("TitaTxtNo");
			iCollLawId.setAcDate(dAcDate);
			iCollLawId.setCaseCode(iCaseCode);
			iCollLawId.setCustNo(iCustNo);
			iCollLawId.setFacmNo(iFacmNo);
			iCollLawId.setTitaTlrNo(dTitaTlrNo);
			iCollLawId.setTitaTxtNo(dTitaTxtNo);
			CollLaw dCollLaw = iCollLawService.holdById(iCollLawId, titaVo);
			if (dCollLaw == null) {
				throw new LogicException(titaVo, "E0004", "");
			}
			// 刪除需刷主管卡
			if (!titaVo.getHsupCode().equals("1")) {
				iSendRsp.addvReason(this.txBuffer, titaVo, "0004", "");
			}
			try {
				iCollLawService.delete(dCollLaw, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0008", e.getErrorMsg());
			}
			break;
		}

		// 更新催收主檔
		CollListId iCollListId = new CollListId();
		CollList iCollList = new CollList();
		iCollListId.setCustNo(iCustNo);
		iCollListId.setFacmNo(iFacmNo);
		iCollList = iCollListService.holdById(iCollListId, titaVo);
		if (iCollList == null) {
			throw new LogicException(titaVo, "E0007", "催收主檔"); // 更新資料時發生錯誤
		}
		iCollList.setTxCode("5");
		iCollList.setTxDate(Integer.valueOf(titaVo.getCalDy()));
		try {
			iCollListService.update(iCollList, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007", e.getErrorMsg()); // 更新資料時發生錯誤
		}
		this.addList(this.totaVo);
		return this.sendList();
	}

}
