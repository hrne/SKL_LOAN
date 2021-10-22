package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.NegMain;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.NegMainId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface NegMainService {

  /**
   * findByPrimaryKey
   *
   * @param negMainId PK
   * @param titaVo Variable-Length Argument
   * @return NegMain NegMain
   */
  public NegMain findById(NegMainId negMainId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice NegMain NegMain of List
   */
  public Slice<NegMain> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * CaseKindCode= , AND CustLoanKind= , AND Status= , AND CustNo=
   *
   * @param caseKindCode_0 caseKindCode_0
   * @param custLoanKind_1 custLoanKind_1
   * @param status_2 status_2
   * @param custNo_3 custNo_3
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice NegMain NegMain of List
   */
  public Slice<NegMain> haveCustNo(String caseKindCode_0, String custLoanKind_1, String status_2, int custNo_3, int index, int limit, TitaVo... titaVo);

  /**
   * CaseKindCode= , AND CustLoanKind= , AND Status=
   *
   * @param caseKindCode_0 caseKindCode_0
   * @param custLoanKind_1 custLoanKind_1
   * @param status_2 status_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice NegMain NegMain of List
   */
  public Slice<NegMain> noCustNo(String caseKindCode_0, String custLoanKind_1, String status_2, int index, int limit, TitaVo... titaVo);

  /**
   * CustNo=
   *
   * @param custNo_0 custNo_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice NegMain NegMain of List
   */
  public Slice<NegMain> custNoEq(int custNo_0, int index, int limit, TitaVo... titaVo);

  /**
   * CaseKindCode=
   *
   * @param caseKindCode_0 caseKindCode_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice NegMain NegMain of List
   */
  public Slice<NegMain> caseKindCodeEq(String caseKindCode_0, int index, int limit, TitaVo... titaVo);

  /**
   * CustLoanKind=
   *
   * @param custLoanKind_0 custLoanKind_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice NegMain NegMain of List
   */
  public Slice<NegMain> custLoanKindEq(String custLoanKind_0, int index, int limit, TitaVo... titaVo);

  /**
   * Status=
   *
   * @param status_0 status_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice NegMain NegMain of List
   */
  public Slice<NegMain> statusEq(String status_0, int index, int limit, TitaVo... titaVo);

  /**
   * CustNo=
   *
   * @param custNo_0 custNo_0
   * @param titaVo Variable-Length Argument
   * @return Slice NegMain NegMain of List
   */
  public NegMain custNoFirst(int custNo_0, TitaVo... titaVo);

  /**
   * Status= ,AND CustNo= 
   *
   * @param status_0 status_0
   * @param custNo_1 custNo_1
   * @param titaVo Variable-Length Argument
   * @return Slice NegMain NegMain of List
   */
  public NegMain statusFirst(String status_0, int custNo_1, TitaVo... titaVo);

  /**
   * Status^i , AND IsMainFin= , AND NextPayDate&gt;= , AND NextPayDate&lt;= , AND CustNo=
   *
   * @param status_0 status_0
   * @param isMainFin_1 isMainFin_1
   * @param nextPayDate_2 nextPayDate_2
   * @param nextPayDate_3 nextPayDate_3
   * @param custNo_4 custNo_4
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice NegMain NegMain of List
   */
  public Slice<NegMain> l5705HadCustId(List<String> status_0, String isMainFin_1, int nextPayDate_2, int nextPayDate_3, int custNo_4, int index, int limit, TitaVo... titaVo);

  /**
   * Status^i , AND IsMainFin= , AND NextPayDate&gt;= , AND NextPayDate&lt;= 
   *
   * @param status_0 status_0
   * @param isMainFin_1 isMainFin_1
   * @param nextPayDate_2 nextPayDate_2
   * @param nextPayDate_3 nextPayDate_3
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice NegMain NegMain of List
   */
  public Slice<NegMain> l5705NoCustId(List<String> status_0, String isMainFin_1, int nextPayDate_2, int nextPayDate_3, int index, int limit, TitaVo... titaVo);

  /**
   * CustNo= , AND ApplDate= , AND MainFinCode= 
   *
   * @param custNo_0 custNo_0
   * @param applDate_1 applDate_1
   * @param mainFinCode_2 mainFinCode_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice NegMain NegMain of List
   */
  public Slice<NegMain> custNoAndApplDate(int custNo_0, int applDate_1, String mainFinCode_2, int index, int limit, TitaVo... titaVo);

  /**
   * CustNo= , AND ApplDate= , AND MainFinCode= 
   *
   * @param custNo_0 custNo_0
   * @param applDate_1 applDate_1
   * @param mainFinCode_2 mainFinCode_2
   * @param titaVo Variable-Length Argument
   * @return Slice NegMain NegMain of List
   */
  public NegMain custNoAndApplDateFirst(int custNo_0, int applDate_1, String mainFinCode_2, TitaVo... titaVo);

  /**
   * CustNo= , AND CaseKindCode= 
   *
   * @param custNo_0 custNo_0
   * @param caseKindCode_1 caseKindCode_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice NegMain NegMain of List
   */
  public Slice<NegMain> forLetter(int custNo_0, String caseKindCode_1, int index, int limit, TitaVo... titaVo);

  /**
   * hold By NegMain
   * 
   * @param negMainId key
   * @param titaVo Variable-Length Argument
   * @return NegMain NegMain
   */
  public NegMain holdById(NegMainId negMainId, TitaVo... titaVo);

  /**
   * hold By NegMain
   * 
   * @param negMain key
   * @param titaVo Variable-Length Argument
   * @return NegMain NegMain
   */
  public NegMain holdById(NegMain negMain, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param negMain Entity
   * @param titaVo Variable-Length Argument
   * @return NegMain Entity
   * @throws DBException exception
   */
  public NegMain insert(NegMain negMain, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param negMain Entity
   * @param titaVo Variable-Length Argument
   * @return NegMain Entity
   * @throws DBException exception
   */
  public NegMain update(NegMain negMain, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param negMain Entity
   * @param titaVo Variable-Length Argument
   * @return NegMain Entity
   * @throws DBException exception
   */
  public NegMain update2(NegMain negMain, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param negMain Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(NegMain negMain, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param negMain Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<NegMain> negMain, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param negMain Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<NegMain> negMain, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param negMain Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<NegMain> negMain, TitaVo... titaVo) throws DBException;

}
