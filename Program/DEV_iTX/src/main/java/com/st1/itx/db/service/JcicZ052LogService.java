package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicZ052Log;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicZ052LogId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ052LogService {

  /**
   * findByPrimaryKey
   *
   * @param jcicZ052LogId PK
   * @param titaVo Variable-Length Argument
   * @return JcicZ052Log JcicZ052Log
   */
  public JcicZ052Log findById(JcicZ052LogId jcicZ052LogId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ052Log JcicZ052Log of List
   */
  public Slice<JcicZ052Log> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * Ukey=
   *
   * @param ukey_0 ukey_0
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ052Log JcicZ052Log of List
   */
  public JcicZ052Log ukeyFirst(String ukey_0, TitaVo... titaVo);

  /**
   * Ukey=
   *
   * @param ukey_0 ukey_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ052Log JcicZ052Log of List
   */
  public Slice<JcicZ052Log> ukeyEq(String ukey_0, int index, int limit, TitaVo... titaVo);

  /**
   * hold By JcicZ052Log
   * 
   * @param jcicZ052LogId key
   * @param titaVo Variable-Length Argument
   * @return JcicZ052Log JcicZ052Log
   */
  public JcicZ052Log holdById(JcicZ052LogId jcicZ052LogId, TitaVo... titaVo);

  /**
   * hold By JcicZ052Log
   * 
   * @param jcicZ052Log key
   * @param titaVo Variable-Length Argument
   * @return JcicZ052Log JcicZ052Log
   */
  public JcicZ052Log holdById(JcicZ052Log jcicZ052Log, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param jcicZ052Log Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ052Log Entity
   * @throws DBException exception
   */
  public JcicZ052Log insert(JcicZ052Log jcicZ052Log, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param jcicZ052Log Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ052Log Entity
   * @throws DBException exception
   */
  public JcicZ052Log update(JcicZ052Log jcicZ052Log, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param jcicZ052Log Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ052Log Entity
   * @throws DBException exception
   */
  public JcicZ052Log update2(JcicZ052Log jcicZ052Log, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param jcicZ052Log Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(JcicZ052Log jcicZ052Log, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param jcicZ052Log Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<JcicZ052Log> jcicZ052Log, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param jcicZ052Log Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<JcicZ052Log> jcicZ052Log, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param jcicZ052Log Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<JcicZ052Log> jcicZ052Log, TitaVo... titaVo) throws DBException;

}
