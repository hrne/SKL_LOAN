package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicZ042Log;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicZ042LogId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ042LogService {

  /**
   * findByPrimaryKey
   *
   * @param jcicZ042LogId PK
   * @param titaVo Variable-Length Argument
   * @return JcicZ042Log JcicZ042Log
   */
  public JcicZ042Log findById(JcicZ042LogId jcicZ042LogId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ042Log JcicZ042Log of List
   */
  public Slice<JcicZ042Log> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * Ukey=
   *
   * @param ukey_0 ukey_0
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ042Log JcicZ042Log of List
   */
  public JcicZ042Log ukeyFirst(String ukey_0, TitaVo... titaVo);

  /**
   * Ukey=
   *
   * @param ukey_0 ukey_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ042Log JcicZ042Log of List
   */
  public Slice<JcicZ042Log> ukeyEq(String ukey_0, int index, int limit, TitaVo... titaVo);

  /**
   * hold By JcicZ042Log
   * 
   * @param jcicZ042LogId key
   * @param titaVo Variable-Length Argument
   * @return JcicZ042Log JcicZ042Log
   */
  public JcicZ042Log holdById(JcicZ042LogId jcicZ042LogId, TitaVo... titaVo);

  /**
   * hold By JcicZ042Log
   * 
   * @param jcicZ042Log key
   * @param titaVo Variable-Length Argument
   * @return JcicZ042Log JcicZ042Log
   */
  public JcicZ042Log holdById(JcicZ042Log jcicZ042Log, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param jcicZ042Log Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ042Log Entity
   * @throws DBException exception
   */
  public JcicZ042Log insert(JcicZ042Log jcicZ042Log, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param jcicZ042Log Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ042Log Entity
   * @throws DBException exception
   */
  public JcicZ042Log update(JcicZ042Log jcicZ042Log, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param jcicZ042Log Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ042Log Entity
   * @throws DBException exception
   */
  public JcicZ042Log update2(JcicZ042Log jcicZ042Log, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param jcicZ042Log Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(JcicZ042Log jcicZ042Log, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param jcicZ042Log Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<JcicZ042Log> jcicZ042Log, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param jcicZ042Log Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<JcicZ042Log> jcicZ042Log, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param jcicZ042Log Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<JcicZ042Log> jcicZ042Log, TitaVo... titaVo) throws DBException;

}
