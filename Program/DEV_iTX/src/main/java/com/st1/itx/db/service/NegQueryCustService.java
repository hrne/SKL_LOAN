package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.NegQueryCust;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.NegQueryCustId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface NegQueryCustService {

  /**
   * findByPrimaryKey
   *
   * @param negQueryCustId PK
   * @param titaVo Variable-Length Argument
   * @return NegQueryCust NegQueryCust
   */
  public NegQueryCust findById(NegQueryCustId negQueryCustId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice NegQueryCust NegQueryCust of List
   */
  public Slice<NegQueryCust> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * CustId=
   *
   * @param custId_0 custId_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice NegQueryCust NegQueryCust of List
   */
  public Slice<NegQueryCust> custIdEq(String custId_0, int index, int limit, TitaVo... titaVo);

  /**
   * AcDate= ,AND FileYN= 
   *
   * @param acDate_0 acDate_0
   * @param fileYN_1 fileYN_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice NegQueryCust NegQueryCust of List
   */
  public Slice<NegQueryCust> firstAcDate(int acDate_0, String fileYN_1, int index, int limit, TitaVo... titaVo);

  /**
   * AcDate= 
   *
   * @param acDate_0 acDate_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice NegQueryCust NegQueryCust of List
   */
  public Slice<NegQueryCust> acDateEq(int acDate_0, int index, int limit, TitaVo... titaVo);

  /**
   * hold By NegQueryCust
   * 
   * @param negQueryCustId key
   * @param titaVo Variable-Length Argument
   * @return NegQueryCust NegQueryCust
   */
  public NegQueryCust holdById(NegQueryCustId negQueryCustId, TitaVo... titaVo);

  /**
   * hold By NegQueryCust
   * 
   * @param negQueryCust key
   * @param titaVo Variable-Length Argument
   * @return NegQueryCust NegQueryCust
   */
  public NegQueryCust holdById(NegQueryCust negQueryCust, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param negQueryCust Entity
   * @param titaVo Variable-Length Argument
   * @return NegQueryCust Entity
   * @throws DBException exception
   */
  public NegQueryCust insert(NegQueryCust negQueryCust, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param negQueryCust Entity
   * @param titaVo Variable-Length Argument
   * @return NegQueryCust Entity
   * @throws DBException exception
   */
  public NegQueryCust update(NegQueryCust negQueryCust, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param negQueryCust Entity
   * @param titaVo Variable-Length Argument
   * @return NegQueryCust Entity
   * @throws DBException exception
   */
  public NegQueryCust update2(NegQueryCust negQueryCust, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param negQueryCust Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(NegQueryCust negQueryCust, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param negQueryCust Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<NegQueryCust> negQueryCust, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param negQueryCust Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<NegQueryCust> negQueryCust, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param negQueryCust Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<NegQueryCust> negQueryCust, TitaVo... titaVo) throws DBException;

}
