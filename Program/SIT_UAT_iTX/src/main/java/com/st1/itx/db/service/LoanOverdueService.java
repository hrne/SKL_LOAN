package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.LoanOverdue;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.LoanOverdueId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface LoanOverdueService {

  /**
   * findByPrimaryKey
   *
   * @param loanOverdueId PK
   * @param titaVo Variable-Length Argument
   * @return LoanOverdue LoanOverdue
   */
  public LoanOverdue findById(LoanOverdueId loanOverdueId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice LoanOverdue LoanOverdue of List
   */
  public Slice<LoanOverdue> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * CustNo = ,AND FacmNo &gt;= ,AND FacmNo &lt;= ,AND BormNo &gt;= ,AND BormNo &lt;= ,AND OvduNo &gt;= ,AND OvduNo &lt;=,AND Status  ^i
   *
   * @param custNo_0 custNo_0
   * @param facmNo_1 facmNo_1
   * @param facmNo_2 facmNo_2
   * @param bormNo_3 bormNo_3
   * @param bormNo_4 bormNo_4
   * @param ovduNo_5 ovduNo_5
   * @param ovduNo_6 ovduNo_6
   * @param status_7 status_7
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice LoanOverdue LoanOverdue of List
   */
  public Slice<LoanOverdue> ovduCustNoRange(int custNo_0, int facmNo_1, int facmNo_2, int bormNo_3, int bormNo_4, int ovduNo_5, int ovduNo_6, List<Integer> status_7, int index, int limit, TitaVo... titaVo);

  /**
   * hold By LoanOverdue
   * 
   * @param loanOverdueId key
   * @param titaVo Variable-Length Argument
   * @return LoanOverdue LoanOverdue
   */
  public LoanOverdue holdById(LoanOverdueId loanOverdueId, TitaVo... titaVo);

  /**
   * hold By LoanOverdue
   * 
   * @param loanOverdue key
   * @param titaVo Variable-Length Argument
   * @return LoanOverdue LoanOverdue
   */
  public LoanOverdue holdById(LoanOverdue loanOverdue, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param loanOverdue Entity
   * @param titaVo Variable-Length Argument
   * @return LoanOverdue Entity
   * @throws DBException exception
   */
  public LoanOverdue insert(LoanOverdue loanOverdue, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param loanOverdue Entity
   * @param titaVo Variable-Length Argument
   * @return LoanOverdue Entity
   * @throws DBException exception
   */
  public LoanOverdue update(LoanOverdue loanOverdue, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param loanOverdue Entity
   * @param titaVo Variable-Length Argument
   * @return LoanOverdue Entity
   * @throws DBException exception
   */
  public LoanOverdue update2(LoanOverdue loanOverdue, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param loanOverdue Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(LoanOverdue loanOverdue, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param loanOverdue Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<LoanOverdue> loanOverdue, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param loanOverdue Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<LoanOverdue> loanOverdue, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param loanOverdue Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<LoanOverdue> loanOverdue, TitaVo... titaVo) throws DBException;

}
