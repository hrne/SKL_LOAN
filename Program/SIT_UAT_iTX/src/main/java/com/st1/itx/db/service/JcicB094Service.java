package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicB094;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicB094Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicB094Service {

  /**
   * findByPrimaryKey
   *
   * @param jcicB094Id PK
   * @param titaVo Variable-Length Argument
   * @return JcicB094 JcicB094
   */
  public JcicB094 findById(JcicB094Id jcicB094Id, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicB094 JcicB094 of List
   */
  public Slice<JcicB094> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * hold By JcicB094
   * 
   * @param jcicB094Id key
   * @param titaVo Variable-Length Argument
   * @return JcicB094 JcicB094
   */
  public JcicB094 holdById(JcicB094Id jcicB094Id, TitaVo... titaVo);

  /**
   * hold By JcicB094
   * 
   * @param jcicB094 key
   * @param titaVo Variable-Length Argument
   * @return JcicB094 JcicB094
   */
  public JcicB094 holdById(JcicB094 jcicB094, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param jcicB094 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicB094 Entity
   * @throws DBException exception
   */
  public JcicB094 insert(JcicB094 jcicB094, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param jcicB094 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicB094 Entity
   * @throws DBException exception
   */
  public JcicB094 update(JcicB094 jcicB094, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param jcicB094 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicB094 Entity
   * @throws DBException exception
   */
  public JcicB094 update2(JcicB094 jcicB094, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param jcicB094 Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(JcicB094 jcicB094, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param jcicB094 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<JcicB094> jcicB094, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param jcicB094 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<JcicB094> jcicB094, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param jcicB094 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<JcicB094> jcicB094, TitaVo... titaVo) throws DBException;

  /**
   * Stored Procedure<br>
   * (每月日終批次)維護 JcicB094 每月聯徵股票擔保品明細檔
   * @param  TBSDYF int
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_L8_JcicB094_Upd(int TBSDYF, String EmpNo, TitaVo... titaVo);

}
