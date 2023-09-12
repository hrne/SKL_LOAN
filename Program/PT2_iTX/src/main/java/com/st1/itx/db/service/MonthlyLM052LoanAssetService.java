package com.st1.itx.db.service;

import java.util.List;

import java.math.BigDecimal;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.MonthlyLM052LoanAsset;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.MonthlyLM052LoanAssetId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface MonthlyLM052LoanAssetService {

  /**
   * findByPrimaryKey
   *
   * @param monthlyLM052LoanAssetId PK
   * @param titaVo Variable-Length Argument
   * @return MonthlyLM052LoanAsset MonthlyLM052LoanAsset
   */
  public MonthlyLM052LoanAsset findById(MonthlyLM052LoanAssetId monthlyLM052LoanAssetId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice MonthlyLM052LoanAsset MonthlyLM052LoanAsset of List
   */
  public Slice<MonthlyLM052LoanAsset> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * hold By MonthlyLM052LoanAsset
   * 
   * @param monthlyLM052LoanAssetId key
   * @param titaVo Variable-Length Argument
   * @return MonthlyLM052LoanAsset MonthlyLM052LoanAsset
   */
  public MonthlyLM052LoanAsset holdById(MonthlyLM052LoanAssetId monthlyLM052LoanAssetId, TitaVo... titaVo);

  /**
   * hold By MonthlyLM052LoanAsset
   * 
   * @param monthlyLM052LoanAsset key
   * @param titaVo Variable-Length Argument
   * @return MonthlyLM052LoanAsset MonthlyLM052LoanAsset
   */
  public MonthlyLM052LoanAsset holdById(MonthlyLM052LoanAsset monthlyLM052LoanAsset, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param monthlyLM052LoanAsset Entity
   * @param titaVo Variable-Length Argument
   * @return MonthlyLM052LoanAsset Entity
   * @throws DBException exception
   */
  public MonthlyLM052LoanAsset insert(MonthlyLM052LoanAsset monthlyLM052LoanAsset, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param monthlyLM052LoanAsset Entity
   * @param titaVo Variable-Length Argument
   * @return MonthlyLM052LoanAsset Entity
   * @throws DBException exception
   */
  public MonthlyLM052LoanAsset update(MonthlyLM052LoanAsset monthlyLM052LoanAsset, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param monthlyLM052LoanAsset Entity
   * @param titaVo Variable-Length Argument
   * @return MonthlyLM052LoanAsset Entity
   * @throws DBException exception
   */
  public MonthlyLM052LoanAsset update2(MonthlyLM052LoanAsset monthlyLM052LoanAsset, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param monthlyLM052LoanAsset Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(MonthlyLM052LoanAsset monthlyLM052LoanAsset, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param monthlyLM052LoanAsset Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<MonthlyLM052LoanAsset> monthlyLM052LoanAsset, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param monthlyLM052LoanAsset Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<MonthlyLM052LoanAsset> monthlyLM052LoanAsset, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param monthlyLM052LoanAsset Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<MonthlyLM052LoanAsset> monthlyLM052LoanAsset, TitaVo... titaVo) throws DBException;

  /**
   * Stored Procedure<br>
   * 
   * @param  tbsdyf int
   * @param  empNo String
   * @param  jobTxSeq String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_L9_MonthlyLM052LoanAsset_Ins(int tbsdyf,  String empNo,  String jobTxSeq, TitaVo... titaVo);

}
