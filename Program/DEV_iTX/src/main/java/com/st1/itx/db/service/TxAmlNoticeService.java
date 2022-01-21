package com.st1.itx.db.service;

import java.util.List;

import java.math.BigDecimal;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.TxAmlNotice;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.TxAmlNoticeId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface TxAmlNoticeService {

  /**
   * findByPrimaryKey
   *
   * @param txAmlNoticeId PK
   * @param titaVo Variable-Length Argument
   * @return TxAmlNotice TxAmlNotice
   */
  public TxAmlNotice findById(TxAmlNoticeId txAmlNoticeId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice TxAmlNotice TxAmlNotice of List
   */
  public Slice<TxAmlNotice> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * DataDt = ,AND CustKey =
   *
   * @param dataDt_0 dataDt_0
   * @param custKey_1 custKey_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice TxAmlNotice TxAmlNotice of List
   */
  public Slice<TxAmlNotice> processAll(int dataDt_0, String custKey_1, int index, int limit, TitaVo... titaVo);

  /**
   * DataDt =
   *
   * @param dataDt_0 dataDt_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice TxAmlNotice TxAmlNotice of List
   */
  public Slice<TxAmlNotice> dataDtAll(int dataDt_0, int index, int limit, TitaVo... titaVo);

  /**
   * ProcessDate = ,
   *
   * @param processDate_0 processDate_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice TxAmlNotice TxAmlNotice of List
   */
  public Slice<TxAmlNotice> processDateAll(int processDate_0, int index, int limit, TitaVo... titaVo);

  /**
   * hold By TxAmlNotice
   * 
   * @param txAmlNoticeId key
   * @param titaVo Variable-Length Argument
   * @return TxAmlNotice TxAmlNotice
   */
  public TxAmlNotice holdById(TxAmlNoticeId txAmlNoticeId, TitaVo... titaVo);

  /**
   * hold By TxAmlNotice
   * 
   * @param txAmlNotice key
   * @param titaVo Variable-Length Argument
   * @return TxAmlNotice TxAmlNotice
   */
  public TxAmlNotice holdById(TxAmlNotice txAmlNotice, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param txAmlNotice Entity
   * @param titaVo Variable-Length Argument
   * @return TxAmlNotice Entity
   * @throws DBException exception
   */
  public TxAmlNotice insert(TxAmlNotice txAmlNotice, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param txAmlNotice Entity
   * @param titaVo Variable-Length Argument
   * @return TxAmlNotice Entity
   * @throws DBException exception
   */
  public TxAmlNotice update(TxAmlNotice txAmlNotice, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param txAmlNotice Entity
   * @param titaVo Variable-Length Argument
   * @return TxAmlNotice Entity
   * @throws DBException exception
   */
  public TxAmlNotice update2(TxAmlNotice txAmlNotice, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param txAmlNotice Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(TxAmlNotice txAmlNotice, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param txAmlNotice Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<TxAmlNotice> txAmlNotice, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param txAmlNotice Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<TxAmlNotice> txAmlNotice, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param txAmlNotice Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<TxAmlNotice> txAmlNotice, TitaVo... titaVo) throws DBException;

}
