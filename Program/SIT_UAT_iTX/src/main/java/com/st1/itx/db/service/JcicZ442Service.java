package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicZ442;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicZ442Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ442Service {

  /**
   * findByPrimaryKey
   *
   * @param jcicZ442Id PK
   * @param titaVo Variable-Length Argument
   * @return JcicZ442 JcicZ442
   */
  public JcicZ442 findById(JcicZ442Id jcicZ442Id, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ442 JcicZ442 of List
   */
  public Slice<JcicZ442> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * CustId=
   *
   * @param custId_0 custId_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ442 JcicZ442 of List
   */
  public Slice<JcicZ442> CustIdEq(String custId_0, int index, int limit, TitaVo... titaVo);

  /**
   * ApplyDate=
   *
   * @param applyDate_0 applyDate_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ442 JcicZ442 of List
   */
  public Slice<JcicZ442> RcDateEq(int applyDate_0, int index, int limit, TitaVo... titaVo);

  /**
   * CustId= , AND ApplyDate=
   *
   * @param custId_0 custId_0
   * @param applyDate_1 applyDate_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ442 JcicZ442 of List
   */
  public Slice<JcicZ442> CustRcEq(String custId_0, int applyDate_1, int index, int limit, TitaVo... titaVo);

  /**
   * hold By JcicZ442
   * 
   * @param jcicZ442Id key
   * @param titaVo Variable-Length Argument
   * @return JcicZ442 JcicZ442
   */
  public JcicZ442 holdById(JcicZ442Id jcicZ442Id, TitaVo... titaVo);

  /**
   * hold By JcicZ442
   * 
   * @param jcicZ442 key
   * @param titaVo Variable-Length Argument
   * @return JcicZ442 JcicZ442
   */
  public JcicZ442 holdById(JcicZ442 jcicZ442, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param jcicZ442 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ442 Entity
   * @throws DBException exception
   */
  public JcicZ442 insert(JcicZ442 jcicZ442, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param jcicZ442 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ442 Entity
   * @throws DBException exception
   */
  public JcicZ442 update(JcicZ442 jcicZ442, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param jcicZ442 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ442 Entity
   * @throws DBException exception
   */
  public JcicZ442 update2(JcicZ442 jcicZ442, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param jcicZ442 Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(JcicZ442 jcicZ442, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param jcicZ442 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<JcicZ442> jcicZ442, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param jcicZ442 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<JcicZ442> jcicZ442, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param jcicZ442 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<JcicZ442> jcicZ442, TitaVo... titaVo) throws DBException;

}
