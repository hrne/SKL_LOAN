package com.st1.itx.trade.LC;

import java.util.ArrayList;
import java.util.HashMap;
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
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.db.domain.TxRecord;
import com.st1.itx.db.service.TxRecordService;
import com.st1.itx.db.domain.CdBranch;
import com.st1.itx.db.service.CdBranchService;
import com.st1.itx.db.domain.TxTeller;
import com.st1.itx.db.service.TxTellerService;
import com.st1.itx.db.domain.TxTranCode;
import com.st1.itx.db.service.TxTranCodeService;

@Service("LC011")
@Scope("prototype")
/**
 * 
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public class LC011 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(LC011.class);

	/* DB服務注入 */
	@Autowired
	public TxRecordService txRecordService;

	@Autowired
	public TxTellerService txTellerService;

	@Autowired
	public TxTranCodeService sTxTranCodeService;
	
	@Autowired
	CdBranchService cdBranchService;

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	Parse parse;

	private HashMap tlrItems = new HashMap();
	
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active LC011 ");
		this.totaVo.init(titaVo);

		int iEntday = Integer.valueOf(titaVo.get("iEntdy").trim()) + 19110000;
		String iBrNo = titaVo.get("iBrNo").trim();
		String iTlrNo = titaVo.get("iTlrNo").trim();
		String iTranNo = titaVo.get("iTranNo").trim();
		int iStatus = Integer.parseInt(titaVo.get("iStatus").trim());
		String iTlrItem = "";
		String iTranItem = "";

		this.info("LC001 parm = " + iEntday + "/" + iBrNo + "/" + iTlrNo + "/" + iTranNo + "/" + iStatus);

		CdBranch cdBranch = cdBranchService.findById(iBrNo);
						
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 200;

		Slice<TxRecord> slTxRecord = null;
		if (iStatus == 9) {
			slTxRecord = txRecordService.findByLC011All(iEntday, iBrNo, "S", iTlrNo + "%", iTranNo + "%", this.index,
					this.limit);
		} else if (iStatus == 0) {
			slTxRecord = txRecordService.findByLC011(iEntday, iBrNo, "S", 0, iTlrNo + "%", iTranNo + "%", this.index,
					this.limit);
		} else if (iStatus == 1 || iStatus == 2) {
			slTxRecord = txRecordService.findByLC011Hcode(iEntday, iBrNo, "S", iStatus, iTlrNo + "%", iTranNo + "%",
					this.index, this.limit);
		} else if (iStatus == 3) {
			slTxRecord = txRecordService.findByLC011(iEntday, iBrNo, "S", 1, iTlrNo + "%", iTranNo + "%", this.index,
					this.limit);
		} else if (iStatus == 4) {
			slTxRecord = txRecordService.findByLC011(iEntday, iBrNo, "S", 2, iTlrNo + "%", iTranNo + "%", this.index,
					this.limit);
		} else if (iStatus == 5) {
			slTxRecord = txRecordService.findByLC011(iEntday, iBrNo, "S", 3, iTlrNo + "%", iTranNo + "%", this.index,
					this.limit);
		}

		List<TxRecord> lTxRecord = slTxRecord == null ? null : slTxRecord.getContent();

		if (lTxRecord == null) {
			throw new LogicException(titaVo, "E0001", "交易明細資料");
		} else {
			for (TxRecord tTxRecord : lTxRecord) {
				OccursList occursList = new OccursList();
				occursList.putParam("CalDate", tTxRecord.getCalDate());
				occursList.putParam("CalTime", tTxRecord.getCalTime());
				occursList.putParam("Entdy", tTxRecord.getEntdy());
				occursList.putParam("TxNo", tTxRecord.getTxNo());
				occursList.putParam("TranNo", tTxRecord.getTranNo());
				occursList.putParam("MrKey", tTxRecord.getMrKey());
				occursList.putParam("CurName", tTxRecord.getCurName());
				occursList.putParam("TxAmt", tTxRecord.getTxAmt());
				occursList.putParam("BrNo",cdBranch.getBranchItem());
				// 放行、審核放行，抓經辦代碼
				if (tTxRecord.getFlowStep() == 2 || tTxRecord.getFlowStep() == 4) {
					occursList.putParam("TlrNo", tTxRecord.getOrgTxNo().substring(4, 10));
					occursList.putParam("SupNo", tTxRecord.getTlrNo());
					iTlrItem = "";
					iTlrItem = inqTxTeller(tTxRecord.getOrgTxNo().substring(4, 10), titaVo);
					occursList.putParam("TlrItem", iTlrItem);
					iTlrItem = "";
					iTlrItem = inqTxTeller(tTxRecord.getTlrNo(), titaVo);
					occursList.putParam("SupItem", iTlrItem);
				} else {
					occursList.putParam("TlrNo", tTxRecord.getTlrNo());
					occursList.putParam("SupNo", tTxRecord.getSupNo());
					iTlrItem = "";
					iTlrItem = inqTxTeller(tTxRecord.getTlrNo(), titaVo);
					occursList.putParam("TlrItem", iTlrItem);
					iTlrItem = "";
					iTlrItem = inqTxTeller(tTxRecord.getSupNo(), titaVo);
					occursList.putParam("SupItem", iTlrItem);
				}
				occursList.putParam("FlowType", tTxRecord.getFlowType());
				occursList.putParam("FlowStep", tTxRecord.getFlowStep());
				if (tTxRecord.getHcode() == 1 && tTxRecord.getOrgEntdy() > 0
						&& tTxRecord.getOrgEntdy() != tTxRecord.getEntdy()) {
					occursList.putParam("Hcode", 3);
				} else {
					occursList.putParam("Hcode", tTxRecord.getHcode());
				}

				occursList.putParam("Status", tTxRecord.getActionFg());
				occursList.putParam("FlowNo", tTxRecord.getFlowNo());
				this.info("會計日期		=" + tTxRecord.getEntdy());
				this.info("原會計日期		=" + tTxRecord.getOrgEntdy());
				if (tTxRecord.getOrgEntdy() > 0 && tTxRecord.getOrgEntdy() != tTxRecord.getEntdy()) {
					occursList.putParam("OOOrgEntdy", tTxRecord.getOrgEntdy());
				} else {
					occursList.putParam("OOOrgEntdy", "");
				}

				iTranItem = "";
				iTranItem = inqTxTranCode(tTxRecord.getTranNo(), iTranItem, titaVo);
				occursList.putParam("TranItem", iTranItem);


				if (tTxRecord.getAcCnt() > 0) {
					// 當天訂正及被訂正交易 無分錄
					if (tTxRecord.getHcode() == 1 && tTxRecord.getOrgEntdy() == tTxRecord.getEntdy()
							|| tTxRecord.getActionFg() == 1) {
						occursList.putParam("AcCnt", 0);
					} else {
						occursList.putParam("AcCnt", 1);
					}

				} else {
					occursList.putParam("AcCnt", 0);
				}

				/* 將每筆資料放入Tota的OcList */
				this.totaVo.addOccursList(occursList);
			}
		}

		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (slTxRecord != null && slTxRecord.hasNext())

		{
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

	// 查詢使用者

	private String inqTxTeller(String tlrNo,TitaVo titaVo) {
		String tlrItem = "";
		
		if ("".equals(tlrNo)) {
			return tlrItem;
		}
		
		if (tlrItems.size() > 0) {
			if (tlrItems.get(tlrNo) != null) {
				tlrItem = tlrItems.get(tlrNo).toString();
			}			
		}
		
		if ("".equals(tlrItem)) {
			TxTeller txTeller = txTellerService.findById(tlrNo, titaVo);
			if (txTeller == null) {
				tlrItem = tlrNo;
			} else {
				tlrItem = txTeller.getTlrItem();
				tlrItems.put(tlrNo, tlrItem);
			}
		} 
		return tlrItem;
	}
}