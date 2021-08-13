package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CustFin;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.CustFinId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CustFinService {

  /**
   * findByPrimaryKey
   *
   * @param custFinId PK
   * @param titaVo Variable-Length Argument
   * @return CustFin CustFin
   */
  public CustFin findById(CustFinId custFinId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CustFin CustFin of List
   */
  public Slice<CustFin> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * CustUKey = 
   *
   * @param custUKey_0 custUKey_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CustFin CustFin of List
   */
  public Slice<CustFin> custUKeyEq(String custUKey_0, int index, int limit, TitaVo... titaVo);

  /**
   * hold By CustFin
   * 
   * @param custFinId key
   * @param titaVo Variable-Length Argument
   * @return CustFin CustFin
   */
  public CustFin holdById(CustFinId custFinId, TitaVo... titaVo);

  /**
   * hold By CustFin
   * 
   * @param custFin key
   * @param titaVo Variable-Length Argument
   * @return CustFin CustFin
   */
  public CustFin holdById(CustFin custFin, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param custFin Entity
   * @param titaVo Variable-Length Argument
   * @return CustFin Entity
   * @throws DBException exception
   */
  public CustFin insert(CustFin custFin, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param custFin Entity
   * @param titaVo Variable-Length Argument
   * @return CustFin Entity
   * @throws DBException exception
   */
  public CustFin update(CustFin custFin, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param custFin Entity
   * @param titaVo Variable-Length Argument
   * @return CustFin Entity
   * @throws DBException exception
   */
  public CustFin update2(CustFin custFin, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param custFin Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(CustFin custFin, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param custFin Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<CustFin> custFin, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param custFin Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<CustFin> custFin, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param custFin Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<CustFin> custFin, TitaVo... titaVo) throws DBException;

}
