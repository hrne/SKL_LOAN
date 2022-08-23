package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicZ051;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicZ051Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ051Service {

  /**
   * findByPrimaryKey
   *
   * @param jcicZ051Id PK
   * @param titaVo Variable-Length Argument
   * @return JcicZ051 JcicZ051
   */
  public JcicZ051 findById(JcicZ051Id jcicZ051Id, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ051 JcicZ051 of List
   */
  public Slice<JcicZ051> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * CustId=
   *
   * @param custId_0 custId_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ051 JcicZ051 of List
   */
  public Slice<JcicZ051> custIdEq(String custId_0, int index, int limit, TitaVo... titaVo);

  /**
   * RcDate=
   *
   * @param rcDate_0 rcDate_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ051 JcicZ051 of List
   */
  public Slice<JcicZ051> rcDateEq(int rcDate_0, int index, int limit, TitaVo... titaVo);

  /**
   * CustId= , AND RcDate=
   *
   * @param custId_0 custId_0
   * @param rcDate_1 rcDate_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ051 JcicZ051 of List
   */
  public Slice<JcicZ051> custRcEq(String custId_0, int rcDate_1, int index, int limit, TitaVo... titaVo);

  /**
   * SubmitKey= , AND CustId= , AND RcDate= , AND DelayYM= 
   *
   * @param submitKey_0 submitKey_0
   * @param custId_1 custId_1
   * @param rcDate_2 rcDate_2
   * @param delayYM_3 delayYM_3
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ051 JcicZ051 of List
   */
  public Slice<JcicZ051> InJcicZ051(String submitKey_0, String custId_1, int rcDate_2, int delayYM_3, int index, int limit, TitaVo... titaVo);

  /**
   * CustId= , AND RcDate= , AND SubmitKey=
   *
   * @param custId_0 custId_0
   * @param rcDate_1 rcDate_1
   * @param submitKey_2 submitKey_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ051 JcicZ051 of List
   */
  public Slice<JcicZ051> SubCustRcEq(String custId_0, int rcDate_1, String submitKey_2, int index, int limit, TitaVo... titaVo);

  /**
   * SubmitKey= , AND CustId= , AND RcDate= , AND DelayYM= 
   *
   * @param submitKey_0 submitKey_0
   * @param custId_1 custId_1
   * @param rcDate_2 rcDate_2
   * @param delayYM_3 delayYM_3
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ051 JcicZ051 of List
   */
  public Slice<JcicZ051> otherEq(String submitKey_0, String custId_1, int rcDate_2, int delayYM_3, int index, int limit, TitaVo... titaVo);

  /**
   * Ukey=
   *
   * @param ukey_0 ukey_0
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ051 JcicZ051 of List
   */
  public JcicZ051 ukeyFirst(String ukey_0, TitaVo... titaVo);

  /**
   * SubmitKey= , AND CustId= , AND RcDate= , AND DelayYM= 
   *
   * @param submitKey_0 submitKey_0
   * @param custId_1 custId_1
   * @param rcDate_2 rcDate_2
   * @param delayYM_3 delayYM_3
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ051 JcicZ051 of List
   */
  public JcicZ051 otherFirst(String submitKey_0, String custId_1, int rcDate_2, int delayYM_3, TitaVo... titaVo);

  /**
   * hold By JcicZ051
   * 
   * @param jcicZ051Id key
   * @param titaVo Variable-Length Argument
   * @return JcicZ051 JcicZ051
   */
  public JcicZ051 holdById(JcicZ051Id jcicZ051Id, TitaVo... titaVo);

  /**
   * hold By JcicZ051
   * 
   * @param jcicZ051 key
   * @param titaVo Variable-Length Argument
   * @return JcicZ051 JcicZ051
   */
  public JcicZ051 holdById(JcicZ051 jcicZ051, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param jcicZ051 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ051 Entity
   * @throws DBException exception
   */
  public JcicZ051 insert(JcicZ051 jcicZ051, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param jcicZ051 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ051 Entity
   * @throws DBException exception
   */
  public JcicZ051 update(JcicZ051 jcicZ051, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param jcicZ051 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ051 Entity
   * @throws DBException exception
   */
  public JcicZ051 update2(JcicZ051 jcicZ051, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param jcicZ051 Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(JcicZ051 jcicZ051, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param jcicZ051 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<JcicZ051> jcicZ051, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param jcicZ051 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<JcicZ051> jcicZ051, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param jcicZ051 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<JcicZ051> jcicZ051, TitaVo... titaVo) throws DBException;

}
