package com.st1.itx.trade.L6;

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
import com.st1.itx.db.domain.CdBaseRate;
import com.st1.itx.db.service.CdBaseRateService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/**
 * Tita FuncCode=X,1 CurrencyCode=X,3 BaseRateCode=X,2 END=X,1
 */

@Service("L6032") // 指標利率檔查詢
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */

public class L6032 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L6032.class);

	/* DB服務注入 */
	@Autowired
	public CdBaseRateService sCdBaseRateService;
	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6032 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		String iBaseRateCode = titaVo.getParam("BaseRateCode");
		String iCurrencyCode = titaVo.getParam("CurrencyCode");

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 200; // 96 * 200 = 19,200

		// 查詢指標利率檔
		Slice<CdBaseRate> slCdBaseRate;
		if (iBaseRateCode.isEmpty() || iBaseRateCode.equals("00")) {
			slCdBaseRate = sCdBaseRateService.baseRateCodeRange(iCurrencyCode, "00", "99", 00000000, 99999999, this.index, this.limit, titaVo);
		} else {
			slCdBaseRate = sCdBaseRateService.baseRateCodeRange(iCurrencyCode, iBaseRateCode, iBaseRateCode, 00000000, 99999999, this.index, this.limit, titaVo);
		}
		List<CdBaseRate> lCdBaseRate = slCdBaseRate == null ? null : slCdBaseRate.getContent();

		if (lCdBaseRate == null || lCdBaseRate.size() == 0) {
			throw new LogicException(titaVo, "E0001", "指標利率檔"); // 查無資料
		}
		// 如有找到資料
		for (CdBaseRate tCdBaseRate : lCdBaseRate) {

			// 99:為自訂利率 , 代號表中是預設值
			if (tCdBaseRate.getBaseRateCode().equals("99")) {
				continue;
			}

			OccursList occursList = new OccursList();
			occursList.putParam("OOBaseRateCode", tCdBaseRate.getBaseRateCode());
			occursList.putParam("OOBaseRate", tCdBaseRate.getBaseRate());
			occursList.putParam("OOEffectDate", tCdBaseRate.getEffectDate());
			occursList.putParam("OORemark", tCdBaseRate.getRemark());
			occursList.putParam("OOEffectFlag", tCdBaseRate.getEffectFlag());
			/* 將每筆資料放入Tota的OcList */
			this.totaVo.addOccursList(occursList);
		}

		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (slCdBaseRate != null && slCdBaseRate.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			this.totaVo.setMsgEndToEnter();// 手動折返
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}