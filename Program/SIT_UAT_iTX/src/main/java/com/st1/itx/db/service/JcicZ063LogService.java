package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicZ063Log;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicZ063LogId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ063LogService {

  /**
   * findByPrimaryKey
   *
   * @param jcicZ063LogId PK
   * @param titaVo Variable-Length Argument
   * @return JcicZ063Log JcicZ063Log
   */
  public JcicZ063Log findById(JcicZ063LogId jcicZ063LogId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ063Log JcicZ063Log of List
   */
  public Slice<JcicZ063Log> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * Ukey=
   *
   * @param ukey_0 ukey_0
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ063Log JcicZ063Log of List
   */
  public JcicZ063Log ukeyFirst(String ukey_0, TitaVo... titaVo);

  /**
   * Ukey=
   *
   * @param ukey_0 ukey_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ063Log JcicZ063Log of List
   */
  public Slice<JcicZ063Log> ukeyEq(String ukey_0, int index, int limit, TitaVo... titaVo);

  /**
   * hold By JcicZ063Log
   * 
   * @param jcicZ063LogId key
   * @param titaVo Variable-Length Argument
   * @return JcicZ063Log JcicZ063Log
   */
  public JcicZ063Log holdById(JcicZ063LogId jcicZ063LogId, TitaVo... titaVo);

  /**
   * hold By JcicZ063Log
   * 
   * @param jcicZ063Log key
   * @param titaVo Variable-Length Argument
   * @return JcicZ063Log JcicZ063Log
   */
  public JcicZ063Log holdById(JcicZ063Log jcicZ063Log, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param jcicZ063Log Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ063Log Entity
   * @throws DBException exception
   */
  public JcicZ063Log insert(JcicZ063Log jcicZ063Log, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param jcicZ063Log Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ063Log Entity
   * @throws DBException exception
   */
  public JcicZ063Log update(JcicZ063Log jcicZ063Log, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param jcicZ063Log Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ063Log Entity
   * @throws DBException exception
   */
  public JcicZ063Log update2(JcicZ063Log jcicZ063Log, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param jcicZ063Log Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(JcicZ063Log jcicZ063Log, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param jcicZ063Log Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<JcicZ063Log> jcicZ063Log, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param jcicZ063Log Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<JcicZ063Log> jcicZ063Log, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param jcicZ063Log Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<JcicZ063Log> jcicZ063Log, TitaVo... titaVo) throws DBException;

}
