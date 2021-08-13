package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicB095;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicB095Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicB095Service {

  /**
   * findByPrimaryKey
   *
   * @param jcicB095Id PK
   * @param titaVo Variable-Length Argument
   * @return JcicB095 JcicB095
   */
  public JcicB095 findById(JcicB095Id jcicB095Id, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicB095 JcicB095 of List
   */
  public Slice<JcicB095> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * hold By JcicB095
   * 
   * @param jcicB095Id key
   * @param titaVo Variable-Length Argument
   * @return JcicB095 JcicB095
   */
  public JcicB095 holdById(JcicB095Id jcicB095Id, TitaVo... titaVo);

  /**
   * hold By JcicB095
   * 
   * @param jcicB095 key
   * @param titaVo Variable-Length Argument
   * @return JcicB095 JcicB095
   */
  public JcicB095 holdById(JcicB095 jcicB095, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param jcicB095 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicB095 Entity
   * @throws DBException exception
   */
  public JcicB095 insert(JcicB095 jcicB095, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param jcicB095 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicB095 Entity
   * @throws DBException exception
   */
  public JcicB095 update(JcicB095 jcicB095, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param jcicB095 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicB095 Entity
   * @throws DBException exception
   */
  public JcicB095 update2(JcicB095 jcicB095, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param jcicB095 Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(JcicB095 jcicB095, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param jcicB095 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<JcicB095> jcicB095, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param jcicB095 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<JcicB095> jcicB095, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param jcicB095 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<JcicB095> jcicB095, TitaVo... titaVo) throws DBException;

  /**
   * Stored Procedure<br>
   * (每月日終批次)維護 JcicB095 每月聯徵不動產擔保品明細-建號附加檔
   * @param  TBSDYF int
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_L8_JcicB095_Upd(int TBSDYF, String EmpNo, TitaVo... titaVo);

}
