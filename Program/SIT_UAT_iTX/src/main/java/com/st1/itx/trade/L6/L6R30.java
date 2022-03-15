package com.st1.itx.trade.L6;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.db.domain.TxAgent;
import com.st1.itx.db.service.TxAgentService;

@Service("L6R30")
@Scope("prototype")
/**
 * 
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public class L6R30 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L6R30.class);

	@Autowired
	public TxAgentService txAgentService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6R30 ");
		this.totaVo.init(titaVo);

		this.totaVo.putParam("AgentTlrno1", "");
		this.totaVo.putParam("AgentTlrno2", "");
		this.totaVo.putParam("AgentTlrno3", "");
		this.totaVo.putParam("AgentTlrno4", "");
		this.totaVo.putParam("AgentTlrno5", "");
		this.totaVo.putParam("BeginDate", 0);
		this.totaVo.putParam("BeginTime", 0);
		this.totaVo.putParam("EndDate", 0);
		this.totaVo.putParam("EndTime", 0);
		this.totaVo.putParam("Status", 0);

		int i = 0;

		Slice<TxAgent> slTxAgent = txAgentService.findByTlrNo(titaVo.getTlrNo(), this.index, Integer.MAX_VALUE, titaVo);
		List<TxAgent> lTxAgent = slTxAgent == null ? null : slTxAgent.getContent();

		if (lTxAgent != null) {
			for (TxAgent txAgent : lTxAgent) {
				if (i >= 5)
					break;
				if (i == 0) {
					i++;
					this.totaVo.putParam("BeginDate", txAgent.getBeginDate());
					this.totaVo.putParam("BeginTime", txAgent.getBeginTime());
					this.totaVo.putParam("EndDate", txAgent.getEndDate());
					this.totaVo.putParam("EndTime", txAgent.getEndTime());
					this.totaVo.putParam("Status", txAgent.getStatus());
				} else {
					i++;
				}
				this.totaVo.putParam("AgentTlrno" + i, txAgent.getAgentTlrNo());
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}