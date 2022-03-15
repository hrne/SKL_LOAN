package com.st1.itx.trade.L8;

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
import com.st1.itx.db.domain.MlaundryRecord;
import com.st1.itx.db.domain.MlaundryRecordId;
import com.st1.itx.db.domain.TxDataLog;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.service.MlaundryRecordService;
import com.st1.itx.db.service.TxDataLogService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.db.service.springjpa.cm.L8923ServiceImpl;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita RecordDateStart=9,7 RecordDateEnd=9,7 ActualRepayDateStart=9,7
 * ActualRepayDateEnd=9,7 END=X,1
 */

@Service("L8923") // 疑似洗錢交易訪談記錄檔查詢
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */

public class L8923 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public MlaundryRecordService sMlaundryRecordService;
	@Autowired
	public CustMainService sCustMainService;
	@Autowired
	Parse parse;
	@Autowired
	public TxDataLogService txDataLogService;
	
	@Autowired 
	L8923ServiceImpl l8923Servicelmpl;
	private int recorddate = 0;
	private int repaydate = 0;
	private int actualrepaydate = 0;
	
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8923 ");
		this.totaVo.init(titaVo);

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 50; // 507 *  50= 
				
		// 取得輸入資料
		String DateTime; // YYY/MM/DD hh:mm:ss
		String Date = "";		
		
		List<Map<String, String>> resultList = null;
		try {
			resultList = l8923Servicelmpl.queryresult(this.index,this.limit,titaVo);
	
		} catch (Exception e) {
			this.error("l8923Servicelmpl findByCondition " + e.getMessage());
			throw new LogicException("E0013", e.getMessage());
		}
		
		int iFacmNo = 0;
		int iBormNo = 0;
		MlaundryRecord iMlaundryRecord = null;
		
		// 如有找到資料
		if (resultList != null && resultList.size() > 0) {
			for (Map<String, String> result : resultList) {
				OccursList occursList = new OccursList();
				setData(result);
				// 查詢客戶資料主檔
				CustMain tCustMain = new CustMain();
				int iCustNo = Integer.parseInt(result.get("F2"));
				tCustMain = sCustMainService.custNoFirst(iCustNo, iCustNo, titaVo);
				if (tCustMain != null) {
					occursList.putParam("OOCustName", tCustMain.getCustName().replace("$n", "")); // 戶名
				} else {
					occursList.putParam("OOCustName", ""); // 戶名
				}
			
			
				occursList.putParam("OORecordDate", recorddate); // 訪談日期
				occursList.putParam("OOCustNo", result.get("F2")); // 戶號
				occursList.putParam("OOFacmNo", result.get("F3")); // 額度編號
				occursList.putParam("OOBormNo", result.get("F4")); // 撥款序號
				occursList.putParam("OORepayDate", repaydate); // 預定還款日期
				occursList.putParam("OOActualRepayDate", actualrepaydate); // 實際還款日期
				occursList.putParam("OORepayAmt", result.get("F5")); // 還款金額
				
				occursList.putParam("OOActualRepayAmt", result.get("F14")); // 實際還款金額
				
				occursList.putParam("OOEmpNo", result.get("F10")); // 經辦
			
				iFacmNo = parse.stringToInteger(result.get("F3"));
				iBormNo = parse.stringToInteger(result.get("F4"));
			
				iMlaundryRecord = sMlaundryRecordService.findById(new MlaundryRecordId(recorddate+19110000,iCustNo,iFacmNo,iBormNo), titaVo);
				if(iMlaundryRecord!=null) {
					DateTime = this.parse.timeStampToString(iMlaundryRecord.getLastUpdate()); // 異動日期
					this.info("DateTime="+DateTime);
					Date = FormatUtil.left(DateTime, 9);
					this.info("Date="+Date);
				} else {
					Date = "";
				}
			
				occursList.putParam("OOUpdate", Date);// 異動日期
				String mrkey = recorddate + String.format("%07d", iCustNo)+String.format("%03d", iFacmNo)+String.format("%03d", iBormNo);
				Slice<TxDataLog> slTxDataLog = txDataLogService.findByTranNo("L8204", mrkey, this.index, this.limit, titaVo);
				if(slTxDataLog == null) {
					occursList.putParam("OOHaveLog", "0"); // 控制歷程按鈕
				} else {
					occursList.putParam("OOHaveLog", "1"); // 控制歷程按鈕
				}
				 
				/* 將每筆資料放入Tota的OcList */
				this.totaVo.addOccursList(occursList);
		 }

		 /* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */

		 if (resultList != null &&  resultList.size() >= this.limit) {
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
	
//	private Boolean hasNext() {
//		Boolean result = true;
//
//		int times = this.index + 1;
//		int cnt = l8923Servicelmpl.getSize();
//		int size = times * this.limit;
//
//		this.info("index ..." + this.index);
//		this.info("times ..." + times);
//		this.info("cnt ..." + cnt);
//		this.info("size ..." + size);
//
//		if (size == cnt) {
//			result = false;
//		}
//		this.info("result ..." + result);
//
//		return result;
//	}
	private void setData(Map<String, String> result) throws LogicException {
		
		recorddate = parse.stringToInteger(result.get("F0"));
		repaydate = parse.stringToInteger(result.get("F12"));
		actualrepaydate = parse.stringToInteger(result.get("F1"));
		this.info("recorddate="+recorddate+",repaydate="+repaydate+",actualrepaydate="+actualrepaydate);
		
		if(recorddate>19110000) {
			recorddate = recorddate-19110000;
		}
		if(repaydate>19110000) {
			repaydate = repaydate-19110000;
		}
		if(actualrepaydate>19110000) {
			actualrepaydate = actualrepaydate-19110000;
		}
	}
}
