package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.TxAuthGroup;
import org.springframework.data.domain.Slice;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface TxAuthGroupService {

  /**
   * findByPrimaryKey
   *
   * @param authNo PK
   * @param titaVo Variable-Length Argument
   * @return TxAuthGroup TxAuthGroup
   */
  public TxAuthGroup findById(String authNo, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice TxAuthGroup TxAuthGroup of List
   */
  public Slice<TxAuthGroup> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * AuthNo %
   *
   * @param authNo_0 authNo_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice TxAuthGroup TxAuthGroup of List
   */
  public Slice<TxAuthGroup> AuthNoLike(String authNo_0, int index, int limit, TitaVo... titaVo);

  /**
   * BranchNo = ,AND LevelFg = 
   *
   * @param branchNo_0 branchNo_0
   * @param levelFg_1 levelFg_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice TxAuthGroup TxAuthGroup of List
   */
  public Slice<TxAuthGroup> BranchAll(String branchNo_0, int levelFg_1, int index, int limit, TitaVo... titaVo);

  /**
   * BranchNo = ,AND AuthNo %
   *
   * @param branchNo_0 branchNo_0
   * @param authNo_1 authNo_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice TxAuthGroup TxAuthGroup of List
   */
  public Slice<TxAuthGroup> BranchAuthNo(String branchNo_0, String authNo_1, int index, int limit, TitaVo... titaVo);

  /**
   * hold By TxAuthGroup
   * 
   * @param authNo key
   * @param titaVo Variable-Length Argument
   * @return TxAuthGroup TxAuthGroup
   */
  public TxAuthGroup holdById(String authNo, TitaVo... titaVo);

  /**
   * hold By TxAuthGroup
   * 
   * @param txAuthGroup key
   * @param titaVo Variable-Length Argument
   * @return TxAuthGroup TxAuthGroup
   */
  public TxAuthGroup holdById(TxAuthGroup txAuthGroup, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param txAuthGroup Entity
   * @param titaVo Variable-Length Argument
   * @return TxAuthGroup Entity
   * @throws DBException exception
   */
  public TxAuthGroup insert(TxAuthGroup txAuthGroup, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param txAuthGroup Entity
   * @param titaVo Variable-Length Argument
   * @return TxAuthGroup Entity
   * @throws DBException exception
   */
  public TxAuthGroup update(TxAuthGroup txAuthGroup, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param txAuthGroup Entity
   * @param titaVo Variable-Length Argument
   * @return TxAuthGroup Entity
   * @throws DBException exception
   */
  public TxAuthGroup update2(TxAuthGroup txAuthGroup, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param txAuthGroup Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(TxAuthGroup txAuthGroup, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param txAuthGroup Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<TxAuthGroup> txAuthGroup, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param txAuthGroup Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<TxAuthGroup> txAuthGroup, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param txAuthGroup Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<TxAuthGroup> txAuthGroup, TitaVo... titaVo) throws DBException;

}
