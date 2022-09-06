package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.ClOtherRights;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.ClOtherRightsId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface ClOtherRightsService {

	/**
	 * findByPrimaryKey
	 *
	 * @param clOtherRightsId PK
	 * @param titaVo          Variable-Length Argument
	 * @return ClOtherRights ClOtherRights
	 */
	public ClOtherRights findById(ClOtherRightsId clOtherRightsId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice ClOtherRights ClOtherRights of List
	 */
	public Slice<ClOtherRights> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * ClCode1 =
	 *
	 * @param clCode1_0 clCode1_0
	 * @param index     Page Index
	 * @param limit     Page Data Limit
	 * @param titaVo    Variable-Length Argument
	 * @return Slice ClOtherRights ClOtherRights of List
	 */
	public Slice<ClOtherRights> findClCode1(int clCode1_0, int index, int limit, TitaVo... titaVo);

	/**
	 * ClCode1 = ,AND ClCode2 =
	 *
	 * @param clCode1_0 clCode1_0
	 * @param clCode2_1 clCode2_1
	 * @param index     Page Index
	 * @param limit     Page Data Limit
	 * @param titaVo    Variable-Length Argument
	 * @return Slice ClOtherRights ClOtherRights of List
	 */
	public Slice<ClOtherRights> findClCode2(int clCode1_0, int clCode2_1, int index, int limit, TitaVo... titaVo);

	/**
	 * ClCode1 = ,AND ClCode2 = ,AND ClNo =
	 *
	 * @param clCode1_0 clCode1_0
	 * @param clCode2_1 clCode2_1
	 * @param clNo_2    clNo_2
	 * @param index     Page Index
	 * @param limit     Page Data Limit
	 * @param titaVo    Variable-Length Argument
	 * @return Slice ClOtherRights ClOtherRights of List
	 */
	public Slice<ClOtherRights> findClNo(int clCode1_0, int clCode2_1, int clNo_2, int index, int limit, TitaVo... titaVo);

	/**
	 * ClCode1 &gt;= ,AND ClCode1 &lt;= ,AND ClCode2 &gt;= ,AND ClCode2 &lt;= ,AND
	 * ClNo &gt;= ,AND ClNo &lt;=
	 *
	 * @param clCode1_0 clCode1_0
	 * @param clCode1_1 clCode1_1
	 * @param clCode2_2 clCode2_2
	 * @param clCode2_3 clCode2_3
	 * @param clNo_4    clNo_4
	 * @param clNo_5    clNo_5
	 * @param index     Page Index
	 * @param limit     Page Data Limit
	 * @param titaVo    Variable-Length Argument
	 * @return Slice ClOtherRights ClOtherRights of List
	 */
	public Slice<ClOtherRights> findClCodeRange(int clCode1_0, int clCode1_1, int clCode2_2, int clCode2_3, int clNo_4, int clNo_5, int index, int limit, TitaVo... titaVo);

	/**
	 * ChoiceDate = ,AND LastUpdateEmpNo =
	 *
	 * @param choiceDate_0      choiceDate_0
	 * @param lastUpdateEmpNo_1 lastUpdateEmpNo_1
	 * @param index             Page Index
	 * @param limit             Page Data Limit
	 * @param titaVo            Variable-Length Argument
	 * @return Slice ClOtherRights ClOtherRights of List
	 */
	public Slice<ClOtherRights> findChoiceDateEq(int choiceDate_0, String lastUpdateEmpNo_1, int index, int limit, TitaVo... titaVo);

	/**
	 * hold By ClOtherRights
	 * 
	 * @param clOtherRightsId key
	 * @param titaVo          Variable-Length Argument
	 * @return ClOtherRights ClOtherRights
	 */
	public ClOtherRights holdById(ClOtherRightsId clOtherRightsId, TitaVo... titaVo);

	/**
	 * hold By ClOtherRights
	 * 
	 * @param clOtherRights key
	 * @param titaVo        Variable-Length Argument
	 * @return ClOtherRights ClOtherRights
	 */
	public ClOtherRights holdById(ClOtherRights clOtherRights, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param clOtherRights Entity
	 * @param titaVo        Variable-Length Argument
	 * @return ClOtherRights Entity
	 * @throws DBException exception
	 */
	public ClOtherRights insert(ClOtherRights clOtherRights, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param clOtherRights Entity
	 * @param titaVo        Variable-Length Argument
	 * @return ClOtherRights Entity
	 * @throws DBException exception
	 */
	public ClOtherRights update(ClOtherRights clOtherRights, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param clOtherRights Entity
	 * @param titaVo        Variable-Length Argument
	 * @return ClOtherRights Entity
	 * @throws DBException exception
	 */
	public ClOtherRights update2(ClOtherRights clOtherRights, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param clOtherRights Entity
	 * @param titaVo        Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(ClOtherRights clOtherRights, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param clOtherRights Entity of List
	 * @param titaVo        Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<ClOtherRights> clOtherRights, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param clOtherRights Entity of List
	 * @param titaVo        Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<ClOtherRights> clOtherRights, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param clOtherRights Entity of List
	 * @param titaVo        Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<ClOtherRights> clOtherRights, TitaVo... titaVo) throws DBException;

}
