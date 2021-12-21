package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.ClBuildingPublic;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.ClBuildingPublicId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface ClBuildingPublicService {

	/**
	 * findByPrimaryKey
	 *
	 * @param clBuildingPublicId PK
	 * @param titaVo             Variable-Length Argument
	 * @return ClBuildingPublic ClBuildingPublic
	 */
	public ClBuildingPublic findById(ClBuildingPublicId clBuildingPublicId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice ClBuildingPublic ClBuildingPublic of List
	 */
	public Slice<ClBuildingPublic> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * ClCode1 = ,AND ClCode2 = ,AND ClNo =
	 *
	 * @param clCode1_0 clCode1_0
	 * @param clCode2_1 clCode2_1
	 * @param clNo_2    clNo_2
	 * @param index     Page Index
	 * @param limit     Page Data Limit
	 * @param titaVo    Variable-Length Argument
	 * @return Slice ClBuildingPublic ClBuildingPublic of List
	 */
	public Slice<ClBuildingPublic> clNoEq(int clCode1_0, int clCode2_1, int clNo_2, int index, int limit, TitaVo... titaVo);

	/**
	 * PublicBdNo1 =
	 *
	 * @param publicBdNo1_0 publicBdNo1_0
	 * @param index         Page Index
	 * @param limit         Page Data Limit
	 * @param titaVo        Variable-Length Argument
	 * @return Slice ClBuildingPublic ClBuildingPublic of List
	 */
	public Slice<ClBuildingPublic> publicBdNo1Eq(int publicBdNo1_0, int index, int limit, TitaVo... titaVo);

	/**
	 * PublicBdNo1 = ,AND PublicBdNo2 =
	 *
	 * @param publicBdNo1_0 publicBdNo1_0
	 * @param publicBdNo2_1 publicBdNo2_1
	 * @param index         Page Index
	 * @param limit         Page Data Limit
	 * @param titaVo        Variable-Length Argument
	 * @return Slice ClBuildingPublic ClBuildingPublic of List
	 */
	public Slice<ClBuildingPublic> publicBdNo2Eq(int publicBdNo1_0, int publicBdNo2_1, int index, int limit, TitaVo... titaVo);

	/**
	 * hold By ClBuildingPublic
	 * 
	 * @param clBuildingPublicId key
	 * @param titaVo             Variable-Length Argument
	 * @return ClBuildingPublic ClBuildingPublic
	 */
	public ClBuildingPublic holdById(ClBuildingPublicId clBuildingPublicId, TitaVo... titaVo);

	/**
	 * hold By ClBuildingPublic
	 * 
	 * @param clBuildingPublic key
	 * @param titaVo           Variable-Length Argument
	 * @return ClBuildingPublic ClBuildingPublic
	 */
	public ClBuildingPublic holdById(ClBuildingPublic clBuildingPublic, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param clBuildingPublic Entity
	 * @param titaVo           Variable-Length Argument
	 * @return ClBuildingPublic Entity
	 * @throws DBException exception
	 */
	public ClBuildingPublic insert(ClBuildingPublic clBuildingPublic, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param clBuildingPublic Entity
	 * @param titaVo           Variable-Length Argument
	 * @return ClBuildingPublic Entity
	 * @throws DBException exception
	 */
	public ClBuildingPublic update(ClBuildingPublic clBuildingPublic, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param clBuildingPublic Entity
	 * @param titaVo           Variable-Length Argument
	 * @return ClBuildingPublic Entity
	 * @throws DBException exception
	 */
	public ClBuildingPublic update2(ClBuildingPublic clBuildingPublic, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param clBuildingPublic Entity
	 * @param titaVo           Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(ClBuildingPublic clBuildingPublic, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param clBuildingPublic Entity of List
	 * @param titaVo           Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<ClBuildingPublic> clBuildingPublic, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param clBuildingPublic Entity of List
	 * @param titaVo           Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<ClBuildingPublic> clBuildingPublic, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param clBuildingPublic Entity of List
	 * @param titaVo           Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<ClBuildingPublic> clBuildingPublic, TitaVo... titaVo) throws DBException;

}
