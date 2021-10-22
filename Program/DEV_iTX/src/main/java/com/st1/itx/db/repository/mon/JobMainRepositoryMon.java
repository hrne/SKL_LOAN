package com.st1.itx.db.repository.mon;


import java.util.Optional;

import javax.persistence.LockModeType;

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
public interface JobMainRepositoryMon extends JpaRepository<JobMain, JobMainId> {

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<JobMain> findByJobMainId(JobMainId jobMainId);

  // (日終批次)維護 CollList 法催紀錄清單檔
  @Procedure(value = "\"Usp_L5_CollList_Upd\"")
  public void uspL5ColllistUpd(int tbsdyf,  String empNo, int l6bsdyf, int l7bsdyf);

  // (日終批次)維護 InnReCheck 覆審案件明細檔 
  @Procedure(value = "\"Usp_L5_InnReCheck_Upd\"")
  public void uspL5InnrecheckUpd(int tbsdyf,  String empNo);

  // (日終批次)維護 DailyLoanBal每日放款餘額檔
  @Procedure(value = "\"Usp_L9_DailyLoanBal_Upd\"")
  public void uspL9DailyloanbalUpd(int tbsdyf,  String empNo, int mfbsdyf);

  // (日終批次)維護 JcicB204每日聯徵授信日報資料檔
  @Procedure(value = "\"Usp_L8_JcicB204_Upd\"")
  public void uspL8Jcicb204Upd(int tbsdyf,  String empNo);

  // (日終批次)維護 JcicB211聯徵每日授信餘額變動資料檔
  @Procedure(value = "\"Usp_L8_JcicB211_Upd\"")
  public void uspL8Jcicb211Upd(int tbsdyf,  String empNo);

  // (月底日日終批次)維護MonthlyLoanBal每月放款餘額檔
  @Procedure(value = "\"Usp_L9_MonthlyLoanBal_Upd\"")
  public void uspL9MonthlyloanbalUpd(int tbsdyf,  String empNo);

  // (月底日日終批次)維護 MonthlyFacBal 額度月報工作檔
  @Procedure(value = "\"Usp_L9_MonthlyFacBal_Upd\"")
  public void uspL9MonthlyfacbalUpd(int tbsdyf,  String empNo);

  // (月底日日終批次)維護 Ias39Loan34Data 每月IAS39放款34號公報資料檔
  @Procedure(value = "\"Usp_L7_Ias39Loan34Data_Upd\"")
  public void uspL7Ias39loan34dataUpd(int tbsdyf,  String empNo);

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
  public void uspL9YearlyhouseloanintUpd(int tbsdyf,  String empNo);

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

  // (月底日日終批次)維護 LoanIfrsAp 每月IFRS9欄位清單A檔
  @Procedure(value = "\"Usp_L7_LoanIfrsAp_Upd\"")
  public void uspL7LoanifrsapUpd(int tbsdyf,  String empNo,int newAcFg);

  // (月底日日終批次)維護 LoanIfrsBp 每月IFRS9欄位清單B檔
  @Procedure(value = "\"Usp_L7_LoanIfrsBp_Upd\"")
  public void uspL7LoanifrsbpUpd(int tbsdyf,  String empNo);

  // (月底日日終批次)維護 LoanIfrsCp 每月IFRS9欄位清單C檔
  @Procedure(value = "\"Usp_L7_LoanIfrsCp_Upd\"")
  public void uspL7LoanifrscpUpd(int tbsdyf,  String empNo);

  // (月底日日終批次)維護 LoanIfrsDp 每月IFRS9欄位清單D檔
  @Procedure(value = "\"Usp_L7_LoanIfrsDp_Upd\"")
  public void uspL7LoanifrsdpUpd(int tbsdyf,  String empNo,int newAcFg);

  // (月底日日終批次)維護 LoanIfrsFp 每月IFRS9欄位清單F檔
  @Procedure(value = "\"Usp_L7_LoanIfrsFp_Upd\"")
  public void uspL7LoanifrsfpUpd(int tbsdyf,  String empNo);

  // (月底日日終批次)維護 LoanIfrsGp 每月IFRS9欄位清單G檔
  @Procedure(value = "\"Usp_L7_LoanIfrsGp_Upd\"")
  public void uspL7LoanifrsgpUpd(int tbsdyf,  String empNo);

  // (月底日日終批次)維護 LoanIfrsHp 每月IFRS9欄位清單H檔
  @Procedure(value = "\"Usp_L7_LoanIfrsHp_Upd\"")
  public void uspL7LoanifrshpUpd(int tbsdyf,  String empNo);

  // (月底日日終批次)維護 LoanIfrsIp 每月IFRS9欄位清單I檔
  @Procedure(value = "\"Usp_L7_LoanIfrsIp_Upd\"")
  public void uspL7LoanifrsipUpd(int tbsdyf,  String empNo,int newAcFg);

  // (月底日日終批次)維護 LoanIfrsJp 每月IFRS9欄位清單J檔
  @Procedure(value = "\"Usp_L7_LoanIfrsJp_Upd\"")
  public void uspL7LoanifrsjpUpd(int tbsdyf,  String empNo);

  // (月底日日終批次)維護 MonthlyLM052AssetClass
  @Procedure(value = "\"Usp_L9_MonthlyLM052AssetClass_Ins\"")
  public void uspL9Monthlylm052assetclassIns(int TYYMM, String EmpNo);

  // (月底日日終批次)維護 MonthlyLM052LoanAsset
  @Procedure(value = "\"Usp_L9_MonthlyLM052LoanAsset_Ins\"")
  public void uspL9Monthlylm052loanassetIns(int TYYMM, String EmpNo);

  // (月底日日終批次)維護 MonthlyLM052Ovdu
  @Procedure(value = "\"Usp_L9_MonthlyLM052Ovdu_Ins\"")
  public void uspL9Monthlylm052ovduIns(int TYYMM, String EmpNo);

}

