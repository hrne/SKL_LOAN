package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.TxBizDate;
import org.springframework.data.domain.Slice;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface TxBizDateService {

	/**
	 * findByPrimaryKey
	 *
	 * @param dateCode PK
	 * @param titaVo   Variable-Length Argument
	 * @return TxBizDate TxBizDate
	 */
	public TxBizDate findById(String dateCode, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice TxBizDate TxBizDate of List
	 */
	public Slice<TxBizDate> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * hold By TxBizDate
	 * 
	 * @param dateCode key
	 * @param titaVo   Variable-Length Argument
	 * @return TxBizDate TxBizDate
	 */
	public TxBizDate holdById(String dateCode, TitaVo... titaVo);

	/**
	 * hold By TxBizDate
	 * 
	 * @param txBizDate key
	 * @param titaVo    Variable-Length Argument
	 * @return TxBizDate TxBizDate
	 */
	public TxBizDate holdById(TxBizDate txBizDate, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param txBizDate Entity
	 * @param titaVo    Variable-Length Argument
	 * @return TxBizDate Entity
	 * @throws DBException exception
	 */
	public TxBizDate insert(TxBizDate txBizDate, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param txBizDate Entity
	 * @param titaVo    Variable-Length Argument
	 * @return TxBizDate Entity
	 * @throws DBException exception
	 */
	public TxBizDate update(TxBizDate txBizDate, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param txBizDate Entity
	 * @param titaVo    Variable-Length Argument
	 * @return TxBizDate Entity
	 * @throws DBException exception
	 */
	public TxBizDate update2(TxBizDate txBizDate, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param txBizDate Entity
	 * @param titaVo    Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(TxBizDate txBizDate, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param txBizDate Entity of List
	 * @param titaVo    Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<TxBizDate> txBizDate, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param txBizDate Entity of List
	 * @param titaVo    Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<TxBizDate> txBizDate, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param txBizDate Entity of List
	 * @param titaVo    Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<TxBizDate> txBizDate, TitaVo... titaVo) throws DBException;

}
