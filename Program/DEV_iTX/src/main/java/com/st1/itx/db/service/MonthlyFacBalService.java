package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.MonthlyFacBal;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.MonthlyFacBalId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface MonthlyFacBalService {

  /**
   * findByPrimaryKey
   *
   * @param monthlyFacBalId PK
   * @param titaVo Variable-Length Argument
   * @return MonthlyFacBal MonthlyFacBal
   */
  public MonthlyFacBal findById(MonthlyFacBalId monthlyFacBalId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice MonthlyFacBal MonthlyFacBal of List
   */
  public Slice<MonthlyFacBal> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * ClCustNo=, AND ClFacmNo=
   *
   * @param clCustNo_0 clCustNo_0
   * @param clFacmNo_1 clFacmNo_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice MonthlyFacBal MonthlyFacBal of List
   */
  public Slice<MonthlyFacBal> findCl(int clCustNo_0, int clFacmNo_1, int index, int limit, TitaVo... titaVo);

  /**
   * hold By MonthlyFacBal
   * 
   * @param monthlyFacBalId key
   * @param titaVo Variable-Length Argument
   * @return MonthlyFacBal MonthlyFacBal
   */
  public MonthlyFacBal holdById(MonthlyFacBalId monthlyFacBalId, TitaVo... titaVo);

  /**
   * hold By MonthlyFacBal
   * 
   * @param monthlyFacBal key
   * @param titaVo Variable-Length Argument
   * @return MonthlyFacBal MonthlyFacBal
   */
  public MonthlyFacBal holdById(MonthlyFacBal monthlyFacBal, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param monthlyFacBal Entity
   * @param titaVo Variable-Length Argument
   * @return MonthlyFacBal Entity
   * @throws DBException exception
   */
  public MonthlyFacBal insert(MonthlyFacBal monthlyFacBal, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param monthlyFacBal Entity
   * @param titaVo Variable-Length Argument
   * @return MonthlyFacBal Entity
   * @throws DBException exception
   */
  public MonthlyFacBal update(MonthlyFacBal monthlyFacBal, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param monthlyFacBal Entity
   * @param titaVo Variable-Length Argument
   * @return MonthlyFacBal Entity
   * @throws DBException exception
   */
  public MonthlyFacBal update2(MonthlyFacBal monthlyFacBal, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param monthlyFacBal Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(MonthlyFacBal monthlyFacBal, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param monthlyFacBal Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<MonthlyFacBal> monthlyFacBal, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param monthlyFacBal Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<MonthlyFacBal> monthlyFacBal, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param monthlyFacBal Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<MonthlyFacBal> monthlyFacBal, TitaVo... titaVo) throws DBException;

  /**
   * Stored Procedure<br>
   * (月底日日終批次)維護 MonthlyFacBal 額度月報工作檔
   * @param  YYYYMM int
   * @param  empNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_L9_MonthlyFacBal_Upd(int YYYYMM,  String empNo, TitaVo... titaVo);

}
