package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.FacProdBreach;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.FacProdBreachId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface FacProdBreachService {

	/**
	 * findByPrimaryKey
	 *
	 * @param facProdBreachId PK
	 * @param titaVo          Variable-Length Argument
	 * @return FacProdBreach FacProdBreach
	 */
	public FacProdBreach findById(FacProdBreachId facProdBreachId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice FacProdBreach FacProdBreach of List
	 */
	public Slice<FacProdBreach> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * BreachNo = ,AND BreachCode &gt;= ,AND BreachCode &lt;=
	 *
	 * @param breachNo_0   breachNo_0
	 * @param breachCode_1 breachCode_1
	 * @param breachCode_2 breachCode_2
	 * @param index        Page Index
	 * @param limit        Page Data Limit
	 * @param titaVo       Variable-Length Argument
	 * @return Slice FacProdBreach FacProdBreach of List
	 */
	public Slice<FacProdBreach> breachNoEq(String breachNo_0, String breachCode_1, String breachCode_2, int index, int limit, TitaVo... titaVo);

	/**
	 * BreachNo = ,AND BreachCode = ,AND MonthStart &lt;= ,AND MonthEnd &gt;
	 *
	 * @param breachNo_0   breachNo_0
	 * @param breachCode_1 breachCode_1
	 * @param monthStart_2 monthStart_2
	 * @param monthEnd_3   monthEnd_3
	 * @param titaVo       Variable-Length Argument
	 * @return Slice FacProdBreach FacProdBreach of List
	 */
	public FacProdBreach breachMonthFirst(String breachNo_0, String breachCode_1, int monthStart_2, int monthEnd_3, TitaVo... titaVo);

	/**
	 * hold By FacProdBreach
	 * 
	 * @param facProdBreachId key
	 * @param titaVo          Variable-Length Argument
	 * @return FacProdBreach FacProdBreach
	 */
	public FacProdBreach holdById(FacProdBreachId facProdBreachId, TitaVo... titaVo);

	/**
	 * hold By FacProdBreach
	 * 
	 * @param facProdBreach key
	 * @param titaVo        Variable-Length Argument
	 * @return FacProdBreach FacProdBreach
	 */
	public FacProdBreach holdById(FacProdBreach facProdBreach, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param facProdBreach Entity
	 * @param titaVo        Variable-Length Argument
	 * @return FacProdBreach Entity
	 * @throws DBException exception
	 */
	public FacProdBreach insert(FacProdBreach facProdBreach, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param facProdBreach Entity
	 * @param titaVo        Variable-Length Argument
	 * @return FacProdBreach Entity
	 * @throws DBException exception
	 */
	public FacProdBreach update(FacProdBreach facProdBreach, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param facProdBreach Entity
	 * @param titaVo        Variable-Length Argument
	 * @return FacProdBreach Entity
	 * @throws DBException exception
	 */
	public FacProdBreach update2(FacProdBreach facProdBreach, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param facProdBreach Entity
	 * @param titaVo        Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(FacProdBreach facProdBreach, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param facProdBreach Entity of List
	 * @param titaVo        Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<FacProdBreach> facProdBreach, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param facProdBreach Entity of List
	 * @param titaVo        Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<FacProdBreach> facProdBreach, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param facProdBreach Entity of List
	 * @param titaVo        Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<FacProdBreach> facProdBreach, TitaVo... titaVo) throws DBException;

}
