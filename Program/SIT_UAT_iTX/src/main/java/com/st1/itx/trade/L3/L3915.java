package com.st1.itx.trade.L3;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AcReceivable;
import com.st1.itx.db.domain.CdBank;
import com.st1.itx.db.domain.CdBankId;
import com.st1.itx.db.domain.ClFac;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.FacMainId;
import com.st1.itx.db.domain.FacProd;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.FacProdService;
import com.st1.itx.db.service.AcReceivableService;
import com.st1.itx.db.service.CdBankService;
import com.st1.itx.db.service.ClFacService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.AcBookCom;
import com.st1.itx.util.common.AuthLogCom;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;

/*
 * Tita
 * CaseNo=9,7
 * TimCustNo=9,7
 * CustId=X,10
 * ApplNo=9,7
 * FacmNo=9,3
 */
/**
 * L3915 額度資料查詢
 * 
 * @author iza
 * @version 1.0.0
 */
@Service("L3915") // 額度資料查詢
@Scope("prototype")
public class L3915 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L3915.class);

	/* DB服務注入 */
	@Autowired
	public FacMainService facMainService;
	@Autowired
	public FacProdService facProdService;
	@Autowired
	public AcReceivableService acReceivableService;
	@Autowired
	public CdBankService cdBankService;
	@Autowired
	public ClFacService clFacService;

	@Autowired
	Parse parse;

	@Autowired
	public AuthLogCom authLogCom;
	@Autowired
	public AcBookCom acBooKCom;

	private TempVo tempVo = new TempVo();

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L3915 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iCaseNo = this.parse.stringToInteger(titaVo.getParam("CaseNo"));
		int iCustNo = this.parse.stringToInteger(titaVo.getParam("TimCustNo"));
		int iFacmNo = this.parse.stringToInteger(titaVo.getParam("FacmNo"));

		// work area
		int wkCustNo;
		int wkFacmNo;
		String acSubBookCode = "";
		List<ClFac> lClFac = new ArrayList<ClFac>();

		FacMain tFacMain;
		if (iCaseNo > 0) {
			tFacMain = facMainService.facmCreditSysNoFirst(iCaseNo, iCaseNo, iFacmNo, iFacmNo, titaVo);
			if (tFacMain == null) {
				throw new LogicException(titaVo, "E0001", "額度主檔 案件編號 = " + iCaseNo); // 查詢資料不存在
			}
		} else {
			tFacMain = facMainService.findById(new FacMainId(iCustNo, iFacmNo), titaVo);
			if (tFacMain == null) {
				throw new LogicException(titaVo, "E0001", "額度主檔 借款人戶號 = " + iCustNo + " 額度編號 = " + iFacmNo); // 查詢資料不存在
			}
		}
		wkCustNo = tFacMain.getCustNo();
		wkFacmNo = tFacMain.getFacmNo();

		// 查詢商品參數檔
		FacProd tFacProd = facProdService.findById(tFacMain.getProdNo(), titaVo);
		if (tFacProd == null) {
			throw new LogicException(titaVo, "E0001", "商品參數檔"); // 查詢資料不存在
		}
		// 查詢會計銷帳檔
		String wkAcBookCode = ""; // 帳冊別
		AcReceivable tAcReceivable = acReceivableService.acrvFacmNoFirst(wkCustNo, 0, wkFacmNo, titaVo);
		if (tAcReceivable != null) {
			wkAcBookCode = tAcReceivable.getAcBookCode();
		}

		tempVo = authLogCom.exec(wkCustNo, iFacmNo, titaVo);
		if (tempVo == null) {
			throw new LogicException(titaVo, "E0001", "L2R05 額度主檔 借款人戶號 = " + wkCustNo + " 額度編號 = " + iFacmNo); // 查詢資料不存在
		}

		// 查詢行庫代號檔
		String wkRepayBankItem = "";
