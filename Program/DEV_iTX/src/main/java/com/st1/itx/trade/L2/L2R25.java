package com.st1.itx.trade.L2;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.ClStock;
import com.st1.itx.db.domain.ClStockId;
import com.st1.itx.db.service.ClStockService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L2R25")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2R25 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L2R25.class);

	/* DB服務注入 */
	@Autowired
	public ClStockService sClStockService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2R25 ");
		this.totaVo.init(titaVo);

		// tita
		int iClCode1 = parse.stringToInteger(titaVo.getParam("RimClCode1"));
		int iClCode2 = parse.stringToInteger(titaVo.getParam("RimClCode2"));
		int iClNo = parse.stringToInteger(titaVo.getParam("RimClNo"));
		int iFunCd = parse.stringToInteger(titaVo.getParam("RimFunCd"));

		// new table ClMain
		ClStock tClStock = new ClStock();
		// new pk
		ClStockId ClStockId = new ClStockId();

		// 塞pk
		ClStockId.setClCode1(iClCode1);
		ClStockId.setClCode2(iClCode2);
		ClStockId.setClNo(iClNo);

		if (iFunCd != 1) {
			tClStock = sClStockService.findById(ClStockId, titaVo);
		}

		if (tClStock == null) {

			switch (iFunCd) {
			case 1: {
				// 若為新增，存空值到totaVo
				tClStock = new ClStock();
				break;
			}
			case 2:
				// 若為修改，但資料不存在，拋錯
				throw new LogicException("E0003", "擔保品股票檔");
			case 4:
				// 若為刪除，但資料不存在，拋錯
				throw new LogicException("E0004", "擔保品股票檔");
			case 5:
				// 若為查詢，但資料不存在，拋錯
				throw new LogicException("E0001", "擔保品股票檔");
			default:
				// funch不在以上範圍，拋錯
				throw new LogicException("E0010", "擔保品股票檔");
			}
		}

		this.totaVo.putParam("L2r25ClCode1", tClStock.getClCode1());
		this.totaVo.putParam("L2r25ClCode2", tClStock.getClCode2());
		this.totaVo.putParam("L2r25ClNo", tClStock.getClNo());
		this.totaVo.putParam("L2r25StockCode", tClStock.getStockCode());
		this.totaVo.putParam("L2r25ListingType", tClStock.getListingType());
		this.totaVo.putParam("L2r25StockType", tClStock.getStockType());
		this.totaVo.putParam("L2r25CompanyId", tClStock.getCompanyId());
		this.totaVo.putParam("L2r25DataYear", tClStock.getDataYear());
		this.totaVo.putParam("L2r25IssuedShares", tClStock.getIssuedShares());
		this.totaVo.putParam("L2r25NetWorth", tClStock.getNetWorth());
		this.totaVo.putParam("L2r25EvaStandard", tClStock.getEvaStandard());
		this.totaVo.putParam("L2r25ParValue", tClStock.getParValue());
		this.totaVo.putParam("L2r25MonthlyAvg", tClStock.getMonthlyAvg());
		this.totaVo.putParam("L2r25YdClosingPrice", tClStock.getYdClosingPrice());
		this.totaVo.putParam("L2r25ThreeMonthAvg", tClStock.getThreeMonthAvg());
		this.totaVo.putParam("L2r25EvaUnitPrice", tClStock.getEvaUnitPrice());
		this.totaVo.putParam("L2r25OwnerId", tClStock.getOwnerId());
		this.totaVo.putParam("L2r25OwnerName", tClStock.getOwnerName());
		this.totaVo.putParam("L2r25InsiderJobTitle", tClStock.getInsiderJobTitle());
		this.totaVo.putParam("L2r25InsiderPosition", tClStock.getInsiderPosition());
		this.totaVo.putParam("L2r25LegalPersonId", tClStock.getLegalPersonId());
		this.totaVo.putParam("L2r25LoanToValue", tClStock.getLoanToValue());
		this.totaVo.putParam("L2r25ClMtr", tClStock.getClMtr());
		this.totaVo.putParam("L2r25NoticeMtr", tClStock.getNoticeMtr());
		this.totaVo.putParam("L2r25ImplementMtr", tClStock.getImplementMtr());
		this.totaVo.putParam("L2r25PledgeNo", tClStock.getPledgeNo());
		this.totaVo.putParam("L2r25ComputeMTR", tClStock.getComputeMTR());
		this.totaVo.putParam("L2r25SettingStat", tClStock.getSettingStat());
		this.totaVo.putParam("L2r25ClStat", tClStock.getClStat());
		this.totaVo.putParam("L2r25SettingDate", tClStock.getSettingDate());
		this.totaVo.putParam("L2r25SettingBalance", tClStock.getSettingBalance());
		this.totaVo.putParam("L2r25MtgDate", tClStock.getMtgDate());
		this.totaVo.putParam("L2r25CustodyNo", tClStock.getCustodyNo());

		this.addList(this.totaVo);
		return this.sendList();
	}
}