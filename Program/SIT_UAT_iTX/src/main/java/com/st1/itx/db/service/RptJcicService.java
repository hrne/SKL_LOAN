package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.RptJcic;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.RptJcicId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface RptJcicService {

	/**
	 * findByPrimaryKey
	 *
	 * @param rptJcicId PK
	 * @param titaVo    Variable-Length Argument
	 * @return RptJcic RptJcic
	 */
	public RptJcic findById(RptJcicId rptJcicId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice RptJcic RptJcic of List
	 */
	public Slice<RptJcic> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * hold By RptJcic
	 * 
	 * @param rptJcicId key
	 * @param titaVo    Variable-Length Argument
	 * @return RptJcic RptJcic
	 */
	public RptJcic holdById(RptJcicId rptJcicId, TitaVo... titaVo);

	/**
	 * hold By RptJcic
	 * 
	 * @param rptJcic key
	 * @param titaVo  Variable-Length Argument
	 * @return RptJcic RptJcic
	 */
	public RptJcic holdById(RptJcic rptJcic, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param rptJcic Entity
	 * @param titaVo  Variable-Length Argument
	 * @return RptJcic Entity
	 * @throws DBException exception
	 */
	public RptJcic insert(RptJcic rptJcic, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param rptJcic Entity
	 * @param titaVo  Variable-Length Argument
	 * @return RptJcic Entity
	 * @throws DBException exception
	 */
	public RptJcic update(RptJcic rptJcic, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param rptJcic Entity
	 * @param titaVo  Variable-Length Argument
	 * @return RptJcic Entity
	 * @throws DBException exception
	 */
	public RptJcic update2(RptJcic rptJcic, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param rptJcic Entity
	 * @param titaVo  Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(RptJcic rptJcic, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param rptJcic Entity of List
	 * @param titaVo  Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<RptJcic> rptJcic, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param rptJcic Entity of List
	 * @param titaVo  Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<RptJcic> rptJcic, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param rptJcic Entity of List
	 * @param titaVo  Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<RptJcic> rptJcic, TitaVo... titaVo) throws DBException;

}
