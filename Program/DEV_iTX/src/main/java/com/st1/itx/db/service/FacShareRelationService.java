package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.FacShareRelation;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.FacShareRelationId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface FacShareRelationService {

	/**
	 * findByPrimaryKey
	 *
	 * @param facShareRelationId PK
	 * @param titaVo             Variable-Length Argument
	 * @return FacShareRelation FacShareRelation
	 */
	public FacShareRelation findById(FacShareRelationId facShareRelationId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice FacShareRelation FacShareRelation of List
	 */
	public Slice<FacShareRelation> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * ApplNo =
	 *
	 * @param applNo_0 applNo_0
	 * @param index    Page Index
	 * @param limit    Page Data Limit
	 * @param titaVo   Variable-Length Argument
	 * @return Slice FacShareRelation FacShareRelation of List
	 */
	public Slice<FacShareRelation> ApplNoAll(int applNo_0, int index, int limit, TitaVo... titaVo);

	/**
	 * hold By FacShareRelation
	 * 
	 * @param facShareRelationId key
	 * @param titaVo             Variable-Length Argument
	 * @return FacShareRelation FacShareRelation
	 */
	public FacShareRelation holdById(FacShareRelationId facShareRelationId, TitaVo... titaVo);

	/**
	 * hold By FacShareRelation
	 * 
	 * @param facShareRelation key
	 * @param titaVo           Variable-Length Argument
	 * @return FacShareRelation FacShareRelation
	 */
	public FacShareRelation holdById(FacShareRelation facShareRelation, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param facShareRelation Entity
	 * @param titaVo           Variable-Length Argument
	 * @return FacShareRelation Entity
	 * @throws DBException exception
	 */
	public FacShareRelation insert(FacShareRelation facShareRelation, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param facShareRelation Entity
	 * @param titaVo           Variable-Length Argument
	 * @return FacShareRelation Entity
	 * @throws DBException exception
	 */
	public FacShareRelation update(FacShareRelation facShareRelation, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param facShareRelation Entity
	 * @param titaVo           Variable-Length Argument
	 * @return FacShareRelation Entity
	 * @throws DBException exception
	 */
	public FacShareRelation update2(FacShareRelation facShareRelation, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param facShareRelation Entity
	 * @param titaVo           Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(FacShareRelation facShareRelation, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param facShareRelation Entity of List
	 * @param titaVo           Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<FacShareRelation> facShareRelation, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param facShareRelation Entity of List
	 * @param titaVo           Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<FacShareRelation> facShareRelation, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param facShareRelation Entity of List
	 * @param titaVo           Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<FacShareRelation> facShareRelation, TitaVo... titaVo) throws DBException;

}
