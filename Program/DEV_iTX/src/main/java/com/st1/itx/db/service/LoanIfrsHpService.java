package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.LoanIfrsHp;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.LoanIfrsHpId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface LoanIfrsHpService {

  /**
   * findByPrimaryKey
   *
   * @param loanIfrsHpId PK
   * @param titaVo Variable-Length Argument
   * @return LoanIfrsHp LoanIfrsHp
   */
  public LoanIfrsHp findById(LoanIfrsHpId loanIfrsHpId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice LoanIfrsHp LoanIfrsHp of List
   */
  public Slice<LoanIfrsHp> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * hold By LoanIfrsHp
   * 
   * @param loanIfrsHpId key
   * @param titaVo Variable-Length Argument
   * @return LoanIfrsHp LoanIfrsHp
   */
  public LoanIfrsHp holdById(LoanIfrsHpId loanIfrsHpId, TitaVo... titaVo);

  /**
   * hold By LoanIfrsHp
   * 
   * @param loanIfrsHp key
   * @param titaVo Variable-Length Argument
   * @return LoanIfrsHp LoanIfrsHp
   */
  public LoanIfrsHp holdById(LoanIfrsHp loanIfrsHp, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param loanIfrsHp Entity
   * @param titaVo Variable-Length Argument
   * @return LoanIfrsHp Entity
   * @throws DBException exception
   */
  public LoanIfrsHp insert(LoanIfrsHp loanIfrsHp, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param loanIfrsHp Entity
   * @param titaVo Variable-Length Argument
   * @return LoanIfrsHp Entity
   * @throws DBException exception
   */
  public LoanIfrsHp update(LoanIfrsHp loanIfrsHp, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param loanIfrsHp Entity
   * @param titaVo Variable-Length Argument
   * @return LoanIfrsHp Entity
   * @throws DBException exception
   */
  public LoanIfrsHp update2(LoanIfrsHp loanIfrsHp, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param loanIfrsHp Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(LoanIfrsHp loanIfrsHp, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param loanIfrsHp Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<LoanIfrsHp> loanIfrsHp, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param loanIfrsHp Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<LoanIfrsHp> loanIfrsHp, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param loanIfrsHp Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<LoanIfrsHp> loanIfrsHp, TitaVo... titaVo) throws DBException;

  /**
   * Stored Procedure<br>
   * (月底日日終批次)維護 LoanIfrsHp IFRS9欄位清單8
   * @param  TBSDYF int
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_L7_LoanIfrsHp_Upd(int TBSDYF, String EmpNo, TitaVo... titaVo);

}
