package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.BatxCheque;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.BatxChequeId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface BatxChequeService {

  /**
   * findByPrimaryKey
   *
   * @param batxChequeId PK
   * @param titaVo Variable-Length Argument
   * @return BatxCheque BatxCheque
   */
  public BatxCheque findById(BatxChequeId batxChequeId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice BatxCheque BatxCheque of List
   */
  public Slice<BatxCheque> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * hold By BatxCheque
   * 
   * @param batxChequeId key
   * @param titaVo Variable-Length Argument
   * @return BatxCheque BatxCheque
   */
  public BatxCheque holdById(BatxChequeId batxChequeId, TitaVo... titaVo);

  /**
   * hold By BatxCheque
   * 
   * @param batxCheque key
   * @param titaVo Variable-Length Argument
   * @return BatxCheque BatxCheque
   */
  public BatxCheque holdById(BatxCheque batxCheque, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param batxCheque Entity
   * @param titaVo Variable-Length Argument
   * @return BatxCheque Entity
   * @throws DBException exception
   */
  public BatxCheque insert(BatxCheque batxCheque, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param batxCheque Entity
   * @param titaVo Variable-Length Argument
   * @return BatxCheque Entity
   * @throws DBException exception
   */
  public BatxCheque update(BatxCheque batxCheque, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param batxCheque Entity
   * @param titaVo Variable-Length Argument
   * @return BatxCheque Entity
   * @throws DBException exception
   */
  public BatxCheque update2(BatxCheque batxCheque, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param batxCheque Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(BatxCheque batxCheque, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param batxCheque Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<BatxCheque> batxCheque, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param batxCheque Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<BatxCheque> batxCheque, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param batxCheque Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<BatxCheque> batxCheque, TitaVo... titaVo) throws DBException;

}
