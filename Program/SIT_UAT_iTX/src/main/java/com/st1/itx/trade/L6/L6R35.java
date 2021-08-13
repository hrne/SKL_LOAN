package com.st1.itx.trade.L6;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdCode;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

@Service("L6R35")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L6R35 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L6R35.class);
	/* DB服務注入 */
	@Autowired
	public CdCodeService sCdCodeDefService;

	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6064 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		String iDefCode = titaVo.getParam("DefCode").trim();

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 200; // 217 * 200 = 43400

		Slice<CdCode> slCdCode = null;
		slCdCode = sCdCodeDefService.defCodeEq("CodeType", iDefCode + "%", this.index, this.limit, titaVo);

		List<CdCode> lCdCode = slCdCode == null ? null : slCdCode.getContent();

		if (lCdCode == null || lCdCode.size() == 0) {
			throw new LogicException(titaVo, "E0001", "各類代碼檔檔"); // 查無資料
		}
		// 如有找到資料
		for (CdCode tCdCode : lCdCode) {
			this.totaVo.putParam("L6R35Item", tCdCode.getItem());
		}

		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (slCdCode != null && slCdCode.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			this.totaVo.setMsgEndToEnter();// 手動折返
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}