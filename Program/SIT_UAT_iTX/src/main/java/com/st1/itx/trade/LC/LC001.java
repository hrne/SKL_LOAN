package com.st1.itx.trade.LC;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.db.domain.TxFlow;
import com.st1.itx.db.domain.TxFlowId;
import com.st1.itx.db.domain.TxRecord;
import com.st1.itx.db.service.TxFlowService;
import com.st1.itx.db.service.TxRecordService;
import com.st1.itx.db.service.springjpa.cm.LC001ServiceImpl;

@Service("LC001")
@Scope("prototype")
/**
 * 訂正查詢
 * 
 * @author eric chang
 * @version 1.0.0
 */
public class LC001 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(LC001.class);

	/* DB服務注入 */
	@Autowired
	public TxRecordService txRecordService;

	@Autowired
	public TxFlowService txFlowService;

	@Autowired
	LC001ServiceImpl lc001ServiceImpl;

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active LC001 ");
		this.totaVo.init(titaVo);

//		int iEntday = Integer.valueOf(titaVo.get("iEntdy").trim()) + 19110000;
//		String iBrNo = titaVo.get("iBrNo").trim();
//		String iTlrNo = titaVo.get("iTlrNo").trim();
//		String iTranNo = titaVo.get("iTranNo").trim();
//		int supRelease = 0;
//		this.info("LC001 parm = " + iEntday + "/" + iBrNo + "/" + iTlrNo + "/" + iTranNo);
//
//		/*
//		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
//		 */
//		this.index = titaVo.getReturnIndex();
//
//		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
//		this.limit = 500;
//
//		Slice<TxRecord> slTxRecord = txRecordService.findByLC001(iEntday, iBrNo, "S", 1, 0, 1, iTlrNo + "%", iTranNo + "%", this.index, this.limit);
//		List<TxRecord> lTxRecord = slTxRecord == null ? null : slTxRecord.getContent();
//
//		if (lTxRecord == null) {
//			throw new LogicException(titaVo, "E0001", "訂正資料");
//		} else {
//			this.info("LC001 size = " + lTxRecord.size());
//			for (TxRecord tTxRecord : lTxRecord) {
//				this.info("LC001 TxRecord = " + tTxRecord.getTranNo() + "/" + tTxRecord.getCanCancel() + "/" + tTxRecord.getTxResult());
//				// 兩段式以上的登錄交易==>主管已放行，不顯示<修正>按鈕
//				supRelease = 0;
//				if (tTxRecord.getFlowType() > 1 && tTxRecord.getFlowStep() == 1) {
//					TxFlowId tTxFlowId = new TxFlowId();
//					tTxFlowId.setEntdy(tTxRecord.getEntdy());
//					tTxFlowId.setFlowNo(tTxRecord.getFlowNo());
//					TxFlow tTxFlow = txFlowService.findById(tTxFlowId);
//					if (tTxFlow != null) {
////						if (tTxFlow.getFlowMode() == 0) {
////							supRelease = 1; // 1的時候 按鈕不開
////						}
//						if (tTxRecord.getFlowStep() != tTxFlow.getFlowStep() || (tTxRecord.getFlowStep() == 1 && tTxFlow.getSubmitFg() == 1 && tTxFlow.getFlowMode() != 3)) {
//							supRelease = 1; // 1的時候 按鈕不開
//						}
//					}
//				}
//				OccursList occursList = new OccursList();
//				occursList.putParam("CalDate", tTxRecord.getCalDate());
//				occursList.putParam("CalTime", tTxRecord.getCalTime());
//				occursList.putParam("Entdy", tTxRecord.getEntdy());
//				occursList.putParam("TxNo", tTxRecord.getTxNo());
//				occursList.putParam("TranNo", tTxRecord.getTranNo());
//				occursList.putParam("MrKey", tTxRecord.getMrKey());
//				occursList.putParam("CurName", tTxRecord.getCurName());
//				occursList.putParam("TxAmt", tTxRecord.getTxAmt());
//				occursList.putParam("BrNo", tTxRecord.getBrNo());
//				occursList.putParam("TlrNo", tTxRecord.getTlrNo());
//				occursList.putParam("FlowType", tTxRecord.getFlowType());
//				occursList.putParam("FlowStep", tTxRecord.getFlowStep());
//				occursList.putParam("SupRelease", supRelease);
//				/* 將每筆資料放入Tota的OcList */
//				this.totaVo.addOccursList(occursList);
//			}
//		}
//
//		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
//		if (slTxRecord != null && slTxRecord.hasNext()) {
//			titaVo.setReturnIndex(this.setIndexNext());
//			this.totaVo.setMsgEndToEnter();// 手動折返
//		}
//	    
//		try {
//
//			List<Map<String, String>> lc001List = lc001ServiceImpl.findAll(titaVo);			
//
//			if (lc001List != null && lc001List.size() != 0) {
//				int cnt = 0;
//				for (Map<String, String> lc001Vo : lc001List) {
//					String flds = "";
//					for (int i = 0; i < lc001Vo.size(); i++) {
//						String fname = "F" + String.valueOf(i);
//						flds+=lc001Vo.get(fname)+",";
//					}
//					this.info("LC001 count = " + ++cnt + "/" + flds);
//				}
//
//			} 
//			
//		} catch (Exception e) {
//			this.error("C001ServiceImpl error");
//		}

		toRun(titaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void toRun(TitaVo titaVo) throws LogicException {
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 500;
		
		try {

			List<Map<String, String>> lc001List = lc001ServiceImpl.findAll(titaVo,index,limit);

			if (lc001List != null && lc001List.size() != 0) {
				int cnt = 0;
				for (Map<String, String> lc001Vo : lc001List) {
					String flds = "";
					for (int i = 0; i < lc001Vo.size(); i++) {
						String fname = "F" + String.valueOf(i);
						flds+=lc001Vo.get(fname)+",";
					}
					this.info("LC001 count = " + ++cnt + "/" + flds);
					
					int daCalDate = X2N(lc001Vo.get("F0")) - 19110000; // sql = "SELECT A.\"CalDate\"";
					String daCalTime = lc001Vo.get("F1"); // sql += ",A.\"CalTime\"";
					int daEntdy = X2N(lc001Vo.get("F2")) - 19110000; // sql += ",A.\"Entdy\"";
					String daTxNo = lc001Vo.get("F3"); // sql += ",A.\"TxNo\"";
					String daTranX = lc001Vo.get("F4"); // sql += ",A.\"TranNo\"||' '||B.\"TranItem\" TranX";
					String daMrKey = lc001Vo.get("F5"); // sql += ",A.\"MrKey\"";
					String daCurName = lc001Vo.get("F6"); // sql += ",A.\"CurName\"";
					String daTxAmt = lc001Vo.get("F7"); // sql += ",A.\"TxAmt\"";
					String daBrX = lc001Vo.get("F8"); // sql += ",A.\"BrNo\"||' '||C.\"BranchShort\" BrX";
					String daTlrX = lc001Vo.get("F9"); // sql += ",A.\"TlrNo\"||' '||D.\"TlrItem\" TlrX";
					int daFlowType = X2N(lc001Vo.get("F10")); // sql += ",A.\"FlowType\"";
					int daFlowStep = X2N(lc001Vo.get("F11")); // sql += ",A.\"FlowStep\"";
					int daFlowStep2 = X2N(lc001Vo.get("F12")); // sql += ",E.\"FlowStep\" flowFlowStep";
					int daSubmitFg = X2N(lc001Vo.get("F13")); // sql += ",E.\"SubmitFg\"";
					int daFlowMode = X2N(lc001Vo.get("F14")); // sql += ",E.\"FlowMode\"";

					// 兩段式以上的登錄交易==>主管已放行，不顯示<修正>按鈕
					int supRelease = 0;
					if (daFlowType > 1 && daFlowStep == 1) {
						if (daFlowStep != daFlowStep2 || (daFlowStep == 1 && daSubmitFg == 1 && daFlowMode != 3)) {
							supRelease = 1; // 1的時候 按鈕不開
						}

					}
					OccursList occursList = new OccursList();
					occursList.putParam("CalDate", daCalDate);
					occursList.putParam("CalTime", daCalTime);
					occursList.putParam("Entdy", daEntdy);
					occursList.putParam("TxNo", daTxNo);
					occursList.putParam("TranNo", daTranX);
					occursList.putParam("MrKey", daMrKey);
					occursList.putParam("CurName", daCurName);
					occursList.putParam("TxAmt", daTxAmt);
					occursList.putParam("BrNo", daBrX);
					occursList.putParam("TlrNo", daTlrX);
					occursList.putParam("FlowType", daFlowType);
					occursList.putParam("FlowStep", daFlowStep);
					occursList.putParam("SupRelease", supRelease);
					/* 將每筆資料放入Tota的OcList */
					this.totaVo.addOccursList(occursList);

				}

			}

			/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
			if (lc001List != null && lc001List.size()>=this.limit) {
				titaVo.setReturnIndex(this.setIndexNext());
				this.totaVo.setMsgEndToEnter();// 手動折返
			}
		} catch (Exception e) {
			this.error("C001ServiceImpl error");
		}
		
	}
	
	private int X2N(String x) {
		int r = 0;
		if (!"".equals(x.trim())) {
			r = Integer.valueOf(x);
		}
		return r;
	}

	private void toRun2(TitaVo titaVo) throws LogicException {
		int iEntday = Integer.valueOf(titaVo.get("iEntdy").trim()) + 19110000;
		String iBrNo = titaVo.get("iBrNo").trim();
		String iTlrNo = titaVo.get("iTlrNo").trim();
		String iTranNo = titaVo.get("iTranNo").trim();
//		int supRelease = 0;
		this.info("LC001 parm = " + iEntday + "/" + iBrNo + "/" + iTlrNo + "/" + iTranNo);

		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 500;

		Slice<TxRecord> slTxRecord = txRecordService.findByLC001(iEntday, iBrNo, "S", 1, 0, 1, iTlrNo + "%", iTranNo + "%", this.index, this.limit);
		List<TxRecord> lTxRecord = slTxRecord == null ? null : slTxRecord.getContent();

		if (lTxRecord == null) {
			throw new LogicException(titaVo, "E0001", "訂正資料");
		} else {
			this.info("LC001 size = " + lTxRecord.size());
			for (TxRecord tTxRecord : lTxRecord) {
				this.info("LC001 TxRecord = " + tTxRecord.getTranNo() + "/" + tTxRecord.getCanCancel() + "/" + tTxRecord.getTxResult());
				// 兩段式以上的登錄交易==>主管已放行，不顯示<修正>按鈕
				int supRelease = 0;
				if (tTxRecord.getFlowType() > 1 && tTxRecord.getFlowStep() == 1) {
					TxFlowId tTxFlowId = new TxFlowId();
					tTxFlowId.setEntdy(tTxRecord.getEntdy());
					tTxFlowId.setFlowNo(tTxRecord.getFlowNo());
					TxFlow tTxFlow = txFlowService.findById(tTxFlowId);
					if (tTxFlow != null) {
//						if (tTxFlow.getFlowMode() == 0) {
//							supRelease = 1; // 1的時候 按鈕不開
//						}
						if (tTxRecord.getFlowStep() != tTxFlow.getFlowStep() || (tTxRecord.getFlowStep() == 1 && tTxFlow.getSubmitFg() == 1 && tTxFlow.getFlowMode() != 3)) {
							supRelease = 1; // 1的時候 按鈕不開
						}
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

	}
}