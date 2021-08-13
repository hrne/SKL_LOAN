package com.st1.itx.trade.L5;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CollTel;
import com.st1.itx.db.domain.CollTelId;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.CollListService;
import com.st1.itx.db.service.CollTelService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L5R18")
@Scope("prototype")
/**
 * 電催調rim使用
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L5R18 extends TradeBuffer {

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
	public CollTelService iCollTelService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		// L5401交易內調RIM用,資料來源為CdBcm表
		this.info("active L5R18 ");
		this.info("L5R18 titaVo=[" + titaVo + "]");
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
		CollTelId iCollTelId = new CollTelId();
		CollTel iCollTel = new CollTel();

		iCollTelId.setAcDate(iRimAcDate);
		iCollTelId.setCaseCode(iRimCaseCode);
		iCollTelId.setCustNo(Integer.valueOf(iRimCustNo));
		iCollTelId.setFacmNo(Integer.valueOf(iRimFacmNo));
		iCollTelId.setTitaTlrNo(iRimTitaTlrNo);
		iCollTelId.setTitaTxtNo(iRimTitaTxtNo);

		iCollTel = iCollTelService.findById(iCollTelId, titaVo);
		if (iCollTel != null) {
			totaVo.putParam("L5R18TelDate", iCollTel.getTelDate());
//			totaVo.putParam("L5R18TelTime", iCollTel.getTelTime());
			String iTime = iCollTel.getTelTime().replaceAll(":", "");
			totaVo.putParam("L5R18TelTime", iTime);
			totaVo.putParam("L5R18ContactCode", iCollTel.getContactCode());
			totaVo.putParam("L5R18RecvrCode", iCollTel.getRecvrCode());
			totaVo.putParam("L5R18TelArea", iCollTel.getTelArea());
			totaVo.putParam("L5R18TelNo", iCollTel.getTelNo());
			totaVo.putParam("L5R18TelExt", iCollTel.getTelExt());
			totaVo.putParam("L5R18ResultCode", iCollTel.getResultCode());
			totaVo.putParam("L5R18CallDate", iCollTel.getCallDate());
			totaVo.putParam("L5R18ReMark", iCollTel.getRemark());
		} else {
			throw new LogicException(titaVo, "E0001", ""); // 查無資料錯誤
		}

		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 500;

		this.addList(this.totaVo);
		return this.sendList();
	}
}