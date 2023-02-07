package com.st1.itx.trade.L5;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.CheckInsurance;
import com.st1.itx.util.common.data.CheckInsuranceVo;

@Service("L5521")
@Scope("prototype")
/**
 * 
 * 
 * @author eric chang
 * @version 1.0.0
 */
public class L5521 extends TradeBuffer {

	@Autowired
	public CheckInsurance checkInsurance;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5521 ");
		this.totaVo.init(titaVo);

		// very importance
		checkInsurance.setTxBuffer(this.txBuffer);

		CheckInsuranceVo checkInsuranceVo = new CheckInsuranceVo();

		checkInsuranceVo.setCustId(titaVo.get("CustId").trim());

		checkInsuranceVo = checkInsurance.checkInsurance(titaVo, checkInsuranceVo);

		this.totaVo.putParam("Success", Boolean.toString(checkInsuranceVo.isSuccess()));
		this.totaVo.putParam("MsgRs", checkInsuranceVo.getMsgRs());

		this.addList(this.totaVo);
		return this.sendList();
	}
}