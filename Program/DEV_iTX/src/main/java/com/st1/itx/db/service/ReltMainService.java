package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.ReltMain;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.ReltMainId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface ReltMainService {

  /**
   * findByPrimaryKey
   *
   * @param reltMainId PK
   * @param titaVo Variable-Length Argument
   * @return ReltMain ReltMain
   */
  public ReltMain findById(ReltMainId reltMainId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice ReltMain ReltMain of List
   */
  public Slice<ReltMain> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * ReltId =
   *
   * @param reltId_0 reltId_0
   * @param titaVo Variable-Length Argument
   * @return Slice ReltMain ReltMain of List
   */
  public ReltMain reltIdFirst(String reltId_0, TitaVo... titaVo);

  /**
   * ReltId =
   *
   * @param reltId_0 reltId_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice ReltMain ReltMain of List
   */
  public Slice<ReltMain> reltIdEq(String reltId_0, int index, int limit, TitaVo... titaVo);

  /**
   * CaseNo =
   *
   * @param caseNo_0 caseNo_0
   * @param titaVo Variable-Length Argument
   * @return Slice ReltMain ReltMain of List
   */
  public ReltMain caseNoFirst(int caseNo_0, TitaVo... titaVo);

  /**
   * CustNo =
   *
   * @param custNo_0 custNo_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice ReltMain ReltMain of List
   */
  public Slice<ReltMain> custNoEq(int custNo_0, int index, int limit, TitaVo... titaVo);

  /**
   * CaseNo = ,AND CustNo = ,AND ReltId = 
   *
   * @param caseNo_0 caseNo_0
   * @param custNo_1 custNo_1
   * @param reltId_2 reltId_2
   * @param titaVo Variable-Length Argument
   * @return Slice ReltMain ReltMain of List
   */
  public ReltMain caseNoCustNoReltIdFirst(int caseNo_0, int custNo_1, String reltId_2, TitaVo... titaVo);

  /**
   * CustNo =
   *
   * @param custNo_0 custNo_0
   * @param titaVo Variable-Length Argument
   * @return Slice ReltMain ReltMain of List
   */
  public ReltMain custNoFirst(int custNo_0, TitaVo... titaVo);

  /**
   * hold By ReltMain
   * 
   * @param reltMainId key
   * @param titaVo Variable-Length Argument
   * @return ReltMain ReltMain
   */
  public ReltMain holdById(ReltMainId reltMainId, TitaVo... titaVo);

  /**
   * hold By ReltMain
   * 
   * @param reltMain key
   * @param titaVo Variable-Length Argument
   * @return ReltMain ReltMain
   */
  public ReltMain holdById(ReltMain reltMain, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param reltMain Entity
   * @param titaVo Variable-Length Argument
   * @return ReltMain Entity
   * @throws DBException exception
   */
  public ReltMain insert(ReltMain reltMain, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param reltMain Entity
   * @param titaVo Variable-Length Argument
   * @return ReltMain Entity
   * @throws DBException exception
   */
  public ReltMain update(ReltMain reltMain, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param reltMain Entity
   * @param titaVo Variable-Length Argument
   * @return ReltMain Entity
   * @throws DBException exception
   */
  public ReltMain update2(ReltMain reltMain, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param reltMain Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(ReltMain reltMain, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param reltMain Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<ReltMain> reltMain, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param reltMain Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<ReltMain> reltMain, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param reltMain Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<ReltMain> reltMain, TitaVo... titaVo) throws DBException;

}
