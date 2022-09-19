package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicZ451;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicZ451Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ451Service {

  /**
   * findByPrimaryKey
   *
   * @param jcicZ451Id PK
   * @param titaVo Variable-Length Argument
   * @return JcicZ451 JcicZ451
   */
  public JcicZ451 findById(JcicZ451Id jcicZ451Id, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ451 JcicZ451 of List
   */
  public Slice<JcicZ451> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * CustId=
   *
   * @param custId_0 custId_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ451 JcicZ451 of List
   */
  public Slice<JcicZ451> custIdEq(String custId_0, int index, int limit, TitaVo... titaVo);

  /**
   * ApplyDate=
   *
   * @param applyDate_0 applyDate_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ451 JcicZ451 of List
   */
  public Slice<JcicZ451> rcDateEq(int applyDate_0, int index, int limit, TitaVo... titaVo);

  /**
   * CustId= , AND ApplyDate=
   *
   * @param custId_0 custId_0
   * @param applyDate_1 applyDate_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ451 JcicZ451 of List
   */
  public Slice<JcicZ451> custRcEq(String custId_0, int applyDate_1, int index, int limit, TitaVo... titaVo);

  /**
   * SubmitKey= , AND CustId= , AND ApplyDate= , AND CourtCode= , AND DelayYM=
   *
   * @param submitKey_0 submitKey_0
   * @param custId_1 custId_1
   * @param applyDate_2 applyDate_2
   * @param courtCode_3 courtCode_3
   * @param delayYM_4 delayYM_4
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ451 JcicZ451 of List
   */
  public Slice<JcicZ451> otherEq(String submitKey_0, String custId_1, int applyDate_2, String courtCode_3, int delayYM_4, int index, int limit, TitaVo... titaVo);

  /**
   * Ukey=
   *
   * @param ukey_0 ukey_0
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ451 JcicZ451 of List
   */
  public JcicZ451 ukeyFirst(String ukey_0, TitaVo... titaVo);

  /**
   * SubmitKey= , AND CustId= , AND ApplyDate= , AND CourtCode= , AND DelayYM=
   *
   * @param submitKey_0 submitKey_0
   * @param custId_1 custId_1
   * @param applyDate_2 applyDate_2
   * @param courtCode_3 courtCode_3
   * @param delayYM_4 delayYM_4
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ451 JcicZ451 of List
   */
  public JcicZ451 otherFirst(String submitKey_0, String custId_1, int applyDate_2, String courtCode_3, int delayYM_4, TitaVo... titaVo);

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
   * @return Slice JcicZ451 JcicZ451 of List
   */
  public Slice<JcicZ451> custRcSubCourtEq(String submitKey_0, String custId_1, int applyDate_2, String courtCode_3, int index, int limit, TitaVo... titaVo);

  /**
   * ActualFilingDate= , AND ActualFilingMark=
   *
   * @param actualFilingDate_0 actualFilingDate_0
   * @param actualFilingMark_1 actualFilingMark_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ451 JcicZ451 of List
   */
  public Slice<JcicZ451> findkeyFilingDate(int actualFilingDate_0, String actualFilingMark_1, int index, int limit, TitaVo... titaVo);

  /**
   * hold By JcicZ451
   * 
   * @param jcicZ451Id key
   * @param titaVo Variable-Length Argument
   * @return JcicZ451 JcicZ451
   */
  public JcicZ451 holdById(JcicZ451Id jcicZ451Id, TitaVo... titaVo);

  /**
   * hold By JcicZ451
   * 
   * @param jcicZ451 key
   * @param titaVo Variable-Length Argument
   * @return JcicZ451 JcicZ451
   */
  public JcicZ451 holdById(JcicZ451 jcicZ451, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param jcicZ451 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ451 Entity
   * @throws DBException exception
   */
  public JcicZ451 insert(JcicZ451 jcicZ451, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param jcicZ451 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ451 Entity
   * @throws DBException exception
   */
  public JcicZ451 update(JcicZ451 jcicZ451, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param jcicZ451 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ451 Entity
   * @throws DBException exception
   */
  public JcicZ451 update2(JcicZ451 jcicZ451, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param jcicZ451 Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(JcicZ451 jcicZ451, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param jcicZ451 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<JcicZ451> jcicZ451, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param jcicZ451 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<JcicZ451> jcicZ451, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param jcicZ451 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<JcicZ451> jcicZ451, TitaVo... titaVo) throws DBException;

}
