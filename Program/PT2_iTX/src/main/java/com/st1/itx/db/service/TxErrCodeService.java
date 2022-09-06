package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.TxErrCode;
import org.springframework.data.domain.Slice;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface TxErrCodeService {

	/**
	 * findByPrimaryKey
	 *
	 * @param errCode PK
	 * @param titaVo  Variable-Length Argument
	 * @return TxErrCode TxErrCode
	 */
	public TxErrCode findById(String errCode, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice TxErrCode TxErrCode of List
	 */
	public Slice<TxErrCode> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * ErrCode %
	 *
	 * @param errCode_0 errCode_0
	 * @param index     Page Index
	 * @param limit     Page Data Limit
	 * @param titaVo    Variable-Length Argument
	 * @return Slice TxErrCode TxErrCode of List
	 */
	public Slice<TxErrCode> findbyErrCode(String errCode_0, int index, int limit, TitaVo... titaVo);

	/**
	 * hold By TxErrCode
	 * 
	 * @param errCode key
	 * @param titaVo  Variable-Length Argument
	 * @return TxErrCode TxErrCode
	 */
	public TxErrCode holdById(String errCode, TitaVo... titaVo);

	/**
	 * hold By TxErrCode
	 * 
	 * @param txErrCode key
	 * @param titaVo    Variable-Length Argument
	 * @return TxErrCode TxErrCode
	 */
	public TxErrCode holdById(TxErrCode txErrCode, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param txErrCode Entity
	 * @param titaVo    Variable-Length Argument
	 * @return TxErrCode Entity
	 * @throws DBException exception
	 */
	public TxErrCode insert(TxErrCode txErrCode, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param txErrCode Entity
	 * @param titaVo    Variable-Length Argument
	 * @return TxErrCode Entity
	 * @throws DBException exception
	 */
	public TxErrCode update(TxErrCode txErrCode, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param txErrCode Entity
	 * @param titaVo    Variable-Length Argument
	 * @return TxErrCode Entity
	 * @throws DBException exception
	 */
	public TxErrCode update2(TxErrCode txErrCode, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param txErrCode Entity
	 * @param titaVo    Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(TxErrCode txErrCode, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param txErrCode Entity of List
	 * @param titaVo    Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<TxErrCode> txErrCode, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param txErrCode Entity of List
	 * @param titaVo    Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<TxErrCode> txErrCode, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param txErrCode Entity of List
	 * @param titaVo    Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<TxErrCode> txErrCode, TitaVo... titaVo) throws DBException;

}
