package com.st1.itx.trade.L6;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AcDetail;
import com.st1.itx.db.domain.AcReceivable;
import com.st1.itx.db.domain.AcReceivableId;
import com.st1.itx.db.domain.CdAcCode;
import com.st1.itx.db.domain.LoanBorTx;
import com.st1.itx.db.domain.TxTranCode;
import com.st1.itx.db.service.AcDetailService;
import com.st1.itx.db.service.AcReceivableService;
import com.st1.itx.db.service.CdAcCodeService;
import com.st1.itx.db.service.LoanBorTxService;
import com.st1.itx.db.service.TxTranCodeService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/**
 * Tita AcBookCode=X,3 BranchNo=9,4 CurrencyCode=X,3 AcNoCode=X,8 AcSubCode=X,5
 * AcDtlCode=X,2 CustNo=9,7 FacmNo=9,3 RvNo=X,30 AcDateSt=9,7 AcDateEd=9,7
 * END=X,1
 */

@Service("L6908") // 銷帳歷史明細查詢
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */

public class L6908 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public AcDetailService sAcDetailService;
	@Autowired
	public CdAcCodeService sCdAcCodeService;
	@Autowired
	public TxTranCodeService sTxTranCodeService;
	@Autowired
	public AcReceivableService sAcReceivableService;
	@Autowired
	public LoanBorTxService sLoanBorTxService;
	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6908 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		String iAcBookCode = titaVo.getParam("AcBookCode");
		String iAcSubBookCode = titaVo.getParam("AcSubBookCode");
		String iBranchNo = titaVo.getParam("BranchNo");
		String iCurrencyCode = titaVo.getParam("CurrencyCode");
		String iAcNoCode = titaVo.getParam("AcNoCode").trim();
		String iAcSubCode = titaVo.getParam("AcSubCode").trim();
		String iAcDtlCode = titaVo.getParam("AcDtlCode").trim();
		String iAcctCode = titaVo.getParam("AcctCode").trim();
		int iAcDateSt = this.parse.stringToInteger(titaVo.getParam("AcDateSt"));
		int iFAcDateSt = iAcDateSt + 19110000;
		int iAcDateEd = this.parse.stringToInteger(titaVo.getParam("AcDateEd"));
		int iFAcDateEd = iAcDateEd + 19110000;

		String iTranItem = "";

		int iCustNo = this.parse.stringToInteger(titaVo.getParam("CustNo"));
		int iFacmNo = this.parse.stringToInteger(titaVo.getParam("FacmNo"));
		// int iBormNo = this.parse.stringToInteger(titaVo.getParam("BormNo"));
		String iRvNo = titaVo.getParam("RvNo").trim();
		if (iRvNo.isEmpty()) {
			iRvNo = " ";
		}

		if (iAcSubCode.isEmpty()) {
			iAcSubCode = "     ";
		}
		if (iAcDtlCode.isEmpty()) {
			iAcDtlCode = "  ";
		}

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = Integer.MAX_VALUE; // 316 * 100 = 31,600

		// 查詢會計帳務明細檔
		Slice<AcDetail> slAcDetail = sAcDetailService.findL6908(iAcBookCode, iAcSubBookCode, iBranchNo, iCurrencyCode,
				iAcNoCode, iAcSubCode, iAcDtlCode, iCustNo, iFacmNo, iFAcDateSt, iFAcDateEd, this.index, this.limit,
				titaVo);

// AcReceivable RvAmt=100, Rvbal=80,  rvCls  = 80      
// ACDTL        起帳             TxAmt=100, rvCls  = -20
// ACDTL        銷帳             TxAmt=20,  rvCls  = 0

// AcReceivable RvAmt=100, Rvbal=80,  rvCls  = 80      
// ACDTL        銷帳             TxAmt=20,  rvCls  = 100
// 補差額             起帳             TxAmt=100, rvCls  = 0

