package com.st1.itx.trade.L5;

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
import com.st1.itx.db.domain.ConstructionCompany;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.service.ConstructionCompanyService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.tradeService.TradeBuffer;

/**
 * L5080 建商名單查詢
 * 
 * @author ST1-ChihWei
 * @version 1.0.0
 */
@Service("L5080")
@Scope("prototype")
public class L5080 extends TradeBuffer {

	@Autowired
	ConstructionCompanyService constructionCompanyService;

	@Autowired
	CustMainService custMainService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5080 ");
		this.totaVo.init(titaVo);

		// *** 折返控制相關 ***
		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 50; // 150 * 50 = 7500

		Slice<ConstructionCompany> sliceConstructionCompany = constructionCompanyService
				.findAll(titaVo.getReturnIndex(), this.limit, titaVo);

		if (sliceConstructionCompany != null && !sliceConstructionCompany.isEmpty()) {

			OccursList occurslist;
			int custNo;
			CustMain custMain;
			String custName;

			List<ConstructionCompany> listConstructionCompany = new ArrayList<>(sliceConstructionCompany.getContent());

			for (ConstructionCompany constructionCompany : listConstructionCompany) {

				occurslist = new OccursList();

				custNo = constructionCompany.getCustNo();
				custMain = custMainService.custNoFirst(custNo, custNo, titaVo);
				custName = custMain == null ? "" : custMain.getCustName();

				occurslist.putParam("OOCustNo", custNo);
				occurslist.putParam("OOCustName", custName);
				occurslist.putParam("OODeleteFlag", constructionCompany.getDeleteFlag());

				/* 將每筆資料放入Tota的OcList */
				this.totaVo.addOccursList(occurslist);
			}

			if (sliceConstructionCompany.getSize() == this.limit && sliceConstructionCompany.hasNext()) {
				titaVo.setReturnIndex(this.setIndexNext());
				/* 手動折返 */
				this.totaVo.setMsgEndToEnter();
			}
		} else {
			// 回傳"查無資料"的錯誤
			throw new LogicException("E2003", "建商名單檔");
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}