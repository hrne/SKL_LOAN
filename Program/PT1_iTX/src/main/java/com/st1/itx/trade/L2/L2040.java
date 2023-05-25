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
import com.st1.itx.db.domain.ClBuilding;
import com.st1.itx.db.domain.ClBuildingId;
import com.st1.itx.db.domain.ClNoMap;
import com.st1.itx.db.service.ClBuildingService;
import com.st1.itx.db.service.ClFacService;
import com.st1.itx.db.service.ClNoMapService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L2040")
@Scope("prototype")

public class L2040 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	ClNoMapService sClNoMapService;

	@Autowired
	ClFacService sClFacService;

	@Autowired
	ClBuildingService sClBuildingService;

	/* 日期工具 */
	@Autowired
	DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2040 ");
		this.totaVo.init(titaVo);
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 100; // 29 * 500 = 14500

		// tita
		int iFunCd = parse.stringToInteger(titaVo.getParam("FunCd"));
		int iClCode1 = parse.stringToInteger(titaVo.getParam("ClCode1"));
		int iClCode2 = parse.stringToInteger(titaVo.getParam("ClCode2"));
		int iClNo = parse.stringToInteger(titaVo.getParam("ClNo"));
		int iClSeq = parse.stringToInteger(titaVo.getParam("ClSeq"));

		List<ClNoMap> lClNoMap = new ArrayList<ClNoMap>();
		Slice<ClNoMap> sClNoMap = null;
		if (iFunCd == 1) { // 原擔保品 查新的
			if (iClSeq == 0) { // 查全部
				sClNoMap = sClNoMapService.findGdrNum(iClCode1, iClCode2, iClNo, 0, Integer.MAX_VALUE, titaVo);
			} else {
				sClNoMap = sClNoMapService.findGdrNum2(iClCode1, iClCode2, iClNo, iClSeq, 0, Integer.MAX_VALUE, titaVo);
			}
		} else { // 新擔保品 查舊的
			sClNoMap = sClNoMapService.findNewClNo(iClCode1, iClCode2, iClNo, 0, Integer.MAX_VALUE, titaVo);
		}

		lClNoMap = sClNoMap == null ? null : sClNoMap.getContent();

		if (lClNoMap != null) {
			for (ClNoMap tClNoMap : lClNoMap) {
				OccursList occurslist = new OccursList();

				if (iFunCd == 1) { // 新的
					occurslist.putParam("OOClCode1", tClNoMap.getClCode1());
					occurslist.putParam("OOClCode2", tClNoMap.getClCode2());
					occurslist.putParam("OOClNo", tClNoMap.getClNo());
					occurslist.putParam("OOClSeq", tClNoMap.getLgtSeq());
				} else { // 舊的
					occurslist.putParam("OOClCode1", tClNoMap.getGdrId1());
					occurslist.putParam("OOClCode2", tClNoMap.getGdrId2());
					occurslist.putParam("OOClNo", tClNoMap.getGdrNum());
					occurslist.putParam("OOClSeq", tClNoMap.getLgtSeq());
				}

				int tClCode1 = tClNoMap.getClCode1();
				int tClCode2 = tClNoMap.getClCode2();
				int tClNo = tClNoMap.getClNo();

				// 2023-05-23 Wei from QC:2426 要能查到舊擔保品綁在哪個額度
				int oriCustNo = tClNoMap.getOriCustNo();
				int oriFacmNo = tClNoMap.getOriFacmNo();

				occurslist.putParam("OOCustNo", oriCustNo);
				occurslist.putParam("OOFacmNo", oriFacmNo);

				if (tClCode1 == 1) {
					ClBuildingId clBuildingId = new ClBuildingId();
					clBuildingId.setClCode1(tClCode1);
					clBuildingId.setClCode2(tClCode2);
					clBuildingId.setClNo(tClNo);
					ClBuilding tClBuilding = new ClBuilding();
					tClBuilding = sClBuildingService.findById(clBuildingId, titaVo);

					if (tClBuilding != null) {
						occurslist.putParam("OOAddress", tClBuilding.getBdLocation() + "，建號" + tClBuilding.getBdNo1()
								+ "-" + tClBuilding.getBdNo2());
					} else {
						occurslist.putParam("OOAddress", "");
					}

				} else {
					occurslist.putParam("OOAddress", "");
				} // else

				// 2023-05-25 Wei 增加 for 連動L2943 from SKL-佳怡:新系統要可以查舊系統建物明細
				occurslist.putParam("OOGdrid1", tClNoMap.getGdrId1());
				occurslist.putParam("OOGdrid2", tClNoMap.getGdrId2());
				occurslist.putParam("OOGdrnum", tClNoMap.getGdrNum());
				occurslist.putParam("OOLgtseq", tClNoMap.getLgtSeq());
				
				/* 將每筆資料放入Tota的OcList */
				this.totaVo.addOccursList(occurslist);
			}

		} else {
			throw new LogicException(titaVo, "E0001", "");
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}