package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.LoanIfrs9Dp;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.LoanIfrs9DpId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface LoanIfrs9DpService {

  /**
   * findByPrimaryKey
   *
   * @param loanIfrs9DpId PK
   * @param titaVo Variable-Length Argument
   * @return LoanIfrs9Dp LoanIfrs9Dp
   */
  public LoanIfrs9Dp findById(LoanIfrs9DpId loanIfrs9DpId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice LoanIfrs9Dp LoanIfrs9Dp of List
   */
  public Slice<LoanIfrs9Dp> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * hold By LoanIfrs9Dp
   * 
   * @param loanIfrs9DpId key
   * @param titaVo Variable-Length Argument
   * @return LoanIfrs9Dp LoanIfrs9Dp
   */
  public LoanIfrs9Dp holdById(LoanIfrs9DpId loanIfrs9DpId, TitaVo... titaVo);

  /**
   * hold By LoanIfrs9Dp
   * 
   * @param loanIfrs9Dp key
   * @param titaVo Variable-Length Argument
   * @return LoanIfrs9Dp LoanIfrs9Dp
   */
  public LoanIfrs9Dp holdById(LoanIfrs9Dp loanIfrs9Dp, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param loanIfrs9Dp Entity
   * @param titaVo Variable-Length Argument
   * @return LoanIfrs9Dp Entity
   * @throws DBException exception
   */
  public LoanIfrs9Dp insert(LoanIfrs9Dp loanIfrs9Dp, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param loanIfrs9Dp Entity
   * @param titaVo Variable-Length Argument
   * @return LoanIfrs9Dp Entity
   * @throws DBException exception
   */
  public LoanIfrs9Dp update(LoanIfrs9Dp loanIfrs9Dp, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param loanIfrs9Dp Entity
   * @param titaVo Variable-Length Argument
   * @return LoanIfrs9Dp Entity
   * @throws DBException exception
   */
  public LoanIfrs9Dp update2(LoanIfrs9Dp loanIfrs9Dp, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param loanIfrs9Dp Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(LoanIfrs9Dp loanIfrs9Dp, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param loanIfrs9Dp Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<LoanIfrs9Dp> loanIfrs9Dp, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param loanIfrs9Dp Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<LoanIfrs9Dp> loanIfrs9Dp, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param loanIfrs9Dp Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<LoanIfrs9Dp> loanIfrs9Dp, TitaVo... titaVo) throws DBException;

  /**
   * Stored Procedure<br>
   * (月底日日終批次)維護 LoanIfrsDp IFRS9欄位清單4
   * @param  TBSDYF int
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_L7_LoanIfrs9Dp_Upd(int TBSDYF, String EmpNo, TitaVo... titaVo);

}
