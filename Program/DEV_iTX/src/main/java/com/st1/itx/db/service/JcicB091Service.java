package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicB091;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicB091Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicB091Service {

  /**
   * findByPrimaryKey
   *
   * @param jcicB091Id PK
   * @param titaVo Variable-Length Argument
   * @return JcicB091 JcicB091
   */
  public JcicB091 findById(JcicB091Id jcicB091Id, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicB091 JcicB091 of List
   */
  public Slice<JcicB091> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * hold By JcicB091
   * 
   * @param jcicB091Id key
   * @param titaVo Variable-Length Argument
   * @return JcicB091 JcicB091
   */
  public JcicB091 holdById(JcicB091Id jcicB091Id, TitaVo... titaVo);

  /**
   * hold By JcicB091
   * 
   * @param jcicB091 key
   * @param titaVo Variable-Length Argument
   * @return JcicB091 JcicB091
   */
  public JcicB091 holdById(JcicB091 jcicB091, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param jcicB091 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicB091 Entity
   * @throws DBException exception
   */
  public JcicB091 insert(JcicB091 jcicB091, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param jcicB091 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicB091 Entity
   * @throws DBException exception
   */
  public JcicB091 update(JcicB091 jcicB091, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param jcicB091 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicB091 Entity
   * @throws DBException exception
   */
  public JcicB091 update2(JcicB091 jcicB091, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param jcicB091 Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(JcicB091 jcicB091, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param jcicB091 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<JcicB091> jcicB091, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param jcicB091 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<JcicB091> jcicB091, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param jcicB091 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<JcicB091> jcicB091, TitaVo... titaVo) throws DBException;

  /**
   * Stored Procedure<br>
   * (每月日終批次)維護 JcicB091 每月有價證券(股票除外)擔保品明細檔
   * @param  TBSDYF int
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_L8_JcicB091_Upd(int TBSDYF, String EmpNo, TitaVo... titaVo);

}
