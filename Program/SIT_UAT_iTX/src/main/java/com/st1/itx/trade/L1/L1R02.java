package com.st1.itx.trade.L1;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.CustRel;
import com.st1.itx.db.domain.CustRelId;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.CustRelService;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L1R02")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L1R02 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CustMainService sCustMainService;

	/* DB服務注入 */
	@Autowired
	public CustRelService sCustRelService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L1R02 ");
		this.totaVo.init(titaVo);

		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 500;

		String iCustId = titaVo.getParam("RimCustId1");
		String iRelId = titaVo.getParam("RimRelId");

		CustMain iCustMain = new CustMain();
		CustMain iRelMain = new CustMain();

		iCustMain = sCustMainService.custIdFirst(iCustId, titaVo);
		iRelMain = sCustMainService.custIdFirst(iRelId, titaVo);

		if (iCustMain == null) {
			throw new LogicException(titaVo, "E0001", "統編" + iCustId + "不存在");
		}
		if (iRelMain == null) {
			throw new LogicException(titaVo, "E0001", "統編" + iRelId + "不存在");
		}

		CustRelId iCustRelId = new CustRelId();
		CustRel iCustRel = new CustRel();
		iCustRelId.setCustUKey(iCustMain.getCustUKey());
		iCustRelId.setRelUKey(iRelMain.getCustUKey());

		iCustRel = sCustRelService.findById(iCustRelId, titaVo);

		String ExitFg = "0";
		if (iCustRel != null) {
			ExitFg = "1";
		}
		this.totaVo.putParam("L1r02ExitFg", ExitFg);

		this.addList(this.totaVo);
		return this.sendList();
	}
}