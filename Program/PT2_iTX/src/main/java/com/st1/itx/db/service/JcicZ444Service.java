package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicZ444;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicZ444Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ444Service {

	/**
	 * findByPrimaryKey
	 *
	 * @param jcicZ444Id PK
	 * @param titaVo     Variable-Length Argument
	 * @return JcicZ444 JcicZ444
	 */
	public JcicZ444 findById(JcicZ444Id jcicZ444Id, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice JcicZ444 JcicZ444 of List
	 */
	public Slice<JcicZ444> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * CustId=
	 *
	 * @param custId_0 custId_0
	 * @param index    Page Index
	 * @param limit    Page Data Limit
	 * @param titaVo   Variable-Length Argument
	 * @return Slice JcicZ444 JcicZ444 of List
	 */
	public Slice<JcicZ444> custIdEq(String custId_0, int index, int limit, TitaVo... titaVo);

	/**
	 * ApplyDate=
	 *
	 * @param applyDate_0 applyDate_0
	 * @param index       Page Index
	 * @param limit       Page Data Limit
	 * @param titaVo      Variable-Length Argument
	 * @return Slice JcicZ444 JcicZ444 of List
	 */
	public Slice<JcicZ444> rcDateEq(int applyDate_0, int index, int limit, TitaVo... titaVo);

	/**
	 * CustId= , AND ApplyDate=
	 *
	 * @param custId_0    custId_0
	 * @param applyDate_1 applyDate_1
	 * @param index       Page Index
	 * @param limit       Page Data Limit
	 * @param titaVo      Variable-Length Argument
	 * @return Slice JcicZ444 JcicZ444 of List
	 */
	public Slice<JcicZ444> custRcEq(String custId_0, int applyDate_1, int index, int limit, TitaVo... titaVo);

	/**
	 * SubmitKey= , AND CustId= , AND ApplyDate= , AND CourtCode=
	 *
	 * @param submitKey_0 submitKey_0
	 * @param custId_1    custId_1
	 * @param applyDate_2 applyDate_2
	 * @param courtCode_3 courtCode_3
	 * @param index       Page Index
	 * @param limit       Page Data Limit
	 * @param titaVo      Variable-Length Argument
	 * @return Slice JcicZ444 JcicZ444 of List
	 */
	public Slice<JcicZ444> otherEq(String submitKey_0, String custId_1, int applyDate_2, String courtCode_3, int index, int limit, TitaVo... titaVo);

	/**
	 * Ukey=
	 *
	 * @param ukey_0 ukey_0
	 * @param titaVo Variable-Length Argument
	 * @return Slice JcicZ444 JcicZ444 of List
	 */
	public JcicZ444 ukeyFirst(String ukey_0, TitaVo... titaVo);

	/**
	 * SubmitKey= , AND CustId= , AND ApplyDate= , AND CourtCode=
	 *
	 * @param submitKey_0 submitKey_0
	 * @param custId_1    custId_1
	 * @param applyDate_2 applyDate_2
	 * @param courtCode_3 courtCode_3
	 * @param titaVo      Variable-Length Argument
	 * @return Slice JcicZ444 JcicZ444 of List
	 */
	public JcicZ444 otherFirst(String submitKey_0, String custId_1, int applyDate_2, String courtCode_3, TitaVo... titaVo);

	/**
	 * hold By JcicZ444
	 * 
	 * @param jcicZ444Id key
	 * @param titaVo     Variable-Length Argument
	 * @return JcicZ444 JcicZ444
	 */
	public JcicZ444 holdById(JcicZ444Id jcicZ444Id, TitaVo... titaVo);

	/**
	 * hold By JcicZ444
	 * 
	 * @param jcicZ444 key
	 * @param titaVo   Variable-Length Argument
	 * @return JcicZ444 JcicZ444
	 */
	public JcicZ444 holdById(JcicZ444 jcicZ444, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param jcicZ444 Entity
	 * @param titaVo   Variable-Length Argument
	 * @return JcicZ444 Entity
	 * @throws DBException exception
	 */
	public JcicZ444 insert(JcicZ444 jcicZ444, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param jcicZ444 Entity
	 * @param titaVo   Variable-Length Argument
	 * @return JcicZ444 Entity
	 * @throws DBException exception
	 */
	public JcicZ444 update(JcicZ444 jcicZ444, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param jcicZ444 Entity
	 * @param titaVo   Variable-Length Argument
	 * @return JcicZ444 Entity
	 * @throws DBException exception
	 */
	public JcicZ444 update2(JcicZ444 jcicZ444, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param jcicZ444 Entity
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(JcicZ444 jcicZ444, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param jcicZ444 Entity of List
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<JcicZ444> jcicZ444, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param jcicZ444 Entity of List
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<JcicZ444> jcicZ444, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param jcicZ444 Entity of List
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<JcicZ444> jcicZ444, TitaVo... titaVo) throws DBException;

}
