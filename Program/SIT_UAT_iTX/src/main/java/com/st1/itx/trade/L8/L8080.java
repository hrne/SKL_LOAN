package com.st1.itx.trade.L8;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;

import com.st1.itx.db.domain.TxAmlLog;
import com.st1.itx.db.service.TxAmlLogService;

import com.st1.itx.util.common.CheckAml;
import com.st1.itx.util.parse.Parse;

@Service("L8080")
@Scope("prototype")
/**
 * 
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public class L8080 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L8080.class);

	/* DB服務注入 */
	@Autowired
	public TxAmlLogService txAmlLogService;

	@Autowired
	Parse parse;
	@Autowired
	public CheckAml checkAml;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8080 ");
		this.totaVo.init(titaVo);

//		String iBrNo = titaVo.get("BrNo").trim();
//		String iStatus = titaVo.get("Status").trim();
//		int iAcDate = parse.stringToInteger(titaVo.get("AcDate")) + 19110000;
		
		String iBrNo = titaVo.getParam("BrNo").trim();
		String iStatus = titaVo.getParam("Status").trim();
		int iAcDate1 = Integer.valueOf(titaVo.getParam("AcDate1"))+19110000;
		int iAcDate2 = Integer.valueOf(titaVo.getParam("AcDate2"))+19110000;
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 200;

		Slice<TxAmlLog> slTxAmlLog = null;
		if ("9".equals(iStatus)) {
			slTxAmlLog = txAmlLogService.findByBrNo(iBrNo, iAcDate1, iAcDate2, this.index, this.limit);
		} else {
			slTxAmlLog = txAmlLogService.findByConfirmStatus(iBrNo, iStatus,iAcDate1, iAcDate2, this.index, this.limit);
		}

		List<TxAmlLog> lTxAmlLog = slTxAmlLog == null ? null : slTxAmlLog.getContent();

		if (lTxAmlLog == null) {
			throw new LogicException("E0001", "");
		} else {
			for (TxAmlLog txAmlLog : lTxAmlLog) {
				OccursList occursList = new OccursList();

				occursList.putParam("oLogNo", txAmlLog.getLogNo());
				occursList.putParam("oEntdy", txAmlLog.getEntdy());
				occursList.putParam("oTransactionId", txAmlLog.getTransactionId());
				occursList.putParam("oAcctNo", txAmlLog.getAcctNo());
				occursList.putParam("oCaseNo", txAmlLog.getCaseNo());

				Document doc = checkAml.convertStringToXml(txAmlLog.getMsgRg());
				String oName = checkAml.getXmlValue(doc, "Name");

				occursList.putParam("oName", oName);

				occursList.putParam("oConfirmStatus", txAmlLog.getConfirmStatus());
				occursList.putParam("oConfirmCode", txAmlLog.getConfirmCode());
				occursList.putParam("oConfirmEmpNo", txAmlLog.getConfirmEmpNo());
				occursList.putParam("oConfirmTranCode", txAmlLog.getConfirmTranCode());

				/* 將每筆資料放入Tota的OcList */
				this.totaVo.addOccursList(occursList);
			}
		}

		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (slTxAmlLog != null && slTxAmlLog.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			this.totaVo.setMsgEndToEnter();// 手動折返
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}