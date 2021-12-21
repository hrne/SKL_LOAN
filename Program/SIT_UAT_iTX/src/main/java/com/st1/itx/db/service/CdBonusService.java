package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CdBonus;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.CdBonusId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CdBonusService {

	/**
	 * findByPrimaryKey
	 *
	 * @param cdBonusId PK
	 * @param titaVo    Variable-Length Argument
	 * @return CdBonus CdBonus
	 */
	public CdBonus findById(CdBonusId cdBonusId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice CdBonus CdBonus of List
	 */
	public Slice<CdBonus> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * WorkMonth= ,AND ConditionCode = ,AND Condition &gt;= ,AND Condition &lt;=
	 *
	 * @param workMonth_0     workMonth_0
	 * @param conditionCode_1 conditionCode_1
	 * @param condition_2     condition_2
	 * @param condition_3     condition_3
	 * @param index           Page Index
	 * @param limit           Page Data Limit
	 * @param titaVo          Variable-Length Argument
	 * @return Slice CdBonus CdBonus of List
	 */
	public Slice<CdBonus> findCondition(int workMonth_0, int conditionCode_1, String condition_2, String condition_3, int index, int limit, TitaVo... titaVo);

	/**
	 * WorkMonth&gt;= ,AND WorkMonth&lt;=
	 *
	 * @param workMonth_0 workMonth_0
	 * @param workMonth_1 workMonth_1
	 * @param index       Page Index
	 * @param limit       Page Data Limit
	 * @param titaVo      Variable-Length Argument
	 * @return Slice CdBonus CdBonus of List
	 */
	public Slice<CdBonus> findYearMonth(int workMonth_0, int workMonth_1, int index, int limit, TitaVo... titaVo);

	/**
	 * WorkMonth&lt;=
	 *
	 * @param workMonth_0 workMonth_0
	 * @param titaVo      Variable-Length Argument
	 * @return Slice CdBonus CdBonus of List
	 */
	public CdBonus findWorkMonthFirst(int workMonth_0, TitaVo... titaVo);

	/**
	 * hold By CdBonus
	 * 
	 * @param cdBonusId key
	 * @param titaVo    Variable-Length Argument
	 * @return CdBonus CdBonus
	 */
	public CdBonus holdById(CdBonusId cdBonusId, TitaVo... titaVo);

	/**
	 * hold By CdBonus
	 * 
	 * @param cdBonus key
	 * @param titaVo  Variable-Length Argument
	 * @return CdBonus CdBonus
	 */
	public CdBonus holdById(CdBonus cdBonus, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param cdBonus Entity
	 * @param titaVo  Variable-Length Argument
	 * @return CdBonus Entity
	 * @throws DBException exception
	 */
	public CdBonus insert(CdBonus cdBonus, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param cdBonus Entity
	 * @param titaVo  Variable-Length Argument
	 * @return CdBonus Entity
	 * @throws DBException exception
	 */
	public CdBonus update(CdBonus cdBonus, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param cdBonus Entity
	 * @param titaVo  Variable-Length Argument
	 * @return CdBonus Entity
	 * @throws DBException exception
	 */
	public CdBonus update2(CdBonus cdBonus, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param cdBonus Entity
	 * @param titaVo  Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(CdBonus cdBonus, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param cdBonus Entity of List
	 * @param titaVo  Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<CdBonus> cdBonus, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param cdBonus Entity of List
	 * @param titaVo  Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<CdBonus> cdBonus, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param cdBonus Entity of List
	 * @param titaVo  Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<CdBonus> cdBonus, TitaVo... titaVo) throws DBException;

}
