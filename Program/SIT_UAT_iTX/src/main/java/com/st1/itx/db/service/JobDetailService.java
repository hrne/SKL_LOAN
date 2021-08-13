package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JobDetail;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JobDetailId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JobDetailService {

	/**
	 * findByPrimaryKey
	 *
	 * @param jobDetailId PK
	 * @param titaVo      Variable-Length Argument
	 * @return JobDetail JobDetail
	 */
	public JobDetail findById(JobDetailId jobDetailId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice JobDetail JobDetail of List
	 */
	public Slice<JobDetail> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * ExecDate = ,AND JobCode =
	 *
	 * @param execDate_0 execDate_0
	 * @param jobCode_1  jobCode_1
	 * @param index      Page Index
	 * @param limit      Page Data Limit
	 * @param titaVo     Variable-Length Argument
	 * @return Slice JobDetail JobDetail of List
	 */
	public Slice<JobDetail> jobCodeEq(int execDate_0, String jobCode_1, int index, int limit, TitaVo... titaVo);

	/**
	 * ExecDate =
	 *
	 * @param execDate_0 execDate_0
	 * @param index      Page Index
	 * @param limit      Page Data Limit
	 * @param titaVo     Variable-Length Argument
	 * @return Slice JobDetail JobDetail of List
	 */
	public Slice<JobDetail> execDateEq(int execDate_0, int index, int limit, TitaVo... titaVo);

	/**
	 * ExecDate &gt;=
	 *
	 * @param execDate_0 execDate_0
	 * @param titaVo     Variable-Length Argument
	 * @return Slice JobDetail JobDetail of List
	 */
	public JobDetail execDateFirst(int execDate_0, TitaVo... titaVo);

	/**
	 * hold By JobDetail
	 * 
	 * @param jobDetailId key
	 * @param titaVo      Variable-Length Argument
	 * @return JobDetail JobDetail
	 */
	public JobDetail holdById(JobDetailId jobDetailId, TitaVo... titaVo);

	/**
	 * hold By JobDetail
	 * 
	 * @param jobDetail key
	 * @param titaVo    Variable-Length Argument
	 * @return JobDetail JobDetail
	 */
	public JobDetail holdById(JobDetail jobDetail, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param jobDetail Entity
	 * @param titaVo    Variable-Length Argument
	 * @return JobDetail Entity
	 * @throws DBException exception
	 */
	public JobDetail insert(JobDetail jobDetail, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param jobDetail Entity
	 * @param titaVo    Variable-Length Argument
	 * @return JobDetail Entity
	 * @throws DBException exception
	 */
	public JobDetail update(JobDetail jobDetail, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param jobDetail Entity
	 * @param titaVo    Variable-Length Argument
	 * @return JobDetail Entity
	 * @throws DBException exception
	 */
	public JobDetail update2(JobDetail jobDetail, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param jobDetail Entity
	 * @param titaVo    Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(JobDetail jobDetail, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param jobDetail Entity of List
	 * @param titaVo    Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<JobDetail> jobDetail, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param jobDetail Entity of List
	 * @param titaVo    Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<JobDetail> jobDetail, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param jobDetail Entity of List
	 * @param titaVo    Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<JobDetail> jobDetail, TitaVo... titaVo) throws DBException;

}
