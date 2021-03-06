package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicZ063;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicZ063Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ063Service {

	/**
	 * findByPrimaryKey
	 *
	 * @param jcicZ063Id PK
	 * @param titaVo     Variable-Length Argument
	 * @return JcicZ063 JcicZ063
	 */
	public JcicZ063 findById(JcicZ063Id jcicZ063Id, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice JcicZ063 JcicZ063 of List
	 */
	public Slice<JcicZ063> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * CustId=
	 *
	 * @param custId_0 custId_0
	 * @param index    Page Index
	 * @param limit    Page Data Limit
	 * @param titaVo   Variable-Length Argument
	 * @return Slice JcicZ063 JcicZ063 of List
	 */
	public Slice<JcicZ063> custIdEq(String custId_0, int index, int limit, TitaVo... titaVo);

	/**
	 * RcDate=
	 *
	 * @param rcDate_0 rcDate_0
	 * @param index    Page Index
	 * @param limit    Page Data Limit
	 * @param titaVo   Variable-Length Argument
	 * @return Slice JcicZ063 JcicZ063 of List
	 */
	public Slice<JcicZ063> rcDateEq(int rcDate_0, int index, int limit, TitaVo... titaVo);

	/**
	 * CustId= , AND RcDate=
	 *
	 * @param custId_0 custId_0
	 * @param rcDate_1 rcDate_1
	 * @param index    Page Index
	 * @param limit    Page Data Limit
	 * @param titaVo   Variable-Length Argument
	 * @return Slice JcicZ063 JcicZ063 of List
	 */
	public Slice<JcicZ063> custRcEq(String custId_0, int rcDate_1, int index, int limit, TitaVo... titaVo);

	/**
	 * SubmitKey= , AND CustId= , AND RcDate= , AND ChangePayDate=
	 *
	 * @param submitKey_0     submitKey_0
	 * @param custId_1        custId_1
	 * @param rcDate_2        rcDate_2
	 * @param changePayDate_3 changePayDate_3
	 * @param index           Page Index
	 * @param limit           Page Data Limit
	 * @param titaVo          Variable-Length Argument
	 * @return Slice JcicZ063 JcicZ063 of List
	 */
	public Slice<JcicZ063> otherEq(String submitKey_0, String custId_1, int rcDate_2, int changePayDate_3, int index, int limit, TitaVo... titaVo);

	/**
	 * Ukey=
	 *
	 * @param ukey_0 ukey_0
	 * @param titaVo Variable-Length Argument
	 * @return Slice JcicZ063 JcicZ063 of List
	 */
	public JcicZ063 ukeyFirst(String ukey_0, TitaVo... titaVo);

	/**
	 * SubmitKey= , AND CustId= , AND RcDate= , AND ChangePayDate=
	 *
	 * @param submitKey_0     submitKey_0
	 * @param custId_1        custId_1
	 * @param rcDate_2        rcDate_2
	 * @param changePayDate_3 changePayDate_3
	 * @param titaVo          Variable-Length Argument
	 * @return Slice JcicZ063 JcicZ063 of List
	 */
	public JcicZ063 otherFirst(String submitKey_0, String custId_1, int rcDate_2, int changePayDate_3, TitaVo... titaVo);

	/**
	 * hold By JcicZ063
	 * 
	 * @param jcicZ063Id key
	 * @param titaVo     Variable-Length Argument
	 * @return JcicZ063 JcicZ063
	 */
	public JcicZ063 holdById(JcicZ063Id jcicZ063Id, TitaVo... titaVo);

	/**
	 * hold By JcicZ063
	 * 
	 * @param jcicZ063 key
	 * @param titaVo   Variable-Length Argument
	 * @return JcicZ063 JcicZ063
	 */
	public JcicZ063 holdById(JcicZ063 jcicZ063, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param jcicZ063 Entity
	 * @param titaVo   Variable-Length Argument
	 * @return JcicZ063 Entity
	 * @throws DBException exception
	 */
	public JcicZ063 insert(JcicZ063 jcicZ063, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param jcicZ063 Entity
	 * @param titaVo   Variable-Length Argument
	 * @return JcicZ063 Entity
	 * @throws DBException exception
	 */
	public JcicZ063 update(JcicZ063 jcicZ063, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param jcicZ063 Entity
	 * @param titaVo   Variable-Length Argument
	 * @return JcicZ063 Entity
	 * @throws DBException exception
	 */
	public JcicZ063 update2(JcicZ063 jcicZ063, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param jcicZ063 Entity
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(JcicZ063 jcicZ063, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param jcicZ063 Entity of List
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<JcicZ063> jcicZ063, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param jcicZ063 Entity of List
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<JcicZ063> jcicZ063, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param jcicZ063 Entity of List
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<JcicZ063> jcicZ063, TitaVo... titaVo) throws DBException;

}
