package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.MonthlyBookValue;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.MonthlyBookValueId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface MonthlyBookValueService {

	/**
	 * findByPrimaryKey
	 *
	 * @param monthlyBookValueId PK
	 * @param titaVo             Variable-Length Argument
	 * @return MonthlyBookValue MonthlyBookValue
	 */
	public MonthlyBookValue findById(MonthlyBookValueId monthlyBookValueId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice MonthlyBookValue MonthlyBookValue of List
	 */
	public Slice<MonthlyBookValue> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * hold By MonthlyBookValue
	 * 
	 * @param monthlyBookValueId key
	 * @param titaVo             Variable-Length Argument
	 * @return MonthlyBookValue MonthlyBookValue
	 */
	public MonthlyBookValue holdById(MonthlyBookValueId monthlyBookValueId, TitaVo... titaVo);

	/**
	 * hold By MonthlyBookValue
	 * 
	 * @param monthlyBookValue key
	 * @param titaVo           Variable-Length Argument
	 * @return MonthlyBookValue MonthlyBookValue
	 */
	public MonthlyBookValue holdById(MonthlyBookValue monthlyBookValue, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param monthlyBookValue Entity
	 * @param titaVo           Variable-Length Argument
	 * @return MonthlyBookValue Entity
	 * @throws DBException exception
	 */
	public MonthlyBookValue insert(MonthlyBookValue monthlyBookValue, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param monthlyBookValue Entity
	 * @param titaVo           Variable-Length Argument
	 * @return MonthlyBookValue Entity
	 * @throws DBException exception
	 */
	public MonthlyBookValue update(MonthlyBookValue monthlyBookValue, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param monthlyBookValue Entity
	 * @param titaVo           Variable-Length Argument
	 * @return MonthlyBookValue Entity
	 * @throws DBException exception
	 */
	public MonthlyBookValue update2(MonthlyBookValue monthlyBookValue, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param monthlyBookValue Entity
	 * @param titaVo           Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(MonthlyBookValue monthlyBookValue, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param monthlyBookValue Entity of List
	 * @param titaVo           Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<MonthlyBookValue> monthlyBookValue, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param monthlyBookValue Entity of List
	 * @param titaVo           Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<MonthlyBookValue> monthlyBookValue, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param monthlyBookValue Entity of List
	 * @param titaVo           Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<MonthlyBookValue> monthlyBookValue, TitaVo... titaVo) throws DBException;

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
