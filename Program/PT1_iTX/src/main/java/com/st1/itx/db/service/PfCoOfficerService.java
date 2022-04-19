package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.PfCoOfficer;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.PfCoOfficerId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface PfCoOfficerService {

	/**
	 * findByPrimaryKey
	 *
	 * @param pfCoOfficerId PK
	 * @param titaVo        Variable-Length Argument
	 * @return PfCoOfficer PfCoOfficer
	 */
	public PfCoOfficer findById(PfCoOfficerId pfCoOfficerId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice PfCoOfficer PfCoOfficer of List
	 */
	public Slice<PfCoOfficer> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * EmpNo = ,AND EffectiveDate &gt;= ,AND EffectiveDate &lt;=
	 *
	 * @param empNo_0         empNo_0
	 * @param effectiveDate_1 effectiveDate_1
	 * @param effectiveDate_2 effectiveDate_2
	 * @param titaVo          Variable-Length Argument
	 * @return Slice PfCoOfficer PfCoOfficer of List
	 */
	public PfCoOfficer effectiveDateFirst(String empNo_0, int effectiveDate_1, int effectiveDate_2, TitaVo... titaVo);

	/**
	 * EmpNo =
	 *
	 * @param empNo_0 empNo_0
	 * @param index   Page Index
	 * @param limit   Page Data Limit
	 * @param titaVo  Variable-Length Argument
	 * @return Slice PfCoOfficer PfCoOfficer of List
	 */
	public Slice<PfCoOfficer> findByEmpNo(String empNo_0, int index, int limit, TitaVo... titaVo);

	/**
	 * EmpNo =
	 *
	 * @param empNo_0 empNo_0
	 * @param titaVo  Variable-Length Argument
	 * @return Slice PfCoOfficer PfCoOfficer of List
	 */
	public PfCoOfficer findByEmpNoFirst(String empNo_0, TitaVo... titaVo);

	/**
	 * EffectiveDate &gt;= ,AND EffectiveDate &lt;=
	 *
	 * @param effectiveDate_0 effectiveDate_0
	 * @param effectiveDate_1 effectiveDate_1
	 * @param index           Page Index
	 * @param limit           Page Data Limit
	 * @param titaVo          Variable-Length Argument
	 * @return Slice PfCoOfficer PfCoOfficer of List
	 */
	public Slice<PfCoOfficer> findByEffectiveDateDate(int effectiveDate_0, int effectiveDate_1, int index, int limit, TitaVo... titaVo);

	/**
	 * EmpNo = ,AND EffectiveDate &gt;= ,AND EffectiveDate &lt;=
	 *
	 * @param empNo_0         empNo_0
	 * @param effectiveDate_1 effectiveDate_1
	 * @param effectiveDate_2 effectiveDate_2
	 * @param index           Page Index
	 * @param limit           Page Data Limit
	 * @param titaVo          Variable-Length Argument
	 * @return Slice PfCoOfficer PfCoOfficer of List
	 */
	public Slice<PfCoOfficer> effectiveDateEq(String empNo_0, int effectiveDate_1, int effectiveDate_2, int index, int limit, TitaVo... titaVo);

	/**
	 * EffectiveDate &gt;
	 *
	 * @param effectiveDate_0 effectiveDate_0
	 * @param index           Page Index
	 * @param limit           Page Data Limit
	 * @param titaVo          Variable-Length Argument
	 * @return Slice PfCoOfficer PfCoOfficer of List
	 */
	public Slice<PfCoOfficer> findNotYet(int effectiveDate_0, int index, int limit, TitaVo... titaVo);

	/**
	 * IneffectiveDate &lt;
	 *
	 * @param ineffectiveDate_0 ineffectiveDate_0
	 * @param index             Page Index
	 * @param limit             Page Data Limit
	 * @param titaVo            Variable-Length Argument
	 * @return Slice PfCoOfficer PfCoOfficer of List
	 */
	public Slice<PfCoOfficer> findOutOf(int ineffectiveDate_0, int index, int limit, TitaVo... titaVo);

	/**
	 * EffectiveDate &gt;=
	 *
	 * @param effectiveDate_0 effectiveDate_0
	 * @param index           Page Index
	 * @param limit           Page Data Limit
	 * @param titaVo          Variable-Length Argument
	 * @return Slice PfCoOfficer PfCoOfficer of List
	 */
	public Slice<PfCoOfficer> findIng(int effectiveDate_0, int index, int limit, TitaVo... titaVo);

	/**
	 * hold By PfCoOfficer
	 * 
	 * @param pfCoOfficerId key
	 * @param titaVo        Variable-Length Argument
	 * @return PfCoOfficer PfCoOfficer
	 */
	public PfCoOfficer holdById(PfCoOfficerId pfCoOfficerId, TitaVo... titaVo);

	/**
	 * hold By PfCoOfficer
	 * 
	 * @param pfCoOfficer key
	 * @param titaVo      Variable-Length Argument
	 * @return PfCoOfficer PfCoOfficer
	 */
	public PfCoOfficer holdById(PfCoOfficer pfCoOfficer, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param pfCoOfficer Entity
	 * @param titaVo      Variable-Length Argument
	 * @return PfCoOfficer Entity
	 * @throws DBException exception
	 */
	public PfCoOfficer insert(PfCoOfficer pfCoOfficer, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param pfCoOfficer Entity
	 * @param titaVo      Variable-Length Argument
	 * @return PfCoOfficer Entity
	 * @throws DBException exception
	 */
	public PfCoOfficer update(PfCoOfficer pfCoOfficer, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param pfCoOfficer Entity
	 * @param titaVo      Variable-Length Argument
	 * @return PfCoOfficer Entity
	 * @throws DBException exception
	 */
	public PfCoOfficer update2(PfCoOfficer pfCoOfficer, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param pfCoOfficer Entity
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(PfCoOfficer pfCoOfficer, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param pfCoOfficer Entity of List
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<PfCoOfficer> pfCoOfficer, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param pfCoOfficer Entity of List
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<PfCoOfficer> pfCoOfficer, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param pfCoOfficer Entity of List
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<PfCoOfficer> pfCoOfficer, TitaVo... titaVo) throws DBException;

}
