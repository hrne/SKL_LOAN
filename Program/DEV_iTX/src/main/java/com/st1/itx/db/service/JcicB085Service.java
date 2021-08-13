package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicB085;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicB085Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicB085Service {

  /**
   * findByPrimaryKey
   *
   * @param jcicB085Id PK
   * @param titaVo Variable-Length Argument
   * @return JcicB085 JcicB085
   */
  public JcicB085 findById(JcicB085Id jcicB085Id, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicB085 JcicB085 of List
   */
  public Slice<JcicB085> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * hold By JcicB085
   * 
   * @param jcicB085Id key
   * @param titaVo Variable-Length Argument
   * @return JcicB085 JcicB085
   */
  public JcicB085 holdById(JcicB085Id jcicB085Id, TitaVo... titaVo);

  /**
   * hold By JcicB085
   * 
   * @param jcicB085 key
   * @param titaVo Variable-Length Argument
   * @return JcicB085 JcicB085
   */
  public JcicB085 holdById(JcicB085 jcicB085, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param jcicB085 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicB085 Entity
   * @throws DBException exception
   */
  public JcicB085 insert(JcicB085 jcicB085, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param jcicB085 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicB085 Entity
   * @throws DBException exception
   */
  public JcicB085 update(JcicB085 jcicB085, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param jcicB085 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicB085 Entity
   * @throws DBException exception
   */
  public JcicB085 update2(JcicB085 jcicB085, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param jcicB085 Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(JcicB085 jcicB085, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param jcicB085 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<JcicB085> jcicB085, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param jcicB085 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<JcicB085> jcicB085, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param jcicB085 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<JcicB085> jcicB085, TitaVo... titaVo) throws DBException;

  /**
   * Stored Procedure<br>
   * (每月日終批次)維護 JcicB085 聯徵帳號轉換資料檔
   * @param  TBSDYF int
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_L8_JcicB085_Upd(int TBSDYF, String EmpNo, TitaVo... titaVo);

}
