package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CdBranch;
import org.springframework.data.domain.Slice;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CdBranchService {

  /**
   * findByPrimaryKey
   *
   * @param branchNo PK
   * @param titaVo Variable-Length Argument
   * @return CdBranch CdBranch
   */
  public CdBranch findById(String branchNo, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdBranch CdBranch of List
   */
  public Slice<CdBranch> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * BranchNo %
   *
   * @param branchNo_0 branchNo_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdBranch CdBranch of List
   */
  public Slice<CdBranch> BranchNoLike(String branchNo_0, int index, int limit, TitaVo... titaVo);

  /**
   * hold By CdBranch
   * 
   * @param branchNo key
   * @param titaVo Variable-Length Argument
   * @return CdBranch CdBranch
   */
  public CdBranch holdById(String branchNo, TitaVo... titaVo);

  /**
   * hold By CdBranch
   * 
   * @param cdBranch key
   * @param titaVo Variable-Length Argument
   * @return CdBranch CdBranch
   */
  public CdBranch holdById(CdBranch cdBranch, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param cdBranch Entity
   * @param titaVo Variable-Length Argument
   * @return CdBranch Entity
   * @throws DBException exception
   */
  public CdBranch insert(CdBranch cdBranch, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param cdBranch Entity
   * @param titaVo Variable-Length Argument
   * @return CdBranch Entity
   * @throws DBException exception
   */
  public CdBranch update(CdBranch cdBranch, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param cdBranch Entity
   * @param titaVo Variable-Length Argument
   * @return CdBranch Entity
   * @throws DBException exception
   */
  public CdBranch update2(CdBranch cdBranch, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param cdBranch Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(CdBranch cdBranch, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param cdBranch Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<CdBranch> cdBranch, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param cdBranch Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<CdBranch> cdBranch, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param cdBranch Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<CdBranch> cdBranch, TitaVo... titaVo) throws DBException;

}
