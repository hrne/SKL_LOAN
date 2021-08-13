package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CdIndustry;
import org.springframework.data.domain.Slice;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CdIndustryService {

  /**
   * findByPrimaryKey
   *
   * @param industryCode PK
   * @param titaVo Variable-Length Argument
   * @return CdIndustry CdIndustry
   */
  public CdIndustry findById(String industryCode, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdIndustry CdIndustry of List
   */
  public Slice<CdIndustry> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * MainType &gt;= ,AND MainType &lt;= 
   *
   * @param mainType_0 mainType_0
   * @param mainType_1 mainType_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdIndustry CdIndustry of List
   */
  public Slice<CdIndustry> findMainType(String mainType_0, String mainType_1, int index, int limit, TitaVo... titaVo);

  /**
   * IndustryCode &gt;= ,AND IndustryCode &lt;= 
   *
   * @param industryCode_0 industryCode_0
   * @param industryCode_1 industryCode_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdIndustry CdIndustry of List
   */
  public Slice<CdIndustry> findIndustryCode(String industryCode_0, String industryCode_1, int index, int limit, TitaVo... titaVo);

  /**
   * hold By CdIndustry
   * 
   * @param industryCode key
   * @param titaVo Variable-Length Argument
   * @return CdIndustry CdIndustry
   */
  public CdIndustry holdById(String industryCode, TitaVo... titaVo);

  /**
   * hold By CdIndustry
   * 
   * @param cdIndustry key
   * @param titaVo Variable-Length Argument
   * @return CdIndustry CdIndustry
   */
  public CdIndustry holdById(CdIndustry cdIndustry, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param cdIndustry Entity
   * @param titaVo Variable-Length Argument
   * @return CdIndustry Entity
   * @throws DBException exception
   */
  public CdIndustry insert(CdIndustry cdIndustry, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param cdIndustry Entity
   * @param titaVo Variable-Length Argument
   * @return CdIndustry Entity
   * @throws DBException exception
   */
  public CdIndustry update(CdIndustry cdIndustry, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param cdIndustry Entity
   * @param titaVo Variable-Length Argument
   * @return CdIndustry Entity
   * @throws DBException exception
   */
  public CdIndustry update2(CdIndustry cdIndustry, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param cdIndustry Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(CdIndustry cdIndustry, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param cdIndustry Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<CdIndustry> cdIndustry, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param cdIndustry Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<CdIndustry> cdIndustry, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param cdIndustry Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<CdIndustry> cdIndustry, TitaVo... titaVo) throws DBException;

}
