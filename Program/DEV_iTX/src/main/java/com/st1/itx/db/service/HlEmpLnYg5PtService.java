package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.HlEmpLnYg5Pt;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.HlEmpLnYg5PtId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface HlEmpLnYg5PtService {

	/**
	 * findByPrimaryKey
	 *
	 * @param hlEmpLnYg5PtId PK
	 * @param titaVo         Variable-Length Argument
	 * @return HlEmpLnYg5Pt HlEmpLnYg5Pt
	 */
	public HlEmpLnYg5Pt findById(HlEmpLnYg5PtId hlEmpLnYg5PtId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice HlEmpLnYg5Pt HlEmpLnYg5Pt of List
	 */
	public Slice<HlEmpLnYg5Pt> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * WorkYM =
	 *
	 * @param workYM_0 workYM_0
	 * @param index    Page Index
	 * @param limit    Page Data Limit
	 * @param titaVo   Variable-Length Argument
	 * @return Slice HlEmpLnYg5Pt HlEmpLnYg5Pt of List
	 */
	public Slice<HlEmpLnYg5Pt> FindWorkYM(String workYM_0, int index, int limit, TitaVo... titaVo);

	/**
	 * AreaUnitNo =
	 *
	 * @param areaUnitNo_0 areaUnitNo_0
	 * @param index        Page Index
	 * @param limit        Page Data Limit
	 * @param titaVo       Variable-Length Argument
	 * @return Slice HlEmpLnYg5Pt HlEmpLnYg5Pt of List
	 */
	public Slice<HlEmpLnYg5Pt> FindAreaUnitNo(String areaUnitNo_0, int index, int limit, TitaVo... titaVo);

	/**
	 * HlEmpNo =
	 *
	 * @param hlEmpNo_0 hlEmpNo_0
	 * @param index     Page Index
	 * @param limit     Page Data Limit
	 * @param titaVo    Variable-Length Argument
	 * @return Slice HlEmpLnYg5Pt HlEmpLnYg5Pt of List
	 */
	public Slice<HlEmpLnYg5Pt> FindHlEmpNo(String hlEmpNo_0, int index, int limit, TitaVo... titaVo);

	/**
	 * hold By HlEmpLnYg5Pt
	 * 
	 * @param hlEmpLnYg5PtId key
	 * @param titaVo         Variable-Length Argument
	 * @return HlEmpLnYg5Pt HlEmpLnYg5Pt
	 */
	public HlEmpLnYg5Pt holdById(HlEmpLnYg5PtId hlEmpLnYg5PtId, TitaVo... titaVo);

	/**
	 * hold By HlEmpLnYg5Pt
	 * 
	 * @param hlEmpLnYg5Pt key
	 * @param titaVo       Variable-Length Argument
	 * @return HlEmpLnYg5Pt HlEmpLnYg5Pt
	 */
	public HlEmpLnYg5Pt holdById(HlEmpLnYg5Pt hlEmpLnYg5Pt, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param hlEmpLnYg5Pt Entity
	 * @param titaVo       Variable-Length Argument
	 * @return HlEmpLnYg5Pt Entity
	 * @throws DBException exception
	 */
	public HlEmpLnYg5Pt insert(HlEmpLnYg5Pt hlEmpLnYg5Pt, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param hlEmpLnYg5Pt Entity
	 * @param titaVo       Variable-Length Argument
	 * @return HlEmpLnYg5Pt Entity
	 * @throws DBException exception
	 */
	public HlEmpLnYg5Pt update(HlEmpLnYg5Pt hlEmpLnYg5Pt, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param hlEmpLnYg5Pt Entity
	 * @param titaVo       Variable-Length Argument
	 * @return HlEmpLnYg5Pt Entity
	 * @throws DBException exception
	 */
	public HlEmpLnYg5Pt update2(HlEmpLnYg5Pt hlEmpLnYg5Pt, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param hlEmpLnYg5Pt Entity
	 * @param titaVo       Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(HlEmpLnYg5Pt hlEmpLnYg5Pt, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param hlEmpLnYg5Pt Entity of List
	 * @param titaVo       Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<HlEmpLnYg5Pt> hlEmpLnYg5Pt, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param hlEmpLnYg5Pt Entity of List
	 * @param titaVo       Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<HlEmpLnYg5Pt> hlEmpLnYg5Pt, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param hlEmpLnYg5Pt Entity of List
	 * @param titaVo       Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<HlEmpLnYg5Pt> hlEmpLnYg5Pt, TitaVo... titaVo) throws DBException;

}
