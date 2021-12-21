package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicZ446Log;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicZ446LogId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ446LogService {

	/**
	 * findByPrimaryKey
	 *
	 * @param jcicZ446LogId PK
	 * @param titaVo        Variable-Length Argument
	 * @return JcicZ446Log JcicZ446Log
	 */
	public JcicZ446Log findById(JcicZ446LogId jcicZ446LogId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice JcicZ446Log JcicZ446Log of List
	 */
	public Slice<JcicZ446Log> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * Ukey=
	 *
	 * @param ukey_0 ukey_0
	 * @param titaVo Variable-Length Argument
	 * @return Slice JcicZ446Log JcicZ446Log of List
	 */
	public JcicZ446Log ukeyFirst(String ukey_0, TitaVo... titaVo);

	/**
	 * Ukey=
	 *
	 * @param ukey_0 ukey_0
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice JcicZ446Log JcicZ446Log of List
	 */
	public Slice<JcicZ446Log> ukeyEq(String ukey_0, int index, int limit, TitaVo... titaVo);

	/**
	 * hold By JcicZ446Log
	 * 
	 * @param jcicZ446LogId key
	 * @param titaVo        Variable-Length Argument
	 * @return JcicZ446Log JcicZ446Log
	 */
	public JcicZ446Log holdById(JcicZ446LogId jcicZ446LogId, TitaVo... titaVo);

	/**
	 * hold By JcicZ446Log
	 * 
	 * @param jcicZ446Log key
	 * @param titaVo      Variable-Length Argument
	 * @return JcicZ446Log JcicZ446Log
	 */
	public JcicZ446Log holdById(JcicZ446Log jcicZ446Log, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param jcicZ446Log Entity
	 * @param titaVo      Variable-Length Argument
	 * @return JcicZ446Log Entity
	 * @throws DBException exception
	 */
	public JcicZ446Log insert(JcicZ446Log jcicZ446Log, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param jcicZ446Log Entity
	 * @param titaVo      Variable-Length Argument
	 * @return JcicZ446Log Entity
	 * @throws DBException exception
	 */
	public JcicZ446Log update(JcicZ446Log jcicZ446Log, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param jcicZ446Log Entity
	 * @param titaVo      Variable-Length Argument
	 * @return JcicZ446Log Entity
	 * @throws DBException exception
	 */
	public JcicZ446Log update2(JcicZ446Log jcicZ446Log, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param jcicZ446Log Entity
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(JcicZ446Log jcicZ446Log, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param jcicZ446Log Entity of List
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<JcicZ446Log> jcicZ446Log, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param jcicZ446Log Entity of List
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<JcicZ446Log> jcicZ446Log, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param jcicZ446Log Entity of List
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<JcicZ446Log> jcicZ446Log, TitaVo... titaVo) throws DBException;

}
