package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.ClLandReason;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.ClLandReasonId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface ClLandReasonService {

	/**
	 * findByPrimaryKey
	 *
	 * @param clLandReasonId PK
	 * @param titaVo         Variable-Length Argument
	 * @return ClLandReason ClLandReason
	 */
	public ClLandReason findById(ClLandReasonId clLandReasonId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice ClLandReason ClLandReason of List
	 */
	public Slice<ClLandReason> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * ClCode1 = ,AND ClCode2 = ,AND ClNo =
	 *
	 * @param clCode1_0 clCode1_0
	 * @param clCode2_1 clCode2_1
	 * @param clNo_2    clNo_2
	 * @param titaVo    Variable-Length Argument
	 * @return Slice ClLandReason ClLandReason of List
	 */
	public ClLandReason clNoFirst(int clCode1_0, int clCode2_1, int clNo_2, TitaVo... titaVo);

	/**
	 * ClCode1 = ,AND ClCode2 = ,AND ClNo =
	 *
	 * @param clCode1_0 clCode1_0
	 * @param clCode2_1 clCode2_1
	 * @param clNo_2    clNo_2
	 * @param index     Page Index
	 * @param limit     Page Data Limit
	 * @param titaVo    Variable-Length Argument
	 * @return Slice ClLandReason ClLandReason of List
	 */
	public Slice<ClLandReason> clNoEq(int clCode1_0, int clCode2_1, int clNo_2, int index, int limit, TitaVo... titaVo);

	/**
	 * hold By ClLandReason
	 * 
	 * @param clLandReasonId key
	 * @param titaVo         Variable-Length Argument
	 * @return ClLandReason ClLandReason
	 */
	public ClLandReason holdById(ClLandReasonId clLandReasonId, TitaVo... titaVo);

	/**
	 * hold By ClLandReason
	 * 
	 * @param clLandReason key
	 * @param titaVo       Variable-Length Argument
	 * @return ClLandReason ClLandReason
	 */
	public ClLandReason holdById(ClLandReason clLandReason, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param clLandReason Entity
	 * @param titaVo       Variable-Length Argument
	 * @return ClLandReason Entity
	 * @throws DBException exception
	 */
	public ClLandReason insert(ClLandReason clLandReason, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param clLandReason Entity
	 * @param titaVo       Variable-Length Argument
	 * @return ClLandReason Entity
	 * @throws DBException exception
	 */
	public ClLandReason update(ClLandReason clLandReason, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param clLandReason Entity
	 * @param titaVo       Variable-Length Argument
	 * @return ClLandReason Entity
	 * @throws DBException exception
	 */
	public ClLandReason update2(ClLandReason clLandReason, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param clLandReason Entity
	 * @param titaVo       Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(ClLandReason clLandReason, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param clLandReason Entity of List
	 * @param titaVo       Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<ClLandReason> clLandReason, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param clLandReason Entity of List
	 * @param titaVo       Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<ClLandReason> clLandReason, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param clLandReason Entity of List
	 * @param titaVo       Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<ClLandReason> clLandReason, TitaVo... titaVo) throws DBException;

}
