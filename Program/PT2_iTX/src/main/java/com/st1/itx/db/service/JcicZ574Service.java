package com.st1.itx.db.service;

import java.util.List;

import java.math.BigDecimal;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicZ574;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicZ574Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ574Service {

  /**
   * findByPrimaryKey
   *
   * @param jcicZ574Id PK
   * @param titaVo Variable-Length Argument
   * @return JcicZ574 JcicZ574
   */
  public JcicZ574 findById(JcicZ574Id jcicZ574Id, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ574 JcicZ574 of List
   */
  public Slice<JcicZ574> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * CustId=
   *
   * @param custId_0 custId_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ574 JcicZ574 of List
   */
  public Slice<JcicZ574> custIdEq(String custId_0, int index, int limit, TitaVo... titaVo);

  /**
   * ApplyDate=
   *
   * @param applyDate_0 applyDate_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ574 JcicZ574 of List
   */
  public Slice<JcicZ574> rcDateEq(int applyDate_0, int index, int limit, TitaVo... titaVo);

  /**
   * CustId= , AND ApplyDate=
   *
   * @param custId_0 custId_0
   * @param applyDate_1 applyDate_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ574 JcicZ574 of List
   */
  public Slice<JcicZ574> custRcEq(String custId_0, int applyDate_1, int index, int limit, TitaVo... titaVo);

  /**
   * Ukey=
   *
   * @param ukey_0 ukey_0
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ574 JcicZ574 of List
   */
  public JcicZ574 ukeyFirst(String ukey_0, TitaVo... titaVo);

  /**
   * CustId=, AND ApplyDate = ,AND SubmitKey = 
   *
   * @param custId_0 custId_0
   * @param applyDate_1 applyDate_1
   * @param submitKey_2 submitKey_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ574 JcicZ574 of List
   */
  public Slice<JcicZ574> otherEq(String custId_0, int applyDate_1, String submitKey_2, int index, int limit, TitaVo... titaVo);

  /**
   * CustId=, AND ApplyDate = ,AND SubmitKey = 
   *
   * @param custId_0 custId_0
   * @param applyDate_1 applyDate_1
   * @param submitKey_2 submitKey_2
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ574 JcicZ574 of List
   */
  public JcicZ574 otherFirst(String custId_0, int applyDate_1, String submitKey_2, TitaVo... titaVo);

  /**
   * hold By JcicZ574
   * 
   * @param jcicZ574Id key
   * @param titaVo Variable-Length Argument
   * @return JcicZ574 JcicZ574
   */
  public JcicZ574 holdById(JcicZ574Id jcicZ574Id, TitaVo... titaVo);

  /**
   * hold By JcicZ574
   * 
   * @param jcicZ574 key
   * @param titaVo Variable-Length Argument
   * @return JcicZ574 JcicZ574
   */
  public JcicZ574 holdById(JcicZ574 jcicZ574, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param jcicZ574 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ574 Entity
   * @throws DBException exception
   */
  public JcicZ574 insert(JcicZ574 jcicZ574, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param jcicZ574 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ574 Entity
   * @throws DBException exception
   */
  public JcicZ574 update(JcicZ574 jcicZ574, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param jcicZ574 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ574 Entity
   * @throws DBException exception
   */
  public JcicZ574 update2(JcicZ574 jcicZ574, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param jcicZ574 Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(JcicZ574 jcicZ574, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param jcicZ574 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<JcicZ574> jcicZ574, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param jcicZ574 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<JcicZ574> jcicZ574, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param jcicZ574 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<JcicZ574> jcicZ574, TitaVo... titaVo) throws DBException;

}
