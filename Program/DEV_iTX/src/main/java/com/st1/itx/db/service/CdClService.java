package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CdCl;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.CdClId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CdClService {

  /**
   * findByPrimaryKey
   *
   * @param cdClId PK
   * @param titaVo Variable-Length Argument
   * @return CdCl CdCl
   */
  public CdCl findById(CdClId cdClId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdCl CdCl of List
   */
  public Slice<CdCl> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * ClCode1 &gt;= ,AND ClCode1 &lt;=
   *
   * @param clCode1_0 clCode1_0
   * @param clCode1_1 clCode1_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdCl CdCl of List
   */
  public Slice<CdCl> clCode1Eq(int clCode1_0, int clCode1_1, int index, int limit, TitaVo... titaVo);

  /**
   * ClTypeJCIC &gt;= ,AND ClTypeJCIC &lt;=
   *
   * @param clTypeJCIC_0 clTypeJCIC_0
   * @param clTypeJCIC_1 clTypeJCIC_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdCl CdCl of List
   */
  public Slice<CdCl> clTypeJCICEq(String clTypeJCIC_0, String clTypeJCIC_1, int index, int limit, TitaVo... titaVo);

  /**
   * ClCode1 &gt;= ,AND ClCode1 &lt;= ,AND ClCode2 &gt;= ,AND ClCode2 &lt;= 
   *
   * @param clCode1_0 clCode1_0
   * @param clCode1_1 clCode1_1
   * @param clCode2_2 clCode2_2
   * @param clCode2_3 clCode2_3
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdCl CdCl of List
   */
  public Slice<CdCl> clCode1Range(int clCode1_0, int clCode1_1, int clCode2_2, int clCode2_3, int index, int limit, TitaVo... titaVo);

  /**
   * hold By CdCl
   * 
   * @param cdClId key
   * @param titaVo Variable-Length Argument
   * @return CdCl CdCl
   */
  public CdCl holdById(CdClId cdClId, TitaVo... titaVo);

  /**
   * hold By CdCl
   * 
   * @param cdCl key
   * @param titaVo Variable-Length Argument
   * @return CdCl CdCl
   */
  public CdCl holdById(CdCl cdCl, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param cdCl Entity
   * @param titaVo Variable-Length Argument
   * @return CdCl Entity
   * @throws DBException exception
   */
  public CdCl insert(CdCl cdCl, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param cdCl Entity
   * @param titaVo Variable-Length Argument
   * @return CdCl Entity
   * @throws DBException exception
   */
  public CdCl update(CdCl cdCl, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param cdCl Entity
   * @param titaVo Variable-Length Argument
   * @return CdCl Entity
   * @throws DBException exception
   */
  public CdCl update2(CdCl cdCl, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param cdCl Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(CdCl cdCl, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param cdCl Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<CdCl> cdCl, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param cdCl Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<CdCl> cdCl, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param cdCl Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<CdCl> cdCl, TitaVo... titaVo) throws DBException;

}
