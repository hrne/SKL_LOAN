package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicZ061Log;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicZ061LogId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ061LogService {

	/**
	 * findByPrimaryKey
	 *
	 * @param jcicZ061LogId PK
	 * @param titaVo        Variable-Length Argument
	 * @return JcicZ061Log JcicZ061Log
	 */
	public JcicZ061Log findById(JcicZ061LogId jcicZ061LogId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice JcicZ061Log JcicZ061Log of List
	 */
	public Slice<JcicZ061Log> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * Ukey=
	 *
	 * @param ukey_0 ukey_0
	 * @param titaVo Variable-Length Argument
	 * @return Slice JcicZ061Log JcicZ061Log of List
	 */
	public JcicZ061Log ukeyFirst(String ukey_0, TitaVo... titaVo);

	/**
	 * Ukey=
	 *
	 * @param ukey_0 ukey_0
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice JcicZ061Log JcicZ061Log of List
	 */
	public Slice<JcicZ061Log> ukeyEq(String ukey_0, int index, int limit, TitaVo... titaVo);

	/**
	 * hold By JcicZ061Log
	 * 
	 * @param jcicZ061LogId key
	 * @param titaVo        Variable-Length Argument
	 * @return JcicZ061Log JcicZ061Log
	 */
	public JcicZ061Log holdById(JcicZ061LogId jcicZ061LogId, TitaVo... titaVo);

	/**
	 * hold By JcicZ061Log
	 * 
	 * @param jcicZ061Log key
	 * @param titaVo      Variable-Length Argument
	 * @return JcicZ061Log JcicZ061Log
	 */
	public JcicZ061Log holdById(JcicZ061Log jcicZ061Log, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param jcicZ061Log Entity
	 * @param titaVo      Variable-Length Argument
	 * @return JcicZ061Log Entity
	 * @throws DBException exception
	 */
	public JcicZ061Log insert(JcicZ061Log jcicZ061Log, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param jcicZ061Log Entity
	 * @param titaVo      Variable-Length Argument
	 * @return JcicZ061Log Entity
	 * @throws DBException exception
	 */
	public JcicZ061Log update(JcicZ061Log jcicZ061Log, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param jcicZ061Log Entity
	 * @param titaVo      Variable-Length Argument
	 * @return JcicZ061Log Entity
	 * @throws DBException exception
	 */
	public JcicZ061Log update2(JcicZ061Log jcicZ061Log, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param jcicZ061Log Entity
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(JcicZ061Log jcicZ061Log, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param jcicZ061Log Entity of List
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<JcicZ061Log> jcicZ061Log, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param jcicZ061Log Entity of List
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<JcicZ061Log> jcicZ061Log, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param jcicZ061Log Entity of List
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<JcicZ061Log> jcicZ061Log, TitaVo... titaVo) throws DBException;

}
