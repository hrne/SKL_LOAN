package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.Lagdtp;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.LagdtpId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface LagdtpService {

  /**
   * findByPrimaryKey
   *
   * @param lagdtpId PK
   * @param titaVo Variable-Length Argument
   * @return Lagdtp Lagdtp
   */
  public Lagdtp findById(LagdtpId lagdtpId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice Lagdtp Lagdtp of List
   */
  public Slice<Lagdtp> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * hold By Lagdtp
   * 
   * @param lagdtpId key
   * @param titaVo Variable-Length Argument
   * @return Lagdtp Lagdtp
   */
  public Lagdtp holdById(LagdtpId lagdtpId, TitaVo... titaVo);

  /**
   * hold By Lagdtp
   * 
   * @param lagdtp key
   * @param titaVo Variable-Length Argument
   * @return Lagdtp Lagdtp
   */
  public Lagdtp holdById(Lagdtp lagdtp, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param lagdtp Entity
   * @param titaVo Variable-Length Argument
   * @return Lagdtp Entity
   * @throws DBException exception
   */
  public Lagdtp insert(Lagdtp lagdtp, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param lagdtp Entity
   * @param titaVo Variable-Length Argument
   * @return Lagdtp Entity
   * @throws DBException exception
   */
  public Lagdtp update(Lagdtp lagdtp, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param lagdtp Entity
   * @param titaVo Variable-Length Argument
   * @return Lagdtp Entity
   * @throws DBException exception
   */
  public Lagdtp update2(Lagdtp lagdtp, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param lagdtp Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(Lagdtp lagdtp, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param lagdtp Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<Lagdtp> lagdtp, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param lagdtp Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<Lagdtp> lagdtp, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param lagdtp Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<Lagdtp> lagdtp, TitaVo... titaVo) throws DBException;

}
