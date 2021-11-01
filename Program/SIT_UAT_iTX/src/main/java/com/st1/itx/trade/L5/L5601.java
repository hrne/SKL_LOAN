package com.st1.itx.trade.L5;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.service.CollListService;
import com.st1.itx.db.service.CollRemindService;
import com.st1.itx.db.service.CollTelService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.common.SendRsp;

/* DB容器 */
import com.st1.itx.db.domain.CollTel;
import com.st1.itx.db.domain.CollTelId;
import com.st1.itx.db.domain.CollList;
import com.st1.itx.db.domain.CollListId;
import com.st1.itx.db.domain.CollRemind;
import com.st1.itx.db.domain.CollRemindId;

@Component("L5601")
@Scope("prototype")

/**
 * 電催明細資料登錄
 * 
 * @author Fegie
 * @version 1.0.0
 */

public class L5601 extends TradeBuffer {
	/* 轉型共用工具 */
	@Autowired
	public CollTelService iCollTelService;
	@Autowired
	public CollListService iCollListService;
	@Autowired
	public CollRemindService iCollRemindService;
	@Autowired
	public DataLog iDataLog;
	@Autowired
	SendRsp iSendRsp;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.totaVo.init(titaVo);
		this.info("L5601 start");
		int iCustNo = Integer.valueOf(titaVo.getParam("CustNo"));
		int iFacmNo = Integer.valueOf(titaVo.getParam("FacmNo"));
		String iFunctioncd = titaVo.getParam("FunctionCd");
		String iCaseCode = titaVo.getParam("CaseCode");
		int iResultCode = Integer.valueOf(titaVo.getParam("ResultCode"));

		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 500;

		// 尋找是否有同擔保品
		CollListId iCollListId = new CollListId();
		iCollListId.setCustNo(iCustNo);
		iCollListId.setFacmNo(iFacmNo);
		CollList aCollList = iCollListService.findById(iCollListId, titaVo);
		// 以該戶號額度尋找同擔保品編號、額度
		Slice<CollList> bCollList = iCollListService.findCl(aCollList.getClCustNo(), aCollList.getClFacmNo(), 0, Integer.MAX_VALUE, titaVo);

		// 其實不太必要的檢核，因為必定有一筆以上
		if (bCollList == null) {
			throw new LogicException(titaVo, "E0005", "");
		}

