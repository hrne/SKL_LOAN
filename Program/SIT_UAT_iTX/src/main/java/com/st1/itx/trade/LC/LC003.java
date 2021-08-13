package com.st1.itx.trade.LC;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.st1.itx.db.service.TxFlowService;
import com.st1.itx.db.domain.TxRecord;
import com.st1.itx.db.domain.TxRecordId;
import com.st1.itx.db.service.TxRecordService;

@Service("LC003")
@Scope("prototype")
/**
 * 放行查詢
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public class LC003 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(LC003.class);

	/* DB服務注入 */
	@Autowired
	public TxFlowService txFlowService;

	@Autowired
	public TxRecordService txRecordService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active LC003 ");
		this.totaVo.init(titaVo);

		int iEntday = Integer.valueOf(titaVo.get("iEntdy").trim()) + 19110000;
		String iBrNo = titaVo.get("iBrNo").trim();
		String iGroupNo = titaVo.get("iGroupNo").trim();
		String iTranNo = titaVo.get("iTranNo").trim();

		this.info("LC003 iBrNo = " + iBrNo);
		this.info("LC003 iGroupNo = " + iGroupNo);
		this.info("LC003 iTranNo = " + iTranNo);

		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 500;

		List<String> groupNoList = new ArrayList<String>();
		
		groupNoList.add(this.txBuffer.getTxCom().getTlrDept());
		
		Slice<TxFlow> slTxFlow = txFlowService.findByLC003(iEntday, iBrNo, 1, iTranNo + "%", groupNoList, this.index, this.limit);
		List<TxFlow> lTxFlow = slTxFlow == null ? null : slTxFlow.getContent();

		if (lTxFlow == null) {
			throw new LogicException(titaVo, "E0001", "放行資料");
		} else {
			for (TxFlow tTxFlow : lTxFlow) {
//				if (!"".equals(iTranNo) && !iTranNo.equals(tTxFlow.getTranNo())) {
//					continue;
//				}

				TxRecordId tTxRecordId = new TxRecordId();
				tTxRecordId.setEntdy(iEntday);
				if (tTxFlow.getFlowStep() == 1) {
					tTxRecordId.setTxNo(tTxFlow.getTxNo1());

				} else if (tTxFlow.getFlowStep() == 3) {
					tTxRecordId.setTxNo(tTxFlow.getTxNo3());

				} else {
					continue;
				}

				TxRecord tTxRecord = txRecordService.findById(tTxRecordId);

				if (tTxRecord == null) {
					continue;
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

				occursList.putParam("FlowType", tTxFlow.getFlowType());
				occursList.putParam("FlowStep", tTxFlow.getFlowStep());
				/* 將每筆資料放入Tota的OcList */
				this.totaVo.addOccursList(occursList);
			}
		}

		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (slTxFlow != null && slTxFlow.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			this.totaVo.setMsgEndToEnter();// 手動折返
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}