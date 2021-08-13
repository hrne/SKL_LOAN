package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.BankRemit;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.BankRemitId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface BankRemitService {

  /**
   * findByPrimaryKey
   *
   * @param bankRemitId PK
   * @param titaVo Variable-Length Argument
   * @return BankRemit BankRemit
   */
  public BankRemit findById(BankRemitId bankRemitId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice BankRemit BankRemit of List
   */
  public Slice<BankRemit> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * AcDate &gt;= ,AND AcDate &lt;= 
   *
   * @param acDate_0 acDate_0
   * @param acDate_1 acDate_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice BankRemit BankRemit of List
   */
  public Slice<BankRemit> findL4001A(int acDate_0, int acDate_1, int index, int limit, TitaVo... titaVo);

  /**
   * CustNo = 
   *
   * @param custNo_0 custNo_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice BankRemit BankRemit of List
   */
  public Slice<BankRemit> findL4901A(int custNo_0, int index, int limit, TitaVo... titaVo);

  /**
   * AcDate = ,AND BatchNo = ,AND DrawdownCode &gt;= ,AND DrawdownCode &lt;= ,AND StatusCode &gt;= ,AND StatusCode &lt;= 
   *
   * @param acDate_0 acDate_0
   * @param batchNo_1 batchNo_1
   * @param drawdownCode_2 drawdownCode_2
   * @param drawdownCode_3 drawdownCode_3
   * @param statusCode_4 statusCode_4
   * @param statusCode_5 statusCode_5
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice BankRemit BankRemit of List
   */
  public Slice<BankRemit> findL4901B(int acDate_0, String batchNo_1, int drawdownCode_2, int drawdownCode_3, int statusCode_4, int statusCode_5, int index, int limit, TitaVo... titaVo);

  /**
   * AcDate = ,AND DrawdownCode &gt;= ,AND DrawdownCode &lt;= ,AND StatusCode &gt;= ,AND StatusCode &lt;= 
   *
   * @param acDate_0 acDate_0
   * @param drawdownCode_1 drawdownCode_1
   * @param drawdownCode_2 drawdownCode_2
   * @param statusCode_3 statusCode_3
   * @param statusCode_4 statusCode_4
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice BankRemit BankRemit of List
   */
  public Slice<BankRemit> findL4901C(int acDate_0, int drawdownCode_1, int drawdownCode_2, int statusCode_3, int statusCode_4, int index, int limit, TitaVo... titaVo);

  /**
   * hold By BankRemit
   * 
   * @param bankRemitId key
   * @param titaVo Variable-Length Argument
   * @return BankRemit BankRemit
   */
  public BankRemit holdById(BankRemitId bankRemitId, TitaVo... titaVo);

  /**
   * hold By BankRemit
   * 
   * @param bankRemit key
   * @param titaVo Variable-Length Argument
   * @return BankRemit BankRemit
   */
  public BankRemit holdById(BankRemit bankRemit, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param bankRemit Entity
   * @param titaVo Variable-Length Argument
   * @return BankRemit Entity
   * @throws DBException exception
   */
  public BankRemit insert(BankRemit bankRemit, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param bankRemit Entity
   * @param titaVo Variable-Length Argument
   * @return BankRemit Entity
   * @throws DBException exception
   */
  public BankRemit update(BankRemit bankRemit, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param bankRemit Entity
   * @param titaVo Variable-Length Argument
   * @return BankRemit Entity
   * @throws DBException exception
   */
  public BankRemit update2(BankRemit bankRemit, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param bankRemit Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(BankRemit bankRemit, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param bankRemit Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<BankRemit> bankRemit, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param bankRemit Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<BankRemit> bankRemit, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param bankRemit Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<BankRemit> bankRemit, TitaVo... titaVo) throws DBException;

}
