package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.FinReportProfit;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.FinReportProfitId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface FinReportProfitService {

	/**
	 * findByPrimaryKey
	 *
	 * @param finReportProfitId PK
	 * @param titaVo            Variable-Length Argument
	 * @return FinReportProfit FinReportProfit
	 */
	public FinReportProfit findById(FinReportProfitId finReportProfitId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice FinReportProfit FinReportProfit of List
	 */
	public Slice<FinReportProfit> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * hold By FinReportProfit
	 * 
	 * @param finReportProfitId key
	 * @param titaVo            Variable-Length Argument
	 * @return FinReportProfit FinReportProfit
	 */
	public FinReportProfit holdById(FinReportProfitId finReportProfitId, TitaVo... titaVo);

	/**
	 * hold By FinReportProfit
	 * 
	 * @param finReportProfit key
	 * @param titaVo          Variable-Length Argument
	 * @return FinReportProfit FinReportProfit
	 */
	public FinReportProfit holdById(FinReportProfit finReportProfit, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param finReportProfit Entity
	 * @param titaVo          Variable-Length Argument
	 * @return FinReportProfit Entity
	 * @throws DBException exception
	 */
	public FinReportProfit insert(FinReportProfit finReportProfit, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param finReportProfit Entity
	 * @param titaVo          Variable-Length Argument
	 * @return FinReportProfit Entity
	 * @throws DBException exception
	 */
	public FinReportProfit update(FinReportProfit finReportProfit, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param finReportProfit Entity
	 * @param titaVo          Variable-Length Argument
	 * @return FinReportProfit Entity
	 * @throws DBException exception
	 */
	public FinReportProfit update2(FinReportProfit finReportProfit, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param finReportProfit Entity
	 * @param titaVo          Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(FinReportProfit finReportProfit, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param finReportProfit Entity of List
	 * @param titaVo          Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<FinReportProfit> finReportProfit, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param finReportProfit Entity of List
	 * @param titaVo          Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<FinReportProfit> finReportProfit, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param finReportProfit Entity of List
	 * @param titaVo          Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<FinReportProfit> finReportProfit, TitaVo... titaVo) throws DBException;

}
