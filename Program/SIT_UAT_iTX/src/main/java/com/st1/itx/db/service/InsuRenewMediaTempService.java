package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.InsuRenewMediaTemp;
import org.springframework.data.domain.Slice;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface InsuRenewMediaTempService {

  /**
   * findByPrimaryKey
   *
   * @param logNo PK
   * @param titaVo Variable-Length Argument
   * @return InsuRenewMediaTemp InsuRenewMediaTemp
   */
  public InsuRenewMediaTemp findById(Long logNo, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice InsuRenewMediaTemp InsuRenewMediaTemp of List
   */
  public Slice<InsuRenewMediaTemp> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * FireInsuMonth &gt;= ,AND FireInsuMonth &lt;= 
   *
   * @param fireInsuMonth_0 fireInsuMonth_0
   * @param fireInsuMonth_1 fireInsuMonth_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice InsuRenewMediaTemp InsuRenewMediaTemp of List
   */
  public Slice<InsuRenewMediaTemp> fireInsuMonthRg(String fireInsuMonth_0, String fireInsuMonth_1, int index, int limit, TitaVo... titaVo);

  /**
   * FireInsuMonth = ,AND ClCode1 = ,AND ClCode2 = ,AND ClNo = ,AND InsuNo =
   *
   * @param fireInsuMonth_0 fireInsuMonth_0
   * @param clCode1_1 clCode1_1
   * @param clCode2_2 clCode2_2
   * @param clNo_3 clNo_3
   * @param insuNo_4 insuNo_4
   * @param titaVo Variable-Length Argument
   * @return Slice InsuRenewMediaTemp InsuRenewMediaTemp of List
   */
  public InsuRenewMediaTemp fireInsuFirst(String fireInsuMonth_0, String clCode1_1, String clCode2_2, String clNo_3, String insuNo_4, TitaVo... titaVo);

  /**
   * hold By InsuRenewMediaTemp
   * 
   * @param logNo key
   * @param titaVo Variable-Length Argument
   * @return InsuRenewMediaTemp InsuRenewMediaTemp
   */
  public InsuRenewMediaTemp holdById(Long logNo, TitaVo... titaVo);

  /**
   * hold By InsuRenewMediaTemp
   * 
   * @param insuRenewMediaTemp key
   * @param titaVo Variable-Length Argument
   * @return InsuRenewMediaTemp InsuRenewMediaTemp
   */
  public InsuRenewMediaTemp holdById(InsuRenewMediaTemp insuRenewMediaTemp, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param insuRenewMediaTemp Entity
   * @param titaVo Variable-Length Argument
   * @return InsuRenewMediaTemp Entity
   * @throws DBException exception
   */
  public InsuRenewMediaTemp insert(InsuRenewMediaTemp insuRenewMediaTemp, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param insuRenewMediaTemp Entity
   * @param titaVo Variable-Length Argument
   * @return InsuRenewMediaTemp Entity
   * @throws DBException exception
   */
  public InsuRenewMediaTemp update(InsuRenewMediaTemp insuRenewMediaTemp, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param insuRenewMediaTemp Entity
   * @param titaVo Variable-Length Argument
   * @return InsuRenewMediaTemp Entity
   * @throws DBException exception
   */
  public InsuRenewMediaTemp update2(InsuRenewMediaTemp insuRenewMediaTemp, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param insuRenewMediaTemp Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(InsuRenewMediaTemp insuRenewMediaTemp, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param insuRenewMediaTemp Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<InsuRenewMediaTemp> insuRenewMediaTemp, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param insuRenewMediaTemp Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<InsuRenewMediaTemp> insuRenewMediaTemp, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param insuRenewMediaTemp Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<InsuRenewMediaTemp> insuRenewMediaTemp, TitaVo... titaVo) throws DBException;

}
