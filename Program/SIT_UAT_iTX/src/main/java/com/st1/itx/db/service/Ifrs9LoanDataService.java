package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.Ifrs9LoanData;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.Ifrs9LoanDataId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface Ifrs9LoanDataService {

  /**
   * findByPrimaryKey
   *
   * @param ifrs9LoanDataId PK
   * @param titaVo Variable-Length Argument
   * @return Ifrs9LoanData Ifrs9LoanData
   */
  public Ifrs9LoanData findById(Ifrs9LoanDataId ifrs9LoanDataId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice Ifrs9LoanData Ifrs9LoanData of List
   */
  public Slice<Ifrs9LoanData> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * hold By Ifrs9LoanData
   * 
   * @param ifrs9LoanDataId key
   * @param titaVo Variable-Length Argument
   * @return Ifrs9LoanData Ifrs9LoanData
   */
  public Ifrs9LoanData holdById(Ifrs9LoanDataId ifrs9LoanDataId, TitaVo... titaVo);

  /**
   * hold By Ifrs9LoanData
   * 
   * @param ifrs9LoanData key
   * @param titaVo Variable-Length Argument
   * @return Ifrs9LoanData Ifrs9LoanData
   */
  public Ifrs9LoanData holdById(Ifrs9LoanData ifrs9LoanData, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param ifrs9LoanData Entity
   * @param titaVo Variable-Length Argument
   * @return Ifrs9LoanData Entity
   * @throws DBException exception
   */
  public Ifrs9LoanData insert(Ifrs9LoanData ifrs9LoanData, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param ifrs9LoanData Entity
   * @param titaVo Variable-Length Argument
   * @return Ifrs9LoanData Entity
   * @throws DBException exception
   */
  public Ifrs9LoanData update(Ifrs9LoanData ifrs9LoanData, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param ifrs9LoanData Entity
   * @param titaVo Variable-Length Argument
   * @return Ifrs9LoanData Entity
   * @throws DBException exception
   */
  public Ifrs9LoanData update2(Ifrs9LoanData ifrs9LoanData, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param ifrs9LoanData Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(Ifrs9LoanData ifrs9LoanData, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param ifrs9LoanData Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<Ifrs9LoanData> ifrs9LoanData, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param ifrs9LoanData Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<Ifrs9LoanData> ifrs9LoanData, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param ifrs9LoanData Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<Ifrs9LoanData> ifrs9LoanData, TitaVo... titaVo) throws DBException;

  /**
   * Stored Procedure<br>
   * (月底日日終批次)維護IFRS9撥款資料檔
   * @param  TBSDYF int
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_L7_Ifrs9LoanData_Upd(int TBSDYF, String EmpNo, TitaVo... titaVo);

}
