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
import com.st1.itx.db.domain.TxTranCode;
import com.st1.itx.db.service.TxTranCodeService;

@Service("L6042")
@Scope("prototype")
/**
 * 
 * 
 * @author Eric Chang
 * @version 1.0.0
 */
public class L6042 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L6042.class);

	/* DB服務注入 */
	@Autowired
	public TxTranCodeService txTranCodeService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6042 ");
		this.totaVo.init(titaVo);

		String iTranNo = titaVo.getParam("TranNo") + "%";

		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 500;

		Slice<TxTranCode> slTxTranCode = txTranCodeService.TranNoLike(iTranNo + "%", this.index, this.limit, titaVo);
		List<TxTranCode> lTxTranCode = slTxTranCode == null ? null : slTxTranCode.getContent();

		if (lTxTranCode == null) {
			throw new LogicException("E0001", "");
		} else {
			for (TxTranCode tTxTranCode : lTxTranCode) {
				OccursList occursList = new OccursList();
				occursList.putParam("OTranNo", tTxTranCode.getTranNo());
				occursList.putParam("OTranItem", tTxTranCode.getTranItem());
				occursList.putParam("ODesc", tTxTranCode.getDesc());
				/* 將每筆資料放入Tota的OcList */
				this.totaVo.addOccursList(occursList);
			}
		}

		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (slTxTranCode != null && slTxTranCode.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			this.totaVo.setMsgEndToEnter();// 手動折返
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}