package com.st1.itx.trade.L6;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.domain.TxAuthorize;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.TxAuthorizeService;
import com.st1.itx.db.domain.TxTranCode;
import com.st1.itx.db.service.TxTranCodeService;
import com.st1.itx.db.service.TxTellerService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/**
 * Tita Entdy=D,7 SupNo=X,6 END=X,1
 */

@Service("L6044") // 主管授權紀錄查詢
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */

public class L6044 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public TxAuthorizeService sTxAuthorizeService;

	@Autowired
	public TxTranCodeService sTxTranCodeService;

	@Autowired
	public TxTellerService sTxTellerService;

	@Autowired
	CdEmpService cdEmpService;
	
	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6044 ");
		this.totaVo.init(titaVo);

		int iEntdyS = this.parse.stringToInteger(titaVo.getParam("EntdyS"));
		int iEntdyE = this.parse.stringToInteger(titaVo.getParam("EntdyE"));
		int iTxDateS = this.parse.stringToInteger(titaVo.getParam("TxDateS"));
		int iTxDateE = this.parse.stringToInteger(titaVo.getParam("TxDateE"));
		Timestamp ts1 = null;
		Timestamp ts2 = null;
		String iSupNo = titaVo.getParam("SupNo");
		String iTlrItem = "";
		String iTranItem = "";
		String DateTime; // YYY/MM/DD hh:mm:ss

		if(iTxDateS>0) {
			String sTxDateS = String.valueOf(iTxDateS+19110000);
			String sTxDateE = String.valueOf(iTxDateE+19110000);
			sTxDateS = sTxDateS.substring(0, 4)+"-"+sTxDateS.substring(4, 6)+"-"+sTxDateS.substring(6, 8)+" 00:00:00.0";
			sTxDateE = sTxDateE.substring(0, 4)+"-"+sTxDateE.substring(4, 6)+"-"+sTxDateE.substring(6, 8)+" 23:59:59.0";
			
			ts1 = Timestamp.valueOf(sTxDateS);
			ts2 = Timestamp.valueOf(sTxDateE);
			this.info("ts1="+ts1);
			this.info("ts2="+ts2);
		}
		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 100; // 395 * 100 = 39,500

		// 查詢主管授權紀錄
		Slice<TxAuthorize> slTxAuthorize = null;
		if (iEntdyS>0) {
			slTxAuthorize = sTxAuthorizeService.findSupNoEntdy(iEntdyS+19110000, iEntdyE+19110000, iSupNo +"%", this.index, this.limit, titaVo);
		} else if(iTxDateS>0){
			slTxAuthorize = sTxAuthorizeService.findCreatDate(ts1, ts2, iSupNo +"%", this.index, this.limit, titaVo);
		}
		
		
		List<TxAuthorize> lTxAuthorize = slTxAuthorize == null ? null : slTxAuthorize.getContent();

		if (lTxAuthorize == null || lTxAuthorize.size() == 0) {
			throw new LogicException(titaVo, "E0001", "主管授權紀錄"); // 查無資料
		}
		
		// 如有找到資料
		for (TxAuthorize tTxAuthorize : lTxAuthorize) {

			OccursList occursList = new OccursList();
			
			occursList.putParam("OOEntdy", tTxAuthorize.getEntdy());
			occursList.putParam("OOSupNo", tTxAuthorize.getSupNo());
			occursList.putParam("OOTlrNo", tTxAuthorize.getTlrNo());
			occursList.putParam("OOTradeReason", tTxAuthorize.getTradeReason());
			
			String reasonFAJson = tTxAuthorize.getReasonFAJson();
			String reasonAuth = "";
			if (reasonFAJson != null)
			{
				try {
					
					TitaVo[] tArray = new ObjectMapper().configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true).readValue(reasonFAJson, TitaVo[].class);
					
					for (TitaVo t : tArray)
					{
						reasonAuth += String.format("%s:%s;", t.getParam("NO"), t.getParam("MSG"));
					}
					
					// strip away last semicolon
					reasonAuth = reasonAuth.replaceAll(";$", "");
				} catch (Exception e)
				{
					this.error("L6044 trying to serialize reasonFAJson and failed!");
					this.error(e.getMessage());
				}
			}
			occursList.putParam("OOReasonAuth", reasonAuth);
			
			occursList.putParam("OOTxcd", tTxAuthorize.getTxcd());
			occursList.putParam("OOTxSeq", tTxAuthorize.getTxSeq());

			iTranItem = "";
			iTranItem = inqTxTranCode(tTxAuthorize.getTxcd(), iTranItem, titaVo);
			occursList.putParam("OOTranItem", iTranItem);

			iTlrItem = "";
			iTlrItem = inqCdEmp(tTxAuthorize.getTlrNo(), iTlrItem, titaVo);
			occursList.putParam("OOTlrItem", iTlrItem);
			iTlrItem = "";
			iTlrItem = inqCdEmp(tTxAuthorize.getSupNo(), iTlrItem, titaVo);
			occursList.putParam("OOSupItem", iTlrItem);

			DateTime = this.parse.timeStampToString(tTxAuthorize.getLastUpdate());
			occursList.putParam("OODateTime", DateTime);
			occursList.putParam("OOLastUpdate", parse.timeStampToStringDate(tTxAuthorize.getLastUpdate())+ " " +parse.timeStampToStringTime(tTxAuthorize.getLastUpdate()));
			occursList.putParam("OOLastEmp", tTxAuthorize.getLastUpdateEmpNo() + " " + empName(titaVo, tTxAuthorize.getLastUpdateEmpNo()));
			
			

			/* 將每筆資料放入Tota的OcList */
		this.totaVo.addOccursList(occursList);
		}

		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (slTxAuthorize != null && slTxAuthorize.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			this.totaVo.setMsgEndToEnter();// 手動折返
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	// 查詢交易控制檔
	private String inqTxTranCode(String uTranNo, String uTranItem, TitaVo titaVo) throws LogicException {

		TxTranCode tTxTranCode = new TxTranCode();

		tTxTranCode = sTxTranCodeService.findById(uTranNo);

		if (tTxTranCode == null) {
			uTranItem = "";
		} else {
			uTranItem = tTxTranCode.getTranItem();
		}

		return uTranItem;

	}

	
	private String inqCdEmp(String uTlrNo, String uTlrItem, TitaVo titaVo) throws LogicException {

		CdEmp tCdEmp = new CdEmp();

		tCdEmp = cdEmpService.findById(uTlrNo);

		if (tCdEmp == null) {
			uTlrItem = uTlrNo;
		} else {
			uTlrItem = tCdEmp.getFullname();
		}

		return uTlrItem;

	}
	private String empName(TitaVo titaVo, String empNo) throws LogicException {
		String rs = empNo;

		CdEmp cdEmp = cdEmpService.findById(empNo, titaVo);
		if (cdEmp != null) {
			rs = cdEmp.getFullname();
		}
		return rs;
	}

}