package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.InsuOrignal;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.InsuOrignalId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface InsuOrignalService {

  /**
   * findByPrimaryKey
   *
   * @param insuOrignalId PK
   * @param titaVo Variable-Length Argument
   * @return InsuOrignal InsuOrignal
   */
  public InsuOrignal findById(InsuOrignalId insuOrignalId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice InsuOrignal InsuOrignal of List
   */
  public Slice<InsuOrignal> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * ClCode1 = ,AND ClCode2 = ,AND ClNo = 
   *
   * @param clCode1_0 clCode1_0
   * @param clCode2_1 clCode2_1
   * @param clNo_2 clNo_2
   * @param titaVo Variable-Length Argument
   * @return Slice InsuOrignal InsuOrignal of List
   */
  public InsuOrignal clNoFirst(int clCode1_0, int clCode2_1, int clNo_2, TitaVo... titaVo);

  /**
   * InsuEndDate &gt;= , AND InsuEndDate &lt;=
   *
   * @param insuEndDate_0 insuEndDate_0
   * @param insuEndDate_1 insuEndDate_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice InsuOrignal InsuOrignal of List
   */
  public Slice<InsuOrignal> insuEndDateRNG(int insuEndDate_0, int insuEndDate_1, int index, int limit, TitaVo... titaVo);

  /**
   * ClCode1 = ,AND ClCode2 = ,AND ClNo = 
   *
   * @param clCode1_0 clCode1_0
   * @param clCode2_1 clCode2_1
   * @param clNo_2 clNo_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice InsuOrignal InsuOrignal of List
   */
  public Slice<InsuOrignal> clNoEqual(int clCode1_0, int clCode2_1, int clNo_2, int index, int limit, TitaVo... titaVo);

  /**
   * hold By InsuOrignal
   * 
   * @param insuOrignalId key
   * @param titaVo Variable-Length Argument
   * @return InsuOrignal InsuOrignal
   */
  public InsuOrignal holdById(InsuOrignalId insuOrignalId, TitaVo... titaVo);

  /**
   * hold By InsuOrignal
   * 
   * @param insuOrignal key
   * @param titaVo Variable-Length Argument
   * @return InsuOrignal InsuOrignal
   */
  public InsuOrignal holdById(InsuOrignal insuOrignal, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param insuOrignal Entity
   * @param titaVo Variable-Length Argument
   * @return InsuOrignal Entity
   * @throws DBException exception
   */
  public InsuOrignal insert(InsuOrignal insuOrignal, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param insuOrignal Entity
   * @param titaVo Variable-Length Argument
   * @return InsuOrignal Entity
   * @throws DBException exception
   */
  public InsuOrignal update(InsuOrignal insuOrignal, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param insuOrignal Entity
   * @param titaVo Variable-Length Argument
   * @return InsuOrignal Entity
   * @throws DBException exception
   */
  public InsuOrignal update2(InsuOrignal insuOrignal, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param insuOrignal Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(InsuOrignal insuOrignal, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param insuOrignal Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<InsuOrignal> insuOrignal, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param insuOrignal Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<InsuOrignal> insuOrignal, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param insuOrignal Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<InsuOrignal> insuOrignal, TitaVo... titaVo) throws DBException;

}
