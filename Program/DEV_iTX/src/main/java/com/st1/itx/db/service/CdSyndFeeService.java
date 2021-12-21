package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CdSyndFee;
import org.springframework.data.domain.Slice;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CdSyndFeeService {

	/**
	 * findByPrimaryKey
	 *
	 * @param syndFeeCode PK
	 * @param titaVo      Variable-Length Argument
	 * @return CdSyndFee CdSyndFee
	 */
	public CdSyndFee findById(String syndFeeCode, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice CdSyndFee CdSyndFee of List
	 */
	public Slice<CdSyndFee> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * AcctCode &gt;= ,AND AcctCode &lt;=
	 *
	 * @param acctCode_0 acctCode_0
	 * @param acctCode_1 acctCode_1
	 * @param titaVo     Variable-Length Argument
	 * @return Slice CdSyndFee CdSyndFee of List
	 */
	public CdSyndFee acctCodeFirst(String acctCode_0, String acctCode_1, TitaVo... titaVo);

	/**
	 * SyndFeeCode &gt;= ,AND SyndFeeCode &lt;=
	 *
	 * @param syndFeeCode_0 syndFeeCode_0
	 * @param syndFeeCode_1 syndFeeCode_1
	 * @param index         Page Index
	 * @param limit         Page Data Limit
	 * @param titaVo        Variable-Length Argument
	 * @return Slice CdSyndFee CdSyndFee of List
	 */
	public Slice<CdSyndFee> findSyndFeeCode(String syndFeeCode_0, String syndFeeCode_1, int index, int limit, TitaVo... titaVo);

	/**
	 * hold By CdSyndFee
	 * 
	 * @param syndFeeCode key
	 * @param titaVo      Variable-Length Argument
	 * @return CdSyndFee CdSyndFee
	 */
	public CdSyndFee holdById(String syndFeeCode, TitaVo... titaVo);

	/**
	 * hold By CdSyndFee
	 * 
	 * @param cdSyndFee key
	 * @param titaVo    Variable-Length Argument
	 * @return CdSyndFee CdSyndFee
	 */
	public CdSyndFee holdById(CdSyndFee cdSyndFee, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param cdSyndFee Entity
	 * @param titaVo    Variable-Length Argument
	 * @return CdSyndFee Entity
	 * @throws DBException exception
	 */
	public CdSyndFee insert(CdSyndFee cdSyndFee, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param cdSyndFee Entity
	 * @param titaVo    Variable-Length Argument
	 * @return CdSyndFee Entity
	 * @throws DBException exception
	 */
	public CdSyndFee update(CdSyndFee cdSyndFee, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param cdSyndFee Entity
	 * @param titaVo    Variable-Length Argument
	 * @return CdSyndFee Entity
	 * @throws DBException exception
	 */
	public CdSyndFee update2(CdSyndFee cdSyndFee, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param cdSyndFee Entity
	 * @param titaVo    Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(CdSyndFee cdSyndFee, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param cdSyndFee Entity of List
	 * @param titaVo    Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<CdSyndFee> cdSyndFee, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param cdSyndFee Entity of List
	 * @param titaVo    Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<CdSyndFee> cdSyndFee, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param cdSyndFee Entity of List
	 * @param titaVo    Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<CdSyndFee> cdSyndFee, TitaVo... titaVo) throws DBException;

}
