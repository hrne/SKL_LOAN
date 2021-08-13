package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.PfBsOfficer;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.PfBsOfficerId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface PfBsOfficerService {

  /**
   * findByPrimaryKey
   *
   * @param pfBsOfficerId PK
   * @param titaVo Variable-Length Argument
   * @return PfBsOfficer PfBsOfficer
   */
  public PfBsOfficer findById(PfBsOfficerId pfBsOfficerId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice PfBsOfficer PfBsOfficer of List
   */
  public Slice<PfBsOfficer> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * WorkMonth= 
   *
   * @param workMonth_0 workMonth_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice PfBsOfficer PfBsOfficer of List
   */
  public Slice<PfBsOfficer> findByMonth(int workMonth_0, int index, int limit, TitaVo... titaVo);

  /**
   * WorkMonth&lt;= , AND WorkMonth&gt;= 
   *
   * @param workMonth_0 workMonth_0
   * @param workMonth_1 workMonth_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice PfBsOfficer PfBsOfficer of List
   */
  public Slice<PfBsOfficer> findBetween(int workMonth_0, int workMonth_1, int index, int limit, TitaVo... titaVo);

  /**
   * EmpNo= , AND WorkMonth=
   *
   * @param empNo_0 empNo_0
   * @param workMonth_1 workMonth_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice PfBsOfficer PfBsOfficer of List
   */
  public Slice<PfBsOfficer> findByEmpNoAndMonth(String empNo_0, int workMonth_1, int index, int limit, TitaVo... titaVo);

  /**
   * EmpNo= 
   *
   * @param empNo_0 empNo_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice PfBsOfficer PfBsOfficer of List
   */
  public Slice<PfBsOfficer> findByEmpNo(String empNo_0, int index, int limit, TitaVo... titaVo);

  /**
   * EmpNo= , AND WorkMonth&lt;= , AND WorkMonth&gt;= 
   *
   * @param empNo_0 empNo_0
   * @param workMonth_1 workMonth_1
   * @param workMonth_2 workMonth_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice PfBsOfficer PfBsOfficer of List
   */
  public Slice<PfBsOfficer> findByEmpNoAndRange(String empNo_0, int workMonth_1, int workMonth_2, int index, int limit, TitaVo... titaVo);

  /**
   * hold By PfBsOfficer
   * 
   * @param pfBsOfficerId key
   * @param titaVo Variable-Length Argument
   * @return PfBsOfficer PfBsOfficer
   */
  public PfBsOfficer holdById(PfBsOfficerId pfBsOfficerId, TitaVo... titaVo);

  /**
   * hold By PfBsOfficer
   * 
   * @param pfBsOfficer key
   * @param titaVo Variable-Length Argument
   * @return PfBsOfficer PfBsOfficer
   */
  public PfBsOfficer holdById(PfBsOfficer pfBsOfficer, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param pfBsOfficer Entity
   * @param titaVo Variable-Length Argument
   * @return PfBsOfficer Entity
   * @throws DBException exception
   */
  public PfBsOfficer insert(PfBsOfficer pfBsOfficer, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param pfBsOfficer Entity
   * @param titaVo Variable-Length Argument
   * @return PfBsOfficer Entity
   * @throws DBException exception
   */
  public PfBsOfficer update(PfBsOfficer pfBsOfficer, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param pfBsOfficer Entity
   * @param titaVo Variable-Length Argument
   * @return PfBsOfficer Entity
   * @throws DBException exception
   */
  public PfBsOfficer update2(PfBsOfficer pfBsOfficer, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param pfBsOfficer Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(PfBsOfficer pfBsOfficer, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param pfBsOfficer Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<PfBsOfficer> pfBsOfficer, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param pfBsOfficer Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<PfBsOfficer> pfBsOfficer, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param pfBsOfficer Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<PfBsOfficer> pfBsOfficer, TitaVo... titaVo) throws DBException;

}
