package com.st1.itx.trade.L6;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AcDetail;
import com.st1.itx.db.domain.InsuRenew;
import com.st1.itx.db.domain.LoanBorTx;
import com.st1.itx.db.domain.LoanBorTxId;
import com.st1.itx.db.domain.TxToDoDetailId;
import com.st1.itx.db.service.InsuRenewService;
import com.st1.itx.db.service.LoanBorTxService;
import com.st1.itx.db.service.TxToDoDetailService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.AcDetailCom;
import com.st1.itx.util.common.AcRepayCom;
import com.st1.itx.util.common.BaTxCom;
import com.st1.itx.util.common.LoanCom;
import com.st1.itx.util.common.TxToDoCom;
import com.st1.itx.util.common.data.BaTxVo;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L618B")
@Scope("prototype")
/**
 * 火險費用轉催收
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L618B extends TradeBuffer {

	@Autowired
	public Parse parse;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	@Autowired
	public TxToDoDetailService txToDoDetailService;

	@Autowired
	public TxToDoCom txToDoCom;

	@Autowired
	public AcDetailCom acDetailCom;
	
	@Autowired
	AcRepayCom acRepayCom;

	@Autowired
	public InsuRenewService insuRenewService;

	@Autowired
	public LoanBorTxService loanBorTxService;

	@Autowired
	LoanCom loanCom;

	@Autowired
	BaTxCom baTxCom;

	@Autowired
	public DataLog dataLog;

	private String iItemCode;
	private int iCustNo;
	private int iFacmNo;
	private String iRvNo;
	private BigDecimal iTxAmt;
	private LoanBorTx tLoanBorTx;
	private LoanBorTxId tLoanBorTxId;
	private TempVo tTempVo = new TempVo();
	private List<AcDetail> lAcDetail = new ArrayList<AcDetail>();
	private ArrayList<BaTxVo> baTxList = new ArrayList<BaTxVo>();
	private BigDecimal wkTempAmt = BigDecimal.ZERO;
	private BigDecimal wkOverflow = BigDecimal.ZERO;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L618B ");
		this.totaVo.init(titaVo);

		baTxCom.setTxBuffer(this.txBuffer);
		loanCom.setTxBuffer(this.txBuffer);
		txToDoCom.setTxBuffer(this.txBuffer);
		acRepayCom.setTxBuffer(this.getTxBuffer());

		this.info("Txamt : " + titaVo.getTxAmt());

//		T(6A,#OOItemCode+#OOCustNo+#OOFacmNo+#OOBormNo+#OODtlValue+#OSelectCode)
		iItemCode = titaVo.getParam("TxItemCode");
		iCustNo = parse.stringToInteger(titaVo.getParam("TxCustNo"));
		iFacmNo = parse.stringToInteger(titaVo.getParam("TxFacmNo"));
		iRvNo = titaVo.getParam("TxDtlValue");
		iTxAmt = parse.stringToBigDecimal(titaVo.getTxAmt());
		
		// 檢查到同戶帳務交易需由最近一筆交易開始訂正
		if (titaVo.isHcodeErase()) {
			loanCom.checkEraseCustNoTxSeqNo(iCustNo, titaVo);
		}

//		update應處理清單
		TxToDoDetailId tTxToDoDetailId = new TxToDoDetailId();

		tTxToDoDetailId.setCustNo(iCustNo);
		tTxToDoDetailId.setFacmNo(iFacmNo);
		tTxToDoDetailId.setItemCode(iItemCode);
		tTxToDoDetailId.setDtlValue(iRvNo);

		txToDoCom.updDetailStatus(2, tTxToDoDetailId, titaVo);

//		帳務處理
		if (this.txBuffer.getTxCom().isBookAcYes()) {

			lAcDetail = new ArrayList<AcDetail>();
			// call 應繳試算
			this.baTxList = baTxCom.settingUnPaid(titaVo.getEntDyI(), iCustNo, iFacmNo, 0, 9, BigDecimal.ZERO, titaVo); //

			// 暫收款金額 (暫收借)
			wkTempAmt = acRepayCom.settleTempAmt(this.baTxList, this.lAcDetail, titaVo);

			// 借: F25 催收款項－火險費用

			AcDetail acDetail = new AcDetail();
			acDetail.setDbCr("D");
			acDetail.setAcctCode("F25");
			acDetail.setSumNo("001"); // 催呆轉帳
			acDetail.setTxAmt(iTxAmt);
			acDetail.setCustNo(iCustNo);
			acDetail.setFacmNo(iFacmNo);
			acDetail.setRvNo(iRvNo);
			lAcDetail.add(acDetail);

			// 貸: F09 暫付火險保費 原保險單號碼
			acDetail = new AcDetail();
			acDetail.setDbCr("C");
			acDetail.setAcctCode("F09");
			acDetail.setTxAmt(iTxAmt);
			acDetail.setCustNo(iCustNo);
			acDetail.setFacmNo(iFacmNo);
			acDetail.setRvNo(iRvNo);

			// 累溢收入帳(暫收貸)
			wkOverflow = acRepayCom.settleOverflow(lAcDetail, titaVo);

			lAcDetail.add(acDetail);
			this.txBuffer.addAllAcDetailList(lAcDetail);
		}
		/* 產生會計分錄 */
		acDetailCom.setTxBuffer(this.txBuffer);
		acDetailCom.run(titaVo);

