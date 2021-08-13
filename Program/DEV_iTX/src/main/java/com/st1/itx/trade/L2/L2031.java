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
import com.st1.itx.db.domain.RelsFamily;
import com.st1.itx.db.domain.RelsMain;
import com.st1.itx.db.service.RelsFamilyService;
import com.st1.itx.db.service.RelsMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L2031")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2031 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L2031.class);

	/* DB服務注入 */
	@Autowired
	public RelsMainService sRelsMainService;

	/* DB服務注入 */
	@Autowired
	public RelsFamilyService sRelsFamilyService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2031 ");
		this.totaVo.init(titaVo);
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 100; // 41 * 500 = 20500

		// tita 選項 0全部 1自然人 2法人
		int iOption = parse.stringToInteger(titaVo.getParam("Option"));
		// tita 統一編號
		String iRelsId = titaVo.getParam("RelsId");
		// new ArrayList
		List<RelsMain> lRelsMain = new ArrayList<RelsMain>();
		// new occurs
		OccursList OccursList = new OccursList();
		// new ArrayList
		List<RelsFamily> lRelsFamily = new ArrayList<RelsFamily>();
		Slice<RelsFamily> slRelsFamily = null;
		// new table
		RelsMain tRelsMain = new RelsMain();
		// new RelsId
		String RelsUKey = "";
		// 選項為1.2 找自然人,法人
		if (iRelsId.isEmpty()) {
			// 依選項取List
			slRelsFamily = sRelsFamilyService.findAll(this.index, this.limit, titaVo);
			lRelsFamily = slRelsFamily == null ? null : slRelsFamily.getContent();
		} else {
			tRelsMain = sRelsMainService.RelsIdFirst(iRelsId, titaVo);
			if (tRelsMain == null) {
				throw new LogicException("E2003", "(準)利害關係人檔");
			}
			RelsUKey = tRelsMain.getRelsUKey();
			slRelsFamily = sRelsFamilyService.RelsUKeyEq(RelsUKey, this.index, this.limit, titaVo);
			lRelsFamily = slRelsFamily == null ? null : slRelsFamily.getContent();
		}
		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (slRelsFamily != null && slRelsFamily.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			/* 手動折返 */
			this.totaVo.setMsgEndToEnter();
		}

		if (lRelsFamily == null || lRelsFamily.size() == 0) {
			throw new LogicException("E2003", "(準)利害關係人親屬檔");
		}

		for (RelsFamily tmpRelsFamily : lRelsFamily) {
			OccursList = new OccursList();

			String relsUKey = tmpRelsFamily.getRelsUKey();
			RelsMain tmpRelsMain = sRelsMainService.findById(relsUKey, titaVo);
			if (tmpRelsMain == null) {
				throw new LogicException("E2003", "(準)利害關係人檔");
			}
			if (iOption != 0) {
				if (tmpRelsMain.getRelsType() != iOption) {
					continue;
				}
			}
			OccursList.putParam("OORelsId", tmpRelsMain.getRelsId());
			OccursList.putParam("OOReltSeq", tmpRelsFamily.getRelsSeq());
			OccursList.putParam("OOFamilId", tmpRelsFamily.getFamilyId());
			OccursList.putParam("OOFamilName", tmpRelsFamily.getFamilyName());
			OccursList.putParam("OOBloodInd", tmpRelsFamily.getFamilyCode());
			OccursList.putParam("OOFamilyInd", tmpRelsFamily.getFamilyCallCode());
			/* 將每筆資料放入Tota的OccList */
			this.totaVo.addOccursList(OccursList);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}