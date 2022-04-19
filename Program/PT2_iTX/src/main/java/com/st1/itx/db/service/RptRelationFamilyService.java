package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.RptRelationFamily;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.RptRelationFamilyId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface RptRelationFamilyService {

	/**
	 * findByPrimaryKey
	 *
	 * @param rptRelationFamilyId PK
	 * @param titaVo              Variable-Length Argument
	 * @return RptRelationFamily RptRelationFamily
	 */
	public RptRelationFamily findById(RptRelationFamilyId rptRelationFamilyId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice RptRelationFamily RptRelationFamily of List
	 */
	public Slice<RptRelationFamily> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * hold By RptRelationFamily
	 * 
	 * @param rptRelationFamilyId key
	 * @param titaVo              Variable-Length Argument
	 * @return RptRelationFamily RptRelationFamily
	 */
	public RptRelationFamily holdById(RptRelationFamilyId rptRelationFamilyId, TitaVo... titaVo);

	/**
	 * hold By RptRelationFamily
	 * 
	 * @param rptRelationFamily key
	 * @param titaVo            Variable-Length Argument
	 * @return RptRelationFamily RptRelationFamily
	 */
	public RptRelationFamily holdById(RptRelationFamily rptRelationFamily, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param rptRelationFamily Entity
	 * @param titaVo            Variable-Length Argument
	 * @return RptRelationFamily Entity
	 * @throws DBException exception
	 */
	public RptRelationFamily insert(RptRelationFamily rptRelationFamily, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param rptRelationFamily Entity
	 * @param titaVo            Variable-Length Argument
	 * @return RptRelationFamily Entity
	 * @throws DBException exception
	 */
	public RptRelationFamily update(RptRelationFamily rptRelationFamily, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param rptRelationFamily Entity
	 * @param titaVo            Variable-Length Argument
	 * @return RptRelationFamily Entity
	 * @throws DBException exception
	 */
	public RptRelationFamily update2(RptRelationFamily rptRelationFamily, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param rptRelationFamily Entity
	 * @param titaVo            Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(RptRelationFamily rptRelationFamily, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param rptRelationFamily Entity of List
	 * @param titaVo            Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<RptRelationFamily> rptRelationFamily, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param rptRelationFamily Entity of List
	 * @param titaVo            Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<RptRelationFamily> rptRelationFamily, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param rptRelationFamily Entity of List
	 * @param titaVo            Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<RptRelationFamily> rptRelationFamily, TitaVo... titaVo) throws DBException;

}
