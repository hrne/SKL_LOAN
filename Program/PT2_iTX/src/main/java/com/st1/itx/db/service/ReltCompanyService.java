package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.ReltCompany;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.ReltCompanyId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface ReltCompanyService {

	/**
	 * findByPrimaryKey
	 *
	 * @param reltCompanyId PK
	 * @param titaVo        Variable-Length Argument
	 * @return ReltCompany ReltCompany
	 */
	public ReltCompany findById(ReltCompanyId reltCompanyId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice ReltCompany ReltCompany of List
	 */
	public Slice<ReltCompany> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * ReltUKey =
	 *
	 * @param reltUKey_0 reltUKey_0
	 * @param index      Page Index
	 * @param limit      Page Data Limit
	 * @param titaVo     Variable-Length Argument
	 * @return Slice ReltCompany ReltCompany of List
	 */
	public Slice<ReltCompany> ReltUKeyEq(String reltUKey_0, int index, int limit, TitaVo... titaVo);

	/**
	 * hold By ReltCompany
	 * 
	 * @param reltCompanyId key
	 * @param titaVo        Variable-Length Argument
	 * @return ReltCompany ReltCompany
	 */
	public ReltCompany holdById(ReltCompanyId reltCompanyId, TitaVo... titaVo);

	/**
	 * hold By ReltCompany
	 * 
	 * @param reltCompany key
	 * @param titaVo      Variable-Length Argument
	 * @return ReltCompany ReltCompany
	 */
	public ReltCompany holdById(ReltCompany reltCompany, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param reltCompany Entity
	 * @param titaVo      Variable-Length Argument
	 * @return ReltCompany Entity
	 * @throws DBException exception
	 */
	public ReltCompany insert(ReltCompany reltCompany, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param reltCompany Entity
	 * @param titaVo      Variable-Length Argument
	 * @return ReltCompany Entity
	 * @throws DBException exception
	 */
	public ReltCompany update(ReltCompany reltCompany, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param reltCompany Entity
	 * @param titaVo      Variable-Length Argument
	 * @return ReltCompany Entity
	 * @throws DBException exception
	 */
	public ReltCompany update2(ReltCompany reltCompany, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param reltCompany Entity
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(ReltCompany reltCompany, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param reltCompany Entity of List
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<ReltCompany> reltCompany, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param reltCompany Entity of List
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<ReltCompany> reltCompany, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param reltCompany Entity of List
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<ReltCompany> reltCompany, TitaVo... titaVo) throws DBException;

}
