package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicZ041;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicZ041Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ041Service {

  /**
   * findByPrimaryKey
   *
   * @param jcicZ041Id PK
   * @param titaVo Variable-Length Argument
   * @return JcicZ041 JcicZ041
   */
  public JcicZ041 findById(JcicZ041Id jcicZ041Id, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ041 JcicZ041 of List
   */
  public Slice<JcicZ041> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * CustId=
   *
   * @param custId_0 custId_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ041 JcicZ041 of List
   */
  public Slice<JcicZ041> custIdEq(String custId_0, int index, int limit, TitaVo... titaVo);

  /**
   * RcDate=
   *
   * @param rcDate_0 rcDate_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ041 JcicZ041 of List
   */
  public Slice<JcicZ041> rcDateEq(int rcDate_0, int index, int limit, TitaVo... titaVo);

  /**
   * CustId= , AND RcDate=
   *
   * @param custId_0 custId_0
   * @param rcDate_1 rcDate_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ041 JcicZ041 of List
   */
  public Slice<JcicZ041> custRcEq(String custId_0, int rcDate_1, int index, int limit, TitaVo... titaVo);

  /**
   * SubmitKey= , AND CustId= , AND RcDate=
   *
   * @param submitKey_0 submitKey_0
   * @param custId_1 custId_1
   * @param rcDate_2 rcDate_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ041 JcicZ041 of List
   */
  public Slice<JcicZ041> otherEq(String submitKey_0, String custId_1, int rcDate_2, int index, int limit, TitaVo... titaVo);

  /**
   * Ukey=
   *
   * @param ukey_0 ukey_0
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ041 JcicZ041 of List
   */
  public JcicZ041 ukeyFirst(String ukey_0, TitaVo... titaVo);

  /**
   * SubmitKey= , AND CustId= , AND RcDate=
   *
   * @param submitKey_0 submitKey_0
   * @param custId_1 custId_1
   * @param rcDate_2 rcDate_2
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ041 JcicZ041 of List
   */
  public JcicZ041 otherFirst(String submitKey_0, String custId_1, int rcDate_2, TitaVo... titaVo);

  /**
   * hold By JcicZ041
   * 
   * @param jcicZ041Id key
   * @param titaVo Variable-Length Argument
   * @return JcicZ041 JcicZ041
   */
  public JcicZ041 holdById(JcicZ041Id jcicZ041Id, TitaVo... titaVo);

  /**
   * hold By JcicZ041
   * 
   * @param jcicZ041 key
   * @param titaVo Variable-Length Argument
   * @return JcicZ041 JcicZ041
   */
  public JcicZ041 holdById(JcicZ041 jcicZ041, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param jcicZ041 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ041 Entity
   * @throws DBException exception
   */
  public JcicZ041 insert(JcicZ041 jcicZ041, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param jcicZ041 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ041 Entity
   * @throws DBException exception
   */
  public JcicZ041 update(JcicZ041 jcicZ041, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param jcicZ041 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ041 Entity
   * @throws DBException exception
   */
  public JcicZ041 update2(JcicZ041 jcicZ041, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param jcicZ041 Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(JcicZ041 jcicZ041, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param jcicZ041 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<JcicZ041> jcicZ041, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param jcicZ041 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<JcicZ041> jcicZ041, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param jcicZ041 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<JcicZ041> jcicZ041, TitaVo... titaVo) throws DBException;

}
