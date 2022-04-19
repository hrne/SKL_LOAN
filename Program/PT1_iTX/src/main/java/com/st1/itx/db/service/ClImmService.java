package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.ClImm;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.ClImmId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface ClImmService {

	/**
	 * findByPrimaryKey
	 *
	 * @param clImmId PK
	 * @param titaVo  Variable-Length Argument
	 * @return ClImm ClImm
	 */
	public ClImm findById(ClImmId clImmId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice ClImm ClImm of List
	 */
	public Slice<ClImm> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * ClCode1 =
	 *
	 * @param clCode1_0 clCode1_0
	 * @param index     Page Index
	 * @param limit     Page Data Limit
	 * @param titaVo    Variable-Length Argument
	 * @return Slice ClImm ClImm of List
	 */
	public Slice<ClImm> findClCode1(int clCode1_0, int index, int limit, TitaVo... titaVo);

	/**
	 * ClCode1 = ,AND ClCode2 =
	 *
	 * @param clCode1_0 clCode1_0
	 * @param clCode2_1 clCode2_1
	 * @param index     Page Index
	 * @param limit     Page Data Limit
	 * @param titaVo    Variable-Length Argument
	 * @return Slice ClImm ClImm of List
	 */
	public Slice<ClImm> findClCode2(int clCode1_0, int clCode2_1, int index, int limit, TitaVo... titaVo);

	/**
	 * SettingStat &gt;= ,AND SettingStat &lt;= ,AND ClStat &gt;= ,AND ClStat &lt;=
	 *
	 * @param settingStat_0 settingStat_0
	 * @param settingStat_1 settingStat_1
	 * @param clStat_2      clStat_2
	 * @param clStat_3      clStat_3
	 * @param index         Page Index
	 * @param limit         Page Data Limit
	 * @param titaVo        Variable-Length Argument
	 * @return Slice ClImm ClImm of List
	 */
	public Slice<ClImm> findRange(String settingStat_0, String settingStat_1, String clStat_2, String clStat_3, int index, int limit, TitaVo... titaVo);

	/**
	 * hold By ClImm
	 * 
	 * @param clImmId key
	 * @param titaVo  Variable-Length Argument
	 * @return ClImm ClImm
	 */
	public ClImm holdById(ClImmId clImmId, TitaVo... titaVo);

	/**
	 * hold By ClImm
	 * 
	 * @param clImm  key
	 * @param titaVo Variable-Length Argument
	 * @return ClImm ClImm
	 */
	public ClImm holdById(ClImm clImm, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param clImm  Entity
	 * @param titaVo Variable-Length Argument
	 * @return ClImm Entity
	 * @throws DBException exception
	 */
	public ClImm insert(ClImm clImm, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param clImm  Entity
	 * @param titaVo Variable-Length Argument
	 * @return ClImm Entity
	 * @throws DBException exception
	 */
	public ClImm update(ClImm clImm, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param clImm  Entity
	 * @param titaVo Variable-Length Argument
	 * @return ClImm Entity
	 * @throws DBException exception
	 */
	public ClImm update2(ClImm clImm, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param clImm  Entity
	 * @param titaVo Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(ClImm clImm, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param clImm  Entity of List
	 * @param titaVo Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<ClImm> clImm, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param clImm  Entity of List
	 * @param titaVo Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<ClImm> clImm, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param clImm  Entity of List
	 * @param titaVo Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<ClImm> clImm, TitaVo... titaVo) throws DBException;

}
