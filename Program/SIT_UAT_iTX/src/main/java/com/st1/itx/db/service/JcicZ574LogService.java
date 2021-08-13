package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicZ574Log;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicZ574LogId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ574LogService {

  /**
   * findByPrimaryKey
   *
   * @param jcicZ574LogId PK
   * @param titaVo Variable-Length Argument
   * @return JcicZ574Log JcicZ574Log
   */
  public JcicZ574Log findById(JcicZ574LogId jcicZ574LogId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ574Log JcicZ574Log of List
   */
  public Slice<JcicZ574Log> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * Ukey=
   *
   * @param ukey_0 ukey_0
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ574Log JcicZ574Log of List
   */
  public JcicZ574Log ukeyFirst(String ukey_0, TitaVo... titaVo);

  /**
   * Ukey=
   *
   * @param ukey_0 ukey_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ574Log JcicZ574Log of List
   */
  public Slice<JcicZ574Log> ukeyEq(String ukey_0, int index, int limit, TitaVo... titaVo);

  /**
   * hold By JcicZ574Log
   * 
   * @param jcicZ574LogId key
   * @param titaVo Variable-Length Argument
   * @return JcicZ574Log JcicZ574Log
   */
  public JcicZ574Log holdById(JcicZ574LogId jcicZ574LogId, TitaVo... titaVo);

  /**
   * hold By JcicZ574Log
   * 
   * @param jcicZ574Log key
   * @param titaVo Variable-Length Argument
   * @return JcicZ574Log JcicZ574Log
   */
  public JcicZ574Log holdById(JcicZ574Log jcicZ574Log, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param jcicZ574Log Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ574Log Entity
   * @throws DBException exception
   */
  public JcicZ574Log insert(JcicZ574Log jcicZ574Log, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param jcicZ574Log Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ574Log Entity
   * @throws DBException exception
   */
  public JcicZ574Log update(JcicZ574Log jcicZ574Log, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param jcicZ574Log Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ574Log Entity
   * @throws DBException exception
   */
  public JcicZ574Log update2(JcicZ574Log jcicZ574Log, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param jcicZ574Log Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(JcicZ574Log jcicZ574Log, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param jcicZ574Log Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<JcicZ574Log> jcicZ574Log, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param jcicZ574Log Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<JcicZ574Log> jcicZ574Log, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param jcicZ574Log Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<JcicZ574Log> jcicZ574Log, TitaVo... titaVo) throws DBException;

}
