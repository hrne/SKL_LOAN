package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicZ048Log;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicZ048LogId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ048LogService {

  /**
   * findByPrimaryKey
   *
   * @param jcicZ048LogId PK
   * @param titaVo Variable-Length Argument
   * @return JcicZ048Log JcicZ048Log
   */
  public JcicZ048Log findById(JcicZ048LogId jcicZ048LogId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ048Log JcicZ048Log of List
   */
  public Slice<JcicZ048Log> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * Ukey=
   *
   * @param ukey_0 ukey_0
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ048Log JcicZ048Log of List
   */
  public JcicZ048Log ukeyFirst(String ukey_0, TitaVo... titaVo);

  /**
   * Ukey=
   *
   * @param ukey_0 ukey_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ048Log JcicZ048Log of List
   */
  public Slice<JcicZ048Log> ukeyEq(String ukey_0, int index, int limit, TitaVo... titaVo);

  /**
   * hold By JcicZ048Log
   * 
   * @param jcicZ048LogId key
   * @param titaVo Variable-Length Argument
   * @return JcicZ048Log JcicZ048Log
   */
  public JcicZ048Log holdById(JcicZ048LogId jcicZ048LogId, TitaVo... titaVo);

  /**
   * hold By JcicZ048Log
   * 
   * @param jcicZ048Log key
   * @param titaVo Variable-Length Argument
   * @return JcicZ048Log JcicZ048Log
   */
  public JcicZ048Log holdById(JcicZ048Log jcicZ048Log, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param jcicZ048Log Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ048Log Entity
   * @throws DBException exception
   */
  public JcicZ048Log insert(JcicZ048Log jcicZ048Log, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param jcicZ048Log Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ048Log Entity
   * @throws DBException exception
   */
  public JcicZ048Log update(JcicZ048Log jcicZ048Log, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param jcicZ048Log Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ048Log Entity
   * @throws DBException exception
   */
  public JcicZ048Log update2(JcicZ048Log jcicZ048Log, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param jcicZ048Log Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(JcicZ048Log jcicZ048Log, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param jcicZ048Log Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<JcicZ048Log> jcicZ048Log, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param jcicZ048Log Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<JcicZ048Log> jcicZ048Log, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param jcicZ048Log Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<JcicZ048Log> jcicZ048Log, TitaVo... titaVo) throws DBException;

}
