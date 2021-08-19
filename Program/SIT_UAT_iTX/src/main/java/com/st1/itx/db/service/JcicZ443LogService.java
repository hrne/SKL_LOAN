package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicZ443Log;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicZ443LogId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ443LogService {

  /**
   * findByPrimaryKey
   *
   * @param jcicZ443LogId PK
   * @param titaVo Variable-Length Argument
   * @return JcicZ443Log JcicZ443Log
   */
  public JcicZ443Log findById(JcicZ443LogId jcicZ443LogId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ443Log JcicZ443Log of List
   */
  public Slice<JcicZ443Log> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * Ukey=
   *
   * @param ukey_0 ukey_0
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ443Log JcicZ443Log of List
   */
  public JcicZ443Log ukeyFirst(String ukey_0, TitaVo... titaVo);

  /**
   * Ukey=
   *
   * @param ukey_0 ukey_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ443Log JcicZ443Log of List
   */
  public Slice<JcicZ443Log> ukeyEq(String ukey_0, int index, int limit, TitaVo... titaVo);

  /**
   * hold By JcicZ443Log
   * 
   * @param jcicZ443LogId key
   * @param titaVo Variable-Length Argument
   * @return JcicZ443Log JcicZ443Log
   */
  public JcicZ443Log holdById(JcicZ443LogId jcicZ443LogId, TitaVo... titaVo);

  /**
   * hold By JcicZ443Log
   * 
   * @param jcicZ443Log key
   * @param titaVo Variable-Length Argument
   * @return JcicZ443Log JcicZ443Log
   */
  public JcicZ443Log holdById(JcicZ443Log jcicZ443Log, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param jcicZ443Log Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ443Log Entity
   * @throws DBException exception
   */
  public JcicZ443Log insert(JcicZ443Log jcicZ443Log, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param jcicZ443Log Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ443Log Entity
   * @throws DBException exception
   */
  public JcicZ443Log update(JcicZ443Log jcicZ443Log, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param jcicZ443Log Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ443Log Entity
   * @throws DBException exception
   */
  public JcicZ443Log update2(JcicZ443Log jcicZ443Log, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param jcicZ443Log Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(JcicZ443Log jcicZ443Log, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param jcicZ443Log Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<JcicZ443Log> jcicZ443Log, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param jcicZ443Log Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<JcicZ443Log> jcicZ443Log, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param jcicZ443Log Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<JcicZ443Log> jcicZ443Log, TitaVo... titaVo) throws DBException;

}
