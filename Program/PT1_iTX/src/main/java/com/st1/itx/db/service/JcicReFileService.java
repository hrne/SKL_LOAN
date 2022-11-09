package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicReFile;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicReFileId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicReFileService {

  /**
   * findByPrimaryKey
   *
   * @param jcicReFileId PK
   * @param titaVo Variable-Length Argument
   * @return JcicReFile JcicReFile
   */
  public JcicReFile findById(JcicReFileId jcicReFileId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicReFile JcicReFile of List
   */
  public Slice<JcicReFile> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * hold By JcicReFile
   * 
   * @param jcicReFileId key
   * @param titaVo Variable-Length Argument
   * @return JcicReFile JcicReFile
   */
  public JcicReFile holdById(JcicReFileId jcicReFileId, TitaVo... titaVo);

  /**
   * hold By JcicReFile
   * 
   * @param jcicReFile key
   * @param titaVo Variable-Length Argument
   * @return JcicReFile JcicReFile
   */
  public JcicReFile holdById(JcicReFile jcicReFile, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param jcicReFile Entity
   * @param titaVo Variable-Length Argument
   * @return JcicReFile Entity
   * @throws DBException exception
   */
  public JcicReFile insert(JcicReFile jcicReFile, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param jcicReFile Entity
   * @param titaVo Variable-Length Argument
   * @return JcicReFile Entity
   * @throws DBException exception
   */
  public JcicReFile update(JcicReFile jcicReFile, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param jcicReFile Entity
   * @param titaVo Variable-Length Argument
   * @return JcicReFile Entity
   * @throws DBException exception
   */
  public JcicReFile update2(JcicReFile jcicReFile, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param jcicReFile Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(JcicReFile jcicReFile, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param jcicReFile Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<JcicReFile> jcicReFile, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param jcicReFile Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<JcicReFile> jcicReFile, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param jcicReFile Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<JcicReFile> jcicReFile, TitaVo... titaVo) throws DBException;

}
