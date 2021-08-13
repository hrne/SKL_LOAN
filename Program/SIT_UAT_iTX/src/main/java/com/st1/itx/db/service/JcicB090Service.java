package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicB090;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicB090Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicB090Service {

  /**
   * findByPrimaryKey
   *
   * @param jcicB090Id PK
   * @param titaVo Variable-Length Argument
   * @return JcicB090 JcicB090
   */
  public JcicB090 findById(JcicB090Id jcicB090Id, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicB090 JcicB090 of List
   */
  public Slice<JcicB090> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * hold By JcicB090
   * 
   * @param jcicB090Id key
   * @param titaVo Variable-Length Argument
   * @return JcicB090 JcicB090
   */
  public JcicB090 holdById(JcicB090Id jcicB090Id, TitaVo... titaVo);

  /**
   * hold By JcicB090
   * 
   * @param jcicB090 key
   * @param titaVo Variable-Length Argument
   * @return JcicB090 JcicB090
   */
  public JcicB090 holdById(JcicB090 jcicB090, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param jcicB090 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicB090 Entity
   * @throws DBException exception
   */
  public JcicB090 insert(JcicB090 jcicB090, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param jcicB090 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicB090 Entity
   * @throws DBException exception
   */
  public JcicB090 update(JcicB090 jcicB090, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param jcicB090 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicB090 Entity
   * @throws DBException exception
   */
  public JcicB090 update2(JcicB090 jcicB090, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param jcicB090 Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(JcicB090 jcicB090, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param jcicB090 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<JcicB090> jcicB090, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param jcicB090 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<JcicB090> jcicB090, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param jcicB090 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<JcicB090> jcicB090, TitaVo... titaVo) throws DBException;

  /**
   * Stored Procedure<br>
   * (每月日終批次)維護 JcicB090 每月聯徵擔保品關聯檔資料檔
   * @param  TBSDYF int
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_L8_JcicB090_Upd(int TBSDYF, String EmpNo, TitaVo... titaVo);

}
