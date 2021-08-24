package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.FacMain;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.FacMainId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface FacMainService {

  /**
   * findByPrimaryKey
   *
   * @param facMainId PK
   * @param titaVo Variable-Length Argument
   * @return FacMain FacMain
   */
  public FacMain findById(FacMainId facMainId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice FacMain FacMain of List
   */
  public Slice<FacMain> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * CustNo &gt;= ,AND CustNo &lt;=  ,AND FacmNo &gt;= ,AND FacmNo &lt;= 
   *
   * @param custNo_0 custNo_0
   * @param custNo_1 custNo_1
   * @param facmNo_2 facmNo_2
   * @param facmNo_3 facmNo_3
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice FacMain FacMain of List
   */
  public Slice<FacMain> facmCustNoRange(int custNo_0, int custNo_1, int facmNo_2, int facmNo_3, int index, int limit, TitaVo... titaVo);

  /**
   * ApplNo &gt;= ,AND ApplNo &lt;= ,AND FacmNo &gt;= ,AND FacmNo &lt;= ,AND ColSetFlag %
   *
   * @param applNo_0 applNo_0
   * @param applNo_1 applNo_1
   * @param facmNo_2 facmNo_2
   * @param facmNo_3 facmNo_3
   * @param colSetFlag_4 colSetFlag_4
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice FacMain FacMain of List
   */
  public Slice<FacMain> facmApplNoRange(int applNo_0, int applNo_1, int facmNo_2, int facmNo_3, String colSetFlag_4, int index, int limit, TitaVo... titaVo);

  /**
   * ApplNo =
   *
   * @param applNo_0 applNo_0
   * @param titaVo Variable-Length Argument
   * @return Slice FacMain FacMain of List
   */
  public FacMain facmApplNoFirst(int applNo_0, TitaVo... titaVo);

  /**
   * CreditSysNo &gt;= ,AND CreditSysNo &lt;= ,AND FacmNo &gt;= ,AND FacmNo &lt;=
   *
   * @param creditSysNo_0 creditSysNo_0
   * @param creditSysNo_1 creditSysNo_1
   * @param facmNo_2 facmNo_2
   * @param facmNo_3 facmNo_3
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice FacMain FacMain of List
   */
  public Slice<FacMain> facmCreditSysNoRange(int creditSysNo_0, int creditSysNo_1, int facmNo_2, int facmNo_3, int index, int limit, TitaVo... titaVo);

  /**
   * CreditSysNo &gt;= ,AND CreditSysNo &lt;= ,AND FacmNo &gt;= ,AND FacmNo &lt;= 
   *
   * @param creditSysNo_0 creditSysNo_0
   * @param creditSysNo_1 creditSysNo_1
   * @param facmNo_2 facmNo_2
   * @param facmNo_3 facmNo_3
   * @param titaVo Variable-Length Argument
   * @return Slice FacMain FacMain of List
   */
  public FacMain facmCreditSysNoFirst(int creditSysNo_0, int creditSysNo_1, int facmNo_2, int facmNo_3, TitaVo... titaVo);

  /**
   * CreditOfficer &gt;= ,AND CreditOfficer &lt;= ,AND FacmNo &gt;= ,AND FacmNo &lt;=
   *
   * @param creditOfficer_0 creditOfficer_0
   * @param creditOfficer_1 creditOfficer_1
   * @param facmNo_2 facmNo_2
   * @param facmNo_3 facmNo_3
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice FacMain FacMain of List
   */
  public Slice<FacMain> facmCreditOfficerRange(String creditOfficer_0, String creditOfficer_1, int facmNo_2, int facmNo_3, int index, int limit, TitaVo... titaVo);

  /**
   * CustNo &gt;= ,AND CustNo &lt;= ,AND ColSetFlag %
   *
   * @param custNo_0 custNo_0
   * @param custNo_1 custNo_1
   * @param colSetFlag_2 colSetFlag_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice FacMain FacMain of List
   */
  public Slice<FacMain> fildCustNoRange(int custNo_0, int custNo_1, String colSetFlag_2, int index, int limit, TitaVo... titaVo);

  /**
   * BusinessOfficer &gt;= ,AND BusinessOfficer &lt;= ,AND FacmNo &gt;= ,AND FacmNo &lt;=
   *
   * @param businessOfficer_0 businessOfficer_0
   * @param businessOfficer_1 businessOfficer_1
   * @param facmNo_2 facmNo_2
   * @param facmNo_3 facmNo_3
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice FacMain FacMain of List
   */
  public Slice<FacMain> facmBusinessOfficerRange(String businessOfficer_0, String businessOfficer_1, int facmNo_2, int facmNo_3, int index, int limit, TitaVo... titaVo);

  /**
   * CustNo = ,AND CreditSysNo =
   *
   * @param custNo_0 custNo_0
   * @param creditSysNo_1 creditSysNo_1
   * @param titaVo Variable-Length Argument
   * @return Slice FacMain FacMain of List
   */
  public FacMain fildCustNoCreditSysNoFirst(int custNo_0, int creditSysNo_1, TitaVo... titaVo);

  /**
   * CustNo = 
   *
   * @param custNo_0 custNo_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice FacMain FacMain of List
   */
  public Slice<FacMain> CustNoAll(int custNo_0, int index, int limit, TitaVo... titaVo);

  /**
   * ProdNo =
   *
   * @param prodNo_0 prodNo_0
   * @param titaVo Variable-Length Argument
   * @return Slice FacMain FacMain of List
   */
  public FacMain findProdNoFirst(String prodNo_0, TitaVo... titaVo);

  /**
   * hold By FacMain
   * 
   * @param facMainId key
   * @param titaVo Variable-Length Argument
   * @return FacMain FacMain
   */
  public FacMain holdById(FacMainId facMainId, TitaVo... titaVo);

  /**
   * hold By FacMain
   * 
   * @param facMain key
   * @param titaVo Variable-Length Argument
   * @return FacMain FacMain
   */
  public FacMain holdById(FacMain facMain, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param facMain Entity
   * @param titaVo Variable-Length Argument
   * @return FacMain Entity
   * @throws DBException exception
   */
  public FacMain insert(FacMain facMain, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param facMain Entity
   * @param titaVo Variable-Length Argument
   * @return FacMain Entity
   * @throws DBException exception
   */
  public FacMain update(FacMain facMain, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param facMain Entity
   * @param titaVo Variable-Length Argument
   * @return FacMain Entity
   * @throws DBException exception
   */
  public FacMain update2(FacMain facMain, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param facMain Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(FacMain facMain, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param facMain Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<FacMain> facMain, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param facMain Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<FacMain> facMain, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param facMain Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<FacMain> facMain, TitaVo... titaVo) throws DBException;

}
