package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.MonthlyLM052Ovdu;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.MonthlyLM052OvduId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface MonthlyLM052OvduService {

	/**
	 * findByPrimaryKey
	 *
	 * @param monthlyLM052OvduId PK
	 * @param titaVo             Variable-Length Argument
	 * @return MonthlyLM052Ovdu MonthlyLM052Ovdu
	 */
	public MonthlyLM052Ovdu findById(MonthlyLM052OvduId monthlyLM052OvduId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice MonthlyLM052Ovdu MonthlyLM052Ovdu of List
	 */
	public Slice<MonthlyLM052Ovdu> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * hold By MonthlyLM052Ovdu
	 * 
	 * @param monthlyLM052OvduId key
	 * @param titaVo             Variable-Length Argument
	 * @return MonthlyLM052Ovdu MonthlyLM052Ovdu
	 */
	public MonthlyLM052Ovdu holdById(MonthlyLM052OvduId monthlyLM052OvduId, TitaVo... titaVo);

	/**
	 * hold By MonthlyLM052Ovdu
	 * 
	 * @param monthlyLM052Ovdu key
	 * @param titaVo           Variable-Length Argument
	 * @return MonthlyLM052Ovdu MonthlyLM052Ovdu
	 */
	public MonthlyLM052Ovdu holdById(MonthlyLM052Ovdu monthlyLM052Ovdu, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param monthlyLM052Ovdu Entity
	 * @param titaVo           Variable-Length Argument
	 * @return MonthlyLM052Ovdu Entity
	 * @throws DBException exception
	 */
	public MonthlyLM052Ovdu insert(MonthlyLM052Ovdu monthlyLM052Ovdu, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param monthlyLM052Ovdu Entity
	 * @param titaVo           Variable-Length Argument
	 * @return MonthlyLM052Ovdu Entity
	 * @throws DBException exception
	 */
	public MonthlyLM052Ovdu update(MonthlyLM052Ovdu monthlyLM052Ovdu, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param monthlyLM052Ovdu Entity
	 * @param titaVo           Variable-Length Argument
	 * @return MonthlyLM052Ovdu Entity
	 * @throws DBException exception
	 */
	public MonthlyLM052Ovdu update2(MonthlyLM052Ovdu monthlyLM052Ovdu, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param monthlyLM052Ovdu Entity
	 * @param titaVo           Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(MonthlyLM052Ovdu monthlyLM052Ovdu, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param monthlyLM052Ovdu Entity of List
	 * @param titaVo           Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<MonthlyLM052Ovdu> monthlyLM052Ovdu, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param monthlyLM052Ovdu Entity of List
	 * @param titaVo           Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<MonthlyLM052Ovdu> monthlyLM052Ovdu, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param monthlyLM052Ovdu Entity of List
	 * @param titaVo           Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<MonthlyLM052Ovdu> monthlyLM052Ovdu, TitaVo... titaVo) throws DBException;

	/**
	 * Stored Procedure<br>
	 * 
	 * @param tbsdyf int
	 * @param empNo  String
	 * @param titaVo Variable-Length Argument
	 *
	 */
	public void Usp_L9_MonthlyLM052Ovdu_Ins(int tbsdyf, String empNo, TitaVo... titaVo);

}
