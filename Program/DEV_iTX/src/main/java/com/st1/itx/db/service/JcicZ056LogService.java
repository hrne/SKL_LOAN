package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicZ056Log;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicZ056LogId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ056LogService {

  /**
   * findByPrimaryKey
   *
   * @param jcicZ056LogId PK
   * @param titaVo Variable-Length Argument
   * @return JcicZ056Log JcicZ056Log
   */
  public JcicZ056Log findById(JcicZ056LogId jcicZ056LogId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ056Log JcicZ056Log of List
   */
  public Slice<JcicZ056Log> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * Ukey=
   *
   * @param ukey_0 ukey_0
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ056Log JcicZ056Log of List
   */
  public JcicZ056Log ukeyFirst(String ukey_0, TitaVo... titaVo);

  /**
   * Ukey=
   *
   * @param ukey_0 ukey_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ056Log JcicZ056Log of List
   */
  public Slice<JcicZ056Log> ukeyEq(String ukey_0, int index, int limit, TitaVo... titaVo);

  /**
   * hold By JcicZ056Log
   * 
   * @param jcicZ056LogId key
   * @param titaVo Variable-Length Argument
   * @return JcicZ056Log JcicZ056Log
   */
  public JcicZ056Log holdById(JcicZ056LogId jcicZ056LogId, TitaVo... titaVo);

  /**
   * hold By JcicZ056Log
   * 
   * @param jcicZ056Log key
   * @param titaVo Variable-Length Argument
   * @return JcicZ056Log JcicZ056Log
   */
  public JcicZ056Log holdById(JcicZ056Log jcicZ056Log, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param jcicZ056Log Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ056Log Entity
   * @throws DBException exception
   */
  public JcicZ056Log insert(JcicZ056Log jcicZ056Log, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param jcicZ056Log Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ056Log Entity
   * @throws DBException exception
   */
  public JcicZ056Log update(JcicZ056Log jcicZ056Log, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param jcicZ056Log Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ056Log Entity
   * @throws DBException exception
   */
  public JcicZ056Log update2(JcicZ056Log jcicZ056Log, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param jcicZ056Log Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(JcicZ056Log jcicZ056Log, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param jcicZ056Log Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<JcicZ056Log> jcicZ056Log, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param jcicZ056Log Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<JcicZ056Log> jcicZ056Log, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param jcicZ056Log Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<JcicZ056Log> jcicZ056Log, TitaVo... titaVo) throws DBException;

}
