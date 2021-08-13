package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicZ043;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicZ043Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ043Service {

  /**
   * findByPrimaryKey
   *
   * @param jcicZ043Id PK
   * @param titaVo Variable-Length Argument
   * @return JcicZ043 JcicZ043
   */
  public JcicZ043 findById(JcicZ043Id jcicZ043Id, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ043 JcicZ043 of List
   */
  public Slice<JcicZ043> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * CustId=
   *
   * @param custId_0 custId_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ043 JcicZ043 of List
   */
  public Slice<JcicZ043> CustIdEq(String custId_0, int index, int limit, TitaVo... titaVo);

  /**
   * RcDate=
   *
   * @param rcDate_0 rcDate_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ043 JcicZ043 of List
   */
  public Slice<JcicZ043> RcDateEq(int rcDate_0, int index, int limit, TitaVo... titaVo);

  /**
   * CustId= , AND RcDate=
   *
   * @param custId_0 custId_0
   * @param rcDate_1 rcDate_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ043 JcicZ043 of List
   */
  public Slice<JcicZ043> CustRcEq(String custId_0, int rcDate_1, int index, int limit, TitaVo... titaVo);

  /**
   * CustId= , AND RcDate= , AND SubmitKey= , AND MaxMainCode=
   *
   * @param custId_0 custId_0
   * @param rcDate_1 rcDate_1
   * @param submitKey_2 submitKey_2
   * @param maxMainCode_3 maxMainCode_3
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ043 JcicZ043 of List
   */
  public Slice<JcicZ043> CoutCollaterals(String custId_0, int rcDate_1, String submitKey_2, String maxMainCode_3, int index, int limit, TitaVo... titaVo);

  /**
   * hold By JcicZ043
   * 
   * @param jcicZ043Id key
   * @param titaVo Variable-Length Argument
   * @return JcicZ043 JcicZ043
   */
  public JcicZ043 holdById(JcicZ043Id jcicZ043Id, TitaVo... titaVo);

  /**
   * hold By JcicZ043
   * 
   * @param jcicZ043 key
   * @param titaVo Variable-Length Argument
   * @return JcicZ043 JcicZ043
   */
  public JcicZ043 holdById(JcicZ043 jcicZ043, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param jcicZ043 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ043 Entity
   * @throws DBException exception
   */
  public JcicZ043 insert(JcicZ043 jcicZ043, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param jcicZ043 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ043 Entity
   * @throws DBException exception
   */
  public JcicZ043 update(JcicZ043 jcicZ043, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param jcicZ043 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ043 Entity
   * @throws DBException exception
   */
  public JcicZ043 update2(JcicZ043 jcicZ043, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param jcicZ043 Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(JcicZ043 jcicZ043, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param jcicZ043 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<JcicZ043> jcicZ043, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param jcicZ043 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<JcicZ043> jcicZ043, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param jcicZ043 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<JcicZ043> jcicZ043, TitaVo... titaVo) throws DBException;

}
