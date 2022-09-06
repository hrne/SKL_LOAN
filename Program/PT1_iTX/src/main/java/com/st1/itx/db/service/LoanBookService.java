package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.LoanBook;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.LoanBookId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface LoanBookService {

	/**
	 * findByPrimaryKey
	 *
	 * @param loanBookId PK
	 * @param titaVo     Variable-Length Argument
	 * @return LoanBook LoanBook
	 */
	public LoanBook findById(LoanBookId loanBookId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice LoanBook LoanBook of List
	 */
	public Slice<LoanBook> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * BookDate &gt;= ,AND BookDate &lt;=
	 *
	 * @param bookDate_0 bookDate_0
	 * @param bookDate_1 bookDate_1
	 * @param index      Page Index
	 * @param limit      Page Data Limit
	 * @param titaVo     Variable-Length Argument
	 * @return Slice LoanBook LoanBook of List
	 */
	public Slice<LoanBook> bookDateRange(int bookDate_0, int bookDate_1, int index, int limit, TitaVo... titaVo);

	/**
	 * CustNo = ,AND FacmNo = ,AND BormNo =
	 *
	 * @param custNo_0 custNo_0
	 * @param facmNo_1 facmNo_1
	 * @param bormNo_2 bormNo_2
	 * @param titaVo   Variable-Length Argument
	 * @return Slice LoanBook LoanBook of List
	 */
	public LoanBook bookBormNoFirst(int custNo_0, int facmNo_1, int bormNo_2, TitaVo... titaVo);

	/**
	 * CustNo = ,AND FacmNo = ,AND BormNo = ,AND BookDate &gt;= ,AND BookDate &lt;=
	 *
	 * @param custNo_0   custNo_0
	 * @param facmNo_1   facmNo_1
	 * @param bormNo_2   bormNo_2
	 * @param bookDate_3 bookDate_3
	 * @param bookDate_4 bookDate_4
	 * @param index      Page Index
	 * @param limit      Page Data Limit
	 * @param titaVo     Variable-Length Argument
	 * @return Slice LoanBook LoanBook of List
	 */
	public Slice<LoanBook> bookBormNoRange(int custNo_0, int facmNo_1, int bormNo_2, int bookDate_3, int bookDate_4, int index, int limit, TitaVo... titaVo);

	/**
	 * CustNo &gt;= ,AND CustNo &lt;= ,AND FacmNo &gt;= ,AND FacmNo &lt;= ,AND
	 * BormNo &gt;= ,AND BormNo &lt;= ,AND BookDate &gt;=
	 *
	 * @param custNo_0   custNo_0
	 * @param custNo_1   custNo_1
	 * @param facmNo_2   facmNo_2
	 * @param facmNo_3   facmNo_3
	 * @param bormNo_4   bormNo_4
	 * @param bormNo_5   bormNo_5
	 * @param bookDate_6 bookDate_6
	 * @param index      Page Index
	 * @param limit      Page Data Limit
	 * @param titaVo     Variable-Length Argument
	 * @return Slice LoanBook LoanBook of List
	 */
	public Slice<LoanBook> bookCustNoRange(int custNo_0, int custNo_1, int facmNo_2, int facmNo_3, int bormNo_4, int bormNo_5, int bookDate_6, int index, int limit, TitaVo... titaVo);

	/**
	 * CustNo = ,AND FacmNo &gt;= ,AND FacmNo &lt;= ,AND BormNo &gt;= ,AND BormNo
	 * &lt;=
	 *
	 * @param custNo_0 custNo_0
	 * @param facmNo_1 facmNo_1
	 * @param facmNo_2 facmNo_2
	 * @param bormNo_3 bormNo_3
	 * @param bormNo_4 bormNo_4
	 * @param titaVo   Variable-Length Argument
	 * @return Slice LoanBook LoanBook of List
	 */
	public LoanBook facmNoLastBookDateFirst(int custNo_0, int facmNo_1, int facmNo_2, int bormNo_3, int bormNo_4, TitaVo... titaVo);

	/**
	 * hold By LoanBook
	 * 
	 * @param loanBookId key
	 * @param titaVo     Variable-Length Argument
	 * @return LoanBook LoanBook
	 */
	public LoanBook holdById(LoanBookId loanBookId, TitaVo... titaVo);

	/**
	 * hold By LoanBook
	 * 
	 * @param loanBook key
	 * @param titaVo   Variable-Length Argument
	 * @return LoanBook LoanBook
	 */
	public LoanBook holdById(LoanBook loanBook, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param loanBook Entity
	 * @param titaVo   Variable-Length Argument
	 * @return LoanBook Entity
	 * @throws DBException exception
	 */
	public LoanBook insert(LoanBook loanBook, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param loanBook Entity
	 * @param titaVo   Variable-Length Argument
	 * @return LoanBook Entity
	 * @throws DBException exception
	 */
	public LoanBook update(LoanBook loanBook, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param loanBook Entity
	 * @param titaVo   Variable-Length Argument
	 * @return LoanBook Entity
	 * @throws DBException exception
	 */
	public LoanBook update2(LoanBook loanBook, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param loanBook Entity
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(LoanBook loanBook, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param loanBook Entity of List
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<LoanBook> loanBook, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param loanBook Entity of List
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<LoanBook> loanBook, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param loanBook Entity of List
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<LoanBook> loanBook, TitaVo... titaVo) throws DBException;

}
