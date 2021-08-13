package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.LoanIfrsDp;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.LoanIfrsDpId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface LoanIfrsDpService {

  /**
   * findByPrimaryKey
   *
   * @param loanIfrsDpId PK
   * @param titaVo Variable-Length Argument
   * @return LoanIfrsDp LoanIfrsDp
   */
  public LoanIfrsDp findById(LoanIfrsDpId loanIfrsDpId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice LoanIfrsDp LoanIfrsDp of List
   */
  public Slice<LoanIfrsDp> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * hold By LoanIfrsDp
   * 
   * @param loanIfrsDpId key
   * @param titaVo Variable-Length Argument
   * @return LoanIfrsDp LoanIfrsDp
   */
  public LoanIfrsDp holdById(LoanIfrsDpId loanIfrsDpId, TitaVo... titaVo);

  /**
   * hold By LoanIfrsDp
   * 
   * @param loanIfrsDp key
   * @param titaVo Variable-Length Argument
   * @return LoanIfrsDp LoanIfrsDp
   */
  public LoanIfrsDp holdById(LoanIfrsDp loanIfrsDp, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param loanIfrsDp Entity
   * @param titaVo Variable-Length Argument
   * @return LoanIfrsDp Entity
   * @throws DBException exception
   */
  public LoanIfrsDp insert(LoanIfrsDp loanIfrsDp, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param loanIfrsDp Entity
   * @param titaVo Variable-Length Argument
   * @return LoanIfrsDp Entity
   * @throws DBException exception
   */
  public LoanIfrsDp update(LoanIfrsDp loanIfrsDp, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param loanIfrsDp Entity
   * @param titaVo Variable-Length Argument
   * @return LoanIfrsDp Entity
   * @throws DBException exception
   */
  public LoanIfrsDp update2(LoanIfrsDp loanIfrsDp, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param loanIfrsDp Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(LoanIfrsDp loanIfrsDp, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param loanIfrsDp Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<LoanIfrsDp> loanIfrsDp, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param loanIfrsDp Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<LoanIfrsDp> loanIfrsDp, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param loanIfrsDp Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<LoanIfrsDp> loanIfrsDp, TitaVo... titaVo) throws DBException;

  /**
   * Stored Procedure<br>
   * (月底日日終批次)維護 LoanIfrsDp IFRS9欄位清單4
   * @param  TBSDYF int
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_L7_LoanIfrsDp_Upd(int TBSDYF, String EmpNo, TitaVo... titaVo);

}
