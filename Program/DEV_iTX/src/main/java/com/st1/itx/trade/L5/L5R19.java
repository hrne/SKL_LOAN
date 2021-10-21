package com.st1.itx.trade.L5;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.domain.CollMeet;
import com.st1.itx.db.domain.CollMeetId;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.CollListService;
import com.st1.itx.db.service.CollMeetService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L5R19")
@Scope("prototype")
/**
 * 面催調rim使用
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L5R19 extends TradeBuffer {

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
	public CollMeetService iCollMeetService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		// L5401交易內調RIM用,資料來源為CdBcm表
		this.info("active L5R19 ");
		this.info("L5R19 titaVo=[" + titaVo + "]");
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
		CollMeetId iCollMeetId = new CollMeetId();
		CollMeet iCollMeet = new CollMeet();
		CdEmp iCdEmp = new CdEmp();

		iCollMeetId.setAcDate(iRimAcDate);
		iCollMeetId.setCaseCode(iRimCaseCode);
		iCollMeetId.setCustNo(Integer.valueOf(iRimCustNo));
		iCollMeetId.setFacmNo(Integer.valueOf(iRimFacmNo));
		iCollMeetId.setTitaTlrNo(iRimTitaTlrNo);
		iCollMeetId.setTitaTxtNo(iRimTitaTxtNo);

		iCollMeet = iCollMeetService.findById(iCollMeetId, titaVo);
		if (iCollMeet != null) {
			totaVo.putParam("L5R19MeetDate", iCollMeet.getMeetDate());
			totaVo.putParam("L5R19MeetTime", iCollMeet.getMeetTime().trim());
			totaVo.putParam("L5R19ContactCode", iCollMeet.getContactCode());
			totaVo.putParam("L5R19MeetPsnCode", iCollMeet.getMeetPsnCode());
			totaVo.putParam("L5R19CollPsnCode", iCollMeet.getCollPsnCode());
			totaVo.putParam("L5R19CollPsnName", iCollMeet.getCollPsnName());
			totaVo.putParam("L5R19MeetPlaceCode", iCollMeet.getMeetPlaceCode());
			totaVo.putParam("L5R19MeetPlace", iCollMeet.getMeetPlace());
			totaVo.putParam("L5R19EditEmpNo", iCollMeet.getLastUpdateEmpNo());
			iCdEmp = sCdEmpService.findById(iCollMeet.getCollPsnName(), titaVo);
			if (iCdEmp != null && !iCdEmp.getFullname().equals("")) {
				totaVo.putParam("L5R19CollPsnNameX", iCdEmp.getFullname());
			} else {
				totaVo.putParam("L5R19CollPsnNameX", "");
			}
			totaVo.putParam("L5R19ReMark", iCollMeet.getRemark());
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