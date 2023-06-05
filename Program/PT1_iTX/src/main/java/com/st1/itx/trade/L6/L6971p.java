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
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.LoanBorTx;
import com.st1.itx.db.domain.TxArchiveTableLog;
import com.st1.itx.db.service.LoanBorTxService;
import com.st1.itx.db.service.TxArchiveTableLogService;
import com.st1.itx.db.service.TxArchiveTableService;
import com.st1.itx.db.service.springjpa.cm.L6971ServiceImpl;
import com.st1.itx.db.service.springjpa.cm.L9729ServiceImpl.WorkType;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;

/**
 * 整批刪檔
 * 
 * @author Wei
 * @version 1.0.0
 */
@Service("L6971p")
@Scope("prototype")
public class L6971p extends TradeBuffer {

	@Autowired
	Parse parse;

	@Autowired
	TxArchiveTableService txArchiveTableService;

	@Autowired
	TxArchiveTableLogService txArchiveTableLogService;

	@Autowired
	LoanBorTxService loanBorTxService;

	@Autowired
	L6971ServiceImpl l6971ServiceImpl;

	@Autowired
	WebClient webClient;

	@Autowired
	DateUtil dateUtil;

	WorkType workType;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6971p ");
		this.totaVo.init(titaVo);

		List<Map<String, String>> l6971Query = null;

		workType = WorkType.getWorkTypeByHelp(titaVo.getParam("InputType"));

		try {
			l6971Query = l6971ServiceImpl.findAll(workType, 0, Integer.MAX_VALUE, titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("l6971ServiceImpl.findAll error = " + errors.toString());
		}
		int count = 0;
		if (l6971Query != null && !l6971Query.isEmpty()) {
			for (Map<String, String> l6971Vo : l6971Query) {
				int execDate = parse.stringToInteger(l6971Vo.get("ExecuteDate"));
				int batchNo = parse.stringToInteger(l6971Vo.get("BatchNo"));
				int custNo = parse.stringToInteger(l6971Vo.get("CustNo"));
				int facmNo = parse.stringToInteger(l6971Vo.get("FacmNo"));
				int bormNo = parse.stringToInteger(l6971Vo.get("BormNo"));
				doExecution5YTX(execDate, batchNo, custNo, facmNo, bormNo, titaVo);
				count++;
				this.info("L6971 doExecution5YTX commit");
				this.batchTransaction.commit();
				setIsDeletedToTrue("5YTX", "LoanBorTx", execDate, batchNo, custNo, facmNo, bormNo, titaVo);
			}
			for (Map<String, String> l6971Vo : l6971Query) {
				int execDate = parse.stringToInteger(l6971Vo.get("ExecuteDate"));
				int batchNo = parse.stringToInteger(l6971Vo.get("BatchNo"));
				int custNo = parse.stringToInteger(l6971Vo.get("CustNo"));
				int facmNo = parse.stringToInteger(l6971Vo.get("FacmNo"));
				int bormNo = parse.stringToInteger(l6971Vo.get("BormNo"));
				setIsDeletedToTrue("5YTX", "LoanBorTx", execDate, batchNo, custNo, facmNo, bormNo, titaVo);
				this.info("L6971 setIsDeletedToTrue commit");
				this.batchTransaction.commit();
			}
			webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "", titaVo.getTlrNo(),
					"L6971 整批刪除，共" + count + "筆，已完成", titaVo);
		} else {
			webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "", titaVo.getTlrNo(),
					"L6971 整批刪除，查無資料", titaVo);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void doExecution5YTX(int execDate, int batchNo, int custNo, int facmNo, int bormNo, TitaVo titaVo)
			throws LogicException {
		this.info("L6971.doExecution5YTX begins. ");
		this.info("CustNo: " + custNo);
		this.info("FacmNo: " + facmNo);
		this.info("BormNo: " + bormNo);

		if (isHistoryAndOnlineSameCount5YTX(custNo, facmNo, bormNo, titaVo)) {
			doDelete5YTX(custNo, facmNo, bormNo, titaVo);
		} else {
			this.error("DATA has different count in ONLINE and HISTORY!");
			throw new LogicException("E0008", "此 PK 在歷史環境與連線環境資料筆數不同");

		}
	}

