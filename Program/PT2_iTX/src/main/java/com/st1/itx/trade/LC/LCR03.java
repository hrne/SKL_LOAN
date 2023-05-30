package com.st1.itx.trade.LC;

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
import com.st1.itx.db.service.TxTellerService;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.domain.TxFlow;
import com.st1.itx.db.domain.TxFlowId;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.TxFlowService;

import com.st1.itx.db.domain.TxTranCode;
import com.st1.itx.db.service.TxTranCodeService;

@Service("LCR03")
@Scope("prototype")
/**
 * 放行RIM
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public class LCR03 extends TradeBuffer {

	@Autowired
	public TxRecordService txRecordService;

	@Autowired
	public TxFlowService txFlowService;

	@Autowired
	public TxTranCodeService txTranCodeService;

	@Autowired
	public TxTellerService txTellerService;

	@Autowired
	public CdEmpService cdEmpService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active LCR03 ");
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

			int flowstep = tTxRecord.getFlowStep();
			String actfg = "";

			if (tTxRecord.getActionFg() == 1) {
				throw new LogicException(titaVo, "EC004", "帳務日:" + entday + ",交易序號:" + txno + "已訂正");
			} else if (tTxRecord.getActionFg() == 2) {
				throw new LogicException(titaVo, "EC004", "帳務日:" + entday + ",交易序號:" + txno + "已修正");
			} else if (flowstep == 1) {
				actfg = "2";
			} else if (flowstep == 3) {
				actfg = "4";
			} else {
				throw new LogicException(titaVo, "EC004", "帳務日:" + entday + ",交易序號:" + txno + "狀態不符");
			}

			checkTxFlow(titaVo, tTxRecord.getEntdy(), tTxRecord.getFlowNo(), tTxRecord.getFlowStep());

			TxTranCode tTxTranCode = txTranCodeService.findById(tTxRecord.getTranNo());
			if (tTxTranCode == null) {
				throw new LogicException("EC001", "交易控制檔(TxTranCode):" + tTxRecord.getTranNo());
			}

			try {
				TitaVo tita2 = titaVo.getVo(tTxRecord.getTranData());
				tita2.put("OrgEntdy", String.valueOf(tTxRecord.getEntdy()));
				tita2.put("ORGKIN", tTxRecord.getBrNo());
				tita2.put("ORGTLR", tTxRecord.getTlrNo());
				tita2.put("ORGTNO", tTxRecord.getTxSeq());
				tita2.put("ACTFG", actfg);

//				TxTeller txTeller = txTellerService.findById(tTxRecord.getTlrNo());

				CdEmp cdEmp = cdEmpService.findById(tTxRecord.getTlrNo());

				if (cdEmp == null) {
					throw new LogicException("EC001", "員工資料檔員編不存在::" + tTxRecord.getTlrNo());
				}

				tita2.put("ORGEMPNM", cdEmp.getFullname().trim());

				this.info("txtranCode == " + tTxTranCode.getSubmitFg());
				if (1 == tTxTranCode.getSubmitFg() || 2 == tTxTranCode.getSubmitFg())
					tita2.put("HCODE", "8");
				else
					tita2.put("HCODE", "0");

				totaVo.setEcTitaVo(tita2);
			} catch (Throwable e) {
				throw new LogicException(titaVo, "EC002", "電文資料格式有誤");
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void checkTxFlow(TitaVo titaVo, int entdy, String flowno, int flowstep) throws LogicException {
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