package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.TxInquiry;
import org.springframework.data.domain.Slice;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface TxInquiryService {

  /**
   * findByPrimaryKey
   *
   * @param logNo PK
   * @param titaVo Variable-Length Argument
   * @return TxInquiry TxInquiry
   */
  public TxInquiry findById(Long logNo, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice TxInquiry TxInquiry of List
   */
  public Slice<TxInquiry> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * CalDate &gt;= ,AND CalDate &lt;= 
   *
   * @param calDate_0 calDate_0
   * @param calDate_1 calDate_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice TxInquiry TxInquiry of List
   */
  public Slice<TxInquiry> findByCalDate(int calDate_0, int calDate_1, int index, int limit, TitaVo... titaVo);

  /**
   * CalDate &gt;= ,AND CalDate &lt;= ,AND ImportFg = ,AND CustNo &gt;= ,AND CustNo &lt;=
   *
   * @param calDate_0 calDate_0
   * @param calDate_1 calDate_1
   * @param importFg_2 importFg_2
   * @param custNo_3 custNo_3
   * @param custNo_4 custNo_4
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice TxInquiry TxInquiry of List
   */
  public Slice<TxInquiry> findImportFg(int calDate_0, int calDate_1, String importFg_2, int custNo_3, int custNo_4, int index, int limit, TitaVo... titaVo);

  /**
   * Entdy &gt;= ,AND Entdy &lt;= ,AND ImportFg = ,AND CustNo &gt;= ,AND CustNo &lt;=
   *
   * @param entdy_0 entdy_0
   * @param entdy_1 entdy_1
   * @param importFg_2 importFg_2
   * @param custNo_3 custNo_3
   * @param custNo_4 custNo_4
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice TxInquiry TxInquiry of List
   */
  public Slice<TxInquiry> findEntdyImportFg(int entdy_0, int entdy_1, String importFg_2, int custNo_3, int custNo_4, int index, int limit, TitaVo... titaVo);

  /**
   * hold By TxInquiry
   * 
   * @param logNo key
   * @param titaVo Variable-Length Argument
   * @return TxInquiry TxInquiry
   */
  public TxInquiry holdById(Long logNo, TitaVo... titaVo);

  /**
   * hold By TxInquiry
   * 
   * @param txInquiry key
   * @param titaVo Variable-Length Argument
   * @return TxInquiry TxInquiry
   */
  public TxInquiry holdById(TxInquiry txInquiry, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param txInquiry Entity
   * @param titaVo Variable-Length Argument
   * @return TxInquiry Entity
   * @throws DBException exception
   */
  public TxInquiry insert(TxInquiry txInquiry, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param txInquiry Entity
   * @param titaVo Variable-Length Argument
   * @return TxInquiry Entity
   * @throws DBException exception
   */
  public TxInquiry update(TxInquiry txInquiry, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param txInquiry Entity
   * @param titaVo Variable-Length Argument
   * @return TxInquiry Entity
   * @throws DBException exception
   */
  public TxInquiry update2(TxInquiry txInquiry, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param txInquiry Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(TxInquiry txInquiry, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param txInquiry Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<TxInquiry> txInquiry, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param txInquiry Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<TxInquiry> txInquiry, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param txInquiry Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<TxInquiry> txInquiry, TitaVo... titaVo) throws DBException;

}
