package com.st1.itx.trade.L6;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

@Service("L6932")
@Scope("prototype")
/**
 * 
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public class L6932 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public TxDataLogService txDataLogService;

	@Autowired
	public TxTellerService sTxTellerService;

	@Autowired
	public TxTranCodeService sTxTranCodeService;

	@Autowired
	Parse parse;

	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6932 ");
		this.totaVo.init(titaVo);

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 20; // 793 * 20 = 11,860

		int sdt = Integer.parseInt(titaVo.getParam("ST_DT").toString()) + 19110000;
		int edt = Integer.parseInt(titaVo.getParam("ED_DT").toString()) + 19110000;
		String TranNo = titaVo.getParam("TRN_CODE");
		int CustNo = Integer.parseInt(titaVo.getParam("CUST_NO").toString());
		int FacmNo = Integer.parseInt(titaVo.getParam("FACM_NO").toString());
		int BormNo = Integer.parseInt(titaVo.getParam("BORM_SEQ").toString());
		String iTxtNo = titaVo.getParam("TxtNo").trim();
		int iChainFlag = Integer.parseInt(titaVo.getParam("CHAINFlag"));

		this.info("sdt====" + sdt);
		this.info("edt====" + edt);
		this.info("iTxtNo====" + iTxtNo);
		this.info("CustNo====" + CustNo);
		this.info("FacmNo====" + FacmNo);
		this.info("BormNo====" + BormNo);

		List<TxDataLog> lTxDataLog = null;
		Slice<TxDataLog> slTxDataLog = null;
		if (!iTxtNo.isEmpty()) {
			slTxDataLog = txDataLogService.findTxSeq(sdt, edt, iTxtNo, this.index, this.limit, titaVo);
		} else if (CustNo > 0 && FacmNo > 0 && BormNo > 0) {
			slTxDataLog = txDataLogService.findByCustNo3(sdt, edt, TranNo + "%", CustNo, FacmNo, BormNo, this.index, this.limit, titaVo);
		} else if (CustNo > 0 && FacmNo > 0) {
			slTxDataLog = txDataLogService.findByCustNo2(sdt, edt, TranNo + "%", CustNo, FacmNo, this.index, this.limit, titaVo);
		} else if (CustNo > 0) {
			slTxDataLog = txDataLogService.findByCustNo1(sdt, edt, TranNo + "%", CustNo, this.index, this.limit, titaVo);
		} else {
			slTxDataLog = txDataLogService.findByCustNo0(sdt, edt, TranNo + "%", this.index, this.limit, titaVo);
		}
		lTxDataLog = slTxDataLog == null ? null : slTxDataLog.getContent();

		if (lTxDataLog == null) {
			throw new LogicException(titaVo, "E0001", "");
		}
		this.info("lTxDataLog==" + lTxDataLog.size());
		for (TxDataLog txDataLog : lTxDataLog) {
//			if (!iTxtNo.isEmpty() && iTxtNo.equals(txDataLog.getTxSeq())) {
//				continue;
//			}
			List<HashMap<String, Object>> listMap = new ArrayList<HashMap<String, Object>>();
			try {
				this.info("txDataLog.getContent()=" + txDataLog.getContent());
				listMap = new ObjectMapper().readValue(txDataLog.getContent(), ArrayList.class);
			} catch (IOException e) {
				throw new LogicException("EC009", "資料格式");
			}
			boolean first = true;
			for (HashMap<String, Object> map : listMap) {
				String fld = "";
				if(map.get("f") !=null) {
					fld = map.get("f").toString();
				}
				String oval = map.get("o").toString();
				String nval = map.get("n").toString();
				if ("最後更新人員".equals(fld) || "交易進行記號".equals(fld) || "上次櫃員編號".equals(fld) || "上次交易序號".equals(fld) || "已編BorTx流水號".equals(fld) || "最後更新日期時間".equals(fld)) {
					continue;
				}
				this.info("iChainFlag=" + iChainFlag + ",TranNo==" + TranNo);
				if (iChainFlag == 1 && ("L5701").equals(TranNo)) { // L5701 喘息期歷程特殊需求
					if ("延期繳款年月(起)".equals(fld) || "延期繳款年月(訖)".equals(fld)) {

					} else {
						continue;
					}
				}

				if (first) {
					this.info("L6932 getReason = " + txDataLog.getReason().trim());
					if (!"".equals(txDataLog.getReason().trim())) {
						OccursList occursList = addOccurs(txDataLog, "變更理由", "", txDataLog.getReason(), titaVo);
						this.totaVo.addOccursList(occursList);
					}
					first = false;
				}

//				OccursList occursList = new OccursList();
//				occursList.putParam("OO_TRN_DTTM", parse.timeStampToString(txDataLog.getCreateDate()));
//				occursList.putParam("OO_TxDate", txDataLog.getTxDate());
//
//				iTlrItem = "";
//				iTlrItem = inqTxTeller(txDataLog.getTlrNo(), iTlrItem, titaVo);
//				occursList.putParam("OO_TlrItem", iTlrItem);
//				iTranItem = "";
//				iTranItem = inqTxTranCode(txDataLog.getTranNo(), iTranItem, titaVo);
//				occursList.putParam("OO_TranItem", iTranItem);
//				occursList.putParam("OO_TRN_CODE", txDataLog.getTranNo());
//
//				occursList.putParam("OO_CUST_NO", txDataLog.getCustNo());
//				occursList.putParam("OO_FACM_NO", txDataLog.getFacmNo());
//				occursList.putParam("OO_BORM_SEQ", txDataLog.getBormNo());
//
//				occursList.putParam("OO_COLUMN_NAME", fld);
//				occursList.putParam("OO_DATA_BEFORE", oval);
//				occursList.putParam("OO_DATA_AFTER", nval);

				/* 將每筆資料放入Tota的OcList */

				OccursList occursList = addOccurs(txDataLog, fld, oval, nval, titaVo);

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

	private OccursList addOccurs(TxDataLog txDataLog, String fld, String oval, String nval, TitaVo titaVo) throws LogicException {
		OccursList occursList = new OccursList();
		occursList.putParam("OO_TRN_DTTM", parse.timeStampToString(txDataLog.getCreateDate()));
		occursList.putParam("OO_TxDate", txDataLog.getTxDate());

		String iTlrItem = inqTxTeller(txDataLog.getTlrNo(), titaVo);
		occursList.putParam("OO_TlrItem", iTlrItem);
		String iTranItem = inqTxTranCode(txDataLog.getTranNo(), titaVo);
		occursList.putParam("OO_TranItem", iTranItem);
		occursList.putParam("OO_TRN_CODE", txDataLog.getTranNo());

		occursList.putParam("OO_CUST_NO", txDataLog.getCustNo());
		occursList.putParam("OO_FACM_NO", txDataLog.getFacmNo());
		occursList.putParam("OO_BORM_SEQ", txDataLog.getBormNo());

		occursList.putParam("OO_COLUMN_NAME", fld);
		occursList.putParam("OO_DATA_BEFORE", oval);
		occursList.putParam("OO_DATA_AFTER", nval);

		return occursList;
	}

	// 查詢交易控制檔
	private String inqTxTranCode(String uTranNo, TitaVo titaVo) throws LogicException {

		TxTranCode tTxTranCode = new TxTranCode();

		tTxTranCode = sTxTranCodeService.findById(uTranNo, titaVo);

		String uTranItem = uTranNo;
		if (tTxTranCode != null) {
			uTranItem = tTxTranCode.getTranItem();
		}

		return uTranItem;

	}

	// 查詢使用者設定檔
	private String inqTxTeller(String uTlrNo, TitaVo titaVo) throws LogicException {

		TxTeller tTxTeller = new TxTeller();

		tTxTeller = sTxTellerService.findById(uTlrNo, titaVo);

		String uTlrItem = uTlrNo;
		if (tTxTeller != null) {
			uTlrItem = tTxTeller.getTlrItem();
		}

		return uTlrItem;

	}

}