package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicZ047Log;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicZ047LogId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ047LogService {

  /**
   * findByPrimaryKey
   *
   * @param jcicZ047LogId PK
   * @param titaVo Variable-Length Argument
   * @return JcicZ047Log JcicZ047Log
   */
  public JcicZ047Log findById(JcicZ047LogId jcicZ047LogId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ047Log JcicZ047Log of List
   */
  public Slice<JcicZ047Log> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * Ukey=
   *
   * @param ukey_0 ukey_0
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ047Log JcicZ047Log of List
   */
  public JcicZ047Log ukeyFirst(String ukey_0, TitaVo... titaVo);

  /**
   * Ukey=
   *
   * @param ukey_0 ukey_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ047Log JcicZ047Log of List
   */
  public Slice<JcicZ047Log> ukeyEq(String ukey_0, int index, int limit, TitaVo... titaVo);

  /**
   * hold By JcicZ047Log
   * 
   * @param jcicZ047LogId key
   * @param titaVo Variable-Length Argument
   * @return JcicZ047Log JcicZ047Log
   */
  public JcicZ047Log holdById(JcicZ047LogId jcicZ047LogId, TitaVo... titaVo);

  /**
   * hold By JcicZ047Log
   * 
   * @param jcicZ047Log key
   * @param titaVo Variable-Length Argument
   * @return JcicZ047Log JcicZ047Log
   */
  public JcicZ047Log holdById(JcicZ047Log jcicZ047Log, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param jcicZ047Log Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ047Log Entity
   * @throws DBException exception
   */
  public JcicZ047Log insert(JcicZ047Log jcicZ047Log, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param jcicZ047Log Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ047Log Entity
   * @throws DBException exception
   */
  public JcicZ047Log update(JcicZ047Log jcicZ047Log, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param jcicZ047Log Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ047Log Entity
   * @throws DBException exception
   */
  public JcicZ047Log update2(JcicZ047Log jcicZ047Log, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param jcicZ047Log Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(JcicZ047Log jcicZ047Log, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param jcicZ047Log Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<JcicZ047Log> jcicZ047Log, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param jcicZ047Log Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<JcicZ047Log> jcicZ047Log, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param jcicZ047Log Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<JcicZ047Log> jcicZ047Log, TitaVo... titaVo) throws DBException;

}
