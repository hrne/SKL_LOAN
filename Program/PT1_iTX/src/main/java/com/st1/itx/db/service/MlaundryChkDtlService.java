package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.MlaundryChkDtl;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.MlaundryChkDtlId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface MlaundryChkDtlService {

	/**
	 * findByPrimaryKey
	 *
	 * @param mlaundryChkDtlId PK
	 * @param titaVo           Variable-Length Argument
	 * @return MlaundryChkDtl MlaundryChkDtl
	 */
	public MlaundryChkDtl findById(MlaundryChkDtlId mlaundryChkDtlId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice MlaundryChkDtl MlaundryChkDtl of List
	 */
	public Slice<MlaundryChkDtl> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * EntryDate &gt;= ,AND EntryDate &lt;=
	 *
	 * @param entryDate_0 entryDate_0
	 * @param entryDate_1 entryDate_1
	 * @param index       Page Index
	 * @param limit       Page Data Limit
	 * @param titaVo      Variable-Length Argument
	 * @return Slice MlaundryChkDtl MlaundryChkDtl of List
	 */
	public Slice<MlaundryChkDtl> findEntryDateRange(int entryDate_0, int entryDate_1, int index, int limit, TitaVo... titaVo);

	/**
	 * EntryDate &gt;= ,AND EntryDate &lt;= ,AND Factor =
	 *
	 * @param entryDate_0 entryDate_0
	 * @param entryDate_1 entryDate_1
	 * @param factor_2    factor_2
	 * @param index       Page Index
	 * @param limit       Page Data Limit
	 * @param titaVo      Variable-Length Argument
	 * @return Slice MlaundryChkDtl MlaundryChkDtl of List
	 */
	public Slice<MlaundryChkDtl> findFactor(int entryDate_0, int entryDate_1, int factor_2, int index, int limit, TitaVo... titaVo);

	/**
	 * EntryDate &gt;= ,AND EntryDate &lt;= ,AND Factor = , AND CustNo =
	 *
	 * @param entryDate_0 entryDate_0
	 * @param entryDate_1 entryDate_1
	 * @param factor_2    factor_2
	 * @param custNo_3    custNo_3
	 * @param index       Page Index
	 * @param limit       Page Data Limit
	 * @param titaVo      Variable-Length Argument
	 * @return Slice MlaundryChkDtl MlaundryChkDtl of List
	 */
	public Slice<MlaundryChkDtl> findEntryDateRangeFactorCustNo(int entryDate_0, int entryDate_1, int factor_2, int custNo_3, int index, int limit, TitaVo... titaVo);

	/**
	 * hold By MlaundryChkDtl
	 * 
	 * @param mlaundryChkDtlId key
	 * @param titaVo           Variable-Length Argument
	 * @return MlaundryChkDtl MlaundryChkDtl
	 */
	public MlaundryChkDtl holdById(MlaundryChkDtlId mlaundryChkDtlId, TitaVo... titaVo);

	/**
	 * hold By MlaundryChkDtl
	 * 
	 * @param mlaundryChkDtl key
	 * @param titaVo         Variable-Length Argument
	 * @return MlaundryChkDtl MlaundryChkDtl
	 */
	public MlaundryChkDtl holdById(MlaundryChkDtl mlaundryChkDtl, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param mlaundryChkDtl Entity
	 * @param titaVo         Variable-Length Argument
	 * @return MlaundryChkDtl Entity
	 * @throws DBException exception
	 */
	public MlaundryChkDtl insert(MlaundryChkDtl mlaundryChkDtl, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param mlaundryChkDtl Entity
	 * @param titaVo         Variable-Length Argument
	 * @return MlaundryChkDtl Entity
	 * @throws DBException exception
	 */
	public MlaundryChkDtl update(MlaundryChkDtl mlaundryChkDtl, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param mlaundryChkDtl Entity
	 * @param titaVo         Variable-Length Argument
	 * @return MlaundryChkDtl Entity
	 * @throws DBException exception
	 */
	public MlaundryChkDtl update2(MlaundryChkDtl mlaundryChkDtl, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param mlaundryChkDtl Entity
	 * @param titaVo         Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(MlaundryChkDtl mlaundryChkDtl, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param mlaundryChkDtl Entity of List
	 * @param titaVo         Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<MlaundryChkDtl> mlaundryChkDtl, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param mlaundryChkDtl Entity of List
	 * @param titaVo         Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<MlaundryChkDtl> mlaundryChkDtl, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param mlaundryChkDtl Entity of List
	 * @param titaVo         Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<MlaundryChkDtl> mlaundryChkDtl, TitaVo... titaVo) throws DBException;

}
