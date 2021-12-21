package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.ClNoMap;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.ClNoMapId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface ClNoMapService {

	/**
	 * findByPrimaryKey
	 *
	 * @param clNoMapId PK
	 * @param titaVo    Variable-Length Argument
	 * @return ClNoMap ClNoMap
	 */
	public ClNoMap findById(ClNoMapId clNoMapId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice ClNoMap ClNoMap of List
	 */
	public Slice<ClNoMap> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * GdrId1 = ,AND GdrId2 = ,AND GdrNum =
	 *
	 * @param gdrId1_0 gdrId1_0
	 * @param gdrId2_1 gdrId2_1
	 * @param gdrNum_2 gdrNum_2
	 * @param index    Page Index
	 * @param limit    Page Data Limit
	 * @param titaVo   Variable-Length Argument
	 * @return Slice ClNoMap ClNoMap of List
	 */
	public Slice<ClNoMap> findGdrNum(int gdrId1_0, int gdrId2_1, int gdrNum_2, int index, int limit, TitaVo... titaVo);

	/**
	 * MainGdrId1 = ,AND MainGdrId2 = ,AND MainGdrNum = ,AND MainLgtSeq =
	 *
	 * @param mainGdrId1_0 mainGdrId1_0
	 * @param mainGdrId2_1 mainGdrId2_1
	 * @param mainGdrNum_2 mainGdrNum_2
	 * @param mainLgtSeq_3 mainLgtSeq_3
	 * @param index        Page Index
	 * @param limit        Page Data Limit
	 * @param titaVo       Variable-Length Argument
	 * @return Slice ClNoMap ClNoMap of List
	 */
	public Slice<ClNoMap> findMainLgtseq(int mainGdrId1_0, int mainGdrId2_1, int mainGdrNum_2, int mainLgtSeq_3, int index, int limit, TitaVo... titaVo);

	/**
	 * MainGdrId1 = ,AND MainGdrId2 = ,AND MainGdrNum =
	 *
	 * @param mainGdrId1_0 mainGdrId1_0
	 * @param mainGdrId2_1 mainGdrId2_1
	 * @param mainGdrNum_2 mainGdrNum_2
	 * @param index        Page Index
	 * @param limit        Page Data Limit
	 * @param titaVo       Variable-Length Argument
	 * @return Slice ClNoMap ClNoMap of List
	 */
	public Slice<ClNoMap> findMainGdrNum(int mainGdrId1_0, int mainGdrId2_1, int mainGdrNum_2, int index, int limit, TitaVo... titaVo);

	/**
	 * ClCode1 = ,AND ClCode2 = ,AND ClNo =
	 *
	 * @param clCode1_0 clCode1_0
	 * @param clCode2_1 clCode2_1
	 * @param clNo_2    clNo_2
	 * @param index     Page Index
	 * @param limit     Page Data Limit
	 * @param titaVo    Variable-Length Argument
	 * @return Slice ClNoMap ClNoMap of List
	 */
	public Slice<ClNoMap> findNewClNo(int clCode1_0, int clCode2_1, int clNo_2, int index, int limit, TitaVo... titaVo);

	/**
	 * hold By ClNoMap
	 * 
	 * @param clNoMapId key
	 * @param titaVo    Variable-Length Argument
	 * @return ClNoMap ClNoMap
	 */
	public ClNoMap holdById(ClNoMapId clNoMapId, TitaVo... titaVo);

	/**
	 * hold By ClNoMap
	 * 
	 * @param clNoMap key
	 * @param titaVo  Variable-Length Argument
	 * @return ClNoMap ClNoMap
	 */
	public ClNoMap holdById(ClNoMap clNoMap, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param clNoMap Entity
	 * @param titaVo  Variable-Length Argument
	 * @return ClNoMap Entity
	 * @throws DBException exception
	 */
	public ClNoMap insert(ClNoMap clNoMap, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param clNoMap Entity
	 * @param titaVo  Variable-Length Argument
	 * @return ClNoMap Entity
	 * @throws DBException exception
	 */
	public ClNoMap update(ClNoMap clNoMap, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param clNoMap Entity
	 * @param titaVo  Variable-Length Argument
	 * @return ClNoMap Entity
	 * @throws DBException exception
	 */
	public ClNoMap update2(ClNoMap clNoMap, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param clNoMap Entity
	 * @param titaVo  Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(ClNoMap clNoMap, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param clNoMap Entity of List
	 * @param titaVo  Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<ClNoMap> clNoMap, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param clNoMap Entity of List
	 * @param titaVo  Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<ClNoMap> clNoMap, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param clNoMap Entity of List
	 * @param titaVo  Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<ClNoMap> clNoMap, TitaVo... titaVo) throws DBException;

}
