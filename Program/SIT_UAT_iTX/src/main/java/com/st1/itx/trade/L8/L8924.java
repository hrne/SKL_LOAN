package com.st1.itx.trade.L8;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;

import com.st1.itx.db.domain.TxDataLog;
import com.st1.itx.db.service.TxDataLogService;
import com.st1.itx.db.domain.TxTeller;
import com.st1.itx.db.service.TxTellerService;
import com.st1.itx.db.domain.TxTranCode;
import com.st1.itx.db.service.TxTranCodeService;

import com.st1.itx.util.parse.Parse;

@Service("L8924")
@Scope("prototype")
/**
 * 
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public class L8924 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L8924.class);

	/* DB服務注入 */
	@Autowired
	public TxDataLogService txDataLogService;

	@Autowired
	public TxTellerService sTxTellerService;

	@Autowired
	public TxTranCodeService sTxTranCodeService;

	@Autowired
	Parse parse;

	private List<String> iTranNo = new ArrayList<String>();
	
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8924 ");
		this.totaVo.init(titaVo);

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 100; // 393 * 100 = 39,300

		String iTlrItem = "";
		String iTranItem = "";

		String TranNo = titaVo.getParam("TRN_CODE");
		
		if(("1").equals(TranNo)) {
			iTranNo.add("L8203");
		} else if(("2").equals(TranNo)) {
			iTranNo.add("L8204");
		} else {
			iTranNo.add("L8203");
			iTranNo.add("L8204");
		}
		
		this.info("L8924 iDateStart="+titaVo.getParam("CDATESTART")+"L8924 iDateEND=" +titaVo.getParam("CDATEEND"));
//		int Code = Integer.parseInt(titaVo.getParam("TRN_CODE").toString());
		int CustNo = Integer.parseInt(titaVo.getParam("CUST_NO").toString());
		int FacmNo = Integer.parseInt(titaVo.getParam("FACM_NO").toString());
		int BormNo = Integer.parseInt(titaVo.getParam("BORM_SEQ").toString());
		int iDateStart = Integer.parseInt(titaVo.getParam("CDATESTART").toString());
		int iDateEnd = Integer.parseInt(titaVo.getParam("CDATEEND").toString());
		iDateStart = iDateStart + 19110000;
		iDateEnd = iDateEnd + 19110000;
		this.info("L8924 iDateStart="+iDateStart+"L8924 iDateEND=" +iDateEnd);
		
		List<TxDataLog> lTxDataLog = null;
		Slice<TxDataLog> slTxDataLog = null;
		if (CustNo > 0 && FacmNo > 0 && BormNo > 0) {
			slTxDataLog = txDataLogService.findByCustNo7(iDateStart, iDateEnd, iTranNo , CustNo, FacmNo, BormNo, this.index,
					this.limit, titaVo); 
		} else if (CustNo > 0 && FacmNo > 0) {
			slTxDataLog = txDataLogService.findByCustNo6(iDateStart, iDateEnd, iTranNo , CustNo, FacmNo, this.index, this.limit,
					titaVo);
		} else if (CustNo > 0) {
			slTxDataLog = txDataLogService.findByCustNo5(iDateStart, iDateEnd, iTranNo , CustNo, this.index, this.limit,
					titaVo);
		} else  {
			slTxDataLog = txDataLogService.findByCustNo4(iDateStart, iDateEnd, iTranNo , this.index, this.limit, titaVo);
		}
		lTxDataLog = slTxDataLog == null ? null : slTxDataLog.getContent();
		
		if (lTxDataLog == null) {
			throw new LogicException(titaVo, "E0001", "");
		}

		for (TxDataLog txDataLog : lTxDataLog) {
			
			
			
			List<HashMap<String, Object>> listMap = new ArrayList<HashMap<String, Object>>();
			try {
				this.info("txDataLog.getContent()=" + txDataLog.getContent());
				listMap = new ObjectMapper().readValue(txDataLog.getContent(), ArrayList.class);
			} catch (IOException e) {
				throw new LogicException("EC009", "資料格式");
			}
			this.info("listMap="+listMap);
			for (HashMap<String, Object> map : listMap) {
				String fld = map.get("f").toString();
				String oval = map.get("o").toString();
				String nval = map.get("n").toString();
				if ("最後更新人員".equals(fld) || "交易進行記號".equals(fld) || "上次櫃員編號".equals(fld) || "上次交易序號".equals(fld)
						|| "已編BorTx流水號".equals(fld) || "最後更新日期時間".equals(fld)) {
					continue;
				}
				OccursList occursList = new OccursList();
				occursList.putParam("OO_TRN_DTTM", parse.timeStampToString(txDataLog.getCreateDate()));
				occursList.putParam("OO_TxDate", txDataLog.getTxDate());

				iTlrItem = "";
				iTlrItem = inqTxTeller(txDataLog.getTlrNo(), iTlrItem, titaVo);
				occursList.putParam("OO_TlrItem", iTlrItem);
				iTranItem = "";
				iTranItem = inqTxTranCode(txDataLog.getTranNo(), iTranItem, titaVo);
				occursList.putParam("OO_TranItem", iTranItem);
				occursList.putParam("OO_TRN_CODE", txDataLog.getTranNo());

				occursList.putParam("OO_CUST_NO", txDataLog.getCustNo());
				occursList.putParam("OO_FACM_NO", txDataLog.getFacmNo());
				occursList.putParam("OO_BORM_SEQ", txDataLog.getBormNo());

				occursList.putParam("OO_COLUMN_NAME", fld);
				occursList.putParam("OO_DATA_BEFORE", oval.replace("$n", ""));
				occursList.putParam("OO_DATA_AFTER", nval.replace("$n", ""));

				/* 將每筆資料放入Tota的OcList */
				this.totaVo.addOccursList(occursList);
			}

		}

		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (slTxDataLog != null && slTxDataLog.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			this.totaVo.setMsgEndToEnter();// 手動折返
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	// 查詢交易控制檔
	private String inqTxTranCode(String uTranNo, String uTranItem, TitaVo titaVo) throws LogicException {

		TxTranCode tTxTranCode = new TxTranCode();

		tTxTranCode = sTxTranCodeService.findById(uTranNo, titaVo);

		if (tTxTranCode == null) {
			uTranItem = "";
		} else {
			uTranItem = tTxTranCode.getTranItem();
		}

		return uTranItem;

	}

	// 查詢使用者設定檔
	private String inqTxTeller(String uTlrNo, String uTlrItem, TitaVo titaVo) throws LogicException {

		TxTeller tTxTeller = new TxTeller();

		tTxTeller = sTxTellerService.findById(uTlrNo, titaVo);

		if (tTxTeller == null) {
			uTlrItem = uTlrNo;
		} else {
			uTlrItem = tTxTeller.getTlrItem();
		}

		return uTlrItem;

	}

}