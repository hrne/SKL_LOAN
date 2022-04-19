package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CdArea;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.CdAreaId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CdAreaService {

	/**
	 * findByPrimaryKey
	 *
	 * @param cdAreaId PK
	 * @param titaVo   Variable-Length Argument
	 * @return CdArea CdArea
	 */
	public CdArea findById(CdAreaId cdAreaId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice CdArea CdArea of List
	 */
	public Slice<CdArea> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * CityCode &gt;= ,AND CityCode &lt;=
	 *
	 * @param cityCode_0 cityCode_0
	 * @param cityCode_1 cityCode_1
	 * @param index      Page Index
	 * @param limit      Page Data Limit
	 * @param titaVo     Variable-Length Argument
	 * @return Slice CdArea CdArea of List
	 */
	public Slice<CdArea> cityCodeEq(String cityCode_0, String cityCode_1, int index, int limit, TitaVo... titaVo);

	/**
	 * CityCode &gt;= ,AND CityCode &lt;= ,AND AreaCode &gt;= ,AND AreaCode &lt;=
	 *
	 * @param cityCode_0 cityCode_0
	 * @param cityCode_1 cityCode_1
	 * @param areaCode_2 areaCode_2
	 * @param areaCode_3 areaCode_3
	 * @param index      Page Index
	 * @param limit      Page Data Limit
	 * @param titaVo     Variable-Length Argument
	 * @return Slice CdArea CdArea of List
	 */
	public Slice<CdArea> areaCodeRange(String cityCode_0, String cityCode_1, String areaCode_2, String areaCode_3, int index, int limit, TitaVo... titaVo);

	/**
	 * Zip3 =
	 *
	 * @param zip3_0 zip3_0
	 * @param titaVo Variable-Length Argument
	 * @return Slice CdArea CdArea of List
	 */
	public CdArea Zip3First(String zip3_0, TitaVo... titaVo);

	/**
	 * hold By CdArea
	 * 
	 * @param cdAreaId key
	 * @param titaVo   Variable-Length Argument
	 * @return CdArea CdArea
	 */
	public CdArea holdById(CdAreaId cdAreaId, TitaVo... titaVo);

	/**
	 * hold By CdArea
	 * 
	 * @param cdArea key
	 * @param titaVo Variable-Length Argument
	 * @return CdArea CdArea
	 */
	public CdArea holdById(CdArea cdArea, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param cdArea Entity
	 * @param titaVo Variable-Length Argument
	 * @return CdArea Entity
	 * @throws DBException exception
	 */
	public CdArea insert(CdArea cdArea, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param cdArea Entity
	 * @param titaVo Variable-Length Argument
	 * @return CdArea Entity
	 * @throws DBException exception
	 */
	public CdArea update(CdArea cdArea, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param cdArea Entity
	 * @param titaVo Variable-Length Argument
	 * @return CdArea Entity
	 * @throws DBException exception
	 */
	public CdArea update2(CdArea cdArea, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param cdArea Entity
	 * @param titaVo Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(CdArea cdArea, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param cdArea Entity of List
	 * @param titaVo Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<CdArea> cdArea, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param cdArea Entity of List
	 * @param titaVo Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<CdArea> cdArea, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param cdArea Entity of List
	 * @param titaVo Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<CdArea> cdArea, TitaVo... titaVo) throws DBException;

}
