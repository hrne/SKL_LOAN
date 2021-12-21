package com.st1.itx.trade.L8;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.domain.TxDataLog;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.TxDataLogService;
import com.st1.itx.db.domain.TxTeller;
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

		String iTlrItem = "";
		String iTranItem = "";

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

			if (!iMrkey.isEmpty()) {
				this.info("iMrkey==" + iMrkey);
				if (!(iMrkey).equals(txDataLog.getMrKey())) {
					continue;
				}
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
			occursList.putParam("OMrKey", txDataLog.getMrKey());

			occursList.putParam("OReason", txDataLog.getReason());

			String lastUpdate = parse.timeStampToString(txDataLog.getLastUpdate());

			occursList.putParam("OLastUpdate", lastUpdate);

			String lastEmp = txDataLog.getLastUpdateEmpNo();
			if (txDataLog.getLastUpdateEmpNo() != null && txDataLog.getLastUpdateEmpNo().toString().length() > 0) {
				CdEmp cdEmp = cdEmpService.findById(txDataLog.getLastUpdateEmpNo(), titaVo);
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