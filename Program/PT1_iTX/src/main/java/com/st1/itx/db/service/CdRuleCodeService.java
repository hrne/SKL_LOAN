package com.st1.itx.db.service;

import java.util.List;

import java.math.BigDecimal;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CdRuleCode;
import org.springframework.data.domain.Slice;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CdRuleCodeService {

  /**
   * findByPrimaryKey
   *
   * @param ruleCode PK
   * @param titaVo Variable-Length Argument
   * @return CdRuleCode CdRuleCode
   */
  public CdRuleCode findById(String ruleCode, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdRuleCode CdRuleCode of List
   */
  public Slice<CdRuleCode> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * RuleStDate &gt;=
   *
   * @param ruleStDate_0 ruleStDate_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdRuleCode CdRuleCode of List
   */
  public Slice<CdRuleCode> findCodeDate(int ruleStDate_0, int index, int limit, TitaVo... titaVo);

  /**
   * EnableMark = 
   *
   * @param enableMark_0 enableMark_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdRuleCode CdRuleCode of List
   */
  public Slice<CdRuleCode> findEnableMark(String enableMark_0, int index, int limit, TitaVo... titaVo);

  /**
   * hold By CdRuleCode
   * 
   * @param ruleCode key
   * @param titaVo Variable-Length Argument
   * @return CdRuleCode CdRuleCode
   */
  public CdRuleCode holdById(String ruleCode, TitaVo... titaVo);

  /**
   * hold By CdRuleCode
   * 
   * @param cdRuleCode key
   * @param titaVo Variable-Length Argument
   * @return CdRuleCode CdRuleCode
   */
  public CdRuleCode holdById(CdRuleCode cdRuleCode, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param cdRuleCode Entity
   * @param titaVo Variable-Length Argument
   * @return CdRuleCode Entity
   * @throws DBException exception
   */
  public CdRuleCode insert(CdRuleCode cdRuleCode, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param cdRuleCode Entity
   * @param titaVo Variable-Length Argument
   * @return CdRuleCode Entity
   * @throws DBException exception
   */
  public CdRuleCode update(CdRuleCode cdRuleCode, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param cdRuleCode Entity
   * @param titaVo Variable-Length Argument
   * @return CdRuleCode Entity
   * @throws DBException exception
   */
  public CdRuleCode update2(CdRuleCode cdRuleCode, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param cdRuleCode Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(CdRuleCode cdRuleCode, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param cdRuleCode Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<CdRuleCode> cdRuleCode, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param cdRuleCode Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<CdRuleCode> cdRuleCode, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param cdRuleCode Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<CdRuleCode> cdRuleCode, TitaVo... titaVo) throws DBException;

}
