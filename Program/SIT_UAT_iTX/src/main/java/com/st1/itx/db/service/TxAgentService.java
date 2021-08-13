package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.TxAgent;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.TxAgentId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface TxAgentService {

  /**
   * findByPrimaryKey
   *
   * @param txAgentId PK
   * @param titaVo Variable-Length Argument
   * @return TxAgent TxAgent
   */
  public TxAgent findById(TxAgentId txAgentId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice TxAgent TxAgent of List
   */
  public Slice<TxAgent> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * AgentTlrNo =
   *
   * @param agentTlrNo_0 agentTlrNo_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice TxAgent TxAgent of List
   */
  public Slice<TxAgent> findByAgentTlrNo(String agentTlrNo_0, int index, int limit, TitaVo... titaVo);

  /**
   * TlrNo =
   *
   * @param tlrNo_0 tlrNo_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice TxAgent TxAgent of List
   */
  public Slice<TxAgent> findByTlrNo(String tlrNo_0, int index, int limit, TitaVo... titaVo);

  /**
   * hold By TxAgent
   * 
   * @param txAgentId key
   * @param titaVo Variable-Length Argument
   * @return TxAgent TxAgent
   */
  public TxAgent holdById(TxAgentId txAgentId, TitaVo... titaVo);

  /**
   * hold By TxAgent
   * 
   * @param txAgent key
   * @param titaVo Variable-Length Argument
   * @return TxAgent TxAgent
   */
  public TxAgent holdById(TxAgent txAgent, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param txAgent Entity
   * @param titaVo Variable-Length Argument
   * @return TxAgent Entity
   * @throws DBException exception
   */
  public TxAgent insert(TxAgent txAgent, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param txAgent Entity
   * @param titaVo Variable-Length Argument
   * @return TxAgent Entity
   * @throws DBException exception
   */
  public TxAgent update(TxAgent txAgent, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param txAgent Entity
   * @param titaVo Variable-Length Argument
   * @return TxAgent Entity
   * @throws DBException exception
   */
  public TxAgent update2(TxAgent txAgent, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param txAgent Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(TxAgent txAgent, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param txAgent Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<TxAgent> txAgent, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param txAgent Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<TxAgent> txAgent, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param txAgent Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<TxAgent> txAgent, TitaVo... titaVo) throws DBException;

}
