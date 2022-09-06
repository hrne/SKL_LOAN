package com.st1.itx.trade.L6;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AcReceivable;
import com.st1.itx.db.domain.ForeclosureFee;
import com.st1.itx.db.domain.LoanBorTx;
import com.st1.itx.db.domain.LoanBorTxId;
import com.st1.itx.db.domain.TxToDoDetailId;
import com.st1.itx.db.service.ForeclosureFeeService;
import com.st1.itx.db.service.LoanBorTxService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.AcDetailCom;
import com.st1.itx.util.common.AcReceivableCom;
import com.st1.itx.util.common.LoanCom;
import com.st1.itx.util.common.TxToDoCom;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L618E")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L618E extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L618E.class);

	@Autowired
	public ForeclosureFeeService sForeclosureFeeService;
	@Autowired
	public LoanBorTxService loanBorTxService;

	@Autowired
	LoanCom loanCom;
	@Autowired
	public TxToDoCom txToDoCom;
	@Autowired
	public AcDetailCom acDetailCom;
	@Autowired
	public AcReceivableCom acReceivableCom;

	@Autowired
	public DataLog dataLog;
	@Autowired
	public Parse parse;
	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	private String iItemCode;
	private int iCustNo;
	private int iFacmNo;
	private String iRvNo;
	private String iAcctCode;
	private BigDecimal iTxAmt;
	private LoanBorTx tLoanBorTx;
	private LoanBorTxId tLoanBorTxId;
	private TempVo tTempVo = new TempVo();
	AcReceivable acReceivable = new AcReceivable();
	List<AcReceivable> acReceivableList = new ArrayList<AcReceivable>();
	ForeclosureFee tForeclosureFee = new ForeclosureFee();

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L618E ");
		this.totaVo.init(titaVo);

//		呆帳戶法務費墊付

		iItemCode = titaVo.getParam("TxItemCode");
		iCustNo = parse.stringToInteger(titaVo.getParam("TxCustNo"));
		iFacmNo = parse.stringToInteger(titaVo.getParam("TxFacmNo"));
		iRvNo = titaVo.getParam("TxDtlValue");
		iAcctCode = titaVo.getParam("AcctCode");
		iTxAmt = parse.stringToBigDecimal(titaVo.getTxAmt());
//		update應處理清單
		txToDoCom.setTxBuffer(this.txBuffer);
		TxToDoDetailId tTxToDoDetailId = new TxToDoDetailId();

		tTxToDoDetailId.setCustNo(iCustNo);
		tTxToDoDetailId.setFacmNo(iFacmNo);
		tTxToDoDetailId.setItemCode(iItemCode);
		tTxToDoDetailId.setDtlValue(iRvNo);
		txToDoCom.updDetailStatus(2, tTxToDoDetailId, titaVo);

		// 銷帳檔有資料時先銷銷帳檔
		if (!"".equals(iAcctCode)) {
			acReceivable = new AcReceivable();
			acReceivableList = new ArrayList<AcReceivable>();
			acReceivable.setReceivableFlag(2); // 銷帳科目記號 -> 2-核心出帳 3-未收費用 4-短繳期金 5-另收欠款
			acReceivable.setAcctCode(iAcctCode); // 業務科目
			acReceivable.setRvAmt(iTxAmt); // 記帳金額
			acReceivable.setCustNo(iCustNo);// 戶號+額度
			acReceivable.setFacmNo(iFacmNo);
			// 紀錄號碼 7 int轉string左補0
			acReceivable.setRvNo(iRvNo); // 銷帳編號
			acReceivable.setOpenAcDate(this.txBuffer.getTxBizDate().getTbsDy());
			acReceivableList.add(acReceivable);
			acReceivableCom.setTxBuffer(this.getTxBuffer());
			acReceivableCom.mnt(1, acReceivableList, titaVo); // 0-起帳 1-銷帳

		}
//		更新法務費檔更新為已銷
//		update 法拍費檔
		tForeclosureFee = sForeclosureFeeService.holdById(Integer.valueOf(iRvNo));
		ForeclosureFee tForeclosureFee2 = (ForeclosureFee) dataLog.clone(tForeclosureFee); // 異動前資料
		if (titaVo.isHcodeNormal()) {
			tForeclosureFee.setCloseDate(this.txBuffer.getTxCom().getTbsdy()); // 銷號日期
			tForeclosureFee.setCaseCode(4); // 4.呆帳戶法務費墊付
		} else {
			tForeclosureFee.setCloseDate(0);
			tForeclosureFee.setCaseCode(0);
		}
		try {
			tForeclosureFee = sForeclosureFeeService.update2(tForeclosureFee, titaVo);
		} catch (DBException e) {
			throw new LogicException("E0007", "sForeclosureFeeService update error : " + e.getErrorMsg());
		}
		dataLog.setEnv(titaVo, tForeclosureFee2, tForeclosureFee); ////
		dataLog.exec(); ////
		// 起帳
		acReceivable = new AcReceivable();
		acReceivableList = new ArrayList<AcReceivable>();
		acReceivable.setReceivableFlag(3); // 銷帳科目記號 -> 2-核心出帳 3-未收費用 4-短繳期金 5-另收欠款
		acReceivable.setAcctCode("F30"); // 業務科目
		// 法拍費用
		acReceivable.setRvAmt(iTxAmt); // 記帳金額
		// 戶號 7
		acReceivable.setCustNo(iCustNo);// 戶號+額度
		// 額度 3
		acReceivable.setFacmNo(iFacmNo);
		// 紀錄號碼 7 int轉string
		acReceivable.setRvNo(iRvNo); // 銷帳編號
		acReceivable.setOpenAcDate(this.txBuffer.getTxBizDate().getTbsDy()); //
		acReceivableList.add(acReceivable);
		acReceivableCom.setTxBuffer(this.getTxBuffer());
//		登錄
		if (titaVo.isHcodeNormal()) {
			acReceivableCom.mnt(0, acReceivableList, titaVo); // 0-起帳 1-銷帳
		} else {
			acReceivableCom.mnt(2, acReceivableList, titaVo); // 0-起帳 1-銷帳
		}
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
		tLoanBorTx.setDesc("呆帳戶法務費墊付");
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