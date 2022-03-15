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
import com.st1.itx.db.domain.CdSupv;
import com.st1.itx.db.service.CdSupvService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * SupvReasonCode=X,4<br>
 * END=X,1<br>
 */

@Service("L6066") // 主管理由檔查詢
@Scope("prototype")
/**
 * 
 * 
 * @author Yoko
 * @version 1.0.0
 */

public class L6066 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L6066.class);

	/* DB服務注入 */
	@Autowired
	public CdSupvService sCdSupvService;
	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6066 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		String iSupvReasonCode = titaVo.getParam("SupvReasonCode");

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 200; // 85 * 200 = 17,000

		// 查詢主管理由檔
		Slice<CdSupv> slCdSupv;
		if (iSupvReasonCode.isEmpty() || iSupvReasonCode.equals("0000")) {
			slCdSupv = sCdSupvService.findAll(this.index, this.limit, titaVo);
		} else {
			slCdSupv = sCdSupvService.findSupvReasonCode(iSupvReasonCode, iSupvReasonCode, this.index, this.limit, titaVo);
		}
		List<CdSupv> lCdSupv = slCdSupv == null ? null : slCdSupv.getContent();

		if (lCdSupv == null || lCdSupv.size() == 0) {
			throw new LogicException(titaVo, "E0001", "主管理由檔"); // 查無資料
		}
		// 如有找到資料
		for (CdSupv tCdSupv : lCdSupv) {
			OccursList occursList = new OccursList();
			occursList.putParam("OOSupvReasonCode", tCdSupv.getSupvReasonCode());
			occursList.putParam("OOSupvReasonItem", tCdSupv.getSupvReasonItem());
			occursList.putParam("OOSupvReasonLevel", tCdSupv.getSupvReasonLevel());

			/* 將每筆資料放入Tota的OcList */
			this.totaVo.addOccursList(occursList);
		}

		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (slCdSupv != null && slCdSupv.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			this.totaVo.setMsgEndToEnter();// 手動折返
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}