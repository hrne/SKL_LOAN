package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicZ068;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicZ068Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ068Service {

	/**
	 * findByPrimaryKey
	 *
	 * @param jcicZ068Id PK
	 * @param titaVo     Variable-Length Argument
	 * @return JcicZ068 JcicZ068
	 */
	public JcicZ068 findById(JcicZ068Id jcicZ068Id, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice JcicZ068 JcicZ068 of List
	 */
	public Slice<JcicZ068> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * CustId=
	 *
	 * @param custId_0 custId_0
	 * @param index    Page Index
	 * @param limit    Page Data Limit
	 * @param titaVo   Variable-Length Argument
	 * @return Slice JcicZ068 JcicZ068 of List
	 */
	public Slice<JcicZ068> CustIdEq(String custId_0, int index, int limit, TitaVo... titaVo);

	/**
	 * ApplyDate=
	 *
	 * @param applyDate_0 applyDate_0
	 * @param index       Page Index
	 * @param limit       Page Data Limit
	 * @param titaVo      Variable-Length Argument
	 * @return Slice JcicZ068 JcicZ068 of List
	 */
	public Slice<JcicZ068> RcDateEq(int applyDate_0, int index, int limit, TitaVo... titaVo);

	/**
	 * CustId= , AND ApplyDate=
	 *
	 * @param custId_0    custId_0
	 * @param applyDate_1 applyDate_1
	 * @param index       Page Index
	 * @param limit       Page Data Limit
	 * @param titaVo      Variable-Length Argument
	 * @return Slice JcicZ068 JcicZ068 of List
	 */
	public Slice<JcicZ068> CustRcEq(String custId_0, int applyDate_1, int index, int limit, TitaVo... titaVo);

	/**
	 * hold By JcicZ068
	 * 
	 * @param jcicZ068Id key
	 * @param titaVo     Variable-Length Argument
	 * @return JcicZ068 JcicZ068
	 */
	public JcicZ068 holdById(JcicZ068Id jcicZ068Id, TitaVo... titaVo);

	/**
	 * hold By JcicZ068
	 * 
	 * @param jcicZ068 key
	 * @param titaVo   Variable-Length Argument
	 * @return JcicZ068 JcicZ068
	 */
	public JcicZ068 holdById(JcicZ068 jcicZ068, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param jcicZ068 Entity
	 * @param titaVo   Variable-Length Argument
	 * @return JcicZ068 Entity
	 * @throws DBException exception
	 */
	public JcicZ068 insert(JcicZ068 jcicZ068, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param jcicZ068 Entity
	 * @param titaVo   Variable-Length Argument
	 * @return JcicZ068 Entity
	 * @throws DBException exception
	 */
	public JcicZ068 update(JcicZ068 jcicZ068, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param jcicZ068 Entity
	 * @param titaVo   Variable-Length Argument
	 * @return JcicZ068 Entity
	 * @throws DBException exception
	 */
	public JcicZ068 update2(JcicZ068 jcicZ068, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param jcicZ068 Entity
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(JcicZ068 jcicZ068, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param jcicZ068 Entity of List
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<JcicZ068> jcicZ068, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param jcicZ068 Entity of List
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<JcicZ068> jcicZ068, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param jcicZ068 Entity of List
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<JcicZ068> jcicZ068, TitaVo... titaVo) throws DBException;

}
