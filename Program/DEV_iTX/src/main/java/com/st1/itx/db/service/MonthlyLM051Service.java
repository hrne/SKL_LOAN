package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.MonthlyLM051;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.MonthlyLM051Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface MonthlyLM051Service {

	/**
	 * findByPrimaryKey
	 *
	 * @param monthlyLM051Id PK
	 * @param titaVo         Variable-Length Argument
	 * @return MonthlyLM051 MonthlyLM051
	 */
	public MonthlyLM051 findById(MonthlyLM051Id monthlyLM051Id, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice MonthlyLM051 MonthlyLM051 of List
	 */
	public Slice<MonthlyLM051> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * hold By MonthlyLM051
	 * 
	 * @param monthlyLM051Id key
	 * @param titaVo         Variable-Length Argument
	 * @return MonthlyLM051 MonthlyLM051
	 */
	public MonthlyLM051 holdById(MonthlyLM051Id monthlyLM051Id, TitaVo... titaVo);

	/**
	 * hold By MonthlyLM051
	 * 
	 * @param monthlyLM051 key
	 * @param titaVo       Variable-Length Argument
	 * @return MonthlyLM051 MonthlyLM051
	 */
	public MonthlyLM051 holdById(MonthlyLM051 monthlyLM051, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param monthlyLM051 Entity
	 * @param titaVo       Variable-Length Argument
	 * @return MonthlyLM051 Entity
	 * @throws DBException exception
	 */
	public MonthlyLM051 insert(MonthlyLM051 monthlyLM051, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param monthlyLM051 Entity
	 * @param titaVo       Variable-Length Argument
	 * @return MonthlyLM051 Entity
	 * @throws DBException exception
	 */
	public MonthlyLM051 update(MonthlyLM051 monthlyLM051, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param monthlyLM051 Entity
	 * @param titaVo       Variable-Length Argument
	 * @return MonthlyLM051 Entity
	 * @throws DBException exception
	 */
	public MonthlyLM051 update2(MonthlyLM051 monthlyLM051, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param monthlyLM051 Entity
	 * @param titaVo       Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(MonthlyLM051 monthlyLM051, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param monthlyLM051 Entity of List
	 * @param titaVo       Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<MonthlyLM051> monthlyLM051, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param monthlyLM051 Entity of List
	 * @param titaVo       Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<MonthlyLM051> monthlyLM051, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param monthlyLM051 Entity of List
	 * @param titaVo       Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<MonthlyLM051> monthlyLM051, TitaVo... titaVo) throws DBException;

	/**
	 * Stored Procedure<br>
	 * (月底日日終批次)維護MonthlyLM051月報工作檔
	 * 
	 * @param TBSDYF int
	 * @param empNo  String
	 * @param titaVo Variable-Length Argument
	 *
	 */
	public void Usp_L9_MonthlyLM051_Upd(int TBSDYF, String empNo, TitaVo... titaVo);

}
