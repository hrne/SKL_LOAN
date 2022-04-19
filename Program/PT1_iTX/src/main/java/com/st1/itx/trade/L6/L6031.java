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
import com.st1.itx.db.domain.CdCode;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/**
 * Tita FuncCode=X,1 CurrencyCode=X,3 BaseRateCode=X,2 END=X,1
 */

@Service("L6031") // 指標利率檔查詢
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */

public class L6031 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CdCodeService sCdCodeService;

	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6031 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		String iBaseRateCode = titaVo.getParam("BaseRateCode");

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 200; // 96 * 200 = 19,200

		// 查詢代碼檔
		Slice<CdCode> sCdCode = null;
		if (("00").equals(iBaseRateCode)) {
			sCdCode = sCdCodeService.defCodeEq("BaseRate", "%", this.index, this.limit, titaVo);
		} else {
			sCdCode = sCdCodeService.defCodeEq("BaseRate", iBaseRateCode + "%", this.index, this.limit, titaVo);
		}

		List<CdCode> lCdCode = sCdCode == null ? null : sCdCode.getContent();
		if (lCdCode == null || lCdCode.size() == 0) {
			throw new LogicException(titaVo, "E0001", "共用代碼檔"); // 查無資料
		}

		for (CdCode tCdCode : lCdCode) {

			OccursList occursList = new OccursList();
			occursList.putParam("OOBaseRateCode", tCdCode.getCode());
			occursList.putParam("OOBaseRateItem", tCdCode.getItem());
			occursList.putParam("OOEffectFlag", tCdCode.getEffectFlag());
			/* 將每筆資料放入Tota的OcList */
			this.totaVo.addOccursList(occursList);
		}

		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (sCdCode != null && sCdCode.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			this.totaVo.setMsgEndToEnter();// 手動折返
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}