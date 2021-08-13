package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicB096;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicB096Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicB096Service {

  /**
   * findByPrimaryKey
   *
   * @param jcicB096Id PK
   * @param titaVo Variable-Length Argument
   * @return JcicB096 JcicB096
   */
  public JcicB096 findById(JcicB096Id jcicB096Id, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicB096 JcicB096 of List
   */
  public Slice<JcicB096> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * hold By JcicB096
   * 
   * @param jcicB096Id key
   * @param titaVo Variable-Length Argument
   * @return JcicB096 JcicB096
   */
  public JcicB096 holdById(JcicB096Id jcicB096Id, TitaVo... titaVo);

  /**
   * hold By JcicB096
   * 
   * @param jcicB096 key
   * @param titaVo Variable-Length Argument
   * @return JcicB096 JcicB096
   */
  public JcicB096 holdById(JcicB096 jcicB096, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param jcicB096 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicB096 Entity
   * @throws DBException exception
   */
  public JcicB096 insert(JcicB096 jcicB096, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param jcicB096 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicB096 Entity
   * @throws DBException exception
   */
  public JcicB096 update(JcicB096 jcicB096, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param jcicB096 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicB096 Entity
   * @throws DBException exception
   */
  public JcicB096 update2(JcicB096 jcicB096, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param jcicB096 Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(JcicB096 jcicB096, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param jcicB096 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<JcicB096> jcicB096, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param jcicB096 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<JcicB096> jcicB096, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param jcicB096 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<JcicB096> jcicB096, TitaVo... titaVo) throws DBException;

  /**
   * Stored Procedure<br>
   * (每月日終批次)維護 JcicB096 每月聯徵不動產擔保品明細-地號附加檔
   * @param  TBSDYF int
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_L8_JcicB096_Upd(int TBSDYF, String EmpNo, TitaVo... titaVo);

}
