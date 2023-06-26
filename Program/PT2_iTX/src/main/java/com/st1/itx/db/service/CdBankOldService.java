package com.st1.itx.db.service;

import java.util.List;

import java.math.BigDecimal;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CdBankOld;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.CdBankOldId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CdBankOldService {

  /**
   * findByPrimaryKey
   *
   * @param cdBankOldId PK
   * @param titaVo Variable-Length Argument
   * @return CdBankOld CdBankOld
   */
  public CdBankOld findById(CdBankOldId cdBankOldId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdBankOld CdBankOld of List
   */
  public Slice<CdBankOld> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * BankCode %
   *
   * @param bankCode_0 bankCode_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdBankOld CdBankOld of List
   */
  public Slice<CdBankOld> bankCodeLike(String bankCode_0, int index, int limit, TitaVo... titaVo);

  /**
   * BankCode % ,AND BranchCode %
   *
   * @param bankCode_0 bankCode_0
   * @param branchCode_1 branchCode_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdBankOld CdBankOld of List
   */
  public Slice<CdBankOld> branchCodeLike(String bankCode_0, String branchCode_1, int index, int limit, TitaVo... titaVo);

  /**
   * BankItem %
   *
   * @param bankItem_0 bankItem_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdBankOld CdBankOld of List
   */
  public Slice<CdBankOld> bankItemLike(String bankItem_0, int index, int limit, TitaVo... titaVo);

  /**
   * hold By CdBankOld
   * 
   * @param cdBankOldId key
   * @param titaVo Variable-Length Argument
   * @return CdBankOld CdBankOld
   */
  public CdBankOld holdById(CdBankOldId cdBankOldId, TitaVo... titaVo);

  /**
   * hold By CdBankOld
   * 
   * @param cdBankOld key
   * @param titaVo Variable-Length Argument
   * @return CdBankOld CdBankOld
   */
  public CdBankOld holdById(CdBankOld cdBankOld, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param cdBankOld Entity
   * @param titaVo Variable-Length Argument
   * @return CdBankOld Entity
   * @throws DBException exception
   */
  public CdBankOld insert(CdBankOld cdBankOld, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param cdBankOld Entity
   * @param titaVo Variable-Length Argument
   * @return CdBankOld Entity
   * @throws DBException exception
   */
  public CdBankOld update(CdBankOld cdBankOld, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param cdBankOld Entity
   * @param titaVo Variable-Length Argument
   * @return CdBankOld Entity
   * @throws DBException exception
   */
  public CdBankOld update2(CdBankOld cdBankOld, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param cdBankOld Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(CdBankOld cdBankOld, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param cdBankOld Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<CdBankOld> cdBankOld, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param cdBankOld Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<CdBankOld> cdBankOld, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param cdBankOld Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<CdBankOld> cdBankOld, TitaVo... titaVo) throws DBException;

}
