package com.st1.itx.trade.L5;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.InnFundApl;
import com.st1.itx.db.service.InnFundAplService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * FunctionCode=9,1<br>
 * AcDate=9,7<br>
 * ResrvStndrd=9,14.2<br>
 * PosbleBorPsn=9,3.2<br>
 * PosbleBorAmt=9,14.2<br>
 * AlrdyBorAmt=9,14.2<br>
 * END=X,1<br>
 */

@Service("L5101")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L5101 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L5101.class);

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
		this.info("active L5101 ");
		this.totaVo.init(titaVo);

		int iFunctionCode = parse.stringToInteger(titaVo.getParam("FunctionCode").trim());
		int acDate = parse.stringToInteger(titaVo.getParam("AcDate")) + 19110000;

		this.info("iFunctionCode : " + iFunctionCode);
		this.info("acDate : " + acDate);
		InnFundApl iInnFundApl = new InnFundApl();
		InnFundApl xInnFundApl = innFundAplService.findById(acDate, titaVo);
		switch (iFunctionCode) {
		case 1:
			if (xInnFundApl != null) {
				throw new LogicException(titaVo, "E0005", "日期:" + titaVo.getParam("AcDate") + "已有相同資料");
			} else {
				iInnFundApl.setAcDate(acDate);
				iInnFundApl.setAlrdyBorAmt(parse.stringToBigDecimal(titaVo.getParam("AlrdyBorAmt")));
				iInnFundApl.setResrvStndrd(parse.stringToBigDecimal(titaVo.getParam("ResrvStndrd")));
				iInnFundApl.setPosbleBorPsn(parse.stringToBigDecimal(titaVo.getParam("PosbleBorPsn")));
				iInnFundApl.setPosbleBorAmt(parse.stringToBigDecimal(titaVo.getParam("PosbleBorAmt")));
				iInnFundApl.setStockHoldersEqt(parse.stringToBigDecimal(titaVo.getParam("StockHoldersEqt")));
				try {
					innFundAplService.insert(iInnFundApl, titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0005", "L5101 InnFundApl insert " + e.getErrorMsg());
				}
			}
			break;
		case 2:
			if (xInnFundApl == null) {
				throw new LogicException(titaVo, "E0006", "日期" + titaVo.getParam("AcDate") + "資料不存在");
			} else {
				iInnFundApl = innFundAplService.holdById(acDate, titaVo);
				iInnFundApl.setAlrdyBorAmt(parse.stringToBigDecimal(titaVo.getParam("AlrdyBorAmt")));
				iInnFundApl.setResrvStndrd(parse.stringToBigDecimal(titaVo.getParam("ResrvStndrd")));
				iInnFundApl.setPosbleBorPsn(parse.stringToBigDecimal(titaVo.getParam("PosbleBorPsn")));
				iInnFundApl.setPosbleBorAmt(parse.stringToBigDecimal(titaVo.getParam("PosbleBorAmt")));
				iInnFundApl.setStockHoldersEqt(parse.stringToBigDecimal(titaVo.getParam("StockHoldersEqt")));
				try {
					innFundAplService.update(iInnFundApl, titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", "L5101 InnFundApl update " + e.getErrorMsg());
				}
			}
			break;
		case 4:
			if (xInnFundApl == null) {
				throw new LogicException(titaVo, "E0006", "日期" + titaVo.getParam("AcDate") + "資料不存在");
			} else {
				iInnFundApl = innFundAplService.holdById(acDate, titaVo);
				try {
					innFundAplService.delete(xInnFundApl, titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", "L5101 InnFundApl delete " + e.getErrorMsg());
				}
			}
			break;
		case 5:
			break;
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}