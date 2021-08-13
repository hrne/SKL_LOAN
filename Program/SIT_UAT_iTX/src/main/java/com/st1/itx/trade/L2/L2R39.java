package com.st1.itx.trade.L2;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.RelsCompany;
import com.st1.itx.db.domain.RelsFamily;
import com.st1.itx.db.domain.RelsMain;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.RelsCompanyService;
import com.st1.itx.db.service.RelsFamilyService;
import com.st1.itx.db.service.RelsMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L2R39")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2R39 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L2R39.class);

	/* DB服務注入 */
	@Autowired
	public CustMainService sCustMainService;

	/* DB服務注入 */
	@Autowired
	public RelsMainService sRelsMainService;

	/* DB服務注入 */
	@Autowired
	public RelsFamilyService sRelsFamilyService;

	/* DB服務注入 */
	@Autowired
	public RelsCompanyService sRelsCompanyService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2R39 ");
		this.totaVo.init(titaVo);

		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 500;

		// tita
		// 身分證字號 CustId
		String iCustId = titaVo.getParam("RimCustId");
		// 姓名 CustName
		String iCustName = titaVo.getParam("RimCustName");
		// 戶號 CustNo
		int iCustNo = parse.stringToInteger(titaVo.getParam("RimCustNo"));

		// 宣告 選項 Option 1:是否為關係人;2:是否為親屬;3:是否為相關事業
		int iOption = 0;

		// new ArrayList
		List<RelsCompany> lRelsCompany = new ArrayList<RelsCompany>();
		List<RelsFamily> lRelsFamily = new ArrayList<RelsFamily>();

		// new table
		RelsMain tRelsMain = new RelsMain();
		CustMain tCustMain = new CustMain();

		if (!iCustId.isEmpty()) {
			this.info("iCustId 統編輸入   ");
			Slice<RelsCompany> slRelsCompany = sRelsCompanyService.findCompanyIdEq(iCustId, 0, Integer.MAX_VALUE,
					titaVo);
			lRelsCompany = slRelsCompany == null ? null : slRelsCompany.getContent();
			this.info("lRelsCompany 統編輸入   = " + lRelsCompany);
			Slice<RelsFamily> slRelsFamily = sRelsFamilyService.findFamilyIdEq(iCustId, 0, Integer.MAX_VALUE, titaVo);
			lRelsFamily = slRelsFamily == null ? null : slRelsFamily.getContent();
			this.info("lRelsFamily 統編輸入   = " + lRelsFamily);
			tRelsMain = sRelsMainService.RelsIdFirst(iCustId, titaVo);
			this.info("tRelsMain 統編輸入   = " + tRelsMain);
		} else if (!iCustName.isEmpty()) {
			this.info("iCustName 姓名輸入   ");
			Slice<RelsCompany> slRelsCompany = sRelsCompanyService.findCompanyNameEq(iCustName, 0, Integer.MAX_VALUE,
					titaVo);
			lRelsCompany = slRelsCompany == null ? null : slRelsCompany.getContent();
			this.info("lRelsCompany 姓名輸入   = " + lRelsCompany);
			Slice<RelsFamily> slRelsFamily = sRelsFamilyService.findFamilyNameEq(iCustName, 0, Integer.MAX_VALUE,
					titaVo);
			lRelsFamily = slRelsFamily == null ? null : slRelsFamily.getContent();
			this.info("lRelsFamily 姓名輸入   = " + lRelsFamily);
			tRelsMain = sRelsMainService.RelsNameFirst(iCustName, titaVo);
			this.info("tRelsMain 姓名輸入   = " + tRelsMain);
		} else {
			this.info("iCustNo 戶號輸入   ");
			tCustMain = sCustMainService.custNoFirst(iCustNo, iCustNo, titaVo);
			if (tCustMain == null) {
				throw new LogicException(titaVo, "E2003", "該戶號不存在客戶主檔"); // 查無資料
			}
			String custid = tCustMain.getCustId();
			Slice<RelsCompany> slRelsCompany = sRelsCompanyService.findCompanyIdEq(custid, 0, Integer.MAX_VALUE,
					titaVo);
			lRelsCompany = slRelsCompany == null ? null : slRelsCompany.getContent();
			this.info("lRelsCompany 戶號輸入   = " + lRelsCompany);
			Slice<RelsFamily> slRelsFamily = sRelsFamilyService.findFamilyIdEq(custid, 0, Integer.MAX_VALUE, titaVo);
			lRelsFamily = slRelsFamily == null ? null : slRelsFamily.getContent();
			this.info("lRelsFamily 戶號輸入   = " + lRelsFamily);
			tRelsMain = sRelsMainService.RelsIdFirst(custid, titaVo);
			this.info("tRelsMain 戶號輸入   = " + tRelsMain);

		}

		if (lRelsCompany != null) {
			iOption = 3;
			this.info("iOption  3 = " + iOption);
		}
		if (lRelsFamily != null) {
			iOption = 2;
			this.info("iOption  2 = " + iOption);
		}
		if (tRelsMain != null) {
			iOption = 1;
			this.info("iOption  1 = " + iOption);
		}

		this.totaVo.putParam("L2r39Option", iOption);

		this.addList(this.totaVo);
		return this.sendList();
	}
}