package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicZ440;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicZ440Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ440Service {

	/**
	 * findByPrimaryKey
	 *
	 * @param jcicZ440Id PK
	 * @param titaVo     Variable-Length Argument
	 * @return JcicZ440 JcicZ440
	 */
	public JcicZ440 findById(JcicZ440Id jcicZ440Id, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice JcicZ440 JcicZ440 of List
	 */
	public Slice<JcicZ440> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * CustId=
	 *
	 * @param custId_0 custId_0
	 * @param index    Page Index
	 * @param limit    Page Data Limit
	 * @param titaVo   Variable-Length Argument
	 * @return Slice JcicZ440 JcicZ440 of List
	 */
	public Slice<JcicZ440> custIdEq(String custId_0, int index, int limit, TitaVo... titaVo);

	/**
	 * ApplyDate=
	 *
	 * @param applyDate_0 applyDate_0
	 * @param index       Page Index
	 * @param limit       Page Data Limit
	 * @param titaVo      Variable-Length Argument
	 * @return Slice JcicZ440 JcicZ440 of List
	 */
	public Slice<JcicZ440> rcDateEq(int applyDate_0, int index, int limit, TitaVo... titaVo);

	/**
	 * CustId= , AND ApplyDate=
	 *
	 * @param custId_0    custId_0
	 * @param applyDate_1 applyDate_1
	 * @param index       Page Index
	 * @param limit       Page Data Limit
	 * @param titaVo      Variable-Length Argument
	 * @return Slice JcicZ440 JcicZ440 of List
	 */
	public Slice<JcicZ440> custRcEq(String custId_0, int applyDate_1, int index, int limit, TitaVo... titaVo);

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
	 * @return Slice JcicZ440 JcicZ440 of List
	 */
	public Slice<JcicZ440> otherEq(String submitKey_0, String custId_1, int applyDate_2, String courtCode_3, int index, int limit, TitaVo... titaVo);

	/**
	 * Ukey=
	 *
	 * @param ukey_0 ukey_0
	 * @param titaVo Variable-Length Argument
	 * @return Slice JcicZ440 JcicZ440 of List
	 */
	public JcicZ440 ukeyFirst(String ukey_0, TitaVo... titaVo);

	/**
	 * SubmitKey= , AND CustId= , AND ApplyDate= , AND CourtCode=
	 *
	 * @param submitKey_0 submitKey_0
	 * @param custId_1    custId_1
	 * @param applyDate_2 applyDate_2
	 * @param courtCode_3 courtCode_3
	 * @param titaVo      Variable-Length Argument
	 * @return Slice JcicZ440 JcicZ440 of List
	 */
	public JcicZ440 otherFirst(String submitKey_0, String custId_1, int applyDate_2, String courtCode_3, TitaVo... titaVo);

	/**
	 * hold By JcicZ440
	 * 
	 * @param jcicZ440Id key
	 * @param titaVo     Variable-Length Argument
	 * @return JcicZ440 JcicZ440
	 */
	public JcicZ440 holdById(JcicZ440Id jcicZ440Id, TitaVo... titaVo);

	/**
	 * hold By JcicZ440
	 * 
	 * @param jcicZ440 key
	 * @param titaVo   Variable-Length Argument
	 * @return JcicZ440 JcicZ440
	 */
	public JcicZ440 holdById(JcicZ440 jcicZ440, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param jcicZ440 Entity
	 * @param titaVo   Variable-Length Argument
	 * @return JcicZ440 Entity
	 * @throws DBException exception
	 */
	public JcicZ440 insert(JcicZ440 jcicZ440, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param jcicZ440 Entity
	 * @param titaVo   Variable-Length Argument
	 * @return JcicZ440 Entity
	 * @throws DBException exception
	 */
	public JcicZ440 update(JcicZ440 jcicZ440, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param jcicZ440 Entity
	 * @param titaVo   Variable-Length Argument
	 * @return JcicZ440 Entity
	 * @throws DBException exception
	 */
	public JcicZ440 update2(JcicZ440 jcicZ440, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param jcicZ440 Entity
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(JcicZ440 jcicZ440, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param jcicZ440 Entity of List
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<JcicZ440> jcicZ440, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param jcicZ440 Entity of List
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<JcicZ440> jcicZ440, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param jcicZ440 Entity of List
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<JcicZ440> jcicZ440, TitaVo... titaVo) throws DBException;

}
