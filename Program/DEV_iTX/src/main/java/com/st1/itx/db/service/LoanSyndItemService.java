package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.LoanSyndItem;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.LoanSyndItemId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface LoanSyndItemService {

  /**
   * findByPrimaryKey
   *
   * @param loanSyndItemId PK
   * @param titaVo Variable-Length Argument
   * @return LoanSyndItem LoanSyndItem
   */
  public LoanSyndItem findById(LoanSyndItemId loanSyndItemId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice LoanSyndItem LoanSyndItem of List
   */
  public Slice<LoanSyndItem> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * SyndNo =
   *
   * @param syndNo_0 syndNo_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice LoanSyndItem LoanSyndItem of List
   */
  public Slice<LoanSyndItem> findSyndNoEq(int syndNo_0, int index, int limit, TitaVo... titaVo);

  /**
   * hold By LoanSyndItem
   * 
   * @param loanSyndItemId key
   * @param titaVo Variable-Length Argument
   * @return LoanSyndItem LoanSyndItem
   */
  public LoanSyndItem holdById(LoanSyndItemId loanSyndItemId, TitaVo... titaVo);

  /**
   * hold By LoanSyndItem
   * 
   * @param loanSyndItem key
   * @param titaVo Variable-Length Argument
   * @return LoanSyndItem LoanSyndItem
   */
  public LoanSyndItem holdById(LoanSyndItem loanSyndItem, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param loanSyndItem Entity
   * @param titaVo Variable-Length Argument
   * @return LoanSyndItem Entity
   * @throws DBException exception
   */
  public LoanSyndItem insert(LoanSyndItem loanSyndItem, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param loanSyndItem Entity
   * @param titaVo Variable-Length Argument
   * @return LoanSyndItem Entity
   * @throws DBException exception
   */
  public LoanSyndItem update(LoanSyndItem loanSyndItem, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param loanSyndItem Entity
   * @param titaVo Variable-Length Argument
   * @return LoanSyndItem Entity
   * @throws DBException exception
   */
  public LoanSyndItem update2(LoanSyndItem loanSyndItem, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param loanSyndItem Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(LoanSyndItem loanSyndItem, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param loanSyndItem Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<LoanSyndItem> loanSyndItem, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param loanSyndItem Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<LoanSyndItem> loanSyndItem, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param loanSyndItem Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<LoanSyndItem> loanSyndItem, TitaVo... titaVo) throws DBException;

}
