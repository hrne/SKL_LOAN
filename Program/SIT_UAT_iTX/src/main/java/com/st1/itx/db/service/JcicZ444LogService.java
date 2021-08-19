package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicZ444Log;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicZ444LogId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ444LogService {

  /**
   * findByPrimaryKey
   *
   * @param jcicZ444LogId PK
   * @param titaVo Variable-Length Argument
   * @return JcicZ444Log JcicZ444Log
   */
  public JcicZ444Log findById(JcicZ444LogId jcicZ444LogId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ444Log JcicZ444Log of List
   */
  public Slice<JcicZ444Log> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * Ukey=
   *
   * @param ukey_0 ukey_0
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ444Log JcicZ444Log of List
   */
  public JcicZ444Log ukeyFirst(String ukey_0, TitaVo... titaVo);

  /**
   * Ukey=
   *
   * @param ukey_0 ukey_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ444Log JcicZ444Log of List
   */
  public Slice<JcicZ444Log> ukeyEq(String ukey_0, int index, int limit, TitaVo... titaVo);

  /**
   * hold By JcicZ444Log
   * 
   * @param jcicZ444LogId key
   * @param titaVo Variable-Length Argument
   * @return JcicZ444Log JcicZ444Log
   */
  public JcicZ444Log holdById(JcicZ444LogId jcicZ444LogId, TitaVo... titaVo);

  /**
   * hold By JcicZ444Log
   * 
   * @param jcicZ444Log key
   * @param titaVo Variable-Length Argument
   * @return JcicZ444Log JcicZ444Log
   */
  public JcicZ444Log holdById(JcicZ444Log jcicZ444Log, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param jcicZ444Log Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ444Log Entity
   * @throws DBException exception
   */
  public JcicZ444Log insert(JcicZ444Log jcicZ444Log, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param jcicZ444Log Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ444Log Entity
   * @throws DBException exception
   */
  public JcicZ444Log update(JcicZ444Log jcicZ444Log, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param jcicZ444Log Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ444Log Entity
   * @throws DBException exception
   */
  public JcicZ444Log update2(JcicZ444Log jcicZ444Log, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param jcicZ444Log Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(JcicZ444Log jcicZ444Log, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param jcicZ444Log Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<JcicZ444Log> jcicZ444Log, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param jcicZ444Log Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<JcicZ444Log> jcicZ444Log, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param jcicZ444Log Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<JcicZ444Log> jcicZ444Log, TitaVo... titaVo) throws DBException;

}
