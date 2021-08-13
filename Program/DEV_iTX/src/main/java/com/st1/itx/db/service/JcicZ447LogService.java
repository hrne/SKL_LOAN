package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicZ447Log;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicZ447LogId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ447LogService {

  /**
   * findByPrimaryKey
   *
   * @param jcicZ447LogId PK
   * @param titaVo Variable-Length Argument
   * @return JcicZ447Log JcicZ447Log
   */
  public JcicZ447Log findById(JcicZ447LogId jcicZ447LogId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ447Log JcicZ447Log of List
   */
  public Slice<JcicZ447Log> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * Ukey=
   *
   * @param ukey_0 ukey_0
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ447Log JcicZ447Log of List
   */
  public JcicZ447Log ukeyFirst(String ukey_0, TitaVo... titaVo);

  /**
   * Ukey=
   *
   * @param ukey_0 ukey_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ447Log JcicZ447Log of List
   */
  public Slice<JcicZ447Log> ukeyEq(String ukey_0, int index, int limit, TitaVo... titaVo);

  /**
   * hold By JcicZ447Log
   * 
   * @param jcicZ447LogId key
   * @param titaVo Variable-Length Argument
   * @return JcicZ447Log JcicZ447Log
   */
  public JcicZ447Log holdById(JcicZ447LogId jcicZ447LogId, TitaVo... titaVo);

  /**
   * hold By JcicZ447Log
   * 
   * @param jcicZ447Log key
   * @param titaVo Variable-Length Argument
   * @return JcicZ447Log JcicZ447Log
   */
  public JcicZ447Log holdById(JcicZ447Log jcicZ447Log, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param jcicZ447Log Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ447Log Entity
   * @throws DBException exception
   */
  public JcicZ447Log insert(JcicZ447Log jcicZ447Log, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param jcicZ447Log Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ447Log Entity
   * @throws DBException exception
   */
  public JcicZ447Log update(JcicZ447Log jcicZ447Log, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param jcicZ447Log Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ447Log Entity
   * @throws DBException exception
   */
  public JcicZ447Log update2(JcicZ447Log jcicZ447Log, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param jcicZ447Log Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(JcicZ447Log jcicZ447Log, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param jcicZ447Log Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<JcicZ447Log> jcicZ447Log, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param jcicZ447Log Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<JcicZ447Log> jcicZ447Log, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param jcicZ447Log Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<JcicZ447Log> jcicZ447Log, TitaVo... titaVo) throws DBException;

}
