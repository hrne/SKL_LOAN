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

import com.st1.itx.db.service.CollLetterService;
import com.st1.itx.db.service.CollListService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.common.SendRsp;
/* DB容器 */
import com.st1.itx.db.domain.CollLetter;
import com.st1.itx.db.domain.CollLetterId;
import com.st1.itx.db.domain.CollList;
import com.st1.itx.db.domain.CollListId;

@Component("L5603")
@Scope("prototype")

/**
 * 函催登錄
 * 
 * @author Fegie
 * @version 1.0.0
 */

public class L5603 extends TradeBuffer {
	/* 轉型共用工具 */
	@Autowired
	public CollLetterService iCollLetterService;
	@Autowired
	public CollListService iCollListService;
	@Autowired
	public DataLog iDataLog;
	@Autowired
	SendRsp iSendRsp;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.totaVo.init(titaVo);
		this.info("L5603 start");
		int iCustNo = Integer.valueOf(titaVo.getParam("CustNo"));
		int iFacmNo = Integer.valueOf(titaVo.getParam("FacmNo"));
		String iFunctionCd = titaVo.getParam("FunctionCd");
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 500;

		// 進主檔找是否有這筆資料
		CollListId iCollListid = new CollListId();
		iCollListid.setCustNo(iCustNo);
		iCollListid.setFacmNo(iFacmNo);
		CollList iCollList = iCollListService.findById(iCollListid, titaVo);
		if (iCollList == null) {
			throw new LogicException(titaVo, "E0002", "");
		}
		// 從找到的資料挖出擔保品戶號、額度
		int iClCustNo = iCollList.getClCustNo();
		int iClFacmNo = iCollList.getClFacmNo();
		// 用撈出的擔保品編號找全部相同擔保品的資料
		Slice<CollList> allCollList = iCollListService.findCl(iClCustNo, iClFacmNo, 0, Integer.MAX_VALUE, titaVo);
		if (allCollList == null) {
			throw new LogicException(titaVo, "E0002", "");
		}
		// 處理找出的資料(包含函催檔登錄和主檔更新)
		for (CollList iCollListVo : allCollList) {
			CollLetterId iCollLetterId = new CollLetterId();
			CollLetter iCollLetter = new CollLetter();
			iCollLetterId.setCaseCode(titaVo.getParam("CaseCode"));
			iCollLetterId.setCustNo(iCollListVo.getCustNo());
			iCollLetterId.setFacmNo(iCollListVo.getFacmNo());
			if (iFunctionCd.equals("1") || iFunctionCd.equals("3")) {
				iCollLetterId.setTitaTlrNo(titaVo.getTlrNo());
				iCollLetterId.setTitaTxtNo(titaVo.getTxtNo());
				iCollLetterId.setAcDate(Integer.valueOf(titaVo.getCalDy()));// 日曆日 放acdate
			} else {
				iCollLetterId.setTitaTlrNo(titaVo.getParam("TitaTlrNo"));
				iCollLetterId.setTitaTxtNo(titaVo.getParam("TitaTxtNo"));
				iCollLetterId.setAcDate(Integer.valueOf(titaVo.getParam("TitaAcDate")));
			}
			iCollLetter.setCollLetterId(iCollLetterId);
			iCollLetter.setMailTypeCode(titaVo.getParam("MailTypeCode"));
			iCollLetter.setMailDate(Integer.valueOf(titaVo.getParam("MailDate")));
			iCollLetter.setMailObj(titaVo.getParam("MailObj"));
			iCollLetter.setCustName(titaVo.getParam("MailCustName"));
			iCollLetter.setDelvrYet(titaVo.getParam("DelvrYet"));
			iCollLetter.setDelvrCode(titaVo.getParam("DelvrCode"));
			iCollLetter.setAddressCode(Integer.valueOf(titaVo.getParam("AddressCode")));
			iCollLetter.setAddress(titaVo.getParam("Address"));
			iCollLetter.setRemark(titaVo.getParam("Remark"));
			CollLetter tCollLetter = iCollLetterService.findById(iCollLetterId, titaVo);
			if (iFunctionCd.equals("1") || iFunctionCd.equals("3")) {
				if (tCollLetter == null) {
					try {
						iCollLetterService.insert(iCollLetter, titaVo);
					} catch (DBException e) {
						throw new LogicException(titaVo, "E0005", e.getErrorMsg());
					}
				} else {
					throw new LogicException(titaVo, "E0002", "");
				}
			} else if (iFunctionCd.equals("2")) {
				if (tCollLetter != null) {
					CollLetter uCollLetter = new CollLetter();
					uCollLetter = iCollLetterService.holdById(iCollLetterId, titaVo);
					CollLetter beforeCollLetter = (CollLetter) iDataLog.clone(uCollLetter);
					uCollLetter.setMailTypeCode(titaVo.getParam("MailTypeCode"));
					uCollLetter.setMailDate(Integer.valueOf(titaVo.getParam("MailDate")));
					uCollLetter.setMailObj(titaVo.getParam("MailObj"));
					uCollLetter.setCustName(titaVo.getParam("MailCustName"));
					uCollLetter.setDelvrYet(titaVo.getParam("DelvrYet"));
					uCollLetter.setDelvrCode(titaVo.getParam("DelvrCode"));
					uCollLetter.setAddressCode(Integer.valueOf(titaVo.getParam("AddressCode")));
					uCollLetter.setAddress(titaVo.getParam("Address"));
					uCollLetter.setRemark(titaVo.getParam("Remark"));
					try {
						iCollLetterService.update(uCollLetter, titaVo);
						iDataLog.setEnv(titaVo, beforeCollLetter, uCollLetter);
						iDataLog.exec("修改法催紀錄函催檔");
					} catch (DBException e) {
						throw new LogicException(titaVo, "E0007", e.getErrorMsg());
					}
				} else {
					throw new LogicException(titaVo, "E0003", "");
				}
			} else {// 刪除需刷主管卡
				if (tCollLetter != null) {
					if (!titaVo.getHsupCode().equals("1")) {
						iSendRsp.addvReason(this.txBuffer, titaVo, "0004", "");
					}
					CollLetter uCollLetter = new CollLetter();
					uCollLetter = iCollLetterService.holdById(iCollLetterId, titaVo);
					if (uCollLetter != null) {
						try {
							iCollLetterService.delete(iCollLetter, titaVo);
						} catch (DBException e) {
							throw new LogicException(titaVo, "E0008", e.getErrorMsg());
						}
						iDataLog.setEnv(titaVo, uCollLetter, uCollLetter);
						iDataLog.exec("刪除法催紀錄函催檔");
					}
				} else {
					throw new LogicException(titaVo, "E0004", "");
				}
			}
			// 更新list的上次作業日期和項目
			CollListId cCollListId = new CollListId();
			cCollListId.setCustNo(iCollListVo.getCustNo());
			cCollListId.setFacmNo(iCollListVo.getFacmNo());
			CollList neCollList = iCollListService.findById(cCollListId, titaVo);
			if (neCollList != null) {
				try {
					CollList upCollList = iCollListService.holdById(cCollListId, titaVo);
					upCollList.setTxCode("2"); // 上次作業項目
					upCollList.setTxDate(Integer.valueOf(titaVo.getCalDy()));
					iCollListService.update(upCollList, titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0005", e.getErrorMsg());
				}
			} else {
				throw new LogicException(titaVo, "E0003", "");
			}
		}
		this.addList(this.totaVo);
		return this.sendList();
	}

}
