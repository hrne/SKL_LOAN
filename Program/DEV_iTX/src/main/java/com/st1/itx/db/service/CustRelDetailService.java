package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CustRelDetail;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.CustRelDetailId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CustRelDetailService {

  /**
   * findByPrimaryKey
   *
   * @param custRelDetailId PK
   * @param titaVo Variable-Length Argument
   * @return CustRelDetail CustRelDetail
   */
  public CustRelDetail findById(CustRelDetailId custRelDetailId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CustRelDetail CustRelDetail of List
   */
  public Slice<CustRelDetail> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * CustRelMainUKey= 
   *
   * @param custRelMainUKey_0 custRelMainUKey_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CustRelDetail CustRelDetail of List
   */
  public Slice<CustRelDetail> custRelMainUKeyEq(String custRelMainUKey_0, int index, int limit, TitaVo... titaVo);

  /**
   * RelId=
   *
   * @param relId_0 relId_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CustRelDetail CustRelDetail of List
   */
  public Slice<CustRelDetail> relIdEq(String relId_0, int index, int limit, TitaVo... titaVo);

  /**
   * RelId=
   *
   * @param relId_0 relId_0
   * @param titaVo Variable-Length Argument
   * @return Slice CustRelDetail CustRelDetail of List
   */
  public CustRelDetail relIdFirst(String relId_0, TitaVo... titaVo);

  /**
   * hold By CustRelDetail
   * 
   * @param custRelDetailId key
   * @param titaVo Variable-Length Argument
   * @return CustRelDetail CustRelDetail
   */
  public CustRelDetail holdById(CustRelDetailId custRelDetailId, TitaVo... titaVo);

  /**
   * hold By CustRelDetail
   * 
   * @param custRelDetail key
   * @param titaVo Variable-Length Argument
   * @return CustRelDetail CustRelDetail
   */
  public CustRelDetail holdById(CustRelDetail custRelDetail, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param custRelDetail Entity
   * @param titaVo Variable-Length Argument
   * @return CustRelDetail Entity
   * @throws DBException exception
   */
  public CustRelDetail insert(CustRelDetail custRelDetail, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param custRelDetail Entity
   * @param titaVo Variable-Length Argument
   * @return CustRelDetail Entity
   * @throws DBException exception
   */
  public CustRelDetail update(CustRelDetail custRelDetail, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param custRelDetail Entity
   * @param titaVo Variable-Length Argument
   * @return CustRelDetail Entity
   * @throws DBException exception
   */
  public CustRelDetail update2(CustRelDetail custRelDetail, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param custRelDetail Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(CustRelDetail custRelDetail, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param custRelDetail Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<CustRelDetail> custRelDetail, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param custRelDetail Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<CustRelDetail> custRelDetail, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param custRelDetail Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<CustRelDetail> custRelDetail, TitaVo... titaVo) throws DBException;

}
