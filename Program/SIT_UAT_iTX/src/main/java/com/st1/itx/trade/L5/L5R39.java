package com.st1.itx.trade.L5;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.domain.PfCoOfficer;
import com.st1.itx.db.domain.PfCoOfficerId;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.PfCoOfficerService;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L5R39")
@Scope("prototype")
/**
 *
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L5R39 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public PfCoOfficerService iPfCoOfficerService;
	@Autowired
	public CdEmpService iCdEmpService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {

		this.totaVo.init(titaVo);
		String iEmpNo = titaVo.getParam("RimEmpNo");
		int iEffectiveDate = Integer.valueOf(titaVo.getParam("RimEffectiveDate")) + 19110000;

		PfCoOfficer iPfCoOfficer = new PfCoOfficer();
		PfCoOfficerId iPfCoOfficerId = new PfCoOfficerId();
		iPfCoOfficerId.setEffectiveDate(iEffectiveDate);
		iPfCoOfficerId.setEmpNo(iEmpNo);
		iPfCoOfficer = iPfCoOfficerService.findById(iPfCoOfficerId, titaVo);
		if (iPfCoOfficer == null) {
			throw new LogicException(titaVo, "E0001", "");
		} else {
			CdEmp iCdEmp = new CdEmp();
			iCdEmp = iCdEmpService.findById(iEmpNo, titaVo);
			if (iCdEmp == null) {
				totaVo.putParam("L5r39Fullname", "");
			} else {
				totaVo.putParam("L5r39Fullname", iCdEmp.getFullname());
			}
			totaVo.putParam("L5r39UnitCode", iPfCoOfficer.getAreaCode());
			totaVo.putParam("L5r39UnitCodeX", iPfCoOfficer.getAreaItem());
			totaVo.putParam("L5r39DistCode", iPfCoOfficer.getDistCode());
			totaVo.putParam("L5r39DistCodeX", iPfCoOfficer.getDistItem());
			totaVo.putParam("L5r39DeptCode", iPfCoOfficer.getDeptCode());
			totaVo.putParam("L5r39DeptCodeX", iPfCoOfficer.getDeptItem());
			totaVo.putParam("L5r39IneffectiveDate", iPfCoOfficer.getIneffectiveDate());
			totaVo.putParam("L5r39EmpClass", iPfCoOfficer.getEmpClass());
			totaVo.putParam("L5r39ClassPass", iPfCoOfficer.getClassPass());
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}