package com.st1.itx.trade.L3;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.FacMainId;
import com.st1.itx.db.domain.FacProd;
import com.st1.itx.db.domain.LoanBorMain;
import com.st1.itx.db.domain.LoanBorMainId;
import com.st1.itx.db.domain.LoanBorTx;
import com.st1.itx.db.domain.LoanBorTxId;
import com.st1.itx.db.domain.TxTemp;
import com.st1.itx.db.domain.TxTempId;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.FacProdService;
import com.st1.itx.db.service.FacProdStepRateService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.db.service.LoanBorTxService;
import com.st1.itx.db.service.LoanRateChangeService;
import com.st1.itx.db.service.TxTempService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.AcDetailCom;
import com.st1.itx.util.common.AuthLogCom;
import com.st1.itx.util.common.LoanCom;
import com.st1.itx.util.common.LoanDueAmtCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;

/*
 * L3110 預約撥款
 * a.此功能供預約次日(含)之後且一個月內，預先建立撥款資料，待到預約當日系統自動建立撥款。
 * b.撥款方式僅提供整批匯款。
 * c.會先佔用額度，不做會計帳。
 * d.此交易為2段式交易
 */
/*
 * Tita
 * TimCustNo=9,7
 * FacmNo=9,3
 * BormNo=9,3
 * RateIncr=+9,2.4
 * ApproveRate=9,2.4
 * RateCode=9,1
 * RateAdjFreq=9,2
 * DrawdownCode=9,1
 * CurrencyCode=X,3
 * TimDrawdownAmt=9,14.2
 * DrawdownDate=9,7
 * LoanTermYy=9,2
 * LoanTermMm=9,2
 * LoanTermDd=9,3
 * MaturityDate=9,7
 * AmortizedCode=9,1
 * FreqBase=9,1
 * PayIntFreq=9,2
 * RepayFreq=9,2
 * GracePeriod=9,3
 * GraceDate=9,7
 * SpecificDd=9,2
 * SpecificDate=9,8
 * FirstDueDate=9,7
 * FirstAdjRateDate=9,7
 * NextIntDate=9,8
 * NextRepayDate=9,8
 * TotalPeriod=9,3
 * TimAcctFee=9,14.2
 * TimFinalBal=9,14.2
 * NotYetFlag=X,1
 * PieceCode=X,1
 * UsageCode=9,1
 * SyndNo=9,3
 * RenewFlag=X,1
 * OLDFacmNo=9,3
 * OLDBormNo=9,3
 * RelationCode=X,2
 * RelationName=X,100
 * RelationId=X,10
 * RelationBirthday=9,7
 * RelationGender=X,1
 * RemitBank=9,3
 * RemitBranch=9,4
 * RemitAcctNo=9,14
 * CompensateAcct=X,60
 * Remark=X,40
 */
/**
 * L3110 預約撥款
 * 
 * @author iza
 * @version 1.0.0
 */
