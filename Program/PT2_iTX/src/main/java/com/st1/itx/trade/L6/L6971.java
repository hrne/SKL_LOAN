package com.st1.itx.trade.L6;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.LoanBorTx;
import com.st1.itx.db.domain.TxArchiveTableLog;
import com.st1.itx.db.domain.TxArchiveTableLogId;
import com.st1.itx.db.service.LoanBorTxService;
import com.st1.itx.db.service.TxArchiveTableLogService;
import com.st1.itx.db.service.springjpa.cm.L6971ServiceImpl;
import com.st1.itx.db.service.springjpa.cm.L9729ServiceImpl.WorkType;
import com.st1.itx.eum.ContentName;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/**
 * 封存明細搬運結果查詢
 * 
 * @author xiangwei
 * @version 1.0.0
 */
@Service("L6971")
@Scope("prototype")
public class L6971 extends TradeBuffer {

	@Autowired
	Parse parse;

	@Autowired
	L6971ServiceImpl l6971ServiceImpl;

	@Autowired
	LoanBorTxService loanBorTxService;

	@Autowired
	TxArchiveTableLogService txArchiveTableLogService;

	WorkType workType;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6971 ");
		this.totaVo.init(titaVo);

		if (!ContentName.onLine.equals(titaVo.getDataBase())) {
			throw new LogicException("E0008", "L6971 只允許在連線環境執行!!");
		}

		workType = WorkType.getWorkTypeByHelp(titaVo.getParam("InputType"));

		// 如果OOCustNo有值, 表示是要進行刪除
		// 如果沒有值, 表示是查詢
		if (titaVo.containsKey("OOCustNo")) {
			doExecution(titaVo);
		} else {
			doInquiry(titaVo);
		}

