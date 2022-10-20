package com.st1.itx.db.service;

import java.util.List;

import java.math.BigDecimal;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.ClMain;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.ClMainId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface ClMainService {

  /**
   * findByPrimaryKey
   *
   * @param clMainId PK
   * @param titaVo Variable-Length Argument
   * @return ClMain ClMain
   */
  public ClMain findById(ClMainId clMainId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice ClMain ClMain of List
   */
  public Slice<ClMain> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * ClCode1 = 
   *
   * @param clCode1_0 clCode1_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice ClMain ClMain of List
   */
  public Slice<ClMain> findClCode1(int clCode1_0, int index, int limit, TitaVo... titaVo);

  /**
   * ClCode1 = ,AND ClCode2 = 
   *
   * @param clCode1_0 clCode1_0
   * @param clCode2_1 clCode2_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice ClMain ClMain of List
   */
  public Slice<ClMain> findClCode2(int clCode1_0, int clCode2_1, int index, int limit, TitaVo... titaVo);

  /**
   * ClCode1 = ,AND ClCode2 = ,AND ClNo =
   *
   * @param clCode1_0 clCode1_0
   * @param clCode2_1 clCode2_1
   * @param clNo_2 clNo_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice ClMain ClMain of List
   */
  public Slice<ClMain> findClNo(int clCode1_0, int clCode2_1, int clNo_2, int index, int limit, TitaVo... titaVo);

  /**
   * CustUKey = 
   *
   * @param custUKey_0 custUKey_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice ClMain ClMain of List
   */
  public Slice<ClMain> findCustUKey(String custUKey_0, int index, int limit, TitaVo... titaVo);

  /**
   * ClCode1 &gt;= ,AND ClCode1 &lt;= ,AND ClTypeCode &gt;= ,AND ClTypeCode &lt;= ,AND ClNo &gt;= ,AND ClNo &lt;=
   *
   * @param clCode1_0 clCode1_0
   * @param clCode1_1 clCode1_1
   * @param clTypeCode_2 clTypeCode_2
   * @param clTypeCode_3 clTypeCode_3
   * @param clNo_4 clNo_4
   * @param clNo_5 clNo_5
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice ClMain ClMain of List
   */
  public Slice<ClMain> findRange(int clCode1_0, int clCode1_1, String clTypeCode_2, String clTypeCode_3, int clNo_4, int clNo_5, int index, int limit, TitaVo... titaVo);

  /**
   * ClCode1 &gt;= ,AND ClCode1 &lt;= ,AND ClTypeCode &gt;= ,AND ClTypeCode &lt;= ,AND ClNo &gt;= ,AND ClNo &lt;= ,AND CustUKey = 
   *
   * @param clCode1_0 clCode1_0
   * @param clCode1_1 clCode1_1
   * @param clTypeCode_2 clTypeCode_2
   * @param clTypeCode_3 clTypeCode_3
   * @param clNo_4 clNo_4
   * @param clNo_5 clNo_5
   * @param custUKey_6 custUKey_6
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice ClMain ClMain of List
   */
  public Slice<ClMain> selectForL2038(int clCode1_0, int clCode1_1, String clTypeCode_2, String clTypeCode_3, int clNo_4, int clNo_5, String custUKey_6, int index, int limit, TitaVo... titaVo);

  /**
   * ClCode1 &gt;= ,AND ClCode1 &lt;= ,AND ClCode2 &gt;= ,AND ClCode2 &lt;= ,AND ClNo &gt;= ,AND ClNo &lt;= ,AND ClTypeCode &gt;= ,AND ClTypeCode &lt;=
   *
   * @param clCode1_0 clCode1_0
   * @param clCode1_1 clCode1_1
   * @param clCode2_2 clCode2_2
   * @param clCode2_3 clCode2_3
   * @param clNo_4 clNo_4
   * @param clNo_5 clNo_5
   * @param clTypeCode_6 clTypeCode_6
   * @param clTypeCode_7 clTypeCode_7
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice ClMain ClMain of List
   */
  public Slice<ClMain> selectForL2049(int clCode1_0, int clCode1_1, int clCode2_2, int clCode2_3, int clNo_4, int clNo_5, String clTypeCode_6, String clTypeCode_7, int index, int limit, TitaVo... titaVo);

  /**
   * ClCode1 = ,AND ClCode2 = 
   *
   * @param clCode1_0 clCode1_0
   * @param clCode2_1 clCode2_1
   * @param titaVo Variable-Length Argument
   * @return Slice ClMain ClMain of List
   */
  public ClMain lastClNoFirst(int clCode1_0, int clCode2_1, TitaVo... titaVo);

  /**
   * ClCode1 &gt;= ,AND ClCode1 &lt;= ,AND CustUKey = 
   *
   * @param clCode1_0 clCode1_0
   * @param clCode1_1 clCode1_1
   * @param custUKey_2 custUKey_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice ClMain ClMain of List
   */
  public Slice<ClMain> selectForL1001(int clCode1_0, int clCode1_1, String custUKey_2, int index, int limit, TitaVo... titaVo);

  /**
   * hold By ClMain
   * 
   * @param clMainId key
   * @param titaVo Variable-Length Argument
   * @return ClMain ClMain
   */
  public ClMain holdById(ClMainId clMainId, TitaVo... titaVo);

  /**
   * hold By ClMain
   * 
   * @param clMain key
   * @param titaVo Variable-Length Argument
   * @return ClMain ClMain
   */
  public ClMain holdById(ClMain clMain, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param clMain Entity
   * @param titaVo Variable-Length Argument
   * @return ClMain Entity
   * @throws DBException exception
   */
  public ClMain insert(ClMain clMain, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param clMain Entity
   * @param titaVo Variable-Length Argument
   * @return ClMain Entity
   * @throws DBException exception
   */
  public ClMain update(ClMain clMain, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param clMain Entity
   * @param titaVo Variable-Length Argument
   * @return ClMain Entity
   * @throws DBException exception
   */
  public ClMain update2(ClMain clMain, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param clMain Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(ClMain clMain, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param clMain Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<ClMain> clMain, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param clMain Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<ClMain> clMain, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param clMain Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<ClMain> clMain, TitaVo... titaVo) throws DBException;

}
