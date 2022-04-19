package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.MonthlyLM028;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.MonthlyLM028Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface MonthlyLM028Service {

	/**
	 * findByPrimaryKey
	 *
	 * @param monthlyLM028Id PK
	 * @param titaVo         Variable-Length Argument
	 * @return MonthlyLM028 MonthlyLM028
	 */
	public MonthlyLM028 findById(MonthlyLM028Id monthlyLM028Id, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice MonthlyLM028 MonthlyLM028 of List
	 */
	public Slice<MonthlyLM028> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * DataMonth =
	 *
	 * @param dataMonth_0 dataMonth_0
	 * @param index       Page Index
	 * @param limit       Page Data Limit
	 * @param titaVo      Variable-Length Argument
	 * @return Slice MonthlyLM028 MonthlyLM028 of List
	 */
	public Slice<MonthlyLM028> findByMonth(int dataMonth_0, int index, int limit, TitaVo... titaVo);

	/**
	 * hold By MonthlyLM028
	 * 
	 * @param monthlyLM028Id key
	 * @param titaVo         Variable-Length Argument
	 * @return MonthlyLM028 MonthlyLM028
	 */
	public MonthlyLM028 holdById(MonthlyLM028Id monthlyLM028Id, TitaVo... titaVo);

	/**
	 * hold By MonthlyLM028
	 * 
	 * @param monthlyLM028 key
	 * @param titaVo       Variable-Length Argument
	 * @return MonthlyLM028 MonthlyLM028
	 */
	public MonthlyLM028 holdById(MonthlyLM028 monthlyLM028, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param monthlyLM028 Entity
	 * @param titaVo       Variable-Length Argument
	 * @return MonthlyLM028 Entity
	 * @throws DBException exception
	 */
	public MonthlyLM028 insert(MonthlyLM028 monthlyLM028, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param monthlyLM028 Entity
	 * @param titaVo       Variable-Length Argument
	 * @return MonthlyLM028 Entity
	 * @throws DBException exception
	 */
	public MonthlyLM028 update(MonthlyLM028 monthlyLM028, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param monthlyLM028 Entity
	 * @param titaVo       Variable-Length Argument
	 * @return MonthlyLM028 Entity
	 * @throws DBException exception
	 */
	public MonthlyLM028 update2(MonthlyLM028 monthlyLM028, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param monthlyLM028 Entity
	 * @param titaVo       Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(MonthlyLM028 monthlyLM028, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param monthlyLM028 Entity of List
	 * @param titaVo       Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<MonthlyLM028> monthlyLM028, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param monthlyLM028 Entity of List
	 * @param titaVo       Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<MonthlyLM028> monthlyLM028, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param monthlyLM028 Entity of List
	 * @param titaVo       Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<MonthlyLM028> monthlyLM028, TitaVo... titaVo) throws DBException;

	/**
	 * Stored Procedure<br>
	 * (月底日日終批次)維護MonthlyLM028月報工作檔
	 * 
	 * @param TBSDYF int
	 * @param empNo  String
	 * @param titaVo Variable-Length Argument
	 *
	 */
	public void Usp_L9_MonthlyLM028_Upd(int TBSDYF, String empNo, TitaVo... titaVo);

}
