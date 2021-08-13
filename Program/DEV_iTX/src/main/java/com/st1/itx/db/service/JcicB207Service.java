package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicB207;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicB207Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicB207Service {

  /**
   * findByPrimaryKey
   *
   * @param jcicB207Id PK
   * @param titaVo Variable-Length Argument
   * @return JcicB207 JcicB207
   */
  public JcicB207 findById(JcicB207Id jcicB207Id, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicB207 JcicB207 of List
   */
  public Slice<JcicB207> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * hold By JcicB207
   * 
   * @param jcicB207Id key
   * @param titaVo Variable-Length Argument
   * @return JcicB207 JcicB207
   */
  public JcicB207 holdById(JcicB207Id jcicB207Id, TitaVo... titaVo);

  /**
   * hold By JcicB207
   * 
   * @param jcicB207 key
   * @param titaVo Variable-Length Argument
   * @return JcicB207 JcicB207
   */
  public JcicB207 holdById(JcicB207 jcicB207, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param jcicB207 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicB207 Entity
   * @throws DBException exception
   */
  public JcicB207 insert(JcicB207 jcicB207, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param jcicB207 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicB207 Entity
   * @throws DBException exception
   */
  public JcicB207 update(JcicB207 jcicB207, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param jcicB207 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicB207 Entity
   * @throws DBException exception
   */
  public JcicB207 update2(JcicB207 jcicB207, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param jcicB207 Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(JcicB207 jcicB207, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param jcicB207 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<JcicB207> jcicB207, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param jcicB207 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<JcicB207> jcicB207, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param jcicB207 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<JcicB207> jcicB207, TitaVo... titaVo) throws DBException;

  /**
   * Stored Procedure<br>
   * (每月日終批次)維護 JcicB207 聯徵授信戶基本資料檔
   * @param  TBSDYF int
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_L8_JcicB207_Upd(int TBSDYF, String EmpNo, TitaVo... titaVo);

}
