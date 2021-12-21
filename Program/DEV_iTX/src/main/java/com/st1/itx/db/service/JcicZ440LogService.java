package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicZ440Log;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicZ440LogId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ440LogService {

	/**
	 * findByPrimaryKey
	 *
	 * @param jcicZ440LogId PK
	 * @param titaVo        Variable-Length Argument
	 * @return JcicZ440Log JcicZ440Log
	 */
	public JcicZ440Log findById(JcicZ440LogId jcicZ440LogId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice JcicZ440Log JcicZ440Log of List
	 */
	public Slice<JcicZ440Log> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * Ukey=
	 *
	 * @param ukey_0 ukey_0
	 * @param titaVo Variable-Length Argument
	 * @return Slice JcicZ440Log JcicZ440Log of List
	 */
	public JcicZ440Log ukeyFirst(String ukey_0, TitaVo... titaVo);

	/**
	 * Ukey=
	 *
	 * @param ukey_0 ukey_0
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice JcicZ440Log JcicZ440Log of List
	 */
	public Slice<JcicZ440Log> ukeyEq(String ukey_0, int index, int limit, TitaVo... titaVo);

	/**
	 * hold By JcicZ440Log
	 * 
	 * @param jcicZ440LogId key
	 * @param titaVo        Variable-Length Argument
	 * @return JcicZ440Log JcicZ440Log
	 */
	public JcicZ440Log holdById(JcicZ440LogId jcicZ440LogId, TitaVo... titaVo);

	/**
	 * hold By JcicZ440Log
	 * 
	 * @param jcicZ440Log key
	 * @param titaVo      Variable-Length Argument
	 * @return JcicZ440Log JcicZ440Log
	 */
	public JcicZ440Log holdById(JcicZ440Log jcicZ440Log, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param jcicZ440Log Entity
	 * @param titaVo      Variable-Length Argument
	 * @return JcicZ440Log Entity
	 * @throws DBException exception
	 */
	public JcicZ440Log insert(JcicZ440Log jcicZ440Log, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param jcicZ440Log Entity
	 * @param titaVo      Variable-Length Argument
	 * @return JcicZ440Log Entity
	 * @throws DBException exception
	 */
	public JcicZ440Log update(JcicZ440Log jcicZ440Log, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param jcicZ440Log Entity
	 * @param titaVo      Variable-Length Argument
	 * @return JcicZ440Log Entity
	 * @throws DBException exception
	 */
	public JcicZ440Log update2(JcicZ440Log jcicZ440Log, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param jcicZ440Log Entity
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(JcicZ440Log jcicZ440Log, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param jcicZ440Log Entity of List
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<JcicZ440Log> jcicZ440Log, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param jcicZ440Log Entity of List
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<JcicZ440Log> jcicZ440Log, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param jcicZ440Log Entity of List
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<JcicZ440Log> jcicZ440Log, TitaVo... titaVo) throws DBException;

}