// AcReceivable RvAmt=100, Rvbal=130, rvCls  = 130      
// ACDTL        銷帳             TxAmt=20,  rvCls  = 150
// 補差額             起帳             TxAmt=150, rvCls  = 0

		BigDecimal rvCls = BigDecimal.ZERO;
		BigDecimal rvAmt = BigDecimal.ZERO;
		int clsFlag = 0;

		AcReceivable tAcReceivable = sAcReceivableService
				.findById(new AcReceivableId(iAcctCode, iCustNo, iFacmNo, iRvNo), titaVo);
		if (tAcReceivable != null) {
			rvCls = tAcReceivable.getRvBal();
		}
		CdAcCode tCdAcCode = sCdAcCodeService.acCodeAcctFirst(iAcctCode, titaVo);
		if (tCdAcCode == null) {
			throw new LogicException(titaVo, "E0001", "會計科子細目設定檔"); // 查無資料
		}
		if (slAcDetail != null) {
			for (AcDetail tAcDetail : slAcDetail.getContent()) {
				if (tAcDetail.getEntAc() != 1) {
					continue;
				}
				if (tAcDetail.getReceivableFlag() == 0) {
					continue;
				}
				// 銷帳編號有輸入時只查詢銷帳編號的資料，業務科目記號=1.資負明細科目（放款、催收款項..)時撥款序號為銷帳編號
				if (tAcDetail.getAcctFlag() == 1) {
					if (!(iRvNo.isEmpty()) && !(iRvNo.equals(parse.IntegerToString(tAcDetail.getBormNo(), 3)))) {
						continue;
					}
				} else {
					if (!(iRvNo.isEmpty()) && !(iRvNo.equals(tAcDetail.getRvNo()))) {
						continue;
					}
				}
				if (tAcDetail.getReceivableFlag() >= 3) {
					rvCls = rvCls.add(tAcDetail.getTxAmt());
				} else {
					if (tAcDetail.getDbCr().equals(tCdAcCode.getDbCr())) {
						rvCls = rvCls.subtract(tAcDetail.getTxAmt());
					} else {
						rvCls = rvCls.add(tAcDetail.getTxAmt());
					}
				}
			}
		}
		// 補差額
		if (rvCls.compareTo(BigDecimal.ZERO) > 0)

		{
			clsFlag = 0;
			rvAmt = rvCls;
		} else {
			clsFlag = 1;
			rvAmt = BigDecimal.ZERO.subtract(rvCls);
		}
		this.info("rvAmt=" + rvAmt + ", rvCls=" + rvCls);

		List<OccursList> tOccursList = new ArrayList<OccursList>();

		if (tAcReceivable != null) {
			if (rvAmt.compareTo(BigDecimal.ZERO) != 0) {
				OccursList occursList = new OccursList();
				occursList.putParam("OORvNo", iRvNo);
				occursList.putParam("OORvAmt", rvAmt);
				occursList.putParam("OOTitaTlrNo", tAcReceivable.getOpenTlrNo());
				occursList.putParam("OOTitaTxtNo", tAcReceivable.getOpenTxtNo());
				iTranItem = "";
				iTranItem = inqTxTranCode(tAcReceivable.getOpenTxCd(), iTranItem, titaVo);
				occursList.putParam("OOTranItem", iTranItem);
				occursList.putParam("OOTitaTxCd", tAcReceivable.getOpenTxCd());
				occursList.putParam("OOSlipNote", "");
				occursList.putParam("OOAcDate", tAcReceivable.getOpenAcDate());
				occursList.putParam("OOClsFlag", clsFlag);

				int entryDate = 0;
				LoanBorTx tLoanBorTx = sLoanBorTxService.borxTxtNoFirst(tAcReceivable.getOpenAcDate() + 19110000,
						tAcReceivable.getTitaTlrNo(), parse.IntegerToString(tAcReceivable.getTitaTxtNo(), 8), titaVo);
				if (tLoanBorTx != null) {
					entryDate = tLoanBorTx.getEntryDate();
				}
				occursList.putParam("OOEntryDate", entryDate);

				occursList.putParam("OOCreateDate",
						parse.timeStampToStringDate(tAcReceivable.getCreateDate()).replace("/", ""));
				occursList.putParam("OOCreateTime", parse.timeStampToStringTime(tAcReceivable.getCreateDate()));

//				this.totaVo.addOccursList(occursList);
				tOccursList.add(occursList);
			}
		}
		// 如有找到資料
		if (slAcDetail != null) {
			for (AcDetail tAcDetail : slAcDetail.getContent()) {

				this.info("AcDetail : " + tAcDetail.toString());
				// 不含未入帳,例如:未放行之交易
				// 0:未入帳 1:已入帳 2:被沖正(隔日訂正) 3.沖正(隔日訂正)
				if (tAcDetail.getEntAc() != 1) {
					continue;
				}
				// 銷帳編號有輸入時只查詢銷帳編號的資料，業務科目記號=1.資負明細科目（放款、催收款項..)時撥款序號為銷帳編號
				if (tAcDetail.getAcctFlag() == 1) {
					if (!(iRvNo.isEmpty()) && !(iRvNo.equals(parse.IntegerToString(tAcDetail.getBormNo(), 3)))) {
						continue;
					}
				} else {
					if (!(iRvNo.isEmpty()) && !(iRvNo.equals(tAcDetail.getRvNo()))) {
						continue;
					}
				}

				OccursList occursList = new OccursList();
				occursList.putParam("OORvNo", tAcDetail.getRvNo());
				occursList.putParam("OORvAmt", tAcDetail.getTxAmt());
				occursList.putParam("OOTitaTlrNo", tAcDetail.getTitaTlrNo());
				occursList.putParam("OOTitaTxtNo", tAcDetail.getTitaTxtNo());
				iTranItem = "";
				iTranItem = inqTxTranCode(tAcDetail.getTitaTxCd(), iTranItem, titaVo);
				occursList.putParam("OOTranItem", iTranItem);
				occursList.putParam("OOTitaTxCd", tAcDetail.getTitaTxCd());
				occursList.putParam("OOSlipNote", tAcDetail.getSlipNote());
				occursList.putParam("OOAcDate", tAcDetail.getAcDate());
				int entryDate = 0;
				LoanBorTx tLoanBorTx = sLoanBorTxService.borxTxtNoFirst(tAcDetail.getAcDate() + 19110000,
						tAcDetail.getTitaTlrNo(), parse.IntegerToString(tAcDetail.getTitaTxtNo(), 8), titaVo);
				if (tLoanBorTx != null) {
					entryDate = tLoanBorTx.getEntryDate();
				}
				occursList.putParam("OOEntryDate", entryDate);
				occursList.putParam("OOCreateDate",
						parse.timeStampToStringDate(tAcDetail.getCreateDate()).replace("/", ""));
				occursList.putParam("OOCreateTime", parse.timeStampToStringTime(tAcDetail.getCreateDate()));

				if (tAcDetail.getReceivableFlag() >= 3) {
					clsFlag = 1;
				} else {
					if (tAcDetail.getDbCr().equals(tCdAcCode.getDbCr())) {
						clsFlag = 0;
					} else {
						clsFlag = 1;
					}
				}
				occursList.putParam("OOClsFlag", clsFlag);

				/* 將每筆資料放入Tota的OcList */
//				this.totaVo.addOccursList(occursList);
				tOccursList.add(occursList);
			}
		}

		// 輸出排序時間
		tOccursList.sort((c1, c2) -> {
			int result = 0;
			this.info("c1 = " + c1);
			this.info("c2 = " + c2);
			if (c1.get("OOCreateDate").compareTo(c2.get("OOCreateDate")) != 0) {
				result = c1.get("OOCreateDate").compareTo(c2.get("OOCreateDate"));
			} if (c1.get("OOCreateTime").compareTo(c2.get("OOCreateTime")) != 0) {
				result = c1.get("OOCreateTime").compareTo(c2.get("OOCreateTime"));
			} else {
				result = 0;
			}
			return result;
		});

		for (LinkedHashMap<String, String> t : tOccursList) {
			this.totaVo.addOccursList(t);
		}

		if (this.totaVo.getOccursList().size() == 0) {
			throw new LogicException(titaVo, "E0001", "會計帳務明細檔"); // 查無資料
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	// 查詢交易控制檔
	private String inqTxTranCode(String uTranNo, String uTranItem, TitaVo titaVo) throws LogicException {
		uTranItem = "";
		if (uTranNo != null && !uTranNo.isEmpty()) {
			TxTranCode tTxTranCode = sTxTranCodeService.findById(uTranNo, titaVo);
			if (tTxTranCode != null) {
				uTranItem = tTxTranCode.getTranItem();
			}
		}
		return uTranItem;

	}
}