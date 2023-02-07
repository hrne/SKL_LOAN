package com.st1.itx.db.service;

import java.util.List;

import java.math.BigDecimal;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.TxAmlRatingAppl;
import org.springframework.data.domain.Slice;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface TxAmlRatingApplService {

  /**
   * findByPrimaryKey
   *
   * @param logNo PK
   * @param titaVo Variable-Length Argument
   * @return TxAmlRatingAppl TxAmlRatingAppl
   */
  public TxAmlRatingAppl findById(Long logNo, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice TxAmlRatingAppl TxAmlRatingAppl of List
   */
  public Slice<TxAmlRatingAppl> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * CaseNo = ,
   *
   * @param caseNo_0 caseNo_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice TxAmlRatingAppl TxAmlRatingAppl of List
   */
  public Slice<TxAmlRatingAppl> findByCaseNo(String caseNo_0, int index, int limit, TitaVo... titaVo);

  /**
   * hold By TxAmlRatingAppl
   * 
   * @param logNo key
   * @param titaVo Variable-Length Argument
   * @return TxAmlRatingAppl TxAmlRatingAppl
   */
  public TxAmlRatingAppl holdById(Long logNo, TitaVo... titaVo);

  /**
   * hold By TxAmlRatingAppl
   * 
   * @param txAmlRatingAppl key
   * @param titaVo Variable-Length Argument
   * @return TxAmlRatingAppl TxAmlRatingAppl
   */
  public TxAmlRatingAppl holdById(TxAmlRatingAppl txAmlRatingAppl, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param txAmlRatingAppl Entity
   * @param titaVo Variable-Length Argument
   * @return TxAmlRatingAppl Entity
   * @throws DBException exception
   */
  public TxAmlRatingAppl insert(TxAmlRatingAppl txAmlRatingAppl, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param txAmlRatingAppl Entity
   * @param titaVo Variable-Length Argument
   * @return TxAmlRatingAppl Entity
   * @throws DBException exception
   */
  public TxAmlRatingAppl update(TxAmlRatingAppl txAmlRatingAppl, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param txAmlRatingAppl Entity
   * @param titaVo Variable-Length Argument
   * @return TxAmlRatingAppl Entity
   * @throws DBException exception
   */
  public TxAmlRatingAppl update2(TxAmlRatingAppl txAmlRatingAppl, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param txAmlRatingAppl Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(TxAmlRatingAppl txAmlRatingAppl, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param txAmlRatingAppl Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<TxAmlRatingAppl> txAmlRatingAppl, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param txAmlRatingAppl Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<TxAmlRatingAppl> txAmlRatingAppl, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param txAmlRatingAppl Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<TxAmlRatingAppl> txAmlRatingAppl, TitaVo... titaVo) throws DBException;

}
