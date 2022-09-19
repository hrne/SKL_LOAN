package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicZ050;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicZ050Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ050Service {

  /**
   * findByPrimaryKey
   *
   * @param jcicZ050Id PK
   * @param titaVo Variable-Length Argument
   * @return JcicZ050 JcicZ050
   */
  public JcicZ050 findById(JcicZ050Id jcicZ050Id, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ050 JcicZ050 of List
   */
  public Slice<JcicZ050> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * CustId=
   *
   * @param custId_0 custId_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ050 JcicZ050 of List
   */
  public Slice<JcicZ050> custIdEq(String custId_0, int index, int limit, TitaVo... titaVo);

  /**
   * RcDate=
   *
   * @param rcDate_0 rcDate_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ050 JcicZ050 of List
   */
  public Slice<JcicZ050> rcDateEq(int rcDate_0, int index, int limit, TitaVo... titaVo);

  /**
   * CustId= , AND RcDate=
   *
   * @param custId_0 custId_0
   * @param rcDate_1 rcDate_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ050 JcicZ050 of List
   */
  public Slice<JcicZ050> custRcEq(String custId_0, int rcDate_1, int index, int limit, TitaVo... titaVo);

  /**
   * CustId= , AND TranKey ^i
   *
   * @param custId_0 custId_0
   * @param tranKey_1 tranKey_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ050 JcicZ050 of List
   */
  public Slice<JcicZ050> RepayActualAmt(String custId_0, List<String> tranKey_1, int index, int limit, TitaVo... titaVo);

  /**
   * CustId= , AND RcDate= , AND PayDate=
   *
   * @param custId_0 custId_0
   * @param rcDate_1 rcDate_1
   * @param payDate_2 payDate_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ050 JcicZ050 of List
   */
  public Slice<JcicZ050> otherEq(String custId_0, int rcDate_1, int payDate_2, int index, int limit, TitaVo... titaVo);

  /**
   * Ukey=
   *
   * @param ukey_0 ukey_0
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ050 JcicZ050 of List
   */
  public JcicZ050 ukeyFirst(String ukey_0, TitaVo... titaVo);

  /**
   * CustId= , AND RcDate= , AND PayDate=
   *
   * @param custId_0 custId_0
   * @param rcDate_1 rcDate_1
   * @param payDate_2 payDate_2
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ050 JcicZ050 of List
   */
  public JcicZ050 otherFirst(String custId_0, int rcDate_1, int payDate_2, TitaVo... titaVo);

  /**
   * ActualFilingDate= , AND ActualFilingMark= 
   *
   * @param actualFilingDate_0 actualFilingDate_0
   * @param actualFilingMark_1 actualFilingMark_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ050 JcicZ050 of List
   */
  public Slice<JcicZ050> findkeyFilingDate(int actualFilingDate_0, String actualFilingMark_1, int index, int limit, TitaVo... titaVo);

  /**
   * hold By JcicZ050
   * 
   * @param jcicZ050Id key
   * @param titaVo Variable-Length Argument
   * @return JcicZ050 JcicZ050
   */
  public JcicZ050 holdById(JcicZ050Id jcicZ050Id, TitaVo... titaVo);

  /**
   * hold By JcicZ050
   * 
   * @param jcicZ050 key
   * @param titaVo Variable-Length Argument
   * @return JcicZ050 JcicZ050
   */
  public JcicZ050 holdById(JcicZ050 jcicZ050, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param jcicZ050 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ050 Entity
   * @throws DBException exception
   */
  public JcicZ050 insert(JcicZ050 jcicZ050, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param jcicZ050 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ050 Entity
   * @throws DBException exception
   */
  public JcicZ050 update(JcicZ050 jcicZ050, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param jcicZ050 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ050 Entity
   * @throws DBException exception
   */
  public JcicZ050 update2(JcicZ050 jcicZ050, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param jcicZ050 Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(JcicZ050 jcicZ050, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param jcicZ050 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<JcicZ050> jcicZ050, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param jcicZ050 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<JcicZ050> jcicZ050, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param jcicZ050 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<JcicZ050> jcicZ050, TitaVo... titaVo) throws DBException;

}
