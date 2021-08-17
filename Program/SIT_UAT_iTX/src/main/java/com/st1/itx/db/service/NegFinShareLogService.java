package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.NegFinShareLog;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.NegFinShareLogId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface NegFinShareLogService {

	/**
	 * findByPrimaryKey
	 *
	 * @param negFinShareLogId PK
	 * @param titaVo           Variable-Length Argument
	 * @return NegFinShareLog NegFinShareLog
	 */
	public NegFinShareLog findById(NegFinShareLogId negFinShareLogId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice NegFinShareLog NegFinShareLog of List
	 */
	public Slice<NegFinShareLog> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * CustNo = , AND CaseSeq =
	 *
	 * @param custNo_0  custNo_0
	 * @param caseSeq_1 caseSeq_1
	 * @param index     Page Index
	 * @param limit     Page Data Limit
	 * @param titaVo    Variable-Length Argument
	 * @return Slice NegFinShareLog NegFinShareLog of List
	 */
	public Slice<NegFinShareLog> FindAllFinCode(int custNo_0, int caseSeq_1, int index, int limit, TitaVo... titaVo);

	/**
	 * CustNo = , AND CaseSeq = ,AND Seq =
	 *
	 * @param custNo_0  custNo_0
	 * @param caseSeq_1 caseSeq_1
	 * @param seq_2     seq_2
	 * @param index     Page Index
	 * @param limit     Page Data Limit
	 * @param titaVo    Variable-Length Argument
	 * @return Slice NegFinShareLog NegFinShareLog of List
	 */
	public Slice<NegFinShareLog> FindNewSeq(int custNo_0, int caseSeq_1, int seq_2, int index, int limit, TitaVo... titaVo);

	/**
	 * hold By NegFinShareLog
	 * 
	 * @param negFinShareLogId key
	 * @param titaVo           Variable-Length Argument
	 * @return NegFinShareLog NegFinShareLog
	 */
	public NegFinShareLog holdById(NegFinShareLogId negFinShareLogId, TitaVo... titaVo);

	/**
	 * hold By NegFinShareLog
	 * 
	 * @param negFinShareLog key
	 * @param titaVo         Variable-Length Argument
	 * @return NegFinShareLog NegFinShareLog
	 */
	public NegFinShareLog holdById(NegFinShareLog negFinShareLog, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param negFinShareLog Entity
	 * @param titaVo         Variable-Length Argument
	 * @return NegFinShareLog Entity
	 * @throws DBException exception
	 */
	public NegFinShareLog insert(NegFinShareLog negFinShareLog, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param negFinShareLog Entity
	 * @param titaVo         Variable-Length Argument
	 * @return NegFinShareLog Entity
	 * @throws DBException exception
	 */
	public NegFinShareLog update(NegFinShareLog negFinShareLog, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param negFinShareLog Entity
	 * @param titaVo         Variable-Length Argument
	 * @return NegFinShareLog Entity
	 * @throws DBException exception
	 */
	public NegFinShareLog update2(NegFinShareLog negFinShareLog, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param negFinShareLog Entity
	 * @param titaVo         Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(NegFinShareLog negFinShareLog, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param negFinShareLog Entity of List
	 * @param titaVo         Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<NegFinShareLog> negFinShareLog, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param negFinShareLog Entity of List
	 * @param titaVo         Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<NegFinShareLog> negFinShareLog, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param negFinShareLog Entity of List
	 * @param titaVo         Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<NegFinShareLog> negFinShareLog, TitaVo... titaVo) throws DBException;

}
