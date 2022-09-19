package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicZ572;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicZ572Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ572Service {

  /**
   * findByPrimaryKey
   *
   * @param jcicZ572Id PK
   * @param titaVo Variable-Length Argument
   * @return JcicZ572 JcicZ572
   */
  public JcicZ572 findById(JcicZ572Id jcicZ572Id, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ572 JcicZ572 of List
   */
  public Slice<JcicZ572> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * CustId=
   *
   * @param custId_0 custId_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ572 JcicZ572 of List
   */
  public Slice<JcicZ572> custIdEq(String custId_0, int index, int limit, TitaVo... titaVo);

  /**
   * ApplyDate=
   *
   * @param applyDate_0 applyDate_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ572 JcicZ572 of List
   */
  public Slice<JcicZ572> rcDateEq(int applyDate_0, int index, int limit, TitaVo... titaVo);

  /**
   * CustId= , AND ApplyDate=
   *
   * @param custId_0 custId_0
   * @param applyDate_1 applyDate_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ572 JcicZ572 of List
   */
  public Slice<JcicZ572> custRcEq(String custId_0, int applyDate_1, int index, int limit, TitaVo... titaVo);

  /**
   * SubmitKey= , AND CustId= , AND ApplyDate= , AND PayDate= , AND BankId=
   *
   * @param submitKey_0 submitKey_0
   * @param custId_1 custId_1
   * @param applyDate_2 applyDate_2
   * @param payDate_3 payDate_3
   * @param bankId_4 bankId_4
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ572 JcicZ572 of List
   */
  public Slice<JcicZ572> otherEq(String submitKey_0, String custId_1, int applyDate_2, int payDate_3, String bankId_4, int index, int limit, TitaVo... titaVo);

  /**
   * Ukey=
   *
   * @param ukey_0 ukey_0
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ572 JcicZ572 of List
   */
  public JcicZ572 ukeyFirst(String ukey_0, TitaVo... titaVo);

  /**
   * SubmitKey= , AND CustId= , AND ApplyDate= , AND PayDate= , AND BankId=
   *
   * @param submitKey_0 submitKey_0
   * @param custId_1 custId_1
   * @param applyDate_2 applyDate_2
   * @param payDate_3 payDate_3
   * @param bankId_4 bankId_4
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ572 JcicZ572 of List
   */
  public JcicZ572 otherFirst(String submitKey_0, String custId_1, int applyDate_2, int payDate_3, String bankId_4, TitaVo... titaVo);

  /**
   * ActualFilingDate= , AND ActualFilingMark=
   *
   * @param actualFilingDate_0 actualFilingDate_0
   * @param actualFilingMark_1 actualFilingMark_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ572 JcicZ572 of List
   */
  public Slice<JcicZ572> findkeyFilingDate(int actualFilingDate_0, String actualFilingMark_1, int index, int limit, TitaVo... titaVo);

  /**
   * hold By JcicZ572
   * 
   * @param jcicZ572Id key
   * @param titaVo Variable-Length Argument
   * @return JcicZ572 JcicZ572
   */
  public JcicZ572 holdById(JcicZ572Id jcicZ572Id, TitaVo... titaVo);

  /**
   * hold By JcicZ572
   * 
   * @param jcicZ572 key
   * @param titaVo Variable-Length Argument
   * @return JcicZ572 JcicZ572
   */
  public JcicZ572 holdById(JcicZ572 jcicZ572, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param jcicZ572 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ572 Entity
   * @throws DBException exception
   */
  public JcicZ572 insert(JcicZ572 jcicZ572, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param jcicZ572 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ572 Entity
   * @throws DBException exception
   */
  public JcicZ572 update(JcicZ572 jcicZ572, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param jcicZ572 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ572 Entity
   * @throws DBException exception
   */
  public JcicZ572 update2(JcicZ572 jcicZ572, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param jcicZ572 Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(JcicZ572 jcicZ572, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param jcicZ572 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<JcicZ572> jcicZ572, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param jcicZ572 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<JcicZ572> jcicZ572, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param jcicZ572 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<JcicZ572> jcicZ572, TitaVo... titaVo) throws DBException;

}
