package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.LoanIfrsIp;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.LoanIfrsIpId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface LoanIfrsIpService {

	/**
	 * findByPrimaryKey
	 *
	 * @param loanIfrsIpId PK
	 * @param titaVo       Variable-Length Argument
	 * @return LoanIfrsIp LoanIfrsIp
	 */
	public LoanIfrsIp findById(LoanIfrsIpId loanIfrsIpId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice LoanIfrsIp LoanIfrsIp of List
	 */
	public Slice<LoanIfrsIp> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * hold By LoanIfrsIp
	 * 
	 * @param loanIfrsIpId key
	 * @param titaVo       Variable-Length Argument
	 * @return LoanIfrsIp LoanIfrsIp
	 */
	public LoanIfrsIp holdById(LoanIfrsIpId loanIfrsIpId, TitaVo... titaVo);

	/**
	 * hold By LoanIfrsIp
	 * 
	 * @param loanIfrsIp key
	 * @param titaVo     Variable-Length Argument
	 * @return LoanIfrsIp LoanIfrsIp
	 */
	public LoanIfrsIp holdById(LoanIfrsIp loanIfrsIp, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param loanIfrsIp Entity
	 * @param titaVo     Variable-Length Argument
	 * @return LoanIfrsIp Entity
	 * @throws DBException exception
	 */
	public LoanIfrsIp insert(LoanIfrsIp loanIfrsIp, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param loanIfrsIp Entity
	 * @param titaVo     Variable-Length Argument
	 * @return LoanIfrsIp Entity
	 * @throws DBException exception
	 */
	public LoanIfrsIp update(LoanIfrsIp loanIfrsIp, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param loanIfrsIp Entity
	 * @param titaVo     Variable-Length Argument
	 * @return LoanIfrsIp Entity
	 * @throws DBException exception
	 */
	public LoanIfrsIp update2(LoanIfrsIp loanIfrsIp, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param loanIfrsIp Entity
	 * @param titaVo     Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(LoanIfrsIp loanIfrsIp, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param loanIfrsIp Entity of List
	 * @param titaVo     Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<LoanIfrsIp> loanIfrsIp, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param loanIfrsIp Entity of List
	 * @param titaVo     Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<LoanIfrsIp> loanIfrsIp, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param loanIfrsIp Entity of List
	 * @param titaVo     Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<LoanIfrsIp> loanIfrsIp, TitaVo... titaVo) throws DBException;

	/**
	 * Stored Procedure<br>
	 * (月底日日終批次)維護 LoanIfrsIp IFRS9欄位清單9
	 * 
	 * @param TBSDYF  int
	 * @param EmpNo   String
	 * @param NewAcFg int
	 * @param titaVo  Variable-Length Argument
	 *
	 */
	public void Usp_L7_LoanIfrsIp_Upd(int TBSDYF, String EmpNo, int NewAcFg, TitaVo... titaVo);

}
