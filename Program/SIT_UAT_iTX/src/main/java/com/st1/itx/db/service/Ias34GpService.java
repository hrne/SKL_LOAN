package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.Ias34Gp;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.Ias34GpId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface Ias34GpService {

  /**
   * findByPrimaryKey
   *
   * @param ias34GpId PK
   * @param titaVo Variable-Length Argument
   * @return Ias34Gp Ias34Gp
   */
  public Ias34Gp findById(Ias34GpId ias34GpId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice Ias34Gp Ias34Gp of List
   */
  public Slice<Ias34Gp> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * hold By Ias34Gp
   * 
   * @param ias34GpId key
   * @param titaVo Variable-Length Argument
   * @return Ias34Gp Ias34Gp
   */
  public Ias34Gp holdById(Ias34GpId ias34GpId, TitaVo... titaVo);

  /**
   * hold By Ias34Gp
   * 
   * @param ias34Gp key
   * @param titaVo Variable-Length Argument
   * @return Ias34Gp Ias34Gp
   */
  public Ias34Gp holdById(Ias34Gp ias34Gp, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param ias34Gp Entity
   * @param titaVo Variable-Length Argument
   * @return Ias34Gp Entity
   * @throws DBException exception
   */
  public Ias34Gp insert(Ias34Gp ias34Gp, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param ias34Gp Entity
   * @param titaVo Variable-Length Argument
   * @return Ias34Gp Entity
   * @throws DBException exception
   */
  public Ias34Gp update(Ias34Gp ias34Gp, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param ias34Gp Entity
   * @param titaVo Variable-Length Argument
   * @return Ias34Gp Entity
   * @throws DBException exception
   */
  public Ias34Gp update2(Ias34Gp ias34Gp, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param ias34Gp Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(Ias34Gp ias34Gp, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param ias34Gp Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<Ias34Gp> ias34Gp, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param ias34Gp Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<Ias34Gp> ias34Gp, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param ias34Gp Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<Ias34Gp> ias34Gp, TitaVo... titaVo) throws DBException;

  /**
   * Stored Procedure<br>
   * (月底日日終批次)維護 IAS34 欄位清單G檔
   * @param  TBSDYF int
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_L7_Ias34Gp_Upd(int TBSDYF, String EmpNo, TitaVo... titaVo);

}
