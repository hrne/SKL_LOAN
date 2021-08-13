package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicZ570Log;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicZ570LogId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ570LogService {

  /**
   * findByPrimaryKey
   *
   * @param jcicZ570LogId PK
   * @param titaVo Variable-Length Argument
   * @return JcicZ570Log JcicZ570Log
   */
  public JcicZ570Log findById(JcicZ570LogId jcicZ570LogId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ570Log JcicZ570Log of List
   */
  public Slice<JcicZ570Log> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * Ukey=
   *
   * @param ukey_0 ukey_0
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ570Log JcicZ570Log of List
   */
  public JcicZ570Log ukeyFirst(String ukey_0, TitaVo... titaVo);

  /**
   * Ukey=
   *
   * @param ukey_0 ukey_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ570Log JcicZ570Log of List
   */
  public Slice<JcicZ570Log> ukeyEq(String ukey_0, int index, int limit, TitaVo... titaVo);

  /**
   * hold By JcicZ570Log
   * 
   * @param jcicZ570LogId key
   * @param titaVo Variable-Length Argument
   * @return JcicZ570Log JcicZ570Log
   */
  public JcicZ570Log holdById(JcicZ570LogId jcicZ570LogId, TitaVo... titaVo);

  /**
   * hold By JcicZ570Log
   * 
   * @param jcicZ570Log key
   * @param titaVo Variable-Length Argument
   * @return JcicZ570Log JcicZ570Log
   */
  public JcicZ570Log holdById(JcicZ570Log jcicZ570Log, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param jcicZ570Log Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ570Log Entity
   * @throws DBException exception
   */
  public JcicZ570Log insert(JcicZ570Log jcicZ570Log, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param jcicZ570Log Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ570Log Entity
   * @throws DBException exception
   */
  public JcicZ570Log update(JcicZ570Log jcicZ570Log, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param jcicZ570Log Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ570Log Entity
   * @throws DBException exception
   */
  public JcicZ570Log update2(JcicZ570Log jcicZ570Log, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param jcicZ570Log Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(JcicZ570Log jcicZ570Log, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param jcicZ570Log Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<JcicZ570Log> jcicZ570Log, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param jcicZ570Log Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<JcicZ570Log> jcicZ570Log, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param jcicZ570Log Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<JcicZ570Log> jcicZ570Log, TitaVo... titaVo) throws DBException;

}
