package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.YearlyHouseLoanIntCheck;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.YearlyHouseLoanIntCheckId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface YearlyHouseLoanIntCheckService {

  /**
   * findByPrimaryKey
   *
   * @param yearlyHouseLoanIntCheckId PK
   * @param titaVo Variable-Length Argument
   * @return YearlyHouseLoanIntCheck YearlyHouseLoanIntCheck
   */
  public YearlyHouseLoanIntCheck findById(YearlyHouseLoanIntCheckId yearlyHouseLoanIntCheckId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice YearlyHouseLoanIntCheck YearlyHouseLoanIntCheck of List
   */
  public Slice<YearlyHouseLoanIntCheck> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * YearMonth=
   *
   * @param yearMonth_0 yearMonth_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice YearlyHouseLoanIntCheck YearlyHouseLoanIntCheck of List
   */
  public Slice<YearlyHouseLoanIntCheck> findYearMonth(int yearMonth_0, int index, int limit, TitaVo... titaVo);

  /**
   * hold By YearlyHouseLoanIntCheck
   * 
   * @param yearlyHouseLoanIntCheckId key
   * @param titaVo Variable-Length Argument
   * @return YearlyHouseLoanIntCheck YearlyHouseLoanIntCheck
   */
  public YearlyHouseLoanIntCheck holdById(YearlyHouseLoanIntCheckId yearlyHouseLoanIntCheckId, TitaVo... titaVo);

  /**
   * hold By YearlyHouseLoanIntCheck
   * 
   * @param yearlyHouseLoanIntCheck key
   * @param titaVo Variable-Length Argument
   * @return YearlyHouseLoanIntCheck YearlyHouseLoanIntCheck
   */
  public YearlyHouseLoanIntCheck holdById(YearlyHouseLoanIntCheck yearlyHouseLoanIntCheck, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param yearlyHouseLoanIntCheck Entity
   * @param titaVo Variable-Length Argument
   * @return YearlyHouseLoanIntCheck Entity
   * @throws DBException exception
   */
  public YearlyHouseLoanIntCheck insert(YearlyHouseLoanIntCheck yearlyHouseLoanIntCheck, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param yearlyHouseLoanIntCheck Entity
   * @param titaVo Variable-Length Argument
   * @return YearlyHouseLoanIntCheck Entity
   * @throws DBException exception
   */
  public YearlyHouseLoanIntCheck update(YearlyHouseLoanIntCheck yearlyHouseLoanIntCheck, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param yearlyHouseLoanIntCheck Entity
   * @param titaVo Variable-Length Argument
   * @return YearlyHouseLoanIntCheck Entity
   * @throws DBException exception
   */
  public YearlyHouseLoanIntCheck update2(YearlyHouseLoanIntCheck yearlyHouseLoanIntCheck, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param yearlyHouseLoanIntCheck Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(YearlyHouseLoanIntCheck yearlyHouseLoanIntCheck, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param yearlyHouseLoanIntCheck Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<YearlyHouseLoanIntCheck> yearlyHouseLoanIntCheck, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param yearlyHouseLoanIntCheck Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<YearlyHouseLoanIntCheck> yearlyHouseLoanIntCheck, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param yearlyHouseLoanIntCheck Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<YearlyHouseLoanIntCheck> yearlyHouseLoanIntCheck, TitaVo... titaVo) throws DBException;

}
