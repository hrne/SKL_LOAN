package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CdLandSection;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.CdLandSectionId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CdLandSectionService {

	/**
	 * findByPrimaryKey
	 *
	 * @param cdLandSectionId PK
	 * @param titaVo          Variable-Length Argument
	 * @return CdLandSection CdLandSection
	 */
	public CdLandSection findById(CdLandSectionId cdLandSectionId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice CdLandSection CdLandSection of List
	 */
	public Slice<CdLandSection> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * CityCode =
	 *
	 * @param cityCode_0 cityCode_0
	 * @param index      Page Index
	 * @param limit      Page Data Limit
	 * @param titaVo     Variable-Length Argument
	 * @return Slice CdLandSection CdLandSection of List
	 */
	public Slice<CdLandSection> cityCodeEq(String cityCode_0, int index, int limit, TitaVo... titaVo);

	/**
	 * CityCode = ,AND AreaCode =
	 *
	 * @param cityCode_0 cityCode_0
	 * @param areaCode_1 areaCode_1
	 * @param index      Page Index
	 * @param limit      Page Data Limit
	 * @param titaVo     Variable-Length Argument
	 * @return Slice CdLandSection CdLandSection of List
	 */
	public Slice<CdLandSection> areaCodeEq(String cityCode_0, String areaCode_1, int index, int limit, TitaVo... titaVo);

	/**
	 * LandOfficeCode =
	 *
	 * @param landOfficeCode_0 landOfficeCode_0
	 * @param index            Page Index
	 * @param limit            Page Data Limit
	 * @param titaVo           Variable-Length Argument
	 * @return Slice CdLandSection CdLandSection of List
	 */
	public Slice<CdLandSection> landOfficeCodeEq(String landOfficeCode_0, int index, int limit, TitaVo... titaVo);

	/**
	 * hold By CdLandSection
	 * 
	 * @param cdLandSectionId key
	 * @param titaVo          Variable-Length Argument
	 * @return CdLandSection CdLandSection
	 */
	public CdLandSection holdById(CdLandSectionId cdLandSectionId, TitaVo... titaVo);

	/**
	 * hold By CdLandSection
	 * 
	 * @param cdLandSection key
	 * @param titaVo        Variable-Length Argument
	 * @return CdLandSection CdLandSection
	 */
	public CdLandSection holdById(CdLandSection cdLandSection, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param cdLandSection Entity
	 * @param titaVo        Variable-Length Argument
	 * @return CdLandSection Entity
	 * @throws DBException exception
	 */
	public CdLandSection insert(CdLandSection cdLandSection, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param cdLandSection Entity
	 * @param titaVo        Variable-Length Argument
	 * @return CdLandSection Entity
	 * @throws DBException exception
	 */
	public CdLandSection update(CdLandSection cdLandSection, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param cdLandSection Entity
	 * @param titaVo        Variable-Length Argument
	 * @return CdLandSection Entity
	 * @throws DBException exception
	 */
	public CdLandSection update2(CdLandSection cdLandSection, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param cdLandSection Entity
	 * @param titaVo        Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(CdLandSection cdLandSection, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param cdLandSection Entity of List
	 * @param titaVo        Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<CdLandSection> cdLandSection, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param cdLandSection Entity of List
	 * @param titaVo        Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<CdLandSection> cdLandSection, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param cdLandSection Entity of List
	 * @param titaVo        Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<CdLandSection> cdLandSection, TitaVo... titaVo) throws DBException;

}
