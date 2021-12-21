package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.LoanIfrsBp;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.LoanIfrsBpId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface LoanIfrsBpService {

	/**
	 * findByPrimaryKey
	 *
	 * @param loanIfrsBpId PK
	 * @param titaVo       Variable-Length Argument
	 * @return LoanIfrsBp LoanIfrsBp
	 */
	public LoanIfrsBp findById(LoanIfrsBpId loanIfrsBpId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice LoanIfrsBp LoanIfrsBp of List
	 */
	public Slice<LoanIfrsBp> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * hold By LoanIfrsBp
	 * 
	 * @param loanIfrsBpId key
	 * @param titaVo       Variable-Length Argument
	 * @return LoanIfrsBp LoanIfrsBp
	 */
	public LoanIfrsBp holdById(LoanIfrsBpId loanIfrsBpId, TitaVo... titaVo);

	/**
	 * hold By LoanIfrsBp
	 * 
	 * @param loanIfrsBp key
	 * @param titaVo     Variable-Length Argument
	 * @return LoanIfrsBp LoanIfrsBp
	 */
	public LoanIfrsBp holdById(LoanIfrsBp loanIfrsBp, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param loanIfrsBp Entity
	 * @param titaVo     Variable-Length Argument
	 * @return LoanIfrsBp Entity
	 * @throws DBException exception
	 */
	public LoanIfrsBp insert(LoanIfrsBp loanIfrsBp, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param loanIfrsBp Entity
	 * @param titaVo     Variable-Length Argument
	 * @return LoanIfrsBp Entity
	 * @throws DBException exception
	 */
	public LoanIfrsBp update(LoanIfrsBp loanIfrsBp, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param loanIfrsBp Entity
	 * @param titaVo     Variable-Length Argument
	 * @return LoanIfrsBp Entity
	 * @throws DBException exception
	 */
	public LoanIfrsBp update2(LoanIfrsBp loanIfrsBp, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param loanIfrsBp Entity
	 * @param titaVo     Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(LoanIfrsBp loanIfrsBp, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param loanIfrsBp Entity of List
	 * @param titaVo     Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<LoanIfrsBp> loanIfrsBp, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param loanIfrsBp Entity of List
	 * @param titaVo     Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<LoanIfrsBp> loanIfrsBp, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param loanIfrsBp Entity of List
	 * @param titaVo     Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<LoanIfrsBp> loanIfrsBp, TitaVo... titaVo) throws DBException;

	/**
	 * Stored Procedure<br>
	 * (月底日日終批次)維護 LoanIfrsBp IFRS9資料欄位清單2
	 * 
	 * @param TBSDYF int
	 * @param EmpNo  String
	 * @param titaVo Variable-Length Argument
	 *
	 */
	public void Usp_L7_LoanIfrsBp_Upd(int TBSDYF, String EmpNo, TitaVo... titaVo);

}
