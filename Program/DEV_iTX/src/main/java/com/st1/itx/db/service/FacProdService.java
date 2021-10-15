package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.FacProd;
import org.springframework.data.domain.Slice;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface FacProdService {

  /**
   * findByPrimaryKey
   *
   * @param prodNo PK
   * @param titaVo Variable-Length Argument
   * @return FacProd FacProd
   */
  public FacProd findById(String prodNo, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice FacProd FacProd of List
   */
  public Slice<FacProd> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * ProdNo %
   *
   * @param prodNo_0 prodNo_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice FacProd FacProd of List
   */
  public Slice<FacProd> prodNoLike(String prodNo_0, int index, int limit, TitaVo... titaVo);

  /**
   * ProdNo % ,AND StatusCode ^i
   *
   * @param prodNo_0 prodNo_0
   * @param statusCode_1 statusCode_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice FacProd FacProd of List
   */
  public Slice<FacProd> fildStatus(String prodNo_0, List<String> statusCode_1, int index, int limit, TitaVo... titaVo);

  /**
   * EnterpriseFg ^i
   *
   * @param enterpriseFg_0 enterpriseFg_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice FacProd FacProd of List
   */
  public Slice<FacProd> fildentCode(List<String> enterpriseFg_0, int index, int limit, TitaVo... titaVo);

  /**
   * ProdNo % ,AND StatusCode ^i ,AND EnterpriseFg ^i
   *
   * @param prodNo_0 prodNo_0
   * @param statusCode_1 statusCode_1
   * @param enterpriseFg_2 enterpriseFg_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice FacProd FacProd of List
   */
  public Slice<FacProd> fildProdNo(String prodNo_0, List<String> statusCode_1, List<String> enterpriseFg_2, int index, int limit, TitaVo... titaVo);

  /**
   * hold By FacProd
   * 
   * @param prodNo key
   * @param titaVo Variable-Length Argument
   * @return FacProd FacProd
   */
  public FacProd holdById(String prodNo, TitaVo... titaVo);

  /**
   * hold By FacProd
   * 
   * @param facProd key
   * @param titaVo Variable-Length Argument
   * @return FacProd FacProd
   */
  public FacProd holdById(FacProd facProd, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param facProd Entity
   * @param titaVo Variable-Length Argument
   * @return FacProd Entity
   * @throws DBException exception
   */
  public FacProd insert(FacProd facProd, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param facProd Entity
   * @param titaVo Variable-Length Argument
   * @return FacProd Entity
   * @throws DBException exception
   */
  public FacProd update(FacProd facProd, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param facProd Entity
   * @param titaVo Variable-Length Argument
   * @return FacProd Entity
   * @throws DBException exception
   */
  public FacProd update2(FacProd facProd, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param facProd Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(FacProd facProd, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param facProd Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<FacProd> facProd, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param facProd Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<FacProd> facProd, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param facProd Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<FacProd> facProd, TitaVo... titaVo) throws DBException;

}
