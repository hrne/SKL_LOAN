package com.st1.itx.db.service;

import java.util.List;

import java.math.BigDecimal;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.MlaundryDetail;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.MlaundryDetailId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface MlaundryDetailService {

  /**
   * findByPrimaryKey
   *
   * @param mlaundryDetailId PK
   * @param titaVo Variable-Length Argument
   * @return MlaundryDetail MlaundryDetail
   */
  public MlaundryDetail findById(MlaundryDetailId mlaundryDetailId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice MlaundryDetail MlaundryDetail of List
   */
  public Slice<MlaundryDetail> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * EntryDate &gt;= ,AND EntryDate &lt;= ,AND Rational ^i
   *
   * @param entryDate_0 entryDate_0
   * @param entryDate_1 entryDate_1
   * @param rational_2 rational_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice MlaundryDetail MlaundryDetail of List
   */
  public Slice<MlaundryDetail> findEntryDateRange(int entryDate_0, int entryDate_1, List<String> rational_2, int index, int limit, TitaVo... titaVo);

  /**
   * EntryDate &gt;= ,AND EntryDate &lt;= ,
   *
   * @param entryDate_0 entryDate_0
   * @param entryDate_1 entryDate_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice MlaundryDetail MlaundryDetail of List
   */
  public Slice<MlaundryDetail> findbyDate(int entryDate_0, int entryDate_1, int index, int limit, TitaVo... titaVo);

  /**
   * EntryDate &gt;= ,AND EntryDate &lt;= ,AND Factor = , AND CustNo = 
   *
   * @param entryDate_0 entryDate_0
   * @param entryDate_1 entryDate_1
   * @param factor_2 factor_2
   * @param custNo_3 custNo_3
   * @param titaVo Variable-Length Argument
   * @return Slice MlaundryDetail MlaundryDetail of List
   */
  public MlaundryDetail findEntryDateRangeFactorCustNoFirst(int entryDate_0, int entryDate_1, int factor_2, int custNo_3, TitaVo... titaVo);

  /**
   * hold By MlaundryDetail
   * 
   * @param mlaundryDetailId key
   * @param titaVo Variable-Length Argument
   * @return MlaundryDetail MlaundryDetail
   */
  public MlaundryDetail holdById(MlaundryDetailId mlaundryDetailId, TitaVo... titaVo);

  /**
   * hold By MlaundryDetail
   * 
   * @param mlaundryDetail key
   * @param titaVo Variable-Length Argument
   * @return MlaundryDetail MlaundryDetail
   */
  public MlaundryDetail holdById(MlaundryDetail mlaundryDetail, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param mlaundryDetail Entity
   * @param titaVo Variable-Length Argument
   * @return MlaundryDetail Entity
   * @throws DBException exception
   */
  public MlaundryDetail insert(MlaundryDetail mlaundryDetail, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param mlaundryDetail Entity
   * @param titaVo Variable-Length Argument
   * @return MlaundryDetail Entity
   * @throws DBException exception
   */
  public MlaundryDetail update(MlaundryDetail mlaundryDetail, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param mlaundryDetail Entity
   * @param titaVo Variable-Length Argument
   * @return MlaundryDetail Entity
   * @throws DBException exception
   */
  public MlaundryDetail update2(MlaundryDetail mlaundryDetail, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param mlaundryDetail Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(MlaundryDetail mlaundryDetail, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param mlaundryDetail Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<MlaundryDetail> mlaundryDetail, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param mlaundryDetail Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<MlaundryDetail> mlaundryDetail, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param mlaundryDetail Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<MlaundryDetail> mlaundryDetail, TitaVo... titaVo) throws DBException;

}
