package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.Guarantor;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.GuarantorId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface GuarantorService {

	/**
	 * findByPrimaryKey
	 *
	 * @param guarantorId PK
	 * @param titaVo      Variable-Length Argument
	 * @return Guarantor Guarantor
	 */
	public Guarantor findById(GuarantorId guarantorId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice Guarantor Guarantor of List
	 */
	public Slice<Guarantor> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * ApproveNo =
	 *
	 * @param approveNo_0 approveNo_0
	 * @param index       Page Index
	 * @param limit       Page Data Limit
	 * @param titaVo      Variable-Length Argument
	 * @return Slice Guarantor Guarantor of List
	 */
	public Slice<Guarantor> approveNoEq(int approveNo_0, int index, int limit, TitaVo... titaVo);

	/**
	 * GuaUKey =
	 *
	 * @param guaUKey_0 guaUKey_0
	 * @param index     Page Index
	 * @param limit     Page Data Limit
	 * @param titaVo    Variable-Length Argument
	 * @return Slice Guarantor Guarantor of List
	 */
	public Slice<Guarantor> guaUKeyEq(String guaUKey_0, int index, int limit, TitaVo... titaVo);

	/**
	 * hold By Guarantor
	 * 
	 * @param guarantorId key
	 * @param titaVo      Variable-Length Argument
	 * @return Guarantor Guarantor
	 */
	public Guarantor holdById(GuarantorId guarantorId, TitaVo... titaVo);

	/**
	 * hold By Guarantor
	 * 
	 * @param guarantor key
	 * @param titaVo    Variable-Length Argument
	 * @return Guarantor Guarantor
	 */
	public Guarantor holdById(Guarantor guarantor, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param guarantor Entity
	 * @param titaVo    Variable-Length Argument
	 * @return Guarantor Entity
	 * @throws DBException exception
	 */
	public Guarantor insert(Guarantor guarantor, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param guarantor Entity
	 * @param titaVo    Variable-Length Argument
	 * @return Guarantor Entity
	 * @throws DBException exception
	 */
	public Guarantor update(Guarantor guarantor, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param guarantor Entity
	 * @param titaVo    Variable-Length Argument
	 * @return Guarantor Entity
	 * @throws DBException exception
	 */
	public Guarantor update2(Guarantor guarantor, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param guarantor Entity
	 * @param titaVo    Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(Guarantor guarantor, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param guarantor Entity of List
	 * @param titaVo    Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<Guarantor> guarantor, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param guarantor Entity of List
	 * @param titaVo    Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<Guarantor> guarantor, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param guarantor Entity of List
	 * @param titaVo    Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<Guarantor> guarantor, TitaVo... titaVo) throws DBException;

}