		this.info("L6971 exit.");

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void doInquiry(TitaVo titaVo) throws LogicException {
		this.info("L6971 doInquiry ... ");

		List<Map<String, String>> l6971Query = null;

		try {
			l6971Query = l6971ServiceImpl.findAll(workType, titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("l6971ServiceImpl.findAll error = " + errors.toString());
		}

		if (l6971Query != null && !l6971Query.isEmpty()) {

			for (Map<String, String> l6971Vo : l6971Query) {
				OccursList occursList = new OccursList();
				occursList.putParam("OOExecDate", parse.stringToInteger(l6971Vo.get("ExecuteDate")) - 19110000);
				occursList.putParam("OOBatchNo", l6971Vo.get("BatchNo"));
				occursList.putParam("OOCustNo", l6971Vo.get("CustNo"));
				occursList.putParam("OOFacmNo", l6971Vo.get("FacmNo"));
				occursList.putParam("OOBormNo", l6971Vo.get("BormNo"));
				this.totaVo.addOccursList(occursList);
			}
		} else {
			// 查無資料
			throw new LogicException("E0001", "查無符合條件的待刪除資料！");
		}
	}

	private void doExecution(TitaVo titaVo) throws LogicException {
		this.info("L6971 doExecution ... ");

		int execDate = parse.stringToInteger(titaVo.get("OOExecDate")) + 19110000;
		int batchNo = parse.stringToInteger(titaVo.get("OOBatchNo"));
		int custNo = parse.stringToInteger(titaVo.get("OOCustNo"));
		int facmNo = parse.stringToInteger(titaVo.get("OOFacmNo"));
		int bormNo = parse.stringToInteger(titaVo.get("OOBormNo"));

		this.info("execDate: " + execDate);
		this.info("batchNo: " + batchNo);
		this.info("custNo: " + custNo);
		this.info("facmNo: " + facmNo);
		this.info("bormNo: " + bormNo);

		if (workType == WorkType.FiveYearsTX) {
			doExecution5YTX(execDate, batchNo, custNo, facmNo, bormNo, titaVo);
		}
	}

	private void doExecution5YTX(int execDate, int batchNo, int custNo, int facmNo, int bormNo, TitaVo titaVo)
			throws LogicException {
		this.info("L6971.doExecution5YTX begins. ");
		this.info("CustNo: " + custNo);
		this.info("FacmNo: " + facmNo);
		this.info("BormNo: " + bormNo);
		
		if (isHistoryAndOnlineSameCount5YTX(custNo, facmNo, bormNo, titaVo))
		{
			int deletedRecords = doDelete5YTX(custNo, facmNo, bormNo, titaVo);
			setIsDeletedToTrue(deletedRecords, "5YTX", "LoanBorTx", execDate, batchNo, custNo, facmNo, bormNo, titaVo);
		} else
		{
			this.error("DATA has different count in ONLINE and HISTORY!");
			throw new LogicException("E0008", "此 PK 在歷史環境與連線環境資料筆數不同");
			
		}
	}
	
	private Boolean isHistoryAndOnlineSameCount5YTX(int custNo, int facmNo, int bormNo, TitaVo titaVo) {
		
		//
		// 檢查此明細 key 在 history 及 online 是否資料數量相符 for 5YTX
		// 應該可以 generalize?
		//
		
		Slice<LoanBorTx> sLoanBorTxOnline = loanBorTxService.borxBormNoEq(custNo, facmNo, bormNo, 0, Integer.MAX_VALUE, 0,
				Integer.MAX_VALUE, titaVo);
		
		TitaVo titaVoHistory = (TitaVo) titaVo.clone();
		titaVoHistory.setDataBaseOnHist();
		
		Slice<LoanBorTx> sLoanBorTxHistory = loanBorTxService.borxBormNoEq(custNo, facmNo, bormNo, 0, Integer.MAX_VALUE, 0,
				Integer.MAX_VALUE, titaVoHistory);
		
		return sLoanBorTxOnline.getNumberOfElements() == sLoanBorTxHistory.getNumberOfElements();
	}

	private int doDelete5YTX(int custNo, int facmNo, int bormNo, TitaVo titaVo) throws LogicException {
		
		//
		// 刪除確認可刪除的明細 for 5YTX
		//
		
		Slice<LoanBorTx> sLoanBorTx = loanBorTxService.borxBormNoEq(custNo, facmNo, bormNo, 0, Integer.MAX_VALUE, 0,
				Integer.MAX_VALUE, titaVo);
		List<LoanBorTx> lLoanBorTx = sLoanBorTx.getContent();

		try {
			loanBorTxService.deleteAll(lLoanBorTx, titaVo);
			return lLoanBorTx.size();
		} catch (Exception e) {
			this.error("L6971.doDelete5YTX failed to delete.");
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(errors.toString());
			throw new LogicException("E0008", "從 LoanBorTx 刪除時失敗");
		}
	}

	private void setIsDeletedToTrue(int deletedRecords, String type, String tableName, int execDate, int batchNo, int custNo, int facmNo,
			int bormNo, TitaVo titaVo) throws LogicException {
		
		//
		// 完成刪除後，在 TxArchiveTableLog 將對應的資料 IsDeleted 設為 1
		// 並且更新該筆資料內容
		//
		
		this.info("L6971.setIsDeletedToTrue deleteAll successful. Set isDeleted to 1.");
		
		try {
			TxArchiveTableLogId txArchiveTableLogId = new TxArchiveTableLogId();
			txArchiveTableLogId.setType(type);
			txArchiveTableLogId.setTableName(tableName);
			txArchiveTableLogId.setExecuteDate(execDate);
			txArchiveTableLogId.setDataFrom("ONLINE");
			txArchiveTableLogId.setDataTo("HISTORY");
			txArchiveTableLogId.setBatchNo(batchNo);
			txArchiveTableLogId.setCustNo(custNo);
			txArchiveTableLogId.setFacmNo(facmNo);
			txArchiveTableLogId.setBormNo(bormNo);

			TxArchiveTableLog record = txArchiveTableLogService.holdById(txArchiveTableLogId, titaVo);
			
			// deletedRecords 和 TxArchiveTableLog.Records 不符時 rollback
			if (record.getResult() != deletedRecords)
			{
				this.error("L6971.setIsDeletedToTrue: different count between deleted records and TxArchiveTableLog.Records! rollback");
				throw new LogicException("E0007", "實際刪除資料數與 TxArchiveTableLog 紀錄的數量不符");
			}
			
			record.setIsDeleted(1);
			record.setDescription("此次封存明細已從連線環境刪除");
			record.setLastUpdate(new Timestamp(System.currentTimeMillis()));
			record.setLastUpdateEmpNo(titaVo.getTlrNo());
			txArchiveTableLogService.update(record, titaVo);
		} catch (Exception e) {
			this.error("L6971.setIsDeletedToTrue failed to update IsDeleted.");
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(errors.toString());
			throw new LogicException("E0007", "更新 TxArchiveTableLog.IsDeleted 失敗");
		}
	}
}