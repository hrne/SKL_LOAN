package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicZ064;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicZ064Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ064Service {

	/**
	 * findByPrimaryKey
	 *
	 * @param jcicZ064Id PK
	 * @param titaVo     Variable-Length Argument
	 * @return JcicZ064 JcicZ064
	 */
	public JcicZ064 findById(JcicZ064Id jcicZ064Id, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice JcicZ064 JcicZ064 of List
	 */
	public Slice<JcicZ064> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * CustId=
	 *
	 * @param custId_0 custId_0
	 * @param index    Page Index
	 * @param limit    Page Data Limit
	 * @param titaVo   Variable-Length Argument
	 * @return Slice JcicZ064 JcicZ064 of List
	 */
	public Slice<JcicZ064> CustIdEq(String custId_0, int index, int limit, TitaVo... titaVo);

	/**
	 * ApplyDate=
	 *
	 * @param applyDate_0 applyDate_0
	 * @param index       Page Index
	 * @param limit       Page Data Limit
	 * @param titaVo      Variable-Length Argument
	 * @return Slice JcicZ064 JcicZ064 of List
	 */
	public Slice<JcicZ064> RcDateEq(int applyDate_0, int index, int limit, TitaVo... titaVo);

	/**
	 * CustId= , AND ApplyDate=
	 *
	 * @param custId_0    custId_0
	 * @param applyDate_1 applyDate_1
	 * @param index       Page Index
	 * @param limit       Page Data Limit
	 * @param titaVo      Variable-Length Argument
	 * @return Slice JcicZ064 JcicZ064 of List
	 */
	public Slice<JcicZ064> CustRcEq(String custId_0, int applyDate_1, int index, int limit, TitaVo... titaVo);

	/**
	 * hold By JcicZ064
	 * 
	 * @param jcicZ064Id key
	 * @param titaVo     Variable-Length Argument
	 * @return JcicZ064 JcicZ064
	 */
	public JcicZ064 holdById(JcicZ064Id jcicZ064Id, TitaVo... titaVo);

	/**
	 * hold By JcicZ064
	 * 
	 * @param jcicZ064 key
	 * @param titaVo   Variable-Length Argument
	 * @return JcicZ064 JcicZ064
	 */
	public JcicZ064 holdById(JcicZ064 jcicZ064, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param jcicZ064 Entity
	 * @param titaVo   Variable-Length Argument
	 * @return JcicZ064 Entity
	 * @throws DBException exception
	 */
	public JcicZ064 insert(JcicZ064 jcicZ064, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param jcicZ064 Entity
	 * @param titaVo   Variable-Length Argument
	 * @return JcicZ064 Entity
	 * @throws DBException exception
	 */
	public JcicZ064 update(JcicZ064 jcicZ064, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param jcicZ064 Entity
	 * @param titaVo   Variable-Length Argument
	 * @return JcicZ064 Entity
	 * @throws DBException exception
	 */
	public JcicZ064 update2(JcicZ064 jcicZ064, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param jcicZ064 Entity
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(JcicZ064 jcicZ064, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param jcicZ064 Entity of List
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<JcicZ064> jcicZ064, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param jcicZ064 Entity of List
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<JcicZ064> jcicZ064, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param jcicZ064 Entity of List
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<JcicZ064> jcicZ064, TitaVo... titaVo) throws DBException;

}
