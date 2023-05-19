package com.st1.itx.db.service;

import java.util.List;

import java.math.BigDecimal;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.PfIntranetAdjust;
import org.springframework.data.domain.Slice;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface PfIntranetAdjustService {

  /**
   * findByPrimaryKey
   *
   * @param logNo PK
   * @param titaVo Variable-Length Argument
   * @return PfIntranetAdjust PfIntranetAdjust
   */
  public PfIntranetAdjust findById(Long logNo, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice PfIntranetAdjust PfIntranetAdjust of List
   */
  public Slice<PfIntranetAdjust> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * CustNo = ,AND FacmNo = ,AND BormNo = ,AND WorkMonth =
   *
   * @param custNo_0 custNo_0
   * @param facmNo_1 facmNo_1
   * @param bormNo_2 bormNo_2
   * @param workMonth_3 workMonth_3
   * @param titaVo Variable-Length Argument
   * @return Slice PfIntranetAdjust PfIntranetAdjust of List
   */
  public PfIntranetAdjust findByBormFirst(int custNo_0, int facmNo_1, int bormNo_2, int workMonth_3, TitaVo... titaVo);

  /**
   * WorkMonth =
   *
   * @param workMonth_0 workMonth_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice PfIntranetAdjust PfIntranetAdjust of List
   */
  public Slice<PfIntranetAdjust> findByWorkMonth(int workMonth_0, int index, int limit, TitaVo... titaVo);

  /**
   * hold By PfIntranetAdjust
   * 
   * @param logNo key
   * @param titaVo Variable-Length Argument
   * @return PfIntranetAdjust PfIntranetAdjust
   */
  public PfIntranetAdjust holdById(Long logNo, TitaVo... titaVo);

  /**
   * hold By PfIntranetAdjust
   * 
   * @param pfIntranetAdjust key
   * @param titaVo Variable-Length Argument
   * @return PfIntranetAdjust PfIntranetAdjust
   */
  public PfIntranetAdjust holdById(PfIntranetAdjust pfIntranetAdjust, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param pfIntranetAdjust Entity
   * @param titaVo Variable-Length Argument
   * @return PfIntranetAdjust Entity
   * @throws DBException exception
   */
  public PfIntranetAdjust insert(PfIntranetAdjust pfIntranetAdjust, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param pfIntranetAdjust Entity
   * @param titaVo Variable-Length Argument
   * @return PfIntranetAdjust Entity
   * @throws DBException exception
   */
  public PfIntranetAdjust update(PfIntranetAdjust pfIntranetAdjust, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param pfIntranetAdjust Entity
   * @param titaVo Variable-Length Argument
   * @return PfIntranetAdjust Entity
   * @throws DBException exception
   */
  public PfIntranetAdjust update2(PfIntranetAdjust pfIntranetAdjust, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param pfIntranetAdjust Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(PfIntranetAdjust pfIntranetAdjust, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param pfIntranetAdjust Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<PfIntranetAdjust> pfIntranetAdjust, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param pfIntranetAdjust Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<PfIntranetAdjust> pfIntranetAdjust, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param pfIntranetAdjust Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<PfIntranetAdjust> pfIntranetAdjust, TitaVo... titaVo) throws DBException;

}
