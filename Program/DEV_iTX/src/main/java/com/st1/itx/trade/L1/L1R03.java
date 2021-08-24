package com.st1.itx.trade.L1;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CustFin;
import com.st1.itx.db.domain.CustFinId;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.service.CustFinService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

@Service("L1R03")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L1R03 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CustMainService sCustMainService;

	/* DB服務注入 */
	@Autowired
	public CustFinService sCustFinService;

	/* 轉換工具 */
	@Autowired
	public Parse iParse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L1R03 ");
		this.totaVo.init(titaVo);

		// 統編
		String custid = titaVo.getParam("RimCustId");
		this.info(" L1R03 " + custid);
		// 功能 1:新增 2:修改 4:刪除 5:查詢
		String funcd = titaVo.getParam("ChainFunCd");

		String DataYear = titaVo.getParam("RimDataYear");
		// 年度
		int datayear = iParse.stringToInteger(DataYear) + 1911;
		this.info(" datayear " + datayear);

		// new CustMain Table後面使用
		CustMain tCustMain = new CustMain();
		CustFin tCustFin = new CustFin();

		// 取統編先確定是否已在客戶資料主檔測試

		tCustMain = sCustMainService.custIdFirst(custid, titaVo);

		if (tCustMain == null) {
			throw new LogicException(titaVo, "E0001", "L1R03 該統編" + custid + "不存在於客戶資料主檔，請先建立客戶資料。");
		}

		// 取得客戶識別碼
		String custUKey = tCustMain.getCustUKey();

		// new table PK
		CustFinId custFinId = new CustFinId();
		// 塞值進table PK
		custFinId.setCustUKey(custUKey);
		custFinId.setDataYear(datayear);
		tCustFin.setCustFinId(custFinId);
		this.info("L1R03 tCustFin=" + tCustFin);

		tCustMain = sCustMainService.findById(custUKey, titaVo);
		this.info("L1R03 tCustMain=" + tCustMain);
		// PK找一筆資料
		tCustFin = sCustFinService.findById(custFinId, titaVo);
		this.info("L1R03 custFinId=" + tCustFin);

		/* 如有有找到資料 */
		if (tCustFin != null) {

			// 例外處理
			if (funcd.equals("1")) {
				throw new LogicException(titaVo, "E0002", "L1R03(客戶主檔)");
			}

			this.totaVo.putParam("L1r03CustId", tCustMain.getCustId());
			this.totaVo.putParam("L1r03DataYear", tCustFin.getDataYear() - 1911);
			this.totaVo.putParam("L1r03ATotal", tCustFin.getAssetTotal());
			this.totaVo.putParam("L1r03Cash", tCustFin.getCash());
			this.totaVo.putParam("L1r03ShortTerm", tCustFin.getShortInv());
			this.totaVo.putParam("L1r03AR", tCustFin.getAR());
			this.totaVo.putParam("L1r03Inventory", tCustFin.getInventory());
			this.totaVo.putParam("L1r03LongInv", tCustFin.getLongInv());
			this.totaVo.putParam("L1r03FixedAsset", tCustFin.getFixedAsset());
			this.totaVo.putParam("L1r03OtherAsset", tCustFin.getOtherAsset());
			this.totaVo.putParam("L1r03LiabTotal", tCustFin.getLiabTotal());
			this.totaVo.putParam("L1r03BankLoan", tCustFin.getBankLoan());
			this.totaVo.putParam("L1r03OtherCurrLiab", tCustFin.getOtherCurrLiab());
			this.totaVo.putParam("L1r03LongLiab", tCustFin.getLongLiab());
			this.totaVo.putParam("L1r03OtherLiab", tCustFin.getOtherLiab());
			this.totaVo.putParam("L1r03NetWorthTotal", tCustFin.getNetWorthTotal());
			this.totaVo.putParam("L1r03Capital", tCustFin.getCapital());
			this.totaVo.putParam("L1r03RetainEarning", tCustFin.getRetainEarning());
			this.totaVo.putParam("L1r03OpIncome", tCustFin.getOpIncome());
			this.totaVo.putParam("L1r03OpCost", tCustFin.getOpCost());
			this.totaVo.putParam("L1r03OpProfit", tCustFin.getOpProfit());
			this.totaVo.putParam("L1r03OpExpense", tCustFin.getOpExpense());
			this.totaVo.putParam("L1r03OpRevenue", tCustFin.getOpRevenue());
			this.totaVo.putParam("L1r03NopIncome", tCustFin.getNopIncome());
			this.totaVo.putParam("L1r03FinExpense", tCustFin.getFinExpense());
			this.totaVo.putParam("L1r03NopExpense", tCustFin.getNopExpense());
			this.totaVo.putParam("L1r03NetIncome", tCustFin.getNetIncome());
			this.totaVo.putParam("L1r03Accountant", tCustFin.getAccountant());
			this.totaVo.putParam("L1r03AccountDate", tCustFin.getAccountDate());

		} else if (funcd.equals("2")) {

			throw new LogicException(titaVo, "E0001", "L1R03(CustMain)");

		} else if (funcd.equals("3")) {

			throw new LogicException(titaVo, "E0001", "L1R03(CustMain)");

		} else if (funcd.equals("4")) {

			throw new LogicException(titaVo, "E0001", "L1R03(CustMain)");

		} else if (funcd.equals("5")) {

			throw new LogicException(titaVo, "E0001", "L1R03(CustMain)");

		} else {

			this.totaVo.putParam("L1r03CustId", "");
			this.totaVo.putParam("L1r03DataYear", "");
			this.totaVo.putParam("L1r03ATotal", "");
			this.totaVo.putParam("L1r03Cash", "");
			this.totaVo.putParam("L1r03ShortTerm", "");
			this.totaVo.putParam("L1r03AR", "");
			this.totaVo.putParam("L1r03Inventory", "");
			this.totaVo.putParam("L1r03LongInv", "");
			this.totaVo.putParam("L1r03FixedAsset", "");
			this.totaVo.putParam("L1r03OtherAsset", "");
			this.totaVo.putParam("L1r03LiabTotal", "");
			this.totaVo.putParam("L1r03BankLoan", "");
			this.totaVo.putParam("L1r03OtherCurrLiab", "");
			this.totaVo.putParam("L1r03LongLiab", "");
			this.totaVo.putParam("L1r03OtherLiab", "");
			this.totaVo.putParam("L1r03NetWorthTotal", "");
			this.totaVo.putParam("L1r03Capital", "");
			this.totaVo.putParam("L1r03RetainEarning", "");
			this.totaVo.putParam("L1r03OpIncome", "");
			this.totaVo.putParam("L1r03OpCost", "");
			this.totaVo.putParam("L1r03OpProfit", "");
			this.totaVo.putParam("L1r03OpExpense", "");
			this.totaVo.putParam("L1r03OpRevenue", "");
			this.totaVo.putParam("L1r03NopIncome", "");
			this.totaVo.putParam("L1r03FinExpense", "");
			this.totaVo.putParam("L1r03NopExpense", "");
			this.totaVo.putParam("L1r03NetIncome", "");
			this.totaVo.putParam("L1r03Accountant", "");
			this.totaVo.putParam("L1r03AccountDate", "");

		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}