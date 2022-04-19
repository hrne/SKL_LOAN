package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CdInsurer;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.CdInsurerId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CdInsurerService {

	/**
	 * findByPrimaryKey
	 *
	 * @param cdInsurerId PK
	 * @param titaVo      Variable-Length Argument
	 * @return CdInsurer CdInsurer
	 */
	public CdInsurer findById(CdInsurerId cdInsurerId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice CdInsurer CdInsurer of List
	 */
	public Slice<CdInsurer> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * InsurerItem %
	 *
	 * @param insurerItem_0 insurerItem_0
	 * @param index         Page Index
	 * @param limit         Page Data Limit
	 * @param titaVo        Variable-Length Argument
	 * @return Slice CdInsurer CdInsurer of List
	 */
	public Slice<CdInsurer> insurerItemLike(String insurerItem_0, int index, int limit, TitaVo... titaVo);

	/**
	 * InsurerType &gt;= ,AND InsurerType &lt;= ,AND InsurerCode &gt;= ,AND
	 * InsurerCode &lt;=
	 *
	 * @param insurerType_0 insurerType_0
	 * @param insurerType_1 insurerType_1
	 * @param insurerCode_2 insurerCode_2
	 * @param insurerCode_3 insurerCode_3
	 * @param index         Page Index
	 * @param limit         Page Data Limit
	 * @param titaVo        Variable-Length Argument
	 * @return Slice CdInsurer CdInsurer of List
	 */
	public Slice<CdInsurer> insurerTypeRange(String insurerType_0, String insurerType_1, String insurerCode_2, String insurerCode_3, int index, int limit, TitaVo... titaVo);

	/**
	 * hold By CdInsurer
	 * 
	 * @param cdInsurerId key
	 * @param titaVo      Variable-Length Argument
	 * @return CdInsurer CdInsurer
	 */
	public CdInsurer holdById(CdInsurerId cdInsurerId, TitaVo... titaVo);

	/**
	 * hold By CdInsurer
	 * 
	 * @param cdInsurer key
	 * @param titaVo    Variable-Length Argument
	 * @return CdInsurer CdInsurer
	 */
	public CdInsurer holdById(CdInsurer cdInsurer, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param cdInsurer Entity
	 * @param titaVo    Variable-Length Argument
	 * @return CdInsurer Entity
	 * @throws DBException exception
	 */
	public CdInsurer insert(CdInsurer cdInsurer, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param cdInsurer Entity
	 * @param titaVo    Variable-Length Argument
	 * @return CdInsurer Entity
	 * @throws DBException exception
	 */
	public CdInsurer update(CdInsurer cdInsurer, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param cdInsurer Entity
	 * @param titaVo    Variable-Length Argument
	 * @return CdInsurer Entity
	 * @throws DBException exception
	 */
	public CdInsurer update2(CdInsurer cdInsurer, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param cdInsurer Entity
	 * @param titaVo    Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(CdInsurer cdInsurer, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param cdInsurer Entity of List
	 * @param titaVo    Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<CdInsurer> cdInsurer, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param cdInsurer Entity of List
	 * @param titaVo    Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<CdInsurer> cdInsurer, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param cdInsurer Entity of List
	 * @param titaVo    Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<CdInsurer> cdInsurer, TitaVo... titaVo) throws DBException;

}
