package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicZ067;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicZ067Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ067Service {

	/**
	 * findByPrimaryKey
	 *
	 * @param jcicZ067Id PK
	 * @param titaVo     Variable-Length Argument
	 * @return JcicZ067 JcicZ067
	 */
	public JcicZ067 findById(JcicZ067Id jcicZ067Id, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice JcicZ067 JcicZ067 of List
	 */
	public Slice<JcicZ067> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * CustId=
	 *
	 * @param custId_0 custId_0
	 * @param index    Page Index
	 * @param limit    Page Data Limit
	 * @param titaVo   Variable-Length Argument
	 * @return Slice JcicZ067 JcicZ067 of List
	 */
	public Slice<JcicZ067> CustIdEq(String custId_0, int index, int limit, TitaVo... titaVo);

	/**
	 * ApplyDate=
	 *
	 * @param applyDate_0 applyDate_0
	 * @param index       Page Index
	 * @param limit       Page Data Limit
	 * @param titaVo      Variable-Length Argument
	 * @return Slice JcicZ067 JcicZ067 of List
	 */
	public Slice<JcicZ067> RcDateEq(int applyDate_0, int index, int limit, TitaVo... titaVo);

	/**
	 * CustId= , AND ApplyDate=
	 *
	 * @param custId_0    custId_0
	 * @param applyDate_1 applyDate_1
	 * @param index       Page Index
	 * @param limit       Page Data Limit
	 * @param titaVo      Variable-Length Argument
	 * @return Slice JcicZ067 JcicZ067 of List
	 */
	public Slice<JcicZ067> CustRcEq(String custId_0, int applyDate_1, int index, int limit, TitaVo... titaVo);

	/**
	 * hold By JcicZ067
	 * 
	 * @param jcicZ067Id key
	 * @param titaVo     Variable-Length Argument
	 * @return JcicZ067 JcicZ067
	 */
	public JcicZ067 holdById(JcicZ067Id jcicZ067Id, TitaVo... titaVo);

	/**
	 * hold By JcicZ067
	 * 
	 * @param jcicZ067 key
	 * @param titaVo   Variable-Length Argument
	 * @return JcicZ067 JcicZ067
	 */
	public JcicZ067 holdById(JcicZ067 jcicZ067, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param jcicZ067 Entity
	 * @param titaVo   Variable-Length Argument
	 * @return JcicZ067 Entity
	 * @throws DBException exception
	 */
	public JcicZ067 insert(JcicZ067 jcicZ067, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param jcicZ067 Entity
	 * @param titaVo   Variable-Length Argument
	 * @return JcicZ067 Entity
	 * @throws DBException exception
	 */
	public JcicZ067 update(JcicZ067 jcicZ067, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param jcicZ067 Entity
	 * @param titaVo   Variable-Length Argument
	 * @return JcicZ067 Entity
	 * @throws DBException exception
	 */
	public JcicZ067 update2(JcicZ067 jcicZ067, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param jcicZ067 Entity
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(JcicZ067 jcicZ067, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param jcicZ067 Entity of List
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<JcicZ067> jcicZ067, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param jcicZ067 Entity of List
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<JcicZ067> jcicZ067, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param jcicZ067 Entity of List
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<JcicZ067> jcicZ067, TitaVo... titaVo) throws DBException;

}
