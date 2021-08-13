package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicB093;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicB093Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicB093Service {

  /**
   * findByPrimaryKey
   *
   * @param jcicB093Id PK
   * @param titaVo Variable-Length Argument
   * @return JcicB093 JcicB093
   */
  public JcicB093 findById(JcicB093Id jcicB093Id, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicB093 JcicB093 of List
   */
  public Slice<JcicB093> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * hold By JcicB093
   * 
   * @param jcicB093Id key
   * @param titaVo Variable-Length Argument
   * @return JcicB093 JcicB093
   */
  public JcicB093 holdById(JcicB093Id jcicB093Id, TitaVo... titaVo);

  /**
   * hold By JcicB093
   * 
   * @param jcicB093 key
   * @param titaVo Variable-Length Argument
   * @return JcicB093 JcicB093
   */
  public JcicB093 holdById(JcicB093 jcicB093, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param jcicB093 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicB093 Entity
   * @throws DBException exception
   */
  public JcicB093 insert(JcicB093 jcicB093, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param jcicB093 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicB093 Entity
   * @throws DBException exception
   */
  public JcicB093 update(JcicB093 jcicB093, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param jcicB093 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicB093 Entity
   * @throws DBException exception
   */
  public JcicB093 update2(JcicB093 jcicB093, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param jcicB093 Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(JcicB093 jcicB093, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param jcicB093 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<JcicB093> jcicB093, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param jcicB093 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<JcicB093> jcicB093, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param jcicB093 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<JcicB093> jcicB093, TitaVo... titaVo) throws DBException;

  /**
   * Stored Procedure<br>
   * (每月日終批次)維護 JcicB093 每月聯徵動產及貴重物品擔保品明細檔
   * @param  TBSDYF int
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_L8_JcicB093_Upd(int TBSDYF, String EmpNo, TitaVo... titaVo);

}
