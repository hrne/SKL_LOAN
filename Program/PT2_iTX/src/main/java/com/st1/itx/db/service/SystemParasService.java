package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.SystemParas;
import org.springframework.data.domain.Slice;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface SystemParasService {

  /**
   * findByPrimaryKey
   *
   * @param businessType PK
   * @param titaVo Variable-Length Argument
   * @return SystemParas SystemParas
   */
  public SystemParas findById(String businessType, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice SystemParas SystemParas of List
   */
  public Slice<SystemParas> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * hold By SystemParas
   * 
   * @param businessType key
   * @param titaVo Variable-Length Argument
   * @return SystemParas SystemParas
   */
  public SystemParas holdById(String businessType, TitaVo... titaVo);

  /**
   * hold By SystemParas
   * 
   * @param systemParas key
   * @param titaVo Variable-Length Argument
   * @return SystemParas SystemParas
   */
  public SystemParas holdById(SystemParas systemParas, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param systemParas Entity
   * @param titaVo Variable-Length Argument
   * @return SystemParas Entity
   * @throws DBException exception
   */
  public SystemParas insert(SystemParas systemParas, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param systemParas Entity
   * @param titaVo Variable-Length Argument
   * @return SystemParas Entity
   * @throws DBException exception
   */
  public SystemParas update(SystemParas systemParas, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param systemParas Entity
   * @param titaVo Variable-Length Argument
   * @return SystemParas Entity
   * @throws DBException exception
   */
  public SystemParas update2(SystemParas systemParas, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param systemParas Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(SystemParas systemParas, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param systemParas Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<SystemParas> systemParas, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param systemParas Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<SystemParas> systemParas, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param systemParas Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<SystemParas> systemParas, TitaVo... titaVo) throws DBException;

}
