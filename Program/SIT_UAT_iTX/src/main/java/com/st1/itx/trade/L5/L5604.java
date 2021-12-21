package com.st1.itx.trade.L5;

import java.math.BigDecimal;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.service.ClFacService;
import com.st1.itx.db.service.CollLawService;
import com.st1.itx.db.service.CollListService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.db.domain.ClFac;
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
	public ClFacService iClFacService;
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
		CollListId iCollListId = new CollListId();
		CollList iCollList = new CollList();

		Slice<ClFac> cClFac = null;
		cClFac = iClFacService.selectForL5064(iClCode1, iClCode2, iClNo, iCustNo, iFacmNo, 0, Integer.MAX_VALUE, titaVo);
		if (cClFac == null) {
			throw new LogicException(titaVo, "E0005", "擔保品與額度關聯檔查無資料"); // 更新資料時發生錯誤
		}

		Slice<ClFac> iClFac = null;
		iClFac = iClFacService.clNoEq(iClCode1, iClCode2, iClNo, 0, Integer.MAX_VALUE, titaVo);
		if (iClFac == null) {
			throw new LogicException(titaVo, "E0005", "擔保品與額度關聯檔查無資料"); // 更新資料時發生錯誤
		}
		for (ClFac rClFac : iClFac) {
			iCollListId.setCustNo(rClFac.getCustNo());
			iCollListId.setFacmNo(rClFac.getFacmNo());
			iCollList = iCollListService.findById(iCollListId, titaVo);
			if (iCollList == null) {
				continue; // 催收主檔若無資料則跳過
			}
			if (iFunctionCd.equals("1") || iFunctionCd.equals("3")) { // 新增
				iCollLawId.setCustNo(rClFac.getCustNo());
				iCollLawId.setFacmNo(rClFac.getFacmNo());
				iCollLawId.setAcDate(Integer.valueOf(titaVo.getCalDy()));
				iCollLawId.setCaseCode(iCaseCode);
				iCollLawId.setTitaTlrNo(titaVo.getTlrNo());
				iCollLawId.setTitaTxtNo(titaVo.getTxtNo());
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
			} else if (iFunctionCd.equals("2")) { // 修改
				int uAcDate = Integer.valueOf(titaVo.getParam("TitaAcDate"));
				String uTitaTlrNo = titaVo.getParam("TitaTlrNo");
				String uTitaTxtNo = titaVo.getParam("TitaTxtNo");
				iCollLawId.setAcDate(uAcDate);
				iCollLawId.setCaseCode(iCaseCode);
				iCollLawId.setCustNo(rClFac.getCustNo());
				iCollLawId.setFacmNo(rClFac.getFacmNo());
				iCollLawId.setTitaTlrNo(uTitaTlrNo);
				iCollLawId.setTitaTxtNo(uTitaTxtNo);
				CollLaw uCollLaw = new CollLaw();
				uCollLaw = iCollLawService.holdById(iCollLawId, titaVo);
				if (uCollLaw == null) {
//					throw new LogicException(titaVo, "E0003", "查無此資料"); 為避免資料不同步問題，只使用continue跳過
					continue;
				}
				CollLaw beforeCollLaw = (CollLaw) iDataLog.clone(uCollLaw);
				CollLaw uuCollLaw = new CollLaw();
				uCollLaw.setRecordDate(iRecordDate);
				uCollLaw.setLegalProg(iLegalProg);
				uCollLaw.setAmount(iAmount);
				uCollLaw.setRemark(iRemark);
				uCollLaw.setMemo(iMemo);
				try {
					uuCollLaw = iCollLawService.update(uCollLaw, titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", e.getErrorMsg());
				}
				iDataLog.setEnv(titaVo, beforeCollLaw, uuCollLaw);
				iDataLog.exec("修改法催紀錄法務進度檔");

			} else { // 刪除
				int dAcDate = Integer.valueOf(titaVo.getParam("TitaAcDate"));
				String dTitaTlrNo = titaVo.getParam("TitaTlrNo");
				String dTitaTxtNo = titaVo.getParam("TitaTxtNo");
				iCollLawId.setAcDate(dAcDate);
				iCollLawId.setCaseCode(iCaseCode);
				iCollLawId.setCustNo(rClFac.getCustNo());
				iCollLawId.setFacmNo(rClFac.getFacmNo());
				iCollLawId.setTitaTlrNo(dTitaTlrNo);
				iCollLawId.setTitaTxtNo(dTitaTxtNo);
				CollLaw dCollLaw = iCollLawService.holdById(iCollLawId, titaVo);
				if (dCollLaw == null) {
//					throw new LogicException(titaVo, "E0004", ""); 為避免資料不同步問題，只使用continue跳過
					continue;
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
				iDataLog.setEnv(titaVo, dCollLaw, dCollLaw);
				iDataLog.exec("刪除法催紀錄法務進度檔");

			}
			CollListId rCollListId = new CollListId();
			CollList rCollList = new CollList();
			rCollListId.setCustNo(rClFac.getCustNo());
			rCollListId.setFacmNo(rClFac.getFacmNo());
			rCollList = iCollListService.holdById(iCollListId, titaVo);
			if (rCollList == null) {
				throw new LogicException(titaVo, "E0007", "催收主檔"); // 更新資料時發生錯誤
			}
			rCollList.setTxCode("5");
			rCollList.setTxDate(Integer.valueOf(titaVo.getCalDy()));
			try {
				iCollListService.update(rCollList, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", e.getErrorMsg()); // 更新資料時發生錯誤
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

}
