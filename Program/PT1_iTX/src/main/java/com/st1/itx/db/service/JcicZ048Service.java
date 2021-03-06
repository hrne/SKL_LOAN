package com.st1.itx.db.service;

import java.util.List;

import java.math.BigDecimal;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicZ048;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicZ048Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ048Service {

  /**
   * findByPrimaryKey
   *
   * @param jcicZ048Id PK
   * @param titaVo Variable-Length Argument
   * @return JcicZ048 JcicZ048
   */
  public JcicZ048 findById(JcicZ048Id jcicZ048Id, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ048 JcicZ048 of List
   */
  public Slice<JcicZ048> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * CustId=
   *
   * @param custId_0 custId_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ048 JcicZ048 of List
   */
  public Slice<JcicZ048> custIdEq(String custId_0, int index, int limit, TitaVo... titaVo);

  /**
   * RcDate=
   *
   * @param rcDate_0 rcDate_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ048 JcicZ048 of List
   */
  public Slice<JcicZ048> rcDateEq(int rcDate_0, int index, int limit, TitaVo... titaVo);

  /**
   * CustId= , AND RcDate=
   *
   * @param custId_0 custId_0
   * @param rcDate_1 rcDate_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ048 JcicZ048 of List
   */
  public Slice<JcicZ048> custRcEq(String custId_0, int rcDate_1, int index, int limit, TitaVo... titaVo);

  /**
   * RcDate&gt;= , AND RcDate&lt;=
   *
   * @param rcDate_0 rcDate_0
   * @param rcDate_1 rcDate_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ048 JcicZ048 of List
   */
  public Slice<JcicZ048> RcDateBetween(int rcDate_0, int rcDate_1, int index, int limit, TitaVo... titaVo);

  /**
   * CustId= , AND RcDate&gt;= , AND RcDate&lt;=
   *
   * @param custId_0 custId_0
   * @param rcDate_1 rcDate_1
   * @param rcDate_2 rcDate_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ048 JcicZ048 of List
   */
  public Slice<JcicZ048> CustIdRcBetween(String custId_0, int rcDate_1, int rcDate_2, int index, int limit, TitaVo... titaVo);

  /**
   * SubmitKey= , AND CustId= , AND RcDate=
   *
   * @param submitKey_0 submitKey_0
   * @param custId_1 custId_1
   * @param rcDate_2 rcDate_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ048 JcicZ048 of List
   */
  public Slice<JcicZ048> otherEq(String submitKey_0, String custId_1, int rcDate_2, int index, int limit, TitaVo... titaVo);

  /**
   * Ukey=
   *
   * @param ukey_0 ukey_0
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ048 JcicZ048 of List
   */
  public JcicZ048 ukeyFirst(String ukey_0, TitaVo... titaVo);

  /**
   * SubmitKey= , AND CustId= , AND RcDate=
   *
   * @param submitKey_0 submitKey_0
   * @param custId_1 custId_1
   * @param rcDate_2 rcDate_2
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ048 JcicZ048 of List
   */
  public JcicZ048 otherFirst(String submitKey_0, String custId_1, int rcDate_2, TitaVo... titaVo);

  /**
   * CustId= , AND RcDate=
   *
   * @param custId_0 custId_0
   * @param rcDate_1 rcDate_1
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ048 JcicZ048 of List
   */
  public JcicZ048 forL5075First(String custId_0, int rcDate_1, TitaVo... titaVo);

  /**
   * hold By JcicZ048
   * 
   * @param jcicZ048Id key
   * @param titaVo Variable-Length Argument
   * @return JcicZ048 JcicZ048
   */
  public JcicZ048 holdById(JcicZ048Id jcicZ048Id, TitaVo... titaVo);

  /**
   * hold By JcicZ048
   * 
   * @param jcicZ048 key
   * @param titaVo Variable-Length Argument
   * @return JcicZ048 JcicZ048
   */
  public JcicZ048 holdById(JcicZ048 jcicZ048, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param jcicZ048 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ048 Entity
   * @throws DBException exception
   */
  public JcicZ048 insert(JcicZ048 jcicZ048, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param jcicZ048 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ048 Entity
   * @throws DBException exception
   */
  public JcicZ048 update(JcicZ048 jcicZ048, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param jcicZ048 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ048 Entity
   * @throws DBException exception
   */
  public JcicZ048 update2(JcicZ048 jcicZ048, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param jcicZ048 Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(JcicZ048 jcicZ048, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param jcicZ048 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<JcicZ048> jcicZ048, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param jcicZ048 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<JcicZ048> jcicZ048, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param jcicZ048 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<JcicZ048> jcicZ048, TitaVo... titaVo) throws DBException;

}