//			update 火險檔
		InsuRenew tIsuRenew = new InsuRenew();
		tIsuRenew = insuRenewService.prevInsuNoFirst(iCustNo, iFacmNo, iRvNo);

		tIsuRenew = insuRenewService.holdById(tIsuRenew.getInsuRenewId());

		InsuRenew tInsuRenew2 = (InsuRenew) dataLog.clone(tIsuRenew); // 異動前資料

		tIsuRenew.setStatusCode(2);

		if (titaVo.isHcodeNormal()) {
			tIsuRenew.setStatusCode(2);
			tIsuRenew.setOvduDate(this.txBuffer.getTxCom().getTbsdy());
		} else {
			tIsuRenew.setStatusCode(1);
			tIsuRenew.setOvduDate(0);
		}

		try {
			tIsuRenew = insuRenewService.update2(tIsuRenew, titaVo);
		} catch (DBException e) {
			throw new LogicException("E0007", "InsuRenew update error : " + e.getErrorMsg());
		}

		dataLog.setEnv(titaVo, tInsuRenew2, tIsuRenew); ////
		dataLog.exec(); ////

		// 放款交易內容檔
		if (titaVo.isHcodeNormal()) {
			addLoanBorTxRoutine(titaVo);
		} else {
			loanCom.setFacmBorTxHcodeByTx(iCustNo, titaVo);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

// 新增放款交易內容檔
	private void addLoanBorTxRoutine(TitaVo titaVo) throws LogicException {
		this.info("addLoanBorTxRoutine ... ");

		tLoanBorTx = new LoanBorTx();
		tLoanBorTxId = new LoanBorTxId();
		loanCom.setFacmBorTx(tLoanBorTx, tLoanBorTxId, iCustNo, iFacmNo, titaVo);
		tLoanBorTx.setDesc("火險費用轉催收");
		tLoanBorTx.setEntryDate(titaVo.getEntDyI());
		tLoanBorTx.setTempAmt(wkTempAmt);
		tLoanBorTx.setOverflow(wkOverflow);
		//
		tLoanBorTx.setDisplayflag("A"); // A:帳務
		tTempVo.clear();
		tTempVo.putParam("RvNo", iRvNo);
		tLoanBorTx.setOtherFields(tTempVo.getJsonString());
		// 更新放款明細檔及帳務明細檔關聯欄
		acRepayCom.updBorTxAcDetail(this.tLoanBorTx, lAcDetail);
		try {
			loanBorTxService.insert(tLoanBorTx, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0005", "放款交易內容檔 " + e.getErrorMsg()); // 新增資料時，發生錯誤
		}
	}
}
