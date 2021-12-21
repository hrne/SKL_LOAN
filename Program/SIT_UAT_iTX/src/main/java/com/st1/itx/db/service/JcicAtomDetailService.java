package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicAtomDetail;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicAtomDetailId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicAtomDetailService {

	/**
	 * findByPrimaryKey
	 *
	 * @param jcicAtomDetailId PK
	 * @param titaVo           Variable-Length Argument
	 * @return JcicAtomDetail JcicAtomDetail
	 */
	public JcicAtomDetail findById(JcicAtomDetailId jcicAtomDetailId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice JcicAtomDetail JcicAtomDetail of List
	 */
	public Slice<JcicAtomDetail> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * FunctionCode =
	 *
	 * @param functionCode_0 functionCode_0
	 * @param index          Page Index
	 * @param limit          Page Data Limit
	 * @param titaVo         Variable-Length Argument
	 * @return Slice JcicAtomDetail JcicAtomDetail of List
	 */
	public Slice<JcicAtomDetail> findByFunctionCode(String functionCode_0, int index, int limit, TitaVo... titaVo);

	/**
	 * hold By JcicAtomDetail
	 * 
	 * @param jcicAtomDetailId key
	 * @param titaVo           Variable-Length Argument
	 * @return JcicAtomDetail JcicAtomDetail
	 */
	public JcicAtomDetail holdById(JcicAtomDetailId jcicAtomDetailId, TitaVo... titaVo);

	/**
	 * hold By JcicAtomDetail
	 * 
	 * @param jcicAtomDetail key
	 * @param titaVo         Variable-Length Argument
	 * @return JcicAtomDetail JcicAtomDetail
	 */
	public JcicAtomDetail holdById(JcicAtomDetail jcicAtomDetail, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param jcicAtomDetail Entity
	 * @param titaVo         Variable-Length Argument
	 * @return JcicAtomDetail Entity
	 * @throws DBException exception
	 */
	public JcicAtomDetail insert(JcicAtomDetail jcicAtomDetail, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param jcicAtomDetail Entity
	 * @param titaVo         Variable-Length Argument
	 * @return JcicAtomDetail Entity
	 * @throws DBException exception
	 */
	public JcicAtomDetail update(JcicAtomDetail jcicAtomDetail, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param jcicAtomDetail Entity
	 * @param titaVo         Variable-Length Argument
	 * @return JcicAtomDetail Entity
	 * @throws DBException exception
	 */
	public JcicAtomDetail update2(JcicAtomDetail jcicAtomDetail, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param jcicAtomDetail Entity
	 * @param titaVo         Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(JcicAtomDetail jcicAtomDetail, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param jcicAtomDetail Entity of List
	 * @param titaVo         Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<JcicAtomDetail> jcicAtomDetail, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param jcicAtomDetail Entity of List
	 * @param titaVo         Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<JcicAtomDetail> jcicAtomDetail, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param jcicAtomDetail Entity of List
	 * @param titaVo         Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<JcicAtomDetail> jcicAtomDetail, TitaVo... titaVo) throws DBException;

}
