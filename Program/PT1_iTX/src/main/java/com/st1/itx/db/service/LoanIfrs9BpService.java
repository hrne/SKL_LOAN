package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.LoanIfrs9Bp;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.LoanIfrs9BpId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface LoanIfrs9BpService {

  /**
   * findByPrimaryKey
   *
   * @param loanIfrs9BpId PK
   * @param titaVo Variable-Length Argument
   * @return LoanIfrs9Bp LoanIfrs9Bp
   */
  public LoanIfrs9Bp findById(LoanIfrs9BpId loanIfrs9BpId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice LoanIfrs9Bp LoanIfrs9Bp of List
   */
  public Slice<LoanIfrs9Bp> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * hold By LoanIfrs9Bp
   * 
   * @param loanIfrs9BpId key
   * @param titaVo Variable-Length Argument
   * @return LoanIfrs9Bp LoanIfrs9Bp
   */
  public LoanIfrs9Bp holdById(LoanIfrs9BpId loanIfrs9BpId, TitaVo... titaVo);

  /**
   * hold By LoanIfrs9Bp
   * 
   * @param loanIfrs9Bp key
   * @param titaVo Variable-Length Argument
   * @return LoanIfrs9Bp LoanIfrs9Bp
   */
  public LoanIfrs9Bp holdById(LoanIfrs9Bp loanIfrs9Bp, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param loanIfrs9Bp Entity
   * @param titaVo Variable-Length Argument
   * @return LoanIfrs9Bp Entity
   * @throws DBException exception
   */
  public LoanIfrs9Bp insert(LoanIfrs9Bp loanIfrs9Bp, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param loanIfrs9Bp Entity
   * @param titaVo Variable-Length Argument
   * @return LoanIfrs9Bp Entity
   * @throws DBException exception
   */
  public LoanIfrs9Bp update(LoanIfrs9Bp loanIfrs9Bp, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param loanIfrs9Bp Entity
   * @param titaVo Variable-Length Argument
   * @return LoanIfrs9Bp Entity
   * @throws DBException exception
   */
  public LoanIfrs9Bp update2(LoanIfrs9Bp loanIfrs9Bp, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param loanIfrs9Bp Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(LoanIfrs9Bp loanIfrs9Bp, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param loanIfrs9Bp Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<LoanIfrs9Bp> loanIfrs9Bp, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param loanIfrs9Bp Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<LoanIfrs9Bp> loanIfrs9Bp, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param loanIfrs9Bp Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<LoanIfrs9Bp> loanIfrs9Bp, TitaVo... titaVo) throws DBException;

  /**
   * Stored Procedure<br>
   * (月底日日終批次)維護 LoanIfrsBp IFRS9資料欄位清單2
   * @param  TBSDYF int
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_L7_LoanIfrs9Bp_Upd(int TBSDYF, String EmpNo, TitaVo... titaVo);

}
