package com.st1.itx.trade.L5;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CollList;
import com.st1.itx.db.domain.CollListId;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.CollListService;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L5R16")
@Scope("prototype")
/**
 * 催收法務主檔使用
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L5R16 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CdEmpService sCdEmpService;

	@Autowired
	public CollListService iCollListService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		// L5401交易內調RIM用,資料來源為CdBcm表
		this.info("active L5R16 ");
		this.info("L5R16 titaVo=[" + titaVo + "]");
		this.totaVo.init(titaVo);
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 500;
		String iRimCustNo = titaVo.getParam("RimCustNo");
		String iRimFacmNo = titaVo.getParam("RimFacmNo");
		CollListId iCollListId = new CollListId();
		iCollListId.setCustNo(Integer.valueOf(iRimCustNo));
		iCollListId.setFacmNo(Integer.valueOf(iRimFacmNo));
		CollList iCollList = iCollListService.findById(iCollListId, titaVo);
		if (iCollList.getAccCollPsn().equals("") || iCollList.getLegalPsn().equals("")) {
			totaVo.putParam("L5R16AccCollPsn", "");
			totaVo.putParam("L5R16LegalPsn", "");
		} else if (!iCollList.getAccCollPsn().equals("") || !iCollList.getLegalPsn().equals("")) {
			totaVo.putParam("L5R16AccCollPsn", iCollList.getAccCollPsn());
			totaVo.putParam("L5R16LegalPsn", iCollList.getLegalPsn());
		} else if (!iCollList.getAccCollPsn().equals("") || iCollList.getLegalPsn().equals("")) {
			totaVo.putParam("L5R16AccCollPsn", iCollList.getAccCollPsn());
			totaVo.putParam("L5R16LegalPsn", "");
		} else {
			totaVo.putParam("L5R16AccCollPsn", "");
			totaVo.putParam("L5R16LegalPsn", iCollList.getLegalPsn());
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}