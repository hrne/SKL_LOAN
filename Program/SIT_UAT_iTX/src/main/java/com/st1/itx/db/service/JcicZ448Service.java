package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicZ448;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicZ448Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ448Service {

	/**
	 * findByPrimaryKey
	 *
	 * @param jcicZ448Id PK
	 * @param titaVo     Variable-Length Argument
	 * @return JcicZ448 JcicZ448
	 */
	public JcicZ448 findById(JcicZ448Id jcicZ448Id, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice JcicZ448 JcicZ448 of List
	 */
	public Slice<JcicZ448> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * CustId=
	 *
	 * @param custId_0 custId_0
	 * @param index    Page Index
	 * @param limit    Page Data Limit
	 * @param titaVo   Variable-Length Argument
	 * @return Slice JcicZ448 JcicZ448 of List
	 */
	public Slice<JcicZ448> custIdEq(String custId_0, int index, int limit, TitaVo... titaVo);

	/**
	 * ApplyDate=
	 *
	 * @param applyDate_0 applyDate_0
	 * @param index       Page Index
	 * @param limit       Page Data Limit
	 * @param titaVo      Variable-Length Argument
	 * @return Slice JcicZ448 JcicZ448 of List
	 */
	public Slice<JcicZ448> rcDateEq(int applyDate_0, int index, int limit, TitaVo... titaVo);

	/**
	 * CustId= , AND ApplyDate=
	 *
	 * @param custId_0    custId_0
	 * @param applyDate_1 applyDate_1
	 * @param index       Page Index
	 * @param limit       Page Data Limit
	 * @param titaVo      Variable-Length Argument
	 * @return Slice JcicZ448 JcicZ448 of List
	 */
	public Slice<JcicZ448> custRcEq(String custId_0, int applyDate_1, int index, int limit, TitaVo... titaVo);

	/**
	 * SubmitKey= , AND CustId= , AND ApplyDate= , AND CourtCode= , AND MaxMainCode=
	 *
	 * @param submitKey_0   submitKey_0
	 * @param custId_1      custId_1
	 * @param applyDate_2   applyDate_2
	 * @param courtCode_3   courtCode_3
	 * @param maxMainCode_4 maxMainCode_4
	 * @param index         Page Index
	 * @param limit         Page Data Limit
	 * @param titaVo        Variable-Length Argument
	 * @return Slice JcicZ448 JcicZ448 of List
	 */
	public Slice<JcicZ448> otherEq(String submitKey_0, String custId_1, int applyDate_2, String courtCode_3, String maxMainCode_4, int index, int limit, TitaVo... titaVo);

	/**
	 * Ukey=
	 *
	 * @param ukey_0 ukey_0
	 * @param titaVo Variable-Length Argument
	 * @return Slice JcicZ448 JcicZ448 of List
	 */
	public JcicZ448 ukeyFirst(String ukey_0, TitaVo... titaVo);

	/**
	 * SubmitKey= , AND CustId= , AND ApplyDate= , AND CourtCode= , AND MaxMainCode=
	 *
	 * @param submitKey_0   submitKey_0
	 * @param custId_1      custId_1
	 * @param applyDate_2   applyDate_2
	 * @param courtCode_3   courtCode_3
	 * @param maxMainCode_4 maxMainCode_4
	 * @param titaVo        Variable-Length Argument
	 * @return Slice JcicZ448 JcicZ448 of List
	 */
	public JcicZ448 otherFirst(String submitKey_0, String custId_1, int applyDate_2, String courtCode_3, String maxMainCode_4, TitaVo... titaVo);

	/**
	 * hold By JcicZ448
	 * 
	 * @param jcicZ448Id key
	 * @param titaVo     Variable-Length Argument
	 * @return JcicZ448 JcicZ448
	 */
	public JcicZ448 holdById(JcicZ448Id jcicZ448Id, TitaVo... titaVo);

	/**
	 * hold By JcicZ448
	 * 
	 * @param jcicZ448 key
	 * @param titaVo   Variable-Length Argument
	 * @return JcicZ448 JcicZ448
	 */
	public JcicZ448 holdById(JcicZ448 jcicZ448, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param jcicZ448 Entity
	 * @param titaVo   Variable-Length Argument
	 * @return JcicZ448 Entity
	 * @throws DBException exception
	 */
	public JcicZ448 insert(JcicZ448 jcicZ448, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param jcicZ448 Entity
	 * @param titaVo   Variable-Length Argument
	 * @return JcicZ448 Entity
	 * @throws DBException exception
	 */
	public JcicZ448 update(JcicZ448 jcicZ448, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param jcicZ448 Entity
	 * @param titaVo   Variable-Length Argument
	 * @return JcicZ448 Entity
	 * @throws DBException exception
	 */
	public JcicZ448 update2(JcicZ448 jcicZ448, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param jcicZ448 Entity
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(JcicZ448 jcicZ448, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param jcicZ448 Entity of List
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<JcicZ448> jcicZ448, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param jcicZ448 Entity of List
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<JcicZ448> jcicZ448, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param jcicZ448 Entity of List
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<JcicZ448> jcicZ448, TitaVo... titaVo) throws DBException;

}
