package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.ClOther;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.ClOtherId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface ClOtherService {

  /**
   * findByPrimaryKey
   *
   * @param clOtherId PK
   * @param titaVo Variable-Length Argument
   * @return ClOther ClOther
   */
  public ClOther findById(ClOtherId clOtherId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice ClOther ClOther of List
   */
  public Slice<ClOther> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * ClCode1 = 
   *
   * @param clCode1_0 clCode1_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice ClOther ClOther of List
   */
  public Slice<ClOther> findClCode1(int clCode1_0, int index, int limit, TitaVo... titaVo);

  /**
   * ClCode1 = ,AND ClCode2 = 
   *
   * @param clCode1_0 clCode1_0
   * @param clCode2_1 clCode2_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice ClOther ClOther of List
   */
  public Slice<ClOther> findClCode2(int clCode1_0, int clCode2_1, int index, int limit, TitaVo... titaVo);

  /**
   * IssuingId = ,AND DocNo = ,AND OwnerId =
   *
   * @param issuingId_0 issuingId_0
   * @param docNo_1 docNo_1
   * @param ownerId_2 ownerId_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice ClOther ClOther of List
   */
  public Slice<ClOther> findUnique(String issuingId_0, String docNo_1, String ownerId_2, int index, int limit, TitaVo... titaVo);

  /**
   * hold By ClOther
   * 
   * @param clOtherId key
   * @param titaVo Variable-Length Argument
   * @return ClOther ClOther
   */
  public ClOther holdById(ClOtherId clOtherId, TitaVo... titaVo);

  /**
   * hold By ClOther
   * 
   * @param clOther key
   * @param titaVo Variable-Length Argument
   * @return ClOther ClOther
   */
  public ClOther holdById(ClOther clOther, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param clOther Entity
   * @param titaVo Variable-Length Argument
   * @return ClOther Entity
   * @throws DBException exception
   */
  public ClOther insert(ClOther clOther, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param clOther Entity
   * @param titaVo Variable-Length Argument
   * @return ClOther Entity
   * @throws DBException exception
   */
  public ClOther update(ClOther clOther, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param clOther Entity
   * @param titaVo Variable-Length Argument
   * @return ClOther Entity
   * @throws DBException exception
   */
  public ClOther update2(ClOther clOther, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param clOther Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(ClOther clOther, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param clOther Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<ClOther> clOther, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param clOther Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<ClOther> clOther, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param clOther Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<ClOther> clOther, TitaVo... titaVo) throws DBException;

}
