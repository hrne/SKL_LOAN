package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.ClParking;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.ClParkingId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface ClParkingService {

	/**
	 * findByPrimaryKey
	 *
	 * @param clParkingId PK
	 * @param titaVo      Variable-Length Argument
	 * @return ClParking ClParking
	 */
	public ClParking findById(ClParkingId clParkingId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice ClParking ClParking of List
	 */
	public Slice<ClParking> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * ClCode1 = ,AND ClCode2 = ,AND ClNo =
	 *
	 * @param clCode1_0 clCode1_0
	 * @param clCode2_1 clCode2_1
	 * @param clNo_2    clNo_2
	 * @param index     Page Index
	 * @param limit     Page Data Limit
	 * @param titaVo    Variable-Length Argument
	 * @return Slice ClParking ClParking of List
	 */
	public Slice<ClParking> clNoEq(int clCode1_0, int clCode2_1, int clNo_2, int index, int limit, TitaVo... titaVo);

	/**
	 * hold By ClParking
	 * 
	 * @param clParkingId key
	 * @param titaVo      Variable-Length Argument
	 * @return ClParking ClParking
	 */
	public ClParking holdById(ClParkingId clParkingId, TitaVo... titaVo);

	/**
	 * hold By ClParking
	 * 
	 * @param clParking key
	 * @param titaVo    Variable-Length Argument
	 * @return ClParking ClParking
	 */
	public ClParking holdById(ClParking clParking, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param clParking Entity
	 * @param titaVo    Variable-Length Argument
	 * @return ClParking Entity
	 * @throws DBException exception
	 */
	public ClParking insert(ClParking clParking, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param clParking Entity
	 * @param titaVo    Variable-Length Argument
	 * @return ClParking Entity
	 * @throws DBException exception
	 */
	public ClParking update(ClParking clParking, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param clParking Entity
	 * @param titaVo    Variable-Length Argument
	 * @return ClParking Entity
	 * @throws DBException exception
	 */
	public ClParking update2(ClParking clParking, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param clParking Entity
	 * @param titaVo    Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(ClParking clParking, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param clParking Entity of List
	 * @param titaVo    Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<ClParking> clParking, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param clParking Entity of List
	 * @param titaVo    Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<ClParking> clParking, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param clParking Entity of List
	 * @param titaVo    Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<ClParking> clParking, TitaVo... titaVo) throws DBException;

}
