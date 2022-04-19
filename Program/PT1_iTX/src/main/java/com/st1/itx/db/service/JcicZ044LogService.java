package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicZ044Log;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicZ044LogId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ044LogService {

	/**
	 * findByPrimaryKey
	 *
	 * @param jcicZ044LogId PK
	 * @param titaVo        Variable-Length Argument
	 * @return JcicZ044Log JcicZ044Log
	 */
	public JcicZ044Log findById(JcicZ044LogId jcicZ044LogId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice JcicZ044Log JcicZ044Log of List
	 */
	public Slice<JcicZ044Log> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * Ukey=
	 *
	 * @param ukey_0 ukey_0
	 * @param titaVo Variable-Length Argument
	 * @return Slice JcicZ044Log JcicZ044Log of List
	 */
	public JcicZ044Log ukeyFirst(String ukey_0, TitaVo... titaVo);

	/**
	 * Ukey=
	 *
	 * @param ukey_0 ukey_0
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice JcicZ044Log JcicZ044Log of List
	 */
	public Slice<JcicZ044Log> ukeyEq(String ukey_0, int index, int limit, TitaVo... titaVo);

	/**
	 * hold By JcicZ044Log
	 * 
	 * @param jcicZ044LogId key
	 * @param titaVo        Variable-Length Argument
	 * @return JcicZ044Log JcicZ044Log
	 */
	public JcicZ044Log holdById(JcicZ044LogId jcicZ044LogId, TitaVo... titaVo);

	/**
	 * hold By JcicZ044Log
	 * 
	 * @param jcicZ044Log key
	 * @param titaVo      Variable-Length Argument
	 * @return JcicZ044Log JcicZ044Log
	 */
	public JcicZ044Log holdById(JcicZ044Log jcicZ044Log, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param jcicZ044Log Entity
	 * @param titaVo      Variable-Length Argument
	 * @return JcicZ044Log Entity
	 * @throws DBException exception
	 */
	public JcicZ044Log insert(JcicZ044Log jcicZ044Log, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param jcicZ044Log Entity
	 * @param titaVo      Variable-Length Argument
	 * @return JcicZ044Log Entity
	 * @throws DBException exception
	 */
	public JcicZ044Log update(JcicZ044Log jcicZ044Log, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param jcicZ044Log Entity
	 * @param titaVo      Variable-Length Argument
	 * @return JcicZ044Log Entity
	 * @throws DBException exception
	 */
	public JcicZ044Log update2(JcicZ044Log jcicZ044Log, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param jcicZ044Log Entity
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(JcicZ044Log jcicZ044Log, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param jcicZ044Log Entity of List
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<JcicZ044Log> jcicZ044Log, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param jcicZ044Log Entity of List
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<JcicZ044Log> jcicZ044Log, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param jcicZ044Log Entity of List
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<JcicZ044Log> jcicZ044Log, TitaVo... titaVo) throws DBException;

}
