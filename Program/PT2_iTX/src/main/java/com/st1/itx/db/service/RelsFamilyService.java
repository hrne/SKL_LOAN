package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.RelsFamily;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.RelsFamilyId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface RelsFamilyService {

	/**
	 * findByPrimaryKey
	 *
	 * @param relsFamilyId PK
	 * @param titaVo       Variable-Length Argument
	 * @return RelsFamily RelsFamily
	 */
	public RelsFamily findById(RelsFamilyId relsFamilyId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice RelsFamily RelsFamily of List
	 */
	public Slice<RelsFamily> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * RelsUKey =
	 *
	 * @param relsUKey_0 relsUKey_0
	 * @param index      Page Index
	 * @param limit      Page Data Limit
	 * @param titaVo     Variable-Length Argument
	 * @return Slice RelsFamily RelsFamily of List
	 */
	public Slice<RelsFamily> RelsUKeyEq(String relsUKey_0, int index, int limit, TitaVo... titaVo);

	/**
	 * RelsUKey =
	 *
	 * @param relsUKey_0 relsUKey_0
	 * @param titaVo     Variable-Length Argument
	 * @return Slice RelsFamily RelsFamily of List
	 */
	public RelsFamily maxRelsSeqFirst(String relsUKey_0, TitaVo... titaVo);

	/**
	 * FamilyId =
	 *
	 * @param familyId_0 familyId_0
	 * @param index      Page Index
	 * @param limit      Page Data Limit
	 * @param titaVo     Variable-Length Argument
	 * @return Slice RelsFamily RelsFamily of List
	 */
	public Slice<RelsFamily> findFamilyIdEq(String familyId_0, int index, int limit, TitaVo... titaVo);

	/**
	 * FamilyName =
	 *
	 * @param familyName_0 familyName_0
	 * @param index        Page Index
	 * @param limit        Page Data Limit
	 * @param titaVo       Variable-Length Argument
	 * @return Slice RelsFamily RelsFamily of List
	 */
	public Slice<RelsFamily> findFamilyNameEq(String familyName_0, int index, int limit, TitaVo... titaVo);

	/**
	 * hold By RelsFamily
	 * 
	 * @param relsFamilyId key
	 * @param titaVo       Variable-Length Argument
	 * @return RelsFamily RelsFamily
	 */
	public RelsFamily holdById(RelsFamilyId relsFamilyId, TitaVo... titaVo);

	/**
	 * hold By RelsFamily
	 * 
	 * @param relsFamily key
	 * @param titaVo     Variable-Length Argument
	 * @return RelsFamily RelsFamily
	 */
	public RelsFamily holdById(RelsFamily relsFamily, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param relsFamily Entity
	 * @param titaVo     Variable-Length Argument
	 * @return RelsFamily Entity
	 * @throws DBException exception
	 */
	public RelsFamily insert(RelsFamily relsFamily, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param relsFamily Entity
	 * @param titaVo     Variable-Length Argument
	 * @return RelsFamily Entity
	 * @throws DBException exception
	 */
	public RelsFamily update(RelsFamily relsFamily, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param relsFamily Entity
	 * @param titaVo     Variable-Length Argument
	 * @return RelsFamily Entity
	 * @throws DBException exception
	 */
	public RelsFamily update2(RelsFamily relsFamily, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param relsFamily Entity
	 * @param titaVo     Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(RelsFamily relsFamily, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param relsFamily Entity of List
	 * @param titaVo     Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<RelsFamily> relsFamily, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param relsFamily Entity of List
	 * @param titaVo     Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<RelsFamily> relsFamily, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param relsFamily Entity of List
	 * @param titaVo     Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<RelsFamily> relsFamily, TitaVo... titaVo) throws DBException;

}
