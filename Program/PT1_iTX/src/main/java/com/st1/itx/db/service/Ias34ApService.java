package com.st1.itx.db.service;

import java.util.List;

import java.math.BigDecimal;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.Ias34Ap;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.Ias34ApId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface Ias34ApService {

  /**
   * findByPrimaryKey
   *
   * @param ias34ApId PK
   * @param titaVo Variable-Length Argument
   * @return Ias34Ap Ias34Ap
   */
  public Ias34Ap findById(Ias34ApId ias34ApId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice Ias34Ap Ias34Ap of List
   */
  public Slice<Ias34Ap> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * CustNo = ,AND FacmNo = ,AND DataYM =
   *
   * @param custNo_0 custNo_0
   * @param facmNo_1 facmNo_1
   * @param dataYM_2 dataYM_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice Ias34Ap Ias34Ap of List
   */
  public Slice<Ias34Ap> dataEq(int custNo_0, int facmNo_1, int dataYM_2, int index, int limit, TitaVo... titaVo);

  /**
   * hold By Ias34Ap
   * 
   * @param ias34ApId key
   * @param titaVo Variable-Length Argument
   * @return Ias34Ap Ias34Ap
   */
  public Ias34Ap holdById(Ias34ApId ias34ApId, TitaVo... titaVo);

  /**
   * hold By Ias34Ap
   * 
   * @param ias34Ap key
   * @param titaVo Variable-Length Argument
   * @return Ias34Ap Ias34Ap
   */
  public Ias34Ap holdById(Ias34Ap ias34Ap, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param ias34Ap Entity
   * @param titaVo Variable-Length Argument
   * @return Ias34Ap Entity
   * @throws DBException exception
   */
  public Ias34Ap insert(Ias34Ap ias34Ap, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param ias34Ap Entity
   * @param titaVo Variable-Length Argument
   * @return Ias34Ap Entity
   * @throws DBException exception
   */
  public Ias34Ap update(Ias34Ap ias34Ap, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param ias34Ap Entity
   * @param titaVo Variable-Length Argument
   * @return Ias34Ap Entity
   * @throws DBException exception
   */
  public Ias34Ap update2(Ias34Ap ias34Ap, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param ias34Ap Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(Ias34Ap ias34Ap, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param ias34Ap Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<Ias34Ap> ias34Ap, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param ias34Ap Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<Ias34Ap> ias34Ap, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param ias34Ap Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<Ias34Ap> ias34Ap, TitaVo... titaVo) throws DBException;

  /**
   * Stored Procedure<br>
   * (月底日日終批次)維護 IAS34 欄位清單A檔
   * @param  TBSDYF int
   * @param  EmpNo String
   * @param  NewAcFg int
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_L7_Ias34Ap_Upd(int TBSDYF, String EmpNo, int NewAcFg, TitaVo... titaVo);

}
