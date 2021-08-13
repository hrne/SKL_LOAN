package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.MonthlyLM003;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.MonthlyLM003Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface MonthlyLM003Service {

	/**
	 * findByPrimaryKey
	 *
	 * @param monthlyLM003Id PK
	 * @param titaVo         Variable-Length Argument
	 * @return MonthlyLM003 MonthlyLM003
	 */
	public MonthlyLM003 findById(MonthlyLM003Id monthlyLM003Id, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice MonthlyLM003 MonthlyLM003 of List
	 */
	public Slice<MonthlyLM003> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * hold By MonthlyLM003
	 * 
	 * @param monthlyLM003Id key
	 * @param titaVo         Variable-Length Argument
	 * @return MonthlyLM003 MonthlyLM003
	 */
	public MonthlyLM003 holdById(MonthlyLM003Id monthlyLM003Id, TitaVo... titaVo);

	/**
	 * hold By MonthlyLM003
	 * 
	 * @param monthlyLM003 key
	 * @param titaVo       Variable-Length Argument
	 * @return MonthlyLM003 MonthlyLM003
	 */
	public MonthlyLM003 holdById(MonthlyLM003 monthlyLM003, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param monthlyLM003 Entity
	 * @param titaVo       Variable-Length Argument
	 * @return MonthlyLM003 Entity
	 * @throws DBException exception
	 */
	public MonthlyLM003 insert(MonthlyLM003 monthlyLM003, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param monthlyLM003 Entity
	 * @param titaVo       Variable-Length Argument
	 * @return MonthlyLM003 Entity
	 * @throws DBException exception
	 */
	public MonthlyLM003 update(MonthlyLM003 monthlyLM003, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param monthlyLM003 Entity
	 * @param titaVo       Variable-Length Argument
	 * @return MonthlyLM003 Entity
	 * @throws DBException exception
	 */
	public MonthlyLM003 update2(MonthlyLM003 monthlyLM003, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param monthlyLM003 Entity
	 * @param titaVo       Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(MonthlyLM003 monthlyLM003, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param monthlyLM003 Entity of List
	 * @param titaVo       Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<MonthlyLM003> monthlyLM003, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param monthlyLM003 Entity of List
	 * @param titaVo       Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<MonthlyLM003> monthlyLM003, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param monthlyLM003 Entity of List
	 * @param titaVo       Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<MonthlyLM003> monthlyLM003, TitaVo... titaVo) throws DBException;

	/**
	 * Stored Procedure<br>
	 * (月底日日終批次)維護MonthlyLM003月報工作檔
	 * 
	 * @param TBSDYF int
	 * @param empNo  String
	 * @param titaVo Variable-Length Argument
	 *
	 */
	public void Usp_L9_MonthlyLM003_Upd(int TBSDYF, String empNo, TitaVo... titaVo);

}
