package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.TxToDoDetail;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.TxToDoDetailId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface TxToDoDetailService {

  /**
   * findByPrimaryKey
   *
   * @param txToDoDetailId PK
   * @param titaVo Variable-Length Argument
   * @return TxToDoDetail TxToDoDetail
   */
  public TxToDoDetail findById(TxToDoDetailId txToDoDetailId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice TxToDoDetail TxToDoDetail of List
   */
  public Slice<TxToDoDetail> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * ItemCode = ,AND Status &gt;= ,AND Status &lt;=
   *
   * @param itemCode_0 itemCode_0
   * @param status_1 status_1
   * @param status_2 status_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice TxToDoDetail TxToDoDetail of List
   */
  public Slice<TxToDoDetail> detailStatusRange(String itemCode_0, int status_1, int status_2, int index, int limit, TitaVo... titaVo);

  /**
   * ItemCode = ,AND DtlValue &gt;= ,AND DtlValue &lt;=
   *
   * @param itemCode_0 itemCode_0
   * @param dtlValue_1 dtlValue_1
   * @param dtlValue_2 dtlValue_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice TxToDoDetail TxToDoDetail of List
   */
  public Slice<TxToDoDetail> DtlValueRange(String itemCode_0, String dtlValue_1, String dtlValue_2, int index, int limit, TitaVo... titaVo);

  /**
   * ItemCode = ,AND DtlValue = ,AND Status &gt;= ,AND Status &lt;=  ,AND DataDate &gt;= ,AND DataDate &lt;=
   *
   * @param itemCode_0 itemCode_0
   * @param dtlValue_1 dtlValue_1
   * @param status_2 status_2
   * @param status_3 status_3
   * @param dataDate_4 dataDate_4
   * @param dataDate_5 dataDate_5
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice TxToDoDetail TxToDoDetail of List
   */
  public Slice<TxToDoDetail> itemCodeRange(String itemCode_0, String dtlValue_1, int status_2, int status_3, int dataDate_4, int dataDate_5, int index, int limit, TitaVo... titaVo);

  /**
   * ItemCode = ,AND Status &gt;= ,AND Status &lt;=  ,AND DataDate &gt;= ,AND DataDate &lt;=
   *
   * @param itemCode_0 itemCode_0
   * @param status_1 status_1
   * @param status_2 status_2
   * @param dataDate_3 dataDate_3
   * @param dataDate_4 dataDate_4
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice TxToDoDetail TxToDoDetail of List
   */
  public Slice<TxToDoDetail> DataDateRange(String itemCode_0, int status_1, int status_2, int dataDate_3, int dataDate_4, int index, int limit, TitaVo... titaVo);

  /**
   * ItemCode = ,AND TitaEntdy = ,AND TitaKinbr = ,AND TitaTlrNo = ,AND TitaTxtNo = 
   *
   * @param itemCode_0 itemCode_0
   * @param titaEntdy_1 titaEntdy_1
   * @param titaKinbr_2 titaKinbr_2
   * @param titaTlrNo_3 titaTlrNo_3
   * @param titaTxtNo_4 titaTxtNo_4
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice TxToDoDetail TxToDoDetail of List
   */
  public Slice<TxToDoDetail> findTxNoEq(String itemCode_0, int titaEntdy_1, String titaKinbr_2, String titaTlrNo_3, int titaTxtNo_4, int index, int limit, TitaVo... titaVo);

  /**
   * hold By TxToDoDetail
   * 
   * @param txToDoDetailId key
   * @param titaVo Variable-Length Argument
   * @return TxToDoDetail TxToDoDetail
   */
  public TxToDoDetail holdById(TxToDoDetailId txToDoDetailId, TitaVo... titaVo);

  /**
   * hold By TxToDoDetail
   * 
   * @param txToDoDetail key
   * @param titaVo Variable-Length Argument
   * @return TxToDoDetail TxToDoDetail
   */
  public TxToDoDetail holdById(TxToDoDetail txToDoDetail, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param txToDoDetail Entity
   * @param titaVo Variable-Length Argument
   * @return TxToDoDetail Entity
   * @throws DBException exception
   */
  public TxToDoDetail insert(TxToDoDetail txToDoDetail, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param txToDoDetail Entity
   * @param titaVo Variable-Length Argument
   * @return TxToDoDetail Entity
   * @throws DBException exception
   */
  public TxToDoDetail update(TxToDoDetail txToDoDetail, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param txToDoDetail Entity
   * @param titaVo Variable-Length Argument
   * @return TxToDoDetail Entity
   * @throws DBException exception
   */
  public TxToDoDetail update2(TxToDoDetail txToDoDetail, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param txToDoDetail Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(TxToDoDetail txToDoDetail, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param txToDoDetail Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<TxToDoDetail> txToDoDetail, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param txToDoDetail Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<TxToDoDetail> txToDoDetail, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param txToDoDetail Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<TxToDoDetail> txToDoDetail, TitaVo... titaVo) throws DBException;

}
