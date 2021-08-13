package com.st1.itx.trade.L1;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CustFin;
import com.st1.itx.db.domain.CustFinId;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.service.CustFinService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * FunCd=9,1<br>
 * CustId=X,8<br>
 * DataYear=9,3<br>
 * ATotal=9,14<br>
 * Cash=9,14<br>
 * ShortTerm=9,14<br>
 * AR=9,14<br>
 * Inventory=9,14<br>
 * LongInv=9,14<br>
 * FixedAsset=9,14<br>
 * OtherAsset=9,14<br>
 * LiabTotal=9,14<br>
 * BankLoan=9,14<br>
 * OtherCurrLiab=9,14<br>
 * LongLiab=9,14<br>
 * OtherLiab=9,14<br>
 * NetWorthTotal=9,14<br>
 * Capital=9,14<br>
 * RetainEarning=9,14<br>
 * OpIncome=9,14<br>
 * OpCost=9,14<br>
 * OpProfit=9,14<br>
 * OpExpense=9,14<br>
 * OpRevenue=9,14<br>
 * NopIncome=9,14<br>
 * FinExpense=9,14<br>
 * NopExpense=9,14<br>
 * NetIncome=9,14<br>
 * Accountant=X,10<br>
 * AccountDate=9,7<br>
 */

