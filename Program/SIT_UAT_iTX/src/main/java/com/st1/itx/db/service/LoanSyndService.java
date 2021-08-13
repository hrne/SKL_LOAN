package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.LoanSynd;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.LoanSyndId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface LoanSyndService {

  /**
   * findByPrimaryKey
   *
   * @param loanSyndId PK
   * @param titaVo Variable-Length Argument
   * @return LoanSynd LoanSynd
   */
  public LoanSynd findById(LoanSyndId loanSyndId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice LoanSynd LoanSynd of List
   */
  public Slice<LoanSynd> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * CustNo &gt;= ,AND CustNo &lt;= ,AND LeadingBank % ,AND SigningDate &gt;= ,AND SigningDate &lt;= ,AND DrawdownStartDate &gt;= ,AND DrawdownStartDate &lt;= ,AND DrawdownEndDate &gt;= ,AND DrawdownEndDate &lt;= 
   *
   * @param custNo_0 custNo_0
   * @param custNo_1 custNo_1
   * @param leadingBank_2 leadingBank_2
   * @param signingDate_3 signingDate_3
   * @param signingDate_4 signingDate_4
   * @param drawdownStartDate_5 drawdownStartDate_5
   * @param drawdownStartDate_6 drawdownStartDate_6
   * @param drawdownEndDate_7 drawdownEndDate_7
   * @param drawdownEndDate_8 drawdownEndDate_8
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice LoanSynd LoanSynd of List
   */
  public Slice<LoanSynd> syndCustNoRange(int custNo_0, int custNo_1, String leadingBank_2, int signingDate_3, int signingDate_4, int drawdownStartDate_5, int drawdownStartDate_6, int drawdownEndDate_7, int drawdownEndDate_8, int index, int limit, TitaVo... titaVo);

  /**
   * hold By LoanSynd
   * 
   * @param loanSyndId key
   * @param titaVo Variable-Length Argument
   * @return LoanSynd LoanSynd
   */
  public LoanSynd holdById(LoanSyndId loanSyndId, TitaVo... titaVo);

  /**
   * hold By LoanSynd
   * 
   * @param loanSynd key
   * @param titaVo Variable-Length Argument
   * @return LoanSynd LoanSynd
   */
  public LoanSynd holdById(LoanSynd loanSynd, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param loanSynd Entity
   * @param titaVo Variable-Length Argument
   * @return LoanSynd Entity
   * @throws DBException exception
   */
  public LoanSynd insert(LoanSynd loanSynd, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param loanSynd Entity
   * @param titaVo Variable-Length Argument
   * @return LoanSynd Entity
   * @throws DBException exception
   */
  public LoanSynd update(LoanSynd loanSynd, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param loanSynd Entity
   * @param titaVo Variable-Length Argument
   * @return LoanSynd Entity
   * @throws DBException exception
   */
  public LoanSynd update2(LoanSynd loanSynd, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param loanSynd Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(LoanSynd loanSynd, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param loanSynd Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<LoanSynd> loanSynd, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param loanSynd Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<LoanSynd> loanSynd, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param loanSynd Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<LoanSynd> loanSynd, TitaVo... titaVo) throws DBException;

}
