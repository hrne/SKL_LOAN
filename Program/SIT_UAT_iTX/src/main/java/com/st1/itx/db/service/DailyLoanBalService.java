package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.DailyLoanBal;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.DailyLoanBalId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface DailyLoanBalService {

  /**
   * findByPrimaryKey
   *
   * @param dailyLoanBalId PK
   * @param titaVo Variable-Length Argument
   * @return DailyLoanBal DailyLoanBal
   */
  public DailyLoanBal findById(DailyLoanBalId dailyLoanBalId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice DailyLoanBal DailyLoanBal of List
   */
  public Slice<DailyLoanBal> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * CustNo = ,AND FacmNo = ,AND BormNo = ,AND DataDate &lt;= 
   *
   * @param custNo_0 custNo_0
   * @param facmNo_1 facmNo_1
   * @param bormNo_2 bormNo_2
   * @param dataDate_3 dataDate_3
   * @param titaVo Variable-Length Argument
   * @return Slice DailyLoanBal DailyLoanBal of List
   */
  public DailyLoanBal dataDateFirst(int custNo_0, int facmNo_1, int bormNo_2, int dataDate_3, TitaVo... titaVo);

  /**
   * hold By DailyLoanBal
   * 
   * @param dailyLoanBalId key
   * @param titaVo Variable-Length Argument
   * @return DailyLoanBal DailyLoanBal
   */
  public DailyLoanBal holdById(DailyLoanBalId dailyLoanBalId, TitaVo... titaVo);

  /**
   * hold By DailyLoanBal
   * 
   * @param dailyLoanBal key
   * @param titaVo Variable-Length Argument
   * @return DailyLoanBal DailyLoanBal
   */
  public DailyLoanBal holdById(DailyLoanBal dailyLoanBal, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param dailyLoanBal Entity
   * @param titaVo Variable-Length Argument
   * @return DailyLoanBal Entity
   * @throws DBException exception
   */
  public DailyLoanBal insert(DailyLoanBal dailyLoanBal, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param dailyLoanBal Entity
   * @param titaVo Variable-Length Argument
   * @return DailyLoanBal Entity
   * @throws DBException exception
   */
  public DailyLoanBal update(DailyLoanBal dailyLoanBal, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param dailyLoanBal Entity
   * @param titaVo Variable-Length Argument
   * @return DailyLoanBal Entity
   * @throws DBException exception
   */
  public DailyLoanBal update2(DailyLoanBal dailyLoanBal, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param dailyLoanBal Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(DailyLoanBal dailyLoanBal, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param dailyLoanBal Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<DailyLoanBal> dailyLoanBal, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param dailyLoanBal Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<DailyLoanBal> dailyLoanBal, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param dailyLoanBal Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<DailyLoanBal> dailyLoanBal, TitaVo... titaVo) throws DBException;

  /**
   * Stored Procedure<br>
   * (日終批次)維護 DailyLoanBal每日放款餘額檔
   * @param  tbsdyf int
   * @param  empNo String
   * @param  mfbsdyf int
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_L9_DailyLoanBal_Upd(int tbsdyf,  String empNo, int mfbsdyf, TitaVo... titaVo);

}
