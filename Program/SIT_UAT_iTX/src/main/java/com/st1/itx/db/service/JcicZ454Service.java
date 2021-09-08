package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicZ454;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicZ454Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ454Service {

  /**
   * findByPrimaryKey
   *
   * @param jcicZ454Id PK
   * @param titaVo Variable-Length Argument
   * @return JcicZ454 JcicZ454
   */
  public JcicZ454 findById(JcicZ454Id jcicZ454Id, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ454 JcicZ454 of List
   */
  public Slice<JcicZ454> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * CustId=
   *
   * @param custId_0 custId_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ454 JcicZ454 of List
   */
  public Slice<JcicZ454> custIdEq(String custId_0, int index, int limit, TitaVo... titaVo);

  /**
   * ApplyDate=
   *
   * @param applyDate_0 applyDate_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ454 JcicZ454 of List
   */
  public Slice<JcicZ454> rcDateEq(int applyDate_0, int index, int limit, TitaVo... titaVo);

  /**
   * CustId= , AND ApplyDate=
   *
   * @param custId_0 custId_0
   * @param applyDate_1 applyDate_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ454 JcicZ454 of List
   */
  public Slice<JcicZ454> custRcEq(String custId_0, int applyDate_1, int index, int limit, TitaVo... titaVo);

  /**
   * SubmitKey= , AND CustId= , AND ApplyDate= , AND CourtCode= , AND MaxMainCode=
   *
   * @param submitKey_0 submitKey_0
   * @param custId_1 custId_1
   * @param applyDate_2 applyDate_2
   * @param courtCode_3 courtCode_3
   * @param maxMainCode_4 maxMainCode_4
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ454 JcicZ454 of List
   */
  public Slice<JcicZ454> otherEq(String submitKey_0, String custId_1, int applyDate_2, String courtCode_3, String maxMainCode_4, int index, int limit, TitaVo... titaVo);

  /**
   * Ukey=
   *
   * @param ukey_0 ukey_0
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ454 JcicZ454 of List
   */
  public JcicZ454 ukeyFirst(String ukey_0, TitaVo... titaVo);

  /**
   * SubmitKey= , AND CustId= , AND ApplyDate= , AND CourtCode= , AND MaxMainCode=
   *
   * @param submitKey_0 submitKey_0
   * @param custId_1 custId_1
   * @param applyDate_2 applyDate_2
   * @param courtCode_3 courtCode_3
   * @param maxMainCode_4 maxMainCode_4
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ454 JcicZ454 of List
   */
  public JcicZ454 otherFirst(String submitKey_0, String custId_1, int applyDate_2, String courtCode_3, String maxMainCode_4, TitaVo... titaVo);

  /**
   * hold By JcicZ454
   * 
   * @param jcicZ454Id key
   * @param titaVo Variable-Length Argument
   * @return JcicZ454 JcicZ454
   */
  public JcicZ454 holdById(JcicZ454Id jcicZ454Id, TitaVo... titaVo);

  /**
   * hold By JcicZ454
   * 
   * @param jcicZ454 key
   * @param titaVo Variable-Length Argument
   * @return JcicZ454 JcicZ454
   */
  public JcicZ454 holdById(JcicZ454 jcicZ454, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param jcicZ454 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ454 Entity
   * @throws DBException exception
   */
  public JcicZ454 insert(JcicZ454 jcicZ454, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param jcicZ454 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ454 Entity
   * @throws DBException exception
   */
  public JcicZ454 update(JcicZ454 jcicZ454, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param jcicZ454 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ454 Entity
   * @throws DBException exception
   */
  public JcicZ454 update2(JcicZ454 jcicZ454, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param jcicZ454 Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(JcicZ454 jcicZ454, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param jcicZ454 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<JcicZ454> jcicZ454, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param jcicZ454 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<JcicZ454> jcicZ454, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param jcicZ454 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<JcicZ454> jcicZ454, TitaVo... titaVo) throws DBException;

}
