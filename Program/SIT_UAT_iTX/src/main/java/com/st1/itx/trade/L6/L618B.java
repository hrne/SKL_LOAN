package com.st1.itx.trade.L6;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.st1.itx.util.common.LoanCom;
import com.st1.itx.util.common.TxToDoCom;
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
	private static final Logger logger = LoggerFactory.getLogger(L618B.class);

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
	public InsuRenewService insuRenewService;

	@Autowired
	public LoanBorTxService loanBorTxService;

	@Autowired
	LoanCom loanCom;

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

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L618B ");
		this.totaVo.init(titaVo);

		txToDoCom.setTxBuffer(this.txBuffer);

		this.info("Txamt : " + titaVo.getTxAmt());

//		T(6A,#OOItemCode+#OOCustNo+#OOFacmNo+#OOBormNo+#OODtlValue+#OSelectCode)
		iItemCode = titaVo.getParam("TxItemCode");
		iCustNo = parse.stringToInteger(titaVo.getParam("TxCustNo"));
		iFacmNo = parse.stringToInteger(titaVo.getParam("TxFacmNo"));
		iRvNo = titaVo.getParam("TxDtlValue");
		iTxAmt = parse.stringToBigDecimal(titaVo.getTxAmt());
//		update應處理清單
		TxToDoDetailId tTxToDoDetailId = new TxToDoDetailId();

		tTxToDoDetailId.setCustNo(iCustNo);
		tTxToDoDetailId.setFacmNo(iFacmNo);
		tTxToDoDetailId.setItemCode(iItemCode);
		tTxToDoDetailId.setDtlValue(iRvNo);

		txToDoCom.updDetailStatus(2, tTxToDoDetailId, titaVo);

//		帳務處理
		if (this.txBuffer.getTxCom().isBookAcYes()) {
			List<AcDetail> acDetailList = new ArrayList<AcDetail>();

			// 借: F25 催收款項－火險費用
			// 貸: F09 暫付火險保費 原保險單號碼

			AcDetail tacDetail = new AcDetail();

			tacDetail.setDbCr("D");
			tacDetail.setAcctCode("F25");
			tacDetail.setTxAmt(iTxAmt);
			tacDetail.setCustNo(iCustNo);
			tacDetail.setFacmNo(iFacmNo);
			tacDetail.setRvNo(iRvNo);

			acDetailList.add(tacDetail);

			tacDetail = new AcDetail();

			tacDetail.setDbCr("C");
			tacDetail.setAcctCode("F09");
			tacDetail.setTxAmt(iTxAmt);
			tacDetail.setCustNo(iCustNo);
			tacDetail.setFacmNo(iFacmNo);
			tacDetail.setRvNo(iRvNo);

			acDetailList.add(tacDetail);
			this.txBuffer.addAllAcDetailList(acDetailList);
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
			loanCom.setFacmBorTxHcode(iCustNo, iFacmNo, titaVo);
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
		//
		tLoanBorTx.setDisplayflag("A"); // A:帳務
		tLoanBorTx.setTxAmt(iTxAmt);
		tTempVo.clear();
		tTempVo.putParam("RvNo", iRvNo);
		tLoanBorTx.setOtherFields(tTempVo.getJsonString());
		try {
			loanBorTxService.insert(tLoanBorTx, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0005", "放款交易內容檔 " + e.getErrorMsg()); // 新增資料時，發生錯誤
		}
	}
}
