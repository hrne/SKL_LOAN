package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.LoanIfrsFp;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.LoanIfrsFpId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface LoanIfrsFpService {

	/**
	 * findByPrimaryKey
	 *
	 * @param loanIfrsFpId PK
	 * @param titaVo       Variable-Length Argument
	 * @return LoanIfrsFp LoanIfrsFp
	 */
	public LoanIfrsFp findById(LoanIfrsFpId loanIfrsFpId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice LoanIfrsFp LoanIfrsFp of List
	 */
	public Slice<LoanIfrsFp> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * hold By LoanIfrsFp
	 * 
	 * @param loanIfrsFpId key
	 * @param titaVo       Variable-Length Argument
	 * @return LoanIfrsFp LoanIfrsFp
	 */
	public LoanIfrsFp holdById(LoanIfrsFpId loanIfrsFpId, TitaVo... titaVo);

	/**
	 * hold By LoanIfrsFp
	 * 
	 * @param loanIfrsFp key
	 * @param titaVo     Variable-Length Argument
	 * @return LoanIfrsFp LoanIfrsFp
	 */
	public LoanIfrsFp holdById(LoanIfrsFp loanIfrsFp, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param loanIfrsFp Entity
	 * @param titaVo     Variable-Length Argument
	 * @return LoanIfrsFp Entity
	 * @throws DBException exception
	 */
	public LoanIfrsFp insert(LoanIfrsFp loanIfrsFp, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param loanIfrsFp Entity
	 * @param titaVo     Variable-Length Argument
	 * @return LoanIfrsFp Entity
	 * @throws DBException exception
	 */
	public LoanIfrsFp update(LoanIfrsFp loanIfrsFp, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param loanIfrsFp Entity
	 * @param titaVo     Variable-Length Argument
	 * @return LoanIfrsFp Entity
	 * @throws DBException exception
	 */
	public LoanIfrsFp update2(LoanIfrsFp loanIfrsFp, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param loanIfrsFp Entity
	 * @param titaVo     Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(LoanIfrsFp loanIfrsFp, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param loanIfrsFp Entity of List
	 * @param titaVo     Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<LoanIfrsFp> loanIfrsFp, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param loanIfrsFp Entity of List
	 * @param titaVo     Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<LoanIfrsFp> loanIfrsFp, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param loanIfrsFp Entity of List
	 * @param titaVo     Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<LoanIfrsFp> loanIfrsFp, TitaVo... titaVo) throws DBException;

	/**
	 * Stored Procedure<br>
	 * (月底日日終批次)維護 LoanIfrsFp IFRS9資料欄位清單6
	 * 
	 * @param TBSDYF int
	 * @param EmpNo  String
	 * @param titaVo Variable-Length Argument
	 *
	 */
	public void Usp_L7_LoanIfrsFp_Upd(int TBSDYF, String EmpNo, TitaVo... titaVo);

}
