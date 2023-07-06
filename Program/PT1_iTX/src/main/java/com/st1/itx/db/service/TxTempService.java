package com.st1.itx.db.service;

import java.util.List;

import java.math.BigDecimal;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.TxTemp;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.TxTempId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface TxTempService {

  /**
   * findByPrimaryKey
   *
   * @param txTempId PK
   * @param titaVo Variable-Length Argument
   * @return TxTemp TxTemp
   */
  public TxTemp findById(TxTempId txTempId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice TxTemp TxTemp of List
   */
  public Slice<TxTemp> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * Entdy = ,AND Kinbr = ,AND TlrNo = ,AND TxtNo = 
   *
   * @param entdy_0 entdy_0
   * @param kinbr_1 kinbr_1
   * @param tlrNo_2 tlrNo_2
   * @param txtNo_3 txtNo_3
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice TxTemp TxTemp of List
   */
  public Slice<TxTemp> txTempTxtNoEq(int entdy_0, String kinbr_1, String tlrNo_2, String txtNo_3, int index, int limit, TitaVo... titaVo);

  /**
   * TlrNo = 
   *
   * @param tlrNo_0 tlrNo_0
   * @param titaVo Variable-Length Argument
   * @return Slice TxTemp TxTemp of List
   */
  public TxTemp txtNoLastFirst(String tlrNo_0, TitaVo... titaVo);

  /**
   * SeqNo =
   *
   * @param seqNo_0 seqNo_0
   * @param titaVo Variable-Length Argument
   * @return Slice TxTemp TxTemp of List
   */
  public TxTemp findSeqNoFirst(String seqNo_0, TitaVo... titaVo);

  /**
   * hold By TxTemp
   * 
   * @param txTempId key
   * @param titaVo Variable-Length Argument
   * @return TxTemp TxTemp
   */
  public TxTemp holdById(TxTempId txTempId, TitaVo... titaVo);

  /**
   * hold By TxTemp
   * 
   * @param txTemp key
   * @param titaVo Variable-Length Argument
   * @return TxTemp TxTemp
   */
  public TxTemp holdById(TxTemp txTemp, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param txTemp Entity
   * @param titaVo Variable-Length Argument
   * @return TxTemp Entity
   * @throws DBException exception
   */
  public TxTemp insert(TxTemp txTemp, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param txTemp Entity
   * @param titaVo Variable-Length Argument
   * @return TxTemp Entity
   * @throws DBException exception
   */
  public TxTemp update(TxTemp txTemp, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param txTemp Entity
   * @param titaVo Variable-Length Argument
   * @return TxTemp Entity
   * @throws DBException exception
   */
  public TxTemp update2(TxTemp txTemp, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param txTemp Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(TxTemp txTemp, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param txTemp Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<TxTemp> txTemp, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param txTemp Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<TxTemp> txTemp, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param txTemp Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<TxTemp> txTemp, TitaVo... titaVo) throws DBException;

}
