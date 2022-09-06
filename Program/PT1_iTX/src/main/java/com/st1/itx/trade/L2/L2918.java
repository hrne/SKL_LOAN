package com.st1.itx.trade.L2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.ClFac;
import com.st1.itx.db.domain.ClOtherRights;
import com.st1.itx.db.service.ClFacService;
import com.st1.itx.db.service.ClOtherRightsService;
import com.st1.itx.db.service.springjpa.cm.L2918ServiceImpl;
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
	public ClFacService clFacService;
	@Autowired
	public L2918ServiceImpl l2918ServiceImpl;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;
	/* 轉換工具 */
	@Autowired
	public Parse parse;

	int wkClCode1St = 1;
	int wkClCode1Ed = 9;
	int wkClCode2St = 1;
	int wkClCode2Ed = 99;
	int wkClNoSt = 1;
	int wkClNoEd = 9999999;
	int clCode1 = 0;
	int clCode2 = 0;
	int clNo = 0;
	String clOtherrightsSeq = "";
	String otherCity = "";
	String otherLandAdm = "";
	int recYear = 0;
	int custNo = 0;
	String otherRecWord = "";
	String recNumber = "";
	String rightsNote = "";
	String securedTotal = "";
	OccursList occurslist = new OccursList();
	// new ArrayList
	List<ClOtherRights> lClOtherRights = new ArrayList<ClOtherRights>();
	List<ClFac> lClFac = new ArrayList<ClFac>();

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
		int iCustNo = parse.stringToInteger(titaVo.getParam("CustNo"));
		int iFacmNo = parse.stringToInteger(titaVo.getParam("FacmNo"));
		int iClCode1 = parse.stringToInteger(titaVo.getParam("ClCode1"));
		int iClCode2 = parse.stringToInteger(titaVo.getParam("ClCode2"));
		int iClNo = parse.stringToInteger(titaVo.getParam("ClNo"));
		this.info("iCustNo = " + iCustNo);
		this.info("iFacmNo = " + iFacmNo);
		this.info("iClCode1 = " + iClCode1);
		this.info("iClCode2 = " + iClCode2);
		this.info("iClNo = " + iClNo);

		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();

		try {
			resultList = l2918ServiceImpl.findAll(this.index, this.limit, titaVo);
		} catch (Exception e) {
			this.info("Error ... " + e.getMessage());
		}

		this.info("resultList = " + resultList);
		if (resultList != null && resultList.size() != 0) {

			this.info("Size =" + resultList.size());

			for (Map<String, String> result : resultList) {
				occurslist = new OccursList();

				clCode1 = parse.stringToInteger(result.get("ClCode1"));
				clCode2 = parse.stringToInteger(result.get("ClCode2"));
				clNo = parse.stringToInteger(result.get("ClNo"));
				clOtherrightsSeq = result.get("Seq");
				otherCity = result.get("CityItem");
				otherLandAdm = result.get("LandAdm");
				recYear = parse.stringToInteger(result.get("RecYear"));
				otherRecWord = result.get("RecWordItem");
				recNumber = result.get("RecNumber");
				rightsNote = result.get("RightsNote");
				securedTotal = result.get("SecuredTotal");
				custNo = parse.stringToInteger(result.get("CustNo"));

				moveOccursList(titaVo);
			}
		}
		if (resultList != null && resultList.size() >= this.limit) {
			/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
			titaVo.setReturnIndex(this.setIndexNext());
			this.totaVo.setMsgEndToEnter();// 手動折返
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void moveOccursList(TitaVo titaVo) throws LogicException {

		occurslist.putParam("OOClCode1", clCode1);
		occurslist.putParam("OOClCode2", clCode2);
		occurslist.putParam("OOClNo", clNo);
		occurslist.putParam("OOClSeq", clOtherrightsSeq);
		occurslist.putParam("OOCityItem", otherCity);
		occurslist.putParam("OOLandAdmItem", otherLandAdm);
		occurslist.putParam("OORecYear", recYear);
		occurslist.putParam("OORecWordItem", otherRecWord);
		occurslist.putParam("OORecNumber", recNumber);
		occurslist.putParam("OORightsNote", rightsNote);
		occurslist.putParam("OOSecuredTotal", securedTotal);
		occurslist.putParam("OOCustNo", custNo);

		/* 將每筆資料放入Tota的OcList */
		this.totaVo.addOccursList(occurslist);

	}

}