		for (CollList cCollList : bCollList) {
			CollTelId aCollTelId = new CollTelId();
			CollTel aCollTel = new CollTel();
			// 新增、更新電催主檔
			aCollTelId.setCaseCode(iCaseCode);
			aCollTelId.setCustNo(cCollList.getCustNo());
			aCollTelId.setFacmNo(cCollList.getFacmNo());
			if (iFunctioncd.equals("1") || iFunctioncd.equals("3")) {
				aCollTelId.setTitaTlrNo(titaVo.getTlrNo());
				aCollTelId.setTitaTxtNo(titaVo.getTxtNo());
				aCollTelId.setAcDate(Integer.valueOf(titaVo.getCalDy())); // 日曆日 放acdate
				aCollTel.setCreateEmpNo(titaVo.getTlrNo());
			} else {
				aCollTelId.setTitaTlrNo(titaVo.getParam("TitaTlrNo"));
				aCollTelId.setTitaTxtNo(titaVo.getParam("TitaTxtNo"));
				aCollTelId.setAcDate(Integer.valueOf(titaVo.getParam("TitaAcDate")));
				aCollTel.setLastUpdateEmpNo(titaVo.getTlrNo());
			}
			aCollTel.setCollTelId(aCollTelId);
			aCollTel.setTelDate(Integer.valueOf(titaVo.getParam("TelDate")));
			aCollTel.setTelTime(titaVo.getParam("TelTime"));
			aCollTel.setContactCode(titaVo.getParam("ContactCode"));
			aCollTel.setRecvrCode(titaVo.getParam("RecvrCode"));
			aCollTel.setTelArea(titaVo.getParam("TelArea"));
			aCollTel.setTelNo(titaVo.getParam("TelNo"));
			aCollTel.setTelExt(titaVo.getParam("TelExt"));
			aCollTel.setResultCode(titaVo.getParam("ResultCode"));
			aCollTel.setRemark(titaVo.getParam("Remark"));
			aCollTel.setCallDate(Integer.valueOf(titaVo.getParam("CallDate")));

			// 查找同pk以做後續判斷
			CollTel bCollTel = iCollTelService.findById(aCollTelId, titaVo);

			// Functoincd: 1-新增 2-修改 5-查詢
			if (iFunctioncd.equals("1") || iFunctioncd.equals("3")) {
				if (bCollTel == null) {
					try {
						iCollTelService.insert(aCollTel, titaVo);
					} catch (DBException e) {
						throw new LogicException(titaVo, "E0005", e.getErrorMsg());
					}
				} else {
					throw new LogicException(titaVo, "E0002", "");
				}
			} else if (iFunctioncd.equals("2")) {
				if (bCollTel != null) {
					CollTel uCollTel = new CollTel();
					uCollTel = iCollTelService.holdById(aCollTelId);
					CollTel beforeCollTel = (CollTel) iDataLog.clone(uCollTel);
					uCollTel.setTelDate(Integer.valueOf(titaVo.getParam("TelDate")));
					uCollTel.setTelTime(titaVo.getParam("TelTime"));
					uCollTel.setContactCode(titaVo.getParam("ContactCode"));
					uCollTel.setRecvrCode(titaVo.getParam("RecvrCode"));
					uCollTel.setTelArea(titaVo.getParam("TelArea"));
					uCollTel.setTelNo(titaVo.getParam("TelNo"));
					uCollTel.setTelExt(titaVo.getParam("TelExt"));
					uCollTel.setResultCode(titaVo.getParam("ResultCode"));
					uCollTel.setRemark(titaVo.getParam("Remark"));
					uCollTel.setCallDate(Integer.valueOf(titaVo.getParam("CallDate")));
					try {
						iCollTelService.update(uCollTel, titaVo);
						iDataLog.setEnv(titaVo, beforeCollTel, uCollTel);
						iDataLog.exec();
					} catch (DBException e) {
						throw new LogicException(titaVo, "E0007", e.getErrorMsg());
					}
				} else {
					throw new LogicException(titaVo, "E0003", "");
				}
			} else {// 刪除需刷主管卡
				if (bCollTel != null) {
					if (!titaVo.getHsupCode().equals("1")) {
						iSendRsp.addvReason(this.txBuffer, titaVo, "0004", "");
					}
					iCollTelService.holdById(aCollTelId);
					try {
						iCollTelService.delete(aCollTel, titaVo);
					} catch (DBException e) {
						throw new LogicException(titaVo, "E0008", e.getErrorMsg());
					}
				} else {
					throw new LogicException(titaVo, "E0004", "");
				}
			}
			// 更新催收主檔的上次作業項目和日期
			CollListId dCollListId = new CollListId();
			dCollListId.setCustNo(cCollList.getCustNo());
			dCollListId.setFacmNo(cCollList.getFacmNo());

			try {
				CollList fCollList = iCollListService.holdById(dCollListId, titaVo);
				fCollList.setTxCode("3"); // 上次作業項目
				fCollList.setTxDate(Integer.valueOf(titaVo.getCalDy()));
				iCollListService.update(fCollList, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", e.getErrorMsg());
			}

			// 若提醒日期有輸入，則把日期一併insert進提醒登錄表 不論更新或新增都是塞入操作人的員編和時間、交易序號
			// 排除刪除時不新增提醒
			if (!titaVo.getParam("CallDate").equals("0000000") && !iFunctioncd.equals("4")) {
				CollRemind iCollRemind = new CollRemind();
				CollRemindId iCollRemindId = new CollRemindId();
				iCollRemindId.setTitaTlrNo(titaVo.getTlrNo());
				iCollRemindId.setTitaTxtNo(titaVo.getTxtNo());
				iCollRemindId.setAcDate(Integer.valueOf(titaVo.getCalDy()));// 日曆日 放acdate
				iCollRemindId.setCaseCode(titaVo.getParam("CaseCode"));
				iCollRemindId.setFacmNo(Integer.valueOf(cCollList.getFacmNo()));
				iCollRemindId.setCustNo(Integer.valueOf(cCollList.getCustNo()));
				iCollRemind.setCollRemindId(iCollRemindId);
				iCollRemind.setCondCode("1");
				iCollRemind.setRemindDate(Integer.valueOf(titaVo.getParam("CallDate")));
				// iCollRemind.setRemark("登錄自電催會繳");
				if (iResultCode == 1) {
					iCollRemind.setRemark("登錄自電催－會繳");
				} else if (iResultCode == 2) {
					iCollRemind.setRemark("登錄自電催－繳款有困難");
				} else if (iResultCode == 3) {
					iCollRemind.setRemark("登錄自電催－無人接聽");
				} else if (iResultCode == 4) {
					iCollRemind.setRemark("登錄自電催－請接話人轉達");
				} else if (iResultCode == 5) {
					iCollRemind.setRemark("登錄自電催－保證人代繳");
				} else if (iResultCode == 6) {
					iCollRemind.setRemark("登錄自電催－電話留言");
				} else {
					iCollRemind.setRemark("登錄自電催－其他");
				}

				iCollRemind.setEditDate(Integer.valueOf(titaVo.getCalDy()));
				iCollRemind.setEditTime(titaVo.getCalTm().substring(0, 4));
				iCollRemind.setRemindCode("01"); // 到時候看會繳的提醒項目編號是多少
				try {
					iCollRemindService.insert(iCollRemind, titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0005", e.getErrorMsg());
				}
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

}
