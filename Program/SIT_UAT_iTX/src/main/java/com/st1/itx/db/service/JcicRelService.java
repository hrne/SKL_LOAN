package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicRel;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicRelId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicRelService {

  /**
   * findByPrimaryKey
   *
   * @param jcicRelId PK
   * @param titaVo Variable-Length Argument
   * @return JcicRel JcicRel
   */
  public JcicRel findById(JcicRelId jcicRelId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicRel JcicRel of List
   */
  public Slice<JcicRel> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * hold By JcicRel
   * 
   * @param jcicRelId key
   * @param titaVo Variable-Length Argument
   * @return JcicRel JcicRel
   */
  public JcicRel holdById(JcicRelId jcicRelId, TitaVo... titaVo);

  /**
   * hold By JcicRel
   * 
   * @param jcicRel key
   * @param titaVo Variable-Length Argument
   * @return JcicRel JcicRel
   */
  public JcicRel holdById(JcicRel jcicRel, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param jcicRel Entity
   * @param titaVo Variable-Length Argument
   * @return JcicRel Entity
   * @throws DBException exception
   */
  public JcicRel insert(JcicRel jcicRel, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param jcicRel Entity
   * @param titaVo Variable-Length Argument
   * @return JcicRel Entity
   * @throws DBException exception
   */
  public JcicRel update(JcicRel jcicRel, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param jcicRel Entity
   * @param titaVo Variable-Length Argument
   * @return JcicRel Entity
   * @throws DBException exception
   */
  public JcicRel update2(JcicRel jcicRel, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param jcicRel Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(JcicRel jcicRel, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param jcicRel Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<JcicRel> jcicRel, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param jcicRel Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<JcicRel> jcicRel, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param jcicRel Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<JcicRel> jcicRel, TitaVo... titaVo) throws DBException;

  /**
   * Stored Procedure<br>
   * (每月日終批次)維護 JcicRel 聯徵授信「同一關係企業及集團企業」資料報送檔
   * @param  TBSDYF int
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_L8_JcicRel_Upd(int TBSDYF, String EmpNo, TitaVo... titaVo);

}
