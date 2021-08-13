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
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.db.domain.CdLoanNotYet;
import com.st1.itx.db.service.CdLoanNotYetService;

@Service("L6070")
@Scope("prototype")
/**
 * 
 * 
 * @author eric chang
 * @version 1.0.0
 */
public class L6070 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L6070.class);

	/* DB服務注入 */
	@Autowired
	public CdLoanNotYetService cdLoanNotYetService;
	
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6070 ");
		this.totaVo.init(titaVo);
		
		String iNotYetCode = titaVo.getParam("NotYetCode");

		this.info("NotYetCode="+iNotYetCode);
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 500;

		Slice<CdLoanNotYet> slCdLoanNotYet = cdLoanNotYetService.codeLike(iNotYetCode+"%", this.index, this.limit, titaVo);
		List<CdLoanNotYet> lCdLoanNotYet = slCdLoanNotYet == null ? null : slCdLoanNotYet.getContent();

		if (lCdLoanNotYet == null) {
			throw new LogicException("E0001", "未齊件代碼檔");
		} else {
			for (CdLoanNotYet cdLoanNotYet : lCdLoanNotYet) {

				OccursList occursList = new OccursList();
				occursList.putParam("ONotYetCode", cdLoanNotYet.getNotYetCode());
				occursList.putParam("ONotYetItem", cdLoanNotYet.getNotYetItem());
				occursList.putParam("OYetDays", cdLoanNotYet.getYetDays());
				occursList.putParam("OEnable", cdLoanNotYet.getEnable());
				/* 將每筆資料放入Tota的OcList */
				this.totaVo.addOccursList(occursList);
			}
		}

		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (slCdLoanNotYet != null && slCdLoanNotYet.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			this.totaVo.setMsgEndToEnter();// 手動折返
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}