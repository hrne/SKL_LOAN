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
import com.st1.itx.db.domain.CdGuarantor;
import com.st1.itx.db.service.CdGuarantorService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * GuaRelCode=X,2<br>
 * END=X,1<br>
 */

@Service("L6067") // 保證人關係代碼檔查詢
@Scope("prototype")
/**
 * 
 * 
 * @author Yoko
 * @version 1.0.0
 */

public class L6067 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L6067.class);

	/* DB服務注入 */
	@Autowired
	public CdGuarantorService sCdGuarantorService;
	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6067 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		String iGuaRelCode = titaVo.getParam("GuaRelCode");

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 200; // 64 * 200 = 12,800

		// 查詢保證人關係代碼檔
		Slice<CdGuarantor> slCdGuarantor;
		if (iGuaRelCode.isEmpty() || iGuaRelCode.equals("00")) {
			slCdGuarantor = sCdGuarantorService.findAll(this.index, this.limit, titaVo);
		} else {
			slCdGuarantor = sCdGuarantorService.findGuaRelCode(iGuaRelCode, iGuaRelCode, this.index, this.limit, titaVo);
		}
		List<CdGuarantor> lCdGuarantor = slCdGuarantor == null ? null : slCdGuarantor.getContent();

		if (lCdGuarantor == null || lCdGuarantor.size() == 0) {
			throw new LogicException(titaVo, "E0001", "保證人關係代碼檔"); // 查無資料
		}
		// 如有找到資料
		for (CdGuarantor tCdGuarantor : lCdGuarantor) {
			OccursList occursList = new OccursList();
			occursList.putParam("OOGuaRelCode", tCdGuarantor.getGuaRelCode());
			occursList.putParam("OOGuaRelItem", tCdGuarantor.getGuaRelItem());
			occursList.putParam("OOGuaRelJcic", tCdGuarantor.getGuaRelJcic());
			/* 將每筆資料放入Tota的OcList */
			this.totaVo.addOccursList(occursList);
		}

		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (slCdGuarantor != null && slCdGuarantor.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			this.totaVo.setMsgEndToEnter();// 手動折返
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}