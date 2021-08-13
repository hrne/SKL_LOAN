package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicZ054;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicZ054Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ054Service {

  /**
   * findByPrimaryKey
   *
   * @param jcicZ054Id PK
   * @param titaVo Variable-Length Argument
   * @return JcicZ054 JcicZ054
   */
  public JcicZ054 findById(JcicZ054Id jcicZ054Id, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ054 JcicZ054 of List
   */
  public Slice<JcicZ054> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * CustId=
   *
   * @param custId_0 custId_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ054 JcicZ054 of List
   */
  public Slice<JcicZ054> CustIdEq(String custId_0, int index, int limit, TitaVo... titaVo);

  /**
   * RcDate=
   *
   * @param rcDate_0 rcDate_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ054 JcicZ054 of List
   */
  public Slice<JcicZ054> RcDateEq(int rcDate_0, int index, int limit, TitaVo... titaVo);

  /**
   * CustId= , AND RcDate=
   *
   * @param custId_0 custId_0
   * @param rcDate_1 rcDate_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ054 JcicZ054 of List
   */
  public Slice<JcicZ054> CustRcEq(String custId_0, int rcDate_1, int index, int limit, TitaVo... titaVo);

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
   * @return Slice JcicZ054 JcicZ054 of List
   */
  public Slice<JcicZ054> otherEq(String submitKey_0, String custId_1, int rcDate_2, String maxMainCode_3, int index, int limit, TitaVo... titaVo);

  /**
   * Ukey=
   *
   * @param ukey_0 ukey_0
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ054 JcicZ054 of List
   */
  public JcicZ054 ukeyFirst(String ukey_0, TitaVo... titaVo);

  /**
   * SubmitKey= , AND CustId= , AND RcDate= , AND MaxMainCode=
   *
   * @param submitKey_0 submitKey_0
   * @param custId_1 custId_1
   * @param rcDate_2 rcDate_2
   * @param maxMainCode_3 maxMainCode_3
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ054 JcicZ054 of List
   */
  public JcicZ054 otherFirst(String submitKey_0, String custId_1, int rcDate_2, String maxMainCode_3, TitaVo... titaVo);

  /**
   * hold By JcicZ054
   * 
   * @param jcicZ054Id key
   * @param titaVo Variable-Length Argument
   * @return JcicZ054 JcicZ054
   */
  public JcicZ054 holdById(JcicZ054Id jcicZ054Id, TitaVo... titaVo);

  /**
   * hold By JcicZ054
   * 
   * @param jcicZ054 key
   * @param titaVo Variable-Length Argument
   * @return JcicZ054 JcicZ054
   */
  public JcicZ054 holdById(JcicZ054 jcicZ054, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param jcicZ054 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ054 Entity
   * @throws DBException exception
   */
  public JcicZ054 insert(JcicZ054 jcicZ054, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param jcicZ054 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ054 Entity
   * @throws DBException exception
   */
  public JcicZ054 update(JcicZ054 jcicZ054, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param jcicZ054 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ054 Entity
   * @throws DBException exception
   */
  public JcicZ054 update2(JcicZ054 jcicZ054, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param jcicZ054 Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(JcicZ054 jcicZ054, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param jcicZ054 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<JcicZ054> jcicZ054, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param jcicZ054 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<JcicZ054> jcicZ054, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param jcicZ054 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<JcicZ054> jcicZ054, TitaVo... titaVo) throws DBException;

}
