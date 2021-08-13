package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CdBonusCo;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.CdBonusCoId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CdBonusCoService {

  /**
   * findByPrimaryKey
   *
   * @param cdBonusCoId PK
   * @param titaVo Variable-Length Argument
   * @return CdBonusCo CdBonusCo
   */
  public CdBonusCo findById(CdBonusCoId cdBonusCoId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdBonusCo CdBonusCo of List
   */
  public Slice<CdBonusCo> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * WorkMonth= ,AND ConditionCode = ,AND Condition &gt;= ,AND Condition &lt;= 
   *
   * @param workMonth_0 workMonth_0
   * @param conditionCode_1 conditionCode_1
   * @param condition_2 condition_2
   * @param condition_3 condition_3
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdBonusCo CdBonusCo of List
   */
  public Slice<CdBonusCo> findCondition(int workMonth_0, int conditionCode_1, String condition_2, String condition_3, int index, int limit, TitaVo... titaVo);

  /**
   * WorkMonth&gt;= ,AND WorkMonth&lt;= 
   *
   * @param workMonth_0 workMonth_0
   * @param workMonth_1 workMonth_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdBonusCo CdBonusCo of List
   */
  public Slice<CdBonusCo> findYearMonth(int workMonth_0, int workMonth_1, int index, int limit, TitaVo... titaVo);

  /**
   * WorkMonth&lt;=
   *
   * @param workMonth_0 workMonth_0
   * @param titaVo Variable-Length Argument
   * @return Slice CdBonusCo CdBonusCo of List
   */
  public CdBonusCo findWorkMonthFirst(int workMonth_0, TitaVo... titaVo);

  /**
   * hold By CdBonusCo
   * 
   * @param cdBonusCoId key
   * @param titaVo Variable-Length Argument
   * @return CdBonusCo CdBonusCo
   */
  public CdBonusCo holdById(CdBonusCoId cdBonusCoId, TitaVo... titaVo);

  /**
   * hold By CdBonusCo
   * 
   * @param cdBonusCo key
   * @param titaVo Variable-Length Argument
   * @return CdBonusCo CdBonusCo
   */
  public CdBonusCo holdById(CdBonusCo cdBonusCo, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param cdBonusCo Entity
   * @param titaVo Variable-Length Argument
   * @return CdBonusCo Entity
   * @throws DBException exception
   */
  public CdBonusCo insert(CdBonusCo cdBonusCo, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param cdBonusCo Entity
   * @param titaVo Variable-Length Argument
   * @return CdBonusCo Entity
   * @throws DBException exception
   */
  public CdBonusCo update(CdBonusCo cdBonusCo, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param cdBonusCo Entity
   * @param titaVo Variable-Length Argument
   * @return CdBonusCo Entity
   * @throws DBException exception
   */
  public CdBonusCo update2(CdBonusCo cdBonusCo, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param cdBonusCo Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(CdBonusCo cdBonusCo, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param cdBonusCo Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<CdBonusCo> cdBonusCo, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param cdBonusCo Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<CdBonusCo> cdBonusCo, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param cdBonusCo Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<CdBonusCo> cdBonusCo, TitaVo... titaVo) throws DBException;

}
