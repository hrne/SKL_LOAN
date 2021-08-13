package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicZ447;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicZ447Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ447Service {

  /**
   * findByPrimaryKey
   *
   * @param jcicZ447Id PK
   * @param titaVo Variable-Length Argument
   * @return JcicZ447 JcicZ447
   */
  public JcicZ447 findById(JcicZ447Id jcicZ447Id, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ447 JcicZ447 of List
   */
  public Slice<JcicZ447> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * CustId=
   *
   * @param custId_0 custId_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ447 JcicZ447 of List
   */
  public Slice<JcicZ447> CustIdEq(String custId_0, int index, int limit, TitaVo... titaVo);

  /**
   * ApplyDate=
   *
   * @param applyDate_0 applyDate_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ447 JcicZ447 of List
   */
  public Slice<JcicZ447> RcDateEq(int applyDate_0, int index, int limit, TitaVo... titaVo);

  /**
   * CustId= , AND ApplyDate=
   *
   * @param custId_0 custId_0
   * @param applyDate_1 applyDate_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ447 JcicZ447 of List
   */
  public Slice<JcicZ447> CustRcEq(String custId_0, int applyDate_1, int index, int limit, TitaVo... titaVo);

  /**
   * hold By JcicZ447
   * 
   * @param jcicZ447Id key
   * @param titaVo Variable-Length Argument
   * @return JcicZ447 JcicZ447
   */
  public JcicZ447 holdById(JcicZ447Id jcicZ447Id, TitaVo... titaVo);

  /**
   * hold By JcicZ447
   * 
   * @param jcicZ447 key
   * @param titaVo Variable-Length Argument
   * @return JcicZ447 JcicZ447
   */
  public JcicZ447 holdById(JcicZ447 jcicZ447, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param jcicZ447 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ447 Entity
   * @throws DBException exception
   */
  public JcicZ447 insert(JcicZ447 jcicZ447, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param jcicZ447 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ447 Entity
   * @throws DBException exception
   */
  public JcicZ447 update(JcicZ447 jcicZ447, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param jcicZ447 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ447 Entity
   * @throws DBException exception
   */
  public JcicZ447 update2(JcicZ447 jcicZ447, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param jcicZ447 Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(JcicZ447 jcicZ447, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param jcicZ447 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<JcicZ447> jcicZ447, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param jcicZ447 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<JcicZ447> jcicZ447, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param jcicZ447 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<JcicZ447> jcicZ447, TitaVo... titaVo) throws DBException;

}
