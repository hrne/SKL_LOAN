package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.LifeRelEmp;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.LifeRelEmpId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface LifeRelEmpService {

  /**
   * findByPrimaryKey
   *
   * @param lifeRelEmpId PK
   * @param titaVo Variable-Length Argument
   * @return LifeRelEmp LifeRelEmp
   */
  public LifeRelEmp findById(LifeRelEmpId lifeRelEmpId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice LifeRelEmp LifeRelEmp of List
   */
  public Slice<LifeRelEmp> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * AcDate = 
   *
   * @param acDate_0 acDate_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice LifeRelEmp LifeRelEmp of List
   */
  public Slice<LifeRelEmp> findAcDate(int acDate_0, int index, int limit, TitaVo... titaVo);

  /**
   * hold By LifeRelEmp
   * 
   * @param lifeRelEmpId key
   * @param titaVo Variable-Length Argument
   * @return LifeRelEmp LifeRelEmp
   */
  public LifeRelEmp holdById(LifeRelEmpId lifeRelEmpId, TitaVo... titaVo);

  /**
   * hold By LifeRelEmp
   * 
   * @param lifeRelEmp key
   * @param titaVo Variable-Length Argument
   * @return LifeRelEmp LifeRelEmp
   */
  public LifeRelEmp holdById(LifeRelEmp lifeRelEmp, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param lifeRelEmp Entity
   * @param titaVo Variable-Length Argument
   * @return LifeRelEmp Entity
   * @throws DBException exception
   */
  public LifeRelEmp insert(LifeRelEmp lifeRelEmp, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param lifeRelEmp Entity
   * @param titaVo Variable-Length Argument
   * @return LifeRelEmp Entity
   * @throws DBException exception
   */
  public LifeRelEmp update(LifeRelEmp lifeRelEmp, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param lifeRelEmp Entity
   * @param titaVo Variable-Length Argument
   * @return LifeRelEmp Entity
   * @throws DBException exception
   */
  public LifeRelEmp update2(LifeRelEmp lifeRelEmp, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param lifeRelEmp Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(LifeRelEmp lifeRelEmp, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param lifeRelEmp Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<LifeRelEmp> lifeRelEmp, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param lifeRelEmp Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<LifeRelEmp> lifeRelEmp, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param lifeRelEmp Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<LifeRelEmp> lifeRelEmp, TitaVo... titaVo) throws DBException;

}
