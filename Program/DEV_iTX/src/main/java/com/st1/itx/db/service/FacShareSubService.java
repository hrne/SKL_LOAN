package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.FacShareSub;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.FacShareSubId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface FacShareSubService {

  /**
   * findByPrimaryKey
   *
   * @param facShareSubId PK
   * @param titaVo Variable-Length Argument
   * @return FacShareSub FacShareSub
   */
  public FacShareSub findById(FacShareSubId facShareSubId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice FacShareSub FacShareSub of List
   */
  public Slice<FacShareSub> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * CreditSysNo = ,AND MainCustNo = 
   *
   * @param creditSysNo_0 creditSysNo_0
   * @param mainCustNo_1 mainCustNo_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice FacShareSub FacShareSub of List
   */
  public Slice<FacShareSub> findMainIdEq(int creditSysNo_0, int mainCustNo_1, int index, int limit, TitaVo... titaVo);

  /**
   * ShareCustNo = ,AND ShareFacmNo &gt;= ,AND ShareFacmNo &lt;=
   *
   * @param shareCustNo_0 shareCustNo_0
   * @param shareFacmNo_1 shareFacmNo_1
   * @param shareFacmNo_2 shareFacmNo_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice FacShareSub FacShareSub of List
   */
  public Slice<FacShareSub> findCustNoEq(int shareCustNo_0, int shareFacmNo_1, int shareFacmNo_2, int index, int limit, TitaVo... titaVo);

  /**
   * MainCustNo = ,AND MainFacmNo = 
   *
   * @param mainCustNo_0 mainCustNo_0
   * @param mainFacmNo_1 mainFacmNo_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice FacShareSub FacShareSub of List
   */
  public Slice<FacShareSub> findMainFacmNoEq(int mainCustNo_0, int mainFacmNo_1, int index, int limit, TitaVo... titaVo);

  /**
   * hold By FacShareSub
   * 
   * @param facShareSubId key
   * @param titaVo Variable-Length Argument
   * @return FacShareSub FacShareSub
   */
  public FacShareSub holdById(FacShareSubId facShareSubId, TitaVo... titaVo);

  /**
   * hold By FacShareSub
   * 
   * @param facShareSub key
   * @param titaVo Variable-Length Argument
   * @return FacShareSub FacShareSub
   */
  public FacShareSub holdById(FacShareSub facShareSub, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param facShareSub Entity
   * @param titaVo Variable-Length Argument
   * @return FacShareSub Entity
   * @throws DBException exception
   */
  public FacShareSub insert(FacShareSub facShareSub, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param facShareSub Entity
   * @param titaVo Variable-Length Argument
   * @return FacShareSub Entity
   * @throws DBException exception
   */
  public FacShareSub update(FacShareSub facShareSub, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param facShareSub Entity
   * @param titaVo Variable-Length Argument
   * @return FacShareSub Entity
   * @throws DBException exception
   */
  public FacShareSub update2(FacShareSub facShareSub, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param facShareSub Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(FacShareSub facShareSub, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param facShareSub Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<FacShareSub> facShareSub, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param facShareSub Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<FacShareSub> facShareSub, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param facShareSub Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<FacShareSub> facShareSub, TitaVo... titaVo) throws DBException;

}
