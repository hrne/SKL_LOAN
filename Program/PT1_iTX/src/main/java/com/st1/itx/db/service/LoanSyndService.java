package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.LoanSynd;
import org.springframework.data.domain.Slice;

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
   * @param syndNo PK
   * @param titaVo Variable-Length Argument
   * @return LoanSynd LoanSynd
   */
  public LoanSynd findById(int syndNo, TitaVo... titaVo);

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
   * SyndNo &gt;= ,AND SyndNo &lt;= ,AND LeadingBank % ,AND SigningDate &gt;= ,AND SigningDate &lt;=
   *
   * @param syndNo_0 syndNo_0
   * @param syndNo_1 syndNo_1
   * @param leadingBank_2 leadingBank_2
   * @param signingDate_3 signingDate_3
   * @param signingDate_4 signingDate_4
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice LoanSynd LoanSynd of List
   */
  public Slice<LoanSynd> syndNoRange(int syndNo_0, int syndNo_1, String leadingBank_2, int signingDate_3, int signingDate_4, int index, int limit, TitaVo... titaVo);

  /**
   * SigningDate &gt;= ,AND SigningDate&lt;=
   *
   * @param signingDate_0 signingDate_0
   * @param signingDate_1 signingDate_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice LoanSynd LoanSynd of List
   */
  public Slice<LoanSynd> signingDateRange(int signingDate_0, int signingDate_1, int index, int limit, TitaVo... titaVo);

  /**
   * LeadingBank =
   *
   * @param leadingBank_0 leadingBank_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice LoanSynd LoanSynd of List
   */
  public Slice<LoanSynd> leadingBankEq(String leadingBank_0, int index, int limit, TitaVo... titaVo);

  /**
   * hold By LoanSynd
   * 
   * @param syndNo key
   * @param titaVo Variable-Length Argument
   * @return LoanSynd LoanSynd
   */
  public LoanSynd holdById(int syndNo, TitaVo... titaVo);

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
