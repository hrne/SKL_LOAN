package com.st1.itx.trade.L6;

import java.math.BigDecimal;
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
import com.st1.itx.db.service.AcDetailService;
import com.st1.itx.db.service.CdAcCodeService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/**
 * Tita AcBookCode=X,3 BranchNo=9,4 CurrencyCode=X,3 AcNoCode=X,8 AcSubCode=X,5
 * AcDtlCode=X,2 AcDate=9,7 InqType=9,1 END=X,1
 */

@Service("L6904") // 日結彙計查詢
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */

public class L6904 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public AcDetailService sAcDetailService;
	@Autowired
	public CdAcCodeService sCdAcCodeService;
	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6904 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		String iAcBookCode = titaVo.getParam("AcBookCode");
		String iAcSubBookCode = titaVo.getParam("AcSubBookCode");
		String iBranchNo = titaVo.getParam("BranchNo");
		String iCurrencyCode = titaVo.getParam("CurrencyCode");
		String iAcNoCodeS = titaVo.getParam("AcNoCode").trim();
		String iAcNoCodeE = "";
		String iAcSubCode = titaVo.getParam("AcSubCode").trim();
		String iAcDtlCode = titaVo.getParam("AcDtlCode").trim();
		int iAcDate = this.parse.stringToInteger(titaVo.getParam("AcDate"));
		int iFAcDate = iAcDate + 19110000;
		int iInqType = this.parse.stringToInteger(titaVo.getParam("InqType"));

		String acNoCode = "";
		String acSubCode = "";
		String acDtlCode = "";
		String sumNo = "";
		String titaTlrNo = "";
		String titaBatchNo = "";
		String dscptCode = "";
		String slipNote = "";
		String titaSecNo = "";
		int slipBatNo = 0;

		int totalCnt = 0;
		int dbCnt = 0;
		int crCnt = 0;
		BigDecimal dbAmt = new BigDecimal(0);
		BigDecimal crAmt = new BigDecimal(0);

		this.index = titaVo.getReturnIndex();

		this.limit = Integer.MAX_VALUE;

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
		// this.limit = 200; // 157 * 200 = 31,400

		// 查詢會計帳務明細檔
		Slice<AcDetail> slAcDetail = null;
		switch (iInqType) {
		case 0: // 全部彙計方式
			slAcDetail = sAcDetailService.SubBookAcNoCodeRange(iAcBookCode, iAcSubBookCode.trim() + "%", iBranchNo, iCurrencyCode, iFAcDate, iAcNoCodeS, iAcNoCodeE, this.index, this.limit, titaVo);
			break;
		case 1: // 彙總別
			slAcDetail = sAcDetailService.SubBookSumNoRange(iAcBookCode, iAcSubBookCode.trim() + "%", iBranchNo, iCurrencyCode, iFAcDate, iAcNoCodeS, iAcNoCodeE, "   ", "ZZZ", this.index, this.limit,
					titaVo);
			break;
		case 2: // 經辦別
			slAcDetail = sAcDetailService.SubBookTitaTlrNoRange(iAcBookCode, iAcSubBookCode.trim() + "%", iBranchNo, iCurrencyCode, iFAcDate, iAcNoCodeS, iAcNoCodeE, "      ", "ZZZZZZ", this.index,
					this.limit, titaVo);
			break;
		case 3: // 整批批號
			slAcDetail = sAcDetailService.SubBookTitaBatchNoRange(iAcBookCode, iAcSubBookCode.trim() + "%", iBranchNo, iCurrencyCode, iFAcDate, iAcNoCodeS, iAcNoCodeE, "      ", "ZZZZZZ", this.index,
					this.limit, titaVo);
			break;
		case 4: // 摘要代號
			slAcDetail = sAcDetailService.SubBookDscptCodeRange(iAcBookCode, iAcSubBookCode.trim() + "%", iBranchNo, iCurrencyCode, iFAcDate, iAcNoCodeS, iAcNoCodeE, "    ", "ZZZZ", this.index,
					this.limit, titaVo);
			break;
		case 5: // 傳票批號
			slAcDetail = sAcDetailService.SubBookSlipBatNoRange(iAcBookCode, iAcSubBookCode.trim() + "%", iBranchNo, iCurrencyCode, iFAcDate, iAcNoCodeS, iAcNoCodeE, 00, 99, this.index, this.limit,
					titaVo);
			break;
		case 6: // 業務類別
			slAcDetail = sAcDetailService.SubBookTitaSecNoRange(iAcBookCode, iAcSubBookCode.trim() + "%", iBranchNo, iCurrencyCode, iFAcDate, iAcNoCodeS, iAcNoCodeE, "  ", "ZZ", this.index,
					this.limit, titaVo);
			break;
		}
		List<AcDetail> lAcDetail = slAcDetail == null ? null : slAcDetail.getContent();

		if (lAcDetail == null || lAcDetail.size() == 0) {
			throw new LogicException(titaVo, "E0001", "會計帳務明細檔"); // 查無資料
		}
		String dAcSubBookCode = "";
		// 如有找到資料
		for (AcDetail tAcDetail : lAcDetail) {

			this.info("L6904 AcNoCode : " + iAcBookCode + "-" + iAcNoCodeS + "-" + iAcSubCode + "-" + iAcDtlCode + "-" + tAcDetail.getAcNoCode() + "-" + tAcDetail.getAcSubCode() + "-"
					+ tAcDetail.getAcDtlCode() + "-" + tAcDetail.getTxAmt() + "-" + tAcDetail.getAcBookFlag() + "-" + tAcDetail.getAcBookCode() + "-" + tAcDetail.getEntAc());

			// 不含未入帳,例如:未放行之交易
			// 0:未入帳 1:已入帳 2:被沖正(隔日訂正) 3.沖正(隔日訂正)
			if (tAcDetail.getEntAc() == 0) {
				this.info("L6904 0 ");
				continue;
			}

			// 科子細目可不輸入 ; 有輸入不等時找下一筆
			// if (!(iAcNoCodeS.isEmpty() || iAcNoCodeS.equals(tAcDetail.getAcNoCode()))) {
			// this.info("L6904 1 ");
			// continue;
			// }

			if (!(titaVo.getParam("AcNoCode").trim().isEmpty())) {
				if (!(iAcSubCode.equals(tAcDetail.getAcSubCode()))) {
					this.info("L6904 2 ");
					continue;
				} else if (!(iAcDtlCode.equals(tAcDetail.getAcDtlCode()))) {
					this.info("L6904 3 ");
					continue;
				}
			}

			// 0: 不細分 (000)
			// 1: 兼全帳冊與特殊帳冊 (輸入000,輸出含000,201 ; 輸入201,只輸出201)
			// 2: 特殊帳冊之應收調撥款，明細檔無(只寫入總帳檔)
			// 3: 特殊帳冊(L6201:其他傳票輸入) (輸入000,只輸出000 ; 輸入201,只輸出201)

//			if (tAcDetail.getAcBookFlag() == 0) {
//				if (!(iAcBookCode.equals("000") || iAcBookCode.equals("10H"))) {
//					this.info("L6904 4 ");
//					continue;
//				}
//			} else if (tAcDetail.getAcBookFlag() == 1) {
//				if (!(iAcBookCode.equals("000") || iAcBookCode.equals("10H")
//						|| iAcBookCode.equals(tAcDetail.getAcBookCode()))) {
//					this.info("L6904 5 ");
//					continue;
//				}
//			} else if (tAcDetail.getAcBookFlag() == 3) {
//				if (!(iAcBookCode.equals(tAcDetail.getAcBookCode()) || iAcBookCode.equals("10H"))) {
//					this.info("L6904 6 ");
//					continue;
//				}
//			} else {
//				this.info("L6904 7 ");
//				continue;
//			}

			totalCnt = totalCnt + 1;
			// 第一筆小計
			if (totalCnt == 1) {
				acNoCode = tAcDetail.getAcNoCode();
				acSubCode = tAcDetail.getAcSubCode();
				acDtlCode = tAcDetail.getAcDtlCode();
				sumNo = tAcDetail.getSumNo();
				titaTlrNo = tAcDetail.getTitaTlrNo();
				titaBatchNo = tAcDetail.getTitaBatchNo();
				dscptCode = tAcDetail.getDscptCode();
				if (tAcDetail.getSlipNote() != null) {
					slipNote = tAcDetail.getSlipNote().trim();
				}
				slipBatNo = tAcDetail.getSlipBatNo();
				titaSecNo = tAcDetail.getTitaSecNo();
				if (tAcDetail.getDbCr().equals("D")) {
					dbAmt = dbAmt.add(tAcDetail.getTxAmt());
					dbCnt = dbCnt + 1;
				} else {
					crAmt = crAmt.add(tAcDetail.getTxAmt());
					crCnt = crCnt + 1;
				}
				continue;
			}

			// 條件一樣小計
			if (acNoCode.equals(tAcDetail.getAcNoCode()) && acSubCode.equals(tAcDetail.getAcSubCode()) && acDtlCode.equals(tAcDetail.getAcDtlCode())
					&& ((iInqType == 0) || (iInqType == 1 && sumNo.equals(tAcDetail.getSumNo())) || (iInqType == 2 && titaTlrNo.equals(tAcDetail.getTitaTlrNo()))
							|| (iInqType == 3 && titaBatchNo.equals(tAcDetail.getTitaBatchNo())) || (iInqType == 4 && dscptCode.equals(tAcDetail.getDscptCode()))
							|| (iInqType == 5 && slipBatNo == tAcDetail.getSlipBatNo()) || (iInqType == 6 && titaSecNo.equals(tAcDetail.getTitaSecNo())))) {
				if (tAcDetail.getDbCr().equals("D")) {
					dbAmt = dbAmt.add(tAcDetail.getTxAmt());
					dbCnt = dbCnt + 1;
				} else {
					crAmt = crAmt.add(tAcDetail.getTxAmt());
					crCnt = crCnt + 1;
				}
				continue;
			}
			dAcSubBookCode = tAcDetail.getAcSubBookCode();
			OccursList occursList = new OccursList();
			occursList.putParam("OOAcSubBookCode", dAcSubBookCode);
			occursList.putParam("OOAcNoCode", acNoCode);
			occursList.putParam("OOAcSubCode", acSubCode);
			occursList.putParam("OOAcDtlCode", acDtlCode);
			occursList.putParam("OODbCnt", dbCnt);
			occursList.putParam("OODbAmt", dbAmt);
			occursList.putParam("OOCrCnt", crCnt);
			occursList.putParam("OOCrAmt", crAmt);
			occursList.putParam("OOSlipNote", slipNote);
			switch (iInqType) {
			case 0: // 全部彙計方式
				occursList.putParam("OOInqData", "");
				break;
			case 1: // 彙總別
				occursList.putParam("OOInqData", sumNo);
				break;
			case 2: // 經辦別
				occursList.putParam("OOInqData", titaTlrNo);
				break;
			case 3: // 整批批號
				occursList.putParam("OOInqData", titaBatchNo);
				break;
			case 4: // 摘要代號
				occursList.putParam("OOInqData", dscptCode);
				break;
			case 5: // 傳票批號
				occursList.putParam("OOInqData", slipBatNo);
				break;
			case 6: // 業務類別
				occursList.putParam("OOInqData", titaSecNo);
				break;
			}

			// 查詢會計科子細目設定檔
			CdAcCode tCdAcCode = sCdAcCodeService.findById(new CdAcCodeId(acNoCode, acSubCode, acDtlCode), titaVo);
			if (tCdAcCode == null) {
				occursList.putParam("OOAcNoItem", "");
			} else {
				occursList.putParam("OOAcNoItem", tCdAcCode.getAcNoItem());
			}

			/* 將每筆資料放入Tota的OcList */
			this.totaVo.addOccursList(occursList);

			// 準備下一筆
			acNoCode = tAcDetail.getAcNoCode();
			acSubCode = tAcDetail.getAcSubCode();
			acDtlCode = tAcDetail.getAcDtlCode();
			sumNo = tAcDetail.getSumNo();
			titaTlrNo = tAcDetail.getTitaTlrNo();
			titaBatchNo = tAcDetail.getTitaBatchNo();
			dscptCode = tAcDetail.getDscptCode();
			if (tAcDetail.getSlipNote() != null) {
				slipNote = tAcDetail.getSlipNote().trim();
			}

			slipBatNo = tAcDetail.getSlipBatNo();
			titaSecNo = tAcDetail.getTitaSecNo();
			dbCnt = 0;
			crCnt = 0;
			dbAmt = new BigDecimal(0);
			crAmt = new BigDecimal(0);
			if (tAcDetail.getDbCr().equals("D")) {
				dbAmt = dbAmt.add(tAcDetail.getTxAmt());
				dbCnt = dbCnt + 1;
			} else {
				crAmt = crAmt.add(tAcDetail.getTxAmt());
				crCnt = crCnt + 1;
			}

		}

		// 最後一筆資料放入Tota的OcList
		if (!(dbAmt.compareTo(BigDecimal.ZERO) == 0 && crAmt.compareTo(BigDecimal.ZERO) == 0)) {

			OccursList occursList = new OccursList();
			occursList.putParam("OOAcSubBookCode", dAcSubBookCode);
			occursList.putParam("OOAcNoCode", acNoCode);
			occursList.putParam("OOAcSubCode", acSubCode);
			occursList.putParam("OOAcDtlCode", acDtlCode);
			occursList.putParam("OODbCnt", dbCnt);
			occursList.putParam("OODbAmt", dbAmt);
			occursList.putParam("OOCrCnt", crCnt);
			occursList.putParam("OOCrAmt", crAmt);
			occursList.putParam("OOSlipNote", slipNote);
			switch (iInqType) {
			case 0: // 全部彙計方式
				occursList.putParam("OOInqData", "");
				break;
			case 1: // 彙總別
				occursList.putParam("OOInqData", sumNo);
				break;
			case 2: // 經辦別
				occursList.putParam("OOInqData", titaTlrNo);
				break;
			case 3: // 整批批號
				occursList.putParam("OOInqData", titaBatchNo);
				break;
			case 4: // 摘要代號
				occursList.putParam("OOInqData", dscptCode);
				break;
			case 5: // 傳票批號
				occursList.putParam("OOInqData", slipBatNo);
				break;
			case 6: // 業務類別
				occursList.putParam("OOInqData", titaSecNo);
				break;
			}

			// 查詢會計科子細目設定檔
			CdAcCode tCdAcCode = sCdAcCodeService.findById(new CdAcCodeId(acNoCode, acSubCode, acDtlCode), titaVo);
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
}