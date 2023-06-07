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

import com.st1.itx.Exception.DBException;
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

	private List<TxArchiveTableLog> listTxArchiveTableLog = new ArrayList<>();

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
			this.info("L6971p l6971Query.size() = " + l6971Query.size());
			for (Map<String, String> l6971Vo : l6971Query) {
				if (count != 0 && count % 10000 == 0) {
					// 2023-06-07 Wei 每10000筆更新一次
					updateAllTxArchiveTableLog(titaVo);
				}
				int execDate = parse.stringToInteger(l6971Vo.get("ExecuteDate"));
				int batchNo = parse.stringToInteger(l6971Vo.get("BatchNo"));
				int custNo = parse.stringToInteger(l6971Vo.get("CustNo"));
				int facmNo = parse.stringToInteger(l6971Vo.get("FacmNo"));
				int bormNo = parse.stringToInteger(l6971Vo.get("BormNo"));
				doExecution5YTX(execDate, batchNo, custNo, facmNo, bormNo, titaVo);
				count++;
			}
			// 2023-06-05 Wei 最後一次更新
			updateAllTxArchiveTableLog(titaVo);
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
		Slice<LoanBorTx> sLoanBorTxOnline = loanBorTxService.borxBormNoEq(custNo, facmNo, bormNo, 0, Integer.MAX_VALUE,
				0, Integer.MAX_VALUE, titaVo);
		List<LoanBorTx> lLoanBorTx = sLoanBorTxOnline.getContent();
		doDelete5YTX(lLoanBorTx, titaVo);
		setIsDeletedToTrue("5YTX", "LoanBorTx", execDate, batchNo, custNo, facmNo, bormNo, titaVo);
	}

	private void doDelete5YTX(List<LoanBorTx> lLoanBorTx, TitaVo titaVo) throws LogicException {
		// 刪除確認可刪除的明細 for 5YTX
		try {
			loanBorTxService.deleteAll(lLoanBorTx, titaVo);
		} catch (Exception e) {
			this.error("L6971.doDelete5YTX failed to delete.");
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(errors.toString());
			throwException("E0008", "從 LoanBorTx 刪除時失敗", titaVo);
		}
	}

	private void setIsDeletedToTrue(String type, String tableName, int execDate, int batchNo, int custNo, int facmNo,
			int bormNo, TitaVo titaVo) throws LogicException {
		// 完成刪除後，在 TxArchiveTableLog 將對應的資料 IsDeleted 設為 1
		// 並且更新該筆資料內容 2023-06-05 Wei 改為存list,全部刪除完以後一次更新
		this.info("L6971.setIsDeletedToTrue deleteAll successful. Set isDeleted to 1.");
		try {
			Slice<TxArchiveTableLog> slice = txArchiveTableLogService.findL6971(type, tableName, execDate, batchNo,
					custNo, facmNo, bormNo, 0, 0, Integer.MAX_VALUE, titaVo);
			List<TxArchiveTableLog> list = slice.getContent();
			if (!list.isEmpty()) {
				for (TxArchiveTableLog record : list) {
					record.setIsDeleted(1);
					record.setDescription("此次封存明細已從連線環境刪除");
					record.setLastUpdate(new Timestamp(System.currentTimeMillis()));
					record.setLastUpdateEmpNo(titaVo.getTlrNo());
				}
				listTxArchiveTableLog.addAll(list);
			}
		} catch (Exception e) {
			this.error("L6971.setIsDeletedToTrue failed to update IsDeleted.");
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(errors.toString());
			throwException("E0007", "更新 TxArchiveTableLog.IsDeleted 失敗", titaVo);
		}
	}

	private void updateAllTxArchiveTableLog(TitaVo titaVo) throws LogicException {
		if (listTxArchiveTableLog.isEmpty()) {
			// 不得為空
			this.info("updateAllTxArchiveTableLog listTxArchiveTableLog為空,不更新.");
			return;
		}
		this.info("L6971 commit");
		this.batchTransaction.commit();
		this.info("updateAllTxArchiveTableLog listTxArchiveTableLog.size() = " + listTxArchiveTableLog.size());
		try {
			txArchiveTableLogService.updateAll(listTxArchiveTableLog, titaVo);
		} catch (DBException e) {
			this.error("L6971.updateAllTxArchiveTableLog failed to update IsDeleted.");
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(errors.toString());
			throw new LogicException("E0007", "更新 TxArchiveTableLog.IsDeleted 失敗");
		}
		// 更新完清空
		listTxArchiveTableLog = new ArrayList<>();
	}

	private void throwException(String errorCode, String errorMsg, TitaVo titaVo) throws LogicException {
		// 2023-06-05 Error前需先更新一次
		updateAllTxArchiveTableLog(titaVo);
		throw new LogicException(errorCode, errorMsg);
	}
}
