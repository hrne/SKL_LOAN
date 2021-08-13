package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.LoanBorHistory;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.LoanBorHistoryId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface LoanBorHistoryService {

	/**
	 * findByPrimaryKey
	 *
	 * @param loanBorHistoryId PK
	 * @param titaVo           Variable-Length Argument
	 * @return LoanBorHistory LoanBorHistory
	 */
	public LoanBorHistory findById(LoanBorHistoryId loanBorHistoryId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice LoanBorHistory LoanBorHistory of List
	 */
	public Slice<LoanBorHistory> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * CustNo = ,AND FacmNo &gt;= ,AND FacmNo &lt;= ,AND BormNo &gt;= ,AND BormNo
	 * &lt;=
	 *
	 * @param custNo_0 custNo_0
	 * @param facmNo_1 facmNo_1
	 * @param facmNo_2 facmNo_2
	 * @param bormNo_3 bormNo_3
	 * @param bormNo_4 bormNo_4
	 * @param index    Page Index
	 * @param limit    Page Data Limit
	 * @param titaVo   Variable-Length Argument
	 * @return Slice LoanBorHistory LoanBorHistory of List
	 */
	public Slice<LoanBorHistory> bormCustNoEq(int custNo_0, int facmNo_1, int facmNo_2, int bormNo_3, int bormNo_4, int index, int limit, TitaVo... titaVo);

	/**
	 * CustNo = ,AND FacmNo ^i ,AND BormNo &gt;= ,AND BormNo &lt;=
	 *
	 * @param custNo_0 custNo_0
	 * @param facmNo_1 facmNo_1
	 * @param bormNo_2 bormNo_2
	 * @param bormNo_3 bormNo_3
	 * @param index    Page Index
	 * @param limit    Page Data Limit
	 * @param titaVo   Variable-Length Argument
	 * @return Slice LoanBorHistory LoanBorHistory of List
	 */
	public Slice<LoanBorHistory> bormFacmNoIn(int custNo_0, List<Integer> facmNo_1, int bormNo_2, int bormNo_3, int index, int limit, TitaVo... titaVo);

	/**
	 * DrawdownDate &gt;= ,AND DrawdownDate &lt;= ,AND BormNo &gt;= ,AND BormNo
	 * &lt;= ,AND Status ^i
	 *
	 * @param drawdownDate_0 drawdownDate_0
	 * @param drawdownDate_1 drawdownDate_1
	 * @param bormNo_2       bormNo_2
	 * @param bormNo_3       bormNo_3
	 * @param status_4       status_4
	 * @param index          Page Index
	 * @param limit          Page Data Limit
	 * @param titaVo         Variable-Length Argument
	 * @return Slice LoanBorHistory LoanBorHistory of List
	 */
	public Slice<LoanBorHistory> bormDrawdownDateRange(int drawdownDate_0, int drawdownDate_1, int bormNo_2, int bormNo_3, List<Integer> status_4, int index, int limit, TitaVo... titaVo);

	/**
	 * NextPayIntDate &gt;= ,AND NextPayIntDate &lt;= ,AND Status =
	 *
	 * @param nextPayIntDate_0 nextPayIntDate_0
	 * @param nextPayIntDate_1 nextPayIntDate_1
	 * @param status_2         status_2
	 * @param index            Page Index
	 * @param limit            Page Data Limit
	 * @param titaVo           Variable-Length Argument
	 * @return Slice LoanBorHistory LoanBorHistory of List
	 */
	public Slice<LoanBorHistory> nextPayIntDateRange(int nextPayIntDate_0, int nextPayIntDate_1, int status_2, int index, int limit, TitaVo... titaVo);

	/**
	 * Status ^i ,AND DrawdownDate &gt;= ,AND DrawdownDate &lt;=
	 *
	 * @param status_0       status_0
	 * @param drawdownDate_1 drawdownDate_1
	 * @param drawdownDate_2 drawdownDate_2
	 * @param index          Page Index
	 * @param limit          Page Data Limit
	 * @param titaVo         Variable-Length Argument
	 * @return Slice LoanBorHistory LoanBorHistory of List
	 */
	public Slice<LoanBorHistory> findStatusRange(List<Integer> status_0, int drawdownDate_1, int drawdownDate_2, int index, int limit, TitaVo... titaVo);

	/**
	 * AmortizedCode = ,AND Status = ,AND NextPayIntDate &gt;= ,AND NextPayIntDate
	 * &lt;= ,AND BormNo &gt;= ,AND BormNo &lt;=
	 *
	 * @param amortizedCode_0  amortizedCode_0
	 * @param status_1         status_1
	 * @param nextPayIntDate_2 nextPayIntDate_2
	 * @param nextPayIntDate_3 nextPayIntDate_3
	 * @param bormNo_4         bormNo_4
	 * @param bormNo_5         bormNo_5
	 * @param index            Page Index
	 * @param limit            Page Data Limit
	 * @param titaVo           Variable-Length Argument
	 * @return Slice LoanBorHistory LoanBorHistory of List
	 */
	public Slice<LoanBorHistory> AmortizedCodeEq(String amortizedCode_0, int status_1, int nextPayIntDate_2, int nextPayIntDate_3, int bormNo_4, int bormNo_5, int index, int limit, TitaVo... titaVo);

	/**
	 * Status ^i ,AND CustNo = ,AND FacmNo &gt;= ,AND FacmNo &lt;=
	 *
	 * @param status_0 status_0
	 * @param custNo_1 custNo_1
	 * @param facmNo_2 facmNo_2
	 * @param facmNo_3 facmNo_3
	 * @param index    Page Index
	 * @param limit    Page Data Limit
	 * @param titaVo   Variable-Length Argument
	 * @return Slice LoanBorHistory LoanBorHistory of List
	 */
	public Slice<LoanBorHistory> findStatusEq(List<Integer> status_0, int custNo_1, int facmNo_2, int facmNo_3, int index, int limit, TitaVo... titaVo);

	/**
	 * hold By LoanBorHistory
	 * 
	 * @param loanBorHistoryId key
	 * @param titaVo           Variable-Length Argument
	 * @return LoanBorHistory LoanBorHistory
	 */
	public LoanBorHistory holdById(LoanBorHistoryId loanBorHistoryId, TitaVo... titaVo);

	/**
	 * hold By LoanBorHistory
	 * 
	 * @param loanBorHistory key
	 * @param titaVo         Variable-Length Argument
	 * @return LoanBorHistory LoanBorHistory
	 */
	public LoanBorHistory holdById(LoanBorHistory loanBorHistory, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param loanBorHistory Entity
	 * @param titaVo         Variable-Length Argument
	 * @return LoanBorHistory Entity
	 * @throws DBException exception
	 */
	public LoanBorHistory insert(LoanBorHistory loanBorHistory, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param loanBorHistory Entity
	 * @param titaVo         Variable-Length Argument
	 * @return LoanBorHistory Entity
	 * @throws DBException exception
	 */
	public LoanBorHistory update(LoanBorHistory loanBorHistory, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param loanBorHistory Entity
	 * @param titaVo         Variable-Length Argument
	 * @return LoanBorHistory Entity
	 * @throws DBException exception
	 */
	public LoanBorHistory update2(LoanBorHistory loanBorHistory, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param loanBorHistory Entity
	 * @param titaVo         Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(LoanBorHistory loanBorHistory, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param loanBorHistory Entity of List
	 * @param titaVo         Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<LoanBorHistory> loanBorHistory, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param loanBorHistory Entity of List
	 * @param titaVo         Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<LoanBorHistory> loanBorHistory, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param loanBorHistory Entity of List
	 * @param titaVo         Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<LoanBorHistory> loanBorHistory, TitaVo... titaVo) throws DBException;

}
