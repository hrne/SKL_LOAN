package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.EmpDeductSchedule;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.EmpDeductScheduleId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface EmpDeductScheduleService {

	/**
	 * findByPrimaryKey
	 *
	 * @param empDeductScheduleId PK
	 * @param titaVo              Variable-Length Argument
	 * @return EmpDeductSchedule EmpDeductSchedule
	 */
	public EmpDeductSchedule findById(EmpDeductScheduleId empDeductScheduleId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice EmpDeductSchedule EmpDeductSchedule of List
	 */
	public Slice<EmpDeductSchedule> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * WorkMonth =
	 *
	 * @param workMonth_0 workMonth_0
	 * @param index       Page Index
	 * @param limit       Page Data Limit
	 * @param titaVo      Variable-Length Argument
	 * @return Slice EmpDeductSchedule EmpDeductSchedule of List
	 */
	public Slice<EmpDeductSchedule> monthEqual(int workMonth_0, int index, int limit, TitaVo... titaVo);

	/**
	 * AgType1 =
	 *
	 * @param agType1_0 agType1_0
	 * @param index     Page Index
	 * @param limit     Page Data Limit
	 * @param titaVo    Variable-Length Argument
	 * @return Slice EmpDeductSchedule EmpDeductSchedule of List
	 */
	public Slice<EmpDeductSchedule> agType1Equal(String agType1_0, int index, int limit, TitaVo... titaVo);

	/**
	 * EntryDate &gt;= ,AND EntryDate &lt;=
	 *
	 * @param entryDate_0 entryDate_0
	 * @param entryDate_1 entryDate_1
	 * @param index       Page Index
	 * @param limit       Page Data Limit
	 * @param titaVo      Variable-Length Argument
	 * @return Slice EmpDeductSchedule EmpDeductSchedule of List
	 */
	public Slice<EmpDeductSchedule> entryDateRange(int entryDate_0, int entryDate_1, int index, int limit, TitaVo... titaVo);

	/**
	 * MediaDate &gt;= ,AND MediaDate &lt;=
	 *
	 * @param mediaDate_0 mediaDate_0
	 * @param mediaDate_1 mediaDate_1
	 * @param index       Page Index
	 * @param limit       Page Data Limit
	 * @param titaVo      Variable-Length Argument
	 * @return Slice EmpDeductSchedule EmpDeductSchedule of List
	 */
	public Slice<EmpDeductSchedule> mediaDateRange(int mediaDate_0, int mediaDate_1, int index, int limit, TitaVo... titaVo);

	/**
	 * WorkMonth &gt;= ,AND WorkMonth &lt;=
	 *
	 * @param workMonth_0 workMonth_0
	 * @param workMonth_1 workMonth_1
	 * @param index       Page Index
	 * @param limit       Page Data Limit
	 * @param titaVo      Variable-Length Argument
	 * @return Slice EmpDeductSchedule EmpDeductSchedule of List
	 */
	public Slice<EmpDeductSchedule> monthRange(int workMonth_0, int workMonth_1, int index, int limit, TitaVo... titaVo);

	/**
	 * WorkMonth &gt;= ,AND WorkMonth &lt;=
	 *
	 * @param workMonth_0 workMonth_0
	 * @param workMonth_1 workMonth_1
	 * @param index       Page Index
	 * @param limit       Page Data Limit
	 * @param titaVo      Variable-Length Argument
	 * @return Slice EmpDeductSchedule EmpDeductSchedule of List
	 */
	public Slice<EmpDeductSchedule> findL4R15A(int workMonth_0, int workMonth_1, int index, int limit, TitaVo... titaVo);

	/**
	 * WorkMonth &gt;= ,AND WorkMonth &lt;= ,AND AgType1 =
	 *
	 * @param workMonth_0 workMonth_0
	 * @param workMonth_1 workMonth_1
	 * @param agType1_2   agType1_2
	 * @param index       Page Index
	 * @param limit       Page Data Limit
	 * @param titaVo      Variable-Length Argument
	 * @return Slice EmpDeductSchedule EmpDeductSchedule of List
	 */
	public Slice<EmpDeductSchedule> findL4R15B(int workMonth_0, int workMonth_1, String agType1_2, int index, int limit, TitaVo... titaVo);

	/**
	 * hold By EmpDeductSchedule
	 * 
	 * @param empDeductScheduleId key
	 * @param titaVo              Variable-Length Argument
	 * @return EmpDeductSchedule EmpDeductSchedule
	 */
	public EmpDeductSchedule holdById(EmpDeductScheduleId empDeductScheduleId, TitaVo... titaVo);

	/**
	 * hold By EmpDeductSchedule
	 * 
	 * @param empDeductSchedule key
	 * @param titaVo            Variable-Length Argument
	 * @return EmpDeductSchedule EmpDeductSchedule
	 */
	public EmpDeductSchedule holdById(EmpDeductSchedule empDeductSchedule, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param empDeductSchedule Entity
	 * @param titaVo            Variable-Length Argument
	 * @return EmpDeductSchedule Entity
	 * @throws DBException exception
	 */
	public EmpDeductSchedule insert(EmpDeductSchedule empDeductSchedule, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param empDeductSchedule Entity
	 * @param titaVo            Variable-Length Argument
	 * @return EmpDeductSchedule Entity
	 * @throws DBException exception
	 */
	public EmpDeductSchedule update(EmpDeductSchedule empDeductSchedule, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param empDeductSchedule Entity
	 * @param titaVo            Variable-Length Argument
	 * @return EmpDeductSchedule Entity
	 * @throws DBException exception
	 */
	public EmpDeductSchedule update2(EmpDeductSchedule empDeductSchedule, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param empDeductSchedule Entity
	 * @param titaVo            Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(EmpDeductSchedule empDeductSchedule, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param empDeductSchedule Entity of List
	 * @param titaVo            Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<EmpDeductSchedule> empDeductSchedule, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param empDeductSchedule Entity of List
	 * @param titaVo            Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<EmpDeductSchedule> empDeductSchedule, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param empDeductSchedule Entity of List
	 * @param titaVo            Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<EmpDeductSchedule> empDeductSchedule, TitaVo... titaVo) throws DBException;

}
