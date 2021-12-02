package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.LoanIfrs9Cp;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.LoanIfrs9CpId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface LoanIfrs9CpService {

  /**
   * findByPrimaryKey
   *
   * @param loanIfrs9CpId PK
   * @param titaVo Variable-Length Argument
   * @return LoanIfrs9Cp LoanIfrs9Cp
   */
  public LoanIfrs9Cp findById(LoanIfrs9CpId loanIfrs9CpId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice LoanIfrs9Cp LoanIfrs9Cp of List
   */
  public Slice<LoanIfrs9Cp> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * hold By LoanIfrs9Cp
   * 
   * @param loanIfrs9CpId key
   * @param titaVo Variable-Length Argument
   * @return LoanIfrs9Cp LoanIfrs9Cp
   */
  public LoanIfrs9Cp holdById(LoanIfrs9CpId loanIfrs9CpId, TitaVo... titaVo);

  /**
   * hold By LoanIfrs9Cp
   * 
   * @param loanIfrs9Cp key
   * @param titaVo Variable-Length Argument
   * @return LoanIfrs9Cp LoanIfrs9Cp
   */
  public LoanIfrs9Cp holdById(LoanIfrs9Cp loanIfrs9Cp, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param loanIfrs9Cp Entity
   * @param titaVo Variable-Length Argument
   * @return LoanIfrs9Cp Entity
   * @throws DBException exception
   */
  public LoanIfrs9Cp insert(LoanIfrs9Cp loanIfrs9Cp, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param loanIfrs9Cp Entity
   * @param titaVo Variable-Length Argument
   * @return LoanIfrs9Cp Entity
   * @throws DBException exception
   */
  public LoanIfrs9Cp update(LoanIfrs9Cp loanIfrs9Cp, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param loanIfrs9Cp Entity
   * @param titaVo Variable-Length Argument
   * @return LoanIfrs9Cp Entity
   * @throws DBException exception
   */
  public LoanIfrs9Cp update2(LoanIfrs9Cp loanIfrs9Cp, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param loanIfrs9Cp Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(LoanIfrs9Cp loanIfrs9Cp, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param loanIfrs9Cp Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<LoanIfrs9Cp> loanIfrs9Cp, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param loanIfrs9Cp Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<LoanIfrs9Cp> loanIfrs9Cp, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param loanIfrs9Cp Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<LoanIfrs9Cp> loanIfrs9Cp, TitaVo... titaVo) throws DBException;

  /**
   * Stored Procedure<br>
   * (月底日日終批次)維護 LoanIfrsCp IFRS9資料欄位清單3
   * @param  TBSDYF int
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_L7_LoanIfrs9Cp_Upd(int TBSDYF, String EmpNo, TitaVo... titaVo);

}
