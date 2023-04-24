package com.st1.itx.trade.LC;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.db.domain.TxFlow;
import com.st1.itx.db.domain.TxFlowId;
import com.st1.itx.db.domain.TxRecord;
import com.st1.itx.db.service.TxFlowService;
import com.st1.itx.db.service.TxRecordService;
import com.st1.itx.db.service.TxTellerService;
import com.st1.itx.db.service.springjpa.cm.LC002ServiceImpl;

@Service("LC002")
@Scope("prototype")
/**
 * 修正查詢
 *
 * @author eric chang
 * @version 1.0.0
 */
public class LC002 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public TxRecordService txRecordService;

	@Autowired
	public TxFlowService txFlowService;

	@Autowired
	public TxTellerService sTxTellerService;

	@Autowired
	LC002ServiceImpl lc002ServiceImpl;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active LC002 ");
		this.totaVo.init(titaVo);

		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 40;

		try {

			List<Map<String, String>> dList = lc002ServiceImpl.findAll(titaVo, index, limit);

			if (dList != null && dList.size() > 0) {
				int cnt = 0;
				for (Map<String, String> dVo : dList) {
					OccursList occursList = new OccursList();
					occursList.putParam("CalDate", dVo.get("CalDate"));
					occursList.putParam("CalTime", dVo.get("CalTime"));
					occursList.putParam("Entdy", dVo.get("Entdy"));
					occursList.putParam("TxNo", dVo.get("TxNo"));
					occursList.putParam("TranNo", dVo.get("TranX"));
					occursList.putParam("MrKey", dVo.get("MrKey"));
					occursList.putParam("CurName", dVo.get("CurName"));
					occursList.putParam("TxAmt", dVo.get("TxAmt"));
					occursList.putParam("BrNo", dVo.get("BrX"));
					occursList.putParam("TlrNo", dVo.get("TlrX"));
					occursList.putParam("FlowType", dVo.get("FlowType"));
					occursList.putParam("FlowStep", dVo.get("FlowStep"));
					occursList.putParam("RejectReason", Objects.isNull(dVo.get("RejectReason")) || dVo.get("RejectReason").isEmpty() ? "" : "退回原因 : " + dVo.get("RejectReason"));

					int supRelease = 0;
					int flowType = dVo.get("FlowType").isEmpty() ? 0 : Integer.valueOf(dVo.get("FlowType"));
					int flowStep = dVo.get("FlowStep").isEmpty() ? 0 : Integer.valueOf(dVo.get("FlowStep"));
					int flowStep2 = dVo.get("FlowStep2").isEmpty() ? 0 : Integer.valueOf(dVo.get("FlowStep2"));
					int submitFg = dVo.get("SubmitFg").isEmpty() ? 0 : Integer.valueOf(dVo.get("SubmitFg"));
					int flowMode = dVo.get("FlowMode").isEmpty() ? 0 : Integer.valueOf(dVo.get("FlowMode"));
					if (flowType > 1 && flowStep == 1) {
						if (flowStep != flowStep2 || (flowStep == 1 && submitFg == 1 && flowMode != 3)) {
							supRelease = 1; // 1的時候 按鈕不開
						}
					}
					occursList.putParam("SupRelease", supRelease);

					/* 將每筆資料放入Tota的OcList */
					this.totaVo.addOccursList(occursList);

				}
			} else
				throw new LogicException(titaVo, "E0001", "修正資料");

			/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
			if (dList != null && dList.size() >= this.limit) {
				titaVo.setReturnIndex(this.setIndexNext());
				this.totaVo.setMsgEndToEnter();// 手動折返
			}

		} catch (LogicException e) {
			throw e;
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(errors.toString());
			throw new LogicException(titaVo, "E0000", errors.toString());
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	public ArrayList<TotaVo> run2(TitaVo titaVo) throws LogicException {
		int iEntday = Integer.valueOf(titaVo.get("iEntdy").trim()) + 19110000;
		String iBrNo = titaVo.get("iBrNo").trim();
		String iTlrNo = titaVo.get("iTlrNo").trim();
		String iTranNo = titaVo.get("iTranNo").trim();
		int supRelease = 0;
		this.info("LC002 parm = " + iEntday + "/" + iBrNo + "/" + iTlrNo + "/" + iTranNo);

		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 500;

		Slice<TxRecord> slTxRecord = txRecordService.findByLC002(iEntday, iBrNo, "S", 1, 0, 1, iTlrNo + "%", iTranNo + "%", this.index, this.limit);
		List<TxRecord> lTxRecord = slTxRecord == null ? null : slTxRecord.getContent();

		if (lTxRecord == null) {
			throw new LogicException(titaVo, "E0001", "修正資料");
		} else {
			for (TxRecord tTxRecord : lTxRecord) {
				// 兩段式以上的登錄交易==>主管已放行，不顯示<修正>按鈕
				supRelease = 0;
				if (tTxRecord.getFlowType() > 1 && tTxRecord.getFlowStep() == 1) {
					TxFlowId tTxFlowId = new TxFlowId();
					tTxFlowId.setEntdy(tTxRecord.getEntdy());
					tTxFlowId.setFlowNo(tTxRecord.getFlowNo());
					TxFlow tTxFlow = txFlowService.findById(tTxFlowId);
					if (tTxRecord.getFlowStep() != tTxFlow.getFlowStep() || (tTxRecord.getFlowStep() == 1 && tTxFlow.getSubmitFg() == 1 && tTxFlow.getFlowMode() != 3)) {
						supRelease = 1; // 1的時候 按鈕不開
					}
				}
				OccursList occursList = new OccursList();
				occursList.putParam("CalDate", tTxRecord.getCalDate());
				occursList.putParam("CalTime", tTxRecord.getCalTime());
				occursList.putParam("Entdy", tTxRecord.getEntdy());
				occursList.putParam("TxNo", tTxRecord.getTxNo());
				occursList.putParam("TranNo", tTxRecord.getTranNo());
				occursList.putParam("MrKey", tTxRecord.getMrKey());
				occursList.putParam("CurName", tTxRecord.getCurName());
				occursList.putParam("TxAmt", tTxRecord.getTxAmt());
				occursList.putParam("BrNo", tTxRecord.getBrNo());
				occursList.putParam("TlrNo", tTxRecord.getTlrNo());
				occursList.putParam("FlowType", tTxRecord.getFlowType());
				occursList.putParam("FlowStep", tTxRecord.getFlowStep());
				occursList.putParam("SupRelease", supRelease);
				/* 將每筆資料放入Tota的OcList */
				this.totaVo.addOccursList(occursList);
			}
		}

		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (slTxRecord != null && slTxRecord.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			this.totaVo.setMsgEndToEnter();// 手動折返
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}