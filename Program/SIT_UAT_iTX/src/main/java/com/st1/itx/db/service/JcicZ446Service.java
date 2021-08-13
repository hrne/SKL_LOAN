package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicZ446;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicZ446Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ446Service {

  /**
   * findByPrimaryKey
   *
   * @param jcicZ446Id PK
   * @param titaVo Variable-Length Argument
   * @return JcicZ446 JcicZ446
   */
  public JcicZ446 findById(JcicZ446Id jcicZ446Id, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ446 JcicZ446 of List
   */
  public Slice<JcicZ446> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * CustId=
   *
   * @param custId_0 custId_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ446 JcicZ446 of List
   */
  public Slice<JcicZ446> CustIdEq(String custId_0, int index, int limit, TitaVo... titaVo);

  /**
   * ApplyDate=
   *
   * @param applyDate_0 applyDate_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ446 JcicZ446 of List
   */
  public Slice<JcicZ446> RcDateEq(int applyDate_0, int index, int limit, TitaVo... titaVo);

  /**
   * CustId= , AND ApplyDate=
   *
   * @param custId_0 custId_0
   * @param applyDate_1 applyDate_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ446 JcicZ446 of List
   */
  public Slice<JcicZ446> CustRcEq(String custId_0, int applyDate_1, int index, int limit, TitaVo... titaVo);

  /**
   * hold By JcicZ446
   * 
   * @param jcicZ446Id key
   * @param titaVo Variable-Length Argument
   * @return JcicZ446 JcicZ446
   */
  public JcicZ446 holdById(JcicZ446Id jcicZ446Id, TitaVo... titaVo);

  /**
   * hold By JcicZ446
   * 
   * @param jcicZ446 key
   * @param titaVo Variable-Length Argument
   * @return JcicZ446 JcicZ446
   */
  public JcicZ446 holdById(JcicZ446 jcicZ446, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param jcicZ446 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ446 Entity
   * @throws DBException exception
   */
  public JcicZ446 insert(JcicZ446 jcicZ446, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param jcicZ446 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ446 Entity
   * @throws DBException exception
   */
  public JcicZ446 update(JcicZ446 jcicZ446, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param jcicZ446 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ446 Entity
   * @throws DBException exception
   */
  public JcicZ446 update2(JcicZ446 jcicZ446, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param jcicZ446 Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(JcicZ446 jcicZ446, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param jcicZ446 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<JcicZ446> jcicZ446, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param jcicZ446 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<JcicZ446> jcicZ446, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param jcicZ446 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<JcicZ446> jcicZ446, TitaVo... titaVo) throws DBException;

}
