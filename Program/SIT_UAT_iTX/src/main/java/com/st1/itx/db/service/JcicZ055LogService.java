package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicZ055Log;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicZ055LogId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ055LogService {

	/**
	 * findByPrimaryKey
	 *
	 * @param jcicZ055LogId PK
	 * @param titaVo        Variable-Length Argument
	 * @return JcicZ055Log JcicZ055Log
	 */
	public JcicZ055Log findById(JcicZ055LogId jcicZ055LogId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice JcicZ055Log JcicZ055Log of List
	 */
	public Slice<JcicZ055Log> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * Ukey=
	 *
	 * @param ukey_0 ukey_0
	 * @param titaVo Variable-Length Argument
	 * @return Slice JcicZ055Log JcicZ055Log of List
	 */
	public JcicZ055Log ukeyFirst(String ukey_0, TitaVo... titaVo);

	/**
	 * Ukey=
	 *
	 * @param ukey_0 ukey_0
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice JcicZ055Log JcicZ055Log of List
	 */
	public Slice<JcicZ055Log> ukeyEq(String ukey_0, int index, int limit, TitaVo... titaVo);

	/**
	 * hold By JcicZ055Log
	 * 
	 * @param jcicZ055LogId key
	 * @param titaVo        Variable-Length Argument
	 * @return JcicZ055Log JcicZ055Log
	 */
	public JcicZ055Log holdById(JcicZ055LogId jcicZ055LogId, TitaVo... titaVo);

	/**
	 * hold By JcicZ055Log
	 * 
	 * @param jcicZ055Log key
	 * @param titaVo      Variable-Length Argument
	 * @return JcicZ055Log JcicZ055Log
	 */
	public JcicZ055Log holdById(JcicZ055Log jcicZ055Log, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param jcicZ055Log Entity
	 * @param titaVo      Variable-Length Argument
	 * @return JcicZ055Log Entity
	 * @throws DBException exception
	 */
	public JcicZ055Log insert(JcicZ055Log jcicZ055Log, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param jcicZ055Log Entity
	 * @param titaVo      Variable-Length Argument
	 * @return JcicZ055Log Entity
	 * @throws DBException exception
	 */
	public JcicZ055Log update(JcicZ055Log jcicZ055Log, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param jcicZ055Log Entity
	 * @param titaVo      Variable-Length Argument
	 * @return JcicZ055Log Entity
	 * @throws DBException exception
	 */
	public JcicZ055Log update2(JcicZ055Log jcicZ055Log, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param jcicZ055Log Entity
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(JcicZ055Log jcicZ055Log, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param jcicZ055Log Entity of List
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<JcicZ055Log> jcicZ055Log, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param jcicZ055Log Entity of List
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<JcicZ055Log> jcicZ055Log, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param jcicZ055Log Entity of List
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<JcicZ055Log> jcicZ055Log, TitaVo... titaVo) throws DBException;

}
