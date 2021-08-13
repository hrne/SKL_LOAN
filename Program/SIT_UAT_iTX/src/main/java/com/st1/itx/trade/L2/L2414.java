package com.st1.itx.trade.L2;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.ClMain;
import com.st1.itx.db.domain.ClMainId;
import com.st1.itx.db.domain.ClOther;
import com.st1.itx.db.domain.ClOtherId;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.service.ClMainService;
import com.st1.itx.db.service.ClOtherService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.GSeqCom;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * END=X,1<br>
 */

@Service("L2414")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2414 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CustMainService sCustMainService;

	/* DB服務注入 */
	@Autowired
	public ClMainService sClMainService;

	/* DB服務注入 */
	@Autowired
	public ClOtherService sClOtherService;

	/* 自動取號 */
	@Autowired
	GSeqCom gGSeqCom;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Autowired
	public DataLog dataLog;

	private boolean isEloan = false;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2414 ");
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
		String iCustId = titaVo.getParam("CustId").trim();
		int iCustNo = parse.stringToInteger(titaVo.getParam("CustNo"));

		// new table PK
		ClMainId tClMainId = new ClMainId();
		ClOtherId tClOtherId = new ClOtherId();

		// new table 裝tita
		CustMain tCustMain = new CustMain();
		ClMain tClMain = new ClMain();
		ClOther tClOther = new ClOther();

		// 塞pk
		tClMainId.setClCode1(iClCode1);
		tClMainId.setClCode2(iClCode2);
		tClMainId.setClNo(iClNo);

		// 塞pk
		tClOtherId.setClCode1(iClCode1);
		tClOtherId.setClCode2(iClCode2);
		tClOtherId.setClNo(iClNo);

		// 宣告
		String custUKey = "";
		String showNewClNo = "";

		if (isEloan && iFunCd == 1) {
			if (iClNo > 0) {
				iFunCd = 2;
			} else {
				int ClNo = uniqueCheck(titaVo);
				if (ClNo > 0) {
					iFunCd = 2;
					iClNo = ClNo;
				}
			}
		}

		if (iFunCd == 1) {

			// 錯誤處理
			if (iCustId.isEmpty() && iCustNo == 0) {
				throw new LogicException("E2051", "");
			}

			if (!iCustId.isEmpty()) {
				tCustMain = sCustMainService.custIdFirst(iCustId);
				// 錯誤處理
				if (tCustMain == null) {
					throw new LogicException("E1003", iCustId);
				}
			} else {
				tCustMain = sCustMainService.custNoFirst(iCustNo, iCustNo);
				// 錯誤處理
				if (tCustMain == null) {
					throw new LogicException("E1004", String.valueOf(iCustNo));
				}
			}

			custUKey = tCustMain.getCustUKey();

			// 取號使用參數
			String Colind4s = StringUtils.leftPad(String.valueOf(iClCode1), 2, "0") + StringUtils.leftPad(String.valueOf(iClCode2), 2, "0");

			this.info("Colind4s=" + Colind4s);
			iClNo = gGSeqCom.getSeqNo(0, 0, "L2", Colind4s, 9999999, titaVo);

			showNewClNo = StringUtils.leftPad(String.valueOf(iClNo), 7, "0");

			tClMain = new ClMain();
			tClOther = new ClOther();

			tClMainId = new ClMainId();
			tClOtherId = new ClOtherId();

			// 塞pk
			tClMainId.setClCode1(iClCode1);
			tClMainId.setClCode2(iClCode2);
			tClMainId.setClNo(iClNo);

			// 塞pk
			tClOtherId.setClCode1(iClCode1);
			tClOtherId.setClCode2(iClCode2);
			tClOtherId.setClNo(iClNo);

			tClMain.setClMainId(tClMainId);
			tClMain.setClCode1(iClCode1);
			tClMain.setClCode2(iClCode2);
			tClMain.setClNo(iClNo);
			tClMain.setCustUKey(custUKey);
			tClMain.setClTypeCode(titaVo.getParam("ClTypeCode"));
			tClMain.setCityCode(titaVo.getParam("CityCode"));/* 地區別 */
			tClMain.setClStatus(titaVo.getParam("ClStatus"));
			tClMain.setEvaDate(parse.stringToInteger(titaVo.getParam("EvaDate")));
			if (titaVo.getParam("ClTypeCode").equals("998") || titaVo.getParam("ClTypeCode").equals("999")) {
				tClMain.setEvaAmt(parse.stringToBigDecimal(titaVo.getParam("PledgeAmt")));
			} else {
				tClMain.setEvaAmt(parse.stringToBigDecimal(titaVo.getParam("EvaAmt")));
			}
			tClMain.setSynd(titaVo.getParam("Synd"));
			tClMain.setSyndCode(titaVo.getParam("SyndCode"));
			tClMain.setDispPrice(parse.stringToBigDecimal(titaVo.getParam("DispPrice")));
			tClMain.setDispDate(parse.stringToInteger(titaVo.getParam("DispDate")));

			// 計算可分配金額
			BigDecimal shareTotal = new BigDecimal(0);

			// 鑑估總值
			BigDecimal evaAmt = new BigDecimal(0);
			if (titaVo.getParam("ClTypeCode").equals("998") || titaVo.getParam("ClTypeCode").equals("999")) {
				evaAmt = new BigDecimal(titaVo.getParam("PledgeAmt"));
			} else {
				evaAmt = new BigDecimal(titaVo.getParam("EvaAmt"));
			}

			// 貸放成數
			BigDecimal loanToValue = new BigDecimal(titaVo.getParam("LoanToValue"));

			this.info("L2411 evaAmt = " + evaAmt.toString());
			this.info("L2411 loanToValue = " + loanToValue.toString());

			// 可分配金額=鑑估總值*貸放成數(四捨五入至個位數)
			// 同一擔保品在ClFac擔保品關聯檔的分配金額加總需小於ClMain擔保品主檔的可分配金額
			shareTotal = evaAmt.multiply(loanToValue).divide(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_HALF_UP);
			if (parse.stringToBigDecimal(titaVo.getParam("SettingBal")).compareTo(shareTotal) < 0) {
				shareTotal = parse.stringToBigDecimal(titaVo.getParam("SettingBal"));
			}
			this.info("L2411 shareTotal = " + shareTotal.toString());

			tClMain.setShareTotal(shareTotal);

			tClOther.setClOtherId(tClOtherId);
			tClOther.setClCode1(iClCode1);
			tClOther.setClCode2(iClCode2);
			tClOther.setClNo(iClNo);
			tClOther.setPledgeStartDate(parse.stringToInteger(titaVo.getParam("PledgeStartDate")));
			tClOther.setPledgeEndDate(parse.stringToInteger(titaVo.getParam("PledgeEndDate")));
			tClOther.setPledgeBankCode(titaVo.getParam("PledgeBankCode"));
			tClOther.setPledgeNO(titaVo.getParam("PledgeNO"));
			tClOther.setOwnerId(titaVo.getParam("OwnerId"));
			tClOther.setOwnerName(titaVo.getParam("OwnerName"));
			tClOther.setIssuingId(titaVo.getParam("IssuingId"));
			tClOther.setIssuingCounty(titaVo.getParam("IssuingCounty"));
			tClOther.setDocNo(titaVo.getParam("DocNo"));
			tClOther.setLoanToValue(new BigDecimal(titaVo.getParam("LoanToValue")));

			tClOther.setSecuritiesType(titaVo.getParam("SecuritiesType"));
			tClOther.setListed(titaVo.getParam("Listed"));
			tClOther.setOfferingDate(parse.stringToInteger(titaVo.getParam("OfferingDate")));
			tClOther.setExpirationDate(parse.stringToInteger(titaVo.getParam("ExpirationDate")));
			tClOther.setTargetIssuer(titaVo.getParam("TargetIssuer"));
			tClOther.setSubTargetIssuer(titaVo.getParam("SubTargetIssuer"));
			tClOther.setCreditDate(parse.stringToInteger(titaVo.getParam("CreditDate")));
			tClOther.setCredit(titaVo.getParam("Credit"));
			tClOther.setExternalCredit(titaVo.getParam("ExternalCredit"));
			tClOther.setIndex(titaVo.getParam("Index"));
			tClOther.setTradingMethod(titaVo.getParam("TradingMethod"));
			tClOther.setCompensation(titaVo.getParam("Compensation"));
			tClOther.setInvestment(titaVo.getParam("Investment"));
			tClOther.setPublicValue(titaVo.getParam("PublicValue"));

			tClOther.setSettingStat(titaVo.getParam("SettingStat"));
			tClOther.setClStat(titaVo.getParam("ClStat"));
			tClOther.setSettingDate(parse.stringToInteger(titaVo.getParam("SettingDate")));
			tClOther.setSettingAmt(new BigDecimal(titaVo.getParam("SettingBal")));

			try {
				sClMainService.insert(tClMain);
			} catch (DBException e) {
				throw new LogicException("E2009", "擔保品主檔");
			}
			try {
				sClOtherService.insert(tClOther);
			} catch (DBException e) {
				throw new LogicException("E2009", "擔保品其他檔");
			}

		} else if (iFunCd == 2) {

			tClMain = new ClMain();
			tClOther = new ClOther();
			// holde table修改
			tClMain = sClMainService.holdById(tClMainId);

			if (tClMain == null) {
				throw new LogicException("E0006", "擔保品主檔");
			}

			// holde table修改
			tClOther = sClOtherService.holdById(tClOtherId);
			if (tClOther == null) {
				throw new LogicException("E0006", "擔保品其他檔");
			}

			// 變更前
			ClMain beforeClMain = (ClMain) dataLog.clone(tClMain);

			tClMain.setClTypeCode(titaVo.getParam("ClTypeCode"));
			tClMain.setCityCode(titaVo.getParam("CityCode"));/* 地區別 */
			tClMain.setClStatus(titaVo.getParam("ClStatus"));
			tClMain.setEvaDate(parse.stringToInteger(titaVo.getParam("EvaDate")));
			if (titaVo.getParam("ClTypeCode").equals("998") || titaVo.getParam("ClTypeCode").equals("999")) {
				tClMain.setEvaAmt(parse.stringToBigDecimal(titaVo.getParam("PledgeAmt")));
			} else {
				tClMain.setEvaAmt(parse.stringToBigDecimal(titaVo.getParam("EvaAmt")));
			}
			tClMain.setSynd(titaVo.getParam("Synd"));
			tClMain.setSyndCode(titaVo.getParam("SyndCode"));
			tClMain.setDispPrice(parse.stringToBigDecimal(titaVo.getParam("DispPrice")));
			tClMain.setDispDate(parse.stringToInteger(titaVo.getParam("DispDate")));

			// 計算可分配金額
			BigDecimal shareTotal = new BigDecimal(0);

			// 鑑估總值
			BigDecimal evaAmt = new BigDecimal(0);
			if (titaVo.getParam("ClTypeCode").equals("998") || titaVo.getParam("ClTypeCode").equals("999")) {
				evaAmt = new BigDecimal(titaVo.getParam("PledgeAmt"));
			} else {
				evaAmt = new BigDecimal(titaVo.getParam("EvaAmt"));
			}

			// 貸放成數
			BigDecimal loanToValue = new BigDecimal(titaVo.getParam("LoanToValue"));

			this.info("L2414 evaAmt = " + evaAmt.toString());
			this.info("L2414 loanToValue = " + loanToValue.toString());


//			"1.若""評估淨值""有值取""評估淨值""否則取""鑑估總值"")*貸放成數(四捨五入至個位數)
//			2.若設定金額低於可分配金額則為設定金額
//			3.擔保品塗銷/解除設定時(該筆擔保品的可分配金額設為零)"

			shareTotal = evaAmt.multiply(loanToValue).divide(new BigDecimal(100)).setScale(0,
					BigDecimal.ROUND_HALF_UP);
			if (parse.stringToBigDecimal(titaVo.getParam("SettingAmt")).compareTo(shareTotal) < 0) {
				shareTotal = parse.stringToBigDecimal(titaVo.getParam("SettingAmt"));
			}

			
			if ("1".equals(titaVo.getParam("ClStat")) || "2".equals(titaVo.getParam("SettingStat"))) {
				tClMain.setShareTotal(BigDecimal.ZERO);
			} else {
				tClMain.setShareTotal(shareTotal);
			}

			// 變更前
			ClOther beforeClOther = (ClOther) dataLog.clone(tClOther);

			tClOther.setSecuritiesType(titaVo.getParam("SecuritiesType"));
			tClOther.setListed(titaVo.getParam("Listed"));
			tClOther.setOfferingDate(parse.stringToInteger(titaVo.getParam("OfferingDate")));
			tClOther.setExpirationDate(parse.stringToInteger(titaVo.getParam("ExpirationDate")));
			tClOther.setTargetIssuer(titaVo.getParam("TargetIssuer"));
			tClOther.setSubTargetIssuer(titaVo.getParam("SubTargetIssuer"));
			tClOther.setCreditDate(parse.stringToInteger(titaVo.getParam("CreditDate")));
			tClOther.setCredit(titaVo.getParam("Credit"));
			tClOther.setExternalCredit(titaVo.getParam("ExternalCredit"));
			tClOther.setIndex(titaVo.getParam("Index"));
			tClOther.setTradingMethod(titaVo.getParam("TradingMethod"));
			tClOther.setCompensation(titaVo.getParam("Compensation"));
			tClOther.setInvestment(titaVo.getParam("Investment"));
			tClOther.setPublicValue(titaVo.getParam("PublicValue"));
			tClOther.setPledgeStartDate(parse.stringToInteger(titaVo.getParam("PledgeStartDate")));
			tClOther.setPledgeEndDate(parse.stringToInteger(titaVo.getParam("PledgeEndDate")));
			tClOther.setPledgeBankCode(titaVo.getParam("PledgeBankCode"));
			tClOther.setPledgeNO(titaVo.getParam("PledgeNO"));
			tClOther.setOwnerId(titaVo.getParam("OwnerId"));
			tClOther.setOwnerName(titaVo.getParam("OwnerName"));
			tClOther.setIssuingId(titaVo.getParam("IssuingId"));
			tClOther.setIssuingCounty(titaVo.getParam("IssuingCounty"));
			tClOther.setDocNo(titaVo.getParam("DocNo"));
			tClOther.setLoanToValue(new BigDecimal(titaVo.getParam("LoanToValue")));
			tClOther.setSettingStat(titaVo.getParam("SettingStat"));
			tClOther.setClStat(titaVo.getParam("ClStat"));
			tClOther.setSettingDate(parse.stringToInteger(titaVo.getParam("SettingDate")));
			tClOther.setSettingAmt(new BigDecimal(titaVo.getParam("SettingBal")));

			try {
				tClMain = sClMainService.update2(tClMain);
			} catch (DBException e) {
				throw new LogicException("E0007", "擔保品主檔");
			}

			// 紀錄變更前變更後
			dataLog.setEnv(titaVo, beforeClMain, tClMain);
			dataLog.exec();

			try {
				tClOther = sClOtherService.update2(tClOther);
			} catch (DBException e) {
				throw new LogicException("E0007", "擔保品其他檔");
			}

			// 紀錄變更前變更後
			dataLog.setEnv(titaVo, beforeClOther, tClOther);
			dataLog.exec();

		} else if (iFunCd == 4) {
			/* 刪除 */

			tClOther = sClOtherService.holdById(tClOtherId);

			if (tClMain == null) {
				throw new LogicException("E0006", "擔保品主檔");
			}

			// holde table修改
			tClOther = sClOtherService.holdById(tClOtherId);
			if (tClOther == null) {
				throw new LogicException("E0006", "擔保品其他檔");
			}

			/* 刪除 */
			try {

				sClMainService.delete(tClMain);
			} catch (DBException e) {
				throw new LogicException("E0008", "擔保品主檔");
			}

			try {
				sClOtherService.delete(tClOther);
			} catch (DBException e) {
				throw new LogicException("E0008", "擔保品其他檔");
			}

		}

		this.totaVo.putParam("OClNo", showNewClNo);
		this.addList(this.totaVo);
		return this.sendList();
	}

	// 其他擔保品判斷同一擔保品規則：受益憑證=>發行機構統編+憑證編號+所有權人ID
	private int uniqueCheck(TitaVo titaVo) throws LogicException {
		int clNo = 0;

		int iClCode1 = parse.stringToInteger(titaVo.getParam("ClCode1"));
		int iClCode2 = parse.stringToInteger(titaVo.getParam("ClCode2"));

		Slice<ClOther> sClOther = sClOtherService.findUnique(titaVo.getParam("StockCode"), titaVo.getParam("OwnerId"), titaVo.getParam("OwnerId"), 0, Integer.MAX_VALUE);
		List<ClOther> lClOther = sClOther == null ? null : sClOther.getContent();
		if (lClOther != null) {
			for (ClOther clOther : lClOther) {
				if (clOther.getClCode1() == iClCode1 && clOther.getClCode2() == iClCode2) {
					clNo = clOther.getClNo();
				} else {
					throw new LogicException("E0012", "其他擔保品判斷同一擔保品規則：受益憑證=>發行機構統編+憑證編號+所有權人ID");
				}

			}
		}
		return clNo;
	}
}