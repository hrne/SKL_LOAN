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
import com.st1.itx.db.domain.CdComm;
import com.st1.itx.db.service.CdCommService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

@Service("L6091")
@Scope("prototype")
/**
 *
 *
 * @author
 * @version 1.0.0
 */

public class L6091 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	private CdCommService sCdCommService;

	@Autowired
	private Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6091 ");

		this.totaVo.init(titaVo);

		this.index = titaVo.getReturnIndex();
		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 200; // 96 * 200 = 19,200

		int tmpEffectDate = parse.stringToInteger(titaVo.getParam("EffectDate"));

		// 全部查詢
		if (tmpEffectDate == 0) {

			Slice<CdComm> sCdComm = sCdCommService.findAll(this.index, Integer.MAX_VALUE, titaVo);

			if (sCdComm == null) {
				throw new LogicException(titaVo, "E0001", "查無資料"); // 查無資料
			}

			for (CdComm r : sCdComm.getContent()) {
				this.info("getEffectDate =" + r.getEffectDate());

				if ("03".equals(r.getCdType()) && "01".equals(r.getCdItem())) {

					OccursList occursList = new OccursList();

					occursList.putParam("OOEffectDate", r.getEffectDate());

					this.totaVo.addOccursList(occursList);
				} else {
					continue;
				}
			}

		}

		// 用年月查詢
		if (tmpEffectDate > 0) {

			Slice<CdComm> sCdComm = sCdCommService.findAll(this.index, Integer.MAX_VALUE, titaVo);

			if (sCdComm == null) {
				throw new LogicException(titaVo, "E0001", "查無資料"); // 查無資料
			}

			for (CdComm r : sCdComm.getContent()) {
				this.info("getEffectDate =" + r.getEffectDate());

				if ("03".equals(r.getCdType()) && "01".equals(r.getCdItem()) && tmpEffectDate <= r.getEffectDate()) {

					OccursList occursList = new OccursList();

					occursList.putParam("OOEffectDate", r.getEffectDate());

					this.totaVo.addOccursList(occursList);
				} else {
					continue;
				}
			}

		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}