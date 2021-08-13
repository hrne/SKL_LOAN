package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicZ062Log;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicZ062LogId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ062LogService {

  /**
   * findByPrimaryKey
   *
   * @param jcicZ062LogId PK
   * @param titaVo Variable-Length Argument
   * @return JcicZ062Log JcicZ062Log
   */
  public JcicZ062Log findById(JcicZ062LogId jcicZ062LogId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ062Log JcicZ062Log of List
   */
  public Slice<JcicZ062Log> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * Ukey=
   *
   * @param ukey_0 ukey_0
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ062Log JcicZ062Log of List
   */
  public JcicZ062Log ukeyFirst(String ukey_0, TitaVo... titaVo);

  /**
   * Ukey=
   *
   * @param ukey_0 ukey_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ062Log JcicZ062Log of List
   */
  public Slice<JcicZ062Log> ukeyEq(String ukey_0, int index, int limit, TitaVo... titaVo);

  /**
   * hold By JcicZ062Log
   * 
   * @param jcicZ062LogId key
   * @param titaVo Variable-Length Argument
   * @return JcicZ062Log JcicZ062Log
   */
  public JcicZ062Log holdById(JcicZ062LogId jcicZ062LogId, TitaVo... titaVo);

  /**
   * hold By JcicZ062Log
   * 
   * @param jcicZ062Log key
   * @param titaVo Variable-Length Argument
   * @return JcicZ062Log JcicZ062Log
   */
  public JcicZ062Log holdById(JcicZ062Log jcicZ062Log, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param jcicZ062Log Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ062Log Entity
   * @throws DBException exception
   */
  public JcicZ062Log insert(JcicZ062Log jcicZ062Log, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param jcicZ062Log Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ062Log Entity
   * @throws DBException exception
   */
  public JcicZ062Log update(JcicZ062Log jcicZ062Log, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param jcicZ062Log Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ062Log Entity
   * @throws DBException exception
   */
  public JcicZ062Log update2(JcicZ062Log jcicZ062Log, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param jcicZ062Log Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(JcicZ062Log jcicZ062Log, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param jcicZ062Log Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<JcicZ062Log> jcicZ062Log, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param jcicZ062Log Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<JcicZ062Log> jcicZ062Log, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param jcicZ062Log Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<JcicZ062Log> jcicZ062Log, TitaVo... titaVo) throws DBException;

}
