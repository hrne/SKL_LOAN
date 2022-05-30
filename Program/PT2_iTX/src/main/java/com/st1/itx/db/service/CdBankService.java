package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CdBank;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.CdBankId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CdBankService {

  /**
   * findByPrimaryKey
   *
   * @param cdBankId PK
   * @param titaVo Variable-Length Argument
   * @return CdBank CdBank
   */
  public CdBank findById(CdBankId cdBankId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdBank CdBank of List
   */
  public Slice<CdBank> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * BankCode %
   *
   * @param bankCode_0 bankCode_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdBank CdBank of List
   */
  public Slice<CdBank> bankCodeLike(String bankCode_0, int index, int limit, TitaVo... titaVo);

  /**
   * BankCode % ,AND BranchCode %
   *
   * @param bankCode_0 bankCode_0
   * @param branchCode_1 branchCode_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdBank CdBank of List
   */
  public Slice<CdBank> branchCodeLike(String bankCode_0, String branchCode_1, int index, int limit, TitaVo... titaVo);

  /**
   * BankCode % ,AND BranchCode % ,AND BankItem %
   *
   * @param bankCode_0 bankCode_0
   * @param branchCode_1 branchCode_1
   * @param bankItem_2 bankItem_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdBank CdBank of List
   */
  public Slice<CdBank> bankItemLike(String bankCode_0, String branchCode_1, String bankItem_2, int index, int limit, TitaVo... titaVo);

  /**
   * hold By CdBank
   * 
   * @param cdBankId key
   * @param titaVo Variable-Length Argument
   * @return CdBank CdBank
   */
  public CdBank holdById(CdBankId cdBankId, TitaVo... titaVo);

  /**
   * hold By CdBank
   * 
   * @param cdBank key
   * @param titaVo Variable-Length Argument
   * @return CdBank CdBank
   */
  public CdBank holdById(CdBank cdBank, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param cdBank Entity
   * @param titaVo Variable-Length Argument
   * @return CdBank Entity
   * @throws DBException exception
   */
  public CdBank insert(CdBank cdBank, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param cdBank Entity
   * @param titaVo Variable-Length Argument
   * @return CdBank Entity
   * @throws DBException exception
   */
  public CdBank update(CdBank cdBank, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param cdBank Entity
   * @param titaVo Variable-Length Argument
   * @return CdBank Entity
   * @throws DBException exception
   */
  public CdBank update2(CdBank cdBank, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param cdBank Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(CdBank cdBank, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param cdBank Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<CdBank> cdBank, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param cdBank Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<CdBank> cdBank, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param cdBank Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<CdBank> cdBank, TitaVo... titaVo) throws DBException;

}
