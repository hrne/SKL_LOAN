package com.st1.itx.db.service;

import java.util.List;

import java.math.BigDecimal;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.PfCoOfficerLog;
import org.springframework.data.domain.Slice;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface PfCoOfficerLogService {

  /**
   * findByPrimaryKey
   *
   * @param logNo PK
   * @param titaVo Variable-Length Argument
   * @return PfCoOfficerLog PfCoOfficerLog
   */
  public PfCoOfficerLog findById(Long logNo, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice PfCoOfficerLog PfCoOfficerLog of List
   */
  public Slice<PfCoOfficerLog> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * EmpNo=
   *
   * @param empNo_0 empNo_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice PfCoOfficerLog PfCoOfficerLog of List
   */
  public Slice<PfCoOfficerLog> findEmpNoEq(String empNo_0, int index, int limit, TitaVo... titaVo);

  /**
   * EmpNo=, AND EffectiveDate= 
   *
   * @param empNo_0 empNo_0
   * @param effectiveDate_1 effectiveDate_1
   * @param titaVo Variable-Length Argument
   * @return Slice PfCoOfficerLog PfCoOfficerLog of List
   */
  public PfCoOfficerLog findEmpEffectiveDateFirst(String empNo_0, int effectiveDate_1, TitaVo... titaVo);

  /**
   * hold By PfCoOfficerLog
   * 
   * @param logNo key
   * @param titaVo Variable-Length Argument
   * @return PfCoOfficerLog PfCoOfficerLog
   */
  public PfCoOfficerLog holdById(Long logNo, TitaVo... titaVo);

  /**
   * hold By PfCoOfficerLog
   * 
   * @param pfCoOfficerLog key
   * @param titaVo Variable-Length Argument
   * @return PfCoOfficerLog PfCoOfficerLog
   */
  public PfCoOfficerLog holdById(PfCoOfficerLog pfCoOfficerLog, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param pfCoOfficerLog Entity
   * @param titaVo Variable-Length Argument
   * @return PfCoOfficerLog Entity
   * @throws DBException exception
   */
  public PfCoOfficerLog insert(PfCoOfficerLog pfCoOfficerLog, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param pfCoOfficerLog Entity
   * @param titaVo Variable-Length Argument
   * @return PfCoOfficerLog Entity
   * @throws DBException exception
   */
  public PfCoOfficerLog update(PfCoOfficerLog pfCoOfficerLog, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param pfCoOfficerLog Entity
   * @param titaVo Variable-Length Argument
   * @return PfCoOfficerLog Entity
   * @throws DBException exception
   */
  public PfCoOfficerLog update2(PfCoOfficerLog pfCoOfficerLog, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param pfCoOfficerLog Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(PfCoOfficerLog pfCoOfficerLog, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param pfCoOfficerLog Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<PfCoOfficerLog> pfCoOfficerLog, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param pfCoOfficerLog Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<PfCoOfficerLog> pfCoOfficerLog, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param pfCoOfficerLog Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<PfCoOfficerLog> pfCoOfficerLog, TitaVo... titaVo) throws DBException;

}
