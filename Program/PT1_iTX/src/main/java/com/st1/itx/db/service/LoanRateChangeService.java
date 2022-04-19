package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.LoanRateChange;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.LoanRateChangeId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface LoanRateChangeService {

	/**
	 * findByPrimaryKey
	 *
	 * @param loanRateChangeId PK
	 * @param titaVo           Variable-Length Argument
	 * @return LoanRateChange LoanRateChange
	 */
	public LoanRateChange findById(LoanRateChangeId loanRateChangeId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice LoanRateChange LoanRateChange of List
	 */
	public Slice<LoanRateChange> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * AcDate = ,AND TellerNo = ,AND TxtNo =
	 *
	 * @param acDate_0   acDate_0
	 * @param tellerNo_1 tellerNo_1
	 * @param txtNo_2    txtNo_2
	 * @param index      Page Index
	 * @param limit      Page Data Limit
	 * @param titaVo     Variable-Length Argument
	 * @return Slice LoanRateChange LoanRateChange of List
	 */
	public Slice<LoanRateChange> rateChangeTxtNoEq(int acDate_0, String tellerNo_1, String txtNo_2, int index, int limit, TitaVo... titaVo);

	/**
	 * CustNo = ,AND FacmNo = ,AND BormNo = ,AND EffectDate &lt;=
	 *
	 * @param custNo_0     custNo_0
	 * @param facmNo_1     facmNo_1
	 * @param bormNo_2     bormNo_2
	 * @param effectDate_3 effectDate_3
	 * @param titaVo       Variable-Length Argument
	 * @return Slice LoanRateChange LoanRateChange of List
	 */
	public LoanRateChange rateChangeEffectDateDescFirst(int custNo_0, int facmNo_1, int bormNo_2, int effectDate_3, TitaVo... titaVo);

	/**
	 * CustNo = ,AND FacmNo = ,AND BormNo = ,AND EffectDate &gt;=
	 *
	 * @param custNo_0     custNo_0
	 * @param facmNo_1     facmNo_1
	 * @param bormNo_2     bormNo_2
	 * @param effectDate_3 effectDate_3
	 * @param titaVo       Variable-Length Argument
	 * @return Slice LoanRateChange LoanRateChange of List
	 */
	public LoanRateChange rateChangeEffectDateAscFirst(int custNo_0, int facmNo_1, int bormNo_2, int effectDate_3, TitaVo... titaVo);

	/**
	 * CustNo = ,AND FacmNo = ,AND BormNo = ,AND EffectDate &gt;=
	 *
	 * @param custNo_0     custNo_0
	 * @param facmNo_1     facmNo_1
	 * @param bormNo_2     bormNo_2
	 * @param effectDate_3 effectDate_3
	 * @param index        Page Index
	 * @param limit        Page Data Limit
	 * @param titaVo       Variable-Length Argument
	 * @return Slice LoanRateChange LoanRateChange of List
	 */
	public Slice<LoanRateChange> rateChangeBormNoEq(int custNo_0, int facmNo_1, int bormNo_2, int effectDate_3, int index, int limit, TitaVo... titaVo);

	/**
	 * CustNo = ,AND FacmNo = ,AND BormNo = ,AND EffectDate =
	 *
	 * @param custNo_0     custNo_0
	 * @param facmNo_1     facmNo_1
	 * @param bormNo_2     bormNo_2
	 * @param effectDate_3 effectDate_3
	 * @param titaVo       Variable-Length Argument
	 * @return Slice LoanRateChange LoanRateChange of List
	 */
	public LoanRateChange rateChangeEffectDateFirst(int custNo_0, int facmNo_1, int bormNo_2, int effectDate_3, TitaVo... titaVo);

	/**
	 * CustNo = ,AND FacmNo &gt;= ,AND FacmNo &lt;= ,AND BormNo &gt;= ,AND BormNo
	 * &lt;= ,AND EffectDate &gt;= ,AND EffectDate &lt;=
	 *
	 * @param custNo_0     custNo_0
	 * @param facmNo_1     facmNo_1
	 * @param facmNo_2     facmNo_2
	 * @param bormNo_3     bormNo_3
	 * @param bormNo_4     bormNo_4
	 * @param effectDate_5 effectDate_5
	 * @param effectDate_6 effectDate_6
	 * @param index        Page Index
	 * @param limit        Page Data Limit
	 * @param titaVo       Variable-Length Argument
	 * @return Slice LoanRateChange LoanRateChange of List
	 */
	public Slice<LoanRateChange> rateChangeEffectDateRange(int custNo_0, int facmNo_1, int facmNo_2, int bormNo_3, int bormNo_4, int effectDate_5, int effectDate_6, int index, int limit,
			TitaVo... titaVo);

	/**
	 * CustNo = ,AND FacmNo &gt;= ,AND FacmNo &lt;= ,AND BormNo &gt;= ,AND BormNo
	 * &lt;= ,AND EffectDate &gt;= ,AND EffectDate &lt;=
	 *
	 * @param custNo_0     custNo_0
	 * @param facmNo_1     facmNo_1
	 * @param facmNo_2     facmNo_2
	 * @param bormNo_3     bormNo_3
	 * @param bormNo_4     bormNo_4
	 * @param effectDate_5 effectDate_5
	 * @param effectDate_6 effectDate_6
	 * @param index        Page Index
	 * @param limit        Page Data Limit
	 * @param titaVo       Variable-Length Argument
	 * @return Slice LoanRateChange LoanRateChange of List
	 */
	public Slice<LoanRateChange> rateChangeFacmNoRange(int custNo_0, int facmNo_1, int facmNo_2, int bormNo_3, int bormNo_4, int effectDate_5, int effectDate_6, int index, int limit,
			TitaVo... titaVo);

	/**
	 * hold By LoanRateChange
	 * 
	 * @param loanRateChangeId key
	 * @param titaVo           Variable-Length Argument
	 * @return LoanRateChange LoanRateChange
	 */
	public LoanRateChange holdById(LoanRateChangeId loanRateChangeId, TitaVo... titaVo);

	/**
	 * hold By LoanRateChange
	 * 
	 * @param loanRateChange key
	 * @param titaVo         Variable-Length Argument
	 * @return LoanRateChange LoanRateChange
	 */
	public LoanRateChange holdById(LoanRateChange loanRateChange, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param loanRateChange Entity
	 * @param titaVo         Variable-Length Argument
	 * @return LoanRateChange Entity
	 * @throws DBException exception
	 */
	public LoanRateChange insert(LoanRateChange loanRateChange, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param loanRateChange Entity
	 * @param titaVo         Variable-Length Argument
	 * @return LoanRateChange Entity
	 * @throws DBException exception
	 */
	public LoanRateChange update(LoanRateChange loanRateChange, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param loanRateChange Entity
	 * @param titaVo         Variable-Length Argument
	 * @return LoanRateChange Entity
	 * @throws DBException exception
	 */
	public LoanRateChange update2(LoanRateChange loanRateChange, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param loanRateChange Entity
	 * @param titaVo         Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(LoanRateChange loanRateChange, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param loanRateChange Entity of List
	 * @param titaVo         Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<LoanRateChange> loanRateChange, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param loanRateChange Entity of List
	 * @param titaVo         Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<LoanRateChange> loanRateChange, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param loanRateChange Entity of List
	 * @param titaVo         Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<LoanRateChange> loanRateChange, TitaVo... titaVo) throws DBException;

}