@Service("L1107")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L1107 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L1107.class);

	/* DB服務注入 */
	@Autowired
	public CustMainService sCustMainService;

	/* DB服務注入 */
	@Autowired
	public CustFinService sCustFinService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Autowired
	public DataLog dataLog;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L1107 ");
		this.totaVo.init(titaVo);

		// 取統編先確定是否已在客戶資料主檔測試
		String custid = titaVo.getParam("CustId").trim();

		CustMain tCustMain = sCustMainService.custIdFirst(custid);

		if (tCustMain == null) {
			throw new LogicException("E1003", "客戶資料主檔");
		}

		// 取客戶資料主檔custukey
		String custUKey = tCustMain.getCustUKey();

		// 功能 1:新增 2:修改 4:刪除 5:查詢
		String funcd = titaVo.getParam("FunCd").trim();

		String DataYear = titaVo.getParam("DataYear");
		// 年度
		int datayear = parse.stringToInteger(DataYear) + 1911;
		this.info(" L1107 datayear" + datayear);

		// new table
		CustFin tCustFin = new CustFin();

		// new TablePK 塞值
		CustFinId custFinId = new CustFinId(custUKey, datayear);

		// 新增
		if (funcd.equals("1")) {

			// 把TablePK塞到Table
			tCustFin.setCustFinId(custFinId);
			// tita值塞進Table
			tCustFin.setCustUKey(custUKey);
			tCustFin.setDataYear(datayear);
			tCustFin.setAssetTotal(parse.stringToBigDecimal(titaVo.getParam("ATotal")));
			tCustFin.setCash(parse.stringToBigDecimal(titaVo.getParam("Cash")));
			tCustFin.setShortInv(parse.stringToBigDecimal(titaVo.getParam("ShortTerm")));
			tCustFin.setAR(parse.stringToBigDecimal(titaVo.getParam("AR")));
			tCustFin.setInventory(parse.stringToBigDecimal(titaVo.getParam("Inventory")));
			tCustFin.setLongInv(parse.stringToBigDecimal(titaVo.getParam("LongInv")));
			tCustFin.setFixedAsset(parse.stringToBigDecimal(titaVo.getParam("FixedAsset")));
			tCustFin.setOtherAsset(parse.stringToBigDecimal(titaVo.getParam("OtherAsset")));
			tCustFin.setLiabTotal(parse.stringToBigDecimal(titaVo.getParam("LiabTotal")));
			tCustFin.setBankLoan(parse.stringToBigDecimal(titaVo.getParam("BankLoan")));
			tCustFin.setOtherCurrLiab(parse.stringToBigDecimal(titaVo.getParam("OtherCurrLiab")));
			tCustFin.setLongLiab(parse.stringToBigDecimal(titaVo.getParam("LongLiab")));
			tCustFin.setOtherLiab(parse.stringToBigDecimal(titaVo.getParam("OtherLiab")));
			tCustFin.setNetWorthTotal(parse.stringToBigDecimal(titaVo.getParam("NetWorthTotal")));
			tCustFin.setCapital(parse.stringToBigDecimal(titaVo.getParam("Capital")));
			tCustFin.setRetainEarning(parse.stringToBigDecimal(titaVo.getParam("RetainEarning")));
			tCustFin.setOpIncome(parse.stringToBigDecimal(titaVo.getParam("OpIncome")));
			tCustFin.setOpCost(parse.stringToBigDecimal(titaVo.getParam("OpCost")));
			tCustFin.setOpProfit(parse.stringToBigDecimal(titaVo.getParam("OpProfit")));
			tCustFin.setOpExpense(parse.stringToBigDecimal(titaVo.getParam("OpExpense")));
			tCustFin.setOpRevenue(parse.stringToBigDecimal(titaVo.getParam("OpRevenue")));
			tCustFin.setNopIncome(parse.stringToBigDecimal(titaVo.getParam("NopIncome")));
			tCustFin.setFinExpense(parse.stringToBigDecimal(titaVo.getParam("FinExpense")));
			tCustFin.setNopExpense(parse.stringToBigDecimal(titaVo.getParam("NopExpense")));
			tCustFin.setNetIncome(parse.stringToBigDecimal(titaVo.getParam("NetIncome")));
			tCustFin.setAccountant(titaVo.getParam("Accountant"));
			tCustFin.setAccountDate(parse.stringToInteger(titaVo.getParam("AccountDate")));

			try {
				sCustFinService.insert(tCustFin,titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "公司戶財務狀況檔");
			}

		} else if (funcd.equals("2")) {

			// PK找公司戶財務狀況檔單筆資料
			tCustFin = sCustFinService.holdById(custFinId);

			if (tCustFin == null) {
				throw new LogicException("E0003", "公司戶財務狀況檔");
			}

			// 變更前
			CustFin beforeCustFin = (CustFin) dataLog.clone(tCustFin);

			this.info(" L1107 beforeCustFin.toString() " + beforeCustFin.toString());

			// tita值塞進Table
			tCustFin.setAssetTotal(parse.stringToBigDecimal(titaVo.getParam("ATotal")));
			tCustFin.setCash(parse.stringToBigDecimal(titaVo.getParam("Cash")));
			tCustFin.setShortInv(parse.stringToBigDecimal(titaVo.getParam("ShortTerm")));
			tCustFin.setAR(parse.stringToBigDecimal(titaVo.getParam("AR")));
			tCustFin.setInventory(parse.stringToBigDecimal(titaVo.getParam("Inventory")));
			tCustFin.setLongInv(parse.stringToBigDecimal(titaVo.getParam("LongInv")));
			tCustFin.setFixedAsset(parse.stringToBigDecimal(titaVo.getParam("FixedAsset")));
			tCustFin.setOtherAsset(parse.stringToBigDecimal(titaVo.getParam("OtherAsset")));
			tCustFin.setLiabTotal(parse.stringToBigDecimal(titaVo.getParam("LiabTotal")));
			tCustFin.setBankLoan(parse.stringToBigDecimal(titaVo.getParam("BankLoan")));
			tCustFin.setOtherCurrLiab(parse.stringToBigDecimal(titaVo.getParam("OtherCurrLiab")));
			tCustFin.setLongLiab(parse.stringToBigDecimal(titaVo.getParam("LongLiab")));
			tCustFin.setOtherLiab(parse.stringToBigDecimal(titaVo.getParam("OtherLiab")));
			tCustFin.setNetWorthTotal(parse.stringToBigDecimal(titaVo.getParam("NetWorthTotal")));
			tCustFin.setCapital(parse.stringToBigDecimal(titaVo.getParam("Capital")));
			tCustFin.setRetainEarning(parse.stringToBigDecimal(titaVo.getParam("RetainEarning")));
			tCustFin.setOpIncome(parse.stringToBigDecimal(titaVo.getParam("OpIncome")));
			tCustFin.setOpCost(parse.stringToBigDecimal(titaVo.getParam("OpCost")));
			tCustFin.setOpProfit(parse.stringToBigDecimal(titaVo.getParam("OpProfit")));
			tCustFin.setOpExpense(parse.stringToBigDecimal(titaVo.getParam("OpExpense")));
			tCustFin.setOpRevenue(parse.stringToBigDecimal(titaVo.getParam("OpRevenue")));
			tCustFin.setNopIncome(parse.stringToBigDecimal(titaVo.getParam("NopIncome")));
			tCustFin.setFinExpense(parse.stringToBigDecimal(titaVo.getParam("FinExpense")));
			tCustFin.setNopExpense(parse.stringToBigDecimal(titaVo.getParam("NopExpense")));
			tCustFin.setNetIncome(parse.stringToBigDecimal(titaVo.getParam("NetIncome")));
			tCustFin.setAccountant(titaVo.getParam("Accountant"));
			tCustFin.setAccountDate(parse.stringToInteger(titaVo.getParam("AccountDate")));

			try {
				tCustFin = sCustFinService.update2(tCustFin,titaVo);
			} catch (DBException e) {
				throw new LogicException("E0007", "公司戶財務狀況檔");
			}

			// 紀錄變更前變更後
			dataLog.setEnv(titaVo, beforeCustFin, tCustFin);
			dataLog.exec();

		} else if (funcd.equals("4")) {

			tCustFin = sCustFinService.holdById(custFinId);

			if (tCustFin == null) {
				throw new LogicException("E0004", "公司戶財務狀況檔");
			}

			try {
				sCustFinService.delete(tCustFin);
			} catch (DBException e) {
				throw new LogicException("E0008", "公司戶財務狀況檔");
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}