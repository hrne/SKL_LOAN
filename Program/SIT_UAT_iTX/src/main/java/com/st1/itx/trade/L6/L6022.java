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
import com.st1.itx.db.domain.CdSyndFee;
import com.st1.itx.db.service.CdSyndFeeService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

@Service("L6022")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L6022 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L6022.class);

	/* DB服務注入 */
	@Autowired
	public CdSyndFeeService cdSyndFeeService;
	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6022 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		String iSyndFeeCode = titaVo.getParam("SyndFeeCode");

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 200; // 64 * 200 = 12,800

		// 查詢保證人關係代碼檔
		Slice<CdSyndFee> slCdSyndFee;
		if (iSyndFeeCode.isEmpty() || iSyndFeeCode.equals("00")) {
			slCdSyndFee = cdSyndFeeService.findAll(this.index, this.limit, titaVo);
		} else {
			slCdSyndFee = cdSyndFeeService.findSyndFeeCode(iSyndFeeCode, iSyndFeeCode, this.index, this.limit, titaVo);
		}
		List<CdSyndFee> lCdSyndFee = slCdSyndFee == null ? null : slCdSyndFee.getContent();

		if (lCdSyndFee == null || lCdSyndFee.size() == 0) {
			throw new LogicException(titaVo, "E0001", "聯貸費用代碼檔"); // 查無資料
		}
		// 如有找到資料
		for (CdSyndFee tCdSyndFee : lCdSyndFee) {
			OccursList occursList = new OccursList();
			occursList.putParam("OOSyndFeeCode", tCdSyndFee.getSyndFeeCode());
			occursList.putParam("OOSyndFeeItem", tCdSyndFee.getSyndFeeItem());
			occursList.putParam("OOAcctCode", tCdSyndFee.getAcctCode());
			/* 將每筆資料放入Tota的OcList */
			this.totaVo.addOccursList(occursList);
		}

		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (slCdSyndFee != null && slCdSyndFee.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			this.totaVo.setMsgEndToEnter();// 手動折返
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}