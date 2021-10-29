package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CustomerAmlRating;
import org.springframework.data.domain.Slice;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CustomerAmlRatingService {

  /**
   * findByPrimaryKey
   *
   * @param custId PK
   * @param titaVo Variable-Length Argument
   * @return CustomerAmlRating CustomerAmlRating
   */
  public CustomerAmlRating findById(String custId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CustomerAmlRating CustomerAmlRating of List
   */
  public Slice<CustomerAmlRating> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * hold By CustomerAmlRating
   * 
   * @param custId key
   * @param titaVo Variable-Length Argument
   * @return CustomerAmlRating CustomerAmlRating
   */
  public CustomerAmlRating holdById(String custId, TitaVo... titaVo);

  /**
   * hold By CustomerAmlRating
   * 
   * @param customerAmlRating key
   * @param titaVo Variable-Length Argument
   * @return CustomerAmlRating CustomerAmlRating
   */
  public CustomerAmlRating holdById(CustomerAmlRating customerAmlRating, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param customerAmlRating Entity
   * @param titaVo Variable-Length Argument
   * @return CustomerAmlRating Entity
   * @throws DBException exception
   */
  public CustomerAmlRating insert(CustomerAmlRating customerAmlRating, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param customerAmlRating Entity
   * @param titaVo Variable-Length Argument
   * @return CustomerAmlRating Entity
   * @throws DBException exception
   */
  public CustomerAmlRating update(CustomerAmlRating customerAmlRating, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param customerAmlRating Entity
   * @param titaVo Variable-Length Argument
   * @return CustomerAmlRating Entity
   * @throws DBException exception
   */
  public CustomerAmlRating update2(CustomerAmlRating customerAmlRating, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param customerAmlRating Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(CustomerAmlRating customerAmlRating, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param customerAmlRating Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<CustomerAmlRating> customerAmlRating, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param customerAmlRating Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<CustomerAmlRating> customerAmlRating, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param customerAmlRating Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<CustomerAmlRating> customerAmlRating, TitaVo... titaVo) throws DBException;

}
