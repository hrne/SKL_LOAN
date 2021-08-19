package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicZ451Log;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicZ451LogId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ451LogService {

  /**
   * findByPrimaryKey
   *
   * @param jcicZ451LogId PK
   * @param titaVo Variable-Length Argument
   * @return JcicZ451Log JcicZ451Log
   */
  public JcicZ451Log findById(JcicZ451LogId jcicZ451LogId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ451Log JcicZ451Log of List
   */
  public Slice<JcicZ451Log> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * Ukey=
   *
   * @param ukey_0 ukey_0
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ451Log JcicZ451Log of List
   */
  public JcicZ451Log ukeyFirst(String ukey_0, TitaVo... titaVo);

  /**
   * Ukey=
   *
   * @param ukey_0 ukey_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ451Log JcicZ451Log of List
   */
  public Slice<JcicZ451Log> ukeyEq(String ukey_0, int index, int limit, TitaVo... titaVo);

  /**
   * hold By JcicZ451Log
   * 
   * @param jcicZ451LogId key
   * @param titaVo Variable-Length Argument
   * @return JcicZ451Log JcicZ451Log
   */
  public JcicZ451Log holdById(JcicZ451LogId jcicZ451LogId, TitaVo... titaVo);

  /**
   * hold By JcicZ451Log
   * 
   * @param jcicZ451Log key
   * @param titaVo Variable-Length Argument
   * @return JcicZ451Log JcicZ451Log
   */
  public JcicZ451Log holdById(JcicZ451Log jcicZ451Log, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param jcicZ451Log Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ451Log Entity
   * @throws DBException exception
   */
  public JcicZ451Log insert(JcicZ451Log jcicZ451Log, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param jcicZ451Log Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ451Log Entity
   * @throws DBException exception
   */
  public JcicZ451Log update(JcicZ451Log jcicZ451Log, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param jcicZ451Log Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ451Log Entity
   * @throws DBException exception
   */
  public JcicZ451Log update2(JcicZ451Log jcicZ451Log, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param jcicZ451Log Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(JcicZ451Log jcicZ451Log, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param jcicZ451Log Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<JcicZ451Log> jcicZ451Log, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param jcicZ451Log Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<JcicZ451Log> jcicZ451Log, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param jcicZ451Log Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<JcicZ451Log> jcicZ451Log, TitaVo... titaVo) throws DBException;

}
