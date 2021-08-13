package com.st1.itx.trade.L1;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CustFin;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.service.CustFinService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L1907")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L1907 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L1907.class);

	/* DB服務注入 */
	@Autowired
	public CustMainService iCustMainService;

	/* DB服務注入 */
	@Autowired
	public CustFinService iCustFinService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L1907 ");
		this.totaVo.init(titaVo);

		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 500; // 75 * 500 = 37500

		// 取tita統編or戶號
		String iCustId = titaVo.getParam("CustId").trim();
		String iCustFullname = titaVo.getParam("CustFullname");
		
		if(iCustId.equals("")) {
			this.info("戶名");
			Slice<CustMain> iCustMain = null;
			String iCustUKey = "";
			iCustMain = iCustMainService.custNameEq(iCustFullname, this.index, this.limit, titaVo);
			if(iCustMain==null) {
				throw new LogicException("E0001", "客戶資料主檔無此公司名稱:"+iCustFullname); // 查無資料
			}
			for(CustMain xCustMain:iCustMain) {
				iCustUKey = xCustMain.getCustUKey();
				Slice<CustFin> iCustFin = null;
				iCustFin = iCustFinService.custUKeyEq(iCustUKey, this.index, this.limit, titaVo);
				if(iCustFin==null) {
					throw new LogicException("E0001", "公司戶財務狀況檔無此公司名稱:"+iCustFullname); // 查無資料
				}
				for (CustFin tCustFin:iCustFin) {
					OccursList occursList = new OccursList();
					occursList.putParam("OOCustId", xCustMain.getCustId());
					occursList.putParam("OODataYear", Integer.valueOf(tCustFin.getDataYear())-1911);
					occursList.putParam("OOATotal", tCustFin.getAssetTotal());
					occursList.putParam("OOLTotal", tCustFin.getLiabTotal());
					occursList.putParam("OOCap", tCustFin.getCapital());
					occursList.putParam("OONetIncome", tCustFin.getNetIncome());
					//配合L1107帶入歷史資料新增
					occursList.putParam("OOCash", tCustFin.getCash());
					occursList.putParam("OOShortInv", tCustFin.getShortInv());
					occursList.putParam("OOAR", tCustFin.getAR());
					occursList.putParam("OOInventory", tCustFin.getInventory());
					occursList.putParam("OOLongInv", tCustFin.getLongInv());
					occursList.putParam("OOFixedAsset", tCustFin.getFixedAsset());
					occursList.putParam("OOOtherAsset", tCustFin.getOtherAsset());
					occursList.putParam("OOBankLoan", tCustFin.getBankLoan());
					occursList.putParam("OOOtherCurrLiab", tCustFin.getOtherCurrLiab());
					occursList.putParam("OOLongLiab", tCustFin.getLongLiab());
					occursList.putParam("OOOtherLiab", tCustFin.getOtherLiab());
					occursList.putParam("OONewWorthTotal", tCustFin.getNetWorthTotal());
					occursList.putParam("OORetainEarning", tCustFin.getRetainEarning());
					occursList.putParam("OOOpIncome", tCustFin.getOpIncome());
					occursList.putParam("OOOpCost", tCustFin.getOpCost());
					occursList.putParam("OOOpProfit", tCustFin.getOpProfit());
					occursList.putParam("OOOpExpense", tCustFin.getOpExpense());
					occursList.putParam("OOOpRevenue", tCustFin.getOpRevenue());
					occursList.putParam("OONopIncome", tCustFin.getNopIncome());
					occursList.putParam("OOFinExpense", tCustFin.getFinExpense());
					occursList.putParam("OONopExpense", tCustFin.getNopExpense());
					occursList.putParam("OOAccountant", tCustFin.getAccountant());
					occursList.putParam("OOAccountDate", tCustFin.getAccountDate());
					this.totaVo.addOccursList(occursList);
				}
			}
		}else {
			this.info("統編");
			CustMain iCustMain  = new CustMain();
			Slice<CustFin> iCustFin = null;
			String iCustUKey = "";
			iCustMain = iCustMainService.custIdFirst(iCustId, titaVo);
			if(iCustMain==null) {
				throw new LogicException("E0001", "客戶資料主檔無此統一編號:"+iCustId); // 查無資料
			}
			iCustUKey = iCustMain.getCustUKey();
			iCustFin = iCustFinService.custUKeyEq(iCustUKey, this.index, this.limit, titaVo);
			if(iCustFin == null) {
				throw new LogicException("E0001", "公司戶財務狀況檔無此統一編號:"+iCustId); // 查無資料
			}
			for(CustFin tCustFin:iCustFin) {
				OccursList occursList = new OccursList();
				occursList.putParam("OOCustId", iCustId);
				occursList.putParam("OODataYear", Integer.valueOf(tCustFin.getDataYear())-1911);
				occursList.putParam("OOATotal", tCustFin.getAssetTotal());
				occursList.putParam("OOLTotal", tCustFin.getLiabTotal());
				occursList.putParam("OOCap", tCustFin.getCapital());
				occursList.putParam("OONetIncome", tCustFin.getNetIncome());
				//配合L1107帶入歷史資料新增
				occursList.putParam("OOCash", tCustFin.getCash());
				occursList.putParam("OOShortInv", tCustFin.getShortInv());
				occursList.putParam("OOAR", tCustFin.getAR());
				occursList.putParam("OOInventory", tCustFin.getInventory());
				occursList.putParam("OOLongInv", tCustFin.getLongInv());
				occursList.putParam("OOFixedAsset", tCustFin.getFixedAsset());
				occursList.putParam("OOOtherAsset", tCustFin.getOtherAsset());
				occursList.putParam("OOBankLoan", tCustFin.getBankLoan());
				occursList.putParam("OOOtherCurrLiab", tCustFin.getOtherCurrLiab());
				occursList.putParam("OOLongLiab", tCustFin.getLongLiab());
				occursList.putParam("OOOtherLiab", tCustFin.getOtherLiab());
				occursList.putParam("OONewWorthTotal", tCustFin.getNetWorthTotal());
				occursList.putParam("OORetainEarning", tCustFin.getRetainEarning());
				occursList.putParam("OOOpIncome", tCustFin.getOpIncome());
				occursList.putParam("OOOpCost", tCustFin.getOpCost());
				occursList.putParam("OOOpProfit", tCustFin.getOpProfit());
				occursList.putParam("OOOpExpense", tCustFin.getOpExpense());
				occursList.putParam("OOOpRevenue", tCustFin.getOpRevenue());
				occursList.putParam("OONopIncome", tCustFin.getNopIncome());
				occursList.putParam("OOFinExpense", tCustFin.getFinExpense());
				occursList.putParam("OONopExpense", tCustFin.getNopExpense());
				occursList.putParam("OOAccountant", tCustFin.getAccountant());
				occursList.putParam("OOAccountDate", tCustFin.getAccountDate());
				this.totaVo.addOccursList(occursList);
			}
		}
		

		this.addList(this.totaVo);
		return this.sendList();
	}
}