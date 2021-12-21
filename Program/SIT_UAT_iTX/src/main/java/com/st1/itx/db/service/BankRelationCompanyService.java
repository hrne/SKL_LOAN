package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.BankRelationCompany;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.BankRelationCompanyId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface BankRelationCompanyService {

	/**
	 * findByPrimaryKey
	 *
	 * @param bankRelationCompanyId PK
	 * @param titaVo                Variable-Length Argument
	 * @return BankRelationCompany BankRelationCompany
	 */
	public BankRelationCompany findById(BankRelationCompanyId bankRelationCompanyId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice BankRelationCompany BankRelationCompany of List
	 */
	public Slice<BankRelationCompany> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * CompanyId =
	 *
	 * @param companyId_0 companyId_0
	 * @param index       Page Index
	 * @param limit       Page Data Limit
	 * @param titaVo      Variable-Length Argument
	 * @return Slice BankRelationCompany BankRelationCompany of List
	 */
	public Slice<BankRelationCompany> findCompanyIdEq(String companyId_0, int index, int limit, TitaVo... titaVo);

	/**
	 * CustId =
	 *
	 * @param custId_0 custId_0
	 * @param titaVo   Variable-Length Argument
	 * @return Slice BankRelationCompany BankRelationCompany of List
	 */
	public BankRelationCompany custIdFirst(String custId_0, TitaVo... titaVo);

	/**
	 * hold By BankRelationCompany
	 * 
	 * @param bankRelationCompanyId key
	 * @param titaVo                Variable-Length Argument
	 * @return BankRelationCompany BankRelationCompany
	 */
	public BankRelationCompany holdById(BankRelationCompanyId bankRelationCompanyId, TitaVo... titaVo);

	/**
	 * hold By BankRelationCompany
	 * 
	 * @param bankRelationCompany key
	 * @param titaVo              Variable-Length Argument
	 * @return BankRelationCompany BankRelationCompany
	 */
	public BankRelationCompany holdById(BankRelationCompany bankRelationCompany, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param bankRelationCompany Entity
	 * @param titaVo              Variable-Length Argument
	 * @return BankRelationCompany Entity
	 * @throws DBException exception
	 */
	public BankRelationCompany insert(BankRelationCompany bankRelationCompany, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param bankRelationCompany Entity
	 * @param titaVo              Variable-Length Argument
	 * @return BankRelationCompany Entity
	 * @throws DBException exception
	 */
	public BankRelationCompany update(BankRelationCompany bankRelationCompany, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param bankRelationCompany Entity
	 * @param titaVo              Variable-Length Argument
	 * @return BankRelationCompany Entity
	 * @throws DBException exception
	 */
	public BankRelationCompany update2(BankRelationCompany bankRelationCompany, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param bankRelationCompany Entity
	 * @param titaVo              Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(BankRelationCompany bankRelationCompany, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param bankRelationCompany Entity of List
	 * @param titaVo              Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<BankRelationCompany> bankRelationCompany, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param bankRelationCompany Entity of List
	 * @param titaVo              Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<BankRelationCompany> bankRelationCompany, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param bankRelationCompany Entity of List
	 * @param titaVo              Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<BankRelationCompany> bankRelationCompany, TitaVo... titaVo) throws DBException;

}
