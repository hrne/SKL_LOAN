package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.NegAppr02;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.NegAppr02Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface NegAppr02Service {

  /**
   * findByPrimaryKey
   *
   * @param negAppr02Id PK
   * @param titaVo Variable-Length Argument
   * @return NegAppr02 NegAppr02
   */
  public NegAppr02 findById(NegAppr02Id negAppr02Id, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice NegAppr02 NegAppr02 of List
   */
  public Slice<NegAppr02> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * AcDate &gt;= , AND AcDate &lt;=
   *
   * @param acDate_0 acDate_0
   * @param acDate_1 acDate_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice NegAppr02 NegAppr02 of List
   */
  public Slice<NegAppr02> acDateBetween(int acDate_0, int acDate_1, int index, int limit, TitaVo... titaVo);

  /**
   * AcDate =
   *
   * @param acDate_0 acDate_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice NegAppr02 NegAppr02 of List
   */
  public Slice<NegAppr02> acDateEq(int acDate_0, int index, int limit, TitaVo... titaVo);

  /**
   * BringUpDate =
   *
   * @param bringUpDate_0 bringUpDate_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice NegAppr02 NegAppr02 of List
   */
  public Slice<NegAppr02> bringUpDateEq(int bringUpDate_0, int index, int limit, TitaVo... titaVo);

  /**
   * hold By NegAppr02
   * 
   * @param negAppr02Id key
   * @param titaVo Variable-Length Argument
   * @return NegAppr02 NegAppr02
   */
  public NegAppr02 holdById(NegAppr02Id negAppr02Id, TitaVo... titaVo);

  /**
   * hold By NegAppr02
   * 
   * @param negAppr02 key
   * @param titaVo Variable-Length Argument
   * @return NegAppr02 NegAppr02
   */
  public NegAppr02 holdById(NegAppr02 negAppr02, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param negAppr02 Entity
   * @param titaVo Variable-Length Argument
   * @return NegAppr02 Entity
   * @throws DBException exception
   */
  public NegAppr02 insert(NegAppr02 negAppr02, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param negAppr02 Entity
   * @param titaVo Variable-Length Argument
   * @return NegAppr02 Entity
   * @throws DBException exception
   */
  public NegAppr02 update(NegAppr02 negAppr02, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param negAppr02 Entity
   * @param titaVo Variable-Length Argument
   * @return NegAppr02 Entity
   * @throws DBException exception
   */
  public NegAppr02 update2(NegAppr02 negAppr02, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param negAppr02 Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(NegAppr02 negAppr02, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param negAppr02 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<NegAppr02> negAppr02, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param negAppr02 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<NegAppr02> negAppr02, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param negAppr02 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<NegAppr02> negAppr02, TitaVo... titaVo) throws DBException;

}
