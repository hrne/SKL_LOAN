package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CdLandOffice;
import org.springframework.data.domain.Slice;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CdLandOfficeService {

	/**
	 * findByPrimaryKey
	 *
	 * @param landOfficeCode PK
	 * @param titaVo         Variable-Length Argument
	 * @return CdLandOffice CdLandOffice
	 */
	public CdLandOffice findById(String landOfficeCode, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice CdLandOffice CdLandOffice of List
	 */
	public Slice<CdLandOffice> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * City =
	 *
	 * @param city_0 city_0
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice CdLandOffice CdLandOffice of List
	 */
	public Slice<CdLandOffice> findCity(String city_0, int index, int limit, TitaVo... titaVo);

	/**
	 * Town =
	 *
	 * @param town_0 town_0
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice CdLandOffice CdLandOffice of List
	 */
	public Slice<CdLandOffice> findTown(String town_0, int index, int limit, TitaVo... titaVo);

	/**
	 * CityCode =
	 *
	 * @param cityCode_0 cityCode_0
	 * @param index      Page Index
	 * @param limit      Page Data Limit
	 * @param titaVo     Variable-Length Argument
	 * @return Slice CdLandOffice CdLandOffice of List
	 */
	public Slice<CdLandOffice> cityCodeEq(String cityCode_0, int index, int limit, TitaVo... titaVo);

	/**
	 * CityCode = ,AND AreaCode =
	 *
	 * @param cityCode_0 cityCode_0
	 * @param areaCode_1 areaCode_1
	 * @param index      Page Index
	 * @param limit      Page Data Limit
	 * @param titaVo     Variable-Length Argument
	 * @return Slice CdLandOffice CdLandOffice of List
	 */
	public Slice<CdLandOffice> AreaCodeEq(String cityCode_0, String areaCode_1, int index, int limit, TitaVo... titaVo);

	/**
	 * hold By CdLandOffice
	 * 
	 * @param landOfficeCode key
	 * @param titaVo         Variable-Length Argument
	 * @return CdLandOffice CdLandOffice
	 */
	public CdLandOffice holdById(String landOfficeCode, TitaVo... titaVo);

	/**
	 * hold By CdLandOffice
	 * 
	 * @param cdLandOffice key
	 * @param titaVo       Variable-Length Argument
	 * @return CdLandOffice CdLandOffice
	 */
	public CdLandOffice holdById(CdLandOffice cdLandOffice, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param cdLandOffice Entity
	 * @param titaVo       Variable-Length Argument
	 * @return CdLandOffice Entity
	 * @throws DBException exception
	 */
	public CdLandOffice insert(CdLandOffice cdLandOffice, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param cdLandOffice Entity
	 * @param titaVo       Variable-Length Argument
	 * @return CdLandOffice Entity
	 * @throws DBException exception
	 */
	public CdLandOffice update(CdLandOffice cdLandOffice, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param cdLandOffice Entity
	 * @param titaVo       Variable-Length Argument
	 * @return CdLandOffice Entity
	 * @throws DBException exception
	 */
	public CdLandOffice update2(CdLandOffice cdLandOffice, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param cdLandOffice Entity
	 * @param titaVo       Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(CdLandOffice cdLandOffice, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param cdLandOffice Entity of List
	 * @param titaVo       Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<CdLandOffice> cdLandOffice, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param cdLandOffice Entity of List
	 * @param titaVo       Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<CdLandOffice> cdLandOffice, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param cdLandOffice Entity of List
	 * @param titaVo       Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<CdLandOffice> cdLandOffice, TitaVo... titaVo) throws DBException;

}
