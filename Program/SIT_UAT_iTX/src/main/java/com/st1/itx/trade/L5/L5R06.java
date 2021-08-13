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
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L5R06")
@Scope("prototype")
/**
 * 
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L5R06 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L5R06.class);
	
	/* DB服務注入 */
	@Autowired
	public CdEmpService sCdEmpService;
	
	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;
	
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		//L5401調RIM用，資料來源為CdEmp
		this.info("active L5R06 ");

		this.totaVo.init(titaVo);
		String iEmpNo = titaVo.getParam("CdEmpRimEmpNo").trim();//員工代號
		CdEmp iCdEmp = sCdEmpService.findById(iEmpNo,titaVo);
		if(iCdEmp==null) {
//			totaVo.putParam("L5R06FullName",""); //員工姓名
//			totaVo.putParam("L5R06AreaCode",""); //區域中心
//			totaVo.putParam("L5R06AreaItem",""); //區域中文
//			totaVo.putParam("L5R06DistCode",""); //部室代號
//			totaVo.putParam("L5R06DistItem",""); //部室中文
//			totaVo.putParam("L5R06DeptCode",""); //區部代號
//			totaVo.putParam("L5R06DeptItem",""); //區部中文
			throw new LogicException(titaVo, "E0001", "無此員工代號");
		}else {
			totaVo.putParam("L5R06FullName",iCdEmp.getFullname()); //員工姓名
			totaVo.putParam("L5R06AreaCode",iCdEmp.getCenterCode()); //區域中心
			totaVo.putParam("L5R06AreaItem",iCdEmp.getCenterCodeName()); //區域中文
			totaVo.putParam("L5R06DistCode",iCdEmp.getCenterCode1()); //部室代號
			totaVo.putParam("L5R06DistItem",iCdEmp.getCenterCode1Name()); //部室中文
			totaVo.putParam("L5R06DeptCode",iCdEmp.getCenterCode2()); //區部代號
			totaVo.putParam("L5R06DeptItem",iCdEmp.getCenterCode2Name()); //區部中文
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}