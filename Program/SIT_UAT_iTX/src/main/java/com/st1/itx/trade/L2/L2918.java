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
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.ClOtherRights;
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
	private static final Logger logger = LoggerFactory.getLogger(L2918.class);

	/* DB服務注入 */
	@Autowired
	public ClOtherRightsService sClOtherRightsService;

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

		for (ClOtherRights tClOtherRights : lClOtherRights) {

			// new occurs
			OccursList occurslist = new OccursList();

			occurslist.putParam("OOClCode1", tClOtherRights.getClCode1());
			occurslist.putParam("OOClCode2", tClOtherRights.getClCode2());
			occurslist.putParam("OOClNo", tClOtherRights.getClNo());
			occurslist.putParam("OOClSeq", tClOtherRights.getSeq());
			occurslist.putParam("OOCity", tClOtherRights.getCity());
			occurslist.putParam("OOLandAdm", tClOtherRights.getLandAdm());
			occurslist.putParam("OORecYear", tClOtherRights.getRecYear());
			occurslist.putParam("OORecWord", tClOtherRights.getRecWord());
			occurslist.putParam("OORecNumber", tClOtherRights.getRecNumber());
			occurslist.putParam("OORightsNote", tClOtherRights.getRightsNote());
			occurslist.putParam("OOSecuredTotal", tClOtherRights.getSecuredTotal());

			/* 將每筆資料放入Tota的OcList */
			this.totaVo.addOccursList(occurslist);
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}