package com.st1.itx.db.service;

import java.util.List;

import java.math.BigDecimal;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.NegFinAcct;
import org.springframework.data.domain.Slice;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface NegFinAcctService {

  /**
   * findByPrimaryKey
   *
   * @param finCode PK
   * @param titaVo Variable-Length Argument
   * @return NegFinAcct NegFinAcct
   */
  public NegFinAcct findById(String finCode, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice NegFinAcct NegFinAcct of List
   */
  public Slice<NegFinAcct> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * NewFinCode=
   *
   * @param newFinCode_0 newFinCode_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice NegFinAcct NegFinAcct of List
   */
  public Slice<NegFinAcct> newFinCodeEq(String newFinCode_0, int index, int limit, TitaVo... titaVo);

  /**
   * MergerDate&lt;= , AND ExecuteDate=
   *
   * @param mergerDate_0 mergerDate_0
   * @param executeDate_1 executeDate_1
   * @param titaVo Variable-Length Argument
   * @return Slice NegFinAcct NegFinAcct of List
   */
  public NegFinAcct mergerDateFirst(int mergerDate_0, int executeDate_1, TitaVo... titaVo);

  /**
   * hold By NegFinAcct
   * 
   * @param finCode key
   * @param titaVo Variable-Length Argument
   * @return NegFinAcct NegFinAcct
   */
  public NegFinAcct holdById(String finCode, TitaVo... titaVo);

  /**
   * hold By NegFinAcct
   * 
   * @param negFinAcct key
   * @param titaVo Variable-Length Argument
   * @return NegFinAcct NegFinAcct
   */
  public NegFinAcct holdById(NegFinAcct negFinAcct, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param negFinAcct Entity
   * @param titaVo Variable-Length Argument
   * @return NegFinAcct Entity
   * @throws DBException exception
   */
  public NegFinAcct insert(NegFinAcct negFinAcct, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param negFinAcct Entity
   * @param titaVo Variable-Length Argument
   * @return NegFinAcct Entity
   * @throws DBException exception
   */
  public NegFinAcct update(NegFinAcct negFinAcct, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param negFinAcct Entity
   * @param titaVo Variable-Length Argument
   * @return NegFinAcct Entity
   * @throws DBException exception
   */
  public NegFinAcct update2(NegFinAcct negFinAcct, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param negFinAcct Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(NegFinAcct negFinAcct, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param negFinAcct Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<NegFinAcct> negFinAcct, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param negFinAcct Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<NegFinAcct> negFinAcct, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param negFinAcct Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<NegFinAcct> negFinAcct, TitaVo... titaVo) throws DBException;

}
