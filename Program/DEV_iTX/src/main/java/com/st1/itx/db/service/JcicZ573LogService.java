package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicZ573Log;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicZ573LogId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ573LogService {

  /**
   * findByPrimaryKey
   *
   * @param jcicZ573LogId PK
   * @param titaVo Variable-Length Argument
   * @return JcicZ573Log JcicZ573Log
   */
  public JcicZ573Log findById(JcicZ573LogId jcicZ573LogId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ573Log JcicZ573Log of List
   */
  public Slice<JcicZ573Log> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * Ukey=
   *
   * @param ukey_0 ukey_0
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ573Log JcicZ573Log of List
   */
  public JcicZ573Log ukeyFirst(String ukey_0, TitaVo... titaVo);

  /**
   * Ukey=
   *
   * @param ukey_0 ukey_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ573Log JcicZ573Log of List
   */
  public Slice<JcicZ573Log> ukeyEq(String ukey_0, int index, int limit, TitaVo... titaVo);

  /**
   * hold By JcicZ573Log
   * 
   * @param jcicZ573LogId key
   * @param titaVo Variable-Length Argument
   * @return JcicZ573Log JcicZ573Log
   */
  public JcicZ573Log holdById(JcicZ573LogId jcicZ573LogId, TitaVo... titaVo);

  /**
   * hold By JcicZ573Log
   * 
   * @param jcicZ573Log key
   * @param titaVo Variable-Length Argument
   * @return JcicZ573Log JcicZ573Log
   */
  public JcicZ573Log holdById(JcicZ573Log jcicZ573Log, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param jcicZ573Log Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ573Log Entity
   * @throws DBException exception
   */
  public JcicZ573Log insert(JcicZ573Log jcicZ573Log, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param jcicZ573Log Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ573Log Entity
   * @throws DBException exception
   */
  public JcicZ573Log update(JcicZ573Log jcicZ573Log, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param jcicZ573Log Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ573Log Entity
   * @throws DBException exception
   */
  public JcicZ573Log update2(JcicZ573Log jcicZ573Log, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param jcicZ573Log Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(JcicZ573Log jcicZ573Log, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param jcicZ573Log Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<JcicZ573Log> jcicZ573Log, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param jcicZ573Log Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<JcicZ573Log> jcicZ573Log, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param jcicZ573Log Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<JcicZ573Log> jcicZ573Log, TitaVo... titaVo) throws DBException;

}
