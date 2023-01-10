package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.LifeRelHead;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.LifeRelHeadId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface LifeRelHeadService {

  /**
   * findByPrimaryKey
   *
   * @param lifeRelHeadId PK
   * @param titaVo Variable-Length Argument
   * @return LifeRelHead LifeRelHead
   */
  public LifeRelHead findById(LifeRelHeadId lifeRelHeadId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice LifeRelHead LifeRelHead of List
   */
  public Slice<LifeRelHead> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * hold By LifeRelHead
   * 
   * @param lifeRelHeadId key
   * @param titaVo Variable-Length Argument
   * @return LifeRelHead LifeRelHead
   */
  public LifeRelHead holdById(LifeRelHeadId lifeRelHeadId, TitaVo... titaVo);

  /**
   * hold By LifeRelHead
   * 
   * @param lifeRelHead key
   * @param titaVo Variable-Length Argument
   * @return LifeRelHead LifeRelHead
   */
  public LifeRelHead holdById(LifeRelHead lifeRelHead, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param lifeRelHead Entity
   * @param titaVo Variable-Length Argument
   * @return LifeRelHead Entity
   * @throws DBException exception
   */
  public LifeRelHead insert(LifeRelHead lifeRelHead, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param lifeRelHead Entity
   * @param titaVo Variable-Length Argument
   * @return LifeRelHead Entity
   * @throws DBException exception
   */
  public LifeRelHead update(LifeRelHead lifeRelHead, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param lifeRelHead Entity
   * @param titaVo Variable-Length Argument
   * @return LifeRelHead Entity
   * @throws DBException exception
   */
  public LifeRelHead update2(LifeRelHead lifeRelHead, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param lifeRelHead Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(LifeRelHead lifeRelHead, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param lifeRelHead Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<LifeRelHead> lifeRelHead, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param lifeRelHead Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<LifeRelHead> lifeRelHead, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param lifeRelHead Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<LifeRelHead> lifeRelHead, TitaVo... titaVo) throws DBException;

}
