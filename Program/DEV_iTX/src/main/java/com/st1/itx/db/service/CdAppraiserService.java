package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CdAppraiser;
import org.springframework.data.domain.Slice;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CdAppraiserService {

  /**
   * findByPrimaryKey
   *
   * @param appraiserCode PK
   * @param titaVo Variable-Length Argument
   * @return CdAppraiser CdAppraiser
   */
  public CdAppraiser findById(String appraiserCode, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdAppraiser CdAppraiser of List
   */
  public Slice<CdAppraiser> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * hold By CdAppraiser
   * 
   * @param appraiserCode key
   * @param titaVo Variable-Length Argument
   * @return CdAppraiser CdAppraiser
   */
  public CdAppraiser holdById(String appraiserCode, TitaVo... titaVo);

  /**
   * hold By CdAppraiser
   * 
   * @param cdAppraiser key
   * @param titaVo Variable-Length Argument
   * @return CdAppraiser CdAppraiser
   */
  public CdAppraiser holdById(CdAppraiser cdAppraiser, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param cdAppraiser Entity
   * @param titaVo Variable-Length Argument
   * @return CdAppraiser Entity
   * @throws DBException exception
   */
  public CdAppraiser insert(CdAppraiser cdAppraiser, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param cdAppraiser Entity
   * @param titaVo Variable-Length Argument
   * @return CdAppraiser Entity
   * @throws DBException exception
   */
  public CdAppraiser update(CdAppraiser cdAppraiser, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param cdAppraiser Entity
   * @param titaVo Variable-Length Argument
   * @return CdAppraiser Entity
   * @throws DBException exception
   */
  public CdAppraiser update2(CdAppraiser cdAppraiser, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param cdAppraiser Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(CdAppraiser cdAppraiser, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param cdAppraiser Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<CdAppraiser> cdAppraiser, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param cdAppraiser Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<CdAppraiser> cdAppraiser, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param cdAppraiser Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<CdAppraiser> cdAppraiser, TitaVo... titaVo) throws DBException;

}
