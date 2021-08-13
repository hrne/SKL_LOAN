package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.LoanIfrsAp;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.LoanIfrsApId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface LoanIfrsApService {

  /**
   * findByPrimaryKey
   *
   * @param loanIfrsApId PK
   * @param titaVo Variable-Length Argument
   * @return LoanIfrsAp LoanIfrsAp
   */
  public LoanIfrsAp findById(LoanIfrsApId loanIfrsApId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice LoanIfrsAp LoanIfrsAp of List
   */
  public Slice<LoanIfrsAp> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * hold By LoanIfrsAp
   * 
   * @param loanIfrsApId key
   * @param titaVo Variable-Length Argument
   * @return LoanIfrsAp LoanIfrsAp
   */
  public LoanIfrsAp holdById(LoanIfrsApId loanIfrsApId, TitaVo... titaVo);

  /**
   * hold By LoanIfrsAp
   * 
   * @param loanIfrsAp key
   * @param titaVo Variable-Length Argument
   * @return LoanIfrsAp LoanIfrsAp
   */
  public LoanIfrsAp holdById(LoanIfrsAp loanIfrsAp, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param loanIfrsAp Entity
   * @param titaVo Variable-Length Argument
   * @return LoanIfrsAp Entity
   * @throws DBException exception
   */
  public LoanIfrsAp insert(LoanIfrsAp loanIfrsAp, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param loanIfrsAp Entity
   * @param titaVo Variable-Length Argument
   * @return LoanIfrsAp Entity
   * @throws DBException exception
   */
  public LoanIfrsAp update(LoanIfrsAp loanIfrsAp, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param loanIfrsAp Entity
   * @param titaVo Variable-Length Argument
   * @return LoanIfrsAp Entity
   * @throws DBException exception
   */
  public LoanIfrsAp update2(LoanIfrsAp loanIfrsAp, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param loanIfrsAp Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(LoanIfrsAp loanIfrsAp, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param loanIfrsAp Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<LoanIfrsAp> loanIfrsAp, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param loanIfrsAp Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<LoanIfrsAp> loanIfrsAp, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param loanIfrsAp Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<LoanIfrsAp> loanIfrsAp, TitaVo... titaVo) throws DBException;

  /**
   * Stored Procedure<br>
   * (月底日日終批次)維護 LoanIfrsAp IFRS9欄位清單1
   * @param  TBSDYF int
   * @param  EmpNo String
   * @param  NewAcFg int
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_L7_LoanIfrsAp_Upd(int TBSDYF, String EmpNo, int NewAcFg, TitaVo... titaVo);

}
