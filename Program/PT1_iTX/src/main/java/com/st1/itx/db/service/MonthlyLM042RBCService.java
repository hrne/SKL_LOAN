package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.MonthlyLM042RBC;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.MonthlyLM042RBCId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface MonthlyLM042RBCService {

	/**
	 * findByPrimaryKey
	 *
	 * @param monthlyLM042RBCId PK
	 * @param titaVo            Variable-Length Argument
	 * @return MonthlyLM042RBC MonthlyLM042RBC
	 */
	public MonthlyLM042RBC findById(MonthlyLM042RBCId monthlyLM042RBCId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice MonthlyLM042RBC MonthlyLM042RBC of List
	 */
	public Slice<MonthlyLM042RBC> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * YearMonth =
	 *
	 * @param yearMonth_0 yearMonth_0
	 * @param index       Page Index
	 * @param limit       Page Data Limit
	 * @param titaVo      Variable-Length Argument
	 * @return Slice MonthlyLM042RBC MonthlyLM042RBC of List
	 */
	public Slice<MonthlyLM042RBC> findYearMonthAll(int yearMonth_0, int index, int limit, TitaVo... titaVo);

	/**
	 * YearMonth = ,AND LoanType = ,AND LoanItem = ,AND RelatedCode =
	 *
	 * @param yearMonth_0   yearMonth_0
	 * @param loanType_1    loanType_1
	 * @param loanItem_2    loanItem_2
	 * @param relatedCode_3 relatedCode_3
	 * @param index         Page Index
	 * @param limit         Page Data Limit
	 * @param titaVo        Variable-Length Argument
	 * @return Slice MonthlyLM042RBC MonthlyLM042RBC of List
	 */
	public Slice<MonthlyLM042RBC> findItem(int yearMonth_0, String loanType_1, String loanItem_2, String relatedCode_3, int index, int limit, TitaVo... titaVo);

	/**
	 * hold By MonthlyLM042RBC
	 * 
	 * @param monthlyLM042RBCId key
	 * @param titaVo            Variable-Length Argument
	 * @return MonthlyLM042RBC MonthlyLM042RBC
	 */
	public MonthlyLM042RBC holdById(MonthlyLM042RBCId monthlyLM042RBCId, TitaVo... titaVo);

	/**
	 * hold By MonthlyLM042RBC
	 * 
	 * @param monthlyLM042RBC key
	 * @param titaVo          Variable-Length Argument
	 * @return MonthlyLM042RBC MonthlyLM042RBC
	 */
	public MonthlyLM042RBC holdById(MonthlyLM042RBC monthlyLM042RBC, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param monthlyLM042RBC Entity
	 * @param titaVo          Variable-Length Argument
	 * @return MonthlyLM042RBC Entity
	 * @throws DBException exception
	 */
	public MonthlyLM042RBC insert(MonthlyLM042RBC monthlyLM042RBC, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param monthlyLM042RBC Entity
	 * @param titaVo          Variable-Length Argument
	 * @return MonthlyLM042RBC Entity
	 * @throws DBException exception
	 */
	public MonthlyLM042RBC update(MonthlyLM042RBC monthlyLM042RBC, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param monthlyLM042RBC Entity
	 * @param titaVo          Variable-Length Argument
	 * @return MonthlyLM042RBC Entity
	 * @throws DBException exception
	 */
	public MonthlyLM042RBC update2(MonthlyLM042RBC monthlyLM042RBC, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param monthlyLM042RBC Entity
	 * @param titaVo          Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(MonthlyLM042RBC monthlyLM042RBC, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param monthlyLM042RBC Entity of List
	 * @param titaVo          Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<MonthlyLM042RBC> monthlyLM042RBC, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param monthlyLM042RBC Entity of List
	 * @param titaVo          Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<MonthlyLM042RBC> monthlyLM042RBC, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param monthlyLM042RBC Entity of List
	 * @param titaVo          Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<MonthlyLM042RBC> monthlyLM042RBC, TitaVo... titaVo) throws DBException;

	/**
	 * Stored Procedure<br>
	 * 
	 * @param tbsdyf      int
	 * @param empNo       String
	 * @param loanType    String
	 * @param loanItem    String
	 * @param relatedCode String
	 * @param titaVo      Variable-Length Argument
	 *
	 */
	public void Usp_L9_MonthlyLM052AssetClass_Ins(int tbsdyf, String empNo, String loanType, String loanItem, String relatedCode, TitaVo... titaVo);

}
