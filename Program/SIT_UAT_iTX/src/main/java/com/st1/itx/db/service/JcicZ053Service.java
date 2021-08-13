package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicZ053;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicZ053Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ053Service {

  /**
   * findByPrimaryKey
   *
   * @param jcicZ053Id PK
   * @param titaVo Variable-Length Argument
   * @return JcicZ053 JcicZ053
   */
  public JcicZ053 findById(JcicZ053Id jcicZ053Id, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ053 JcicZ053 of List
   */
  public Slice<JcicZ053> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * CustId=
   *
   * @param custId_0 custId_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ053 JcicZ053 of List
   */
  public Slice<JcicZ053> CustIdEq(String custId_0, int index, int limit, TitaVo... titaVo);

  /**
   * RcDate=
   *
   * @param rcDate_0 rcDate_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ053 JcicZ053 of List
   */
  public Slice<JcicZ053> RcDateEq(int rcDate_0, int index, int limit, TitaVo... titaVo);

  /**
   * CustId= , AND RcDate=
   *
   * @param custId_0 custId_0
   * @param rcDate_1 rcDate_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ053 JcicZ053 of List
   */
  public Slice<JcicZ053> CustRcEq(String custId_0, int rcDate_1, int index, int limit, TitaVo... titaVo);

  /**
   * hold By JcicZ053
   * 
   * @param jcicZ053Id key
   * @param titaVo Variable-Length Argument
   * @return JcicZ053 JcicZ053
   */
  public JcicZ053 holdById(JcicZ053Id jcicZ053Id, TitaVo... titaVo);

  /**
   * hold By JcicZ053
   * 
   * @param jcicZ053 key
   * @param titaVo Variable-Length Argument
   * @return JcicZ053 JcicZ053
   */
  public JcicZ053 holdById(JcicZ053 jcicZ053, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param jcicZ053 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ053 Entity
   * @throws DBException exception
   */
  public JcicZ053 insert(JcicZ053 jcicZ053, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param jcicZ053 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ053 Entity
   * @throws DBException exception
   */
  public JcicZ053 update(JcicZ053 jcicZ053, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param jcicZ053 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ053 Entity
   * @throws DBException exception
   */
  public JcicZ053 update2(JcicZ053 jcicZ053, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param jcicZ053 Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(JcicZ053 jcicZ053, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param jcicZ053 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<JcicZ053> jcicZ053, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param jcicZ053 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<JcicZ053> jcicZ053, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param jcicZ053 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<JcicZ053> jcicZ053, TitaVo... titaVo) throws DBException;

}
