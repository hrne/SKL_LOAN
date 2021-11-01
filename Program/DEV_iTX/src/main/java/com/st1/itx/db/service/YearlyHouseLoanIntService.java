package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.YearlyHouseLoanInt;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.YearlyHouseLoanIntId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface YearlyHouseLoanIntService {

  /**
   * findByPrimaryKey
   *
   * @param yearlyHouseLoanIntId PK
   * @param titaVo Variable-Length Argument
   * @return YearlyHouseLoanInt YearlyHouseLoanInt
   */
  public YearlyHouseLoanInt findById(YearlyHouseLoanIntId yearlyHouseLoanIntId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice YearlyHouseLoanInt YearlyHouseLoanInt of List
   */
  public Slice<YearlyHouseLoanInt> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * YearMonth=
   *
   * @param yearMonth_0 yearMonth_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice YearlyHouseLoanInt YearlyHouseLoanInt of List
   */
  public Slice<YearlyHouseLoanInt> findYearMonth(int yearMonth_0, int index, int limit, TitaVo... titaVo);

  /**
   * CustNo=
   *
   * @param custNo_0 custNo_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice YearlyHouseLoanInt YearlyHouseLoanInt of List
   */
  public Slice<YearlyHouseLoanInt> findCustNo(int custNo_0, int index, int limit, TitaVo... titaVo);

  /**
   * YearMonth= ,AND CustNo=
   *
   * @param yearMonth_0 yearMonth_0
   * @param custNo_1 custNo_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice YearlyHouseLoanInt YearlyHouseLoanInt of List
   */
  public Slice<YearlyHouseLoanInt> findYearCustNo(int yearMonth_0, int custNo_1, int index, int limit, TitaVo... titaVo);

  /**
   * YearMonth&gt;= , AND YearMonth&lt;=
   *
   * @param yearMonth_0 yearMonth_0
   * @param yearMonth_1 yearMonth_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice YearlyHouseLoanInt YearlyHouseLoanInt of List
   */
  public Slice<YearlyHouseLoanInt> findbyYear(int yearMonth_0, int yearMonth_1, int index, int limit, TitaVo... titaVo);

  /**
   * hold By YearlyHouseLoanInt
   * 
   * @param yearlyHouseLoanIntId key
   * @param titaVo Variable-Length Argument
   * @return YearlyHouseLoanInt YearlyHouseLoanInt
   */
  public YearlyHouseLoanInt holdById(YearlyHouseLoanIntId yearlyHouseLoanIntId, TitaVo... titaVo);

  /**
   * hold By YearlyHouseLoanInt
   * 
   * @param yearlyHouseLoanInt key
   * @param titaVo Variable-Length Argument
   * @return YearlyHouseLoanInt YearlyHouseLoanInt
   */
  public YearlyHouseLoanInt holdById(YearlyHouseLoanInt yearlyHouseLoanInt, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param yearlyHouseLoanInt Entity
   * @param titaVo Variable-Length Argument
   * @return YearlyHouseLoanInt Entity
   * @throws DBException exception
   */
  public YearlyHouseLoanInt insert(YearlyHouseLoanInt yearlyHouseLoanInt, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param yearlyHouseLoanInt Entity
   * @param titaVo Variable-Length Argument
   * @return YearlyHouseLoanInt Entity
   * @throws DBException exception
   */
  public YearlyHouseLoanInt update(YearlyHouseLoanInt yearlyHouseLoanInt, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param yearlyHouseLoanInt Entity
   * @param titaVo Variable-Length Argument
   * @return YearlyHouseLoanInt Entity
   * @throws DBException exception
   */
  public YearlyHouseLoanInt update2(YearlyHouseLoanInt yearlyHouseLoanInt, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param yearlyHouseLoanInt Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(YearlyHouseLoanInt yearlyHouseLoanInt, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param yearlyHouseLoanInt Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<YearlyHouseLoanInt> yearlyHouseLoanInt, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param yearlyHouseLoanInt Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<YearlyHouseLoanInt> yearlyHouseLoanInt, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param yearlyHouseLoanInt Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<YearlyHouseLoanInt> yearlyHouseLoanInt, TitaVo... titaVo) throws DBException;

}
