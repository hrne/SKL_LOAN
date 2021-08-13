package com.st1.itx.trade.L6;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
//import com.st1.itx.db.service.CustMainService;
//import com.st1.itx.db.service.CustTelNoService;
import com.st1.itx.db.domain.TxTeller;
import com.st1.itx.db.service.TxTellerService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

@Service("L6R20")
@Scope("prototype")
/**
 * 
 * 
 * @author Eric Chang
 * @version 1.0.0
 * @description for L6401 rim
 */
public class L6R20 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L6R20.class);

	/* DB服務注入 */
	@Autowired
	public TxTellerService sTxTellerService;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6R20 ");
		this.totaVo.init(titaVo);

		String iFunCode = titaVo.get("FunCode").trim();
		String iTlrNo = titaVo.get("TlrNo").trim();

		this.info("FunCode = " + iFunCode);
		this.info("TlrNO = " + iTlrNo);

//		TxTeller ttxTeller = new TxTeller();
		TxTeller tTxTeller = sTxTellerService.findById(iTlrNo, titaVo);

		if (tTxTeller == null) {
			if ("2".equals(iFunCode)) {
				throw new LogicException(titaVo, "E0003", "使用者:" + iTlrNo);
			} else if ("4".equals(iFunCode)) {
				throw new LogicException(titaVo, "E0004", "使用者:" + iTlrNo);
			}
			MoveTota(iTlrNo, new TxTeller());
		} else {
			if ("1".equals(iFunCode)) {
				throw new LogicException(titaVo, "E0002", "使用者:" + iTlrNo);
			}
			MoveTota(iTlrNo, tTxTeller);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void MoveTota(String iTlrNo, TxTeller tTxTeller) {
		this.info("L6R20 MoveTota");

		this.totaVo.putParam("BrNo", tTxTeller.getBrNo());
		this.totaVo.putParam("AdFg", tTxTeller.getAdFg());
		this.totaVo.putParam("LevelFg", tTxTeller.getLevelFg());
		this.totaVo.putParam("Status", tTxTeller.getStatus());
		this.totaVo.putParam("GroupNo", tTxTeller.getGroupNo());
		this.totaVo.putParam("Entdy", tTxTeller.getEntdy());
		this.totaVo.putParam("LogonFg", tTxTeller.getLogonFg());
		this.totaVo.putParam("TxtNo", tTxTeller.getTxtNo());
		this.totaVo.putParam("LtxDate", tTxTeller.getLtxDate());
		this.totaVo.putParam("LtxTime", tTxTeller.getLtxTime());
		this.totaVo.putParam("Desc", tTxTeller.getDesc());
		this.totaVo.putParam("TlrItem", tTxTeller.getTlrItem());
		
		
		this.totaVo.putParam("AmlHighFg", tTxTeller.getAmlHighFg());
		this.totaVo.putParam("AuthNo1", tTxTeller.getAuthNo1());
		this.totaVo.putParam("AuthNo2", tTxTeller.getAuthNo2());
		this.totaVo.putParam("AuthNo3", tTxTeller.getAuthNo3());
		this.totaVo.putParam("AuthNo4", tTxTeller.getAuthNo4());
		this.totaVo.putParam("AuthNo5", tTxTeller.getAuthNo5());
		this.totaVo.putParam("AuthNo6", tTxTeller.getAuthNo6());
		this.totaVo.putParam("AuthNo7", tTxTeller.getAuthNo7());
		this.totaVo.putParam("AuthNo8", tTxTeller.getAuthNo8());
		this.totaVo.putParam("AuthNo9", tTxTeller.getAuthNo9());
		this.totaVo.putParam("AuthNo10", tTxTeller.getAuthNo10());
	}
}