package com.st1.itx.trade.L5;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L5R28")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L5R28 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L5R28.class);
	@Autowired
	public CdEmpService sCdEmpService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5R28 ");

		this.totaVo.init(titaVo);
		String employeeNo = titaVo.getParam("RimEmployeeNo").trim();
		this.info("Run L5R28 employeeNo=[" + employeeNo + "]");
		CdEmp CdEmpVo = sCdEmpService.findById(employeeNo, titaVo);
		String FullName = "";
		if (CdEmpVo != null) {
			FullName = CdEmpVo.getFullname();
		}

		this.totaVo.putParam("L5r28FullName", FullName);

		this.addList(this.totaVo);
		return this.sendList();
	}
}