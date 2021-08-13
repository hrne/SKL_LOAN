package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CdLoanNotYet;
import org.springframework.data.domain.Slice;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CdLoanNotYetService {

  /**
   * findByPrimaryKey
   *
   * @param notYetCode PK
   * @param titaVo Variable-Length Argument
   * @return CdLoanNotYet CdLoanNotYet
   */
  public CdLoanNotYet findById(String notYetCode, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdLoanNotYet CdLoanNotYet of List
   */
  public Slice<CdLoanNotYet> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * NotYetCode %
   *
   * @param notYetCode_0 notYetCode_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdLoanNotYet CdLoanNotYet of List
   */
  public Slice<CdLoanNotYet> codeLike(String notYetCode_0, int index, int limit, TitaVo... titaVo);

  /**
   * hold By CdLoanNotYet
   * 
   * @param notYetCode key
   * @param titaVo Variable-Length Argument
   * @return CdLoanNotYet CdLoanNotYet
   */
  public CdLoanNotYet holdById(String notYetCode, TitaVo... titaVo);

  /**
   * hold By CdLoanNotYet
   * 
   * @param cdLoanNotYet key
   * @param titaVo Variable-Length Argument
   * @return CdLoanNotYet CdLoanNotYet
   */
  public CdLoanNotYet holdById(CdLoanNotYet cdLoanNotYet, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param cdLoanNotYet Entity
   * @param titaVo Variable-Length Argument
   * @return CdLoanNotYet Entity
   * @throws DBException exception
   */
  public CdLoanNotYet insert(CdLoanNotYet cdLoanNotYet, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param cdLoanNotYet Entity
   * @param titaVo Variable-Length Argument
   * @return CdLoanNotYet Entity
   * @throws DBException exception
   */
  public CdLoanNotYet update(CdLoanNotYet cdLoanNotYet, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param cdLoanNotYet Entity
   * @param titaVo Variable-Length Argument
   * @return CdLoanNotYet Entity
   * @throws DBException exception
   */
  public CdLoanNotYet update2(CdLoanNotYet cdLoanNotYet, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param cdLoanNotYet Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(CdLoanNotYet cdLoanNotYet, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param cdLoanNotYet Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<CdLoanNotYet> cdLoanNotYet, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param cdLoanNotYet Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<CdLoanNotYet> cdLoanNotYet, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param cdLoanNotYet Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<CdLoanNotYet> cdLoanNotYet, TitaVo... titaVo) throws DBException;

}
