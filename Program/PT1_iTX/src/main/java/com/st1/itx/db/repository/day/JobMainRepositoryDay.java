package com.st1.itx.db.repository.day;


import java.util.Optional;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.JobMain;
import com.st1.itx.db.domain.JobMainId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JobMainRepositoryDay extends JpaRepository<JobMain, JobMainId> {

  // TxSeq =
  public Slice<JobMain> findAllByTxSeqIs(String txSeq_0, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<JobMain> findByJobMainId(JobMainId jobMainId);

  // (日終批次)更新TxHoliday
  @Procedure(value = "\"Usp_Tx_TxHoliday_Ins\"")
  public void uspTxTxholidayIns(String EmpNo);

  // (日終批次)更新員工檔
  @Procedure(value = "\"Usp_L6_CdEmp_Ins\"")
  public void uspL6CdempIns(String EmpNo);

  // (日終批次)更新組織檔
  @Procedure(value = "\"Usp_L6_CdBcm_Ins\"")
  public void uspL6CdbcmIns(String EmpNo);

  // (日終批次)更新離職員工檔
  @Procedure(value = "\"Usp_L6_QuitEmp_Ins\"")
  public void uspL6QuitempIns(String InputEmpNo);

  // (日終批次)維護 CustDataCtrl 結清戶個資控管檔
  @Procedure(value = "\"Usp_L2_CustDataCtrl_Ins\"")
  public void uspL2CustdatactrlIns(int tbsdyf,  String empNo);

  // (日終批次)維護 CollList 法催紀錄清單檔
  @Procedure(value = "\"Usp_L5_CollList_Upd\"")
  public void uspL5ColllistUpd(int tbsdyf,  String empNo,String txtNo, int l6bsdyf, int l7bsdyf);

  // (日終批次)維護 InnReCheck 覆審案件明細檔 
  @Procedure(value = "\"Usp_L5_InnReCheck_Upd\"")
  public void uspL5InnrecheckUpd(int tbsdyf,  String empNo);

  // (日終批次)維護 DailyLoanBal每日放款餘額檔
  @Procedure(value = "\"Usp_L9_DailyLoanBal_Upd\"")
  public void uspL9DailyloanbalUpd(int tbsdyf,  String empNo, int mfbsdyf);

  // (日終批次)維護 DailyLoanBal每日暫收款餘額檔
  @Procedure(value = "\"Usp_L9_DailyTav_Ins\"")
  public void uspL9DailytavIns(int tbsdyf,  String empNo);

  // (日終批次)維護 JcicB204每日聯徵授信日報資料檔
  @Procedure(value = "\"Usp_L8_JcicB204_Upd\"")
  public void uspL8Jcicb204Upd(int tbsdyf,  String empNo);

  // (日終批次)維護 JcicB211聯徵每日授信餘額變動資料檔
  @Procedure(value = "\"Usp_L8_JcicB211_Upd\"")
  public void uspL8Jcicb211Upd(int tbsdyf,  String empNo);

  // (月底日日終批次)更新 ForeclosureFinished 法拍完成檔
  @Procedure(value = "\"Usp_L2_ForeclosureFinished_Upd\"")
  public void uspL2ForeclosurefinishedUpd(int tbsdyf,  String empNo);

  // (月底日日終批次)維護MonthlyLoanBal每月放款餘額檔
  @Procedure(value = "\"Usp_L9_MonthlyLoanBal_Upd\"")
  public void uspL9MonthlyloanbalUpd(int tbsdyf,  String empNo);

  // (月底日日終批次)維護 MonthlyFacBal 額度月報工作檔
  @Procedure(value = "\"Usp_L9_MonthlyFacBal_Upd\"")
  public void uspL9MonthlyfacbalUpd(int tbsdyf,  String empNo);

  // (月底日日終批次)維護 JcicMonthlyLoanData 聯徵放款月報資料檔
  @Procedure(value = "\"Usp_L8_JcicMonthlyLoanData_Upd\"")
  public void uspL8JcicmonthlyloandataUpd(int tbsdyf,  String empNo);

  // (月底日日終批次)維護 Ifrs9LoanData 每月IFRS9撥款資料檔
  @Procedure(value = "\"Usp_L7_Ifrs9LoanData_Upd\"")
  public void uspL7Ifrs9loandataUpd(int tbsdyf,  String empNo);

  // (月底日日終批次)維護 Ifrs9FacData 每月IFRS9額度資料檔
  @Procedure(value = "\"Usp_L7_Ifrs9FacData_Upd\"")
  public void uspL7Ifrs9facdataUpd(int tbsdyf,  String empNo);

  // (月底日日終批次)維護 Ias39LoanCommit 每月IAS39放款承諾明細檔
  @Procedure(value = "\"Usp_L7_Ias39LoanCommit_Upd\"")
  public void uspL7Ias39loancommitUpd(int tbsdyf,  String empNo);

  // (月底日日終批次)維護MonthlyLM003月報工作檔
  @Procedure(value = "\"Usp_L9_MonthlyLM003_Upd\"")
  public void uspL9Monthlylm003Upd(int tbsdyf,  String empNo);

  // (月底日日終批次)維護MonthlyLM028月報工作檔
  @Procedure(value = "\"Usp_L9_MonthlyLM028_Upd\"")
  public void uspL9Monthlylm028Upd(int tbsdyf,  String empNo);

  // (月底日日終批次)維護MonthlyLM051月報工作檔
  @Procedure(value = "\"Usp_L9_MonthlyLM051_Upd\"")
  public void uspL9Monthlylm051Upd(int tbsdyf,  String empNo);

  // (月底日日終批次)維護MonthlyLM032月報工作檔
  @Procedure(value = "\"Usp_L9_MonthlyLM032_Upd\"")
  public void uspL9Monthlylm032Upd(int TBSDYF, String empNo,int LMBSDYF);

  // (年底日日終批次)維護 YearlyHouseLoanInt 每年房屋擔保借款繳息工作檔 
  @Procedure(value = "\"Usp_L9_YearlyHouseLoanInt_Upd\"")
  public void uspL9YearlyhouseloanintUpd(int tbsdyf,  String empNo,int StartMonth,int EndMonth,int CustNo,String AcctCode);

  // (月底日日終批次)維護 JcicB201 每月聯徵授信餘額月報資料檔
  @Procedure(value = "\"Usp_L8_JcicB201_Upd\"")
  public void uspL8Jcicb201Upd(int tbsdyf,  String empNo);

  // (月底日日終批次)維護 JcicB207 每月聯徵授信戶基本資料檔
  @Procedure(value = "\"Usp_L8_JcicB207_Upd\"")
  public void uspL8Jcicb207Upd(int tbsdyf,  String empNo);

  // (月底日日終批次)維護 JcicB080 每月聯徵授信額度資料檔
  @Procedure(value = "\"Usp_L8_JcicB080_Upd\"")
  public void uspL8Jcicb080Upd(int tbsdyf,  String empNo);

  // (月底日日終批次)維護 JcicB085 每月聯徵帳號轉換資料檔
  @Procedure(value = "\"Usp_L8_JcicB085_Upd\"")
  public void uspL8Jcicb085Upd(int tbsdyf,  String empNo);

  // (月底日日終批次)維護 JcicB090 每月聯徵擔保品關聯檔資料檔
  @Procedure(value = "\"Usp_L8_JcicB090_Upd\"")
  public void uspL8Jcicb090Upd(int tbsdyf,  String empNo);

  // (月底日日終批次)維護 JcicB092 每月聯徵不動產擔保品明細檔
  @Procedure(value = "\"Usp_L8_JcicB092_Upd\"")
  public void uspL8Jcicb092Upd(int tbsdyf,  String empNo);

  // (月底日日終批次)維護 JcicB093 每月聯徵動產及貴重物品擔保品明細檔
  @Procedure(value = "\"Usp_L8_JcicB093_Upd\"")
  public void uspL8Jcicb093Upd(int tbsdyf,  String empNo);

  // (月底日日終批次)維護 JcicB094 每月聯徵股票擔保品明細檔
  @Procedure(value = "\"Usp_L8_JcicB094_Upd\"")
  public void uspL8Jcicb094Upd(int tbsdyf,  String empNo);

  // (月底日日終批次)維護 JcicB095 每月聯徵不動產擔保品明細－建號附加檔
  @Procedure(value = "\"Usp_L8_JcicB095_Upd\"")
  public void uspL8Jcicb095Upd(int tbsdyf,  String empNo);

  // (月底日日終批次)維護 JcicB096 每月聯徵不動產擔保品明細－地號附加檔
  @Procedure(value = "\"Usp_L8_JcicB096_Upd\"")
  public void uspL8Jcicb096Upd(int tbsdyf,  String empNo);

  // (月底日日終批次)維護 JcicB680 每月聯徵「貸款餘額(擔保放款餘額加上部分擔保、副擔保貸款餘額)扣除擔保品鑑估值」之金額資料檔
  @Procedure(value = "\"Usp_L8_JcicB680_Upd\"")
  public void uspL8Jcicb680Upd(int tbsdyf,  String empNo);

  // (月底日日終批次)維護 JcicRel聯徵授信「同一關係企業及集團企業」資料報送檔
  @Procedure(value = "\"Usp_L8_JcicRel_Upd\"")
  public void uspL8JcicrelUpd(int tbsdyf,  String empNo);

  // (月底日日終批次)維護 Ias34Ap 每月IAS34資料欄位清單A檔
  @Procedure(value = "\"Usp_L7_Ias34Ap_Upd\"")
  public void uspL7Ias34apUpd(int tbsdyf,  String empNo,int newAcFg);

  // (月底日日終批次)維護 Ias34Bp 每月IAS34資料欄位清單B檔
  @Procedure(value = "\"Usp_L7_Ias34Bp_Upd\"")
  public void uspL7Ias34bpUpd(int tbsdyf,  String empNo);

  // (月底日日終批次)維護 Ias34Cp 每月IAS34資料欄位清單C檔
  @Procedure(value = "\"Usp_L7_Ias34Cp_Upd\"")
  public void uspL7Ias34cpUpd(int tbsdyf,  String empNo);

  // (月底日日終批次)維護 Ias34Dp 每月IAS34資料欄位清單D檔
  @Procedure(value = "\"Usp_L7_Ias34Dp_Upd\"")
  public void uspL7Ias34dpUpd(int tbsdyf,  String empNo,int newAcFg);

  // (月底日日終批次)維護 Ias34Ep 每月IAS34資料欄位清單E檔
  @Procedure(value = "\"Usp_L7_Ias34Ep_Upd\"")
  public void uspL7Ias34epUpd(int tbsdyf,  String empNo,int newAcFg);

  // (月底日日終批次)維護 Ias34Gp 每月IAS34資料欄位清單G檔
  @Procedure(value = "\"Usp_L7_Ias34Gp_Upd\"")
  public void uspL7Ias34gpUpd(int tbsdyf,  String empNo);

  // (月底日日終批次)維護 LoanIfrs9Ap 每月IFRS9欄位清單A檔
  @Procedure(value = "\"Usp_L7_LoanIfrs9Ap_Upd\"")
  public void uspL7Loanifrs9apUpd(int tbsdyf,  String empNo,int newAcFg);

  // (月底日日終批次)維護 LoanIfrs9Bp 每月IFRS9欄位清單B檔
  @Procedure(value = "\"Usp_L7_LoanIfrs9Bp_Upd\"")
  public void uspL7Loanifrs9bpUpd(int tbsdyf,  String empNo);

  // (月底日日終批次)維護 LoanIfrs9Cp 每月IFRS9欄位清單C檔
  @Procedure(value = "\"Usp_L7_LoanIfrs9Cp_Upd\"")
  public void uspL7Loanifrs9cpUpd(int tbsdyf,  String empNo);

  // (月底日日終批次)維護 LoanIfrs9Dp 每月IFRS9欄位清單D檔
  @Procedure(value = "\"Usp_L7_LoanIfrs9Dp_Upd\"")
  public void uspL7Loanifrs9dpUpd(int tbsdyf,  String empNo,int newAcFg);

  // (月底日日終批次)維護 LoanIfrs9Fp 每月IFRS9欄位清單F檔
  @Procedure(value = "\"Usp_L7_LoanIfrs9Fp_Upd\"")
  public void uspL7Loanifrs9fpUpd(int tbsdyf,  String empNo);

  // (月底日日終批次)維護 LoanIfrs9Gp 每月IFRS9欄位清單G檔
  @Procedure(value = "\"Usp_L7_LoanIfrs9Gp_Upd\"")
  public void uspL7Loanifrs9gpUpd(int tbsdyf,  String empNo);

  // (月底日日終批次)維護 LoanIfrs9Hp 每月IFRS9欄位清單H檔
  @Procedure(value = "\"Usp_L7_LoanIfrs9Hp_Upd\"")
  public void uspL7Loanifrs9hpUpd(int tbsdyf,  String empNo);

  // (月底日日終批次)維護 LoanIfrs9Ip 每月IFRS9欄位清單I檔
  @Procedure(value = "\"Usp_L7_LoanIfrs9Ip_Upd\"")
  public void uspL7Loanifrs9ipUpd(int tbsdyf,  String empNo,int newAcFg);

  // (月底日日終批次)維護 LoanIfrs9Jp 每月IFRS9欄位清單J檔
  @Procedure(value = "\"Usp_L7_LoanIfrs9Jp_Upd\"")
  public void uspL7Loanifrs9jpUpd(int tbsdyf,  String empNo);

  // (月底日日終批次)維護 MonthlyLM052AssetClass
  @Procedure(value = "\"Usp_L9_MonthlyLM052AssetClass_Ins\"")
  public void uspL9Monthlylm052assetclassIns(int TYYMM, String EmpNo);

  // (月底日日終批次)維護 MonthlyLM052LoanAsset
  @Procedure(value = "\"Usp_L9_MonthlyLM052LoanAsset_Ins\"")
  public void uspL9Monthlylm052loanassetIns(int TYYMM, String EmpNo);

  // (月底日日終批次)維護 MonthlyLM052Ovdu
  @Procedure(value = "\"Usp_L9_MonthlyLM052Ovdu_Ins\"")
  public void uspL9Monthlylm052ovduIns(int TYYMM, String EmpNo);

  // 執行L5811產生國稅局申報檢核檔時
  @Procedure(value = "\"Usp_L9_YearlyHouseLoanIntCheck_Upd\"")
  public void uspL9YearlyhouseloanintcheckUpd(int tbsdyf,  String empNo,int YYYYMM,int StartMonth,int EndMonth,int CustNo,String AcctCode);

  // (每日複製)控制外來鍵
  @Procedure(value = "\"Usp_Cp_ForeignKeyControl_Upd\"")
  public void uspCpForeignkeycontrolUpd(int TBSDYF, String empNo,int Switch);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_AcAcctCheck_Ins\"")
  public void uspCpAcacctcheckIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_AcAcctCheckDetail_Ins\"")
  public void uspCpAcacctcheckdetailIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_AcClose_Ins\"")
  public void uspCpAccloseIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_AcDetail_Ins\"")
  public void uspCpAcdetailIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_AchAuthLog_Ins\"")
  public void uspCpAchauthlogIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_AchAuthLogHistory_Ins\"")
  public void uspCpAchauthloghistoryIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_AchDeductMedia_Ins\"")
  public void uspCpAchdeductmediaIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_AcLoanInt_Ins\"")
  public void uspCpAcloanintIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_AcLoanRenew_Ins\"")
  public void uspCpAcloanrenewIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_AcMain_Ins\"")
  public void uspCpAcmainIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_AcReceivable_Ins\"")
  public void uspCpAcreceivableIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_AmlCustList_Ins\"")
  public void uspCpAmlcustlistIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_BankAuthAct_Ins\"")
  public void uspCpBankauthactIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_BankDeductDtl_Ins\"")
  public void uspCpBankdeductdtlIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_BankRelationCompany_Ins\"")
  public void uspCpBankrelationcompanyIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_BankRelationFamily_Ins\"")
  public void uspCpBankrelationfamilyIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_BankRelationSelf_Ins\"")
  public void uspCpBankrelationselfIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_BankRelationSuspected_Ins\"")
  public void uspCpBankrelationsuspectedIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_BankRemit_Ins\"")
  public void uspCpBankremitIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_BankRmtf_Ins\"")
  public void uspCpBankrmtfIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_BatxCheque_Ins\"")
  public void uspCpBatxchequeIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_BatxDetail_Ins\"")
  public void uspCpBatxdetailIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_BatxHead_Ins\"")
  public void uspCpBatxheadIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_BatxOthers_Ins\"")
  public void uspCpBatxothersIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_BatxRateChange_Ins\"")
  public void uspCpBatxratechangeIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_CdAcBook_Ins\"")
  public void uspCpCdacbookIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_CdAcCode_Ins\"")
  public void uspCpCdaccodeIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_CdAoDept_Ins\"")
  public void uspCpCdaodeptIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_CdAppraisalCompany_Ins\"")
  public void uspCpCdappraisalcompanyIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_CdAppraiser_Ins\"")
  public void uspCpCdappraiserIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_CdArea_Ins\"")
  public void uspCpCdareaIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_CdBank_Ins\"")
  public void uspCpCdbankIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_CdBankOld_Ins\"")
  public void uspCpCdbankoldIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_CdBaseRate_Ins\"")
  public void uspCpCdbaserateIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_CdBcm_Ins\"")
  public void uspCpCdbcmIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_CdBonus_Ins\"")
  public void uspCpCdbonusIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_CdBonusCo_Ins\"")
  public void uspCpCdbonuscoIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_CdBranch_Ins\"")
  public void uspCpCdbranchIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_CdBranchGroup_Ins\"")
  public void uspCpCdbranchgroupIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_CdBudget_Ins\"")
  public void uspCpCdbudgetIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_CdBuildingCost_Ins\"")
  public void uspCpCdbuildingcostIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_CdCashFlow_Ins\"")
  public void uspCpCdcashflowIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_CdCity_Ins\"")
  public void uspCpCdcityIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_CdCl_Ins\"")
  public void uspCpCdclIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_CdCode_Ins\"")
  public void uspCpCdcodeIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_CdEmp_Ins\"")
  public void uspCpCdempIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_CdGseq_Ins\"")
  public void uspCpCdgseqIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_CdGuarantor_Ins\"")
  public void uspCpCdguarantorIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_CdIndustry_Ins\"")
  public void uspCpCdindustryIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_CdInsurer_Ins\"")
  public void uspCpCdinsurerIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_CdLandOffice_Ins\"")
  public void uspCpCdlandofficeIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_CdLandSection_Ins\"")
  public void uspCpCdlandsectionIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_CdLoanNotYet_Ins\"")
  public void uspCpCdloannotyetIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_CdOverdue_Ins\"")
  public void uspCpCdoverdueIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_CdPerformance_Ins\"")
  public void uspCpCdperformanceIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_CdPfParms_Ins\"")
  public void uspCpCdpfparmsIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_CdReport_Ins\"")
  public void uspCpCdreportIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_CdStock_Ins\"")
  public void uspCpCdstockIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_CdSupv_Ins\"")
  public void uspCpCdsupvIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_CdSyndFee_Ins\"")
  public void uspCpCdsyndfeeIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_CdVarValue_Ins\"")
  public void uspCpCdvarvalueIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_CdWorkMonth_Ins\"")
  public void uspCpCdworkmonthIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_ClBuilding_Ins\"")
  public void uspCpClbuildingIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_ClBuildingOwner_Ins\"")
  public void uspCpClbuildingownerIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_ClBuildingPublic_Ins\"")
  public void uspCpClbuildingpublicIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_ClBuildingReason_Ins\"")
  public void uspCpClbuildingreasonIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_ClEva_Ins\"")
  public void uspCpClevaIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_ClFac_Ins\"")
  public void uspCpClfacIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_ClImm_Ins\"")
  public void uspCpClimmIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_ClImmRankDetail_Ins\"")
  public void uspCpClimmrankdetailIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_ClLand_Ins\"")
  public void uspCpCllandIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_ClLandOwner_Ins\"")
  public void uspCpCllandownerIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_ClLandReason_Ins\"")
  public void uspCpCllandreasonIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_ClMain_Ins\"")
  public void uspCpClmainIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_ClMovables_Ins\"")
  public void uspCpClmovablesIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_ClNoMap_Ins\"")
  public void uspCpClnomapIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_ClOther_Ins\"")
  public void uspCpClotherIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_ClOtherRights_Ins\"")
  public void uspCpClotherrightsIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_ClOwnerRelation_Ins\"")
  public void uspCpClownerrelationIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_ClParking_Ins\"")
  public void uspCpClparkingIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_ClParkingType_Ins\"")
  public void uspCpClparkingtypeIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_ClStock_Ins\"")
  public void uspCpClstockIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_CollLaw_Ins\"")
  public void uspCpColllawIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_CollLetter_Ins\"")
  public void uspCpCollletterIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_CollList_Ins\"")
  public void uspCpColllistIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_CollListTmp_Ins\"")
  public void uspCpColllisttmpIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_CollMeet_Ins\"")
  public void uspCpCollmeetIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_CollRemind_Ins\"")
  public void uspCpCollremindIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_CollTel_Ins\"")
  public void uspCpColltelIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_CreditRating_Ins\"")
  public void uspCpCreditratingIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_CustCross_Ins\"")
  public void uspCpCustcrossIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_CustDataCtrl_Ins\"")
  public void uspCpCustdatactrlIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_CustFin_Ins\"")
  public void uspCpCustfinIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_CustMain_Ins\"")
  public void uspCpCustmainIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_CustNotice_Ins\"")
  public void uspCpCustnoticeIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_CustomerAmlRating_Ins\"")
  public void uspCpCustomeramlratingIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_CustRmk_Ins\"")
  public void uspCpCustrmkIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_CustTelNo_Ins\"")
  public void uspCpCusttelnoIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_DailyLoanBal_Ins\"")
  public void uspCpDailyloanbalIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_EmpDeductDtl_Ins\"")
  public void uspCpEmpdeductdtlIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_EmpDeductMedia_Ins\"")
  public void uspCpEmpdeductmediaIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_EmpDeductSchedule_Ins\"")
  public void uspCpEmpdeductscheduleIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_FacCaseAppl_Ins\"")
  public void uspCpFaccaseapplIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_FacClose_Ins\"")
  public void uspCpFaccloseIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_FacMain_Ins\"")
  public void uspCpFacmainIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_FacProd_Ins\"")
  public void uspCpFacprodIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_FacProdAcctFee_Ins\"")
  public void uspCpFacprodacctfeeIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_FacProdPremium_Ins\"")
  public void uspCpFacprodpremiumIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_FacProdStepRate_Ins\"")
  public void uspCpFacprodsteprateIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_FacRelation_Ins\"")
  public void uspCpFacrelationIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_FacShareAppl_Ins\"")
  public void uspCpFacshareapplIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_FacShareLimit_Ins\"")
  public void uspCpFacsharelimitIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_FacShareRelation_Ins\"")
  public void uspCpFacsharerelationIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_FinReportCashFlow_Ins\"")
  public void uspCpFinreportcashflowIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_FinReportDebt_Ins\"")
  public void uspCpFinreportdebtIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_FinReportProfit_Ins\"")
  public void uspCpFinreportprofitIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_FinReportQuality_Ins\"")
  public void uspCpFinreportqualityIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_FinReportRate_Ins\"")
  public void uspCpFinreportrateIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_FinReportReview_Ins\"")
  public void uspCpFinreportreviewIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_ForeclosureFee_Ins\"")
  public void uspCpForeclosurefeeIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_ForeclosureFinished_Ins\"")
  public void uspCpForeclosurefinishedIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_GraceCondition_Ins\"")
  public void uspCpGraceconditionIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_Guarantor_Ins\"")
  public void uspCpGuarantorIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_GuildBuilders_Ins\"")
  public void uspCpGuildbuildersIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_HlAreaData_Ins\"")
  public void uspCpHlareadataIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_HlAreaLnYg6Pt_Ins\"")
  public void uspCpHlarealnyg6ptIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_HlCusData_Ins\"")
  public void uspCpHlcusdataIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_HlEmpLnYg5Pt_Ins\"")
  public void uspCpHlemplnyg5ptIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_HlThreeDetail_Ins\"")
  public void uspCpHlthreedetailIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_HlThreeLaqhcp_Ins\"")
  public void uspCpHlthreelaqhcpIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_Ias34Ap_Ins\"")
  public void uspCpIas34apIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_Ias34Bp_Ins\"")
  public void uspCpIas34bpIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_Ias34Cp_Ins\"")
  public void uspCpIas34cpIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_Ias34Dp_Ins\"")
  public void uspCpIas34dpIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_Ias34Ep_Ins\"")
  public void uspCpIas34epIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_Ias34Gp_Ins\"")
  public void uspCpIas34gpIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_Ias39IntMethod_Ins\"")
  public void uspCpIas39intmethodIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_Ias39LGD_Ins\"")
  public void uspCpIas39lgdIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_Ias39Loan34Data_Ins\"")
  public void uspCpIas39loan34dataIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_Ias39LoanCommit_Ins\"")
  public void uspCpIas39loancommitIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_Ias39Loss_Ins\"")
  public void uspCpIas39lossIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_Ifrs9FacData_Ins\"")
  public void uspCpIfrs9facdataIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_Ifrs9LoanData_Ins\"")
  public void uspCpIfrs9loandataIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_InnDocRecord_Ins\"")
  public void uspCpInndocrecordIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_InnFundApl_Ins\"")
  public void uspCpInnfundaplIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_InnLoanMeeting_Ins\"")
  public void uspCpInnloanmeetingIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_InnReCheck_Ins\"")
  public void uspCpInnrecheckIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_InsuComm_Ins\"")
  public void uspCpInsucommIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_InsuOrignal_Ins\"")
  public void uspCpInsuorignalIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_InsuRenew_Ins\"")
  public void uspCpInsurenewIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_InsuRenewMediaTemp_Ins\"")
  public void uspCpInsurenewmediatempIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_JcicAtomDetail_Ins\"")
  public void uspCpJcicatomdetailIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_JcicAtomMain_Ins\"")
  public void uspCpJcicatommainIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_JcicB080_Ins\"")
  public void uspCpJcicb080Ins(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_JcicB085_Ins\"")
  public void uspCpJcicb085Ins(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_JcicB090_Ins\"")
  public void uspCpJcicb090Ins(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_JcicB091_Ins\"")
  public void uspCpJcicb091Ins(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_JcicB092_Ins\"")
  public void uspCpJcicb092Ins(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_JcicB093_Ins\"")
  public void uspCpJcicb093Ins(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_JcicB094_Ins\"")
  public void uspCpJcicb094Ins(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_JcicB095_Ins\"")
  public void uspCpJcicb095Ins(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_JcicB096_Ins\"")
  public void uspCpJcicb096Ins(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_JcicB201_Ins\"")
  public void uspCpJcicb201Ins(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_JcicB204_Ins\"")
  public void uspCpJcicb204Ins(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_JcicB207_Ins\"")
  public void uspCpJcicb207Ins(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_JcicB211_Ins\"")
  public void uspCpJcicb211Ins(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_JcicB680_Ins\"")
  public void uspCpJcicb680Ins(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_JcicMonthlyLoanData_Ins\"")
  public void uspCpJcicmonthlyloandataIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_JcicRel_Ins\"")
  public void uspCpJcicrelIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_JcicZ040_Ins\"")
  public void uspCpJcicz040Ins(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_JcicZ040Log_Ins\"")
  public void uspCpJcicz040logIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_JcicZ041_Ins\"")
  public void uspCpJcicz041Ins(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_JcicZ041Log_Ins\"")
  public void uspCpJcicz041logIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_JcicZ042_Ins\"")
  public void uspCpJcicz042Ins(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_JcicZ042Log_Ins\"")
  public void uspCpJcicz042logIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_JcicZ043_Ins\"")
  public void uspCpJcicz043Ins(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_JcicZ043Log_Ins\"")
  public void uspCpJcicz043logIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_JcicZ044_Ins\"")
  public void uspCpJcicz044Ins(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_JcicZ044Log_Ins\"")
  public void uspCpJcicz044logIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_JcicZ045_Ins\"")
  public void uspCpJcicz045Ins(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_JcicZ045Log_Ins\"")
  public void uspCpJcicz045logIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_JcicZ046_Ins\"")
  public void uspCpJcicz046Ins(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_JcicZ046Log_Ins\"")
  public void uspCpJcicz046logIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_JcicZ047_Ins\"")
  public void uspCpJcicz047Ins(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_JcicZ047Log_Ins\"")
  public void uspCpJcicz047logIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_JcicZ048_Ins\"")
  public void uspCpJcicz048Ins(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_JcicZ048Log_Ins\"")
  public void uspCpJcicz048logIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_JcicZ049_Ins\"")
  public void uspCpJcicz049Ins(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_JcicZ049Log_Ins\"")
  public void uspCpJcicz049logIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_JcicZ050_Ins\"")
  public void uspCpJcicz050Ins(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_JcicZ050Log_Ins\"")
  public void uspCpJcicz050logIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_JcicZ051_Ins\"")
  public void uspCpJcicz051Ins(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_JcicZ051Log_Ins\"")
  public void uspCpJcicz051logIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_JcicZ052_Ins\"")
  public void uspCpJcicz052Ins(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_JcicZ052Log_Ins\"")
  public void uspCpJcicz052logIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_JcicZ053_Ins\"")
  public void uspCpJcicz053Ins(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_JcicZ053Log_Ins\"")
  public void uspCpJcicz053logIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_JcicZ054_Ins\"")
  public void uspCpJcicz054Ins(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_JcicZ054Log_Ins\"")
  public void uspCpJcicz054logIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_JcicZ055_Ins\"")
  public void uspCpJcicz055Ins(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_JcicZ055Log_Ins\"")
  public void uspCpJcicz055logIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_JcicZ056_Ins\"")
  public void uspCpJcicz056Ins(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_JcicZ056Log_Ins\"")
  public void uspCpJcicz056logIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_JcicZ060_Ins\"")
  public void uspCpJcicz060Ins(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_JcicZ060Log_Ins\"")
  public void uspCpJcicz060logIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_JcicZ061_Ins\"")
  public void uspCpJcicz061Ins(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_JcicZ061Log_Ins\"")
  public void uspCpJcicz061logIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_JcicZ062_Ins\"")
  public void uspCpJcicz062Ins(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_JcicZ062Log_Ins\"")
  public void uspCpJcicz062logIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_JcicZ063_Ins\"")
  public void uspCpJcicz063Ins(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_JcicZ063Log_Ins\"")
  public void uspCpJcicz063logIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_JcicZ440_Ins\"")
  public void uspCpJcicz440Ins(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_JcicZ440Log_Ins\"")
  public void uspCpJcicz440logIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_JcicZ442_Ins\"")
  public void uspCpJcicz442Ins(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_JcicZ442Log_Ins\"")
  public void uspCpJcicz442logIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_JcicZ443_Ins\"")
  public void uspCpJcicz443Ins(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_JcicZ443Log_Ins\"")
  public void uspCpJcicz443logIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_JcicZ444_Ins\"")
  public void uspCpJcicz444Ins(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_JcicZ444Log_Ins\"")
  public void uspCpJcicz444logIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_JcicZ446_Ins\"")
  public void uspCpJcicz446Ins(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_JcicZ446Log_Ins\"")
  public void uspCpJcicz446logIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_JcicZ447_Ins\"")
  public void uspCpJcicz447Ins(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_JcicZ447Log_Ins\"")
  public void uspCpJcicz447logIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_JcicZ448_Ins\"")
  public void uspCpJcicz448Ins(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_JcicZ448Log_Ins\"")
  public void uspCpJcicz448logIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_JcicZ450_Ins\"")
  public void uspCpJcicz450Ins(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_JcicZ450Log_Ins\"")
  public void uspCpJcicz450logIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_JcicZ451_Ins\"")
  public void uspCpJcicz451Ins(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_JcicZ451Log_Ins\"")
  public void uspCpJcicz451logIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_JcicZ454_Ins\"")
  public void uspCpJcicz454Ins(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_JcicZ454Log_Ins\"")
  public void uspCpJcicz454logIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_JcicZ570_Ins\"")
  public void uspCpJcicz570Ins(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_JcicZ570Log_Ins\"")
  public void uspCpJcicz570logIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_JcicZ571_Ins\"")
  public void uspCpJcicz571Ins(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_JcicZ571Log_Ins\"")
  public void uspCpJcicz571logIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_JcicZ572_Ins\"")
  public void uspCpJcicz572Ins(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_JcicZ572Log_Ins\"")
  public void uspCpJcicz572logIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_JcicZ573_Ins\"")
  public void uspCpJcicz573Ins(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_JcicZ573Log_Ins\"")
  public void uspCpJcicz573logIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_JcicZ574_Ins\"")
  public void uspCpJcicz574Ins(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_JcicZ574Log_Ins\"")
  public void uspCpJcicz574logIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_JcicZ575_Ins\"")
  public void uspCpJcicz575Ins(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_JcicZ575Log_Ins\"")
  public void uspCpJcicz575logIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_JobDetail_Ins\"")
  public void uspCpJobdetailIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_JobMain_Ins\"")
  public void uspCpJobmainIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_LoanBook_Ins\"")
  public void uspCpLoanbookIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_LoanBorMain_Ins\"")
  public void uspCpLoanbormainIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_LoanBorTx_Ins\"")
  public void uspCpLoanbortxIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_LoanCheque_Ins\"")
  public void uspCpLoanchequeIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_LoanCustRmk_Ins\"")
  public void uspCpLoancustrmkIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_LoanIfrs9Ap_Ins\"")
  public void uspCpLoanifrs9apIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_LoanIfrs9Bp_Ins\"")
  public void uspCpLoanifrs9bpIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_LoanIfrs9Cp_Ins\"")
  public void uspCpLoanifrs9cpIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_LoanIfrs9Dp_Ins\"")
  public void uspCpLoanifrs9dpIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_LoanIfrs9Fp_Ins\"")
  public void uspCpLoanifrs9fpIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_LoanIfrs9Gp_Ins\"")
  public void uspCpLoanifrs9gpIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_LoanIfrs9Hp_Ins\"")
  public void uspCpLoanifrs9hpIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_LoanIfrs9Ip_Ins\"")
  public void uspCpLoanifrs9ipIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_LoanIfrs9Jp_Ins\"")
  public void uspCpLoanifrs9jpIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_LoanIntDetail_Ins\"")
  public void uspCpLoanintdetailIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_LoanNotYet_Ins\"")
  public void uspCpLoannotyetIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_LoanOverdue_Ins\"")
  public void uspCpLoanoverdueIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_LoanRateChange_Ins\"")
  public void uspCpLoanratechangeIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_LoanSynd_Ins\"")
  public void uspCpLoansyndIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_LoanSyndItem_Ins\"")
  public void uspCpLoansynditemIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_MlaundryChkDtl_Ins\"")
  public void uspCpMlaundrychkdtlIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_MlaundryDetail_Ins\"")
  public void uspCpMlaundrydetailIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_MlaundryParas_Ins\"")
  public void uspCpMlaundryparasIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_MlaundryRecord_Ins\"")
  public void uspCpMlaundryrecordIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_MonthlyLM003_Ins\"")
  public void uspCpMonthlylm003Ins(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_MonthlyLM028_Ins\"")
  public void uspCpMonthlylm028Ins(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_MonthlyLM032_Ins\"")
  public void uspCpMonthlylm032Ins(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_MonthlyLM036Portfolio_Ins\"")
  public void uspCpMonthlylm036portfolioIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_MonthlyLM052AssetClass_Ins\"")
  public void uspCpMonthlylm052assetclassIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_MonthlyLM052LoanAsset_Ins\"")
  public void uspCpMonthlylm052loanassetIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_MonthlyLM052Loss_Ins\"")
  public void uspCpMonthlylm052lossIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_MonthlyLM052Ovdu_Ins\"")
  public void uspCpMonthlylm052ovduIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_MonthlyLoanBal_Ins\"")
  public void uspCpMonthlyloanbalIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_NegAppr_Ins\"")
  public void uspCpNegapprIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_NegAppr01_Ins\"")
  public void uspCpNegappr01Ins(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_NegAppr02_Ins\"")
  public void uspCpNegappr02Ins(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_NegFinAcct_Ins\"")
  public void uspCpNegfinacctIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_NegFinShare_Ins\"")
  public void uspCpNegfinshareIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_NegFinShareLog_Ins\"")
  public void uspCpNegfinsharelogIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_NegMain_Ins\"")
  public void uspCpNegmainIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_NegQueryCust_Ins\"")
  public void uspCpNegquerycustIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_NegTrans_Ins\"")
  public void uspCpNegtransIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_PfBsDetail_Ins\"")
  public void uspCpPfbsdetailIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_PfBsDetailAdjust_Ins\"")
  public void uspCpPfbsdetailadjustIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_PfBsOfficer_Ins\"")
  public void uspCpPfbsofficerIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_PfCoOfficer_Ins\"")
  public void uspCpPfcoofficerIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_PfCoOfficerLog_Ins\"")
  public void uspCpPfcoofficerlogIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_PfDeparment_Ins\"")
  public void uspCpPfdeparmentIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_PfDetail_Ins\"")
  public void uspCpPfdetailIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_PfInsCheck_Ins\"")
  public void uspCpPfinscheckIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_PfIntranetAdjust_Ins\"")
  public void uspCpPfintranetadjustIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_PfItDetail_Ins\"")
  public void uspCpPfitdetailIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_PfItDetailAdjust_Ins\"")
  public void uspCpPfitdetailadjustIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_PfReward_Ins\"")
  public void uspCpPfrewardIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_PfRewardMedia_Ins\"")
  public void uspCpPfrewardmediaIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_PfSpecParms_Ins\"")
  public void uspCpPfspecparmsIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_PostAuthLog_Ins\"")
  public void uspCpPostauthlogIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_PostAuthLogHistory_Ins\"")
  public void uspCpPostauthloghistoryIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_PostDeductMedia_Ins\"")
  public void uspCpPostdeductmediaIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_ReltMain_Ins\"")
  public void uspCpReltmainIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_RepayActChangeLog_Ins\"")
  public void uspCpRepayactchangelogIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_RptJcic_Ins\"")
  public void uspCpRptjcicIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_RptRelationCompany_Ins\"")
  public void uspCpRptrelationcompanyIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_RptRelationFamily_Ins\"")
  public void uspCpRptrelationfamilyIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_RptRelationSelf_Ins\"")
  public void uspCpRptrelationselfIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_RptSubCom_Ins\"")
  public void uspCpRptsubcomIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_SlipEbsRecord_Ins\"")
  public void uspCpSlipebsrecordIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_SlipMedia_Ins\"")
  public void uspCpSlipmediaIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_SlipMedia2022_Ins\"")
  public void uspCpSlipmedia2022Ins(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_SpecInnReCheck_Ins\"")
  public void uspCpSpecinnrecheckIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_StgCdEmp_Ins\"")
  public void uspCpStgcdempIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_SystemParas_Ins\"")
  public void uspCpSystemparasIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_TbJcicMu01_Ins\"")
  public void uspCpTbjcicmu01Ins(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_TbJcicW020_Ins\"")
  public void uspCpTbjcicw020Ins(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_TbJcicZZ50_Ins\"")
  public void uspCpTbjciczz50Ins(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_TxAgent_Ins\"")
  public void uspCpTxagentIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_TxAmlCredit_Ins\"")
  public void uspCpTxamlcreditIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_TxAmlLog_Ins\"")
  public void uspCpTxamllogIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_TxAmlNotice_Ins\"")
  public void uspCpTxamlnoticeIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_TxAmlRating_Ins\"")
  public void uspCpTxamlratingIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_TxApLog_Ins\"")
  public void uspCpTxaplogIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_TxApLogList_Ins\"")
  public void uspCpTxaploglistIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_TxArchiveTable_Ins\"")
  public void uspCpTxarchivetableIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_TxArchiveTableLog_Ins\"")
  public void uspCpTxarchivetablelogIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_TxAttachment_Ins\"")
  public void uspCpTxattachmentIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_TxAttachType_Ins\"")
  public void uspCpTxattachtypeIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_TxAuthGroup_Ins\"")
  public void uspCpTxauthgroupIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_TxAuthority_Ins\"")
  public void uspCpTxauthorityIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_TxAuthorize_Ins\"")
  public void uspCpTxauthorizeIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_TxBizDate_Ins\"")
  public void uspCpTxbizdateIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_TxControl_Ins\"")
  public void uspCpTxcontrolIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_TxCruiser_Ins\"")
  public void uspCpTxcruiserIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_TxCurr_Ins\"")
  public void uspCpTxcurrIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_TxDataLog_Ins\"")
  public void uspCpTxdatalogIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_TxErrCode_Ins\"")
  public void uspCpTxerrcodeIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_TxFile_Ins\"")
  public void uspCpTxfileIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_TxFlow_Ins\"")
  public void uspCpTxflowIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_TxHoliday_Ins\"")
  public void uspCpTxholidayIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_TxInquiry_Ins\"")
  public void uspCpTxinquiryIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_TxLock_Ins\"")
  public void uspCpTxlockIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_TxPrinter_Ins\"")
  public void uspCpTxprinterIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_TxProcess_Ins\"")
  public void uspCpTxprocessIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_TxRecord_Ins\"")
  public void uspCpTxrecordIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_TxTeller_Ins\"")
  public void uspCpTxtellerIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_TxTellerAuth_Ins\"")
  public void uspCpTxtellerauthIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_TxTellerTest_Ins\"")
  public void uspCpTxtellertestIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_TxTemp_Ins\"")
  public void uspCpTxtempIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_TxToDoDetail_Ins\"")
  public void uspCpTxtododetailIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_TxToDoDetailReserve_Ins\"")
  public void uspCpTxtododetailreserveIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_TxToDoMain_Ins\"")
  public void uspCpTxtodomainIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_TxTranCode_Ins\"")
  public void uspCpTxtrancodeIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_TxUnLock_Ins\"")
  public void uspCpTxunlockIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_UspErrorLog_Ins\"")
  public void uspCpUsperrorlogIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_YearlyHouseLoanInt_Ins\"")
  public void uspCpYearlyhouseloanintIns(String EmpNo);

  // (每日複製)
  @Procedure(value = "\"Usp_Cp_YearlyHouseLoanIntCheck_Ins\"")
  public void uspCpYearlyhouseloanintcheckIns(String EmpNo);

  // 每日複製
  @Procedure(value = "\"Usp_L9_DailyBackup_Copy\"")
  public void uspL9DailybackupCopy(int Tbsdyf,String EmpNo);

}

