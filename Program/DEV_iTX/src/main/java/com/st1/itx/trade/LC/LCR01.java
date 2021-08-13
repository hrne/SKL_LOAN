package com.st1.itx.trade.LC;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;

import com.st1.itx.db.domain.TxRecord;
import com.st1.itx.db.domain.TxRecordId;
import com.st1.itx.db.service.TxRecordService;

import com.st1.itx.db.domain.TxFlow;
import com.st1.itx.db.domain.TxFlowId;
import com.st1.itx.db.service.TxFlowService;

@Service("LCR01")
@Scope("prototype")
/**
 * 訂正RIM
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public class LCR01 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(LCR01.class);

	@Autowired
	public TxRecordService txRecordService;

	@Autowired
	public TxFlowService txFlowService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active LCR01 ");
		this.totaVo.init(titaVo);

		int entday = Integer.valueOf(titaVo.get("Entdy").trim()) + 19110000;
		String txno = titaVo.get("TxNo").trim();

		this.info("TxRecord key = " + entday + "/" + txno);

		TxRecordId tTxRecordId = new TxRecordId();

		tTxRecordId.setEntdy(entday);
		tTxRecordId.setTxNo(txno);

		TxRecord tTxRecord = txRecordService.findById(tTxRecordId);

		if (tTxRecord == null) {
			throw new LogicException(titaVo, "EC001", "帳務日:" + entday + ",交易序號:" + txno);
		} else {

			if (tTxRecord.getActionFg() == 1) {
				throw new LogicException(titaVo, "EC004", "帳務日:" + entday + ",交易序號:" + txno + "已訂正");
			} else if (tTxRecord.getActionFg() == 2) {
				throw new LogicException(titaVo, "EC004", "帳務日:" + entday + ",交易序號:" + txno + "已修正");
			}

			if (tTxRecord.getFlowType() > 1) {
				checkTxFlow(titaVo, tTxRecord.getEntdy(), tTxRecord.getFlowNo(), tTxRecord.getFlowStep());
			}
			try {
				TitaVo tita2 = titaVo.getVo(tTxRecord.getTranData());
				tita2.put("OrgEntdy", String.valueOf(tTxRecord.getEntdy()));
				tita2.put("ORGKIN", tTxRecord.getBrNo());
				tita2.put("ORGTLR", tTxRecord.getTlrNo());
				tita2.put("ORGTNO", tTxRecord.getTxSeq());
				tita2.put("HCODE", "1");
				totaVo.setEcTitaVo(tita2);
			} catch (Throwable e) {
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				this.error(errors.toString());
				throw new LogicException(titaVo, "EC002", "電文資料格式有誤");
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void checkTxFlow(TitaVo titaVo, int entdy, String flowno, int flowstep) throws LogicException {
		this.info("checkTxFlow = " + entdy + "/" + flowno + "/" + flowstep);

		TxFlowId tTxFlowId = new TxFlowId();

		tTxFlowId.setEntdy(entdy);
		tTxFlowId.setFlowNo(flowno);

		TxFlow tTxFlow = txFlowService.findById(tTxFlowId);
		if (tTxFlow == null) {
			throw new LogicException(titaVo, "EC001", "交易流程控制檔(TxFlow)帳務日:" + entdy + ",交易序號:" + flowno);
		}

		if (tTxFlow.getFlowStep() > flowstep) {
			switch (tTxFlow.getFlowStep()) {
			case 2:
				throw new LogicException(titaVo, "EC008", "交易已放行");
			case 3:
				throw new LogicException(titaVo, "EC008", "交易已審核");
			case 4:
				throw new LogicException(titaVo, "EC008", "交易已審核放");
			default:
				throw new LogicException(titaVo, "EC008", "交易已異動");
			}

		} else if (tTxFlow.getFlowStep() != flowstep) {
			throw new LogicException(titaVo, "EC008", "交易已異動");
		}
	}

}