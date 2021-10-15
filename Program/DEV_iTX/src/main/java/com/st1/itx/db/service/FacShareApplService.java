package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.FacShareAppl;
import org.springframework.data.domain.Slice;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface FacShareApplService {

  /**
   * findByPrimaryKey
   *
   * @param applNo PK
   * @param titaVo Variable-Length Argument
   * @return FacShareAppl FacShareAppl
   */
  public FacShareAppl findById(int applNo, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice FacShareAppl FacShareAppl of List
   */
  public Slice<FacShareAppl> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * MainApplNo = 
   *
   * @param mainApplNo_0 mainApplNo_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice FacShareAppl FacShareAppl of List
   */
  public Slice<FacShareAppl> findMainApplNo(int mainApplNo_0, int index, int limit, TitaVo... titaVo);

  /**
   * CustNo = 
   *
   * @param custNo_0 custNo_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice FacShareAppl FacShareAppl of List
   */
  public Slice<FacShareAppl> findCustNoEq(int custNo_0, int index, int limit, TitaVo... titaVo);

  /**
   * MainApplNo = 
   *
   * @param mainApplNo_0 mainApplNo_0
   * @param titaVo Variable-Length Argument
   * @return Slice FacShareAppl FacShareAppl of List
   */
  public FacShareAppl mApplNoFirst(int mainApplNo_0, TitaVo... titaVo);

  /**
   * hold By FacShareAppl
   * 
   * @param applNo key
   * @param titaVo Variable-Length Argument
   * @return FacShareAppl FacShareAppl
   */
  public FacShareAppl holdById(int applNo, TitaVo... titaVo);

  /**
   * hold By FacShareAppl
   * 
   * @param facShareAppl key
   * @param titaVo Variable-Length Argument
   * @return FacShareAppl FacShareAppl
   */
  public FacShareAppl holdById(FacShareAppl facShareAppl, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param facShareAppl Entity
   * @param titaVo Variable-Length Argument
   * @return FacShareAppl Entity
   * @throws DBException exception
   */
  public FacShareAppl insert(FacShareAppl facShareAppl, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param facShareAppl Entity
   * @param titaVo Variable-Length Argument
   * @return FacShareAppl Entity
   * @throws DBException exception
   */
  public FacShareAppl update(FacShareAppl facShareAppl, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param facShareAppl Entity
   * @param titaVo Variable-Length Argument
   * @return FacShareAppl Entity
   * @throws DBException exception
   */
  public FacShareAppl update2(FacShareAppl facShareAppl, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param facShareAppl Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(FacShareAppl facShareAppl, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param facShareAppl Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<FacShareAppl> facShareAppl, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param facShareAppl Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<FacShareAppl> facShareAppl, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param facShareAppl Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<FacShareAppl> facShareAppl, TitaVo... titaVo) throws DBException;

}
