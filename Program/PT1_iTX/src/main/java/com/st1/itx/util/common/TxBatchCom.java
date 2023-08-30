package com.st1.itx.util.common;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AcDetail;
import com.st1.itx.db.domain.BankDeductDtl;
import com.st1.itx.db.domain.BankRmtf;
import com.st1.itx.db.domain.BankRmtfId;
import com.st1.itx.db.domain.BatxDetail;
import com.st1.itx.db.domain.BatxDetailId;
import com.st1.itx.db.domain.BatxHead;
import com.st1.itx.db.domain.BatxHeadId;
import com.st1.itx.db.domain.BatxOthers;
import com.st1.itx.db.domain.BatxOthersId;
import com.st1.itx.db.domain.EmpDeductDtl;
import com.st1.itx.db.domain.FacClose;
import com.st1.itx.db.domain.LoanBook;
import com.st1.itx.db.domain.LoanCheque;
import com.st1.itx.db.domain.LoanChequeId;
import com.st1.itx.db.domain.LoanCustRmk;
import com.st1.itx.db.domain.NegMain;
import com.st1.itx.db.domain.TxErrCode;
import com.st1.itx.db.domain.TxRecord;
import com.st1.itx.db.domain.TxRecordId;
import com.st1.itx.db.service.BankDeductDtlService;
import com.st1.itx.db.service.BankRmtfService;
import com.st1.itx.db.service.BatxDetailService;
import com.st1.itx.db.service.BatxHeadService;
import com.st1.itx.db.service.BatxOthersService;
import com.st1.itx.db.service.EmpDeductDtlService;
import com.st1.itx.db.service.FacCloseService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.LoanBookService;
import com.st1.itx.db.service.LoanChequeService;
import com.st1.itx.db.service.LoanCustRmkService;
import com.st1.itx.db.service.NegMainService;
import com.st1.itx.db.service.TxErrCodeService;
import com.st1.itx.db.service.TxRecordService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.MySpring;
import com.st1.itx.util.common.data.BaTxVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;
//
// txCheck 交易檢核功能：
//  1.設定初值(tempVo的初值為處理說明欄的原始值)
//  2.執行 AML 交易檢核(匯款轉帳及支票兌現) 
//    檢核狀態 0.非可疑名單/已完成名單確認 1.需審查/確認 2.為凍結名單/未確定名單
//    如為 1,2，需再讀取AmlLog檔的最新確認狀態
//    匯款轉帳： 1.借款人 2.交易人
//    支票兌現： 1.借款人 2.發票人
//  3.預設匯款轉帳還款類別 
//    1. 債協匯入款:(A6) ==> 11-債協匯入款
//    2. 期款(A2):95102 ==> 01-期款
//    3. 有約定還本  ==> 還款類別:02-部分償還 
//       條件：匯入金額>=約定金額、入帳日期與約定日期相同 
//    4. 有清償作業  ==> 還款類別:03-結案
//條件：匯入金額>=約定金額、入帳日期與約定日期相同 
//    企金戶 (A1):95101 ==> <預設還款類別>，處理狀態 : 2.人工處理
//    個人戶期款(A2):95102 ==> 最後一期(還款後本金餘額 =0 )=> 還款類別: 03-結案 ---->
//    個人戶還本(A3):95103 ==> 
//      1.與約定還本金額相同 ==> 還款類別:02-部分償還
//        (if LoanBook.BookAmt >= txamt and status = 0: 未回收 & entryDate = LoanBook.BookDate)
//      2.執行過清償作業     ==> 還款類別:03-結案  (check FacClose exist)
//    債協匯入款:95105(A6)     ==> 11-債協匯入款
//企金戶 (A1):95101 ==> <預設還款類別>，處理狀態 : 2.人工處理
//  4.執行債協匯入款戶號檢核
//  5.執行應繳試算，依結果重設還款類別，再依還款類別進行檢核
//    step01. 應繳試算
//    step02. 匯款轉帳未設定還款類別，與未收費用金額相同，則設定為費用類別
//    step03. 01-期款，最後一期，還款類別為03-結案
//    step04. 依還款類別檢核
//        case 00 :
//             處理狀態:3.檢核錯誤
//             處理說明:請變更(還款類別)後重新檢核
//        case 01-期款:
//           1.回收金額 + 暫收可抵繳 - 全部應繳 < 期金
//             處理狀態:2.人工處理
//             處理說明:<不足利息>,<積欠期款> 999999 
//           2.回收金額  > 全部應繳 
//             處理狀態:4.檢核正常
//             處理說明:有溢繳款:999999
//        case 02-部分償還 :
//           1.有清償違約金 
//             處理狀態:2.人工處理
//             處理說明:有清償違約金:999999 		      
//           2.暫收可抵繳 < 未繳費用
//             處理狀態:2.人工處理
//             處理說明:尚有未繳費用:999999 		      
//        case 03-結案 :
//           1.回收金額 + 暫收可抵繳 - 全部應繳 < 結案金額(不足金額)
//             處理狀態:2.人工處理
//             處理說明:<金額不足>999999 	
//           2.提前結案需先執行 L2631-清償作業
//             處理狀態:2.人工處理
//             處理說明:提前結案請先執行 L2631-清償作業(L2077)
//           3.回收金額 + 暫收可抵繳 - 全部應繳 > 結案金額
//             處理狀態:2.人工處理
//             處理說明: 結案有溢繳款：999999
//        case 04-帳管費 05-火險費 06-契變手續費 07-法務費 
//           1.有回收該還款類別費用
//             處理狀態:4.檢核正常
//           2.無該還款類別未繳費用
//             處理狀態:2.人工處理
//             處理說明: 無該還款類別未繳費用
//           3.金額不足
//             處理狀態:2.人工處理
//             處理說明: ：金額不足: 999999
//        case 09-其他
//           1.清償違約金
//             處理狀態:4.檢核正常
//             處理說明:戶況說明 
//           2.催呆戶
//             處理狀態:4.檢核正常
//             處理說明:戶況說明 
//           3.結案戶
//             處理狀態:2.人工處理
//             處理說明:戶況說明 
//              
//         處理說明之金額欄：
//           1.償還本利:  短繳利息:  短繳本金:  清償違約金:
//           2.帳管費: 火險費:  契變手續費:  法務費: 未到期火險費用:
//           3.暫收可抵繳:其他額度暫收可抵繳:暫收抵繳:
//
//
//  6.將檢核結果存入整批入帳明細檔(還款類別、處理說明、處理狀態) 
//

/**
 * 整批入帳處理<BR>
 * 1.TxBatchCom.run 人工或批次入帳後，將交易結果更新整批入帳檔 call by ApControl<BR>
 * 1.1 匯款轉帳入帳更新匯款轉帳檔BankRmtf會計日<BR>
 * 1.2 銀行扣款入帳更新BankDeductDtl銀扣檔會計日<BR>
 * 1.3 員工扣薪更新EmpDeductDtl員工扣薪款檔會計日<BR>
 * 1.4 支票兌現入帳更新支票檔LoanCheque<BR>
 * 
 * 2.TxBatchCom.txCheck 交易檢核 call by 整批檢核(L420ABatch、L420BBatch)<BR>
 * 3.TxBatchCom.txTita 組入帳交易電文call by 整批入帳(L420BBatch)、整批入帳轉暫收(L420C)<BR>
 * 3.1 正常交易，組交易電文 3.2 訂正交易，讀取TxRecord 交易記錄檔，組原電文
 * 
 * @author st1
 *
 */
@Component("txBatchCom")
@Scope("prototype")
public class TxBatchCom extends TradeBuffer {

	@Autowired
	public Parse parse;
	@Autowired
	DateUtil dDateUtil;
	@Autowired
	public BaTxCom baTxCom;
	@Autowired
	public AcNegCom acNegCom;
	@Autowired
	public TxAmlCom txAmlCom;
	@Autowired
	LoanCom loanCom;
	@Autowired
	private LoanAvailableAmt loanAvailableAmt;

	@Autowired
	public LoanBookService loanBookService;
	@Autowired
	public FacCloseService facCloseService;
	@Autowired
	public FacMainService facMainService;
	@Autowired
	public BatxDetailService batxDetailService;
	@Autowired
	public BatxHeadService batxHeadService;
	@Autowired
	public BankRmtfService bankRmtfService;
	@Autowired
	public BankDeductDtlService bankDeductDtlService;
	@Autowired
	public EmpDeductDtlService empDeductDtlService;
	@Autowired
	public LoanChequeService loanChequeService;
	@Autowired
	public BatxOthersService batxOthersService;
	@Autowired
	public TxRecordService txRecordService;
	@Autowired
	public TxErrCodeService txErrCodeService;
	@Autowired
	public LoanCustRmkService loanCustRmkService;
	@Autowired
	public NegMainService negMainService;

	private ArrayList<BaTxVo> baTxList;

//  全部應繳
	private BigDecimal unPayTotal = BigDecimal.ZERO;
//  應繳費用 
	private BigDecimal unPayFee = BigDecimal.ZERO;
//	應繳本利 
	private BigDecimal unPayLoan = BigDecimal.ZERO;

//  還款總金額
	private BigDecimal repayTotal = BigDecimal.ZERO;
//	償還本利 
	private BigDecimal repayLoan = BigDecimal.ZERO;
//  本金 
	private BigDecimal principal = BigDecimal.ZERO;
//  利息 
	private BigDecimal interest = BigDecimal.ZERO;
//  延滯息
	private BigDecimal delayInt = BigDecimal.ZERO;
//  違約金 
	private BigDecimal breachAmt = BigDecimal.ZERO;
//  清償違約金
	private BigDecimal closeBreachAmt = BigDecimal.ZERO;
//  回收費用
	private BigDecimal repayFee = BigDecimal.ZERO;
//  未收還款類別費用
	private BigDecimal unPayRepayTypeFee = BigDecimal.ZERO;
//  費用
	private BigDecimal acctFee = BigDecimal.ZERO;
	private BigDecimal modifyFee = BigDecimal.ZERO;
	private BigDecimal fireFee = BigDecimal.ZERO;
	private int fireFeeDate = 0;
	private BigDecimal lawFee = BigDecimal.ZERO;
	private BigDecimal shortfallInt = BigDecimal.ZERO;
	private BigDecimal shortfallPrin = BigDecimal.ZERO;
	private BigDecimal shortCloseBreach = BigDecimal.ZERO;
//  另收欠款= 6 未到期火險費用
	private BigDecimal unOpenfireFee = BigDecimal.ZERO;
//  當期未收火險費用
	private BigDecimal unSettlefireFee = BigDecimal.ZERO;
//	暫收可抵繳  
	private BigDecimal tavAmt = BigDecimal.ZERO;
//	暫收抵繳
	private BigDecimal tmpAmt = BigDecimal.ZERO;
//  短繳金額
	private BigDecimal shortAmt = BigDecimal.ZERO;
//  溢繳金額 
	private BigDecimal overAmt = BigDecimal.ZERO;

//	其他額度暫收可抵繳  
	private BigDecimal otrTavAmt = BigDecimal.ZERO;
//  放款餘額(還款前)	
	private BigDecimal loanBal = BigDecimal.ZERO;

//  還款類別 
	private int repayType = 0;

//  還款額度 
	private int repayFacmNo = 0;

//  還款撥款序號 
	private int repayBormNo = 0;

//  溢繳款額度 
	private int overRpFacmNo = 0;

//  應繳息日
	private int repayIntDate = 0;

//  計息起日
	private int intStartDate = 0;
//  計息迄日
	private int intEndDate = 0;
//  上次繳息日
	private int prevPayintDate = 0;

//  額度還款應繳日
	private String repayIntDateByFacmNoVo = null;

//	本息計算表
	private TempVo intListTempVo = new TempVo();
//	溢短收記號
	private int overRpFg = 0; // 1->溢短收記號 1->短收 2->溢收 3->溢收(整批入帳、部分繳款)

//  戶況
	private int facStatus = 0;

//  AML	檢核 0-正常 1-錯誤
	private int amlRsp = 0;

//  結案記號  1.正常結案  2.提前結案 
	private int closeFg = 0;

//  檢核訊息
	private String checkMsg = null;

//  錯誤訊息
	private String errorMsg = null;

//  處理狀態
	private String procStsCode = null;

// 暫收指定額度	
	private String tmpFacmNoX = "";

//  TempVo
	private TempVo tTempVo = new TempVo();

//  other
	private TempVo otherTempVo = new TempVo();

	private DecimalFormat df = new DecimalFormat("##,###,###,###,##0");

	@PostConstruct
	public void init() {
		this.unPayFee = BigDecimal.ZERO;
		this.acctFee = BigDecimal.ZERO;
		this.modifyFee = BigDecimal.ZERO;
		this.fireFee = BigDecimal.ZERO;
		this.fireFeeDate = 0;
		this.lawFee = BigDecimal.ZERO;
		this.shortfallInt = BigDecimal.ZERO;
		this.shortfallPrin = BigDecimal.ZERO;
		this.shortCloseBreach = BigDecimal.ZERO;
		this.unPayLoan = BigDecimal.ZERO;
		this.repayLoan = BigDecimal.ZERO;
		this.principal = BigDecimal.ZERO;
		this.interest = BigDecimal.ZERO;
		this.delayInt = BigDecimal.ZERO;
		this.breachAmt = BigDecimal.ZERO;
		this.closeBreachAmt = BigDecimal.ZERO;
		this.unPayTotal = BigDecimal.ZERO;
		this.loanBal = BigDecimal.ZERO;
		this.tavAmt = BigDecimal.ZERO;
		this.tmpAmt = BigDecimal.ZERO;
		this.otrTavAmt = BigDecimal.ZERO;
		this.shortAmt = BigDecimal.ZERO;
		this.overAmt = BigDecimal.ZERO;
		this.repayFee = BigDecimal.ZERO;
		this.unPayRepayTypeFee = BigDecimal.ZERO;
		this.unOpenfireFee = BigDecimal.ZERO;
		this.unSettlefireFee = BigDecimal.ZERO;
		this.repayTotal = BigDecimal.ZERO;
		this.amlRsp = 0;
		this.repayFacmNo = 0;
		this.repayBormNo = 0;
		this.overRpFacmNo = 0;
		this.repayType = 0;
		this.repayIntDate = 0;
		this.repayIntDateByFacmNoVo = null;
		this.intStartDate = 0;
		this.intEndDate = 0;
		this.prevPayintDate = 0;
		this.facStatus = 0;
		this.closeFg = 0;
		this.checkMsg = "";
		this.errorMsg = "";
		this.procStsCode = "";
		this.tmpFacmNoX = "";
		this.tTempVo = new TempVo();
		this.otherTempVo = new TempVo();
		this.intListTempVo = new TempVo();
	}

	@Override

