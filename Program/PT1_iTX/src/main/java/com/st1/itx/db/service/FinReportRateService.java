package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.FinReportRate;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.FinReportRateId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface FinReportRateService {

	/**
	 * findByPrimaryKey
	 *
	 * @param finReportRateId PK
	 * @param titaVo          Variable-Length Argument
	 * @return FinReportRate FinReportRate
	 */
	public FinReportRate findById(FinReportRateId finReportRateId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice FinReportRate FinReportRate of List
	 */
	public Slice<FinReportRate> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * CustUKey =
	 *
	 * @param custUKey_0 custUKey_0
	 * @param index      Page Index
	 * @param limit      Page Data Limit
	 * @param titaVo     Variable-Length Argument
	 * @return Slice FinReportRate FinReportRate of List
	 */
	public Slice<FinReportRate> findCustUKey(String custUKey_0, int index, int limit, TitaVo... titaVo);

	/**
	 * hold By FinReportRate
	 * 
	 * @param finReportRateId key
	 * @param titaVo          Variable-Length Argument
	 * @return FinReportRate FinReportRate
	 */
	public FinReportRate holdById(FinReportRateId finReportRateId, TitaVo... titaVo);

	/**
	 * hold By FinReportRate
	 * 
	 * @param finReportRate key
	 * @param titaVo        Variable-Length Argument
	 * @return FinReportRate FinReportRate
	 */
	public FinReportRate holdById(FinReportRate finReportRate, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param finReportRate Entity
	 * @param titaVo        Variable-Length Argument
	 * @return FinReportRate Entity
	 * @throws DBException exception
	 */
	public FinReportRate insert(FinReportRate finReportRate, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param finReportRate Entity
	 * @param titaVo        Variable-Length Argument
	 * @return FinReportRate Entity
	 * @throws DBException exception
	 */
	public FinReportRate update(FinReportRate finReportRate, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param finReportRate Entity
	 * @param titaVo        Variable-Length Argument
	 * @return FinReportRate Entity
	 * @throws DBException exception
	 */
	public FinReportRate update2(FinReportRate finReportRate, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param finReportRate Entity
	 * @param titaVo        Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(FinReportRate finReportRate, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param finReportRate Entity of List
	 * @param titaVo        Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<FinReportRate> finReportRate, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param finReportRate Entity of List
	 * @param titaVo        Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<FinReportRate> finReportRate, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param finReportRate Entity of List
	 * @param titaVo        Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<FinReportRate> finReportRate, TitaVo... titaVo) throws DBException;

}
