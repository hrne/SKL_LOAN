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
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdGuarantor;
import com.st1.itx.db.service.CdGuarantorService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L2R42")
@Scope("prototype")
/**
 * L2R42 : 取保證人關係代碼下拉選單
 * 
 * @author ST1 Chih Wei
 * @version 1.0.0
 */
public class L2R42 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L2R42.class);

	/* DB服務注入 */
	@Autowired
	public CdGuarantorService sCdGuarantorService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2R42 ");
		this.totaVo.init(titaVo);
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 100; // 代碼長度2 限制為數字(0~99),所以最多只有100筆

		// new array list
		List<CdGuarantor> lCdGuarantor = new ArrayList<CdGuarantor>();

		Slice<CdGuarantor> slCdGuarantor = sCdGuarantorService.findAll(this.index, this.limit, titaVo);
		lCdGuarantor = slCdGuarantor == null ? null : new ArrayList<CdGuarantor>(slCdGuarantor.getContent());

		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (slCdGuarantor != null && slCdGuarantor.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			/* 手動折返 */
			this.totaVo.setMsgEndToEnter();
		}

		if (lCdGuarantor == null || lCdGuarantor.size() == 0) {
			throw new LogicException(titaVo, "E2003", "保證人關係代碼檔"); // 查無資料
		}

		ArrayList<String> defList = new ArrayList<String>();

		for (CdGuarantor tCdGuarantor : lCdGuarantor) {
			this.info("L2R42 tCdGuarantor = " + tCdGuarantor);

			defList.add(tCdGuarantor.getGuaRelCode() + ":" + tCdGuarantor.getGuaRelItem());
		}

		int listSize = defList.size();

		String l2r42GuaRelDef = "";

		for (int i = 0; i < listSize; i++) {
			String tmpDef = defList.get(i);
			l2r42GuaRelDef += tmpDef;
			if (i < listSize - 1) {
				l2r42GuaRelDef += ";";
			}
			this.info("L2R42 i = " + i + " , l2r42GuaRelDef = " + l2r42GuaRelDef);
		}

		this.totaVo.putParam("L2r42GuaRelDef", l2r42GuaRelDef);

		this.addList(this.totaVo);
		return this.sendList();
	}
}