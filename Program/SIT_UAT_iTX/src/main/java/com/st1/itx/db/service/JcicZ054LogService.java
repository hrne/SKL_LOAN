package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicZ054Log;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicZ054LogId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ054LogService {

  /**
   * findByPrimaryKey
   *
   * @param jcicZ054LogId PK
   * @param titaVo Variable-Length Argument
   * @return JcicZ054Log JcicZ054Log
   */
  public JcicZ054Log findById(JcicZ054LogId jcicZ054LogId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ054Log JcicZ054Log of List
   */
  public Slice<JcicZ054Log> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * Ukey=
   *
   * @param ukey_0 ukey_0
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ054Log JcicZ054Log of List
   */
  public JcicZ054Log ukeyFirst(String ukey_0, TitaVo... titaVo);

  /**
   * Ukey=
   *
   * @param ukey_0 ukey_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ054Log JcicZ054Log of List
   */
  public Slice<JcicZ054Log> ukeyEq(String ukey_0, int index, int limit, TitaVo... titaVo);

  /**
   * hold By JcicZ054Log
   * 
   * @param jcicZ054LogId key
   * @param titaVo Variable-Length Argument
   * @return JcicZ054Log JcicZ054Log
   */
  public JcicZ054Log holdById(JcicZ054LogId jcicZ054LogId, TitaVo... titaVo);

  /**
   * hold By JcicZ054Log
   * 
   * @param jcicZ054Log key
   * @param titaVo Variable-Length Argument
   * @return JcicZ054Log JcicZ054Log
   */
  public JcicZ054Log holdById(JcicZ054Log jcicZ054Log, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param jcicZ054Log Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ054Log Entity
   * @throws DBException exception
   */
  public JcicZ054Log insert(JcicZ054Log jcicZ054Log, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param jcicZ054Log Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ054Log Entity
   * @throws DBException exception
   */
  public JcicZ054Log update(JcicZ054Log jcicZ054Log, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param jcicZ054Log Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ054Log Entity
   * @throws DBException exception
   */
  public JcicZ054Log update2(JcicZ054Log jcicZ054Log, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param jcicZ054Log Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(JcicZ054Log jcicZ054Log, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param jcicZ054Log Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<JcicZ054Log> jcicZ054Log, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param jcicZ054Log Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<JcicZ054Log> jcicZ054Log, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param jcicZ054Log Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<JcicZ054Log> jcicZ054Log, TitaVo... titaVo) throws DBException;

}
