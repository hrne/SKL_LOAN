package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.HlThreeDetail;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.HlThreeDetailId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface HlThreeDetailService {

	/**
	 * findByPrimaryKey
	 *
	 * @param hlThreeDetailId PK
	 * @param titaVo          Variable-Length Argument
	 * @return HlThreeDetail HlThreeDetail
	 */
	public HlThreeDetail findById(HlThreeDetailId hlThreeDetailId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice HlThreeDetail HlThreeDetail of List
	 */
	public Slice<HlThreeDetail> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * hold By HlThreeDetail
	 * 
	 * @param hlThreeDetailId key
	 * @param titaVo          Variable-Length Argument
	 * @return HlThreeDetail HlThreeDetail
	 */
	public HlThreeDetail holdById(HlThreeDetailId hlThreeDetailId, TitaVo... titaVo);

	/**
	 * hold By HlThreeDetail
	 * 
	 * @param hlThreeDetail key
	 * @param titaVo        Variable-Length Argument
	 * @return HlThreeDetail HlThreeDetail
	 */
	public HlThreeDetail holdById(HlThreeDetail hlThreeDetail, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param hlThreeDetail Entity
	 * @param titaVo        Variable-Length Argument
	 * @return HlThreeDetail Entity
	 * @throws DBException exception
	 */
	public HlThreeDetail insert(HlThreeDetail hlThreeDetail, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param hlThreeDetail Entity
	 * @param titaVo        Variable-Length Argument
	 * @return HlThreeDetail Entity
	 * @throws DBException exception
	 */
	public HlThreeDetail update(HlThreeDetail hlThreeDetail, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param hlThreeDetail Entity
	 * @param titaVo        Variable-Length Argument
	 * @return HlThreeDetail Entity
	 * @throws DBException exception
	 */
	public HlThreeDetail update2(HlThreeDetail hlThreeDetail, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param hlThreeDetail Entity
	 * @param titaVo        Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(HlThreeDetail hlThreeDetail, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param hlThreeDetail Entity of List
	 * @param titaVo        Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<HlThreeDetail> hlThreeDetail, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param hlThreeDetail Entity of List
	 * @param titaVo        Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<HlThreeDetail> hlThreeDetail, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param hlThreeDetail Entity of List
	 * @param titaVo        Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<HlThreeDetail> hlThreeDetail, TitaVo... titaVo) throws DBException;

}
