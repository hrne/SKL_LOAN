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
import com.st1.itx.util.parse.Parse;
import com.st1.itx.db.domain.TxErrCode;
import com.st1.itx.db.service.TxErrCodeService;

@Service("L6024")
@Scope("prototype")
/**
 * 使用者資料查詢
 * 
 * @author Chih Cheng
 * @version 1.0.0
 */
public class L6024 extends TradeBuffer {

	/* DB服務注入 */

	@Autowired
	TxErrCodeService sTxErrCodeService;


	
	@Autowired
	Parse parse;
	
	boolean first = true;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6041 ");
		this.totaVo.init(titaVo);

		String iErrCode = titaVo.getParam("ErrCode");

			
		
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 500;
		Slice<TxErrCode> sTxErrCode = null;
		
		if(iErrCode.isEmpty()) {
			sTxErrCode = sTxErrCodeService.findAll(this.index, this.limit, titaVo);
		} else {
			sTxErrCode = sTxErrCodeService.findbyErrCode("%"+iErrCode+"%", this.index, this.limit, titaVo);
		}
		
		
		List<TxErrCode> lTxErrCode = sTxErrCode == null ? null : sTxErrCode.getContent();

		if (lTxErrCode == null) {
			throw new LogicException("E0001", "");
		} else {
			for (TxErrCode tTxErrCode : lTxErrCode) {
	
				
				OccursList occursList = new OccursList();
				occursList.putParam("OOErrCode", tTxErrCode.getErrCode());
				occursList.putParam("OOErrContent", tTxErrCode.getErrContent());
				


//				occursList.putParam("OAuthNo", tTxTeller.getAuthNo());
				/* 將每筆資料放入Tota的OcList */
				this.totaVo.addOccursList(occursList);
			}
		}

		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (sTxErrCode != null && sTxErrCode.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			this.totaVo.setMsgEndToEnter();// 手動折返
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}