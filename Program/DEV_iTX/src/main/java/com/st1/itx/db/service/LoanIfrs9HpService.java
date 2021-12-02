package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.LoanIfrs9Hp;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.LoanIfrs9HpId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface LoanIfrs9HpService {

  /**
   * findByPrimaryKey
   *
   * @param loanIfrs9HpId PK
   * @param titaVo Variable-Length Argument
   * @return LoanIfrs9Hp LoanIfrs9Hp
   */
  public LoanIfrs9Hp findById(LoanIfrs9HpId loanIfrs9HpId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice LoanIfrs9Hp LoanIfrs9Hp of List
   */
  public Slice<LoanIfrs9Hp> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * hold By LoanIfrs9Hp
   * 
   * @param loanIfrs9HpId key
   * @param titaVo Variable-Length Argument
   * @return LoanIfrs9Hp LoanIfrs9Hp
   */
  public LoanIfrs9Hp holdById(LoanIfrs9HpId loanIfrs9HpId, TitaVo... titaVo);

  /**
   * hold By LoanIfrs9Hp
   * 
   * @param loanIfrs9Hp key
   * @param titaVo Variable-Length Argument
   * @return LoanIfrs9Hp LoanIfrs9Hp
   */
  public LoanIfrs9Hp holdById(LoanIfrs9Hp loanIfrs9Hp, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param loanIfrs9Hp Entity
   * @param titaVo Variable-Length Argument
   * @return LoanIfrs9Hp Entity
   * @throws DBException exception
   */
  public LoanIfrs9Hp insert(LoanIfrs9Hp loanIfrs9Hp, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param loanIfrs9Hp Entity
   * @param titaVo Variable-Length Argument
   * @return LoanIfrs9Hp Entity
   * @throws DBException exception
   */
  public LoanIfrs9Hp update(LoanIfrs9Hp loanIfrs9Hp, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param loanIfrs9Hp Entity
   * @param titaVo Variable-Length Argument
   * @return LoanIfrs9Hp Entity
   * @throws DBException exception
   */
  public LoanIfrs9Hp update2(LoanIfrs9Hp loanIfrs9Hp, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param loanIfrs9Hp Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(LoanIfrs9Hp loanIfrs9Hp, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param loanIfrs9Hp Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<LoanIfrs9Hp> loanIfrs9Hp, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param loanIfrs9Hp Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<LoanIfrs9Hp> loanIfrs9Hp, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param loanIfrs9Hp Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<LoanIfrs9Hp> loanIfrs9Hp, TitaVo... titaVo) throws DBException;

  /**
   * Stored Procedure<br>
   * (月底日日終批次)維護 LoanIfrsHp IFRS9欄位清單8
   * @param  TBSDYF int
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_L7_LoanIfrs9Hp_Upd(int TBSDYF, String EmpNo, TitaVo... titaVo);

}
