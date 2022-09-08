package com.st1.itx.db.service;

import java.util.List;

import java.math.BigDecimal;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicZ573;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicZ573Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ573Service {

  /**
   * findByPrimaryKey
   *
   * @param jcicZ573Id PK
   * @param titaVo Variable-Length Argument
   * @return JcicZ573 JcicZ573
   */
  public JcicZ573 findById(JcicZ573Id jcicZ573Id, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ573 JcicZ573 of List
   */
  public Slice<JcicZ573> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * CustId=
   *
   * @param custId_0 custId_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ573 JcicZ573 of List
   */
  public Slice<JcicZ573> custIdEq(String custId_0, int index, int limit, TitaVo... titaVo);

  /**
   * ApplyDate=
   *
   * @param applyDate_0 applyDate_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ573 JcicZ573 of List
   */
  public Slice<JcicZ573> rcDateEq(int applyDate_0, int index, int limit, TitaVo... titaVo);

  /**
   * CustId= , AND ApplyDate=
   *
   * @param custId_0 custId_0
   * @param applyDate_1 applyDate_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ573 JcicZ573 of List
   */
  public Slice<JcicZ573> custRcEq(String custId_0, int applyDate_1, int index, int limit, TitaVo... titaVo);

  /**
   * Ukey=
   *
   * @param ukey_0 ukey_0
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ573 JcicZ573 of List
   */
  public JcicZ573 ukeyFirst(String ukey_0, TitaVo... titaVo);

  /**
   * CustId=, AND ApplyDate = ,AND SubmitKey = ,AND PayDate = 
   *
   * @param custId_0 custId_0
   * @param applyDate_1 applyDate_1
   * @param submitKey_2 submitKey_2
   * @param payDate_3 payDate_3
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ573 JcicZ573 of List
   */
  public Slice<JcicZ573> otherEq(String custId_0, int applyDate_1, String submitKey_2, int payDate_3, int index, int limit, TitaVo... titaVo);

  /**
   * CustId=, AND ApplyDate = ,AND SubmitKey = ,AND PayDate = 
   *
   * @param custId_0 custId_0
   * @param applyDate_1 applyDate_1
   * @param submitKey_2 submitKey_2
   * @param payDate_3 payDate_3
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ573 JcicZ573 of List
   */
  public JcicZ573 otherFirst(String custId_0, int applyDate_1, String submitKey_2, int payDate_3, TitaVo... titaVo);

  /**
   * ActualFilingDate= , AND ActualFilingMark= 
   *
   * @param actualFilingDate_0 actualFilingDate_0
   * @param actualFilingMark_1 actualFilingMark_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ573 JcicZ573 of List
   */
  public Slice<JcicZ573> findkeyFilingDate(int actualFilingDate_0, String actualFilingMark_1, int index, int limit, TitaVo... titaVo);

  /**
   * hold By JcicZ573
   * 
   * @param jcicZ573Id key
   * @param titaVo Variable-Length Argument
   * @return JcicZ573 JcicZ573
   */
  public JcicZ573 holdById(JcicZ573Id jcicZ573Id, TitaVo... titaVo);

  /**
   * hold By JcicZ573
   * 
   * @param jcicZ573 key
   * @param titaVo Variable-Length Argument
   * @return JcicZ573 JcicZ573
   */
  public JcicZ573 holdById(JcicZ573 jcicZ573, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param jcicZ573 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ573 Entity
   * @throws DBException exception
   */
  public JcicZ573 insert(JcicZ573 jcicZ573, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param jcicZ573 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ573 Entity
   * @throws DBException exception
   */
  public JcicZ573 update(JcicZ573 jcicZ573, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param jcicZ573 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ573 Entity
   * @throws DBException exception
   */
  public JcicZ573 update2(JcicZ573 jcicZ573, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param jcicZ573 Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(JcicZ573 jcicZ573, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param jcicZ573 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<JcicZ573> jcicZ573, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param jcicZ573 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<JcicZ573> jcicZ573, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param jcicZ573 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<JcicZ573> jcicZ573, TitaVo... titaVo) throws DBException;

}
