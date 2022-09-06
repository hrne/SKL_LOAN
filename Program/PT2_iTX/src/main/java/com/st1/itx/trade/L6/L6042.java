package com.st1.itx.trade.L6;

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
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.domain.TxDataLog;
import com.st1.itx.db.domain.TxTranCode;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.TxDataLogService;
import com.st1.itx.db.service.TxTranCodeService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

@Service("L6042")
@Scope("prototype")
/**
 * 
 * 
 * @author Eric Chang
 * @version 1.0.0
 */
public class L6042 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public TxTranCodeService txTranCodeService;
	@Autowired
	public CdEmpService cdEmpService;
	@Autowired
	public TxDataLogService txDataLogService;
	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6042 ");
		this.totaVo.init(titaVo);

		String iTranNo = titaVo.getParam("TranNo") + "%";

		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 500;

		Slice<TxTranCode> slTxTranCode = txTranCodeService.TranNoLike(iTranNo + "%", this.index, this.limit, titaVo);
		List<TxTranCode> lTxTranCode = slTxTranCode == null ? null : slTxTranCode.getContent();

		if (lTxTranCode == null) {
			throw new LogicException("E0001", "");
		} else {
			for (TxTranCode tTxTranCode : lTxTranCode) {
				OccursList occursList = new OccursList();
				occursList.putParam("OTranNo", tTxTranCode.getTranNo());
				occursList.putParam("OTranItem", tTxTranCode.getTranItem());
				occursList.putParam("ODesc", tTxTranCode.getDesc());
				occursList.putParam("OOLastUpdate", parse.timeStampToStringDate(tTxTranCode.getLastUpdate()) + " " + parse.timeStampToStringTime(tTxTranCode.getLastUpdate()));
				occursList.putParam("OOLastEmp", tTxTranCode.getLastUpdateEmpNo() + " " + empName(titaVo, tTxTranCode.getLastUpdateEmpNo()));

				// 新增：歷程查詢按鈕，如果查無資料就不顯示按鈕
				Slice<TxDataLog> slTxDataLog = null;
				try {
					slTxDataLog = txDataLogService.findByTranNo("L6402", "CODE:" + tTxTranCode.getTranNo(), 0, 1, titaVo);
				} catch (Exception e) {
					this.error("L6042 Exception when txDataLogService: " + e.getMessage());
					throw new LogicException("E0013", "txDataLogService");
				}
				List<TxDataLog> lTxDataLog = slTxDataLog == null ? null : slTxDataLog.getContent();
				if (lTxDataLog == null || lTxDataLog.isEmpty())
					occursList.putParam("OOHasL6933", "N");
				else
					occursList.putParam("OOHasL6933", "Y");

				/* 將每筆資料放入Tota的OcList */
				this.totaVo.addOccursList(occursList);
			}
		}

		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (slTxTranCode != null && slTxTranCode.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			this.totaVo.setMsgEndToEnter();// 手動折返
		}

		this.addList(this.totaVo);
		return this.sendList();
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