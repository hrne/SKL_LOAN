package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicMonthlyLoanData;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicMonthlyLoanDataId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicMonthlyLoanDataService {

	/**
	 * findByPrimaryKey
	 *
	 * @param jcicMonthlyLoanDataId PK
	 * @param titaVo                Variable-Length Argument
	 * @return JcicMonthlyLoanData JcicMonthlyLoanData
	 */
	public JcicMonthlyLoanData findById(JcicMonthlyLoanDataId jcicMonthlyLoanDataId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice JcicMonthlyLoanData JcicMonthlyLoanData of List
	 */
	public Slice<JcicMonthlyLoanData> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * hold By JcicMonthlyLoanData
	 * 
	 * @param jcicMonthlyLoanDataId key
	 * @param titaVo                Variable-Length Argument
	 * @return JcicMonthlyLoanData JcicMonthlyLoanData
	 */
	public JcicMonthlyLoanData holdById(JcicMonthlyLoanDataId jcicMonthlyLoanDataId, TitaVo... titaVo);

	/**
	 * hold By JcicMonthlyLoanData
	 * 
	 * @param jcicMonthlyLoanData key
	 * @param titaVo              Variable-Length Argument
	 * @return JcicMonthlyLoanData JcicMonthlyLoanData
	 */
	public JcicMonthlyLoanData holdById(JcicMonthlyLoanData jcicMonthlyLoanData, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param jcicMonthlyLoanData Entity
	 * @param titaVo              Variable-Length Argument
	 * @return JcicMonthlyLoanData Entity
	 * @throws DBException exception
	 */
	public JcicMonthlyLoanData insert(JcicMonthlyLoanData jcicMonthlyLoanData, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param jcicMonthlyLoanData Entity
	 * @param titaVo              Variable-Length Argument
	 * @return JcicMonthlyLoanData Entity
	 * @throws DBException exception
	 */
	public JcicMonthlyLoanData update(JcicMonthlyLoanData jcicMonthlyLoanData, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param jcicMonthlyLoanData Entity
	 * @param titaVo              Variable-Length Argument
	 * @return JcicMonthlyLoanData Entity
	 * @throws DBException exception
	 */
	public JcicMonthlyLoanData update2(JcicMonthlyLoanData jcicMonthlyLoanData, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param jcicMonthlyLoanData Entity
	 * @param titaVo              Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(JcicMonthlyLoanData jcicMonthlyLoanData, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param jcicMonthlyLoanData Entity of List
	 * @param titaVo              Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<JcicMonthlyLoanData> jcicMonthlyLoanData, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param jcicMonthlyLoanData Entity of List
	 * @param titaVo              Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<JcicMonthlyLoanData> jcicMonthlyLoanData, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param jcicMonthlyLoanData Entity of List
	 * @param titaVo              Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<JcicMonthlyLoanData> jcicMonthlyLoanData, TitaVo... titaVo) throws DBException;

	/**
	 * Stored Procedure<br>
	 * (月底日日終批次)維護 JcicMonthlyLoanData 聯徵放款月報資料檔
	 * 
	 * @param TBSDYF int
	 * @param EmpNo  String
	 * @param titaVo Variable-Length Argument
	 *
	 */
	public void Usp_L8_JcicMonthlyLoanData_Upd(int TBSDYF, String EmpNo, TitaVo... titaVo);

}
