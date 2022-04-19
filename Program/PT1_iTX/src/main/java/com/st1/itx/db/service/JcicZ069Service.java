package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicZ069;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicZ069Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ069Service {

	/**
	 * findByPrimaryKey
	 *
	 * @param jcicZ069Id PK
	 * @param titaVo     Variable-Length Argument
	 * @return JcicZ069 JcicZ069
	 */
	public JcicZ069 findById(JcicZ069Id jcicZ069Id, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice JcicZ069 JcicZ069 of List
	 */
	public Slice<JcicZ069> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * CustId=
	 *
	 * @param custId_0 custId_0
	 * @param index    Page Index
	 * @param limit    Page Data Limit
	 * @param titaVo   Variable-Length Argument
	 * @return Slice JcicZ069 JcicZ069 of List
	 */
	public Slice<JcicZ069> CustIdEq(String custId_0, int index, int limit, TitaVo... titaVo);

	/**
	 * ApplyDate=
	 *
	 * @param applyDate_0 applyDate_0
	 * @param index       Page Index
	 * @param limit       Page Data Limit
	 * @param titaVo      Variable-Length Argument
	 * @return Slice JcicZ069 JcicZ069 of List
	 */
	public Slice<JcicZ069> RcDateEq(int applyDate_0, int index, int limit, TitaVo... titaVo);

	/**
	 * CustId= , AND ApplyDate=
	 *
	 * @param custId_0    custId_0
	 * @param applyDate_1 applyDate_1
	 * @param index       Page Index
	 * @param limit       Page Data Limit
	 * @param titaVo      Variable-Length Argument
	 * @return Slice JcicZ069 JcicZ069 of List
	 */
	public Slice<JcicZ069> CustRcEq(String custId_0, int applyDate_1, int index, int limit, TitaVo... titaVo);

	/**
	 * hold By JcicZ069
	 * 
	 * @param jcicZ069Id key
	 * @param titaVo     Variable-Length Argument
	 * @return JcicZ069 JcicZ069
	 */
	public JcicZ069 holdById(JcicZ069Id jcicZ069Id, TitaVo... titaVo);

	/**
	 * hold By JcicZ069
	 * 
	 * @param jcicZ069 key
	 * @param titaVo   Variable-Length Argument
	 * @return JcicZ069 JcicZ069
	 */
	public JcicZ069 holdById(JcicZ069 jcicZ069, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param jcicZ069 Entity
	 * @param titaVo   Variable-Length Argument
	 * @return JcicZ069 Entity
	 * @throws DBException exception
	 */
	public JcicZ069 insert(JcicZ069 jcicZ069, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param jcicZ069 Entity
	 * @param titaVo   Variable-Length Argument
	 * @return JcicZ069 Entity
	 * @throws DBException exception
	 */
	public JcicZ069 update(JcicZ069 jcicZ069, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param jcicZ069 Entity
	 * @param titaVo   Variable-Length Argument
	 * @return JcicZ069 Entity
	 * @throws DBException exception
	 */
	public JcicZ069 update2(JcicZ069 jcicZ069, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param jcicZ069 Entity
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(JcicZ069 jcicZ069, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param jcicZ069 Entity of List
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<JcicZ069> jcicZ069, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param jcicZ069 Entity of List
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<JcicZ069> jcicZ069, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param jcicZ069 Entity of List
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<JcicZ069> jcicZ069, TitaVo... titaVo) throws DBException;

}
