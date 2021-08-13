package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.BankRelationFamily;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.BankRelationFamilyId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface BankRelationFamilyService {

  /**
   * findByPrimaryKey
   *
   * @param bankRelationFamilyId PK
   * @param titaVo Variable-Length Argument
   * @return BankRelationFamily BankRelationFamily
   */
  public BankRelationFamily findById(BankRelationFamilyId bankRelationFamilyId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice BankRelationFamily BankRelationFamily of List
   */
  public Slice<BankRelationFamily> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * RelationId =
   *
   * @param relationId_0 relationId_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice BankRelationFamily BankRelationFamily of List
   */
  public Slice<BankRelationFamily> findRelationIdEq(String relationId_0, int index, int limit, TitaVo... titaVo);

  /**
   * hold By BankRelationFamily
   * 
   * @param bankRelationFamilyId key
   * @param titaVo Variable-Length Argument
   * @return BankRelationFamily BankRelationFamily
   */
  public BankRelationFamily holdById(BankRelationFamilyId bankRelationFamilyId, TitaVo... titaVo);

  /**
   * hold By BankRelationFamily
   * 
   * @param bankRelationFamily key
   * @param titaVo Variable-Length Argument
   * @return BankRelationFamily BankRelationFamily
   */
  public BankRelationFamily holdById(BankRelationFamily bankRelationFamily, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param bankRelationFamily Entity
   * @param titaVo Variable-Length Argument
   * @return BankRelationFamily Entity
   * @throws DBException exception
   */
  public BankRelationFamily insert(BankRelationFamily bankRelationFamily, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param bankRelationFamily Entity
   * @param titaVo Variable-Length Argument
   * @return BankRelationFamily Entity
   * @throws DBException exception
   */
  public BankRelationFamily update(BankRelationFamily bankRelationFamily, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param bankRelationFamily Entity
   * @param titaVo Variable-Length Argument
   * @return BankRelationFamily Entity
   * @throws DBException exception
   */
  public BankRelationFamily update2(BankRelationFamily bankRelationFamily, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param bankRelationFamily Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(BankRelationFamily bankRelationFamily, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param bankRelationFamily Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<BankRelationFamily> bankRelationFamily, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param bankRelationFamily Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<BankRelationFamily> bankRelationFamily, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param bankRelationFamily Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<BankRelationFamily> bankRelationFamily, TitaVo... titaVo) throws DBException;

}