@Service("L3110")
@Scope("prototype")
public class L3110 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public FacProdService facProdService;
	@Autowired
	public FacProdStepRateService facProdStepRateService;
	@Autowired
	public FacMainService facMainService;
	@Autowired
	public LoanBorMainService loanBorMainService;
	@Autowired
	public LoanBorTxService loanBorTxService;
	@Autowired
	public LoanRateChangeService loanRateChangeService;
	@Autowired
	public TxTempService txTempService;

	@Autowired
	Parse parse;
	@Autowired
	DateUtil dDateUtil;
	@Autowired
	AcDetailCom acDetailCom;
	@Autowired
	LoanDueAmtCom loanDueAmtCom;
	@Autowired
	LoanCom loanCom;
	@Autowired
	public AuthLogCom authLogCom;

	TitaVo titaVo = new TitaVo();
	private int iCustNo;
	private int iFacmNo;
	private int iBormNo;
	private int iDrawdownDate;
	private int iMaturityDate;
	private BigDecimal iDrawdownAmt;
	private String iCompensateFlag;

	// work area
	private int wkBormNo;
	private int wkBorxNo;
	private int wkTbsDy;
	private FacProd tFacProd;
	private FacMain tFacMain;
	private LoanBorMain tLoanBorMain;
	private LoanBorTx tLoanBorTx;
	private LoanBorTxId tLoanBorTxId;
	private TxTemp tTxTemp;
	private TxTempId tTxTempId;
	private TempVo tTempVo;
	private TempVo tempVo = new TempVo();
	private DecimalFormat df = new DecimalFormat("##,###,###,###,##0");

	// initialize variable
	@PostConstruct
	public void init() {
		this.iCustNo = 0;
		this.iFacmNo = 0;
		this.wkBormNo = 0;
		this.wkBorxNo = 1;
		this.iDrawdownAmt = new BigDecimal(0);
		this.tFacProd = new FacProd();
		this.tFacMain = new FacMain();
		this.tLoanBorMain = new LoanBorMain();
		this.tLoanBorTx = new LoanBorTx();
		this.tLoanBorTxId = new LoanBorTxId();
		this.tTempVo = new TempVo();
	}

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L3110 ");
		this.info("   isActfgEntry   = " + titaVo.isActfgEntry());
		this.info("   isActfgSuprele = " + titaVo.isActfgSuprele());
		this.info("   isHcodeNormal  = " + titaVo.isHcodeNormal());
		this.info("   isHcodeErase   = " + titaVo.isHcodeErase());
		this.info("   isHcodeModify  = " + titaVo.isHcodeModify());
		this.info("   EntdyI         = " + titaVo.getEntDyI());
		this.info("   Kinbr          = " + titaVo.getKinbr());
		this.info("   TlrNo          = " + titaVo.getTlrNo());
		this.info("   Tno            = " + titaVo.getTxtNo());
		this.info("   OrgEntdyI      = " + titaVo.getOrgEntdyI());
		this.info("   OrgKin         = " + titaVo.getOrgKin());
		this.info("   OrgTlr         = " + titaVo.getOrgTlr());
		this.info("   OrgTno         = " + titaVo.getOrgTno());

		this.totaVo.init(titaVo);
		this.titaVo = titaVo;
		this.wkTbsDy = this.txBuffer.getTxCom().getTbsdy();
		loanCom.setTxBuffer(this.txBuffer);

		// 取得輸入資料
		iCustNo = this.parse.stringToInteger(titaVo.getParam("TimCustNo"));
		iFacmNo = this.parse.stringToInteger(titaVo.getParam("FacmNo"));
		iBormNo = this.parse.stringToInteger(titaVo.getParam("BormNo"));
		iDrawdownAmt = this.parse.stringToBigDecimal(titaVo.getParam("TimDrawdownAmt"));
		iDrawdownDate = this.parse.stringToInteger(titaVo.getParam("DrawdownDate"));
		iMaturityDate = this.parse.stringToInteger(titaVo.getParam("MaturityDate"));
		iCompensateFlag = titaVo.getParam("CompensateFlag");

		wkBormNo = iBormNo;

		// 維護額度主檔
		FacMainRoutine();

		// 查詢商品參數檔
		tFacProd = facProdService.findById(tFacMain.getProdNo());
		if (tFacProd == null) {
			throw new LogicException(titaVo, "E0001", "商品參數檔"); // 查詢資料不存在
		}

		// 維護放款檔
		LoanBorMainRoutine();

		// 維護放款交易內容檔
		LoanBorTxRoutine();

		this.totaVo.putParam("BormNo", wkBormNo);
		this.addList(this.totaVo);
		return this.sendList();
	}
	// -------------------------------------------------------------------------------

	private void FacMainRoutine() throws LogicException {
		BigDecimal wkAvailableAmt = new BigDecimal(0);
		BigDecimal wkDrawdownAmt = new BigDecimal(0);
		BigDecimal wkRvDrawdownAmt = new BigDecimal(0);

		int wkFirstDrawdownDate = 0;
		int wkMaturityDate = 0;

		if (titaVo.isActfgSuprele()) {
			tFacMain = facMainService.findById(new FacMainId(iCustNo, iFacmNo));
			if (tFacMain == null) {
				throw new LogicException(titaVo, "E0001", "額度主檔 戶號 = " + iCustNo + " 額度編號 = " + iFacmNo); // 查詢資料不存在
			}
			wkBormNo = iBormNo;
			return;
		}
		// 已預約金額
		Slice<LoanBorMain> slLoanBorMain = loanBorMainService.findStatusEq(Arrays.asList(99), iCustNo, iFacmNo, iFacmNo,
				0, Integer.MAX_VALUE, titaVo);
		if (slLoanBorMain != null) {
			for (LoanBorMain rv : slLoanBorMain.getContent()) {
				wkRvDrawdownAmt = wkRvDrawdownAmt.add(rv.getDrawdownAmt());
			}
		}
		// 鎖定額度檔
		tFacMain = facMainService.holdById(new FacMainId(iCustNo, iFacmNo));
		tempVo = authLogCom.exec(iCustNo, iFacmNo, titaVo);

		if (tFacMain == null || tempVo == null) {
			throw new LogicException(titaVo, "E0006", "額度主檔"); // 鎖定資料時，發生錯誤
		}
		if (tFacMain.getActFg() == 1) {
			throw new LogicException(titaVo, "E0021",
					"額度檔 戶號 = " + tFacMain.getCustNo() + " 額度編號 =  " + tFacMain.getFacmNo()); // 該筆資料待放行中
		}
		if (titaVo.isHcodeNormal()) {
			// 檢查額度
			wkAvailableAmt = tFacMain.getLineAmt().subtract(tFacMain.getUtilBal()).subtract(wkRvDrawdownAmt);
			if (wkAvailableAmt.compareTo(iDrawdownAmt) < 0) {
				throw new LogicException(titaVo, "E3051", "可用額度=" + wkAvailableAmt); // 額度不足放款金額
			}
			if (tFacMain.getUtilDeadline() < wkTbsDy) {
				throw new LogicException(titaVo, "E3052", "動支期限=" + tFacMain.getUtilDeadline()); // 已超過動支期限

			}
			if (tFacMain.getRecycleCode().equals("1") && tFacMain.getRecycleDeadline() < wkTbsDy) {
				throw new LogicException(titaVo, "E3053", "循環動用期限=" + tFacMain.getRecycleDeadline()); // 已超過循環動用期限
			}
			if (!tFacMain.getL9110Flag().equals("Y")) {
				throw new LogicException(titaVo, "E3083", "額度主檔 戶號 = " + iCustNo + " 額度編號 = " + iFacmNo); // 撥款審核資料表尚未列印，請先作L9110交易
			}
			wkBormNo = tFacMain.getLastBormRvNo() + 1;
			// 新增交易暫存檔
			tTxTempId = new TxTempId();
			tTxTemp = new TxTemp();
			loanCom.setTxTemp(tTxTempId, tTxTemp, iCustNo, iFacmNo, wkBormNo, 0, titaVo);
			tTempVo.clear();
			tTempVo.putParam("DrawdownAmt", iDrawdownAmt);
			tTempVo.putParam("FirstDrawdownDate", tFacMain.getFirstDrawdownDate());
			tTempVo.putParam("MaturityDate", tFacMain.getMaturityDate());
			tTempVo.putParam("BormNo", wkBormNo);
			tTxTemp.setText(tTempVo.getJsonString());
			try {
				txTempService.insert(tTxTemp);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005", "交易暫存檔 Key = " + tTxTempId); // 新增資料時，發生錯誤
			}
			titaVo.putParam("BormNo", wkBormNo);
			titaVo.putParam("MRKEY", FormatUtil.pad9(String.valueOf(iCustNo), 7) + "-"
					+ FormatUtil.pad9(String.valueOf(iFacmNo), 3) + "-" + FormatUtil.pad9(String.valueOf(wkBormNo), 3));
			// 更新額度資料
			tFacMain.setLastBormRvNo(wkBormNo);

			if (tFacMain.getFirstDrawdownDate() == 0) {
				tFacMain.setFirstDrawdownDate(iDrawdownDate);
			}
			if (tFacMain.getMaturityDate() == 0) {
				tFacMain.setMaturityDate(iMaturityDate);
			}
		}

		if (titaVo.isHcodeErase() || titaVo.isHcodeModify()) {
			// 查詢交易暫存檔
			String wkSeqNo = FormatUtil.pad9(String.valueOf(iCustNo), 7) + FormatUtil.pad9(String.valueOf(iFacmNo), 3)
					+ FormatUtil.pad9(String.valueOf(wkBormNo), 3) + "000";
			tTxTemp = txTempService.findById(new TxTempId(titaVo.getOrgEntdyI() + 19110000, titaVo.getOrgKin(),
					titaVo.getOrgTlr(), titaVo.getOrgTno(), wkSeqNo));
			if (tTxTemp == null) {
				throw new LogicException(titaVo, "E0001", "交易暫存檔"); // 查詢資料不存在
			}
			tTempVo = tTempVo.getVo(tTxTemp.getText());
			wkDrawdownAmt = this.parse.stringToBigDecimal(tTempVo.get("DrawdownAmt"));
			wkFirstDrawdownDate = this.parse.stringToInteger(tTempVo.get("FirstDrawdownDate"));
			wkMaturityDate = this.parse.stringToInteger(tTempVo.get("MaturityDate"));
			wkBormNo = this.parse.stringToInteger(tTempVo.get("BormNo"));

			// 準備更新額度資料
			tFacMain.setFirstDrawdownDate(wkFirstDrawdownDate);
			tFacMain.setMaturityDate(wkMaturityDate);

		}

		if (titaVo.isHcodeModify()) {
			// 新增交易暫存檔
			tTxTemp = new TxTemp();
			tTxTempId = new TxTempId();
			loanCom.setTxTemp(tTxTempId, tTxTemp, iCustNo, iFacmNo, wkBormNo, 0, titaVo);
			tTempVo.clear();
			tTempVo.putParam("DrawdownAmt", iDrawdownAmt);
			tTempVo.putParam("FirstDrawdownDate", tFacMain.getFirstDrawdownDate());
			tTempVo.putParam("MaturityDate", tFacMain.getMaturityDate());
			tTempVo.putParam("BormNo", wkBormNo);
			tTxTemp.setText(tTempVo.getJsonString());
			try {
				txTempService.insert(tTxTemp);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", "交易暫存檔"); // 更新資料時，發生錯誤
			}
		}
		// 更新額度檔
		try {
			tFacMain.setLastUpdate(
					parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
			tFacMain.setLastUpdateEmpNo(titaVo.getTlrNo());
			facMainService.update(tFacMain);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007", "額度主檔"); // 更新資料時，發生錯誤
		}
	}

	private void LoanBorMainRoutine() throws LogicException {
		this.info("LoanBorMainRoutine ...");
		this.info("   tFacMain.getUtilAmt()  = " + tFacMain.getUtilAmt());

		// 登錄 新增撥款檔
		if (titaVo.isActfgEntry() && titaVo.isHcodeNormal()) {
			tLoanBorMain = new LoanBorMain();
			SetLoanBorMain();
			try {
				tLoanBorMain.setCreateDate(
						parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
				tLoanBorMain.setCreateEmpNo(titaVo.getTlrNo());
				loanBorMainService.insert(tLoanBorMain);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005",
						"放款檔 戶號 = " + iCustNo + " 額度編號 = " + iFacmNo + " 預約序號 = " + wkBormNo + " " + e.getErrorMsg()); // 新增資料時，發生錯誤
			}
		}
		// 登錄訂正 刪除撥款檔
		if (titaVo.isActfgEntry() && titaVo.isHcodeErase()) {
			tLoanBorMain = loanBorMainService.holdById(new LoanBorMainId(iCustNo, iFacmNo, wkBormNo));
			if (tLoanBorMain == null) {
				throw new LogicException(titaVo, "E0006", "放款主檔"); // 鎖定資料時，發生錯誤
			}
			// 放款交易訂正交易須由最後一筆交易開始訂正
			loanCom.checkEraseBormTxSeqNo(tLoanBorMain, titaVo);
			try {
				loanBorMainService.delete(tLoanBorMain);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0008", "放款主檔 " + e.getErrorMsg()); // 新增資料時，發生錯誤
			}
		}
		// 登錄修正 修改撥款檔
		if (titaVo.isActfgEntry() && titaVo.isHcodeModify()) {
			tLoanBorMain = loanBorMainService.holdById(new LoanBorMainId(iCustNo, iFacmNo, wkBormNo));
			if (tLoanBorMain == null) {
				throw new LogicException(titaVo, "E0006", "放款主檔"); // 鎖定資料時，發生錯誤
			}
			tLoanBorMain = new LoanBorMain();
			SetLoanBorMain();
			try {
				loanBorMainService.update(tLoanBorMain);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0008", "放款主檔 " + e.getErrorMsg()); // 新增資料時，發生錯誤
			}
		}
		// 放行
		if (titaVo.isActfgSuprele() && titaVo.isHcodeNormal()) {
			tLoanBorMain = loanBorMainService.holdById(new LoanBorMainId(iCustNo, iFacmNo, wkBormNo));
			if (tLoanBorMain == null) {
				throw new LogicException(titaVo, "E0006", "撥款主檔"); // 鎖定資料時，發生錯誤
			}
			if (tLoanBorMain.getActFg() != 1) {
				throw new LogicException(titaVo, "E0017",
						"撥款主檔 戶號 = " + iCustNo + "額度編號 = " + iFacmNo + "撥款序號 = " + wkBormNo); // 該筆交易狀態非待放行，不可做交易放行
			}
			// 新增交易暫存檔
			tTxTemp = new TxTemp();
			tTxTempId = new TxTempId();
			loanCom.setTxTemp(tTxTempId, tTxTemp, iCustNo, iFacmNo, wkBormNo, 0, titaVo);
			tTempVo.clear();
			tTempVo.putParam("ActFg", tLoanBorMain.getActFg());
			tTempVo.putParam("LastEntDy", tLoanBorMain.getLastEntDy());
			tTempVo.putParam("LastKinbr", tLoanBorMain.getLastKinbr());
			tTempVo.putParam("LastTlrNo", tLoanBorMain.getLastTlrNo());
			tTempVo.putParam("LastTxtNo", tLoanBorMain.getLastTxtNo());
			tTxTemp.setText(tTempVo.getJsonString());
			try {
				txTempService.insert(tTxTemp);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005", "交易暫存檔 Key = " + tTxTempId); // 新增資料時，發生錯誤
			}
			// 更新撥款主檔
			tLoanBorMain.setActFg(titaVo.getActFgI());
			tLoanBorMain.setLastEntDy(titaVo.getEntDyI());
			tLoanBorMain.setLastKinbr(titaVo.getKinbr());
			tLoanBorMain.setLastTlrNo(titaVo.getTlrNo());
			tLoanBorMain.setLastTxtNo(titaVo.getTxtNo());
			try {
				loanBorMainService.update(tLoanBorMain);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0008", "撥款主檔 " + e.getErrorMsg()); // 新增資料時，發生錯誤
			}
		}
		// 放行訂正
		if (titaVo.isActfgSuprele() && titaVo.isHcodeErase()) {
			tLoanBorMain = loanBorMainService.holdById(new LoanBorMainId(iCustNo, iFacmNo, wkBormNo));
			if (tLoanBorMain == null) {
				throw new LogicException(titaVo, "E0006",
						"撥款主檔 戶號 = " + iCustNo + "額度編號 = " + iFacmNo + "撥款序號 = " + wkBormNo); // 鎖定資料時，發生錯誤
			}
			// 放款交易訂正交易須由最後一筆交易開始訂正
			loanCom.checkEraseBormTxSeqNo(tLoanBorMain, titaVo);
			// 查詢交易暫存檔
			String wkSeqNo = FormatUtil.pad9(String.valueOf(iCustNo), 7) + FormatUtil.pad9(String.valueOf(iFacmNo), 3)
					+ FormatUtil.pad9(String.valueOf(wkBormNo), 3) + "000";
			tTxTemp = txTempService.findById(new TxTempId(titaVo.getOrgEntdyI() + 19110000, titaVo.getOrgKin(),
					titaVo.getOrgTlr(), titaVo.getOrgTno(), wkSeqNo));
			if (tTxTemp == null) {
				throw new LogicException(titaVo, "E0001", "交易暫存檔"); // 查詢資料不存在
			}
			tTempVo = tTempVo.getVo(tTxTemp.getText());
			tLoanBorMain.setActFg(this.parse.stringToInteger(tTempVo.get("ActFg")));
			tLoanBorMain.setLastEntDy(this.parse.stringToInteger(tTempVo.get("LastEntDy")));
			tLoanBorMain.setLastKinbr(tTempVo.get("LastKinbr"));
			tLoanBorMain.setLastTlrNo(tTempVo.get("LastTlrNo"));
			tLoanBorMain.setLastTxtNo(tTempVo.get("LastTxtNo"));
			try {
				loanBorMainService.update(tLoanBorMain);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0008",
						"撥款主檔 戶號 = " + iCustNo + "額度編號 = " + iFacmNo + "撥款序號 = " + wkBormNo + e.getErrorMsg()); // 新增資料時，發生錯誤
			}
		}
	}

	private void LoanBorTxRoutine() throws LogicException {
		this.info("LoanBorMainRoutine ...");

		if (titaVo.isActfgSuprele()) {
			return;
		}
		// 刪除放款交易內容檔
		if (titaVo.isHcodeErase() || titaVo.isHcodeModify()) {
			tLoanBorTx = loanBorTxService.holdById(new LoanBorTxId(iCustNo, iFacmNo, wkBormNo, wkBorxNo));
			if (tLoanBorTx == null) {
				throw new LogicException(titaVo, "E0006", "放款交易內容檔"); // 鎖定資料時，發生錯誤
			}
			try {
				loanBorTxService.delete(tLoanBorTx);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0008", "放款交易內容檔 " + e.getErrorMsg()); // 刪除資料時，發生錯誤
			}
		}

		// 新增放款交易內容檔
		if (titaVo.isHcodeNormal() || titaVo.isHcodeModify()) {
			tLoanBorTx = new LoanBorTx();
			tLoanBorTxId = new LoanBorTxId();
			loanCom.setLoanBorTx(tLoanBorTx, tLoanBorTxId, iCustNo, iFacmNo, wkBormNo, wkBorxNo, titaVo);
			SetLoanBorTx();
			try {
				loanBorTxService.insert(tLoanBorTx);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005", "放款交易內容檔 " + e.getErrorMsg()); // 新增資料時，發生錯誤
			}
		}
	}

	// -------------------------------------------------------------------------------
	private void SetLoanBorMain() throws LogicException {
		BigDecimal wkDueAmt = BigDecimal.ZERO;
		int wGracePeriod = 0;
		int wkNextRepayDate = 0;

		tLoanBorMain.setCustNo(this.parse.stringToInteger(titaVo.getParam("TimCustNo")));
		tLoanBorMain.setFacmNo(this.parse.stringToInteger(titaVo.getParam("FacmNo")));
		tLoanBorMain.setBormNo(wkBormNo);
		tLoanBorMain.setLoanBorMainId(new LoanBorMainId(iCustNo, iFacmNo, wkBormNo));
		tLoanBorMain.setLastBorxNo(wkBorxNo);
		tLoanBorMain.setLastOvduNo(0);
		tLoanBorMain.setStatus(99); // 99:預約撥款
		tLoanBorMain.setRateIncr(this.parse.stringToBigDecimal(titaVo.getParam("RateIncr")));
		tLoanBorMain.setIndividualIncr(new BigDecimal(0));
		tLoanBorMain.setApproveRate(this.parse.stringToBigDecimal(titaVo.getParam("ApproveRate")));
		tLoanBorMain.setStoreRate(this.parse.stringToBigDecimal(titaVo.getParam("ApproveRate")));
		tLoanBorMain.setRateCode(titaVo.getParam("RateCode"));
		tLoanBorMain.setRateAdjFreq(this.parse.stringToInteger(titaVo.getParam("RateAdjFreq")));
		tLoanBorMain.setDrawdownCode(titaVo.getParam("DrawdownCode"));
		tLoanBorMain.setCurrencyCode(titaVo.getParam("CurrencyCode"));
		tLoanBorMain.setDrawdownAmt(iDrawdownAmt);
		tLoanBorMain.setLoanBal(new BigDecimal(0));
		tLoanBorMain.setDrawdownDate(this.parse.stringToInteger(titaVo.getParam("DrawdownDate")));
		tLoanBorMain.setLoanTermYy(this.parse.stringToInteger(titaVo.getParam("LoanTermYy")));
		tLoanBorMain.setLoanTermMm(this.parse.stringToInteger(titaVo.getParam("LoanTermMm")));
		tLoanBorMain.setLoanTermDd(this.parse.stringToInteger(titaVo.getParam("LoanTermDd")));
		tLoanBorMain.setMaturityDate(this.parse.stringToInteger(titaVo.getParam("MaturityDate")));
		tLoanBorMain.setIntCalcCode(titaVo.getParam("IntCalcCode"));
		tLoanBorMain.setAmortizedCode(titaVo.getParam("AmortizedCode"));
		tLoanBorMain.setFreqBase(this.parse.stringToInteger(titaVo.getParam("FreqBase")));
		tLoanBorMain.setPayIntFreq(this.parse.stringToInteger(titaVo.getParam("PayIntFreq")));
		tLoanBorMain.setRepayFreq(this.parse.stringToInteger(titaVo.getParam("RepayFreq")));
		tLoanBorMain.setTotalPeriod(this.parse.stringToInteger(titaVo.getParam("TotalPeriod")));
		tLoanBorMain.setRepaidPeriod(0);
		tLoanBorMain.setPaidTerms(0);
		tLoanBorMain.setPrevPayIntDate(0);
		tLoanBorMain.setPrevRepaidDate(0);
		tLoanBorMain.setNextPayIntDate(this.parse.stringToInteger(titaVo.getParam("FirstDueDate")));
		tLoanBorMain.setNextRepayDate(this.parse.stringToInteger(titaVo.getParam("NextRepayDate")));
		tLoanBorMain.setGracePeriod(this.parse.stringToInteger(titaVo.getParam("GracePeriod")));
		tLoanBorMain.setGraceDate(this.parse.stringToInteger(titaVo.getParam("GraceDate")));
		tLoanBorMain.setSpecificDd(this.parse.stringToInteger(titaVo.getParam("SpecificDd")));
		tLoanBorMain.setSpecificDate(this.parse.stringToInteger(titaVo.getParam("SpecificDate")));
		tLoanBorMain.setFirstDueDate(this.parse.stringToInteger(titaVo.getParam("FirstDueDate")));
		tLoanBorMain.setFirstAdjRateDate(this.parse.stringToInteger(titaVo.getParam("FirstAdjRateDate")));
		tLoanBorMain.setAcctFee(this.parse.stringToBigDecimal(titaVo.getParam("TimAcctFee")));
		tLoanBorMain.setFinalBal(this.parse.stringToBigDecimal(titaVo.getParam("TimFinalBal")));
		tLoanBorMain.setNotYetFlag(titaVo.getParam("NotYetFlag"));
		tLoanBorMain.setRenewFlag(titaVo.getParam("RenewFlag"));
		tLoanBorMain.setPieceCode(titaVo.getParam("PieceCode"));
		tLoanBorMain.setPieceCodeSecond(titaVo.getParam("PieceCodeSecond"));
		tLoanBorMain.setPieceCodeSecondAmt(parse.stringToBigDecimal(titaVo.getParam("TimPieceCodeSecondAmt")));
		tLoanBorMain.setUsageCode(titaVo.getParam("UsageCode"));
		tLoanBorMain.setSyndNo(this.parse.stringToInteger(titaVo.getParam("SyndNo")));
		tLoanBorMain.setRelationCode(tempVo.getParam("RelationCode"));
		tLoanBorMain.setRelationName(tempVo.getParam("RelationName"));
		tLoanBorMain.setRelationId(tempVo.getParam("RelationId"));
		tLoanBorMain.setRelationBirthday(parse.stringToInteger(tempVo.getParam("RelationBirthday")));
		tLoanBorMain.setRelationGender(tempVo.getParam("RelationGender"));
		tLoanBorMain.setActFg(titaVo.getActFgI());
		tLoanBorMain.setLastEntDy(titaVo.getEntDyI());
		tLoanBorMain.setLastKinbr(titaVo.getKinbr());
		tLoanBorMain.setLastTlrNo(titaVo.getTlrNo());
		tLoanBorMain.setLastTxtNo(titaVo.getTxtNo());
		tLoanBorMain.setRemitBank(titaVo.getParam("RemitBank"));
		tLoanBorMain.setRemitBranch(titaVo.getParam("RemitBranch"));
		tLoanBorMain.setRemitAcctNo(this.parse.stringToBigDecimal(titaVo.getParam("RemitAcctNo")));
		tLoanBorMain.setCompensateAcct(titaVo.getParam("CompensateAcct"));
		tLoanBorMain.setRemark(titaVo.getParam("Remark"));
		tLoanBorMain.setAcDate(wkTbsDy);
		tLoanBorMain.setNextAcDate(0);
		tLoanBorMain
				.setLastUpdate(parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
		tLoanBorMain.setLastUpdateEmpNo(titaVo.getTlrNo());
		// 計算寬限期數
		wGracePeriod = loanCom.getGracePeriod(tLoanBorMain.getAmortizedCode(), tLoanBorMain.getFreqBase(),
				tLoanBorMain.getPayIntFreq(), tLoanBorMain.getSpecificDate(), tLoanBorMain.getSpecificDd(),
				tLoanBorMain.getGraceDate());
		tLoanBorMain.setGracePeriod(wGracePeriod);
		// 計算期金
		wkDueAmt = loanDueAmtCom.getDueAmt(tLoanBorMain.getDrawdownAmt(), tLoanBorMain.getApproveRate(),
				tLoanBorMain.getAmortizedCode(), tLoanBorMain.getFreqBase(), tLoanBorMain.getTotalPeriod(),
				tLoanBorMain.getGracePeriod(), tLoanBorMain.getPayIntFreq(), tLoanBorMain.getFinalBal(), titaVo);
		tLoanBorMain.setDueAmt(wkDueAmt);
		// 下次還本日
		if (wGracePeriod > 0) {
			// 用期數計算
			wkNextRepayDate = loanCom.getPayIntEndDate(tLoanBorMain.getFreqBase(), tLoanBorMain.getRepayFreq(),
					tLoanBorMain.getSpecificDate(), tLoanBorMain.getSpecificDd(),
					wGracePeriod + tLoanBorMain.getRepayFreq(), tLoanBorMain.getMaturityDate());
			tLoanBorMain.setNextRepayDate(wkNextRepayDate);
		}
	}

	private void SetLoanBorTx() throws LogicException {
		this.info("SetLoanBorTx ...");

		tLoanBorTx.setTxTypeCode(0); // 0: 臨櫃交易 1: 批次交易
		tLoanBorTx.setDesc("預約撥款");
		tLoanBorTx.setAcDate(wkTbsDy);
		tLoanBorTx.setDisplayflag("Y");
		tLoanBorTx.setEntryDate(tLoanBorMain.getDrawdownDate());
		tLoanBorTx.setTxAmt(this.parse.stringToBigDecimal(titaVo.getTxAmt()));
		tLoanBorTx.setLoanBal(iDrawdownAmt);
		tLoanBorTx.setRate(tLoanBorMain.getStoreRate());
		// 其他欄位
		tTempVo.clear();
		tTempVo.putParam("RemitBank", titaVo.getParam("RemitBank") + titaVo.getParam("RemitBranch"));
		tTempVo.putParam("RemitAcctNo", titaVo.getParam("RemitAcctNo"));
		tTempVo.putParam("CompensateFlag", iCompensateFlag);
		tLoanBorTx.setOtherFields(tTempVo.getJsonString());
	}
}