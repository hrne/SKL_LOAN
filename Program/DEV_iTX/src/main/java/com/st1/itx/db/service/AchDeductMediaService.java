package com.st1.itx.db.service;

import java.util.List;

import java.math.BigDecimal;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.AchDeductMedia;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.AchDeductMediaId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface AchDeductMediaService {

  /**
   * findByPrimaryKey
   *
   * @param achDeductMediaId PK
   * @param titaVo Variable-Length Argument
   * @return AchDeductMedia AchDeductMedia
   */
  public AchDeductMedia findById(AchDeductMediaId achDeductMediaId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice AchDeductMedia AchDeductMedia of List
   */
  public Slice<AchDeductMedia> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * AcDate = , AND BatchNo = , AND DetailSeq = 
   *
   * @param acDate_0 acDate_0
   * @param batchNo_1 batchNo_1
   * @param detailSeq_2 detailSeq_2
   * @param titaVo Variable-Length Argument
   * @return Slice AchDeductMedia AchDeductMedia of List
   */
  public AchDeductMedia detailSeqFirst(int acDate_0, String batchNo_1, int detailSeq_2, TitaVo... titaVo);

  /**
   * CustNo = ,AND FacmNo = ,AND AchRepayCode = ,AND PrevIntDate = ,AND RepayAmt =
   *
   * @param custNo_0 custNo_0
   * @param facmNo_1 facmNo_1
   * @param achRepayCode_2 achRepayCode_2
   * @param prevIntDate_3 prevIntDate_3
   * @param repayAmt_4 repayAmt_4
   * @param titaVo Variable-Length Argument
   * @return Slice AchDeductMedia AchDeductMedia of List
   */
  public AchDeductMedia reseiveCheckFirst(int custNo_0, int facmNo_1, String achRepayCode_2, int prevIntDate_3, BigDecimal repayAmt_4, TitaVo... titaVo);

  /**
   * MediaDate = , AND MediaKind = 
   *
   * @param mediaDate_0 mediaDate_0
   * @param mediaKind_1 mediaKind_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice AchDeductMedia AchDeductMedia of List
   */
  public Slice<AchDeductMedia> mediaDateEq(int mediaDate_0, String mediaKind_1, int index, int limit, TitaVo... titaVo);

  /**
   * hold By AchDeductMedia
   * 
   * @param achDeductMediaId key
   * @param titaVo Variable-Length Argument
   * @return AchDeductMedia AchDeductMedia
   */
  public AchDeductMedia holdById(AchDeductMediaId achDeductMediaId, TitaVo... titaVo);

  /**
   * hold By AchDeductMedia
   * 
   * @param achDeductMedia key
   * @param titaVo Variable-Length Argument
   * @return AchDeductMedia AchDeductMedia
   */
  public AchDeductMedia holdById(AchDeductMedia achDeductMedia, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param achDeductMedia Entity
   * @param titaVo Variable-Length Argument
   * @return AchDeductMedia Entity
   * @throws DBException exception
   */
  public AchDeductMedia insert(AchDeductMedia achDeductMedia, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param achDeductMedia Entity
   * @param titaVo Variable-Length Argument
   * @return AchDeductMedia Entity
   * @throws DBException exception
   */
  public AchDeductMedia update(AchDeductMedia achDeductMedia, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param achDeductMedia Entity
   * @param titaVo Variable-Length Argument
   * @return AchDeductMedia Entity
   * @throws DBException exception
   */
  public AchDeductMedia update2(AchDeductMedia achDeductMedia, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param achDeductMedia Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(AchDeductMedia achDeductMedia, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param achDeductMedia Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<AchDeductMedia> achDeductMedia, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param achDeductMedia Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<AchDeductMedia> achDeductMedia, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param achDeductMedia Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<AchDeductMedia> achDeductMedia, TitaVo... titaVo) throws DBException;

}
