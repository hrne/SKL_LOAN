package com.st1.itx.trade.L6;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdBonusCo;
import com.st1.itx.db.service.CdBonusCoService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;


@Service("L6087")
@Scope("prototype")
public class L6087 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L6087.class);

	/* DB服務注入 */
	/* 轉換工具 */
	@Autowired
	public Parse parse;
	
	@Autowired
	public CdBonusCoService iCdBonusCoService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6087 ");
		this.totaVo.init(titaVo);

		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 200; // 229 * 200 = 45800
		
		int iWorkMonth = Integer.valueOf(titaVo.getParam("WorkMonth"))+191100;
	
		Slice<CdBonusCo> tCdBonusCo = null;
		
		if (Integer.valueOf(titaVo.getParam("WorkMonth")) == 0) {
			tCdBonusCo = iCdBonusCoService.findAll(this.index, this.limit, titaVo);
		}else {
			tCdBonusCo = iCdBonusCoService.findYearMonth(iWorkMonth, iWorkMonth, this.index, this.limit, titaVo);
		}
		ArrayList<Integer> workMonth_list = new ArrayList<>();
		if (tCdBonusCo == null) {
			throw new LogicException(titaVo, "E0001", "查無資料"); // 查無資料
		}
		for(CdBonusCo iCdBonusCo :tCdBonusCo) {
			int cWorkMonth = iCdBonusCo.getWorkMonth();
			if (workMonth_list.contains(cWorkMonth)) {
				continue;
			}else {
				workMonth_list.add(0,cWorkMonth);
			}
		}
		for(int reWorkMonth:workMonth_list) {
			OccursList occursList = new OccursList();
			occursList.putParam("OOWorkMonth", reWorkMonth-191100);
			this.totaVo.addOccursList(occursList);
		}	
		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (tCdBonusCo != null && tCdBonusCo.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			/* 手動折返 */
			this.totaVo.setMsgEndToEnter();
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}