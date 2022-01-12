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
   * (月底日日終批次)維護 Ias39Loan34Data 每月IAS39放款34號公報資料檔
   * @param  tbsdyf int
   * @param  empNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_L7_Ias39Loan34Data_Upd(int tbsdyf,  String empNo, TitaVo... titaVo);

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
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_L9_YearlyHouseLoanInt_Upd(int tbsdyf,  String empNo, TitaVo... titaVo);

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

}
