package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicZ575Log;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicZ575LogId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ575LogService {

	/**
	 * findByPrimaryKey
	 *
	 * @param jcicZ575LogId PK
	 * @param titaVo        Variable-Length Argument
	 * @return JcicZ575Log JcicZ575Log
	 */
	public JcicZ575Log findById(JcicZ575LogId jcicZ575LogId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice JcicZ575Log JcicZ575Log of List
	 */
	public Slice<JcicZ575Log> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * Ukey=
	 *
	 * @param ukey_0 ukey_0
	 * @param titaVo Variable-Length Argument
	 * @return Slice JcicZ575Log JcicZ575Log of List
	 */
	public JcicZ575Log ukeyFirst(String ukey_0, TitaVo... titaVo);

	/**
	 * Ukey=
	 *
	 * @param ukey_0 ukey_0
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice JcicZ575Log JcicZ575Log of List
	 */
	public Slice<JcicZ575Log> ukeyEq(String ukey_0, int index, int limit, TitaVo... titaVo);

	/**
	 * hold By JcicZ575Log
	 * 
	 * @param jcicZ575LogId key
	 * @param titaVo        Variable-Length Argument
	 * @return JcicZ575Log JcicZ575Log
	 */
	public JcicZ575Log holdById(JcicZ575LogId jcicZ575LogId, TitaVo... titaVo);

	/**
	 * hold By JcicZ575Log
	 * 
	 * @param jcicZ575Log key
	 * @param titaVo      Variable-Length Argument
	 * @return JcicZ575Log JcicZ575Log
	 */
	public JcicZ575Log holdById(JcicZ575Log jcicZ575Log, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param jcicZ575Log Entity
	 * @param titaVo      Variable-Length Argument
	 * @return JcicZ575Log Entity
	 * @throws DBException exception
	 */
	public JcicZ575Log insert(JcicZ575Log jcicZ575Log, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param jcicZ575Log Entity
	 * @param titaVo      Variable-Length Argument
	 * @return JcicZ575Log Entity
	 * @throws DBException exception
	 */
	public JcicZ575Log update(JcicZ575Log jcicZ575Log, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param jcicZ575Log Entity
	 * @param titaVo      Variable-Length Argument
	 * @return JcicZ575Log Entity
	 * @throws DBException exception
	 */
	public JcicZ575Log update2(JcicZ575Log jcicZ575Log, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param jcicZ575Log Entity
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(JcicZ575Log jcicZ575Log, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param jcicZ575Log Entity of List
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<JcicZ575Log> jcicZ575Log, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param jcicZ575Log Entity of List
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<JcicZ575Log> jcicZ575Log, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param jcicZ575Log Entity of List
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<JcicZ575Log> jcicZ575Log, TitaVo... titaVo) throws DBException;

}
