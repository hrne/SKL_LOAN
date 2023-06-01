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
import com.st1.itx.db.domain.InnFundApl;
import com.st1.itx.db.service.InnFundAplService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * AcdateYear=9,3<br>
 * END=X,1<br>
 */

@Service("L5901")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L5901 extends TradeBuffer {

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	@Autowired
	public InnFundAplService innFundAplService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5901 ");
		this.totaVo.init(titaVo);

//		 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();
//		設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 500;

		Slice<InnFundApl> sInnFundApl = null;

		int acDateFrom = parse.stringToInteger(titaVo.getParam("AcdateYear") + "0000") + 19110101;
		int acDateTo = parse.stringToInteger(titaVo.getParam("AcdateYear") + "0000") + 19111231;

		List<InnFundApl> lInnFundApl = new ArrayList<InnFundApl>();

		this.info("L5901 - A DateRange : " + acDateFrom + " ~ " + acDateTo);
		sInnFundApl = innFundAplService.acDateYearEq(acDateFrom, acDateTo, this.index, this.limit);

		lInnFundApl = sInnFundApl == null ? null : sInnFundApl.getContent();

		if (lInnFundApl != null && lInnFundApl.size() != 0) {
			for (InnFundApl tInnFundApl : lInnFundApl) {
				OccursList occursList = new OccursList();

				int date = tInnFundApl.getAcDate();

				if (tInnFundApl.getAcDate() > 19110000) {
					date = tInnFundApl.getAcDate() - 19110000;
				}

				occursList.putParam("OOAcDate", date);
				occursList.putParam("OOResrvStndrd", tInnFundApl.getResrvStndrd());
				occursList.putParam("OOPosbleBorPsn", tInnFundApl.getPosbleBorPsn());
				occursList.putParam("OOPosbleBorAmt", tInnFundApl.getPosbleBorAmt());
				occursList.putParam("OOAlrdyBorAmt", tInnFundApl.getAlrdyBorAmt());
				occursList.putParam("OOStockHoldersEqt", tInnFundApl.getStockHoldersEqt());
				occursList.putParam("OOAvailableFunds", tInnFundApl.getAvailableFunds());

				/* 將每筆資料放入Tota的OcList */
				this.totaVo.addOccursList(occursList);
			}
		} else {
			throw new LogicException(titaVo, "E0001", "查無資料");
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}