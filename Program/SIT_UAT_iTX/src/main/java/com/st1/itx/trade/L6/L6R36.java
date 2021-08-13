package com.st1.itx.trade.L6;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdBonusCo;
import com.st1.itx.db.service.CdBonusCoService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;


@Service("L6R36")
@Scope("prototype")
/**
 * 
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L6R36 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L6R36.class);

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;
	
	@Autowired
	public CdBonusCoService iCdBonusCoService;
	
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 500;
		
		this.info("active L6R36 ");
		this.totaVo.init(titaVo);
		
		int iWorkMonth = Integer.valueOf(titaVo.getParam("RimWorkMonth"))+191100;
	
		Slice<CdBonusCo> iCdBonusCo = null;
		iCdBonusCo = iCdBonusCoService.findYearMonth(iWorkMonth, iWorkMonth, this.index, this.limit, titaVo);
		
		if (iCdBonusCo == null) {
			throw new LogicException(titaVo, "E0001", "該工作月尚無資料");
		}
		
		int iPieceCode = 0;
		int iCondition = 0;
		
		
		//預設20筆
		for(int i = 0 ; i<20 ; i++) {
			totaVo.putParam("L6R36PieceCode"+String.valueOf(i),"");
		}
		
		for (CdBonusCo rCdBonusCo : iCdBonusCo) {
			if (rCdBonusCo.getConditionCode() == 1 ) {
				totaVo.putParam("L6R36PieceCode"+String.valueOf(iPieceCode),rCdBonusCo.getCondition());
				if (iPieceCode == 0) { //第一筆的標準金額
					totaVo.putParam("L6R36ConditionAmt", rCdBonusCo.getConditionAmt());
				}
				iPieceCode ++ ;
			}else {
				totaVo.putParam("L6R36Bonus"+String.valueOf(iCondition), rCdBonusCo.getBonus());
				totaVo.putParam("L6R36ClassPassBonus"+String.valueOf(iCondition), rCdBonusCo.getClassPassBonus());
				iCondition ++ ;
			}
		}
		
		

		this.addList(this.totaVo);
		return this.sendList();
	}
}