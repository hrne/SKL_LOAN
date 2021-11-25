package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CdGseq;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.CdGseqId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CdGseqService {

  /**
   * findByPrimaryKey
   *
   * @param cdGseqId PK
   * @param titaVo Variable-Length Argument
   * @return CdGseq CdGseq
   */
  public CdGseq findById(CdGseqId cdGseqId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdGseq CdGseq of List
   */
  public Slice<CdGseq> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * hold By CdGseq
   * 
   * @param cdGseqId key
   * @param titaVo Variable-Length Argument
   * @return CdGseq CdGseq
   */
  public CdGseq holdById(CdGseqId cdGseqId, TitaVo... titaVo);

  /**
   * hold By CdGseq
   * 
   * @param cdGseq key
   * @param titaVo Variable-Length Argument
   * @return CdGseq CdGseq
   */
  public CdGseq holdById(CdGseq cdGseq, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param cdGseq Entity
   * @param titaVo Variable-Length Argument
   * @return CdGseq Entity
   * @throws DBException exception
   */
  public CdGseq insert(CdGseq cdGseq, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param cdGseq Entity
   * @param titaVo Variable-Length Argument
   * @return CdGseq Entity
   * @throws DBException exception
   */
  public CdGseq update(CdGseq cdGseq, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param cdGseq Entity
   * @param titaVo Variable-Length Argument
   * @return CdGseq Entity
   * @throws DBException exception
   */
  public CdGseq update2(CdGseq cdGseq, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param cdGseq Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(CdGseq cdGseq, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param cdGseq Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<CdGseq> cdGseq, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param cdGseq Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<CdGseq> cdGseq, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param cdGseq Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<CdGseq> cdGseq, TitaVo... titaVo) throws DBException;

}
