package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicZ570;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicZ570Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ570Service {

	/**
	 * findByPrimaryKey
	 *
	 * @param jcicZ570Id PK
	 * @param titaVo     Variable-Length Argument
	 * @return JcicZ570 JcicZ570
	 */
	public JcicZ570 findById(JcicZ570Id jcicZ570Id, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice JcicZ570 JcicZ570 of List
	 */
	public Slice<JcicZ570> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * CustId=
	 *
	 * @param custId_0 custId_0
	 * @param index    Page Index
	 * @param limit    Page Data Limit
	 * @param titaVo   Variable-Length Argument
	 * @return Slice JcicZ570 JcicZ570 of List
	 */
	public Slice<JcicZ570> custIdEq(String custId_0, int index, int limit, TitaVo... titaVo);

	/**
	 * ApplyDate=
	 *
	 * @param applyDate_0 applyDate_0
	 * @param index       Page Index
	 * @param limit       Page Data Limit
	 * @param titaVo      Variable-Length Argument
	 * @return Slice JcicZ570 JcicZ570 of List
	 */
	public Slice<JcicZ570> rcDateEq(int applyDate_0, int index, int limit, TitaVo... titaVo);

	/**
	 * CustId= , AND ApplyDate=
	 *
	 * @param custId_0    custId_0
	 * @param applyDate_1 applyDate_1
	 * @param index       Page Index
	 * @param limit       Page Data Limit
	 * @param titaVo      Variable-Length Argument
	 * @return Slice JcicZ570 JcicZ570 of List
	 */
	public Slice<JcicZ570> custRcEq(String custId_0, int applyDate_1, int index, int limit, TitaVo... titaVo);

	/**
	 * CustId=, AND ApplyDate = ,AND SubmitKey =
	 *
	 * @param custId_0    custId_0
	 * @param applyDate_1 applyDate_1
	 * @param submitKey_2 submitKey_2
	 * @param index       Page Index
	 * @param limit       Page Data Limit
	 * @param titaVo      Variable-Length Argument
	 * @return Slice JcicZ570 JcicZ570 of List
	 */
	public Slice<JcicZ570> otherEq(String custId_0, int applyDate_1, String submitKey_2, int index, int limit, TitaVo... titaVo);

	/**
	 * Ukey=
	 *
	 * @param ukey_0 ukey_0
	 * @param titaVo Variable-Length Argument
	 * @return Slice JcicZ570 JcicZ570 of List
	 */
	public JcicZ570 ukeyFirst(String ukey_0, TitaVo... titaVo);

	/**
	 * CustId=, AND ApplyDate = ,AND SubmitKey =
	 *
	 * @param custId_0    custId_0
	 * @param applyDate_1 applyDate_1
	 * @param submitKey_2 submitKey_2
	 * @param titaVo      Variable-Length Argument
	 * @return Slice JcicZ570 JcicZ570 of List
	 */
	public JcicZ570 otherFirst(String custId_0, int applyDate_1, String submitKey_2, TitaVo... titaVo);

	/**
	 * hold By JcicZ570
	 * 
	 * @param jcicZ570Id key
	 * @param titaVo     Variable-Length Argument
	 * @return JcicZ570 JcicZ570
	 */
	public JcicZ570 holdById(JcicZ570Id jcicZ570Id, TitaVo... titaVo);

	/**
	 * hold By JcicZ570
	 * 
	 * @param jcicZ570 key
	 * @param titaVo   Variable-Length Argument
	 * @return JcicZ570 JcicZ570
	 */
	public JcicZ570 holdById(JcicZ570 jcicZ570, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param jcicZ570 Entity
	 * @param titaVo   Variable-Length Argument
	 * @return JcicZ570 Entity
	 * @throws DBException exception
	 */
	public JcicZ570 insert(JcicZ570 jcicZ570, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param jcicZ570 Entity
	 * @param titaVo   Variable-Length Argument
	 * @return JcicZ570 Entity
	 * @throws DBException exception
	 */
	public JcicZ570 update(JcicZ570 jcicZ570, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param jcicZ570 Entity
	 * @param titaVo   Variable-Length Argument
	 * @return JcicZ570 Entity
	 * @throws DBException exception
	 */
	public JcicZ570 update2(JcicZ570 jcicZ570, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param jcicZ570 Entity
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(JcicZ570 jcicZ570, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param jcicZ570 Entity of List
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<JcicZ570> jcicZ570, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param jcicZ570 Entity of List
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<JcicZ570> jcicZ570, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param jcicZ570 Entity of List
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<JcicZ570> jcicZ570, TitaVo... titaVo) throws DBException;

}
