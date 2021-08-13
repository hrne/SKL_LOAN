package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.Ias34Ep;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.Ias34EpId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface Ias34EpService {

  /**
   * findByPrimaryKey
   *
   * @param ias34EpId PK
   * @param titaVo Variable-Length Argument
   * @return Ias34Ep Ias34Ep
   */
  public Ias34Ep findById(Ias34EpId ias34EpId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice Ias34Ep Ias34Ep of List
   */
  public Slice<Ias34Ep> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * hold By Ias34Ep
   * 
   * @param ias34EpId key
   * @param titaVo Variable-Length Argument
   * @return Ias34Ep Ias34Ep
   */
  public Ias34Ep holdById(Ias34EpId ias34EpId, TitaVo... titaVo);

  /**
   * hold By Ias34Ep
   * 
   * @param ias34Ep key
   * @param titaVo Variable-Length Argument
   * @return Ias34Ep Ias34Ep
   */
  public Ias34Ep holdById(Ias34Ep ias34Ep, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param ias34Ep Entity
   * @param titaVo Variable-Length Argument
   * @return Ias34Ep Entity
   * @throws DBException exception
   */
  public Ias34Ep insert(Ias34Ep ias34Ep, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param ias34Ep Entity
   * @param titaVo Variable-Length Argument
   * @return Ias34Ep Entity
   * @throws DBException exception
   */
  public Ias34Ep update(Ias34Ep ias34Ep, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param ias34Ep Entity
   * @param titaVo Variable-Length Argument
   * @return Ias34Ep Entity
   * @throws DBException exception
   */
  public Ias34Ep update2(Ias34Ep ias34Ep, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param ias34Ep Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(Ias34Ep ias34Ep, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param ias34Ep Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<Ias34Ep> ias34Ep, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param ias34Ep Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<Ias34Ep> ias34Ep, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param ias34Ep Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<Ias34Ep> ias34Ep, TitaVo... titaVo) throws DBException;

  /**
   * Stored Procedure<br>
   * (月底日日終批次)維護 IAS34 欄位清單E檔
   * @param  TBSDYF int
   * @param  EmpNo String
   * @param  NewAcFg int
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_L7_Ias34Ep_Upd(int TBSDYF, String EmpNo, int NewAcFg, TitaVo... titaVo);

}
