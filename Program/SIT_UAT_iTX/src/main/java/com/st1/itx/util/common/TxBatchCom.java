package com.st1.itx.util.common;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
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
import com.st1.itx.db.domain.BatxDetail;
import com.st1.itx.db.domain.BatxDetailId;
import com.st1.itx.db.domain.BatxHead;
import com.st1.itx.db.domain.BatxHeadId;
import com.st1.itx.db.domain.EmpDeductDtl;
import com.st1.itx.db.domain.FacClose;
import com.st1.itx.db.domain.LoanBook;
import com.st1.itx.db.domain.TxErrCode;
import com.st1.itx.db.domain.TxRecord;
import com.st1.itx.db.domain.TxRecordId;
import com.st1.itx.db.service.BankDeductDtlService;
import com.st1.itx.db.service.BatxDetailService;
import com.st1.itx.db.service.BatxHeadService;
import com.st1.itx.db.service.EmpDeductDtlService;
import com.st1.itx.db.service.FacCloseService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.LoanBookService;
import com.st1.itx.db.service.TxErrCodeService;
import com.st1.itx.db.service.TxRecordService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.MySpring;
import com.st1.itx.util.common.data.BaTxVo;
import com.st1.itx.util.common.data.LoanCloseBreachVo;
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
//    帳還款類別 1.期款 2.部分償還 3.結案 4.帳管費 5.火險費 6.契變手續費 7.法務費 9.其他 11.債協匯入款 
//    企金戶:95101 ==> 還款類別: 00 處理狀態 : 2.人工處理 處理說明: 企金戶請變更<還款類別>後，重新檢核
//    個人戶期款:95102 ==> 最後一期(還款後本金餘額 =0 )=> 還款類別: 03-結案 ---->
//    個人戶還本:95103 ==>
//      1.與約定還本金額相同 ==> 還款類別:02-部分償還
//        (if LoanBook.BookAmt = txamt and status = 0: 未回收 & entryDate = LoanBook.BookDate)
//      2.執行過清償作業     ==> 還款類別:03-結案  (check FacClose exist)
//    債協匯入款:95105     ==> 11-債協匯入款
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
//             處理說明:<不足利息>,<積欠期款> 999,999,999,999 
//           2.回收金額  > 全部應繳 
//             處理狀態:4.檢核正常
//             處理說明:有溢繳款:999,999,999,999
//        case 02-部分償還 :
//           1.有清償違約金 
//             處理狀態:2.人工處理
//             處理說明:有清償違約金:999,999,999,999 		      
//           2.暫收可抵繳 < 未繳費用
//             處理狀態:2.人工處理
//             處理說明:尚有未繳費用:999,999,999,999 		      
//        case 03-結案 :
//           1.回收金額 + 暫收可抵繳 - 全部應繳 < 結案金額(不足金額)
//             處理狀態:2.人工處理
//             處理說明:<金額不足>999,999,999,999 	
//           2.提前結案需先執行 L2631-清償作業
//             處理狀態:2.人工處理
//             處理說明:提前結案請先執行 L2631-清償作業(L2077)
//           3.回收金額 + 暫收可抵繳 - 全部應繳 > 結案金額
//             處理狀態:2.人工處理
//             處理說明: 結案有溢繳款：999,999,999,999
//        case 04-帳管費 05-火險費 06-契變手續費 07-法務費
//           1.有回收該還款類別費用
//             處理狀態:4.檢核正常
//           2.無該還款類別未繳費用
//             處理狀態:2.人工處理
//             處理說明: 無該還款類別未繳費用
//           3.金額不足
//             處理狀態:2.人工處理
//             處理說明: ：金額不足: 999,999,999,999
//         case 09-其他
//             處理狀態::2.人工處理
//      		if (this.unPayLoan.compareTo(BigDecimal.ZERO) > 0)
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
 * 1.1 隔日訂正不處理(隔日訂正，來源科目回沖至暫收可抵繳)<BR>
 * 1.2 更新BankDeductDtl銀扣檔會計日<BR>
 * 1.3 更新EmpDeductDtl員工扣薪款檔會計日<BR>
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

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	public BaTxCom baTxCom;

	@Autowired
	LoanCloseBreachCom loanCloseBreachCom;

	@Autowired
	public AcNegCom acNegCom;

	@Autowired
	public TxAmlCom txAmlCom;

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
	public BankDeductDtlService bankDeductDtlService;

	@Autowired
	public EmpDeductDtlService empDeductDtlService;

	@Autowired
	public TxRecordService txRecordService;

	@Autowired
	public TxErrCodeService txErrCodeService;

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
//  回收還款類別費用
	private BigDecimal repayTypeFee = BigDecimal.ZERO;
//  未收還款類別費用
	private BigDecimal unPayRepayTypeFee = BigDecimal.ZERO;
//  費用
	private BigDecimal acctFee = BigDecimal.ZERO;
	private BigDecimal modifyFee = BigDecimal.ZERO;
	private BigDecimal fireFee = BigDecimal.ZERO;
	private BigDecimal lawFee = BigDecimal.ZERO;
	private BigDecimal shortfallInt = BigDecimal.ZERO;
	private BigDecimal shortfallPrin = BigDecimal.ZERO;
	private BigDecimal shortCloseBreach = BigDecimal.ZERO;
//  另收欠款= 6 未到期火險費用
	private BigDecimal unOpenfireFee = BigDecimal.ZERO;
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

//  費用還款類別 
	private int feeRepayType = 0;

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

// 額度還款應繳日
	private TempVo repayIntDateByFacmNoVo = new TempVo();

//	溢短收記號
	private int overRpFg = 0; // 1->溢短收記號 1->短收 2->溢收 3->溢收(整批入帳、部分繳款)

//  結案記號  1.正常結案  2.提前結案 
	private int closeFg = 0;
//  部分償還 
	private String includeIntFlag = null;// 是否內含利息
	private String unpaidIntFlag = null;// 利息是否可欠繳

//  清償原因 for 結案
	private String closeReasonCode = null;

//  是否領取清償證明(Y/N) for 結案
	private String collectFlag = "";

//  繳納方式 for 部分償還
	private String payMethod = null;

//  檢核訊息
	private String checkMsg = null;

//  錯誤訊息
	private String errorMsg = null;

//  處理狀態
	private String procStsCode = null;

