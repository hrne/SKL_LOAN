package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicZ051Log;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicZ051LogId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ051LogService {

  /**
   * findByPrimaryKey
   *
   * @param jcicZ051LogId PK
   * @param titaVo Variable-Length Argument
   * @return JcicZ051Log JcicZ051Log
   */
  public JcicZ051Log findById(JcicZ051LogId jcicZ051LogId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ051Log JcicZ051Log of List
   */
  public Slice<JcicZ051Log> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * Ukey=
   *
   * @param ukey_0 ukey_0
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ051Log JcicZ051Log of List
   */
  public JcicZ051Log ukeyFirst(String ukey_0, TitaVo... titaVo);

  /**
   * Ukey=
   *
   * @param ukey_0 ukey_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ051Log JcicZ051Log of List
   */
  public Slice<JcicZ051Log> ukeyEq(String ukey_0, int index, int limit, TitaVo... titaVo);

  /**
   * hold By JcicZ051Log
   * 
   * @param jcicZ051LogId key
   * @param titaVo Variable-Length Argument
   * @return JcicZ051Log JcicZ051Log
   */
  public JcicZ051Log holdById(JcicZ051LogId jcicZ051LogId, TitaVo... titaVo);

  /**
   * hold By JcicZ051Log
   * 
   * @param jcicZ051Log key
   * @param titaVo Variable-Length Argument
   * @return JcicZ051Log JcicZ051Log
   */
  public JcicZ051Log holdById(JcicZ051Log jcicZ051Log, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param jcicZ051Log Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ051Log Entity
   * @throws DBException exception
   */
  public JcicZ051Log insert(JcicZ051Log jcicZ051Log, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param jcicZ051Log Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ051Log Entity
   * @throws DBException exception
   */
  public JcicZ051Log update(JcicZ051Log jcicZ051Log, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param jcicZ051Log Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ051Log Entity
   * @throws DBException exception
   */
  public JcicZ051Log update2(JcicZ051Log jcicZ051Log, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param jcicZ051Log Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(JcicZ051Log jcicZ051Log, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param jcicZ051Log Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<JcicZ051Log> jcicZ051Log, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param jcicZ051Log Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<JcicZ051Log> jcicZ051Log, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param jcicZ051Log Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<JcicZ051Log> jcicZ051Log, TitaVo... titaVo) throws DBException;

}
