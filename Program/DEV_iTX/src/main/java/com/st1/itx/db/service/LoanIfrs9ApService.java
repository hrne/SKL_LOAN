package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.LoanIfrs9Ap;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.LoanIfrs9ApId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface LoanIfrs9ApService {

	/**
	 * findByPrimaryKey
	 *
	 * @param loanIfrs9ApId PK
	 * @param titaVo        Variable-Length Argument
	 * @return LoanIfrs9Ap LoanIfrs9Ap
	 */
	public LoanIfrs9Ap findById(LoanIfrs9ApId loanIfrs9ApId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice LoanIfrs9Ap LoanIfrs9Ap of List
	 */
	public Slice<LoanIfrs9Ap> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * hold By LoanIfrs9Ap
	 * 
	 * @param loanIfrs9ApId key
	 * @param titaVo        Variable-Length Argument
	 * @return LoanIfrs9Ap LoanIfrs9Ap
	 */
	public LoanIfrs9Ap holdById(LoanIfrs9ApId loanIfrs9ApId, TitaVo... titaVo);

	/**
	 * hold By LoanIfrs9Ap
	 * 
	 * @param loanIfrs9Ap key
	 * @param titaVo      Variable-Length Argument
	 * @return LoanIfrs9Ap LoanIfrs9Ap
	 */
	public LoanIfrs9Ap holdById(LoanIfrs9Ap loanIfrs9Ap, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param loanIfrs9Ap Entity
	 * @param titaVo      Variable-Length Argument
	 * @return LoanIfrs9Ap Entity
	 * @throws DBException exception
	 */
	public LoanIfrs9Ap insert(LoanIfrs9Ap loanIfrs9Ap, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param loanIfrs9Ap Entity
	 * @param titaVo      Variable-Length Argument
	 * @return LoanIfrs9Ap Entity
	 * @throws DBException exception
	 */
	public LoanIfrs9Ap update(LoanIfrs9Ap loanIfrs9Ap, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param loanIfrs9Ap Entity
	 * @param titaVo      Variable-Length Argument
	 * @return LoanIfrs9Ap Entity
	 * @throws DBException exception
	 */
	public LoanIfrs9Ap update2(LoanIfrs9Ap loanIfrs9Ap, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param loanIfrs9Ap Entity
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(LoanIfrs9Ap loanIfrs9Ap, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param loanIfrs9Ap Entity of List
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<LoanIfrs9Ap> loanIfrs9Ap, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param loanIfrs9Ap Entity of List
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<LoanIfrs9Ap> loanIfrs9Ap, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param loanIfrs9Ap Entity of List
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<LoanIfrs9Ap> loanIfrs9Ap, TitaVo... titaVo) throws DBException;

	/**
	 * Stored Procedure<br>
	 * (月底日日終批次)維護 LoanIfrsAp IFRS9欄位清單1
	 * 
	 * @param TBSDYF  int
	 * @param EmpNo   String
	 * @param NewAcFg int
	 * @param titaVo  Variable-Length Argument
	 *
	 */
	public void Usp_L7_LoanIfrs9Ap_Upd(int TBSDYF, String EmpNo, int NewAcFg, TitaVo... titaVo);

}
