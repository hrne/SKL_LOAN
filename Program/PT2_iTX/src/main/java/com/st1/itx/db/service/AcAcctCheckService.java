package com.st1.itx.db.service;

import java.util.List;

import java.math.BigDecimal;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.AcAcctCheck;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.AcAcctCheckId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface AcAcctCheckService {

  /**
   * findByPrimaryKey
   *
   * @param acAcctCheckId PK
   * @param titaVo Variable-Length Argument
   * @return AcAcctCheck AcAcctCheck
   */
  public AcAcctCheck findById(AcAcctCheckId acAcctCheckId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice AcAcctCheck AcAcctCheck of List
   */
  public Slice<AcAcctCheck> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * AcDate = 
   *
   * @param acDate_0 acDate_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice AcAcctCheck AcAcctCheck of List
   */
  public Slice<AcAcctCheck> findAcDate(int acDate_0, int index, int limit, TitaVo... titaVo);

  /**
   * hold By AcAcctCheck
   * 
   * @param acAcctCheckId key
   * @param titaVo Variable-Length Argument
   * @return AcAcctCheck AcAcctCheck
   */
  public AcAcctCheck holdById(AcAcctCheckId acAcctCheckId, TitaVo... titaVo);

  /**
   * hold By AcAcctCheck
   * 
   * @param acAcctCheck key
   * @param titaVo Variable-Length Argument
   * @return AcAcctCheck AcAcctCheck
   */
  public AcAcctCheck holdById(AcAcctCheck acAcctCheck, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param acAcctCheck Entity
   * @param titaVo Variable-Length Argument
   * @return AcAcctCheck Entity
   * @throws DBException exception
   */
  public AcAcctCheck insert(AcAcctCheck acAcctCheck, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param acAcctCheck Entity
   * @param titaVo Variable-Length Argument
   * @return AcAcctCheck Entity
   * @throws DBException exception
   */
  public AcAcctCheck update(AcAcctCheck acAcctCheck, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param acAcctCheck Entity
   * @param titaVo Variable-Length Argument
   * @return AcAcctCheck Entity
   * @throws DBException exception
   */
  public AcAcctCheck update2(AcAcctCheck acAcctCheck, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param acAcctCheck Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(AcAcctCheck acAcctCheck, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param acAcctCheck Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<AcAcctCheck> acAcctCheck, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param acAcctCheck Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<AcAcctCheck> acAcctCheck, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param acAcctCheck Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<AcAcctCheck> acAcctCheck, TitaVo... titaVo) throws DBException;

  /**
   * Stored Procedure<br>
   * (放款關帳 )維護 AcAcctCheck 會計業務檢核檔
   * @param  tbsdyf int
   * @param  empNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_L6_AcAcctCheck_Upd(int tbsdyf,  String empNo, TitaVo... titaVo);

}
