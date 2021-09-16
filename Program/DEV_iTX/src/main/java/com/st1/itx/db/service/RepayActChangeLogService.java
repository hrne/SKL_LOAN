package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.RepayActChangeLog;
import org.springframework.data.domain.Slice;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface RepayActChangeLogService {

  /**
   * findByPrimaryKey
   *
   * @param logNo PK
   * @param titaVo Variable-Length Argument
   * @return RepayActChangeLog RepayActChangeLog
   */
  public RepayActChangeLog findById(Long logNo, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice RepayActChangeLog RepayActChangeLog of List
   */
  public Slice<RepayActChangeLog> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * CustNo = , AND FacmNo = 
   *
   * @param custNo_0 custNo_0
   * @param facmNo_1 facmNo_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice RepayActChangeLog RepayActChangeLog of List
   */
  public Slice<RepayActChangeLog> findFacmNoEq(int custNo_0, int facmNo_1, int index, int limit, TitaVo... titaVo);

  /**
   * RelDy = , AND RelTxseq =  
   *
   * @param relDy_0 relDy_0
   * @param relTxseq_1 relTxseq_1
   * @param titaVo Variable-Length Argument
   * @return Slice RepayActChangeLog RepayActChangeLog of List
   */
  public RepayActChangeLog findRelTxseqFirst(int relDy_0, String relTxseq_1, TitaVo... titaVo);

  /**
   * CustNo = , AND FacmNo = 
   *
   * @param custNo_0 custNo_0
   * @param facmNo_1 facmNo_1
   * @param titaVo Variable-Length Argument
   * @return Slice RepayActChangeLog RepayActChangeLog of List
   */
  public RepayActChangeLog findFacmNoFirst(int custNo_0, int facmNo_1, TitaVo... titaVo);

  /**
   * hold By RepayActChangeLog
   * 
   * @param logNo key
   * @param titaVo Variable-Length Argument
   * @return RepayActChangeLog RepayActChangeLog
   */
  public RepayActChangeLog holdById(Long logNo, TitaVo... titaVo);

  /**
   * hold By RepayActChangeLog
   * 
   * @param repayActChangeLog key
   * @param titaVo Variable-Length Argument
   * @return RepayActChangeLog RepayActChangeLog
   */
  public RepayActChangeLog holdById(RepayActChangeLog repayActChangeLog, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param repayActChangeLog Entity
   * @param titaVo Variable-Length Argument
   * @return RepayActChangeLog Entity
   * @throws DBException exception
   */
  public RepayActChangeLog insert(RepayActChangeLog repayActChangeLog, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param repayActChangeLog Entity
   * @param titaVo Variable-Length Argument
   * @return RepayActChangeLog Entity
   * @throws DBException exception
   */
  public RepayActChangeLog update(RepayActChangeLog repayActChangeLog, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param repayActChangeLog Entity
   * @param titaVo Variable-Length Argument
   * @return RepayActChangeLog Entity
   * @throws DBException exception
   */
  public RepayActChangeLog update2(RepayActChangeLog repayActChangeLog, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param repayActChangeLog Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(RepayActChangeLog repayActChangeLog, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param repayActChangeLog Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<RepayActChangeLog> repayActChangeLog, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param repayActChangeLog Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<RepayActChangeLog> repayActChangeLog, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param repayActChangeLog Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<RepayActChangeLog> repayActChangeLog, TitaVo... titaVo) throws DBException;

}
