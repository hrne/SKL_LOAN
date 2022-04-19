package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.ReltFamily;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.ReltFamilyId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface ReltFamilyService {

	/**
	 * findByPrimaryKey
	 *
	 * @param reltFamilyId PK
	 * @param titaVo       Variable-Length Argument
	 * @return ReltFamily ReltFamily
	 */
	public ReltFamily findById(ReltFamilyId reltFamilyId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice ReltFamily ReltFamily of List
	 */
	public Slice<ReltFamily> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * ReltUKey =
	 *
	 * @param reltUKey_0 reltUKey_0
	 * @param index      Page Index
	 * @param limit      Page Data Limit
	 * @param titaVo     Variable-Length Argument
	 * @return Slice ReltFamily ReltFamily of List
	 */
	public Slice<ReltFamily> ReltUKeyEq(String reltUKey_0, int index, int limit, TitaVo... titaVo);

	/**
	 * ReltUKey =
	 *
	 * @param reltUKey_0 reltUKey_0
	 * @param titaVo     Variable-Length Argument
	 * @return Slice ReltFamily ReltFamily of List
	 */
	public ReltFamily maxReltSeqFirst(String reltUKey_0, TitaVo... titaVo);

	/**
	 * hold By ReltFamily
	 * 
	 * @param reltFamilyId key
	 * @param titaVo       Variable-Length Argument
	 * @return ReltFamily ReltFamily
	 */
	public ReltFamily holdById(ReltFamilyId reltFamilyId, TitaVo... titaVo);

	/**
	 * hold By ReltFamily
	 * 
	 * @param reltFamily key
	 * @param titaVo     Variable-Length Argument
	 * @return ReltFamily ReltFamily
	 */
	public ReltFamily holdById(ReltFamily reltFamily, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param reltFamily Entity
	 * @param titaVo     Variable-Length Argument
	 * @return ReltFamily Entity
	 * @throws DBException exception
	 */
	public ReltFamily insert(ReltFamily reltFamily, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param reltFamily Entity
	 * @param titaVo     Variable-Length Argument
	 * @return ReltFamily Entity
	 * @throws DBException exception
	 */
	public ReltFamily update(ReltFamily reltFamily, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param reltFamily Entity
	 * @param titaVo     Variable-Length Argument
	 * @return ReltFamily Entity
	 * @throws DBException exception
	 */
	public ReltFamily update2(ReltFamily reltFamily, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param reltFamily Entity
	 * @param titaVo     Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(ReltFamily reltFamily, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param reltFamily Entity of List
	 * @param titaVo     Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<ReltFamily> reltFamily, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param reltFamily Entity of List
	 * @param titaVo     Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<ReltFamily> reltFamily, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param reltFamily Entity of List
	 * @param titaVo     Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<ReltFamily> reltFamily, TitaVo... titaVo) throws DBException;

}
