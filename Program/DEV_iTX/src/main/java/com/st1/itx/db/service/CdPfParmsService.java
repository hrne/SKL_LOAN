package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CdPfParms;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.CdPfParmsId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CdPfParmsService {

	/**
	 * findByPrimaryKey
	 *
	 * @param cdPfParmsId PK
	 * @param titaVo      Variable-Length Argument
	 * @return CdPfParms CdPfParms
	 */
	public CdPfParms findById(CdPfParmsId cdPfParmsId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice CdPfParms CdPfParms of List
	 */
	public Slice<CdPfParms> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * ConditionCode1 =
	 *
	 * @param conditionCode1_0 conditionCode1_0
	 * @param index            Page Index
	 * @param limit            Page Data Limit
	 * @param titaVo           Variable-Length Argument
	 * @return Slice CdPfParms CdPfParms of List
	 */
	public Slice<CdPfParms> findConditionCode1Eq(String conditionCode1_0, int index, int limit, TitaVo... titaVo);

	/**
	 * ConditionCode1 = ,AND ConditionCode2 =
	 *
	 * @param conditionCode1_0 conditionCode1_0
	 * @param conditionCode2_1 conditionCode2_1
	 * @param index            Page Index
	 * @param limit            Page Data Limit
	 * @param titaVo           Variable-Length Argument
	 * @return Slice CdPfParms CdPfParms of List
	 */
	public Slice<CdPfParms> findCode1AndCode2Eq(String conditionCode1_0, String conditionCode2_1, int index, int limit, TitaVo... titaVo);

	/**
	 * hold By CdPfParms
	 * 
	 * @param cdPfParmsId key
	 * @param titaVo      Variable-Length Argument
	 * @return CdPfParms CdPfParms
	 */
	public CdPfParms holdById(CdPfParmsId cdPfParmsId, TitaVo... titaVo);

	/**
	 * hold By CdPfParms
	 * 
	 * @param cdPfParms key
	 * @param titaVo    Variable-Length Argument
	 * @return CdPfParms CdPfParms
	 */
	public CdPfParms holdById(CdPfParms cdPfParms, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param cdPfParms Entity
	 * @param titaVo    Variable-Length Argument
	 * @return CdPfParms Entity
	 * @throws DBException exception
	 */
	public CdPfParms insert(CdPfParms cdPfParms, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param cdPfParms Entity
	 * @param titaVo    Variable-Length Argument
	 * @return CdPfParms Entity
	 * @throws DBException exception
	 */
	public CdPfParms update(CdPfParms cdPfParms, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param cdPfParms Entity
	 * @param titaVo    Variable-Length Argument
	 * @return CdPfParms Entity
	 * @throws DBException exception
	 */
	public CdPfParms update2(CdPfParms cdPfParms, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param cdPfParms Entity
	 * @param titaVo    Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(CdPfParms cdPfParms, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param cdPfParms Entity of List
	 * @param titaVo    Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<CdPfParms> cdPfParms, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param cdPfParms Entity of List
	 * @param titaVo    Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<CdPfParms> cdPfParms, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param cdPfParms Entity of List
	 * @param titaVo    Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<CdPfParms> cdPfParms, TitaVo... titaVo) throws DBException;

}
