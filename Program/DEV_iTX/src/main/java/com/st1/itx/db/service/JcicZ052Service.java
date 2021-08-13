package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicZ052;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicZ052Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ052Service {

  /**
   * findByPrimaryKey
   *
   * @param jcicZ052Id PK
   * @param titaVo Variable-Length Argument
   * @return JcicZ052 JcicZ052
   */
  public JcicZ052 findById(JcicZ052Id jcicZ052Id, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ052 JcicZ052 of List
   */
  public Slice<JcicZ052> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * CustId=
   *
   * @param custId_0 custId_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ052 JcicZ052 of List
   */
  public Slice<JcicZ052> CustIdEq(String custId_0, int index, int limit, TitaVo... titaVo);

  /**
   * RcDate=
   *
   * @param rcDate_0 rcDate_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ052 JcicZ052 of List
   */
  public Slice<JcicZ052> RcDateEq(int rcDate_0, int index, int limit, TitaVo... titaVo);

  /**
   * CustId= , AND RcDate=
   *
   * @param custId_0 custId_0
   * @param rcDate_1 rcDate_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ052 JcicZ052 of List
   */
  public Slice<JcicZ052> CustRcEq(String custId_0, int rcDate_1, int index, int limit, TitaVo... titaVo);

  /**
   * SubmitKey= , AND CustId= , AND RcDate=
   *
   * @param submitKey_0 submitKey_0
   * @param custId_1 custId_1
   * @param rcDate_2 rcDate_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ052 JcicZ052 of List
   */
  public Slice<JcicZ052> otherEq(String submitKey_0, String custId_1, int rcDate_2, int index, int limit, TitaVo... titaVo);

  /**
   * Ukey=
   *
   * @param ukey_0 ukey_0
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ052 JcicZ052 of List
   */
  public JcicZ052 ukeyFirst(String ukey_0, TitaVo... titaVo);

  /**
   * SubmitKey= , AND CustId= , AND RcDate=
   *
   * @param submitKey_0 submitKey_0
   * @param custId_1 custId_1
   * @param rcDate_2 rcDate_2
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ052 JcicZ052 of List
   */
  public JcicZ052 otherFirst(String submitKey_0, String custId_1, int rcDate_2, TitaVo... titaVo);

  /**
   * hold By JcicZ052
   * 
   * @param jcicZ052Id key
   * @param titaVo Variable-Length Argument
   * @return JcicZ052 JcicZ052
   */
  public JcicZ052 holdById(JcicZ052Id jcicZ052Id, TitaVo... titaVo);

  /**
   * hold By JcicZ052
   * 
   * @param jcicZ052 key
   * @param titaVo Variable-Length Argument
   * @return JcicZ052 JcicZ052
   */
  public JcicZ052 holdById(JcicZ052 jcicZ052, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param jcicZ052 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ052 Entity
   * @throws DBException exception
   */
  public JcicZ052 insert(JcicZ052 jcicZ052, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param jcicZ052 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ052 Entity
   * @throws DBException exception
   */
  public JcicZ052 update(JcicZ052 jcicZ052, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param jcicZ052 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ052 Entity
   * @throws DBException exception
   */
  public JcicZ052 update2(JcicZ052 jcicZ052, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param jcicZ052 Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(JcicZ052 jcicZ052, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param jcicZ052 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<JcicZ052> jcicZ052, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param jcicZ052 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<JcicZ052> jcicZ052, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param jcicZ052 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<JcicZ052> jcicZ052, TitaVo... titaVo) throws DBException;

}
