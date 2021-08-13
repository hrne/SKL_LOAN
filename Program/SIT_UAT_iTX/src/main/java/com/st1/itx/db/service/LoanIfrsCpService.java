package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.LoanIfrsCp;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.LoanIfrsCpId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface LoanIfrsCpService {

  /**
   * findByPrimaryKey
   *
   * @param loanIfrsCpId PK
   * @param titaVo Variable-Length Argument
   * @return LoanIfrsCp LoanIfrsCp
   */
  public LoanIfrsCp findById(LoanIfrsCpId loanIfrsCpId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice LoanIfrsCp LoanIfrsCp of List
   */
  public Slice<LoanIfrsCp> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * hold By LoanIfrsCp
   * 
   * @param loanIfrsCpId key
   * @param titaVo Variable-Length Argument
   * @return LoanIfrsCp LoanIfrsCp
   */
  public LoanIfrsCp holdById(LoanIfrsCpId loanIfrsCpId, TitaVo... titaVo);

  /**
   * hold By LoanIfrsCp
   * 
   * @param loanIfrsCp key
   * @param titaVo Variable-Length Argument
   * @return LoanIfrsCp LoanIfrsCp
   */
  public LoanIfrsCp holdById(LoanIfrsCp loanIfrsCp, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param loanIfrsCp Entity
   * @param titaVo Variable-Length Argument
   * @return LoanIfrsCp Entity
   * @throws DBException exception
   */
  public LoanIfrsCp insert(LoanIfrsCp loanIfrsCp, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param loanIfrsCp Entity
   * @param titaVo Variable-Length Argument
   * @return LoanIfrsCp Entity
   * @throws DBException exception
   */
  public LoanIfrsCp update(LoanIfrsCp loanIfrsCp, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param loanIfrsCp Entity
   * @param titaVo Variable-Length Argument
   * @return LoanIfrsCp Entity
   * @throws DBException exception
   */
  public LoanIfrsCp update2(LoanIfrsCp loanIfrsCp, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param loanIfrsCp Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(LoanIfrsCp loanIfrsCp, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param loanIfrsCp Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<LoanIfrsCp> loanIfrsCp, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param loanIfrsCp Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<LoanIfrsCp> loanIfrsCp, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param loanIfrsCp Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<LoanIfrsCp> loanIfrsCp, TitaVo... titaVo) throws DBException;

  /**
   * Stored Procedure<br>
   * (月底日日終批次)維護 LoanIfrsCp IFRS9資料欄位清單3
   * @param  TBSDYF int
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_L7_LoanIfrsCp_Upd(int TBSDYF, String EmpNo, TitaVo... titaVo);

}
