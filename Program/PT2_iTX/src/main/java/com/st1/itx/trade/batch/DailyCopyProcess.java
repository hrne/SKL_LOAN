package com.st1.itx.trade.batch;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.service.JobMainService;
import com.st1.itx.eum.ContentName;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;

/**
 * DailyCopyProcess
 * 
 * @author ChihWei
 * @version 1.0.0
 */
@Service("DailyCopyProcess")
@Scope("prototype")
public class DailyCopyProcess extends TradeBuffer {

	@Autowired
	JobMainService sJobMainService;

	@Autowired
	WebClient webClient;

	@Autowired
	DateUtil dateUtil;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("DailyCopyProcess start...");

		int tbsdyf = this.txBuffer.getTxCom().getTbsdyf();
		String tlrNo = titaVo.getTlrNo();
		String targetEnv = titaVo.getParam(ContentName.dataBase);

		this.info("DailyCopyProcess tbsdyf = " + tbsdyf);
		this.info("DailyCopyProcess tlrNo = " + tlrNo);
		this.info("DailyCopyProcess targetEnv = " + targetEnv);

		String txSeq = titaVo.getParam("JobTxSeq");

		try {
			// 關閉全部ForeignKey
			this.info("DailyCopyProcess 關閉全部ForeignKey ...");
			sJobMainService.Usp_Cp_ForeignKeyControl_Upd(tbsdyf, tlrNo, 0, txSeq, titaVo);
			doCommit();

			// 複製資料
			sJobMainService.Usp_L9_DailyBackup_Copy(tbsdyf, tlrNo, txSeq, titaVo);
			doCommit();

			// 開啟全部ForeignKey
			this.info("DailyCopyProcess 開啟全部ForeignKey ...");
			sJobMainService.Usp_Cp_ForeignKeyControl_Upd(tbsdyf, tlrNo, 1, txSeq, titaVo);
			doCommit();
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("DailyCopyProcess error :" + errors.toString());
		}
		this.info("DailyCopyProcess Finished.");
		return null;
	}

	private void doCommit() {
		this.info("DailyCopyProcess doCommit");
		this.batchTransaction.commit();
	}
}