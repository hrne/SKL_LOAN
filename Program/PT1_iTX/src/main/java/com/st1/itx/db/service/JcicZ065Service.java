package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicZ065;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicZ065Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ065Service {

	/**
	 * findByPrimaryKey
	 *
	 * @param jcicZ065Id PK
	 * @param titaVo     Variable-Length Argument
	 * @return JcicZ065 JcicZ065
	 */
	public JcicZ065 findById(JcicZ065Id jcicZ065Id, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice JcicZ065 JcicZ065 of List
	 */
	public Slice<JcicZ065> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * CustId=
	 *
	 * @param custId_0 custId_0
	 * @param index    Page Index
	 * @param limit    Page Data Limit
	 * @param titaVo   Variable-Length Argument
	 * @return Slice JcicZ065 JcicZ065 of List
	 */
	public Slice<JcicZ065> CustIdEq(String custId_0, int index, int limit, TitaVo... titaVo);

	/**
	 * ApplyDate=
	 *
	 * @param applyDate_0 applyDate_0
	 * @param index       Page Index
	 * @param limit       Page Data Limit
	 * @param titaVo      Variable-Length Argument
	 * @return Slice JcicZ065 JcicZ065 of List
	 */
	public Slice<JcicZ065> RcDateEq(int applyDate_0, int index, int limit, TitaVo... titaVo);

	/**
	 * CustId= , AND ApplyDate=
	 *
	 * @param custId_0    custId_0
	 * @param applyDate_1 applyDate_1
	 * @param index       Page Index
	 * @param limit       Page Data Limit
	 * @param titaVo      Variable-Length Argument
	 * @return Slice JcicZ065 JcicZ065 of List
	 */
	public Slice<JcicZ065> CustRcEq(String custId_0, int applyDate_1, int index, int limit, TitaVo... titaVo);

	/**
	 * hold By JcicZ065
	 * 
	 * @param jcicZ065Id key
	 * @param titaVo     Variable-Length Argument
	 * @return JcicZ065 JcicZ065
	 */
	public JcicZ065 holdById(JcicZ065Id jcicZ065Id, TitaVo... titaVo);

	/**
	 * hold By JcicZ065
	 * 
	 * @param jcicZ065 key
	 * @param titaVo   Variable-Length Argument
	 * @return JcicZ065 JcicZ065
	 */
	public JcicZ065 holdById(JcicZ065 jcicZ065, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param jcicZ065 Entity
	 * @param titaVo   Variable-Length Argument
	 * @return JcicZ065 Entity
	 * @throws DBException exception
	 */
	public JcicZ065 insert(JcicZ065 jcicZ065, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param jcicZ065 Entity
	 * @param titaVo   Variable-Length Argument
	 * @return JcicZ065 Entity
	 * @throws DBException exception
	 */
	public JcicZ065 update(JcicZ065 jcicZ065, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param jcicZ065 Entity
	 * @param titaVo   Variable-Length Argument
	 * @return JcicZ065 Entity
	 * @throws DBException exception
	 */
	public JcicZ065 update2(JcicZ065 jcicZ065, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param jcicZ065 Entity
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(JcicZ065 jcicZ065, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param jcicZ065 Entity of List
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<JcicZ065> jcicZ065, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param jcicZ065 Entity of List
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<JcicZ065> jcicZ065, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param jcicZ065 Entity of List
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<JcicZ065> jcicZ065, TitaVo... titaVo) throws DBException;

}
