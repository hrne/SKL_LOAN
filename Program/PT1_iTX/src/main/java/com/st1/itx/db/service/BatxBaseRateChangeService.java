package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.BatxBaseRateChange;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.BatxBaseRateChangeId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface BatxBaseRateChangeService {

  /**
   * findByPrimaryKey
   *
   * @param batxBaseRateChangeId PK
   * @param titaVo Variable-Length Argument
   * @return BatxBaseRateChange BatxBaseRateChange
   */
  public BatxBaseRateChange findById(BatxBaseRateChangeId batxBaseRateChangeId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice BatxBaseRateChange BatxBaseRateChange of List
   */
  public Slice<BatxBaseRateChange> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * hold By BatxBaseRateChange
   * 
   * @param batxBaseRateChangeId key
   * @param titaVo Variable-Length Argument
   * @return BatxBaseRateChange BatxBaseRateChange
   */
  public BatxBaseRateChange holdById(BatxBaseRateChangeId batxBaseRateChangeId, TitaVo... titaVo);

  /**
   * hold By BatxBaseRateChange
   * 
   * @param batxBaseRateChange key
   * @param titaVo Variable-Length Argument
   * @return BatxBaseRateChange BatxBaseRateChange
   */
  public BatxBaseRateChange holdById(BatxBaseRateChange batxBaseRateChange, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param batxBaseRateChange Entity
   * @param titaVo Variable-Length Argument
   * @return BatxBaseRateChange Entity
   * @throws DBException exception
   */
  public BatxBaseRateChange insert(BatxBaseRateChange batxBaseRateChange, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param batxBaseRateChange Entity
   * @param titaVo Variable-Length Argument
   * @return BatxBaseRateChange Entity
   * @throws DBException exception
   */
  public BatxBaseRateChange update(BatxBaseRateChange batxBaseRateChange, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param batxBaseRateChange Entity
   * @param titaVo Variable-Length Argument
   * @return BatxBaseRateChange Entity
   * @throws DBException exception
   */
  public BatxBaseRateChange update2(BatxBaseRateChange batxBaseRateChange, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param batxBaseRateChange Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(BatxBaseRateChange batxBaseRateChange, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param batxBaseRateChange Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<BatxBaseRateChange> batxBaseRateChange, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param batxBaseRateChange Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<BatxBaseRateChange> batxBaseRateChange, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param batxBaseRateChange Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<BatxBaseRateChange> batxBaseRateChange, TitaVo... titaVo) throws DBException;

}
