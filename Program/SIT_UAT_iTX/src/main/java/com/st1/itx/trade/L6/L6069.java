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
import com.st1.itx.tradeService.TradeBuffer;

import com.st1.itx.db.domain.CdCode;
import com.st1.itx.db.service.CdCodeService;

@Service("L6069")
@Scope("prototype")
/**
 * 
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public class L6069 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CdCodeService sCdCodeDefService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6069 ");
		this.info("active L6069 ");
		this.totaVo.init(titaVo);

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 200; // 64 * 200 = 12,800

		String iDefType = titaVo.getParam("DefType");
		String iCode = titaVo.getParam("Code");

		Slice<CdCode> slCdCode = null;
		if ("".equals(iDefType)) {
			slCdCode = sCdCodeDefService.defCodeEq("CodeType", iCode + "%" ,  this.index, this.limit, titaVo);
		} else {
			int iDefType9 = Integer.parseInt(iDefType);
			slCdCode = sCdCodeDefService.defCodeEq2("CodeType", iDefType9, iCode + "%", this.index, this.limit, titaVo);
		}
		List<CdCode> lCdCode = slCdCode == null ? null : slCdCode.getContent();

		if (lCdCode == null) {
			throw new LogicException("E0001", "");
		} else {
			for (CdCode tCdCode : lCdCode) {
				OccursList occursList = new OccursList();
				occursList.putParam("OOCode", tCdCode.getCode().trim());
				occursList.putParam("OOItem", tCdCode.getItem().trim());
				occursList.putParam("OOType", tCdCode.getDefType());
				/* 將每筆資料放入Tota的OcList */
				this.totaVo.addOccursList(occursList);
			}
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