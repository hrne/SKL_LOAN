package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CdAppraisalCompany;
import org.springframework.data.domain.Slice;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CdAppraisalCompanyService {

	/**
	 * findByPrimaryKey
	 *
	 * @param appraisalCompany PK
	 * @param titaVo           Variable-Length Argument
	 * @return CdAppraisalCompany CdAppraisalCompany
	 */
	public CdAppraisalCompany findById(String appraisalCompany, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice CdAppraisalCompany CdAppraisalCompany of List
	 */
	public Slice<CdAppraisalCompany> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * hold By CdAppraisalCompany
	 * 
	 * @param appraisalCompany key
	 * @param titaVo           Variable-Length Argument
	 * @return CdAppraisalCompany CdAppraisalCompany
	 */
	public CdAppraisalCompany holdById(String appraisalCompany, TitaVo... titaVo);

	/**
	 * hold By CdAppraisalCompany
	 * 
	 * @param cdAppraisalCompany key
	 * @param titaVo             Variable-Length Argument
	 * @return CdAppraisalCompany CdAppraisalCompany
	 */
	public CdAppraisalCompany holdById(CdAppraisalCompany cdAppraisalCompany, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param cdAppraisalCompany Entity
	 * @param titaVo             Variable-Length Argument
	 * @return CdAppraisalCompany Entity
	 * @throws DBException exception
	 */
	public CdAppraisalCompany insert(CdAppraisalCompany cdAppraisalCompany, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param cdAppraisalCompany Entity
	 * @param titaVo             Variable-Length Argument
	 * @return CdAppraisalCompany Entity
	 * @throws DBException exception
	 */
	public CdAppraisalCompany update(CdAppraisalCompany cdAppraisalCompany, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param cdAppraisalCompany Entity
	 * @param titaVo             Variable-Length Argument
	 * @return CdAppraisalCompany Entity
	 * @throws DBException exception
	 */
	public CdAppraisalCompany update2(CdAppraisalCompany cdAppraisalCompany, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param cdAppraisalCompany Entity
	 * @param titaVo             Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(CdAppraisalCompany cdAppraisalCompany, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param cdAppraisalCompany Entity of List
	 * @param titaVo             Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<CdAppraisalCompany> cdAppraisalCompany, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param cdAppraisalCompany Entity of List
	 * @param titaVo             Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<CdAppraisalCompany> cdAppraisalCompany, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param cdAppraisalCompany Entity of List
	 * @param titaVo             Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<CdAppraisalCompany> cdAppraisalCompany, TitaVo... titaVo) throws DBException;

}
