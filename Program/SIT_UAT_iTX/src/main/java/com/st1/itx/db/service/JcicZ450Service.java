package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicZ450;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicZ450Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ450Service {

  /**
   * findByPrimaryKey
   *
   * @param jcicZ450Id PK
   * @param titaVo Variable-Length Argument
   * @return JcicZ450 JcicZ450
   */
  public JcicZ450 findById(JcicZ450Id jcicZ450Id, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ450 JcicZ450 of List
   */
  public Slice<JcicZ450> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * CustId=
   *
   * @param custId_0 custId_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ450 JcicZ450 of List
   */
  public Slice<JcicZ450> CustIdEq(String custId_0, int index, int limit, TitaVo... titaVo);

  /**
   * ApplyDate=
   *
   * @param applyDate_0 applyDate_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ450 JcicZ450 of List
   */
  public Slice<JcicZ450> RcDateEq(int applyDate_0, int index, int limit, TitaVo... titaVo);

  /**
   * CustId= , AND ApplyDate=
   *
   * @param custId_0 custId_0
   * @param applyDate_1 applyDate_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ450 JcicZ450 of List
   */
  public Slice<JcicZ450> CustRcEq(String custId_0, int applyDate_1, int index, int limit, TitaVo... titaVo);

  /**
   * hold By JcicZ450
   * 
   * @param jcicZ450Id key
   * @param titaVo Variable-Length Argument
   * @return JcicZ450 JcicZ450
   */
  public JcicZ450 holdById(JcicZ450Id jcicZ450Id, TitaVo... titaVo);

  /**
   * hold By JcicZ450
   * 
   * @param jcicZ450 key
   * @param titaVo Variable-Length Argument
   * @return JcicZ450 JcicZ450
   */
  public JcicZ450 holdById(JcicZ450 jcicZ450, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param jcicZ450 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ450 Entity
   * @throws DBException exception
   */
  public JcicZ450 insert(JcicZ450 jcicZ450, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param jcicZ450 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ450 Entity
   * @throws DBException exception
   */
  public JcicZ450 update(JcicZ450 jcicZ450, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param jcicZ450 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ450 Entity
   * @throws DBException exception
   */
  public JcicZ450 update2(JcicZ450 jcicZ450, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param jcicZ450 Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(JcicZ450 jcicZ450, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param jcicZ450 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<JcicZ450> jcicZ450, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param jcicZ450 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<JcicZ450> jcicZ450, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param jcicZ450 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<JcicZ450> jcicZ450, TitaVo... titaVo) throws DBException;

}
