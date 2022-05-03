package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.LoanCustRmk;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.LoanCustRmkId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface LoanCustRmkService {

  /**
   * findByPrimaryKey
   *
   * @param loanCustRmkId PK
   * @param titaVo Variable-Length Argument
   * @return LoanCustRmk LoanCustRmk
   */
  public LoanCustRmk findById(LoanCustRmkId loanCustRmkId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice LoanCustRmk LoanCustRmk of List
   */
  public Slice<LoanCustRmk> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * CustNo = 
   *
   * @param custNo_0 custNo_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice LoanCustRmk LoanCustRmk of List
   */
  public Slice<LoanCustRmk> findCustNo(int custNo_0, int index, int limit, TitaVo... titaVo);

  /**
   * CustNo = ,AND AcDate =  
   *
   * @param custNo_0 custNo_0
   * @param acDate_1 acDate_1
   * @param titaVo Variable-Length Argument
   * @return Slice LoanCustRmk LoanCustRmk of List
   */
  public LoanCustRmk maxRmkNoFirst(int custNo_0, int acDate_1, TitaVo... titaVo);

  /**
   * CustNo = ,AND FacmNo = ,AND BormNo = ,AND BorxNo =
   *
   * @param custNo_0 custNo_0
   * @param facmNo_1 facmNo_1
   * @param bormNo_2 bormNo_2
   * @param borxNo_3 borxNo_3
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice LoanCustRmk LoanCustRmk of List
   */
  public Slice<LoanCustRmk> BorxNoAll(int custNo_0, int facmNo_1, int bormNo_2, int borxNo_3, int index, int limit, TitaVo... titaVo);

  /**
   * hold By LoanCustRmk
   * 
   * @param loanCustRmkId key
   * @param titaVo Variable-Length Argument
   * @return LoanCustRmk LoanCustRmk
   */
  public LoanCustRmk holdById(LoanCustRmkId loanCustRmkId, TitaVo... titaVo);

  /**
   * hold By LoanCustRmk
   * 
   * @param loanCustRmk key
   * @param titaVo Variable-Length Argument
   * @return LoanCustRmk LoanCustRmk
   */
  public LoanCustRmk holdById(LoanCustRmk loanCustRmk, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param loanCustRmk Entity
   * @param titaVo Variable-Length Argument
   * @return LoanCustRmk Entity
   * @throws DBException exception
   */
  public LoanCustRmk insert(LoanCustRmk loanCustRmk, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param loanCustRmk Entity
   * @param titaVo Variable-Length Argument
   * @return LoanCustRmk Entity
   * @throws DBException exception
   */
  public LoanCustRmk update(LoanCustRmk loanCustRmk, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param loanCustRmk Entity
   * @param titaVo Variable-Length Argument
   * @return LoanCustRmk Entity
   * @throws DBException exception
   */
  public LoanCustRmk update2(LoanCustRmk loanCustRmk, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param loanCustRmk Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(LoanCustRmk loanCustRmk, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param loanCustRmk Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<LoanCustRmk> loanCustRmk, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param loanCustRmk Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<LoanCustRmk> loanCustRmk, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param loanCustRmk Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<LoanCustRmk> loanCustRmk, TitaVo... titaVo) throws DBException;

}
