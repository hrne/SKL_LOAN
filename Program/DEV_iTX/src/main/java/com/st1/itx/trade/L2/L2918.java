package com.st1.itx.trade.L2;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdCity;
import com.st1.itx.db.domain.CdCode;
import com.st1.itx.db.domain.CdCodeId;
import com.st1.itx.db.domain.CdLandOffice;
import com.st1.itx.db.domain.CdLandOfficeId;
import com.st1.itx.db.domain.ClOtherRights;
import com.st1.itx.db.service.CdCityService;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.CdLandOfficeService;
import com.st1.itx.db.service.ClOtherRightsService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L2918")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2918 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public ClOtherRightsService sClOtherRightsService;
	@Autowired
	public CdCodeService cdCodeService;
	@Autowired
	public CdCityService cdCityService;
	@Autowired
	public CdLandOfficeService cdLandOfficeService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;
	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2918 ");
		this.totaVo.init(titaVo);

		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();
		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 100; // 122 * 400 = 48800

		// tita
		int iClCode1 = parse.stringToInteger(titaVo.getParam("ClCode1"));
		int iClCode2 = parse.stringToInteger(titaVo.getParam("ClCode2"));
		int iClNo = parse.stringToInteger(titaVo.getParam("ClNo"));

		int wkClCode1St = 1;
		int wkClCode1Ed = 9;
		int wkClCode2St = 1;
		int wkClCode2Ed = 99;
		int wkClNoSt = 1;
		int wkClNoEd = 9999999;
		if (iClCode1 > 0) {
			wkClCode1St = iClCode1;
			wkClCode1Ed = iClCode1;
		}
		if (iClCode2 > 0) {
			wkClCode2St = iClCode2;
			wkClCode2Ed = iClCode2;
		}
		if (iClNo > 0) {
			wkClNoSt = iClNo;
			wkClNoEd = iClNo;
		}

		// new ArrayList
		List<ClOtherRights> lClOtherRights = new ArrayList<ClOtherRights>();
		Slice<ClOtherRights> slClOtherRights = sClOtherRightsService.findClCodeRange(wkClCode1St, wkClCode1Ed,
				wkClCode2St, wkClCode2Ed, wkClNoSt, wkClNoEd, this.index, this.limit, titaVo);

		lClOtherRights = slClOtherRights == null ? null : slClOtherRights.getContent();
		// 該統編查無擔保品主檔
		if (lClOtherRights == null) {
			throw new LogicException(titaVo, "E2003", "不存在於擔保品他項權利檔。");
		}

		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (slClOtherRights != null && slClOtherRights.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			/* 手動折返 */
			this.totaVo.setMsgEndToEnter();
		}

		for (ClOtherRights t : lClOtherRights) {
			// wk
			String wkCityItem = "";
			String wkLandOfficeItem = "";
			String wkRecWordItem = "";

			// new occurs
			OccursList occurslist = new OccursList();

			occurslist.putParam("OOClCode1", t.getClCode1());
			occurslist.putParam("OOClCode2", t.getClCode2());
			occurslist.putParam("OOClNo", t.getClNo());
			occurslist.putParam("OOClSeq", t.getSeq());
			// 找縣市名稱
			if ("".equals(t.getOtherCity())) {
				CdCity tCdCity = cdCityService.findById(t.getCity(), titaVo);
				if (tCdCity != null) {
					wkCityItem = tCdCity.getCityItem();
				}
			} else {
				wkCityItem = t.getOtherCity();
			}
			occurslist.putParam("OOCityItem", wkCityItem);
			// 找地政所名稱
			if ("".equals(t.getOtherLandAdm())) {
				CdCode tCdCode = cdCodeService.findById(new CdCodeId("LandOfficeCode", t.getLandAdm()), titaVo);
				if (tCdCode != null) {
					wkLandOfficeItem = tCdCode.getItem();
				}
			} else {
				wkLandOfficeItem = t.getOtherLandAdm();
			}
			occurslist.putParam("OOLandAdmItem", wkLandOfficeItem);
			occurslist.putParam("OORecYear", t.getRecYear());
			// 找 地政所名稱
			if ("".equals(t.getOtherRecWord())) {
				CdLandOffice tCdLandOffice = cdLandOfficeService
						.findById(new CdLandOfficeId(t.getLandAdm(), t.getRecWord()), titaVo);
				if (tCdLandOffice != null) {
					wkRecWordItem = tCdLandOffice.getRecWordItem();
				}
			} else {
				wkRecWordItem = t.getOtherRecWord();
			}
			occurslist.putParam("OORecWordItem", wkRecWordItem);
			occurslist.putParam("OORecNumber", t.getRecNumber());
			occurslist.putParam("OORightsNote", t.getRightsNote());
			occurslist.putParam("OOSecuredTotal", t.getSecuredTotal());

			/* 將每筆資料放入Tota的OcList */
			this.totaVo.addOccursList(occurslist);
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}