package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.SlipMedia2022;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.SlipMedia2022Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface SlipMedia2022Service {

  /**
   * findByPrimaryKey
   *
   * @param slipMedia2022Id PK
   * @param titaVo Variable-Length Argument
   * @return SlipMedia2022 SlipMedia2022
   */
  public SlipMedia2022 findById(SlipMedia2022Id slipMedia2022Id, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice SlipMedia2022 SlipMedia2022 of List
   */
  public Slice<SlipMedia2022> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * AcDate = ,AND BatchNo = ,AND MediaSeq = ,AND LatestFlag = 
   *
   * @param acDate_0 acDate_0
   * @param batchNo_1 batchNo_1
   * @param mediaSeq_2 mediaSeq_2
   * @param latestFlag_3 latestFlag_3
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice SlipMedia2022 SlipMedia2022 of List
   */
  public Slice<SlipMedia2022> findMediaSeq(int acDate_0, int batchNo_1, int mediaSeq_2, String latestFlag_3, int index, int limit, TitaVo... titaVo);

  /**
   * AcDate = ,AND BatchNo = 
   *
   * @param acDate_0 acDate_0
   * @param batchNo_1 batchNo_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice SlipMedia2022 SlipMedia2022 of List
   */
  public Slice<SlipMedia2022> findBatchNo(int acDate_0, int batchNo_1, int index, int limit, TitaVo... titaVo);

  /**
   * AcDate = ,AND BatchNo = 
   *
   * @param acDate_0 acDate_0
   * @param batchNo_1 batchNo_1
   * @param titaVo Variable-Length Argument
   * @return Slice SlipMedia2022 SlipMedia2022 of List
   */
  public SlipMedia2022 findMediaSeqFirst(int acDate_0, int batchNo_1, TitaVo... titaVo);

  /**
   * hold By SlipMedia2022
   * 
   * @param slipMedia2022Id key
   * @param titaVo Variable-Length Argument
   * @return SlipMedia2022 SlipMedia2022
   */
  public SlipMedia2022 holdById(SlipMedia2022Id slipMedia2022Id, TitaVo... titaVo);

  /**
   * hold By SlipMedia2022
   * 
   * @param slipMedia2022 key
   * @param titaVo Variable-Length Argument
   * @return SlipMedia2022 SlipMedia2022
   */
  public SlipMedia2022 holdById(SlipMedia2022 slipMedia2022, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param slipMedia2022 Entity
   * @param titaVo Variable-Length Argument
   * @return SlipMedia2022 Entity
   * @throws DBException exception
   */
  public SlipMedia2022 insert(SlipMedia2022 slipMedia2022, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param slipMedia2022 Entity
   * @param titaVo Variable-Length Argument
   * @return SlipMedia2022 Entity
   * @throws DBException exception
   */
  public SlipMedia2022 update(SlipMedia2022 slipMedia2022, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param slipMedia2022 Entity
   * @param titaVo Variable-Length Argument
   * @return SlipMedia2022 Entity
   * @throws DBException exception
   */
  public SlipMedia2022 update2(SlipMedia2022 slipMedia2022, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param slipMedia2022 Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(SlipMedia2022 slipMedia2022, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param slipMedia2022 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<SlipMedia2022> slipMedia2022, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param slipMedia2022 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<SlipMedia2022> slipMedia2022, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param slipMedia2022 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<SlipMedia2022> slipMedia2022, TitaVo... titaVo) throws DBException;

}
