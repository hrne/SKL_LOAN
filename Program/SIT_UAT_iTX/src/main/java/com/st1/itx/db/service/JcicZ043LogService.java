package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicZ043Log;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicZ043LogId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ043LogService {

	/**
	 * findByPrimaryKey
	 *
	 * @param jcicZ043LogId PK
	 * @param titaVo        Variable-Length Argument
	 * @return JcicZ043Log JcicZ043Log
	 */
	public JcicZ043Log findById(JcicZ043LogId jcicZ043LogId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice JcicZ043Log JcicZ043Log of List
	 */
	public Slice<JcicZ043Log> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * Ukey=
	 *
	 * @param ukey_0 ukey_0
	 * @param titaVo Variable-Length Argument
	 * @return Slice JcicZ043Log JcicZ043Log of List
	 */
	public JcicZ043Log ukeyFirst(String ukey_0, TitaVo... titaVo);

	/**
	 * Ukey=
	 *
	 * @param ukey_0 ukey_0
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice JcicZ043Log JcicZ043Log of List
	 */
	public Slice<JcicZ043Log> ukeyEq(String ukey_0, int index, int limit, TitaVo... titaVo);

	/**
	 * hold By JcicZ043Log
	 * 
	 * @param jcicZ043LogId key
	 * @param titaVo        Variable-Length Argument
	 * @return JcicZ043Log JcicZ043Log
	 */
	public JcicZ043Log holdById(JcicZ043LogId jcicZ043LogId, TitaVo... titaVo);

	/**
	 * hold By JcicZ043Log
	 * 
	 * @param jcicZ043Log key
	 * @param titaVo      Variable-Length Argument
	 * @return JcicZ043Log JcicZ043Log
	 */
	public JcicZ043Log holdById(JcicZ043Log jcicZ043Log, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param jcicZ043Log Entity
	 * @param titaVo      Variable-Length Argument
	 * @return JcicZ043Log Entity
	 * @throws DBException exception
	 */
	public JcicZ043Log insert(JcicZ043Log jcicZ043Log, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param jcicZ043Log Entity
	 * @param titaVo      Variable-Length Argument
	 * @return JcicZ043Log Entity
	 * @throws DBException exception
	 */
	public JcicZ043Log update(JcicZ043Log jcicZ043Log, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param jcicZ043Log Entity
	 * @param titaVo      Variable-Length Argument
	 * @return JcicZ043Log Entity
	 * @throws DBException exception
	 */
	public JcicZ043Log update2(JcicZ043Log jcicZ043Log, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param jcicZ043Log Entity
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(JcicZ043Log jcicZ043Log, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param jcicZ043Log Entity of List
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<JcicZ043Log> jcicZ043Log, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param jcicZ043Log Entity of List
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<JcicZ043Log> jcicZ043Log, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param jcicZ043Log Entity of List
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<JcicZ043Log> jcicZ043Log, TitaVo... titaVo) throws DBException;

}
