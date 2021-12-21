package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.MonthlyLoanBal;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.MonthlyLoanBalId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface MonthlyLoanBalService {

	/**
	 * findByPrimaryKey
	 *
	 * @param monthlyLoanBalId PK
	 * @param titaVo           Variable-Length Argument
	 * @return MonthlyLoanBal MonthlyLoanBal
	 */
	public MonthlyLoanBal findById(MonthlyLoanBalId monthlyLoanBalId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice MonthlyLoanBal MonthlyLoanBal of List
	 */
	public Slice<MonthlyLoanBal> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * hold By MonthlyLoanBal
	 * 
	 * @param monthlyLoanBalId key
	 * @param titaVo           Variable-Length Argument
	 * @return MonthlyLoanBal MonthlyLoanBal
	 */
	public MonthlyLoanBal holdById(MonthlyLoanBalId monthlyLoanBalId, TitaVo... titaVo);

	/**
	 * hold By MonthlyLoanBal
	 * 
	 * @param monthlyLoanBal key
	 * @param titaVo         Variable-Length Argument
	 * @return MonthlyLoanBal MonthlyLoanBal
	 */
	public MonthlyLoanBal holdById(MonthlyLoanBal monthlyLoanBal, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param monthlyLoanBal Entity
	 * @param titaVo         Variable-Length Argument
	 * @return MonthlyLoanBal Entity
	 * @throws DBException exception
	 */
	public MonthlyLoanBal insert(MonthlyLoanBal monthlyLoanBal, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param monthlyLoanBal Entity
	 * @param titaVo         Variable-Length Argument
	 * @return MonthlyLoanBal Entity
	 * @throws DBException exception
	 */
	public MonthlyLoanBal update(MonthlyLoanBal monthlyLoanBal, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param monthlyLoanBal Entity
	 * @param titaVo         Variable-Length Argument
	 * @return MonthlyLoanBal Entity
	 * @throws DBException exception
	 */
	public MonthlyLoanBal update2(MonthlyLoanBal monthlyLoanBal, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param monthlyLoanBal Entity
	 * @param titaVo         Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(MonthlyLoanBal monthlyLoanBal, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param monthlyLoanBal Entity of List
	 * @param titaVo         Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<MonthlyLoanBal> monthlyLoanBal, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param monthlyLoanBal Entity of List
	 * @param titaVo         Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<MonthlyLoanBal> monthlyLoanBal, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param monthlyLoanBal Entity of List
	 * @param titaVo         Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<MonthlyLoanBal> monthlyLoanBal, TitaVo... titaVo) throws DBException;

	/**
	 * Stored Procedure<br>
	 * (月底日日終批次)維護MonthlyLoanBal每月放款餘額檔
	 * 
	 * @param TBSDYF int
	 * @param empNo  String
	 * @param titaVo Variable-Length Argument
	 *
	 */
	public void Usp_L9_MonthlyLoanBal_Upd(int TBSDYF, String empNo, TitaVo... titaVo);

}
