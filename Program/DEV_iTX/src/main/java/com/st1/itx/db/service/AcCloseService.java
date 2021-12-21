package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.AcClose;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.AcCloseId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface AcCloseService {

	/**
	 * findByPrimaryKey
	 *
	 * @param acCloseId PK
	 * @param titaVo    Variable-Length Argument
	 * @return AcClose AcClose
	 */
	public AcClose findById(AcCloseId acCloseId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice AcClose AcClose of List
	 */
	public Slice<AcClose> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * AcDate = ,AND BranchNo = ,AND SecNo &gt;=
	 *
	 * @param acDate_0   acDate_0
	 * @param branchNo_1 branchNo_1
	 * @param secNo_2    secNo_2
	 * @param index      Page Index
	 * @param limit      Page Data Limit
	 * @param titaVo     Variable-Length Argument
	 * @return Slice AcClose AcClose of List
	 */
	public Slice<AcClose> acCloseBranchNoEq(int acDate_0, String branchNo_1, String secNo_2, int index, int limit, TitaVo... titaVo);

	/**
	 * hold By AcClose
	 * 
	 * @param acCloseId key
	 * @param titaVo    Variable-Length Argument
	 * @return AcClose AcClose
	 */
	public AcClose holdById(AcCloseId acCloseId, TitaVo... titaVo);

	/**
	 * hold By AcClose
	 * 
	 * @param acClose key
	 * @param titaVo  Variable-Length Argument
	 * @return AcClose AcClose
	 */
	public AcClose holdById(AcClose acClose, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param acClose Entity
	 * @param titaVo  Variable-Length Argument
	 * @return AcClose Entity
	 * @throws DBException exception
	 */
	public AcClose insert(AcClose acClose, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param acClose Entity
	 * @param titaVo  Variable-Length Argument
	 * @return AcClose Entity
	 * @throws DBException exception
	 */
	public AcClose update(AcClose acClose, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param acClose Entity
	 * @param titaVo  Variable-Length Argument
	 * @return AcClose Entity
	 * @throws DBException exception
	 */
	public AcClose update2(AcClose acClose, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param acClose Entity
	 * @param titaVo  Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(AcClose acClose, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param acClose Entity of List
	 * @param titaVo  Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<AcClose> acClose, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param acClose Entity of List
	 * @param titaVo  Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<AcClose> acClose, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param acClose Entity of List
	 * @param titaVo  Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<AcClose> acClose, TitaVo... titaVo) throws DBException;

}
