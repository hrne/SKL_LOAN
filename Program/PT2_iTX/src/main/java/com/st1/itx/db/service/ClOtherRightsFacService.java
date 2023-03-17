package com.st1.itx.db.service;

import java.util.List;

import java.math.BigDecimal;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.ClOtherRightsFac;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.ClOtherRightsFacId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface ClOtherRightsFacService {

  /**
   * findByPrimaryKey
   *
   * @param clOtherRightsFacId PK
   * @param titaVo Variable-Length Argument
   * @return ClOtherRightsFac ClOtherRightsFac
   */
  public ClOtherRightsFac findById(ClOtherRightsFacId clOtherRightsFacId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice ClOtherRightsFac ClOtherRightsFac of List
   */
  public Slice<ClOtherRightsFac> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * ClCode1 = 
   *
   * @param clCode1_0 clCode1_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice ClOtherRightsFac ClOtherRightsFac of List
   */
  public Slice<ClOtherRightsFac> findClCode1(int clCode1_0, int index, int limit, TitaVo... titaVo);

  /**
   * ClCode1 = ,AND ClCode2 = 
   *
   * @param clCode1_0 clCode1_0
   * @param clCode2_1 clCode2_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice ClOtherRightsFac ClOtherRightsFac of List
   */
  public Slice<ClOtherRightsFac> findClCode2(int clCode1_0, int clCode2_1, int index, int limit, TitaVo... titaVo);

  /**
   * ClCode1 = ,AND ClCode2 = ,AND ClNo = 
   *
   * @param clCode1_0 clCode1_0
   * @param clCode2_1 clCode2_1
   * @param clNo_2 clNo_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice ClOtherRightsFac ClOtherRightsFac of List
   */
  public Slice<ClOtherRightsFac> findClNo(int clCode1_0, int clCode2_1, int clNo_2, int index, int limit, TitaVo... titaVo);

  /**
   * ClCode1 &gt;= ,AND ClCode1 &lt;= ,AND ClCode2 &gt;= ,AND ClCode2 &lt;= ,AND ClNo &gt;= ,AND ClNo &lt;=
   *
   * @param clCode1_0 clCode1_0
   * @param clCode1_1 clCode1_1
   * @param clCode2_2 clCode2_2
   * @param clCode2_3 clCode2_3
   * @param clNo_4 clNo_4
   * @param clNo_5 clNo_5
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice ClOtherRightsFac ClOtherRightsFac of List
   */
  public Slice<ClOtherRightsFac> findClCodeRange(int clCode1_0, int clCode1_1, int clCode2_2, int clCode2_3, int clNo_4, int clNo_5, int index, int limit, TitaVo... titaVo);

  /**
   * hold By ClOtherRightsFac
   * 
   * @param clOtherRightsFacId key
   * @param titaVo Variable-Length Argument
   * @return ClOtherRightsFac ClOtherRightsFac
   */
  public ClOtherRightsFac holdById(ClOtherRightsFacId clOtherRightsFacId, TitaVo... titaVo);

  /**
   * hold By ClOtherRightsFac
   * 
   * @param clOtherRightsFac key
   * @param titaVo Variable-Length Argument
   * @return ClOtherRightsFac ClOtherRightsFac
   */
  public ClOtherRightsFac holdById(ClOtherRightsFac clOtherRightsFac, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param clOtherRightsFac Entity
   * @param titaVo Variable-Length Argument
   * @return ClOtherRightsFac Entity
   * @throws DBException exception
   */
  public ClOtherRightsFac insert(ClOtherRightsFac clOtherRightsFac, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param clOtherRightsFac Entity
   * @param titaVo Variable-Length Argument
   * @return ClOtherRightsFac Entity
   * @throws DBException exception
   */
  public ClOtherRightsFac update(ClOtherRightsFac clOtherRightsFac, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param clOtherRightsFac Entity
   * @param titaVo Variable-Length Argument
   * @return ClOtherRightsFac Entity
   * @throws DBException exception
   */
  public ClOtherRightsFac update2(ClOtherRightsFac clOtherRightsFac, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param clOtherRightsFac Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(ClOtherRightsFac clOtherRightsFac, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param clOtherRightsFac Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<ClOtherRightsFac> clOtherRightsFac, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param clOtherRightsFac Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<ClOtherRightsFac> clOtherRightsFac, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param clOtherRightsFac Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<ClOtherRightsFac> clOtherRightsFac, TitaVo... titaVo) throws DBException;

}
