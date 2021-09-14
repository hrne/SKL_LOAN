package com.st1.itx.trade.L6;

import java.util.ArrayList;
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
import com.st1.itx.db.domain.TxTranCode;
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
		int iAcDateSt = this.parse.stringToInteger(titaVo.getParam("AcDateSt"));
		int iFAcDateSt = iAcDateSt + 19110000;
		int iAcDateEd = this.parse.stringToInteger(titaVo.getParam("AcDateEd"));
		int iFAcDateEd = iAcDateEd + 19110000;

		String iTranItem = "";

		int iCustNo = this.parse.stringToInteger(titaVo.getParam("CustNo"));
		int iFacmNo = this.parse.stringToInteger(titaVo.getParam("FacmNo"));
		// int iBormNo = this.parse.stringToInteger(titaVo.getParam("BormNo"));
		String iRvNo = titaVo.getParam("RvNo").trim();

		if (iAcSubCode.isEmpty()) {
			iAcSubCode = "     ";
		}
		if (iAcDtlCode.isEmpty()) {
			iAcDtlCode = "  ";
		}

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 100; // 316 * 100 = 31,600

		// 查詢會計帳務明細檔
		Slice<AcDetail> slAcDetail = sAcDetailService.findL6908(iAcBookCode, iAcSubBookCode.trim() + "%", iBranchNo,
				iCurrencyCode, iAcNoCode, iAcSubCode, iAcDtlCode, iCustNo, iFacmNo, iFAcDateSt, iFAcDateEd, this.index,
				this.limit, titaVo);

		if (slAcDetail == null) {
			throw new LogicException(titaVo, "E0001", "會計帳務明細檔"); // 查無資料
		}
		// 如有找到資料
		for (AcDetail tAcDetail : slAcDetail.getContent()) {

			this.info("L6908 iRvNo : " + iRvNo + "-" + tAcDetail.getRvNo());
			this.info("L6908 AcNoCode : " + iAcBookCode + "-" + iAcNoCode + "-" + iAcSubCode + "-" + iAcDtlCode + "-"
					+ tAcDetail.getAcNoCode() + "-" + tAcDetail.getAcSubCode() + "-" + tAcDetail.getAcDtlCode() + "-"
					+ tAcDetail.getTxAmt() + "-" + tAcDetail.getAcBookFlag() + "-" + tAcDetail.getAcBookCode() + "-"
					+ tAcDetail.getEntAc() + "-" + tAcDetail.getCustNo() + "-" + tAcDetail.getFacmNo());

			// 不含未入帳,例如:未放行之交易
			// 0:未入帳 1:已入帳 2:被沖正(隔日訂正) 3.沖正(隔日訂正)
			if (tAcDetail.getEntAc() == 0) {
				this.info("L6908 0 ");
				continue;
			}

			// 銷帳編號有輸入時只查詢銷帳編號的資料，業務科目記號=1.資負明細科目（放款、催收款項..)時撥款序號為銷帳編號
			if (tAcDetail.getAcctFlag() == 1) {
				if (!(iRvNo.isEmpty()) && !(iRvNo.equals(parse.IntegerToString(tAcDetail.getBormNo(), 3)))) {
					this.info("L6908 1.1 " + iRvNo + "-" + parse.IntegerToString(tAcDetail.getBormNo(), 3));
					continue;
				}
			} else {
				if (!(iRvNo.isEmpty()) && !(iRvNo.equals(tAcDetail.getRvNo()))) {
					this.info("L6908 1.2 ");
					continue;
				}
			}
			// 戶號有輸入時只查詢戶號的資料
			if (!(iCustNo == tAcDetail.getCustNo())) {
				this.info("L6908 2 ");
				continue;
			}
			if (!(iFacmNo == tAcDetail.getFacmNo())) {
				this.info("L6908 3 ");
				continue;
			}
			// if (!(iBormNo == tAcDetail.getBormNo())) {
			// continue;
			// }

			// 0: 不細分 (000)
			// 1: 兼全帳冊與特殊帳冊 (輸入000,輸出含000,201 ; 輸入201,只輸出201)
			// 2: 特殊帳冊之應收調撥款，明細檔無(只寫入總帳檔)
			// 3: 特殊帳冊(L6201:其他傳票輸入) (輸入000,只輸出000 ; 輸入201,只輸出201)

//			if (tAcDetail.getAcBookFlag() == 0) {
//				if (!(iAcBookCode.equals("000") || iAcBookCode.equals("10H"))) {
//					this.info("L6908 4 ");
//					continue;
//				}
//			} else if (tAcDetail.getAcBookFlag() == 1) {
//				if (!(iAcBookCode.equals("000") || iAcBookCode.equals("10H")
//						|| iAcBookCode.equals(tAcDetail.getAcBookCode()))) {
//					this.info("L6908 5 ");
//					continue;
//				}
//			} else if (tAcDetail.getAcBookFlag() == 3) {
//				if (!(iAcBookCode.equals(tAcDetail.getAcBookCode()) || iAcBookCode.equals("10H"))) {
//					this.info("L6908 6 ");
//					continue;
//				}
//			} else {
//				this.info("L6908 7 ");
//				continue;
//			}

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

			// 查詢會計科子細目設定檔
			CdAcCode tCdAcCode = sCdAcCodeService.findById(new CdAcCodeId(iAcNoCode, iAcSubCode, iAcDtlCode), titaVo);
			if (tCdAcCode == null) {
				throw new LogicException(titaVo, "E0001", "會計科子細目設定檔"); // 查無資料
			}
			// 
			int clsFlag = 1;
			if (tCdAcCode.getReceivableFlag() > 0 && tAcDetail.getDbCr().equals(tCdAcCode.getDbCr())) {
				clsFlag = 0;
			}
			occursList.putParam("OOClsFlag", clsFlag);

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
}