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
import com.st1.itx.db.domain.ClMain;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.service.CdCityService;
import com.st1.itx.db.service.ClMainService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * CustId=X,10<br>
 * BORROWER=X,1<br>
 * END=X,1<br>
 */

@Service("L2919")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2919 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L2919.class);

	/* DB服務注入 */
	@Autowired
	public ClMainService sClMainService;
	@Autowired
	public CustMainService sCustMainService;
	@Autowired
	public CdCityService cdCityService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2919 ");
		this.totaVo.init(titaVo);

		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 100; // 122 * 400 = 48800

		// tita
		String iCustId = titaVo.getParam("CustId");
		// new table
		CustMain tCustMain = new CustMain();
		// new ArrayList
		List<ClMain> lClMain = new ArrayList<ClMain>();
		// 統編查客戶主檔CustUKey取擔保品主檔資料
		tCustMain = sCustMainService.custIdFirst(iCustId, titaVo);
		// 該統編不存在客戶主檔 拋錯
		if (tCustMain == null) {
			throw new LogicException(titaVo, "E2003", "L2919 該統編" + iCustId + "不存在於客戶主檔。");
		}
		String custUKey = tCustMain.getCustUKey();

		Slice<ClMain> slClMain = sClMainService.findCustUKey(custUKey, this.index, this.limit, titaVo);
		lClMain = slClMain == null ? null : slClMain.getContent();
		// 該統編查無擔保品主檔
		if (lClMain == null) {
			throw new LogicException(titaVo, "E2003", "L2919 該統編" + iCustId + "不存在於擔保品主檔。");
		}
		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (slClMain != null && slClMain.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			/* 手動折返 */
			this.totaVo.setMsgEndToEnter();
		}
		for (ClMain tClMain : lClMain) {

			// new occurs
			OccursList occurslist = new OccursList();
			// new table
			tCustMain = new CustMain();

			tCustMain = sCustMainService.findById(tClMain.getCustUKey(), titaVo);

			// 取通訊名稱
			/* table 取值 */
			/* table-參數2:CityCode */
			String cityCode = tClMain.getCityCode();
			this.info("縣市 = " + cityCode);

			CdCity tCdCity = new CdCity();

			if (!(cityCode.isEmpty() || cityCode == null)) {
				/* 取縣市名稱 */
				tCdCity = cdCityService.findById(cityCode, titaVo);
				if (tCdCity == null) {
					tCdCity = new CdCity();
				}
				this.info("戶籍縣市   " + tCdCity.getCityItem());
			}

			occurslist.putParam("OOClCode1", tClMain.getClCode1());
			occurslist.putParam("OOClCode2", tClMain.getClCode2());
			occurslist.putParam("OOClNo", tClMain.getClNo());
			occurslist.putParam("OOClTypeCode", tClMain.getClTypeCode());
//			occurslist.putParam("OOCityCode", tClMain.getCityCode());
			occurslist.putParam("OOCityItem", tCdCity.getCityItem());
			occurslist.putParam("OOCustName", tCustMain.getCustName());
			occurslist.putParam("OOCustNo", tCustMain.getCustNo());

			/* 將每筆資料放入Tota的OcList */
			this.totaVo.addOccursList(occurslist);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}