//  TempVo
	TempVo tTempVo = new TempVo();

	@PostConstruct
	public void init() {
		this.unPayFee = BigDecimal.ZERO;
		this.acctFee = BigDecimal.ZERO;
		this.modifyFee = BigDecimal.ZERO;
		this.fireFee = BigDecimal.ZERO;
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
		this.repayTypeFee = BigDecimal.ZERO;
		this.unPayRepayTypeFee = BigDecimal.ZERO;
		this.unOpenfireFee = BigDecimal.ZERO;
		this.repayTotal = BigDecimal.ZERO;

		this.feeRepayType = 0;
		this.repayFacmNo = 0;
		this.repayBormNo = 0;
		this.overRpFacmNo = 0;
		this.repayType = 0;
		this.repayIntDate = 0;
		this.repayIntDateByFacmNoVo = new TempVo();
		this.closeFg = 0;
		this.closeReasonCode = null;
		this.collectFlag = "";
		this.payMethod = null;
		this.checkMsg = "";
		this.errorMsg = "";
		this.procStsCode = "";
		this.includeIntFlag = null;
		this.unpaidIntFlag = null;
		this.tTempVo = new TempVo();
	}

	@Override

	/**
	 * 入帳交易執行後，更新整批入帳檔明細處理狀態
	 */
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("TxBatchCom run ... " + titaVo);
		// 以批號、明細檔序號更新整批入帳檔
		if ("L32".equals(titaVo.getTxCode().substring(0, 3)) || "L34".equals(titaVo.getTxCode().substring(0, 3))) {
			if (titaVo.get("BATCHNO") != null && titaVo.get("BATCHNO").trim().length() == 6
					&& "BATX".equals(titaVo.get("BATCHNO").substring(0, 4)) && titaVo.get("RpDetailSeq1") != null) {
				// 隔日訂正，回沖至暫收可抵繳，不處理
				if (titaVo.isHcodeErase() && titaVo.getEntDyI() != titaVo.getOrgEntdyI()) {
					this.info("TxBatchCom run  隔日訂正不處理 " + titaVo);
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
		this.info("TxBatchCom txCheck .... checkBatxDetail" + tDetail.toString());
		baTxCom.setTxBuffer(this.getTxBuffer());
		acNegCom.setTxBuffer(this.getTxBuffer());
		loanCloseBreachCom.setTxBuffer(this.txBuffer);

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
		initialProcNote(tempVo.getVo(tDetail.getProcNote()), titaVo);

		// --------------- 執行 AML 交易檢核(匯款轉帳及支票兌現) ------------------
		// 檢核狀態 0.非可疑名單/已完成名單確認 1.需審查/確認 2.為凍結名單/未確定名單
		// 如為 1,2，需再讀取AmlLog檔的最新確認狀態
		// 匯款轉帳： 1.借款人 2.交易人
		// 支票兌現： 1.借款人 2.發票人
		if (tDetail.getRepayCode() == 1 || tDetail.getRepayCode() == 4) {
			if ("1".equals(this.tTempVo.get("AmlRsp1")) || "1".equals(this.tTempVo.get("AmlRsp2"))
					|| "2".equals(this.tTempVo.get("AmlRsp1")) || "2".equals(this.tTempVo.get("AmlRsp2"))) {
				txAmlCom.setTxBuffer(this.getTxBuffer());
				this.tTempVo = txAmlCom.batxCheck(this.tTempVo, tDetail, titaVo);
				if ("2".equals(this.tTempVo.get("AmlRsp1")) || "2".equals(this.tTempVo.get("AmlRsp2"))) {
					this.checkMsg = "AML姓名檢核：為凍結名單/未確定名單";
					this.procStsCode = "3"; // 3.檢核錯誤
				} else if ("1".equals(this.tTempVo.get("AmlRsp1")) || "1".equals(this.tTempVo.get("AmlRsp2"))) {
					this.checkMsg = "AML姓名檢核：需審查/確認";
					this.procStsCode = "3"; // 3.檢核錯誤
				}
			}
		}
		this.info("TxBatchCom txCheck " + this.procStsCode + "," + this.repayType);

		// --------------- 預設匯款轉帳還款類別 ------------------
		if ("0".equals(this.procStsCode) && this.repayType == 0) {
			if (tDetail.getRepayCode() == 1) {
				bankRmtRepayType(tDetail, titaVo);
			}
		}

		// --------------- 執行債協匯入款戶號檢核 ------------------
		if ("0".equals(this.procStsCode) && this.repayType == 11) {
			if (acNegCom.isNegCustNo(tDetail.getCustNo(), titaVo)) {
				this.procStsCode = "4"; // 4.檢核正常
			} else {
				this.checkMsg = "非債協戶或債協戶非正常戶";
				this.procStsCode = "3"; // 3.檢核錯誤
			}
		}

		// --------------- 結案清償作業設定 ------------------
		if ("0".equals(this.procStsCode) && tDetail.getRepayType() == 3) {
			facCloseRepayType(tDetail, titaVo);
		}
		// --------------- 部分償還作業設定 ------------------
		if ("0".equals(this.procStsCode) && tDetail.getRepayType() == 2) {
			loanBookRepayType(tDetail, titaVo);
		}

		// ------------- 執行應繳試算，依結果重設還款類別，再依還款類別進行檢核 ----------------------
		if ("0".equals(this.procStsCode)) {
			// step01. 應繳試算
			settingUnPaid(tDetail, titaVo);

			// step02. 匯款轉帳未設定還款類別，與未收費用金額相同，則設定為費用類別
			if ("0".equals(this.procStsCode) && tDetail.getRepayCode() == 1) {
				bankRmtRepayFee(tDetail, titaVo);
			}
			// step03. 01-期款，最後一期，還款類別為03-結案
			if ("0".equals(this.procStsCode) && this.repayType == 1 && this.closeFg > 0) {
				this.repayType = 3;
			}

			// step04. 依還款類別檢核
			if ("0".equals(this.procStsCode)) {
				settingprocStsCode(tDetail, titaVo);
			}
		}

		// ------------- 將檢核結果存入整批入帳明細檔(還款類別、處理說明、處理狀態) -----------
		// 檢核正常
		if ("0".equals(this.procStsCode)) {
			this.procStsCode = "4"; // 4.檢核正常
		}
		// 設定還款類別
		if ("4".equals(this.procStsCode) && this.repayType > 0 && tDetail.getRepayType() == 0) {
			tDetail.setRepayType(this.repayType);
		}
		// 設定處理說明
		settingProcNote(tDetail, titaVo);

		this.info("this.tTempVo.getJsonString=" + this.tTempVo.getJsonString());
		tDetail.setProcNote(this.tTempVo.getJsonString());
		// 設定處理狀態
		tDetail.setProcStsCode(this.procStsCode);
		// 還款總金額
		tDetail.setAcquiredAmt(this.repayTotal);

		// end
		return tDetail;

	}

	/**
	 * 組入帳交易電文
	 * 
	 * @param functionCode 0:入帳 1:訂正 2.虛擬轉暫收
	 * @param tDetail      整批入帳明細檔
	 * @param iTotalcnt    總筆數
	 * @param titaVo       TitaVo
	 * @return TitaVo
	 * @throws LogicException ... LogicException
	 */
	public TitaVo txTita(int functionCode, BatxDetail tDetail, int iTotalcnt, TitaVo titaVo) throws LogicException {
		this.info("TxBatchCom txTita .... functionCode=" + functionCode + tDetail.toString());
// 功能 0:入帳 1:訂正 2.虛擬轉暫收
		int hcode = 0;
		if (functionCode == 1) {
			hcode = 1;
		}
		// TempVo
		this.tTempVo = new TempVo();
		this.tTempVo = this.tTempVo.getVo(tDetail.getProcNote());

// TXTNO = BatchNo[2] + TxtSeq [6]
// TxtSeq = EraseAddNo * 10000 + mod (DetailSeq 
// EraseAddNo = if EraseCnt = 0  then 0 else =  EraseCnt * 2  + TotalCnt / 10000 
//		                          EraseAddNo  
//		        EraseCnt     Normal      Erase
// Normal          0           0                
// Erase           0                       4       
// Normal          1           5      
// Erase           1                       6       
// Normal          2           7 

		int eraseAddNo = 0;
		if (this.tTempVo.get("EraseCnt") != null) {
			eraseAddNo = ((parse.stringToInteger(tTempVo.get("EraseCnt")) * 2) + (iTotalcnt / 10000));
		}
		if (hcode == 1) {
			eraseAddNo++;
		}
		String txtNo = tDetail.getBatchNo().substring(4, 6)
				+ parse.IntegerToString(tDetail.getDetailSeq() + eraseAddNo * 10000, 6);

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
			txTitaVo.putParam("TXTNO", txtNo);
			txTitaVo.putParam("ORGTLR", tDetail.getTitaTlrNo()); // 原經辦
			txTitaVo.putParam("ORGTNO", tDetail.getTitaTxtNo()); // 原交易序號
			txTitaVo.putParam("HCODE", "1"); // 訂正交易
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

		// 交易戶號
		String MRKEY = parse.IntegerToString(tDetail.getCustNo(), 7);
		if (tDetail.getFacmNo() > 0) {
			MRKEY = MRKEY + "-" + parse.IntegerToString(tDetail.getFacmNo(), 3) + "    ";
		} else {
			MRKEY = MRKEY + "    " + "    ";
		}
		txTitaVo.putParam("MRKEY", MRKEY);

		// 交易金額
		txTitaVo.putParam("TXAMT", tDetail.getRepayAmt());

		//
		txTitaVo.putParam("HCODE", "0"); // 正常交易
		txTitaVo.putParam("RELCD", "1"); // 一段式
		txTitaVo.putParam("ACTFG", "0"); // 登錄
		txTitaVo.putParam("CRDB", "2"); // 借貸別 2.貸
		txTitaVo.putParam("SECNO", "09"); // 業務別 09-放款

		// 還款額度
		if (this.tTempVo.get("FacmNo") != null) {
			this.repayFacmNo = parse.stringToInteger(this.tTempVo.get("FacmNo"));
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
			txTitaVo = setL3210Tita(txTitaVo, tDetail);
		}
		// 匯款同戶號合併，非最後一筆轉暫收
		else if (this.tTempVo.get("MergeCnt") != null
				&& !this.tTempVo.get("MergeSeq").equals(this.tTempVo.get("MergeCnt"))) {
			txTitaVo.putParam("MergeCnt", this.tTempVo.get("MergeCnt"));
			txTitaVo = setL3210Tita(txTitaVo, tDetail);
		}
		// 1:期款 2:部分償還 12:催收收回
		else if (this.repayType == 1 || this.repayType == 2 || this.repayType == 12) {
			txTitaVo = setL3200Tita(txTitaVo, tDetail);
		}
		// 3:結案
		else if (this.repayType == 3) {
			txTitaVo = setL3420Tita(txTitaVo, tDetail);
		}
		// 暫收
		else {
			txTitaVo = setL3210Tita(txTitaVo, tDetail);
		}

		// 支票兌現
		String iChequeAcct = "";
		String iChequeNo = "";
		// 030102806 0760500
		// 01234567890123456
		if (tDetail.getRepayCode() == 4) {
			iChequeAcct = tDetail.getRvNo().substring(0, 9);
			iChequeNo = tDetail.getRvNo().substring(10, 17);
		}
		txTitaVo.putParam("ChequeName", "");
		txTitaVo.putParam("ChequeAcct", iChequeAcct);
		txTitaVo.putParam("ChequeNo", iChequeNo);
		txTitaVo.putParam("ChequeDate", 0);

		// 收付欄
		txTitaVo.putParam("RpFlag", "1"); // 1:應收
		txTitaVo.putParam("RpType1", tDetail.getRepayType());
		int i = 1;
		txTitaVo.putParam("RpCode1", tDetail.getRepayCode());
		txTitaVo.putParam("RpAmt1", tDetail.getRepayAmt());
		txTitaVo.putParam("RpAcctCode1", tDetail.getReconCode());
		txTitaVo.putParam("RpAcCode1", tDetail.getRepayAcCode());
		txTitaVo.putParam("RpCustNo1", tDetail.getCustNo());
		txTitaVo.putParam("RpFacmNo1", repayFacmNo);
		txTitaVo.putParam("RpDetailSeq1", tDetail.getDetailSeq());
		txTitaVo.putParam("RpEntryDate1", tDetail.getEntryDate());
		txTitaVo.putParam("RpRvno1", tDetail.getRvNo());
		txTitaVo.putParam("RpDscpt1", this.tTempVo.get("DscptCode")); // 摘要代碼
		txTitaVo.putParam("RpNote1", this.tTempVo.get("Note")); // 摘要 for 收付欄分錄之傳票摘要
		txTitaVo.putParam("RpRemark1", this.tTempVo.get("Remark")); // 備註 for 暫收款分錄(暫收款登錄)之傳票摘要

		// 短繳金額
		String shortAmt = null;
		// 溢繳金額
		String overAmt = null;
		if ("L3420".equals(txTitaVo.getTxcd()) || "L3200".equals(txTitaVo.getTxcd())) {
			// 來源金額為零(暫收抵繳還期款)時，第一欄是暫收抵繳欄
			if (tDetail.getRepayAmt().compareTo(BigDecimal.ZERO) > 0) {
				i++;
			}
			// 暫收抵繳欄
			String amt = null;
			for (int j = 1; j <= 50; j++) {
				amt = this.tTempVo.get("TmpAmt" + j);
				if (amt == null)
					break;
				txTitaVo.putParam("RpCode" + i, "90");
				txTitaVo.putParam("RpAmt" + i, amt);
				txTitaVo.putParam("RpFacmNo" + i, this.tTempVo.get("TmpFacmNo" + j));
				this.info("RpAmt:" + amt);

				i++;
			}

			// 本金、利息
			txTitaVo.putParam("TimPrincipal", this.tTempVo.get("Principal"));
			txTitaVo.putParam("TimInterest", this.tTempVo.get("Interest"));
			txTitaVo.putParam("TimDelayInt", this.tTempVo.get("DelayInt"));
			txTitaVo.putParam("TimBreachAmt", this.tTempVo.get("BreachAmt"));
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
			putFeeAmt("AcctFee", "AcctFee1", txTitaVo);
			putFeeAmt("ModifyFee", "ModifyFee1", txTitaVo);
			putFeeAmt("FireFee", "FireFee1", txTitaVo);
			putFeeAmt("LawFee", "LawFee1", txTitaVo);
			putFeeAmt("ShortfallInt", "ShortfallInt", txTitaVo);
			putFeeAmt("ShortfallPrin", "ShortfallPrin", txTitaVo);
			putFeeAmt("ShortCloseBreach", "ShortCloseBreach", txTitaVo);
		} else if ("L3200".equals(txTitaVo.getTxcd())) {
			putFeeAmt("AcctFee", "TimAcctFee", txTitaVo);
			putFeeAmt("ModifyFee", "TimModifyFee", txTitaVo);
			putFeeAmt("FireFee", "TimFireFee", txTitaVo);
			putFeeAmt("LawFee", "TimLawFee", txTitaVo);
			putFeeAmt("ShortfallInt", "TimShortfallInt", txTitaVo);
			putFeeAmt("ShortfallPrin", "TimShortfallPrin", txTitaVo);
			putFeeAmt("ShortCloseBreach", "TimShortCloseBreach", txTitaVo);
		} else {
			putFeeAmt("AcctFee", "AcctFee", txTitaVo);
			putFeeAmt("ModifyFee", "ModifyFee", txTitaVo);
			putFeeAmt("FireFee", "FireFee", txTitaVo);
			putFeeAmt("LawFee", "LawFee", txTitaVo);
		}
		return txTitaVo;
	}

	private void putFeeAmt(String item, String itemName, TitaVo txTitaVo) {
		String amt = this.tTempVo.get(item);
		if (amt == null)
			txTitaVo.putParam(itemName, BigDecimal.ZERO);
		else
			txTitaVo.putParam(itemName, amt);
	}

	/* L3210 暫收款登錄 */
	private TitaVo setL3210Tita(TitaVo l3210TitaVo, BatxDetail tBatxDetail) throws LogicException {
		int iReasonCode = 0;
//		ReasonCode	     RepayType	         RepayCode 
//
//		00債協暫收款	     11.債協匯入款 
//		01溢繳		     X	
//		02不足利息		 01.期款		  
//		03期票		     X	
//		04本金異動	     X	
//		05積欠期款		 01.期款		   
//		06即期票現金          X	
//		07火險、帳管	     04.帳管費、05.火險費	
//		08兌現票入帳	 	                      04.支票兌現      
//		09其他		     09.其他	
//      10AML凍結／未確定 X

//      AmlRsp1 0.非可疑名單/已完成名單確認 1.需審查/確認 2.為凍結名單/未確定名單		
		if ("2".equals(this.tTempVo.get("AmlRsp1")) || "2".equals(this.tTempVo.get("AmlRsp2")))
			iReasonCode = 10;
		else if ("1".equals(this.tTempVo.get("AmlRsp1")) || "1".equals(this.tTempVo.get("AmlRsp2")))
			throw new LogicException(l3210TitaVo, "E0022", ""); // E0022 該筆資料需進行AML審查/確認
		else if (tBatxDetail.getRepayCode() == 4)
			iReasonCode = 8;
		else if (tBatxDetail.getRepayType() == 11
				|| tBatxDetail.getCustNo() == this.txBuffer.getSystemParas().getNegDeptCustNo())
			iReasonCode = 0;
		else if (tBatxDetail.getRepayType() == 4 || tBatxDetail.getRepayType() == 5)
			iReasonCode = 7;
		else if (this.tTempVo.get("CheckMsg").length() >= 4
				&& "不足利息".equals(this.tTempVo.get("CheckMsg").substring(0, 4)))
			iReasonCode = 2;
		else if (this.tTempVo.get("CheckMsg").length() >= 4
				&& "積欠期款".equals(this.tTempVo.get("CheckMsg").substring(0, 4)))
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
			if (!"".equals(this.tTempVo.getParam("FacmNo"))) {
				l3210TitaVo.putParam("FacmNo", this.tTempVo.getParam("FacmNo"));
			} else if (!"".equals(this.tTempVo.getParam("OverRpFacmNo"))) {
				l3210TitaVo.putParam("FacmNo", this.tTempVo.getParam("OverRpFacmNo"));
			}
		}
		l3210TitaVo.putParam("EntryDate", tBatxDetail.getEntryDate());
		l3210TitaVo.putParam("CurrencyCode", "TWD");
		l3210TitaVo.putParam("TimTempAmt", tBatxDetail.getRepayAmt());
		l3210TitaVo.putParam("TempReasonCode", iReasonCode);
		l3210TitaVo.putParam("TempSourceCode", iSourceCode);
		l3210TitaVo.putParam("AreaCode", "");
		l3210TitaVo.putParam("BankCode", "");
		l3210TitaVo.putParam("BktwFlag", "N");
		l3210TitaVo.putParam("TsibFlag", "N");
		l3210TitaVo.putParam("MediaFlag", "N");
		l3210TitaVo.putParam("OutsideCode", 1);
		l3210TitaVo.putParam("UsageCode", "");
		l3210TitaVo.putParam("ServiceCenter", "");
		l3210TitaVo.putParam("CreditorId", "");
		l3210TitaVo.putParam("CreditorBankCode", "");
		l3210TitaVo.putParam("OtherAcctCode", "");
		l3210TitaVo.putParam("ReceiptNo", "");

		return l3210TitaVo;
	}

	/* L3200 回收登錄 */
	private TitaVo setL3200Tita(TitaVo l3200TitaVo, BatxDetail tBatxDetail) throws LogicException {
		// 1.期款 2.部分償還 12.催收收回
		l3200TitaVo.putParam("TXCD", "L3200");
		l3200TitaVo.putParam("TXCODE", "L3200");
		l3200TitaVo.putParam("CustNo", tBatxDetail.getCustNo());
		l3200TitaVo.putParam("TimCustNo", tBatxDetail.getCustNo());
		if (!"".equals(this.tTempVo.getParam("FacmNo"))) {
			l3200TitaVo.putParam("FacmNo", this.tTempVo.getParam("FacmNo"));
		} else {
			l3200TitaVo.putParam("FacmNo", tBatxDetail.getFacmNo());
		}
		if (!"".equals(this.tTempVo.getParam("BormNo"))) {
			l3200TitaVo.putParam("BormNo", this.tTempVo.getParam("BormNo"));
		} else {
			l3200TitaVo.putParam("BormNo", "0");
		}
		l3200TitaVo.putParam("RepayTerms", "0"); // 回收期數
		l3200TitaVo.putParam("EntryDate", tBatxDetail.getEntryDate()); // 入帳日期
		l3200TitaVo.putParam("RepayType", tBatxDetail.getRepayType());
		l3200TitaVo.putParam("TimRepayAmt", tBatxDetail.getRepayAmt());
		l3200TitaVo.putParam("TwRepayAmt", tBatxDetail.getRepayAmt());
		l3200TitaVo.putParam("UsRepayAmt", tBatxDetail.getRepayAmt());
		l3200TitaVo.putParam("TimCloseBreachAmt", "0"); // 清償違約金
		l3200TitaVo.putParam("RqspFlag", ""); // 減免金額超過限額，Y.需主管核可
		l3200TitaVo.putParam("ShortPrinPercent", "0"); // 短收本金比率
		l3200TitaVo.putParam("ShortIntPercent", "0"); // 短收利息比率
		if (tBatxDetail.getRepayType() == 2) {
			l3200TitaVo.putParam("TimExtraRepay", tBatxDetail.getRepayAmt()); // 部分償還金額
			l3200TitaVo.putParam("TwExtraRepay", tBatxDetail.getRepayAmt());
			l3200TitaVo.putParam("UsExtraRepay", tBatxDetail.getRepayAmt());
			l3200TitaVo.putParam("IncludeIntFlag", this.tTempVo.getParam("IncludeIntFlag")); // 是否內含利息
			l3200TitaVo.putParam("UnpaidIntFlag", this.tTempVo.getParam("UnpaidIntFlag")); // 利息是否可欠繳
			l3200TitaVo.putParam("PayMethod", this.tTempVo.getParam("PayMethod"));// 繳納方式 1.減少每期攤還金額 2.縮短應繳期數
			if ("".equals(this.tTempVo.getParam("CloseBreachAmt"))) {
				l3200TitaVo.putParam("TimExtraCloseBreachAmt", "0");
			} else {
				l3200TitaVo.putParam("TimExtraCloseBreachAmt", this.tTempVo.getParam("CloseBreachAmt"));
			}
		} else {
			l3200TitaVo.putParam("TimExtraRepay", "0");
			l3200TitaVo.putParam("IncludeIntFlag", " ");
			l3200TitaVo.putParam("UnpaidIntFlag", " ");
			l3200TitaVo.putParam("PayMethod", " ");
		}
		if (tBatxDetail.getRepayType() == 12)
			l3200TitaVo.putParam("TimOvduRepay", tBatxDetail.getRepayAmt());
		else
			l3200TitaVo.putParam("TimOvduRepay", "0");
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
		if (!"".equals(this.tTempVo.getParam("FacmNo"))) {
			l3420TitaVo.putParam("FacmNo", this.tTempVo.getParam("FacmNo"));
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
		if ("".equals(this.tTempVo.getParam("CloseReasonCode")))
			l3420TitaVo.putParam("AdvanceCloseCode", "00"); // 00-無
		else
			l3420TitaVo.putParam("AdvanceCloseCode", this.tTempVo.getParam("CloseReasonCode"));
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
		// 存入暫收可抵繳金額
		BigDecimal disacctAmt = BigDecimal.ZERO;

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
						if ("TAV".equals(ac.getAcctCode()) && "C".equals(ac.getDbCr()))
							disacctAmt = disacctAmt.add(ac.getTxAmt());
					}
				}
				tDetail.setDisacctAmt(disacctAmt);
				tDetail.setAcctAmt(tDetail.getRepayAmt().subtract(disacctAmt)); // 還款金額 - 入暫收金額
				unfinishCnt = -1;
				if (titaVo.isTrmtypBatch()) { // 批次入帳
					// 轉暫收功能
					if ("2".equals(titaVo.get("FunctionCode"))) {
						tDetail.setProcStsCode("7"); // 7.轉暫收
					} else {
						tDetail.setProcStsCode("6"); // 6.批次入帳
					}
				} else {
					if ("L3210".equals(titaVo.getTxcd())) {
						tDetail.setProcStsCode("7"); // 7.轉暫收
					} else {
						tDetail.setProcStsCode("5");
					}
				}
				acDate = this.txBuffer.getTxCom().getTbsdy();
				tDetail.setTitaTlrNo(titaVo.getTlrNo());
				tDetail.setTitaTxtNo(titaVo.getTxtNo());
			} else {
				// 訂正交易
				unfinishCnt = 1;
				this.info("updBatxResult 訂正交易");
				tDetail.setProcStsCode("0"); // 訂正後為未檢核，需重新檢核或刪除
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

			// 02.銀行扣款
			if (tDetail.getRepayCode() == 2) {
				updBankDeductDtl(acDate, tDetail, titaVo);
			}
			// 03.員工扣款
			if (tDetail.getRepayCode() == 3) {
				updEmpDeductDtl(acDate, tDetail, titaVo);
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
			if ("0".equals(tDetail.getProcStsCode())) {
				tBatxHead.setBatxExeCode("0");// 0.待檢核
			}
			try {
				batxHeadService.update(tBatxHead, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", "update BatxHead " + tBatxHead + e.getErrorMsg());
			}

			// 啟動背景作業－整批入帳完成(非整批入帳)
			if (tBatxHead.getUnfinishCnt() == 0 && !"6".equals(tDetail.getProcStsCode())) {
				TitaVo bs401TitaVo = new TitaVo();
				bs401TitaVo = (TitaVo) titaVo.clone();
				bs401TitaVo.putParam("FunctionCode", "3");// 處理代碼 3.檢核
				bs401TitaVo.putParam("AcDate", tBatxHead.getAcDate() - 19110000); // 會計日期
				bs401TitaVo.putParam("BatchNo", tBatxHead.getBatchNo());// 批號
				MySpring.newTask("BS401", this.txBuffer, bs401TitaVo);
			}
		}

	}

//		L4002Update BankDeductDtl 交易結束更新銀扣明細檔
	private void updBankDeductDtl(int acDate, BatxDetail tBatxDetail, TitaVo titaVo) throws LogicException {

//		BatxDetail's MediaDate MediaCode MediaSeq to find ACH/POST (僅一個媒體Table需產出多媒體File者才需區分MediaCode)
		Slice<BankDeductDtl> slBankDeductDtl = bankDeductDtlService.mediaSeqRng(tBatxDetail.getMediaDate() + 19110000,
				tBatxDetail.getMediaKind(), tBatxDetail.getMediaSeq(), this.index, Integer.MAX_VALUE, titaVo);
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

	}

//		L4002Update EmpDeductDtl  交易結束更新員工扣薪明細檔
	private void updEmpDeductDtl(int acDate, BatxDetail tBatxDetail, TitaVo titaVo) throws LogicException {

//		BatxDetail's MediaDate MediaCode MediaSeq to find 15/un15 
		List<EmpDeductDtl> lEmpDeductDtl = new ArrayList<EmpDeductDtl>();
		Slice<EmpDeductDtl> sEmpDeductDtl = empDeductDtlService.mediaSeqEq(tBatxDetail.getMediaDate() + 19110000,
				tBatxDetail.getMediaKind(), tBatxDetail.getMediaSeq(), this.index, Integer.MAX_VALUE, titaVo);
		BigDecimal txAmt = parse.stringToBigDecimal(titaVo.getTxAmt());
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

	/* 設定明細處理狀態 */
	private void settingprocStsCode(BatxDetail tBatxDetail, TitaVo titaVo) throws LogicException {
// this.repayType 還款類別 1.期款 2.部分償還 3.結案 4.帳管費 5.火險費 6.契變手續費 7.法務費 9.其他 11.債協匯入款(虛擬帳號為9510500NNNNNNN)
		switch (this.repayType) {
		// 無還款類別
		case 0:
			// 處理狀態: 3.檢核錯誤
			// 處理說明:請變更(還款類別)後重新檢核
			this.checkMsg = "請變更(還款類別)後重新檢核，";
			apendcheckMsgAmounts(tBatxDetail, titaVo);
			this.procStsCode = "3"; // 3.檢核錯誤
			break;
		// 01-期款
		case 1:
			// 回收金額 + 暫收可抵繳 - 全部應繳 < 期金
			// 處理狀態:2.人工處理
			// 處理說明:<不足利息>,<積欠期款> 999,999,999,999 期金:999,999,999,999 未繳費用:999,999,999,999
			if (this.unPayLoan.compareTo(BigDecimal.ZERO) == 0 && this.repayLoan.compareTo(BigDecimal.ZERO) == 0) {
				this.checkMsg = "需償還本利金額: 0";
				apendcheckMsgAmounts(tBatxDetail, titaVo);
				this.procStsCode = "2"; // 2.人工處理
				break;
			}
			if (this.repayIntDate == 0) {
				if (this.principal.compareTo(BigDecimal.ZERO) > 0) {
					this.checkMsg = "積欠期款: " + this.shortAmt;
				} else {
					this.checkMsg = "不足利息: " + this.shortAmt;
				}
				apendcheckMsgAmounts(tBatxDetail, titaVo);
				this.procStsCode = "2"; // 2.人工處理
				break;
			}
			// 處理狀態:4.檢核正常
			// if 回收金額 > 全部應繳 then 處理說明:有溢繳款:999,999,999,999
			this.procStsCode = "4"; // 4.檢核正常
			if (this.overAmt.compareTo(BigDecimal.ZERO) > 0) {
				this.checkMsg = this.checkMsg + "有溢繳款: " + this.overAmt;
			}

			if (this.shortAmt.compareTo(BigDecimal.ZERO) > 0) {
				this.checkMsg = this.checkMsg + "有短繳款: " + this.shortAmt;
			}
			apendcheckMsgAmounts(tBatxDetail, titaVo);
			break;
		// 02-部分償還
		case 2:
			// 有即時收取清償違約金
			// 處理狀態:2.人工處理
			// 處理說明:有清償違約金:999,999,999,999
			if (this.closeBreachAmt.compareTo(BigDecimal.ZERO) > 0) {
				this.checkMsg = "有即時收取清償違約金 : ";
				apendcheckMsgAmounts(tBatxDetail, titaVo);
				this.procStsCode = "2"; // 2.人工處理
				break;
			}
			// 撥款序號 = 0
			// 處理狀態:2.人工處理
			// 處理說明: 無可償還撥款資料
			if (this.repayLoan.compareTo(BigDecimal.ZERO) == 0) {
				this.checkMsg = "無可償還撥款資料 ";
				apendcheckMsgAmounts(tBatxDetail, titaVo);
				this.procStsCode = "2"; // 2.人工處理
				break;
			}

			// 繳納方式 1.減少每期攤還金額 2.縮短應繳期數
			if ("1".equals(this.payMethod)) {
				this.checkMsg = "減少每期攤還金額 ";
			}
			if ("2".equals(this.payMethod)) {
				this.checkMsg = "縮短應繳期數";
			}
			// 檢核正常
			this.procStsCode = "4"; // 4.檢核正常
			apendcheckMsgAmounts(tBatxDetail, titaVo);
			break;
		// 03-結案
		case 3:
			// 回收金額 + 暫收可抵繳 - 全部應繳 < 結案金額(不足金額)
			// 處理狀態:2.人工處理
			// 處理說明:<金額不足>999,999,999,999
			if (this.shortAmt.compareTo(BigDecimal.ZERO) > 0) {
				this.checkMsg = "金額不足: " + this.shortAmt;
				apendcheckMsgAmounts(tBatxDetail, titaVo);
				this.procStsCode = "2"; // 2.人工處理
				break;
			}
			// 提前結案需先執行 L2631-清償作業
			// 處理狀態:2.人工處理
			// 處理說明:提前結案請先執行 L2631-清償作業(L2077)
			if (this.closeFg == 2) {
				facCloseRepayType(tBatxDetail, titaVo);
				if (this.closeReasonCode == null) {
					this.checkMsg = "提前結案請先執行 L2631-清償作業(L2077)";
					this.procStsCode = "2"; // 2.人工處理
					break;
				}
			}
			// 檢核正常
			if (this.closeFg == 1) {
				this.checkMsg = "正常結案 ";
			} else {
				this.checkMsg = "提前結案 ";
			}

			// 結案有溢繳款：999,999,999,999
			if (this.overAmt.add(this.tavAmt).subtract(this.tmpAmt).compareTo(BigDecimal.ZERO) > 0) {
				this.checkMsg += ", 有溢繳款: " + this.overAmt.add(this.tavAmt).subtract(this.tmpAmt);
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
			if (this.repayTypeFee.compareTo(BigDecimal.ZERO) > 0) {
				apendcheckMsgAmounts(tBatxDetail, titaVo);
				this.procStsCode = "4"; // 4.檢核正常
				break;
			}
			// 無該還款類別未繳費用
			// 處理狀態:2.人工處理
			// 處理說明: 無該還款類別未繳費用
			if (this.unPayRepayTypeFee.compareTo(BigDecimal.ZERO) == 0) {
				this.checkMsg = "無該還款類別應繳費用 ";
				apendcheckMsgAmounts(tBatxDetail, titaVo);
				this.procStsCode = "2"; // 2.人工處理
				break;
			}
			// 金額不足
			// 處理狀態:2.人工處理
			// 處理說明: ：金額不足: 999,999,999,999
			if (this.repayTypeFee.compareTo(BigDecimal.ZERO) == 0) {
				this.checkMsg = "金額不足: "
						+ this.unPayRepayTypeFee.subtract(tBatxDetail.getRepayAmt().subtract(this.tavAmt));
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
		// 去起頭的逗號
		if (this.checkMsg.length() > 2 && this.checkMsg.startsWith(", ")) {
			String str = this.checkMsg.substring(2);
			this.checkMsg = str;
		}
		this.info("log output");
		this.info("還款戶號額度                              = " + tBatxDetail.getCustNo() + "-" + this.repayFacmNo + "-"
				+ this.repayBormNo);
		this.info("還款類別 repayType           = " + this.repayType);
		this.info("還款金額 RepayAmt            = " + tBatxDetail.getRepayAmt());
		this.info("還款總金額 RepayTotal        = " + this.repayTotal);
		this.info("全部應繳 unPayTotal          = " + this.unPayTotal);
		this.info("應繳費用 unPayFee            = " + this.unPayFee);
		this.info("應繳本利 unPayLoan           = " + this.unPayLoan);
		this.info("償還本利 repayLoan           = " + this.repayLoan);
		this.info("應繳日 repayIntDat           = " + this.repayIntDate + " ," + this.repayIntDateByFacmNoVo.toString());
		this.info("償還本金 Principal           = " + this.principal);
		this.info("清償違約金 closeBreachAmt    = " + this.closeBreachAmt);
		this.info("回收費用 repayFee            = " + this.repayFee);
		this.info("暫收可抵繳 tavAmt            = " + this.tavAmt);
		this.info("暫收抵繳 tmpAmt              = " + this.tmpAmt);
		this.info("短繳金額 shortAmt            = " + this.shortAmt);
		this.info("溢繳金額 overAmt             = " + this.overAmt);
		this.info("溢繳款額度 OverRpFacmNo      = " + this.overRpFacmNo + ", 溢短收記號=" + this.overRpFg);
		this.info("其他額度暫收可抵繳 otrTavAmt = " + this.otrTavAmt);
		this.info("費用還款類別 feeRepayType    = " + this.feeRepayType);
		this.info("費用還款類別 feeRepayType    = " + this.feeRepayType);
	}

	/* append this.checkMsg Amount Field */
	private void apendcheckMsgAmounts(BatxDetail tBatxDetail, TitaVo titaVo) throws LogicException {
		if (this.tTempVo.get("MergeAmt") != null) {
			this.checkMsg = this.checkMsg + ", 同戶號合併檢核 總金額:" + this.tTempVo.get("MergeAmt");
		}
		if (this.repayFacmNo != tBatxDetail.getFacmNo())
			this.checkMsg = this.checkMsg + ", 額度:" + parse.IntegerToString(this.repayFacmNo, 3);
		if (this.repayBormNo > 0)
			this.checkMsg = this.checkMsg + ", 撥款:" + parse.IntegerToString(this.repayBormNo, 3);
		// 應繳本利 > 償還本利 => 顯示應繳本利
		if (this.unPayLoan.compareTo(this.repayLoan) > 0)
			this.checkMsg = this.checkMsg + ", 應繳本利:" + this.unPayLoan;
		if (this.repayLoan.compareTo(BigDecimal.ZERO) > 0)
			this.checkMsg = this.checkMsg + ", 償還本利:" + this.repayLoan;
		if (this.repayLoan.compareTo(BigDecimal.ZERO) > 0)
			this.checkMsg = this.checkMsg + ", 償還本利:" + this.repayLoan;
		if (this.shortfallInt.compareTo(BigDecimal.ZERO) > 0)
			this.checkMsg = this.checkMsg + ", 累短收利息:" + this.shortfallInt;
		if (this.shortfallPrin.compareTo(BigDecimal.ZERO) > 0)
			this.checkMsg = this.checkMsg + ", 累短收本金:" + this.shortfallPrin;
		if (this.shortCloseBreach.compareTo(BigDecimal.ZERO) > 0)
			this.checkMsg = this.checkMsg + ", 累短收清償違約金:" + this.shortCloseBreach;
		if (this.acctFee.compareTo(BigDecimal.ZERO) > 0)
			this.checkMsg = this.checkMsg + ", 帳管費:" + this.acctFee;
		if (this.fireFee.compareTo(BigDecimal.ZERO) > 0)
			this.checkMsg = this.checkMsg + ", 火險費:" + this.fireFee;
		if (this.modifyFee.compareTo(BigDecimal.ZERO) > 0)
			this.checkMsg = this.checkMsg + ", 契變手續費:" + this.modifyFee;
		if (this.lawFee.compareTo(BigDecimal.ZERO) > 0)
			this.checkMsg = this.checkMsg + ", 法務費:" + this.lawFee;
		if (this.closeBreachAmt.compareTo(BigDecimal.ZERO) > 0)
			this.checkMsg = this.checkMsg + ", 清償違約金:" + this.closeBreachAmt;
		if (this.unOpenfireFee.compareTo(BigDecimal.ZERO) > 0)
			this.checkMsg = this.checkMsg + ", 未到期火險費用:" + this.unOpenfireFee;

		// 暫收抵繳
		if (this.tmpAmt.compareTo(BigDecimal.ZERO) > 0) {
			this.checkMsg = this.checkMsg + ", 暫收抵繳:" + this.tmpAmt;
		}

		// 暫收可抵繳
		if (this.shortAmt.compareTo(BigDecimal.ZERO) > 0) {
			if (this.tavAmt.compareTo(BigDecimal.ZERO) > 0)
				this.checkMsg = this.checkMsg + ", 暫收可抵繳:" + this.tavAmt;
			if (this.otrTavAmt.compareTo(BigDecimal.ZERO) > 0)
				this.checkMsg = this.checkMsg + ", 其他額度暫收可抵繳:" + this.otrTavAmt;
		}
	}

	/* 匯款轉帳設定還款類別為費用類別 */
	private void bankRmtRepayFee(BatxDetail tBatxDetail, TitaVo titaVo) throws LogicException {
//		還款類別
//          3.與未收費用個別金額相同 ==> 取該項目還款類別 
//          4.其他 ==> baTxList = baTxCom.settingUnPaid(01-期款)
//            還款類別: 00 處理狀態  : 2.人工處理  
//            處理說明: 請變更(還款類別)後重新檢核，期金:999,999,999,999 未繳費用:999,999,999,999 暫收可抵繳:999,999,999,999 其他額度暫收可抵繳:999,999,999,999
		if (this.repayType == 0) {
			if (this.feeRepayType > 0) {
				this.repayType = this.feeRepayType; // 3.與未收費用個別金額相同 ==> 取該項目還款類別
			} else {
				this.procStsCode = "3"; // 3.檢核錯誤
				this.checkMsg = "請變更(還款類別)後重新檢核，";
				apendcheckMsgAmounts(tBatxDetail, titaVo);
			}
		}

	}

	/* 匯款轉帳依虛擬帳號設定還款類別 */
	private void bankRmtRepayType(BatxDetail tDetail, TitaVo titaVo) throws LogicException {
//		 1.期款 2.部分償還 3.結案 4.帳管費 5.火險費 6.契變手續費 7.法務費 9.其他 11.債協匯入款 
// 企金戶:95101 ==> 還款類別: 00 處理狀態 : 2.人工處理 處理說明: 企金戶請變更<還款類別>後，重新檢核
// 個人戶期款:95102 ==> 最後一期(還款後本金餘額 =0 )=> 還款類別: 03-結案 ---->
// 個人戶還本:95103 ==>
//          1.與大於等於約定還本金額 ==> 還款類別:02-部分償還
//            (if LoanBook.BookAmt = txamt and status = 0: 未回收 & entryDate = LoanBook.BookDate)
//          2.執行過清償作業     ==> 還款類別:03-結案  (check FacClose exist)
// 債協匯入款:95105     ==> 11-債協匯入款
		String VirtualAcctNo = this.tTempVo.getParam("VirtualAcctNo"); // 虛擬帳號
		if (VirtualAcctNo != null && VirtualAcctNo.length() >= 5)
			switch (VirtualAcctNo.substring(0, 5)) {
			case "95101":
				this.checkMsg = "企金戶請變更<還款類別>後，重新檢核";
				this.procStsCode = "2"; // 2.人工處理
				break;
			case "95102":
				this.repayType = 1;
				break;
			case "95103":
				// 1.大於等於約定還本金額 ==> 還款類別:02-部分償還
				if (this.repayType == 0) {
					loanBookRepayType(tDetail, titaVo);
				}
				// 2.執行過清償作業 ==> 還款類別:03-結案 (check FacClose exist)
				if (this.repayType == 0) {
					facCloseRepayType(tDetail, titaVo);
				}
				break;
			case "95105":
				// 11-債協匯入款
				this.repayType = 11;
				break;
			}
	}

	/* 1.大於約定還本金額 ==> 還款類別:02-部分償還 */
	private void loanBookRepayType(BatxDetail tDetail, TitaVo titaVo) throws LogicException {
		this.info("CASE 3-1!!!!");
		Slice<LoanBook> slLoanBook = loanBookService.bookCustNoRange(tDetail.getCustNo(), tDetail.getCustNo(),
				tDetail.getFacmNo(), tDetail.getFacmNo() > 0 ? tDetail.getFacmNo() : 999, 0, 990,
				tDetail.getEntryDate(), this.index, Integer.MAX_VALUE, titaVo);
		if (slLoanBook != null) {
			for (LoanBook tLoanBook : slLoanBook.getContent()) {
				if (tLoanBook.getStatus() == 0 && tDetail.getRepayAmt().compareTo(tLoanBook.getBookAmt()) >= 0) {
					this.repayType = 02; // 02-部分償還
					this.repayFacmNo = tLoanBook.getFacmNo(); // 還款額度
					this.repayBormNo = tLoanBook.getBormNo(); // 撥款序號
					break;
				}
			}
		}
	}

	/* 2.執行過清償作業 ==> 還款類別:03-結案 */
	private void facCloseRepayType(BatxDetail tDetail, TitaVo titaVo) throws LogicException {
		this.info("CASE 3-2!!!!");
		Slice<FacClose> facCloseList = facCloseService.findCustNo(tDetail.getCustNo(), this.index, Integer.MAX_VALUE,
				titaVo); // 清償作業檔
		// 還清金額需相同
		if (facCloseList != null) {
			for (FacClose tFacClose : facCloseList.getContent()) {
				if (tFacClose.getCloseDate() == 0 && tFacClose.getCloseAmt().equals(tDetail.getRepayAmt())) {
					this.repayType = 3; // 03-結案
					if (tDetail.getFacmNo() == 0) {
						this.repayFacmNo = tFacClose.getFacmNo(); // 還款額度
					}
					this.collectFlag = tFacClose.getCollectFlag();
					this.closeReasonCode = tFacClose.getCloseReasonCode(); // CloseReasonCode 清償原因
				}
			}
		}
	}

	/* 初始處理說明 */
	private void initialProcNote(TempVo t, TitaVo titaVo) throws LogicException {
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

	}

	/* 設定處理說明 */
	private void settingProcNote(BatxDetail tDetail, TitaVo titaVo) throws LogicException {
		this.tTempVo.putParam("CheckMsg", this.checkMsg);
		this.tTempVo.putParam("ErrorMsg", this.errorMsg);
		if (tDetail.getRepayType() != this.repayType)
			this.tTempVo.putParam("RepayType", this.repayType);
		if (tDetail.getFacmNo() == 0 && this.repayFacmNo > 0)
			this.tTempVo.putParam("FacmNo", this.repayFacmNo);
		if (this.repayBormNo > 0)
			this.tTempVo.putParam("BormNo", this.repayBormNo);
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
			this.tTempVo.putParam("closeFg", this.closeFg);
		if (this.closeReasonCode != null)
			this.tTempVo.putParam("CloseReasonCode", this.closeReasonCode);
		if (this.payMethod != null)
			this.tTempVo.putParam("PayMethod", this.payMethod);
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

		// 還款應繳日
		if (this.repayIntDate > 0) {
			this.tTempVo.putParam("RepayIntDate", this.repayIntDate);
			this.tTempVo.putParam("RepayIntDateByFacmNoVo", this.repayIntDateByFacmNoVo.getJsonString());
		}
		// 償還本利
		if (this.repayLoan.compareTo(BigDecimal.ZERO) > 0) {
			this.tTempVo.putParam("RepayLoan", this.repayLoan);
		}
		// 是否內含利息
		if (this.includeIntFlag != null) {
			this.tTempVo.putParam("IncludeIntFlag", this.includeIntFlag);
		}
		// 利息是否可欠繳
		if (this.unpaidIntFlag != null) {
			this.tTempVo.putParam("UnpaidIntFlag", this.unpaidIntFlag);
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

		ArrayList<LoanCloseBreachVo> iListCloseBreach = new ArrayList<LoanCloseBreachVo>();
		ArrayList<LoanCloseBreachVo> oListCloseBreach = new ArrayList<LoanCloseBreachVo>();
		baTxList = new ArrayList<BaTxVo>();
		// call 應繳試算，試算至會計日
		try {
			baTxList = baTxCom.settleUnPaid(tDetail.getEntryDate(), 0, tDetail.getCustNo(), this.repayFacmNo,
					this.repayBormNo, tDetail.getRepayCode(), this.repayType, tDetail.getRepayAmt(), titaVo);
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

		// 還款應繳日 (期款按期償還)
		if (this.repayType == 1) {
			this.repayIntDate = baTxCom.getRepayIntDate();
			this.repayIntDateByFacmNoVo = baTxCom.getRepayIntDateByFacmNoVo();
			this.info("PreRepayTermsBatch=" + baTxCom.getPreRepayTermsBatch());
		}

		// 部分償還
		if (this.repayType == 2) {
			this.includeIntFlag = baTxCom.getIncludeIntFlag();// 是否內含利息
			this.unpaidIntFlag = baTxCom.getUnpaidIntFlag();// 利息是否可欠繳
			this.payMethod = baTxCom.getPayMethod();// 繳納方式 1.減少每期攤還金額 2.縮短應繳期數
		}

		// 結案記號判斷
		boolean isCloseFg = true;

		if ("0".equals(this.procStsCode) && baTxList != null && baTxList.size() != 0) {
			for (BaTxVo baTxVo : baTxList) {
				if (baTxVo.getDataKind() == 1) {
					// 應繳費用 = 1.應收費用+未收費用+短繳期金
					this.unPayFee = this.unPayFee.add(baTxVo.getUnPaidAmt());
					// 全部應繳
					this.unPayTotal = this.unPayTotal.add(baTxVo.getUnPaidAmt()); // 全部應繳
					if (baTxVo.getAcctAmt().compareTo(BigDecimal.ZERO) > 0) {
						switch (baTxVo.getRepayType()) {
						case 1: // 01-期款
							this.shortfallInt = this.shortfallInt.add(baTxVo.getInterest()); // 短繳利息
							this.shortfallPrin = this.shortfallPrin.add(baTxVo.getPrincipal()); // 短繳本金
							this.shortCloseBreach = this.shortfallPrin.add(baTxVo.getCloseBreachAmt());// 短繳清償違約金
							break;
						case 4: // 04-帳管費
							this.acctFee = this.acctFee.add(baTxVo.getAcctAmt());
							break;
						case 5: // 05-火險費
							this.fireFee = this.fireFee.add(baTxVo.getAcctAmt());
							break;
						case 6: // 06-契變手續費
							this.modifyFee = this.modifyFee.add(baTxVo.getAcctAmt());
							break;
						case 7: // 07-法務費
							this.lawFee = this.lawFee.add(baTxVo.getAcctAmt());
							break;
						case 9: // 09-其他(清償違約金)
							this.closeBreachAmt = this.closeBreachAmt.add(baTxVo.getCloseBreachAmt());// 未收清償違約金
							break;
						default:
							break;
						}
						// 回收費用
						this.repayFee = this.repayFee.add(baTxVo.getAcctAmt());
					}
					// 費用還款類別
					if (this.feeRepayType == 0 && baTxVo.getUnPaidAmt().compareTo(tDetail.getRepayAmt()) == 0) {
						this.feeRepayType = baTxVo.getRepayType();
					}
					// 未收還款類別費用、回收還款類別費用
					if (baTxVo.getRepayType() == tDetail.getRepayType()) {
						this.unPayRepayTypeFee = this.unPayRepayTypeFee.add(baTxVo.getUnPaidAmt());
						this.repayTypeFee = this.repayTypeFee.add(baTxVo.getAcctAmt());
					}
				}
				if (baTxVo.getDataKind() == 2) {
					// 全部應繳
					if (baTxVo.getPayIntDate() <= this.txBuffer.getTxCom().getTbsdy()) {
						this.unPayTotal = this.unPayTotal.add(baTxVo.getUnPaidAmt());
						this.unPayLoan = this.unPayLoan.add(baTxVo.getUnPaidAmt());
					}
					this.loanBal = loanBal.add(baTxVo.getLoanBal());
					// 結案記號 1.正常結案 2.提前結案
					if (baTxVo.getCloseFg() > this.closeFg) {
						this.closeFg = baTxVo.getCloseFg();
					}
					// 有未結案
					if (baTxVo.getCloseFg() == 0) {
						isCloseFg = false;
					}
					if (baTxVo.getAcctAmt().compareTo(BigDecimal.ZERO) > 0) {
						this.repayLoan = this.repayLoan.add(baTxVo.getAcctAmt());
						this.principal = this.principal.add(baTxVo.getPrincipal());
						this.interest = this.principal.add(baTxVo.getInterest());
						this.delayInt = this.delayInt.add(baTxVo.getDelayInt());
						this.breachAmt = this.breachAmt.add(baTxVo.getBreachAmt());
					}
					// 提前還款金額 extraAmt = BigDecimal.ZERO; // 提前還款金額
					if (baTxVo.getExtraAmt().compareTo(BigDecimal.ZERO) > 0) {
						LoanCloseBreachVo v = new LoanCloseBreachVo();
						v.setCustNo(baTxVo.getCustNo());
						v.setFacmNo(baTxVo.getFacmNo());
						v.setBormNo(baTxVo.getBormNo());
						v.setExtraRepay(baTxVo.getExtraAmt());
						v.setEndDate(tDetail.getEntryDate());
						iListCloseBreach.add(v);
					}
				}
				if (baTxVo.getDataKind() == 3) {
					this.tavAmt = this.tavAmt.add(baTxVo.getUnPaidAmt());
					this.tmpAmt = this.tmpAmt.add(baTxVo.getAcctAmt());
				}
				if (baTxVo.getDataKind() == 5) {
					this.otrTavAmt = this.otrTavAmt.add(baTxVo.getUnPaidAmt());
				}
				if (baTxVo.getDataKind() == 6 && baTxVo.getRepayType() == 5) { // 未到期火險費用
					this.unOpenfireFee = this.unOpenfireFee.add(baTxVo.getUnPaidAmt());
				}
			}
			if (!isCloseFg) {
				this.closeFg = 0;
			}
		}

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

		// 計算清償違約金
		if (this.repayType == 3)

		{
			if ("Y".equals(this.collectFlag)) {
				oListCloseBreach = loanCloseBreachCom.getCloseBreachAmtAll(tDetail.getCustNo(), this.repayFacmNo, 0,
						iListCloseBreach, titaVo);
			} else {
				oListCloseBreach = loanCloseBreachCom.getCloseBreachAmtPaid(tDetail.getCustNo(), this.repayFacmNo, 0,
						iListCloseBreach, titaVo);
			}
			if (oListCloseBreach != null && oListCloseBreach.size() > 0) {
				for (LoanCloseBreachVo v : oListCloseBreach) {
					this.closeBreachAmt = this.closeBreachAmt.add(v.getCloseBreachAmt());
				}
				this.unPayTotal = this.unPayTotal.add(this.closeBreachAmt);
				this.repayTotal = this.repayTotal.add(this.closeBreachAmt);
			}
		}
		// 還款總金額
		this.repayTotal = this.repayLoan.add(this.repayFee).add(this.closeBreachAmt);
	}
}
