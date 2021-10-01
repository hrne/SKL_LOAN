package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.LoanCheque;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.LoanChequeId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface LoanChequeService {

  /**
   * findByPrimaryKey
   *
   * @param loanChequeId PK
   * @param titaVo Variable-Length Argument
   * @return LoanCheque LoanCheque
   */
  public LoanCheque findById(LoanChequeId loanChequeId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice LoanCheque LoanCheque of List
   */
  public Slice<LoanCheque> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * ChequeDate &gt;= ,AND ChequeDate &lt;= ,AND ChequeAcct &gt;= ,AND ChequeAcct &lt;= ,AND ChequeNo &gt;= ,AND ChequeNo &lt;=
   *
   * @param chequeDate_0 chequeDate_0
   * @param chequeDate_1 chequeDate_1
   * @param chequeAcct_2 chequeAcct_2
   * @param chequeAcct_3 chequeAcct_3
   * @param chequeNo_4 chequeNo_4
   * @param chequeNo_5 chequeNo_5
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice LoanCheque LoanCheque of List
   */
  public Slice<LoanCheque> chequeDateRange(int chequeDate_0, int chequeDate_1, int chequeAcct_2, int chequeAcct_3, int chequeNo_4, int chequeNo_5, int index, int limit, TitaVo... titaVo);

  /**
   * CustNo = ,AND StatusCode ^i ,AND  ChequeDate &gt;= ,AND ChequeDate &lt;=
   *
   * @param custNo_0 custNo_0
   * @param statusCode_1 statusCode_1
   * @param chequeDate_2 chequeDate_2
   * @param chequeDate_3 chequeDate_3
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice LoanCheque LoanCheque of List
   */
  public Slice<LoanCheque> chequeCustNoEq(int custNo_0, List<String> statusCode_1, int chequeDate_2, int chequeDate_3, int index, int limit, TitaVo... titaVo);

  /**
   * AcDate &gt;= ,AND AcDate &lt;= ,AND StatusCode ^i 
   *
   * @param acDate_0 acDate_0
   * @param acDate_1 acDate_1
   * @param statusCode_2 statusCode_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice LoanCheque LoanCheque of List
   */
  public Slice<LoanCheque> acDateRange(int acDate_0, int acDate_1, List<String> statusCode_2, int index, int limit, TitaVo... titaVo);

  /**
   * StatusCode ^i ,AND  ChequeDate &gt;= ,AND ChequeDate &lt;=
   *
   * @param statusCode_0 statusCode_0
   * @param chequeDate_1 chequeDate_1
   * @param chequeDate_2 chequeDate_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice LoanCheque LoanCheque of List
   */
  public Slice<LoanCheque> statusCodeRange(List<String> statusCode_0, int chequeDate_1, int chequeDate_2, int index, int limit, TitaVo... titaVo);

  /**
   * ChequeDate &gt;= ,AND ChequeDate &lt;= ,AND ChequeAcct &gt;= ,AND ChequeAcct &lt;= ,AND ChequeNo &gt;= ,AND ChequeNo &lt;= ,AND StatusCode =
   *
   * @param chequeDate_0 chequeDate_0
   * @param chequeDate_1 chequeDate_1
   * @param chequeAcct_2 chequeAcct_2
   * @param chequeAcct_3 chequeAcct_3
   * @param chequeNo_4 chequeNo_4
   * @param chequeNo_5 chequeNo_5
   * @param statusCode_6 statusCode_6
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice LoanCheque LoanCheque of List
   */
  public Slice<LoanCheque> forStatusCodeSelect(int chequeDate_0, int chequeDate_1, int chequeAcct_2, int chequeAcct_3, int chequeNo_4, int chequeNo_5, String statusCode_6, int index, int limit, TitaVo... titaVo);

  /**
   * ReceiveDate &gt;= ,AND ReceiveDate &lt;= ,AND StatusCode ^i 
   *
   * @param receiveDate_0 receiveDate_0
   * @param receiveDate_1 receiveDate_1
   * @param statusCode_2 statusCode_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice LoanCheque LoanCheque of List
   */
  public Slice<LoanCheque> receiveDateRange(int receiveDate_0, int receiveDate_1, List<String> statusCode_2, int index, int limit, TitaVo... titaVo);

  /**
   * CustNo = ,AND StatusCode ^i ,AND ChequeNo &gt;= ,AND ChequeNo &lt;= ,AND  ChequeDate &gt;= ,AND ChequeDate &lt;=
   *
   * @param custNo_0 custNo_0
   * @param statusCode_1 statusCode_1
   * @param chequeNo_2 chequeNo_2
   * @param chequeNo_3 chequeNo_3
   * @param chequeDate_4 chequeDate_4
   * @param chequeDate_5 chequeDate_5
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice LoanCheque LoanCheque of List
   */
  public Slice<LoanCheque> custNoChequeRange(int custNo_0, List<String> statusCode_1, int chequeNo_2, int chequeNo_3, int chequeDate_4, int chequeDate_5, int index, int limit, TitaVo... titaVo);

  /**
   * hold By LoanCheque
   * 
   * @param loanChequeId key
   * @param titaVo Variable-Length Argument
   * @return LoanCheque LoanCheque
   */
  public LoanCheque holdById(LoanChequeId loanChequeId, TitaVo... titaVo);

  /**
   * hold By LoanCheque
   * 
   * @param loanCheque key
   * @param titaVo Variable-Length Argument
   * @return LoanCheque LoanCheque
   */
  public LoanCheque holdById(LoanCheque loanCheque, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param loanCheque Entity
   * @param titaVo Variable-Length Argument
   * @return LoanCheque Entity
   * @throws DBException exception
   */
  public LoanCheque insert(LoanCheque loanCheque, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param loanCheque Entity
   * @param titaVo Variable-Length Argument
   * @return LoanCheque Entity
   * @throws DBException exception
   */
  public LoanCheque update(LoanCheque loanCheque, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param loanCheque Entity
   * @param titaVo Variable-Length Argument
   * @return LoanCheque Entity
   * @throws DBException exception
   */
  public LoanCheque update2(LoanCheque loanCheque, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param loanCheque Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(LoanCheque loanCheque, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param loanCheque Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<LoanCheque> loanCheque, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param loanCheque Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<LoanCheque> loanCheque, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param loanCheque Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<LoanCheque> loanCheque, TitaVo... titaVo) throws DBException;

}
