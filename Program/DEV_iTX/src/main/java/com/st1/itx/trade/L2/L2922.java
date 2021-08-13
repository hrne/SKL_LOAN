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
import com.st1.itx.db.domain.CdCity;
import com.st1.itx.db.domain.CdLandSection;
import com.st1.itx.db.domain.CdLandSectionId;
import com.st1.itx.db.domain.ClImm;
import com.st1.itx.db.domain.ClImmId;
import com.st1.itx.db.domain.ClLand;
import com.st1.itx.db.domain.ClMain;
import com.st1.itx.db.domain.ClMainId;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.service.CdAreaService;
import com.st1.itx.db.service.CdCityService;
import com.st1.itx.db.service.CdLandSectionService;
import com.st1.itx.db.service.ClImmService;
import com.st1.itx.db.service.ClLandService;
import com.st1.itx.db.service.ClMainService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * CityCode=X,2<br>
 * AreaCode=X,3<br>
 * IrCode=X,5<br>
 * LandNo1=X,4<br>
 * LandNo2=X,4<br>
 * END=X,1<br>
 */

@Service("L2922")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2922 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L2922.class);

	/* DB服務注入 */
	@Autowired
	public ClLandService sClLandService;

	/* DB服務注入 */
	@Autowired
	public ClMainService sClMainService;

	/* DB服務注入 */
	@Autowired
	public CustMainService sCustMainService;

	/* DB服務注入 */
	@Autowired
	public ClImmService sClImmService;

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
		this.info("active L2922 ");
		this.totaVo.init(titaVo);

		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 100; // 153 * 300 = 45900

		// tita
		// 縣市代碼
		String iCityCode = titaVo.getParam("CityCode");
		// 鄉鎮代碼
		String iAreaCode = titaVo.getParam("AreaCode");
		// 段小段代號
		String iIrCode = titaVo.getParam("IrCode");
		// 地號1 無輸入範圍0-9999
		int iLandNoStartAt1 = parse.stringToInteger(titaVo.getParam("LandNo1"));
		int iLandNoEndAt1 = 9999;
		if (iLandNoStartAt1 > 1) {
			iLandNoEndAt1 = iLandNoStartAt1;
		}

		// 地號2
		int iLandNoStartAt2 = parse.stringToInteger(titaVo.getParam("LandNo2"));
		int iLandNoEndAt2 = 9999;
		if (iLandNoStartAt2 > 1) {
			iLandNoEndAt2 = iLandNoStartAt2;
		}
		this.info("前L2922 iCityCode : " + iCityCode);
		this.info("前L2922 iCityCode : " + iAreaCode);
		this.info("前L2922 iCityCode : " + iIrCode);

		this.info("後L2922 iCityCode : " + String.valueOf(iCityCode));
		this.info("後L2922 iCityCode : " + String.valueOf(iAreaCode));
		this.info("後L2922 iCityCode : " + String.valueOf(iIrCode));

		// new ArrayList
		List<ClLand> lClLand = new ArrayList<ClLand>();

		// 測試該縣市.鄉鎮.段小段是否存在不動產土地檔
		Slice<ClLand> slClLand = sClLandService.findIrCode(String.valueOf(iCityCode), String.valueOf(iAreaCode),
				String.valueOf(iIrCode), String.valueOf(iLandNoStartAt1), String.valueOf(iLandNoEndAt1),
				String.valueOf(iLandNoStartAt2), String.valueOf(iLandNoEndAt2), this.index, this.limit, titaVo);
		lClLand = slClLand == null ? null : slClLand.getContent();
		// 如查無資料 拋錯
		if (lClLand == null) {
			throw new LogicException(titaVo, "E2003", "L2922 該土地坐落 不存在於不動產土地檔。");
		}
		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (slClLand != null && slClLand.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			/* 自動折返 */
			this.totaVo.setMsgEndToAuto();
		}
		for (ClLand tClLand : lClLand) {

			// new occurs
			OccursList occurslist = new OccursList();
			// new table
			ClMain tClMain = new ClMain();
			CustMain tCustMain = new CustMain();
			ClImm tClImm = new ClImm();

			// 取提供人
			tClMain = sClMainService
					.findById(new ClMainId(tClLand.getClCode1(), tClLand.getClCode2(), tClLand.getClNo()), titaVo);

			String custUKey = tClMain.getCustUKey();
			if (custUKey == null) {
				this.info(" 無CustUKey 無CustUKey 無CustUKey");
			}
			tCustMain = sCustMainService.findById(custUKey, titaVo);
			if (tCustMain == null) {
				tCustMain = new CustMain();
			}
			// 取設定順序
			tClImm = sClImmService.findById(new ClImmId(tClLand.getClCode1(), tClLand.getClCode2(), tClLand.getClNo()),
					titaVo);
			if (tClImm == null) {
				tClImm = new ClImm();
			}
			// 取名稱
			/* table 取值 */
			/* table-參數2:CityCode */
			String cityCode = tClLand.getCityCode();
			/* table-參數3:AreaCode */
			String areaCode = tClLand.getAreaCode();
			/* table-參數3:AreaCode */
			String Land = tClLand.getIrCode();
			// new PK
			CdLandSectionId CdLandSectionId = new CdLandSectionId();
			CdLandSectionId.setCityCode(cityCode);
			CdLandSectionId.setAreaCode(areaCode);
			CdLandSectionId.setIrCode(Land);

			/* 取縣市名稱 */
			Slice<CdCity> slCdCity = cdCityService.findCityCode(cityCode, cityCode, 0, Integer.MAX_VALUE, titaVo);
			List<CdCity> lCdCity = slCdCity == null ? null : slCdCity.getContent();
			CdCity tCdCity = lCdCity.get(0);

			this.info("縣 市   " + lCdCity);

			/* 取行政區名稱 */
			Slice<CdArea> slCdArea = cdAreaService.areaCodeRange(cityCode, cityCode, areaCode, areaCode, 0,
					Integer.MAX_VALUE, titaVo);
			List<CdArea> lCdArea = slCdArea == null ? null : slCdArea.getContent();
			CdArea tCdArea = lCdArea.get(0);
			this.info("縣 市   " + lCdArea);

			/* 取段小段 */
			CdLandSection tCdLandSection = cdLandSectionService.findById(CdLandSectionId, titaVo);
			this.info("L2041 tCdLandSection " + tCdLandSection);

			occurslist.putParam("OOCl", tClLand.getClCode1() + "-" + tClLand.getClCode2() + "-" + tClLand.getClNo());
			occurslist.putParam("OOProvider", tCustMain.getCustName());
			occurslist.putParam("OOSeq", tClImm.getSettingSeq());
			occurslist.putParam("OOCity", tCdCity.getCityItem());
			occurslist.putParam("OOArea", tCdArea.getAreaItem());
			occurslist.putParam("OOIr", tCdLandSection.getIrItem());

			/* 將每筆資料放入Tota的OcList */
			this.totaVo.addOccursList(occurslist);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}