	/**
	 * 入帳交易執行後，更新整批入帳檔明細處理狀態
	 */
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("TxBatchCom run ... " + titaVo);
		// 以批號、明細檔序號更新整批入帳檔
		if ("L32".equals(titaVo.getTxCode().substring(0, 3)) || "L34".equals(titaVo.getTxCode().substring(0, 3))) {
			// 訂正AS400帳務，產生暫收沖正
			if ("L3240".equals(titaVo.getTxCode()) || "L3250".equals(titaVo.getTxCode())) {
				insBatxResv(titaVo);
			}
			// 整批入帳
			if (titaVo.get("BATCHNO") != null && titaVo.get("BATCHNO").trim().length() == 6
					&& ("BATX".equals(titaVo.get("BATCHNO").substring(0, 4))
							|| "RESV".equals(titaVo.get("BATCHNO").substring(0, 4)))
					&& titaVo.get("RpDetailSeq1") != null) {
				// 隔日訂正，回沖至暫收款－沖正(RESV00)
				if (titaVo.isHcodeErase() && titaVo.getEntDyI() != titaVo.getOrgEntdyI()) {
					insBatxResv(titaVo);
				} else {
					updBatxResult(titaVo.getParam("BATCHNO"), titaVo.getParam("RpDetailSeq1"), titaVo);
				}
			}
		}
		return null;
	}

	/**
	 * 交易檢核
	 * 
	 * @param iRepayType 還款類別
	 * @param tDetail    整批入帳明細檔
	 * @param titaVo     TitaVo
	 * @return 整批入帳明細檔
	 * @throws LogicException ... LogicException
	 */
	public BatxDetail txCheck(int iRepayType, BatxDetail tDetail, TitaVo titaVo) throws LogicException {
		this.info("TxBatchCom txCheck .... iRepayType=" + iRepayType + ", BatxDetail=" + tDetail.toString());
		baTxCom.setTxBuffer(this.getTxBuffer());
		acNegCom.setTxBuffer(this.getTxBuffer());
		loanCom.setTxBuffer(this.txBuffer);

// RepayCode 還款來源 01.匯款轉帳 02.銀行扣款 03.員工扣款 04.支票兌現
//                    05.法院扣薪 06.理賠金 07.代收款-債權協商 09.其他 11.匯款轉帳預先作業 90.暫收抵繳
// this.procStsCode 處理狀態 0.未檢核 1.不處理 2.人工處理 3.檢核錯誤 4.檢核正常 5.人工入帳 6.批次入帳 7.轉暫收
// this.repayType 還款類別 1.期款 2.部分償還 3.結案 4.帳管費 5.火險費 6.契變手續費 7.法務費 9.其他 11.債協匯入款(虛擬帳號為9510500NNNNNNN)
		// ---------------------------- 設定初值 -------------------------------
		init();
		this.procStsCode = "0";
		if (iRepayType == 0)
			this.repayType = tDetail.getRepayType();
		else
			this.repayType = iRepayType;

		this.repayFacmNo = tDetail.getFacmNo();

		// tempVo的初值為處理說明欄的原始值
		TempVo tempVo = new TempVo();
		initialProcNote(tDetail, tempVo.getVo(tDetail.getProcNote()), titaVo);
		this.otherTempVo = new TempVo();

		// --------------- 入帳日期檢核 ---------------
		if (tDetail.getEntryDate() > this.txBuffer.getTxBizDate().getTbsDy()) {
			this.checkMsg += "入帳日" + tDetail.getEntryDate() + " 大於會計日" + this.txBuffer.getTxBizDate().getTbsDy();
			this.procStsCode = "3"; // 3.檢核錯誤
		}

		// --------------- 執行 AML 交易檢核(匯款轉帳及支票兌現) ------------------
		// 檢核狀態 0.非可疑名單/已完成名單確認 1.需審查/確認 2.為凍結名單/未確定名單
		// 如為 1,2，需再讀取AmlLog檔的最新確認狀態
		// 匯款轉帳： 1.借款人 2.交易人
		// 支票兌現： 1.借款人 2.發票人
		if ("0".equals(this.procStsCode)) {
			if (tDetail.getRepayCode() == 1 || tDetail.getRepayCode() == 4) {
				if ("1".equals(this.tTempVo.getParam("AmlRsp1")) || "1".equals(this.tTempVo.getParam("AmlRsp2"))
						|| "2".equals(this.tTempVo.getParam("AmlRsp1"))
						|| "2".equals(this.tTempVo.getParam("AmlRsp2"))) {
					txAmlCom.setTxBuffer(this.getTxBuffer());
					this.tTempVo = txAmlCom.batxCheck(this.tTempVo, tDetail, titaVo);
					if ("2".equals(this.tTempVo.getParam("AmlRsp1")) || "2".equals(this.tTempVo.getParam("AmlRsp2"))) {
						this.checkMsg += " AML姓名檢核：為凍結名單/未確定名單 ";
						this.amlRsp = 2;
					} else if ("1".equals(this.tTempVo.getParam("AmlRsp1"))
							|| "1".equals(this.tTempVo.getParam("AmlRsp2"))) {
						this.checkMsg += " AML姓名檢核：需審查/確認 ";
						this.amlRsp = 1;
					}
				}
			}
		}
		// ------------- 設定 AML 檢核結果 ----------------------
		// AML 檢核 0-正常 1-錯誤
		if (this.amlRsp > 0) {
			this.procStsCode = "3"; // 3.檢核錯誤
		}

		// ---- 執行債一般債權匯入款A7 債權撥付檔檢核 ------------------
		// 如有MATCH到一般債權撥付檔,則顯示一般債權客戶戶號,若沒有,則顯示[不符一般債權撥付檔]
		if ("A7".equals(tDetail.getReconCode())) {
			List<AcDetail> lAcDetail = acNegCom.getNegAppr02CustNo(tDetail.getEntryDate(), tDetail.getRepayAmt(),
					tDetail.getCustNo(), titaVo);
			if (lAcDetail == null || lAcDetail.size() == 0) {
				this.checkMsg += " 不符一般債權撥付檔";
				this.repayType = 9; // 暫收
				this.procStsCode = "4"; // 4.檢核正常
			} else {
				String custNoX = "";
				for (AcDetail ac : lAcDetail) {
					custNoX += " " + ac.getCustNo();
				}
				this.checkMsg += " 一般債權客戶戶號:" + custNoX;
			}
		}

		// --------------- 執行債協匯入款戶號檢核 ------------------
		if ("0".equals(this.procStsCode) && this.repayType == 11) {
			if (acNegCom.isNegCustNo(tDetail.getCustNo(), titaVo)) {
				this.procStsCode = "4"; // 4.檢核正常
			} else {
				this.checkMsg += " 非債協戶或債協戶非正常戶";
				this.procStsCode = "3"; // 3.檢核錯誤
			}
		}

		// --------------- 執行債一般債權客戶檢核 ------------------
		// 匯款轉帳若為一般債權客戶,整批檢核訊息提示債協的案件種類且需人工入帳,A6與A7的還款類別為債協匯入款,其他的依原預設還款類別
		// USER需以L3210自行輸入暫收原因或以整批入帳之維護交易維護
		if ("0".equals(this.procStsCode) && tDetail.getRepayCode() == 1 && this.repayType != 11) {
			NegMain tNegMain = negMainService.statusFirst("0", tDetail.getCustNo(), titaVo); // 0-正常
			if (tNegMain != null && "N".equals(tNegMain.getIsMainFin())) {
				this.checkMsg += loanCom.getCdCodeX("CaseKindCode", tNegMain.getCaseKindCode(), titaVo) + "案件";
				this.procStsCode = "2"; // 2.人工處理
			}
		}
		this.info("TxBatchCom txCheck " + this.procStsCode + "," + this.repayType);

		BigDecimal checkAmt = tDetail.getRepayAmt();
		if (this.tTempVo.get("MergeAmt") != null) {
			checkAmt = parse.stringToBigDecimal(this.tTempVo.get("MergeAmt"));
		}

		// --------------- 預設匯款轉帳還款類別 ------------------
		if ("0".equals(this.procStsCode) && this.repayType == 0) {
			if (tDetail.getRepayCode() == 1) {
				bankRmtRepayType(tDetail, checkAmt, titaVo);
			}
		}

		// --------------- 執行債協匯入款戶號檢核 ------------------
		if ("0".equals(this.procStsCode) && this.repayType == 11) {
			if (acNegCom.isNegCustNo(tDetail.getCustNo(), titaVo)) {
				this.procStsCode = "4"; // 4.檢核正常
			} else {
				this.checkMsg += " 非債協戶或債協戶非正常戶";
				this.procStsCode = "3"; // 3.檢核錯誤
			}
		}

		// --------------- 結案清償作業設定 ------------------
		if ("0".equals(this.procStsCode) && tDetail.getRepayType() == 3) {
			facCloseRepayType(tDetail, checkAmt, titaVo);
		}
		// --------------- 部分償還作業設定 ------------------
		if ("0".equals(this.procStsCode) && tDetail.getRepayType() == 2) {
			loanBookRepayType(tDetail, checkAmt, titaVo);
		}

		// ------------- 執行應繳試算，依結果重設還款類別，再依還款類別進行檢核 ----------------------
		if ("0".equals(this.procStsCode)) {
			// 應繳試算
			settingUnPaid(tDetail, titaVo);
			// 還款額度
			if (this.repayFacmNo > 0 && tDetail.getFacmNo() == 0) {
				tDetail.setFacmNo(this.repayFacmNo);
			}
			// step01. 01-期款，最後一期，還款類別為03-結案
			if ("0".equals(this.procStsCode) && this.repayType == 1 && this.closeFg > 0) {
				this.repayType = 3;
			}

			// step02. 依還款類別檢核
			if ("0".equals(this.procStsCode)) {
				settingprocStsCode(tDetail, titaVo);
			}
		}

		// ------------- 將檢核結果存入整批入帳明細檔(還款類別、處理說明、處理狀態) -----------
		// 檢核正常
		if ("0".equals(this.procStsCode)) {
			this.procStsCode = "4"; // 4.檢核正常
		}

		// 設定處理說明
		settingProcNote(tDetail, titaVo);

		this.info("this.tTempVo.getJsonString=" + this.tTempVo.getJsonString());
		tDetail.setProcNote(this.tTempVo.getJsonString());
		tDetail.setOtherNote(this.otherTempVo.getJsonString());

		tDetail.setRepayType(this.repayType);
		// 還款額度
		tDetail.setRepayType(this.repayType);
		// 還款類別
		tDetail.setRepayType(this.repayType);
		// 處理狀態
		tDetail.setProcStsCode(this.procStsCode);
		// 還款總金額
		tDetail.setAcquiredAmt(this.repayTotal);

		// end
		return tDetail;

	}

	// 催呆、結案戶轉暫收
	public void checkFacStatus(TitaVo titaVo) throws LogicException {

		this.checkMsg += loanCom.getCdCodeX("Status", parse.IntegerToString(this.facStatus, 2), titaVo);
		// 催呆戶、結案戶須轉暫收，有可還費用整批檢核時需人工處理
		this.repayType = 9;
		if ("L420A".equals(titaVo.getTxcd()) && this.repayFee.compareTo(BigDecimal.ZERO) > 0) {
			this.procStsCode = "2"; // 2.人工處理
		} else {
			this.procStsCode = "4"; // 4.檢核正常
		}
	}

	/**
	 * 組入帳交易電文
	 * 
	 * @param functionCode 0:入帳 1:訂正 2.轉暫收
	 * @param tDetail      整批入帳明細檔
	 * @param iTotalcnt    總筆數
	 * @param titaVo       TitaVo
	 * @return TitaVo
	 * @throws LogicException ... LogicException
	 */
	public TitaVo txTita(int functionCode, BatxDetail tDetail, int iTotalcnt, TitaVo titaVo) throws LogicException {
		this.info("TxBatchCom txTita .... functionCode=" + functionCode + tDetail.toString());
// 功能 0:入帳 1:訂正 2.轉暫收
		int hcode = 0;
		if (functionCode == 1) {
			hcode = 1;
		}
		// TempVo
		this.tTempVo = new TempVo();
		this.tTempVo = this.tTempVo.getVo(tDetail.getProcNote());
		this.otherTempVo = new TempVo();
		this.otherTempVo = this.otherTempVo.getVo(tDetail.getOtherNote());
// TXTNO = BatchNo[2] + eraseNo(1) + TxtSeq [5] 
// 整批訂正只會有一次( 整批訂正會自動刪除、單筆需人工訂正)
// BatchNo:Batx09, TotalCnt: 32321 
//		        EraseCnt   eraseNo       TxtNo                 
// Normal          0           0        09000001~09032321    
// Erase           0           1        09100001~09132321                
		int eraseNo = 0;
		int eraseCnt = 0;
		if (this.tTempVo.get("EraseCnt") != null) {
			eraseCnt = parse.stringToInteger(tTempVo.get("EraseCnt"));
		}
		if (hcode == 0) {
			eraseNo = eraseCnt * 2;
		} else {
			eraseNo = eraseCnt * 2 + 1;
		}
		String txtNo = tDetail.getBatchNo().substring(4, 6)
				+ parse.IntegerToString(tDetail.getDetailSeq() + eraseNo * 100000, 6);

		TitaVo txTitaVo = new TitaVo();
		// 正常交易，組交易電文
		if (hcode == 0) {
			txTitaVo = (TitaVo) titaVo.clone();
			txTitaVo.putParam("TXTNO", txtNo);
			txTitaVo = isTxCode(functionCode, tDetail, txTitaVo);
			titaVo = txTitaVo;
		} else {
			// 訂正交易，讀取TxRecord 交易記錄檔，組原電文
			TxRecordId tTxRecordId = new TxRecordId();
			tTxRecordId.setEntdy(this.txBuffer.getTxCom().getTbsdyf());
			tTxRecordId.setTxNo(titaVo.getKinbr() + tDetail.getTitaTlrNo() + tDetail.getTitaTxtNo());
			TxRecord tTxRecord = txRecordService.findById(tTxRecordId, titaVo);
			if (tTxRecord == null)
				throw new LogicException("E0006", "txExecute 交易記錄檔(訂正)"); // E0006 鎖定資料時，發生錯誤
			try {
				txTitaVo = txTitaVo.getVo(tTxRecord.getTranData());
			} catch (IOException e) {
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				this.error(errors.toString());
				throw new LogicException("E0013", "txExecute 轉換交易記錄檔電文"); // E0013 程式邏輯有誤
			}
			txTitaVo.putParam("TLRNO", titaVo.getParam("TLRNO"));
			txTitaVo.putParam("TXTNO", txtNo);
			txTitaVo.putParam("ORGTLR", tDetail.getTitaTlrNo()); // 原經辦
			txTitaVo.putParam("ORGTNO", tDetail.getTitaTxtNo()); // 原交易序號
			txTitaVo.putParam("HCODE", "1"); // 訂正交易
			txTitaVo.putParam("EMPNOS", titaVo.getParam("EMPNOS")); // 主管
			txTitaVo.putParam("EMPNOT", titaVo.getParam("EMPNOT")); // 經辦
			txTitaVo.putParam("SUPNO", titaVo.getParam("SUPNO")); // 主管授權
			txTitaVo.putParam("RQSP", titaVo.getParam("RQSP")); // 主管授權理由
		}

		return txTitaVo;

	}

	/* 組交易電文 */
	private TitaVo isTxCode(int functionCode, BatxDetail tDetail, TitaVo txTitaVo) throws LogicException {
// RepayType 還款類別 1.期款 2.部分償還 3.結案 4.帳管費 5.火險費 6.契變手續費 7.法務費 9.其他 11.債協匯入款 
//				L3210 暫收款登錄	4.帳管費 5.火險費 6.契變手續費 7.法務費 9.其他 11.債協匯入款 
//				L3420 結案登錄     3.結案         
//				L3200 回收登錄     1.期款 2.部分償還 
		// 交易序號 : 批號後兩碼(2)+ 訂正次數 * 2 + 明細序號(4)

		// 櫃台機種類 : 批次
		txTitaVo.putParam("TRMTYP", "BA");

		// 整批批號、整批明細序號
		txTitaVo.putParam("BATCHNO", tDetail.getBatchNo());
		txTitaVo.putParam("BATCHSEQ", parse.IntegerToString(tDetail.getDetailSeq(), 6));

		// 彙總傳票

		// 交易戶號
		String MRKEY = parse.IntegerToString(tDetail.getCustNo(), 7);
		if (tDetail.getFacmNo() > 0) {
			MRKEY = MRKEY + "-" + parse.IntegerToString(tDetail.getFacmNo(), 3) + "    ";
		} else {
			MRKEY = MRKEY + "    " + "    ";
		}
		txTitaVo.putParam("MRKEY", MRKEY);

		txTitaVo.putParam("CURBM", "TWD");

		// 交易金額
		txTitaVo.putParam("TXAMT", tDetail.getRepayAmt());

		txTitaVo.putParam("HCODE", "0"); // 正常交易
		txTitaVo.putParam("RELCD", "1"); // 一段式
		txTitaVo.putParam("ACTFG", "0"); // 登錄
		txTitaVo.putParam("CRDB", "2"); // 借貸別 2.貸
		txTitaVo.putParam("SECNO", "09"); // 業務別 09-放款

		// 還款額度
		if (this.tTempVo.get("RepayFacmNo") != null) {
			this.repayFacmNo = parse.stringToInteger(this.tTempVo.get("RepayFacmNo"));
		} else {
			this.repayFacmNo = tDetail.getFacmNo();
		}
		// 組L3200、L3210、L3420 交易電文
		if (this.tTempVo.get("RepayType") != null) {
			this.repayType = parse.stringToInteger(this.tTempVo.get("RepayType"));
		} else {
			this.repayType = tDetail.getRepayType();
		}

		// 轉暫收
		if (functionCode == 2) {
			txTitaVo = setL3210Tita(functionCode, txTitaVo, tDetail);
			txTitaVo.putParam("FunctionCode", functionCode);
		}
		// 1:期款 2:部分償還 10:清償違約金 12:催收收回
		else if (this.repayType == 1 || this.repayType == 2 || this.repayType == 10 || this.repayType == 12) {
			txTitaVo = setL3200Tita(txTitaVo, tDetail);
		}
		// 3:結案
		else if (this.repayType == 3) {
			txTitaVo = setL3420Tita(txTitaVo, tDetail);
		}
		// 暫收
		else {
			txTitaVo = setL3210Tita(functionCode, txTitaVo, tDetail);
		}

		// 支票兌現
		int iChequeAcct = 0;
		int iChequeNo = 0;
		// 030102806 0760500
		// 01234567890123456
		if (tDetail.getRepayCode() == 4) {
			iChequeAcct = parse.stringToInteger(tDetail.getRvNo().substring(0, 9));
			iChequeNo = parse.stringToInteger(tDetail.getRvNo().substring(10, 17));
		}
		txTitaVo.putParam("ChequeAcct", iChequeAcct);
		txTitaVo.putParam("ChequeNo", iChequeNo);

		// 收付欄
		int ii = 1;
		txTitaVo.putParam("RpFlag", "1"); // 1:應收
		txTitaVo.putParam("RpType1", tDetail.getRepayType());
		if (tDetail.getRepayAmt().compareTo(BigDecimal.ZERO) > 0) {
			txTitaVo.putParam("RpCode1", tDetail.getRepayCode());
			txTitaVo.putParam("RpCodeX1",
					loanCom.getCdCodeX("BatchRepayCode", parse.IntegerToString(tDetail.getRepayCode(), 2), txTitaVo));
			txTitaVo.putParam("RpAmt1", tDetail.getRepayAmt());
			txTitaVo.putParam("RpAcctCode1", tDetail.getReconCode());
			txTitaVo.putParam("RpAcCode1", tDetail.getRepayAcCode());
			txTitaVo.putParam("RpCustNo1", tDetail.getCustNo());
			txTitaVo.putParam("RpFacmNo1", this.repayFacmNo);
			txTitaVo.putParam("RpDetailSeq1", tDetail.getDetailSeq());
			txTitaVo.putParam("RpEntryDate1", tDetail.getEntryDate());
			txTitaVo.putParam("RpRvno1", tDetail.getRvNo());
			txTitaVo.putParam("RpDscpt1", this.tTempVo.get("DscptCode")); // 摘要代碼
			txTitaVo.putParam("RpNote1", this.tTempVo.get("Note")); // 摘要 for 收付欄分錄之傳票摘要
			txTitaVo.putParam("RpRemark1", this.tTempVo.get("Remark")); // 備註 for 暫收款分錄(暫收款登錄)之傳票摘要
			ii++;
		}
		// 暫收抵繳欄
		String amt = null;
		for (int j = 1; j <= 50; j++) {
			amt = this.tTempVo.get("TmpAmt" + j);
			if (amt == null)
				break;
			txTitaVo.putParam("RpCode" + ii, "90");
			txTitaVo.putParam("RpAmt" + ii, amt);
			txTitaVo.putParam("RpFacmNo" + ii, this.tTempVo.get("TmpFacmNo" + j));
			this.info("RpAmt:" + amt);
			ii++;
		}
		// 短繳金額
		String shortAmt = null;
		// 溢繳金額
		String overAmt = null;
		if ("L3420".equals(txTitaVo.getTxcd()) || "L3200".equals(txTitaVo.getTxcd())) {
			// 本金、利息
			txTitaVo.putParam("TimPrincipal", this.tTempVo.get("Principal"));
			txTitaVo.putParam("TimInterest", this.tTempVo.get("Interest"));
			txTitaVo.putParam("TimDelayInt", this.tTempVo.get("DelayInt"));
			txTitaVo.putParam("TimBreachAmt", this.tTempVo.get("BreachAmt"));
			txTitaVo.putParam("TwPrincipal", this.tTempVo.get("Principal"));
			txTitaVo.putParam("TwInterest", this.tTempVo.get("Interest"));
			txTitaVo.putParam("TwDelayInt", this.tTempVo.get("DelayInt"));
			txTitaVo.putParam("TwBreachAmt", this.tTempVo.get("BreachAmt"));
			shortAmt = this.tTempVo.get("ShortAmt");
			overAmt = this.tTempVo.get("OverAmt");
		}
		// 短溢收
		if (shortAmt != null) {
			txTitaVo.putParam("OverRpFg", "1");
			txTitaVo.putParam("OverRpAmt", shortAmt);
		} else if (overAmt != null) {
			txTitaVo.putParam("OverRpFg", this.tTempVo.get("OverRpFg"));
			txTitaVo.putParam("OverRpAmt", overAmt);
		} else {
			txTitaVo.putParam("OverRpFg", "0");
			txTitaVo.putParam("OverRpAmt", BigDecimal.ZERO);
		}
		// 溢繳款額度
		if (this.tTempVo.get("OverRpFacmNo") != null) {
			txTitaVo.putParam("OverRpFacmNo", this.tTempVo.get("OverRpFacmNo"));
		} else {
			txTitaVo.putParam("OverRpFacmNo", parse.IntegerToString(this.repayFacmNo, 3));
		}
		// 其他費用欄
		if ("L3420".equals(txTitaVo.getTxcd())) {
			putAmt("AcctFee", "AcctFee1", txTitaVo);
			putAmt("ModifyFee", "ModifyFee1", txTitaVo);
			putAmt("FireFee", "FireFee1", txTitaVo);
			putAmt("LawFee", "LawFee1", txTitaVo);
			putAmt("ShortfallInt", "ShortfallInt", txTitaVo);
			putAmt("ShortfallPrin", "ShortfallPrin", txTitaVo);
			putAmt("ShortCloseBreach", "ShortCloseBreach", txTitaVo);
		} else if ("L3200".equals(txTitaVo.getTxcd())) {
			putAmt("AcctFee", "TimAcctFee", txTitaVo);
			putAmt("ModifyFee", "TimModifyFee", txTitaVo);
			putAmt("FireFee", "TimFireFee", txTitaVo);
			putAmt("LawFee", "TimLawFee", txTitaVo);
			putAmt("ShortfallInt", "TimShortfallInt", txTitaVo);
			putAmt("ShortfallPrin", "TimShortfallPrin", txTitaVo);
			putAmt("ShortCloseBreach", "TimShortCloseBreach", txTitaVo);
		} else {
			putAmt("AcctFee", "AcctFee", txTitaVo);
			putAmt("ModifyFee", "ModifyFee", txTitaVo);
			putAmt("FireFee", "FireFee", txTitaVo);
			putAmt("LawFee", "LawFee", txTitaVo);
		}

		// 本息計算表
		if ("L3200".equals(txTitaVo.getTxcd()) || "L3420".equals(txTitaVo.getTxcd())) {
			if (this.otherTempVo.get("IntListTempVo") != null) {
				this.intListTempVo = this.intListTempVo.getVo(this.otherTempVo.get("IntListTempVo"));
				for (int i = 1; i <= 20; i++) {
					if (this.intListTempVo.get("FacmBormNo" + i) != null) {
						txTitaVo.putParam("FacmBormNo" + i, this.intListTempVo.getParam("FacmBormNo" + i));
						txTitaVo.putParam("IntSEDate" + i, this.intListTempVo.getParam("IntSEDate" + i));
						txTitaVo.putParam("Principal" + i, this.intListTempVo.getParam("Principal" + i));
						txTitaVo.putParam("Interest" + i, this.intListTempVo.getParam("Interest" + i));
						txTitaVo.putParam("DelayInt" + i, this.intListTempVo.getParam("DelayInt" + i));
						txTitaVo.putParam("BreachAmt" + i, this.intListTempVo.getParam("BreachAmt" + i));
						txTitaVo.putParam("Total" + i, this.intListTempVo.getParam("Total" + i));
						txTitaVo.putParam("CloseBreachAmt" + i, this.intListTempVo.getParam("CloseBreachAmt" + i));
					}
				}
			}
		}
		// 檢核訊息
		txTitaVo.put("CheckMsg", tTempVo.getParam("CheckMsg"));

		return txTitaVo;
	}

	private void putAmt(String item, String itemName, TitaVo txTitaVo) {
		String amt = this.tTempVo.get(item);
		if (amt == null) {
			txTitaVo.putParam(itemName, BigDecimal.ZERO);
			txTitaVo.putParam("Tw" + item, BigDecimal.ZERO);
		} else {
			txTitaVo.putParam(itemName, amt);
			txTitaVo.putParam("Tw" + item, amt);
		}
	}

	/* L3210 暫收款登錄 */
	private TitaVo setL3210Tita(int functionCode, TitaVo l3210TitaVo, BatxDetail tBatxDetail) throws LogicException {
		int iReasonCode = 0;
//		ReasonCode	     RepayType	         RepayCode 
//
//		00債協暫收款	     11.債協匯入款 
//		01溢繳		     X	
//		02不足利息		 01.期款	           	  
//		03期票		     X	
//		04本金異動	     X	
//		05積欠期款		 01.期款		         02:銀行扣款 03:員工扣款( 03.結案) 
//		06即期票現金          X	
//		07火險、帳管	     04.帳管費、05.火險費 、06.契變手續費 、07.法務費
//		08兌現票入帳	 	                      04.支票兌現      
//		09其他		     09.其他	
//      10AML凍結／未確定 X

//      09 其他  同戶合併 轉暫收        
//      AmlRsp1 0.非可疑名單/已完成名單確認 1.需審查/確認 2.為凍結名單/未確定名單	
		if ("2".equals(this.tTempVo.getParam("AmlRsp1")) || "2".equals(this.tTempVo.getParam("AmlRsp2")))
			iReasonCode = 10;
		else if ("1".equals(this.tTempVo.getParam("AmlRsp1")) || "1".equals(this.tTempVo.getParam("AmlRsp2")))
			throw new LogicException(l3210TitaVo, "E0022", ""); // E0022 該筆資料需進行AML審查/確認
		else if (tBatxDetail.getRepayCode() == 4)
			iReasonCode = 8;
		else if (functionCode == 2 && !tTempVo.getParam("MergeSeq").equals(tTempVo.getParam("MergeCnt")))
			iReasonCode = 9;
		else if (tBatxDetail.getRepayType() == 11)
			iReasonCode = 0;
		else if (tBatxDetail.getCustNo() == this.txBuffer.getSystemParas().getNegDeptCustNo())
			iReasonCode = 0;
		else if (tBatxDetail.getRepayType() >= 4 && tBatxDetail.getRepayType() <= 7)
			iReasonCode = 7;
		else if (tBatxDetail.getRepayType() == 1 && "".equals(this.tTempVo.getParam("Principal")))
			iReasonCode = 2;
		else if (tBatxDetail.getRepayType() == 3
				&& (tBatxDetail.getRepayCode() == 2 || tBatxDetail.getRepayCode() == 3))
			iReasonCode = 2;
		else if (tBatxDetail.getRepayType() == 1)
			iReasonCode = 5;
		else
			iReasonCode = 9;

		int iSourceCode = tBatxDetail.getRepayCode();
		if (iSourceCode == 11) // 11.匯款轉帳預先作業 -> 01.匯款轉帳
			iSourceCode = 01;
		l3210TitaVo.putParam("TXCD", "L3210");
		l3210TitaVo.putParam("TXCODE", "L3210");
		l3210TitaVo.putParam("CustNo", tBatxDetail.getCustNo());
		l3210TitaVo.putParam("TimCustNo", tBatxDetail.getCustNo());
		l3210TitaVo.putParam("FacmNo", tBatxDetail.getFacmNo());
		if (tBatxDetail.getFacmNo() == 0) {
			if (!"".equals(this.tTempVo.getParam("RepayFacmNo"))) {
				l3210TitaVo.putParam("FacmNo", this.tTempVo.getParam("RepayFacmNo"));
			}
		}
		l3210TitaVo.putParam("EntryDate", tBatxDetail.getEntryDate());
		l3210TitaVo.putParam("CurrencyCode", "TWD");
		l3210TitaVo.putParam("TimTempAmt", tBatxDetail.getRepayAmt());
		l3210TitaVo.putParam("TwTempAmt", tBatxDetail.getRepayAmt());
		l3210TitaVo.putParam("TempReasonCode", iReasonCode);
		l3210TitaVo.putParam("TempReasonCodeX",
				loanCom.getCdCodeX("TempReasonCode", parse.IntegerToString(iReasonCode, 2), l3210TitaVo));
		l3210TitaVo.putParam("TempSourceCode", iSourceCode);
		l3210TitaVo.putParam("AreaCode", "");
		l3210TitaVo.putParam("BankCode", "");
		l3210TitaVo.putParam("BktwFlag", "N");
		l3210TitaVo.putParam("MediaFlag", "N");
		l3210TitaVo.putParam("OutsideCode", 1);
		l3210TitaVo.putParam("UsageCode", "");
		l3210TitaVo.putParam("ServiceCenter", "");
		l3210TitaVo.putParam("CreditorId", "");
		l3210TitaVo.putParam("CreditorBankCode", "");
		l3210TitaVo.putParam("OtherAcctCode", "");
		l3210TitaVo.putParam("ReceiptNo", "");
		// 是否回收費用，非正常戶轉暫收不回收費用
		if (this.tTempVo.get("FacStatus") != null) {
			if (functionCode == 2) {
				l3210TitaVo.putParam("PayFeeFlag", "N");
			} else {
				l3210TitaVo.putParam("PayFeeFlag", "Y");
			}
		} else {
			l3210TitaVo.putParam("PayFeeFlag", this.tTempVo.getParam("PayFeeFlag")); // 是否回收費用
		}
		return l3210TitaVo;
	}

	/* L3200 回收登錄 */
	private TitaVo setL3200Tita(TitaVo l3200TitaVo, BatxDetail tBatxDetail) throws LogicException {
		// 1.期款 2.部分償還 9.清償違約金 12.催收收回
		l3200TitaVo.putParam("TXCD", "L3200");
		l3200TitaVo.putParam("TXCODE", "L3200");
		l3200TitaVo.putParam("CustNo", tBatxDetail.getCustNo());
		l3200TitaVo.putParam("TimCustNo", tBatxDetail.getCustNo());
		if (!"".equals(this.tTempVo.getParam("RepayFacmNo"))) {
			l3200TitaVo.putParam("FacmNo", this.tTempVo.getParam("RepayFacmNo"));
		} else {
			l3200TitaVo.putParam("FacmNo", tBatxDetail.getFacmNo());
		}
		if (!"".equals(this.tTempVo.getParam("RepayBormNo"))) {
			l3200TitaVo.putParam("BormNo", this.tTempVo.getParam("RepayBormNo"));
		} else {
			l3200TitaVo.putParam("BormNo", "0");
		}
		l3200TitaVo.putParam("RepayTerms", "0"); // 回收期數
		l3200TitaVo.putParam("EntryDate", tBatxDetail.getEntryDate()); // 入帳日期
		l3200TitaVo.putParam("RepayType", tBatxDetail.getRepayType());
		l3200TitaVo.putParam("TimRepayAmt", tBatxDetail.getRepayAmt());
		l3200TitaVo.putParam("TwRepayAmt", tBatxDetail.getRepayAmt());
		l3200TitaVo.putParam("UsRepayAmt", tBatxDetail.getRepayAmt());
		// 清償違約金
		if (tBatxDetail.getRepayType() == 10) {
			l3200TitaVo.putParam("TimCloseBreachAmt", this.tTempVo.getParam("CloseBreachAmt"));
		} else {
			l3200TitaVo.putParam("TimCloseBreachAmt", "0");
		}
		l3200TitaVo.putParam("RqspFlag", ""); // 減免金額超過限額，Y.需主管核可
		l3200TitaVo.putParam("ShortPrinPercent", "0"); // 短收本金比率
		l3200TitaVo.putParam("ShortIntPercent", "0"); // 短收利息比率
		if (tBatxDetail.getRepayType() == 2) {
			if (!"".equals(this.tTempVo.getParam("MergeAmt"))) {
				l3200TitaVo.putParam("TimExtraRepay", this.tTempVo.getParam("MergeAmt")); // 部分償還金額
				l3200TitaVo.putParam("TwExtraRepay", this.tTempVo.getParam("MergeAmt"));
				l3200TitaVo.putParam("UsExtraRepay", this.tTempVo.getParam("MergeAmt"));
			} else {
				l3200TitaVo.putParam("TimExtraRepay", this.tTempVo.getParam("ExtraRepay")); // 部分償還金額
				l3200TitaVo.putParam("TwExtraRepay", this.tTempVo.getParam("ExtraRepay"));
				l3200TitaVo.putParam("UsExtraRepay", this.tTempVo.getParam("ExtraRepay"));
			}
			l3200TitaVo.putParam("IncludeIntFlag", this.tTempVo.getParam("IncludeIntFlag")); // 是否內含利息
			l3200TitaVo.putParam("UnpaidIntFlag", this.tTempVo.getParam("UnpaidIntFlag")); // 利息是否可欠繳
			l3200TitaVo.putParam("PayFeeFlag", this.tTempVo.getParam("PayFeeFlag")); // 是否回收費用
			l3200TitaVo.putParam("PayMethod", this.tTempVo.getParam("PayMethod"));// 繳納方式 1.減少每期攤還金額 2.縮短應繳期數
			l3200TitaVo.putParam("PayMethodX",
					loanCom.getCdCodeX("PayMethod", this.tTempVo.getParam("PayMethod"), l3200TitaVo));
			if ("".equals(this.tTempVo.getParam("CloseBreachAmt"))) {
				l3200TitaVo.putParam("TimExtraCloseBreachAmt", "0");
			} else {
				l3200TitaVo.putParam("TimExtraCloseBreachAmt", this.tTempVo.getParam("CloseBreachAmt"));
			}
		} else {
			l3200TitaVo.putParam("TimExtraRepay", "0");
			l3200TitaVo.putParam("IncludeIntFlag", " ");
			l3200TitaVo.putParam("UnpaidIntFlag", " ");
			l3200TitaVo.putParam("PayFeeFlag", "N".equals(tTempVo.getParam("PayFeeFlag")) ? "N" : "Y"); // 是否回收費用
			l3200TitaVo.putParam("PayMethod", " ");
		}
		if (tBatxDetail.getRepayType() == 12) {
			l3200TitaVo.putParam("TimOvduRepay", tBatxDetail.getRepayAmt());
		} else {
			l3200TitaVo.putParam("TimOvduRepay", "0");
		}
		l3200TitaVo.putParam("TimReduceAmt", "0"); // 減免金額
		l3200TitaVo.putParam("TotalRepayAmt", tBatxDetail.getAcquiredAmt()); // 應收付總金額 --> 還款總金額
		l3200TitaVo.putParam("RealRepayAmt", tBatxDetail.getRepayAmt()); // 實際收付金額 --> 還款金額
		l3200TitaVo.putParam("RepayIntDate", this.tTempVo.getParam("RepayIntDate"));// 應繳日
		l3200TitaVo.putParam("RepayIntDateByFacmNoVo", this.tTempVo.getParam("RepayIntDateByFacmNoVo"));// 額度應繳日
		l3200TitaVo.putParam("RepayLoan", this.tTempVo.getParam("RepayLoan"));// 償還本利

		return l3200TitaVo;
	}

	/* L3420 結案登錄 */
	private TitaVo setL3420Tita(TitaVo l3420TitaVo, BatxDetail tBatxDetail) throws LogicException {

		l3420TitaVo.putParam("TXCD", "L3420");
		l3420TitaVo.putParam("TXCODE", "L3420");
		l3420TitaVo.putParam("CustNo", tBatxDetail.getCustNo());
		l3420TitaVo.putParam("TimCustNo", tBatxDetail.getCustNo());
		if (!"".equals(this.tTempVo.getParam("RepayFacmNo"))) {
			l3420TitaVo.putParam("FacmNo", this.tTempVo.getParam("RepayFacmNo"));
		} else {
			l3420TitaVo.putParam("FacmNo", tBatxDetail.getFacmNo());
		}
		l3420TitaVo.putParam("BormNo", "0");
		l3420TitaVo.putParam("EntryDate", tBatxDetail.getEntryDate()); // 入帳日期
		l3420TitaVo.putParam("CaseCloseCode", 0); // 結案區分 0:正常結案
		l3420TitaVo.putParam("NewApplNo", "0"); // 結案區分非展期，核准號碼不可輸入
		l3420TitaVo.putParam("NewFacmNo", 0); // 新額度
		l3420TitaVo.putParam("RenewCode", 0); // 展期記號
		l3420TitaVo.putParam("RqspFlag", ""); // 減免金額超過限額，Y.需主管核可
		l3420TitaVo.putParam("ShortPrinPercent", "0"); // 短收本金比率
		l3420TitaVo.putParam("ShortIntPercent", "0"); // 短收利息比率
		// 提前清償原因
		// CloseFg 1.正常結案因 2.提前結案
		// 提前結案且無清償原因時，提前清償原因為 13-銀扣到期或02-自行還清
		if ("2".equals(this.tTempVo.getParam("CloseFg"))) {
			if ("".equals(this.tTempVo.getParam("CloseReasonCode"))) {
				if (tBatxDetail.getRepayCode() == 2) {
					l3420TitaVo.putParam("AdvanceCloseCode", "13"); // 13-銀扣到期
				} else {
					l3420TitaVo.putParam("AdvanceCloseCode", "02"); // 02-自行還清
				}
			} else {
				l3420TitaVo.putParam("AdvanceCloseCode", this.tTempVo.getParam("CloseReasonCode"));
			}
		} else {
			l3420TitaVo.putParam("AdvanceCloseCode", "00"); // 00-無
		}
		// 清償違約金
		if ("".equals(this.tTempVo.getParam("CloseBreachAmt"))) {
			l3420TitaVo.putParam("CloseBreachAmt", "0");
		} else {
			l3420TitaVo.putParam("CloseBreachAmt", this.tTempVo.getParam("CloseBreachAmt"));
		}
		l3420TitaVo.putParam("TimReduceAmt", "0"); // 減免金額
		l3420TitaVo.putParam("TimTrfBadAmt", "0"); // 轉銷呆帳金額
		l3420TitaVo.putParam("TotalRepayAmt", tBatxDetail.getAcquiredAmt()); // 應收付總金額 --> 還款總金額
		l3420TitaVo.putParam("RealRepayAmt", tBatxDetail.getRepayAmt()); // 實際收付金額 --> 還款金額

		return l3420TitaVo;
	}

	// 隔日訂正新增整批入帳明細檔(RESV00)
	private void insBatxResv(TitaVo titaVo) throws LogicException {
		// 交易失敗不處理
		if (this.txBuffer.getTxCom().getTxRsut() != 0) { // 交易失敗
			return;
		}
		BigDecimal repayAmt = parse.stringToBigDecimal(titaVo.getParam("RpAmt1"));
		int repayType = 0;
		if (titaVo.get("RpType1") != null) {
			repayType = parse.stringToInteger(titaVo.getParam("RpType1"));
		}
		int repayCode = parse.stringToInteger(titaVo.getParam("RpCode1"));

		String rvNo = "" + titaVo.getOrgEntdyI();
		String reconCode = "";
		if (titaVo.get("RpAcctCode1") != null) {
			reconCode = titaVo.getParam("RpAcctCode1");
		}
		// 暫收抵繳不處理
		if (repayCode >= 90 || repayAmt.compareTo(BigDecimal.ZERO) <= 0) {
			return;
		}
		BatxHeadId tBatxHeadId = new BatxHeadId();
		tBatxHeadId.setAcDate(this.txBuffer.getTxCom().getTbsdyf());
		tBatxHeadId.setBatchNo("RESV00");
		BatxHead tBatxHead = batxHeadService.holdById(tBatxHeadId, titaVo);
		if (tBatxHead == null) {
			tBatxHead = new BatxHead();
			tBatxHead.setBatxHeadId(tBatxHeadId);
			tBatxHead.setBatxTotAmt(repayAmt);
			tBatxHead.setBatxTotCnt(1);
			tBatxHead.setUnfinishCnt(1);
			tBatxHead.setBatxExeCode("3");
			tBatxHead.setBatxStsCode("0");
			try {
				this.info("Insert BatxHead !!!");
				batxHeadService.insert(tBatxHead, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", "update BatxHead " + tBatxHead + e.getErrorMsg());
			}
		} else {
			tBatxHead.setBatxTotAmt(tBatxHead.getBatxTotAmt().add(repayAmt));
			tBatxHead.setBatxTotCnt(tBatxHead.getBatxTotCnt() + 1);
			tBatxHead.setUnfinishCnt(tBatxHead.getUnfinishCnt() + 1);
			tBatxHead.setBatxExeCode("3");
		}
		try {
			batxHeadService.update(tBatxHead, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007", "update BatxHead " + tBatxHead + e.getErrorMsg());
		}
		BatxDetailId tBatxDetailId = new BatxDetailId();
		tBatxDetailId.setAcDate(tBatxHeadId.getAcDate());
		tBatxDetailId.setBatchNo(tBatxHeadId.getBatchNo());
		tBatxDetailId.setDetailSeq(tBatxHead.getBatxTotCnt());
		BatxDetail tBatxDetail = new BatxDetail();
		tBatxDetail.setBatxDetailId(tBatxDetailId);
		tBatxDetail.setRepayCode(repayCode);
		tBatxDetail.setEntryDate(parse.stringToInteger(titaVo.getParam("EntryDate")));
		tBatxDetail.setCustNo(parse.stringToInteger(titaVo.getParam("RpCustNo1")));
		tBatxDetail.setFacmNo(parse.stringToInteger(titaVo.getParam("RpFacmNo1")));
		tBatxDetail.setRvNo(rvNo);
		tBatxDetail.setRepayType(repayType);
		tBatxDetail.setReconCode(reconCode);
		tBatxDetail.setRepayAcCode("");
		tBatxDetail.setRepayAmt(repayAmt);
		tBatxDetail.setAcquiredAmt(BigDecimal.ZERO);
		tBatxDetail.setAcctAmt(BigDecimal.ZERO);
		tBatxDetail.setDisacctAmt(BigDecimal.ZERO);
		tBatxDetail.setProcStsCode("2");
		tBatxDetail.setProcCode("");
		tBatxDetail.setTitaTlrNo("");
		tBatxDetail.setTitaTxtNo("");
		TempVo tempVo = new TempVo();
		// 01.匯款轉帳
		if (tBatxDetail.getRepayCode() == 1) {
			updBankRmtf(0, tBatxDetail, titaVo);
		}
		// 02.銀行扣款
		if (tBatxDetail.getRepayCode() == 2) {
			updBankDeductDtl(0, tBatxDetail, titaVo);
		}
		// 03.員工扣款
		if (tBatxDetail.getRepayCode() == 3) {
			updEmpDeductDtl(0, tBatxDetail, titaVo);
		}
		// 04.支票兌現
		if (tBatxDetail.getRepayCode() == 4) {
			updLoanCheque(0, tBatxDetail, titaVo);
		}
		// 其他還款來源
		if (tBatxDetail.getRepayCode() >= 5 && tBatxDetail.getRepayCode() < 90) {
			updBatxOthers(0, tBatxDetail, titaVo);
		}

		tBatxDetail.setProcNote(tempVo.getJsonString());

		try {
			batxDetailService.insert(tBatxDetail, titaVo);
		} catch (DBException e) {
			e.printStackTrace();
			throw new LogicException("E0005", "BatxDetail Insert Fail");
		}
	}

	// 交易結束更新整批入帳明細檔
	private void updBatxResult(String batchNo, String batchSeq, TitaVo titaVo) throws LogicException {
//		                                                                                   成功                   失敗
//		  ProcStsCode   處理狀態    VARCHAR2(1)         5.人工入帳          3.人工處理
//		                                              6.批次入帳
//		  ProcCode      處理代碼    VARCHAR2(5)                        MSGID                           
//		  ProcNote      處理說明    NVARCHAR2(60)                      ERRMSG                                      
//		  AcctAmt       已作帳金額  DECIMAL(14)         TitaTxAmt -  OverRpAmt
//		  DisacctAmt    入暫收金額  DECIMAL(14)         OverRpAmt

		BatxDetailId tBatxDetailId = new BatxDetailId();
		tBatxDetailId.setAcDate(this.txBuffer.getTxCom().getTbsdyf());
		tBatxDetailId.setBatchNo(batchNo);
		tBatxDetailId.setDetailSeq(parse.stringToInteger(batchSeq));
		BatxHeadId tBatxHeadId = new BatxHeadId();
		tBatxHeadId.setAcDate(this.txBuffer.getTxCom().getTbsdyf());
		tBatxHeadId.setBatchNo(batchNo);
		int acDate = 0;
		// 已作帳金額
		BigDecimal acctAmt = BigDecimal.ZERO;

		BatxDetail tDetail = batxDetailService.holdById(tBatxDetailId, titaVo);

		if (tDetail == null) {
			throw new LogicException("E0006", "TxBatchCom " + tBatxDetailId); // E0006 鎖定資料時，發生錯誤
		}

		this.tTempVo = new TempVo();
		this.tTempVo = this.tTempVo.getVo(tDetail.getProcNote());

		this.tTempVo.putParam("Txcd", titaVo.getTxcd());
		this.tTempVo.putParam("ErrorMsg", "");
		int unfinishCnt = 0;
		if (this.txBuffer.getTxCom().getTxRsut() == 0) { // 交易成功
			if (titaVo.isHcodeNormal()) { // 正常交易
				if (this.txBuffer.getAcDetailList() != null && this.txBuffer.getAcDetailList().size() > 0) {
					for (AcDetail ac : this.txBuffer.getAcDetailList()) {
						if (ac.getSumNo().isEmpty() && "C".equals(ac.getDbCr()))
							acctAmt = acctAmt.add(ac.getTxAmt());
					}
				}
				tDetail.setAcctAmt(acctAmt); // 已作帳金額
				tDetail.setDisacctAmt(tDetail.getRepayAmt().subtract(acctAmt));
				unfinishCnt = -1;
				if ("BATX".equals(titaVo.get("BATCHNO").substring(0, 4))) {
					tDetail.setFacmNo(parse.stringToInteger(titaVo.getParam("FacmNo")));
				}
				// 檢核訊息
				if (titaVo.get("CheckMsg") != null) {
					tTempVo.putParam("CheckMsg", titaVo.get("CheckMsg"));
				}
				switch (titaVo.getTxcd()) {
				case "L3200":
					tDetail.setRepayType(parse.stringToInteger(titaVo.getParam("RepayType")));
					if (titaVo.get("PrePaidTerms") != null) {
						this.tTempVo.putParam("PrePaidTerms", titaVo.getParam("PrePaidTerms"));
					}
					break;
				case "L3210":
					// 轉暫收
					if (titaVo.get("FunctionCode") != null && "2".equals(titaVo.get("FunctionCode"))) {
						tDetail.setRepayType(9); // 9.暫收
						tTempVo.putParam("CheckMsgOrg", tTempVo.getParam("CheckMsg"));
						tTempVo.putParam("CheckMsg", "");
					}
					this.tTempVo.putParam("TempReasonCodeX", titaVo.getParam("TempReasonCodeX"));
					break;
				case "L3420":
					tDetail.setRepayType(3);
					break;
				}
				if (titaVo.isTrmtypBatch()) { // 批次入帳
					// 批次入帳(repayCode 1~4) 出彙總，其他:單張
					if (tDetail.getRepayCode() >= 1 && tDetail.getRepayCode() <= 4) {
						tDetail.setProcStsCode("6"); // 6.批次入帳
						this.tTempVo.putParam("BatchProcStsCode", "6");
					} else {
						tDetail.setProcStsCode("5"); // 5:單筆入帳
					}
				} else {
					tTempVo.putParam("CheckMsg", "");
					if ("6".equals(this.tTempVo.getParam("BatchProcStsCode"))) {
						tDetail.setProcStsCode("7"); // 7.批次入帳後人工
					} else {
						tDetail.setProcStsCode("5"); // 5:單筆入帳
					}
				}
				acDate = this.txBuffer.getTxCom().getTbsdy();
				tDetail.setTitaTlrNo(titaVo.getTlrNo());
				tDetail.setTitaTxtNo(titaVo.getTxtNo());
			} else {
				// 訂正交易
				unfinishCnt = 1;
				this.info("updBatxResult 訂正交易");
				tDetail.setProcStsCode("2"); // 訂正後為 2:人工處理
				if (this.tTempVo.get("RepayType") != null) {
					tDetail.setRepayType(parse.stringToInteger(this.tTempVo.get("RepayType"))); // 還原還款類別
				}
				tDetail.setDisacctAmt(BigDecimal.ZERO);
				tDetail.setAcctAmt(BigDecimal.ZERO); // 還款金額 - 入暫收金額
				if (this.tTempVo.get("EraseCnt") != null) {
					this.tTempVo.putParam("EraseCnt", parse.stringToInteger(this.tTempVo.get("EraseCnt")) + 1);
				} else {
					this.tTempVo.putParam("EraseCnt", "1");
				}
				tDetail.setTitaTlrNo("");
				tDetail.setTitaTxtNo("");

				acDate = 0;
			}
			// 01.匯款轉帳
			if (tDetail.getRepayCode() == 1) {
				updBankRmtf(acDate, tDetail, titaVo);
			}
			// 02.銀行扣款
			if (tDetail.getRepayCode() == 2) {
				updBankDeductDtl(acDate, tDetail, titaVo);
			}
			// 03.員工扣款
			if (tDetail.getRepayCode() == 3) {
				updEmpDeductDtl(acDate, tDetail, titaVo);
			}
			// 04.支票兌現
			if (tDetail.getRepayCode() == 4) {
				updLoanCheque(acDate, tDetail, titaVo);
			}
			// 其他還款來源
			if (tDetail.getRepayCode() >= 5 && tDetail.getRepayCode() < 90) {
				updBatxOthers(acDate, tDetail, titaVo);
			}

		} else {
			// 交易失敗
			String errorMsg = this.txBuffer.getTxCom().getErrorMsg().length() > 100
					? this.txBuffer.getTxCom().getErrorMsg().substring(0, 100)
					: this.txBuffer.getTxCom().getErrorMsg();
			this.tTempVo.putParam("ErrorMsg", this.txBuffer.getTxCom().getMsgId() + errorMsg); // 錯誤訊息
			if (titaVo.isHcodeNormal()) {
				tDetail.setProcStsCode("3"); // 正常交易失敗，設定為檢核錯誤
			} else { // 訂正交易失敗，狀態不變
			}
		}
		tDetail.setProcNote(this.tTempVo.getJsonString());
		try {
			batxDetailService.update(tDetail, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007",
					"TxBatchCom update BatxDetail " + tBatxDetailId + e.getErrorMsg());
		}

		if (this.txBuffer.getTxCom().getTxRsut() == 0) { // 交易成功
			BatxHead tBatxHead = batxHeadService.holdById(tBatxHeadId, titaVo);
			if (tBatxHead == null) {
				throw new LogicException("E0014", tBatxHeadId + " hold not exist"); // E0014 檔案錯誤
			}
			tBatxHead.setUnfinishCnt(tBatxHead.getUnfinishCnt() + unfinishCnt);

			// 訂正成功後更新為入帳未完
			if (tBatxHead.getUnfinishCnt() == 0) {
				tBatxHead.setBatxExeCode("4");// 4.入帳完成
			} else {
				tBatxHead.setBatxExeCode("3");// 3.入帳未完
			}

			try {
				batxHeadService.update(tBatxHead, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", "update BatxHead " + tBatxHead + e.getErrorMsg());
			}
		}

	}

//	Update BankRmtf 更新匯款轉帳檔
	private BatxDetail updBankRmtf(int acDate, BatxDetail tBatxDetail, TitaVo titaVo) throws LogicException {
		this.info("updBankRmtf ...");
		BankRmtf tBankRmtf = null;
		if (acDate == 0) {
			tBankRmtf = bankRmtfService.findTxSeqFirst(titaVo.getOrgEntdyI() + 19110000, titaVo.getOrgTlr(),
					titaVo.getOrgTno(), titaVo);
			if (tBankRmtf == null) {
				throw new LogicException("E0006", "TxBatchCom BankRmtf"); // E0006 鎖定資料時，發生錯誤
			}
			tBankRmtf = bankRmtfService.holdById(tBankRmtf, titaVo);
			tBatxDetail.setMediaDate(tBankRmtf.getAcDate());
			tBatxDetail.setMediaKind(tBankRmtf.getBatchNo().substring(4, 6));
			tBatxDetail.setMediaSeq(tBankRmtf.getDetailSeq());
		} else {
			BankRmtfId tBankRmtfId = new BankRmtfId();
			if (!"".equals(tBatxDetail.getMediaKind())) {
				tBankRmtfId.setAcDate(tBatxDetail.getMediaDate());
				tBankRmtfId.setBatchNo("BATX" + tBatxDetail.getMediaKind()); // 上傳批號後兩碼(匯款轉帳、支票兌現)
				tBankRmtfId.setDetailSeq(tBatxDetail.getMediaSeq());
			} else {
				tBankRmtfId.setAcDate(tBatxDetail.getAcDate());
				tBankRmtfId.setBatchNo(tBatxDetail.getBatchNo());
				tBankRmtfId.setDetailSeq(tBatxDetail.getDetailSeq());
			}
			tBankRmtf = bankRmtfService.holdById(tBankRmtfId, titaVo);
			if (tBankRmtf == null) {
				throw new LogicException("E0006", "TxBatchCom BankRmtf"); // E0006 鎖定資料時，發生錯誤
			}
		}
		tBankRmtf.setRepayType(parse.IntegerToString(tBatxDetail.getRepayType(), 2));
		tBankRmtf.setTitaEntdy(acDate);
		if (acDate == 0) {
			tBankRmtf.setTitaTlrNo("");
			tBankRmtf.setTitaTxtNo("");
		} else {
			tBankRmtf.setTitaTlrNo(titaVo.getTlrNo());
			tBankRmtf.setTitaTxtNo(titaVo.getTxtNo());
		}
		try {
			bankRmtfService.update(tBankRmtf, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007", "TxBatchCom update BankRmtf " + e.getErrorMsg());
		}
		return tBatxDetail;
	}

//		Update BankDeductDtl 交易結束更新銀扣明細檔
	private BatxDetail updBankDeductDtl(int acDate, BatxDetail tBatxDetail, TitaVo titaVo) throws LogicException {
		this.info("updBankDeductDtl ...");

		Slice<BankDeductDtl> slBankDeductDtl = null;
		if (acDate == 0) {
			slBankDeductDtl = bankDeductDtlService.findTxSeq(titaVo.getOrgEntdyI() + 19110000, titaVo.getOrgTlr(),
					titaVo.getOrgTno(), this.index, Integer.MAX_VALUE, titaVo);
		} else {
			slBankDeductDtl = bankDeductDtlService.mediaSeqRng(tBatxDetail.getMediaDate() + 19110000,
					tBatxDetail.getMediaKind(), tBatxDetail.getMediaSeq(), this.index, Integer.MAX_VALUE, titaVo);
		}
		List<BankDeductDtl> lBankDeductDtl = slBankDeductDtl == null ? new ArrayList<BankDeductDtl>()
				: slBankDeductDtl.getContent();
		if (lBankDeductDtl != null && lBankDeductDtl.size() != 0) {
			for (BankDeductDtl tBankDeductDtl : lBankDeductDtl) {
				if (acDate > 0 && tBankDeductDtl.getAcDate() > 0) {
					throw new LogicException(titaVo, "E0007",
							"TxBatchCom BankDeductDtl 已入帳 " + tBankDeductDtl.getBankDeductDtlId());
				}
				if (acDate == 0 && tBankDeductDtl.getAcDate() == 0) {
					throw new LogicException(titaVo, "E0007",
							"TxBatchCom BankDeductDtl 未入帳 " + tBankDeductDtl.getBankDeductDtlId());
				}
				tBankDeductDtl = bankDeductDtlService.holdById(tBankDeductDtl, titaVo);
				tBankDeductDtl.setAcDate(acDate);
				if (acDate == 0) {
					tBatxDetail.setMediaDate(tBankDeductDtl.getMediaDate());
					tBatxDetail.setMediaKind(tBankDeductDtl.getMediaKind());
					tBatxDetail.setMediaSeq(tBankDeductDtl.getMediaSeq());
					tBankDeductDtl.setTitaTlrNo("");
					tBankDeductDtl.setTitaTxtNo("");
				} else {
					tBankDeductDtl.setTitaTlrNo(titaVo.getTlrNo());
					tBankDeductDtl.setTitaTxtNo(titaVo.getTxtNo());
				}
				TempVo bTempVo = new TempVo();
				bTempVo = bTempVo.getVo(tBankDeductDtl.getJsonFields());
				bTempVo.putParam("ProcStsCode", tBatxDetail.getProcStsCode());
				tBankDeductDtl.setJsonFields(bTempVo.getJsonString());
				try {
					bankDeductDtlService.update(tBankDeductDtl, titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", "TxBatchCom update BankDeductDtl " + e.getErrorMsg());
				}
			}
		} else {
			throw new LogicException("E0006", "TxBatchCom mediaSeqRng BankDeductDtl"); // E0006 鎖定資料時，發生錯誤
		}
		return tBatxDetail;
	}

//		Update EmpDeductDtl  交易結束更新員工扣薪明細檔
	private void updEmpDeductDtl(int acDate, BatxDetail tBatxDetail, TitaVo titaVo) throws LogicException {
		this.info("updEmpDeductDtl ...");

//		BatxDetail's MediaDate MediaCode MediaSeq to find 15/un15 
		Slice<EmpDeductDtl> sEmpDeductDtl = null;
		if (acDate == 0) {
			sEmpDeductDtl = empDeductDtlService.findTxSeq(titaVo.getOrgEntdyI() + 19110000, titaVo.getOrgTlr(),
					titaVo.getOrgTno(), 0, Integer.MAX_VALUE, titaVo);
		} else {
			sEmpDeductDtl = empDeductDtlService.mediaSeqEq(tBatxDetail.getMediaDate() + 19110000,
					tBatxDetail.getMediaKind(), tBatxDetail.getMediaSeq(), this.index, Integer.MAX_VALUE, titaVo);

		}
		BigDecimal txAmt = parse.stringToBigDecimal(titaVo.getTxAmt());
		List<EmpDeductDtl> lEmpDeductDtl = new ArrayList<EmpDeductDtl>();
		lEmpDeductDtl = sEmpDeductDtl == null ? null : sEmpDeductDtl.getContent();
		if (lEmpDeductDtl != null && lEmpDeductDtl.size() != 0) {
			for (EmpDeductDtl tEmpDeductDtl : lEmpDeductDtl) {
				if (acDate > 0 && tEmpDeductDtl.getAcdate() > 0) {
					throw new LogicException(titaVo, "E0007",
							"TxBatchCom EmpDeductDtl 已入帳 " + tEmpDeductDtl.getEmpDeductDtlId());
				}
				if (acDate == 0 && tEmpDeductDtl.getAcdate() == 0) {
					throw new LogicException(titaVo, "E0007",
							"TxBatchCom EmpDeductDtl 未入帳 " + tEmpDeductDtl.getEmpDeductDtlId());
				}
				tEmpDeductDtl = empDeductDtlService.holdById(tEmpDeductDtl, titaVo);
				if (acDate == 0) {
					tBatxDetail.setMediaDate(tEmpDeductDtl.getMediaDate());
					tBatxDetail.setMediaKind(tEmpDeductDtl.getMediaKind());
					tBatxDetail.setMediaSeq(tEmpDeductDtl.getMediaSeq());
					tEmpDeductDtl.setAcdate(acDate);
					tEmpDeductDtl.setTitaTlrNo("");
					tEmpDeductDtl.setTitaTxtNo("");
					tEmpDeductDtl.setTxAmt(BigDecimal.ZERO);
					txAmt = BigDecimal.ZERO;
				} else {
					tEmpDeductDtl.setBatchNo(titaVo.get("BATCHNO"));
					tEmpDeductDtl.setAcdate(acDate);
					tEmpDeductDtl.setTitaTlrNo(titaVo.getTlrNo());
					tEmpDeductDtl.setTitaTxtNo(titaVo.getTxtNo());
					tEmpDeductDtl.setTxAmt(txAmt); // 實扣金額放第一筆
					txAmt = BigDecimal.ZERO;
				}
				try {
					empDeductDtlService.update(tEmpDeductDtl, titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", "TxBatchCom update EmpDeductDtl " + e.getErrorMsg());
				}
			}
		} else {
			throw new LogicException("E0006", "TxBatchCom mediaSeqEq EmpDeductDtl"); // E0006 鎖定資料時，發生錯誤
		}
	}

	// 兌現票入帳更新支票檔
	private BatxDetail updLoanCheque(int acDate, BatxDetail tBatxDetail, TitaVo titaVo) throws LogicException {
		this.info("updLoanCheque ...");
		int iCustNo = parse.stringToInteger(titaVo.getMrKey().substring(0, 7));
		String iRpRvno = titaVo.getParam("RpRvno1");
		int iChequeAcct = this.parse.stringToInteger(iRpRvno.substring(0, 9));
		int iChequeNo = this.parse.stringToInteger(iRpRvno.substring(10, 17));
		BigDecimal iRpAmt = this.parse.stringToBigDecimal(titaVo.getParam("RpAmt1"));

		this.info("   iRpRvno = " + iRpRvno);
		this.info("   iRpAmt = " + iRpAmt);
		this.info("   iChequeAcct = " + iChequeAcct);
		this.info("   iChequeNo = " + iChequeNo);
		LoanCheque tLoanCheque = null;
		if (acDate == 0) {
			tLoanCheque = loanChequeService.findTxSeqFirst(titaVo.getOrgEntdyI() + 19110000, titaVo.getOrgTlr(),
					titaVo.getOrgTno(), titaVo);
			if (tLoanCheque == null) {
				throw new LogicException(titaVo, "E0006",
						"戶號 = " + iCustNo + " 支票帳號 = " + iChequeAcct + " 支票號碼 = " + iChequeNo); // 鎖定資料時，發生錯誤
			}
			tLoanCheque = loanChequeService.holdById(tLoanCheque, titaVo);
		} else {
			tLoanCheque = loanChequeService.holdById(new LoanChequeId(iChequeAcct, iChequeNo), titaVo);
			if (tLoanCheque == null) {
				throw new LogicException(titaVo, "E0006",
						"戶號 = " + iCustNo + " 支票帳號 = " + iChequeAcct + " 支票號碼 = " + iChequeNo); // 鎖定資料時，發生錯誤
			}
		}

		// 更新欄
		// StatusCode票據狀況碼 4: 兌現未入帳 -> 1: 兌現入帳
		// AcDate 會計日期
		// RepaidAmt已入帳金額

		// 正常交易
		if (acDate > 0) {
			if (!(tLoanCheque.getStatusCode().equals("1") || tLoanCheque.getStatusCode().equals("4"))) {
				throw new LogicException(titaVo, "E3057",
						"戶號 = " + iCustNo + " 支票帳號 = " + iChequeAcct + " 支票號碼 = " + iChequeNo); // 該票據狀況碼非為兌現，不可入帳
			}
			if (tLoanCheque.getChequeAmt().subtract(tLoanCheque.getRepaidAmt()).compareTo(iRpAmt) < 0) {
				throw new LogicException(titaVo, "E3058",
						"支票尚未入帳金額 = " + tLoanCheque.getChequeAmt().subtract(tLoanCheque.getRepaidAmt())); // 暫收金額超過支票尚未入帳金額
			}
			tLoanCheque.setStatusCode("1"); // 1: 兌現入帳
			tLoanCheque.setRepaidAmt(tLoanCheque.getRepaidAmt().add(iRpAmt));
			tLoanCheque.setAcDate(acDate);
			tLoanCheque.setKinbr(titaVo.getKinbr());
			tLoanCheque.setTellerNo(titaVo.getTlrNo());
			tLoanCheque.setTxtNo(titaVo.getTxtNo());
		}
		// 訂正
		if (acDate == 0) {
			tLoanCheque.setRepaidAmt(tLoanCheque.getRepaidAmt().subtract(iRpAmt));
			if (tLoanCheque.getRepaidAmt().compareTo(new BigDecimal(0)) > 0) {
				tLoanCheque.setStatusCode("1"); // 1: 兌現入帳
			} else {
				tLoanCheque.setStatusCode("4"); // 4: 兌現未入帳
				tLoanCheque.setAcDate(0);
				tLoanCheque.setKinbr("");
				tLoanCheque.setTellerNo("");
				tLoanCheque.setTxtNo("");
			}
		}
		try {
			loanChequeService.update(tLoanCheque, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007",
					"戶號 = " + iCustNo + " 支票帳號 = " + iChequeAcct + " 支票號碼 = " + iChequeNo + " " + e.getErrorMsg()); // 更新資料時，發生錯誤
		}
		return tBatxDetail;
	}

//	Update BatxOthers 更新其他還款來源檔
	private BatxDetail updBatxOthers(int acDate, BatxDetail tBatxDetail, TitaVo titaVo) throws LogicException {
		this.info("updBatxOthers ...");
		BatxOthersId tBatxOthersId = new BatxOthersId();
		BatxOthers tBatxOthers = null;
		if (acDate == 0) {
			tBatxOthers = batxOthersService.findTxSeqFirst(titaVo.getOrgEntdyI() + 19110000, titaVo.getOrgTlr(),
					titaVo.getOrgTno(), titaVo);
			if (tBatxOthers == null) {
				throw new LogicException("E0006", "TxBatchCom BatxOthers"); // E0006 鎖定資料時，發生錯誤
			}
			tBatxOthers = batxOthersService.holdById(tBatxOthers, titaVo);
			tBatxDetail.setMediaDate(tBatxOthers.getAcDate());
			tBatxDetail.setMediaKind(tBatxOthers.getBatchNo().substring(4, 6));
			tBatxDetail.setMediaSeq(tBatxOthers.getDetailSeq());
		} else {
			tBatxOthersId.setAcDate(tBatxDetail.getMediaDate());
			tBatxOthersId.setBatchNo("BATX" + tBatxDetail.getMediaKind()); // 上傳批號後兩碼
			tBatxOthersId.setDetailSeq(tBatxDetail.getMediaSeq());
			tBatxOthers = batxOthersService.holdById(tBatxOthersId, titaVo);
			if (tBatxOthers == null) {
				throw new LogicException("E0006", "TxBatchCom BatxOthers"); // E0006 鎖定資料時，發生錯誤
			}
		}
		tBatxOthers.setTitaEntdy(acDate);
		if (acDate == 0) {
			tBatxOthers.setTitaTlrNo("");
			tBatxOthers.setTitaTxtNo("");
		} else {
			tBatxOthers.setTitaTlrNo(titaVo.getTlrNo());
			tBatxOthers.setTitaTxtNo(titaVo.getTxtNo());
		}
		try {
			batxOthersService.update(tBatxOthers, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007", "TxBatchCom update BatxOthers " + e.getErrorMsg());
		}
		return tBatxDetail;
	}

	/* 設定明細處理狀態 */
	private void settingprocStsCode(BatxDetail tBatxDetail, TitaVo titaVo) throws LogicException {
		this.info("settingprocStsCode ...");
// this.repayType 還款類別 1.期款 2.部分償還 3.結案 4.帳管費 5.火險費 6.契變手續費 7.法務費 9.其他 11.債協匯入款(虛擬帳號為9510500NNNNNNN)
		BigDecimal checkAmt = tBatxDetail.getRepayAmt();
		if (this.tTempVo.get("MergeAmt") != null) {
			checkAmt = parse.stringToBigDecimal(this.tTempVo.get("MergeAmt"));
			this.checkMsg += " 同戶號合併檢核 總金額:" + df.format(checkAmt) + " ";
		}
		// step01. 應繳試算
		// 合併檢核、轉暫收
		if (this.facStatus > 0) {
			checkFacStatus(titaVo);
			apendcheckMsgAmounts(tBatxDetail, titaVo);
		} else if (this.tTempVo.get("MergeAmt") != null && !tTempVo.get("MergeSeq").equals(tTempVo.get("MergeCnt"))) {
			this.checkMsg += " 轉暫收";
		} else {
			switch (this.repayType) {
			// 無還款類別
			case 0:
				// 處理狀態: 3.檢核錯誤
				// 處理說明:請變更(還款類別)後重新檢核
				this.checkMsg += " 請變更(還款類別)後重新檢核，";
				apendcheckMsgAmounts(tBatxDetail, titaVo);
				this.procStsCode = "3"; // 3.檢核錯誤
				break;
			// 01-期款
			case 1:
				// A3-還本帳戶，有期款未回收，人工處理
				if ("A3".equals(tBatxDetail.getReconCode()) && tBatxDetail.getRepayType() == 0) {
					this.checkMsg += " 有期款未回收，應繳日=" + tTempVo.getParam("NextPayIntDate");
					this.repayType = 2;
					this.procStsCode = "2"; // 2.人工處理
					break;
				}
				// 無償還本利
				// 處理狀態:2.人工處理
				// 處理說明:繳息迄日:999999
				if (this.repayLoan.compareTo(BigDecimal.ZERO) == 0) {
					this.checkMsg += " 繳息迄日:" + this.prevPayintDate;
					apendcheckMsgAmounts(tBatxDetail, titaVo);
					this.procStsCode = "2"; // 2.人工處理
					break;
				}
				// 無償還本利，銀扣除外
				// 處理狀態:2.人工處理
				// 處理說明:<不足利息>,<不足期款> 999999 期金:999999 未繳費用:999999
				// 還款應繳日期=0
				if (this.repayIntDate == 0 && tBatxDetail.getRepayCode() != 2) {
					if (this.principal.compareTo(BigDecimal.ZERO) > 0) {
						this.checkMsg += " 不足期款  差額:" + df.format(this.shortAmt);
					} else {
						this.checkMsg += " 不足利息  差額:" + df.format(this.shortAmt);
					}
					apendcheckMsgAmounts(tBatxDetail, titaVo);
					this.procStsCode = "2"; // 2.人工處理
					break;
				}
				// 處理狀態:4.檢核正常
				// if 回收金額 > 全部應繳 then 處理說明:有溢繳款:999999
				this.procStsCode = "4"; // 4.檢核正常
				if (this.overAmt.compareTo(BigDecimal.ZERO) > 0) {
					this.checkMsg += " 有溢繳款:" + df.format(this.overAmt);
				}

				if (this.shortAmt.compareTo(BigDecimal.ZERO) > 0) {
					this.checkMsg += " 有短繳款:" + df.format(this.shortAmt);
				}
				apendcheckMsgAmounts(tBatxDetail, titaVo);
				break;
			// 02-部分償還
			case 2:
				this.checkMsg += " 繳息迄日:" + this.prevPayintDate;
				// 有期款未回收，人工處理
				if ("1".equals(tTempVo.getParam("RepayTypeChange"))) {
					this.checkMsg += " 有期款未回收，應繳日=" + tTempVo.getParam("NextPayIntDate");
					this.procStsCode = "2"; // 2.人工處理
					break;
				}
				// 有即時收取清償違約金
				// 處理狀態:2.人工處理
				// 處理說明:有清償違約金:999999
				if (this.closeBreachAmt.compareTo(BigDecimal.ZERO) > 0) {
					this.checkMsg += " 有即時收取清償違約金 :";
					apendcheckMsgAmounts(tBatxDetail, titaVo);
					this.procStsCode = "2"; // 2.人工處理
					break;
				}
				// 撥款序號 = 0
				// 處理狀態:2.人工處理
				// 處理說明: 無可償還撥款資料
				if (this.repayLoan.compareTo(BigDecimal.ZERO) == 0) {
					// 催呆結案戶
					this.checkMsg += " 無可償還撥款資料";
					apendcheckMsgAmounts(tBatxDetail, titaVo);
					this.procStsCode = "2"; // 2.人工處理
					break;
				}
				// 部分償還本金
				this.checkMsg += " 部分償還金額 :" + df.format(parse.stringToBigDecimal(this.tTempVo.get("ExtraRepay")));

				// 檢核正常
				if (this.overAmt.compareTo(BigDecimal.ZERO) > 0) {
					this.checkMsg += " 有溢繳款:" + df.format(this.overAmt);
				}

				if (this.shortAmt.compareTo(BigDecimal.ZERO) > 0) {
					this.checkMsg += " 有短繳款:" + df.format(this.shortAmt);
				}

				// 短繳金額超過利息(有本金累短收)
				if (this.shortAmt.compareTo(this.shortfallInt.add(this.interest)) > 0) {
					this.checkMsg += " 短繳金額超過利息(有本金累短收)";
					apendcheckMsgAmounts(tBatxDetail, titaVo);
					this.procStsCode = "2"; // 3.檢核錯誤
					break;
				}
				this.procStsCode = "4"; // 4.檢核正常
				apendcheckMsgAmounts(tBatxDetail, titaVo);
				break;
			// 03-結案
			case 3:
				// 回收金額 + 暫收可抵繳 - 全部應繳 < 結案金額(不足金額)
				// 處理狀態:2.人工處理
				// 處理說明:<金額不足>999999
				if (this.unPayLoan.compareTo(BigDecimal.ZERO) == 0 && this.repayLoan.compareTo(BigDecimal.ZERO) == 0) {
					this.checkMsg += " 需償還本利金額:0";
					apendcheckMsgAmounts(tBatxDetail, titaVo);
					this.procStsCode = "2"; // 2.人工處理
					break;
				}
				if (this.shortAmt.compareTo(BigDecimal.ZERO) > 0) {
					this.checkMsg += " 金額不足: " + df.format(this.shortAmt);
					apendcheckMsgAmounts(tBatxDetail, titaVo);
					this.procStsCode = "2"; // 2.人工處理
					break;
				}
				// 提前結案需先執行 L2631-清償作業
				// 處理狀態:2.人工處理
				// 處理說明:提前結案請先執行 L2631-清償作業(L2077)
				if (this.closeFg == 2) {
					this.checkMsg += " 繳息迄日:" + this.prevPayintDate;
					// 結清時判斷該戶號額度下主要擔保的其他額度是否已全部結清
					if (loanAvailableAmt.isAllCloseClFac(tBatxDetail.getCustNo(), this.repayFacmNo, titaVo)) {
						facCloseRepayType(tBatxDetail, checkAmt, titaVo);
						if (this.tTempVo.get("CloseReasonCode") == null) {
							this.checkMsg += " 提前結案且擔保品額度是已全部結清，請先執行 L2631-清償作業(L2077)";
							this.procStsCode = "2"; // 2.人工處理
							break;
						}
					}
				}
				// 檢核正常
				if (this.closeFg == 1) {
					this.checkMsg += " 正常結案 ";
				} else {
					this.checkMsg += " 提前結案 ";
				}

				// 結案有溢繳款：999999
				if (this.overAmt.add(this.tavAmt).subtract(this.tmpAmt).compareTo(BigDecimal.ZERO) > 0) {
					this.checkMsg += " 有溢繳款: " + df.format(this.overAmt.add(this.tavAmt).subtract(this.tmpAmt));
				}

				apendcheckMsgAmounts(tBatxDetail, titaVo);
				this.procStsCode = "4"; // 4.檢核正常
				break;

			// 04-帳管費 05-火險費 06-契變手續費 07-法務費
			case 4:
			case 5:
			case 6:
			case 7:
				// 有回收該還款類別費用
				// 處理狀態:4.檢核正常
				if (this.repayFee.compareTo(BigDecimal.ZERO) > 0) {
					apendcheckMsgAmounts(tBatxDetail, titaVo);
					this.procStsCode = "4"; // 4.檢核正常
					break;
				}
				// 無該還款類別未繳費用
				// 處理狀態:2.人工處理
				// 處理說明: 無該還款類別未繳費用
				if (this.unPayRepayTypeFee.compareTo(BigDecimal.ZERO) == 0) {
					this.checkMsg += " 無該還款類別應繳費用 ";
					apendcheckMsgAmounts(tBatxDetail, titaVo);
					this.procStsCode = "2"; // 2.人工處理
					break;
				}
				// 無符合該類別的費用金額
				// 處理狀態:2.人工處理
				// 處理說明: 無該還款類別未繳費用
				if (this.unPayRepayTypeFee.compareTo(BigDecimal.ZERO) >= 0) {
					this.checkMsg += " 無符合該類別的費用金額，未繳費用： " + df.format(this.unPayRepayTypeFee);
					apendcheckMsgAmounts(tBatxDetail, titaVo);
					this.procStsCode = "2"; // 2.人工處理
					break;
				}
				break;
			// 09-其他
			default:
				apendcheckMsgAmounts(tBatxDetail, titaVo);
				this.procStsCode = "4"; // 4.檢核正常
				break;
			}
		}

		// 訂正後需人工處理
		if ("4".equals(this.procStsCode) && this.tTempVo.get("EraseCnt") != null) {
			this.procStsCode = "2"; // 2.人工處理
		}

		// 去起頭的空白
		if (this.checkMsg.length() > 2 && this.checkMsg.startsWith(" ")) {
			String str = this.checkMsg.substring(1);
			this.checkMsg = str;
		}
		this.info("log output");
		this.info("還款戶號額度     = " + tBatxDetail.getCustNo() + "-" + this.repayFacmNo + "-" + this.repayBormNo);
		this.info("還款類別 repayType           = " + this.repayType);
		this.info("還款金額 RepayAmt            = " + tBatxDetail.getRepayAmt());
		this.info("還款總金額 RepayTotal        = " + this.repayTotal);
		this.info("全部應繳 unPayTotal          = " + this.unPayTotal);
		this.info("應繳費用 unPayFee            = " + this.unPayFee);
		this.info("應繳本利 unPayLoan           = " + this.unPayLoan);
		this.info("償還本利 repayLoan           = " + this.repayLoan);
		this.info("應繳日 repayIntDat           = " + this.repayIntDate + " ," + this.repayIntDateByFacmNoVo);
		this.info("償還本金 Principal           = " + this.principal);
		this.info("含清償違約金 closeBreachAmt  = " + this.closeBreachAmt);
		this.info("含累短收 shortFallAmt        = " + this.shortfallPrin.add(this.shortfallInt));
		this.info("回收費用 repayFee            = " + this.repayFee);
		this.info("暫收可抵繳 tavAmt            = " + this.tavAmt);
		this.info("暫收抵繳 tmpAmt              = " + this.tmpAmt);
		this.info("溢繳金額 overAmt             = " + this.overAmt);
		this.info("短繳金額 shortAmt            = " + this.shortAmt);
		this.info("溢繳款額度 OverRpFacmNo      = " + this.overRpFacmNo + ", 溢短收記號=" + this.overRpFg);
		this.info("其他額度暫收可抵繳 otrTavAmt = " + this.otrTavAmt);
	}

	/* append this.checkMsg Amount Field */
	private void apendcheckMsgAmounts(BatxDetail tBatxDetail, TitaVo titaVo) throws LogicException {
		if (this.repayFacmNo != tBatxDetail.getFacmNo()) {
			this.checkMsg += " 額度:" + parse.IntegerToString(this.repayFacmNo, 3);
		}
		if (this.repayBormNo > 0) {
			this.checkMsg += " 撥款:" + parse.IntegerToString(this.repayBormNo, 3);
		}
		if (this.repayLoan.compareTo(BigDecimal.ZERO) > 0) {
			this.checkMsg += " 償還本利:" + df.format(this.repayLoan);
			if (this.repayType == 1) {
				this.checkMsg += "(" + this.intStartDate + "~" + this.intEndDate + ")";
				if (this.intEndDate > titaVo.getEntDyI()) {
					this.checkMsg += "預繳";
				}
			}
		}
		if (this.closeBreachAmt.compareTo(BigDecimal.ZERO) > 0) {
			this.checkMsg += " 含清償違約金:" + df.format(this.closeBreachAmt);
		}
		if (this.shortfallInt.compareTo(BigDecimal.ZERO) > 0) {
			this.checkMsg += " 含累短收利息:" + df.format(this.shortfallInt);
		}
		if (this.shortfallPrin.compareTo(BigDecimal.ZERO) > 0) {
			this.checkMsg += " 含累短收本金:" + df.format(this.shortfallPrin);
		}
		if (this.shortCloseBreach.compareTo(BigDecimal.ZERO) > 0) {
			this.checkMsg += " 含累短收清償違約金:" + df.format(this.shortCloseBreach);
		}
		if (this.acctFee.compareTo(BigDecimal.ZERO) > 0) {
			this.checkMsg += " 帳管費:" + this.acctFee;
		}
		if (this.fireFee.compareTo(BigDecimal.ZERO) > 0) {
			this.checkMsg += " 火險費:" + df.format(this.fireFee) + "(" + this.fireFeeDate + ")";
		}
		if (this.unSettlefireFee.compareTo(BigDecimal.ZERO) > 0) {
			this.checkMsg += " 未收當月火險費:" + df.format(this.unSettlefireFee);
		}
		if (this.modifyFee.compareTo(BigDecimal.ZERO) > 0) {
			this.checkMsg += " 契變手續費:" + df.format(this.modifyFee);
		}
		if (this.lawFee.compareTo(BigDecimal.ZERO) > 0) {
			this.checkMsg += " 法務費:" + df.format(this.lawFee);
		}
		// 暫收指定額度
		if (!tmpFacmNoX.isEmpty()) {
			this.checkMsg += " 暫收指定額度:" + tmpFacmNoX;
		}
		// 暫收抵繳
		if (this.tmpAmt.compareTo(BigDecimal.ZERO) > 0) {
			this.checkMsg += " 暫收抵繳:" + df.format(this.tmpAmt);
		}
		// 暫收可抵繳
		if (this.shortAmt.compareTo(BigDecimal.ZERO) > 0) {
			if (this.tavAmt.compareTo(BigDecimal.ZERO) > 0)
				this.checkMsg += " 暫收可抵繳:" + df.format(this.tavAmt);
			if (this.otrTavAmt.compareTo(BigDecimal.ZERO) > 0)
				this.checkMsg += " 其他額度暫收可抵繳:" + df.format(this.otrTavAmt);
		}
		// 應繳本利 > 償還本利 => 顯示應繳本利
		if (this.unPayLoan.compareTo(this.repayLoan) > 0) {
			this.checkMsg += " 應繳本利:" + df.format(this.unPayLoan);
		}
		// 顧客控管警訊檔
		Slice<LoanCustRmk> slLoanCustRmk = loanCustRmkService.findCustNo(tBatxDetail.getCustNo(), this.index,
				Integer.MAX_VALUE, titaVo);
		if (slLoanCustRmk != null) {
			int rmkNo = 0;
			String rmkDesc = "";
			for (LoanCustRmk tLoanCustRmk : slLoanCustRmk) {
				if (parse.isNumeric(tLoanCustRmk.getRmkCode())
						&& parse.stringToInteger(tLoanCustRmk.getRmkCode()) >= 301
						&& parse.stringToInteger(tLoanCustRmk.getRmkCode()) <= 310) {
					if (rmkNo == 0 || tLoanCustRmk.getRmkNo() > rmkNo) {
						rmkNo = tLoanCustRmk.getRmkNo();
						rmkDesc = " " + tLoanCustRmk.getRmkDesc();
					}
				}
			}
			this.checkMsg += rmkDesc;
		}

	}

	/* 匯款轉帳依虛擬帳號設定還款類別 */
	private void bankRmtRepayType(BatxDetail tDetail, BigDecimal checkAmt, TitaVo titaVo) throws LogicException {
//		 1.期款 2.部分償還 3.結案 4.帳管費 5.火險費 6.契變手續費 7.法務費 9.其他 11.債協匯入款 
// 企金戶(A1):95101 ==> 預設<還款類別> , 處理狀態 : 2.人工處理 處理說明: 企金戶請人工入帳
// 個人戶期款(A2):95102 ==> 最後一期(還款後本金餘額 =0 )=> 還款類別: 03-結案 ---->
// 個人戶還本(A3):95103 ==>
//          1.與大於等於約定還本金額 ==> 還款類別:02-部分償還
//            (if LoanBook.BookAmt = txamt and status = 0: 未回收 & entryDate = LoanBook.BookDate)
//          2.執行過清償作業     ==> 還款類別:03-結案  (check FacClose exist)
// 債協匯入款(A6):95105     ==> 11-債協匯入款
		switch (tDetail.getReconCode()) {
		case "A2":
			this.repayType = 1;
			break;
		case "A1":
		case "A3":
			// 1.大於等於約定還本金額 ==> 還款類別:02-部分償還
			if (this.repayType == 0) {
				loanBookRepayType(tDetail, checkAmt, titaVo);
			}
			// 2.執行過清償作業 ==> 還款類別:03-結案 (check FacClose exist)
			if (this.repayType == 0) {
				facCloseRepayType(tDetail, checkAmt, titaVo);
			}
			break;
		case "A6":
		case "A7": // 債協專戶
			// 11-債協匯入款
			this.repayType = 11;
			break;
		}
	}

	/* 1.還款金額 >=約定還本金額 ==> 還款類別:02-部分償還 */
	private void loanBookRepayType(BatxDetail tDetail, BigDecimal checkAmt, TitaVo titaVo) throws LogicException {

		Slice<LoanBook> slLoanBook = loanBookService.bookCustNoRange(tDetail.getCustNo(), tDetail.getCustNo(),
				tDetail.getFacmNo(), tDetail.getFacmNo() > 0 ? tDetail.getFacmNo() : 999, 0, 990,
				tDetail.getEntryDate() + 19110000, this.index, Integer.MAX_VALUE, titaVo);
		if (slLoanBook != null) {
			for (LoanBook tLoanBook : slLoanBook.getContent()) {
				if (tLoanBook.getStatus() == 0 && checkAmt.compareTo(tLoanBook.getBookAmt()) >= 0) {
					this.repayType = 02; // 02-部分償還
					this.repayFacmNo = tLoanBook.getFacmNo(); // 還款額度
					this.repayBormNo = tLoanBook.getBormNo(); // 撥款序號
					this.tTempVo.putParam("IncludeIntFlag", tLoanBook.getIncludeIntFlag()); // 是否內含利息
					this.tTempVo.putParam("IncludeFeeFlag", tLoanBook.getIncludeFeeFlag()); // 是否回收費用
					this.tTempVo.putParam("ExtraRepay", tLoanBook.getBookAmt()); // tLoanBook.getBookAmt(); // 約定還本金額
					this.tTempVo.putParam("UnpaidIntFlag", tLoanBook.getUnpaidIntFlag());// 利息是否可欠繳
					this.tTempVo.putParam("PayMethod", tLoanBook.getPayMethod());// 繳納方式 1.減少每期攤還金額 2.縮短應繳期數
					break;
				}
			}
		}
		this.info("loanBookRepayType end RepayType = " + this.repayType);
	}

	/* 2.執行過清償作業 ==> 還款類別:03-結案 */
	private void facCloseRepayType(BatxDetail tDetail, BigDecimal checkAmt, TitaVo titaVo) throws LogicException {
		Slice<FacClose> facCloseList = facCloseService.findCustNo(tDetail.getCustNo(), this.index, Integer.MAX_VALUE,
				titaVo); // 清償作業檔
		// 還款金額>=償還清金額
		if (facCloseList != null) {
			for (FacClose tFacClose : facCloseList.getContent()) {
				if (tFacClose.getCloseDate() == 0 && checkAmt.compareTo(tFacClose.getCloseAmt()) >= 0) {
					this.repayType = 3; // 03-結案
					if (tDetail.getFacmNo() == 0) {
						this.repayFacmNo = tFacClose.getFacmNo(); // 還款額度
					}
					this.tTempVo.putParam("CollectFlag", tFacClose.getCollectFlag()); // 是否領取清償證明(Y/N/)
					this.tTempVo.putParam("CloseReasonCode", tFacClose.getCloseReasonCode()); // CloseReasonCode 清償原因
				}
			}
		}
		this.info("facCloseRepayType end RepayType = " + this.repayType);
	}

	/* 初始處理說明 */
	private void initialProcNote(BatxDetail tDetail, TempVo t, TitaVo titaVo) throws LogicException {
		this.info("initialProcNote AmlRsp1=" + t.get("AmlRsp1") + ", AmlRsp2=" + t.get("AmlRsp2"));
		this.tTempVo.clear();
		this.tTempVo.putParam("CheckMsg", "");
		this.tTempVo.putParam("ErrorMsg", "");
		if (t.get("DscptCode") != null)
			this.tTempVo.putParam("DscptCode", t.get("DscptCode")); // 摘要代碼
		if (t.get("Note") != null)
			this.tTempVo.putParam("Note", t.get("Note")); // 摘要
		if (t.get("VirtualAcctNo") != null)
			this.tTempVo.putParam("VirtualAcctNo", t.get("VirtualAcctNo")); // 虛擬帳號
		if (t.get("PayIntDate") != null)
			this.tTempVo.putParam("PayIntDate", t.get("PayIntDate")); // 銀扣期款應繳日
		if (t.get("RepayBank") != null)
			this.tTempVo.putParam("RepayBank", t.get("RepayBank")); // 扣款銀行
		if (t.get("ReturnMsg") != null)
			this.tTempVo.putParam("ReturnMsg", t.get("ReturnMsg")); // 回應訊息
		if (t.get("Remark") != null)
			this.tTempVo.putParam("Remark", t.get("Remark")); // 備註(交易人資料..)
		if (t.get("AmlRsp1") != null)
			this.tTempVo.putParam("AmlRsp1", t.get("AmlRsp1")); // AML檢核狀態
		if (t.get("AmlRsp2") != null)
			this.tTempVo.putParam("AmlRsp2", t.get("AmlRsp2")); // AML檢核狀態
		if (t.get("EraseCnt") != null)
			this.tTempVo.putParam("EraseCnt", t.get("EraseCnt")); // 訂正次數
		if (t.get("FileSeq") != null)
			this.tTempVo.putParam("FileSeq", t.get("FileSeq")); // 檔案序號
		if (t.get("MergeCnt") != null) {
			this.tTempVo.putParam("MergeCnt", t.get("MergeCnt")); // 合併總筆數
			this.tTempVo.putParam("MergeAmt", t.get("MergeAmt")); // 合併總金額
			this.tTempVo.putParam("MergeSeq", t.get("MergeSeq")); // 合併序號
			this.tTempVo.putParam("MergeTempAmt", t.get("MergeTempAmt")); // 合併轉暫收金額
		}
		if (t.get("PreRepayTerms") != null) {
			tTempVo.putParam("PreRepayTerms", t.get("PreRepayTerms")); // 批次預收期數
		}
		if (t.get("PayFeeMethod") != null) {
			tTempVo.putParam("PayFeeMethod", t.get("PayFeeMethod")); // 回收費用方式 Y/N
		}
		if (t.get("ChequeAmt") != null) {
			tTempVo.putParam("ChequeAmt", t.get("ChequeAmt")); // 回收費用方式 Y/N
		}
		// 非15日薪僅扣期款
		if ("5".equals(tDetail.getMediaKind())) {
			tTempVo.putParam("PayFeeMethod", "N");
		}

	}

	/* 設定處理說明 */
	private void settingProcNote(BatxDetail tDetail, TitaVo titaVo) throws LogicException {
		this.tTempVo.putParam("CheckMsg", this.checkMsg);
		this.tTempVo.putParam("ErrorMsg", this.errorMsg);
		this.tTempVo.putParam("RepayType", this.repayType);
		if (tDetail.getFacmNo() == 0 && this.repayFacmNo > 0)
			this.tTempVo.putParam("RepayFacmNo", this.repayFacmNo);
		if (this.repayBormNo > 0)
			this.tTempVo.putParam("RepayBormNo", this.repayBormNo);
		if (this.shortAmt.compareTo(BigDecimal.ZERO) > 0)
			this.tTempVo.putParam("ShortAmt", this.shortAmt);
		if (this.overAmt.compareTo(BigDecimal.ZERO) > 0)
			this.tTempVo.putParam("OverAmt", this.overAmt);
		if (this.overRpFacmNo != this.repayFacmNo && this.overRpFacmNo > 0)
			this.tTempVo.putParam("OverRpFacmNo", this.overRpFacmNo);
		if (this.overRpFg > 0)
			this.tTempVo.putParam("OverRpFg", this.overRpFg);
		if (this.principal.compareTo(BigDecimal.ZERO) > 0)
			this.tTempVo.putParam("Principal", this.principal);
		if (this.interest.compareTo(BigDecimal.ZERO) > 0)
			this.tTempVo.putParam("Interest", this.interest);
		if (this.delayInt.compareTo(BigDecimal.ZERO) > 0)
			this.tTempVo.putParam("DelayInt", this.delayInt);
		if (this.breachAmt.compareTo(BigDecimal.ZERO) > 0)
			this.tTempVo.putParam("BreachAmt", this.breachAmt);
		if (this.shortfallInt.compareTo(BigDecimal.ZERO) > 0)
			this.tTempVo.putParam("ShortfallInt", this.shortfallInt);
		if (this.shortfallPrin.compareTo(BigDecimal.ZERO) > 0)
			this.tTempVo.putParam("ShortfallPrin", this.shortfallPrin);
		if (this.shortCloseBreach.compareTo(BigDecimal.ZERO) > 0)
			this.tTempVo.putParam("ShortCloseBreach", this.shortCloseBreach);
		if (this.acctFee.compareTo(BigDecimal.ZERO) > 0)
			this.tTempVo.putParam("AcctFee", this.acctFee);
		if (this.fireFee.compareTo(BigDecimal.ZERO) > 0)
			this.tTempVo.putParam("FireFee", this.fireFee);
		if (this.modifyFee.compareTo(BigDecimal.ZERO) > 0)
			this.tTempVo.putParam("ModifyFee", this.modifyFee);
		if (this.lawFee.compareTo(BigDecimal.ZERO) > 0)
			this.tTempVo.putParam("LawFee", this.lawFee);
		if (this.closeBreachAmt.compareTo(BigDecimal.ZERO) > 0)
			this.tTempVo.putParam("CloseBreachAmt", this.closeBreachAmt);
		if (this.closeFg > 0)
			this.tTempVo.putParam("CloseFg", this.closeFg);
		// 暫收抵繳
		if (this.tmpAmt.compareTo(BigDecimal.ZERO) > 0) {
			int i = 1;
			for (BaTxVo baTxVo : baTxList) {
				if (baTxVo.getDataKind() == 3 && baTxVo.getAcctAmt().compareTo(BigDecimal.ZERO) > 0) {
					this.tTempVo.putParam("TmpAmt" + i, baTxVo.getAcctAmt());
					this.tTempVo.putParam("TmpFacmNo" + i, parse.IntegerToString(baTxVo.getFacmNo(), 3));
					i++;
				}
			}
		}

		// 償還本利
		if (this.repayLoan.compareTo(BigDecimal.ZERO) > 0) {
			this.tTempVo.putParam("RepayLoan", this.repayLoan);
		}
		// 本息計算表
		if (this.intListTempVo != null) {
			this.otherTempVo.putParam("IntListTempVo", this.intListTempVo.getJsonString());
		}

	}

	/* 應繳試算 */
	private void settingUnPaid(BatxDetail tDetail, TitaVo titaVo) throws LogicException {
// this.unPayTotal  全部應繳
// this.unPayFee 應繳費用 = 1.應收費用+未收費用+短繳期金  this.unPayFee
// this.unPayLoan 期金 = 2.本金利息  
// this.repayFee 回收費用
// this.repayLoan回收本利
// this.closeBreachAmt 清償違約金

// this.tempAmt 暫收可抵繳 = 3.暫收抵繳   
// this.shortAmt 短繳金額
// this.overAmt 溢繳金額
// this.otrtempAmt 其他額度暫收可抵繳 = 5.其他額度暫收 

// loanBal 放款餘額(還款前)
// this.feeRepayType 費用還款類別
		baTxList = new ArrayList<BaTxVo>();
		// call 應繳試算，試算至應繳，可預收(批次可預收期數)， 應繳日設定為入帳日
		try {
			baTxList = baTxCom.settleUnPaid(tDetail.getEntryDate(), tDetail.getEntryDate(), tDetail.getCustNo(),
					this.repayFacmNo, this.repayBormNo, tDetail.getRepayCode(), this.repayType, tDetail.getRepayAmt(),
					tTempVo, titaVo);
		} catch (LogicException e) {
			this.errorMsg = e.getMessage();
			if (this.errorMsg.length() >= 5) {
				TxErrCode tTxErrCode = txErrCodeService.findById(this.errorMsg.substring(0, 5), titaVo);
				if (tTxErrCode != null) {
					this.errorMsg = this.errorMsg.substring(0, 5) + tTxErrCode.getErrContent() + "("
							+ this.errorMsg.substring(5) + ")";
				}
			}

			this.procStsCode = "3"; // 3.檢核錯誤
		}

		// TempVo
		this.tTempVo = baTxCom.getTempVo();
		this.info("TxBatchCom baTxCom.getTempVo()=" + this.tTempVo.toString());

		// 還款額度
		if (this.tTempVo.get("RepayFacmNo") != null) {
			this.repayFacmNo = parse.stringToInteger(this.tTempVo.get("RepayFacmNo"));
		}

		// 還款類別
		if (this.tTempVo.get("RepayType") != null) {
			this.repayType = parse.stringToInteger(this.tTempVo.get("RepayType"));
		}

		// 期款 還款應繳日 (期款按期償還)
		if (this.repayType == 1) {
			this.repayIntDate = parse.stringToInteger(this.tTempVo.get("RepayIntDate")); // 還款應繳日
			this.repayIntDateByFacmNoVo = this.tTempVo.get("RepayIntDateByFacmNoVo"); // 還款額度
		}
		// 上次繳息日
		if (this.repayType <= 3) {
			this.prevPayintDate = baTxCom.getPrevPayIntDate();
		}

		// 戶況
		if (this.tTempVo.get("FacStatus") != null) {
			this.facStatus = parse.stringToInteger(this.tTempVo.get("FacStatus"));

		}
		// 結案記號
		boolean isCloseFg = true;
		if ("0".equals(this.procStsCode) && baTxList != null && baTxList.size() != 0) {
			for (BaTxVo baTxVo : baTxList) {
				if (baTxVo.getDataKind() == 2) {
					this.loanBal = loanBal.add(baTxVo.getLoanBal());
					// 結案記號 1.正常結案 2.提前結案
					if (baTxVo.getCloseFg() > this.closeFg) {
						this.closeFg = baTxVo.getCloseFg();
					}
					// 有未結案
					if (baTxVo.getCloseFg() == 0) {
						isCloseFg = false;
					}
				}
			}
			if (!isCloseFg) {
				this.closeFg = 0;
			}
		}

		// 應繳本利
		this.unPayLoan = baTxCom.getUnPayLoan();
		this.unPayTotal = this.unPayLoan;

		if ("0".equals(this.procStsCode) && baTxList != null && baTxList.size() != 0) {
			int i = 0;
			for (BaTxVo baTxVo : baTxList) {
				if (baTxVo.getDataKind() == 1 && baTxVo.getAcctAmt().compareTo(BigDecimal.ZERO) > 0) {
					// 應繳費用 = 1.應收費用+未收費用
					// +短繳期金
					// 全部應繳
					this.unPayTotal = this.unPayTotal.add(baTxVo.getUnPaidAmt()); // 全部應繳
					if (baTxVo.getRepayType() >= 4) {
						this.unPayFee = this.unPayFee.add(baTxVo.getUnPaidAmt());
						this.repayFee = this.repayFee.add(baTxVo.getAcctAmt());
					}
					if (baTxVo.getRepayType() <= 3) {
						this.unPayLoan = this.unPayLoan.add(baTxVo.getUnPaidAmt());
						this.repayLoan = this.repayLoan.add(baTxVo.getAcctAmt());
						this.shortfallInt = this.shortfallInt.add(baTxVo.getInterest()); // 短繳利息
						this.shortfallPrin = this.shortfallPrin.add(baTxVo.getPrincipal()); // 短繳本金
						// 短繳清償違約金(提前償還有即時清償違約金時寫入)
						this.shortCloseBreach = this.shortCloseBreach.add(baTxVo.getCloseBreachAmt());
					}
					switch (baTxVo.getRepayType()) {
					case 4: // 04-帳管費
						this.acctFee = this.acctFee.add(baTxVo.getAcctAmt());
						break;
					case 5: // 05-火險費
						this.fireFee = this.fireFee.add(baTxVo.getAcctAmt());
						if (this.fireFeeDate == 0 || baTxVo.getPayIntDate() < this.fireFeeDate) {
							this.fireFeeDate = baTxVo.getPayIntDate();
						}
						break;
					case 6: // 06-契變手續費
						this.modifyFee = this.modifyFee.add(baTxVo.getAcctAmt());
						break;
					case 7: // 07-法務費
						this.unPayFee = this.unPayFee.add(baTxVo.getUnPaidAmt());
						this.lawFee = this.lawFee.add(baTxVo.getAcctAmt());
						break;
					default:
						break;
					}
					// 未收還款類別費用、回收還款類別費用
					if (baTxVo.getRepayType() == this.repayType) {
						this.unPayRepayTypeFee = this.unPayRepayTypeFee.add(baTxVo.getUnPaidAmt());
					}
				}
				if (baTxVo.getDataKind() == 2) {
					if (baTxVo.getAcctAmt().compareTo(BigDecimal.ZERO) > 0) {
						this.repayLoan = this.repayLoan.add(baTxVo.getAcctAmt());
						this.principal = this.principal.add(baTxVo.getPrincipal());
						this.interest = this.interest.add(baTxVo.getInterest());
						this.delayInt = this.delayInt.add(baTxVo.getDelayInt());
						this.breachAmt = this.breachAmt.add(baTxVo.getBreachAmt());
						this.closeBreachAmt = this.closeBreachAmt.add(baTxVo.getCloseBreachAmt());
						if (this.intStartDate == 0 || baTxVo.getIntStartDate() < this.intStartDate) {
							this.intStartDate = baTxVo.getIntStartDate();
						}
						if (baTxVo.getIntEndDate() > this.intEndDate) {
							this.intEndDate = baTxVo.getIntEndDate();
						}
					}
					// 本息計算表
					i++;
					this.intListTempVo.put("FacmBormNo" + i, parse.IntegerToString(baTxVo.getFacmNo(), 3) + "-"
							+ parse.IntegerToString(baTxVo.getBormNo(), 3));
					this.intListTempVo.put("IntSEDate" + i,
							"" + baTxVo.getIntStartDate() + "-" + baTxVo.getIntEndDate());
					this.intListTempVo.put("Principal" + i, "" + baTxVo.getPrincipal());
					this.intListTempVo.put("Interest" + i, "" + baTxVo.getInterest());
					this.intListTempVo.put("DelayInt" + i, "" + baTxVo.getDelayInt());
					this.intListTempVo.put("BreachAmt" + i, "" + baTxVo.getBreachAmt());
					this.intListTempVo.put("CloseBreachAmt" + i, "" + baTxVo.getCloseBreachAmt());
					this.intListTempVo.put("Total" + i, "" + baTxVo.getAcctAmt());
				}

				if (baTxVo.getDataKind() == 3) {
					this.tavAmt = this.tavAmt.add(baTxVo.getUnPaidAmt());
					this.tmpAmt = this.tmpAmt.add(baTxVo.getAcctAmt());
				}
				if (baTxVo.getDataKind() == 5) {
					this.otrTavAmt = this.otrTavAmt.add(baTxVo.getUnPaidAmt());
				}
				// 另收費用
				if (baTxVo.getDataKind() == 6) {
					// 未到期火險費用
					if (baTxVo.getRepayType() == 5) {
						this.unOpenfireFee = this.unOpenfireFee.add(baTxVo.getUnPaidAmt());
					}
					if (baTxVo.getAcctAmt().compareTo(BigDecimal.ZERO) > 0) {
						this.unPayTotal = this.unPayTotal.add(baTxVo.getUnPaidAmt()); // 全部應繳
						this.unPayFee = this.unPayFee.add(baTxVo.getFeeAmt());
						this.repayFee = this.repayFee.add(baTxVo.getAcctAmt());
					}
				}
				// 當期未收火險費用
				if (this.repayType <= 2 || this.repayType >= 9) {
					if (baTxVo.getDataKind() == 1 && baTxVo.getAcctAmt().compareTo(BigDecimal.ZERO) == 0) {
						if (baTxVo.getRepayType() == 5) {
							this.unSettlefireFee = this.unSettlefireFee.add(baTxVo.getUnPaidAmt());
						}
					}
				}
			}
		}

		// 暫收指定額度
		this.tmpFacmNoX = baTxCom.getTmpFacmNoX();

		// 溢短繳
		this.shortAmt = baTxCom.getShortAmt();// 短繳(正值)
		this.overAmt = baTxCom.getOverAmt();// 溢繳(正值)
		this.overRpFacmNo = baTxCom.getOverRpFacmNo();// 溢短繳額度;

		// 溢短收記號 1->短收 2->溢收 3->溢收:整批入帳部分繳 (回收部分額度期款)
		this.overRpFg = 0;
		if (this.shortAmt.compareTo(BigDecimal.ZERO) > 0) {
			this.overRpFg = 1;
		} else {
			if (this.overAmt.compareTo(BigDecimal.ZERO) > 0) {
				this.overRpFg = 2;
			}
		}

		// 還款總金額
		this.repayTotal = this.repayLoan.add(this.repayFee);
	}

	/**
	 * 整批入帳明細
	 * 
	 * @return ArrayList of BaTxVo
	 */
	public ArrayList<BaTxVo> getBaTxList() {
		return baTxList;
	}

}
