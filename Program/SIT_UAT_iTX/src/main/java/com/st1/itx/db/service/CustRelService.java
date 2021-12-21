package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CustRel;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.CustRelId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CustRelService {

	/**
	 * findByPrimaryKey
	 *
	 * @param custRelId PK
	 * @param titaVo    Variable-Length Argument
	 * @return CustRel CustRel
	 */
	public CustRel findById(CustRelId custRelId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice CustRel CustRel of List
	 */
	public Slice<CustRel> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * CustUKey =
	 *
	 * @param custUKey_0 custUKey_0
	 * @param index      Page Index
	 * @param limit      Page Data Limit
	 * @param titaVo     Variable-Length Argument
	 * @return Slice CustRel CustRel of List
	 */
	public Slice<CustRel> custUKeyEq(String custUKey_0, int index, int limit, TitaVo... titaVo);

	/**
	 * RelUKey =
	 *
	 * @param relUKey_0 relUKey_0
	 * @param index     Page Index
	 * @param limit     Page Data Limit
	 * @param titaVo    Variable-Length Argument
	 * @return Slice CustRel CustRel of List
	 */
	public Slice<CustRel> relUKeyEq(String relUKey_0, int index, int limit, TitaVo... titaVo);

	/**
	 * RelCode %
	 *
	 * @param relCode_0 relCode_0
	 * @param index     Page Index
	 * @param limit     Page Data Limit
	 * @param titaVo    Variable-Length Argument
	 * @return Slice CustRel CustRel of List
	 */
	public Slice<CustRel> RelCodeLike(String relCode_0, int index, int limit, TitaVo... titaVo);

	/**
	 * CustUKey = ,AND Enable =
	 *
	 * @param custUKey_0 custUKey_0
	 * @param enable_1   enable_1
	 * @param index      Page Index
	 * @param limit      Page Data Limit
	 * @param titaVo     Variable-Length Argument
	 * @return Slice CustRel CustRel of List
	 */
	public Slice<CustRel> findRelUKeyEq(String custUKey_0, String enable_1, int index, int limit, TitaVo... titaVo);

	/**
	 * hold By CustRel
	 * 
	 * @param custRelId key
	 * @param titaVo    Variable-Length Argument
	 * @return CustRel CustRel
	 */
	public CustRel holdById(CustRelId custRelId, TitaVo... titaVo);

	/**
	 * hold By CustRel
	 * 
	 * @param custRel key
	 * @param titaVo  Variable-Length Argument
	 * @return CustRel CustRel
	 */
	public CustRel holdById(CustRel custRel, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param custRel Entity
	 * @param titaVo  Variable-Length Argument
	 * @return CustRel Entity
	 * @throws DBException exception
	 */
	public CustRel insert(CustRel custRel, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param custRel Entity
	 * @param titaVo  Variable-Length Argument
	 * @return CustRel Entity
	 * @throws DBException exception
	 */
	public CustRel update(CustRel custRel, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param custRel Entity
	 * @param titaVo  Variable-Length Argument
	 * @return CustRel Entity
	 * @throws DBException exception
	 */
	public CustRel update2(CustRel custRel, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param custRel Entity
	 * @param titaVo  Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(CustRel custRel, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param custRel Entity of List
	 * @param titaVo  Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<CustRel> custRel, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param custRel Entity of List
	 * @param titaVo  Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<CustRel> custRel, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param custRel Entity of List
	 * @param titaVo  Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<CustRel> custRel, TitaVo... titaVo) throws DBException;

}
