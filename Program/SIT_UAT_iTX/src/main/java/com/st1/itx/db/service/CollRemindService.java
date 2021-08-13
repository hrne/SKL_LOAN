package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CollRemind;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.CollRemindId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CollRemindService {

  /**
   * findByPrimaryKey
   *
   * @param collRemindId PK
   * @param titaVo Variable-Length Argument
   * @return CollRemind CollRemind
   */
  public CollRemind findById(CollRemindId collRemindId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CollRemind CollRemind of List
   */
  public Slice<CollRemind> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * CaseCode= , AND CustNo = , AND FacmNo = , AND CondCode =
   *
   * @param caseCode_0 caseCode_0
   * @param custNo_1 custNo_1
   * @param facmNo_2 facmNo_2
   * @param condCode_3 condCode_3
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CollRemind CollRemind of List
   */
  public Slice<CollRemind> findCl(String caseCode_0, int custNo_1, int facmNo_2, String condCode_3, int index, int limit, TitaVo... titaVo);

  /**
   * CaseCode= , AND CustNo = , AND CondCode =
   *
   * @param caseCode_0 caseCode_0
   * @param custNo_1 custNo_1
   * @param condCode_2 condCode_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CollRemind CollRemind of List
   */
  public Slice<CollRemind> findWithoutFacm(String caseCode_0, int custNo_1, String condCode_2, int index, int limit, TitaVo... titaVo);

  /**
   * hold By CollRemind
   * 
   * @param collRemindId key
   * @param titaVo Variable-Length Argument
   * @return CollRemind CollRemind
   */
  public CollRemind holdById(CollRemindId collRemindId, TitaVo... titaVo);

  /**
   * hold By CollRemind
   * 
   * @param collRemind key
   * @param titaVo Variable-Length Argument
   * @return CollRemind CollRemind
   */
  public CollRemind holdById(CollRemind collRemind, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param collRemind Entity
   * @param titaVo Variable-Length Argument
   * @return CollRemind Entity
   * @throws DBException exception
   */
  public CollRemind insert(CollRemind collRemind, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param collRemind Entity
   * @param titaVo Variable-Length Argument
   * @return CollRemind Entity
   * @throws DBException exception
   */
  public CollRemind update(CollRemind collRemind, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param collRemind Entity
   * @param titaVo Variable-Length Argument
   * @return CollRemind Entity
   * @throws DBException exception
   */
  public CollRemind update2(CollRemind collRemind, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param collRemind Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(CollRemind collRemind, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param collRemind Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<CollRemind> collRemind, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param collRemind Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<CollRemind> collRemind, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param collRemind Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<CollRemind> collRemind, TitaVo... titaVo) throws DBException;

}
