package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicZ049Log;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicZ049LogId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ049LogService {

  /**
   * findByPrimaryKey
   *
   * @param jcicZ049LogId PK
   * @param titaVo Variable-Length Argument
   * @return JcicZ049Log JcicZ049Log
   */
  public JcicZ049Log findById(JcicZ049LogId jcicZ049LogId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ049Log JcicZ049Log of List
   */
  public Slice<JcicZ049Log> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * Ukey=
   *
   * @param ukey_0 ukey_0
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ049Log JcicZ049Log of List
   */
  public JcicZ049Log ukeyFirst(String ukey_0, TitaVo... titaVo);

  /**
   * Ukey=
   *
   * @param ukey_0 ukey_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ049Log JcicZ049Log of List
   */
  public Slice<JcicZ049Log> ukeyEq(String ukey_0, int index, int limit, TitaVo... titaVo);

  /**
   * hold By JcicZ049Log
   * 
   * @param jcicZ049LogId key
   * @param titaVo Variable-Length Argument
   * @return JcicZ049Log JcicZ049Log
   */
  public JcicZ049Log holdById(JcicZ049LogId jcicZ049LogId, TitaVo... titaVo);

  /**
   * hold By JcicZ049Log
   * 
   * @param jcicZ049Log key
   * @param titaVo Variable-Length Argument
   * @return JcicZ049Log JcicZ049Log
   */
  public JcicZ049Log holdById(JcicZ049Log jcicZ049Log, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param jcicZ049Log Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ049Log Entity
   * @throws DBException exception
   */
  public JcicZ049Log insert(JcicZ049Log jcicZ049Log, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param jcicZ049Log Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ049Log Entity
   * @throws DBException exception
   */
  public JcicZ049Log update(JcicZ049Log jcicZ049Log, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param jcicZ049Log Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ049Log Entity
   * @throws DBException exception
   */
  public JcicZ049Log update2(JcicZ049Log jcicZ049Log, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param jcicZ049Log Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(JcicZ049Log jcicZ049Log, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param jcicZ049Log Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<JcicZ049Log> jcicZ049Log, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param jcicZ049Log Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<JcicZ049Log> jcicZ049Log, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param jcicZ049Log Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<JcicZ049Log> jcicZ049Log, TitaVo... titaVo) throws DBException;

}
