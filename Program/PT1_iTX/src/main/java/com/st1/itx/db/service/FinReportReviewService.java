package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.FinReportReview;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.FinReportReviewId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface FinReportReviewService {

	/**
	 * findByPrimaryKey
	 *
	 * @param finReportReviewId PK
	 * @param titaVo            Variable-Length Argument
	 * @return FinReportReview FinReportReview
	 */
	public FinReportReview findById(FinReportReviewId finReportReviewId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice FinReportReview FinReportReview of List
	 */
	public Slice<FinReportReview> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * CustUKey =
	 *
	 * @param custUKey_0 custUKey_0
	 * @param index      Page Index
	 * @param limit      Page Data Limit
	 * @param titaVo     Variable-Length Argument
	 * @return Slice FinReportReview FinReportReview of List
	 */
	public Slice<FinReportReview> findCustUKey(String custUKey_0, int index, int limit, TitaVo... titaVo);

	/**
	 * hold By FinReportReview
	 * 
	 * @param finReportReviewId key
	 * @param titaVo            Variable-Length Argument
	 * @return FinReportReview FinReportReview
	 */
	public FinReportReview holdById(FinReportReviewId finReportReviewId, TitaVo... titaVo);

	/**
	 * hold By FinReportReview
	 * 
	 * @param finReportReview key
	 * @param titaVo          Variable-Length Argument
	 * @return FinReportReview FinReportReview
	 */
	public FinReportReview holdById(FinReportReview finReportReview, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param finReportReview Entity
	 * @param titaVo          Variable-Length Argument
	 * @return FinReportReview Entity
	 * @throws DBException exception
	 */
	public FinReportReview insert(FinReportReview finReportReview, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param finReportReview Entity
	 * @param titaVo          Variable-Length Argument
	 * @return FinReportReview Entity
	 * @throws DBException exception
	 */
	public FinReportReview update(FinReportReview finReportReview, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param finReportReview Entity
	 * @param titaVo          Variable-Length Argument
	 * @return FinReportReview Entity
	 * @throws DBException exception
	 */
	public FinReportReview update2(FinReportReview finReportReview, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param finReportReview Entity
	 * @param titaVo          Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(FinReportReview finReportReview, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param finReportReview Entity of List
	 * @param titaVo          Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<FinReportReview> finReportReview, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param finReportReview Entity of List
	 * @param titaVo          Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<FinReportReview> finReportReview, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param finReportReview Entity of List
	 * @param titaVo          Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<FinReportReview> finReportReview, TitaVo... titaVo) throws DBException;

}
