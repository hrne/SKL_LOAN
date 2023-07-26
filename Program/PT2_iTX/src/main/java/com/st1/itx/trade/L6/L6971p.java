package com.st1.itx.trade.L6;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.service.LoanBorTxService;
import com.st1.itx.db.service.TxArchiveTableLogService;
import com.st1.itx.db.service.TxArchiveTableService;
import com.st1.itx.db.service.springjpa.cm.L6971ServiceImpl;
import com.st1.itx.db.service.springjpa.cm.L9729ServiceImpl.WorkType;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.MySpring;
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

		if (titaVo.containsKey("InputType")) {
			workType = WorkType.getWorkTypeByHelp(titaVo.getParam("InputType"));
		} else {
			workType = WorkType.FiveYearsTX;
		}

		// 2023-06-07 Wei 從資料庫整批刪除
		int deleteCount = 0;

		try {
			deleteCount = l6971ServiceImpl.deleteWhenMatched(workType, titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("l6971ServiceImpl.deleteWhenMatched error = " + errors.toString());
			throw new LogicException("E0008", "從 LoanBorTx 刪除時失敗");
		}

		if (deleteCount != 0) {
			this.batchTransaction.commit();
			int updateCount = 0;
			try {
				updateCount = l6971ServiceImpl.updateIsDeleted(workType, titaVo);
			} catch (Exception e) {
				this.error("l6971ServiceImpl updateIsDeleted failed to update IsDeleted.");
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				this.error(errors.toString());
				throw new LogicException("E0007", "更新 TxArchiveTableLog.IsDeleted 失敗");
			}
			webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "", titaVo.getTlrNo(),
					"L6971 整批刪除，共" + updateCount + "筆，已完成", titaVo);
			// 2023-06-07 Wei 整批刪除完成時，產生報表
			titaVo.putParam("InputType", 1);
			titaVo.putParam("InputDate", titaVo.getEntDyI());

			MySpring.newTask("L9729p", this.txBuffer, titaVo);
		} else {
			webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "", titaVo.getTlrNo(),
					"L6971 整批刪除，查無資料", titaVo);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}
