package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.TxPrinter;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.TxPrinterId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface TxPrinterService {

	/**
	 * findByPrimaryKey
	 *
	 * @param txPrinterId PK
	 * @param titaVo      Variable-Length Argument
	 * @return TxPrinter TxPrinter
	 */
	public TxPrinter findById(TxPrinterId txPrinterId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice TxPrinter TxPrinter of List
	 */
	public Slice<TxPrinter> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * StanIp =
	 *
	 * @param stanIp_0 stanIp_0
	 * @param index    Page Index
	 * @param limit    Page Data Limit
	 * @param titaVo   Variable-Length Argument
	 * @return Slice TxPrinter TxPrinter of List
	 */
	public Slice<TxPrinter> findByStanIp(String stanIp_0, int index, int limit, TitaVo... titaVo);

	/**
	 * hold By TxPrinter
	 * 
	 * @param txPrinterId key
	 * @param titaVo      Variable-Length Argument
	 * @return TxPrinter TxPrinter
	 */
	public TxPrinter holdById(TxPrinterId txPrinterId, TitaVo... titaVo);

	/**
	 * hold By TxPrinter
	 * 
	 * @param txPrinter key
	 * @param titaVo    Variable-Length Argument
	 * @return TxPrinter TxPrinter
	 */
	public TxPrinter holdById(TxPrinter txPrinter, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param txPrinter Entity
	 * @param titaVo    Variable-Length Argument
	 * @return TxPrinter Entity
	 * @throws DBException exception
	 */
	public TxPrinter insert(TxPrinter txPrinter, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param txPrinter Entity
	 * @param titaVo    Variable-Length Argument
	 * @return TxPrinter Entity
	 * @throws DBException exception
	 */
	public TxPrinter update(TxPrinter txPrinter, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param txPrinter Entity
	 * @param titaVo    Variable-Length Argument
	 * @return TxPrinter Entity
	 * @throws DBException exception
	 */
	public TxPrinter update2(TxPrinter txPrinter, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param txPrinter Entity
	 * @param titaVo    Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(TxPrinter txPrinter, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param txPrinter Entity of List
	 * @param titaVo    Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<TxPrinter> txPrinter, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param txPrinter Entity of List
	 * @param titaVo    Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<TxPrinter> txPrinter, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param txPrinter Entity of List
	 * @param titaVo    Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<TxPrinter> txPrinter, TitaVo... titaVo) throws DBException;

}
