package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.TxApLogList;
import org.springframework.data.domain.Slice;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface TxApLogListService {

	/**
	 * findByPrimaryKey
	 *
	 * @param txCode PK
	 * @param titaVo Variable-Length Argument
	 * @return TxApLogList TxApLogList
	 */
	public TxApLogList findById(String txCode, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice TxApLogList TxApLogList of List
	 */
	public Slice<TxApLogList> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * hold By TxApLogList
	 * 
	 * @param txCode key
	 * @param titaVo Variable-Length Argument
	 * @return TxApLogList TxApLogList
	 */
	public TxApLogList holdById(String txCode, TitaVo... titaVo);

	/**
	 * hold By TxApLogList
	 * 
	 * @param txApLogList key
	 * @param titaVo      Variable-Length Argument
	 * @return TxApLogList TxApLogList
	 */
	public TxApLogList holdById(TxApLogList txApLogList, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param txApLogList Entity
	 * @param titaVo      Variable-Length Argument
	 * @return TxApLogList Entity
	 * @throws DBException exception
	 */
	public TxApLogList insert(TxApLogList txApLogList, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param txApLogList Entity
	 * @param titaVo      Variable-Length Argument
	 * @return TxApLogList Entity
	 * @throws DBException exception
	 */
	public TxApLogList update(TxApLogList txApLogList, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param txApLogList Entity
	 * @param titaVo      Variable-Length Argument
	 * @return TxApLogList Entity
	 * @throws DBException exception
	 */
	public TxApLogList update2(TxApLogList txApLogList, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param txApLogList Entity
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(TxApLogList txApLogList, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param txApLogList Entity of List
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<TxApLogList> txApLogList, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param txApLogList Entity of List
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<TxApLogList> txApLogList, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param txApLogList Entity of List
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<TxApLogList> txApLogList, TitaVo... titaVo) throws DBException;

}
