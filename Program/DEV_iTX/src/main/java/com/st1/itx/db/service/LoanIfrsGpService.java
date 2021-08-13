package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.LoanIfrsGp;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.LoanIfrsGpId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface LoanIfrsGpService {

  /**
   * findByPrimaryKey
   *
   * @param loanIfrsGpId PK
   * @param titaVo Variable-Length Argument
   * @return LoanIfrsGp LoanIfrsGp
   */
  public LoanIfrsGp findById(LoanIfrsGpId loanIfrsGpId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice LoanIfrsGp LoanIfrsGp of List
   */
  public Slice<LoanIfrsGp> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * hold By LoanIfrsGp
   * 
   * @param loanIfrsGpId key
   * @param titaVo Variable-Length Argument
   * @return LoanIfrsGp LoanIfrsGp
   */
  public LoanIfrsGp holdById(LoanIfrsGpId loanIfrsGpId, TitaVo... titaVo);

  /**
   * hold By LoanIfrsGp
   * 
   * @param loanIfrsGp key
   * @param titaVo Variable-Length Argument
   * @return LoanIfrsGp LoanIfrsGp
   */
  public LoanIfrsGp holdById(LoanIfrsGp loanIfrsGp, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param loanIfrsGp Entity
   * @param titaVo Variable-Length Argument
   * @return LoanIfrsGp Entity
   * @throws DBException exception
   */
  public LoanIfrsGp insert(LoanIfrsGp loanIfrsGp, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param loanIfrsGp Entity
   * @param titaVo Variable-Length Argument
   * @return LoanIfrsGp Entity
   * @throws DBException exception
   */
  public LoanIfrsGp update(LoanIfrsGp loanIfrsGp, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param loanIfrsGp Entity
   * @param titaVo Variable-Length Argument
   * @return LoanIfrsGp Entity
   * @throws DBException exception
   */
  public LoanIfrsGp update2(LoanIfrsGp loanIfrsGp, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param loanIfrsGp Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(LoanIfrsGp loanIfrsGp, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param loanIfrsGp Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<LoanIfrsGp> loanIfrsGp, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param loanIfrsGp Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<LoanIfrsGp> loanIfrsGp, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param loanIfrsGp Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<LoanIfrsGp> loanIfrsGp, TitaVo... titaVo) throws DBException;

  /**
   * Stored Procedure<br>
   * (月底日日終批次)維護 LoanIfrsGp IFRS9資料欄位清單7
   * @param  TBSDYF int
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_L7_LoanIfrsGp_Upd(int TBSDYF, String EmpNo, TitaVo... titaVo);

}
