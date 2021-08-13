package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicZ066;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicZ066Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ066Service {

	/**
	 * findByPrimaryKey
	 *
	 * @param jcicZ066Id PK
	 * @param titaVo     Variable-Length Argument
	 * @return JcicZ066 JcicZ066
	 */
	public JcicZ066 findById(JcicZ066Id jcicZ066Id, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice JcicZ066 JcicZ066 of List
	 */
	public Slice<JcicZ066> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * CustId=
	 *
	 * @param custId_0 custId_0
	 * @param index    Page Index
	 * @param limit    Page Data Limit
	 * @param titaVo   Variable-Length Argument
	 * @return Slice JcicZ066 JcicZ066 of List
	 */
	public Slice<JcicZ066> CustIdEq(String custId_0, int index, int limit, TitaVo... titaVo);

	/**
	 * ApplyDate=
	 *
	 * @param applyDate_0 applyDate_0
	 * @param index       Page Index
	 * @param limit       Page Data Limit
	 * @param titaVo      Variable-Length Argument
	 * @return Slice JcicZ066 JcicZ066 of List
	 */
	public Slice<JcicZ066> RcDateEq(int applyDate_0, int index, int limit, TitaVo... titaVo);

	/**
	 * CustId= , AND ApplyDate=
	 *
	 * @param custId_0    custId_0
	 * @param applyDate_1 applyDate_1
	 * @param index       Page Index
	 * @param limit       Page Data Limit
	 * @param titaVo      Variable-Length Argument
	 * @return Slice JcicZ066 JcicZ066 of List
	 */
	public Slice<JcicZ066> CustRcEq(String custId_0, int applyDate_1, int index, int limit, TitaVo... titaVo);

	/**
	 * hold By JcicZ066
	 * 
	 * @param jcicZ066Id key
	 * @param titaVo     Variable-Length Argument
	 * @return JcicZ066 JcicZ066
	 */
	public JcicZ066 holdById(JcicZ066Id jcicZ066Id, TitaVo... titaVo);

	/**
	 * hold By JcicZ066
	 * 
	 * @param jcicZ066 key
	 * @param titaVo   Variable-Length Argument
	 * @return JcicZ066 JcicZ066
	 */
	public JcicZ066 holdById(JcicZ066 jcicZ066, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param jcicZ066 Entity
	 * @param titaVo   Variable-Length Argument
	 * @return JcicZ066 Entity
	 * @throws DBException exception
	 */
	public JcicZ066 insert(JcicZ066 jcicZ066, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param jcicZ066 Entity
	 * @param titaVo   Variable-Length Argument
	 * @return JcicZ066 Entity
	 * @throws DBException exception
	 */
	public JcicZ066 update(JcicZ066 jcicZ066, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param jcicZ066 Entity
	 * @param titaVo   Variable-Length Argument
	 * @return JcicZ066 Entity
	 * @throws DBException exception
	 */
	public JcicZ066 update2(JcicZ066 jcicZ066, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param jcicZ066 Entity
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(JcicZ066 jcicZ066, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param jcicZ066 Entity of List
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<JcicZ066> jcicZ066, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param jcicZ066 Entity of List
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<JcicZ066> jcicZ066, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param jcicZ066 Entity of List
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<JcicZ066> jcicZ066, TitaVo... titaVo) throws DBException;

}
