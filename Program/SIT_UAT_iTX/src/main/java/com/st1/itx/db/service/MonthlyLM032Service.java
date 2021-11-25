package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.MonthlyLM032;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.MonthlyLM032Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface MonthlyLM032Service {

  /**
   * findByPrimaryKey
   *
   * @param monthlyLM032Id PK
   * @param titaVo Variable-Length Argument
   * @return MonthlyLM032 MonthlyLM032
   */
  public MonthlyLM032 findById(MonthlyLM032Id monthlyLM032Id, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice MonthlyLM032 MonthlyLM032 of List
   */
  public Slice<MonthlyLM032> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * hold By MonthlyLM032
   * 
   * @param monthlyLM032Id key
   * @param titaVo Variable-Length Argument
   * @return MonthlyLM032 MonthlyLM032
   */
  public MonthlyLM032 holdById(MonthlyLM032Id monthlyLM032Id, TitaVo... titaVo);

  /**
   * hold By MonthlyLM032
   * 
   * @param monthlyLM032 key
   * @param titaVo Variable-Length Argument
   * @return MonthlyLM032 MonthlyLM032
   */
  public MonthlyLM032 holdById(MonthlyLM032 monthlyLM032, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param monthlyLM032 Entity
   * @param titaVo Variable-Length Argument
   * @return MonthlyLM032 Entity
   * @throws DBException exception
   */
  public MonthlyLM032 insert(MonthlyLM032 monthlyLM032, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param monthlyLM032 Entity
   * @param titaVo Variable-Length Argument
   * @return MonthlyLM032 Entity
   * @throws DBException exception
   */
  public MonthlyLM032 update(MonthlyLM032 monthlyLM032, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param monthlyLM032 Entity
   * @param titaVo Variable-Length Argument
   * @return MonthlyLM032 Entity
   * @throws DBException exception
   */
  public MonthlyLM032 update2(MonthlyLM032 monthlyLM032, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param monthlyLM032 Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(MonthlyLM032 monthlyLM032, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param monthlyLM032 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<MonthlyLM032> monthlyLM032, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param monthlyLM032 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<MonthlyLM032> monthlyLM032, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param monthlyLM032 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<MonthlyLM032> monthlyLM032, TitaVo... titaVo) throws DBException;

  /**
   * Stored Procedure<br>
   * (月底日日終批次)維護MonthlyLM032月報工作檔
   * @param  TBSDYF int
   * @param  empNo String
   * @param  LMBSDYF int
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_L9_MonthlyLM032_Upd(int TBSDYF, String empNo,int LMBSDYF, TitaVo... titaVo);

}
