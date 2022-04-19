package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CdBranchGroup;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.CdBranchGroupId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CdBranchGroupService {

	/**
	 * findByPrimaryKey
	 *
	 * @param cdBranchGroupId PK
	 * @param titaVo          Variable-Length Argument
	 * @return CdBranchGroup CdBranchGroup
	 */
	public CdBranchGroup findById(CdBranchGroupId cdBranchGroupId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice CdBranchGroup CdBranchGroup of List
	 */
	public Slice<CdBranchGroup> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * BranchNo =
	 *
	 * @param branchNo_0 branchNo_0
	 * @param index      Page Index
	 * @param limit      Page Data Limit
	 * @param titaVo     Variable-Length Argument
	 * @return Slice CdBranchGroup CdBranchGroup of List
	 */
	public Slice<CdBranchGroup> findByBranchNo(String branchNo_0, int index, int limit, TitaVo... titaVo);

	/**
	 * hold By CdBranchGroup
	 * 
	 * @param cdBranchGroupId key
	 * @param titaVo          Variable-Length Argument
	 * @return CdBranchGroup CdBranchGroup
	 */
	public CdBranchGroup holdById(CdBranchGroupId cdBranchGroupId, TitaVo... titaVo);

	/**
	 * hold By CdBranchGroup
	 * 
	 * @param cdBranchGroup key
	 * @param titaVo        Variable-Length Argument
	 * @return CdBranchGroup CdBranchGroup
	 */
	public CdBranchGroup holdById(CdBranchGroup cdBranchGroup, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param cdBranchGroup Entity
	 * @param titaVo        Variable-Length Argument
	 * @return CdBranchGroup Entity
	 * @throws DBException exception
	 */
	public CdBranchGroup insert(CdBranchGroup cdBranchGroup, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param cdBranchGroup Entity
	 * @param titaVo        Variable-Length Argument
	 * @return CdBranchGroup Entity
	 * @throws DBException exception
	 */
	public CdBranchGroup update(CdBranchGroup cdBranchGroup, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param cdBranchGroup Entity
	 * @param titaVo        Variable-Length Argument
	 * @return CdBranchGroup Entity
	 * @throws DBException exception
	 */
	public CdBranchGroup update2(CdBranchGroup cdBranchGroup, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param cdBranchGroup Entity
	 * @param titaVo        Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(CdBranchGroup cdBranchGroup, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param cdBranchGroup Entity of List
	 * @param titaVo        Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<CdBranchGroup> cdBranchGroup, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param cdBranchGroup Entity of List
	 * @param titaVo        Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<CdBranchGroup> cdBranchGroup, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param cdBranchGroup Entity of List
	 * @param titaVo        Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<CdBranchGroup> cdBranchGroup, TitaVo... titaVo) throws DBException;

}
