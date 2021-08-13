package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.AcCheque;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.AcChequeId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface AcChequeService {

  /**
   * findByPrimaryKey
   *
   * @param acChequeId PK
   * @param titaVo Variable-Length Argument
   * @return AcCheque AcCheque
   */
  public AcCheque findById(AcChequeId acChequeId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice AcCheque AcCheque of List
   */
  public Slice<AcCheque> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * hold By AcCheque
   * 
   * @param acChequeId key
   * @param titaVo Variable-Length Argument
   * @return AcCheque AcCheque
   */
  public AcCheque holdById(AcChequeId acChequeId, TitaVo... titaVo);

  /**
   * hold By AcCheque
   * 
   * @param acCheque key
   * @param titaVo Variable-Length Argument
   * @return AcCheque AcCheque
   */
  public AcCheque holdById(AcCheque acCheque, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param acCheque Entity
   * @param titaVo Variable-Length Argument
   * @return AcCheque Entity
   * @throws DBException exception
   */
  public AcCheque insert(AcCheque acCheque, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param acCheque Entity
   * @param titaVo Variable-Length Argument
   * @return AcCheque Entity
   * @throws DBException exception
   */
  public AcCheque update(AcCheque acCheque, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param acCheque Entity
   * @param titaVo Variable-Length Argument
   * @return AcCheque Entity
   * @throws DBException exception
   */
  public AcCheque update2(AcCheque acCheque, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param acCheque Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(AcCheque acCheque, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param acCheque Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<AcCheque> acCheque, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param acCheque Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<AcCheque> acCheque, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param acCheque Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<AcCheque> acCheque, TitaVo... titaVo) throws DBException;

}
