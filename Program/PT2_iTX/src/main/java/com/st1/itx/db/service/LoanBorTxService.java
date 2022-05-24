package com.st1.itx.db.service;

import java.util.List;

import java.math.BigDecimal;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.LoanBorTx;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.LoanBorTxId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface LoanBorTxService {

  /**
   * findByPrimaryKey
   *
   * @param loanBorTxId PK
   * @param titaVo Variable-Length Argument
   * @return LoanBorTx LoanBorTx
   */
  public LoanBorTx findById(LoanBorTxId loanBorTxId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice LoanBorTx LoanBorTx of List
   */
  public Slice<LoanBorTx> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * CustNo = ,AND FacmNo &gt;= ,AND FacmNo &lt;= ,AND BormNo &gt;= ,AND BormNo &lt;= ,AND AcDate &gt;= ,AND AcDate &lt;=,AND Displayflag  ^i
   *
   * @param custNo_0 custNo_0
   * @param facmNo_1 facmNo_1
   * @param facmNo_2 facmNo_2
   * @param bormNo_3 bormNo_3
   * @param bormNo_4 bormNo_4
   * @param acDate_5 acDate_5
   * @param acDate_6 acDate_6
   * @param displayflag_7 displayflag_7
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice LoanBorTx LoanBorTx of List
   */
  public Slice<LoanBorTx> borxAcDateRange(int custNo_0, int facmNo_1, int facmNo_2, int bormNo_3, int bormNo_4, int acDate_5, int acDate_6, List<String> displayflag_7, int index, int limit, TitaVo... titaVo);

  /**
   * CustNo = ,AND FacmNo = ,AND BormNo &gt;= ,AND BormNo &lt;= 
   *
   * @param custNo_0 custNo_0
   * @param facmNo_1 facmNo_1
   * @param bormNo_2 bormNo_2
   * @param bormNo_3 bormNo_3
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice LoanBorTx LoanBorTx of List
   */
  public Slice<LoanBorTx> borxFacmNoEq(int custNo_0, int facmNo_1, int bormNo_2, int bormNo_3, int index, int limit, TitaVo... titaVo);

  /**
   * CustNo = ,AND FacmNo = ,AND BormNo = ,AND BorxNo &gt;= ,AND BorxNo &lt;=  
   *
   * @param custNo_0 custNo_0
   * @param facmNo_1 facmNo_1
   * @param bormNo_2 bormNo_2
   * @param borxNo_3 borxNo_3
   * @param borxNo_4 borxNo_4
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice LoanBorTx LoanBorTx of List
   */
  public Slice<LoanBorTx> borxBormNoEq(int custNo_0, int facmNo_1, int bormNo_2, int borxNo_3, int borxNo_4, int index, int limit, TitaVo... titaVo);

  /**
   * AcDate = ,AND TitaTlrNo = ,AND TitaTxtNo =
   *
   * @param acDate_0 acDate_0
   * @param titaTlrNo_1 titaTlrNo_1
   * @param titaTxtNo_2 titaTxtNo_2
   * @param titaVo Variable-Length Argument
   * @return Slice LoanBorTx LoanBorTx of List
   */
  public LoanBorTx borxTxtNoFirst(int acDate_0, String titaTlrNo_1, String titaTxtNo_2, TitaVo... titaVo);

  /**
   * CustNo = ,AND FacmNo &gt;= ,AND FacmNo &lt;= ,AND BormNo &gt;= ,AND BormNo &lt;= ,AND EntryDate &gt;= ,AND EntryDate &lt;=,AND Displayflag  ^i
   *
   * @param custNo_0 custNo_0
   * @param facmNo_1 facmNo_1
   * @param facmNo_2 facmNo_2
   * @param bormNo_3 bormNo_3
   * @param bormNo_4 bormNo_4
   * @param entryDate_5 entryDate_5
   * @param entryDate_6 entryDate_6
   * @param displayflag_7 displayflag_7
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice LoanBorTx LoanBorTx of List
   */
  public Slice<LoanBorTx> borxEntryDateRange(int custNo_0, int facmNo_1, int facmNo_2, int bormNo_3, int bormNo_4, int entryDate_5, int entryDate_6, List<String> displayflag_7, int index, int limit, TitaVo... titaVo);

  /**
   * CustNo = ,AND FacmNo = ,AND BormNo = ,AND AcDate = ,AND TitaTlrNo = ,AND TitaTxtNo =
   *
   * @param custNo_0 custNo_0
   * @param facmNo_1 facmNo_1
   * @param bormNo_2 bormNo_2
   * @param acDate_3 acDate_3
   * @param titaTlrNo_4 titaTlrNo_4
   * @param titaTxtNo_5 titaTxtNo_5
   * @param titaVo Variable-Length Argument
   * @return Slice LoanBorTx LoanBorTx of List
   */
  public LoanBorTx custNoTxtNoFirst(int custNo_0, int facmNo_1, int bormNo_2, int acDate_3, String titaTlrNo_4, String titaTxtNo_5, TitaVo... titaVo);

  /**
   * CustNo = ,AND FacmNo = ,AND BormNo = 
   *
   * @param custNo_0 custNo_0
   * @param facmNo_1 facmNo_1
   * @param bormNo_2 bormNo_2
   * @param titaVo Variable-Length Argument
   * @return Slice LoanBorTx LoanBorTx of List
   */
  public LoanBorTx bormNoDescFirst(int custNo_0, int facmNo_1, int bormNo_2, TitaVo... titaVo);

  /**
   * CustNo = ,AND FacmNo = 
   *
   * @param custNo_0 custNo_0
   * @param facmNo_1 facmNo_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice LoanBorTx LoanBorTx of List
   */
  public Slice<LoanBorTx> findByCustNoandFacmNo(int custNo_0, int facmNo_1, int index, int limit, TitaVo... titaVo);

  /**
   * CustNo = ,AND AcDate = ,AND TitaKinBr = ,AND TitaTlrNo = ,AND TitaTxtNo =
   *
   * @param custNo_0 custNo_0
   * @param acDate_1 acDate_1
   * @param titaKinBr_2 titaKinBr_2
   * @param titaTlrNo_3 titaTlrNo_3
   * @param titaTxtNo_4 titaTxtNo_4
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice LoanBorTx LoanBorTx of List
   */
  public Slice<LoanBorTx> custNoTxtNoEq(int custNo_0, int acDate_1, String titaKinBr_2, String titaTlrNo_3, String titaTxtNo_4, int index, int limit, TitaVo... titaVo);

  /**
   * CustNo = ,AND FacmNo &gt;= ,AND FacmNo &lt;= ,AND BormNo &gt;= ,AND BormNo &lt;= ,AND DueDate &gt;= ,AND DueDate &lt;= 
   *
   * @param custNo_0 custNo_0
   * @param facmNo_1 facmNo_1
   * @param facmNo_2 facmNo_2
   * @param bormNo_3 bormNo_3
   * @param bormNo_4 bormNo_4
   * @param dueDate_5 dueDate_5
   * @param dueDate_6 dueDate_6
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice LoanBorTx LoanBorTx of List
   */
  public Slice<LoanBorTx> findDueDateRange(int custNo_0, int facmNo_1, int facmNo_2, int bormNo_3, int bormNo_4, int dueDate_5, int dueDate_6, int index, int limit, TitaVo... titaVo);

  /**
   * AcDate &gt;= ,AND AcDate &lt;= ,AND TitaHCode ^i
   *
   * @param acDate_0 acDate_0
   * @param acDate_1 acDate_1
   * @param titaHCode_2 titaHCode_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice LoanBorTx LoanBorTx of List
   */
  public Slice<LoanBorTx> findAcDateRange(int acDate_0, int acDate_1, List<String> titaHCode_2, int index, int limit, TitaVo... titaVo);

  /**
   * CustNo = ,AND FacmNo = ,AND BormNo &gt;= ,AND BormNo &lt;= ,AND IntEndDate = ,AND TitaHCode ^i ,AND AcDate = ,AND TitaTlrNo = ,AND TitaTxtNo =
   *
   * @param custNo_0 custNo_0
   * @param facmNo_1 facmNo_1
   * @param bormNo_2 bormNo_2
   * @param bormNo_3 bormNo_3
   * @param intEndDate_4 intEndDate_4
   * @param titaHCode_5 titaHCode_5
   * @param acDate_6 acDate_6
   * @param titaTlrNo_7 titaTlrNo_7
   * @param titaTxtNo_8 titaTxtNo_8
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice LoanBorTx LoanBorTx of List
   */
  public Slice<LoanBorTx> findIntEndDateEq(int custNo_0, int facmNo_1, int bormNo_2, int bormNo_3, int intEndDate_4, List<String> titaHCode_5, int acDate_6, String titaTlrNo_7, String titaTxtNo_8, int index, int limit, TitaVo... titaVo);

  /**
   * CustNo = ,AND FacmNo &gt;= ,AND FacmNo &lt;= ,AND BormNo &gt;= ,AND BormNo &lt;= ,AND EntryDate &gt;= ,AND EntryDate &lt;=,AND Displayflag  ^i
   *
   * @param custNo_0 custNo_0
   * @param facmNo_1 facmNo_1
   * @param facmNo_2 facmNo_2
   * @param bormNo_3 bormNo_3
   * @param bormNo_4 bormNo_4
   * @param entryDate_5 entryDate_5
   * @param entryDate_6 entryDate_6
   * @param displayflag_7 displayflag_7
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice LoanBorTx LoanBorTx of List
   */
  public Slice<LoanBorTx> borxIntEndDateDescRange(int custNo_0, int facmNo_1, int facmNo_2, int bormNo_3, int bormNo_4, int entryDate_5, int entryDate_6, List<String> displayflag_7, int index, int limit, TitaVo... titaVo);

  /**
   * hold By LoanBorTx
   * 
   * @param loanBorTxId key
   * @param titaVo Variable-Length Argument
   * @return LoanBorTx LoanBorTx
   */
  public LoanBorTx holdById(LoanBorTxId loanBorTxId, TitaVo... titaVo);

  /**
   * hold By LoanBorTx
   * 
   * @param loanBorTx key
   * @param titaVo Variable-Length Argument
   * @return LoanBorTx LoanBorTx
   */
  public LoanBorTx holdById(LoanBorTx loanBorTx, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param loanBorTx Entity
   * @param titaVo Variable-Length Argument
   * @return LoanBorTx Entity
   * @throws DBException exception
   */
  public LoanBorTx insert(LoanBorTx loanBorTx, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param loanBorTx Entity
   * @param titaVo Variable-Length Argument
   * @return LoanBorTx Entity
   * @throws DBException exception
   */
  public LoanBorTx update(LoanBorTx loanBorTx, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param loanBorTx Entity
   * @param titaVo Variable-Length Argument
   * @return LoanBorTx Entity
   * @throws DBException exception
   */
  public LoanBorTx update2(LoanBorTx loanBorTx, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param loanBorTx Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(LoanBorTx loanBorTx, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param loanBorTx Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<LoanBorTx> loanBorTx, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param loanBorTx Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<LoanBorTx> loanBorTx, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param loanBorTx Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<LoanBorTx> loanBorTx, TitaVo... titaVo) throws DBException;

}
