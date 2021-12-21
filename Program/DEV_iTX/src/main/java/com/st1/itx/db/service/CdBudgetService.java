package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CdBudget;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.CdBudgetId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CdBudgetService {

	/**
	 * findByPrimaryKey
	 *
	 * @param cdBudgetId PK
	 * @param titaVo     Variable-Length Argument
	 * @return CdBudget CdBudget
	 */
	public CdBudget findById(CdBudgetId cdBudgetId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice CdBudget CdBudget of List
	 */
	public Slice<CdBudget> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * Year &gt;= ,AND Year &lt;= ,AND Month &gt;= ,AND Month &lt;=
	 *
	 * @param year_0  year_0
	 * @param year_1  year_1
	 * @param month_2 month_2
	 * @param month_3 month_3
	 * @param index   Page Index
	 * @param limit   Page Data Limit
	 * @param titaVo  Variable-Length Argument
	 * @return Slice CdBudget CdBudget of List
	 */
	public Slice<CdBudget> findYearMonth(int year_0, int year_1, int month_2, int month_3, int index, int limit, TitaVo... titaVo);

	/**
	 * hold By CdBudget
	 * 
	 * @param cdBudgetId key
	 * @param titaVo     Variable-Length Argument
	 * @return CdBudget CdBudget
	 */
	public CdBudget holdById(CdBudgetId cdBudgetId, TitaVo... titaVo);

	/**
	 * hold By CdBudget
	 * 
	 * @param cdBudget key
	 * @param titaVo   Variable-Length Argument
	 * @return CdBudget CdBudget
	 */
	public CdBudget holdById(CdBudget cdBudget, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param cdBudget Entity
	 * @param titaVo   Variable-Length Argument
	 * @return CdBudget Entity
	 * @throws DBException exception
	 */
	public CdBudget insert(CdBudget cdBudget, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param cdBudget Entity
	 * @param titaVo   Variable-Length Argument
	 * @return CdBudget Entity
	 * @throws DBException exception
	 */
	public CdBudget update(CdBudget cdBudget, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param cdBudget Entity
	 * @param titaVo   Variable-Length Argument
	 * @return CdBudget Entity
	 * @throws DBException exception
	 */
	public CdBudget update2(CdBudget cdBudget, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param cdBudget Entity
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(CdBudget cdBudget, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param cdBudget Entity of List
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<CdBudget> cdBudget, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param cdBudget Entity of List
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<CdBudget> cdBudget, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param cdBudget Entity of List
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<CdBudget> cdBudget, TitaVo... titaVo) throws DBException;

}
