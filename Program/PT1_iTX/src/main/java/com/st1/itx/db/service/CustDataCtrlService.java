package com.st1.itx.db.service;

import java.util.List;

import java.math.BigDecimal;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CustDataCtrl;
import org.springframework.data.domain.Slice;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CustDataCtrlService {

  /**
   * findByPrimaryKey
   *
   * @param custNo PK
   * @param titaVo Variable-Length Argument
   * @return CustDataCtrl CustDataCtrl
   */
  public CustDataCtrl findById(int custNo, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CustDataCtrl CustDataCtrl of List
   */
  public Slice<CustDataCtrl> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * CustNo = 
   *
   * @param custNo_0 custNo_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CustDataCtrl CustDataCtrl of List
   */
  public Slice<CustDataCtrl> findCustNo(int custNo_0, int index, int limit, TitaVo... titaVo);

  /**
   * CustUKey = 
   *
   * @param custUKey_0 custUKey_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CustDataCtrl CustDataCtrl of List
   */
  public Slice<CustDataCtrl> findCustUKey(String custUKey_0, int index, int limit, TitaVo... titaVo);

  /**
   * CustId = 
   *
   * @param custId_0 custId_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CustDataCtrl CustDataCtrl of List
   */
  public Slice<CustDataCtrl> findCustId(String custId_0, int index, int limit, TitaVo... titaVo);

  /**
   * ApplMark ^i ,AND CustNo = 
   *
   * @param applMark_0 applMark_0
   * @param custNo_1 custNo_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CustDataCtrl CustDataCtrl of List
   */
  public Slice<CustDataCtrl> applMarkCustNo(List<Integer> applMark_0, int custNo_1, int index, int limit, TitaVo... titaVo);

  /**
   * ApplMark ^i ,AND CustUKey = 
   *
   * @param applMark_0 applMark_0
   * @param custUKey_1 custUKey_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CustDataCtrl CustDataCtrl of List
   */
  public Slice<CustDataCtrl> applMarkCustUKey(List<Integer> applMark_0, String custUKey_1, int index, int limit, TitaVo... titaVo);

  /**
   * ApplMark ^i ,AND CustId =
   *
   * @param applMark_0 applMark_0
   * @param custId_1 custId_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CustDataCtrl CustDataCtrl of List
   */
  public Slice<CustDataCtrl> applMarkCustId(List<Integer> applMark_0, String custId_1, int index, int limit, TitaVo... titaVo);

  /**
   * ApplMark ^i
   *
   * @param applMark_0 applMark_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CustDataCtrl CustDataCtrl of List
   */
  public Slice<CustDataCtrl> applMarkAll(List<Integer> applMark_0, int index, int limit, TitaVo... titaVo);

  /**
   * hold By CustDataCtrl
   * 
   * @param custNo key
   * @param titaVo Variable-Length Argument
   * @return CustDataCtrl CustDataCtrl
   */
  public CustDataCtrl holdById(int custNo, TitaVo... titaVo);

  /**
   * hold By CustDataCtrl
   * 
   * @param custDataCtrl key
   * @param titaVo Variable-Length Argument
   * @return CustDataCtrl CustDataCtrl
   */
  public CustDataCtrl holdById(CustDataCtrl custDataCtrl, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param custDataCtrl Entity
   * @param titaVo Variable-Length Argument
   * @return CustDataCtrl Entity
   * @throws DBException exception
   */
  public CustDataCtrl insert(CustDataCtrl custDataCtrl, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param custDataCtrl Entity
   * @param titaVo Variable-Length Argument
   * @return CustDataCtrl Entity
   * @throws DBException exception
   */
  public CustDataCtrl update(CustDataCtrl custDataCtrl, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param custDataCtrl Entity
   * @param titaVo Variable-Length Argument
   * @return CustDataCtrl Entity
   * @throws DBException exception
   */
  public CustDataCtrl update2(CustDataCtrl custDataCtrl, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param custDataCtrl Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(CustDataCtrl custDataCtrl, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param custDataCtrl Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<CustDataCtrl> custDataCtrl, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param custDataCtrl Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<CustDataCtrl> custDataCtrl, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param custDataCtrl Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<CustDataCtrl> custDataCtrl, TitaVo... titaVo) throws DBException;

}
