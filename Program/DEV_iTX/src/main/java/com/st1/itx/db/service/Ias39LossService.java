package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.Ias39Loss;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.Ias39LossId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface Ias39LossService {

  /**
   * findByPrimaryKey
   *
   * @param ias39LossId PK
   * @param titaVo Variable-Length Argument
   * @return Ias39Loss Ias39Loss
   */
  public Ias39Loss findById(Ias39LossId ias39LossId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice Ias39Loss Ias39Loss of List
   */
  public Slice<Ias39Loss> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * CustNo = ,AND FacmNo &gt;= ,AND FacmNo &lt;= 
   *
   * @param custNo_0 custNo_0
   * @param facmNo_1 facmNo_1
   * @param facmNo_2 facmNo_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice Ias39Loss Ias39Loss of List
   */
  public Slice<Ias39Loss> findFacmNo(int custNo_0, int facmNo_1, int facmNo_2, int index, int limit, TitaVo... titaVo);

  /**
   * CustNo &gt;= ,AND CustNo &lt;= ,AND FacmNo &gt;= ,AND FacmNo &lt;= 
   *
   * @param custNo_0 custNo_0
   * @param custNo_1 custNo_1
   * @param facmNo_2 facmNo_2
   * @param facmNo_3 facmNo_3
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice Ias39Loss Ias39Loss of List
   */
  public Slice<Ias39Loss> findCustNo(int custNo_0, int custNo_1, int facmNo_2, int facmNo_3, int index, int limit, TitaVo... titaVo);

  /**
   * hold By Ias39Loss
   * 
   * @param ias39LossId key
   * @param titaVo Variable-Length Argument
   * @return Ias39Loss Ias39Loss
   */
  public Ias39Loss holdById(Ias39LossId ias39LossId, TitaVo... titaVo);

  /**
   * hold By Ias39Loss
   * 
   * @param ias39Loss key
   * @param titaVo Variable-Length Argument
   * @return Ias39Loss Ias39Loss
   */
  public Ias39Loss holdById(Ias39Loss ias39Loss, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param ias39Loss Entity
   * @param titaVo Variable-Length Argument
   * @return Ias39Loss Entity
   * @throws DBException exception
   */
  public Ias39Loss insert(Ias39Loss ias39Loss, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param ias39Loss Entity
   * @param titaVo Variable-Length Argument
   * @return Ias39Loss Entity
   * @throws DBException exception
   */
  public Ias39Loss update(Ias39Loss ias39Loss, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param ias39Loss Entity
   * @param titaVo Variable-Length Argument
   * @return Ias39Loss Entity
   * @throws DBException exception
   */
  public Ias39Loss update2(Ias39Loss ias39Loss, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param ias39Loss Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(Ias39Loss ias39Loss, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param ias39Loss Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<Ias39Loss> ias39Loss, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param ias39Loss Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<Ias39Loss> ias39Loss, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param ias39Loss Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<Ias39Loss> ias39Loss, TitaVo... titaVo) throws DBException;

}
