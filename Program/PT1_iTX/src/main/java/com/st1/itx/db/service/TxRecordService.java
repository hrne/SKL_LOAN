package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.TxRecord;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.TxRecordId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface TxRecordService {

	/**
	 * findByPrimaryKey
	 *
	 * @param txRecordId PK
	 * @param titaVo     Variable-Length Argument
	 * @return TxRecord TxRecord
	 */
	public TxRecord findById(TxRecordId txRecordId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice TxRecord TxRecord of List
	 */
	public Slice<TxRecord> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * Entdy = ,AND BrNo =
	 *
	 * @param entdy_0 entdy_0
	 * @param brNo_1  brNo_1
	 * @param index   Page Index
	 * @param limit   Page Data Limit
	 * @param titaVo  Variable-Length Argument
	 * @return Slice TxRecord TxRecord of List
	 */
	public Slice<TxRecord> findByBrNo(int entdy_0, String brNo_1, int index, int limit, TitaVo... titaVo);

	/**
	 * Entdy = ,AND BrNo = ,AND TxResult = ,AND CanCancel = ,AND ActionFg = ,AND
	 * Hcode&lt;&gt; ,AND TlrNo % ,AND TranNo %
	 *
	 * @param entdy_0     entdy_0
	 * @param brNo_1      brNo_1
	 * @param txResult_2  txResult_2
	 * @param canCancel_3 canCancel_3
	 * @param actionFg_4  actionFg_4
	 * @param hcode_5     hcode_5
	 * @param tlrNo_6     tlrNo_6
	 * @param tranNo_7    tranNo_7
	 * @param index       Page Index
	 * @param limit       Page Data Limit
	 * @param titaVo      Variable-Length Argument
	 * @return Slice TxRecord TxRecord of List
	 */
	public Slice<TxRecord> findByLC001(int entdy_0, String brNo_1, String txResult_2, int canCancel_3, int actionFg_4, int hcode_5, String tlrNo_6, String tranNo_7, int index, int limit,
			TitaVo... titaVo);

	/**
	 * Entdy = ,AND BrNo = ,AND TxResult = ,AND CanModify = ,AND ActionFg = ,AND
	 * Hcode&lt;&gt; ,AND TlrNo % ,AND TranNo %
	 *
	 * @param entdy_0     entdy_0
	 * @param brNo_1      brNo_1
	 * @param txResult_2  txResult_2
	 * @param canModify_3 canModify_3
	 * @param actionFg_4  actionFg_4
	 * @param hcode_5     hcode_5
	 * @param tlrNo_6     tlrNo_6
	 * @param tranNo_7    tranNo_7
	 * @param index       Page Index
	 * @param limit       Page Data Limit
	 * @param titaVo      Variable-Length Argument
	 * @return Slice TxRecord TxRecord of List
	 */
	public Slice<TxRecord> findByLC002(int entdy_0, String brNo_1, String txResult_2, int canModify_3, int actionFg_4, int hcode_5, String tlrNo_6, String tranNo_7, int index, int limit,
			TitaVo... titaVo);

	/**
	 * Entdy &gt;= ,AND Entdy &lt;= ,AND BrNo = ,AND TxResult = ,AND ActionFg = ,AND
	 * TlrNo % ,AND TranNo %
	 *
	 * @param entdy_0    entdy_0
	 * @param entdy_1    entdy_1
	 * @param brNo_2     brNo_2
	 * @param txResult_3 txResult_3
	 * @param actionFg_4 actionFg_4
	 * @param tlrNo_5    tlrNo_5
	 * @param tranNo_6   tranNo_6
	 * @param index      Page Index
	 * @param limit      Page Data Limit
	 * @param titaVo     Variable-Length Argument
	 * @return Slice TxRecord TxRecord of List
	 */
	public Slice<TxRecord> findByLC011(int entdy_0, int entdy_1, String brNo_2, String txResult_3, int actionFg_4, String tlrNo_5, String tranNo_6, int index, int limit, TitaVo... titaVo);

	/**
	 * Entdy &gt;= ,AND Entdy &lt;= ,AND BrNo = ,AND TxResult = ,AND TlrNo % ,AND
	 * TranNo %
	 *
	 * @param entdy_0    entdy_0
	 * @param entdy_1    entdy_1
	 * @param brNo_2     brNo_2
	 * @param txResult_3 txResult_3
	 * @param tlrNo_4    tlrNo_4
	 * @param tranNo_5   tranNo_5
	 * @param index      Page Index
	 * @param limit      Page Data Limit
	 * @param titaVo     Variable-Length Argument
	 * @return Slice TxRecord TxRecord of List
	 */
	public Slice<TxRecord> findByLC011All(int entdy_0, int entdy_1, String brNo_2, String txResult_3, String tlrNo_4, String tranNo_5, int index, int limit, TitaVo... titaVo);

	/**
	 * Entdy &gt;= ,AND Entdy &lt;= ,AND BrNo = ,AND TxResult = ,AND Hcode = ,AND
	 * TlrNo % ,AND TranNo %
	 *
	 * @param entdy_0    entdy_0
	 * @param entdy_1    entdy_1
	 * @param brNo_2     brNo_2
	 * @param txResult_3 txResult_3
	 * @param hcode_4    hcode_4
	 * @param tlrNo_5    tlrNo_5
	 * @param tranNo_6   tranNo_6
	 * @param index      Page Index
	 * @param limit      Page Data Limit
	 * @param titaVo     Variable-Length Argument
	 * @return Slice TxRecord TxRecord of List
	 */
	public Slice<TxRecord> findByLC011Hcode(int entdy_0, int entdy_1, String brNo_2, String txResult_3, int hcode_4, String tlrNo_5, String tranNo_6, int index, int limit, TitaVo... titaVo);

	/**
	 * MrKey % ,AND TranNo ^i ,AND Entdy &gt;= ,AND Entdy &lt;=
	 *
	 * @param mrKey_0  mrKey_0
	 * @param tranNo_1 tranNo_1
	 * @param entdy_2  entdy_2
	 * @param entdy_3  entdy_3
	 * @param index    Page Index
	 * @param limit    Page Data Limit
	 * @param titaVo   Variable-Length Argument
	 * @return Slice TxRecord TxRecord of List
	 */
	public Slice<TxRecord> findByL3005(String mrKey_0, List<String> tranNo_1, int entdy_2, int entdy_3, int index, int limit, TitaVo... titaVo);

	/**
	 * CalDate = ,AND BrNo =,AND ImportFg =
	 *
	 * @param calDate_0  calDate_0
	 * @param brNo_1     brNo_1
	 * @param importFg_2 importFg_2
	 * @param index      Page Index
	 * @param limit      Page Data Limit
	 * @param titaVo     Variable-Length Argument
	 * @return Slice TxRecord TxRecord of List
	 */
	public Slice<TxRecord> findImportFg(int calDate_0, String brNo_1, String importFg_2, int index, int limit, TitaVo... titaVo);

	/**
	 * CalDate &gt;= ,AND CalDate &lt;= ,AND BrNo = ,AND ImportFg =
	 *
	 * @param calDate_0  calDate_0
	 * @param calDate_1  calDate_1
	 * @param brNo_2     brNo_2
	 * @param importFg_3 importFg_3
	 * @param index      Page Index
	 * @param limit      Page Data Limit
	 * @param titaVo     Variable-Length Argument
	 * @return Slice TxRecord TxRecord of List
	 */
	public Slice<TxRecord> findByCalDate(int calDate_0, int calDate_1, String brNo_2, String importFg_3, int index, int limit, TitaVo... titaVo);

	/**
	 * CalDate &gt;= ,AND CalDate &lt;= ,AND BrNo = ,AND LockCustNo = ,AND ImportFg
	 * =
	 *
	 * @param calDate_0    calDate_0
	 * @param calDate_1    calDate_1
	 * @param brNo_2       brNo_2
	 * @param lockCustNo_3 lockCustNo_3
	 * @param importFg_4   importFg_4
	 * @param index        Page Index
	 * @param limit        Page Data Limit
	 * @param titaVo       Variable-Length Argument
	 * @return Slice TxRecord TxRecord of List
	 */
	public Slice<TxRecord> findByCustNo(int calDate_0, int calDate_1, String brNo_2, int lockCustNo_3, String importFg_4, int index, int limit, TitaVo... titaVo);

	/**
	 * hold By TxRecord
	 * 
	 * @param txRecordId key
	 * @param titaVo     Variable-Length Argument
	 * @return TxRecord TxRecord
	 */
	public TxRecord holdById(TxRecordId txRecordId, TitaVo... titaVo);

	/**
	 * hold By TxRecord
	 * 
	 * @param txRecord key
	 * @param titaVo   Variable-Length Argument
	 * @return TxRecord TxRecord
	 */
	public TxRecord holdById(TxRecord txRecord, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param txRecord Entity
	 * @param titaVo   Variable-Length Argument
	 * @return TxRecord Entity
	 * @throws DBException exception
	 */
	public TxRecord insert(TxRecord txRecord, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param txRecord Entity
	 * @param titaVo   Variable-Length Argument
	 * @return TxRecord Entity
	 * @throws DBException exception
	 */
	public TxRecord update(TxRecord txRecord, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param txRecord Entity
	 * @param titaVo   Variable-Length Argument
	 * @return TxRecord Entity
	 * @throws DBException exception
	 */
	public TxRecord update2(TxRecord txRecord, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param txRecord Entity
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(TxRecord txRecord, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param txRecord Entity of List
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<TxRecord> txRecord, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param txRecord Entity of List
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<TxRecord> txRecord, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param txRecord Entity of List
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<TxRecord> txRecord, TitaVo... titaVo) throws DBException;

}
