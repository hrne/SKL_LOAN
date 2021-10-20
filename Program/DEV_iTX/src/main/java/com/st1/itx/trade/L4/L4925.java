package com.st1.itx.trade.L4;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.service.BatxDetailService;
import com.st1.itx.db.service.BatxHeadService;
import com.st1.itx.db.service.springjpa.cm.L4925ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * EntryDateFrom=9,7<br>
 * EntryDateTo=9,7<br>
 * RepayCode=9,2<br>
 * CustNo=9,7<br>
 * END=X,1<br>
 */

@Service("L4925")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4925 extends TradeBuffer {
	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	@Autowired
	public BatxDetailService batxDetailService;

	@Autowired
	public BatxHeadService batxHeadService;

	@Autowired
	public L4925ServiceImpl sL4925ServiceImpl;
	
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4925 ");
		this.totaVo.init(titaVo);

//		 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();
//		設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 100;

		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
		try {
			// *** 折返控制相關 ***
			resultList = sL4925ServiceImpl.findAll(this.index, this.limit, titaVo);
		} catch (Exception e) {
			this.error("L4925ServiceImpl findByCondition " + e.getMessage());
			throw new LogicException("E0013", "L4925");

		}

		List<LinkedHashMap<String, String>> chkOccursList = null; 
		
		if (resultList != null && resultList.size() > 0) {
			
			for (Map<String, String> result : resultList) {
		
			    this.info("L4925 result = " + result.toString());
			    // new occurs
			  
			    TempVo tempVo = new TempVo();
			    OccursList occursList = new OccursList();
			    
			    occursList.putParam("OOAcDate", 0);
			    if(parse.stringToInteger(result.get("F0")) > 0) {
			      occursList.putParam("OOAcDate", parse.stringToInteger(result.get("F0")) - 19110000);
			    } 
				occursList.putParam("OOBatchNo", result.get("F1"));
				occursList.putParam("OODetailSeq", result.get("F2"));
				occursList.putParam("OORepayCode", result.get("F3"));
				
				occursList.putParam("OOEntryDate", 0);
				if(parse.stringToInteger(result.get("F4")) > 0) {
				  occursList.putParam("OOEntryDate", parse.stringToInteger(result.get("F4")) - 19110000);
				} 
				occursList.putParam("OOCustNo", result.get("F5"));
				occursList.putParam("OOFacmNo", result.get("F6"));
				occursList.putParam("OORepayType", result.get("F7"));
				occursList.putParam("OOReconCode", result.get("F8"));
				occursList.putParam("OORepayAmt", result.get("F9"));
				occursList.putParam("OOAcctAmt", result.get("F10"));
				occursList.putParam("OODisacctAmt", result.get("F11"));
				occursList.putParam("OOProcStsCode", result.get("F12"));
				occursList.putParam("OOProcCode", result.get("F13"));
				
				
				String procNote = "";
				if (result.get("F16") != null) {
					tempVo = tempVo.getVo(result.get("F16") );

					if (tempVo.get("CheckMsg") != null && tempVo.get("CheckMsg").length() > 0) {
						procNote = " 檢核訊息:" + tempVo.get("CheckMsg") + " ";
					}
					if (tempVo.get("ErrorMsg") != null && tempVo.get("ErrorMsg").length() > 0) {
						procNote = procNote + " 錯誤訊息:" + tempVo.get("ErrorMsg") + " ";
					}
					if (tempVo.get("Note") != null && tempVo.get("Note").length() > 0) {
						procNote = procNote + " 摘要:" + tempVo.get("Note");
					}
//					當吃檔進去時不會寫入還款類別，檢核後才會寫入。
//					若該筆無還款類別且為數字型態，顯示虛擬帳號
					if (tempVo.get("VirtualAcctNo") != null && parse.stringToInteger(result.get("F7")) == 0
							&& isNumeric(tempVo.get("VirtualAcctNo"))) {
						procNote = procNote + " 虛擬帳號:" + tempVo.get("VirtualAcctNo");
					}
					if (tempVo.get("PayIntDate") != null && tempVo.get("PayIntDate").length() > 0) {
						procNote = procNote + "應繳日:" + tempVo.get("PayIntDate");
					}
				} // if
				
				occursList.putParam("OOProcNote", procNote);

				occursList.putParam("OOTitaTlrNo", result.get("F14"));
				occursList.putParam("OOTitaTxtNo", result.get("F15"));
				occursList.putParam("OOTxSn",
						titaVo.getKinbr() + result.get("F14") + result.get("F15"));

				/* 將每筆資料放入Tota的OcList */
				this.totaVo.addOccursList(occursList);
				
			} // for
			
			chkOccursList = this.totaVo.getOccursList();
			  
			if (resultList.size() == this.limit && hasNext()) {
				 titaVo.setReturnIndex(this.setIndexNext());
					/* 手動折返 */
				 this.totaVo.setMsgEndToEnter();
			}  // if

		} // if
		
		if ( chkOccursList == null  && titaVo.getReturnIndex() == 0 ) {
			throw new LogicException(titaVo, "E0001", "查無資料");
		}
		
		this.addList(this.totaVo);
		return this.sendList();
		
	}

	public static boolean isNumeric(String str) {
		for (int i = 0; i < str.length(); i++) {
//			System.out.println(str.charAt(i));
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}
	
	private Boolean hasNext() {
		Boolean result = true;

		int times = this.index + 1;
		int cnt = sL4925ServiceImpl.getSize();
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