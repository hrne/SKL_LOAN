package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.LoanIfrs9Ip;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.LoanIfrs9IpId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface LoanIfrs9IpService {

	/**
	 * findByPrimaryKey
	 *
	 * @param loanIfrs9IpId PK
	 * @param titaVo        Variable-Length Argument
	 * @return LoanIfrs9Ip LoanIfrs9Ip
	 */
	public LoanIfrs9Ip findById(LoanIfrs9IpId loanIfrs9IpId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice LoanIfrs9Ip LoanIfrs9Ip of List
	 */
	public Slice<LoanIfrs9Ip> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * hold By LoanIfrs9Ip
	 * 
	 * @param loanIfrs9IpId key
	 * @param titaVo        Variable-Length Argument
	 * @return LoanIfrs9Ip LoanIfrs9Ip
	 */
	public LoanIfrs9Ip holdById(LoanIfrs9IpId loanIfrs9IpId, TitaVo... titaVo);

	/**
	 * hold By LoanIfrs9Ip
	 * 
	 * @param loanIfrs9Ip key
	 * @param titaVo      Variable-Length Argument
	 * @return LoanIfrs9Ip LoanIfrs9Ip
	 */
	public LoanIfrs9Ip holdById(LoanIfrs9Ip loanIfrs9Ip, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param loanIfrs9Ip Entity
	 * @param titaVo      Variable-Length Argument
	 * @return LoanIfrs9Ip Entity
	 * @throws DBException exception
	 */
	public LoanIfrs9Ip insert(LoanIfrs9Ip loanIfrs9Ip, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param loanIfrs9Ip Entity
	 * @param titaVo      Variable-Length Argument
	 * @return LoanIfrs9Ip Entity
	 * @throws DBException exception
	 */
	public LoanIfrs9Ip update(LoanIfrs9Ip loanIfrs9Ip, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param loanIfrs9Ip Entity
	 * @param titaVo      Variable-Length Argument
	 * @return LoanIfrs9Ip Entity
	 * @throws DBException exception
	 */
	public LoanIfrs9Ip update2(LoanIfrs9Ip loanIfrs9Ip, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param loanIfrs9Ip Entity
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(LoanIfrs9Ip loanIfrs9Ip, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param loanIfrs9Ip Entity of List
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<LoanIfrs9Ip> loanIfrs9Ip, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param loanIfrs9Ip Entity of List
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<LoanIfrs9Ip> loanIfrs9Ip, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param loanIfrs9Ip Entity of List
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<LoanIfrs9Ip> loanIfrs9Ip, TitaVo... titaVo) throws DBException;

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
	public void Usp_L7_LoanIfrs9Ip_Upd(int TBSDYF, String EmpNo, int NewAcFg, TitaVo... titaVo);

}
