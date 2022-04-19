package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CustRmk;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.CustRmkId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CustRmkService {

	/**
	 * findByPrimaryKey
	 *
	 * @param custRmkId PK
	 * @param titaVo    Variable-Length Argument
	 * @return CustRmk CustRmk
	 */
	public CustRmk findById(CustRmkId custRmkId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice CustRmk CustRmk of List
	 */
	public Slice<CustRmk> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * CustNo =
	 *
	 * @param custNo_0 custNo_0
	 * @param index    Page Index
	 * @param limit    Page Data Limit
	 * @param titaVo   Variable-Length Argument
	 * @return Slice CustRmk CustRmk of List
	 */
	public Slice<CustRmk> findCustNo(int custNo_0, int index, int limit, TitaVo... titaVo);

	/**
	 * RmkCode =
	 *
	 * @param rmkCode_0 rmkCode_0
	 * @param index     Page Index
	 * @param limit     Page Data Limit
	 * @param titaVo    Variable-Length Argument
	 * @return Slice CustRmk CustRmk of List
	 */
	public Slice<CustRmk> findRmkCode(String rmkCode_0, int index, int limit, TitaVo... titaVo);

	/**
	 * CustNo =
	 *
	 * @param custNo_0 custNo_0
	 * @param titaVo   Variable-Length Argument
	 * @return Slice CustRmk CustRmk of List
	 */
	public CustRmk maxRmkNoFirst(int custNo_0, TitaVo... titaVo);

	/**
	 * hold By CustRmk
	 * 
	 * @param custRmkId key
	 * @param titaVo    Variable-Length Argument
	 * @return CustRmk CustRmk
	 */
	public CustRmk holdById(CustRmkId custRmkId, TitaVo... titaVo);

	/**
	 * hold By CustRmk
	 * 
	 * @param custRmk key
	 * @param titaVo  Variable-Length Argument
	 * @return CustRmk CustRmk
	 */
	public CustRmk holdById(CustRmk custRmk, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param custRmk Entity
	 * @param titaVo  Variable-Length Argument
	 * @return CustRmk Entity
	 * @throws DBException exception
	 */
	public CustRmk insert(CustRmk custRmk, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param custRmk Entity
	 * @param titaVo  Variable-Length Argument
	 * @return CustRmk Entity
	 * @throws DBException exception
	 */
	public CustRmk update(CustRmk custRmk, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param custRmk Entity
	 * @param titaVo  Variable-Length Argument
	 * @return CustRmk Entity
	 * @throws DBException exception
	 */
	public CustRmk update2(CustRmk custRmk, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param custRmk Entity
	 * @param titaVo  Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(CustRmk custRmk, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param custRmk Entity of List
	 * @param titaVo  Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<CustRmk> custRmk, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param custRmk Entity of List
	 * @param titaVo  Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<CustRmk> custRmk, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param custRmk Entity of List
	 * @param titaVo  Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<CustRmk> custRmk, TitaVo... titaVo) throws DBException;

}
