package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicZ060Log;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicZ060LogId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ060LogService {

  /**
   * findByPrimaryKey
   *
   * @param jcicZ060LogId PK
   * @param titaVo Variable-Length Argument
   * @return JcicZ060Log JcicZ060Log
   */
  public JcicZ060Log findById(JcicZ060LogId jcicZ060LogId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ060Log JcicZ060Log of List
   */
  public Slice<JcicZ060Log> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * Ukey=
   *
   * @param ukey_0 ukey_0
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ060Log JcicZ060Log of List
   */
  public JcicZ060Log ukeyFirst(String ukey_0, TitaVo... titaVo);

  /**
   * Ukey=
   *
   * @param ukey_0 ukey_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ060Log JcicZ060Log of List
   */
  public Slice<JcicZ060Log> ukeyEq(String ukey_0, int index, int limit, TitaVo... titaVo);

  /**
   * hold By JcicZ060Log
   * 
   * @param jcicZ060LogId key
   * @param titaVo Variable-Length Argument
   * @return JcicZ060Log JcicZ060Log
   */
  public JcicZ060Log holdById(JcicZ060LogId jcicZ060LogId, TitaVo... titaVo);

  /**
   * hold By JcicZ060Log
   * 
   * @param jcicZ060Log key
   * @param titaVo Variable-Length Argument
   * @return JcicZ060Log JcicZ060Log
   */
  public JcicZ060Log holdById(JcicZ060Log jcicZ060Log, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param jcicZ060Log Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ060Log Entity
   * @throws DBException exception
   */
  public JcicZ060Log insert(JcicZ060Log jcicZ060Log, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param jcicZ060Log Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ060Log Entity
   * @throws DBException exception
   */
  public JcicZ060Log update(JcicZ060Log jcicZ060Log, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param jcicZ060Log Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ060Log Entity
   * @throws DBException exception
   */
  public JcicZ060Log update2(JcicZ060Log jcicZ060Log, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param jcicZ060Log Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(JcicZ060Log jcicZ060Log, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param jcicZ060Log Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<JcicZ060Log> jcicZ060Log, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param jcicZ060Log Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<JcicZ060Log> jcicZ060Log, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param jcicZ060Log Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<JcicZ060Log> jcicZ060Log, TitaVo... titaVo) throws DBException;

}
