package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CdEmp;
import org.springframework.data.domain.Slice;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CdEmpService {

	/**
	 * findByPrimaryKey
	 *
	 * @param employeeNo PK
	 * @param titaVo     Variable-Length Argument
	 * @return CdEmp CdEmp
	 */
	public CdEmp findById(String employeeNo, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice CdEmp CdEmp of List
	 */
	public Slice<CdEmp> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * EmployeeNo &gt;= ,AND EmployeeNo &lt;=
	 *
	 * @param employeeNo_0 employeeNo_0
	 * @param employeeNo_1 employeeNo_1
	 * @param index        Page Index
	 * @param limit        Page Data Limit
	 * @param titaVo       Variable-Length Argument
	 * @return Slice CdEmp CdEmp of List
	 */
	public Slice<CdEmp> findEmployeeNo(String employeeNo_0, String employeeNo_1, int index, int limit, TitaVo... titaVo);

	/**
	 * EmployeeNo %
	 *
	 * @param employeeNo_0 employeeNo_0
	 * @param index        Page Index
	 * @param limit        Page Data Limit
	 * @param titaVo       Variable-Length Argument
	 * @return Slice CdEmp CdEmp of List
	 */
	public Slice<CdEmp> EmployeeNoLike(String employeeNo_0, int index, int limit, TitaVo... titaVo);

	/**
	 * AgentId =
	 *
	 * @param agentId_0 agentId_0
	 * @param titaVo    Variable-Length Argument
	 * @return Slice CdEmp CdEmp of List
	 */
	public CdEmp findAgentIdFirst(String agentId_0, TitaVo... titaVo);

	/**
	 * CenterCode =
	 *
	 * @param centerCode_0 centerCode_0
	 * @param index        Page Index
	 * @param limit        Page Data Limit
	 * @param titaVo       Variable-Length Argument
	 * @return Slice CdEmp CdEmp of List
	 */
	public Slice<CdEmp> findCenterCode(String centerCode_0, int index, int limit, TitaVo... titaVo);

	/**
	 * Fullname =
	 *
	 * @param fullname_0 fullname_0
	 * @param index      Page Index
	 * @param limit      Page Data Limit
	 * @param titaVo     Variable-Length Argument
	 * @return Slice CdEmp CdEmp of List
	 */
	public Slice<CdEmp> findFullname(String fullname_0, int index, int limit, TitaVo... titaVo);

	/**
	 * Fullname %
	 *
	 * @param fullname_0 fullname_0
	 * @param index      Page Index
	 * @param limit      Page Data Limit
	 * @param titaVo     Variable-Length Argument
	 * @return Slice CdEmp CdEmp of List
	 */
	public Slice<CdEmp> findFullnameLike(String fullname_0, int index, int limit, TitaVo... titaVo);

	/**
	 * CenterCode = , AND AgCurInd =
	 *
	 * @param centerCode_0 centerCode_0
	 * @param agCurInd_1   agCurInd_1
	 * @param index        Page Index
	 * @param limit        Page Data Limit
	 * @param titaVo       Variable-Length Argument
	 * @return Slice CdEmp CdEmp of List
	 */
	public Slice<CdEmp> findCenterCodeAndAgCurInd(String centerCode_0, String agCurInd_1, int index, int limit, TitaVo... titaVo);

	/**
	 * EmployeeNo % , AND AgCurInd =
	 *
	 * @param employeeNo_0 employeeNo_0
	 * @param agCurInd_1   agCurInd_1
	 * @param index        Page Index
	 * @param limit        Page Data Limit
	 * @param titaVo       Variable-Length Argument
	 * @return Slice CdEmp CdEmp of List
	 */
	public Slice<CdEmp> EmployeeNoLikeAndAgCurInd(String employeeNo_0, String agCurInd_1, int index, int limit, TitaVo... titaVo);

	/**
	 * Fullname % , AND AgCurInd =
	 *
	 * @param fullname_0 fullname_0
	 * @param agCurInd_1 agCurInd_1
	 * @param index      Page Index
	 * @param limit      Page Data Limit
	 * @param titaVo     Variable-Length Argument
	 * @return Slice CdEmp CdEmp of List
	 */
	public Slice<CdEmp> findFullnameLikeAndAgCurInd(String fullname_0, String agCurInd_1, int index, int limit, TitaVo... titaVo);

	/**
	 * EmployeeNo &gt;= ,AND EmployeeNo &lt;= , AND AgCurInd =
	 *
	 * @param employeeNo_0 employeeNo_0
	 * @param employeeNo_1 employeeNo_1
	 * @param agCurInd_2   agCurInd_2
	 * @param index        Page Index
	 * @param limit        Page Data Limit
	 * @param titaVo       Variable-Length Argument
	 * @return Slice CdEmp CdEmp of List
	 */
	public Slice<CdEmp> findEmployeeNoAndAgCurInd(String employeeNo_0, String employeeNo_1, String agCurInd_2, int index, int limit, TitaVo... titaVo);

	/**
	 * hold By CdEmp
	 * 
	 * @param employeeNo key
	 * @param titaVo     Variable-Length Argument
	 * @return CdEmp CdEmp
	 */
	public CdEmp holdById(String employeeNo, TitaVo... titaVo);

	/**
	 * hold By CdEmp
	 * 
	 * @param cdEmp  key
	 * @param titaVo Variable-Length Argument
	 * @return CdEmp CdEmp
	 */
	public CdEmp holdById(CdEmp cdEmp, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param cdEmp  Entity
	 * @param titaVo Variable-Length Argument
	 * @return CdEmp Entity
	 * @throws DBException exception
	 */
	public CdEmp insert(CdEmp cdEmp, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param cdEmp  Entity
	 * @param titaVo Variable-Length Argument
	 * @return CdEmp Entity
	 * @throws DBException exception
	 */
	public CdEmp update(CdEmp cdEmp, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param cdEmp  Entity
	 * @param titaVo Variable-Length Argument
	 * @return CdEmp Entity
	 * @throws DBException exception
	 */
	public CdEmp update2(CdEmp cdEmp, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param cdEmp  Entity
	 * @param titaVo Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(CdEmp cdEmp, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param cdEmp  Entity of List
	 * @param titaVo Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<CdEmp> cdEmp, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param cdEmp  Entity of List
	 * @param titaVo Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<CdEmp> cdEmp, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param cdEmp  Entity of List
	 * @param titaVo Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<CdEmp> cdEmp, TitaVo... titaVo) throws DBException;

}
