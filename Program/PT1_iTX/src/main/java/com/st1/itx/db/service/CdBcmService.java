package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CdBcm;
import org.springframework.data.domain.Slice;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CdBcmService {

  /**
   * findByPrimaryKey
   *
   * @param unitCode PK
   * @param titaVo Variable-Length Argument
   * @return CdBcm CdBcm
   */
  public CdBcm findById(String unitCode, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdBcm CdBcm of List
   */
  public Slice<CdBcm> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * DeptCode &gt;= ,AND DeptCode &lt;=
   *
   * @param deptCode_0 deptCode_0
   * @param deptCode_1 deptCode_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdBcm CdBcm of List
   */
  public Slice<CdBcm> findDeptCode(String deptCode_0, String deptCode_1, int index, int limit, TitaVo... titaVo);

  /**
   * DistCode &gt;= ,AND DistCode &lt;=
   *
   * @param distCode_0 distCode_0
   * @param distCode_1 distCode_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdBcm CdBcm of List
   */
  public Slice<CdBcm> findDistCode(String distCode_0, String distCode_1, int index, int limit, TitaVo... titaVo);

  /**
   * UnitCode &gt;= ,AND UnitCode &lt;=
   *
   * @param unitCode_0 unitCode_0
   * @param unitCode_1 unitCode_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdBcm CdBcm of List
   */
  public Slice<CdBcm> findUnitCode(String unitCode_0, String unitCode_1, int index, int limit, TitaVo... titaVo);

  /**
   * DeptCode =
   *
   * @param deptCode_0 deptCode_0
   * @param titaVo Variable-Length Argument
   * @return Slice CdBcm CdBcm of List
   */
  public CdBcm deptCodeFirst(String deptCode_0, TitaVo... titaVo);

  /**
   * DistCode =
   *
   * @param distCode_0 distCode_0
   * @param titaVo Variable-Length Argument
   * @return Slice CdBcm CdBcm of List
   */
  public CdBcm distCodeFirst(String distCode_0, TitaVo... titaVo);

  /**
   * UnitManager %
   *
   * @param unitManager_0 unitManager_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdBcm CdBcm of List
   */
  public Slice<CdBcm> findUnitManager(String unitManager_0, int index, int limit, TitaVo... titaVo);

  /**
   * DeptManager %
   *
   * @param deptManager_0 deptManager_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdBcm CdBcm of List
   */
  public Slice<CdBcm> findDeptManager(String deptManager_0, int index, int limit, TitaVo... titaVo);

  /**
   * DistManager %
   *
   * @param distManager_0 distManager_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdBcm CdBcm of List
   */
  public Slice<CdBcm> findDistManager(String distManager_0, int index, int limit, TitaVo... titaVo);

  /**
   * DeptCode %
   *
   * @param deptCode_0 deptCode_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdBcm CdBcm of List
   */
  public Slice<CdBcm> findDeptCode1(String deptCode_0, int index, int limit, TitaVo... titaVo);

  /**
   * DistCode %
   *
   * @param distCode_0 distCode_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdBcm CdBcm of List
   */
  public Slice<CdBcm> findDistCode1(String distCode_0, int index, int limit, TitaVo... titaVo);

  /**
   * UnitCode %
   *
   * @param unitCode_0 unitCode_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdBcm CdBcm of List
   */
  public Slice<CdBcm> findUnitCode1(String unitCode_0, int index, int limit, TitaVo... titaVo);

  /**
   * DistItem %
   *
   * @param distItem_0 distItem_0
   * @param titaVo Variable-Length Argument
   * @return Slice CdBcm CdBcm of List
   */
  public CdBcm distItemFirst(String distItem_0, TitaVo... titaVo);

  /**
   * hold By CdBcm
   * 
   * @param unitCode key
   * @param titaVo Variable-Length Argument
   * @return CdBcm CdBcm
   */
  public CdBcm holdById(String unitCode, TitaVo... titaVo);

  /**
   * hold By CdBcm
   * 
   * @param cdBcm key
   * @param titaVo Variable-Length Argument
   * @return CdBcm CdBcm
   */
  public CdBcm holdById(CdBcm cdBcm, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param cdBcm Entity
   * @param titaVo Variable-Length Argument
   * @return CdBcm Entity
   * @throws DBException exception
   */
  public CdBcm insert(CdBcm cdBcm, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param cdBcm Entity
   * @param titaVo Variable-Length Argument
   * @return CdBcm Entity
   * @throws DBException exception
   */
  public CdBcm update(CdBcm cdBcm, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param cdBcm Entity
   * @param titaVo Variable-Length Argument
   * @return CdBcm Entity
   * @throws DBException exception
   */
  public CdBcm update2(CdBcm cdBcm, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param cdBcm Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(CdBcm cdBcm, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param cdBcm Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<CdBcm> cdBcm, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param cdBcm Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<CdBcm> cdBcm, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param cdBcm Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<CdBcm> cdBcm, TitaVo... titaVo) throws DBException;

}
