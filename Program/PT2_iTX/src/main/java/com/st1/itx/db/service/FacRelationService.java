package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.FacRelation;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.FacRelationId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface FacRelationService {

	/**
	 * findByPrimaryKey
	 *
	 * @param facRelationId PK
	 * @param titaVo        Variable-Length Argument
	 * @return FacRelation FacRelation
	 */
	public FacRelation findById(FacRelationId facRelationId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice FacRelation FacRelation of List
	 */
	public Slice<FacRelation> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * CreditSysNo =
	 *
	 * @param creditSysNo_0 creditSysNo_0
	 * @param index         Page Index
	 * @param limit         Page Data Limit
	 * @param titaVo        Variable-Length Argument
	 * @return Slice FacRelation FacRelation of List
	 */
	public Slice<FacRelation> CreditSysNoAll(int creditSysNo_0, int index, int limit, TitaVo... titaVo);

	/**
	 * CustUKey =
	 *
	 * @param custUKey_0 custUKey_0
	 * @param index      Page Index
	 * @param limit      Page Data Limit
	 * @param titaVo     Variable-Length Argument
	 * @return Slice FacRelation FacRelation of List
	 */
	public Slice<FacRelation> CustUKeyAll(String custUKey_0, int index, int limit, TitaVo... titaVo);

	/**
	 * hold By FacRelation
	 * 
	 * @param facRelationId key
	 * @param titaVo        Variable-Length Argument
	 * @return FacRelation FacRelation
	 */
	public FacRelation holdById(FacRelationId facRelationId, TitaVo... titaVo);

	/**
	 * hold By FacRelation
	 * 
	 * @param facRelation key
	 * @param titaVo      Variable-Length Argument
	 * @return FacRelation FacRelation
	 */
	public FacRelation holdById(FacRelation facRelation, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param facRelation Entity
	 * @param titaVo      Variable-Length Argument
	 * @return FacRelation Entity
	 * @throws DBException exception
	 */
	public FacRelation insert(FacRelation facRelation, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param facRelation Entity
	 * @param titaVo      Variable-Length Argument
	 * @return FacRelation Entity
	 * @throws DBException exception
	 */
	public FacRelation update(FacRelation facRelation, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param facRelation Entity
	 * @param titaVo      Variable-Length Argument
	 * @return FacRelation Entity
	 * @throws DBException exception
	 */
	public FacRelation update2(FacRelation facRelation, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param facRelation Entity
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(FacRelation facRelation, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param facRelation Entity of List
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<FacRelation> facRelation, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param facRelation Entity of List
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<FacRelation> facRelation, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param facRelation Entity of List
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<FacRelation> facRelation, TitaVo... titaVo) throws DBException;

}
