package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.SlipEbsRecord;
import org.springframework.data.domain.Slice;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface SlipEbsRecordService {

  /**
   * findByPrimaryKey
   *
   * @param uploadNo PK
   * @param titaVo Variable-Length Argument
   * @return SlipEbsRecord SlipEbsRecord
   */
  public SlipEbsRecord findById(Long uploadNo, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice SlipEbsRecord SlipEbsRecord of List
   */
  public Slice<SlipEbsRecord> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * hold By SlipEbsRecord
   * 
   * @param uploadNo key
   * @param titaVo Variable-Length Argument
   * @return SlipEbsRecord SlipEbsRecord
   */
  public SlipEbsRecord holdById(Long uploadNo, TitaVo... titaVo);

  /**
   * hold By SlipEbsRecord
   * 
   * @param slipEbsRecord key
   * @param titaVo Variable-Length Argument
   * @return SlipEbsRecord SlipEbsRecord
   */
  public SlipEbsRecord holdById(SlipEbsRecord slipEbsRecord, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param slipEbsRecord Entity
   * @param titaVo Variable-Length Argument
   * @return SlipEbsRecord Entity
   * @throws DBException exception
   */
  public SlipEbsRecord insert(SlipEbsRecord slipEbsRecord, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param slipEbsRecord Entity
   * @param titaVo Variable-Length Argument
   * @return SlipEbsRecord Entity
   * @throws DBException exception
   */
  public SlipEbsRecord update(SlipEbsRecord slipEbsRecord, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param slipEbsRecord Entity
   * @param titaVo Variable-Length Argument
   * @return SlipEbsRecord Entity
   * @throws DBException exception
   */
  public SlipEbsRecord update2(SlipEbsRecord slipEbsRecord, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param slipEbsRecord Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(SlipEbsRecord slipEbsRecord, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param slipEbsRecord Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<SlipEbsRecord> slipEbsRecord, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param slipEbsRecord Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<SlipEbsRecord> slipEbsRecord, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param slipEbsRecord Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<SlipEbsRecord> slipEbsRecord, TitaVo... titaVo) throws DBException;

}
