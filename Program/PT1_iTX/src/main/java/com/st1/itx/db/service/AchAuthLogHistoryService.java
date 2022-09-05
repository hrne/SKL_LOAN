package com.st1.itx.db.service;

import java.util.List;

import java.math.BigDecimal;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.AchAuthLogHistory;
import org.springframework.data.domain.Slice;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface AchAuthLogHistoryService {

  /**
   * findByPrimaryKey
   *
   * @param logNo PK
   * @param titaVo Variable-Length Argument
   * @return AchAuthLogHistory AchAuthLogHistory
   */
  public AchAuthLogHistory findById(Long logNo, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice AchAuthLogHistory AchAuthLogHistory of List
   */
  public Slice<AchAuthLogHistory> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * CustNo = ,AND FacmNo = 
   *
   * @param custNo_0 custNo_0
   * @param facmNo_1 facmNo_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice AchAuthLogHistory AchAuthLogHistory of List
   */
  public Slice<AchAuthLogHistory> facmNoEq(int custNo_0, int facmNo_1, int index, int limit, TitaVo... titaVo);

  /**
   * hold By AchAuthLogHistory
   * 
   * @param logNo key
   * @param titaVo Variable-Length Argument
   * @return AchAuthLogHistory AchAuthLogHistory
   */
  public AchAuthLogHistory holdById(Long logNo, TitaVo... titaVo);

  /**
   * hold By AchAuthLogHistory
   * 
   * @param achAuthLogHistory key
   * @param titaVo Variable-Length Argument
   * @return AchAuthLogHistory AchAuthLogHistory
   */
  public AchAuthLogHistory holdById(AchAuthLogHistory achAuthLogHistory, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param achAuthLogHistory Entity
   * @param titaVo Variable-Length Argument
   * @return AchAuthLogHistory Entity
   * @throws DBException exception
   */
  public AchAuthLogHistory insert(AchAuthLogHistory achAuthLogHistory, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param achAuthLogHistory Entity
   * @param titaVo Variable-Length Argument
   * @return AchAuthLogHistory Entity
   * @throws DBException exception
   */
  public AchAuthLogHistory update(AchAuthLogHistory achAuthLogHistory, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param achAuthLogHistory Entity
   * @param titaVo Variable-Length Argument
   * @return AchAuthLogHistory Entity
   * @throws DBException exception
   */
  public AchAuthLogHistory update2(AchAuthLogHistory achAuthLogHistory, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param achAuthLogHistory Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(AchAuthLogHistory achAuthLogHistory, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param achAuthLogHistory Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<AchAuthLogHistory> achAuthLogHistory, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param achAuthLogHistory Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<AchAuthLogHistory> achAuthLogHistory, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param achAuthLogHistory Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<AchAuthLogHistory> achAuthLogHistory, TitaVo... titaVo) throws DBException;

}
