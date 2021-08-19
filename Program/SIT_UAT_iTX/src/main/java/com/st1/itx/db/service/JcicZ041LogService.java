package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicZ041Log;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicZ041LogId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ041LogService {

  /**
   * findByPrimaryKey
   *
   * @param jcicZ041LogId PK
   * @param titaVo Variable-Length Argument
   * @return JcicZ041Log JcicZ041Log
   */
  public JcicZ041Log findById(JcicZ041LogId jcicZ041LogId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ041Log JcicZ041Log of List
   */
  public Slice<JcicZ041Log> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * Ukey=
   *
   * @param ukey_0 ukey_0
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ041Log JcicZ041Log of List
   */
  public JcicZ041Log ukeyFirst(String ukey_0, TitaVo... titaVo);

  /**
   * Ukey=
   *
   * @param ukey_0 ukey_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ041Log JcicZ041Log of List
   */
  public Slice<JcicZ041Log> ukeyEq(String ukey_0, int index, int limit, TitaVo... titaVo);

  /**
   * hold By JcicZ041Log
   * 
   * @param jcicZ041LogId key
   * @param titaVo Variable-Length Argument
   * @return JcicZ041Log JcicZ041Log
   */
  public JcicZ041Log holdById(JcicZ041LogId jcicZ041LogId, TitaVo... titaVo);

  /**
   * hold By JcicZ041Log
   * 
   * @param jcicZ041Log key
   * @param titaVo Variable-Length Argument
   * @return JcicZ041Log JcicZ041Log
   */
  public JcicZ041Log holdById(JcicZ041Log jcicZ041Log, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param jcicZ041Log Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ041Log Entity
   * @throws DBException exception
   */
  public JcicZ041Log insert(JcicZ041Log jcicZ041Log, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param jcicZ041Log Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ041Log Entity
   * @throws DBException exception
   */
  public JcicZ041Log update(JcicZ041Log jcicZ041Log, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param jcicZ041Log Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ041Log Entity
   * @throws DBException exception
   */
  public JcicZ041Log update2(JcicZ041Log jcicZ041Log, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param jcicZ041Log Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(JcicZ041Log jcicZ041Log, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param jcicZ041Log Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<JcicZ041Log> jcicZ041Log, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param jcicZ041Log Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<JcicZ041Log> jcicZ041Log, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param jcicZ041Log Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<JcicZ041Log> jcicZ041Log, TitaVo... titaVo) throws DBException;

}
