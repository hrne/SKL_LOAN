package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.TxFlow;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.TxFlowId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface TxFlowService {

  /**
   * findByPrimaryKey
   *
   * @param txFlowId PK
   * @param titaVo Variable-Length Argument
   * @return TxFlow TxFlow
   */
  public TxFlow findById(TxFlowId txFlowId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice TxFlow TxFlow of List
   */
  public Slice<TxFlow> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * Entdy = ,AND FlowBrNo =
   *
   * @param entdy_0 entdy_0
   * @param flowBrNo_1 flowBrNo_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice TxFlow TxFlow of List
   */
  public Slice<TxFlow> findByFlowBrNo(int entdy_0, String flowBrNo_1, int index, int limit, TitaVo... titaVo);

  /**
   * Entdy = ,AND FlowBrNo = ,AND FlowMode = ,AND TranNo % ,AND FlowGroupNo ^i
   *
   * @param entdy_0 entdy_0
   * @param flowBrNo_1 flowBrNo_1
   * @param flowMode_2 flowMode_2
   * @param tranNo_3 tranNo_3
   * @param flowGroupNo_4 flowGroupNo_4
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice TxFlow TxFlow of List
   */
  public Slice<TxFlow> findByLC003(int entdy_0, String flowBrNo_1, int flowMode_2, String tranNo_3, List<String> flowGroupNo_4, int index, int limit, TitaVo... titaVo);

  /**
   * Entdy = ,AND SecNo =
   *
   * @param entdy_0 entdy_0
   * @param secNo_1 secNo_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice TxFlow TxFlow of List
   */
  public Slice<TxFlow> findBySecNo(int entdy_0, String secNo_1, int index, int limit, TitaVo... titaVo);

  /**
   * hold By TxFlow
   * 
   * @param txFlowId key
   * @param titaVo Variable-Length Argument
   * @return TxFlow TxFlow
   */
  public TxFlow holdById(TxFlowId txFlowId, TitaVo... titaVo);

  /**
   * hold By TxFlow
   * 
   * @param txFlow key
   * @param titaVo Variable-Length Argument
   * @return TxFlow TxFlow
   */
  public TxFlow holdById(TxFlow txFlow, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param txFlow Entity
   * @param titaVo Variable-Length Argument
   * @return TxFlow Entity
   * @throws DBException exception
   */
  public TxFlow insert(TxFlow txFlow, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param txFlow Entity
   * @param titaVo Variable-Length Argument
   * @return TxFlow Entity
   * @throws DBException exception
   */
  public TxFlow update(TxFlow txFlow, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param txFlow Entity
   * @param titaVo Variable-Length Argument
   * @return TxFlow Entity
   * @throws DBException exception
   */
  public TxFlow update2(TxFlow txFlow, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param txFlow Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(TxFlow txFlow, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param txFlow Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<TxFlow> txFlow, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param txFlow Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<TxFlow> txFlow, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param txFlow Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<TxFlow> txFlow, TitaVo... titaVo) throws DBException;

}
