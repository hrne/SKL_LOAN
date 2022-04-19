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
import com.st1.itx.db.domain.CdArea;
import com.st1.itx.db.domain.CdAreaId;
import com.st1.itx.db.domain.CdCity;
import com.st1.itx.db.domain.CdLandSection;
import com.st1.itx.db.domain.CdLandSectionId;
import com.st1.itx.db.domain.ClLand;
import com.st1.itx.db.domain.ClLandId;
import com.st1.itx.db.service.CdAreaService;
import com.st1.itx.db.service.CdCityService;
import com.st1.itx.db.service.CdLandSectionService;
import com.st1.itx.db.service.ClLandService;
import com.st1.itx.db.service.ClMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L2041")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2041 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public ClMainService sClMainService;

	/* DB服務注入 */
	@Autowired
	public ClLandService sClLandService;

	/* DB服務注入 */
	@Autowired
	public CdCityService cdCityService;
	@Autowired
	public CdAreaService cdAreaService;
	@Autowired
	public CdLandSectionService cdLandSectionService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2041 ");
		this.totaVo.init(titaVo);
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 100; // 29 * 500 = 14500

		// tita
		int iClCode1 = parse.stringToInteger(titaVo.getParam("ClCode1"));
		int iClCode2 = parse.stringToInteger(titaVo.getParam("ClCode2"));
		int iClNo = parse.stringToInteger(titaVo.getParam("ClNo"));

		// new ArrayList ClLand
		List<ClLand> lClLand = new ArrayList<ClLand>();
		Slice<ClLand> slClLand = null;
		new ClLand();

		// new ClImm PK
		ClLandId ClLandId = new ClLandId();

		ClLandId.setClCode1(iClCode1);
		ClLandId.setClCode2(iClCode2);
		ClLandId.setClNo(iClNo);

		// 擔保品編號有輸入
		if (iClNo > 0) {
			slClLand = sClLandService.findClNo(iClCode1, iClCode2, iClNo, this.index, this.limit, titaVo);
			lClLand = slClLand == null ? null : slClLand.getContent();
			// 查無資料拋錯
			if (slClLand == null) {
				throw new LogicException("E0001", "L2041該擔保品編號在擔保品不動產土地檔無資料");
			}

			// 擔保品編號沒輸入
		} else {
			slClLand = sClLandService.findClCode2(iClCode1, iClCode2, this.index, this.limit, titaVo);
			lClLand = slClLand == null ? null : slClLand.getContent();
			if (lClLand == null) {
				throw new LogicException("E0001", "L2041該擔保品代號2在擔保品不動產土地檔無資料");
			}
		}
		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (slClLand != null && slClLand.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			/* 手動折返 */
			this.totaVo.setMsgEndToEnter();
		}

		for (ClLand tClLand2 : lClLand) {

			/* table 取值 */
			/* table-參數2:CityCode */
			String cityCode = parse.IntegerToString(parse.stringToInteger(tClLand2.getCityCode()), 2);
			/* table-參數3:AreaCode */
			String areaCode = parse.IntegerToString(parse.stringToInteger(tClLand2.getAreaCode()), 2);
			/* table-參數3:AreaCode */
			String Land = parse.IntegerToString(parse.stringToInteger(tClLand2.getIrCode()), 4);

			// new PK
			CdLandSectionId CdLandSectionId = new CdLandSectionId();
			CdLandSectionId.setCityCode(cityCode);
			CdLandSectionId.setAreaCode(areaCode);
			CdLandSectionId.setIrCode(Land);

			/* 取縣市名稱 */
			CdCity tCdCity = cdCityService.findById(cityCode, titaVo);

			String wkCdCityItem = "";
			if (tCdCity != null) {
				wkCdCityItem = tCdCity.getCityItem();
			}

			/* 取行政區名稱 */

			CdArea tCdArea = cdAreaService.findById(new CdAreaId(cityCode, areaCode), titaVo);
			String wkCdArea = "";
			if (tCdArea != null) {
				wkCdArea = tCdArea.getAreaItem();
			}

			/* 取段小段 */
			CdLandSection tCdLandSection = cdLandSectionService.findById(CdLandSectionId, titaVo);

			String wkIrItem = "";
			if (tCdLandSection != null) {
				wkIrItem = tCdLandSection.getIrItem();
			}
			this.info("L2041 tCdLandSection " + tCdLandSection);

			// new occurs
			OccursList occurslist = new OccursList();

			occurslist.putParam("OOClCode1", tClLand2.getClCode1());
			occurslist.putParam("OOClCode2", tClLand2.getClCode2());
			occurslist.putParam("OOClNo", tClLand2.getClNo());
			occurslist.putParam("OOLandSeq", tClLand2.getLandSeq());
			occurslist.putParam("OOCityCode", wkCdCityItem);
			occurslist.putParam("OOAreaCode", wkCdArea);
			occurslist.putParam("OOIrCode", wkIrItem);
			occurslist.putParam("OOLandNo1", tClLand2.getLandNo1());
			occurslist.putParam("OOLandNo2", tClLand2.getLandNo2());

			/* 將每筆資料放入Tota的OcList */
			this.totaVo.addOccursList(occurslist);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}