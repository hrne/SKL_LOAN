package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicZ045;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicZ045Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ045Service {

  /**
   * findByPrimaryKey
   *
   * @param jcicZ045Id PK
   * @param titaVo Variable-Length Argument
   * @return JcicZ045 JcicZ045
   */
  public JcicZ045 findById(JcicZ045Id jcicZ045Id, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ045 JcicZ045 of List
   */
  public Slice<JcicZ045> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * CustId=
   *
   * @param custId_0 custId_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ045 JcicZ045 of List
   */
  public Slice<JcicZ045> custIdEq(String custId_0, int index, int limit, TitaVo... titaVo);

  /**
   * RcDate=
   *
   * @param rcDate_0 rcDate_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ045 JcicZ045 of List
   */
  public Slice<JcicZ045> rcDateEq(int rcDate_0, int index, int limit, TitaVo... titaVo);

  /**
   * CustId= , AND RcDate=
   *
   * @param custId_0 custId_0
   * @param rcDate_1 rcDate_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ045 JcicZ045 of List
   */
  public Slice<JcicZ045> custRcEq(String custId_0, int rcDate_1, int index, int limit, TitaVo... titaVo);

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
   * @return Slice JcicZ045 JcicZ045 of List
   */
  public Slice<JcicZ045> otherEq(String submitKey_0, String custId_1, int rcDate_2, String maxMainCode_3, int index, int limit, TitaVo... titaVo);

  /**
   * Ukey=
   *
   * @param ukey_0 ukey_0
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ045 JcicZ045 of List
   */
  public JcicZ045 ukeyFirst(String ukey_0, TitaVo... titaVo);

  /**
   * SubmitKey= , AND CustId= , AND RcDate= , AND MaxMainCode=
   *
   * @param submitKey_0 submitKey_0
   * @param custId_1 custId_1
   * @param rcDate_2 rcDate_2
   * @param maxMainCode_3 maxMainCode_3
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ045 JcicZ045 of List
   */
  public JcicZ045 otherFirst(String submitKey_0, String custId_1, int rcDate_2, String maxMainCode_3, TitaVo... titaVo);

  /**
   * hold By JcicZ045
   * 
   * @param jcicZ045Id key
   * @param titaVo Variable-Length Argument
   * @return JcicZ045 JcicZ045
   */
  public JcicZ045 holdById(JcicZ045Id jcicZ045Id, TitaVo... titaVo);

  /**
   * hold By JcicZ045
   * 
   * @param jcicZ045 key
   * @param titaVo Variable-Length Argument
   * @return JcicZ045 JcicZ045
   */
  public JcicZ045 holdById(JcicZ045 jcicZ045, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param jcicZ045 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ045 Entity
   * @throws DBException exception
   */
  public JcicZ045 insert(JcicZ045 jcicZ045, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param jcicZ045 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ045 Entity
   * @throws DBException exception
   */
  public JcicZ045 update(JcicZ045 jcicZ045, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param jcicZ045 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ045 Entity
   * @throws DBException exception
   */
  public JcicZ045 update2(JcicZ045 jcicZ045, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param jcicZ045 Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(JcicZ045 jcicZ045, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param jcicZ045 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<JcicZ045> jcicZ045, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param jcicZ045 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<JcicZ045> jcicZ045, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param jcicZ045 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<JcicZ045> jcicZ045, TitaVo... titaVo) throws DBException;

}
