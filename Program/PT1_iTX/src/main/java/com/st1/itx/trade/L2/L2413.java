package com.st1.itx.trade.L2;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.ClFac;
import com.st1.itx.db.domain.ClFacId;
import com.st1.itx.db.domain.ClMain;
import com.st1.itx.db.domain.ClMainId;
import com.st1.itx.db.domain.ClStock;
import com.st1.itx.db.domain.ClStockId;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.service.ClFacService;
import com.st1.itx.db.service.ClMainService;
import com.st1.itx.db.service.ClStockService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacCaseApplService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.ClFacCom;
import com.st1.itx.util.common.GSeqCom;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L2413")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2413 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CustMainService sCustMainService;

	/* DB服務注入 */
	@Autowired
	public ClMainService sClMainService;

	/* DB服務注入 */
	@Autowired
	public ClStockService sClStockService;
	@Autowired
	public ClFacService sClFacService;
	@Autowired
	public FacMainService sFacMainService;
	@Autowired
	public FacCaseApplService sFacCaseApplService;

	/* 自動取號 */
	@Autowired
	public GSeqCom gGSeqCom;

	@Autowired
	public ClFacCom clFacCom;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	@Autowired
	public DataLog dataLog;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	private boolean isEloan = false;
	// 核准號碼
	private int iApplNo;
	private FacMain tFacMain;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2413 ");
		this.totaVo.init(titaVo);

		// isEloan
		if (titaVo.isEloan() || "ELTEST".equals(titaVo.getTlrNo())) {
			this.isEloan = true;
		}

		// tita
		int iFunCd = parse.stringToInteger(titaVo.getParam("FunCd"));
		int iClCode1 = parse.stringToInteger(titaVo.getParam("ClCode1"));
		int iClCode2 = parse.stringToInteger(titaVo.getParam("ClCode2"));
		int iClNo = parse.stringToInteger(titaVo.getParam("ClNo"));
		iApplNo = parse.stringToInteger(titaVo.getParam("ApplNo"));

		// new table PK
		ClMainId tClMainId = new ClMainId();
		ClStockId tClStockId = new ClStockId();

		// new table 裝tita
		ClMain tClMain = new ClMain();
		ClStock tClStock = new ClStock();

		String showNewClNo = "";

		if (isEloan && iFunCd == 1) {
			if (iClNo > 0) {
				tClStock = sClStockService.findById(tClStockId, titaVo);
				if (tClStock != null) {
					iFunCd = 2;
				}
			} else {
				int ClNo = uniqueCheck(titaVo);
				if (ClNo > 0) {
					iFunCd = 2;
					iClNo = ClNo;
				}
			}
		}

		if (iApplNo > 0) { // 核准編號大於0才去做
			tFacMain = sFacMainService.facmApplNoFirst(iApplNo, titaVo);
			if (tFacMain == null) {
				throw new LogicException("E2019", "核准號碼 = " + iApplNo);
			} else {
//				ELOAN:擔保品案件在上送的時候，會回寫額度設定日，傳的日期就是上送當下的日期
				if (isEloan) {
					FacMain updFacMain = sFacMainService.holdById(tFacMain, titaVo);
					if (updFacMain != null) {
						updFacMain.setSettingDate(this.txBuffer.getTxCom().getTbsdy());
						try {
							sFacMainService.update(updFacMain, titaVo);
						} catch (DBException e) {
							throw new LogicException("E0007", "額度主檔" + e.getErrorMsg());
						}
					}

				}
			}
		}

		if (iFunCd == 1) {

			// 取號使用參數
			String Colind4s = StringUtils.leftPad(String.valueOf(iClCode1), 2, "0")
					+ StringUtils.leftPad(String.valueOf(iClCode2), 2, "0");

			this.info("FOR Colind4s = " + Colind4s);
			// 新增時取號 進擔保品代號檔取最後使用碼+1

			iClNo = gGSeqCom.getSeqNo(0, 0, "L2", Colind4s, 9999999, titaVo);
			showNewClNo = StringUtils.leftPad(String.valueOf(iClNo), 7, "0");

			tClMainId.setClCode1(iClCode1);
			tClMainId.setClCode2(iClCode2);
			tClMainId.setClNo(iClNo);

			tClMain.setClMainId(tClMainId);
			tClMain.setClCode1(iClCode1);
			tClMain.setClCode2(iClCode2);
			tClMain.setClNo(iClNo);
//			tClMain.setCustUKey(custUKey);
			tClMain.setClTypeCode(titaVo.getParam("ClTypeCode"));
			tClMain.setCityCode(titaVo.getParam("CityCode"));/* 地區別 */
			tClMain.setClStatus(titaVo.getParam("ClStatus"));
			tClMain.setEvaDate(parse.stringToInteger(titaVo.getParam("EvaDate")));
			tClMain.setEvaAmt(parse.stringToBigDecimal(titaVo.getParam("EvaAmt")));
			tClMain.setSynd(titaVo.getParam("Synd"));
			tClMain.setSyndCode(titaVo.getParam("SyndCode"));
			tClMain.setDispPrice(parse.stringToBigDecimal(titaVo.getParam("DispPrice")));
			tClMain.setDispDate(parse.stringToInteger(titaVo.getParam("DispDate")));

			// 計算可分配金額
			BigDecimal shareTotal = new BigDecimal(0);

			// 鑑估總值
			BigDecimal evaAmt = new BigDecimal(titaVo.getParam("EvaAmt"));

			// 貸放成數
			BigDecimal loanToValue = new BigDecimal(titaVo.getParam("LoanToValue"));

			this.info("L2411 evaAmt = " + evaAmt.toString());
			this.info("L2411 loanToValue = " + loanToValue.toString());

			// 可分配金額=鑑估總值*貸放成數(四捨五入至個位數)
			// 同一擔保品在ClFac擔保品關聯檔的分配金額加總需小於ClMain擔保品主檔的可分配金額
			shareTotal = evaAmt.multiply(loanToValue).divide(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_HALF_UP);

			this.info("L2411 shareTotal = " + shareTotal.toString());

			tClMain.setShareTotal(shareTotal);

			tClStockId.setClCode1(iClCode1);
			tClStockId.setClCode2(iClCode2);
			tClStockId.setClNo(iClNo);

			tClStock.setClStockId(tClStockId);
			tClStock.setClCode1(iClCode1);
			tClStock.setClCode2(iClCode2);
			tClStock.setClNo(iClNo);
			tClStock.setStockCode(titaVo.getParam("StockCode"));
			tClStock.setListingType(titaVo.getParam("ListingType"));
			tClStock.setStockType(titaVo.getParam("StockType"));
			tClStock.setCompanyId(titaVo.getParam("CompanyId"));
			tClStock.setDataYear(parse.stringToInteger(titaVo.getParam("DataYear")));
			tClStock.setIssuedShares(parse.stringToBigDecimal(titaVo.getParam("IssuedShares")));
			tClStock.setNetWorth(parse.stringToBigDecimal(titaVo.getParam("NetWorth")));
			tClStock.setEvaStandard(titaVo.getParam("EvaStandard"));
			tClStock.setParValue(parse.stringToBigDecimal(titaVo.getParam("ParValue")));
			tClStock.setMonthlyAvg(parse.stringToBigDecimal(titaVo.getParam("MonthlyAvg")));
			tClStock.setYdClosingPrice(parse.stringToBigDecimal(titaVo.getParam("YdClosingPrice")));
			tClStock.setThreeMonthAvg(parse.stringToBigDecimal(titaVo.getParam("ThreeMonthAvg")));
			tClStock.setEvaUnitPrice(parse.stringToBigDecimal(titaVo.getParam("EvaUnitPrice")));

			tClStock = setOwnerCustUKey(tClStock, titaVo);

			tClStock.setInsiderJobTitle(titaVo.getParam("InsiderJobTitle"));
			tClStock.setInsiderPosition(titaVo.getParam("InsiderPosition"));
			tClStock.setLegalPersonId(titaVo.getParam("LegalPersonId"));
			tClStock.setLoanToValue(parse.stringToBigDecimal(titaVo.getParam("LoanToValue")));
			tClStock.setClMtr(parse.stringToBigDecimal(titaVo.getParam("ClMtr")));
			tClStock.setNoticeMtr(parse.stringToBigDecimal(titaVo.getParam("NoticeMtr")));
			tClStock.setImplementMtr(parse.stringToBigDecimal(titaVo.getParam("ImplementMtr")));
			tClStock.setPledgeNo(titaVo.getParam("PledgeNo"));
			tClStock.setComputeMTR(titaVo.getParam("ComputeMTR"));
			tClStock.setSettingStat(titaVo.getParam("SettingStat"));
			tClStock.setClStat(titaVo.getParam("ClStat"));
			tClStock.setSettingDate(parse.stringToInteger(titaVo.getParam("SettingDate")));
			tClStock.setSettingBalance(parse.stringToBigDecimal(titaVo.getParam("SettingBalance")));
			tClStock.setMtgDate(parse.stringToInteger(titaVo.getParam("MtgDate")));
			tClStock.setCustodyNo(titaVo.getParam("CustodyNo"));

			try {
				sClMainService.insert(tClMain);
			} catch (DBException e) {
				throw new LogicException("E0005", "擔保品主檔");
			}
			try {
				sClStockService.insert(tClStock);
			} catch (DBException e) {
				throw new LogicException("E0005", "擔保品股票檔");
			}

			if (iApplNo > 0) {
				List<HashMap<String, String>> ownerMap = new ArrayList<HashMap<String, String>>();
				String iOwnerId = titaVo.getParam("OwnerId");

				CustMain custMain = sCustMainService.custIdFirst(iOwnerId, titaVo);
				if (custMain != null) {
					String custUKey = custMain.getCustUKey().trim();
					String relCode = titaVo.getParam("OwnerRelCode").trim();
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("OwnerCustUKey", custUKey);
					map.put("OwnerRelCode", relCode);
					ownerMap.add(map);
				}
				clFacCom.insertClFac(titaVo, iClCode1, iClCode2, iClNo, iApplNo, ownerMap);

			} // if

		} else {
			// 塞pk
			tClMainId.setClCode1(iClCode1);
			tClMainId.setClCode2(iClCode2);
			tClMainId.setClNo(iClNo);

			// 塞pk
			tClStockId.setClCode1(iClCode1);
			tClStockId.setClCode2(iClCode2);
			tClStockId.setClNo(iClNo);

			tClMain = sClMainService.holdById(tClMainId);

			if (tClMain == null) {
				if (iFunCd == 2) {
					throw new LogicException("E0003", "擔保品主檔");
				} else if (iFunCd == 4) {
					throw new LogicException("E0004", "擔保品主檔");
				}
			}

			tClStock = sClStockService.holdById(tClStockId);
			if (tClStock == null) {
				if (iFunCd == 2) {
					throw new LogicException("E0003", "擔保品股票檔");
				} else if (iFunCd == 4) {
					throw new LogicException("E0004", "擔保品股票檔");
				}
			}

			if (iFunCd == 2) {

				tClStock = sClStockService.holdById(tClStockId);

				// 變更前
				ClMain beforeClMain = (ClMain) dataLog.clone(tClMain);
				ClStock beforeClStock = (ClStock) dataLog.clone(tClStock);

				tClMain.setClTypeCode(titaVo.getParam("ClTypeCode"));
				tClMain.setCityCode(titaVo.getParam("CityCode"));/* 地區別 */
				tClMain.setClStatus(titaVo.getParam("ClStatus"));
				tClMain.setEvaDate(parse.stringToInteger(titaVo.getParam("EvaDate")));
				tClMain.setEvaAmt(parse.stringToBigDecimal(titaVo.getParam("EvaAmt")));
				tClMain.setSynd(titaVo.getParam("Synd"));
				tClMain.setSyndCode(titaVo.getParam("SyndCode"));
				tClMain.setDispPrice(parse.stringToBigDecimal(titaVo.getParam("DispPrice")));
				tClMain.setDispDate(parse.stringToInteger(titaVo.getParam("DispDate")));

				// 計算可分配金額
				BigDecimal shareTotal = new BigDecimal(0);

				// 鑑估總值
				BigDecimal evaAmt = new BigDecimal(titaVo.getParam("EvaAmt"));

				// 貸放成數
				BigDecimal loanToValue = new BigDecimal(titaVo.getParam("LoanToValue"));

				this.info("L2413 evaAmt = " + evaAmt.toString());
				this.info("L2413 loanToValue = " + loanToValue.toString());

//				"1.若""評估淨值""有值取""評估淨值""否則取""鑑估總值"")*貸放成數(四捨五入至個位數)
//				2.若設定金額低於可分配金額則為設定金額
//				3.擔保品塗銷/解除設定時(該筆擔保品的可分配金額設為零)"

				shareTotal = evaAmt.multiply(loanToValue).divide(new BigDecimal(100)).setScale(0,
						BigDecimal.ROUND_HALF_UP);

				if ("1".equals(titaVo.getParam("ClStat")) || "2".equals(titaVo.getParam("SettingStat"))) {
					tClMain.setShareTotal(BigDecimal.ZERO);
				} else {
					tClMain.setShareTotal(shareTotal);
				}

				tClStock.setStockCode(titaVo.getParam("StockCode"));
				tClStock.setListingType(titaVo.getParam("ListingType"));
				tClStock.setStockType(titaVo.getParam("StockType"));
				tClStock.setCompanyId(titaVo.getParam("CompanyId"));
				tClStock.setDataYear(parse.stringToInteger(titaVo.getParam("DataYear")));
				tClStock.setIssuedShares(parse.stringToBigDecimal(titaVo.getParam("IssuedShares")));
				tClStock.setNetWorth(parse.stringToBigDecimal(titaVo.getParam("NetWorth")));
				tClStock.setEvaStandard(titaVo.getParam("EvaStandard"));
				tClStock.setParValue(parse.stringToBigDecimal(titaVo.getParam("ParValue")));
				tClStock.setMonthlyAvg(parse.stringToBigDecimal(titaVo.getParam("MonthlyAvg")));
				tClStock.setYdClosingPrice(parse.stringToBigDecimal(titaVo.getParam("YdClosingPrice")));
				tClStock.setThreeMonthAvg(parse.stringToBigDecimal(titaVo.getParam("ThreeMonthAvg")));
				tClStock.setEvaUnitPrice(parse.stringToBigDecimal(titaVo.getParam("EvaUnitPrice")));

				tClStock = setOwnerCustUKey(tClStock, titaVo);

				tClStock.setInsiderJobTitle(titaVo.getParam("InsiderJobTitle"));
				tClStock.setInsiderPosition(titaVo.getParam("InsiderPosition"));
				tClStock.setLegalPersonId(titaVo.getParam("LegalPersonId"));
				tClStock.setLoanToValue(parse.stringToBigDecimal(titaVo.getParam("LoanToValue")));
				tClStock.setClMtr(parse.stringToBigDecimal(titaVo.getParam("ClMtr")));
				tClStock.setNoticeMtr(parse.stringToBigDecimal(titaVo.getParam("NoticeMtr")));
				tClStock.setImplementMtr(parse.stringToBigDecimal(titaVo.getParam("ImplementMtr")));
				tClStock.setPledgeNo(titaVo.getParam("PledgeNo"));
				tClStock.setComputeMTR(titaVo.getParam("ComputeMTR"));
				tClStock.setSettingStat(titaVo.getParam("SettingStat"));
				tClStock.setClStat(titaVo.getParam("ClStat"));
				tClStock.setSettingDate(parse.stringToInteger(titaVo.getParam("SettingDate")));
				tClStock.setSettingBalance(parse.stringToBigDecimal(titaVo.getParam("SettingBalance")));
				tClStock.setMtgDate(parse.stringToInteger(titaVo.getParam("MtgDate")));
				tClStock.setCustodyNo(titaVo.getParam("CustodyNo"));

				try {
					// 修改
					tClMain = sClMainService.update2(tClMain);
				} catch (DBException e) {
					throw new LogicException("E0007", "擔保品主檔");
				}
				// 紀錄變更前變更後
				dataLog.setEnv(titaVo, beforeClMain, tClMain);
				dataLog.exec("修改擔保品主檔資料");
				try {
					// 修改
					tClStock = sClStockService.update2(tClStock);
				} catch (DBException e) {
					throw new LogicException("E0007", "擔保品股票檔");
				}
				// 紀錄變更前變更後
				dataLog.setEnv(titaVo, beforeClStock, tClStock);
				dataLog.exec("修改擔保品股票檔資料");

				if (iApplNo > 0 && this.isEloan) { // eloan 檢核不同核准號碼要新增額度關聯 2022.3.10
					List<HashMap<String, String>> ownerMap = new ArrayList<HashMap<String, String>>();
					String iOwnerId = titaVo.getParam("OwnerId");

					CustMain custMain = sCustMainService.custIdFirst(iOwnerId, titaVo);
					if (custMain != null) {
						String custUKey = custMain.getCustUKey().trim();
						String relCode = titaVo.getParam("OwnerRelCode").trim();
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("OwnerCustUKey", custUKey);
						map.put("OwnerRelCode", relCode);
						ownerMap.add(map);
					}

					ClFacId clFacId = new ClFacId();
					clFacId.setClCode1(iClCode1);
					clFacId.setClCode2(iClCode2);
					clFacId.setClNo(iClNo);
					clFacId.setApproveNo(iApplNo);
					ClFac clFac = sClFacService.findById(clFacId, titaVo);
					if (clFac == null) {
						clFacCom.insertClFac(titaVo, iClCode1, iClCode2, iClNo, iApplNo, ownerMap);
					}

				} // if

			} else if (iFunCd == 4) {
				/* 刪除 */
				try {

					if (tClStock != null) {
						sClStockService.delete(tClStock);
					}
				} catch (DBException e) {

					throw new LogicException("E0008", "擔保品股票檔");

				}
				try {

					sClMainService.delete(tClMain);
				} catch (DBException e) {
					throw new LogicException("E0008", "擔保品主檔");
				}

			}

		}

		this.totaVo.putParam("OClNo", showNewClNo);

		this.addList(this.totaVo);
		return this.sendList();
	}

	private ClStock setOwnerCustUKey(ClStock clStock, TitaVo titaVo) throws LogicException {

		CustMain custMain = sCustMainService.custIdFirst(titaVo.getParam("OwnerId"), titaVo);
		if (custMain == null) {
			String Ukey = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
			custMain = new CustMain();
			custMain.setCustUKey(Ukey);
			custMain.setCustId(titaVo.getParam("OwnerId"));
			custMain.setCustName(titaVo.getParam("OwnerName"));
			custMain.setDataStatus(1);
			custMain.setTypeCode(2);
			if (titaVo.getParam("OwnerId").length() == 8) {
				custMain.setCuscCd("2");
			} else {
				custMain.setCuscCd("1");
			}
			try {
				sCustMainService.insert(custMain, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "客戶資料主檔");
			}
		}
		clStock.setOwnerCustUKey(custMain.getCustUKey());

		return clStock;
	}

	// 股票判斷同一擔保品規則：股票代號+股票持有人統編
	private int uniqueCheck(TitaVo titaVo) throws LogicException {
		int clNo = 0;

		int iClCode1 = parse.stringToInteger(titaVo.getParam("ClCode1"));
		int iClCode2 = parse.stringToInteger(titaVo.getParam("ClCode2"));

		CustMain custMain = sCustMainService.custIdFirst(titaVo.getParam("OwnerId"), titaVo);
		if (custMain != null) {
			Slice<ClStock> sClStock = sClStockService.findUnique(titaVo.getParam("StockCode"), custMain.getCustId(), 0,
					Integer.MAX_VALUE);
			List<ClStock> lClStock = sClStock == null ? null : sClStock.getContent();
			if (lClStock != null) {
				for (ClStock clStock : lClStock) {
					if (clStock.getClCode1() == iClCode1 && clStock.getClCode2() == iClCode2) {
						clNo = clStock.getClNo();
					} else {
						throw new LogicException("E0012", "股票同一擔保品規則 =>股票代號+股票持有人統編");
					}

				}
			}
		}
		return clNo;
	}
}