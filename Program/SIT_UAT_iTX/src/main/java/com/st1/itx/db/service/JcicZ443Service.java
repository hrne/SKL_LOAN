package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicZ443;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicZ443Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ443Service {

  /**
   * findByPrimaryKey
   *
   * @param jcicZ443Id PK
   * @param titaVo Variable-Length Argument
   * @return JcicZ443 JcicZ443
   */
  public JcicZ443 findById(JcicZ443Id jcicZ443Id, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ443 JcicZ443 of List
   */
  public Slice<JcicZ443> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * CustId=
   *
   * @param custId_0 custId_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ443 JcicZ443 of List
   */
  public Slice<JcicZ443> CustIdEq(String custId_0, int index, int limit, TitaVo... titaVo);

  /**
   * ApplyDate=
   *
   * @param applyDate_0 applyDate_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ443 JcicZ443 of List
   */
  public Slice<JcicZ443> RcDateEq(int applyDate_0, int index, int limit, TitaVo... titaVo);

  /**
   * CustId= , AND ApplyDate=
   *
   * @param custId_0 custId_0
   * @param applyDate_1 applyDate_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ443 JcicZ443 of List
   */
  public Slice<JcicZ443> CustRcEq(String custId_0, int applyDate_1, int index, int limit, TitaVo... titaVo);

  /**
   * hold By JcicZ443
   * 
   * @param jcicZ443Id key
   * @param titaVo Variable-Length Argument
   * @return JcicZ443 JcicZ443
   */
  public JcicZ443 holdById(JcicZ443Id jcicZ443Id, TitaVo... titaVo);

  /**
   * hold By JcicZ443
   * 
   * @param jcicZ443 key
   * @param titaVo Variable-Length Argument
   * @return JcicZ443 JcicZ443
   */
  public JcicZ443 holdById(JcicZ443 jcicZ443, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param jcicZ443 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ443 Entity
   * @throws DBException exception
   */
  public JcicZ443 insert(JcicZ443 jcicZ443, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param jcicZ443 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ443 Entity
   * @throws DBException exception
   */
  public JcicZ443 update(JcicZ443 jcicZ443, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param jcicZ443 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ443 Entity
   * @throws DBException exception
   */
  public JcicZ443 update2(JcicZ443 jcicZ443, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param jcicZ443 Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(JcicZ443 jcicZ443, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param jcicZ443 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<JcicZ443> jcicZ443, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param jcicZ443 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<JcicZ443> jcicZ443, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param jcicZ443 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<JcicZ443> jcicZ443, TitaVo... titaVo) throws DBException;

}
