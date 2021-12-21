package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CustTelNo;
import org.springframework.data.domain.Slice;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CustTelNoService {

	/**
	 * findByPrimaryKey
	 *
	 * @param telNoUKey PK
	 * @param titaVo    Variable-Length Argument
	 * @return CustTelNo CustTelNo
	 */
	public CustTelNo findById(String telNoUKey, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice CustTelNo CustTelNo of List
	 */
	public Slice<CustTelNo> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * CustUKey =
	 *
	 * @param custUKey_0 custUKey_0
	 * @param index      Page Index
	 * @param limit      Page Data Limit
	 * @param titaVo     Variable-Length Argument
	 * @return Slice CustTelNo CustTelNo of List
	 */
	public Slice<CustTelNo> findCustUKey(String custUKey_0, int index, int limit, TitaVo... titaVo);

	/**
	 * CustUKey = ,AND TelTypeCode =
	 *
	 * @param custUKey_0    custUKey_0
	 * @param telTypeCode_1 telTypeCode_1
	 * @param titaVo        Variable-Length Argument
	 * @return Slice CustTelNo CustTelNo of List
	 */
	public CustTelNo custUKeyFirst(String custUKey_0, String telTypeCode_1, TitaVo... titaVo);

	/**
	 * TelTypeCode = ,AND TelNo =
	 *
	 * @param telTypeCode_0 telTypeCode_0
	 * @param telNo_1       telNo_1
	 * @param index         Page Index
	 * @param limit         Page Data Limit
	 * @param titaVo        Variable-Length Argument
	 * @return Slice CustTelNo CustTelNo of List
	 */
	public Slice<CustTelNo> mobileEq(String telTypeCode_0, String telNo_1, int index, int limit, TitaVo... titaVo);

	/**
	 * hold By CustTelNo
	 * 
	 * @param telNoUKey key
	 * @param titaVo    Variable-Length Argument
	 * @return CustTelNo CustTelNo
	 */
	public CustTelNo holdById(String telNoUKey, TitaVo... titaVo);

	/**
	 * hold By CustTelNo
	 * 
	 * @param custTelNo key
	 * @param titaVo    Variable-Length Argument
	 * @return CustTelNo CustTelNo
	 */
	public CustTelNo holdById(CustTelNo custTelNo, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param custTelNo Entity
	 * @param titaVo    Variable-Length Argument
	 * @return CustTelNo Entity
	 * @throws DBException exception
	 */
	public CustTelNo insert(CustTelNo custTelNo, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param custTelNo Entity
	 * @param titaVo    Variable-Length Argument
	 * @return CustTelNo Entity
	 * @throws DBException exception
	 */
	public CustTelNo update(CustTelNo custTelNo, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param custTelNo Entity
	 * @param titaVo    Variable-Length Argument
	 * @return CustTelNo Entity
	 * @throws DBException exception
	 */
	public CustTelNo update2(CustTelNo custTelNo, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param custTelNo Entity
	 * @param titaVo    Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(CustTelNo custTelNo, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param custTelNo Entity of List
	 * @param titaVo    Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<CustTelNo> custTelNo, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param custTelNo Entity of List
	 * @param titaVo    Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<CustTelNo> custTelNo, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param custTelNo Entity of List
	 * @param titaVo    Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<CustTelNo> custTelNo, TitaVo... titaVo) throws DBException;

}
