package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicZ443;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicZ443Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ443Service {

  /**
   * findByPrimaryKey
   *
   * @param jcicZ443Id PK
   * @param titaVo Variable-Length Argument
   * @return JcicZ443 JcicZ443
   */
  public JcicZ443 findById(JcicZ443Id jcicZ443Id, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ443 JcicZ443 of List
   */
  public Slice<JcicZ443> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * CustId=
   *
   * @param custId_0 custId_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ443 JcicZ443 of List
   */
  public Slice<JcicZ443> custIdEq(String custId_0, int index, int limit, TitaVo... titaVo);

  /**
   * ApplyDate=
   *
   * @param applyDate_0 applyDate_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ443 JcicZ443 of List
   */
  public Slice<JcicZ443> rcDateEq(int applyDate_0, int index, int limit, TitaVo... titaVo);

  /**
   * CustId= , AND ApplyDate=
   *
   * @param custId_0 custId_0
   * @param applyDate_1 applyDate_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ443 JcicZ443 of List
   */
  public Slice<JcicZ443> custRcEq(String custId_0, int applyDate_1, int index, int limit, TitaVo... titaVo);

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
   * @return Slice JcicZ443 JcicZ443 of List
   */
  public Slice<JcicZ443> otherEq(String submitKey_0, String custId_1, int applyDate_2, String courtCode_3, String maxMainCode_4, int index, int limit, TitaVo... titaVo);

  /**
   * Ukey=
   *
   * @param ukey_0 ukey_0
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ443 JcicZ443 of List
   */
  public JcicZ443 ukeyFirst(String ukey_0, TitaVo... titaVo);

  /**
   * SubmitKey= , AND CustId= , AND ApplyDate= , AND CourtCode= , AND MaxMainCode=
   *
   * @param submitKey_0 submitKey_0
   * @param custId_1 custId_1
   * @param applyDate_2 applyDate_2
   * @param courtCode_3 courtCode_3
   * @param maxMainCode_4 maxMainCode_4
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ443 JcicZ443 of List
   */
  public JcicZ443 otherFirst(String submitKey_0, String custId_1, int applyDate_2, String courtCode_3, String maxMainCode_4, TitaVo... titaVo);

  /**
   * ActualFilingDate= , AND ActualFilingMark= 
   *
   * @param actualFilingDate_0 actualFilingDate_0
   * @param actualFilingMark_1 actualFilingMark_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ443 JcicZ443 of List
   */
  public Slice<JcicZ443> findkeyFilingDate(int actualFilingDate_0, String actualFilingMark_1, int index, int limit, TitaVo... titaVo);

  /**
   * hold By JcicZ443
   * 
   * @param jcicZ443Id key
   * @param titaVo Variable-Length Argument
   * @return JcicZ443 JcicZ443
   */
  public JcicZ443 holdById(JcicZ443Id jcicZ443Id, TitaVo... titaVo);

  /**
   * hold By JcicZ443
   * 
   * @param jcicZ443 key
   * @param titaVo Variable-Length Argument
   * @return JcicZ443 JcicZ443
   */
  public JcicZ443 holdById(JcicZ443 jcicZ443, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param jcicZ443 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ443 Entity
   * @throws DBException exception
   */
  public JcicZ443 insert(JcicZ443 jcicZ443, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param jcicZ443 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ443 Entity
   * @throws DBException exception
   */
  public JcicZ443 update(JcicZ443 jcicZ443, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param jcicZ443 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ443 Entity
   * @throws DBException exception
   */
  public JcicZ443 update2(JcicZ443 jcicZ443, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param jcicZ443 Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(JcicZ443 jcicZ443, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param jcicZ443 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<JcicZ443> jcicZ443, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param jcicZ443 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<JcicZ443> jcicZ443, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param jcicZ443 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<JcicZ443> jcicZ443, TitaVo... titaVo) throws DBException;

}
