package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicZ446;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicZ446Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ446Service {

  /**
   * findByPrimaryKey
   *
   * @param jcicZ446Id PK
   * @param titaVo Variable-Length Argument
   * @return JcicZ446 JcicZ446
   */
  public JcicZ446 findById(JcicZ446Id jcicZ446Id, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ446 JcicZ446 of List
   */
  public Slice<JcicZ446> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * CustId=
   *
   * @param custId_0 custId_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ446 JcicZ446 of List
   */
  public Slice<JcicZ446> custIdEq(String custId_0, int index, int limit, TitaVo... titaVo);

  /**
   * ApplyDate=
   *
   * @param applyDate_0 applyDate_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ446 JcicZ446 of List
   */
  public Slice<JcicZ446> rcDateEq(int applyDate_0, int index, int limit, TitaVo... titaVo);

  /**
   * CustId= , AND ApplyDate=
   *
   * @param custId_0 custId_0
   * @param applyDate_1 applyDate_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ446 JcicZ446 of List
   */
  public Slice<JcicZ446> custRcEq(String custId_0, int applyDate_1, int index, int limit, TitaVo... titaVo);

  /**
   * SubmitKey= , AND CustId= , AND ApplyDate= , AND CourtCode=
   *
   * @param submitKey_0 submitKey_0
   * @param custId_1 custId_1
   * @param applyDate_2 applyDate_2
   * @param courtCode_3 courtCode_3
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ446 JcicZ446 of List
   */
  public Slice<JcicZ446> otherEq(String submitKey_0, String custId_1, int applyDate_2, String courtCode_3, int index, int limit, TitaVo... titaVo);

  /**
   * Ukey=
   *
   * @param ukey_0 ukey_0
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ446 JcicZ446 of List
   */
  public JcicZ446 ukeyFirst(String ukey_0, TitaVo... titaVo);

  /**
   * SubmitKey= , AND CustId= , AND ApplyDate= , AND CourtCode= 
   *
   * @param submitKey_0 submitKey_0
   * @param custId_1 custId_1
   * @param applyDate_2 applyDate_2
   * @param courtCode_3 courtCode_3
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ446 JcicZ446 of List
   */
  public JcicZ446 otherFirst(String submitKey_0, String custId_1, int applyDate_2, String courtCode_3, TitaVo... titaVo);

  /**
   * ActualFilingDate= , AND ActualFilingMark=
   *
   * @param actualFilingDate_0 actualFilingDate_0
   * @param actualFilingMark_1 actualFilingMark_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ446 JcicZ446 of List
   */
  public Slice<JcicZ446> findkeyFilingDate(int actualFilingDate_0, String actualFilingMark_1, int index, int limit, TitaVo... titaVo);

  /**
   * hold By JcicZ446
   * 
   * @param jcicZ446Id key
   * @param titaVo Variable-Length Argument
   * @return JcicZ446 JcicZ446
   */
  public JcicZ446 holdById(JcicZ446Id jcicZ446Id, TitaVo... titaVo);

  /**
   * hold By JcicZ446
   * 
   * @param jcicZ446 key
   * @param titaVo Variable-Length Argument
   * @return JcicZ446 JcicZ446
   */
  public JcicZ446 holdById(JcicZ446 jcicZ446, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param jcicZ446 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ446 Entity
   * @throws DBException exception
   */
  public JcicZ446 insert(JcicZ446 jcicZ446, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param jcicZ446 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ446 Entity
   * @throws DBException exception
   */
  public JcicZ446 update(JcicZ446 jcicZ446, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param jcicZ446 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ446 Entity
   * @throws DBException exception
   */
  public JcicZ446 update2(JcicZ446 jcicZ446, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param jcicZ446 Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(JcicZ446 jcicZ446, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param jcicZ446 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<JcicZ446> jcicZ446, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param jcicZ446 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<JcicZ446> jcicZ446, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param jcicZ446 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<JcicZ446> jcicZ446, TitaVo... titaVo) throws DBException;

}
