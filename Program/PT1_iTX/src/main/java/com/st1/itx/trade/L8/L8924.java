package com.st1.itx.trade.L8;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.TxDataLog;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.TxDataLogService;
import com.st1.itx.db.service.TxTellerService;
import com.st1.itx.db.domain.TxTranCode;
import com.st1.itx.db.service.TxTranCodeService;
import com.st1.itx.util.format.StringCut;
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

	/* DB服務注入 */
	@Autowired
	public TxDataLogService txDataLogService;

	@Autowired
	public TxTellerService sTxTellerService;

	@Autowired
	public TxTranCodeService sTxTranCodeService;

	@Autowired
	public CdEmpService cdEmpService;
	
	@Autowired
	public CustMainService sCustMainService;
	
	@Autowired
	Parse parse;

	private List<String> iTranNo = new ArrayList<String>();

	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8924 ");
		this.totaVo.init(titaVo);

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 100; // 393 * 100 = 39,300


		String TranNo = titaVo.getParam("TRN_CODE");

		if (("1").equals(TranNo)) {
			iTranNo.add("L8203");
			TranNo = "L8203";
		} else if (("2").equals(TranNo)) {
			iTranNo.add("L8204");
			TranNo = "L8204";
		} 

		int CustNo = Integer.parseInt(titaVo.getParam("CUST_NO").toString());
		int iDateStart = Integer.parseInt(titaVo.getParam("CDATESTART").toString());
		int iDateEnd = Integer.parseInt(titaVo.getParam("CDATEEND").toString());
		iDateStart = iDateStart + 19110000;
		iDateEnd = iDateEnd + 19110000;
		
		String iMrkey = titaVo.getMrKey().trim();
		

		 Slice<TxDataLog> slTxDataLog = txDataLogService.findByCustNo5(iDateStart, iDateEnd, iTranNo, CustNo, this.index, this.limit, titaVo);


		 List<TxDataLog> lTxDataLog = slTxDataLog == null ? null : slTxDataLog.getContent();

		if (lTxDataLog == null) {
			throw new LogicException(titaVo, "E0001", "");
		}
		
			for (TxDataLog txDataLog : lTxDataLog) {

			
				OccursList occursList = new OccursList();

				if(!iMrkey.isEmpty()) {
					this.info("iMrkey=="+iMrkey);
					if(!(iMrkey).equals(txDataLog.getMrKey())) {
						continue;					}
				}

				String tranName = txDataLog.getTranNo();
				
				TxTranCode txTranCode = sTxTranCodeService.findById(txDataLog.getTranNo(), titaVo);
				
				if (txTranCode != null) {
					tranName += " " + txTranCode.getTranItem();
				}
				
				occursList.putParam("OTranName", tranName);

				occursList.putParam("OCustNo", txDataLog.getCustNo());
				occursList.putParam("OFacmNo", txDataLog.getFacmNo());
				occursList.putParam("OBormNo", txDataLog.getBormNo());
				
				// 查詢客戶資料主檔
				CustMain tCustMain = new CustMain();
				int iCustNo = txDataLog.getCustNo();
				tCustMain = sCustMainService.custNoFirst(iCustNo, iCustNo, titaVo);
				if (tCustMain != null) {
					occursList.putParam("OCustName", tCustMain.getCustName().replace("$n", "")); // 戶名
				} else {
					occursList.putParam("OCustName", ""); // 戶名
				}
				
				occursList.putParam("OMrKey", txDataLog.getMrKey());
				
				// 如果變更明細有詳細的 before/after 那就列出
				// 否則用 TxDataLog.Reason
				
				String beforeAfter = getBeforeAfter(txDataLog);
				occursList.putParam("OReason", beforeAfter != null && !beforeAfter.trim().isEmpty() ? beforeAfter : txDataLog.getReason());
				
				String lastUpdate = parse.timeStampToString(txDataLog.getLastUpdate());

				occursList.putParam("OLastUpdate", lastUpdate);
				
				// 此次修改是主管覆核?
				// (放行時 update, 最後修改者仍會是經辦代號
				//  所以用這種比較 hacky 的作法...)		
				// 同樣邏輯在 L8925 有用到
				boolean isSupervisor = txDataLog.getReason() != null && "主管覆核".equals(txDataLog.getReason().trim());

				String lastEmp = isSupervisor ? txDataLog.getTlrNo() : txDataLog.getLastUpdateEmpNo();
				if (lastEmp != null && lastEmp.length() > 0) {
					CdEmp cdEmp = cdEmpService.findById(lastEmp, titaVo);
					if (cdEmp != null) {
						lastEmp += " " + StringCut.stringCut(cdEmp.getFullname(), 0, 10);
					}
				}
				occursList.putParam("OLastEmp", lastEmp);
				occursList.putParam("OTxDate", txDataLog.getTxDate());
				occursList.putParam("OTxSeq", txDataLog.getTxSeq());
				occursList.putParam("OTxSno", txDataLog.getTxSno());

				/* 將每筆資料放入Tota的OcList */
				this.totaVo.addOccursList(occursList);
			}

//		}

		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (slTxDataLog != null && slTxDataLog.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			this.totaVo.setMsgEndToEnter();// 手動折返
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
	
	@SuppressWarnings("unchecked")
	private String getBeforeAfter(TxDataLog txDataLog) throws LogicException
	{
		List<String> results = new ArrayList<String>();
		List<HashMap<String, Object>> listMap = new ArrayList<HashMap<String, Object>>();
		try {
			this.info("txDataLog.getContent()=" + txDataLog.getContent());
			listMap = new ObjectMapper().readValue(txDataLog.getContent(), ArrayList.class);
		} catch (IOException e) {
			throw new LogicException("EC009", "資料格式");
		}
		
		for (HashMap<String, Object> map : listMap) {

			boolean hasF = map.get("f") != null;
			boolean hasN = map.get("n") != null;
			String fld = hasF ? map.get("f").toString() : "";
			String nval = hasN ? map.get("n").toString() : "";
			
			if ("最後更新人員".equals(fld) || "交易進行記號".equals(fld) || "上次櫃員編號".equals(fld) || "上次交易序號".equals(fld) || "已編BorTx流水號".equals(fld) || "最後更新日期時間".equals(fld)) {
				continue;
			}
			
			if (hasF || hasN)
			{
				results.add(fld + ":" + nval);
			}
		}
		
		return String.join(",", results);
	}

}