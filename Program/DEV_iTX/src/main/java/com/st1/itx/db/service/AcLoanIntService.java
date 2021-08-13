package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.AcLoanInt;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.AcLoanIntId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface AcLoanIntService {

  /**
   * findByPrimaryKey
   *
   * @param acLoanIntId PK
   * @param titaVo Variable-Length Argument
   * @return AcLoanInt AcLoanInt
   */
  public AcLoanInt findById(AcLoanIntId acLoanIntId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice AcLoanInt AcLoanInt of List
   */
  public Slice<AcLoanInt> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * YearMonth =  
   *
   * @param yearMonth_0 yearMonth_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice AcLoanInt AcLoanInt of List
   */
  public Slice<AcLoanInt> findYearMonthEq(int yearMonth_0, int index, int limit, TitaVo... titaVo);

  /**
   * hold By AcLoanInt
   * 
   * @param acLoanIntId key
   * @param titaVo Variable-Length Argument
   * @return AcLoanInt AcLoanInt
   */
  public AcLoanInt holdById(AcLoanIntId acLoanIntId, TitaVo... titaVo);

  /**
   * hold By AcLoanInt
   * 
   * @param acLoanInt key
   * @param titaVo Variable-Length Argument
   * @return AcLoanInt AcLoanInt
   */
  public AcLoanInt holdById(AcLoanInt acLoanInt, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param acLoanInt Entity
   * @param titaVo Variable-Length Argument
   * @return AcLoanInt Entity
   * @throws DBException exception
   */
  public AcLoanInt insert(AcLoanInt acLoanInt, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param acLoanInt Entity
   * @param titaVo Variable-Length Argument
   * @return AcLoanInt Entity
   * @throws DBException exception
   */
  public AcLoanInt update(AcLoanInt acLoanInt, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param acLoanInt Entity
   * @param titaVo Variable-Length Argument
   * @return AcLoanInt Entity
   * @throws DBException exception
   */
  public AcLoanInt update2(AcLoanInt acLoanInt, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param acLoanInt Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(AcLoanInt acLoanInt, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param acLoanInt Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<AcLoanInt> acLoanInt, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param acLoanInt Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<AcLoanInt> acLoanInt, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param acLoanInt Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<AcLoanInt> acLoanInt, TitaVo... titaVo) throws DBException;

}
