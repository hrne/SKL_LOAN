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
import com.st1.itx.db.domain.TxTranCode;
import com.st1.itx.db.service.AcDetailService;
import com.st1.itx.db.service.CdAcCodeService;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.TxTellerService;
import com.st1.itx.db.service.TxTranCodeService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/**
 * Tita AcDate=9,7 BranchNo=9,4 CurrencyCode=X,3 CustNo=9,7 TitaTlrNo=X,6
 * TitaBatchNo=X,6 TitaTxCd=X,5 RelTxseq=X,18 END=X,1
 */

@Service("L6906") // 會計分錄查詢
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */

public class L6906 extends TradeBuffer {

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
		this.info("active L6906 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		String iBranchNo = titaVo.getParam("BranchNo");
		String iCurrencyCode = titaVo.getParam("CurrencyCode");
		int iAcDate = this.parse.stringToInteger(titaVo.getParam("AcDate"));
		int iFAcDate = iAcDate + 19110000;

		int iCustNo = this.parse.stringToInteger(titaVo.getParam("CustNo"));
		String iTitaTlrNo = titaVo.getParam("TitaTlrNo");
		String iTitaBatchNo = titaVo.getParam("TitaBatchNo");
		String iTitaTxCd = titaVo.getParam("TitaTxCd");
		String iRelTxseq = titaVo.getParam("RelTxseq");
		String iTlrItem = "";
		String iTranItem = "";

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 100; // 461 * 100 = 46,100

		// 查詢會計帳務明細檔
		Slice<AcDetail> slAcDetail = null;
		// 戶號 or 經辦 or 整批批號 or 交易代號
		if (!(iCustNo == 0)) {
			slAcDetail = sAcDetailService.acdtlCustNo(iBranchNo, iCurrencyCode, iFAcDate, iCustNo, this.index,
					this.limit, titaVo);
			if (slAcDetail == null) {
				throw new LogicException("E0001", "無該戶號資料");
			}
		} else if (!(iTitaTlrNo.isEmpty())) {
			slAcDetail = sAcDetailService.acdtlTitaTlrNo(iBranchNo, iCurrencyCode, iFAcDate, iTitaTlrNo, this.index,
					this.limit, titaVo);
			if (slAcDetail == null) {
				throw new LogicException("E0001", "無該經辦資料");
			}
		} else if (!(iTitaBatchNo.isEmpty())) {
			slAcDetail = sAcDetailService.acdtlTitaBatchNo(iBranchNo, iCurrencyCode, iFAcDate, iTitaBatchNo, this.index,
					this.limit, titaVo);
			if (slAcDetail == null) {
				throw new LogicException("E0001", "無該整批批號資料");
			}
		} else if (!(iTitaTxCd.isEmpty())) {
			slAcDetail = sAcDetailService.acdtlTitaTxCd(iBranchNo, iCurrencyCode, iFAcDate, iTitaTxCd, this.index,
					this.limit, titaVo);
			if (slAcDetail == null) {
				throw new LogicException("E0001", "無該交易代號資料");
			}
		} else {
			throw new LogicException(titaVo, "E6011", "戶號或經辦或整批批號或交易代號擇一輸入"); // 查詢資料不可為空白
		}
		List<AcDetail> lAcDetail = slAcDetail == null ? null : new ArrayList<AcDetail>(slAcDetail.getContent());

		// 排序依 交易序號 戶號 由小到大
		lAcDetail.sort((c1, c2) -> {
			int result = 0;
			if (c1.getRelTxseq().compareTo(c2.getRelTxseq()) != 0) {
				result = c1.getRelTxseq().compareTo(c2.getRelTxseq());
			} else if (c1.getCustNo() - c2.getCustNo() != 0) {
				result = c1.getCustNo() - c2.getCustNo();
			} else if (c1.getFacmNo() - c2.getFacmNo() != 0) {
				result = c1.getFacmNo() - c2.getFacmNo();
			} else if (c1.getBormNo() - c2.getBormNo() != 0) {
				result = c1.getBormNo() - c2.getBormNo();
			} else if (c1.getAcNoCode().compareTo(c2.getAcNoCode()) != 0) {
				result = c1.getAcNoCode().compareTo(c2.getAcNoCode());
			} else if (c1.getAcSubCode().compareTo(c2.getAcSubCode()) != 0) {
				result = c1.getAcSubCode().compareTo(c2.getAcSubCode());
			} else if (c1.getAcDtlCode().compareTo(c2.getAcDtlCode()) != 0) {
				result = c1.getAcDtlCode().compareTo(c2.getAcDtlCode());
			} else {
				result = 0;
			}
			return result;
		});

		for (AcDetail tAcDetail : lAcDetail) {

			this.info("L6906 RelTxseq : " + iRelTxseq);

			// 登放序號有輸入時只查詢登放序號的資料
			if (!(iRelTxseq.isEmpty()) && !(iRelTxseq.equals(tAcDetail.getRelTxseq()))) {
				continue;
			}

			OccursList occursList = new OccursList();
			occursList.putParam("OORelTxseq", tAcDetail.getRelTxseq());
			occursList.putParam("OOCustNo", tAcDetail.getCustNo());
			occursList.putParam("OOFacmNo", tAcDetail.getFacmNo());
			occursList.putParam("OOBormNo", tAcDetail.getBormNo());
			occursList.putParam("OOAcNoCode", tAcDetail.getAcNoCode());
			occursList.putParam("OOAcSubCode", tAcDetail.getAcSubCode());
			occursList.putParam("OOAcDtlCode", tAcDetail.getAcDtlCode());
			occursList.putParam("OOTitaBatchNo", tAcDetail.getTitaBatchNo());
			occursList.putParam("OOSlipNote", tAcDetail.getSlipNote());
			iTranItem = "";
			iTranItem = inqTxTranCode(tAcDetail.getTitaTxCd(), iTranItem, titaVo);
			occursList.putParam("OOTranItem", iTranItem);
			occursList.putParam("OOTitaTxCd", tAcDetail.getTitaTxCd());

			iTlrItem = "";
			iTlrItem = inqCdEmp(tAcDetail.getTitaTlrNo(), iTlrItem, titaVo);
			occursList.putParam("OOTlrItem", iTlrItem);
			occursList.putParam("OOTitaTlrNo", tAcDetail.getTitaTlrNo());

			iTlrItem = "";
			iTlrItem = inqCdEmp(tAcDetail.getTitaSupNo(), iTlrItem, titaVo);
			occursList.putParam("OOSupItem", iTlrItem);
			occursList.putParam("OOTitaSupNo", tAcDetail.getTitaSupNo());

			if (tAcDetail.getDbCr().equals("D")) {
				occursList.putParam("OODbAmt", tAcDetail.getTxAmt());
				occursList.putParam("OOCrAmt", 0);
			} else {
				occursList.putParam("OODbAmt", 0);
				occursList.putParam("OOCrAmt", tAcDetail.getTxAmt());
			}

			// 查詢會計科子細目設定檔
			CdAcCode tCdAcCode = sCdAcCodeService.findById(
					new CdAcCodeId(tAcDetail.getAcNoCode(), tAcDetail.getAcSubCode(), tAcDetail.getAcDtlCode()),
					titaVo);
			if (tCdAcCode == null) {
				occursList.putParam("OOAcNoItem", "");
			} else {
				occursList.putParam("OOAcNoItem", tCdAcCode.getAcNoItem());
			}
			occursList.putParam("OOLastUpdate", parse.timeStampToStringDate(tCdAcCode.getLastUpdate()) + " "
					+ parse.timeStampToStringTime(tCdAcCode.getLastUpdate()));
			occursList.putParam("OOLastEmp",
					tCdAcCode.getLastUpdateEmpNo() + " " + empName(titaVo, tCdAcCode.getLastUpdateEmpNo()));

			/* 將每筆資料放入Tota的OcList */
			this.totaVo.addOccursList(occursList);
		}

		if (this.totaVo.getOccursList().size() == 0) {
			throw new LogicException(titaVo, "E0001", "會計帳務明細檔"); // 查無資料
		}

		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (slAcDetail != null && slAcDetail.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			this.totaVo.setMsgEndToEnter();// 手動折返
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

	private String empName(TitaVo titaVo, String empNo) throws LogicException {
		String rs = empNo;

		CdEmp cdEmp = cdEmpService.findById(empNo, titaVo);
		if (cdEmp != null) {
			rs = cdEmp.getFullname();
		}
		return rs;
	}
}