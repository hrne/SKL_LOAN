package com.st1.itx.trade.L4;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.BankRmtf;
import com.st1.itx.db.domain.TxDataLog;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.service.BankRmtfService;
import com.st1.itx.db.service.TxDataLogService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.db.service.springjpa.cm.L4926ServiceImpl;
import com.st1.itx.util.parse.Parse;

/**
 * Tita ReconCode=X,3 EntryDate=9,7 CustNo=9,7
 * TraderInfo=X,20 END=X,1
 */

@Service("L4926") // 匯款轉帳檔查詢
@Scope("prototype")
/**
 *
 *
 * @author Linda
 * @version 1.0.0
 */

public class L4926 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public BankRmtfService sBankRmtfService;
	@Autowired
	public CustMainService sCustMainService;
	@Autowired
	Parse parse;
	@Autowired
	public TxDataLogService txDataLogService;
	
	@Autowired 
	L4926ServiceImpl l4926Servicelmpl;
	
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4926 ");
		this.totaVo.init(titaVo);

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 100; //  
						
		List<Map<String, String>> resultList = null;
		try {
			resultList = l4926Servicelmpl.queryresult(this.index,this.limit,titaVo);
	
		} catch (Exception e) {
			this.error("l4926Servicelmpl findByCondition " + e.getMessage());
			throw new LogicException("E0013", e.getMessage());
		}
				
		// 如有找到資料
		if (resultList != null && resultList.size() > 0) {
			for (Map<String, String> result : resultList) {
				OccursList occursList = new OccursList();
			
				int iAcDate = parse.stringToInteger(result.get("AcDate")) - 19110000;
				int iEntryDate = parse.stringToInteger(result.get("EntryDate")) - 19110000;
				occursList.putParam("OOAcDate",  iAcDate); // 會計日
				occursList.putParam("OOBatchNo",  result.get("BatchNo")); // 批號
				occursList.putParam("OODetailSeq",  result.get("DetailSeq")); // 明細序號
				occursList.putParam("OOCustNo", result.get("CustNo")); // 戶號
				occursList.putParam("OORepayType", result.get("RepayType")); // 還款類別
				occursList.putParam("OORepayAmt", result.get("RepayAmt")); // 還款金額
				occursList.putParam("OOEntryDate",  iEntryDate); // 入帳日期
				occursList.putParam("OODscptCode",  result.get("DscptCode")); // 摘要代碼
				occursList.putParam("OORemintBank",  result.get("RemintBank")); // 匯款銀行代碼
				occursList.putParam("OOTraderInfo", result.get("TraderInfo")); // 交易人資料
				occursList.putParam("OOReconCode", result.get("ReconCode")); // 對帳類別
				occursList.putParam("OOTitaTlrNo", result.get("TitaTlrNo")); // 經辦
				occursList.putParam("OOTitaTxtNo", result.get("TitaTxtNo")); // 交易序號
						 
				/* 將每筆資料放入Tota的OcList */
				this.totaVo.addOccursList(occursList);
		 }

		 /* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */

		 if (resultList != null &&  resultList.size() > this.limit) {
	 		 titaVo.setReturnIndex(this.setIndexNext());
		 	 /* 手動折返 */
		 	 this.totaVo.setMsgEndToEnter();
		 }
		} else {
			throw new LogicException(titaVo, "E0001", "");
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
	
}
