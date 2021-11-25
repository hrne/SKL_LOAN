package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.Ias34Bp;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.Ias34BpId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface Ias34BpService {

  /**
   * findByPrimaryKey
   *
   * @param ias34BpId PK
   * @param titaVo Variable-Length Argument
   * @return Ias34Bp Ias34Bp
   */
  public Ias34Bp findById(Ias34BpId ias34BpId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice Ias34Bp Ias34Bp of List
   */
  public Slice<Ias34Bp> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * hold By Ias34Bp
   * 
   * @param ias34BpId key
   * @param titaVo Variable-Length Argument
   * @return Ias34Bp Ias34Bp
   */
  public Ias34Bp holdById(Ias34BpId ias34BpId, TitaVo... titaVo);

  /**
   * hold By Ias34Bp
   * 
   * @param ias34Bp key
   * @param titaVo Variable-Length Argument
   * @return Ias34Bp Ias34Bp
   */
  public Ias34Bp holdById(Ias34Bp ias34Bp, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param ias34Bp Entity
   * @param titaVo Variable-Length Argument
   * @return Ias34Bp Entity
   * @throws DBException exception
   */
  public Ias34Bp insert(Ias34Bp ias34Bp, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param ias34Bp Entity
   * @param titaVo Variable-Length Argument
   * @return Ias34Bp Entity
   * @throws DBException exception
   */
  public Ias34Bp update(Ias34Bp ias34Bp, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param ias34Bp Entity
   * @param titaVo Variable-Length Argument
   * @return Ias34Bp Entity
   * @throws DBException exception
   */
  public Ias34Bp update2(Ias34Bp ias34Bp, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param ias34Bp Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(Ias34Bp ias34Bp, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param ias34Bp Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<Ias34Bp> ias34Bp, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param ias34Bp Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<Ias34Bp> ias34Bp, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param ias34Bp Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<Ias34Bp> ias34Bp, TitaVo... titaVo) throws DBException;

  /**
   * Stored Procedure<br>
   * (月底日日終批次)維護 IAS34 欄位清單B檔
   * @param  TBSDYF int
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_L7_Ias34Bp_Upd(int TBSDYF, String EmpNo, TitaVo... titaVo);

}
