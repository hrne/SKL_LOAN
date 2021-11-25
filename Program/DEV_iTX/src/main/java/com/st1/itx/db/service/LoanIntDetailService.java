package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.LoanIntDetail;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.LoanIntDetailId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface LoanIntDetailService {

  /**
   * findByPrimaryKey
   *
   * @param loanIntDetailId PK
   * @param titaVo Variable-Length Argument
   * @return LoanIntDetail LoanIntDetail
   */
  public LoanIntDetail findById(LoanIntDetailId loanIntDetailId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice LoanIntDetail LoanIntDetail of List
   */
  public Slice<LoanIntDetail> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * CustNo = ,AND FacmNo &gt;= ,AND FacmNo &lt;= ,AND BormNo &gt;= ,AND BormNo &lt;= ,AND BreachGetCode = 
   *
   * @param custNo_0 custNo_0
   * @param facmNo_1 facmNo_1
   * @param facmNo_2 facmNo_2
   * @param bormNo_3 bormNo_3
   * @param bormNo_4 bormNo_4
   * @param breachGetCode_5 breachGetCode_5
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice LoanIntDetail LoanIntDetail of List
   */
  public Slice<LoanIntDetail> intDetailBreachGetCodeEq(int custNo_0, int facmNo_1, int facmNo_2, int bormNo_3, int bormNo_4, String breachGetCode_5, int index, int limit, TitaVo... titaVo);

  /**
   * CustNo = ,AND FacmNo = ,AND BormNo = ,AND AcDate = ,AND TlrNo = ,AND TxtNo =
   *
   * @param custNo_0 custNo_0
   * @param facmNo_1 facmNo_1
   * @param bormNo_2 bormNo_2
   * @param acDate_3 acDate_3
   * @param tlrNo_4 tlrNo_4
   * @param txtNo_5 txtNo_5
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice LoanIntDetail LoanIntDetail of List
   */
  public Slice<LoanIntDetail> fildFacmNoEq(int custNo_0, int facmNo_1, int bormNo_2, int acDate_3, String tlrNo_4, String txtNo_5, int index, int limit, TitaVo... titaVo);

  /**
   * hold By LoanIntDetail
   * 
   * @param loanIntDetailId key
   * @param titaVo Variable-Length Argument
   * @return LoanIntDetail LoanIntDetail
   */
  public LoanIntDetail holdById(LoanIntDetailId loanIntDetailId, TitaVo... titaVo);

  /**
   * hold By LoanIntDetail
   * 
   * @param loanIntDetail key
   * @param titaVo Variable-Length Argument
   * @return LoanIntDetail LoanIntDetail
   */
  public LoanIntDetail holdById(LoanIntDetail loanIntDetail, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param loanIntDetail Entity
   * @param titaVo Variable-Length Argument
   * @return LoanIntDetail Entity
   * @throws DBException exception
   */
  public LoanIntDetail insert(LoanIntDetail loanIntDetail, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param loanIntDetail Entity
   * @param titaVo Variable-Length Argument
   * @return LoanIntDetail Entity
   * @throws DBException exception
   */
  public LoanIntDetail update(LoanIntDetail loanIntDetail, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param loanIntDetail Entity
   * @param titaVo Variable-Length Argument
   * @return LoanIntDetail Entity
   * @throws DBException exception
   */
  public LoanIntDetail update2(LoanIntDetail loanIntDetail, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param loanIntDetail Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(LoanIntDetail loanIntDetail, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param loanIntDetail Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<LoanIntDetail> loanIntDetail, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param loanIntDetail Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<LoanIntDetail> loanIntDetail, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param loanIntDetail Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<LoanIntDetail> loanIntDetail, TitaVo... titaVo) throws DBException;

}
