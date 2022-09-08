package com.st1.itx.db.service;

import java.util.List;

import java.math.BigDecimal;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicZ062;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicZ062Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ062Service {

  /**
   * findByPrimaryKey
   *
   * @param jcicZ062Id PK
   * @param titaVo Variable-Length Argument
   * @return JcicZ062 JcicZ062
   */
  public JcicZ062 findById(JcicZ062Id jcicZ062Id, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ062 JcicZ062 of List
   */
  public Slice<JcicZ062> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * CustId=
   *
   * @param custId_0 custId_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ062 JcicZ062 of List
   */
  public Slice<JcicZ062> custIdEq(String custId_0, int index, int limit, TitaVo... titaVo);

  /**
   * RcDate=
   *
   * @param rcDate_0 rcDate_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ062 JcicZ062 of List
   */
  public Slice<JcicZ062> rcDateEq(int rcDate_0, int index, int limit, TitaVo... titaVo);

  /**
   * CustId= , AND RcDate=
   *
   * @param custId_0 custId_0
   * @param rcDate_1 rcDate_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ062 JcicZ062 of List
   */
  public Slice<JcicZ062> custRcEq(String custId_0, int rcDate_1, int index, int limit, TitaVo... titaVo);

  /**
   * SubmitKey= , AND CustId= , AND RcDate= , AND ChangePayDate= 
   *
   * @param submitKey_0 submitKey_0
   * @param custId_1 custId_1
   * @param rcDate_2 rcDate_2
   * @param changePayDate_3 changePayDate_3
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ062 JcicZ062 of List
   */
  public Slice<JcicZ062> otherEq(String submitKey_0, String custId_1, int rcDate_2, int changePayDate_3, int index, int limit, TitaVo... titaVo);

  /**
   * Ukey=
   *
   * @param ukey_0 ukey_0
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ062 JcicZ062 of List
   */
  public JcicZ062 ukeyFirst(String ukey_0, TitaVo... titaVo);

  /**
   * SubmitKey= , AND CustId= , AND RcDate= , AND ChangePayDate=
   *
   * @param submitKey_0 submitKey_0
   * @param custId_1 custId_1
   * @param rcDate_2 rcDate_2
   * @param changePayDate_3 changePayDate_3
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ062 JcicZ062 of List
   */
  public JcicZ062 otherFirst(String submitKey_0, String custId_1, int rcDate_2, int changePayDate_3, TitaVo... titaVo);

  /**
   * ActualFilingDate= , AND ActualFilingMark= 
   *
   * @param actualFilingDate_0 actualFilingDate_0
   * @param actualFilingMark_1 actualFilingMark_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ062 JcicZ062 of List
   */
  public Slice<JcicZ062> findkeyFilingDate(int actualFilingDate_0, String actualFilingMark_1, int index, int limit, TitaVo... titaVo);

  /**
   * hold By JcicZ062
   * 
   * @param jcicZ062Id key
   * @param titaVo Variable-Length Argument
   * @return JcicZ062 JcicZ062
   */
  public JcicZ062 holdById(JcicZ062Id jcicZ062Id, TitaVo... titaVo);

  /**
   * hold By JcicZ062
   * 
   * @param jcicZ062 key
   * @param titaVo Variable-Length Argument
   * @return JcicZ062 JcicZ062
   */
  public JcicZ062 holdById(JcicZ062 jcicZ062, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param jcicZ062 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ062 Entity
   * @throws DBException exception
   */
  public JcicZ062 insert(JcicZ062 jcicZ062, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param jcicZ062 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ062 Entity
   * @throws DBException exception
   */
  public JcicZ062 update(JcicZ062 jcicZ062, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param jcicZ062 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ062 Entity
   * @throws DBException exception
   */
  public JcicZ062 update2(JcicZ062 jcicZ062, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param jcicZ062 Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(JcicZ062 jcicZ062, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param jcicZ062 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<JcicZ062> jcicZ062, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param jcicZ062 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<JcicZ062> jcicZ062, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param jcicZ062 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<JcicZ062> jcicZ062, TitaVo... titaVo) throws DBException;

}
