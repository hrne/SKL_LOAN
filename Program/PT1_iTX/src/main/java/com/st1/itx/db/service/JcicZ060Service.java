package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicZ060;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicZ060Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ060Service {

  /**
   * findByPrimaryKey
   *
   * @param jcicZ060Id PK
   * @param titaVo Variable-Length Argument
   * @return JcicZ060 JcicZ060
   */
  public JcicZ060 findById(JcicZ060Id jcicZ060Id, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ060 JcicZ060 of List
   */
  public Slice<JcicZ060> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * CustId=
   *
   * @param custId_0 custId_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ060 JcicZ060 of List
   */
  public Slice<JcicZ060> custIdEq(String custId_0, int index, int limit, TitaVo... titaVo);

  /**
   * RcDate=
   *
   * @param rcDate_0 rcDate_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ060 JcicZ060 of List
   */
  public Slice<JcicZ060> rcDateEq(int rcDate_0, int index, int limit, TitaVo... titaVo);

  /**
   * CustId= , AND RcDate=
   *
   * @param custId_0 custId_0
   * @param rcDate_1 rcDate_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ060 JcicZ060 of List
   */
  public Slice<JcicZ060> custRcEq(String custId_0, int rcDate_1, int index, int limit, TitaVo... titaVo);

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
   * @return Slice JcicZ060 JcicZ060 of List
   */
  public Slice<JcicZ060> otherEq(String submitKey_0, String custId_1, int rcDate_2, int changePayDate_3, int index, int limit, TitaVo... titaVo);

  /**
   * Ukey=
   *
   * @param ukey_0 ukey_0
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ060 JcicZ060 of List
   */
  public JcicZ060 ukeyFirst(String ukey_0, TitaVo... titaVo);

  /**
   * SubmitKey= , AND CustId= , AND RcDate= , AND ChangePayDate=
   *
   * @param submitKey_0 submitKey_0
   * @param custId_1 custId_1
   * @param rcDate_2 rcDate_2
   * @param changePayDate_3 changePayDate_3
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ060 JcicZ060 of List
   */
  public JcicZ060 otherFirst(String submitKey_0, String custId_1, int rcDate_2, int changePayDate_3, TitaVo... titaVo);

  /**
   * hold By JcicZ060
   * 
   * @param jcicZ060Id key
   * @param titaVo Variable-Length Argument
   * @return JcicZ060 JcicZ060
   */
  public JcicZ060 holdById(JcicZ060Id jcicZ060Id, TitaVo... titaVo);

  /**
   * hold By JcicZ060
   * 
   * @param jcicZ060 key
   * @param titaVo Variable-Length Argument
   * @return JcicZ060 JcicZ060
   */
  public JcicZ060 holdById(JcicZ060 jcicZ060, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param jcicZ060 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ060 Entity
   * @throws DBException exception
   */
  public JcicZ060 insert(JcicZ060 jcicZ060, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param jcicZ060 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ060 Entity
   * @throws DBException exception
   */
  public JcicZ060 update(JcicZ060 jcicZ060, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param jcicZ060 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ060 Entity
   * @throws DBException exception
   */
  public JcicZ060 update2(JcicZ060 jcicZ060, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param jcicZ060 Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(JcicZ060 jcicZ060, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param jcicZ060 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<JcicZ060> jcicZ060, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param jcicZ060 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<JcicZ060> jcicZ060, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param jcicZ060 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<JcicZ060> jcicZ060, TitaVo... titaVo) throws DBException;

}
