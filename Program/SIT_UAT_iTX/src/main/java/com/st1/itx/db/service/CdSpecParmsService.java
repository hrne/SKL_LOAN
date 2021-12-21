package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CdSpecParms;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.CdSpecParmsId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CdSpecParmsService {

	/**
	 * findByPrimaryKey
	 *
	 * @param cdSpecParmsId PK
	 * @param titaVo        Variable-Length Argument
	 * @return CdSpecParms CdSpecParms
	 */
	public CdSpecParms findById(CdSpecParmsId cdSpecParmsId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice CdSpecParms CdSpecParms of List
	 */
	public Slice<CdSpecParms> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * hold By CdSpecParms
	 * 
	 * @param cdSpecParmsId key
	 * @param titaVo        Variable-Length Argument
	 * @return CdSpecParms CdSpecParms
	 */
	public CdSpecParms holdById(CdSpecParmsId cdSpecParmsId, TitaVo... titaVo);

	/**
	 * hold By CdSpecParms
	 * 
	 * @param cdSpecParms key
	 * @param titaVo      Variable-Length Argument
	 * @return CdSpecParms CdSpecParms
	 */
	public CdSpecParms holdById(CdSpecParms cdSpecParms, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param cdSpecParms Entity
	 * @param titaVo      Variable-Length Argument
	 * @return CdSpecParms Entity
	 * @throws DBException exception
	 */
	public CdSpecParms insert(CdSpecParms cdSpecParms, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param cdSpecParms Entity
	 * @param titaVo      Variable-Length Argument
	 * @return CdSpecParms Entity
	 * @throws DBException exception
	 */
	public CdSpecParms update(CdSpecParms cdSpecParms, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param cdSpecParms Entity
	 * @param titaVo      Variable-Length Argument
	 * @return CdSpecParms Entity
	 * @throws DBException exception
	 */
	public CdSpecParms update2(CdSpecParms cdSpecParms, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param cdSpecParms Entity
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(CdSpecParms cdSpecParms, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param cdSpecParms Entity of List
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<CdSpecParms> cdSpecParms, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param cdSpecParms Entity of List
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<CdSpecParms> cdSpecParms, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param cdSpecParms Entity of List
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<CdSpecParms> cdSpecParms, TitaVo... titaVo) throws DBException;

}
