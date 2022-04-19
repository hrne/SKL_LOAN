package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicZ454Log;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicZ454LogId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ454LogService {

	/**
	 * findByPrimaryKey
	 *
	 * @param jcicZ454LogId PK
	 * @param titaVo        Variable-Length Argument
	 * @return JcicZ454Log JcicZ454Log
	 */
	public JcicZ454Log findById(JcicZ454LogId jcicZ454LogId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice JcicZ454Log JcicZ454Log of List
	 */
	public Slice<JcicZ454Log> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * Ukey=
	 *
	 * @param ukey_0 ukey_0
	 * @param titaVo Variable-Length Argument
	 * @return Slice JcicZ454Log JcicZ454Log of List
	 */
	public JcicZ454Log ukeyFirst(String ukey_0, TitaVo... titaVo);

	/**
	 * Ukey=
	 *
	 * @param ukey_0 ukey_0
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice JcicZ454Log JcicZ454Log of List
	 */
	public Slice<JcicZ454Log> ukeyEq(String ukey_0, int index, int limit, TitaVo... titaVo);

	/**
	 * hold By JcicZ454Log
	 * 
	 * @param jcicZ454LogId key
	 * @param titaVo        Variable-Length Argument
	 * @return JcicZ454Log JcicZ454Log
	 */
	public JcicZ454Log holdById(JcicZ454LogId jcicZ454LogId, TitaVo... titaVo);

	/**
	 * hold By JcicZ454Log
	 * 
	 * @param jcicZ454Log key
	 * @param titaVo      Variable-Length Argument
	 * @return JcicZ454Log JcicZ454Log
	 */
	public JcicZ454Log holdById(JcicZ454Log jcicZ454Log, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param jcicZ454Log Entity
	 * @param titaVo      Variable-Length Argument
	 * @return JcicZ454Log Entity
	 * @throws DBException exception
	 */
	public JcicZ454Log insert(JcicZ454Log jcicZ454Log, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param jcicZ454Log Entity
	 * @param titaVo      Variable-Length Argument
	 * @return JcicZ454Log Entity
	 * @throws DBException exception
	 */
	public JcicZ454Log update(JcicZ454Log jcicZ454Log, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param jcicZ454Log Entity
	 * @param titaVo      Variable-Length Argument
	 * @return JcicZ454Log Entity
	 * @throws DBException exception
	 */
	public JcicZ454Log update2(JcicZ454Log jcicZ454Log, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param jcicZ454Log Entity
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(JcicZ454Log jcicZ454Log, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param jcicZ454Log Entity of List
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<JcicZ454Log> jcicZ454Log, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param jcicZ454Log Entity of List
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<JcicZ454Log> jcicZ454Log, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param jcicZ454Log Entity of List
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<JcicZ454Log> jcicZ454Log, TitaVo... titaVo) throws DBException;

}
