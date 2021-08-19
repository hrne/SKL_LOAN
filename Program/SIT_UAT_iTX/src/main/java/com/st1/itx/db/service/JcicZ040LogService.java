package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicZ040Log;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicZ040LogId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ040LogService {

  /**
   * findByPrimaryKey
   *
   * @param jcicZ040LogId PK
   * @param titaVo Variable-Length Argument
   * @return JcicZ040Log JcicZ040Log
   */
  public JcicZ040Log findById(JcicZ040LogId jcicZ040LogId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ040Log JcicZ040Log of List
   */
  public Slice<JcicZ040Log> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * Ukey=
   *
   * @param ukey_0 ukey_0
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ040Log JcicZ040Log of List
   */
  public JcicZ040Log ukeyFirst(String ukey_0, TitaVo... titaVo);

  /**
   * Ukey=
   *
   * @param ukey_0 ukey_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ040Log JcicZ040Log of List
   */
  public Slice<JcicZ040Log> ukeyEq(String ukey_0, int index, int limit, TitaVo... titaVo);

  /**
   * hold By JcicZ040Log
   * 
   * @param jcicZ040LogId key
   * @param titaVo Variable-Length Argument
   * @return JcicZ040Log JcicZ040Log
   */
  public JcicZ040Log holdById(JcicZ040LogId jcicZ040LogId, TitaVo... titaVo);

  /**
   * hold By JcicZ040Log
   * 
   * @param jcicZ040Log key
   * @param titaVo Variable-Length Argument
   * @return JcicZ040Log JcicZ040Log
   */
  public JcicZ040Log holdById(JcicZ040Log jcicZ040Log, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param jcicZ040Log Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ040Log Entity
   * @throws DBException exception
   */
  public JcicZ040Log insert(JcicZ040Log jcicZ040Log, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param jcicZ040Log Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ040Log Entity
   * @throws DBException exception
   */
  public JcicZ040Log update(JcicZ040Log jcicZ040Log, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param jcicZ040Log Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ040Log Entity
   * @throws DBException exception
   */
  public JcicZ040Log update2(JcicZ040Log jcicZ040Log, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param jcicZ040Log Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(JcicZ040Log jcicZ040Log, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param jcicZ040Log Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<JcicZ040Log> jcicZ040Log, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param jcicZ040Log Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<JcicZ040Log> jcicZ040Log, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param jcicZ040Log Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<JcicZ040Log> jcicZ040Log, TitaVo... titaVo) throws DBException;

}
