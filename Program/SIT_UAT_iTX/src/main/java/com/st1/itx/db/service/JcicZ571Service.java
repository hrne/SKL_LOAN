package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicZ571;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicZ571Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ571Service {

  /**
   * findByPrimaryKey
   *
   * @param jcicZ571Id PK
   * @param titaVo Variable-Length Argument
   * @return JcicZ571 JcicZ571
   */
  public JcicZ571 findById(JcicZ571Id jcicZ571Id, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ571 JcicZ571 of List
   */
  public Slice<JcicZ571> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * CustId=
   *
   * @param custId_0 custId_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ571 JcicZ571 of List
   */
  public Slice<JcicZ571> CustIdEq(String custId_0, int index, int limit, TitaVo... titaVo);

  /**
   * ApplyDate=
   *
   * @param applyDate_0 applyDate_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ571 JcicZ571 of List
   */
  public Slice<JcicZ571> RcDateEq(int applyDate_0, int index, int limit, TitaVo... titaVo);

  /**
   * CustId= , AND ApplyDate=
   *
   * @param custId_0 custId_0
   * @param applyDate_1 applyDate_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ571 JcicZ571 of List
   */
  public Slice<JcicZ571> CustRcEq(String custId_0, int applyDate_1, int index, int limit, TitaVo... titaVo);

  /**
   * SubmitKey= , AND CustId= , AND ApplyDate= , AND BankId=
   *
   * @param submitKey_0 submitKey_0
   * @param custId_1 custId_1
   * @param applyDate_2 applyDate_2
   * @param bankId_3 bankId_3
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ571 JcicZ571 of List
   */
  public Slice<JcicZ571> otherEq(String submitKey_0, String custId_1, int applyDate_2, String bankId_3, int index, int limit, TitaVo... titaVo);

  /**
   * Ukey=
   *
   * @param ukey_0 ukey_0
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ571 JcicZ571 of List
   */
  public JcicZ571 ukeyFirst(String ukey_0, TitaVo... titaVo);

  /**
   * SubmitKey= , AND CustId= , AND ApplyDate= , AND BankId= 
   *
   * @param submitKey_0 submitKey_0
   * @param custId_1 custId_1
   * @param applyDate_2 applyDate_2
   * @param bankId_3 bankId_3
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ571 JcicZ571 of List
   */
  public JcicZ571 otherFirst(String submitKey_0, String custId_1, int applyDate_2, String bankId_3, TitaVo... titaVo);

  /**
   * hold By JcicZ571
   * 
   * @param jcicZ571Id key
   * @param titaVo Variable-Length Argument
   * @return JcicZ571 JcicZ571
   */
  public JcicZ571 holdById(JcicZ571Id jcicZ571Id, TitaVo... titaVo);

  /**
   * hold By JcicZ571
   * 
   * @param jcicZ571 key
   * @param titaVo Variable-Length Argument
   * @return JcicZ571 JcicZ571
   */
  public JcicZ571 holdById(JcicZ571 jcicZ571, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param jcicZ571 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ571 Entity
   * @throws DBException exception
   */
  public JcicZ571 insert(JcicZ571 jcicZ571, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param jcicZ571 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ571 Entity
   * @throws DBException exception
   */
  public JcicZ571 update(JcicZ571 jcicZ571, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param jcicZ571 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ571 Entity
   * @throws DBException exception
   */
  public JcicZ571 update2(JcicZ571 jcicZ571, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param jcicZ571 Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(JcicZ571 jcicZ571, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param jcicZ571 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<JcicZ571> jcicZ571, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param jcicZ571 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<JcicZ571> jcicZ571, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param jcicZ571 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<JcicZ571> jcicZ571, TitaVo... titaVo) throws DBException;

}
