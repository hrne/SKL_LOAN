package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.TxApLog;
import org.springframework.data.domain.Slice;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface TxApLogService {

	/**
	 * findByPrimaryKey
	 *
	 * @param autoSeq PK
	 * @param titaVo  Variable-Length Argument
	 * @return TxApLog TxApLog
	 */
	public TxApLog findById(Long autoSeq, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice TxApLog TxApLog of List
	 */
	public Slice<TxApLog> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * TlrNo =
	 *
	 * @param tlrNo_0 tlrNo_0
	 * @param index   Page Index
	 * @param limit   Page Data Limit
	 * @param titaVo  Variable-Length Argument
	 * @return Slice TxApLog TxApLog of List
	 */
	public Slice<TxApLog> findTlrNo(String tlrNo_0, int index, int limit, TitaVo... titaVo);

	/**
	 * Entdy =
	 *
	 * @param entdy_0 entdy_0
	 * @param index   Page Index
	 * @param limit   Page Data Limit
	 * @param titaVo  Variable-Length Argument
	 * @return Slice TxApLog TxApLog of List
	 */
	public Slice<TxApLog> findEntdy(int entdy_0, int index, int limit, TitaVo... titaVo);

	/**
	 * Entdy =,AND TlrNo =
	 *
	 * @param entdy_0 entdy_0
	 * @param tlrNo_1 tlrNo_1
	 * @param index   Page Index
	 * @param limit   Page Data Limit
	 * @param titaVo  Variable-Length Argument
	 * @return Slice TxApLog TxApLog of List
	 */
	public Slice<TxApLog> findEntdyAndTlrno(int entdy_0, String tlrNo_1, int index, int limit, TitaVo... titaVo);

	/**
	 * hold By TxApLog
	 * 
	 * @param autoSeq key
	 * @param titaVo  Variable-Length Argument
	 * @return TxApLog TxApLog
	 */
	public TxApLog holdById(Long autoSeq, TitaVo... titaVo);

	/**
	 * hold By TxApLog
	 * 
	 * @param txApLog key
	 * @param titaVo  Variable-Length Argument
	 * @return TxApLog TxApLog
	 */
	public TxApLog holdById(TxApLog txApLog, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param txApLog Entity
	 * @param titaVo  Variable-Length Argument
	 * @return TxApLog Entity
	 * @throws DBException exception
	 */
	public TxApLog insert(TxApLog txApLog, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param txApLog Entity
	 * @param titaVo  Variable-Length Argument
	 * @return TxApLog Entity
	 * @throws DBException exception
	 */
	public TxApLog update(TxApLog txApLog, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param txApLog Entity
	 * @param titaVo  Variable-Length Argument
	 * @return TxApLog Entity
	 * @throws DBException exception
	 */
	public TxApLog update2(TxApLog txApLog, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param txApLog Entity
	 * @param titaVo  Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(TxApLog txApLog, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param txApLog Entity of List
	 * @param titaVo  Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<TxApLog> txApLog, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param txApLog Entity of List
	 * @param titaVo  Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<TxApLog> txApLog, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param txApLog Entity of List
	 * @param titaVo  Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<TxApLog> txApLog, TitaVo... titaVo) throws DBException;

}
