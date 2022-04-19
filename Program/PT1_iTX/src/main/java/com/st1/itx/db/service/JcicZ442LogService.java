package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicZ442Log;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicZ442LogId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ442LogService {

	/**
	 * findByPrimaryKey
	 *
	 * @param jcicZ442LogId PK
	 * @param titaVo        Variable-Length Argument
	 * @return JcicZ442Log JcicZ442Log
	 */
	public JcicZ442Log findById(JcicZ442LogId jcicZ442LogId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice JcicZ442Log JcicZ442Log of List
	 */
	public Slice<JcicZ442Log> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * Ukey=
	 *
	 * @param ukey_0 ukey_0
	 * @param titaVo Variable-Length Argument
	 * @return Slice JcicZ442Log JcicZ442Log of List
	 */
	public JcicZ442Log ukeyFirst(String ukey_0, TitaVo... titaVo);

	/**
	 * Ukey=
	 *
	 * @param ukey_0 ukey_0
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice JcicZ442Log JcicZ442Log of List
	 */
	public Slice<JcicZ442Log> ukeyEq(String ukey_0, int index, int limit, TitaVo... titaVo);

	/**
	 * hold By JcicZ442Log
	 * 
	 * @param jcicZ442LogId key
	 * @param titaVo        Variable-Length Argument
	 * @return JcicZ442Log JcicZ442Log
	 */
	public JcicZ442Log holdById(JcicZ442LogId jcicZ442LogId, TitaVo... titaVo);

	/**
	 * hold By JcicZ442Log
	 * 
	 * @param jcicZ442Log key
	 * @param titaVo      Variable-Length Argument
	 * @return JcicZ442Log JcicZ442Log
	 */
	public JcicZ442Log holdById(JcicZ442Log jcicZ442Log, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param jcicZ442Log Entity
	 * @param titaVo      Variable-Length Argument
	 * @return JcicZ442Log Entity
	 * @throws DBException exception
	 */
	public JcicZ442Log insert(JcicZ442Log jcicZ442Log, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param jcicZ442Log Entity
	 * @param titaVo      Variable-Length Argument
	 * @return JcicZ442Log Entity
	 * @throws DBException exception
	 */
	public JcicZ442Log update(JcicZ442Log jcicZ442Log, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param jcicZ442Log Entity
	 * @param titaVo      Variable-Length Argument
	 * @return JcicZ442Log Entity
	 * @throws DBException exception
	 */
	public JcicZ442Log update2(JcicZ442Log jcicZ442Log, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param jcicZ442Log Entity
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(JcicZ442Log jcicZ442Log, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param jcicZ442Log Entity of List
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<JcicZ442Log> jcicZ442Log, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param jcicZ442Log Entity of List
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<JcicZ442Log> jcicZ442Log, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param jcicZ442Log Entity of List
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<JcicZ442Log> jcicZ442Log, TitaVo... titaVo) throws DBException;

}
