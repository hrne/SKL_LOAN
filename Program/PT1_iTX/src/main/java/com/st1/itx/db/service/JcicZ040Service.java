package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicZ040;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicZ040Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ040Service {

  /**
   * findByPrimaryKey
   *
   * @param jcicZ040Id PK
   * @param titaVo Variable-Length Argument
   * @return JcicZ040 JcicZ040
   */
  public JcicZ040 findById(JcicZ040Id jcicZ040Id, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ040 JcicZ040 of List
   */
  public Slice<JcicZ040> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * CustId=
   *
   * @param custId_0 custId_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ040 JcicZ040 of List
   */
  public Slice<JcicZ040> custIdEq(String custId_0, int index, int limit, TitaVo... titaVo);

  /**
   * RcDate=
   *
   * @param rcDate_0 rcDate_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ040 JcicZ040 of List
   */
  public Slice<JcicZ040> rcDateEq(int rcDate_0, int index, int limit, TitaVo... titaVo);

  /**
   * CustId= , AND RcDate=
   *
   * @param custId_0 custId_0
   * @param rcDate_1 rcDate_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ040 JcicZ040 of List
   */
  public Slice<JcicZ040> custRcEq(String custId_0, int rcDate_1, int index, int limit, TitaVo... titaVo);

  /**
   * SubmitKey= , AND CustId= , AND RcDate=
   *
   * @param submitKey_0 submitKey_0
   * @param custId_1 custId_1
   * @param rcDate_2 rcDate_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ040 JcicZ040 of List
   */
  public Slice<JcicZ040> otherEq(String submitKey_0, String custId_1, int rcDate_2, int index, int limit, TitaVo... titaVo);

  /**
   * Ukey=
   *
   * @param ukey_0 ukey_0
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ040 JcicZ040 of List
   */
  public JcicZ040 ukeyFirst(String ukey_0, TitaVo... titaVo);

  /**
   * SubmitKey= , AND CustId= , AND RcDate=
   *
   * @param submitKey_0 submitKey_0
   * @param custId_1 custId_1
   * @param rcDate_2 rcDate_2
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ040 JcicZ040 of List
   */
  public JcicZ040 otherFirst(String submitKey_0, String custId_1, int rcDate_2, TitaVo... titaVo);

  /**
   * hold By JcicZ040
   * 
   * @param jcicZ040Id key
   * @param titaVo Variable-Length Argument
   * @return JcicZ040 JcicZ040
   */
  public JcicZ040 holdById(JcicZ040Id jcicZ040Id, TitaVo... titaVo);

  /**
   * hold By JcicZ040
   * 
   * @param jcicZ040 key
   * @param titaVo Variable-Length Argument
   * @return JcicZ040 JcicZ040
   */
  public JcicZ040 holdById(JcicZ040 jcicZ040, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param jcicZ040 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ040 Entity
   * @throws DBException exception
   */
  public JcicZ040 insert(JcicZ040 jcicZ040, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param jcicZ040 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ040 Entity
   * @throws DBException exception
   */
  public JcicZ040 update(JcicZ040 jcicZ040, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param jcicZ040 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ040 Entity
   * @throws DBException exception
   */
  public JcicZ040 update2(JcicZ040 jcicZ040, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param jcicZ040 Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(JcicZ040 jcicZ040, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param jcicZ040 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<JcicZ040> jcicZ040, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param jcicZ040 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<JcicZ040> jcicZ040, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param jcicZ040 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<JcicZ040> jcicZ040, TitaVo... titaVo) throws DBException;

}
