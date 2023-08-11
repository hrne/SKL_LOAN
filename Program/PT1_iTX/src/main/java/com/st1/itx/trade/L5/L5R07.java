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
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.PfCoOfficerService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

@Service("L5R07")
@Scope("prototype")
/**
 * 
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L5R07 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public PfCoOfficerService sPfCoOfficerService;

	@Autowired
	public CdEmpService sCdEmpService;
	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		// L5407調RIM用
		this.info("active L5R07 ");
		this.info("L5R07 titaVo=[" + titaVo + "]");
		this.totaVo.init(titaVo);
		int iFunCd = parse.stringToInteger(titaVo.getParam("RimFunCd"));// 功能
		String iEmpNo = titaVo.getParam("RimEmpNo").trim();// 員工代號
		CdEmp iCdEmp = sCdEmpService.findById(iEmpNo, titaVo);
		PfCoOfficer tPfCoOfficer = sPfCoOfficerService.findByEmpNoFirst(iEmpNo, titaVo);
		if (iFunCd == 1) {
			if (tPfCoOfficer != null) {
				throw new LogicException(titaVo, "E0010", "協辦人員已存在: " + iEmpNo);// 功能選擇錯誤
			}
		}
		if (iCdEmp == null) {
			totaVo.putParam("L5r07Fullname", "");
			totaVo.putParam("L5r07UnitCode", "");
			totaVo.putParam("L5r07DistCode", "");
			totaVo.putParam("L5r07DeptCode", "");
			totaVo.putParam("L5r07UnitCodeX", "");
			totaVo.putParam("L5r07DistCodeX", "");
			totaVo.putParam("L5r07DeptCodeX", "");
		} else {
			totaVo.putParam("L5r07Fullname", iCdEmp.getFullname());
			totaVo.putParam("L5r07UnitCode", iCdEmp.getCenterCode());
			totaVo.putParam("L5r07DistCode", iCdEmp.getCenterCode1());
			totaVo.putParam("L5r07DeptCode", iCdEmp.getCenterCode2());
			totaVo.putParam("L5r07UnitCodeX", iCdEmp.getCenterCodeName());
			totaVo.putParam("L5r07DistCodeX", iCdEmp.getCenterCode1Name());
			totaVo.putParam("L5r07DeptCodeX", iCdEmp.getCenterCode2Name());
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}