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
import com.st1.itx.util.parse.Parse;

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
	public Parse parse;

	@Autowired
	public CollLetterService iCollLetterService;
	@Autowired
	public CollListService iCollListService;

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
			throw new LogicException(titaVo, "E0005", "");
		}
		// 從找到的資料挖出擔保品戶號、額度
		int iClCustNo = iCollList.getClCustNo();
		int iClFacmNo = iCollList.getClFacmNo();
		// 用撈出的擔保品編號找全部相同擔保品的資料
		Slice<CollList> allCollList = iCollListService.findCl(iClCustNo, iClFacmNo, 0,Integer.MAX_VALUE, titaVo);
		if (allCollList == null) {
			throw new LogicException(titaVo, "E0005", "");
		}
		// 處理找出的資料(包含函催檔登錄和主檔更新)
		for (CollList iCollListVo : allCollList) {
			CollLetterId iCollLetterId = new CollLetterId();
			CollLetter iCollLetter = new CollLetter();
			iCollLetterId.setCaseCode(titaVo.getParam("CaseCode"));
			iCollLetterId.setCustNo(iCollListVo.getCustNo());
			iCollLetterId.setFacmNo(iCollListVo.getFacmNo());
			if (iFunctionCd.equals("1")) {
				iCollLetterId.setTitaTlrNo(titaVo.getTlrNo());
				iCollLetterId.setTitaTxtNo(titaVo.getTxtNo());
				iCollLetterId.setAcDate(Integer.valueOf(titaVo.getEntDy()));// 營業日 放acdate
			} else if (iFunctionCd.equals("2")) {
				iCollLetterId.setTitaTlrNo(titaVo.getParam("TitaTlrNo"));
				iCollLetterId.setTitaTxtNo(titaVo.getParam("TitaTxtNo"));
				iCollLetterId.setAcDate(Integer.valueOf(titaVo.getParam("TitaAcDate")));// 營業日 放acdate
				this.info("updatetlrno" + titaVo.getParam("TitaTlrNo"));
				this.info("updatetlrno" + titaVo.getParam("TitaTxtNo"));
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
			if (iFunctionCd.equals("1")) {
				if (tCollLetter == null) {
					try {
						this.info("trytoinsert");
						iCollLetterService.insert(iCollLetter, titaVo);
					} catch (DBException e) {
						throw new LogicException(titaVo, "E0005", e.getErrorMsg()); // 電催檔找不到資料錯誤
					}
				}
			}
			if (iFunctionCd.equals("2")) {
				this.info("update choose");
				if (tCollLetter != null) {
					try {
						this.info("trytoupdate");
						iCollLetterService.holdById(iCollLetterId);
						iCollLetterService.update(iCollLetter, titaVo);
					} catch (DBException e) {
						throw new LogicException(titaVo, "E0005", e.getErrorMsg()); // 電催檔更新資料錯誤
					}
				} else {
					this.info("nottrytoupdate");
					throw new LogicException(titaVo, "E0005", ""); // 電催檔找不到資料錯誤
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
					upCollList.setTxDate(Integer.valueOf(titaVo.getEntDy())); // 上次作業日期
					iCollListService.update(upCollList, titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0005", e.getErrorMsg()); // 主檔更新錯誤訊息
				}
			} else {
				throw new LogicException(titaVo, "E0005", ""); // 主檔無資料錯誤訊息
			}
		}
		this.addList(this.totaVo);
		return this.sendList();
	}

}
