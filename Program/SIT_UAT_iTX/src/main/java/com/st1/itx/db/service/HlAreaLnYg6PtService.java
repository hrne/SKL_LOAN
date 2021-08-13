package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.HlAreaLnYg6Pt;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.HlAreaLnYg6PtId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface HlAreaLnYg6PtService {

  /**
   * findByPrimaryKey
   *
   * @param hlAreaLnYg6PtId PK
   * @param titaVo Variable-Length Argument
   * @return HlAreaLnYg6Pt HlAreaLnYg6Pt
   */
  public HlAreaLnYg6Pt findById(HlAreaLnYg6PtId hlAreaLnYg6PtId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice HlAreaLnYg6Pt HlAreaLnYg6Pt of List
   */
  public Slice<HlAreaLnYg6Pt> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * WorkYM = 
   *
   * @param workYM_0 workYM_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice HlAreaLnYg6Pt HlAreaLnYg6Pt of List
   */
  public Slice<HlAreaLnYg6Pt> FindWorkYM(String workYM_0, int index, int limit, TitaVo... titaVo);

  /**
   * AreaUnitNo = 
   *
   * @param areaUnitNo_0 areaUnitNo_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice HlAreaLnYg6Pt HlAreaLnYg6Pt of List
   */
  public Slice<HlAreaLnYg6Pt> FindAreaUnitNo(String areaUnitNo_0, int index, int limit, TitaVo... titaVo);

  /**
   * hold By HlAreaLnYg6Pt
   * 
   * @param hlAreaLnYg6PtId key
   * @param titaVo Variable-Length Argument
   * @return HlAreaLnYg6Pt HlAreaLnYg6Pt
   */
  public HlAreaLnYg6Pt holdById(HlAreaLnYg6PtId hlAreaLnYg6PtId, TitaVo... titaVo);

  /**
   * hold By HlAreaLnYg6Pt
   * 
   * @param hlAreaLnYg6Pt key
   * @param titaVo Variable-Length Argument
   * @return HlAreaLnYg6Pt HlAreaLnYg6Pt
   */
  public HlAreaLnYg6Pt holdById(HlAreaLnYg6Pt hlAreaLnYg6Pt, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param hlAreaLnYg6Pt Entity
   * @param titaVo Variable-Length Argument
   * @return HlAreaLnYg6Pt Entity
   * @throws DBException exception
   */
  public HlAreaLnYg6Pt insert(HlAreaLnYg6Pt hlAreaLnYg6Pt, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param hlAreaLnYg6Pt Entity
   * @param titaVo Variable-Length Argument
   * @return HlAreaLnYg6Pt Entity
   * @throws DBException exception
   */
  public HlAreaLnYg6Pt update(HlAreaLnYg6Pt hlAreaLnYg6Pt, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param hlAreaLnYg6Pt Entity
   * @param titaVo Variable-Length Argument
   * @return HlAreaLnYg6Pt Entity
   * @throws DBException exception
   */
  public HlAreaLnYg6Pt update2(HlAreaLnYg6Pt hlAreaLnYg6Pt, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param hlAreaLnYg6Pt Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(HlAreaLnYg6Pt hlAreaLnYg6Pt, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param hlAreaLnYg6Pt Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<HlAreaLnYg6Pt> hlAreaLnYg6Pt, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param hlAreaLnYg6Pt Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<HlAreaLnYg6Pt> hlAreaLnYg6Pt, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param hlAreaLnYg6Pt Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<HlAreaLnYg6Pt> hlAreaLnYg6Pt, TitaVo... titaVo) throws DBException;

}
