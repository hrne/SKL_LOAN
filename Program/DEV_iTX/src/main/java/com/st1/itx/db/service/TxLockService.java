package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.TxLock;
import org.springframework.data.domain.Slice;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface TxLockService {

	/**
	 * findByPrimaryKey
	 *
	 * @param lockNo PK
	 * @param titaVo Variable-Length Argument
	 * @return TxLock TxLock
	 */
	public TxLock findById(Long lockNo, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice TxLock TxLock of List
	 */
	public Slice<TxLock> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * CustNo =
	 *
	 * @param custNo_0 custNo_0
	 * @param index    Page Index
	 * @param limit    Page Data Limit
	 * @param titaVo   Variable-Length Argument
	 * @return Slice TxLock TxLock of List
	 */
	public Slice<TxLock> findByCustNo(int custNo_0, int index, int limit, TitaVo... titaVo);

	/**
	 * hold By TxLock
	 * 
	 * @param lockNo key
	 * @param titaVo Variable-Length Argument
	 * @return TxLock TxLock
	 */
	public TxLock holdById(Long lockNo, TitaVo... titaVo);

	/**
	 * hold By TxLock
	 * 
	 * @param txLock key
	 * @param titaVo Variable-Length Argument
	 * @return TxLock TxLock
	 */
	public TxLock holdById(TxLock txLock, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param txLock Entity
	 * @param titaVo Variable-Length Argument
	 * @return TxLock Entity
	 * @throws DBException exception
	 */
	public TxLock insert(TxLock txLock, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param txLock Entity
	 * @param titaVo Variable-Length Argument
	 * @return TxLock Entity
	 * @throws DBException exception
	 */
	public TxLock update(TxLock txLock, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param txLock Entity
	 * @param titaVo Variable-Length Argument
	 * @return TxLock Entity
	 * @throws DBException exception
	 */
	public TxLock update2(TxLock txLock, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param txLock Entity
	 * @param titaVo Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(TxLock txLock, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param txLock Entity of List
	 * @param titaVo Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<TxLock> txLock, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param txLock Entity of List
	 * @param titaVo Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<TxLock> txLock, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param txLock Entity of List
	 * @param titaVo Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<TxLock> txLock, TitaVo... titaVo) throws DBException;

}
