package com.st1.itx.trade.L3;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AcDetail;
import com.st1.itx.db.domain.CdAcCode;
import com.st1.itx.db.domain.CdAcCodeId;
import com.st1.itx.db.domain.InsuRenew;

import com.st1.itx.db.domain.LoanBorTx;
import com.st1.itx.db.service.CdAcCodeService;
import com.st1.itx.db.service.InsuRenewService;
import com.st1.itx.db.service.LoanBorTxService;
import com.st1.itx.db.service.LoanRateChangeService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.AcDetailCom;
import com.st1.itx.util.common.AcReceivableCom;
import com.st1.itx.util.common.AcRepayCom;
import com.st1.itx.util.common.BaTxCom;
import com.st1.itx.util.common.LoanCom;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.common.data.BaTxVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/*
 * L3250 還款來源冲正
 * a.使用時機:將暫收款金額轉回還款來源
 * b.本交易不可訂正
 */

/*
 * Tita
 * TimCustNo=9,7 戶號
 * FacmNo=9,3  額度編號 
 * EntryDate 入帳日期
 * AcDate 會計日期
 * AcDate 經辦
 * TxtNo 交易序號
  * 沖正交易序號 ，會計日(7)+單位別(4)+經辦(6)+交易序號(8)
 */

/**
 * L3250 回收登錄
 * 
 * @author iza
 * @version 1.0.0
 */
@Service("L3250")
@Scope("prototype")
public class L3250 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public InsuRenewService insuRenewService;
	@Autowired
	public LoanBorTxService loanBorTxService;
	@Autowired
	public LoanRateChangeService loanRateChangeService;
	@Autowired
	public CdAcCodeService cdAcCodeService;

	@Autowired
	Parse parse;
	@Autowired
	DateUtil dDateUtil;
	@Autowired
	LoanCom loanCom;
	@Autowired
	AcDetailCom acDetailCom;
	@Autowired
	AcRepayCom acRepayCom;
	@Autowired
	AcReceivableCom acReceivableCom;
	@Autowired
	BaTxCom baTxCom;
	@Autowired
	public SendRsp sendRsp;

	private TitaVo titaVo;
	private int iCustNo;
	private int iFacmNo;
	private int iEntryDate;
	private BigDecimal iTxAmt = BigDecimal.ZERO;
	private String iRvNo;
	private String iAcctCode;
	private BigDecimal wkTxAmt = BigDecimal.ZERO;
	private int wkRepayCode;

	// work area
	private AcDetail acDetail;
	private List<AcDetail> lAcDetail = new ArrayList<AcDetail>();
	private ArrayList<BaTxVo> baTxList = new ArrayList<BaTxVo>();
	private CdAcCode tCdAcCode = new CdAcCode();

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L3250 ");

		this.totaVo.init(titaVo);
		this.titaVo = titaVo;
		loanCom.setTxBuffer(this.txBuffer);
		baTxCom.setTxBuffer(this.txBuffer);
		acRepayCom.setTxBuffer(this.getTxBuffer());

		// 取得輸入資料
		iCustNo = this.parse.stringToInteger(titaVo.getParam("TimCustNo"));
		iFacmNo = this.parse.stringToInteger(titaVo.getParam("FacmNo"));
		iEntryDate = this.parse.stringToInteger(titaVo.getParam("EntryDate"));
		iRvNo = titaVo.getParam("RvNo");
		iAcctCode = titaVo.getParam("AcctCode");
		// Check Input
		if (!titaVo.getHsupCode().equals("1")) {
			sendRsp.addvReason(this.txBuffer, titaVo, "0005", ""); // 訂正需主管核可
		}

		checkInputRoutine();

		// 檢查到同戶帳務交易需由最近一筆交易開始訂正
		loanCom.checkEraseCustNoTxSeqNo(iCustNo, titaVo);

		// 沖正處理
		repayEraseRoutine();

		wkRepayCode = 90; // 暫收抵繳
		titaVo.putParam("RpCode1", wkRepayCode);
		titaVo.putParam("RpAmt1", wkTxAmt);
		titaVo.putParam("RpCustNo1", iCustNo);
		titaVo.putParam("RpFacmNo1", iFacmNo);
		this.baTxList = baTxCom.settingUnPaid(iEntryDate, iCustNo, 0, 0, 99, BigDecimal.ZERO, titaVo); // 99-費用全部

		// 暫收款金額 (暫收借)
		acRepayCom.settleTempAmt(this.baTxList, this.lAcDetail, titaVo);

