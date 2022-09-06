package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.TxAmlCredit;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.TxAmlCreditId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface TxAmlCreditService {

	/**
	 * findByPrimaryKey
	 *
	 * @param txAmlCreditId PK
	 * @param titaVo        Variable-Length Argument
	 * @return TxAmlCredit TxAmlCredit
	 */
	public TxAmlCredit findById(TxAmlCreditId txAmlCreditId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice TxAmlCredit TxAmlCredit of List
	 */
	public Slice<TxAmlCredit> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * ReviewType ^i ,AND DataDt &gt;= ,AND DataDt &lt;= ,AND ProcessType %
	 *
	 * @param reviewType_0  reviewType_0
	 * @param dataDt_1      dataDt_1
	 * @param dataDt_2      dataDt_2
	 * @param processType_3 processType_3
	 * @param index         Page Index
	 * @param limit         Page Data Limit
	 * @param titaVo        Variable-Length Argument
	 * @return Slice TxAmlCredit TxAmlCredit of List
	 */
	public Slice<TxAmlCredit> processAll(List<String> reviewType_0, int dataDt_1, int dataDt_2, String processType_3, int index, int limit, TitaVo... titaVo);

	/**
	 * ReviewType ^i ,AND DataDt &gt;= ,AND DataDt &lt;= ,AND ProcessType % ,AND
	 * ProcessCount =
	 *
	 * @param reviewType_0   reviewType_0
	 * @param dataDt_1       dataDt_1
	 * @param dataDt_2       dataDt_2
	 * @param processType_3  processType_3
	 * @param processCount_4 processCount_4
	 * @param index          Page Index
	 * @param limit          Page Data Limit
	 * @param titaVo         Variable-Length Argument
	 * @return Slice TxAmlCredit TxAmlCredit of List
	 */
	public Slice<TxAmlCredit> processNo(List<String> reviewType_0, int dataDt_1, int dataDt_2, String processType_3, int processCount_4, int index, int limit, TitaVo... titaVo);

	/**
	 * ReviewType ^i ,AND DataDt &gt;= ,AND DataDt &lt;= ,AND ProcessType % ,AND
	 * ProcessCount &gt;
	 *
	 * @param reviewType_0   reviewType_0
	 * @param dataDt_1       dataDt_1
	 * @param dataDt_2       dataDt_2
	 * @param processType_3  processType_3
	 * @param processCount_4 processCount_4
	 * @param index          Page Index
	 * @param limit          Page Data Limit
	 * @param titaVo         Variable-Length Argument
	 * @return Slice TxAmlCredit TxAmlCredit of List
	 */
	public Slice<TxAmlCredit> processYes(List<String> reviewType_0, int dataDt_1, int dataDt_2, String processType_3, int processCount_4, int index, int limit, TitaVo... titaVo);

	/**
	 * DataDt =
	 *
	 * @param dataDt_0 dataDt_0
	 * @param index    Page Index
	 * @param limit    Page Data Limit
	 * @param titaVo   Variable-Length Argument
	 * @return Slice TxAmlCredit TxAmlCredit of List
	 */
	public Slice<TxAmlCredit> dataDtAll(int dataDt_0, int index, int limit, TitaVo... titaVo);

	/**
	 * hold By TxAmlCredit
	 * 
	 * @param txAmlCreditId key
	 * @param titaVo        Variable-Length Argument
	 * @return TxAmlCredit TxAmlCredit
	 */
	public TxAmlCredit holdById(TxAmlCreditId txAmlCreditId, TitaVo... titaVo);

	/**
	 * hold By TxAmlCredit
	 * 
	 * @param txAmlCredit key
	 * @param titaVo      Variable-Length Argument
	 * @return TxAmlCredit TxAmlCredit
	 */
	public TxAmlCredit holdById(TxAmlCredit txAmlCredit, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param txAmlCredit Entity
	 * @param titaVo      Variable-Length Argument
	 * @return TxAmlCredit Entity
	 * @throws DBException exception
	 */
	public TxAmlCredit insert(TxAmlCredit txAmlCredit, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param txAmlCredit Entity
	 * @param titaVo      Variable-Length Argument
	 * @return TxAmlCredit Entity
	 * @throws DBException exception
	 */
	public TxAmlCredit update(TxAmlCredit txAmlCredit, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param txAmlCredit Entity
	 * @param titaVo      Variable-Length Argument
	 * @return TxAmlCredit Entity
	 * @throws DBException exception
	 */
	public TxAmlCredit update2(TxAmlCredit txAmlCredit, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param txAmlCredit Entity
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(TxAmlCredit txAmlCredit, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param txAmlCredit Entity of List
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<TxAmlCredit> txAmlCredit, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param txAmlCredit Entity of List
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<TxAmlCredit> txAmlCredit, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param txAmlCredit Entity of List
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<TxAmlCredit> txAmlCredit, TitaVo... titaVo) throws DBException;

}
