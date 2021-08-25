package com.st1.itx.trade.L5;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.FacMainId;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L5R25")
@Scope("prototype")
/**
 * 函催登錄發送對象用
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L5R25 extends TradeBuffer {

	@Autowired
	public FacMainService iFacMainService;

	@Autowired
	public CustMainService iCustMainService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 500;

		this.info("active L5R25 ");
		this.totaVo.init(titaVo);
		String iCustNo = titaVo.getParam("RimCustNo");
		String iFacmNo = titaVo.getParam("RimFacmNo");
		String iCustUkey = titaVo.getParam("RimCustUKey");

		CustMain iCustMain = new CustMain();
		iCustMain = iCustMainService.findById(iCustUkey, titaVo);
		if (iCustMain.getCustName().equals("")) {
			totaVo.putParam("L5R25CustName", "");
		} else {
			totaVo.putParam("L5R25CustName", iCustMain.getCustName());
		}

		FacMainId iFacMainId = new FacMainId();
		FacMain iFacMain = new FacMain();

		iFacMainId.setCustNo(Integer.valueOf(iCustNo));
		iFacMainId.setFacmNo(Integer.valueOf(iFacmNo));
		iFacMain = iFacMainService.findById(iFacMainId, titaVo);

		if (iFacMain != null) {
			totaVo.putParam("L5R25ApplNo", iFacMain.getApplNo());
		} else {
			totaVo.putParam("L5R25ApplNo", 0);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}