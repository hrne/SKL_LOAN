package com.st1.itx.trade.L6;

import java.util.ArrayList;
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
import com.st1.itx.db.domain.CdAcCode;
import com.st1.itx.db.domain.CdAcCodeId;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.service.AcDetailService;
import com.st1.itx.db.service.CdAcCodeService;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.TxTellerService;
import com.st1.itx.db.domain.TxTranCode;
import com.st1.itx.db.service.TxTranCodeService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita AcBookCode=X,3 BranchNo=9,4 CurrencyCode=X,3 AcNoCode=X,8 AcSubCode=X,5
 * AcDtlCode=X,2 AcDate=9,7 InqType=9,1 InqData=X,6 DbCr=9,1 END=X,1
 */

@Service("L6905") // 日結明細查詢
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */

public class L6905 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public AcDetailService sAcDetailService;
	@Autowired
	public CdAcCodeService sCdAcCodeService;
	@Autowired
	public TxTellerService sTxTellerService;
	@Autowired
	public TxTranCodeService sTxTranCodeService;
	@Autowired
	CdEmpService cdEmpService;
	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6905 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		String DateTime; // YYY/MM/DD hh:mm:ss
		String Date = "";
		String Time = "";
		String iAcBookCode = titaVo.getParam("AcBookCode");
		String iAcSubBookCode = titaVo.getParam("AcSubBookCode");
		String iBranchNo = titaVo.getParam("BranchNo");
		String iCurrencyCode = titaVo.getParam("CurrencyCode");
		String iAcNoCodeS = titaVo.getParam("AcNoCode").trim();
		String iAcNoCodeE = "";
		String iAcSubCode = titaVo.getParam("AcSubCode").trim();
		String iAcDtlCode = titaVo.getParam("AcDtlCode").trim();
		String iRvNo = titaVo.getParam("RvNo").trim();
		int iAcDate = this.parse.stringToInteger(titaVo.getParam("AcDate"));
		int iFAcDate = iAcDate + 19110000;
		String iTlrItem = "";
		String iTranItem = "";

		String iDbCr = titaVo.getParam("DbCr");
		String iSumNo = titaVo.getParam("InqData");
		iSumNo = FormatUtil.padX(iSumNo, 3);
		String iTitaTlrNo = titaVo.getParam("InqData");
		String iTitaBatchNo = titaVo.getParam("InqData");
		String iDscptCode = titaVo.getParam("InqData");
		iDscptCode = FormatUtil.padX(iDscptCode, 4);
		String iTitaSecNo = titaVo.getParam("InqData");
		iTitaSecNo = FormatUtil.padX(iTitaSecNo, 2);
		int iInqType = this.parse.stringToInteger(titaVo.getParam("InqType"));

		int iSlipBatNo = 0;
		if (iInqType == 5) {
			iSlipBatNo = this.parse.stringToInteger(titaVo.getParam("InqData"));
		}

		if (iAcNoCodeS.isEmpty()) {
			iAcNoCodeS = "           ";
			iAcNoCodeE = "ZZZZZZZZZZZ";
		} else {
			iAcNoCodeE = titaVo.getParam("AcNoCode").trim();
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
		this.limit = 50; // 482 * 100 = 48,200

		// 查詢會計帳務明細檔
		Slice<AcDetail> slAcDetail = null;
		switch (iInqType) {
		case 0: // 全部彙計方式
			if (iRvNo.trim().isEmpty()) {
				slAcDetail = sAcDetailService.SubBookAcNoCodeRange(iAcBookCode, iAcSubBookCode.trim() + "%", iBranchNo, iCurrencyCode, iFAcDate, iAcNoCodeS, iAcNoCodeE, this.index, this.limit,
						titaVo);
			} else {
				slAcDetail = sAcDetailService.SubBookAcNoCodeRange1(iAcBookCode, iAcSubBookCode.trim() + "%", iBranchNo, iCurrencyCode, iFAcDate, iAcNoCodeS, iAcNoCodeE, iRvNo + "%", this.index,
						this.limit, titaVo);
			}

			break;
		case 1: // 彙總別
			if (iRvNo.trim().isEmpty()) {
				slAcDetail = sAcDetailService.SubBookSumNoRange(iAcBookCode, iAcSubBookCode.trim() + "%", iBranchNo, iCurrencyCode, iFAcDate, iAcNoCodeS, iAcNoCodeE, iSumNo, iSumNo, this.index,
						this.limit, titaVo);
			} else {
				slAcDetail = sAcDetailService.SubBookSumNoRange1(iAcBookCode, iAcSubBookCode.trim() + "%", iBranchNo, iCurrencyCode, iFAcDate, iAcNoCodeS, iAcNoCodeE, iSumNo, iSumNo, iRvNo + "%",
						this.index, this.limit, titaVo);
			}
			break;
		case 2: // 經辦別
			if (iRvNo.trim().isEmpty()) {
				slAcDetail = sAcDetailService.SubBookTitaTlrNoRange(iAcBookCode, iAcSubBookCode.trim() + "%", iBranchNo, iCurrencyCode, iFAcDate, iAcNoCodeS, iAcNoCodeE, iTitaTlrNo, iTitaTlrNo,
						this.index, this.limit, titaVo);
			} else {
				slAcDetail = sAcDetailService.SubBookTitaTlrNoRange1(iAcBookCode, iAcSubBookCode.trim() + "%", iBranchNo, iCurrencyCode, iFAcDate, iAcNoCodeS, iAcNoCodeE, iTitaTlrNo, iTitaTlrNo,
						iRvNo + "%", this.index, this.limit, titaVo);
			}

			break;
		case 3: // 整批批號
			if (iRvNo.trim().isEmpty()) {
				slAcDetail = sAcDetailService.SubBookTitaBatchNoRange(iAcBookCode, iAcSubBookCode.trim() + "%", iBranchNo, iCurrencyCode, iFAcDate, iAcNoCodeS, iAcNoCodeE, iTitaBatchNo, iTitaBatchNo,
						this.index, this.limit, titaVo);
			} else {
				slAcDetail = sAcDetailService.SubBookTitaBatchNoRange1(iAcBookCode, iAcSubBookCode.trim() + "%", iBranchNo, iCurrencyCode, iFAcDate, iAcNoCodeS, iAcNoCodeE, iTitaBatchNo, iTitaBatchNo,
						iRvNo + "%", this.index, this.limit, titaVo);
			}

			break;
		case 4: // 摘要代號
			if (iRvNo.trim().isEmpty()) {
				slAcDetail = sAcDetailService.SubBookDscptCodeRange(iAcBookCode, iAcSubBookCode.trim() + "%", iBranchNo, iCurrencyCode, iFAcDate, iAcNoCodeS, iAcNoCodeE, iDscptCode, iDscptCode,
						this.index, this.limit, titaVo);
			} else {
				slAcDetail = sAcDetailService.SubBookDscptCodeRange1(iAcBookCode, iAcSubBookCode.trim() + "%", iBranchNo, iCurrencyCode, iFAcDate, iAcNoCodeS, iAcNoCodeE, iDscptCode, iDscptCode,
						iRvNo + "%", this.index, this.limit, titaVo);
			}

			break;
		case 5: // 傳票批號
			if (iRvNo.trim().isEmpty()) {
				slAcDetail = sAcDetailService.SubBookSlipBatNoRange(iAcBookCode, iAcSubBookCode.trim() + "%", iBranchNo, iCurrencyCode, iFAcDate, iAcNoCodeS, iAcNoCodeE, iSlipBatNo, iSlipBatNo,
						this.index, this.limit, titaVo);
			} else {
				slAcDetail = sAcDetailService.SubBookSlipBatNoRange1(iAcBookCode, iAcSubBookCode.trim() + "%", iBranchNo, iCurrencyCode, iFAcDate, iAcNoCodeS, iAcNoCodeE, iSlipBatNo, iSlipBatNo,
						iRvNo + "%", this.index, this.limit, titaVo);
			}

			break;
		case 6: // 業務類別
			if (iRvNo.trim().isEmpty()) {
				slAcDetail = sAcDetailService.SubBookTitaSecNoRange(iAcBookCode, iAcSubBookCode.trim() + "%", iBranchNo, iCurrencyCode, iFAcDate, iAcNoCodeS, iAcNoCodeE, iTitaSecNo, iTitaSecNo,
						this.index, this.limit, titaVo);
			} else {
				slAcDetail = sAcDetailService.SubBookTitaSecNoRange1(iAcBookCode, iAcSubBookCode.trim() + "%", iBranchNo, iCurrencyCode, iFAcDate, iAcNoCodeS, iAcNoCodeE, iTitaSecNo, iTitaSecNo,
						iRvNo + "%", this.index, this.limit, titaVo);
			}

			break;
		}
		List<AcDetail> lAcDetail = slAcDetail == null ? null : slAcDetail.getContent();

		if (lAcDetail == null || lAcDetail.size() == 0) {
			throw new LogicException(titaVo, "E0001", "會計帳務明細檔"); // 查無資料
		}
		// 如有找到資料
		for (AcDetail tAcDetail : lAcDetail) {

			this.info("L6905 AcNoCode : " + iAcBookCode + "-" + iAcNoCodeS + "-" + iAcSubCode + "-" + iAcDtlCode + "-" + tAcDetail.getAcNoCode() + "-" + tAcDetail.getAcSubCode() + "-"
					+ tAcDetail.getAcDtlCode() + "-" + tAcDetail.getTxAmt() + "-" + tAcDetail.getAcBookFlag() + "-" + tAcDetail.getAcBookCode() + "-" + tAcDetail.getEntAc());

			// 不含未入帳,例如:未放行之交易
			// 0:未入帳 1:已入帳 2:被沖正(隔日訂正) 3.沖正(隔日訂正)
			if (tAcDetail.getEntAc() == 0) {
				this.info("L6905 0 ");
				continue;
			}

			// 科子細目可不輸入 ; 有輸入不等時找下一筆
			// if (!(iAcNoCodeS.isEmpty() || iAcNoCodeS.equals(tAcDetail.getAcNoCode()))) {
			// this.info("L6905 1 ");
			// continue;
			// }

			if (!(titaVo.getParam("AcNoCode").trim().isEmpty())) {
				if (!(iAcSubCode.equals(tAcDetail.getAcSubCode()))) {
					this.info("L6905 2 ");
					continue;
				} else if (!(iAcDtlCode.equals(tAcDetail.getAcDtlCode()))) {
					this.info("L6905 3 ");
					continue;
				}
			}

			// 0:全部;1:借;2:貸 -> 不等時找下一筆
			if ((iDbCr.equals("1") && !(tAcDetail.getDbCr().equals("D"))) || (iDbCr.equals("2") && !(tAcDetail.getDbCr().equals("C")))) {
				this.info("L6905 DC ");
				continue;
			}

			// 0: 不細分 (000)
			// 1: 兼全帳冊與特殊帳冊 (輸入000,輸出含000,201 ; 輸入201,只輸出201)
			// 2: 特殊帳冊之應收調撥款，明細檔無(只寫入總帳檔)
			// 3: 特殊帳冊(L6201:其他傳票輸入) (輸入000,只輸出000 ; 輸入201,只輸出201)

//			if (tAcDetail.getAcBookFlag() == 0) {
//				if (!(iAcBookCode.equals("000") || iAcBookCode.equals("10H"))) {
//					this.info("L6905 4 ");
//					continue;
//				}
//			} else if (tAcDetail.getAcBookFlag() == 1) {
//				if (!(iAcBookCode.equals("000") || iAcBookCode.equals("10H")
//						|| iAcBookCode.equals(tAcDetail.getAcBookCode()))) {
//					this.info("L6905 5 ");
//					continue;
//				}
//			} else if (tAcDetail.getAcBookFlag() == 3) {
//				if (!(iAcBookCode.equals(tAcDetail.getAcBookCode()) || iAcBookCode.equals("10H"))) {
//					this.info("L6905 6 ");
//					continue;
//				}
//			} else {
//				this.info("L6905 7 ");
//				continue;
//			}

			OccursList occursList = new OccursList();
			occursList.putParam("OOAcNoCode", tAcDetail.getAcNoCode());
			occursList.putParam("OOAcSubCode", tAcDetail.getAcSubCode());
			occursList.putParam("OOAcDtlCode", tAcDetail.getAcDtlCode());
			occursList.putParam("OOCustNo", tAcDetail.getCustNo());
			occursList.putParam("OOFacmNo", tAcDetail.getFacmNo());
			occursList.putParam("OOBormNo", tAcDetail.getBormNo());
			occursList.putParam("OORelDy", tAcDetail.getRelDy());
			occursList.putParam("OOSlipNote", tAcDetail.getSlipNote());
			iTranItem = "";
			iTranItem = inqTxTranCode(tAcDetail.getTitaTxCd(), iTranItem, titaVo);
			occursList.putParam("OOTranItem", iTranItem);
			occursList.putParam("OOTitaTxCd", tAcDetail.getTitaTxCd());
			occursList.putParam("OOSumNo", tAcDetail.getSumNo());

			iTlrItem = "";
			iTlrItem = inqCdEmp(tAcDetail.getTitaSupNo(), iTlrItem, titaVo);
			occursList.putParam("OOSupItem", iTlrItem);
			occursList.putParam("OOTitaSupNo", tAcDetail.getTitaSupNo());

			iTlrItem = "";
			iTlrItem = inqCdEmp(tAcDetail.getTitaTlrNo(), iTlrItem, titaVo);
			occursList.putParam("OOTlrItem", iTlrItem);
			occursList.putParam("OOTitaTlrNo", tAcDetail.getTitaTlrNo());

			occursList.putParam("OORelTxseq", tAcDetail.getRelTxseq());

			if (tAcDetail.getDbCr().equals("D")) {
				occursList.putParam("OODbAmt", tAcDetail.getTxAmt());
				occursList.putParam("OOCrAmt", 0);
			} else {
				occursList.putParam("OODbAmt", 0);
				occursList.putParam("OOCrAmt", tAcDetail.getTxAmt());
			}

			DateTime = this.parse.timeStampToString(tAcDetail.getLastUpdate());
			this.info("L6905 DateTime : " + DateTime);
			Date = FormatUtil.left(DateTime, 9);
			occursList.putParam("OOLastDate", Date);
			Time = FormatUtil.right(DateTime, 8);
			occursList.putParam("OOLastTime", Time);

			// 查詢會計科子細目設定檔
			CdAcCode tCdAcCode = sCdAcCodeService.findById(new CdAcCodeId(tAcDetail.getAcNoCode(), tAcDetail.getAcSubCode(), tAcDetail.getAcDtlCode()), titaVo);
			if (tCdAcCode == null) {
				occursList.putParam("OOAcNoItem", "");
			} else {
				occursList.putParam("OOAcNoItem", tCdAcCode.getAcNoItem());
			}

			/* 將每筆資料放入Tota的OcList */
			this.totaVo.addOccursList(occursList);
		}

		if (this.totaVo.getOccursList().size() == 0) {
			throw new LogicException(titaVo, "E0001", "會計帳務明細檔"); // 查無資料
		}

		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (slAcDetail != null && slAcDetail.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			// this.totaVo.setMsgEndToEnter();// 手動折返
			this.totaVo.setMsgEndToAuto();// 自動折返
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

	// 查詢使用者設定檔
	private String inqCdEmp(String uTlrNo, String uTlrItem, TitaVo titaVo) throws LogicException {

		CdEmp tCdEmp = new CdEmp();

		tCdEmp = cdEmpService.findById(uTlrNo, titaVo);

		if (tCdEmp == null) {
			uTlrItem = uTlrNo;
		} else {
			uTlrItem = tCdEmp.getFullname();
		}

		return uTlrItem;

	}
}