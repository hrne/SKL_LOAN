package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicAtomMain;
import org.springframework.data.domain.Slice;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicAtomMainService {

  /**
   * findByPrimaryKey
   *
   * @param functionCode PK
   * @param titaVo Variable-Length Argument
   * @return JcicAtomMain JcicAtomMain
   */
  public JcicAtomMain findById(String functionCode, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicAtomMain JcicAtomMain of List
   */
  public Slice<JcicAtomMain> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * hold By JcicAtomMain
   * 
   * @param functionCode key
   * @param titaVo Variable-Length Argument
   * @return JcicAtomMain JcicAtomMain
   */
  public JcicAtomMain holdById(String functionCode, TitaVo... titaVo);

  /**
   * hold By JcicAtomMain
   * 
   * @param jcicAtomMain key
   * @param titaVo Variable-Length Argument
   * @return JcicAtomMain JcicAtomMain
   */
  public JcicAtomMain holdById(JcicAtomMain jcicAtomMain, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param jcicAtomMain Entity
   * @param titaVo Variable-Length Argument
   * @return JcicAtomMain Entity
   * @throws DBException exception
   */
  public JcicAtomMain insert(JcicAtomMain jcicAtomMain, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param jcicAtomMain Entity
   * @param titaVo Variable-Length Argument
   * @return JcicAtomMain Entity
   * @throws DBException exception
   */
  public JcicAtomMain update(JcicAtomMain jcicAtomMain, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param jcicAtomMain Entity
   * @param titaVo Variable-Length Argument
   * @return JcicAtomMain Entity
   * @throws DBException exception
   */
  public JcicAtomMain update2(JcicAtomMain jcicAtomMain, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param jcicAtomMain Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(JcicAtomMain jcicAtomMain, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param jcicAtomMain Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<JcicAtomMain> jcicAtomMain, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param jcicAtomMain Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<JcicAtomMain> jcicAtomMain, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param jcicAtomMain Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<JcicAtomMain> jcicAtomMain, TitaVo... titaVo) throws DBException;

}