	private void doDelete5YTX(int custNo, int facmNo, int bormNo, TitaVo titaVo) throws LogicException {

		//
		// 刪除確認可刪除的明細 for 5YTX
		//

		Slice<LoanBorTx> sLoanBorTx = loanBorTxService.borxBormNoEq(custNo, facmNo, bormNo, 0, Integer.MAX_VALUE, 0,
				Integer.MAX_VALUE, titaVo);
		List<LoanBorTx> lLoanBorTx = sLoanBorTx.getContent();

		try {
			loanBorTxService.deleteAll(lLoanBorTx, titaVo);
		} catch (Exception e) {
			this.error("L6971.doDelete5YTX failed to delete.");
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(errors.toString());
			throw new LogicException("E0008", "從 LoanBorTx 刪除時失敗");
		}
	}

	private Boolean isHistoryAndOnlineSameCount5YTX(int custNo, int facmNo, int bormNo, TitaVo titaVo) {

		//
		// 檢查此明細 key 在 history 及 online 是否資料數量相符 for 5YTX
		// 應該可以 generalize?
		//

		Slice<LoanBorTx> sLoanBorTxOnline = loanBorTxService.borxBormNoEq(custNo, facmNo, bormNo, 0, Integer.MAX_VALUE,
				0, Integer.MAX_VALUE, titaVo);

		TitaVo titaVoHistory = (TitaVo) titaVo.clone();
//		titaVoHistory.setDataBaseOnHist();
		// 2023-05-25 Wei for DEV測試用
		titaVoHistory.setDataBaseOnDay();

		Slice<LoanBorTx> sLoanBorTxHistory = loanBorTxService.borxBormNoEq(custNo, facmNo, bormNo, 0, Integer.MAX_VALUE,
				0, Integer.MAX_VALUE, titaVoHistory);

		return sLoanBorTxOnline.getNumberOfElements() == sLoanBorTxHistory.getNumberOfElements();
	}

	private void setIsDeletedToTrue(String type, String tableName, int execDate, int batchNo, int custNo, int facmNo,
			int bormNo, TitaVo titaVo) throws LogicException {

		//
		// 完成刪除後，在 TxArchiveTableLog 將對應的資料 IsDeleted 設為 1
		// 並且更新該筆資料內容
		//

		this.info("L6971.setIsDeletedToTrue deleteAll successful. Set isDeleted to 1.");

		try {
			Slice<TxArchiveTableLog> recordList = txArchiveTableLogService.findL6971(type, tableName, execDate, batchNo,
					custNo, facmNo, bormNo, 0, 0, Integer.MAX_VALUE, titaVo);
			if (recordList != null && !recordList.isEmpty()) {
				for (TxArchiveTableLog record : recordList) {
					record.setIsDeleted(1);
					record.setDescription("此次封存明細已從連線環境刪除");
					record.setLastUpdate(new Timestamp(System.currentTimeMillis()));
					record.setLastUpdateEmpNo(titaVo.getTlrNo());
					txArchiveTableLogService.update(record, titaVo);
				}
			} else {
				this.error(
						"L6971.setIsDeletedToTrue: different count between deleted records and TxArchiveTableLog.Records! rollback");
				throw new LogicException("E0007", "實際刪除資料數與 TxArchiveTableLog 紀錄的數量不符");
			}
		} catch (Exception e) {
			this.error("L6971.setIsDeletedToTrue failed to update IsDeleted.");
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(errors.toString());
			throw new LogicException("E0007", "更新 TxArchiveTableLog.IsDeleted 失敗");
		}
	}
}
