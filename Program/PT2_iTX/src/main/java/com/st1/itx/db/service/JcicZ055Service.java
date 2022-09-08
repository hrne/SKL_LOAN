package com.st1.itx.db.service;

import java.util.List;

import java.math.BigDecimal;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicZ055;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicZ055Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ055Service {

  /**
   * findByPrimaryKey
   *
   * @param jcicZ055Id PK
   * @param titaVo Variable-Length Argument
   * @return JcicZ055 JcicZ055
   */
  public JcicZ055 findById(JcicZ055Id jcicZ055Id, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ055 JcicZ055 of List
   */
  public Slice<JcicZ055> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * CustId=
   *
   * @param custId_0 custId_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ055 JcicZ055 of List
   */
  public Slice<JcicZ055> custIdEq(String custId_0, int index, int limit, TitaVo... titaVo);

  /**
   * ClaimDate=
   *
   * @param claimDate_0 claimDate_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ055 JcicZ055 of List
   */
  public Slice<JcicZ055> claimDateEq(int claimDate_0, int index, int limit, TitaVo... titaVo);

  /**
   * CustId= , AND ClaimDate=
   *
   * @param custId_0 custId_0
   * @param claimDate_1 claimDate_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ055 JcicZ055 of List
   */
  public Slice<JcicZ055> custRcEq(String custId_0, int claimDate_1, int index, int limit, TitaVo... titaVo);

  /**
   * SubmitKey= , AND CustId= , AND ClaimDate= , AND CourtCode=
   *
   * @param submitKey_0 submitKey_0
   * @param custId_1 custId_1
   * @param claimDate_2 claimDate_2
   * @param courtCode_3 courtCode_3
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ055 JcicZ055 of List
   */
  public Slice<JcicZ055> checkCaseStatus(String submitKey_0, String custId_1, int claimDate_2, String courtCode_3, int index, int limit, TitaVo... titaVo);

  /**
   * SubmitKey= , AND CustId= , AND CaseStatus= , AND ClaimDate= , AND CourtCode=
   *
   * @param submitKey_0 submitKey_0
   * @param custId_1 custId_1
   * @param caseStatus_2 caseStatus_2
   * @param claimDate_3 claimDate_3
   * @param courtCode_4 courtCode_4
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ055 JcicZ055 of List
   */
  public Slice<JcicZ055> otherEq(String submitKey_0, String custId_1, String caseStatus_2, int claimDate_3, String courtCode_4, int index, int limit, TitaVo... titaVo);

  /**
   * Ukey=
   *
   * @param ukey_0 ukey_0
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ055 JcicZ055 of List
   */
  public JcicZ055 ukeyFirst(String ukey_0, TitaVo... titaVo);

  /**
   * SubmitKey= , AND CustId= , AND CaseStatus= , AND ClaimDate= , AND CourtCode=
   *
   * @param submitKey_0 submitKey_0
   * @param custId_1 custId_1
   * @param caseStatus_2 caseStatus_2
   * @param claimDate_3 claimDate_3
   * @param courtCode_4 courtCode_4
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ055 JcicZ055 of List
   */
  public JcicZ055 otherFirst(String submitKey_0, String custId_1, String caseStatus_2, int claimDate_3, String courtCode_4, TitaVo... titaVo);

  /**
   * ActualFilingDate= , AND ActualFilingMark= 
   *
   * @param actualFilingDate_0 actualFilingDate_0
   * @param actualFilingMark_1 actualFilingMark_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ055 JcicZ055 of List
   */
  public Slice<JcicZ055> findkeyFilingDate(int actualFilingDate_0, String actualFilingMark_1, int index, int limit, TitaVo... titaVo);

  /**
   * hold By JcicZ055
   * 
   * @param jcicZ055Id key
   * @param titaVo Variable-Length Argument
   * @return JcicZ055 JcicZ055
   */
  public JcicZ055 holdById(JcicZ055Id jcicZ055Id, TitaVo... titaVo);

  /**
   * hold By JcicZ055
   * 
   * @param jcicZ055 key
   * @param titaVo Variable-Length Argument
   * @return JcicZ055 JcicZ055
   */
  public JcicZ055 holdById(JcicZ055 jcicZ055, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param jcicZ055 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ055 Entity
   * @throws DBException exception
   */
  public JcicZ055 insert(JcicZ055 jcicZ055, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param jcicZ055 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ055 Entity
   * @throws DBException exception
   */
  public JcicZ055 update(JcicZ055 jcicZ055, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param jcicZ055 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ055 Entity
   * @throws DBException exception
   */
  public JcicZ055 update2(JcicZ055 jcicZ055, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param jcicZ055 Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(JcicZ055 jcicZ055, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param jcicZ055 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<JcicZ055> jcicZ055, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param jcicZ055 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<JcicZ055> jcicZ055, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param jcicZ055 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<JcicZ055> jcicZ055, TitaVo... titaVo) throws DBException;

}
