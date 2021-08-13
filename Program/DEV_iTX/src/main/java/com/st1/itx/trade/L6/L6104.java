package com.st1.itx.trade.L6;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.db.domain.TxAgent;
import com.st1.itx.db.domain.TxAgentId;
import com.st1.itx.db.service.TxAgentService;

@Service("L6104")
@Scope("prototype")
/**
 * 
 * 
 * @author eric chang
 * @version 1.0.0
 */
public class L6104 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L6104.class);

	@Autowired
	public TxAgentService txAgentService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6104 ");
		this.totaVo.init(titaVo);

		String FunCode = titaVo.getParam("FunCode").trim();

		delAll(titaVo);

		for (int i = 1; i <= 5; i++) {
			String AgentTlrno = titaVo.getParam("AgentTlrno" + i).trim();
			if (!"".equals(AgentTlrno)) {
				addOne(titaVo, AgentTlrno);
			}
		}

		this.addList(this.totaVo);
		return this.sendList();

	}

	private void addOne(TitaVo titaVo, String agentTlrno) throws LogicException {
		TxAgent txAgent = new TxAgent();

		TxAgentId txAgentId = new TxAgentId();
		txAgentId.setTlrNo(titaVo.getTlrNo());
		txAgentId.setAgentTlrNo(agentTlrno);

		txAgent.setTxAgentId(txAgentId);
		txAgent.setBeginDate(Integer.parseInt(titaVo.getParam("BeginDate").toString().trim()));
		txAgent.setBeginTime(Integer.parseInt(titaVo.getParam("BeginTime").toString().trim()));
		txAgent.setEndDate(Integer.parseInt(titaVo.getParam("EndDate").toString().trim()));
		txAgent.setEndTime(Integer.parseInt(titaVo.getParam("EndTime").toString().trim()));
		txAgent.setStatus(Integer.parseInt(titaVo.getParam("Status").toString().trim()));

		try {
			txAgentService.insert(txAgent);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0005", e.getErrorMsg());
		}
	}

	private void delAll(TitaVo titaVo) throws LogicException {
		Slice<TxAgent> slTxAgent = txAgentService.findByTlrNo(titaVo.getTlrNo().trim(), 0, Integer.MAX_VALUE);
		List<TxAgent> lTxAgent = slTxAgent == null ? null : slTxAgent.getContent();
		if (lTxAgent == null) {
			return;
		}
		for (TxAgent txAgent : lTxAgent) {
			TxAgent txAgent2 = txAgentService.holdById(txAgent.getTxAgentId());
			try {
				txAgentService.delete(txAgent2);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0008", e.getErrorMsg());
			}

		}
	}
}