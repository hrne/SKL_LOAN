package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.RelsMain;
import org.springframework.data.domain.Slice;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface RelsMainService {

	/**
	 * findByPrimaryKey
	 *
	 * @param relsUKey PK
	 * @param titaVo   Variable-Length Argument
	 * @return RelsMain RelsMain
	 */
	public RelsMain findById(String relsUKey, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice RelsMain RelsMain of List
	 */
	public Slice<RelsMain> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * RelsId =
	 *
	 * @param relsId_0 relsId_0
	 * @param titaVo   Variable-Length Argument
	 * @return Slice RelsMain RelsMain of List
	 */
	public RelsMain RelsIdFirst(String relsId_0, TitaVo... titaVo);

	/**
	 * RelsType =
	 *
	 * @param relsType_0 relsType_0
	 * @param index      Page Index
	 * @param limit      Page Data Limit
	 * @param titaVo     Variable-Length Argument
	 * @return Slice RelsMain RelsMain of List
	 */
	public Slice<RelsMain> RelsPerson(int relsType_0, int index, int limit, TitaVo... titaVo);

	/**
	 * RelsName =
	 *
	 * @param relsName_0 relsName_0
	 * @param titaVo     Variable-Length Argument
	 * @return Slice RelsMain RelsMain of List
	 */
	public RelsMain RelsNameFirst(String relsName_0, TitaVo... titaVo);

	/**
	 * RelsName =
	 *
	 * @param relsName_0 relsName_0
	 * @param index      Page Index
	 * @param limit      Page Data Limit
	 * @param titaVo     Variable-Length Argument
	 * @return Slice RelsMain RelsMain of List
	 */
	public Slice<RelsMain> RelsNameEq(String relsName_0, int index, int limit, TitaVo... titaVo);

	/**
	 * RelsId =
	 *
	 * @param relsId_0 relsId_0
	 * @param index    Page Index
	 * @param limit    Page Data Limit
	 * @param titaVo   Variable-Length Argument
	 * @return Slice RelsMain RelsMain of List
	 */
	public Slice<RelsMain> RelsIdEq(String relsId_0, int index, int limit, TitaVo... titaVo);

	/**
	 * hold By RelsMain
	 * 
	 * @param relsUKey key
	 * @param titaVo   Variable-Length Argument
	 * @return RelsMain RelsMain
	 */
	public RelsMain holdById(String relsUKey, TitaVo... titaVo);

	/**
	 * hold By RelsMain
	 * 
	 * @param relsMain key
	 * @param titaVo   Variable-Length Argument
	 * @return RelsMain RelsMain
	 */
	public RelsMain holdById(RelsMain relsMain, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param relsMain Entity
	 * @param titaVo   Variable-Length Argument
	 * @return RelsMain Entity
	 * @throws DBException exception
	 */
	public RelsMain insert(RelsMain relsMain, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param relsMain Entity
	 * @param titaVo   Variable-Length Argument
	 * @return RelsMain Entity
	 * @throws DBException exception
	 */
	public RelsMain update(RelsMain relsMain, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param relsMain Entity
	 * @param titaVo   Variable-Length Argument
	 * @return RelsMain Entity
	 * @throws DBException exception
	 */
	public RelsMain update2(RelsMain relsMain, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param relsMain Entity
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(RelsMain relsMain, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param relsMain Entity of List
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<RelsMain> relsMain, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param relsMain Entity of List
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<RelsMain> relsMain, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param relsMain Entity of List
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<RelsMain> relsMain, TitaVo... titaVo) throws DBException;

}
