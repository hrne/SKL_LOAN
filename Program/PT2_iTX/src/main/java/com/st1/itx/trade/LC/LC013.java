package com.st1.itx.trade.LC;

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
import com.st1.itx.tradeService.TradeBuffer;

import com.st1.itx.db.domain.TxTeller;
import com.st1.itx.db.service.TxTellerService;

import com.st1.itx.db.domain.TxAuthGroup;
import com.st1.itx.db.service.TxAuthGroupService;
import com.st1.itx.db.domain.TxAgent;
import com.st1.itx.db.service.TxAgentService;
import com.st1.itx.db.domain.TxTellerAuth;
import com.st1.itx.db.service.TxTellerAuthService;

import com.st1.itx.util.common.CheckAuth;

import java.util.Date;
import java.util.HashMap;
import java.text.SimpleDateFormat;

@Service("LC013")
@Scope("prototype")
/**
 * 
 * 
 * @author eric chang
 * @version 1.0.0
 */
public class LC013 extends TradeBuffer {

	@Autowired
	public TxTellerService txTellerService;

	@Autowired
	public TxTellerAuthService txTellerAuthService;

	@Autowired
	public TxAuthGroupService txAuthGroupService;

	@Autowired
	public TxAgentService txAgentService;

	@Autowired
	public CheckAuth checkAuth;

	Map<String, String> authNos = new HashMap<String, String>();

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active LC013 ");
		this.totaVo.init(titaVo);

		// 保留原本DBFG Adam
		titaVo.keepOrgDataBase();
		titaVo.setDataBaseOnLine();

		// 經辦權限
		TlrAuth(titaVo.getTlrNo(), "", "", titaVo);

		// 代理權限
		Slice<TxAgent> slTxAgent = txAgentService.findByAgentTlrNo(titaVo.getTlrNo(), 0, 1000);
		List<TxAgent> lTxAgent = slTxAgent == null ? null : slTxAgent.getContent();

		// 現在時間
		SimpleDateFormat sdFormat = new SimpleDateFormat("HHmm");
		Date date = new Date();
		int nowTime = Integer.valueOf(sdFormat.format(date));

		this.info("LC013 nowTime = " + nowTime);

		if (lTxAgent != null) {
			for (TxAgent txAgent : lTxAgent) {
				this.info("LC013 TxAgent = " + txAgent.getTlrNo() + '/' + txAgent.getBeginDate() + '/' + txAgent.getEndDate() + '/' + this.getTxBuffer().getTxCom().getTbsdy());

				/*
				 * // 未生效 if (txAgent.getStatus() != 1) { continue; } // 未在有效期間 if
				 * (txAgent.getBeginDate() > this.getTxBuffer().getTxCom().getTbsdy()) {
				 * continue; }
				 * 
				 * if (txAgent.getBeginDate() == this.getTxBuffer().getTxCom().getTbsdy() &&
				 * txAgent.getBeginTime() > nowTime) { continue; }
				 * 
				 * if (txAgent.getEndDate() < this.getTxBuffer().getTxCom().getTbsdy()) {
				 * continue; }
				 * 
				 * if (txAgent.getEndDate() == this.getTxBuffer().getTxCom().getTbsdy() &&
				 * txAgent.getEndTime() < nowTime) { continue; }
				 */

				checkAuth.setTxBuffer(this.txBuffer);

				if (!checkAuth.isAgent(titaVo, txAgent)) {
					continue;
				}

				this.info("LC013 1 = " + txAgent.getTlrNo());

				// 代理人姓名
				TxTeller txTeller = txTellerService.findById(txAgent.getTlrNo());

				this.info("LC013 2 ");

				if (txTeller == null) {
					continue;
				}

//				this.info("LC013 : " + txAgent.getTlrNo() + '/' + txTeller.getTlrItem());

				TlrAuth(txAgent.getTlrNo(), txAgent.getTlrNo(), "代理：" + txTeller.getTlrItem(), titaVo);

			}
		}

		// 恢復原本DBFG Adam
		titaVo.setDataBaseOnOrg();

		this.addList(this.totaVo);
		return this.sendList();

	}

	private void TlrAuth(String tlrNo, String agentNo, String agentItem, TitaVo titaVo) throws LogicException {

		this.info("LC013.TlrAuth : " + tlrNo + "/" + agentNo);

		TxTeller txTeller = txTellerService.findById(tlrNo);

		if (txTeller == null) {
			return;
		}

		if ("".equals(agentNo)) {
			this.totaVo.putParam("AdFg", txTeller.getAdFg());
		}

//		addOccurs(txTeller.getAuthNo1(), agentNo, agentItem, titaVo);
//		addOccurs(txTeller.getAuthNo2(), agentNo, agentItem, titaVo);
//		addOccurs(txTeller.getAuthNo3(), agentNo, agentItem, titaVo);
//		addOccurs(txTeller.getAuthNo4(), agentNo, agentItem, titaVo);
//		addOccurs(txTeller.getAuthNo5(), agentNo, agentItem, titaVo);
//		addOccurs(txTeller.getAuthNo6(), agentNo, agentItem, titaVo);
//		addOccurs(txTeller.getAuthNo7(), agentNo, agentItem, titaVo);
//		addOccurs(txTeller.getAuthNo8(), agentNo, agentItem, titaVo);
//		addOccurs(txTeller.getAuthNo9(), agentNo, agentItem, titaVo);
//		addOccurs(txTeller.getAuthNo10(), agentNo, agentItem, titaVo);

		Slice<TxTellerAuth> slTxTellerAuth = txTellerAuthService.findByTlrNo(tlrNo, 0, Integer.MAX_VALUE, titaVo);
		List<TxTellerAuth> lTxTellerAuth = slTxTellerAuth == null ? null : slTxTellerAuth.getContent();
		if (lTxTellerAuth != null && lTxTellerAuth.size() > 0) {
			for (TxTellerAuth txTellerAuth : lTxTellerAuth) {
				addOccurs(txTellerAuth.getAuthNo(), agentNo, agentItem, titaVo);
			}
		}
	}

	private void addOccurs(String authNo, String agentNo, String agentItem, TitaVo titaVo) throws LogicException {

		this.info("LC013.addOccurs : " + authNo + "/" + agentNo);

		if ("".equals(authNo)) {
			return;
		}

		TxAuthGroup txAuthGroup = txAuthGroupService.findById(authNo);

		if (txAuthGroup == null) {
			return;
		}

		// 檢查重複權限 remove by eric 2021.11.29
//		if (authNos.size() > 0) {
//			if (authNos.get(authNo) != null) {
//				return;
//			}
//
//		}
//		this.info("LC013.authNos.put : " + authNo);
		authNos.put(authNo, authNo);

		OccursList occursList = new OccursList();
		occursList.putParam("AuthNo", authNo);
		occursList.putParam("AuthItem", txAuthGroup.getAuthItem());
		occursList.putParam("AgentNo", agentNo);
		occursList.putParam("AgentItem", agentItem);

		this.totaVo.addOccursList(occursList);

	}

}