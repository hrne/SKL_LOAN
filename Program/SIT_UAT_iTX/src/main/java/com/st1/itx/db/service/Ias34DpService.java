package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.Ias34Dp;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.Ias34DpId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface Ias34DpService {

  /**
   * findByPrimaryKey
   *
   * @param ias34DpId PK
   * @param titaVo Variable-Length Argument
   * @return Ias34Dp Ias34Dp
   */
  public Ias34Dp findById(Ias34DpId ias34DpId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice Ias34Dp Ias34Dp of List
   */
  public Slice<Ias34Dp> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * hold By Ias34Dp
   * 
   * @param ias34DpId key
   * @param titaVo Variable-Length Argument
   * @return Ias34Dp Ias34Dp
   */
  public Ias34Dp holdById(Ias34DpId ias34DpId, TitaVo... titaVo);

  /**
   * hold By Ias34Dp
   * 
   * @param ias34Dp key
   * @param titaVo Variable-Length Argument
   * @return Ias34Dp Ias34Dp
   */
  public Ias34Dp holdById(Ias34Dp ias34Dp, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param ias34Dp Entity
   * @param titaVo Variable-Length Argument
   * @return Ias34Dp Entity
   * @throws DBException exception
   */
  public Ias34Dp insert(Ias34Dp ias34Dp, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param ias34Dp Entity
   * @param titaVo Variable-Length Argument
   * @return Ias34Dp Entity
   * @throws DBException exception
   */
  public Ias34Dp update(Ias34Dp ias34Dp, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param ias34Dp Entity
   * @param titaVo Variable-Length Argument
   * @return Ias34Dp Entity
   * @throws DBException exception
   */
  public Ias34Dp update2(Ias34Dp ias34Dp, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param ias34Dp Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(Ias34Dp ias34Dp, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param ias34Dp Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<Ias34Dp> ias34Dp, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param ias34Dp Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<Ias34Dp> ias34Dp, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param ias34Dp Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<Ias34Dp> ias34Dp, TitaVo... titaVo) throws DBException;

  /**
   * Stored Procedure<br>
   * (月底日日終批次)維護 IAS34 欄位清單D檔
   * @param  TBSDYF int
   * @param  EmpNo String
   * @param  NewAcFg int
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_L7_Ias34Dp_Upd(int TBSDYF, String EmpNo, int NewAcFg, TitaVo... titaVo);

}
