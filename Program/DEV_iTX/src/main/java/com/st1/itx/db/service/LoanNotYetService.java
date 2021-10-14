package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.LoanNotYet;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.LoanNotYetId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface LoanNotYetService {

  /**
   * findByPrimaryKey
   *
   * @param loanNotYetId PK
   * @param titaVo Variable-Length Argument
   * @return LoanNotYet LoanNotYet
   */
  public LoanNotYet findById(LoanNotYetId loanNotYetId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice LoanNotYet LoanNotYet of List
   */
  public Slice<LoanNotYet> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * CustNo = ,AND FacmNo &gt;= ,AND FacmNo &lt;= ,AND YetDate&gt;= ,AND YetDate&lt;= ,AND CloseDate&gt;= ,AND CloseDate&lt;=
   *
   * @param custNo_0 custNo_0
   * @param facmNo_1 facmNo_1
   * @param facmNo_2 facmNo_2
   * @param yetDate_3 yetDate_3
   * @param yetDate_4 yetDate_4
   * @param closeDate_5 closeDate_5
   * @param closeDate_6 closeDate_6
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice LoanNotYet LoanNotYet of List
   */
  public Slice<LoanNotYet> notYetCustNoEq(int custNo_0, int facmNo_1, int facmNo_2, int yetDate_3, int yetDate_4, int closeDate_5, int closeDate_6, int index, int limit, TitaVo... titaVo);

  /**
   * CustNo =
   *
   * @param custNo_0 custNo_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice LoanNotYet LoanNotYet of List
   */
  public Slice<LoanNotYet> findCustNoEq(int custNo_0, int index, int limit, TitaVo... titaVo);

  /**
   * CustNo = ,AND FacmNo = ,AND NotYetCode =
   *
   * @param custNo_0 custNo_0
   * @param facmNo_1 facmNo_1
   * @param notYetCode_2 notYetCode_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice LoanNotYet LoanNotYet of List
   */
  public Slice<LoanNotYet> notYetCodeFisrt(int custNo_0, int facmNo_1, String notYetCode_2, int index, int limit, TitaVo... titaVo);

  /**
   * CloseDate = ,AND YetDate &lt;=
   *
   * @param closeDate_0 closeDate_0
   * @param yetDate_1 yetDate_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice LoanNotYet LoanNotYet of List
   */
  public Slice<LoanNotYet> allNoClose(int closeDate_0, int yetDate_1, int index, int limit, TitaVo... titaVo);

  /**
   * hold By LoanNotYet
   * 
   * @param loanNotYetId key
   * @param titaVo Variable-Length Argument
   * @return LoanNotYet LoanNotYet
   */
  public LoanNotYet holdById(LoanNotYetId loanNotYetId, TitaVo... titaVo);

  /**
   * hold By LoanNotYet
   * 
   * @param loanNotYet key
   * @param titaVo Variable-Length Argument
   * @return LoanNotYet LoanNotYet
   */
  public LoanNotYet holdById(LoanNotYet loanNotYet, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param loanNotYet Entity
   * @param titaVo Variable-Length Argument
   * @return LoanNotYet Entity
   * @throws DBException exception
   */
  public LoanNotYet insert(LoanNotYet loanNotYet, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param loanNotYet Entity
   * @param titaVo Variable-Length Argument
   * @return LoanNotYet Entity
   * @throws DBException exception
   */
  public LoanNotYet update(LoanNotYet loanNotYet, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param loanNotYet Entity
   * @param titaVo Variable-Length Argument
   * @return LoanNotYet Entity
   * @throws DBException exception
   */
  public LoanNotYet update2(LoanNotYet loanNotYet, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param loanNotYet Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(LoanNotYet loanNotYet, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param loanNotYet Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<LoanNotYet> loanNotYet, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param loanNotYet Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<LoanNotYet> loanNotYet, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param loanNotYet Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<LoanNotYet> loanNotYet, TitaVo... titaVo) throws DBException;

}
