package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.HlCusData;
import org.springframework.data.domain.Slice;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface HlCusDataService {

	/**
	 * findByPrimaryKey
	 *
	 * @param hlCusNo PK
	 * @param titaVo  Variable-Length Argument
	 * @return HlCusData HlCusData
	 */
	public HlCusData findById(Long hlCusNo, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice HlCusData HlCusData of List
	 */
	public Slice<HlCusData> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * hold By HlCusData
	 * 
	 * @param hlCusNo key
	 * @param titaVo  Variable-Length Argument
	 * @return HlCusData HlCusData
	 */
	public HlCusData holdById(Long hlCusNo, TitaVo... titaVo);

	/**
	 * hold By HlCusData
	 * 
	 * @param hlCusData key
	 * @param titaVo    Variable-Length Argument
	 * @return HlCusData HlCusData
	 */
	public HlCusData holdById(HlCusData hlCusData, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param hlCusData Entity
	 * @param titaVo    Variable-Length Argument
	 * @return HlCusData Entity
	 * @throws DBException exception
	 */
	public HlCusData insert(HlCusData hlCusData, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param hlCusData Entity
	 * @param titaVo    Variable-Length Argument
	 * @return HlCusData Entity
	 * @throws DBException exception
	 */
	public HlCusData update(HlCusData hlCusData, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param hlCusData Entity
	 * @param titaVo    Variable-Length Argument
	 * @return HlCusData Entity
	 * @throws DBException exception
	 */
	public HlCusData update2(HlCusData hlCusData, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param hlCusData Entity
	 * @param titaVo    Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(HlCusData hlCusData, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param hlCusData Entity of List
	 * @param titaVo    Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<HlCusData> hlCusData, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param hlCusData Entity of List
	 * @param titaVo    Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<HlCusData> hlCusData, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param hlCusData Entity of List
	 * @param titaVo    Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<HlCusData> hlCusData, TitaVo... titaVo) throws DBException;

}
