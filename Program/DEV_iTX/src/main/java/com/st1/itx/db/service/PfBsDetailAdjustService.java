package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.PfBsDetailAdjust;
import org.springframework.data.domain.Slice;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface PfBsDetailAdjustService {

  /**
   * findByPrimaryKey
   *
   * @param logNo PK
   * @param titaVo Variable-Length Argument
   * @return PfBsDetailAdjust PfBsDetailAdjust
   */
  public PfBsDetailAdjust findById(Long logNo, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice PfBsDetailAdjust PfBsDetailAdjust of List
   */
  public Slice<PfBsDetailAdjust> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * CustNo = ,AND FacmNo = ,AND WorkMonth =
   *
   * @param custNo_0 custNo_0
   * @param facmNo_1 facmNo_1
   * @param workMonth_2 workMonth_2
   * @param titaVo Variable-Length Argument
   * @return Slice PfBsDetailAdjust PfBsDetailAdjust of List
   */
  public PfBsDetailAdjust findCustFacmFirst(int custNo_0, int facmNo_1, int workMonth_2, TitaVo... titaVo);

  /**
   * hold By PfBsDetailAdjust
   * 
   * @param logNo key
   * @param titaVo Variable-Length Argument
   * @return PfBsDetailAdjust PfBsDetailAdjust
   */
  public PfBsDetailAdjust holdById(Long logNo, TitaVo... titaVo);

  /**
   * hold By PfBsDetailAdjust
   * 
   * @param pfBsDetailAdjust key
   * @param titaVo Variable-Length Argument
   * @return PfBsDetailAdjust PfBsDetailAdjust
   */
  public PfBsDetailAdjust holdById(PfBsDetailAdjust pfBsDetailAdjust, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param pfBsDetailAdjust Entity
   * @param titaVo Variable-Length Argument
   * @return PfBsDetailAdjust Entity
   * @throws DBException exception
   */
  public PfBsDetailAdjust insert(PfBsDetailAdjust pfBsDetailAdjust, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param pfBsDetailAdjust Entity
   * @param titaVo Variable-Length Argument
   * @return PfBsDetailAdjust Entity
   * @throws DBException exception
   */
  public PfBsDetailAdjust update(PfBsDetailAdjust pfBsDetailAdjust, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param pfBsDetailAdjust Entity
   * @param titaVo Variable-Length Argument
   * @return PfBsDetailAdjust Entity
   * @throws DBException exception
   */
  public PfBsDetailAdjust update2(PfBsDetailAdjust pfBsDetailAdjust, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param pfBsDetailAdjust Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(PfBsDetailAdjust pfBsDetailAdjust, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param pfBsDetailAdjust Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<PfBsDetailAdjust> pfBsDetailAdjust, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param pfBsDetailAdjust Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<PfBsDetailAdjust> pfBsDetailAdjust, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param pfBsDetailAdjust Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<PfBsDetailAdjust> pfBsDetailAdjust, TitaVo... titaVo) throws DBException;

}