//			// 費用科目
		acDetail = new AcDetail();
		acDetail.setDbCr("D");
		acDetail.setAcctCode(iAcctCode);
		acDetail.setTxAmt(iTxAmt);
		acDetail.setCustNo(iCustNo);
		acDetail.setFacmNo(iFacmNo);
		acDetail.setRvNo(iRvNo);
		acDetail.setReceivableFlag(3);
		if (tCdAcCode.getReceivableFlag() > 0) {
			acDetail.setReceivableFlag(tCdAcCode.getReceivableFlag());
		}
		if ("TMI".equals(iAcctCode)) {
			checkInsuRenew(acDetail, titaVo);
		}
		lAcDetail.add(acDetail);

		// 累溢收入帳(暫收貸)
		acRepayCom.settleOverflow(lAcDetail, titaVo);

		// 產生會計分錄
		this.txBuffer.setAcDetailList(lAcDetail);
		acDetailCom.setTxBuffer(this.txBuffer);
		acDetailCom.run(titaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void checkInputRoutine() throws LogicException {
		if (titaVo.isHcodeErase()) {
			throw new LogicException(titaVo, "E0010", "本交易不可訂正"); // 功能選擇錯誤
		}
		tCdAcCode = cdAcCodeService.acCodeAcctFirst(iAcctCode, titaVo);
		if (tCdAcCode == null) {
			throw new LogicException("E6001", "AcDetailCom 科目有誤" + iAcctCode); // E6001 分錄檢核有誤
		}
	}

	// 沖正處理
	private void repayEraseRoutine() throws LogicException {
		this.info("calcRepayEraseRoutine ...");
		// 查詢放款交易內容檔
		Slice<LoanBorTx> slLoanBorTx = loanBorTxService.custNoTxtNoEq(iCustNo, titaVo.getOrgEntdyI() + 19110000,
				titaVo.getOrgKin(), titaVo.getOrgTlr(), titaVo.getOrgTno(), 0, Integer.MAX_VALUE, titaVo);
		if (slLoanBorTx == null) {
			throw new LogicException(titaVo, "E0001", "放款交易內容檔"); // 查詢資料不存在
		}
		for (LoanBorTx tx : slLoanBorTx.getContent()) {
			if (!tx.getTitaHCode().equals("0")) {
				continue;
			}
			if (tx.getEntryDate() != iEntryDate) {
				continue;
			}
//			if (!tx.getCreateEmpNo().equals("999999")) {
//				throw new LogicException(titaVo, "E0010", "非轉換資料不可執行L3240回收冲正（轉換前資料）"); // 功能選擇錯誤
//			}
			wkRepayCode = tx.getRepayCode();
			// 註記交易內容檔
			loanCom.setFacmBorTxHcode(tx.getCustNo(), tx.getFacmNo(), tx.getBorxNo(), titaVo);
			wkTxAmt = wkTxAmt.add(tx.getTxAmt());
		}
	}

	private void checkInsuRenew(AcDetail ac, TitaVo titaVo) throws LogicException {
		Slice<InsuRenew> slInsuRenew = insuRenewService.findCustEq(iCustNo, 0, Integer.MAX_VALUE, titaVo);
		if (slInsuRenew != null) {
			for (InsuRenew tInsuRenew : slInsuRenew.getContent()) {
				this.info("InsuRenew=".toString());
				if (tInsuRenew.getAcDate() > 0 && tInsuRenew.getTitaTxtNo().equals(titaVo.getOrgTno())
						&& tInsuRenew.getTotInsuPrem().compareTo(iTxAmt) == 0) {
					this.info("this.InsuRenew=".toString());
					switch (tInsuRenew.getStatusCode()) {
					case 0:
						ac.setReceivableFlag(3);
						ac.setAcctCode("TMI"); // TMI 火險保費
						break;
					case 1:
						ac.setReceivableFlag(2);
						ac.setAcctCode("F09"); // F09 暫付款－火險保費
						break;
					case 2:
						ac.setReceivableFlag(2);
						ac.setAcctCode("F24"); // F24 催收款項－火險保費
						break;
					default:
						break;
					}
					ac.setCustNo(tInsuRenew.getCustNo());// 戶號+額度
					ac.setFacmNo(tInsuRenew.getFacmNo());
					ac.setRvNo(tInsuRenew.getPrevInsuNo()); // 銷帳編號
					TempVo tTempVo = new TempVo();
					tTempVo.clear();
					tTempVo.putParam("OpenAcDate", tInsuRenew.getInsuStartDate());
					tTempVo.putParam("InsuYearMonth", tInsuRenew.getInsuYearMonth());
					ac.setJsonFields(tTempVo.getJsonString());
				}
			}
		}
	}

}