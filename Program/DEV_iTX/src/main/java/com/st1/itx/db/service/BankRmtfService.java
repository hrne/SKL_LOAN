package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.BankRmtf;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.BankRmtfId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface BankRmtfService {

  /**
   * findByPrimaryKey
   *
   * @param bankRmtfId PK
   * @param titaVo Variable-Length Argument
   * @return BankRmtf BankRmtf
   */
  public BankRmtf findById(BankRmtfId bankRmtfId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice BankRmtf BankRmtf of List
   */
  public Slice<BankRmtf> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * hold By BankRmtf
   * 
   * @param bankRmtfId key
   * @param titaVo Variable-Length Argument
   * @return BankRmtf BankRmtf
   */
  public BankRmtf holdById(BankRmtfId bankRmtfId, TitaVo... titaVo);

  /**
   * hold By BankRmtf
   * 
   * @param bankRmtf key
   * @param titaVo Variable-Length Argument
   * @return BankRmtf BankRmtf
   */
  public BankRmtf holdById(BankRmtf bankRmtf, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param bankRmtf Entity
   * @param titaVo Variable-Length Argument
   * @return BankRmtf Entity
   * @throws DBException exception
   */
  public BankRmtf insert(BankRmtf bankRmtf, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param bankRmtf Entity
   * @param titaVo Variable-Length Argument
   * @return BankRmtf Entity
   * @throws DBException exception
   */
  public BankRmtf update(BankRmtf bankRmtf, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param bankRmtf Entity
   * @param titaVo Variable-Length Argument
   * @return BankRmtf Entity
   * @throws DBException exception
   */
  public BankRmtf update2(BankRmtf bankRmtf, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param bankRmtf Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(BankRmtf bankRmtf, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param bankRmtf Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<BankRmtf> bankRmtf, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param bankRmtf Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<BankRmtf> bankRmtf, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param bankRmtf Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<BankRmtf> bankRmtf, TitaVo... titaVo) throws DBException;

}
