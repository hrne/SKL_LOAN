package com.st1.itx.db.service;

import java.util.List;

import java.math.BigDecimal;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.Lahgtp;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.LahgtpId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface LahgtpService {

  /**
   * findByPrimaryKey
   *
   * @param lahgtpId PK
   * @param titaVo Variable-Length Argument
   * @return Lahgtp Lahgtp
   */
  public Lahgtp findById(LahgtpId lahgtpId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice Lahgtp Lahgtp of List
   */
  public Slice<Lahgtp> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * hold By Lahgtp
   * 
   * @param lahgtpId key
   * @param titaVo Variable-Length Argument
   * @return Lahgtp Lahgtp
   */
  public Lahgtp holdById(LahgtpId lahgtpId, TitaVo... titaVo);

  /**
   * hold By Lahgtp
   * 
   * @param lahgtp key
   * @param titaVo Variable-Length Argument
   * @return Lahgtp Lahgtp
   */
  public Lahgtp holdById(Lahgtp lahgtp, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param lahgtp Entity
   * @param titaVo Variable-Length Argument
   * @return Lahgtp Entity
   * @throws DBException exception
   */
  public Lahgtp insert(Lahgtp lahgtp, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param lahgtp Entity
   * @param titaVo Variable-Length Argument
   * @return Lahgtp Entity
   * @throws DBException exception
   */
  public Lahgtp update(Lahgtp lahgtp, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param lahgtp Entity
   * @param titaVo Variable-Length Argument
   * @return Lahgtp Entity
   * @throws DBException exception
   */
  public Lahgtp update2(Lahgtp lahgtp, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param lahgtp Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(Lahgtp lahgtp, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param lahgtp Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<Lahgtp> lahgtp, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param lahgtp Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<Lahgtp> lahgtp, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param lahgtp Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<Lahgtp> lahgtp, TitaVo... titaVo) throws DBException;

}
