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
  public Slice<JcicZ061> custIdEq(String custId_0, int index, int limit, TitaVo... titaVo);

  /**
   * RcDate=
   *
   * @param rcDate_0 rcDate_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ061 JcicZ061 of List
   */
  public Slice<JcicZ061> rcDateEq(int rcDate_0, int index, int limit, TitaVo... titaVo);

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
  public Slice<JcicZ061> custRcEq(String custId_0, int rcDate_1, int index, int limit, TitaVo... titaVo);

  /**
   * SubmitKey= , AND CustId= , AND RcDate= , AND ChangePayDate= , AND MaxMainCode= 
   *
   * @param submitKey_0 submitKey_0
   * @param custId_1 custId_1
   * @param rcDate_2 rcDate_2
   * @param changePayDate_3 changePayDate_3
   * @param maxMainCode_4 maxMainCode_4
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ061 JcicZ061 of List
   */
  public Slice<JcicZ061> otherEq(String submitKey_0, String custId_1, int rcDate_2, int changePayDate_3, String maxMainCode_4, int index, int limit, TitaVo... titaVo);

  /**
   * Ukey=
   *
   * @param ukey_0 ukey_0
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ061 JcicZ061 of List
   */
  public JcicZ061 ukeyFirst(String ukey_0, TitaVo... titaVo);

  /**
   * SubmitKey= , AND CustId= , AND RcDate= , AND ChangePayDate= , AND MaxMainCode= 
   *
   * @param submitKey_0 submitKey_0
   * @param custId_1 custId_1
   * @param rcDate_2 rcDate_2
   * @param changePayDate_3 changePayDate_3
   * @param maxMainCode_4 maxMainCode_4
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ061 JcicZ061 of List
   */
  public JcicZ061 otherFirst(String submitKey_0, String custId_1, int rcDate_2, int changePayDate_3, String maxMainCode_4, TitaVo... titaVo);

  /**
   * ActualFilingDate= , AND ActualFilingMark= 
   *
   * @param actualFilingDate_0 actualFilingDate_0
   * @param actualFilingMark_1 actualFilingMark_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ061 JcicZ061 of List
   */
  public Slice<JcicZ061> findkeyFilingDate(int actualFilingDate_0, String actualFilingMark_1, int index, int limit, TitaVo... titaVo);

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
