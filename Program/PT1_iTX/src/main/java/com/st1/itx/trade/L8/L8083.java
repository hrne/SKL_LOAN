package com.st1.itx.trade.L8;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.db.domain.TxAmlCredit;
import com.st1.itx.db.service.TxAmlCreditService;

import com.st1.itx.db.domain.CdBcm;
import com.st1.itx.db.service.CdBcmService;

@Service("L8083")
@Scope("prototype")
/**
 * 
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public class L8083 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	TxAmlCreditService txAmlCreditService;

	@Autowired
	CdBcmService cdBcmService;

	@Autowired
	Parse parse;

	HashMap<String, String> unitItems = new HashMap<String, String>();

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8081 ");
		this.totaVo.init(titaVo);

		String iReviewType = titaVo.get("ReviewType").trim();
		String iProcessType = titaVo.get("ProcessType").trim();
		if ("9".equals(iProcessType)) {
			iProcessType = "%";
		}
		String iStatus = titaVo.get("Status").trim();
		int iAcDate1 = parse.stringToInteger(titaVo.get("AcDate1")) + 19110000;
		int iAcDate2 = parse.stringToInteger(titaVo.get("AcDate2")) + 19110000;

		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 500;

		Slice<TxAmlCredit> slTxAmlCredit = null;
		
		List<String> reviewType = null;
//		List<String> reviewType = Arrays.asList("L");
		if ("9".equals(iReviewType)) {
			reviewType = Arrays.asList("L");
		} else {
			reviewType = Arrays.asList(iReviewType);
		}
		
		if ("9".equals(iStatus)) {
			slTxAmlCredit = txAmlCreditService.processAll(reviewType, iAcDate1, iAcDate2, iProcessType, this.index, this.limit);
		} else if ("1".equals(iStatus)) {
			slTxAmlCredit = txAmlCreditService.processYes(reviewType, iAcDate1, iAcDate2, iProcessType, 0, this.index, this.limit);
		} else {
			slTxAmlCredit = txAmlCreditService.processNo(reviewType, iAcDate1, iAcDate2, iProcessType, 0, this.index, this.limit);
		}

		List<TxAmlCredit> lTxAmlCredit = slTxAmlCredit == null ? null : slTxAmlCredit.getContent();

		if (lTxAmlCredit == null) {
			throw new LogicException("E0001", "");
		} else {
			for (TxAmlCredit txAmlCredit : lTxAmlCredit) {

				OccursList occursList = new OccursList();

				occursList.putParam("oDataDt", txAmlCredit.getDataDt());
				occursList.putParam("oCustKey", txAmlCredit.getCustKey());
				occursList.putParam("oRRSeq", txAmlCredit.getRRSeq());
				occursList.putParam("oReviewType", txAmlCredit.getReviewType());
				occursList.putParam("oUnit", txAmlCredit.getUnit());
				occursList.putParam("oUnitItem", getUnitItem(txAmlCredit.getUnit().trim(), titaVo));
				occursList.putParam("oIsStatus", txAmlCredit.getIsStatus());
//				this.info("txAmlCredit.ProcessType="+txAmlCredit.getProcessType());
				occursList.putParam("oProcessType", txAmlCredit.getProcessType());
				occursList.putParam("oProcessCount", txAmlCredit.getProcessCount());
//				
//				occursList.putParam("oDataDt", txAmlCredit.getDataDt());
//				occursList.putParam("oCustKey", txAmlCredit.getCustKey());
//				occursList.putParam("oRRSeq", txAmlCredit.getRRSeq());
//				occursList.putParam("oReviewType", txAmlCredit.getReviewType());
////				occursList.putParam("oUnit", txAmlCredit.getUnit());
//				occursList.putParam("oUnitItem", getUnitItem(txAmlCredit.getUnit().trim(), titaVo));
//				occursList.putParam("oIsStatus", txAmlCredit.getIsStatus());
////				this.info("txAmlCredit.ProcessType="+txAmlCredit.getProcessType());
//				occursList.putParam("oProcessType", txAmlCredit.getProcessType());
//				occursList.putParam("oProcessCount", txAmlCredit.getProcessCount());

				/* 將每筆資料放入Tota的OcList */
				this.totaVo.addOccursList(occursList);
			}
		}

		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (slTxAmlCredit != null && slTxAmlCredit.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			this.totaVo.setMsgEndToEnter();// 手動折返
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private String getUnitItem(String unitCode, TitaVo titaVo) {
		String unitItem = "";

		if ("".equals(unitCode)) {
			return unitItem;
		}

		if (unitItems.size() > 0) {
			if (unitItems.get(unitCode) != null) {
				unitItem = unitItems.get(unitCode).toString();
			}

		}

		if ("".equals(unitItem)) {
			CdBcm cdBcm = cdBcmService.findById(unitCode, titaVo);
			if (cdBcm == null) {
				unitItem = unitCode;
			} else {
				unitItem = cdBcm.getUnitItem();
				unitItems.put(unitCode, unitItem);
			}
		}
		return unitItem;
	}
}