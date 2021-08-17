package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.ClMovables;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.ClMovablesId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface ClMovablesService {

  /**
   * findByPrimaryKey
   *
   * @param clMovablesId PK
   * @param titaVo Variable-Length Argument
   * @return ClMovables ClMovables
   */
  public ClMovables findById(ClMovablesId clMovablesId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice ClMovables ClMovables of List
   */
  public Slice<ClMovables> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * ClCode1 = 
   *
   * @param clCode1_0 clCode1_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice ClMovables ClMovables of List
   */
  public Slice<ClMovables> findClCode1(int clCode1_0, int index, int limit, TitaVo... titaVo);

  /**
   * ClCode1 = ,AND ClCode2 = 
   *
   * @param clCode1_0 clCode1_0
   * @param clCode2_1 clCode2_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice ClMovables ClMovables of List
   */
  public Slice<ClMovables> findClCode2(int clCode1_0, int clCode2_1, int index, int limit, TitaVo... titaVo);

  /**
   * ClCode1 = ,AND ClCode2 &gt;= ,AND ClCode2 &lt;= ,AND ClNo = 
   *
   * @param clCode1_0 clCode1_0
   * @param clCode2_1 clCode2_1
   * @param clCode2_2 clCode2_2
   * @param clNo_3 clNo_3
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice ClMovables ClMovables of List
   */
  public Slice<ClMovables> selectL2047(int clCode1_0, int clCode2_1, int clCode2_2, int clNo_3, int index, int limit, TitaVo... titaVo);

  /**
   * ProductBrand = ,AND ProductSpec = ,AND OwnerCustUKey =
   *
   * @param productBrand_0 productBrand_0
   * @param productSpec_1 productSpec_1
   * @param ownerCustUKey_2 ownerCustUKey_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice ClMovables ClMovables of List
   */
  public Slice<ClMovables> findUnique1(String productBrand_0, String productSpec_1, String ownerCustUKey_2, int index, int limit, TitaVo... titaVo);

  /**
   * ProductBrand =
   *
   * @param productBrand_0 productBrand_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice ClMovables ClMovables of List
   */
  public Slice<ClMovables> findUnique2(String productBrand_0, int index, int limit, TitaVo... titaVo);

  /**
   * LicenseNo = 
   *
   * @param licenseNo_0 licenseNo_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice ClMovables ClMovables of List
   */
  public Slice<ClMovables> findUnique3(String licenseNo_0, int index, int limit, TitaVo... titaVo);

  /**
   * EngineSN =
   *
   * @param engineSN_0 engineSN_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice ClMovables ClMovables of List
   */
  public Slice<ClMovables> findUnique4(String engineSN_0, int index, int limit, TitaVo... titaVo);

  /**
   * hold By ClMovables
   * 
   * @param clMovablesId key
   * @param titaVo Variable-Length Argument
   * @return ClMovables ClMovables
   */
  public ClMovables holdById(ClMovablesId clMovablesId, TitaVo... titaVo);

  /**
   * hold By ClMovables
   * 
   * @param clMovables key
   * @param titaVo Variable-Length Argument
   * @return ClMovables ClMovables
   */
  public ClMovables holdById(ClMovables clMovables, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param clMovables Entity
   * @param titaVo Variable-Length Argument
   * @return ClMovables Entity
   * @throws DBException exception
   */
  public ClMovables insert(ClMovables clMovables, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param clMovables Entity
   * @param titaVo Variable-Length Argument
   * @return ClMovables Entity
   * @throws DBException exception
   */
  public ClMovables update(ClMovables clMovables, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param clMovables Entity
   * @param titaVo Variable-Length Argument
   * @return ClMovables Entity
   * @throws DBException exception
   */
  public ClMovables update2(ClMovables clMovables, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param clMovables Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(ClMovables clMovables, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param clMovables Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<ClMovables> clMovables, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param clMovables Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<ClMovables> clMovables, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param clMovables Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<ClMovables> clMovables, TitaVo... titaVo) throws DBException;

}
