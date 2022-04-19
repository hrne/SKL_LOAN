package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.NegFinShare;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.NegFinShareId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface NegFinShareService {

	/**
	 * findByPrimaryKey
	 *
	 * @param negFinShareId PK
	 * @param titaVo        Variable-Length Argument
	 * @return NegFinShare NegFinShare
	 */
	public NegFinShare findById(NegFinShareId negFinShareId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice NegFinShare NegFinShare of List
	 */
	public Slice<NegFinShare> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * CustNo = , AND CaseSeq =
	 *
	 * @param custNo_0  custNo_0
	 * @param caseSeq_1 caseSeq_1
	 * @param index     Page Index
	 * @param limit     Page Data Limit
	 * @param titaVo    Variable-Length Argument
	 * @return Slice NegFinShare NegFinShare of List
	 */
	public Slice<NegFinShare> findFinCodeAll(int custNo_0, int caseSeq_1, int index, int limit, TitaVo... titaVo);

	/**
	 * hold By NegFinShare
	 * 
	 * @param negFinShareId key
	 * @param titaVo        Variable-Length Argument
	 * @return NegFinShare NegFinShare
	 */
	public NegFinShare holdById(NegFinShareId negFinShareId, TitaVo... titaVo);

	/**
	 * hold By NegFinShare
	 * 
	 * @param negFinShare key
	 * @param titaVo      Variable-Length Argument
	 * @return NegFinShare NegFinShare
	 */
	public NegFinShare holdById(NegFinShare negFinShare, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param negFinShare Entity
	 * @param titaVo      Variable-Length Argument
	 * @return NegFinShare Entity
	 * @throws DBException exception
	 */
	public NegFinShare insert(NegFinShare negFinShare, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param negFinShare Entity
	 * @param titaVo      Variable-Length Argument
	 * @return NegFinShare Entity
	 * @throws DBException exception
	 */
	public NegFinShare update(NegFinShare negFinShare, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param negFinShare Entity
	 * @param titaVo      Variable-Length Argument
	 * @return NegFinShare Entity
	 * @throws DBException exception
	 */
	public NegFinShare update2(NegFinShare negFinShare, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param negFinShare Entity
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(NegFinShare negFinShare, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param negFinShare Entity of List
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<NegFinShare> negFinShare, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param negFinShare Entity of List
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<NegFinShare> negFinShare, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param negFinShare Entity of List
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<NegFinShare> negFinShare, TitaVo... titaVo) throws DBException;

}
