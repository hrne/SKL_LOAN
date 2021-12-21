package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.LoanIfrs9Jp;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.LoanIfrs9JpId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface LoanIfrs9JpService {

	/**
	 * findByPrimaryKey
	 *
	 * @param loanIfrs9JpId PK
	 * @param titaVo        Variable-Length Argument
	 * @return LoanIfrs9Jp LoanIfrs9Jp
	 */
	public LoanIfrs9Jp findById(LoanIfrs9JpId loanIfrs9JpId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice LoanIfrs9Jp LoanIfrs9Jp of List
	 */
	public Slice<LoanIfrs9Jp> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * hold By LoanIfrs9Jp
	 * 
	 * @param loanIfrs9JpId key
	 * @param titaVo        Variable-Length Argument
	 * @return LoanIfrs9Jp LoanIfrs9Jp
	 */
	public LoanIfrs9Jp holdById(LoanIfrs9JpId loanIfrs9JpId, TitaVo... titaVo);

	/**
	 * hold By LoanIfrs9Jp
	 * 
	 * @param loanIfrs9Jp key
	 * @param titaVo      Variable-Length Argument
	 * @return LoanIfrs9Jp LoanIfrs9Jp
	 */
	public LoanIfrs9Jp holdById(LoanIfrs9Jp loanIfrs9Jp, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param loanIfrs9Jp Entity
	 * @param titaVo      Variable-Length Argument
	 * @return LoanIfrs9Jp Entity
	 * @throws DBException exception
	 */
	public LoanIfrs9Jp insert(LoanIfrs9Jp loanIfrs9Jp, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param loanIfrs9Jp Entity
	 * @param titaVo      Variable-Length Argument
	 * @return LoanIfrs9Jp Entity
	 * @throws DBException exception
	 */
	public LoanIfrs9Jp update(LoanIfrs9Jp loanIfrs9Jp, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param loanIfrs9Jp Entity
	 * @param titaVo      Variable-Length Argument
	 * @return LoanIfrs9Jp Entity
	 * @throws DBException exception
	 */
	public LoanIfrs9Jp update2(LoanIfrs9Jp loanIfrs9Jp, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param loanIfrs9Jp Entity
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(LoanIfrs9Jp loanIfrs9Jp, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param loanIfrs9Jp Entity of List
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<LoanIfrs9Jp> loanIfrs9Jp, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param loanIfrs9Jp Entity of List
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<LoanIfrs9Jp> loanIfrs9Jp, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param loanIfrs9Jp Entity of List
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<LoanIfrs9Jp> loanIfrs9Jp, TitaVo... titaVo) throws DBException;

	/**
	 * Stored Procedure<br>
	 * (月底日日終批次)維護 LoanIfrsJp IFRS9欄位清單10
	 * 
	 * @param TBSDYF int
	 * @param EmpNo  String
	 * @param titaVo Variable-Length Argument
	 *
	 */
	public void Usp_L7_LoanIfrs9Jp_Upd(int TBSDYF, String EmpNo, TitaVo... titaVo);

}
