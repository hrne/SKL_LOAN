package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JobMain;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JobMainId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JobMainService {

  /**
   * findByPrimaryKey
   *
   * @param jobMainId PK
   * @param titaVo Variable-Length Argument
   * @return JobMain JobMain
   */
  public JobMain findById(JobMainId jobMainId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JobMain JobMain of List
   */
  public Slice<JobMain> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * TxSeq =
   *
   * @param txSeq_0 txSeq_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JobMain JobMain of List
   */
  public Slice<JobMain> findAllByTxSeq(String txSeq_0, int index, int limit, TitaVo... titaVo);

  /**
   * hold By JobMain
   * 
   * @param jobMainId key
   * @param titaVo Variable-Length Argument
   * @return JobMain JobMain
   */
  public JobMain holdById(JobMainId jobMainId, TitaVo... titaVo);

  /**
   * hold By JobMain
   * 
   * @param jobMain key
   * @param titaVo Variable-Length Argument
   * @return JobMain JobMain
   */
  public JobMain holdById(JobMain jobMain, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param jobMain Entity
   * @param titaVo Variable-Length Argument
   * @return JobMain Entity
   * @throws DBException exception
   */
  public JobMain insert(JobMain jobMain, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param jobMain Entity
   * @param titaVo Variable-Length Argument
   * @return JobMain Entity
   * @throws DBException exception
   */
  public JobMain update(JobMain jobMain, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param jobMain Entity
   * @param titaVo Variable-Length Argument
   * @return JobMain Entity
   * @throws DBException exception
   */
  public JobMain update2(JobMain jobMain, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param jobMain Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(JobMain jobMain, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param jobMain Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<JobMain> jobMain, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param jobMain Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<JobMain> jobMain, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param jobMain Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<JobMain> jobMain, TitaVo... titaVo) throws DBException;

  /**
   * Stored Procedure<br>
   * (日終批次)更新TxHoliday
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Tx_TxHoliday_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (日終批次)更新員工檔
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_L6_CdEmp_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (日終批次)更新組織檔
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_L6_CdBcm_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (日終批次)更新離職員工檔
   * @param  InputEmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_L6_QuitEmp_Ins(String InputEmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (日終批次)維護 CustDataCtrl 結清戶個資控管檔
   * @param  tbsdyf int
   * @param  empNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_L2_CustDataCtrl_Ins(int tbsdyf,  String empNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (日終批次)維護 CollList 法催紀錄清單檔
   * @param  tbsdyf int
   * @param  empNo String
   * @param  txtNo String
   * @param  l6bsdyf int
   * @param  l7bsdyf int
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_L5_CollList_Upd(int tbsdyf,  String empNo,String txtNo, int l6bsdyf, int l7bsdyf, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (日終批次)維護 InnReCheck 覆審案件明細檔 
   * @param  tbsdyf int
   * @param  empNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_L5_InnReCheck_Upd(int tbsdyf,  String empNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (日終批次)維護 DailyLoanBal每日放款餘額檔
   * @param  tbsdyf int
   * @param  empNo String
   * @param  mfbsdyf int
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_L9_DailyLoanBal_Upd(int tbsdyf,  String empNo, int mfbsdyf, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (日終批次)維護 DailyLoanBal每日暫收款餘額檔
   * @param  tbsdyf int
   * @param  empNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_L9_DailyTav_Ins(int tbsdyf,  String empNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (日終批次)維護 JcicB204每日聯徵授信日報資料檔
   * @param  tbsdyf int
   * @param  empNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_L8_JcicB204_Upd(int tbsdyf,  String empNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (日終批次)維護 JcicB211聯徵每日授信餘額變動資料檔
   * @param  tbsdyf int
   * @param  empNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_L8_JcicB211_Upd(int tbsdyf,  String empNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (月底日日終批次)更新 ForeclosureFinished 法拍完成檔
   * @param  tbsdyf int
   * @param  empNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_L2_ForeclosureFinished_Upd(int tbsdyf,  String empNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (月底日日終批次)維護MonthlyLoanBal每月放款餘額檔
   * @param  tbsdyf int
   * @param  empNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_L9_MonthlyLoanBal_Upd(int tbsdyf,  String empNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (月底日日終批次)維護 MonthlyFacBal 額度月報工作檔
   * @param  tbsdyf int
   * @param  empNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_L9_MonthlyFacBal_Upd(int tbsdyf,  String empNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (月底日日終批次)維護 JcicMonthlyLoanData 聯徵放款月報資料檔
   * @param  tbsdyf int
   * @param  empNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_L8_JcicMonthlyLoanData_Upd(int tbsdyf,  String empNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (月底日日終批次)維護 Ifrs9LoanData 每月IFRS9撥款資料檔
   * @param  tbsdyf int
   * @param  empNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_L7_Ifrs9LoanData_Upd(int tbsdyf,  String empNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (月底日日終批次)維護 Ifrs9FacData 每月IFRS9額度資料檔
   * @param  tbsdyf int
   * @param  empNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_L7_Ifrs9FacData_Upd(int tbsdyf,  String empNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (月底日日終批次)維護 Ias39LoanCommit 每月IAS39放款承諾明細檔
   * @param  tbsdyf int
   * @param  empNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_L7_Ias39LoanCommit_Upd(int tbsdyf,  String empNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (月底日日終批次)維護MonthlyLM003月報工作檔
   * @param  tbsdyf int
   * @param  empNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_L9_MonthlyLM003_Upd(int tbsdyf,  String empNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (月底日日終批次)維護MonthlyLM028月報工作檔
   * @param  tbsdyf int
   * @param  empNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_L9_MonthlyLM028_Upd(int tbsdyf,  String empNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (月底日日終批次)維護MonthlyLM051月報工作檔
   * @param  tbsdyf int
   * @param  empNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_L9_MonthlyLM051_Upd(int tbsdyf,  String empNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (月底日日終批次)維護MonthlyLM032月報工作檔
   * @param  TBSDYF int
   * @param  empNo String
   * @param  LMBSDYF int
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_L9_MonthlyLM032_Upd(int TBSDYF, String empNo,int LMBSDYF, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (年底日日終批次)維護 YearlyHouseLoanInt 每年房屋擔保借款繳息工作檔 
   * @param  tbsdyf int
   * @param  empNo String
   * @param  StartMonth int
   * @param  EndMonth int
   * @param  CustNo int
   * @param  AcctCode String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_L9_YearlyHouseLoanInt_Upd(int tbsdyf,  String empNo,int StartMonth,int EndMonth,int CustNo,String AcctCode, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (月底日日終批次)維護 JcicB201 每月聯徵授信餘額月報資料檔
   * @param  tbsdyf int
   * @param  empNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_L8_JcicB201_Upd(int tbsdyf,  String empNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (月底日日終批次)維護 JcicB207 每月聯徵授信戶基本資料檔
   * @param  tbsdyf int
   * @param  empNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_L8_JcicB207_Upd(int tbsdyf,  String empNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (月底日日終批次)維護 JcicB080 每月聯徵授信額度資料檔
   * @param  tbsdyf int
   * @param  empNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_L8_JcicB080_Upd(int tbsdyf,  String empNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (月底日日終批次)維護 JcicB085 每月聯徵帳號轉換資料檔
   * @param  tbsdyf int
   * @param  empNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_L8_JcicB085_Upd(int tbsdyf,  String empNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (月底日日終批次)維護 JcicB090 每月聯徵擔保品關聯檔資料檔
   * @param  tbsdyf int
   * @param  empNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_L8_JcicB090_Upd(int tbsdyf,  String empNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (月底日日終批次)維護 JcicB092 每月聯徵不動產擔保品明細檔
   * @param  tbsdyf int
   * @param  empNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_L8_JcicB092_Upd(int tbsdyf,  String empNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (月底日日終批次)維護 JcicB093 每月聯徵動產及貴重物品擔保品明細檔
   * @param  tbsdyf int
   * @param  empNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_L8_JcicB093_Upd(int tbsdyf,  String empNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (月底日日終批次)維護 JcicB094 每月聯徵股票擔保品明細檔
   * @param  tbsdyf int
   * @param  empNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_L8_JcicB094_Upd(int tbsdyf,  String empNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (月底日日終批次)維護 JcicB095 每月聯徵不動產擔保品明細－建號附加檔
   * @param  tbsdyf int
   * @param  empNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_L8_JcicB095_Upd(int tbsdyf,  String empNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (月底日日終批次)維護 JcicB096 每月聯徵不動產擔保品明細－地號附加檔
   * @param  tbsdyf int
   * @param  empNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_L8_JcicB096_Upd(int tbsdyf,  String empNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (月底日日終批次)維護 JcicB680 每月聯徵「貸款餘額(擔保放款餘額加上部分擔保、副擔保貸款餘額)扣除擔保品鑑估值」之金額資料檔
   * @param  tbsdyf int
   * @param  empNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_L8_JcicB680_Upd(int tbsdyf,  String empNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (月底日日終批次)維護 JcicRel聯徵授信「同一關係企業及集團企業」資料報送檔
   * @param  tbsdyf int
   * @param  empNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_L8_JcicRel_Upd(int tbsdyf,  String empNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (月底日日終批次)維護 Ias34Ap 每月IAS34資料欄位清單A檔
   * @param  tbsdyf int
   * @param  empNo String
   * @param  newAcFg int
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_L7_Ias34Ap_Upd(int tbsdyf,  String empNo,int newAcFg, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (月底日日終批次)維護 Ias34Bp 每月IAS34資料欄位清單B檔
   * @param  tbsdyf int
   * @param  empNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_L7_Ias34Bp_Upd(int tbsdyf,  String empNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (月底日日終批次)維護 Ias34Cp 每月IAS34資料欄位清單C檔
   * @param  tbsdyf int
   * @param  empNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_L7_Ias34Cp_Upd(int tbsdyf,  String empNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (月底日日終批次)維護 Ias34Dp 每月IAS34資料欄位清單D檔
   * @param  tbsdyf int
   * @param  empNo String
   * @param  newAcFg int
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_L7_Ias34Dp_Upd(int tbsdyf,  String empNo,int newAcFg, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (月底日日終批次)維護 Ias34Ep 每月IAS34資料欄位清單E檔
   * @param  tbsdyf int
   * @param  empNo String
   * @param  newAcFg int
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_L7_Ias34Ep_Upd(int tbsdyf,  String empNo,int newAcFg, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (月底日日終批次)維護 Ias34Gp 每月IAS34資料欄位清單G檔
   * @param  tbsdyf int
   * @param  empNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_L7_Ias34Gp_Upd(int tbsdyf,  String empNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (月底日日終批次)維護 LoanIfrs9Ap 每月IFRS9欄位清單A檔
   * @param  tbsdyf int
   * @param  empNo String
   * @param  newAcFg int
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_L7_LoanIfrs9Ap_Upd(int tbsdyf,  String empNo,int newAcFg, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (月底日日終批次)維護 LoanIfrs9Bp 每月IFRS9欄位清單B檔
   * @param  tbsdyf int
   * @param  empNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_L7_LoanIfrs9Bp_Upd(int tbsdyf,  String empNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (月底日日終批次)維護 LoanIfrs9Cp 每月IFRS9欄位清單C檔
   * @param  tbsdyf int
   * @param  empNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_L7_LoanIfrs9Cp_Upd(int tbsdyf,  String empNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (月底日日終批次)維護 LoanIfrs9Dp 每月IFRS9欄位清單D檔
   * @param  tbsdyf int
   * @param  empNo String
   * @param  newAcFg int
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_L7_LoanIfrs9Dp_Upd(int tbsdyf,  String empNo,int newAcFg, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (月底日日終批次)維護 LoanIfrs9Fp 每月IFRS9欄位清單F檔
   * @param  tbsdyf int
   * @param  empNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_L7_LoanIfrs9Fp_Upd(int tbsdyf,  String empNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (月底日日終批次)維護 LoanIfrs9Gp 每月IFRS9欄位清單G檔
   * @param  tbsdyf int
   * @param  empNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_L7_LoanIfrs9Gp_Upd(int tbsdyf,  String empNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (月底日日終批次)維護 LoanIfrs9Hp 每月IFRS9欄位清單H檔
   * @param  tbsdyf int
   * @param  empNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_L7_LoanIfrs9Hp_Upd(int tbsdyf,  String empNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (月底日日終批次)維護 LoanIfrs9Ip 每月IFRS9欄位清單I檔
   * @param  tbsdyf int
   * @param  empNo String
   * @param  newAcFg int
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_L7_LoanIfrs9Ip_Upd(int tbsdyf,  String empNo,int newAcFg, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (月底日日終批次)維護 LoanIfrs9Jp 每月IFRS9欄位清單J檔
   * @param  tbsdyf int
   * @param  empNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_L7_LoanIfrs9Jp_Upd(int tbsdyf,  String empNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (月底日日終批次)維護 MonthlyLM052AssetClass
   * @param  TYYMM int
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_L9_MonthlyLM052AssetClass_Ins(int TYYMM, String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (月底日日終批次)維護 MonthlyLM052LoanAsset
   * @param  TYYMM int
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_L9_MonthlyLM052LoanAsset_Ins(int TYYMM, String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (月底日日終批次)維護 MonthlyLM052Ovdu
   * @param  TYYMM int
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_L9_MonthlyLM052Ovdu_Ins(int TYYMM, String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * 執行L5811產生國稅局申報檢核檔時
   * @param  tbsdyf int
   * @param  empNo String
   * @param  YYYYMM int
   * @param  StartMonth int
   * @param  EndMonth int
   * @param  CustNo int
   * @param  AcctCode String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_L9_YearlyHouseLoanIntCheck_Upd(int tbsdyf,  String empNo,int YYYYMM,int StartMonth,int EndMonth,int CustNo,String AcctCode, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)控制外來鍵
   * @param  TBSDYF int
   * @param  empNo String
   * @param  Switch int
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_ForeignKeyControl_Upd(int TBSDYF, String empNo,int Switch, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_AcAcctCheck_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_AcAcctCheckDetail_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_AcClose_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_AcDetail_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_AchAuthLog_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_AchAuthLogHistory_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_AchDeductMedia_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_AcLoanInt_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_AcLoanRenew_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_AcMain_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_AcReceivable_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_AmlCustList_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_BankAuthAct_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_BankDeductDtl_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_BankRelationCompany_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_BankRelationFamily_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_BankRelationSelf_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_BankRelationSuspected_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_BankRemit_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_BankRmtf_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_BatxCheque_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_BatxDetail_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_BatxHead_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_BatxOthers_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_BatxRateChange_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_CdAcBook_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_CdAcCode_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_CdAoDept_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_CdAppraisalCompany_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_CdAppraiser_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_CdArea_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_CdBank_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_CdBankOld_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_CdBaseRate_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_CdBcm_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_CdBonus_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_CdBonusCo_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_CdBranch_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_CdBranchGroup_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_CdBudget_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_CdBuildingCost_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_CdCashFlow_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_CdCity_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_CdCl_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_CdCode_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_CdEmp_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_CdGseq_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_CdGuarantor_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_CdIndustry_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_CdInsurer_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_CdLandOffice_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_CdLandSection_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_CdLoanNotYet_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_CdOverdue_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_CdPerformance_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_CdPfParms_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_CdReport_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_CdStock_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_CdSupv_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_CdSyndFee_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_CdVarValue_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_CdWorkMonth_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_ClBuilding_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_ClBuildingOwner_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_ClBuildingPublic_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_ClBuildingReason_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_ClEva_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_ClFac_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_ClImm_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_ClImmRankDetail_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_ClLand_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_ClLandOwner_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_ClLandReason_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_ClMain_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_ClMovables_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_ClNoMap_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_ClOther_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_ClOtherRights_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_ClOwnerRelation_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_ClParking_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_ClParkingType_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_ClStock_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_CollLaw_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_CollLetter_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_CollList_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_CollListTmp_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_CollMeet_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_CollRemind_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_CollTel_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_CreditRating_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_CustCross_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_CustDataCtrl_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_CustFin_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_CustMain_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_CustNotice_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_CustomerAmlRating_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_CustRmk_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_CustTelNo_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_DailyLoanBal_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_EmpDeductDtl_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_EmpDeductMedia_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_EmpDeductSchedule_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_FacCaseAppl_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_FacClose_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_FacMain_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_FacProd_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_FacProdAcctFee_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_FacProdPremium_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_FacProdStepRate_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_FacRelation_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_FacShareAppl_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_FacShareLimit_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_FacShareRelation_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_FinReportCashFlow_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_FinReportDebt_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_FinReportProfit_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_FinReportQuality_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_FinReportRate_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_FinReportReview_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_ForeclosureFee_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_ForeclosureFinished_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_GraceCondition_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_Guarantor_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_GuildBuilders_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_HlAreaData_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_HlAreaLnYg6Pt_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_HlCusData_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_HlEmpLnYg5Pt_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_HlThreeDetail_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_HlThreeLaqhcp_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_Ias34Ap_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_Ias34Bp_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_Ias34Cp_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_Ias34Dp_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_Ias34Ep_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_Ias34Gp_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_Ias39IntMethod_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_Ias39LGD_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_Ias39Loan34Data_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_Ias39LoanCommit_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_Ias39Loss_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_Ifrs9FacData_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_Ifrs9LoanData_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_InnDocRecord_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_InnFundApl_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_InnLoanMeeting_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_InnReCheck_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_InsuComm_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_InsuOrignal_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_InsuRenew_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_InsuRenewMediaTemp_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_JcicAtomDetail_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_JcicAtomMain_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_JcicB080_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_JcicB085_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_JcicB090_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_JcicB091_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_JcicB092_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_JcicB093_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_JcicB094_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_JcicB095_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_JcicB096_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_JcicB201_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_JcicB204_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_JcicB207_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_JcicB211_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_JcicB680_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_JcicMonthlyLoanData_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_JcicRel_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_JcicZ040_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_JcicZ040Log_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_JcicZ041_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_JcicZ041Log_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_JcicZ042_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_JcicZ042Log_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_JcicZ043_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_JcicZ043Log_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_JcicZ044_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_JcicZ044Log_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_JcicZ045_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_JcicZ045Log_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_JcicZ046_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_JcicZ046Log_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_JcicZ047_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_JcicZ047Log_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_JcicZ048_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_JcicZ048Log_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_JcicZ049_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_JcicZ049Log_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_JcicZ050_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_JcicZ050Log_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_JcicZ051_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_JcicZ051Log_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_JcicZ052_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_JcicZ052Log_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_JcicZ053_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_JcicZ053Log_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_JcicZ054_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_JcicZ054Log_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_JcicZ055_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_JcicZ055Log_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_JcicZ056_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_JcicZ056Log_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_JcicZ060_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_JcicZ060Log_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_JcicZ061_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_JcicZ061Log_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_JcicZ062_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_JcicZ062Log_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_JcicZ063_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_JcicZ063Log_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_JcicZ440_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_JcicZ440Log_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_JcicZ442_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_JcicZ442Log_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_JcicZ443_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_JcicZ443Log_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_JcicZ444_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_JcicZ444Log_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_JcicZ446_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_JcicZ446Log_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_JcicZ447_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_JcicZ447Log_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_JcicZ448_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_JcicZ448Log_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_JcicZ450_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_JcicZ450Log_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_JcicZ451_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_JcicZ451Log_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_JcicZ454_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_JcicZ454Log_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_JcicZ570_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_JcicZ570Log_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_JcicZ571_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_JcicZ571Log_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_JcicZ572_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_JcicZ572Log_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_JcicZ573_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_JcicZ573Log_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_JcicZ574_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_JcicZ574Log_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_JcicZ575_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_JcicZ575Log_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_JobDetail_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_JobMain_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_LoanBook_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_LoanBorMain_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_LoanBorTx_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_LoanCheque_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_LoanCustRmk_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_LoanIfrs9Ap_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_LoanIfrs9Bp_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_LoanIfrs9Cp_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_LoanIfrs9Dp_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_LoanIfrs9Fp_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_LoanIfrs9Gp_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_LoanIfrs9Hp_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_LoanIfrs9Ip_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_LoanIfrs9Jp_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_LoanIntDetail_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_LoanNotYet_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_LoanOverdue_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_LoanRateChange_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_LoanSynd_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_LoanSyndItem_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_MlaundryChkDtl_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_MlaundryDetail_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_MlaundryParas_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_MlaundryRecord_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_MonthlyLM003_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_MonthlyLM028_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_MonthlyLM032_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_MonthlyLM036Portfolio_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_MonthlyLM052AssetClass_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_MonthlyLM052LoanAsset_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_MonthlyLM052Loss_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_MonthlyLM052Ovdu_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_MonthlyLoanBal_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_NegAppr_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_NegAppr01_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_NegAppr02_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_NegFinAcct_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_NegFinShare_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_NegFinShareLog_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_NegMain_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_NegQueryCust_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_NegTrans_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_PfBsDetail_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_PfBsDetailAdjust_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_PfBsOfficer_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_PfCoOfficer_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_PfCoOfficerLog_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_PfDeparment_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_PfDetail_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_PfInsCheck_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_PfIntranetAdjust_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_PfItDetail_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_PfItDetailAdjust_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_PfReward_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_PfRewardMedia_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_PfSpecParms_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_PostAuthLog_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_PostAuthLogHistory_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_PostDeductMedia_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_ReltMain_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_RepayActChangeLog_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_RptJcic_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_RptRelationCompany_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_RptRelationFamily_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_RptRelationSelf_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_RptSubCom_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_SlipEbsRecord_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_SlipMedia_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_SlipMedia2022_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_SpecInnReCheck_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_StgCdEmp_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_SystemParas_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_TbJcicMu01_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_TbJcicW020_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_TbJcicZZ50_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_TxAgent_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_TxAmlCredit_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_TxAmlLog_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_TxAmlNotice_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_TxAmlRating_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_TxApLog_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_TxApLogList_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_TxArchiveTable_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_TxArchiveTableLog_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_TxAttachment_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_TxAttachType_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_TxAuthGroup_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_TxAuthority_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_TxAuthorize_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_TxBizDate_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_TxControl_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_TxCruiser_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_TxCurr_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_TxDataLog_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_TxErrCode_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_TxFile_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_TxFlow_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_TxHoliday_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_TxInquiry_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_TxLock_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_TxPrinter_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_TxProcess_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_TxRecord_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_TxTeller_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_TxTellerAuth_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_TxTellerTest_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_TxTemp_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_TxToDoDetail_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_TxToDoDetailReserve_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_TxToDoMain_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_TxTranCode_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_TxUnLock_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_UspErrorLog_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_YearlyHouseLoanInt_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * (每日複製)
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_Cp_YearlyHouseLoanIntCheck_Ins(String EmpNo, TitaVo... titaVo);

  /**
   * Stored Procedure<br>
   * 每日複製
   * @param  Tbsdyf int
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_L9_DailyBackup_Copy(int Tbsdyf,String EmpNo, TitaVo... titaVo);

}
