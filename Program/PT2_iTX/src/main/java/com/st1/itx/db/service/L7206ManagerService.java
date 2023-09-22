package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.L7206Manager;
import org.springframework.data.domain.Slice;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface L7206ManagerService {

  /**
   * findByPrimaryKey
   *
   * @param logNo PK
   * @param titaVo Variable-Length Argument
   * @return L7206Manager L7206Manager
   */
  public L7206Manager findById(Long logNo, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice L7206Manager L7206Manager of List
   */
  public Slice<L7206Manager> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * hold By L7206Manager
   * 
   * @param logNo key
   * @param titaVo Variable-Length Argument
   * @return L7206Manager L7206Manager
   */
  public L7206Manager holdById(Long logNo, TitaVo... titaVo);

  /**
   * hold By L7206Manager
   * 
   * @param l7206Manager key
   * @param titaVo Variable-Length Argument
   * @return L7206Manager L7206Manager
   */
  public L7206Manager holdById(L7206Manager l7206Manager, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param l7206Manager Entity
   * @param titaVo Variable-Length Argument
   * @return L7206Manager Entity
   * @throws DBException exception
   */
  public L7206Manager insert(L7206Manager l7206Manager, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param l7206Manager Entity
   * @param titaVo Variable-Length Argument
   * @return L7206Manager Entity
   * @throws DBException exception
   */
  public L7206Manager update(L7206Manager l7206Manager, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param l7206Manager Entity
   * @param titaVo Variable-Length Argument
   * @return L7206Manager Entity
   * @throws DBException exception
   */
  public L7206Manager update2(L7206Manager l7206Manager, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param l7206Manager Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(L7206Manager l7206Manager, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param l7206Manager Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<L7206Manager> l7206Manager, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param l7206Manager Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<L7206Manager> l7206Manager, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param l7206Manager Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<L7206Manager> l7206Manager, TitaVo... titaVo) throws DBException;

  /**
   * Stored Procedure<br>
   * 更新利害關係人負責人資料
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_L7_L7206Manager_Ins(String EmpNo, TitaVo... titaVo);

}
