package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.ConstructionCompany;
import org.springframework.data.domain.Slice;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface ConstructionCompanyService {

  /**
   * findByPrimaryKey
   *
   * @param custNo PK
   * @param titaVo Variable-Length Argument
   * @return ConstructionCompany ConstructionCompany
   */
  public ConstructionCompany findById(int custNo, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice ConstructionCompany ConstructionCompany of List
   */
  public Slice<ConstructionCompany> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * hold By ConstructionCompany
   * 
   * @param custNo key
   * @param titaVo Variable-Length Argument
   * @return ConstructionCompany ConstructionCompany
   */
  public ConstructionCompany holdById(int custNo, TitaVo... titaVo);

  /**
   * hold By ConstructionCompany
   * 
   * @param constructionCompany key
   * @param titaVo Variable-Length Argument
   * @return ConstructionCompany ConstructionCompany
   */
  public ConstructionCompany holdById(ConstructionCompany constructionCompany, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param constructionCompany Entity
   * @param titaVo Variable-Length Argument
   * @return ConstructionCompany Entity
   * @throws DBException exception
   */
  public ConstructionCompany insert(ConstructionCompany constructionCompany, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param constructionCompany Entity
   * @param titaVo Variable-Length Argument
   * @return ConstructionCompany Entity
   * @throws DBException exception
   */
  public ConstructionCompany update(ConstructionCompany constructionCompany, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param constructionCompany Entity
   * @param titaVo Variable-Length Argument
   * @return ConstructionCompany Entity
   * @throws DBException exception
   */
  public ConstructionCompany update2(ConstructionCompany constructionCompany, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param constructionCompany Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(ConstructionCompany constructionCompany, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param constructionCompany Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<ConstructionCompany> constructionCompany, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param constructionCompany Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<ConstructionCompany> constructionCompany, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param constructionCompany Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<ConstructionCompany> constructionCompany, TitaVo... titaVo) throws DBException;

}
