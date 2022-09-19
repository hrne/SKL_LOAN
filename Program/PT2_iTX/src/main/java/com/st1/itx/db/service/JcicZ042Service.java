package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicZ042;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicZ042Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ042Service {

  /**
   * findByPrimaryKey
   *
   * @param jcicZ042Id PK
   * @param titaVo Variable-Length Argument
   * @return JcicZ042 JcicZ042
   */
  public JcicZ042 findById(JcicZ042Id jcicZ042Id, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ042 JcicZ042 of List
   */
  public Slice<JcicZ042> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * CustId=
   *
   * @param custId_0 custId_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ042 JcicZ042 of List
   */
  public Slice<JcicZ042> custIdEq(String custId_0, int index, int limit, TitaVo... titaVo);

  /**
   * RcDate=
   *
   * @param rcDate_0 rcDate_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ042 JcicZ042 of List
   */
  public Slice<JcicZ042> rcDateEq(int rcDate_0, int index, int limit, TitaVo... titaVo);

  /**
   * CustId= , AND RcDate=
   *
   * @param custId_0 custId_0
   * @param rcDate_1 rcDate_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ042 JcicZ042 of List
   */
  public Slice<JcicZ042> custRcEq(String custId_0, int rcDate_1, int index, int limit, TitaVo... titaVo);

  /**
   * SubmitKey= , AND CustId= , AND RcDate= , AND MaxMainCode=
   *
   * @param submitKey_0 submitKey_0
   * @param custId_1 custId_1
   * @param rcDate_2 rcDate_2
   * @param maxMainCode_3 maxMainCode_3
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ042 JcicZ042 of List
   */
  public Slice<JcicZ042> otherEq(String submitKey_0, String custId_1, int rcDate_2, String maxMainCode_3, int index, int limit, TitaVo... titaVo);

  /**
   * Ukey=
   *
   * @param ukey_0 ukey_0
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ042 JcicZ042 of List
   */
  public JcicZ042 ukeyFirst(String ukey_0, TitaVo... titaVo);

  /**
   * SubmitKey= , AND CustId= , AND RcDate= , AND MaxMainCode=
   *
   * @param submitKey_0 submitKey_0
   * @param custId_1 custId_1
   * @param rcDate_2 rcDate_2
   * @param maxMainCode_3 maxMainCode_3
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ042 JcicZ042 of List
   */
  public JcicZ042 otherFirst(String submitKey_0, String custId_1, int rcDate_2, String maxMainCode_3, TitaVo... titaVo);

  /**
   * SubmitKey= , AND CustId= , AND RcDate= 
   *
   * @param submitKey_0 submitKey_0
   * @param custId_1 custId_1
   * @param rcDate_2 rcDate_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ042 JcicZ042 of List
   */
  public Slice<JcicZ042> custRcSubEq(String submitKey_0, String custId_1, int rcDate_2, int index, int limit, TitaVo... titaVo);

  /**
   * ActualFilingDate= , AND ActualFilingMark= 
   *
   * @param actualFilingDate_0 actualFilingDate_0
   * @param actualFilingMark_1 actualFilingMark_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ042 JcicZ042 of List
   */
  public Slice<JcicZ042> findkeyFilingDate(int actualFilingDate_0, String actualFilingMark_1, int index, int limit, TitaVo... titaVo);

  /**
   * hold By JcicZ042
   * 
   * @param jcicZ042Id key
   * @param titaVo Variable-Length Argument
   * @return JcicZ042 JcicZ042
   */
  public JcicZ042 holdById(JcicZ042Id jcicZ042Id, TitaVo... titaVo);

  /**
   * hold By JcicZ042
   * 
   * @param jcicZ042 key
   * @param titaVo Variable-Length Argument
   * @return JcicZ042 JcicZ042
   */
  public JcicZ042 holdById(JcicZ042 jcicZ042, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param jcicZ042 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ042 Entity
   * @throws DBException exception
   */
  public JcicZ042 insert(JcicZ042 jcicZ042, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param jcicZ042 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ042 Entity
   * @throws DBException exception
   */
  public JcicZ042 update(JcicZ042 jcicZ042, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param jcicZ042 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ042 Entity
   * @throws DBException exception
   */
  public JcicZ042 update2(JcicZ042 jcicZ042, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param jcicZ042 Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(JcicZ042 jcicZ042, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param jcicZ042 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<JcicZ042> jcicZ042, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param jcicZ042 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<JcicZ042> jcicZ042, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param jcicZ042 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<JcicZ042> jcicZ042, TitaVo... titaVo) throws DBException;

}
