package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CdReport;
import org.springframework.data.domain.Slice;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CdReportService {

  /**
   * findByPrimaryKey
   *
   * @param formNo PK
   * @param titaVo Variable-Length Argument
   * @return CdReport CdReport
   */
  public CdReport findById(String formNo, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdReport CdReport of List
   */
  public Slice<CdReport> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * Cycle = 
   *
   * @param cycle_0 cycle_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdReport CdReport of List
   */
  public Slice<CdReport> findCycle(int cycle_0, int index, int limit, TitaVo... titaVo);

  /**
   * Enable = 
   *
   * @param enable_0 enable_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdReport CdReport of List
   */
  public Slice<CdReport> findEnable(String enable_0, int index, int limit, TitaVo... titaVo);

  /**
   * FormNo %
   *
   * @param formNo_0 formNo_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdReport CdReport of List
   */
  public Slice<CdReport> formNoLike(String formNo_0, int index, int limit, TitaVo... titaVo);

  /**
   * FormName %
   *
   * @param formName_0 formName_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdReport CdReport of List
   */
  public Slice<CdReport> formNameLike(String formName_0, int index, int limit, TitaVo... titaVo);

  /**
   * FormNo =
   *
   * @param formNo_0 formNo_0
   * @param titaVo Variable-Length Argument
   * @return Slice CdReport CdReport of List
   */
  public CdReport FormNoFirst(String formNo_0, TitaVo... titaVo);

  /**
   * hold By CdReport
   * 
   * @param formNo key
   * @param titaVo Variable-Length Argument
   * @return CdReport CdReport
   */
  public CdReport holdById(String formNo, TitaVo... titaVo);

  /**
   * hold By CdReport
   * 
   * @param cdReport key
   * @param titaVo Variable-Length Argument
   * @return CdReport CdReport
   */
  public CdReport holdById(CdReport cdReport, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param cdReport Entity
   * @param titaVo Variable-Length Argument
   * @return CdReport Entity
   * @throws DBException exception
   */
  public CdReport insert(CdReport cdReport, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param cdReport Entity
   * @param titaVo Variable-Length Argument
   * @return CdReport Entity
   * @throws DBException exception
   */
  public CdReport update(CdReport cdReport, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param cdReport Entity
   * @param titaVo Variable-Length Argument
   * @return CdReport Entity
   * @throws DBException exception
   */
  public CdReport update2(CdReport cdReport, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param cdReport Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(CdReport cdReport, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param cdReport Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<CdReport> cdReport, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param cdReport Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<CdReport> cdReport, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param cdReport Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<CdReport> cdReport, TitaVo... titaVo) throws DBException;

}
