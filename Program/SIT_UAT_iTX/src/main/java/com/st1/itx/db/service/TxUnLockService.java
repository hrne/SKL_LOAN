package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.TxUnLock;
import org.springframework.data.domain.Slice;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface TxUnLockService {

	/**
	 * findByPrimaryKey
	 *
	 * @param lockNo PK
	 * @param titaVo Variable-Length Argument
	 * @return TxUnLock TxUnLock
	 */
	public TxUnLock findById(Long lockNo, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice TxUnLock TxUnLock of List
	 */
	public Slice<TxUnLock> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * Entdy = ,AND CustNo =
	 *
	 * @param entdy_0  entdy_0
	 * @param custNo_1 custNo_1
	 * @param index    Page Index
	 * @param limit    Page Data Limit
	 * @param titaVo   Variable-Length Argument
	 * @return Slice TxUnLock TxUnLock of List
	 */
	public Slice<TxUnLock> findByCustNo(int entdy_0, int custNo_1, int index, int limit, TitaVo... titaVo);

	/**
	 * hold By TxUnLock
	 * 
	 * @param lockNo key
	 * @param titaVo Variable-Length Argument
	 * @return TxUnLock TxUnLock
	 */
	public TxUnLock holdById(Long lockNo, TitaVo... titaVo);

	/**
	 * hold By TxUnLock
	 * 
	 * @param txUnLock key
	 * @param titaVo   Variable-Length Argument
	 * @return TxUnLock TxUnLock
	 */
	public TxUnLock holdById(TxUnLock txUnLock, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param txUnLock Entity
	 * @param titaVo   Variable-Length Argument
	 * @return TxUnLock Entity
	 * @throws DBException exception
	 */
	public TxUnLock insert(TxUnLock txUnLock, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param txUnLock Entity
	 * @param titaVo   Variable-Length Argument
	 * @return TxUnLock Entity
	 * @throws DBException exception
	 */
	public TxUnLock update(TxUnLock txUnLock, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param txUnLock Entity
	 * @param titaVo   Variable-Length Argument
	 * @return TxUnLock Entity
	 * @throws DBException exception
	 */
	public TxUnLock update2(TxUnLock txUnLock, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param txUnLock Entity
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(TxUnLock txUnLock, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param txUnLock Entity of List
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<TxUnLock> txUnLock, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param txUnLock Entity of List
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<TxUnLock> txUnLock, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param txUnLock Entity of List
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<TxUnLock> txUnLock, TitaVo... titaVo) throws DBException;

}
