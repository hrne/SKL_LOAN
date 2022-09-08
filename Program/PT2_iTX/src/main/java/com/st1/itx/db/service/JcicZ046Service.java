package com.st1.itx.db.service;

import java.util.List;

import java.math.BigDecimal;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicZ046;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicZ046Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ046Service {

  /**
   * findByPrimaryKey
   *
   * @param jcicZ046Id PK
   * @param titaVo Variable-Length Argument
   * @return JcicZ046 JcicZ046
   */
  public JcicZ046 findById(JcicZ046Id jcicZ046Id, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ046 JcicZ046 of List
   */
  public Slice<JcicZ046> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * CustId=
   *
   * @param custId_0 custId_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ046 JcicZ046 of List
   */
  public Slice<JcicZ046> custIdEq(String custId_0, int index, int limit, TitaVo... titaVo);

  /**
   * RcDate=
   *
   * @param rcDate_0 rcDate_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ046 JcicZ046 of List
   */
  public Slice<JcicZ046> rcDateEq(int rcDate_0, int index, int limit, TitaVo... titaVo);

  /**
   * CustId= , AND RcDate=
   *
   * @param custId_0 custId_0
   * @param rcDate_1 rcDate_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ046 JcicZ046 of List
   */
  public Slice<JcicZ046> custRcEq(String custId_0, int rcDate_1, int index, int limit, TitaVo... titaVo);

  /**
   * CustId= , AND RcDate= , AND SubmitKey=
   *
   * @param custId_0 custId_0
   * @param rcDate_1 rcDate_1
   * @param submitKey_2 submitKey_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ046 JcicZ046 of List
   */
  public Slice<JcicZ046> hadZ046(String custId_0, int rcDate_1, String submitKey_2, int index, int limit, TitaVo... titaVo);

  /**
   * SubmitKey= , AND CustId= , AND RcDate= , AND CloseDate=
   *
   * @param submitKey_0 submitKey_0
   * @param custId_1 custId_1
   * @param rcDate_2 rcDate_2
   * @param closeDate_3 closeDate_3
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ046 JcicZ046 of List
   */
  public Slice<JcicZ046> otherEq(String submitKey_0, String custId_1, int rcDate_2, int closeDate_3, int index, int limit, TitaVo... titaVo);

  /**
   * Ukey=
   *
   * @param ukey_0 ukey_0
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ046 JcicZ046 of List
   */
  public JcicZ046 ukeyFirst(String ukey_0, TitaVo... titaVo);

  /**
   * SubmitKey= , AND CustId= , AND RcDate= , AND CloseDate=
   *
   * @param submitKey_0 submitKey_0
   * @param custId_1 custId_1
   * @param rcDate_2 rcDate_2
   * @param closeDate_3 closeDate_3
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ046 JcicZ046 of List
   */
  public JcicZ046 otherFirst(String submitKey_0, String custId_1, int rcDate_2, int closeDate_3, TitaVo... titaVo);

  /**
   * CustId=
   *
   * @param custId_0 custId_0
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ046 JcicZ046 of List
   */
  public JcicZ046 custIdFirst(String custId_0, TitaVo... titaVo);

  /**
   * ActualFilingDate= , AND ActualFilingMark=
   *
   * @param actualFilingDate_0 actualFilingDate_0
   * @param actualFilingMark_1 actualFilingMark_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ046 JcicZ046 of List
   */
  public Slice<JcicZ046> findkeyFilingDate(int actualFilingDate_0, String actualFilingMark_1, int index, int limit, TitaVo... titaVo);

  /**
   * hold By JcicZ046
   * 
   * @param jcicZ046Id key
   * @param titaVo Variable-Length Argument
   * @return JcicZ046 JcicZ046
   */
  public JcicZ046 holdById(JcicZ046Id jcicZ046Id, TitaVo... titaVo);

  /**
   * hold By JcicZ046
   * 
   * @param jcicZ046 key
   * @param titaVo Variable-Length Argument
   * @return JcicZ046 JcicZ046
   */
  public JcicZ046 holdById(JcicZ046 jcicZ046, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param jcicZ046 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ046 Entity
   * @throws DBException exception
   */
  public JcicZ046 insert(JcicZ046 jcicZ046, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param jcicZ046 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ046 Entity
   * @throws DBException exception
   */
  public JcicZ046 update(JcicZ046 jcicZ046, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param jcicZ046 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ046 Entity
   * @throws DBException exception
   */
  public JcicZ046 update2(JcicZ046 jcicZ046, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param jcicZ046 Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(JcicZ046 jcicZ046, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param jcicZ046 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<JcicZ046> jcicZ046, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param jcicZ046 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<JcicZ046> jcicZ046, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param jcicZ046 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<JcicZ046> jcicZ046, TitaVo... titaVo) throws DBException;

}
