package com.st1.itx.trade.L5;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CollLetter;
import com.st1.itx.db.domain.CollLetterId;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.CollLetterService;
import com.st1.itx.db.service.CollListService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L5R20")
@Scope("prototype")
/**
 * 函催調rim使用
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L5R20 extends TradeBuffer {

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
	public CollLetterService iCollLetterService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		// L5401交易內調RIM用,資料來源為CdBcm表
		this.info("active L5R20 ");
		this.info("L5R20 titaVo=[" + titaVo + "]");
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
		CollLetterId iCollLetterId = new CollLetterId();
		CollLetter iCollLetter = new CollLetter();

		iCollLetterId.setAcDate(iRimAcDate);
		iCollLetterId.setCaseCode(iRimCaseCode);
		iCollLetterId.setCustNo(Integer.valueOf(iRimCustNo));
		iCollLetterId.setFacmNo(Integer.valueOf(iRimFacmNo));
		iCollLetterId.setTitaTlrNo(iRimTitaTlrNo);
		iCollLetterId.setTitaTxtNo(iRimTitaTxtNo);

		iCollLetter = iCollLetterService.findById(iCollLetterId, titaVo);
		if (iCollLetter != null) {
			totaVo.putParam("L5R20MailTypeCode", iCollLetter.getMailTypeCode());
			totaVo.putParam("L5R20MailDate", iCollLetter.getMailDate());
			totaVo.putParam("L5R20MailObj", iCollLetter.getMailObj());
			totaVo.putParam("L5R20CustName", iCollLetter.getCustName());
			totaVo.putParam("L5R20DelvrYet", iCollLetter.getDelvrYet());
			totaVo.putParam("L5R20DelvrCode", iCollLetter.getDelvrCode());
			totaVo.putParam("L5R20Address", iCollLetter.getAddress());
			totaVo.putParam("L5R20ReMark", iCollLetter.getRemark());
			totaVo.putParam("L5R20AddressCode", iCollLetter.getAddressCode());
			totaVo.putParam("L5R20EditEmpNo", iCollLetter.getLastUpdateEmpNo());
		} else {
			throw new LogicException(titaVo, "E0001", ""); // 查無資料錯誤
		}

		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 200;

		this.addList(this.totaVo);
		return this.sendList();
	}
}