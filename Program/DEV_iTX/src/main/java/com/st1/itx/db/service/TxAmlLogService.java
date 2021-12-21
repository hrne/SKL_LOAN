package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.TxAmlLog;
import org.springframework.data.domain.Slice;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface TxAmlLogService {

	/**
	 * findByPrimaryKey
	 *
	 * @param logNo  PK
	 * @param titaVo Variable-Length Argument
	 * @return TxAmlLog TxAmlLog
	 */
	public TxAmlLog findById(Long logNo, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice TxAmlLog TxAmlLog of List
	 */
	public Slice<TxAmlLog> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * BrNo = ,AND ConfirmStatus = ,AND Entdy &gt;= ,AND Entdy &lt;=
	 *
	 * @param brNo_0          brNo_0
	 * @param confirmStatus_1 confirmStatus_1
	 * @param entdy_2         entdy_2
	 * @param entdy_3         entdy_3
	 * @param index           Page Index
	 * @param limit           Page Data Limit
	 * @param titaVo          Variable-Length Argument
	 * @return Slice TxAmlLog TxAmlLog of List
	 */
	public Slice<TxAmlLog> findByConfirmStatus(String brNo_0, String confirmStatus_1, int entdy_2, int entdy_3, int index, int limit, TitaVo... titaVo);

	/**
	 * BrNo = ,AND Entdy &gt;= ,AND Entdy &lt;=
	 *
	 * @param brNo_0  brNo_0
	 * @param entdy_1 entdy_1
	 * @param entdy_2 entdy_2
	 * @param index   Page Index
	 * @param limit   Page Data Limit
	 * @param titaVo  Variable-Length Argument
	 * @return Slice TxAmlLog TxAmlLog of List
	 */
	public Slice<TxAmlLog> findByBrNo(String brNo_0, int entdy_1, int entdy_2, int index, int limit, TitaVo... titaVo);

	/**
	 * Entdy = ,AND TransactionId =
	 *
	 * @param entdy_0         entdy_0
	 * @param transactionId_1 transactionId_1
	 * @param titaVo          Variable-Length Argument
	 * @return Slice TxAmlLog TxAmlLog of List
	 */
	public TxAmlLog findByTransactionIdFirst(int entdy_0, String transactionId_1, TitaVo... titaVo);

	/**
	 * hold By TxAmlLog
	 * 
	 * @param logNo  key
	 * @param titaVo Variable-Length Argument
	 * @return TxAmlLog TxAmlLog
	 */
	public TxAmlLog holdById(Long logNo, TitaVo... titaVo);

	/**
	 * hold By TxAmlLog
	 * 
	 * @param txAmlLog key
	 * @param titaVo   Variable-Length Argument
	 * @return TxAmlLog TxAmlLog
	 */
	public TxAmlLog holdById(TxAmlLog txAmlLog, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param txAmlLog Entity
	 * @param titaVo   Variable-Length Argument
	 * @return TxAmlLog Entity
	 * @throws DBException exception
	 */
	public TxAmlLog insert(TxAmlLog txAmlLog, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param txAmlLog Entity
	 * @param titaVo   Variable-Length Argument
	 * @return TxAmlLog Entity
	 * @throws DBException exception
	 */
	public TxAmlLog update(TxAmlLog txAmlLog, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param txAmlLog Entity
	 * @param titaVo   Variable-Length Argument
	 * @return TxAmlLog Entity
	 * @throws DBException exception
	 */
	public TxAmlLog update2(TxAmlLog txAmlLog, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param txAmlLog Entity
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(TxAmlLog txAmlLog, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param txAmlLog Entity of List
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<TxAmlLog> txAmlLog, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param txAmlLog Entity of List
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<TxAmlLog> txAmlLog, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param txAmlLog Entity of List
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<TxAmlLog> txAmlLog, TitaVo... titaVo) throws DBException;

}
