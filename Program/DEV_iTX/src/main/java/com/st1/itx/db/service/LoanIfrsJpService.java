package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.LoanIfrsJp;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.LoanIfrsJpId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface LoanIfrsJpService {

  /**
   * findByPrimaryKey
   *
   * @param loanIfrsJpId PK
   * @param titaVo Variable-Length Argument
   * @return LoanIfrsJp LoanIfrsJp
   */
  public LoanIfrsJp findById(LoanIfrsJpId loanIfrsJpId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice LoanIfrsJp LoanIfrsJp of List
   */
  public Slice<LoanIfrsJp> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * hold By LoanIfrsJp
   * 
   * @param loanIfrsJpId key
   * @param titaVo Variable-Length Argument
   * @return LoanIfrsJp LoanIfrsJp
   */
  public LoanIfrsJp holdById(LoanIfrsJpId loanIfrsJpId, TitaVo... titaVo);

  /**
   * hold By LoanIfrsJp
   * 
   * @param loanIfrsJp key
   * @param titaVo Variable-Length Argument
   * @return LoanIfrsJp LoanIfrsJp
   */
  public LoanIfrsJp holdById(LoanIfrsJp loanIfrsJp, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param loanIfrsJp Entity
   * @param titaVo Variable-Length Argument
   * @return LoanIfrsJp Entity
   * @throws DBException exception
   */
  public LoanIfrsJp insert(LoanIfrsJp loanIfrsJp, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param loanIfrsJp Entity
   * @param titaVo Variable-Length Argument
   * @return LoanIfrsJp Entity
   * @throws DBException exception
   */
  public LoanIfrsJp update(LoanIfrsJp loanIfrsJp, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param loanIfrsJp Entity
   * @param titaVo Variable-Length Argument
   * @return LoanIfrsJp Entity
   * @throws DBException exception
   */
  public LoanIfrsJp update2(LoanIfrsJp loanIfrsJp, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param loanIfrsJp Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(LoanIfrsJp loanIfrsJp, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param loanIfrsJp Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<LoanIfrsJp> loanIfrsJp, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param loanIfrsJp Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<LoanIfrsJp> loanIfrsJp, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param loanIfrsJp Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<LoanIfrsJp> loanIfrsJp, TitaVo... titaVo) throws DBException;

  /**
   * Stored Procedure<br>
   * (月底日日終批次)維護 LoanIfrsJp IFRS9欄位清單10
   * @param  TBSDYF int
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_L7_LoanIfrsJp_Upd(int TBSDYF, String EmpNo, TitaVo... titaVo);

}
