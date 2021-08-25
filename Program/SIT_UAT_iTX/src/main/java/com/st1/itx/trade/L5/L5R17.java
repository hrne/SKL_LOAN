package com.st1.itx.trade.L5;

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

@Service("L5R17")
@Scope("prototype")
/**
 * 
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L5R17 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CdEmpService sCdEmpService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		// L5401交易內調RIM用,資料來源為CdBcm表
		this.info("active L5R17 ");
		this.info("L5R17 titaVo=[" + titaVo + "]");
		this.totaVo.init(titaVo);
		String iRimEmpNo = titaVo.getParam("RimEmpNo");

		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 500;

		CdEmp iCdEmp = new CdEmp();
		iCdEmp = sCdEmpService.findById(iRimEmpNo, titaVo);

		if (iCdEmp != null) {
			totaVo.putParam("L5R17FullName", iCdEmp.getFullname());
		} else {
			totaVo.putParam("L5R17FullName", "");
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}