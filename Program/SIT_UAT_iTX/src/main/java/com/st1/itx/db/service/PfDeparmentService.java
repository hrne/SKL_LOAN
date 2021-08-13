package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.PfDeparment;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.PfDeparmentId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface PfDeparmentService {

  /**
   * findByPrimaryKey
   *
   * @param pfDeparmentId PK
   * @param titaVo Variable-Length Argument
   * @return PfDeparment PfDeparment
   */
  public PfDeparment findById(PfDeparmentId pfDeparmentId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice PfDeparment PfDeparment of List
   */
  public Slice<PfDeparment> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * DeptCode= 
   *
   * @param deptCode_0 deptCode_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice PfDeparment PfDeparment of List
   */
  public Slice<PfDeparment> findByDeptCode(String deptCode_0, int index, int limit, TitaVo... titaVo);

  /**
   * DistCode=
   *
   * @param distCode_0 distCode_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice PfDeparment PfDeparment of List
   */
  public Slice<PfDeparment> findByDistCode(String distCode_0, int index, int limit, TitaVo... titaVo);

  /**
   * UnitCode=
   *
   * @param unitCode_0 unitCode_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice PfDeparment PfDeparment of List
   */
  public Slice<PfDeparment> findByUnitCode(String unitCode_0, int index, int limit, TitaVo... titaVo);

  /**
   * DeptCode= , AND DistCode=
   *
   * @param deptCode_0 deptCode_0
   * @param distCode_1 distCode_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice PfDeparment PfDeparment of List
   */
  public Slice<PfDeparment> findByDeptCodeAndDistCode(String deptCode_0, String distCode_1, int index, int limit, TitaVo... titaVo);

  /**
   * DeptCode= , AND UnitCode=
   *
   * @param deptCode_0 deptCode_0
   * @param unitCode_1 unitCode_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice PfDeparment PfDeparment of List
   */
  public Slice<PfDeparment> findByDeptCodeAndUnitCode(String deptCode_0, String unitCode_1, int index, int limit, TitaVo... titaVo);

  /**
   * DistCode= , AND UnitCode=
   *
   * @param distCode_0 distCode_0
   * @param unitCode_1 unitCode_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice PfDeparment PfDeparment of List
   */
  public Slice<PfDeparment> findByDistCodeAndUnitCode(String distCode_0, String unitCode_1, int index, int limit, TitaVo... titaVo);

  /**
   * DeptCode= , AND DistCode= , AND UnitCode=
   *
   * @param deptCode_0 deptCode_0
   * @param distCode_1 distCode_1
   * @param unitCode_2 unitCode_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice PfDeparment PfDeparment of List
   */
  public Slice<PfDeparment> findByDeptCodeAndDistCodeAndUnitCode(String deptCode_0, String distCode_1, String unitCode_2, int index, int limit, TitaVo... titaVo);

  /**
   * hold By PfDeparment
   * 
   * @param pfDeparmentId key
   * @param titaVo Variable-Length Argument
   * @return PfDeparment PfDeparment
   */
  public PfDeparment holdById(PfDeparmentId pfDeparmentId, TitaVo... titaVo);

  /**
   * hold By PfDeparment
   * 
   * @param pfDeparment key
   * @param titaVo Variable-Length Argument
   * @return PfDeparment PfDeparment
   */
  public PfDeparment holdById(PfDeparment pfDeparment, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param pfDeparment Entity
   * @param titaVo Variable-Length Argument
   * @return PfDeparment Entity
   * @throws DBException exception
   */
  public PfDeparment insert(PfDeparment pfDeparment, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param pfDeparment Entity
   * @param titaVo Variable-Length Argument
   * @return PfDeparment Entity
   * @throws DBException exception
   */
  public PfDeparment update(PfDeparment pfDeparment, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param pfDeparment Entity
   * @param titaVo Variable-Length Argument
   * @return PfDeparment Entity
   * @throws DBException exception
   */
  public PfDeparment update2(PfDeparment pfDeparment, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param pfDeparment Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(PfDeparment pfDeparment, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param pfDeparment Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<PfDeparment> pfDeparment, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param pfDeparment Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<PfDeparment> pfDeparment, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param pfDeparment Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<PfDeparment> pfDeparment, TitaVo... titaVo) throws DBException;

}
