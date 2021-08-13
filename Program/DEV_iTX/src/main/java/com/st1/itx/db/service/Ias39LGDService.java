package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.Ias39LGD;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.Ias39LGDId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface Ias39LGDService {

  /**
   * findByPrimaryKey
   *
   * @param ias39LGDId PK
   * @param titaVo Variable-Length Argument
   * @return Ias39LGD Ias39LGD
   */
  public Ias39LGD findById(Ias39LGDId ias39LGDId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice Ias39LGD Ias39LGD of List
   */
  public Slice<Ias39LGD> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * Type = ,AND Date &gt;= ,AND Date &lt;= 
   *
   * @param type_0 type_0
   * @param date_1 date_1
   * @param date_2 date_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice Ias39LGD Ias39LGD of List
   */
  public Slice<Ias39LGD> findDate(String type_0, int date_1, int date_2, int index, int limit, TitaVo... titaVo);

  /**
   * Date &gt;= ,AND Date &lt;= ,AND Type &gt;= ,AND Type &lt;= 
   *
   * @param date_0 date_0
   * @param date_1 date_1
   * @param type_2 type_2
   * @param type_3 type_3
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice Ias39LGD Ias39LGD of List
   */
  public Slice<Ias39LGD> findType(int date_0, int date_1, String type_2, String type_3, int index, int limit, TitaVo... titaVo);

  /**
   * hold By Ias39LGD
   * 
   * @param ias39LGDId key
   * @param titaVo Variable-Length Argument
   * @return Ias39LGD Ias39LGD
   */
  public Ias39LGD holdById(Ias39LGDId ias39LGDId, TitaVo... titaVo);

  /**
   * hold By Ias39LGD
   * 
   * @param ias39LGD key
   * @param titaVo Variable-Length Argument
   * @return Ias39LGD Ias39LGD
   */
  public Ias39LGD holdById(Ias39LGD ias39LGD, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param ias39LGD Entity
   * @param titaVo Variable-Length Argument
   * @return Ias39LGD Entity
   * @throws DBException exception
   */
  public Ias39LGD insert(Ias39LGD ias39LGD, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param ias39LGD Entity
   * @param titaVo Variable-Length Argument
   * @return Ias39LGD Entity
   * @throws DBException exception
   */
  public Ias39LGD update(Ias39LGD ias39LGD, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param ias39LGD Entity
   * @param titaVo Variable-Length Argument
   * @return Ias39LGD Entity
   * @throws DBException exception
   */
  public Ias39LGD update2(Ias39LGD ias39LGD, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param ias39LGD Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(Ias39LGD ias39LGD, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param ias39LGD Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<Ias39LGD> ias39LGD, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param ias39LGD Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<Ias39LGD> ias39LGD, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param ias39LGD Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<Ias39LGD> ias39LGD, TitaVo... titaVo) throws DBException;

}
