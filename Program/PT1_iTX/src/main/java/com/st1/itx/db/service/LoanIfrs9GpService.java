package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.LoanIfrs9Gp;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.LoanIfrs9GpId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface LoanIfrs9GpService {

	/**
	 * findByPrimaryKey
	 *
	 * @param loanIfrs9GpId PK
	 * @param titaVo        Variable-Length Argument
	 * @return LoanIfrs9Gp LoanIfrs9Gp
	 */
	public LoanIfrs9Gp findById(LoanIfrs9GpId loanIfrs9GpId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice LoanIfrs9Gp LoanIfrs9Gp of List
	 */
	public Slice<LoanIfrs9Gp> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * hold By LoanIfrs9Gp
	 * 
	 * @param loanIfrs9GpId key
	 * @param titaVo        Variable-Length Argument
	 * @return LoanIfrs9Gp LoanIfrs9Gp
	 */
	public LoanIfrs9Gp holdById(LoanIfrs9GpId loanIfrs9GpId, TitaVo... titaVo);

	/**
	 * hold By LoanIfrs9Gp
	 * 
	 * @param loanIfrs9Gp key
	 * @param titaVo      Variable-Length Argument
	 * @return LoanIfrs9Gp LoanIfrs9Gp
	 */
	public LoanIfrs9Gp holdById(LoanIfrs9Gp loanIfrs9Gp, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param loanIfrs9Gp Entity
	 * @param titaVo      Variable-Length Argument
	 * @return LoanIfrs9Gp Entity
	 * @throws DBException exception
	 */
	public LoanIfrs9Gp insert(LoanIfrs9Gp loanIfrs9Gp, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param loanIfrs9Gp Entity
	 * @param titaVo      Variable-Length Argument
	 * @return LoanIfrs9Gp Entity
	 * @throws DBException exception
	 */
	public LoanIfrs9Gp update(LoanIfrs9Gp loanIfrs9Gp, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param loanIfrs9Gp Entity
	 * @param titaVo      Variable-Length Argument
	 * @return LoanIfrs9Gp Entity
	 * @throws DBException exception
	 */
	public LoanIfrs9Gp update2(LoanIfrs9Gp loanIfrs9Gp, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param loanIfrs9Gp Entity
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(LoanIfrs9Gp loanIfrs9Gp, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param loanIfrs9Gp Entity of List
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<LoanIfrs9Gp> loanIfrs9Gp, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param loanIfrs9Gp Entity of List
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<LoanIfrs9Gp> loanIfrs9Gp, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param loanIfrs9Gp Entity of List
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<LoanIfrs9Gp> loanIfrs9Gp, TitaVo... titaVo) throws DBException;

	/**
	 * Stored Procedure<br>
	 * (月底日日終批次)維護 LoanIfrsGp IFRS9資料欄位清單7
	 * 
	 * @param TBSDYF int
	 * @param EmpNo  String
	 * @param titaVo Variable-Length Argument
	 *
	 */
	public void Usp_L7_LoanIfrs9Gp_Upd(int TBSDYF, String EmpNo, TitaVo... titaVo);

}
