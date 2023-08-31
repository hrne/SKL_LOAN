package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.LaLgtp;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.LaLgtpId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface LaLgtpService {

  /**
   * findByPrimaryKey
   *
   * @param laLgtpId PK
   * @param titaVo Variable-Length Argument
   * @return LaLgtp LaLgtp
   */
  public LaLgtp findById(LaLgtpId laLgtpId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice LaLgtp LaLgtp of List
   */
  public Slice<LaLgtp> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * hold By LaLgtp
   * 
   * @param laLgtpId key
   * @param titaVo Variable-Length Argument
   * @return LaLgtp LaLgtp
   */
  public LaLgtp holdById(LaLgtpId laLgtpId, TitaVo... titaVo);

  /**
   * hold By LaLgtp
   * 
   * @param laLgtp key
   * @param titaVo Variable-Length Argument
   * @return LaLgtp LaLgtp
   */
  public LaLgtp holdById(LaLgtp laLgtp, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param laLgtp Entity
   * @param titaVo Variable-Length Argument
   * @return LaLgtp Entity
   * @throws DBException exception
   */
  public LaLgtp insert(LaLgtp laLgtp, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param laLgtp Entity
   * @param titaVo Variable-Length Argument
   * @return LaLgtp Entity
   * @throws DBException exception
   */
  public LaLgtp update(LaLgtp laLgtp, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param laLgtp Entity
   * @param titaVo Variable-Length Argument
   * @return LaLgtp Entity
   * @throws DBException exception
   */
  public LaLgtp update2(LaLgtp laLgtp, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param laLgtp Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(LaLgtp laLgtp, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param laLgtp Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<LaLgtp> laLgtp, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param laLgtp Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<LaLgtp> laLgtp, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param laLgtp Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<LaLgtp> laLgtp, TitaVo... titaVo) throws DBException;

}
