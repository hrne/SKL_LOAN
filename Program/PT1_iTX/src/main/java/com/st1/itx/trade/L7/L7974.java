package com.st1.itx.trade.L7;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.ForeclosureFee;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.ForeclosureFeeService;
import com.st1.itx.db.service.springjpa.cm.L7074ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L7974")
@Scope("prototype")
/**
 * 
 * 
 * @author Mata
 * @version 1.0.0
 */
public class L7974 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public L7074ServiceImpl iL7074ServiceImpl;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L7974 ");
		this.totaVo.init(titaVo);

//		/*
//		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
//		 */
//		this.index = titaVo.getReturnIndex();
//
//		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
//		this.limit = 100; // 57 * 500 = 28500

		// tita
		// 會計日期
		int iAccDateStart = parse.stringToInteger(titaVo.getParam("AccDateStart")) + 19110000;
		String iGroupId =  titaVo.getParam("GroupId");
		int iSendStatus	= parse.stringToInteger(titaVo.getParam("SendStatus"));
		
		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
		
		try {

			// *** 折返控制相關 ***
			resultList = iL7074ServiceImpl.findAcDate2(iAccDateStart ,iGroupId , iSendStatus, titaVo);
		} catch (Exception e) {
			this.error("L7074ServiceImpl findByCondition " + e.getMessage());
			throw new LogicException("E0013", e.getMessage());
		}
		if (resultList != null && resultList.size() > 0) {
			/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */

			for (Map<String, String> result : resultList) {

			    OccursList occurslist = new OccursList();
			
					occurslist.putParam("OOAcBookCode",      result.get("AcBookCode")      ); //帳冊別
					occurslist.putParam("OOMediaSlipNo",     result.get("MediaSlipNo")     ); //傳票號碼
					occurslist.putParam("OOSeq",             result.get("Seq")             ); //傳票明細序號
					occurslist.putParam("OOAcDate",          result.get("AcDate")          ); //傳票日期
					occurslist.putParam("OOBatchNo",         result.get("BatchNo")         ); //傳票批號
					occurslist.putParam("OOMediaSeq",        result.get("MediaSeq")        ); //上傳核心序號
					occurslist.putParam("OOAcSubBookCode",   result.get("AcSubBookCode")   ); //區隔帳冊
					occurslist.putParam("OOAcNoCode" ,       result.get("AcNoCode" )       ); //科目代號
					occurslist.putParam("OOAcSubCode" ,      result.get("AcSubCode" )      ); //子目代號
					occurslist.putParam("OODeptCode"   ,     result.get("DeptCode"   )     ); //部門代號
					occurslist.putParam("OODbCr"       ,     result.get("DbCr")            ); //借貸別
					occurslist.putParam("OOTxAmt"      ,     result.get("TxAmt")           ); //金額
					occurslist.putParam("OOSlipRmk"    ,     result.get("SlipRmk")         ); //傳票摘要
					occurslist.putParam("OOReceiveCode" ,    result.get("ReceiveCode")     ); //會計科目銷帳碼
					occurslist.putParam("OOCostMonth"   ,    result.get("CostMonth" )      ); //成本月份
					occurslist.putParam("OOIfrs17Group" ,    result.get("Ifrs17Group")     ); //IFRS17群組
					occurslist.putParam("OOLatestFlag"  ,    result.get("LatestFlag"  )    ); //是否為最新
					occurslist.putParam("OOTransferFlag" ,   result.get("TransferFlag" )   ); //是否已傳輸
					occurslist.putParam("OOCreateDate"   ,   result.get("CreateDate"   )   ); //建檔日期時間
					occurslist.putParam("OOCreateEmpNo"  ,   result.get("CreateEmpNo"  )   ); //建檔人員
					String DateTime = this.parse.stringToStringDate(result.get("LastUpdate"  ));
					occurslist.putParam("OOLastUpdate"   ,   DateTime   ); //最後更新日期時間
					occurslist.putParam("OOLastUpdateEmpNo", result.get("LastUpdateEmpNo") ); //最後更新人員
					occurslist.putParam("OOErrorCode"  ,     result.get("ErrorCode"  )     ); //回應錯誤代碼
					occurslist.putParam("OOErrorMsg"   ,     result.get("ErrorMsg"  )      ); //回應錯誤訊息
					occurslist.putParam("OOSendStatusX",     result.get("SendStatusX"  )      ); //回應錯誤訊息


			/* 將每筆資料放入Tota的OcList */
			this.totaVo.addOccursList(occurslist);
		}
		}else {
			throw new LogicException("E0001", "查無資料");
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}