package com.st1.itx.trade.L8;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.service.TxAmlLogService;
import com.st1.itx.db.service.springjpa.cm.L8080ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
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

	/* DB服務注入 */
	@Autowired
	public TxAmlLogService txAmlLogService;
	@Autowired
	public L8080ServiceImpl l8080ServiceImpl;

	@Autowired
	Parse parse;
	@Autowired
	public CheckAml checkAml;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8080 ");
		this.totaVo.init(titaVo);

		checkAml.setTxBuffer(this.getTxBuffer());

		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 200;
		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();

		try {
			resultList = l8080ServiceImpl.findAll(this.index, this.limit, titaVo);
		} catch (Exception e) {
			this.info("Error ... " + e.getMessage());
		}

		if (resultList != null && resultList.size() != 0) {
			/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
			if (resultList.size() == this.limit && hasNext()) {
				titaVo.setReturnIndex(this.setIndexNext());
				/* 手動折返 */
				this.totaVo.setMsgEndToEnter();
			}

			for (Map<String, String> result : resultList) {
				this.info("result = " + result);
//			for (TxAmlLog txAmlLog : lTxAmlLog) {
				OccursList occursList = new OccursList();

				occursList.putParam("oLogNo", result.get("LogNo"));
				occursList.putParam("oEntdy", result.get("Entdy"));
				occursList.putParam("oTransactionId", result.get("TransactionId"));
				occursList.putParam("oAcctNo", result.get("AcctNo"));
				occursList.putParam("oCaseNo", result.get("CaseNo"));

				Document doc = checkAml.convertStringToXml(result.get("MsgRg"));
				this.info("result.get(\"F5\") = " + result.get("MsgRg"));
				this.info("doc = " + doc);
				String oName = checkAml.getXmlValue(doc, "Name");

				occursList.putParam("oName", oName);

				occursList.putParam("oConfirmStatus", result.get("ConfirmStatus"));
				occursList.putParam("oConfirmCode", result.get("ConfirmCode"));
				occursList.putParam("oConfirmEmpNo", result.get("ConfirmEmpNo"));
				occursList.putParam("oConfirmTranCode", result.get("ConfirmTranCode"));

				/* 將每筆資料放入Tota的OcList */
				this.totaVo.addOccursList(occursList);
			}
		} else {

			throw new LogicException(titaVo, "E0001", "查無資料");
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private Boolean hasNext() {
		Boolean result = true;

		int times = this.index + 1;
		int cnt = l8080ServiceImpl.getSize();
		int size = times * this.limit;

		this.info("index ..." + this.index);
		this.info("times ..." + times);
		this.info("cnt ..." + cnt);
		this.info("size ..." + size);

		if (size == cnt) {
			result = false;
		}
		this.info("result ..." + result);

		return result;
	}
	
}