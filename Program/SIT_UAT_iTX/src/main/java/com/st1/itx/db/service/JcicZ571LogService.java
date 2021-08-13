package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicZ571Log;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicZ571LogId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ571LogService {

  /**
   * findByPrimaryKey
   *
   * @param jcicZ571LogId PK
   * @param titaVo Variable-Length Argument
   * @return JcicZ571Log JcicZ571Log
   */
  public JcicZ571Log findById(JcicZ571LogId jcicZ571LogId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ571Log JcicZ571Log of List
   */
  public Slice<JcicZ571Log> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * Ukey=
   *
   * @param ukey_0 ukey_0
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ571Log JcicZ571Log of List
   */
  public JcicZ571Log ukeyFirst(String ukey_0, TitaVo... titaVo);

  /**
   * Ukey=
   *
   * @param ukey_0 ukey_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ571Log JcicZ571Log of List
   */
  public Slice<JcicZ571Log> ukeyEq(String ukey_0, int index, int limit, TitaVo... titaVo);

  /**
   * hold By JcicZ571Log
   * 
   * @param jcicZ571LogId key
   * @param titaVo Variable-Length Argument
   * @return JcicZ571Log JcicZ571Log
   */
  public JcicZ571Log holdById(JcicZ571LogId jcicZ571LogId, TitaVo... titaVo);

  /**
   * hold By JcicZ571Log
   * 
   * @param jcicZ571Log key
   * @param titaVo Variable-Length Argument
   * @return JcicZ571Log JcicZ571Log
   */
  public JcicZ571Log holdById(JcicZ571Log jcicZ571Log, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param jcicZ571Log Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ571Log Entity
   * @throws DBException exception
   */
  public JcicZ571Log insert(JcicZ571Log jcicZ571Log, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param jcicZ571Log Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ571Log Entity
   * @throws DBException exception
   */
  public JcicZ571Log update(JcicZ571Log jcicZ571Log, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param jcicZ571Log Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ571Log Entity
   * @throws DBException exception
   */
  public JcicZ571Log update2(JcicZ571Log jcicZ571Log, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param jcicZ571Log Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(JcicZ571Log jcicZ571Log, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param jcicZ571Log Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<JcicZ571Log> jcicZ571Log, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param jcicZ571Log Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<JcicZ571Log> jcicZ571Log, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param jcicZ571Log Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<JcicZ571Log> jcicZ571Log, TitaVo... titaVo) throws DBException;

}
