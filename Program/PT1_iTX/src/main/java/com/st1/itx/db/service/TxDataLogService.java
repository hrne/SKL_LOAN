package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.TxDataLog;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.TxDataLogId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface TxDataLogService {

	/**
	 * findByPrimaryKey
	 *
	 * @param txDataLogId PK
	 * @param titaVo      Variable-Length Argument
	 * @return TxDataLog TxDataLog
	 */
	public TxDataLog findById(TxDataLogId txDataLogId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice TxDataLog TxDataLog of List
	 */
	public Slice<TxDataLog> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * TxDate &gt;= ,AND TxDate &lt;= ,AND TranNo %
	 *
	 * @param txDate_0 txDate_0
	 * @param txDate_1 txDate_1
	 * @param tranNo_2 tranNo_2
	 * @param index    Page Index
	 * @param limit    Page Data Limit
	 * @param titaVo   Variable-Length Argument
	 * @return Slice TxDataLog TxDataLog of List
	 */
	public Slice<TxDataLog> findByCustNo0(int txDate_0, int txDate_1, String tranNo_2, int index, int limit, TitaVo... titaVo);

	/**
	 * TxDate &gt;= ,AND TxDate &lt;= ,AND TranNo % ,AND CustNo =
	 *
	 * @param txDate_0 txDate_0
	 * @param txDate_1 txDate_1
	 * @param tranNo_2 tranNo_2
	 * @param custNo_3 custNo_3
	 * @param index    Page Index
	 * @param limit    Page Data Limit
	 * @param titaVo   Variable-Length Argument
	 * @return Slice TxDataLog TxDataLog of List
	 */
	public Slice<TxDataLog> findByCustNo1(int txDate_0, int txDate_1, String tranNo_2, int custNo_3, int index, int limit, TitaVo... titaVo);

	/**
	 * TxDate &gt;= ,AND TxDate &lt;= ,AND TranNo % ,AND CustNo = ,AND FacmNo =
	 *
	 * @param txDate_0 txDate_0
	 * @param txDate_1 txDate_1
	 * @param tranNo_2 tranNo_2
	 * @param custNo_3 custNo_3
	 * @param facmNo_4 facmNo_4
	 * @param index    Page Index
	 * @param limit    Page Data Limit
	 * @param titaVo   Variable-Length Argument
	 * @return Slice TxDataLog TxDataLog of List
	 */
	public Slice<TxDataLog> findByCustNo2(int txDate_0, int txDate_1, String tranNo_2, int custNo_3, int facmNo_4, int index, int limit, TitaVo... titaVo);

	/**
	 * TxDate &gt;= ,AND TxDate &lt;= ,AND TranNo % ,AND CustNo = ,AND FacmNo = ,AND
	 * BormNo =
	 *
	 * @param txDate_0 txDate_0
	 * @param txDate_1 txDate_1
	 * @param tranNo_2 tranNo_2
	 * @param custNo_3 custNo_3
	 * @param facmNo_4 facmNo_4
	 * @param bormNo_5 bormNo_5
	 * @param index    Page Index
	 * @param limit    Page Data Limit
	 * @param titaVo   Variable-Length Argument
	 * @return Slice TxDataLog TxDataLog of List
	 */
	public Slice<TxDataLog> findByCustNo3(int txDate_0, int txDate_1, String tranNo_2, int custNo_3, int facmNo_4, int bormNo_5, int index, int limit, TitaVo... titaVo);

	/**
	 * TxDate &gt;= ,AND TxDate &lt;= ,AND TxSeq %
	 *
	 * @param txDate_0 txDate_0
	 * @param txDate_1 txDate_1
	 * @param txSeq_2  txSeq_2
	 * @param index    Page Index
	 * @param limit    Page Data Limit
	 * @param titaVo   Variable-Length Argument
	 * @return Slice TxDataLog TxDataLog of List
	 */
	public Slice<TxDataLog> findTxSeq(int txDate_0, int txDate_1, String txSeq_2, int index, int limit, TitaVo... titaVo);

	/**
	 * TxDate &gt;= ,AND TxDate &lt;= ,AND TranNo ^i
	 *
	 * @param txDate_0 txDate_0
	 * @param txDate_1 txDate_1
	 * @param tranNo_2 tranNo_2
	 * @param index    Page Index
	 * @param limit    Page Data Limit
	 * @param titaVo   Variable-Length Argument
	 * @return Slice TxDataLog TxDataLog of List
	 */
	public Slice<TxDataLog> findByCustNo4(int txDate_0, int txDate_1, List<String> tranNo_2, int index, int limit, TitaVo... titaVo);

	/**
	 * TxDate &gt;= ,AND TxDate &lt;= ,AND TranNo ^i ,AND CustNo =
	 *
	 * @param txDate_0 txDate_0
	 * @param txDate_1 txDate_1
	 * @param tranNo_2 tranNo_2
	 * @param custNo_3 custNo_3
	 * @param index    Page Index
	 * @param limit    Page Data Limit
	 * @param titaVo   Variable-Length Argument
	 * @return Slice TxDataLog TxDataLog of List
	 */
	public Slice<TxDataLog> findByCustNo5(int txDate_0, int txDate_1, List<String> tranNo_2, int custNo_3, int index, int limit, TitaVo... titaVo);

	/**
	 * TxDate &gt;= ,AND TxDate &lt;= ,AND TranNo ^i ,AND CustNo = ,AND FacmNo =
	 *
	 * @param txDate_0 txDate_0
	 * @param txDate_1 txDate_1
	 * @param tranNo_2 tranNo_2
	 * @param custNo_3 custNo_3
	 * @param facmNo_4 facmNo_4
	 * @param index    Page Index
	 * @param limit    Page Data Limit
	 * @param titaVo   Variable-Length Argument
	 * @return Slice TxDataLog TxDataLog of List
	 */
	public Slice<TxDataLog> findByCustNo6(int txDate_0, int txDate_1, List<String> tranNo_2, int custNo_3, int facmNo_4, int index, int limit, TitaVo... titaVo);

	/**
	 * TxDate &gt;= ,AND TxDate &lt;= ,AND TranNo ^i ,AND CustNo = ,AND FacmNo =
	 * ,AND BormNo =
	 *
	 * @param txDate_0 txDate_0
	 * @param txDate_1 txDate_1
	 * @param tranNo_2 tranNo_2
	 * @param custNo_3 custNo_3
	 * @param facmNo_4 facmNo_4
	 * @param bormNo_5 bormNo_5
	 * @param index    Page Index
	 * @param limit    Page Data Limit
	 * @param titaVo   Variable-Length Argument
	 * @return Slice TxDataLog TxDataLog of List
	 */
	public Slice<TxDataLog> findByCustNo7(int txDate_0, int txDate_1, List<String> tranNo_2, int custNo_3, int facmNo_4, int bormNo_5, int index, int limit, TitaVo... titaVo);

	/**
	 * TranNo = ,AND MrKey =
	 *
	 * @param tranNo_0 tranNo_0
	 * @param mrKey_1  mrKey_1
	 * @param index    Page Index
	 * @param limit    Page Data Limit
	 * @param titaVo   Variable-Length Argument
	 * @return Slice TxDataLog TxDataLog of List
	 */
	public Slice<TxDataLog> findByTranNo(String tranNo_0, String mrKey_1, int index, int limit, TitaVo... titaVo);

	/**
	 * MrKey = ,AND TranNo ^i
	 *
	 * @param mrKey_0  mrKey_0
	 * @param tranNo_1 tranNo_1
	 * @param titaVo   Variable-Length Argument
	 * @return Slice TxDataLog TxDataLog of List
	 */
	public TxDataLog findByMrKeyFirst(String mrKey_0, List<String> tranNo_1, TitaVo... titaVo);

	/**
	 * hold By TxDataLog
	 * 
	 * @param txDataLogId key
	 * @param titaVo      Variable-Length Argument
	 * @return TxDataLog TxDataLog
	 */
	public TxDataLog holdById(TxDataLogId txDataLogId, TitaVo... titaVo);

	/**
	 * hold By TxDataLog
	 * 
	 * @param txDataLog key
	 * @param titaVo    Variable-Length Argument
	 * @return TxDataLog TxDataLog
	 */
	public TxDataLog holdById(TxDataLog txDataLog, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param txDataLog Entity
	 * @param titaVo    Variable-Length Argument
	 * @return TxDataLog Entity
	 * @throws DBException exception
	 */
	public TxDataLog insert(TxDataLog txDataLog, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param txDataLog Entity
	 * @param titaVo    Variable-Length Argument
	 * @return TxDataLog Entity
	 * @throws DBException exception
	 */
	public TxDataLog update(TxDataLog txDataLog, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param txDataLog Entity
	 * @param titaVo    Variable-Length Argument
	 * @return TxDataLog Entity
	 * @throws DBException exception
	 */
	public TxDataLog update2(TxDataLog txDataLog, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param txDataLog Entity
	 * @param titaVo    Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(TxDataLog txDataLog, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param txDataLog Entity of List
	 * @param titaVo    Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<TxDataLog> txDataLog, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param txDataLog Entity of List
	 * @param titaVo    Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<TxDataLog> txDataLog, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param txDataLog Entity of List
	 * @param titaVo    Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<TxDataLog> txDataLog, TitaVo... titaVo) throws DBException;

}
