package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.LoanIfrs9Fp;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.LoanIfrs9FpId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface LoanIfrs9FpService {

  /**
   * findByPrimaryKey
   *
   * @param loanIfrs9FpId PK
   * @param titaVo Variable-Length Argument
   * @return LoanIfrs9Fp LoanIfrs9Fp
   */
  public LoanIfrs9Fp findById(LoanIfrs9FpId loanIfrs9FpId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice LoanIfrs9Fp LoanIfrs9Fp of List
   */
  public Slice<LoanIfrs9Fp> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * hold By LoanIfrs9Fp
   * 
   * @param loanIfrs9FpId key
   * @param titaVo Variable-Length Argument
   * @return LoanIfrs9Fp LoanIfrs9Fp
   */
  public LoanIfrs9Fp holdById(LoanIfrs9FpId loanIfrs9FpId, TitaVo... titaVo);

  /**
   * hold By LoanIfrs9Fp
   * 
   * @param loanIfrs9Fp key
   * @param titaVo Variable-Length Argument
   * @return LoanIfrs9Fp LoanIfrs9Fp
   */
  public LoanIfrs9Fp holdById(LoanIfrs9Fp loanIfrs9Fp, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param loanIfrs9Fp Entity
   * @param titaVo Variable-Length Argument
   * @return LoanIfrs9Fp Entity
   * @throws DBException exception
   */
  public LoanIfrs9Fp insert(LoanIfrs9Fp loanIfrs9Fp, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param loanIfrs9Fp Entity
   * @param titaVo Variable-Length Argument
   * @return LoanIfrs9Fp Entity
   * @throws DBException exception
   */
  public LoanIfrs9Fp update(LoanIfrs9Fp loanIfrs9Fp, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param loanIfrs9Fp Entity
   * @param titaVo Variable-Length Argument
   * @return LoanIfrs9Fp Entity
   * @throws DBException exception
   */
  public LoanIfrs9Fp update2(LoanIfrs9Fp loanIfrs9Fp, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param loanIfrs9Fp Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(LoanIfrs9Fp loanIfrs9Fp, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param loanIfrs9Fp Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<LoanIfrs9Fp> loanIfrs9Fp, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param loanIfrs9Fp Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<LoanIfrs9Fp> loanIfrs9Fp, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param loanIfrs9Fp Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<LoanIfrs9Fp> loanIfrs9Fp, TitaVo... titaVo) throws DBException;

  /**
   * Stored Procedure<br>
   * (月底日日終批次)維護 LoanIfrsFp IFRS9資料欄位清單6
   * @param  TBSDYF int
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_L7_LoanIfrs9Fp_Upd(int TBSDYF, String EmpNo, TitaVo... titaVo);

}
