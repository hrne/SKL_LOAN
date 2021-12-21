package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.RptRelationSelf;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.RptRelationSelfId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface RptRelationSelfService {

	/**
	 * findByPrimaryKey
	 *
	 * @param rptRelationSelfId PK
	 * @param titaVo            Variable-Length Argument
	 * @return RptRelationSelf RptRelationSelf
	 */
	public RptRelationSelf findById(RptRelationSelfId rptRelationSelfId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice RptRelationSelf RptRelationSelf of List
	 */
	public Slice<RptRelationSelf> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * hold By RptRelationSelf
	 * 
	 * @param rptRelationSelfId key
	 * @param titaVo            Variable-Length Argument
	 * @return RptRelationSelf RptRelationSelf
	 */
	public RptRelationSelf holdById(RptRelationSelfId rptRelationSelfId, TitaVo... titaVo);

	/**
	 * hold By RptRelationSelf
	 * 
	 * @param rptRelationSelf key
	 * @param titaVo          Variable-Length Argument
	 * @return RptRelationSelf RptRelationSelf
	 */
	public RptRelationSelf holdById(RptRelationSelf rptRelationSelf, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param rptRelationSelf Entity
	 * @param titaVo          Variable-Length Argument
	 * @return RptRelationSelf Entity
	 * @throws DBException exception
	 */
	public RptRelationSelf insert(RptRelationSelf rptRelationSelf, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param rptRelationSelf Entity
	 * @param titaVo          Variable-Length Argument
	 * @return RptRelationSelf Entity
	 * @throws DBException exception
	 */
	public RptRelationSelf update(RptRelationSelf rptRelationSelf, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param rptRelationSelf Entity
	 * @param titaVo          Variable-Length Argument
	 * @return RptRelationSelf Entity
	 * @throws DBException exception
	 */
	public RptRelationSelf update2(RptRelationSelf rptRelationSelf, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param rptRelationSelf Entity
	 * @param titaVo          Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(RptRelationSelf rptRelationSelf, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param rptRelationSelf Entity of List
	 * @param titaVo          Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<RptRelationSelf> rptRelationSelf, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param rptRelationSelf Entity of List
	 * @param titaVo          Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<RptRelationSelf> rptRelationSelf, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param rptRelationSelf Entity of List
	 * @param titaVo          Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<RptRelationSelf> rptRelationSelf, TitaVo... titaVo) throws DBException;

}
