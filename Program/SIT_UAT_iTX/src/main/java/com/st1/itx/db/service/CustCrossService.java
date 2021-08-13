package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CustCross;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.CustCrossId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CustCrossService {

  /**
   * findByPrimaryKey
   *
   * @param custCrossId PK
   * @param titaVo Variable-Length Argument
   * @return CustCross CustCross
   */
  public CustCross findById(CustCrossId custCrossId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CustCross CustCross of List
   */
  public Slice<CustCross> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * CustUKey = 
   *
   * @param custUKey_0 custUKey_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CustCross CustCross of List
   */
  public Slice<CustCross> custUKeyEq(String custUKey_0, int index, int limit, TitaVo... titaVo);

  /**
   * CustUKey = ,AND SubCompanyCode = 
   *
   * @param custUKey_0 custUKey_0
   * @param subCompanyCode_1 subCompanyCode_1
   * @param titaVo Variable-Length Argument
   * @return Slice CustCross CustCross of List
   */
  public CustCross subCompanyCodeFirst(String custUKey_0, String subCompanyCode_1, TitaVo... titaVo);

  /**
   * hold By CustCross
   * 
   * @param custCrossId key
   * @param titaVo Variable-Length Argument
   * @return CustCross CustCross
   */
  public CustCross holdById(CustCrossId custCrossId, TitaVo... titaVo);

  /**
   * hold By CustCross
   * 
   * @param custCross key
   * @param titaVo Variable-Length Argument
   * @return CustCross CustCross
   */
  public CustCross holdById(CustCross custCross, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param custCross Entity
   * @param titaVo Variable-Length Argument
   * @return CustCross Entity
   * @throws DBException exception
   */
  public CustCross insert(CustCross custCross, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param custCross Entity
   * @param titaVo Variable-Length Argument
   * @return CustCross Entity
   * @throws DBException exception
   */
  public CustCross update(CustCross custCross, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param custCross Entity
   * @param titaVo Variable-Length Argument
   * @return CustCross Entity
   * @throws DBException exception
   */
  public CustCross update2(CustCross custCross, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param custCross Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(CustCross custCross, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param custCross Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<CustCross> custCross, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param custCross Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<CustCross> custCross, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param custCross Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<CustCross> custCross, TitaVo... titaVo) throws DBException;

}
