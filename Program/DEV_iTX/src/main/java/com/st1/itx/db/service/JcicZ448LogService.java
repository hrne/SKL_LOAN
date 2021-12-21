package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicZ448Log;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicZ448LogId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ448LogService {

	/**
	 * findByPrimaryKey
	 *
	 * @param jcicZ448LogId PK
	 * @param titaVo        Variable-Length Argument
	 * @return JcicZ448Log JcicZ448Log
	 */
	public JcicZ448Log findById(JcicZ448LogId jcicZ448LogId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice JcicZ448Log JcicZ448Log of List
	 */
	public Slice<JcicZ448Log> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * Ukey=
	 *
	 * @param ukey_0 ukey_0
	 * @param titaVo Variable-Length Argument
	 * @return Slice JcicZ448Log JcicZ448Log of List
	 */
	public JcicZ448Log ukeyFirst(String ukey_0, TitaVo... titaVo);

	/**
	 * Ukey=
	 *
	 * @param ukey_0 ukey_0
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice JcicZ448Log JcicZ448Log of List
	 */
	public Slice<JcicZ448Log> ukeyEq(String ukey_0, int index, int limit, TitaVo... titaVo);

	/**
	 * hold By JcicZ448Log
	 * 
	 * @param jcicZ448LogId key
	 * @param titaVo        Variable-Length Argument
	 * @return JcicZ448Log JcicZ448Log
	 */
	public JcicZ448Log holdById(JcicZ448LogId jcicZ448LogId, TitaVo... titaVo);

	/**
	 * hold By JcicZ448Log
	 * 
	 * @param jcicZ448Log key
	 * @param titaVo      Variable-Length Argument
	 * @return JcicZ448Log JcicZ448Log
	 */
	public JcicZ448Log holdById(JcicZ448Log jcicZ448Log, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param jcicZ448Log Entity
	 * @param titaVo      Variable-Length Argument
	 * @return JcicZ448Log Entity
	 * @throws DBException exception
	 */
	public JcicZ448Log insert(JcicZ448Log jcicZ448Log, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param jcicZ448Log Entity
	 * @param titaVo      Variable-Length Argument
	 * @return JcicZ448Log Entity
	 * @throws DBException exception
	 */
	public JcicZ448Log update(JcicZ448Log jcicZ448Log, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param jcicZ448Log Entity
	 * @param titaVo      Variable-Length Argument
	 * @return JcicZ448Log Entity
	 * @throws DBException exception
	 */
	public JcicZ448Log update2(JcicZ448Log jcicZ448Log, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param jcicZ448Log Entity
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(JcicZ448Log jcicZ448Log, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param jcicZ448Log Entity of List
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<JcicZ448Log> jcicZ448Log, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param jcicZ448Log Entity of List
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<JcicZ448Log> jcicZ448Log, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param jcicZ448Log Entity of List
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<JcicZ448Log> jcicZ448Log, TitaVo... titaVo) throws DBException;

}
