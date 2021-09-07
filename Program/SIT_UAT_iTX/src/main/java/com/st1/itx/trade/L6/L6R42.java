package com.st1.itx.trade.L6;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L6R42")
@Scope("prototype")
/**
 * 特殊參數設定(L650D)-調員工電子信箱
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L6R42 extends TradeBuffer {	
	@Autowired
	public CdEmpService iCdEmpService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {

		this.info("active L6R42 ");
		this.totaVo.init(titaVo);
		
		CdEmp iCdEmp = new CdEmp();
		String iEmpNo = titaVo.getParam("RimEmpNo");
		
		iCdEmp = iCdEmpService.findById(iEmpNo, titaVo);
		if (iCdEmp == null) {
			throw new LogicException(titaVo, "E0001", "無此員工代號");
		}else {
			totaVo.putParam("L6R42Email", iCdEmp.getEmail());
		}


		this.addList(this.totaVo);
		return this.sendList();
	}
}