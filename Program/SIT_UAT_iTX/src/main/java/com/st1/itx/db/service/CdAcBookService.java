package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CdAcBook;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.CdAcBookId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CdAcBookService {

  /**
   * findByPrimaryKey
   *
   * @param cdAcBookId PK
   * @param titaVo Variable-Length Argument
   * @return CdAcBook CdAcBook
   */
  public CdAcBook findById(CdAcBookId cdAcBookId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdAcBook CdAcBook of List
   */
  public Slice<CdAcBook> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * AcBookCode = ,AND AssignSeq &gt;=
   *
   * @param acBookCode_0 acBookCode_0
   * @param assignSeq_1 assignSeq_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdAcBook CdAcBook of List
   */
  public Slice<CdAcBook> acBookAssignSeqGeq(String acBookCode_0, int assignSeq_1, int index, int limit, TitaVo... titaVo);

  /**
   * AcBookCode = ,AND AcSubBookCode =
   *
   * @param acBookCode_0 acBookCode_0
   * @param acSubBookCode_1 acSubBookCode_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdAcBook CdAcBook of List
   */
  public Slice<CdAcBook> findAcBookCode(String acBookCode_0, String acSubBookCode_1, int index, int limit, TitaVo... titaVo);

  /**
   * hold By CdAcBook
   * 
   * @param cdAcBookId key
   * @param titaVo Variable-Length Argument
   * @return CdAcBook CdAcBook
   */
  public CdAcBook holdById(CdAcBookId cdAcBookId, TitaVo... titaVo);

  /**
   * hold By CdAcBook
   * 
   * @param cdAcBook key
   * @param titaVo Variable-Length Argument
   * @return CdAcBook CdAcBook
   */
  public CdAcBook holdById(CdAcBook cdAcBook, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param cdAcBook Entity
   * @param titaVo Variable-Length Argument
   * @return CdAcBook Entity
   * @throws DBException exception
   */
  public CdAcBook insert(CdAcBook cdAcBook, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param cdAcBook Entity
   * @param titaVo Variable-Length Argument
   * @return CdAcBook Entity
   * @throws DBException exception
   */
  public CdAcBook update(CdAcBook cdAcBook, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param cdAcBook Entity
   * @param titaVo Variable-Length Argument
   * @return CdAcBook Entity
   * @throws DBException exception
   */
  public CdAcBook update2(CdAcBook cdAcBook, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param cdAcBook Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(CdAcBook cdAcBook, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param cdAcBook Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<CdAcBook> cdAcBook, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param cdAcBook Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<CdAcBook> cdAcBook, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param cdAcBook Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<CdAcBook> cdAcBook, TitaVo... titaVo) throws DBException;

}
