package com.st1.itx.trade.L6;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
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
import com.st1.itx.db.domain.AcMain;
import com.st1.itx.db.domain.AcMainId;
import com.st1.itx.db.domain.CdAcCode;
import com.st1.itx.db.domain.CdAcCodeId;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.service.AcDetailService;
import com.st1.itx.db.service.AcMainService;
import com.st1.itx.db.service.CdAcCodeService;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.domain.TxTranCode;
import com.st1.itx.db.service.TxTranCodeService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/**
 * Tita AcBookCode=X,3 BranchNo=X,4 CurrencyCode=X,3 AcNoCode=X,8 AcSubCode=X,5
 * AcDtlCode=X,2 AcDateSt=9,7 AcDateEd=9,7 END=X,1
 */

@Service("L6903") // 會計帳務明細查詢
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */

public class L6903 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public AcDetailService sAcDetailService;
	@Autowired
	public AcMainService sAcMainService;
	@Autowired
	public TxTranCodeService sTxTranCodeService;
	@Autowired
	public CdAcCodeService sCdAcCodeService;
	@Autowired
	public CdEmpService cdEmpService;
	@Autowired
	Parse parse;
	private String debits[] = { "1", "5", "6", "9" }; // 資產、支出為借方科目，負債、收入為貸方科目
	private List<String> debitsList = Arrays.asList(debits);

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6903 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		String iAcBookCode = titaVo.getParam("AcBookCode");
		String iAcSubBookCode = titaVo.getParam("AcSubBookCode");
		String iBranchNo = titaVo.getParam("BranchNo");
		String iCurrencyCode = titaVo.getParam("CurrencyCode");
		String iAcNoCode = titaVo.getParam("AcNoCode").trim();
		String iAcSubCode = titaVo.getParam("AcSubCode").trim();
		String iAcDtlCode = titaVo.getParam("AcDtlCode").trim();
		int iAcDateSt = this.parse.stringToInteger(titaVo.getParam("AcDateSt"));
		int iFAcDateSt = iAcDateSt + 19110000;
		int iAcDateEd = this.parse.stringToInteger(titaVo.getParam("AcDateEd"));
		int iFAcDateEd = iAcDateEd + 19110000;
		BigDecimal wkBal = new BigDecimal(0);
		BigDecimal wkDb = new BigDecimal(0);
		BigDecimal wkCr = new BigDecimal(0);
		String iTranItem = "";
		int classcode = 0;
		if (iAcSubCode.isEmpty()) {
			iAcSubCode = "     ";
		}
		if (iAcDtlCode.isEmpty()) {
			iAcDtlCode = "  ";
		}

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 100;

		// 查詢會計科子細目設定檔
		if (!(iAcNoCode.isEmpty())) {
			CdAcCode tCdAcCode = sCdAcCodeService.findById(new CdAcCodeId(iAcNoCode, iAcSubCode, iAcDtlCode), titaVo);
			if (tCdAcCode == null) {
				throw new LogicException(titaVo, "E0001", "會計科子細目設定檔"); // 查無資料
			}
			classcode = tCdAcCode.getClassCode(); // 1:下編子細目
		}

		// 查詢會計總帳檔
		Slice<AcMain> slAcMain;
		if (classcode == 1) {
			slAcMain = sAcMainService.acmainAcBookCodeRange2(iAcBookCode, iAcSubBookCode.trim() + "%", iBranchNo,
					iCurrencyCode, iAcNoCode, iAcSubCode, iFAcDateSt, iFAcDateEd, 0, Integer.MAX_VALUE, titaVo);
		} else {
			slAcMain = sAcMainService.acmainAcBookCodeRange(iAcBookCode, iAcSubBookCode.trim() + "%", iBranchNo,
					iCurrencyCode, iAcNoCode, iAcSubCode, iAcDtlCode, iFAcDateSt, iFAcDateEd, 0, Integer.MAX_VALUE,
					titaVo);
		}

		if (slAcMain != null) {
			for (AcMain tAcMain : slAcMain.getContent()) {
				wkBal = wkBal.add(tAcMain.getYdBal());
				wkDb = wkDb.add(tAcMain.getDbAmt());
				wkCr = wkCr.add(tAcMain.getCrAmt());
			}
		}

		// 查詢會計帳務明細檔
		Slice<AcDetail> slAcDetail;
		if (classcode == 1) {
			slAcDetail = sAcDetailService.acdtlAcDateRange2(iAcBookCode, iAcSubBookCode + "%", iBranchNo, iCurrencyCode,
					iAcNoCode, iAcSubCode, iFAcDateSt, iFAcDateEd, this.index, this.limit, titaVo);
		} else {
			slAcDetail = sAcDetailService.acdtlAcDateRange(iAcBookCode, iAcSubBookCode + "%", iBranchNo, iCurrencyCode,
					iAcNoCode, iAcSubCode, iAcDtlCode, iFAcDateSt, iFAcDateEd, this.index, this.limit, titaVo);
		}

		List<AcDetail> lAcDetail = slAcDetail == null ? null : slAcDetail.getContent();

		if (lAcDetail == null || lAcDetail.size() == 0) {
			throw new LogicException(titaVo, "E0001", "會計帳務明細檔"); // 查無資料
		}
		// 如有找到資料
		for (AcDetail tAcDetail : lAcDetail) {
			// 不含未入帳,例如:未放行之交易
			if (tAcDetail.getEntAc() == 0) {
				continue;
			}

			OccursList occursList = new OccursList();
			occursList.putParam("OOAcDate", tAcDetail.getAcDate());
			occursList.putParam("OORelDy", tAcDetail.getRelDy());
			occursList.putParam("OORelTxseq", tAcDetail.getRelTxseq());
			iTranItem = "";
			iTranItem = inqTxTranCode(tAcDetail.getTitaTxCd(), iTranItem, titaVo);
			occursList.putParam("OOTranItem", iTranItem);
			occursList.putParam("OOTitaTxCd", tAcDetail.getTitaTxCd());
			String custNo = "";
			if (tAcDetail.getCustNo() > 0) {
				custNo = parse.IntegerToString(tAcDetail.getCustNo(), 7);
			}
			if (tAcDetail.getFacmNo() > 0) {
				custNo += "-" + parse.IntegerToString(tAcDetail.getFacmNo(), 3);
			}
			if (tAcDetail.getBormNo() > 0) {
				custNo += "-" + parse.IntegerToString(tAcDetail.getBormNo(), 3);
			}
			occursList.putParam("OOCustNo", custNo);
			occursList.putParam("OODbCr", tAcDetail.getDbCr());
			occursList.putParam("OOTxAmt", tAcDetail.getTxAmt());
			occursList.putParam("OOSlipNote", tAcDetail.getSlipNote());
			occursList.putParam("OOLastUpdate", parse.timeStampToStringDate(tAcDetail.getLastUpdate()) + " "
					+ parse.timeStampToStringTime(tAcDetail.getLastUpdate()));
			occursList.putParam("OOLastEmp",
					tAcDetail.getLastUpdateEmpNo() + " " + empName(titaVo, tAcDetail.getLastUpdateEmpNo()));

			// 餘額
			// 借方科目{ "1", "5","6","9" }資產
			// 借方科目本日餘額 = 昨日餘額 + 借方金額 - 貸方金額
			// 貸方科目本日餘額 = 昨日餘額 + 貸方金額 - 借方金額
			if (debitsList.contains(iAcNoCode.substring(0, 1))) {
				if (tAcDetail.getDbCr().equals("D")) {
					wkBal = wkBal.add(tAcDetail.getTxAmt());
				} else {
					wkBal = wkBal.subtract(tAcDetail.getTxAmt());
				}
			} else {
				if (tAcDetail.getDbCr().equals("D")) {
					wkBal = wkBal.subtract(tAcDetail.getTxAmt());
				} else {
					wkBal = wkBal.add(tAcDetail.getTxAmt());
				}
			}

			occursList.putParam("OOTdBal", wkBal);

			/* 將每筆資料放入Tota的OcList */
			this.totaVo.addOccursList(occursList);
		}

		this.totaVo.putParam("OODbAmt", wkDb);
		this.totaVo.putParam("OOCrAmt", wkCr);

		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (slAcDetail != null && slAcDetail.hasNext()) {
			this.info("lAcDetail hasNext");
			titaVo.setReturnIndex(this.setIndexNext());
			this.totaVo.setMsgEndToEnter();// 手動折返
	//		this.totaVo.setMsgEndToAuto();// 自動折返
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	// 查詢交易控制檔
	private String inqTxTranCode(String uTranNo, String uTranItem, TitaVo titaVo) throws LogicException {

		TxTranCode tTxTranCode = new TxTranCode();

		tTxTranCode = sTxTranCodeService.findById(uTranNo, titaVo);

		if (tTxTranCode == null) {
			uTranItem = "";
		} else {
			uTranItem = tTxTranCode.getTranItem();
		}

		return uTranItem;

	}

	private String empName(TitaVo titaVo, String empNo) throws LogicException {
		String rs = empNo;

		CdEmp cdEmp = cdEmpService.findById(empNo, titaVo);
		if (cdEmp != null) {
			rs = cdEmp.getFullname();
		}
		return rs;
	}
}