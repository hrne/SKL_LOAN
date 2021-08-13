package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicZ061;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicZ061Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ061Service {

  /**
   * findByPrimaryKey
   *
   * @param jcicZ061Id PK
   * @param titaVo Variable-Length Argument
   * @return JcicZ061 JcicZ061
   */
  public JcicZ061 findById(JcicZ061Id jcicZ061Id, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ061 JcicZ061 of List
   */
  public Slice<JcicZ061> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * CustId=
   *
   * @param custId_0 custId_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ061 JcicZ061 of List
   */
  public Slice<JcicZ061> CustIdEq(String custId_0, int index, int limit, TitaVo... titaVo);

  /**
   * RcDate=
   *
   * @param rcDate_0 rcDate_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ061 JcicZ061 of List
   */
  public Slice<JcicZ061> RcDateEq(int rcDate_0, int index, int limit, TitaVo... titaVo);

  /**
   * CustId= , AND RcDate=
   *
   * @param custId_0 custId_0
   * @param rcDate_1 rcDate_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ061 JcicZ061 of List
   */
  public Slice<JcicZ061> CustRcEq(String custId_0, int rcDate_1, int index, int limit, TitaVo... titaVo);

  /**
   * hold By JcicZ061
   * 
   * @param jcicZ061Id key
   * @param titaVo Variable-Length Argument
   * @return JcicZ061 JcicZ061
   */
  public JcicZ061 holdById(JcicZ061Id jcicZ061Id, TitaVo... titaVo);

  /**
   * hold By JcicZ061
   * 
   * @param jcicZ061 key
   * @param titaVo Variable-Length Argument
   * @return JcicZ061 JcicZ061
   */
  public JcicZ061 holdById(JcicZ061 jcicZ061, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param jcicZ061 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ061 Entity
   * @throws DBException exception
   */
  public JcicZ061 insert(JcicZ061 jcicZ061, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param jcicZ061 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ061 Entity
   * @throws DBException exception
   */
  public JcicZ061 update(JcicZ061 jcicZ061, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param jcicZ061 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ061 Entity
   * @throws DBException exception
   */
  public JcicZ061 update2(JcicZ061 jcicZ061, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param jcicZ061 Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(JcicZ061 jcicZ061, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param jcicZ061 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<JcicZ061> jcicZ061, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param jcicZ061 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<JcicZ061> jcicZ061, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param jcicZ061 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<JcicZ061> jcicZ061, TitaVo... titaVo) throws DBException;

}
