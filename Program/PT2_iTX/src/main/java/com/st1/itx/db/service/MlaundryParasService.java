package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.MlaundryParas;
import org.springframework.data.domain.Slice;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface MlaundryParasService {

  /**
   * findByPrimaryKey
   *
   * @param businessType PK
   * @param titaVo Variable-Length Argument
   * @return MlaundryParas MlaundryParas
   */
  public MlaundryParas findById(String businessType, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice MlaundryParas MlaundryParas of List
   */
  public Slice<MlaundryParas> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * hold By MlaundryParas
   * 
   * @param businessType key
   * @param titaVo Variable-Length Argument
   * @return MlaundryParas MlaundryParas
   */
  public MlaundryParas holdById(String businessType, TitaVo... titaVo);

  /**
   * hold By MlaundryParas
   * 
   * @param mlaundryParas key
   * @param titaVo Variable-Length Argument
   * @return MlaundryParas MlaundryParas
   */
  public MlaundryParas holdById(MlaundryParas mlaundryParas, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param mlaundryParas Entity
   * @param titaVo Variable-Length Argument
   * @return MlaundryParas Entity
   * @throws DBException exception
   */
  public MlaundryParas insert(MlaundryParas mlaundryParas, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param mlaundryParas Entity
   * @param titaVo Variable-Length Argument
   * @return MlaundryParas Entity
   * @throws DBException exception
   */
  public MlaundryParas update(MlaundryParas mlaundryParas, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param mlaundryParas Entity
   * @param titaVo Variable-Length Argument
   * @return MlaundryParas Entity
   * @throws DBException exception
   */
  public MlaundryParas update2(MlaundryParas mlaundryParas, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param mlaundryParas Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(MlaundryParas mlaundryParas, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param mlaundryParas Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<MlaundryParas> mlaundryParas, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param mlaundryParas Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<MlaundryParas> mlaundryParas, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param mlaundryParas Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<MlaundryParas> mlaundryParas, TitaVo... titaVo) throws DBException;

}
