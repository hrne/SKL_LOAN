package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.RelsCompany;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.RelsCompanyId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface RelsCompanyService {

	/**
	 * findByPrimaryKey
	 *
	 * @param relsCompanyId PK
	 * @param titaVo        Variable-Length Argument
	 * @return RelsCompany RelsCompany
	 */
	public RelsCompany findById(RelsCompanyId relsCompanyId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice RelsCompany RelsCompany of List
	 */
	public Slice<RelsCompany> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * RelsUKey =
	 *
	 * @param relsUKey_0 relsUKey_0
	 * @param index      Page Index
	 * @param limit      Page Data Limit
	 * @param titaVo     Variable-Length Argument
	 * @return Slice RelsCompany RelsCompany of List
	 */
	public Slice<RelsCompany> RelsUKeyEq(String relsUKey_0, int index, int limit, TitaVo... titaVo);

	/**
	 * CompanyId =
	 *
	 * @param companyId_0 companyId_0
	 * @param index       Page Index
	 * @param limit       Page Data Limit
	 * @param titaVo      Variable-Length Argument
	 * @return Slice RelsCompany RelsCompany of List
	 */
	public Slice<RelsCompany> findCompanyIdEq(String companyId_0, int index, int limit, TitaVo... titaVo);

	/**
	 * CompanyName =
	 *
	 * @param companyName_0 companyName_0
	 * @param index         Page Index
	 * @param limit         Page Data Limit
	 * @param titaVo        Variable-Length Argument
	 * @return Slice RelsCompany RelsCompany of List
	 */
	public Slice<RelsCompany> findCompanyNameEq(String companyName_0, int index, int limit, TitaVo... titaVo);

	/**
	 * hold By RelsCompany
	 * 
	 * @param relsCompanyId key
	 * @param titaVo        Variable-Length Argument
	 * @return RelsCompany RelsCompany
	 */
	public RelsCompany holdById(RelsCompanyId relsCompanyId, TitaVo... titaVo);

	/**
	 * hold By RelsCompany
	 * 
	 * @param relsCompany key
	 * @param titaVo      Variable-Length Argument
	 * @return RelsCompany RelsCompany
	 */
	public RelsCompany holdById(RelsCompany relsCompany, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param relsCompany Entity
	 * @param titaVo      Variable-Length Argument
	 * @return RelsCompany Entity
	 * @throws DBException exception
	 */
	public RelsCompany insert(RelsCompany relsCompany, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param relsCompany Entity
	 * @param titaVo      Variable-Length Argument
	 * @return RelsCompany Entity
	 * @throws DBException exception
	 */
	public RelsCompany update(RelsCompany relsCompany, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param relsCompany Entity
	 * @param titaVo      Variable-Length Argument
	 * @return RelsCompany Entity
	 * @throws DBException exception
	 */
	public RelsCompany update2(RelsCompany relsCompany, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param relsCompany Entity
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(RelsCompany relsCompany, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param relsCompany Entity of List
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<RelsCompany> relsCompany, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param relsCompany Entity of List
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<RelsCompany> relsCompany, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param relsCompany Entity of List
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<RelsCompany> relsCompany, TitaVo... titaVo) throws DBException;

}
