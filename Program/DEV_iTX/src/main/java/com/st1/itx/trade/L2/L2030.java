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
import com.st1.itx.db.domain.RelsCompany;
import com.st1.itx.db.domain.RelsFamily;
import com.st1.itx.db.domain.RelsMain;
import com.st1.itx.db.service.RelsCompanyService;
import com.st1.itx.db.service.RelsFamilyService;
import com.st1.itx.db.service.RelsMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L2030")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2030 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L2030.class);

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
		this.info("active L2030 ");
		this.totaVo.init(titaVo);
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 100; // 23 * 500 = 11500

		// tita 選項 0全部 1自然人 2法人
		String iOption = titaVo.getParam("Option");
		// tita 統一編號
		String iRelsId = titaVo.getParam("RelsId");
		// new ArrayList
		List<RelsMain> lRelsMain = new ArrayList<RelsMain>();
		Slice<RelsMain> slRelsMain = null;
		// 選項為0 找全部
		if (!iOption.isEmpty()) {
			if (iOption.equals("0")) {
				slRelsMain = sRelsMainService.findAll(this.index, this.limit, titaVo);
				lRelsMain = slRelsMain == null ? null : slRelsMain.getContent();
			} else {
				slRelsMain = sRelsMainService.RelsPerson(parse.stringToInteger(iOption), this.index, this.limit,
						titaVo);
				lRelsMain = slRelsMain == null ? null : slRelsMain.getContent();
			}

		} else if (!iRelsId.isEmpty()) {
			slRelsMain = sRelsMainService.RelsIdEq(iRelsId, this.index, this.limit, titaVo);
			lRelsMain = slRelsMain == null ? null : slRelsMain.getContent();
		} else {
			slRelsMain = sRelsMainService.findAll(this.index, this.limit, titaVo);
			lRelsMain = slRelsMain == null ? null : slRelsMain.getContent();
		}
		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (slRelsMain != null && slRelsMain.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			/* 手動折返 */
			this.totaVo.setMsgEndToEnter();
		}

		// 查無資料 拋錯
		if (lRelsMain == null) {
			throw new LogicException("E2003", "(準)利害關係人檔");
		}

		for (RelsMain tRelsMain : lRelsMain) {
			// new occurslist
			OccursList OccursList = new OccursList();
			List<RelsFamily> lRelsFamily = new ArrayList<RelsFamily>();
			List<RelsCompany> lRelsCompany = new ArrayList<RelsCompany>();
			String relsUKey = tRelsMain.getRelsUKey();

			OccursList.putParam("OORelsId", tRelsMain.getRelsId());
			OccursList.putParam("OORelsName", tRelsMain.getRelsName());
			OccursList.putParam("OORelsCode", tRelsMain.getRelsCode());
			OccursList.putParam("OORelsType", tRelsMain.getRelsType());

			/* 將每筆資料放入Tota的OcList */
			this.totaVo.addOccursList(OccursList);

		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}
