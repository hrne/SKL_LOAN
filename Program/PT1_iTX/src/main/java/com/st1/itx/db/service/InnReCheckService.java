package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.InnReCheck;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.InnReCheckId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface InnReCheckService {

	/**
	 * findByPrimaryKey
	 *
	 * @param innReCheckId PK
	 * @param titaVo       Variable-Length Argument
	 * @return InnReCheck InnReCheck
	 */
	public InnReCheck findById(InnReCheckId innReCheckId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice InnReCheck InnReCheck of List
	 */
	public Slice<InnReCheck> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * YearMonth = ,AND ConditionCode = ,AND CustNo &gt;= ,AND CustNo &lt;=
	 *
	 * @param yearMonth_0     yearMonth_0
	 * @param conditionCode_1 conditionCode_1
	 * @param custNo_2        custNo_2
	 * @param custNo_3        custNo_3
	 * @param index           Page Index
	 * @param limit           Page Data Limit
	 * @param titaVo          Variable-Length Argument
	 * @return Slice InnReCheck InnReCheck of List
	 */
	public Slice<InnReCheck> findCustNo(int yearMonth_0, int conditionCode_1, int custNo_2, int custNo_3, int index, int limit, TitaVo... titaVo);

	/**
	 * YearMonth &gt;= ,AND YearMonth &lt;= ,AND CustNo &gt;= ,AND CustNo &lt;=
	 *
	 * @param yearMonth_0 yearMonth_0
	 * @param yearMonth_1 yearMonth_1
	 * @param custNo_2    custNo_2
	 * @param custNo_3    custNo_3
	 * @param index       Page Index
	 * @param limit       Page Data Limit
	 * @param titaVo      Variable-Length Argument
	 * @return Slice InnReCheck InnReCheck of List
	 */
	public Slice<InnReCheck> findYearMonth(int yearMonth_0, int yearMonth_1, int custNo_2, int custNo_3, int index, int limit, TitaVo... titaVo);

	/**
	 * TraceMonth &gt;= ,AND TraceMonth &lt;=
	 *
	 * @param traceMonth_0 traceMonth_0
	 * @param traceMonth_1 traceMonth_1
	 * @param index        Page Index
	 * @param limit        Page Data Limit
	 * @param titaVo       Variable-Length Argument
	 * @return Slice InnReCheck InnReCheck of List
	 */
	public Slice<InnReCheck> findTraceMonth(int traceMonth_0, int traceMonth_1, int index, int limit, TitaVo... titaVo);

	/**
	 * ConditionCode = ,AND YearMonth=
	 *
	 * @param conditionCode_0 conditionCode_0
	 * @param yearMonth_1     yearMonth_1
	 * @param index           Page Index
	 * @param limit           Page Data Limit
	 * @param titaVo          Variable-Length Argument
	 * @return Slice InnReCheck InnReCheck of List
	 */
	public Slice<InnReCheck> findSpecify(int conditionCode_0, int yearMonth_1, int index, int limit, TitaVo... titaVo);

	/**
	 * ConditionCode = ,AND YearMonth= ,AND CustNo=
	 *
	 * @param conditionCode_0 conditionCode_0
	 * @param yearMonth_1     yearMonth_1
	 * @param custNo_2        custNo_2
	 * @param index           Page Index
	 * @param limit           Page Data Limit
	 * @param titaVo          Variable-Length Argument
	 * @return Slice InnReCheck InnReCheck of List
	 */
	public Slice<InnReCheck> findSpecifyNo(int conditionCode_0, int yearMonth_1, int custNo_2, int index, int limit, TitaVo... titaVo);

	/**
	 * hold By InnReCheck
	 * 
	 * @param innReCheckId key
	 * @param titaVo       Variable-Length Argument
	 * @return InnReCheck InnReCheck
	 */
	public InnReCheck holdById(InnReCheckId innReCheckId, TitaVo... titaVo);

	/**
	 * hold By InnReCheck
	 * 
	 * @param innReCheck key
	 * @param titaVo     Variable-Length Argument
	 * @return InnReCheck InnReCheck
	 */
	public InnReCheck holdById(InnReCheck innReCheck, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param innReCheck Entity
	 * @param titaVo     Variable-Length Argument
	 * @return InnReCheck Entity
	 * @throws DBException exception
	 */
	public InnReCheck insert(InnReCheck innReCheck, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param innReCheck Entity
	 * @param titaVo     Variable-Length Argument
	 * @return InnReCheck Entity
	 * @throws DBException exception
	 */
	public InnReCheck update(InnReCheck innReCheck, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param innReCheck Entity
	 * @param titaVo     Variable-Length Argument
	 * @return InnReCheck Entity
	 * @throws DBException exception
	 */
	public InnReCheck update2(InnReCheck innReCheck, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param innReCheck Entity
	 * @param titaVo     Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(InnReCheck innReCheck, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param innReCheck Entity of List
	 * @param titaVo     Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<InnReCheck> innReCheck, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param innReCheck Entity of List
	 * @param titaVo     Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<InnReCheck> innReCheck, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param innReCheck Entity of List
	 * @param titaVo     Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<InnReCheck> innReCheck, TitaVo... titaVo) throws DBException;

	/**
	 * Stored Procedure<br>
	 * (????????????)?????? InnReCheck ?????????????????????
	 * 
	 * @param tbsdyf int
	 * @param empNo  String
	 * @param titaVo Variable-Length Argument
	 *
	 */
	public void Usp_L5_InnReCheck_Upd(int tbsdyf, String empNo, TitaVo... titaVo);

}
