package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.DailyTav;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.DailyTavId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface DailyTavService {

  /**
   * findByPrimaryKey
   *
   * @param dailyTavId PK
   * @param titaVo Variable-Length Argument
   * @return DailyTav DailyTav
   */
  public DailyTav findById(DailyTavId dailyTavId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice DailyTav DailyTav of List
   */
  public Slice<DailyTav> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * CustNo = ,AND AcDate &gt;= ,AND AcDate &lt;=
   *
   * @param custNo_0 custNo_0
   * @param acDate_1 acDate_1
   * @param acDate_2 acDate_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice DailyTav DailyTav of List
   */
  public Slice<DailyTav> CustNoAcDateRange(int custNo_0, int acDate_1, int acDate_2, int index, int limit, TitaVo... titaVo);

  /**
   * hold By DailyTav
   * 
   * @param dailyTavId key
   * @param titaVo Variable-Length Argument
   * @return DailyTav DailyTav
   */
  public DailyTav holdById(DailyTavId dailyTavId, TitaVo... titaVo);

  /**
   * hold By DailyTav
   * 
   * @param dailyTav key
   * @param titaVo Variable-Length Argument
   * @return DailyTav DailyTav
   */
  public DailyTav holdById(DailyTav dailyTav, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param dailyTav Entity
   * @param titaVo Variable-Length Argument
   * @return DailyTav Entity
   * @throws DBException exception
   */
  public DailyTav insert(DailyTav dailyTav, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param dailyTav Entity
   * @param titaVo Variable-Length Argument
   * @return DailyTav Entity
   * @throws DBException exception
   */
  public DailyTav update(DailyTav dailyTav, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param dailyTav Entity
   * @param titaVo Variable-Length Argument
   * @return DailyTav Entity
   * @throws DBException exception
   */
  public DailyTav update2(DailyTav dailyTav, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param dailyTav Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(DailyTav dailyTav, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param dailyTav Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<DailyTav> dailyTav, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param dailyTav Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<DailyTav> dailyTav, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param dailyTav Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<DailyTav> dailyTav, TitaVo... titaVo) throws DBException;

}
