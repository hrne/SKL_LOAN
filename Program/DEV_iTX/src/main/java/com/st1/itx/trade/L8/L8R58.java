package com.st1.itx.trade.L8;

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

@Service("L8R58")
@Scope("prototype")
/**
 * L8通用員工代號找姓名
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L8R58 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public CdEmpService iCdEmpService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8r14 ");
		this.totaVo.init(titaVo);
		String iRimEmpNo = titaVo.getParam("RimEmpNo");
		CdEmp iCdEmp = new CdEmp();
		iCdEmp = iCdEmpService.findById(iRimEmpNo, titaVo);
		if (iCdEmp == null) {
			throw new LogicException(titaVo, "E0001", ""); // 無此代號錯誤
		} else {
			totaVo.putParam("L8R58Fullname", iCdEmp.getFullname());
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}