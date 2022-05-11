package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.FinReportQuality;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.FinReportQualityId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface FinReportQualityService {

  /**
   * findByPrimaryKey
   *
   * @param finReportQualityId PK
   * @param titaVo Variable-Length Argument
   * @return FinReportQuality FinReportQuality
   */
  public FinReportQuality findById(FinReportQualityId finReportQualityId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice FinReportQuality FinReportQuality of List
   */
  public Slice<FinReportQuality> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * CustUKey = 
   *
   * @param custUKey_0 custUKey_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice FinReportQuality FinReportQuality of List
   */
  public Slice<FinReportQuality> findCustUKey(String custUKey_0, int index, int limit, TitaVo... titaVo);

  /**
   * hold By FinReportQuality
   * 
   * @param finReportQualityId key
   * @param titaVo Variable-Length Argument
   * @return FinReportQuality FinReportQuality
   */
  public FinReportQuality holdById(FinReportQualityId finReportQualityId, TitaVo... titaVo);

  /**
   * hold By FinReportQuality
   * 
   * @param finReportQuality key
   * @param titaVo Variable-Length Argument
   * @return FinReportQuality FinReportQuality
   */
  public FinReportQuality holdById(FinReportQuality finReportQuality, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param finReportQuality Entity
   * @param titaVo Variable-Length Argument
   * @return FinReportQuality Entity
   * @throws DBException exception
   */
  public FinReportQuality insert(FinReportQuality finReportQuality, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param finReportQuality Entity
   * @param titaVo Variable-Length Argument
   * @return FinReportQuality Entity
   * @throws DBException exception
   */
  public FinReportQuality update(FinReportQuality finReportQuality, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param finReportQuality Entity
   * @param titaVo Variable-Length Argument
   * @return FinReportQuality Entity
   * @throws DBException exception
   */
  public FinReportQuality update2(FinReportQuality finReportQuality, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param finReportQuality Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(FinReportQuality finReportQuality, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param finReportQuality Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<FinReportQuality> finReportQuality, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param finReportQuality Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<FinReportQuality> finReportQuality, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param finReportQuality Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<FinReportQuality> finReportQuality, TitaVo... titaVo) throws DBException;

}
