package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CdAcCode;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.CdAcCodeId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CdAcCodeService {

  /**
   * findByPrimaryKey
   *
   * @param cdAcCodeId PK
   * @param titaVo Variable-Length Argument
   * @return CdAcCode CdAcCode
   */
  public CdAcCode findById(CdAcCodeId cdAcCodeId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdAcCode CdAcCode of List
   */
  public Slice<CdAcCode> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * AcctCode = 
   *
   * @param acctCode_0 acctCode_0
   * @param titaVo Variable-Length Argument
   * @return Slice CdAcCode CdAcCode of List
   */
  public CdAcCode acCodeAcctFirst(String acctCode_0, TitaVo... titaVo);

  /**
   * AcNoCode &gt;= ,AND AcNoCode &lt;= ,AND AcSubCode &gt;= ,AND AcSubCode &lt;= ,AND AcDtlCode &gt;= ,AND AcDtlCode &lt;= 
   *
   * @param acNoCode_0 acNoCode_0
   * @param acNoCode_1 acNoCode_1
   * @param acSubCode_2 acSubCode_2
   * @param acSubCode_3 acSubCode_3
   * @param acDtlCode_4 acDtlCode_4
   * @param acDtlCode_5 acDtlCode_5
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdAcCode CdAcCode of List
   */
  public Slice<CdAcCode> findAcCode(String acNoCode_0, String acNoCode_1, String acSubCode_2, String acSubCode_3, String acDtlCode_4, String acDtlCode_5, int index, int limit, TitaVo... titaVo);

  /**
   * hold By CdAcCode
   * 
   * @param cdAcCodeId key
   * @param titaVo Variable-Length Argument
   * @return CdAcCode CdAcCode
   */
  public CdAcCode holdById(CdAcCodeId cdAcCodeId, TitaVo... titaVo);

  /**
   * hold By CdAcCode
   * 
   * @param cdAcCode key
   * @param titaVo Variable-Length Argument
   * @return CdAcCode CdAcCode
   */
  public CdAcCode holdById(CdAcCode cdAcCode, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param cdAcCode Entity
   * @param titaVo Variable-Length Argument
   * @return CdAcCode Entity
   * @throws DBException exception
   */
  public CdAcCode insert(CdAcCode cdAcCode, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param cdAcCode Entity
   * @param titaVo Variable-Length Argument
   * @return CdAcCode Entity
   * @throws DBException exception
   */
  public CdAcCode update(CdAcCode cdAcCode, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param cdAcCode Entity
   * @param titaVo Variable-Length Argument
   * @return CdAcCode Entity
   * @throws DBException exception
   */
  public CdAcCode update2(CdAcCode cdAcCode, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param cdAcCode Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(CdAcCode cdAcCode, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param cdAcCode Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<CdAcCode> cdAcCode, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param cdAcCode Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<CdAcCode> cdAcCode, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param cdAcCode Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<CdAcCode> cdAcCode, TitaVo... titaVo) throws DBException;

}
