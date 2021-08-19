package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicZ450Log;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicZ450LogId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ450LogService {

  /**
   * findByPrimaryKey
   *
   * @param jcicZ450LogId PK
   * @param titaVo Variable-Length Argument
   * @return JcicZ450Log JcicZ450Log
   */
  public JcicZ450Log findById(JcicZ450LogId jcicZ450LogId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ450Log JcicZ450Log of List
   */
  public Slice<JcicZ450Log> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * Ukey=
   *
   * @param ukey_0 ukey_0
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ450Log JcicZ450Log of List
   */
  public JcicZ450Log ukeyFirst(String ukey_0, TitaVo... titaVo);

  /**
   * Ukey=
   *
   * @param ukey_0 ukey_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ450Log JcicZ450Log of List
   */
  public Slice<JcicZ450Log> ukeyEq(String ukey_0, int index, int limit, TitaVo... titaVo);

  /**
   * hold By JcicZ450Log
   * 
   * @param jcicZ450LogId key
   * @param titaVo Variable-Length Argument
   * @return JcicZ450Log JcicZ450Log
   */
  public JcicZ450Log holdById(JcicZ450LogId jcicZ450LogId, TitaVo... titaVo);

  /**
   * hold By JcicZ450Log
   * 
   * @param jcicZ450Log key
   * @param titaVo Variable-Length Argument
   * @return JcicZ450Log JcicZ450Log
   */
  public JcicZ450Log holdById(JcicZ450Log jcicZ450Log, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param jcicZ450Log Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ450Log Entity
   * @throws DBException exception
   */
  public JcicZ450Log insert(JcicZ450Log jcicZ450Log, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param jcicZ450Log Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ450Log Entity
   * @throws DBException exception
   */
  public JcicZ450Log update(JcicZ450Log jcicZ450Log, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param jcicZ450Log Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ450Log Entity
   * @throws DBException exception
   */
  public JcicZ450Log update2(JcicZ450Log jcicZ450Log, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param jcicZ450Log Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(JcicZ450Log jcicZ450Log, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param jcicZ450Log Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<JcicZ450Log> jcicZ450Log, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param jcicZ450Log Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<JcicZ450Log> jcicZ450Log, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param jcicZ450Log Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<JcicZ450Log> jcicZ450Log, TitaVo... titaVo) throws DBException;

}
