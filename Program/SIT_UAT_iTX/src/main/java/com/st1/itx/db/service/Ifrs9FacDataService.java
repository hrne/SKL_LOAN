package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.Ifrs9FacData;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.Ifrs9FacDataId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface Ifrs9FacDataService {

  /**
   * findByPrimaryKey
   *
   * @param ifrs9FacDataId PK
   * @param titaVo Variable-Length Argument
   * @return Ifrs9FacData Ifrs9FacData
   */
  public Ifrs9FacData findById(Ifrs9FacDataId ifrs9FacDataId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice Ifrs9FacData Ifrs9FacData of List
   */
  public Slice<Ifrs9FacData> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * hold By Ifrs9FacData
   * 
   * @param ifrs9FacDataId key
   * @param titaVo Variable-Length Argument
   * @return Ifrs9FacData Ifrs9FacData
   */
  public Ifrs9FacData holdById(Ifrs9FacDataId ifrs9FacDataId, TitaVo... titaVo);

  /**
   * hold By Ifrs9FacData
   * 
   * @param ifrs9FacData key
   * @param titaVo Variable-Length Argument
   * @return Ifrs9FacData Ifrs9FacData
   */
  public Ifrs9FacData holdById(Ifrs9FacData ifrs9FacData, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param ifrs9FacData Entity
   * @param titaVo Variable-Length Argument
   * @return Ifrs9FacData Entity
   * @throws DBException exception
   */
  public Ifrs9FacData insert(Ifrs9FacData ifrs9FacData, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param ifrs9FacData Entity
   * @param titaVo Variable-Length Argument
   * @return Ifrs9FacData Entity
   * @throws DBException exception
   */
  public Ifrs9FacData update(Ifrs9FacData ifrs9FacData, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param ifrs9FacData Entity
   * @param titaVo Variable-Length Argument
   * @return Ifrs9FacData Entity
   * @throws DBException exception
   */
  public Ifrs9FacData update2(Ifrs9FacData ifrs9FacData, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param ifrs9FacData Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(Ifrs9FacData ifrs9FacData, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param ifrs9FacData Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<Ifrs9FacData> ifrs9FacData, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param ifrs9FacData Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<Ifrs9FacData> ifrs9FacData, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param ifrs9FacData Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<Ifrs9FacData> ifrs9FacData, TitaVo... titaVo) throws DBException;

  /**
   * Stored Procedure<br>
   * (月底日日終批次)維護IFRS9額度資料檔
   * @param  TBSDYF int
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_L7_Ifrs9FacData_Upd(int TBSDYF, String EmpNo, TitaVo... titaVo);

}
