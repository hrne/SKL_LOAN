package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.TxAuthorize;
import org.springframework.data.domain.Slice;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface TxAuthorizeService {

  /**
   * findByPrimaryKey
   *
   * @param autoSeq PK
   * @param titaVo Variable-Length Argument
   * @return TxAuthorize TxAuthorize
   */
  public TxAuthorize findById(Long autoSeq, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice TxAuthorize TxAuthorize of List
   */
  public Slice<TxAuthorize> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * Entdy = ,AND SupNo =
   *
   * @param entdy_0 entdy_0
   * @param supNo_1 supNo_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice TxAuthorize TxAuthorize of List
   */
  public Slice<TxAuthorize> findSupNo(int entdy_0, String supNo_1, int index, int limit, TitaVo... titaVo);

  /**
   * Entdy &gt;= ,AND Entdy &lt;=,AND SupNo %
   *
   * @param entdy_0 entdy_0
   * @param entdy_1 entdy_1
   * @param supNo_2 supNo_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice TxAuthorize TxAuthorize of List
   */
  public Slice<TxAuthorize> findEntdy(int entdy_0, int entdy_1, String supNo_2, int index, int limit, TitaVo... titaVo);

  /**
   * CreateDate&gt;=, AND CreateDate&lt;= ,AND SupNo %
   *
   * @param createDate_0 createDate_0
   * @param createDate_1 createDate_1
   * @param supNo_2 supNo_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice TxAuthorize TxAuthorize of List
   */
  public Slice<TxAuthorize> findCreatDate(java.sql.Timestamp createDate_0, java.sql.Timestamp createDate_1, String supNo_2, int index, int limit, TitaVo... titaVo);

  /**
   * Entdy &gt;= ,AND Entdy &lt;=,AND SupNo %
   *
   * @param entdy_0 entdy_0
   * @param entdy_1 entdy_1
   * @param supNo_2 supNo_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice TxAuthorize TxAuthorize of List
   */
  public Slice<TxAuthorize> findSupNoEntdy(int entdy_0, int entdy_1, String supNo_2, int index, int limit, TitaVo... titaVo);

  /**
   * hold By TxAuthorize
   * 
   * @param autoSeq key
   * @param titaVo Variable-Length Argument
   * @return TxAuthorize TxAuthorize
   */
  public TxAuthorize holdById(Long autoSeq, TitaVo... titaVo);

  /**
   * hold By TxAuthorize
   * 
   * @param txAuthorize key
   * @param titaVo Variable-Length Argument
   * @return TxAuthorize TxAuthorize
   */
  public TxAuthorize holdById(TxAuthorize txAuthorize, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param txAuthorize Entity
   * @param titaVo Variable-Length Argument
   * @return TxAuthorize Entity
   * @throws DBException exception
   */
  public TxAuthorize insert(TxAuthorize txAuthorize, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param txAuthorize Entity
   * @param titaVo Variable-Length Argument
   * @return TxAuthorize Entity
   * @throws DBException exception
   */
  public TxAuthorize update(TxAuthorize txAuthorize, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param txAuthorize Entity
   * @param titaVo Variable-Length Argument
   * @return TxAuthorize Entity
   * @throws DBException exception
   */
  public TxAuthorize update2(TxAuthorize txAuthorize, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param txAuthorize Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(TxAuthorize txAuthorize, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param txAuthorize Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<TxAuthorize> txAuthorize, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param txAuthorize Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<TxAuthorize> txAuthorize, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param txAuthorize Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<TxAuthorize> txAuthorize, TitaVo... titaVo) throws DBException;

}
