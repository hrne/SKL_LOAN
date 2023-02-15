package com.st1.itx.trade.L6;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.db.domain.TxTranCode;
import com.st1.itx.db.service.TxTranCodeService;

@Service("L6R24")
@Scope("prototype")
/**
 * 
 * 
 * @author Eric Chang
 * @version 1.0.0
 * @description for L6402 rim
 */
public class L6R24 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public TxTranCodeService sTxTranCodeService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6R24 ");
		this.totaVo.init(titaVo);

		String iFunCode = titaVo.get("FunCode").trim();
		String iTranNo = titaVo.get("TranNo").trim();

		TxTranCode tTxTranCode = sTxTranCodeService.findById(iTranNo, titaVo);

		if (tTxTranCode == null) {
			if ("2".equals(iFunCode)) {
				throw new LogicException(titaVo, "E0003", "交易代碼:" + iTranNo);
			} else if ("4".equals(iFunCode)) {
				throw new LogicException(titaVo, "E0004", "交易代碼:" + iTranNo);
			}

			MoveTota(new TxTranCode());
		} else {
			if ("1".equals(iFunCode)) {
				throw new LogicException(titaVo, "E0002", "交易代碼:" + iTranNo);
			}

			MoveTota(tTxTranCode);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void MoveTota(TxTranCode tTxTranCode) {
		this.info("L6R24 MoveTota");
		this.totaVo.putParam("TranItem", tTxTranCode.getTranItem());
		this.totaVo.putParam("Desc", tTxTranCode.getDesc());
		this.totaVo.putParam("TypeFg", tTxTranCode.getTypeFg());
		this.totaVo.putParam("Status", tTxTranCode.getStatus());
		this.totaVo.putParam("CancelFg", tTxTranCode.getCancelFg());
		this.totaVo.putParam("ModifyFg", tTxTranCode.getModifyFg());
		this.totaVo.putParam("MenuNo", tTxTranCode.getMenuNo());
		this.totaVo.putParam("SubMenuNo", tTxTranCode.getSubMenuNo());
		this.totaVo.putParam("MenuFg", tTxTranCode.getMenuFg());
		this.totaVo.putParam("SubmitFg", tTxTranCode.getSubmitFg());
		this.totaVo.putParam("CustDataCtrlFg", tTxTranCode.getCustDataCtrlFg());
		this.totaVo.putParam("CustRmkFg", tTxTranCode.getCustRmkFg());
		this.totaVo.putParam("ChainTranMsg", tTxTranCode.getChainTranMsg());
		this.totaVo.putParam("ApLogFlag", tTxTranCode.getApLogFlag());
		this.totaVo.putParam("ApLogRim", tTxTranCode.getApLogRim());
	}

}