package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicZ046Log;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicZ046LogId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ046LogService {

	/**
	 * findByPrimaryKey
	 *
	 * @param jcicZ046LogId PK
	 * @param titaVo        Variable-Length Argument
	 * @return JcicZ046Log JcicZ046Log
	 */
	public JcicZ046Log findById(JcicZ046LogId jcicZ046LogId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice JcicZ046Log JcicZ046Log of List
	 */
	public Slice<JcicZ046Log> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * Ukey=
	 *
	 * @param ukey_0 ukey_0
	 * @param titaVo Variable-Length Argument
	 * @return Slice JcicZ046Log JcicZ046Log of List
	 */
	public JcicZ046Log ukeyFirst(String ukey_0, TitaVo... titaVo);

	/**
	 * Ukey=
	 *
	 * @param ukey_0 ukey_0
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice JcicZ046Log JcicZ046Log of List
	 */
	public Slice<JcicZ046Log> ukeyEq(String ukey_0, int index, int limit, TitaVo... titaVo);

	/**
	 * hold By JcicZ046Log
	 * 
	 * @param jcicZ046LogId key
	 * @param titaVo        Variable-Length Argument
	 * @return JcicZ046Log JcicZ046Log
	 */
	public JcicZ046Log holdById(JcicZ046LogId jcicZ046LogId, TitaVo... titaVo);

	/**
	 * hold By JcicZ046Log
	 * 
	 * @param jcicZ046Log key
	 * @param titaVo      Variable-Length Argument
	 * @return JcicZ046Log JcicZ046Log
	 */
	public JcicZ046Log holdById(JcicZ046Log jcicZ046Log, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param jcicZ046Log Entity
	 * @param titaVo      Variable-Length Argument
	 * @return JcicZ046Log Entity
	 * @throws DBException exception
	 */
	public JcicZ046Log insert(JcicZ046Log jcicZ046Log, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param jcicZ046Log Entity
	 * @param titaVo      Variable-Length Argument
	 * @return JcicZ046Log Entity
	 * @throws DBException exception
	 */
	public JcicZ046Log update(JcicZ046Log jcicZ046Log, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param jcicZ046Log Entity
	 * @param titaVo      Variable-Length Argument
	 * @return JcicZ046Log Entity
	 * @throws DBException exception
	 */
	public JcicZ046Log update2(JcicZ046Log jcicZ046Log, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param jcicZ046Log Entity
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(JcicZ046Log jcicZ046Log, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param jcicZ046Log Entity of List
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<JcicZ046Log> jcicZ046Log, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param jcicZ046Log Entity of List
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<JcicZ046Log> jcicZ046Log, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param jcicZ046Log Entity of List
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<JcicZ046Log> jcicZ046Log, TitaVo... titaVo) throws DBException;

}
