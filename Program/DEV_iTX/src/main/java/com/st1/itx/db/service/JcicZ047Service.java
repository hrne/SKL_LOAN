package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicZ047;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicZ047Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ047Service {

	/**
	 * findByPrimaryKey
	 *
	 * @param jcicZ047Id PK
	 * @param titaVo     Variable-Length Argument
	 * @return JcicZ047 JcicZ047
	 */
	public JcicZ047 findById(JcicZ047Id jcicZ047Id, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice JcicZ047 JcicZ047 of List
	 */
	public Slice<JcicZ047> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * CustId=
	 *
	 * @param custId_0 custId_0
	 * @param index    Page Index
	 * @param limit    Page Data Limit
	 * @param titaVo   Variable-Length Argument
	 * @return Slice JcicZ047 JcicZ047 of List
	 */
	public Slice<JcicZ047> custIdEq(String custId_0, int index, int limit, TitaVo... titaVo);

	/**
	 * RcDate=
	 *
	 * @param rcDate_0 rcDate_0
	 * @param index    Page Index
	 * @param limit    Page Data Limit
	 * @param titaVo   Variable-Length Argument
	 * @return Slice JcicZ047 JcicZ047 of List
	 */
	public Slice<JcicZ047> rcDateEq(int rcDate_0, int index, int limit, TitaVo... titaVo);

	/**
	 * CustId= , AND RcDate=
	 *
	 * @param custId_0 custId_0
	 * @param rcDate_1 rcDate_1
	 * @param index    Page Index
	 * @param limit    Page Data Limit
	 * @param titaVo   Variable-Length Argument
	 * @return Slice JcicZ047 JcicZ047 of List
	 */
	public Slice<JcicZ047> custRcEq(String custId_0, int rcDate_1, int index, int limit, TitaVo... titaVo);

	/**
	 * SubmitKey= , AND CustId= , AND RcDate=
	 *
	 * @param submitKey_0 submitKey_0
	 * @param custId_1    custId_1
	 * @param rcDate_2    rcDate_2
	 * @param index       Page Index
	 * @param limit       Page Data Limit
	 * @param titaVo      Variable-Length Argument
	 * @return Slice JcicZ047 JcicZ047 of List
	 */
	public Slice<JcicZ047> otherEq(String submitKey_0, String custId_1, int rcDate_2, int index, int limit, TitaVo... titaVo);

	/**
	 * Ukey=
	 *
	 * @param ukey_0 ukey_0
	 * @param titaVo Variable-Length Argument
	 * @return Slice JcicZ047 JcicZ047 of List
	 */
	public JcicZ047 ukeyFirst(String ukey_0, TitaVo... titaVo);

	/**
	 * SubmitKey= , AND CustId= , AND RcDate=
	 *
	 * @param submitKey_0 submitKey_0
	 * @param custId_1    custId_1
	 * @param rcDate_2    rcDate_2
	 * @param titaVo      Variable-Length Argument
	 * @return Slice JcicZ047 JcicZ047 of List
	 */
	public JcicZ047 otherFirst(String submitKey_0, String custId_1, int rcDate_2, TitaVo... titaVo);

	/**
	 * hold By JcicZ047
	 * 
	 * @param jcicZ047Id key
	 * @param titaVo     Variable-Length Argument
	 * @return JcicZ047 JcicZ047
	 */
	public JcicZ047 holdById(JcicZ047Id jcicZ047Id, TitaVo... titaVo);

	/**
	 * hold By JcicZ047
	 * 
	 * @param jcicZ047 key
	 * @param titaVo   Variable-Length Argument
	 * @return JcicZ047 JcicZ047
	 */
	public JcicZ047 holdById(JcicZ047 jcicZ047, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param jcicZ047 Entity
	 * @param titaVo   Variable-Length Argument
	 * @return JcicZ047 Entity
	 * @throws DBException exception
	 */
	public JcicZ047 insert(JcicZ047 jcicZ047, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param jcicZ047 Entity
	 * @param titaVo   Variable-Length Argument
	 * @return JcicZ047 Entity
	 * @throws DBException exception
	 */
	public JcicZ047 update(JcicZ047 jcicZ047, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param jcicZ047 Entity
	 * @param titaVo   Variable-Length Argument
	 * @return JcicZ047 Entity
	 * @throws DBException exception
	 */
	public JcicZ047 update2(JcicZ047 jcicZ047, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param jcicZ047 Entity
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(JcicZ047 jcicZ047, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param jcicZ047 Entity of List
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<JcicZ047> jcicZ047, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param jcicZ047 Entity of List
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<JcicZ047> jcicZ047, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param jcicZ047 Entity of List
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<JcicZ047> jcicZ047, TitaVo... titaVo) throws DBException;

}
