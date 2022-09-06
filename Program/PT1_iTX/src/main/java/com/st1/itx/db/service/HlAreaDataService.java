package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.HlAreaData;
import org.springframework.data.domain.Slice;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface HlAreaDataService {

	/**
	 * findByPrimaryKey
	 *
	 * @param areaUnitNo PK
	 * @param titaVo     Variable-Length Argument
	 * @return HlAreaData HlAreaData
	 */
	public HlAreaData findById(String areaUnitNo, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice HlAreaData HlAreaData of List
	 */
	public Slice<HlAreaData> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * AreaUnitNo =
	 *
	 * @param areaUnitNo_0 areaUnitNo_0
	 * @param index        Page Index
	 * @param limit        Page Data Limit
	 * @param titaVo       Variable-Length Argument
	 * @return Slice HlAreaData HlAreaData of List
	 */
	public Slice<HlAreaData> FindAreaUnitNo(String areaUnitNo_0, int index, int limit, TitaVo... titaVo);

	/**
	 * AreaChiefEmpNo =
	 *
	 * @param areaChiefEmpNo_0 areaChiefEmpNo_0
	 * @param index            Page Index
	 * @param limit            Page Data Limit
	 * @param titaVo           Variable-Length Argument
	 * @return Slice HlAreaData HlAreaData of List
	 */
	public Slice<HlAreaData> FindAreaChiefEmpNo(String areaChiefEmpNo_0, int index, int limit, TitaVo... titaVo);

	/**
	 * hold By HlAreaData
	 * 
	 * @param areaUnitNo key
	 * @param titaVo     Variable-Length Argument
	 * @return HlAreaData HlAreaData
	 */
	public HlAreaData holdById(String areaUnitNo, TitaVo... titaVo);

	/**
	 * hold By HlAreaData
	 * 
	 * @param hlAreaData key
	 * @param titaVo     Variable-Length Argument
	 * @return HlAreaData HlAreaData
	 */
	public HlAreaData holdById(HlAreaData hlAreaData, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param hlAreaData Entity
	 * @param titaVo     Variable-Length Argument
	 * @return HlAreaData Entity
	 * @throws DBException exception
	 */
	public HlAreaData insert(HlAreaData hlAreaData, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param hlAreaData Entity
	 * @param titaVo     Variable-Length Argument
	 * @return HlAreaData Entity
	 * @throws DBException exception
	 */
	public HlAreaData update(HlAreaData hlAreaData, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param hlAreaData Entity
	 * @param titaVo     Variable-Length Argument
	 * @return HlAreaData Entity
	 * @throws DBException exception
	 */
	public HlAreaData update2(HlAreaData hlAreaData, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param hlAreaData Entity
	 * @param titaVo     Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(HlAreaData hlAreaData, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param hlAreaData Entity of List
	 * @param titaVo     Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<HlAreaData> hlAreaData, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param hlAreaData Entity of List
	 * @param titaVo     Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<HlAreaData> hlAreaData, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param hlAreaData Entity of List
	 * @param titaVo     Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<HlAreaData> hlAreaData, TitaVo... titaVo) throws DBException;

}
