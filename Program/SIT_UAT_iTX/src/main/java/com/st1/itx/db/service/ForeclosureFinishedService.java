package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.ForeclosureFinished;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.ForeclosureFinishedId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface ForeclosureFinishedService {

	/**
	 * findByPrimaryKey
	 *
	 * @param foreclosureFinishedId PK
	 * @param titaVo                Variable-Length Argument
	 * @return ForeclosureFinished ForeclosureFinished
	 */
	public ForeclosureFinished findById(ForeclosureFinishedId foreclosureFinishedId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice ForeclosureFinished ForeclosureFinished of List
	 */
	public Slice<ForeclosureFinished> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * hold By ForeclosureFinished
	 * 
	 * @param foreclosureFinishedId key
	 * @param titaVo                Variable-Length Argument
	 * @return ForeclosureFinished ForeclosureFinished
	 */
	public ForeclosureFinished holdById(ForeclosureFinishedId foreclosureFinishedId, TitaVo... titaVo);

	/**
	 * hold By ForeclosureFinished
	 * 
	 * @param foreclosureFinished key
	 * @param titaVo              Variable-Length Argument
	 * @return ForeclosureFinished ForeclosureFinished
	 */
	public ForeclosureFinished holdById(ForeclosureFinished foreclosureFinished, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param foreclosureFinished Entity
	 * @param titaVo              Variable-Length Argument
	 * @return ForeclosureFinished Entity
	 * @throws DBException exception
	 */
	public ForeclosureFinished insert(ForeclosureFinished foreclosureFinished, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param foreclosureFinished Entity
	 * @param titaVo              Variable-Length Argument
	 * @return ForeclosureFinished Entity
	 * @throws DBException exception
	 */
	public ForeclosureFinished update(ForeclosureFinished foreclosureFinished, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param foreclosureFinished Entity
	 * @param titaVo              Variable-Length Argument
	 * @return ForeclosureFinished Entity
	 * @throws DBException exception
	 */
	public ForeclosureFinished update2(ForeclosureFinished foreclosureFinished, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param foreclosureFinished Entity
	 * @param titaVo              Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(ForeclosureFinished foreclosureFinished, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param foreclosureFinished Entity of List
	 * @param titaVo              Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<ForeclosureFinished> foreclosureFinished, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param foreclosureFinished Entity of List
	 * @param titaVo              Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<ForeclosureFinished> foreclosureFinished, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param foreclosureFinished Entity of List
	 * @param titaVo              Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<ForeclosureFinished> foreclosureFinished, TitaVo... titaVo) throws DBException;

}
