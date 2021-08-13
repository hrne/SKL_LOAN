package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.TxCurr;
import org.springframework.data.domain.Slice;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface TxCurrService {

	/**
	 * findByPrimaryKey
	 *
	 * @param curCd  PK
	 * @param titaVo Variable-Length Argument
	 * @return TxCurr TxCurr
	 */
	public TxCurr findById(int curCd, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice TxCurr TxCurr of List
	 */
	public Slice<TxCurr> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * hold By TxCurr
	 * 
	 * @param curCd  key
	 * @param titaVo Variable-Length Argument
	 * @return TxCurr TxCurr
	 */
	public TxCurr holdById(int curCd, TitaVo... titaVo);

	/**
	 * hold By TxCurr
	 * 
	 * @param txCurr key
	 * @param titaVo Variable-Length Argument
	 * @return TxCurr TxCurr
	 */
	public TxCurr holdById(TxCurr txCurr, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param txCurr Entity
	 * @param titaVo Variable-Length Argument
	 * @return TxCurr Entity
	 * @throws DBException exception
	 */
	public TxCurr insert(TxCurr txCurr, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param txCurr Entity
	 * @param titaVo Variable-Length Argument
	 * @return TxCurr Entity
	 * @throws DBException exception
	 */
	public TxCurr update(TxCurr txCurr, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param txCurr Entity
	 * @param titaVo Variable-Length Argument
	 * @return TxCurr Entity
	 * @throws DBException exception
	 */
	public TxCurr update2(TxCurr txCurr, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param txCurr Entity
	 * @param titaVo Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(TxCurr txCurr, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param txCurr Entity of List
	 * @param titaVo Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<TxCurr> txCurr, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param txCurr Entity of List
	 * @param titaVo Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<TxCurr> txCurr, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param txCurr Entity of List
	 * @param titaVo Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<TxCurr> txCurr, TitaVo... titaVo) throws DBException;

}
