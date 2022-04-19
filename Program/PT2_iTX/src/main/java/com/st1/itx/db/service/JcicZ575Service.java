package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicZ575;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicZ575Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ575Service {

	/**
	 * findByPrimaryKey
	 *
	 * @param jcicZ575Id PK
	 * @param titaVo     Variable-Length Argument
	 * @return JcicZ575 JcicZ575
	 */
	public JcicZ575 findById(JcicZ575Id jcicZ575Id, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice JcicZ575 JcicZ575 of List
	 */
	public Slice<JcicZ575> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * CustId=
	 *
	 * @param custId_0 custId_0
	 * @param index    Page Index
	 * @param limit    Page Data Limit
	 * @param titaVo   Variable-Length Argument
	 * @return Slice JcicZ575 JcicZ575 of List
	 */
	public Slice<JcicZ575> custIdEq(String custId_0, int index, int limit, TitaVo... titaVo);

	/**
	 * ApplyDate=
	 *
	 * @param applyDate_0 applyDate_0
	 * @param index       Page Index
	 * @param limit       Page Data Limit
	 * @param titaVo      Variable-Length Argument
	 * @return Slice JcicZ575 JcicZ575 of List
	 */
	public Slice<JcicZ575> rcDateEq(int applyDate_0, int index, int limit, TitaVo... titaVo);

	/**
	 * CustId= , AND ApplyDate=
	 *
	 * @param custId_0    custId_0
	 * @param applyDate_1 applyDate_1
	 * @param index       Page Index
	 * @param limit       Page Data Limit
	 * @param titaVo      Variable-Length Argument
	 * @return Slice JcicZ575 JcicZ575 of List
	 */
	public Slice<JcicZ575> custRcEq(String custId_0, int applyDate_1, int index, int limit, TitaVo... titaVo);

	/**
	 * CustId=, AND ApplyDate = ,AND SubmitKey = ,AND BankId =
	 *
	 * @param custId_0    custId_0
	 * @param applyDate_1 applyDate_1
	 * @param submitKey_2 submitKey_2
	 * @param bankId_3    bankId_3
	 * @param index       Page Index
	 * @param limit       Page Data Limit
	 * @param titaVo      Variable-Length Argument
	 * @return Slice JcicZ575 JcicZ575 of List
	 */
	public Slice<JcicZ575> otherEq(String custId_0, int applyDate_1, String submitKey_2, String bankId_3, int index, int limit, TitaVo... titaVo);

	/**
	 * Ukey=
	 *
	 * @param ukey_0 ukey_0
	 * @param titaVo Variable-Length Argument
	 * @return Slice JcicZ575 JcicZ575 of List
	 */
	public JcicZ575 ukeyFirst(String ukey_0, TitaVo... titaVo);

	/**
	 * CustId=, AND ApplyDate = ,AND SubmitKey = ,AND BankId =
	 *
	 * @param custId_0    custId_0
	 * @param applyDate_1 applyDate_1
	 * @param submitKey_2 submitKey_2
	 * @param bankId_3    bankId_3
	 * @param titaVo      Variable-Length Argument
	 * @return Slice JcicZ575 JcicZ575 of List
	 */
	public JcicZ575 otherFirst(String custId_0, int applyDate_1, String submitKey_2, String bankId_3, TitaVo... titaVo);

	/**
	 * hold By JcicZ575
	 * 
	 * @param jcicZ575Id key
	 * @param titaVo     Variable-Length Argument
	 * @return JcicZ575 JcicZ575
	 */
	public JcicZ575 holdById(JcicZ575Id jcicZ575Id, TitaVo... titaVo);

	/**
	 * hold By JcicZ575
	 * 
	 * @param jcicZ575 key
	 * @param titaVo   Variable-Length Argument
	 * @return JcicZ575 JcicZ575
	 */
	public JcicZ575 holdById(JcicZ575 jcicZ575, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param jcicZ575 Entity
	 * @param titaVo   Variable-Length Argument
	 * @return JcicZ575 Entity
	 * @throws DBException exception
	 */
	public JcicZ575 insert(JcicZ575 jcicZ575, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param jcicZ575 Entity
	 * @param titaVo   Variable-Length Argument
	 * @return JcicZ575 Entity
	 * @throws DBException exception
	 */
	public JcicZ575 update(JcicZ575 jcicZ575, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param jcicZ575 Entity
	 * @param titaVo   Variable-Length Argument
	 * @return JcicZ575 Entity
	 * @throws DBException exception
	 */
	public JcicZ575 update2(JcicZ575 jcicZ575, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param jcicZ575 Entity
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(JcicZ575 jcicZ575, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param jcicZ575 Entity of List
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<JcicZ575> jcicZ575, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param jcicZ575 Entity of List
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<JcicZ575> jcicZ575, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param jcicZ575 Entity of List
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<JcicZ575> jcicZ575, TitaVo... titaVo) throws DBException;

}
