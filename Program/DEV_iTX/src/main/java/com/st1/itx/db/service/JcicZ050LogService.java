package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicZ050Log;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicZ050LogId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ050LogService {

  /**
   * findByPrimaryKey
   *
   * @param jcicZ050LogId PK
   * @param titaVo Variable-Length Argument
   * @return JcicZ050Log JcicZ050Log
   */
  public JcicZ050Log findById(JcicZ050LogId jcicZ050LogId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ050Log JcicZ050Log of List
   */
  public Slice<JcicZ050Log> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * Ukey=
   *
   * @param ukey_0 ukey_0
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ050Log JcicZ050Log of List
   */
  public JcicZ050Log ukeyFirst(String ukey_0, TitaVo... titaVo);

  /**
   * Ukey=
   *
   * @param ukey_0 ukey_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ050Log JcicZ050Log of List
   */
  public Slice<JcicZ050Log> ukeyEq(String ukey_0, int index, int limit, TitaVo... titaVo);

  /**
   * hold By JcicZ050Log
   * 
   * @param jcicZ050LogId key
   * @param titaVo Variable-Length Argument
   * @return JcicZ050Log JcicZ050Log
   */
  public JcicZ050Log holdById(JcicZ050LogId jcicZ050LogId, TitaVo... titaVo);

  /**
   * hold By JcicZ050Log
   * 
   * @param jcicZ050Log key
   * @param titaVo Variable-Length Argument
   * @return JcicZ050Log JcicZ050Log
   */
  public JcicZ050Log holdById(JcicZ050Log jcicZ050Log, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param jcicZ050Log Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ050Log Entity
   * @throws DBException exception
   */
  public JcicZ050Log insert(JcicZ050Log jcicZ050Log, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param jcicZ050Log Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ050Log Entity
   * @throws DBException exception
   */
  public JcicZ050Log update(JcicZ050Log jcicZ050Log, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param jcicZ050Log Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ050Log Entity
   * @throws DBException exception
   */
  public JcicZ050Log update2(JcicZ050Log jcicZ050Log, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param jcicZ050Log Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(JcicZ050Log jcicZ050Log, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param jcicZ050Log Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<JcicZ050Log> jcicZ050Log, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param jcicZ050Log Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<JcicZ050Log> jcicZ050Log, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param jcicZ050Log Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<JcicZ050Log> jcicZ050Log, TitaVo... titaVo) throws DBException;

}
