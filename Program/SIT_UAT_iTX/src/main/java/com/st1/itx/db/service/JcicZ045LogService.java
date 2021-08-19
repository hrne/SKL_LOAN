package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicZ045Log;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicZ045LogId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ045LogService {

  /**
   * findByPrimaryKey
   *
   * @param jcicZ045LogId PK
   * @param titaVo Variable-Length Argument
   * @return JcicZ045Log JcicZ045Log
   */
  public JcicZ045Log findById(JcicZ045LogId jcicZ045LogId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ045Log JcicZ045Log of List
   */
  public Slice<JcicZ045Log> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * Ukey=
   *
   * @param ukey_0 ukey_0
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ045Log JcicZ045Log of List
   */
  public JcicZ045Log ukeyFirst(String ukey_0, TitaVo... titaVo);

  /**
   * Ukey=
   *
   * @param ukey_0 ukey_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ045Log JcicZ045Log of List
   */
  public Slice<JcicZ045Log> ukeyEq(String ukey_0, int index, int limit, TitaVo... titaVo);

  /**
   * hold By JcicZ045Log
   * 
   * @param jcicZ045LogId key
   * @param titaVo Variable-Length Argument
   * @return JcicZ045Log JcicZ045Log
   */
  public JcicZ045Log holdById(JcicZ045LogId jcicZ045LogId, TitaVo... titaVo);

  /**
   * hold By JcicZ045Log
   * 
   * @param jcicZ045Log key
   * @param titaVo Variable-Length Argument
   * @return JcicZ045Log JcicZ045Log
   */
  public JcicZ045Log holdById(JcicZ045Log jcicZ045Log, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param jcicZ045Log Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ045Log Entity
   * @throws DBException exception
   */
  public JcicZ045Log insert(JcicZ045Log jcicZ045Log, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param jcicZ045Log Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ045Log Entity
   * @throws DBException exception
   */
  public JcicZ045Log update(JcicZ045Log jcicZ045Log, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param jcicZ045Log Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ045Log Entity
   * @throws DBException exception
   */
  public JcicZ045Log update2(JcicZ045Log jcicZ045Log, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param jcicZ045Log Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(JcicZ045Log jcicZ045Log, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param jcicZ045Log Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<JcicZ045Log> jcicZ045Log, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param jcicZ045Log Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<JcicZ045Log> jcicZ045Log, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param jcicZ045Log Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<JcicZ045Log> jcicZ045Log, TitaVo... titaVo) throws DBException;

}
