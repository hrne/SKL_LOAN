package com.st1.itx.db.service;

import java.util.List;

import java.math.BigDecimal;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.LoanFacTmp;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.LoanFacTmpId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface LoanFacTmpService {

  /**
   * findByPrimaryKey
   *
   * @param loanFacTmpId PK
   * @param titaVo Variable-Length Argument
   * @return LoanFacTmp LoanFacTmp
   */
  public LoanFacTmp findById(LoanFacTmpId loanFacTmpId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice LoanFacTmp LoanFacTmp of List
   */
  public Slice<LoanFacTmp> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * CustNo = 
   *
   * @param custNo_0 custNo_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice LoanFacTmp LoanFacTmp of List
   */
  public Slice<LoanFacTmp> findCustNo(int custNo_0, int index, int limit, TitaVo... titaVo);

  /**
   * hold By LoanFacTmp
   * 
   * @param loanFacTmpId key
   * @param titaVo Variable-Length Argument
   * @return LoanFacTmp LoanFacTmp
   */
  public LoanFacTmp holdById(LoanFacTmpId loanFacTmpId, TitaVo... titaVo);

  /**
   * hold By LoanFacTmp
   * 
   * @param loanFacTmp key
   * @param titaVo Variable-Length Argument
   * @return LoanFacTmp LoanFacTmp
   */
  public LoanFacTmp holdById(LoanFacTmp loanFacTmp, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param loanFacTmp Entity
   * @param titaVo Variable-Length Argument
   * @return LoanFacTmp Entity
   * @throws DBException exception
   */
  public LoanFacTmp insert(LoanFacTmp loanFacTmp, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param loanFacTmp Entity
   * @param titaVo Variable-Length Argument
   * @return LoanFacTmp Entity
   * @throws DBException exception
   */
  public LoanFacTmp update(LoanFacTmp loanFacTmp, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param loanFacTmp Entity
   * @param titaVo Variable-Length Argument
   * @return LoanFacTmp Entity
   * @throws DBException exception
   */
  public LoanFacTmp update2(LoanFacTmp loanFacTmp, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param loanFacTmp Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(LoanFacTmp loanFacTmp, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param loanFacTmp Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<LoanFacTmp> loanFacTmp, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param loanFacTmp Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<LoanFacTmp> loanFacTmp, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param loanFacTmp Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<LoanFacTmp> loanFacTmp, TitaVo... titaVo) throws DBException;

}
