package com.st1.itx.trade.L1;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.BankRelationCom;
import com.st1.itx.util.common.data.BankRelationVo;

@Service("L1R16")
@Scope("prototype")
/**
 * 客戶關係調Rim
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L1R16 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public BankRelationCom iBankRelationCom;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L1R16 ");
		this.totaVo.init(titaVo);

		String iCustId = titaVo.getParam("RimCustId");
		String iCustName = titaVo.getParam("RimCustName").toString().trim();

		BankRelationVo vo = iBankRelationCom.getBankRelation(iCustId,iCustName, titaVo);

		if ("Y".equals(vo.getIsLimit())) {
			totaVo.putParam("L1R16IsLimitYn", "Y");
		} else {
			totaVo.putParam("L1R16IsLimitYn", "N");
		}
		if ("Y".equals(vo.getIsRelated())) {
			totaVo.putParam("L1R16IsRelatedYn", "Y");
		} else {
			totaVo.putParam("L1R16IsRelatedYn", "N");
		}
		if ("Y".equals(vo.getIsLnrelNear())) {
			totaVo.putParam("L1R16IsLnrelNearYn", "Y");
		} else {
			totaVo.putParam("L1R16IsLnrelNearYn", "N");
		}

		//2021.8.31 by eric
		if ("Y".equals(vo.getIsSuspected())) {
			totaVo.putParam("L1R16IsSuspectedYn", "Y");
		} else {
			totaVo.putParam("L1R16IsSuspectedYn", "N");
		}
		
		totaVo.putParam("L1R16DataDate", vo.getDataDate());

		this.addList(this.totaVo);
		return this.sendList();
	}
}