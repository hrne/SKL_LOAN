package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicZ053Log;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicZ053LogId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ053LogService {

  /**
   * findByPrimaryKey
   *
   * @param jcicZ053LogId PK
   * @param titaVo Variable-Length Argument
   * @return JcicZ053Log JcicZ053Log
   */
  public JcicZ053Log findById(JcicZ053LogId jcicZ053LogId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ053Log JcicZ053Log of List
   */
  public Slice<JcicZ053Log> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * Ukey=
   *
   * @param ukey_0 ukey_0
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ053Log JcicZ053Log of List
   */
  public JcicZ053Log ukeyFirst(String ukey_0, TitaVo... titaVo);

  /**
   * Ukey=
   *
   * @param ukey_0 ukey_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ053Log JcicZ053Log of List
   */
  public Slice<JcicZ053Log> ukeyEq(String ukey_0, int index, int limit, TitaVo... titaVo);

  /**
   * hold By JcicZ053Log
   * 
   * @param jcicZ053LogId key
   * @param titaVo Variable-Length Argument
   * @return JcicZ053Log JcicZ053Log
   */
  public JcicZ053Log holdById(JcicZ053LogId jcicZ053LogId, TitaVo... titaVo);

  /**
   * hold By JcicZ053Log
   * 
   * @param jcicZ053Log key
   * @param titaVo Variable-Length Argument
   * @return JcicZ053Log JcicZ053Log
   */
  public JcicZ053Log holdById(JcicZ053Log jcicZ053Log, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param jcicZ053Log Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ053Log Entity
   * @throws DBException exception
   */
  public JcicZ053Log insert(JcicZ053Log jcicZ053Log, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param jcicZ053Log Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ053Log Entity
   * @throws DBException exception
   */
  public JcicZ053Log update(JcicZ053Log jcicZ053Log, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param jcicZ053Log Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ053Log Entity
   * @throws DBException exception
   */
  public JcicZ053Log update2(JcicZ053Log jcicZ053Log, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param jcicZ053Log Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(JcicZ053Log jcicZ053Log, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param jcicZ053Log Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<JcicZ053Log> jcicZ053Log, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param jcicZ053Log Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<JcicZ053Log> jcicZ053Log, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param jcicZ053Log Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<JcicZ053Log> jcicZ053Log, TitaVo... titaVo) throws DBException;

}
