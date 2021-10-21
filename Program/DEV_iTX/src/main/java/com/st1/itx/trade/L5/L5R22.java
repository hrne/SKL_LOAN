package com.st1.itx.trade.L5;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CollRemind;
import com.st1.itx.db.domain.CollRemindId;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.CollListService;
import com.st1.itx.db.service.CollRemindService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L5R22")
@Scope("prototype")
/**
 * 提醒調rim使用
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L5R22 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CdEmpService sCdEmpService;

	@Autowired
	public CollListService iCollListService;

	@Autowired
	public FacMainService sFacMainService;

	@Autowired
	public CustMainService iCustMainService;

	@Autowired
	public CollRemindService iCollRemindService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		// L5401交易內調RIM用,資料來源為CdBcm表
		this.info("active L5R22 ");
		this.info("L5R22 titaVo=[" + titaVo + "]");
		this.totaVo.init(titaVo);
		String iRimCaseCode = titaVo.getParam("RimCaseCode");
		String iRimCustNo = titaVo.getParam("RimCustNo");
		String iRimFacmNo = titaVo.getParam("RimFacmNo");
		int iRimAcDate = Integer.valueOf(titaVo.getParam("RimAcDate")) + 19110000;
		String iRimTitaTlrNo = titaVo.getParam("RimTitaTlrNo");
		String iRimTitaTxtNo = titaVo.getParam("RimTitaTxtNo");
		this.info("1=" + iRimAcDate);
		this.info("2=" + iRimTitaTlrNo);
		this.info("3=" + iRimTitaTxtNo);
		CollRemindId iCollRemindId = new CollRemindId();
		CollRemind iCollRemind = new CollRemind();

		iCollRemindId.setAcDate(iRimAcDate);
		iCollRemindId.setCaseCode(iRimCaseCode);
		iCollRemindId.setCustNo(Integer.valueOf(iRimCustNo));
		iCollRemindId.setFacmNo(Integer.valueOf(iRimFacmNo));
		iCollRemindId.setTitaTlrNo(iRimTitaTlrNo);
		iCollRemindId.setTitaTxtNo(iRimTitaTxtNo);

		iCollRemind = iCollRemindService.findById(iCollRemindId, titaVo);
		if (iCollRemind != null) {
			totaVo.putParam("L5R22CondCode", iCollRemind.getCondCode());
			totaVo.putParam("L5R22RemindDate", iCollRemind.getRemindDate());
			totaVo.putParam("L5R22EditDate", iCollRemind.getEditDate());
			totaVo.putParam("L5R22EditTime", iCollRemind.getEditTime().trim());
			totaVo.putParam("L5R22RemindCode", iCollRemind.getRemindCode());
			totaVo.putParam("L5R22ReMark", iCollRemind.getRemark());
			totaVo.putParam("L5R22EditEmpNo", iCollRemind.getLastUpdateEmpNo());
		} else {
			throw new LogicException(titaVo, "E0001", ""); // 查無資料錯誤
		}

		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 300;

		this.addList(this.totaVo);
		return this.sendList();
	}
}