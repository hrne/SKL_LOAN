package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicZ572Log;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicZ572LogId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ572LogService {

	/**
	 * findByPrimaryKey
	 *
	 * @param jcicZ572LogId PK
	 * @param titaVo        Variable-Length Argument
	 * @return JcicZ572Log JcicZ572Log
	 */
	public JcicZ572Log findById(JcicZ572LogId jcicZ572LogId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice JcicZ572Log JcicZ572Log of List
	 */
	public Slice<JcicZ572Log> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * Ukey=
	 *
	 * @param ukey_0 ukey_0
	 * @param titaVo Variable-Length Argument
	 * @return Slice JcicZ572Log JcicZ572Log of List
	 */
	public JcicZ572Log ukeyFirst(String ukey_0, TitaVo... titaVo);

	/**
	 * Ukey=
	 *
	 * @param ukey_0 ukey_0
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice JcicZ572Log JcicZ572Log of List
	 */
	public Slice<JcicZ572Log> ukeyEq(String ukey_0, int index, int limit, TitaVo... titaVo);

	/**
	 * hold By JcicZ572Log
	 * 
	 * @param jcicZ572LogId key
	 * @param titaVo        Variable-Length Argument
	 * @return JcicZ572Log JcicZ572Log
	 */
	public JcicZ572Log holdById(JcicZ572LogId jcicZ572LogId, TitaVo... titaVo);

	/**
	 * hold By JcicZ572Log
	 * 
	 * @param jcicZ572Log key
	 * @param titaVo      Variable-Length Argument
	 * @return JcicZ572Log JcicZ572Log
	 */
	public JcicZ572Log holdById(JcicZ572Log jcicZ572Log, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param jcicZ572Log Entity
	 * @param titaVo      Variable-Length Argument
	 * @return JcicZ572Log Entity
	 * @throws DBException exception
	 */
	public JcicZ572Log insert(JcicZ572Log jcicZ572Log, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param jcicZ572Log Entity
	 * @param titaVo      Variable-Length Argument
	 * @return JcicZ572Log Entity
	 * @throws DBException exception
	 */
	public JcicZ572Log update(JcicZ572Log jcicZ572Log, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param jcicZ572Log Entity
	 * @param titaVo      Variable-Length Argument
	 * @return JcicZ572Log Entity
	 * @throws DBException exception
	 */
	public JcicZ572Log update2(JcicZ572Log jcicZ572Log, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param jcicZ572Log Entity
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(JcicZ572Log jcicZ572Log, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param jcicZ572Log Entity of List
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<JcicZ572Log> jcicZ572Log, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param jcicZ572Log Entity of List
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<JcicZ572Log> jcicZ572Log, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param jcicZ572Log Entity of List
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<JcicZ572Log> jcicZ572Log, TitaVo... titaVo) throws DBException;

}
