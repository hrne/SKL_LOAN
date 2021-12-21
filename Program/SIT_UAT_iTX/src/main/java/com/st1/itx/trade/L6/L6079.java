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
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdAcBook;
import com.st1.itx.db.service.CdAcBookService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/**
 * Tita AcBookCode=X,3 END=X,1
 */

@Service("L6079") // 帳冊別目標金額查詢
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */

public class L6079 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L6079.class);

	/* DB服務注入 */
	@Autowired
	public CdAcBookService sCdAcBookService;
	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6079 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		String iAcBookCode = titaVo.getParam("AcBookCode");
		String iAcSubBookCode = titaVo.getParam("AcSubBookCode");

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 200; // 41 * 200 = 8,200

		// 查詢帳冊別金額設定檔
		Slice<CdAcBook> slCdAcBook;
		if (iAcSubBookCode.isEmpty()) {
			slCdAcBook = sCdAcBookService.acBookAssignSeqGeq(iAcBookCode, 1, this.index, this.limit, titaVo);
		} else {
			slCdAcBook = sCdAcBookService.findAcBookCode(iAcBookCode, iAcSubBookCode, this.index, this.limit, titaVo);
		}
		List<CdAcBook> lCdAcBook = slCdAcBook == null ? null : slCdAcBook.getContent();

		if (lCdAcBook == null || lCdAcBook.size() == 0) {
			throw new LogicException(titaVo, "E0001", "帳冊別金額設定檔"); // 查無資料
		}
		// 如有找到資料
		for (CdAcBook tCdAcBook : lCdAcBook) {
			OccursList occursList = new OccursList();
			occursList.putParam("OOAssignSeq", tCdAcBook.getAssignSeq());
			occursList.putParam("OOAcBookCode", tCdAcBook.getAcBookCode());
			occursList.putParam("OOAcSubBookCode", tCdAcBook.getAcSubBookCode());
			occursList.putParam("OOAcctSource", tCdAcBook.getAcctSource());
			occursList.putParam("OOCurrencyCode", tCdAcBook.getCurrencyCode());
			occursList.putParam("OOTargetAmt", tCdAcBook.getTargetAmt());
			occursList.putParam("OOActualAmt", tCdAcBook.getActualAmt());
			/* 將每筆資料放入Tota的OcList */
			this.totaVo.addOccursList(occursList);
		}

		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (slCdAcBook != null && slCdAcBook.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			this.totaVo.setMsgEndToEnter();// 手動折返
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}