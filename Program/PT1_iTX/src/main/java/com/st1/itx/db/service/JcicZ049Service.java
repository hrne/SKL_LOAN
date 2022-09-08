package com.st1.itx.db.service;

import java.util.List;

import java.math.BigDecimal;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicZ049;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicZ049Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ049Service {

  /**
   * findByPrimaryKey
   *
   * @param jcicZ049Id PK
   * @param titaVo Variable-Length Argument
   * @return JcicZ049 JcicZ049
   */
  public JcicZ049 findById(JcicZ049Id jcicZ049Id, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ049 JcicZ049 of List
   */
  public Slice<JcicZ049> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * CustId=
   *
   * @param custId_0 custId_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ049 JcicZ049 of List
   */
  public Slice<JcicZ049> custIdEq(String custId_0, int index, int limit, TitaVo... titaVo);

  /**
   * RcDate=
   *
   * @param rcDate_0 rcDate_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ049 JcicZ049 of List
   */
  public Slice<JcicZ049> rcDateEq(int rcDate_0, int index, int limit, TitaVo... titaVo);

  /**
   * CustId= , AND RcDate=
   *
   * @param custId_0 custId_0
   * @param rcDate_1 rcDate_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ049 JcicZ049 of List
   */
  public Slice<JcicZ049> custRcEq(String custId_0, int rcDate_1, int index, int limit, TitaVo... titaVo);

  /**
   * SubmitKey= , AND CustId= , AND RcDate=
   *
   * @param submitKey_0 submitKey_0
   * @param custId_1 custId_1
   * @param rcDate_2 rcDate_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ049 JcicZ049 of List
   */
  public Slice<JcicZ049> otherEq(String submitKey_0, String custId_1, int rcDate_2, int index, int limit, TitaVo... titaVo);

  /**
   * Ukey=
   *
   * @param ukey_0 ukey_0
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ049 JcicZ049 of List
   */
  public JcicZ049 ukeyFirst(String ukey_0, TitaVo... titaVo);

  /**
   * SubmitKey= , AND CustId= , AND RcDate=
   *
   * @param submitKey_0 submitKey_0
   * @param custId_1 custId_1
   * @param rcDate_2 rcDate_2
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ049 JcicZ049 of List
   */
  public JcicZ049 otherFirst(String submitKey_0, String custId_1, int rcDate_2, TitaVo... titaVo);

  /**
   * ActualFilingDate= , AND ActualFilingMark= 
   *
   * @param actualFilingDate_0 actualFilingDate_0
   * @param actualFilingMark_1 actualFilingMark_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ049 JcicZ049 of List
   */
  public Slice<JcicZ049> findkeyFilingDate(int actualFilingDate_0, String actualFilingMark_1, int index, int limit, TitaVo... titaVo);

  /**
   * hold By JcicZ049
   * 
   * @param jcicZ049Id key
   * @param titaVo Variable-Length Argument
   * @return JcicZ049 JcicZ049
   */
  public JcicZ049 holdById(JcicZ049Id jcicZ049Id, TitaVo... titaVo);

  /**
   * hold By JcicZ049
   * 
   * @param jcicZ049 key
   * @param titaVo Variable-Length Argument
   * @return JcicZ049 JcicZ049
   */
  public JcicZ049 holdById(JcicZ049 jcicZ049, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param jcicZ049 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ049 Entity
   * @throws DBException exception
   */
  public JcicZ049 insert(JcicZ049 jcicZ049, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param jcicZ049 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ049 Entity
   * @throws DBException exception
   */
  public JcicZ049 update(JcicZ049 jcicZ049, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param jcicZ049 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ049 Entity
   * @throws DBException exception
   */
  public JcicZ049 update2(JcicZ049 jcicZ049, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param jcicZ049 Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(JcicZ049 jcicZ049, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param jcicZ049 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<JcicZ049> jcicZ049, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param jcicZ049 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<JcicZ049> jcicZ049, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param jcicZ049 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<JcicZ049> jcicZ049, TitaVo... titaVo) throws DBException;

}
