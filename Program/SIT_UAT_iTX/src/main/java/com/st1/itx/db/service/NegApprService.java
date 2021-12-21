package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.NegAppr;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.NegApprId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface NegApprService {

	/**
	 * findByPrimaryKey
	 *
	 * @param negApprId PK
	 * @param titaVo    Variable-Length Argument
	 * @return NegAppr NegAppr
	 */
	public NegAppr findById(NegApprId negApprId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice NegAppr NegAppr of List
	 */
	public Slice<NegAppr> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * YyyyMm&gt;= , AND YyyyMm&lt;=
	 *
	 * @param yyyyMm_0 yyyyMm_0
	 * @param yyyyMm_1 yyyyMm_1
	 * @param index    Page Index
	 * @param limit    Page Data Limit
	 * @param titaVo   Variable-Length Argument
	 * @return Slice NegAppr NegAppr of List
	 */
	public Slice<NegAppr> yyyyMmBetween(int yyyyMm_0, int yyyyMm_1, int index, int limit, TitaVo... titaVo);

	/**
	 * YyyyMm=
	 *
	 * @param yyyyMm_0 yyyyMm_0
	 * @param index    Page Index
	 * @param limit    Page Data Limit
	 * @param titaVo   Variable-Length Argument
	 * @return Slice NegAppr NegAppr of List
	 */
	public Slice<NegAppr> yyyyMmEq(int yyyyMm_0, int index, int limit, TitaVo... titaVo);

	/**
	 * ExportDate = , OR ApprAcDate = , OR BringUpDate =
	 *
	 * @param exportDate_0  exportDate_0
	 * @param apprAcDate_1  apprAcDate_1
	 * @param bringUpDate_2 bringUpDate_2
	 * @param index         Page Index
	 * @param limit         Page Data Limit
	 * @param titaVo        Variable-Length Argument
	 * @return Slice NegAppr NegAppr of List
	 */
	public Slice<NegAppr> acDateEq(int exportDate_0, int apprAcDate_1, int bringUpDate_2, int index, int limit, TitaVo... titaVo);

	/**
	 * BringUpDate =
	 *
	 * @param bringUpDate_0 bringUpDate_0
	 * @param index         Page Index
	 * @param limit         Page Data Limit
	 * @param titaVo        Variable-Length Argument
	 * @return Slice NegAppr NegAppr of List
	 */
	public Slice<NegAppr> bringUpDateEq(int bringUpDate_0, int index, int limit, TitaVo... titaVo);

	/**
	 * hold By NegAppr
	 * 
	 * @param negApprId key
	 * @param titaVo    Variable-Length Argument
	 * @return NegAppr NegAppr
	 */
	public NegAppr holdById(NegApprId negApprId, TitaVo... titaVo);

	/**
	 * hold By NegAppr
	 * 
	 * @param negAppr key
	 * @param titaVo  Variable-Length Argument
	 * @return NegAppr NegAppr
	 */
	public NegAppr holdById(NegAppr negAppr, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param negAppr Entity
	 * @param titaVo  Variable-Length Argument
	 * @return NegAppr Entity
	 * @throws DBException exception
	 */
	public NegAppr insert(NegAppr negAppr, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param negAppr Entity
	 * @param titaVo  Variable-Length Argument
	 * @return NegAppr Entity
	 * @throws DBException exception
	 */
	public NegAppr update(NegAppr negAppr, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param negAppr Entity
	 * @param titaVo  Variable-Length Argument
	 * @return NegAppr Entity
	 * @throws DBException exception
	 */
	public NegAppr update2(NegAppr negAppr, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param negAppr Entity
	 * @param titaVo  Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(NegAppr negAppr, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param negAppr Entity of List
	 * @param titaVo  Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<NegAppr> negAppr, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param negAppr Entity of List
	 * @param titaVo  Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<NegAppr> negAppr, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param negAppr Entity of List
	 * @param titaVo  Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<NegAppr> negAppr, TitaVo... titaVo) throws DBException;

}
