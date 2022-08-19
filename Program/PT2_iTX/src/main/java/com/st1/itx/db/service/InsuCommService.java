package com.st1.itx.db.service;

import java.util.List;

import java.math.BigDecimal;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.InsuComm;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.InsuCommId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface InsuCommService {

  /**
   * findByPrimaryKey
   *
   * @param insuCommId PK
   * @param titaVo Variable-Length Argument
   * @return InsuComm InsuComm
   */
  public InsuComm findById(InsuCommId insuCommId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice InsuComm InsuComm of List
   */
  public Slice<InsuComm> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * InsuYearMonth &gt;= ,AND InsuYearMonth &lt;= 
   *
   * @param insuYearMonth_0 insuYearMonth_0
   * @param insuYearMonth_1 insuYearMonth_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice InsuComm InsuComm of List
   */
  public Slice<InsuComm> insuYearMonthRng(int insuYearMonth_0, int insuYearMonth_1, int index, int limit, TitaVo... titaVo);

  /**
   * InsuYearMonth = , AND CommDate &gt;= , AND CommDate &lt;=
   *
   * @param insuYearMonth_0 insuYearMonth_0
   * @param commDate_1 commDate_1
   * @param commDate_2 commDate_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice InsuComm InsuComm of List
   */
  public Slice<InsuComm> findL4606A(int insuYearMonth_0, int commDate_1, int commDate_2, int index, int limit, TitaVo... titaVo);

  /**
   * CustNo =
   *
   * @param custNo_0 custNo_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice InsuComm InsuComm of List
   */
  public Slice<InsuComm> findCustNo(int custNo_0, int index, int limit, TitaVo... titaVo);

  /**
   * FireOfficer = 
   *
   * @param fireOfficer_0 fireOfficer_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice InsuComm InsuComm of List
   */
  public Slice<InsuComm> findFireOfficer(String fireOfficer_0, int index, int limit, TitaVo... titaVo);

  /**
   * EmpId = 
   *
   * @param empId_0 empId_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice InsuComm InsuComm of List
   */
  public Slice<InsuComm> findEmpId(String empId_0, int index, int limit, TitaVo... titaVo);

  /**
   * hold By InsuComm
   * 
   * @param insuCommId key
   * @param titaVo Variable-Length Argument
   * @return InsuComm InsuComm
   */
  public InsuComm holdById(InsuCommId insuCommId, TitaVo... titaVo);

  /**
   * hold By InsuComm
   * 
   * @param insuComm key
   * @param titaVo Variable-Length Argument
   * @return InsuComm InsuComm
   */
  public InsuComm holdById(InsuComm insuComm, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param insuComm Entity
   * @param titaVo Variable-Length Argument
   * @return InsuComm Entity
   * @throws DBException exception
   */
  public InsuComm insert(InsuComm insuComm, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param insuComm Entity
   * @param titaVo Variable-Length Argument
   * @return InsuComm Entity
   * @throws DBException exception
   */
  public InsuComm update(InsuComm insuComm, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param insuComm Entity
   * @param titaVo Variable-Length Argument
   * @return InsuComm Entity
   * @throws DBException exception
   */
  public InsuComm update2(InsuComm insuComm, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param insuComm Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(InsuComm insuComm, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param insuComm Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<InsuComm> insuComm, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param insuComm Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<InsuComm> insuComm, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param insuComm Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<InsuComm> insuComm, TitaVo... titaVo) throws DBException;

}
