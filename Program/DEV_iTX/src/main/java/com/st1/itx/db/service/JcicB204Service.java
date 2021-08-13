package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicB204;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicB204Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicB204Service {

  /**
   * findByPrimaryKey
   *
   * @param jcicB204Id PK
   * @param titaVo Variable-Length Argument
   * @return JcicB204 JcicB204
   */
  public JcicB204 findById(JcicB204Id jcicB204Id, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicB204 JcicB204 of List
   */
  public Slice<JcicB204> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * hold By JcicB204
   * 
   * @param jcicB204Id key
   * @param titaVo Variable-Length Argument
   * @return JcicB204 JcicB204
   */
  public JcicB204 holdById(JcicB204Id jcicB204Id, TitaVo... titaVo);

  /**
   * hold By JcicB204
   * 
   * @param jcicB204 key
   * @param titaVo Variable-Length Argument
   * @return JcicB204 JcicB204
   */
  public JcicB204 holdById(JcicB204 jcicB204, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param jcicB204 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicB204 Entity
   * @throws DBException exception
   */
  public JcicB204 insert(JcicB204 jcicB204, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param jcicB204 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicB204 Entity
   * @throws DBException exception
   */
  public JcicB204 update(JcicB204 jcicB204, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param jcicB204 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicB204 Entity
   * @throws DBException exception
   */
  public JcicB204 update2(JcicB204 jcicB204, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param jcicB204 Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(JcicB204 jcicB204, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param jcicB204 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<JcicB204> jcicB204, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param jcicB204 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<JcicB204> jcicB204, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param jcicB204 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<JcicB204> jcicB204, TitaVo... titaVo) throws DBException;

  /**
   * Stored Procedure<br>
   * (每日日終批次)維護 JcicB204 聯徵授信餘額日報檔
   * @param  TBSDYF int
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_L8_JcicB204_Upd(int TBSDYF, String EmpNo, TitaVo... titaVo);

}
