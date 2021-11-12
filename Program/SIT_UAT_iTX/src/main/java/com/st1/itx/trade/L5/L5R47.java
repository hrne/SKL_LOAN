package com.st1.itx.trade.L5;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.db.domain.SpecInnReCheck;
import com.st1.itx.db.domain.SpecInnReCheckId;
import com.st1.itx.db.service.SpecInnReCheckService;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.service.CustMainService;

@Service("L5R47")
@Scope("prototype")
/**
 * 指定覆審調Rim
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L5R47 extends TradeBuffer {

	
	@Autowired
	public SpecInnReCheckService iSpecInnReCheckService;
	@Autowired
	public CustMainService iCustMainService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5R47 ");
		this.totaVo.init(titaVo);

		int iCustNo = Integer.valueOf(titaVo.getParam("RimCustNo"));
		int iFacmNo = Integer.valueOf(titaVo.getParam("RimFacmNo"));

		SpecInnReCheckId iSpecInnReCheckId = new SpecInnReCheckId();
		SpecInnReCheck iSpecInnReCheck = new SpecInnReCheck();
		CustMain iCustMain = new CustMain();
		iSpecInnReCheckId.setCustNo(iCustNo);
		iSpecInnReCheckId.setFacmNo(iFacmNo);
		iSpecInnReCheck = iSpecInnReCheckService.findById(iSpecInnReCheckId, titaVo);
		
		if (iSpecInnReCheck == null) {
			throw new LogicException(titaVo, "E0001", "指定覆審名單"); // 查無資料
		}
		iCustMain = iCustMainService.custNoFirst(iCustNo, iCustNo, titaVo);
		if(iCustMain == null) {
			totaVo.putParam("L5R47CustName", "");
		}else {
			totaVo.putParam("L5R47CustName", iCustMain.getCustName());
		}
		if (iSpecInnReCheck.getReChkYearMonth()==0) {
			totaVo.putParam("L5R47ReCheckYearMonth", 0);
		}else {
			totaVo.putParam("L5R47ReCheckYearMonth", iSpecInnReCheck.getReChkYearMonth()-191100);
		}
		totaVo.putParam("L5R47Cycle", iSpecInnReCheck.getCycle());
		totaVo.putParam("L5R47Remark", iSpecInnReCheck.getRemark());
		

		this.addList(this.totaVo);
		return this.sendList();
	}
}