//		String iBankCode = FormatUtil.padX(tFacMain.getRepayBank().trim(), 7);
		String iBankCode = FormatUtil.padX(tempVo.getParam("RepayBank").trim(), 7);
		String bankCode = FormatUtil.padX(iBankCode, 3);
		String branchCode = FormatUtil.right(iBankCode, 4);
		CdBank tCdBank = cdBankService.findById(new CdBankId(bankCode, branchCode), titaVo);
		if (tCdBank != null) {
			wkRepayBankItem = tCdBank.getBankItem();
		}

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 1000; // 379 + 28 * 1000 = 28379

		Slice<ClFac> slClFac = clFacService.facmNoEq(iCustNo, iFacmNo, this.index, this.limit, titaVo);
		lClFac = slClFac == null ? null : slClFac.getContent();

		if (lClFac != null && lClFac.size() > 0) {
			for (ClFac cl : lClFac) {
				OccursList occursList = new OccursList();
				occursList.putParam("OOClKey1", cl.getClCode1());
				occursList.putParam("OOClKey2", cl.getClCode2());
				occursList.putParam("OOClKey3", cl.getClNo());
				occursList.putParam("OOApplNo", cl.getApproveNo());
				occursList.putParam("OOCustNo", cl.getCustNo());
				occursList.putParam("OOFacmNo", cl.getFacmNo());
				occursList.putParam("OOMainFlag", cl.getMainFlag());
				// 將每筆資料放入Tota的OcList
				this.totaVo.addOccursList(occursList);
			}
		}

		String[] strAr = acBooKCom.getAcBookCode(tFacMain.getCustNo(), titaVo);
		if(strAr != null) {
			acSubBookCode = strAr[1];
		}

		this.totaVo.putParam("OCustNo", tFacMain.getCustNo());
		this.totaVo.putParam("OFacmNo", tFacMain.getFacmNo());
		this.totaVo.putParam("AcctCode", tFacMain.getAcctCode());
		this.totaVo.putParam("CaptialSource", acSubBookCode);
		this.totaVo.putParam("OApplNo", tFacMain.getApplNo());
		this.totaVo.putParam("ProdNo", tFacMain.getProdNo());
		this.totaVo.putParam("ProdName", tFacProd.getProdName());
		this.totaVo.putParam("CurrencyCode", tFacMain.getCurrencyCode());
		this.totaVo.putParam("LineAmt", tFacMain.getLineAmt());
		this.totaVo.putParam("LoanTermYy", tFacMain.getLoanTermYy());
		this.totaVo.putParam("LoanTermMm", tFacMain.getLoanTermMm());
		this.totaVo.putParam("LoanTermDd", tFacMain.getLoanTermDd());
		this.totaVo.putParam("MaturityDate", tFacMain.getMaturityDate());
		this.totaVo.putParam("BaseRateCode", tFacMain.getBaseRateCode());
		if (tFacProd.getIncrFlag().equals("Y")) {
			this.totaVo.putParam("ProdRate", tFacMain.getApproveRate().subtract(tFacMain.getRateIncr()));
		} else {
			this.totaVo.putParam("ProdRate", tFacMain.getApproveRate().subtract(tFacMain.getIndividualIncr()));
		}
		this.totaVo.putParam("RateIncr", tFacMain.getRateIncr());
		this.totaVo.putParam("IndividualIncr", tFacMain.getIndividualIncr());
		this.totaVo.putParam("ApproveRate", tFacMain.getApproveRate());
		this.totaVo.putParam("PROHIBITPERIOD", 0);
		this.totaVo.putParam("RateCode", tFacMain.getRateCode());
		this.totaVo.putParam("FirstRateAdjFreq", tFacMain.getFirstRateAdjFreq());
		this.totaVo.putParam("RateAdjFreq", tFacMain.getRateAdjFreq());
		this.totaVo.putParam("ExtraRepayCode", tFacMain.getExtraRepayCode());
		this.totaVo.putParam("FreqBase", tFacMain.getFreqBase());
		this.totaVo.putParam("RepayFreq", tFacMain.getRepayFreq());
		this.totaVo.putParam("PayIntFreq", tFacMain.getPayIntFreq());
		this.totaVo.putParam("RecycleCode", tFacMain.getRecycleCode());
		this.totaVo.putParam("RecycleDeadline", tFacMain.getRecycleDeadline());
		this.totaVo.putParam("UtilDeadline", tFacMain.getUtilDeadline());
		this.totaVo.putParam("BreachCode", tFacProd.getBreachCode());
		this.totaVo.putParam("UsageCode", tFacMain.getUsageCode());
		this.totaVo.putParam("CustTypeCode", tFacMain.getCustTypeCode());
		this.totaVo.putParam("IncomeTaxFlag", tFacMain.getIncomeTaxFlag());
		this.totaVo.putParam("CompensateFlag", tFacMain.getCompensateFlag());
		this.totaVo.putParam("IntCalcCode", tFacMain.getIntCalcCode());
		this.totaVo.putParam("AmortizedCode", tFacMain.getAmortizedCode());
		this.totaVo.putParam("GracePeriod", tFacMain.getGracePeriod());
		this.totaVo.putParam("RepayCode", tFacMain.getRepayCode());
		this.totaVo.putParam("RepayBank", tempVo.getParam("RepayBank"));
//		this.totaVo.putParam("RepayBank", tFacMain.getRepayBank());
		this.totaVo.putParam("RepayBankItem", wkRepayBankItem);
		this.totaVo.putParam("RepayAcctNo", tempVo.getParam("RepayAcctNo"));
//		this.totaVo.putParam("RepayAcctNo", tFacMain.getRepayAcctNo());
		this.totaVo.putParam("PostCode", tempVo.getParam("PostCode"));
//		this.totaVo.putParam("PostCode", tFacMain.getPostCode());
		this.totaVo.putParam("Collateral", wkAcBookCode);
		this.totaVo.putParam("AcctFee", tFacMain.getAcctFee());
		this.totaVo.putParam("RelationCode", tempVo.getParam("RelationCode"));
//		this.totaVo.putParam("RelationCode", tFacMain.getRelationCode());
		this.totaVo.putParam("RelationName", tempVo.getParam("RelationName"));
//		this.totaVo.putParam("RelationName", tFacMain.getRelationName());
		this.totaVo.putParam("RelationId", tempVo.getParam("RelationId"));
//		this.totaVo.putParam("RelationId", tFacMain.getRelationId());
		this.totaVo.putParam("RelationBirthday", tempVo.getParam("RelationBirthday"));
//		this.totaVo.putParam("RelationBirthday", tFacMain.getRelationBirthday());
		this.totaVo.putParam("RelationGender", tempVo.getParam("RelationGender"));
//		this.totaVo.putParam("RelationGender", tFacMain.getRelationGender());

		this.addList(this.totaVo);
		return this.sendList();
	}
}